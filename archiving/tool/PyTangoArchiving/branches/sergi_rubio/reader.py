""" 
    USAGE:
    from PyTangoArchiving import hdb
    reader = hdb.Reader('hdb','user:passwd@host') #Initializes an HdbAPI with the values just required for reading
    reader = hdb.Reader() #In this case initializes an API with no db, using the HdbExtractor instances
    reader.read_attribute(attr,start_date,stop_date) #Which extractor/method to use must be hidden from user
    
    3 : import PyTangoArchiving.hdb
    4 : rd = PyTangoArchiving.hdb.Reader()
    5 : values = rd.get_attr_values('Straights/VC02/MKS-01/P1','2009-07-24','2009-07-29 16:00')

"""

import traceback,time
from random import randrange
from collections import defaultdict
import PyTango
import PyTango_utils as fandango
from fandango.objects import Object
from fandango.log import Logger
from fandango.servers import ServersDict
from fandango.db import FriendlyDB

isNaN = lambda f: 'nan' in str(f).lower()

def get_jumps(values):
    jumps = [(values[i][0],values[i+1][0]) for i in range(len(values)-1) if 120<(values[i+1][0]-values[i][0])]
    return [[time.ctime(d) for d in j] for j in jumps]

def get_failed(values):
    i,failed = 0,[]
    while i<len(values)-1:
        if not isNaN(values[i][1]) and isNaN(values[i+1][1]):
            print 'found error at %s' % time.ctime(values[i+1][0])
            try:
                next = (j for j in range(i+1,len(values)) if not isNaN(values[j][1])).next()
                failed.append((values[i][0],values[i+1][0],values[next][0]))
                i=next
            except StopIteration: #Unable to find the next valid value
                print 'no more values found afterwards ...'
                failed.append((values[i][0],values[i+1][0],-1))
                break
        i+=1
    return [[time.ctime(d) for d in j] for j in failed]
        
       
