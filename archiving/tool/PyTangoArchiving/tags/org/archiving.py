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

from PyTangoArchiving import CommonAPI

import PyTango
import PyTango_utils as fandango
from fandango.objects import Object
from fandango.log import Logger
from fandango.servers import ServersDict
from fandango.db import FriendlyDB

from PyTangoArchiving import ARCHIVING_CLASSES,ARCHIVING_TYPES,MAX_SERVERS_FOR_CLASS,MIN_ARCHIVING_PERIOD
from dbs import ArchivingDB

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
                if o in modes.keys(): 
                    nmodes+=[o]+['%d'%int(n) for n in modes[o]]
            return ','.join(nmodes)
    def __repr__(self):
        if not self.modes and self.archiving_mode: self.extractModeString()
        return '%d:%s:%s:%s'%(self.ID,self.name,self.archiver,str(self.modes))    
        
        
class ArchivingAPI(CommonAPI):
    """ SubClass of PyTangoArchiving.CommonAPI that provides specific methods for Hdb/Tdb databases """
    def __init__(self,schema,host=None,user='browser',passwd='browser',classes=[],LogLevel='info',load=True):
        self.schema = schema.lower()
        assert self.schema in ['hdb','tdb'], 'UnknownSchema_%s'%schema
        CommonAPI.__init__(self,self.schema,host,user,passwd,classes=self.get_archiving_classes(),LogLevel=LogLevel)
        self.db = ArchivingDB(self.schema,self.host,self.user,self.passwd)
        if load: self.load_all()
    
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

    def get_reserved_attributes(self,archiver):
        properties = self.tango.get_device_property(archiver,['isDedicated','reservedAttributes'])
        if properties['isDedicated']:
            [self.reserved_attributes[archiver].add(p) for p in properties['reservedAttributes']]
        return list(self.reserved_attributes[archiver])
        
    def get_attribute_archivers(self,attribute_list=None):
        '''
        This method returns a dictionary {attribute:archiver}. 
        This information is acquired through the ArchivingManager device.
        Note: This method does the same function than ArchivingDB.loadAttrStatus; 
        but using the Java API instead of accessing the database.
        '''
        if not attribute_list: atts = sorted(self.attributes.keys())
        else: atts = sorted(attribute_list.keys() if isinstance(attribute_list,dict) else attribute_list)
        
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
            if a in dbattrs.keys():
                modes=v['modes']
                dbmodes=dbattrs[a].extractModeString()
                #print a,':',modes
                #print '\tDB:',dbmodes
                for k,v in modes.items():
                    if k not in dbmodes.keys(): continue
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

    def get_attribute_errors(self):
        # Launch GetErrorArchiversCurrent command on HdbArchivingWatcher and then 
        # use GetErrorsForArchiverCurrent(archiver) to get the list of failed attributes
        raise 'NotImplemented'       
        
    def get_attribute_failed(self,interval=24*3600,load=True):
        """ Return a dictionary with the attributes not updated in "interval".
        @argin load forces an update from the database
        """
        if load: self.load_all()
        return dict([(att,val) for att,val in sorted(self.attributes.items()) if val.archiving_mode and val.last_date<(time.time()-(interval))])
     
    def get_archiver_status(self,archiver):
        return self.proxies[archiver].command_inout('StateDetailed')     
    
    def check_modes(self,modes,min_periodic=0,min_absolute=.01,min_relative=1):
        """ modes must be a dictionary in the form: {'MODE_X':[params]} 
        This is Tango!!! ... so periods must be milliseconds!
        """
        porder = ['MODE_P','MODE_A','MODE_R','MODE_T','MODE_C','MODE_D','MODE_E']        
        arch_type = self.schema #'hdb' or 'tdb'
        min_periodic = min_periodic or (('MODE_R' in modes or 'MODE_A' in modes) and 30000) or 10000
        max_periodic = (('MODE_R' in modes or 'MODE_A' in modes) and 300000) or 60000
        new_modes = {}
        for mode,params in modes.items():
            mode = mode.upper()
            if mode not in porder:
                continue
            #Period correction ... with Log!
            #Converting the period to a number of milliseconds (multiplying by 1000 if period is below 10000)
            try: params[0]
            except: params = [params]
            if params[0]<(3600 if arch_type=='hdb' else 1000): #Minimum allowed period is 10000 ms; so it is not allowed to introduce a number of 1800(30min.)!?
                params[0]=params[0]*1000 #Periods must be between 15 and 3600
                self.log.info('%s Period corrected to %f milliseconds'%(mode,params[0]))
            if (mode=='MODE_R' or mode=='MODE_A') and params[0]<(10000 if arch_type=='hdb' else 1000):
                params[0]=(10000 if arch_type=='hdb' else 1000)
                self.log.info('MODE_R Period corrected to %f'%(params[0]))
            if mode=='MODE_R':
                if len(params)==1:
                    params.extend([min_relative,min_relative])
                    self.log.info('MODE_R Change range assigned to %s'%(params[1:]))
                for i in range(len(params)):
                    if not params[i]: 
                        params[i] = min_relative
                        self.log.info('MODE_R Change range corrected to %s'%(params[1:]))
            if mode=='MODE_A':
                if len(params)==1:
                    params.extend([min_absolute,min_absolute])
                    self.log.info('MODE_A Change range assigned to %s'%(params[1:]))
                for i in range(len(params)):
                    if not params[i]: 
                        params[i] = min_absolute
                        self.log.info('MODE_A Change range corrected to %s'%(params[1:]))
            if mode=='MODE_P':
                if min_periodic>params[0] or max_periodic<params[0]: 
                    params[0]= min((max((params[0],min_periodic)),max_periodic))
                    self.log.info('MODE_P Period corrected to %f'%(params[0]))                
                if len(params)>1: params = params[:1]
            new_modes[mode]=params            
        return new_modes
    
    def check_attributes_config(self,attributes,repair=False):
        """ It checks if real attribute configuration matches with the values inserted in the database adt table.
        param repair If True it forces an ALTER TABLE to repair the error
        @todo Now it only repairs AttrWriteType ; ArgType and AttrDataFormat should be also repaired.
        """
        self.log.debug('In DB_check_attributes_config(...)')
        repaired = 0
        if not isinstance(attributes,list): attributes = [attributes]
        if any(attr not in self.attributes or not hasattr(self.attributes[attr],'data_type') for attr in attributes):
            self.load_attribute_description('hdb')
        for attr in attributes:
            try:
                ap = PyTango.AttributeProxy(attr)
                ac = ap.get_config()
                ad = self.attributes[attr]
                self.log.debug('In DB_check_attributes_config(%s): AC vs ADT'%attr)
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
    
    #----------------------------------------------------------------------------------------------
    # DEDICATED ARCHIVING   
    
    def set_device_to_archiver(self,device,archiver):
        """ Reads the attribute list of a device and inserts its name into ReservedAttributes list 
        @todo attrs_list = ['/'.join([device,attr]) for attr in DeviceProxy(device).get_attribute_list()]
        @todo It should call set_archiver_dedicated(archiver,attrs_list) 
        """
        raise Exception,'NotImplemented!'
    
    def set_archiver_dedicated(self,archiver,attrs_list):
        """ Sets an existing archiver as dedicated; or creates a new one. 
        @todo For an existing archiver it should move not-dedicated attributes to other archivers
        @todo For a new archiver use : ServersDict().create_new_server('server/instance','class',['dev_name'])
        @todo Properties to initialize : {'IsDedicated':['True'],'ReservedAttributes':list(attr_names_list)}
        """
        raise Exception,'NotImplemented!'    
    
    #----------------------------------------------------------------------------------------------
    # DEPRECATED

    def get_attribute_values(self,attribute,start_date,stop_date=None,asHistoryBuffer=False):
        ''' THIS METHOD IS DEPRECATED ... USE PyTangoArchiving.Reader object instead
        def attr_getValues(self,attribute,start_date,stop_date=None):
        This method uses an H/TdbExtractor DeviceServer to get the values from the database.
        The format of values returned is [(epoch,value),]
        The flag 'asHistoryBuffer' forces to return the rawHistBuffer returned by the DS.
        '''
        raise Exception,'Deprecated_by_PyTangoArchiving.Reader.get_attr_values'
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

    def start_archiving(self,attribute_list,modes={'MODE_P':[60000]},action='start'):
        ''' def start_archiving(attribute_list):
        @argin modes {MODE_P:[1000],MODE_R:[1000,5,5],...}
        '''
        self.log.info('In %s_archiving(%s,%s)'%(action,attribute_list,modes))
        if isinstance(attribute_list,str): attribute_list=[attribute_list]
        elif not hasattr(attribute_list,'__len__'): attribute_list=[attribute_list]
        if not attribute_list: 
            self.log_error('%s_archiving(%s): The Attribute Names list is EMPTY!0'%(action,self.schema))
            return
        else: 
            self.log.info('In %s_archiving(%s): %d attribute names in the list'%(action,self.schema,len(attribute_list)))

        action = action.lower().strip()
        dp = self.get_manager()
        dp.set_timeout_millis(90000)
        #current_archived = self.get_archived_attributes()
        try:
            dp.ping()
            #Stop archiving of existing attributes
            self.log.debug('Checking if %s are %s archived ...'%(len(attribute_list)==1 and attribute_list or 'attributes',self.schema))#str(attribute_list))
            isArch = dp.command_inout('IsArchived%s'%self.schema.upper(),attribute_list)
            stoplist,archatts = [],[attribute_list[i] for i,v in enumerate(isArch) if v]
            
            #Checking Archivers to be running (To avoid SegFault in command_inout)
            for a in archatts:
                archiver = self.attributes.has_key(a) and self.attributes[a].archiver or ''
                if not archiver:
                    self.log.warning('ArchivingStop/Start for %s, its archiver is unknown! Launch DB.loatAttrDescription/Status first.'%a)
                    #attribute_list.remove(a)
                elif self.servers[self.servers.get_device_server(archiver)].ping() is None: 
                    self.log.warning('ArchivingStop/Start for %s, archiver %s is not running!'%(a,archiver))
                    #attribute_list.remove(a)
                else:
                    stoplist.append(a)
                        
            #Stopping archiving
            if stoplist:
                self.log.info('Stopping archiving for %s attribs ...'%(len(stoplist)<100 and stoplist or str(len(stoplist))))
                print 'Stopping archiving for %s attribs ...'%(len(stoplist)<100 and stoplist or str(len(stoplist)))
                dp.command_inout('ArchivingStop%s'%self.schema.upper(),stoplist)

            time.sleep(1.)
            #Starting archiving
            if attribute_list and action == 'start':
                archivedTogether='1'
                _modes=ArchivedAttribute().extractModeString(modes)
                if type(_modes) is not str: _modes = ArchivedAttribute().extractModeString(_modes)
                args = [archivedTogether,str(len(attribute_list))]+attribute_list+_modes.split(',')
                self.log.debug('Created atribute arguments: %s'%str(args))
                print 'Created atribute arguments ...'#: %s'%str(args)
                #Start archiving of attributes
                print 'Starting %s archiving for %s attributes ...'%(self.schema,len(attribute_list)<100 and attribute_list or str(len(attribute_list)))
                self.log.info('Launching: ArchivingManager.ArchivingStart%s(1,%d,%s,%s) ...'%\
                    (self.schema.upper(),len(attribute_list),len(attribute_list)<10 and attribute_list or '...',modes))
                #print 'ArchivingStartHdb',args
                if self.schema.lower()=='tdb':
                    #TDB_SPECs: ExportPeriod and KeepingWindowDuration
                    args = args + ['TDB_SPEC',str(int(10*60e3)),str(int(3600e3*24*3))]
                r = dp.command_inout('ArchivingStart%s'%self.schema.upper(),args) #It Worked!
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
    
    def stop_archiving(self,attribute_list): 
        '''def attr_StopArchiving(self,attribute_list,htype): 
        attribute_list list, htype = 'hdb'/'tdb'
        '''
        self.start_archiving(attribute_list,action='STOP')

    #----------------------------------------------------------------------------------------------
    # INFORMATION DIRECTLY READ FROM THE DATABASE
    
    def load_all(self,values=True):
        self.db.renewMySQLconnection()
        self.load_attribute_descriptions()
        self.load_attribute_properties()
        self.load_attribute_modes()
        if values: self.load_last_values(n=1)
        
    def load_attribute_descriptions(self):
        self.log.info('Loading attributes description from %s database ...'%self.schema)
        lines=self.db.get_attribute_descriptions()
        for line in lines:
            #print str(line)
            attribute,ID,data_type,data_format,writable=line
            if attribute not in self.attributes.keys():
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
                self.attributes[att.name].setArchiver(archiver,start_date,arch_mode)
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
        '''
        lines = []
        now = time.time()
        if attribute is None or type(attribute) in [list,set]:
            self.log.info('Getting last attributes values from %s database ...'%self.schema)
        elif attribute not in self.attributes.keys(): 
            self.log.error('The Attribute %s is not being Archived in %s database ...'%(attribute,self.schema))
        else: 
            self.log.debug('Getting last attributes values for %s from %s database ...'%(attribute,self.schema))
        if not self.attributes: 
            self.log.warning( 'attribute list is empty, try load_attributes_descriptions()')
            
        if attribute is None or type(attribute) in [list,set]:
            self.log.debug('Getting values for a list of attributes ...')
            for att in (type(attribute) in [list,set] and attribute or self.attributes.keys()):
                values = self.load_last_values(att, n)
                self.log.debug('Last values acquired for %s'%att)
                if values: lines=lines+list(values)
                elif att in self.attributes.keys() and self.attributes[att].archiver: 
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
                    if date<(now-3600) and self.attributes[attribute].archiving_mode: 
                        self.log.warning('The Attribute %s has not recorded new values since %s.'%(attribute,time.ctime(date)))
                elif self.attributes[attribute].archiving_mode:
                    self.log.error('No values has been found for attribute %s!'%attribute)
            except Exception,e:
                self.log.error('Exception while acquiring data from MySQL for attribute %s:%s'%(attribute,str(e)))
                self.attributes[attribute].exception='%s:%s'%(time.ctime(),str(e))
        return list(reversed(lines))
                
    def load_attribute_values(self,attribute,start_date,stop_date):
        if not self.attributes:
            self.log.warning( 'ERROR!: loadAttributesDescriptionFIRST!')
            return
        if type(start_date) is not str: start_date=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(start_date))
        if type(stop_date) is not str: stop_date=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(stop_date))   
        lines=self.db.get_attribute_values(self.attributes[attribute].table,start_date,stop_date)
        date=time.mktime(lines[0][0].timetuple())+1e-6*lines[0][0].microsecond
        self.attributes[attribute].setLastValue(lines[0][1],date)
        return lines         
