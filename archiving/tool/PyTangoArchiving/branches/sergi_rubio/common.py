# CLASS FOR TANGO ARCHIVING MANAGEMENT
# by Sergi Rubio Manrique, srubio@cells.es
# ALBA Synchrotron Control Group
# 16 June 2007

import PyTango
#from PyTango import *
import time
import traceback
import os
import threading
import datetime
from random import randrange

import PyTangoArchiving
from PyTango_utils.dicts import CaselessDict,CaselessDefaultDict
from PyTango_utils.servers import ServersDict
from PyTangoArchiving import ARCHIVING_CLASSES,ARCHIVING_TYPES,MAX_SERVERS_FOR_CLASS,MIN_ARCHIVING_PERIOD
from PyTangoArchiving.utils import *

USE_TAU = False
if USE_TAU:
    from tau.core.utils import Object,Logger
    from tau import Database
else:
    from PyTango import Database
    from PyTango_utils.log import Logger
    from PyTango_utils.objects import Object
    
    
def int2DevState(n): return str(PyTango.DevState.values[n])    
def int2DevType(n): return str(PyTango.ArgType.values[n])    

class CommonAPI(Object):
    """ This class provides common methods for managing a Soleil-like database (for either archiving or snapshoting)
    The methods starting by "get" retrieve values using ArchivingDSs
    The methods starting by "load" access directly to MySQL database
    """
    singletone=None
    
    #ArchivingTypes = ARCHIVING_TYPES
    #ArchivingClasses = ARCHIVING_CLASSES
    
    MAX_SERVERS_FOR_CLASS=5
    MIN_ARCHIVING_PERIOD=10
    
    def __init__(self,schema,host=None,user='browser',passwd='browser',classes=[],LogLevel='info'):
        """
        """
        self.log = Logger('ArchivingAPI(%s)'%schema,format='%(levelname)-8s %(asctime)s %(name)s: %(message)s')
        self.log.info('Logger streams initialized (error,warning,info,debug)')
        self.log.setLogLevel(LogLevel)

        self.tango = Database() #access to Tango database
        self.api = self #getSingletoneApi('hdb')#self #Hook use for portability of some methods
        
        self.schema = str(schema).lower()
        self.user,self.passwd = user,passwd
        
        if host is None:
           self.host = self.tango.get_class_property('HdbArchiver',['DbHost'])['DbHost'][0]
            #if 'TANGO_HOST' in os.environ:
            #    self.host=os.environ['TANGO_HOST'].split(':')[0]
        else: self.host=host
        
        self.dbs={} #pointers to Archiving databases
        
        self.ArchivingClasses = classes or self.get_archiving_classes()
        self.servers = ServersDict()
        [self.servers.load_by_name(k) for k in self.ArchivingClasses]
        self.proxies = self.servers.proxies
        self.ArchiverClass = (k for k in self.ArchivingClasses if 'Archiver' in k).next()
        self.ManagerClass = (k for k in self.ArchivingClasses if 'Manager' in k).next()
        self.ExtractorClass = (k for k in self.ArchivingClasses if 'Extractor' in k).next()
        try: self.WatcherClass = (k for k in self.ArchivingClasses if 'Watcher' in k).next()
        except: self.WatcherClass = None
        
        self.loads=CaselessDefaultDict(lambda k:0) #a dict with the archiving load for each device
        self.attributes=CaselessDict() #a dict of ArchivedAttribute objects
        self.reserved_attributes = CaselessDefaultDict(lambda k:set()) #Dictionary for keeping the attributes reserved for each archiver
        self.servers.refresh()
        
    def get_archiving_classes(self):        
        self.ArchivingClasses = [k for k in ARCHIVING_CLASSES if self.schema in k.lower()]
        if self.schema!='snap': self.ArchivingClasses.append('ArchivingManager')
        return self.ArchivingClasses

    def __del__(self):
        self.log.debug( 'Deleting ArchivingAPI ...')
        for p in self.proxies.values():
            del p
        del self.tango
        for d in self.dbs.values():
            del d
            
    def __repr__(self):
        '''def server_Report(self): The status of Archiving device servers '''
        report='The status of %s Archiving device servers is:\n'%self.schema
        for k,v in self.servers.items():
            report+='%s:\t%s\n'%(k,v.state)
        if self.WatcherClass:
            try: report+=self.proxies(self.servers.get_class_devices(self.WatcherClass)[0]).command_inout('GetReportCurrent')+'\n'
            except: pass
        self.log.debug(report)
        return report         

    ####################################################################################################

    def get_random_device(self,klass,timeout=300000):
        device = None
        remaining = self.servers.get_class_devices(klass)
        while remaining: #for i in range(len(self.extractors)):
            next = randrange(len(remaining))
            devname = remaining.pop(next)
            device = self.servers.proxies[devname]
            try:
                device.ping()
                device.set_timeout_millis(timeout)
                break
            except Exception,e: 
                self.debug('%s unreachable: %s'%(devname,str(e)))
        return device

    def get_manager(self):
        ''' returns a DeviceProxy object '''
        return self.get_random_device(self.ManagerClass)
    
    def get_extractor(self):
        ''' returns a DeviceProxy object '''
        return self.get_random_device(self.ExtractorClass)  
    
    def get_extractors(self):
        ''' returns a list of device names '''
        return self.servers.get_class_devices(self.ExtractorClass)  
    
    def get_archiver(self,archiver=''):
        ''' returns a DeviceProxy object '''
        if archiver: return self.proxies[archiver]
        else: return self.get_random_device(self.ArchiverClass)
    
    def get_archivers(self):
        ''' returns a list of device names '''
        return self.servers.get_class_devices(self.ArchiverClass)
    
    def get_watcher(self):
        ''' returns a DeviceProxy object '''
        if not self.WatcherClass: return None
        else: return self.get_random_device(self.WatcherClass) 
              
    ####################################################################################################            

def getSingletoneAPI(*args,**kwargs):
    if not CommonAPI.singletone:
        print 'Creating CommonAPI singletone object ...'
        CommonAPI.singletone=CommonAPI(*args,**kwargs)
    return CommonAPI.singletone