class Reader(object):
    """ Lightweight API for read-only archiving applications """
    
    __singletone__ = None
    def __init__(self,db='',config='',servers = None, schema = 'hdb',timeout=300000):
        '''@param config must be an string like user@host:passwd'''
        self.log = Logger('hdb.Reader',format='%(levelname)-8s %(asctime)s %(name)s: %(message)s')
        self.log.debug('In PyTangoArchiving.Reader.__init__()')
        self.db_name = db    
        self.schema = schema
        self.last_dates = defaultdict(lambda:(1e10,0))
        self.servers = servers or ServersDict('%sextractor'%schema)
        self.extractors = self.servers.get_class_devices(['TdbExtractor','HdbExtractor'][schema=='hdb'])
        self.timeout = timeout
        if self.db_name:
            if config:
                self.user,self.host = '@' in config and config.split('@',1) or (config,os.environ['HOST'])
                self.user,self.passwd = ':' in self.user and self.user.split(':',1) or (self.user,'')            
            else: self.user,self.host,self.passwd = '','',''
            self.db = FriendlyDB(self.db_name,self.host,self.user,self.passwd)
        else: self.db = None
        
    def __del__(self):
        if self.db: del self.db
        
    @classmethod
    def singletone(cls,*p,**k):
        if not cls.__singletone__: 
            cls.__singletone__ = cls(*p,**k)
        return cls.__singletone__
        
    #################################################################################################
    #################################################################################################
                
    def get_extractor(self):
        extractor = None
        remaining = self.extractors[:]
        while remaining: #for i in range(len(self.extractors)):
            next = randrange(len(remaining))
            devname = remaining.pop(next)
            extractor = self.servers.proxies[devname]
            try:
                extractor.ping()
                extractor.set_timeout_millis(self.timeout)
                break
            except Exception,e: print traceback.format_exc()
        return extractor    
        
    def get_attributes(self):
        return self.__extractorCommand(self.get_extractor(),'GetCurrentArchivedAtt')
    
    def is_attribute_archived(self,attribute):
        return (attribute.lower() in [a.lower() for a in self.get_attributes()])
          
    def get_attribute_values(self,attribute,start_date,stop_date=None,asHistoryBuffer=False):
        ''' def attr_getValues(self,attribute,htype,start_date,stop_date=None):
        This method uses an H/TdbExtractor DeviceServer to get the values from the database.
        The format of values returned is [(epoch,value),]
        The flag 'asHistoryBuffer' forces to return the rawHistBuffer returned by the DS.
        '''
        self.log.debug('In PyTangoArchiving.Reader.get_attribute_values')
        tformat = '%Y-%m-%d %H:%M:%S'
        str2epoch = lambda s: time.mktime(time.strptime(s,tformat))
        epoch2str = lambda f: time.strftime(tformat,time.localtime(f))
        ctime2time = lambda t: (float(t.tv_sec)+1e-6*float(t.tv_usec))        
        
        if not stop_date: stop_date=time.time()
        if type(start_date) is not str: start_date = epoch2str(start_date)
        if type(stop_date) is not str: stop_date = epoch2str(stop_date)
        self.last_dates[attribute] = str2epoch(start_date),str2epoch(stop_date)
     
        if not self.db:
            try: 
                extractor = self.get_extractor()
                #self.clean_extractor(extractor)
                result = self.__extractorCommand(extractor,'GetAttDataBetweenDates',[attribute,start_date,stop_date])
                vattr,vsize=result[1][0],result[0][0]
                time.sleep(0.2)
                if vattr not in [a.name for a in extractor.attribute_list_query()]:
                    raise Exception,'%s_NotIn%sAttributeList'%(vattr,extractor.name())
                print '\treading last value of attribute %s'%vattr
                last_value = extractor.read_attribute(vattr).value
                print '\treading %s attribute history values of %s (last_value = %s)'% (vsize,vattr,last_value)
                history=extractor.attribute_history(vattr,vsize)
                self.clean_extractor(extractor,vattr)
            except Exception,e: 
                print traceback.format_exc()
                raise Exception,'Archiving.Reader_ExtractorFailed(%s)!:%s' % (extractor.name(),str(e))
            if int(PyTango.__version__.split('.')[0])>=7:
                values = asHistoryBuffer and history or [(ctime2time(h.time),h.value) for h in history]
                self.last_reads = history and (ctime2time(history[0].time),ctime2time(history[-1].time)) or (1e10,1e10)
            else:
                values = asHistoryBuffer and history or [(ctime2time(h.value.time),h.value.value) for h in history]
                self.last_reads = history and (ctime2time(history[0].value.time),ctime2time(history[-1].value.time)) or (1e10,1e10)                
        else:
            ID = self.db.Query("select ID from adt where full_name like '%s';" % attribute)[0][0]
            query = "select * from att_%05d where time between '%s' and '%s'"%(ID,start_date,stop_date)
            print '... the query is ... ', query
            values = self.db.Query(query)
            history = []
            while values:
                v = values.pop(0)
                if len(v)<4: history.append((v[0],v[1]))
                else: history.append((v[0],v[2]))
            mysql2time = lambda h: (time.mktime(h[0].timetuple()),h[1])
            values = asHistoryBuffer and history or [(time.mktime(h[0].timetuple()),h[1]) for h in history]
            self.last_reads = history and (mysql2time(history[0]),mysql2time(history[-1])) or (1e10,1e10)
        return values
                    
    #################################################################################################
    #################################################################################################
        
    def clean_extractor(self,extractor,vattr=None):
        ''' removing dynamic attributes from extractor devices ...'''
        #self.log.debug('In PyTangoArchiving.Reader.__cleanExtractor(): removing dynamic attributes')
        print 'In PyTangoArchiving.Reader.__cleanExtractor(): removing dynamic attributes'
        if isinstance(extractor,PyTango.DeviceProxy): 
            name,proxy=extractor.dev_name(),extractor
        else: 
            name,proxy=extractor,self.servers.proxies[extractor]
        if vattr: proxy.RemoveDynamicAttribute(vattr)
        else: proxy.RemoveDynamicAttributes()
        
    def __initMySQLconnection(self):
        try: self.db = MySQLdb.connect(db=self.db_name,host=self.host,user=self.user,passwd=self.passwd)
        except Exception,e:
            self.log.error( 'Unable to create a MySQLdb connection to "%s"@%s.%s: %s'%(self.user,self.host,self.db_name,traceback.format_exc()))
            self.db = None
            
    def __extractorCommand(self,extractor=None,command='',args=[]):
        if not command: raise Exception,'Reader__extractorCommand:CommandArgumentRequired!'
        if not extractor: extractor = self.get_extractor()
        extractor.ping()
        try:
            print 'in Reader.__extractorCommand: calling HdbExtractor(%s).%s(%s)'%(extractor.name(),command,args)            
            result = extractor.command_inout(*([command]+(args and [args] or [])))
        except PyTango.DevFailed, e:
            #e.args[0]['reason'],e.args[0]['desc'],e.args[0]['origin']
            reason = '__len__' in dir(e.args[0]) and e.args[0]['reason'] or e.args[0]
            if 'Broken pipe' in str(reason):
                extractor.init()
                result = extractor.command_inout(*([command]+(args and [args] or [])))
            elif 'MEMORY_ERROR' in str(reason):
                #raise Exception,'Extractor_%s'%reason
                self.clean_extractor()
                extractor.init()
                result = extractor.command_inout(*([command]+(args and [args] or [])))
            else:
                print traceback.format_exc()
                raise Exception,'Reader__extractorCommand:Failed(%s)!'% str(e)
        print 'in Reader.__extractorCommand: command finished'
        return result
            

#################################################################################################
# Method for enabling archiving values in TauTrends
#################################################################################################            

def getArchivedTrendValues(tau_trend,model,start_date,stop_date):
    if not hasattr(tau_trend,'ArchivingReader'): tau_trend.ArchivingReader = Reader.singletone()
    self,reader = tau_trend,tau_trend.ArchivingReader
    attribute = model.getNormalName()
    #Conditions ordered by CPU usage ...
    self.debug('%s: In getArchivedTrendValues(%s,%s,%s)'% (time.ctime(),attribute,start_date,stop_date))
    if self._parent.xIsTime and self.ArchivingReader:           
        #if not self._history ... deprecated as it caused to be continuously read for null attributes
        if (start_date+(.3*(stop_date-start_date)))<reader.last_dates[attribute][0]:
            if reader.is_attribute_archived(attribute):
                self.debug('%s:reading %s from archiving(%d); last = %s; start_date=%s, stop_date=%s'%(time.ctime(),attribute,len(self._history),reader.last_dates[attribute][0],start_date,stop_date))
                history = reader.get_attribute_values(attribute,start_date,stop_date,asHistoryBuffer=True)
                #if history: self.info('readed %d values between %s and %s '%(len(history),history[0].time,history[-1].time))
                #self.info('last_reads: %s - %s'%reader.last_reads)
                return history
            else: self.debug('%s: attribute %s is not archived'%(time.ctime(),attribute))
        else: self.debug('%s: %s archiving data (%s) is newer than showed (%s)'%(time.ctime(),attribute,reader.last_dates[attribute][0],start_date))
    else: self.debug('%s: archiving not available'%(time.ctime()))
    return []