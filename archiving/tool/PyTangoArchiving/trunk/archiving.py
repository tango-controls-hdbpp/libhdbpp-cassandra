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

import traceback,time,sys,re
from random import randrange
from collections import defaultdict

import PyTango

try: import fandango
except ImportError: import PyTango_utils as fandango
from fandango.objects import Object
from fandango.log import Logger
from fandango.servers import ServersDict
from fandango.db import FriendlyDB
import fandango.functional as fun

from PyTangoArchiving.common import CommonAPI
from PyTangoArchiving import ARCHIVING_CLASSES,ARCHIVING_TYPES,MAX_SERVERS_FOR_CLASS,MIN_ARCHIVING_PERIOD
import PyTangoArchiving.utils as utils
from PyTangoArchiving.dbs import ArchivingDB


#################################################################################################
# Class for managing archived attributes
#################################################################################################      

class ArchivedAttribute(Object):
    """ 'Class used within the ArchivingAPI to keep the information for each attribute '
    """
    def __init__(self,name='',device='',Type='',ID=None,archiver='',start_date=0,archiving_mode='',last_value=None,last_date=0):
        """
        name='' "Attribute name"
        device='' "Attribute device (not the archiver)"
        Type='' "Archiving Type (hdb or tdb)"
        modes='' #"Archiving Modes (Dictionary containing a list of parameters for each mode)"
        dedicated='' #" Whether there's an archiver dedicated to this attribute or not"
        """
        #self.api = api
        self.name,self.device,self.Type,self.modes,self.dedicated,self.exception=name,device,Type,{},False,''
        self.setID(ID)
        self.setArchiver(archiver,archiving_mode)
        self.setLastValue(last_value,last_date)
        self.dedicated = ''
        self.properties = {}

    def setID(self,ID):
        """ Store Information from adt table
        ID=None ;"Unique ID associated to the attribute in the database"
        """
        self.ID,self.table=ID,('att_%05d'%int(ID) if ID else None)

    def setConfig(self,data_type,data_format,writable):
        """ Stores the information about Attribute Format and Type (adt table)
        writable=False; This value defines the structure of the database table
        """
        self.data_type = PyTango.ArgType.values[data_type]
        self.data_format = PyTango.AttrDataFormat.values[data_format]
        self.writable = PyTango.AttrWriteType.values[writable]
        
    def setArchiver(self,archiver,start_date=0,archiving_mode=''):
        """ Store Information from amt table
        archiver='' ;"Archiving device on charge of archiving this attribute"
        start_date=None ;"Date of last archiving configuration"
        archiving_mode='' "Last archiving configuration"
        """        
        self.archiver,self.start_date,self.archiving_mode=archiver,start_date,archiving_mode
        self.extractModeString()

    def setLastValue(self,last_value,last_date):
        """ Store Information from att_%05d%ID table
        last_value=None ;"Last value archived"
        last_date=None ;"Last epoch archived"
        """                
        self.last_value,self.last_date=last_value,last_date
    #------------------------------------------------------------------------
    def extractModeString(self,modestring=None):
        ''' It is used to convert between the format used by the JAVA Api and a dictionary
        String="attribute:MODE_P,1,MODE_A,1,2,3,MODE_R,1,2,3,..."
        Dictionary={'MODE_P':[1],'MODE_A':[1,2,3],'MODE_R':[1,2,3]}
        '''
        if self and not modestring: modestring=self.archiving_mode
        if type(modestring)==str:
            modestring=modestring.replace(' ','')
            if not ':' in modestring: attrib,params=self.name,modestring
            else: attrib,params=modestring.split(':')
            modes={}
            for m in params.split('MODE_'):
                if not m: continue
                modes['MODE_'+m.split(',')[0]]=[]
                for p in [v for v in m.split(',')[1:] if v]:
                    modes['MODE_'+m.split(',')[0]].append(float(p))
            if self and attrib==self.name or attrib==(self.device+'/'+self.name):
                self.modes=modes
            return modes
        if type(modestring)==dict:
            modes,nmodes=modestring,[]
            #Building a sorted list of modes
            porder = ['MODE_P','MODE_A','MODE_R','MODE_T','MODE_C','MODE_D','MODE_E']
            for o in porder: 
                if o in modes: 
                    nmodes+=[o]+['%d'%int(modes[o][0])]+[str(n) for n in modes[o][1:]] #First argument is integer period, for the rest ints will be ints, floats will be floats
            return ','.join(nmodes)
    def __repr__(self):
        if not self.modes and self.archiving_mode: self.extractModeString()
        return '%d:%s:%s:%s'%(self.ID,self.name,self.archiver,str(self.modes))    
        
####################################################################################################
##              ARCHIVING API OBJECT        
####################################################################################################        
        
