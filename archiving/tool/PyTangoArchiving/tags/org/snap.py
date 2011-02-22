#!/usr/bin/env python2.5
''' @if gnuheader
#############################################################################
##
## file :       object.py
##
## description : see below
##
## project :     Tango Control System
##
## $Author$
##
##
## $Revision$
##
## copyleft :    ALBA Synchrotron Controls Section, CELLS
##               Bellaterra
##               Spain
##
#############################################################################
##
## This file is part of Tango Control System
##
## Tango Control System is free software; you can redistribute it and/or
## modify it under the terms of the GNU General Public License as published
## by the Free Software Foundation; either version 3 of the License, or
## (at your option) any later version.
##
## Tango Control System is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
##
## You should have received a copy of the GNU General Public License
## along with this program; if not, see <http://www.gnu.org/licenses/>.
###########################################################################
@endif

@package PyTangoArchiving.snap

@htmlinclude SnapUsage.html

'''

# by Sergi Rubio Manrique, srubio@cells.es
# ALBA Synchrotron Control Group

import PyTango

import time,traceback,os,re,threading,datetime
from random import randrange

from tau.core.utils import Object,Singleton,CaselessDict

import PyTango_utils.log
import MySQLdb,sys
from PyTango_utils.db import FriendlyDB
from PyTangoArchiving.common import getSingletoneAPI

__all__ = ['snap_db','SnapDB','SnapAPI','SnapContext','Snapshot']

