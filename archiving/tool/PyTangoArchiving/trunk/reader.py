
import traceback,time,os
from random import randrange
from collections import defaultdict
import PyTango
from PyTango import TimeVal

try: import fandango
except ImportError: import PyTango_utils as fandango
from fandango.objects import Object,SingletonMap
from fandango.log import Logger
from fandango.servers import ServersDict
from fandango.db import FriendlyDB

from fandango.functional import str2time as str2epoch
from fandango.functional import time2str as epoch2str
try:
    from fandango.functional import ctime2time, mysql2time
except:
    ctime2time = lambda time_struct: (float(time_struct.tv_sec)+1e-6*float(time_struct.tv_usec))
    mysql2time = lambda mysql_time: time.mktime(mysql_time.timetuple())

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
        
       
class Reader(Object,SingletonMap):
    """ 
    Lightweight API for read-only archiving applications 
    
    Arguments:
    
     * If no arguments are passed the Reader object uses *HdbExtractor*s to access the archiving.    
     * If a *db* argument is passed it creates a MySQL connection.
     * *config* param must be an string like **user:passwd@host**
     * if no *config* is passed it is read from Extractor class properties
     * *schema* selects between hdb and tdb databases (historical and temporary)
     
    USAGE:
    
    from PyTangoArchiving import hdb
    reader = hdb.Reader('hdb','user:passwd@host') #Initializes an HdbAPI with the values just required for reading
    reader = hdb.Reader() #In this case initializes an API with no db, using the HdbExtractor instances
    reader.read_attribute(attr,start_date,stop_date) #Which extractor/method to use must be hidden from user
    
    3 : import PyTangoArchiving
    4 : rd = PyTangoArchiving.Reader()
    5 : values = rd.get_attr_values('Straights/VC02/MKS-01/P1','2009-07-24 10:00:00','2009-07-29 16:00:00')
   
    """
    
    ## Methods replaced by SingletonMap
    #__singleton__ = None
    #@classmethod
    #def singleton(cls,*p,**k):
        #if not cls.__singleton__: 
            #cls.__singleton__ = cls(*p,**k)
        #return cls.__singleton__
            
    def __init__(self,db='',config='',servers = None, schema = 'hdb',timeout=300000,log='INFO',logger=None):
        '''@param config must be an string like user:passwd@host'''
        if not logger:
            self.log = Logger('%s.Reader'%schema,format='%(levelname)-8s %(asctime)s %(name)s: %(message)s')
            self.log.setLogLevel(log)
        else: self.log = logger
        
        self.log.info('In PyTangoArchiving.Reader.__init__(%s)' % schema)
        self.db_name = db    
        self.db = None
        self.schema = schema        
        self.timeout = timeout
        self.updated = time.time()
        self.attr_extracted = {}
        
        #Initializing Database connection
        if self.db_name:
            try:
                if not config:
                    try: config = (PyTango.Database().get_class_property('%sextractor'%schema,['DbConfig'])['DbConfig'] or [''])[0]
                    except: config = ''
                self.user,self.host = '@' in config and config.split('@',1) or (config,os.environ['HOST'])
                self.user,self.passwd = ':' in self.user and self.user.split(':',1) or (self.user,'')            
                #else: self.user,self.host,self.passwd = '','',''
                self.log.info('Accessing MySQL using config = %s:...@%s' % (self.user,self.host))
                self.db = FriendlyDB(self.db_name,self.host,self.user,self.passwd)
            except:
                self.log.error(traceback.format_exc())
                self.log.warning('Unable to connect to MySQL, using Java %sExtractor devices'%self.schema.upper())
                self.db = None
                
        if self.db is None:
            #Initializing archiver extractors proxies
            self.servers = servers or ServersDict()
            self.servers.log.setLogLevel(log)
            self.servers.load_by_name('%sextractor'%schema)
            self.extractors = self.servers.get_class_devices(['TdbExtractor','HdbExtractor'][schema=='hdb'])        
        else:
            self.servers,self.extractors = {},[]
        
        #Initializing the state machine        
        self.reset() 
        
    def __del__(self):
        if self.db: del self.db
        
    def reset(self):
        self.last_dates = defaultdict(lambda:(1e10,0))
        if hasattr(self,'state') and self.db: 
            self.db.renewMySQLconnection()
        self.last_retry = 0
        self.available_attributes = []
        self.failed_attributes = []
        if self.extractors or self.db:
            self.state = PyTango.DevState.INIT
        else:
            self.state = PyTango.DevState.FAULT
            self.log.info('No available extractors found, PyTangoArchiving.Reader disabled')
        
    def check_state(self,period=300):
        """ It tries to reconnect to extractors every 5 minutes. """
        if (time.time()-self.last_retry)>period: 
            self.last_retry = time.time()
            try:
                if self.db: 
                    return bool(self.db.Query('describe adt'))
                elif self.extractors:
                    return bool(self.get_extractor(check=False))
                else:
                    return False
            except: 
                self.log.error('In Reader.check_state: %s'%traceback.format_exc())
                return False
        else: 
            return (self.state!=PyTango.DevState.FAULT)
        
    #################################################################################################
    #################################################################################################
                
    def get_extractor(self,check=True,attribute=''):
        """ Gets a random extractor device."""
        if (check and not self.check_state()) or not self.servers or not self.extractors:
            self.warning('Archiving seems not available')
            return None
        extractor = None
        #First tries to get the previously used extractor, if it is not available then searches for a new one ....
        if attribute and attribute in self.attr_extracted:
            extractor = self.servers.proxies[self.attr_extracted[attribute]]
            try:
                extractor.ping()
                extractor.set_timeout_millis(self.timeout)
            except Exception,e: extractor = None
        if not extractor:
            remaining = self.extractors[:]
            while remaining: #for i in range(len(self.extractors)):
                next = randrange(len(remaining))
                devname = remaining.pop(next)
                extractor = self.servers.proxies[devname]
                try:
                    extractor.ping()
                    extractor.set_timeout_millis(self.timeout)
                    break
                except Exception,e: 
                    self.log.debug(traceback.format_exc())
        self.state = PyTango.DevState.ON if extractor else PyTango.DevState.FAULT
        return extractor    
        
    def get_attributes(self):
        """ Queries the database for the current list of archived attributes."""
        if self.available_attributes and time.time()<(self.updated+15): 
            return self.available_attributes
        if self.db: #Using a database Query
            self.available_attributes = [a[0].lower() for a in self.db.Query('select full_name from adt,amt where amt.stop_date is NULL and adt.ID=amt.ID;') if a and a[0]]
        elif self.extractors: #Using extractors
            self.available_attributes = [a.lower() for a in self.__extractorCommand(self.get_extractor(),'GetCurrentArchivedAtt')]
        return self.available_attributes
    
    def is_attribute_archived(self,attribute):
        """ This method uses two list caches to avoid redundant device proxy calls, launch .reset() to clean those lists. """
        attribute = attribute.lower()
        if not self.check_state() or attribute in self.failed_attributes: return False
        if attribute in self.available_attributes: return True
        value = (attribute in [a.lower() for a in self.get_attributes()])
        (self.available_attributes if value else self.failed_attributes).append(attribute)
        return value
        
    def get_attribute_values(self,attribute,start_date,stop_date=None,asHistoryBuffer=False):
        '''         
        This method reads values for an attribute between specified dates.
        This method may use MySQL queries or an H/TdbExtractor DeviceServer to get the values from the database.
        The format of values returned is [(epoch,value),]
        The flag 'asHistoryBuffer' forces to return the rawHistBuffer returned by the DS.
                
        :param attributes: list of attributes
        :param start_date: timestamp of the first value
        :param stop_date: timestamp of the last value
        :param asHistoryBuffer: return a history buffer object instead of a list (for trends)
        
        :return: a list with values (History or tuple values depending of args)
        '''
        self.log.debug('In PyTangoArchiving.Reader.get_attribute_values')
        if not self.check_state(): 
            self.log.info('In PyTangoArchiving.Reader.get_attribute_values: Archiving not available!')
            return []
        
        if not stop_date: stop_date=time.time()
        if type(start_date) is not str: start_date = epoch2str(start_date)
        if type(stop_date) is not str: stop_date = epoch2str(stop_date)
        self.last_dates[attribute] = str2epoch(start_date),str2epoch(stop_date)
     
        if not self.db:
            try: 
                extractor = self.get_extractor(attribute=attribute)
                #self.clean_extractor(extractor)
                result = self.__extractorCommand(extractor,'GetAttDataBetweenDates',[attribute,start_date,stop_date])
                vattr,vsize=str(result[1][0]),int(result[0][0])
                time.sleep(0.2)
                if vattr not in [a.name for a in extractor.attribute_list_query()]:
                    raise Exception,'%s_NotIn%sAttributeList'%(vattr,extractor.name())
                self.log.debug( '\treading last value of attribute %s'%vattr)
                last_value = extractor.read_attribute(vattr).value
                self.log.debug('\treading %s attribute history values of %s (last_value = %s)'% (vsize,vattr,last_value))
                history=extractor.attribute_history(vattr,vsize)
                self.clean_extractor(extractor,vattr)
                self.attr_extracted[attribute]=extractor.name()
            except Exception,e: 
                self.log.debug( traceback.format_exc())
                raise Exception,'Archiving.Reader_ExtractorFailed(%s)!:%s' % (extractor.name(),str(e))
            if int(PyTango.__version__.split('.')[0])>=7:
                values = asHistoryBuffer and history or [(ctime2time(h.time),h.value) for h in history]
                self.last_reads = history and (ctime2time(history[0].time),ctime2time(history[-1].time)) or (1e10,1e10)
            else:
                values = asHistoryBuffer and history or [(ctime2time(h.value.time),h.value.value) for h in history]
                self.last_reads = history and (ctime2time(history[0].value.time),ctime2time(history[-1].value.time)) or (1e10,1e10)                
        else:
            get_mysql_value = lambda v: (mysql2time(v[0]),v[1 if len(v)<4 else 2]) #Date and read value (excluding dimension?!?)
            
            #Getting the data from DB
            ID = self.db.Query("select ID from adt where full_name like '%s';" % attribute)[0][0]
            table = 'att_%05d'%ID
            column = 'read_value' if 'read_value' in ','.join([l[0] for l in self.db.Query("describe %s"%table)]).lower() else 'value'
            query = "select time,%s from %s where time between '%s' and '%s'"%(column,table,start_date,stop_date)
            self.log.debug( '... the query is ... %s'%query)
            values = self.db.Query(query)
            self.last_reads = values and (mysql2time(values[0][0]),mysql2time(values[-1][0])) or (1e10,1e10)

            if not asHistoryBuffer: #Returning a list of (epoch,value) tuples
                for i in range(len(values)):
                    values[i] = get_mysql_value(values[i])
            else: #Simulating DeviceAttributeHistory structs
                class FakeAttributeHistory():
                    def __init__(self,date,value):
                        self.value = value
                        self.time = TimeVal(date)
                    def __repr__(self): 
                        return 'FakeAttributeHistory(value=%s,time=%s)'%(self.value,self.time)
                for i in range(len(values)):
                    values[i] = FakeAttributeHistory(*get_mysql_value(values[i]))
            #history = []
            #while values:
                #v = values.pop(0)
                #if len(v)<4: history.append((v[0],v[1])) #Date and read value
                #else: history.append((v[0],v[2])) #Date and read value (excluding dimension?!?)            
            #values = asHistoryBuffer and history or [(time.mktime(h[0].timetuple()),h[1]) for h in history]
            #self.last_reads = history and (mysql2time(history[0]),mysql2time(history[-1])) or (1e10,1e10)
        return values
                
    def get_attributes_values(self,attributes,start_date,stop_date=None,correlate=True,asHistoryBuffer=False,trace = False, text = False):
        """ 
        This method reads values for a list of attributes between specified dates.
        
        :param attributes: list of attributes
        :param start_date: timestamp of the first value
        :param stop_date: timestamp of the last value
        :param correlate: group values by time using first attribute timestamps
        :param asHistoryBuffer: return a history buffer object instead of a list (for trends)
        :param trace: print out the values obtained
        :param text: return a tabulated text instead of a dictionary of values
        
        :return: a dictionary with the values of each attribute or (if text=True) a text with tabulated columns
        
        """
        if not attributes: raise Exception('Empty List!')
        values = dict([(attr,self.get_attribute_values(attr,start_date,stop_date,asHistoryBuffer)) for attr in attributes])
        if correlate or text:
            #table = [ [t0]+
                    #[([v for t,v in var if t<=t0] or [None])[-1] for attr,var in sorted(values.items())] 
                    #for t0,v0 in values[attributes[0]] ]
            #if trace: print('\n'.join(['\t'.join(['time']+[k for k in sorted(values.keys())])]+['\t'.join([str(s) for s in t]) for t in table]))     
            table = defaultdict(list)
            index = dict((k,0) for k in values)        
            reference = sorted((len(v),k) for k,v in values.items())[-1][-1] #attributes[0]
            for t0,v0 in values[reference]:
                for k,v in sorted(values.items()):
                    last = None
                    for t in v[index[k]:]:
                        if t[0]>t0: break
                        last = t[1] if t[1] is not None else last
                    table[k].append((t0,last))
            if trace or text: 
                csv = ('\n'.join(['\t'.join(['date','time']+[k for k in sorted(table.keys())])]+
                    ['\t'.join([time.ctime(table.values()[0][i][0]),str(table.values()[0][i][0])]+[str(table[k][i][1]) for k in sorted(table.keys())]) 
                        for i in range(len(table.values()[0]))]))
                if text: return csv
                elif trace: print text
            return table
        else:
            if trace: print values
            return values
                    
    #################################################################################################
    #################################################################################################
        
    def clean_extractor(self,extractor,vattr=None):
        ''' removing dynamic attributes from extractor devices ...'''
        #self.log.debug('In PyTangoArchiving.Reader.__cleanExtractor(): removing dynamic attributes')
        self.log.debug( 'In PyTangoArchiving.Reader.__cleanExtractor(): removing dynamic attributes')
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
            self.log.debug( 'in Reader.__extractorCommand: calling HdbExtractor(%s).%s(%s)'%(extractor.name(),command,args))
            result = extractor.command_inout(*([command]+(args and [args] or [])))
        except PyTango.DevFailed, e:
            #e.args[0]['reason'],e.args[0]['desc'],e.args[0]['origin']
            reason = '__len__' in dir(e.args[0]) and e.args[0]['reason'] or e.args[0]
            if 'Broken pipe' in str(reason):
                extractor.init()
                result = extractor.command_inout(*([command]+(args and [args] or [])))
            elif 'MEMORY_ERROR' in str(reason):
                #raise Exception,'Extractor_%s'%reason
                self.clean_extractor(extractor.name())
                extractor.init()
                result = extractor.command_inout(*([command]+(args and [args] or [])))
            else:
                self.log.debug(traceback.format_exc())
                raise Exception,'Reader__extractorCommand:Failed(%s)!'% str(e)
        #self.log.debug( 'in Reader.__extractorCommand: command finished')
        return result
            