class ArchivingAPI(CommonAPI):
    """ SubClass of PyTangoArchiving.CommonAPI that provides specific methods for Hdb/Tdb databases. 
    Singleton objects for each schema are managed from CommonAPI.
    """
    KEEPING_PERIOD = 5*24*60*60 # Time, in seconds, that the data will be kept in TDB database
    EXPORT_PERIOD = 10*60 # Time, in seconds, that TDB data will be buffered before inserting in MySQL
    
    def __init__(self,schema,host=None,user='browser',passwd='browser',classes=[],LogLevel='info',load=False,values=False):
        self.schema = schema.lower()
        assert self.schema in ['hdb','tdb'], 'UnknownSchema_%s'%schema
        CommonAPI.__init__(self,self.schema,host,user,passwd,classes=self.get_archiving_classes(),LogLevel=LogLevel)
        self.db = ArchivingDB(self.schema,self.host,self.user,self.passwd)
        self.load_all(values=values,dedicated=load)
        
    ## The ArchivingAPI is an iterator to its own attributes
    def __getitem__(self,k): return self.attributes.__getitem__(k)
    def __contains__(self,k): return self.attributes.__contains__(k)
    def get(self,k): return self.attributes.get(k)
    def has_key(self,k): return self.attributes.has_key(k)
    #[setattr(self,method,lambda k,meth=method:getattr(self.attributes,meth)(k)) for method in ('__getitem__','__contains__','get','has_key')]
    def __iter__(self): return self.attributes.__iter__()
    def iteritems(self): return self.attributes.iteritems()
    def keys(self): return self.attributes.keys()
    def values(self): return self.attributes.values()
    #[setattr(self,method,lambda meth=method:getattr(self.attributes,meth)()) for method in ('__iter__','iteritems','items','keys','values')]

    
    def get_archiving_classes(self):        
        '''overriden for convenience '''
        self.ArchivingClasses = [k for k in ARCHIVING_CLASSES if self.schema in k.lower()]
        if self.schema!='snap': self.ArchivingClasses.append('ArchivingManager')
        return self.ArchivingClasses
    
    def __clean_extractor(self,extractor,vattr=None):
        ''' removing dynamic attributes from extractor devices ...'''
        #self.log.debug('In PyTangoArchiving.Reader.__cleanExtractor(): removing dynamic attributes')
        print 'In PyTangoArchiving.Reader.__cleanExtractor(): removing dynamic attributes'
        if isinstance(extractor,PyTango.DeviceProxy): 
            name,proxy=extractor.dev_name(),extractor
        else: 
            name,proxy=extractor,self.servers.proxies[extractor]
        if vattr: proxy.RemoveDynamicAttribute(vattr)
        else: proxy.RemoveDynamicAttributes()              
        
    def __extractorCommand(self,extractor=None,command='',args=[]):
        if not command: raise Exception,'Reader__extractorCommand:CommandArgumentRequired!'
        if not extractor: extractor = self.get_extractor()
        extractor.ping()
        try:
            print 'in Reader.__extractorCommand: calling HdbExtractor command %s(%s)'%(command,args)            
            result = extractor.command_inout(*([command]+(args and [args] or [])))
        except PyTango.DevFailed, e:
            #e.args[0]['reason'],e.args[0]['desc'],e.args[0]['origin']
            reason = '__len__' in dir(e.args[0]) and e.args[0]['reason'] or e.args[0]
            if 'Broken pipe' in str(reason):
                extractor.init()
                result = extractor.command_inout(*([command]+(args and [args] or [])))
            elif 'MEMORY_ERROR' in str(reason):
                self.clean_extractor()
                extractor.init()                
                raise Exception,'Extractor_%s'%reason
            else:
                print traceback.format_exc()
                raise Exception,'Reader__extractorCommand:Failed(%s)!'% str(e)
        print 'in Reader.__extractorCommand: command finished'
        return result
                
    #----------------------------------------------------------------------------------------------
    # METHODS TO GET/SET ARCHIVING INFORMATION    
    
    def get_archived_attributes(self):
        return self.__extractorCommand(self.get_extractor(),'GetCurrentArchivedAtt')
    
    def is_attribute_archived(self,attribute):
        return (attribute.lower() in [a.lower() for a in self.get_archived_attributes()])    
        
    def get_attribute_archivers(self,attribute_list=None):
        '''
        This method returns a dictionary {attribute:archiver}. 
        This information is acquired through the ArchivingManager device.
        Note: This method does the same function than ArchivingDB.loadAttrStatus; 
        but using the Java API instead of accessing the database.
        '''
        if not attribute_list: atts = sorted(self.attributes)
        else: atts = sorted(attribute_list)
        
        try:
            isArch = self.get_manager().command_inout('IsArchived%s'%self.schema.upper(),atts)
        except Exception,e:
            self.log.error('Exception in get_attribute_archivers: IsArchived%s(%s): %s'%(self.schema.upper(),str(atts),str(e)))
            return {}
        archAtts = [a.lower() for i,a in enumerate(atts) if isArch[i]]
        try:
            dbstatus = manager.command_inout('GetStatus%s'%self.schema.upper(),archAtts)
        except Exception,e:
            self.log.error('Exception in get_attribute_archivers: GetStatus%s(%s): %s'%(self.schema.upper(),str(archAtts),str(e)))
            return {}
        archivers = dict([(archAtts[i],dbstatus[i].split(':')[0].strip()) for i in range(len(dbstatus))])
        [setattr(self.attributes[a],'archiver',archivers[a.lower()]) for a in atts if a.lower() in archAtts and a in self.attributes]
        #for k,v in previous_archiver.items(): print k,v
        return previous_archiver

    def get_archivers_load(self):
        '''def getArchiversLoad(self,schema): return a dict with archiver:load for the given schema'''
        for a in self.get_archivers():
            self.log.debug('checking %s load'%a)
            try:
                self.loads[a]=self.proxies[a].read_attribute('scalar_charge').value
                self.loads[a]+=self.proxies[a].read_attribute('image_charge').value
                self.loads[a]+=self.proxies[a].read_attribute('spectrum_charge').value
            except Exception,e:
                #print 'Unable to get %s archiver load. %s'%(a,str(e))
                self.log.warning('Unable to get %s archiver load. %s'%(a,str(e)))
                self.loads[a]=0
                pass
        return self.loads
    
    def get_attribute_by_ID(self,ID):
        for a in self.attributes.values():
            if a.ID==ID: return a
        return None
            
    
    #----------------------------------------------------------------------------------------------
    # METHODS FOR DEBUGGING

    def diff(self,attribute_list):
        ''' 
        Attrslist input is a dictionary with keys = attribute_names and values = modes
        Warning if new archiving is less intensive than existing one
        returns a list with the attributes not found in the database (new ones)
        '''
        dbattrs = self.attributes
        newattributes=[]
        for a,v in attribute_list.items():
            if a in dbattrs:
                modes=v['modes']
                dbmodes=dbattrs[a].extractModeString()
                #print a,':',modes
                #print '\tDB:',dbmodes
                for k,v in modes.items():
                    if k not in dbmodes: continue
                    else:
                        if len(modes[k])!=len(dbmodes[k]) or any(modes[k][i]>dbmodes[k][i] for i in range(len(modes[k]))):
                            #print a,':',k,'=',v,' vs DB:',dbmodes[k]
                            self.log.debug('%s:%s = %s vs DB: %s'%(str(a),str(k),str(v),str(dbmodes[k])))
                pass
            else:
                self.log.debug('%s,New attribute!: %s'%(a,v))
                newattributes.append(a)
        return newattributes    

    def get_archiving_modes(self,attribute=''):
        #getAttributesArchived(ArchivingType='ALL'): returns the list of attributes archived in the specified database        
        if not attribute: return self.get_watcher().command_inout('GetAllArchivingAttributes')
        else: return self.get_manager().command_inout('GetArchivingMode%s'%self.schema.upper(),[attribute])

    def get_archiver_status(self,archiver):
        return self.proxies[archiver].command_inout('StateDetailed')     
    
    def check_modes(self,modes,min_periodic=0,min_absolute=.01,min_relative=1):
        """ modes must be a dictionary in the form: {'MODE_X':[params]} 
        This is Tango!!! ... so periods must be milliseconds!
        """
        porder = ['MODE_P','MODE_A','MODE_R','MODE_T','MODE_C','MODE_D','MODE_E']        
        arch_type = self.schema #'hdb' or 'tdb'
        min_periodic = min_periodic or (('MODE_R' in modes or 'MODE_A' in modes) and 30000) or 10000
        max_periodic = (('MODE_R' in modes or 'MODE_A' in modes) and 43200000) or 60000
        new_modes = {}
        corrections = 0
        for mode,old_params in modes.items():
            mode = mode.upper()
            if mode not in porder:
                continue
            #Period correction ... with Log!
            #Converting the period to a number of milliseconds (multiplying by 1000 if period is below 10000)
            try: params = old_params[:]
            except: params = [old_params]
            for i,p in enumerate(params): #Convert strings into float numbers
                params[i]=float(p or 0)
            if params[0]<(3600 if arch_type=='hdb' else 1000): #Minimum allowed period is 10000 ms; so it is not allowed to introduce a number of 1800(30min.)!?
                prev = params[0]
                params[0]=prev*1000. #Periods must be between 15 and 3600
                self.log.debug('%s Period converted from %s seconds to %f milliseconds'%(mode,prev,params[0]))
            if (mode=='MODE_R' or mode=='MODE_A') and params[0]<(10000 if arch_type=='hdb' else 1000):
                params[0]=(10000 if arch_type=='hdb' else 1000)
            if mode=='MODE_R':
                if len(params)==1:
                    params.extend([min_relative,min_relative])
                elif len(params)==2:
                    params.append(params[1])
                elif len(params)>3:
                    params = params[:3]
                for i in range(len(params)):
                    if not params[i]: 
                        params[i] = min_relative
            if mode=='MODE_A':
                if len(params)==1:
                    params.extend([min_absolute,min_absolute])
                elif len(params)==2:
                    params.append(params[1])
                elif len(params)>3:
                    params = params[:3]                    
                for i in range(len(params)):
                    if not params[i]: 
                        params[i] = min_absolute
            if mode=='MODE_P':
                if min_periodic>params[0] or max_periodic<params[0]: 
                    params[0]= min((max((params[0],min_periodic)),max_periodic))   
                if len(params)>1: 
                    params = params[:1]
            if old_params!=params:
                corrections += 1
            new_modes[mode]=[float(p) for p in params]
            if corrections:
                #self.log.debug('Modes corrected from %s to %s'%(sorted(modes.items()),sorted(new_modes.items())))
                pass
        return new_modes
    
    def check_attributes_description(self,attributes,repair=False):
        """ It checks if real attribute properties matches with the values inserted in the database adt table.
        param repair If True it forces an ALTER TABLE to repair the error
        @todo Now it only repairs AttrWriteType ; ArgType and AttrDataFormat should be also repaired.
        :return: the number of attribute descriptions repaired
        """
        self.log.debug('In DB_check_attributes_description(...)')
        repaired = 0
        if not isinstance(attributes,list): attributes = [attributes]
        if any(attr not in self.attributes or not hasattr(self.attributes[attr],'data_type') for attr in attributes):
            self.load_attribute_description('hdb')
        for attr in attributes:
            try:
                ap = PyTango.AttributeProxy(attr)
                ac = ap.get_config()
                ad = self.attributes[attr]
                self.log.debug('In DB_check_attributes_description(%s): AC vs ADT'%attr)
                #Checking data TYPE
                if ac.data_type!=ad.data_type:
                    self.log.warning('Attribute %s Type differs between Tango(%s) and ADT(%s)'% \
                        (attr,PyTango.ArgType.values[ac.data_type],PyTango.ArgType.values[ad.data_type]))
                #Checking data FORMAT
                if ac.data_format!=ad.data_format:
                    self.log.warning('Attribute %s Format differs between Tango(%s) and ADT(%s)'% \
                        (attr,PyTango.AttrDataFormat.values[ac.data_type],PyTango.AttrDataFormat.values[ad.data_type]))
                #Checking WRITE type
                if ac.writable!=ad.writable:
                    self.log.warning('Attribute %s WriteType differs between Tango(%s) and ADT(%s)'% \
                        (attr,PyTango.AttrWriteType.values[ac.writable],PyTango.AttrWriteType.values[ad.writable]))
            except Exception,e:
                self.log.warning('Unable to get AttributeConfig for %s: %s'%(attr,str(e)))
        return repaired      
                  
    def check_archivers(self,load=False):
        """ This method checks if archiver devices are effectively running (StateDetailed should not trigger exception) 
        
        :return: {Archiver:True/None/False} # It will return True if device is Ok, 
                False if not running, None if it seems unresponsive
        """
        if load: self.load_all(values=False)
        archivers = set(a.archiver for a in self.attributes.values() if a.archiver)#hdb.get_archivers()
        return dict((d,utils.check_device(d,command='StateDetailed')) for d in archivers)
                
    def check_attributes_errors(self, attributes=None, hours=12, load=False,time_tolerance=.2,value_tolerance=1.,exclude=None, lazy=False, use_watcher=False):
        """ It crosschecks if the attributes have been archived in a given period according the actual configuration.
        
        Returns a dictionary with the shape: {attribute:{} or {MODE:[errors epochs]}
        It is different from utils.check_archived_attribute as it does not evaluate new values, just the archived ones.
        
        :param attributes: list of attributes to be checked, if nothing specified all the attributes are checked
        :param hours: the time interval in which the values will be examinated
        :param lazy: it checks only if the last value read is within the hours interval
        :param exclude: it is a list of regular expressions for rejecting attributes
        
        :return: {Attribute:[errors_epoch]} # If the value returned for a key is empty it means that attribute has no errors, 
                if an epoch=0 is returned it means that no value has been found
        """
        if attributes is None: attributes = self.attributes.keys()
        else: attributes = fun.toList(attributes)
        exclude,errors = fun.toList(exclude),{}
        if exclude: attributes = [a for a in attributes if not fun.anyone(re.match(e,a) for e in exclude)]
        self.log.info('In check_attributes_errors(attributes[%s],hours=%s,lazy=%s)'%(len(attributes),hours,lazy))        
        
        #Check different behaviours
        if use_watcher:
            # Launch GetErrorArchiversCurrent command on HdbArchivingWatcher and then 
            # use GetErrorsForArchiverCurrent(archiver) to get the list of failed attributes
            raise 'NotImplemented'       
        elif lazy:
            # It simply checks if last reading is within interval; if it is returns False (no errors), if not it returns just the last value
            self.load_last_values(attributes,1)#all(dedicated=False)
            return dict((att,[self[att].last_date] if self[att].last_date<(time.time()-(hours*3600)) else []) for att in attributes if self[att].archiving_mode)
        elif load: 
            self.load_all(values=False,dedicated=False)
        
        for attribute in sorted(attributes):
            if attribute not in self.attributes or not self.attributes[attribute].archiver: continue #Attribute not archived
            archiver,modes = self[attribute].archiver,self.attributes[attribute].modes
            now = time.time()
            t0,v0 = now-3600*hours,None
            #last_date,last_value = 0,None
            try:            
                if not archiver: #The attribute is not scheduled for archiving
                    continue
                if not utils.check_device(archiver): #The archiver is not available    
                    self.log.info('The archiver %s for attribute %s is currently not available, excluding it from test ...'%(archiver,attribute))
                    continue          
                
                # it is verified that the attribute currently exists
                attr_value = utils.check_attribute(attribute)
                if attr_value is None:
                    self.log.info('The attribute %s is currently not available, excluding it from test ...'%(attribute))
                    continue
                
                # Then we check if it is readable
                if isinstance(attr_value,Exception):
                    # For attributes with exceptions only MODE_P can be studied!
                    try: 
                        t0,v0 = self.load_last_values(a)
                        t0 = utils.date2time(last_date)
                    except: 
                        last_date = 0
                    params = self[attribute].modes['MODE_P']
                    period = params[0]/1000.
                    if last_date and (now-t0)>time_tolerance*period:
                        t1 = now
                        self.log.warning('FAILED %s(%s):%s(%s) between %s and %s; delay =  %s;'%
                                            (attribute,archiver,mode,params[1:] or '',time.ctime(t0),time.ctime(t1),(t1-t0-period)))
                        errors[attribute]['MODE_P']=[last_date]
                
                # If readable we check all modes
                else:
                    if modes['MODE_P'][0]/1000.>=hours*3600.:
                        self.log.info('%s(%s) cannot be evaluated because min period (%s) is >= than test interval (%s)' % (attribute,archiver,modes['MODE_P'],hours*3600))                
                    else:
                        errors[attribute]=defaultdict(list)
                        values = self.load_attribute_values(attribute,t0,now)
                        if not values: 
                            [errors[attribute][mode].append(t0) for mode in modes]
                        for value in values:
                            t1,v1 = utils.date2time(value[0]),value[1]
                            for mode,check in utils.check_attribute_modes((t0,v0),(t1,v1),self[attribute].modes).items():
                                if not check:
                                    if mode not in errors[attribute]: 
                                        params = self[attribute].modes[mode]
                                        period = params[0]/1000.
                                        try: diff = v1-v0 #It will crash for any not-number type
                                        except: diff = 0
                                        self.log.warning('FAILED %s(%s)(%s):%s(%s)\n\t between %s and %s;\n\t delay =  %s; diff = %s = %1.1f%%' %
                                            (type(v1).__name__,attribute,archiver,mode,params,time.ctime(t0),time.ctime(t1),(t1-t0-period),diff,abs(100.*diff/v0) if (diff and v0) else 0))
                                    errors[attribute][mode].append(t1)
                            t0,v0 = t1,v1
                        if errors[attribute]: errors[attribute]=dict(errors[attribute])
                        else: errors.pop(attribute)
            except Exception,e:
                #self.log.warning('FAILED %s(%s) ... %s'%(attribute,archiver,traceback.format_exc()))#e))
                self.log.warning('FAILED %s(%s,%s); line %d:  %s'%(attribute,archiver,modes,sys.exc_info()[2].tb_lineno,e))
                if 'Commands out of sync' in str(e): 
                    self.db.renewMySQLconnection()
                    return self.check_attributes_errors(attributes,hours,load,exclude,lazy=True)
                else: errors[attribute] = dict([(m,[0]) for m in modes])
        self.log.warning('In check_attributes_errors(...): %d attributes checked, %d errors found'%(len(attributes),len(errors)))
        return errors
    
    #----------------------------------------------------------------------------------------------
    # DEPRECATED

    def get_attribute_values(self,attribute,start_date,stop_date=None,asHistoryBuffer=False):
        ''' THIS METHOD IS DEPRECATED ... USE PyTangoArchiving.Reader object instead
        def attr_getValues(self,attribute,start_date,stop_date=None):
        This method uses an H/TdbExtractor DeviceServer to get the values from the database.
        The format of values returned is [(epoch,value),]
        The flag 'asHistoryBuffer' forces to return the rawHistBuffer returned by the DS.
        '''
        raise Exception,'Deprecated_by_PyTangoArchiving.Reader.get_attribute_values'
        
        if not stop_date: stop_date=time.time()
        self.log.debug( 'in getArchivedValues(%s) ...'%self.schema)
        if type(start_date) is not str: start_date=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(start_date))
        if type(stop_date) is not str: stop_date=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(stop_date))
        extractor=self.get_extractor()
        self.log.debug( 'GetAttDataBetweenDates(',str([attribute,start_date,stop_date]),')')
        result=extractor.command_inout('GetAttDataBetweenDates',[attribute,start_date,stop_date])
        self.log.debug( ' done ... creating array ...')
        vattr,vsize=result[1][0],result[0][0]
        self.log.debug( ' attribute_history ...(%s,%d)'%(vattr,vsize))
        history=extractor.attribute_history(vattr,vsize)
        self.__cleanExtractor(extractor)
        if asHistoryBuffer: return history
        values=[]
        for h in history: 
            date=float(h.value.time.tv_sec)+1e-6*float(h.value.time.tv_usec)
            values.append((date,h.value.value))
        return values    
    
    #----------------------------------------------------------------------------------------------
    # METHODS TO START/STOP ARCHIVING

    def start_archiving(self,attribute_list,modes={'MODE_P':[60000]},action='start',load=True,retries=3,timeout=90000,max_list=10):
        ''' def start_archiving(attribute_list):
        @argin modes {MODE_P:[1000],MODE_R:[1000,5,5],...}
        '''
        if isinstance(attribute_list,str): attribute_list=[attribute_list]
        elif not hasattr(attribute_list,'__len__'): attribute_list=[attribute_list]
        if not attribute_list: 
            self.log.warning('%s_archiving(%s): The Attribute Names list is EMPTY!0'%(action,self.schema))
            return
        else: 
            self.log.debug('In %s_archiving(%s,%s): %d attribute names in the list'%(action,self.schema,modes,len(attribute_list)))
        devices = set([c.rsplit('/',1)[0] for c in attribute_list])
        devs = defaultdict(list)
        [devs[a.rsplit('/',1)[0]].append(a) for a in attribute_list] #The attributes are be inserted separately for each device
        if len(devs)>1: #Start_archiving is executed independently for each device
            result = True
            for dev,attributes in sorted(devs.items()):
                result = result and self.start_archiving(sorted(attributes),modes,action,False,retries,max_list=max_list)
            if load: self.load_all(values=False)
            return result
        elif len(attribute_list)>max_list:
            result = True
            for i in range(1+int((len(attribute_list)-1)/max_list)): 
                attributes = attribute_list[i*max_list:(i+1)*max_list]     
                result = result and self.start_archiving(sorted(attributes),modes,action,False,retries)
            if load: self.load_all(values=False)
            return result
        else:
            def retry_command(comm,retries=retries):
                """ Function added to do commands with retries. """
                for x in reversed(range(retries)):
                    try: return comm()
                    except Exception,e: 
                        self.log.error('%s_archiving(...): %s command_inout failed, %d retries left' % (action.lower(),str(comm),x))
                        if not x: raise e
                        time.sleep(10.)
                    time.sleep(.5)
                return
                        
            action = action.lower().strip()
            dp = self.get_manager()
            dp.set_timeout_millis(timeout)
            #current_archived = self.get_archived_attributes()
            try:
                dp.ping()
                #Stop archiving of existing attributes
                isArch = retry_command(lambda:dp.command_inout('IsArchived%s'%self.schema.upper(),attribute_list))
                stoplist,archatts = [],[attribute_list[i] for i,v in enumerate(isArch) if v]
                self.log.info('Checking if %s are already %s archived: %s'%(len(attribute_list)==1 and attribute_list or 'attributes',self.schema, isArch))#str(attribute_list))
                
                #Checking Archivers to be running (To avoid SegFault in command_inout)
                for a in archatts:
                    archiver = self.attributes.has_key(a) and self.attributes[a].archiver or ''
                    if not archiver:
                        self.log.warning('ArchivingStop for %s, its archiver is unknown! Launch load_attribute_descriptions first.'%a)
                        #attribute_list.remove(a)
                        stoplist.append(a)
                    elif self.servers[self.servers.get_device_server(archiver)].ping() is None: 
                        self.log.warning('ArchivingStop for %s, archiver %s is not running!'%(a,archiver))
                        #attribute_list.remove(a)
                    else:
                        stoplist.append(a)
                            
                #Stopping archiving
                if stoplist:
                    self.log.info('Stopping archiving for %s attribs ...'%(len(stoplist)<100 and stoplist or str(len(stoplist))))
                    #print 'Stopping archiving for %s attribs ...'%(len(stoplist)<100 and stoplist or str(len(stoplist)))
                    retry_command(lambda:dp.command_inout('ArchivingStop%s'%self.schema.upper(),stoplist))
                elif 'stop' in action.lower(): self.log.warning('No attributes were stop! (not archived or archiver not running)')
    
                time.sleep(1.)
                #Starting archiving
                if attribute_list and action.lower().strip() == 'start':
                    archivedTogether='1'
                    _modes=ArchivedAttribute().extractModeString(modes)
                    if type(_modes) is not str: _modes = ArchivedAttribute().extractModeString(_modes)
                    args = [archivedTogether,str(len(attribute_list))]+attribute_list+_modes.split(',')
                    self.log.debug('Created atribute arguments: %s -> %s'%(modes,str(args)))
                    if self.schema.lower()=='tdb':
                        #TDB_SPECs: ExportPeriod and KeepingWindowDuration
                        args = args + ['TDB_SPEC',str(int(1000*self.EXPORT_PERIOD)),str(int(1000*self.KEEPING_PERIOD))]
                    #Start archiving of attributes
                    self.log.info('Launching: ArchivingManager.ArchivingStart%s(%s) ...'%(self.schema.upper(),args))
                    r = retry_command(lambda:dp.command_inout('ArchivingStart%s'%self.schema.upper(),args))
                
                if load: self.load_all(values=False) #Reloading archiving information  after a modification
                self.log.debug('Out of %s_archiving(...)'%(action))
                return True
            except PyTango.DevFailed,e:
                try:
                    if any(['supported' in a.desc for a in e.args]):
                        self.log.warning()
                except:pass
                PyTango.Except.print_exception(e)
                return False
            except Exception,e:
                exstring = traceback.format_exc()
                print 'Exception occurred and catched at attr_RestartArchiving: ', exstring
                print 'Last exception was: \n'+str(e)+'\n'        
                return False
        pass
    
    def stop_archiving(self,attribute_list,load=True): 
        '''def attr_StopArchiving(self,attribute_list,htype): 
        attribute_list list, htype = 'hdb'/'tdb'
        '''
        self.start_archiving(attribute_list,action='STOP',load=load,max_list=5)
        
    def restart_archiving(self,attribute_list,load=True):
        """ stops/starts archiving of attributes but keeping actual configuration """
        if isinstance(attribute_list,str): attribute_list = [attribute_list]
        for a in attribute_list:
            config = self.attributes[a].modes if (a in self.attributes and self.attributes[a].archiver) else None
            self.stop_archiving(a)
            if config: self.start_archiving(a,config)
            else: self.start_archiving(a)

    #----------------------------------------------------------------------------------------------
    # INFORMATION DIRECTLY READ FROM THE DATABASE
    
    def load(self):
        self.load_all(values=False)
        
    def load_all(self,values=True,dedicated=True):
        self.db.renewMySQLconnection()
        self.load_servers()
        self.load_attribute_descriptions()
        self.load_attribute_properties()
        self.load_attribute_modes()
        self.db.tables.clear()
        self.db.getTables()
        if dedicated: 
            try: self.dedicated = self.get_dedicated_archivers()
            except Exception,e: self.log.warning('Unable to get dedicated archivers: %s'%traceback.format_exc())
        if values: self.load_last_values(n=1)
        
    def load_attribute_descriptions(self):
        self.log.info('Loading attributes description from %s database ...'%self.schema)
        lines=self.db.get_attribute_descriptions()
        for line in lines:
            #print str(line)
            attribute,ID,data_type,data_format,writable=line
            if attribute not in self.attributes:
                self.attributes[attribute]=ArchivedAttribute(name=attribute,device=attribute.rsplit('/',1)[0],Type=self.schema)
            self.attributes[attribute].setID(ID)
            self.attributes[attribute].setConfig(data_type,data_format,writable)
            #self.attributes[attribute].archiver=line[2]
            self.log.debug( 'Attribute configuration is {%s,%s,%s}'%tuple(
                [a+':'+str(getattr(self.attributes[attribute],a)) for a in ['name','device','table']]))
        if self.attributes: self.log.info('%d attribute descriptions loaded from DB'%len(self.attributes))
        return
        
    def load_attribute_properties(self):
        self.log.info('Loading attribute properties from %s database ...'%self.schema)
        if not self.attributes: 
            self.log.warning( 'attribute list is empty, try load_attributes_descriptions()')        
        lines=self.db.get_attribute_properties()            
        for line in lines:
            att = self.get_attribute_by_ID(line['ID'])
            if att: att.properties = line
        return
        
    def load_attribute_modes(self):
        ''' This method reads the contents of the table AMT and sets the values of archiver, start_date and archiving modes inside each ArchivedAttribute struct'''
        self.log.info('Loading attributes status from %s database ...'%self.schema)
        if not self.attributes: 
            self.log.warning( 'attribute list is empty, try load_attributes_descriptions()')
        ##The adt table may be empty!
        self.db.renewMySQLconnection()
        IDs = self.db.get_attribute_modes()
        for att in self.attributes.values():
            #print att
            if att.ID in IDs:
                line=IDs[att.ID]
                ID,archiver,start,stop=line[0:4]
                start_date = time.mktime(time.strptime(str(start),'%Y-%m-%d %H:%M:%S'))
                arch_mode='MODE_P,'+str(line[5])
                if line[6]: arch_mode+=(',MODE_A,'+','.join(str(l) for l in line[7:10]))
                if line[10]: arch_mode+=(',MODE_R,'+','.join(str(l) for l in line[11:14]))
                if line[14]: arch_mode+=(',MODE_T,'+','.join(str(l) for l in line[15:18]))
                if line[18]: arch_mode+=(',MODE_C,'+','.join(str(l) for l in line[19:23]))
                if line[23]: arch_mode+=(',MODE_D,'+str(line[24]))
                if line[25]: arch_mode+=(',MODE_E')
                #att=self.getAttributeByID(ID)
                #self.attributes[att.name].setArchiver(archiver,start_date,arch_mode)
                att.setArchiver(archiver,start_date,arch_mode)
                self.log.debug( 'Attribute %s Status loaded: %s,%s,%s'%tuple(
                    [a+':'+str(getattr(self.attributes[att.name],a)) for a in ['name','archiver','start_date','archiving_mode']]))
            else:
                self.attributes[att.name].setArchiver('') #This allows to differentiate not archived attributes
        return
                
    def load_last_values(self,attribute=None,n=1):
        ''' def DB_getLastNValues(self,attribute,n=1):
        attribute argument could be an string, list or 'ALL' for updating all atributes
        The method returns N registers (1 by def.) ordered BY TIME DESC! (last first)
        The AttributesList.lastValue is updated
        :returns:  [[t0,v0],[t-1,v-1]]
        '''
        lines = []
        now = time.time()
        if attribute is None or type(attribute) in [list,set]:
            self.log.info('Getting last attributes values from %s database ...'%self.schema)
        elif attribute not in self.attributes: 
            self.log.error('The Attribute %s is not being Archived in %s database ...'%(attribute,self.schema))
        else: 
            self.log.debug('Getting last attributes values for %s from %s database ...'%(attribute,self.schema))
        if not self.attributes: 
            self.log.warning( 'attribute list is empty, try load_attributes_descriptions()')
            
        if attribute is None or type(attribute) in [list,set]: #If no attribute is passed all attribute last values will be updated
            self.log.debug('Getting values for a list of attributes ...')
            for att in (type(attribute) in [list,set] and attribute or self.attributes):
                values = self.load_last_values(att, n)
                self.log.debug('Last values acquired for %s'%att)
                if values: 
                    #lines=lines+list(reversed(values))
                    lines=lines+list(values)
                elif att in self.attributes and self.attributes[att].archiver: 
                    self.log.info('Attribute %s archived by %s has no values in archiving database!'%(str(att),str(self.attributes[att].archiver)))
            #IT SHOULD BE SORTED BY TIME!!!
        else:
            self.log.debug('Preparing query for  %s ...'%attribute)
            att=self.attributes[attribute]
            #self.log.debug('Preparing query for  %s ...'%att)
            try:
                lines = self.db.get_last_attribute_values(att.table,n)
                if len(lines):
                    date=time.mktime(lines[0][0].timetuple())+1e-6*lines[0][0].microsecond
                    self.attributes[attribute].setLastValue(lines[0][1],date)
                    if self.attributes[attribute].archiver and date<(now-max([3600]+[mode[0]/1000. for mode in self[attribute].modes.values()])): 
                        self.log.warning('The Attribute %s has not recorded new values since %s.'%(attribute,time.ctime(date)))
                elif self.attributes[attribute].archiver:
                    self.log.error('No values has been found for attribute %s!'%attribute)
            except Exception,e:
                self.log.error('Exception while acquiring data from MySQL for attribute %s:\n%s'%(attribute,traceback.format_exc()))
                self.attributes[attribute].exception='%s:%s'%(time.ctime(),str(e))
        return lines
                
    def load_attribute_values(self,attribute,start_date,stop_date):
        if not self.attributes:
            self.log.warning( 'ERROR!: loadAttributesDescriptionFIRST!')
            return
        if type(start_date) is not str: start_date=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(start_date))
        if type(stop_date) is not str: stop_date=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(stop_date))   
        lines=self.db.get_attribute_values(self.attributes[attribute].table,start_date,stop_date)
        if lines:
            date=time.mktime(lines[-1][0].timetuple())+1e-6*lines[-1][0].microsecond
            self.attributes[attribute].setLastValue(lines[-1][1],date)
            return lines      
        else: return []
        
    #----------------------------------------------------------------------------------------------
    # DEDICATED ARCHIVING   
    
    def get_archiver_reserved_attributes(self,archiver):
        self.dedicated[archiver] = set()        
        properties = self.tango.get_device_property(archiver,['isDedicated','reservedAttributes'])
        if 'true' in (properties['isDedicated'] or ['false'])[0].lower():
            [self.dedicated[archiver].add(str(p).lower()) for p in properties['reservedAttributes']]
            return list(self.dedicated[archiver])    
        else:
            self.dedicated.pop(archiver)
            return []

    def get_dedicated_archivers(self,attr_list=None,check=True,wrongs=None):
        """" This method reads all Dedicated Archivers properties from the Tango database and updates the Attributes list and Dedicated dictionary."""
        self.log.info('In ArchivingAPI.get_dedicated_archivers()')
        archivers,attributes= defaultdict(list),defaultdict(list)        
        wrongs = wrongs if wrongs is not None else []
        if not attr_list: 
            attr_list = self.attributes.keys()
        else:
            if type(attr_list) is str: attr_list=[attr_list]
            attr_list = [a.lower() for a in attr_list]
        for arch in self.get_archivers():
            attrs = self.get_archiver_reserved_attributes(arch)
            if not attrs: continue
            archivers[arch] = attrs
            [(attributes[r].append(arch)) for r in archivers[arch] if (not attr_list) or r in attr_list]
        if check: # It is crosschecked if the configuration is ok
            for d,v in attributes.items():
                if d not in self.attributes: 
                    self.log.warning('Attribute "%s" is dedicated but not in Archiving Database!!! (%s)'%(d,v))
                elif not v: 
                    self.attributes[d].dedicated=''
                else: 
                    self.attributes[d].dedicated=v[0]
                    if v[1:]: 
                        self.log.error('Attribute "%s" has several Dedicated Archivers, modify properties and restart all!!! (%s)'%(d,v))
                        #[self.tango.put_device_property(arch,{'reservedAttributes': [a for a in archivers[arch] if a!=d]}) for arch in v[1:]]
                    if self.attributes[d].archiver and self.attributes[d].dedicated.lower()!=self.attributes[d].archiver.lower():
                        wrongs.append(d)
        if wrongs:
            self.log.error('%d Attribute archivers does not match with dedicated ones!, modify properties and restart all!!!\n%s'%(len(wrongs),wrongs))
        return dict(archivers)

    def set_dedicated_archivers(self,hostslist,max_per_device=35,max_per_server=5,create=True):
        ''' 
        hostslist = {host:[attributes]}
        
        1st: get actual archivers for each host (archiving/schemaarchiver/host-%02d)
        2nd: get load for each existing archiver; {host:{archiver:load}}
        3rd: create new instances if needed and setup properties
            NOTE: In this first release new instances are always created for each new configuration!
        4th: restart all archivers and the ArchivingManager
        '''
        self.log.info('In ArchivingAPI.set_dedicated_archivers(...)')
        klass = '%sArchiver' % ('Hdb' if self.schema == 'hdb' else 'Tdb')
        servers = [s.lower() for s in self.servers if klass.lower() in s.lower()]
        archivers = sorted(self.get_archivers())
        assigned = defaultdict(list)
        for host,attributes in sorted(hostslist.items()):
            attributes= sorted([a.lower() for a in attributes])
            existing = dict.fromkeys( filter(lambda s:bool(re.match('.*/.*/'+host+'[\-0-9]+',s)),archivers) )
            
            ## Filtering the attribute list to remove all attributes already archived in *HOST* archivers
            #self.log.info('Dedicated archivers already existing for this host: %s' % existing.keys())
            for archiver in existing:
                existing[archiver] = self.get_archiver_reserved_attributes(archiver)
                self.log.info('%s is already archiving %s attributes'%(archiver,len(existing[archiver])))
                for p in existing[archiver]:
                    if p in attributes:
                        assigned[archiver].append(p)
                        attributes.remove(p)   
            if not attributes: continue
            
            # Iterating to find the next server/archiver to be created for this host
            iserver,next_server = 1,''
            while iserver<100:
                next_server = '%s/%s-%02d'%(klass,host,iserver)
                if next_server not in servers: break
                iserver+=1
            iarchiver,next_archiver = 1,''
            while iarchiver<100:
                next_archiver = 'archiving/%s/%s-%02d-%02d'%(klass.lower(),host,iserver,iarchiver) #@todo: Change this condition to reuse last archiver
                if next_archiver not in archivers: break
                iarchiver+=1            
            
            #The addition groups attributes from same devices when possible.
            devices = defaultdict(list)
            [devices[a.rsplit('/',1)[0]].append(a) for a in attributes]           
            #use_devs controls if attributes are separated by devices
            group_devs = devices and max(len(a) for a in devices.values())<(max_per_device/2.)           
            if group_devs:  self.log.info('In configure_dedicated_archivers(...): grouping attributes by devices')
            
            # Adding attribute to each archiver (and creating if needed). 
            x=len(attributes)
            while x:
                next_server = '%s/%s-%02d'%(klass,host,iserver)                
                y=max_per_server
                while y and x:
                    next_archiver = 'archiving/%s/%s-%02d-%02d'%(klass.lower(),host,iserver,iarchiver)
                    attribs = [] #Attribs contains the new attributes, while assigned[] contains the already existing; #It would have sense if it was not creating a new device every time is adding devices!?!
                    if group_devs:
                        cop = sorted(devices.keys())
                        for dev in cop:
                            self.log.info('In configure_dedicated_archivers(%s): getting %d attributes from %s' % (host,len(devices[dev]),dev))
                            if (len(assigned[next_archiver])+len(attribs)+len(devices[dev]))<max_per_device: 
                                attribs.extend(devices[dev])
                                devices.pop(dev)
                            else: break
                    else:
                        attribs = attributes[len(attributes)-x:min((len(attributes)+1,len(attributes)-x+max_per_device))]   #y*max_per_device:(y+1)*max_per_device]
                    self.log.info('In configure_dedicated_archivers(%s): Adding new %s device: %s - %s [%s]' % (host,klass,next_server,next_archiver,(len(attribs) or 'EMPTY!')))
                    if attribs: 
                        assigned[next_archiver].extend(attribs)
                        if create:
                            #self.tango.add_server(next_server,di)
                            di = PyTango.DbDevInfo()
                            di.server,di._class,di.klass,di.name = next_server,klass,klass,next_archiver                        
                            self.tango.add_device(di)
                            self.tango.put_device_property(di.name,{'isDedicated':True,'reservedAttributes':assigned[next_archiver]})
                            if self.schema.lower()=='tdb':
                                props = self.tango.get_class_property('TdbArchiver',['DbPath','DsPath','DiaryPath'])
                                self.tango.put_device_property(di.name,props)
                        x ,y,iarchiver = x-len(attribs),y-1,iarchiver+1
                    else: x=0
                iserver+=1
            print ''
            
        [self.servers.load_by_name(k) for k in self.get_archiving_classes()]
        return assigned


    ###########################################################################################################
    # Old dedicated code
    ###########################################################################################################    
        
        #archivers=self.get_archivers_load()
        #if archivers:
            #print 'Loads of existing archivers are: '
            #for n,l in archivers.items():
            #if l: print '%s:\t%d'%(n,l)
        
        ##Using the result of api.DB_loadAttrStatus() instead   #creating a dictionary {attribute:archiver}
        #previous_archiver={}
        #for a in api.attributes.values():
            #if a.archiver: previous_archiver[a.name.lower()]=a.archiver
        
        ##Getting the previous dedicated variables introduced in the archiving system
        #archiver_properties={}
        #for archiver in archivers:
            #archiver_properties[archiver]=api.db.get_device_property(archiver,['isDedicated','reservedAttributes'])
        
        #mod_hosts = {} #To this dict will be added the modified servers
        #mod_archivers = []
        
        ## Checking for each attribute which archivers are available
        ## It will depend of the previous load of each archiver and whether it is dedicated or not
        #atts = [a for a in attrslist.keys() if a not in delete_list]
        #for a in atts:
            #archiver,host,label,serial='','','',1
            #dev=a.rsplit('/',1)[0]
            #host=attrslist[a]['host']
            
            ##Create regular expression for finding a suitable archiver
            #if dev in dedicated.keys():
            #print 'The device %s has a dedicated archiver.'%dev
            #label = dedicated[dev]
            #reg = ('(.*?%s.*?%s-)([0-9]{2,2})'%(host,label)).lower()
            #server='%sArchiver/%s_%s'%('Hdb' if arch_type=='hdb' else 'Tdb',host,label)
            ##print 'The attribute %s is dedicated for %s/%s'%(a,host,label)
            #else:
            ##print 'The attributes is not dedicated, uses generic archivers'
            #reg = ('(.*-)([0-9]{1,2})')
            #server='%sArchiver/%s'%('Hdb' if arch_type=='hdb' else 'Tdb',host)
            
            #if a.lower() in previous_archiver.keys() and re.match(reg,previous_archiver[a.lower()].lower()):
            ##Keeping the same archiver that was being used previously
            #archiver=previous_archiver[a.lower()]
            #print 'Attribute %s will use the same archiver %s'%(a,archiver)
            
            ##DEPRECATED BECAUSE THE API DOESN'T RESPECT THE EXECUTABLE NAME CASE
            ##try:
                ##server=api.API_getProxy(archiver).info().server_id
            ##except Exception,e:
                ##print e
            
            ##Adding to mod_hosts the server with not_running archivers
            #if not api.server_Ping(archiver):
                #if host not in mod_hosts: mod_hosts[host]={}
                #if server not in mod_hosts[host]: mod_hosts[host][server]=[]
                #print 'Archiver %s exists but is not running, it must be started!'%(archiver)
                #mod_hosts[host][server].append(archiver)
            #else:  
            ##Creating a new link archiver-attribute
            ##print 'Creating a new Archiver-Attribute link (%s) for %s'%(reg,a)
            #match = [arch for arch in archivers if re.match(reg,arch)]
            ##To an existing archiving server ... (could need a new archiver instance)
            #if match:
                #match.sort()#Getting the last of the matching archiver names
                #suitable = [m for m in match if archivers[m]<MAX_INSTANCE_LOAD]
                #lastmatch = re.match(reg,match[-1])
                ##With Full Load
                #if not suitable:
                #archiver='%s%02d'%(lastmatch.groups()[0],(int(lastmatch.groups()[-1])+1))
                ##print 'Archiver %s overloaded (%d), creating %s'%(lastmatch.group(),archivers[lastmatch.group()],archiver)
                ##Reusable
                #else: 
                #archiver=suitable[0]
                ##print 'Suitable archiver %s found for attribute %s,' % (str(archiver),a)
                #archivers[archiver]+=1
                ##DEPRECATED BECAUSE THE API DOESN'T RESPECT THE EXECUTABLE NAME CASE
                ##try:
                ##dp=PyTango.DeviceProxy(archiver if suitable else lastmatch.group())
                ##server=dp.info().server_id
                ##print 'Server name got from TangoDB : %s'%server
                ##except Exception,e:
                ###Using the name previously generated
                ##pass
            ##To a New Dedicated Archiver ...
            #elif dev in dedicated.keys():
                #archiver='%sArchiver/%s/%s-01'%('Hdb' if arch_type=='hdb' else 'Tdb',host,label)
                #server='%sArchiver/%s_%s'%('Hdb' if arch_type=='hdb' else 'Tdb',host,label)
            #else:
                #print 'No matching archiver has been found for %s'%a
        
            ##Adding to mod_hosts the server with new,not_running or dedicated archivers
            #if archiver not in archivers.keys() or not api.server_Ping(archiver) or dev in dedicated.keys():
                #if host not in mod_hosts: mod_hosts[host]={}
                #if server not in mod_hosts[host]: mod_hosts[host][server]=[]
                #if not (dev in dedicated.keys()): #The archiver is new or not running
                #if archiver not in archivers.keys(): #Creating new archivers if necessary
                    #print 'Creating new archiver %s on %s'%(archiver,server)
                    #di = DbDevInfo()
                    #di.name,di._class,di.server = archiver,'%sArchiver'%('Hdb' if arch_type=='hdb' else 'Tdb',),server
                    #api.db.add_device(di)
                    #archivers[archiver.lower()]=1
                    #archiver_properties[archiver.lower()]={}
                #else: #It means that server_Ping has failed! 
                    #print 'Archiver %s exists but is not running, it must be started!'%(archiver)
                #mod_hosts[host][server].append(archiver)
                #else:
                    #pass
                #pass
                
            #if dev in dedicated.keys():
                ##Update isDedicated and reservedAttributes properties if necessary
                ##print 'archiver_properties length is %d vs %d archivers'%(len(archiver_properties),len(archivers))
                #for arch,d in archiver_properties.items():
                #if arch==archiver.lower():
                    #if 'reservedAttributes' not in d: 
                    ##print 'Adding default Dedicated properties to archiver %s'%(arch)
                    #d['reservedAttributes']=[]
                    #if 'isDedicated' not in d or not d['isDedicated']: d['isDedicated']=['TRUE']
                    #if a not in d['reservedAttributes']:
                    #print 'Adding attribute %s to archiver %s'%(a,arch)
                    #d['reservedAttributes'].append(a)
                    #if archiver not in mod_hosts[host][server]: mod_hosts[host][server].append(archiver)
                    #if archiver not in mod_archivers: mod_archivers.append(archiver)
                    #else:
                    #print 'The attribute %s was already assigned to archiver %s; but it doesnt appear to be the actual archiver!!!'%(a,arch)
                    #if archiver not in mod_hosts[host][server]: mod_hosts[host][server].append(archiver)
                    #if archiver not in mod_archivers: mod_archivers.append(archiver)
                #elif 'reservedAttributes' in d and a in d['reservedAttributes']:
                    #print 'Removing attribute %s from archiver %s'%(a,arch)
                    #d['reservedAttributes'].remove(a)
                    ##@TODO: When host information for existing servers become readable
                    ## this lines must be changed.
                    #mod_hosts[host][server].append(arch)
                    #if arch not in mod_archivers: mod_archivers.append(arch)
                #elif ('reservedAttributes' not in d or not len(d['reservedAttributes'])) and 'isDedicated' in d and 'TRUE' in d['isDedicated']:
                    #print 'Removing isDedicated property from archiver %s'%(arch)
                    #d['reservedAttributes']=[]
                    #d['isDedicated']=['FALSE']
                    #mod_hosts[host][server].append(arch)
                    #if arch not in mod_archivers: mod_archivers.append(arch)
                ##Updating the modified properties
                #archiver_properties[arch]=d
                
            #pass #End of adding archivers
                    
            ##Checking TDB Properties
            #if arch_type.lower()=='tdb':
            #if archiver in archiver_properties.keys():
                #val = archiver_properties[archiver]
            #else: archiver_properties[archiver],val = {},{}
            #pathprops=['DbPath','DiaryPath','DsPath']
            #tdbpath='/tmp/archiving/tdb'
            #if any(p not in val or val[p][0]!=tdbpath for p in pathprops):
                #print 'Updating TdbPath properties of %s'%archiver
                #[archiver_properties[archiver].__setitem__(p,[tdbpath]) for p in pathprops]
                #if host not in mod_hosts: mod_hosts[host]={}
                #if server not in mod_hosts[host]: mod_hosts[host][server]=[]
                #if archiver not in mod_hosts[host][server]: mod_hosts[host][server].append(archiver)
                #if archiver not in mod_archivers: mod_archivers.append(archiver)
            #pass #End of checking each attribute archiver
            
        #print 'Updating properties of %d archivers ...'%(len(mod_archivers))
        #for archiver in mod_archivers:
            ##print 'Updating properties of %s: %s'%(archiver,str(archiver_properties[archiver]))
            #api.db.put_device_property(archiver,archiver_properties[archiver])
        #return mod_hosts
        pass