class SnapAPI(PyTango_utils.log.Logger,Singleton):
    """ This object encapsulates the methods used to manage the SnapArchiving System.
    An SnapDB object is needed to manage the persistency of MySQL connections and tango proxies.
    """
    
    __instantiated__ = 0
    TABLES={'context':['id_context','time','name','author','reason','description'],
        'ast':['ID','time','full_name','device','domain','family','member','att_name','data_type','data_format','writable','max_dim_x','max_dim_y','levelg','facility','archivable','substitute'],
        }
        
    def __init__(self,user='browser',passwd='browser',api=None,SINGLETON=True):
        """ The SnapAPI is a Singleton object; is instantiated only one time
        @param[in] api An ArchivingAPI object is required.
        """        
        print 'Creating SnapAPI object ...'
        if SINGLETON and self.__class__.__instantiated__: return
        
        self.call__init__(PyTango_utils.log.Logger,'SnapDict',format='%(levelname)-8s %(asctime)s %(name)s: %(message)s')
        if api is None:api=getSingletoneAPI(schema='snap')
        self.api=api
        self.manager=api.get_manager()
        self.archivers=dict([(a,api.proxies[a]) for a in api.servers.get_class_devices(api.ArchiverClass)])
        self.extractors=dict([(a,api.proxies[a]) for a in api.servers.get_class_devices(api.ExtractorClass)])        
        
        self.contexts={} #{ID:{'author','name','attributes':[]}}
        user,host = user.split('@') if '@' in user else (user,self.api.tango.get_class_property('SnapArchiver',['DbHost'])['DbHost'][0])
        print 'Using %s as Archiving Host ...' % host
        self.set_db_config(api,host,user,passwd)
        #self.db=SnapDB(api,host=host,user=user,passwd=passwd)        
        if SINGLETON: 
            SnapAPI.__singletone = self
        self.__class__.__instantiated__+=1
        
    def set_db_config(self,api,host,user,passwd):
        self.db=SnapDB(api,host=host,user=user,passwd=passwd)

    ## @name Get methods
    # @{
    
    def get_contexts(self,wildcard='*',update=True):
        """ Returns a dicitonary of loaded contexts; if the list was empty it loads all contexts with name matching argument ('*' allowed)
        @param wildcard The argument allows to filter wich contexts are loaded; '*' can be used for All. Lists and dict can be used also.
        """
        result = {}
        if update or not self.contexts: 
            assert wildcard,'SnapAPI_WildcardArgumentRequired'
            ids = self.load_contexts(wildcard)
        import re
        if wildcard == '*': 
            return self.contexts
        if wildcard in self.contexts: 
            return {wildcard:self.contexts[wildcard]}
        if '*' in wildcard and '.*' not in wildcard:
            wildcard = wildcard.replace('*','.*')
        [result.__setitem__(k,c) for k,c in self.contexts.items() if re.match(wildcard,c.name)]
        return result            
        
    def get_context(self,ID=None,name=None):
        """ Return a single context matching the given int(ID) or str(name)
        """
        assert name or ID, 'SnapAPI_get_context_ArgumentRequired'
        if not name and (type(ID) is int or type(ID) is str and ID.isdigit()):
            name = ID
            if int(name) in self.contexts: 
                return self.contexts[name]
            else: 
                name = int(name)
                cts = self.db.get_id_contexts(name)
                assert cts, 'SnapAPI_Context%sDoesntExist'%name
                return SnapContext(self,name,ct_info=cts[0])                 
        else: #Getting context by name
            name = name or ID
            ids = [k for k,c in self.contexts.items() if name.lower()==c.name.lower()]
            if not ids:
                ids = self.load_contexts(name)
            return self.contexts[ids[0]]

    def get_random_archiver(self):
        return self.archivers.values()[randrange(len(self.archivers))]
        
    def get_random_extractor(self):
        return self.extractors.values()[randrange(len(self.extractors))]
    
    ## @}
  
    ## @name Load methods
    # @{
    
    def load_contexts(self,wildcard):
        """ Loads a list of contexts from the database using a wildcard
        """
        if type(wildcard) is str: wildcard.replace('*','%')
        ids = self.db.get_context_ids(wildcard)
        [self.contexts.__setitem__(k,SnapContext(self,k)) for k in ids if k not in self.contexts]
        to_delete = [k for k in self.contexts if k not in ids]
        [self.contexts.pop(k) for k in to_delete]
        return ids
    
    def load_snapshots(self,wildcard):
        """ Loads a list of snapshots for a given context, using a wildcard"""
        pass

    ##@}
    
    ## @name Context/Snapshot creation
    # @{
    def check_attribute_allowed(self,attr,allow_exceptions=False):
        """ attr must be the full attribute name, it returns True if the snapshoting of the attribute is available. """
        try:     
            if isinstance(attr,PyTango.AttributeInfoEx) or isinstance(attr,PyTango.AttributeInfo):
                ac = attr
            else: 
                ac = PyTango.AttributeProxy(attr).get_config()
            if str(PyTango.ArgType.values[ac.data_type]) in ('DevBoolean','DevState','DevShort','DevLong','DevUShort','DevULong','DevDouble','DevFloat'):
                return True
            if str(PyTango.ArgType.values[ac.data_type]) == 'DevString' and ac.data_format == PyTango.SCALAR:
                return True
            else:
                ##The attribute is not supported by the actual release of the snapshoting system
                return False
        except Exception,e:
            print 'SnapAPI.check_attribute_allowed(%s): Exception trying to check attribute: %s'%(attr,str(e))
            return allow_exceptions or None
              
    def filter_attributes_allowed(self,attributes,allow_exceptions=False):
        """ It checks a list of attributes and returns which are allowed to snap. Full name of attribute is required."""
        return [attr for attr in attributes if self.check_attribute_allowed(attr,allow_exceptions)]

    def filter_device_attributes_allowed(self,device,allow_exceptions=False):
        """ It cheks all the attributes of a device. """
        goods = []
        try:
            alq = PyTango.DeviceProxy(device).attribute_list_query()
            [goods.append(device+'/'+attr.name) for attr in alq if self.check_attribute_allowed(attr,allow_exceptions)]
            return goods
        except Exception,e:
            print 'SnapAPI.filter_device_attributes_allowed(%s): Exception trying to check device: %s'%(device,str(e))
            return allow_exceptions and goods or []
        
       
    def create_context(self,author,name,reason,description,attributes):
        """ Creates a new context in the database
        @todo Before inserting a new context it should check if another one with same name exists!
        @return the created SnapContext object
        """
        date = time.strftime('%Y-%m-%d',time.localtime())        
        nattrs = str(len(attributes))
        print 'args to CreateNewContext with %d attributes are: %s'%(len(attributes),str([author,name,nattrs,date,reason,description]))
        self.manager.set_timeout_millis(60000)
        sid=self.manager.command_inout('CreateNewContext',[author,name,nattrs,date,reason,description]+[a.lower() for a in attributes])
        print 'Context created with id = %s'%sid
        self.db.renewMySQLconnection()
        #self.db.getCursor(renew=True)
        self.contexts[sid] = self.get_context(sid)
        attrs = self.contexts[sid].get_attributes().values()
        for att in attrs:
            if att['full_name'].lower() not in [a.strip().lower() for a in attributes]:
                self.warning('Unable to add %s attribute to context!'%att['full_name'])
        return self.contexts[sid]
    
    ## @}
    
    ## @name Argument conversion
    # @{
    
    def get_ctxlist_as_dict(self,author,name,date,reason,description,attributes):
        """ converts a list of arguments in a single dictionary """
        return {'author':author,'name':name,'date':date,'reason':reason,'description':description,'attributes':attributes}
    
    def get_dict_as_ctxlist(self,context):
        """ converts a dictionary in a list of arguments """
        return [context[k] for k in ['author','name','date','reason','description']]+context['attributes']
    
    ## @}
    