#################################################################################################
# Method for enabling archiving values in TauTrends
#################################################################################################            

def getArchivedTrendValues(tau_trend,model,start_date,stop_date,log='INFO',use_db=True,db_config=''):
    """This method allows to extract the values from the archiving system either using HdbExtractor device servers or MySQL access (requires permissions).
    
    This method can be tested with the following code:
    def debug(s): print s
    trend = type('fake',(object,),{})()
    trend._history,type(trend).debug,trend._parent = [],(lambda c,s:debug(s)),type('',(object,),{'xIsTime':True} )()
    PyTangoArchiving.reader.getArchivedTrendValues(trend,'BO02/VC/SPBX-03/I1',time.time()-24*3600,time.time(),'DEBUG')
    """
    self = tau_trend
    try:
        if not self._parent.xIsTime: 
            #self.debug('Archiving is available only for trends')
            return
        logger = self.info if not getattr(self,'HDBArchivingReader','') else self.debug
        logger('In getArchivedTrendValues(%s,%s,%s,%s)'%(model,start_date,stop_date,use_db))
        for schema in (['HDB','TDB'] if start_date>(time.time()-3*24*3600) else ['HDB']):
            if not hasattr(tau_trend,'%sArchivingReader'%schema):
                try:
                    if not use_db: raise Exception,'DB_NOT_USED'
                    else: #The ArchivingReader will use direct MySQL access
                        logger('getArchivedTrendValues(use_db=True)')
                        if db_config: 
                            reader = Reader(db=schema.lower(),config=db_config,schema=schema.lower())
                        else: 
                            reader = Reader(db=schema.lower(),schema=schema.lower())
                        setattr(tau_trend,'%sArchivingReader'%schema,reader)
                
                except Exception,e: #If DB is not used or not available, Java Device Servers are used instead.   
                    if use_db: self.info('Unable to use MySQL, switching to Java extractors:\n%s' %traceback.format_exc())
                    #Reader using HdbExtractor devices
                    reader = Reader(schema = schema.lower())
                    setattr(tau_trend,'%sArchivingReader'%schema,reader) 
                            
        attribute = getattr(model,'getNormalName',model.__str__)()
        
        #Conditions ordered by CPU usage ...    
        if start_date>(time.time()-3*24*3600) and tau_trend.TDBArchivingReader.is_attribute_archived(attribute):
            logger('Accessing TDB archiving ...')
            reader = tau_trend.TDBArchivingReader
        else:
            logger('Accessing HDB archiving ...')
            reader = tau_trend.HDBArchivingReader
        if not reader or not reader.check_state(): 
            self.info('Archiving readings not available!')
            return    
        
        #if not self._history ... deprecated as it caused to be continuously read for null attributes
        if (start_date+(.3*(stop_date-start_date)))<reader.last_dates[attribute][0]:
            if reader.is_attribute_archived(attribute):
                self.info('%s:reading %s from archiving(%d); last = %s; start_date=%s, stop_date=%s'%(time.ctime(),attribute,len(self._history),reader.last_dates[attribute][0],start_date,stop_date))
                history = reader.get_attribute_values(attribute,start_date,stop_date,asHistoryBuffer=True)
                #if history: self.info('readed %d values between %s and %s '%(len(history),history[0].time,history[-1].time))
                #self.info('last_reads: %s - %s'%reader.last_reads)
                self.info('%d readings obtained for %s: %s ...' % (len(history),attribute,','.join([str(s) for s in history[:3]]) ))
                return history
            else: logger('%s: attribute %s is not archived'%(time.ctime(),attribute))
        else: logger('%s: %s archiving data (%s) is newer than showed (%s)'%(time.ctime(),attribute,reader.last_dates[attribute][0],start_date))
        
        ## DISABLED AS IT DIDN'T WORKED WELL
        #model = [str(s).lower() for s in self.getModel().split(',')]
        #for attribute in reader.last_dates.keys():
            #if attribute.lower() not in model:
                #self.info('In getArchivedTrendValues(): attribute %s removed from trend(%s).'%(attribute,model))
                #reader.last_dates.pop(attribute)
        return []
    except:
        self.error('Exception in Reader.getArchivedTrendValues(%s): %s' % (model,traceback.format_exc()))