class SnapContext(object):
    """SnapContext refers to a set of attributes that are stored/reloaded together.
    """
    
    def __init__(self,api,ID,ct_info={},loadAll=True):
        self.api = api ##WARNING! ... this api is an SnapAPI object, not a CommonAPI
        self.db = api.db
        self.ID = ID
        self.get_info(ct_info)
        self.attributes = {}
        self.snapshots = {}
        self.tables = []
        if loadAll:
            self.get_attributes()
            if self.attributes:
                self.get_attributes_data()
                self.get_snapshots()
            if not ct_info:
                self.get_info()
                
    def __repr__(self):
        return 'SnapContext(%s,%s,%s,%s,Attributes[%d],Snapshots[%d])'%\
            (self.ID,self.name,self.author,self.reason,len(self.attributes),len(self.snapshots))

    ## @name Get methods
    # @{
    
    def get_info(self,ct_info={}):
        if not ct_info:
            cts = self.db.get_id_contexts(self.ID)
            ct_info = cts[0] if cts else None
        self.time = ct_info['time'] if ct_info else 0.
        self.name = ct_info['name'] if ct_info else ''
        self.author = ct_info['author'] if ct_info else ''
        self.reason = ct_info['reason'] if ct_info else ''
        self.description = ct_info['description'] if ct_info else ''
        return ct_info
    
    def get_attributes(self):
        if not self.attributes:
            [self.attributes.__setitem__(line[0],line[1]) for line in self.db.get_context_attributes(self.ID)]
        return self.attributes
        
    def get_attributes_data(self):
        if not self.attributes: self.get_attributes()
        if self.attributes:
            self.attributes = self.db.get_attributes_data(self.attributes.keys())
            self.tables = self.db.get_attributes_tables(self.attributes.values())
        return self.attributes
           
    def get_snapshots(self, date=None, latest=0):
        """ Returns a dict with {SnapID:(time,comment)} values; it performs a Database Call!
        @date load values FROM this date
        @latest load only this amount of values
        @return By default all snapshots are returned.
        @todo latest argument must be used to query only the latest values from the database
        """      
        #print 'In SnapContext.get_snapshots(%s,%s)'%(date,latest)
        kwargs = {'context_id':self.ID}
        if date:
            if type(date) in [int,float]: date = datetime.datetime.fromtimestamp(date)
            last_date = max([s[0] for s in self.snapshots]) if self.snapshots else datetime.datetime.fromtimestamp(0)
            kwargs['dates']=(last_date,date)
        if latest: 
            kwargs['limit']=latest
            
        result = self.db.get_context_snapshots(**kwargs)
        #print '%d snapshots read from database'%len(result)
        for element in result: #Each element is an (ID,time,Comment) tuple
            self.snapshots[element[0]]=element[1:]
        return self.snapshots#[self.snapshots[el[0]] for el in result]
        
        ## @todo SnapExtractor should be used instead of direct attack to the DB
        #try:
            #self.snapshots = self.api.get_random_extractor().command_inout('GetSnapsForContext',self.ID)
            #raise Exception,'GetSnapsForContext_ResultNotManaged'
        #except Exception,e:
            #print 'SnapContext.get_snapshots: SnapExtractor.GetSnapsForContext failed!: %s' % e
    
    def get_snapshot(self,snapid=None):
        """Returns an Snapshot dictionary with succesfully read attributes.
        @param snapid The long ID of the snapshot to return
        @return An Snapshot object
        """
        #print 'In SnapContext.get_snapshot(%s)'%snapid
        if snapid is None:
            if not self.snapshots: self.get_snapshots()
            snapid = sorted(self.snapshots.keys())[-1]
        if snapid not in self.snapshots: 
            print '.get_snapshot(%d): Unknown snap_id, loading from db ...'%snapid
            self.get_snapshots()            
            if snapid not in self.snapshots: 
                raise Exception,'SnapIDNotFoundInThisContext!(%d)'
        date,comment = self.snapshots[snapid]
        attributes = self.db.get_snapshot_attributes(snapid,self.tables).values()   
        if not attributes:
            self.api.warning('SnapContext.get_snapshot(%d): The attribute list is empty!'%snapid)
        #print 'attributes as returned from db are: %s' % attributes
        attr_names = [self.attributes[a['id_att']]['full_name'] for a in attributes]
        attr_values = []
        for a in attributes:
            values = [a['value'],] if 'value' in a else [a['read_value'],a['write_value']]
            for i,value in enumerate(values):
                if type(value) is not float:
                    if not self.attributes: self.get_attributes_data()
                    if self.attributes[a['id_att']]['data_format'] is PyTango.SPECTRUM:
                        value = value.split(',')
                        #if self.attributes[a['id_att']]['data_type'] in (PyTango.DevString,PyTango.DevBoolean,PyTango.DevState):
                            #@todo
                        if self.attributes[a['id_att']]['data_type'] in (PyTango.DevFloat,PyTango.DevDouble):
                            try: value = [float(v) for v in value]
                            except: pass
                        if self.attributes[a['id_att']]['data_type'] in (PyTango.DevShort,PyTango.DevUShort,PyTango.DevLong,PyTango.DevULong):
                            try: value = [int(v) for v in value]
                            except: pass                        
                    if self.attributes[a['id_att']]['data_format'] is PyTango.IMAGE:
                        ##@todo ... It must be crosschecked what to do for images!
                        pass
                values[i] = value
            attr_values.append(tuple(values))
        return Snapshot(snapid,time.mktime(date.timetuple()),zip(attr_names,attr_values),comment)
            
        ## @todo SnapExtractor should be used instead of direct attack to the DB
        #try:
            #attributes = self.api.get_random_extractor().command_inout('GetSnap',snapid)
            #raise Exception,'GetSnap_ResultNotManaged'
        #except Exception,e:
            #print 'SnapContext.get_snapshot: SnapExtractor.GetSnap failed!: %s' % e

    def get_snapshot_by_date(self,date,update=True):
        """ It returns the ID of the last snapshot in db with timestamp below date; 
        @param date It can be datetime or time epoch, date=-1 returns the last snapshot; date=0 or None returns the first
        """
        #print 'In SnapContext.get_snapshot_by_date(%s)'%date
        if update: 
            self.get_snapshots(\
                date=(None if date in (0,-1) else date),\
                latest=(1 if date==-1 else 0))
        
        if not self.snapshots: 
            self.api.debug("There's no snapshots to search for!")
            #print "There's no snapshots to search for!"
            return None
        
        keys = sorted(self.snapshots.keys())
        #Checking limits
        if not date: 
            return self.get_snapshot(keys[0])
        if date==-1:
            return self.get_snapshot(keys[-1])
        #Checking dates
        if type(date) in [int,float]: 
            date = datetime.datetime.fromtimestamp(date)   
        
        if date>self.snapshots[keys[-1]][0]:
            return self.get_snapshot(keys[-1])
        elif date<self.snapshots[keys[0]][0]: 
            #print 'No matching snapshot found'
            return None #
        else: 
            for i in range(keys-1):
                if self.snapshots[keys[i]][0]<=date<self.snapshots[keys[i+1]][0]:
                    return self.get_snapshot(keys[i])
            self.api.warning('No matching snapshot found for date = %s'%date)
            print 'No matching snapshot found for date = %s'%date
            return None
            
    # @}
    #--------------------------------------------------------------------------------------
    
    def take_snapshot(self,comment='',archiver=None):
        """ Executes an snapshot for the given context; it could be a context name or ID
        @param comment Text to be inserted in the database.
        """
        print 'In SnapContext.take_snapshot(%s,%s)'%(comment,None)
        try:
            if not self.snapshots:
                last_snap = self.get_snapshot_by_date(-1,update=True)
                if last_snap: last_snap = last_snap.ID
            else:
                last_snap = sorted(self.snapshots)[-1]
                
            if archiver and isinstance(archiver,str): 
                try:
                    if archiver in self.api.archivers: archiver = self.api.archivers[archiver]
                    else: archiver = PyTango.DeviceProxy(archiver)#self.api.get_random_archiver()
                except: archiver=None
                
            if not archiver and self.api.manager:
                self.api.debug('Trying to use SnapManager.LaunchSnapshot (java archiving release>1.4)')
                try:
                    if 'launchsnapshot' in [cmd.cmd_name.strip().lower() for cmd in self.api.manager.command_list_query()]:
                        archiver = self.api.manager
                    else: archiver = None
                except: archiver = None
                
            if not archiver: 
                self.api.debug('Getting a random archiver (java archiving release<1.4)')
                archiver = self.api.get_random_archiver()
                
            print 'Executing LauncSnapshot command in archiver %s'%archiver.name()
            
            archiver.set_timeout_millis(60000)
            result = archiver.command_inout('LaunchSnapShot',self.ID)
            
            ## In some future release (archiving 1.5) the manager will be used to launch the snapshots
            #self.api.manager.command_inout('LaunchSnapShot',self.ID)
            #self.db.renewMySQLconnection()            
            
            snapshot = self.get_snapshot_by_date(-1,update=True)            
            if (last_snap and last_snap == snapshot.ID):
                self.api.error('Launch Snapshot has not added a new entry in the database!')
                raise Exception,'LaunchSnapshotFailed'                           
            if snapshot is None:
                self.api.error('Snapshots table has no values for this context!')
                raise Exception,'NoSnapshotsFound'
            if comment:
                self.api.manager.command_inout('UpdateSnapComment',[[snapshot.ID],[comment]])
                #snapshot.comment = comment
                self.snapshots[snapshot.ID][1] = comment
                #self.get_snapshots()
            #return result #this is not the snapid, I dont know what it is
            return snapshot
        except Exception,e:
            self.api.error('Exception in LaunchSnapshot: %s'%str(traceback.format_exc()))
            return None
    
    #def reload_snapshot(self, key=None):
        #"""Writes snapshot for date to attributes. Shorthand for get_snapshot(before=date).write()"""
        #if type(key) in [int,long] and key not in self.snapshots: self.get_snapshots()
        
        #self.api.manager.command_inout('SetEquipmentsWithSnapshot')
        #return True

class Snapshot(CaselessDict):
    """Snapshot refers to a number of values taken at the same point in time.
    Snapshots with a comment set will have d.comment = "Comment" and 
    a date d.date = time.ctime(), the attributes will be accessible by []    
    """
    def __init__(self,ID,time_,values,comment=''):
        self.ID = ID
        self.time = time_
        self.comment = comment
        CaselessDict(self)
        self.update(dict(values))
        
    def __repr__(self):
        return 'Snapshot(%s,%s,%s,{%s})'%(self.ID,time.ctime(self.time),self.comment,','.join(self.keys()))
    #def __init__(self,attr_list = [],time_=time.time(),comment=''):
        #self.attr_list = attr_list
        #self.date = time_
        #self.comment = comment
    pass

class SnapDB(FriendlyDB,Singleton):
    """
    This class simplifies the direct access to the Snapshot database.
    tau.core.utils.Singleton forbids to create multiple SnapDB instances.
    
    TABLES STRUCTURE: 
    <pre>
    [snap.db.getTableCols(t) for t in snap.db.getTables()] =
    {'ast': ['ID',
        'time',
        'full_name',
        'device',
        'domain',
        'family',
        'member',
        'att_name',
        'data_type',
        'data_format',
        'writable',
        'max_dim_x',
        'max_dim_y',
        'levelg',
        'facility',
        'archivable',
        'substitute'],
    'context': ['id_context', 'time', 'name', 'author', 'reason', 'description'],
    'list': ['id_context', 'id_att'],
    'snapshot': ['id_snap', 'id_context', 'time', 'snap_comment'],
    't_im_1val': ['id_snap', 'id_att', 'dim_x', 'dim_y', 'value'],
    't_im_2val': ['id_snap',
                'id_att',
                'dim_x',
                'dim_y',
                'read_value',
                'write_value'],
    't_sc_num_1val': ['id_snap', 'id_att', 'value'],
    't_sc_num_2val': ['id_snap', 'id_att', 'read_value', 'write_value'],
    't_sc_str_1val': ['id_snap', 'id_att', 'value'],
    't_sc_str_2val': ['id_snap', 'id_att', 'read_value', 'write_value'],
    't_sp_1val': ['id_snap', 'id_att', 'dim_x', 'value'],
    't_sp_2val': ['id_snap', 'id_att', 'dim_x', 'read_value', 'write_value']}
    </pre>
    """
    def __init__(self,*p,**k):
        """method used to override FriendlyDB.__init__"""
        pass
    
    def init_single(self,api=None,host='',user='',passwd='',db_name='snap'):
        """SnapDB Singleton creator
        @param api it links the SnapDB object to its associated SnapAPI (unneeded?)
        """
        print 'Creating SnapDB object ... (%s,%s,%s,%s,%s)' % (api,host,user,passwd,db_name)
        self._api=api if api else None #getSingletoneAPI()
        FriendlyDB.__init__(self,db_name,host or (api and api.arch_host) or 'localhost',user,passwd)
        assert hasattr(self,'db'),'SnapDB_UnableToCreateConnection'
        [self.getTableCols(t) for t in self.getTables()]
        
    ## @name Context methods
    # @{
    
    def search_context(self,id_context=None,clause=''):
        """ Get all elements from context table
        @return A dictionary with all context table columns is returned
        """
        if not id_context:
            clause1=''
        elif type(id_context) is int:
            clause1 = 'context.id_context=%d'%id_context
        elif type(id_context) is str:
            clause1 = "context.name like '%s'"%id_context
        elif type(id_context) is dict:
            clause1=''
            for i in range(len(id_context)):
                k,v = id_context.items()[i]
                clause1+= "%s like '%s'"%(k,v) if type(v) is str else '%s=%s'%(k,str(v))
                if (i+1)<len(id_context): clause1+=" and "
        else:
            raise Exception("SnapDB_getContextList_ArgTypeNotSuported")

        return self.Select('*','context',clause1,asDict=True)
        
    def get_context_ids(self,context):
        """returns all the context IDs with a name matching the given string or regular expression"""
        if '*' in context: #It is a regular expression; comparing with all context names!
            #WARNING! ... it should be compared with a pre-loaded list of contexts! ... at SnapAPI
            if '.*' not in context: context = context.replace('*','.*')
            els = self.Select('name,id_context','context')
            ids = [l[1] for l in els if re.match(context,l[0])]
            return ids
        else:    #It is a normal SQL clause
            return self.Select('id_context','context',"name like '%s'"%context)[0]
    
    def get_context_snapshots(self,context_id,dates=None,limit=0):
        """ 
        @remarks It returns newest snapshot first! 
        """
        cols = ['id_snap','time','snap_comment']
        clause = 'id_context=%d' % context_id
        if dates: clause+=' AND time BETWEEN %s AND %s'%dates
        self.db.commit() ## @remark it forces a db.commit() to be sure that last inserted values are there
        result = self.Select(cols,'snapshot',clause,order='time desc',limit=limit)
        return result
    
    def get_id_contexts(self,id_context):
        return self.Select('*','context','id_context=%d'%id_context,asDict=True)
    
    def get_context_attributes(self,context):
        """ Gets a list of ID,attribute_name pairs for a given context
        @param context it can be either an int or an string, contexts can be searched by ID or name
        """
        if type(context) in [int,long]: 
            clause1='context.id_context=%d'%context
        elif type(context) is str:
            clause1="context.name like '%s'"%context
        return self.Select('ast.ID,ast.full_name','ast,list,context',
            [clause1,'context.id_context=list.id_context','list.id_att=ast.ID']
            ,distinct=True)    
            
    ## @}
    #------------------------------------------------------------------------------------------------
    
    ## @name Attribute methods
    # @{
        
    def search_attribute(self,att_id=None):
        """ Get a list of registered attributes matching the given ID or wildcard
        @param att_id: if it is an int is used as an unique ID, if it is an string is compared with attribute names using Sql wildcard '%'
        @return 
        """
        if not att_id:
            clause1=''
        elif type(att_id) is int:
            clause1 = 'ast.ID=%d'%att_id
        elif type(att_id) is str:
            clause1 = clause1.replace('*','%')
            clause1 = "ast.full_name like '%s'"%att_id
        else:
            raise Exception("SnapDB_getAttributesList_ArgTypeNotSuported")
        
        return self.Select('*','ast',clause1,asDict=True)        
        
    def get_attribute_ids(self,attribute):
        return self.Select('ID','ast',"full_name like '%s'"%attribute)[0]
    
    def get_attribute_contexts(self,attribute):
        return self.Select('context.id_context',['ass','context','list'],
            ["ast.full_name like '%s'"%attribute,'context.id_context=list.id_context','list.id_att=ast.ID'])
    
    def get_attributes_data(self,attr_id):
        """ For a given ID it retrieves attribute name, type and dimensions from the database
        @param attr_id could be either a single ID or a list of attribute IDs
        @return an {AttID:{full_name,Type,Writable,max_x,max_y}} dict
        """
        if not attr_id: raise Exception,'SnapDB_EmptyArgument!'
        if type(attr_id) is not list: attr_id = [attr_id]
        attrs = ','.join(['%d'%a for a in attr_id])
        values = self.Select(['ID','full_name','data_type','data_format','writable','max_dim_x','max_dim_y'],'ast','ID in (%s)'%attrs,asDict=True)
        result = {}
        for data in values:
            result[data['ID']] = {'full_name':data['full_name'],
                'data_format':PyTango.AttrDataFormat.values[data['data_format']],
                'data_type':PyTango.ArgType.values[data['data_type']],
                'writable':PyTango.AttrWriteType.values[data['writable']],
                'max_dim_x':data['max_dim_x'],'max_dim_y':data['max_dim_y']
                }
        return result
        
    def get_attributes_tables(self,attributes_data):
        """ Returns the list of tables to be read for the given attributes
        @param attributes_data should be a list of dictionaries containing data_type,writable,max_dim_x,max_dim_y keys
        Value tables are:
        {'t_im_1val': ['id_snap', 'id_att', 'dim_x', 'dim_y', 'value'],
        't_im_2val': ['id_snap', 'id_att', 'dim_x', 'dim_y', 'read_value', 'write_value'],
        't_sc_num_1val': ['id_snap', 'id_att', 'value'],
        't_sc_num_2val': ['id_snap', 'id_att', 'read_value', 'write_value'],
        't_sc_str_1val': ['id_snap', 'id_att', 'value'],
        't_sc_str_2val': ['id_snap', 'id_att', 'read_value', 'write_value'],
        't_sp_1val': ['id_snap', 'id_att', 'dim_x', 'value'],
        't_sp_2val': ['id_snap', 'id_att', 'dim_x', 'read_value', 'write_value']}
        """
        result = []
        for attr in attributes_data:
            if attr['data_format'] == PyTango.AttrDataFormat.IMAGE:
                if PyTango.AttrWriteType.values[attr['writable']]==PyTango.AttrWriteType.READ:
                    result.append('t_im_1val')
                else: result.append('t_im_2val')
            elif attr['data_format'] == PyTango.AttrDataFormat.SPECTRUM:
                if PyTango.AttrWriteType.values[attr['writable']]==PyTango.AttrWriteType.READ:
                    result.append('t_sp_1val')
                else: result.append('t_sp_2val')
            elif PyTango.ArgType.values[attr['data_type']]==PyTango.ArgType.DevString:
                if PyTango.AttrWriteType.values[attr['writable']]==PyTango.AttrWriteType.READ:
                    result.append('t_sc_str_1val')
                else: result.append('t_sc_str_2val')
            elif PyTango.AttrWriteType.values[attr['writable']]==PyTango.AttrWriteType.READ:
                result.append('t_sc_num_1val')
            else: result.append('t_sc_num_2val')
        return result
   
    def get_attribute_snapshots(self,attribute,context):
        pass
    
    ## @}
    #------------------------------------------------------------------------------------------------
    
    ## @name Snapshot methods
    # @{
    
    def get_snapshot_attributes(self,snapid,tables=[]):
        """ For a given snapid it returns all values found in the database
        @param snapid Snapshot ID
        @param tables If allows to restrict tables to be searched, all tables by default
        @return It returns {AttID:{value,read_value,write_value}}
        """        
        values = {}
        tables = tables or [t for t in self.tables if t.startswith('t_')]
        for table in tables:
            data = self.Select(self.tables[table],table,'id_snap=%d'%snapid,asDict=True)
            for d in data:
                values[d['id_att']]=d
        return values
            
    ## @}
    #------------------------------------------------------------------------------------------------
    
    ## @name Remove methods
    # @{
    
    def remove_context(self,id_context):
        """ 
        * delete a context from the Database:
            - Select the id_context.
            - Select all the attributes ids from list table.
            - Get all snapshots ids from snapshot table.
            * deleteSnapshot from the Database:    
            - Delete from list where id_context in the list.
            - Delete from context where id_context in the list.
            - Delete from ast if ID in atts_list and not in id_att from list.
        """
        self.info( 'In SnapDB.remove_context(%s)'%id_context)
        if not self.db:
            self.__initMySQLconnection()
        self.db.autocommit(False)
        try:
            q = self.db.cursor()
            q.execute('SELECT id_att from list where id_context=%d'%id_context)
            att_ids=q.fetchall()
            if not att_ids:
                print 'Unknown Context Id %s'%id_context
                self.error( 'ERROR: Unknown Context Id!')
                return
            att_ids=[a[0] for a in att_ids if a]
            self.info( 'listing snaps')
            q.execute('SELECT id_snap from snapshot where id_context=%d'%id_context)
            snap_ids=q.fetchall()
            snap_ids=[a[0] for a in snap_ids if a]
            for snap in snap_ids:
                if not self.remove_snapshot(snap): 
                    raise Exception,'Exception removing snapshot!'
            self.info( 'deleting from list')
            q.execute('DELETE FROM list WHERE id_context=%d'%id_context)
            self.info( 'deleting from context'    )
            q.execute('DELETE FROM context WHERE id_context=%d'%id_context)
            
            ##q.execute('SELECT id_att FROM list')
            ##att_ids=q.fetchall()
            ##att_ids=','.join(['%d'%a[0] for a in att_ids if a])
            ###print '%d attributes in snap database'%len(att_ids)
            ##q.execute('SELECT full_name FROM ast where ID not in (%s)'%att_ids)
            #q.execute('SELECT full_name FROM ast where ID not in (SELECT id_att FROM list)')
            #bad_att_ids=','.join(['%s'%str(a[0]) for a in q.fetchall() if a])
            #self.info('Attributes to delete are %s'%bad_att_ids)
            #q.execute('DELETE FROM ast WHERE ID NOT IN (%s)'%att_ids)
            self.db.commit()
            self.info( 'Context %d erased from database'%id_context)
            return True
        except Exception,e:
            self.error( 'Exception in query: ',str(e))
            self.db.rollback()
            return False
            
    def remove_empty_contexts(self):
        """ @todo search contexts with no snapshots and no attributes and removed
        """
        raise Exception,'SnapAPI_NotImplemented'
        
    def remove_snapshot(self,id_snap):
        """
        - Delete from all value tables where snapshot id is in the list.
            - DON'T USE ATTRIBUTE ID TO DELETE, COULD BE USED IN MORE CONTEXTS!
                - It will be verified at the end ...
            - Delete from snapshot where id_snap in the list.
        """
        self.info( 'In DB_removeSnapshot(%s)'%id_snap)
        if not self.db:
            self.__initMySQLconnection()
        self.db.autocommit(False)
        try:
            q = self.db.cursor()
            for table in ['t_im_1val','t_im_2val','t_sc_num_1val','t_sc_num_2val',
                    't_sc_str_1val','t_sc_str_2val','t_sp_1val','t_sp_2val','snapshot']:
                q.execute('DELETE FROM %s WHERE id_snap=%d'%(table,id_snap))
            self.db.commit()
            self.info( 'Snapshot %d erased from database'%id_snap)
            return True
        except Exception,e:
            self.error( 'Exception in DB_removeSnapshot query: %s'%str(e))
            self.db.rollback()
            return False
        
    ## @}