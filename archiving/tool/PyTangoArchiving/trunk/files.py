# by Sergi Rubio Manrique, srubio@cells.es
# ALBA Synchrotron Control Group

import time,os,re,traceback
from collections import defaultdict
from xml.dom import minidom
from os.path import abspath

import PyTango
from fandango.arrays import CSVArray
import fandango.functional as fun

import PyTangoArchiving
import PyTangoArchiving.utils as utils

ARCHIVING_CONFIGS =  os.environ.get('ARCHIVING_CONFIGS','/data/Archiving')
RESULT = None # Last result of each test is stored for convenience

def GetConfigFiles(folder=ARCHIVING_CONFIGS,mask='.*.csv'):
    print 'In GetConfigFiles(%s,%s)' % (folder,mask)
    return ['%s/%s'%(folder,f) for f in os.listdir(folder) if fun.matchCl(fun.toRegexp(mask),f)]

def getAPI(schema):
    """ 
    :param schema: hdb/tdb
    """
    api = PyTangoArchiving.ArchivingAPI(schema,load=False) #lightweight api
    api.load_attribute_descriptions()
    api.load_attribute_modes()    
    return api

def LoadArchivingConfiguration(filename, schema,
        launch=False,force=False,stop=False,
        dedicated=False, check=True, overwrite=True, centralized=None,
        failed=None,unavailable=None,hosts=None,
        api=None,filters=None,exclude=None):
    ''' This is the function used to load an archiving configuration from a CSV file and start archiving. 
    **NOTE**: The function must be called separately for each archiving type LoadArchivingConfiguration(..,schema='hdb' or 'tdb')
    
    - There's a single mandatory argument:
        :param filename:
        :param schema: HDB or TDB; the objects that do not match the schema will be discarded; it forces the script to be called separately for TDB and HDB
    
    - Several flags help to control the process:
        :param launch: the configuration will be launched or simply reviewed; False by default
        :param force: if forced then all the errors are summarized at the end, if not configuration is interrumpted at first error; False by default
        :param overwrite: whether the attributes already archived are modified or not; True by default
        :param check: it pruns all attributes not available
        :param stop: the configuration will be used to STOP the archiving instead of starting it; False by default
        :param dedicated: the attributes will be assigned to specific host archivers instead of using generic ones; False by default
        :param centralized: the archivers created will be started in the central server instead of distributed
    
    - Other arguments help to control where the information is stored
        :param failed: attributes that couldn't be configured
        :param hosts: {hosts:[attributes]} dictionary
        :param unavailable: list of unavailable attributes
        :param api: ArchivingAPI object to use
    
    :return: A dictionary with {modes:[attributes]} is returned
    '''
    failed,unavailable,hosts = fun.notNone(failed,[]),fun.notNone(unavailable,[]),fun.notNone(hosts,defaultdict(list))
    centralized = centralized if centralized is not None else (True if schema.lower().strip()=='tdb' else False)
    filters,exclude = fun.notNone(filters,{}),fun.notNone(exclude,{})
    tstart = time.time()
    
    if schema and schema.lower() not in filters.get('type','').lower(): 
        filters['type'] = filters.get('type') and '(%s|%s)'%(filters['type'],schema) or schema
    if 'stop' not in exclude.get('type','').lower(): 
        exclude['type'] = exclude.get('type') and '(%s|%s)'%(exclude['type'],'stop') or 'stop'
                
    config = ParseCSV(filename,filters=filters,exclude=exclude) # Attributes not for this schema will be pruned    
    n_all = len(config)
    print '>>> In LoadArchivingConfiguration(%s,%s,launch=%s,dedicated=%s,force=%s,overwrite=%s,filters=%s,exclude=%s)'\
        %((filename,schema,launch,dedicated,force,overwrite,filters,exclude,))
    print '>>> %d attributes read from %s file'%(len(config),filename)
    
    api = api or getAPI(schema)
    if dedicated:
        DedicateArchiversFromConfiguration(config,schema=schema,launch=launch,filters=filters,exclude=exclude,hosts=hosts,centralized=centralized)
        
    if check:
        print '>>> Pruning attributes not available ...'
        unavailable.extend([attr for attr in config if not utils.check_attribute(attr,readable=False)])#True)])
        if unavailable:
            print '\n%d attributes are not available!!!\n' % len(unavailable)
            if force: 
                [config.pop(att) for att in unavailable]
            else: raise Exception, 'Attributes not available: %s'%fun.list2str(unavailable)    
    
    #Attributes classified by Mode config
    modes = defaultdict(list)    
    for k,v in sorted(config.items()): 
        mode = utils.modes_to_string(api.check_modes(v['modes']))
        modes[mode].append(k)
    
    #The active part
    if launch or stop:
        #Archiving started in groups of 10 attributes
        for mode,alist in sorted(modes.items()): 
            if not overwrite: 
                print 'NOTE: Only those attributes not already archived will be modified in the database.'
                alist = [a for a in alist if a not in api or not api.attributes[a].archiver]
            devs = defaultdict(list)
            [devs[a.rsplit('/',1)[0]].append(a) for a in alist] #The devices will be inserted separately for each device
            for dev,attributes in sorted(devs.items()):
                attributes = sorted(attributes)
                for i in range(1+int((len(attributes)-1)/10)): 
                    attrs = attributes[i*10:(i+1)*10]
                    if stop and not api.stop_archiving(attrs,load=False):
                        if force: failed.extend(attrs)
                        else: raise Exception,'Archiving stop failed for: %s'%(attrs)
                    if launch and not api.start_archiving(attrs,utils.modes_to_dict(mode),load=False):
                        if force: failed.extend(attrs)
                        else: raise Exception,'Archiving start failed for: %s'%(attrs)
        api.load_all(values=False)
        
    ##Final Report
    if unavailable: 
        print 'Attributes not available: %s'%fun.list2str(unavailable)
    if failed: 
        print 'Attributes unable to start/stop archiving: %s' % fun.list2str(failed)
    #failed.extend(unavailable)
    if not launch: 
        print ('THE ARCHIVING OF THE ATTRIBUTES HAS NOT BEEN PROCESSED, EXECUTE LoadArchivingConfiguration(%s,launch=True) TO DO IT'%filename)
    else: 
        print ('%d attributes requested, %d have been introduced into archiving, %d failed, %d unavailable' %(n_all,n_all-len(unavailable)-len(failed),len(failed),len(unavailable)))
    
    print 'LoadArchivingConfiguration finished in %f minutes.' % ((time.time()-tstart)/60.)
    RESULT = dict(modes)
    return RESULT
    
def DedicateArchiversFromConfiguration(filename, schema,launch=True,force=False,filters=None,exclude=None,hosts=None,centralized=False):
    ''' This is the function used to load a list of attributes and hosts 
    **NOTE**: The function must be called separately for each archiving type LoadArchivingConfiguration(..,schema='hdb' or 'tdb')
    
    - There's a single mandatory argument:
        :param filename: it could be a file name or a config already parsed using ParseCSV
        :param schema: HDB or TDB; the objects that do not match the schema will be discarded; it forces the script to be called separately for TDB and HDB
        :param launch: if False device servers will be configured but not started
    
    - Other arguments help to control where the information is stored
        :param hosts: {hosts:[attributes]} dictionary
        :param centralized: if host is set it will start all servers in a single host (dedicated but not distributed)
    
    :return: A dictionary with {modes:[attributes]} is returned
    '''
    hosts = fun.notNone(hosts,defaultdict(list))
    filters,exclude = fun.notNone(filters,{}),fun.notNone(exclude,{})
    tstart = time.time()
    
    api = PyTangoArchiving.ArchivingAPI(schema,load=False) #lightweight api
    api.load_servers()    
    if centralized and not fun.isString(centralized):
        centralized = api.host.split('.')[0]    
    
    if isinstance(filename,dict):
        config,filename = filename,'user_values'
    else:
        if schema and schema.lower() not in filters.get('type','').lower(): 
            filters['type'] = filters.get('type') and '(%s|%s)'%(filters['type'],schema) or schema
        if 'stop' not in exclude.get('type','').lower(): 
            exclude['type'] = exclude.get('type') and '(%s|%s)'%(exclude['type'],'stop') or 'stop'
        config = ParseCSV(filename,filters=filters,exclude=exclude) # Attributes not for this schema will be pruned    
        
    print ('In DedicateArchiversFromConfiguration(%s,schema=%s,centralized=%s,filters=%s,exclude=%s)'%(filename,schema,centralized,filters,exclude))
        
    print '>>> Configuring dedicated archiving, creating new archiver servers and starting them if needed ...'
    assigned = api.get_dedicated_archivers()
    assert type(hosts)==defaultdict,'LoadArchivingConfigurationException: hosts argument must be a collections.defaultdict(list) object'
    if hosts: hosts.clear()
    for k,v in sorted(config.items()): 
        if any(k.lower() in atts for dev,atts in assigned.items()): continue #Skip already assigned attributes
        hosts[v['host'].lower()].append(k.lower())
    if any(hosts.values()):
        assigned = api.set_dedicated_archivers(hosts,30)
        manager = api.servers.get_device_server(api.get_manager().name())
        api.servers.stop_servers(manager)
        print 'ArchivingManager stop, restarting archivers ...'
        all_servers = list(set([api.servers.get_device_server(archiver) for archiver,attrs in assigned.items() if attrs]))
        try:
            for host,vals in hosts.items():
                servers = [s for s in all_servers if host.split('.')[0].lower() in s.lower()]
                print 'Restarting the Dedicated archiving servers in host %s: %s' % (host,servers)
                for server in servers:
                    try:
                        if api.servers[server].ping() is not None: api.servers.stop_servers(server)
                    except Exception,e: print 'The server may be not running: %s'%str(e)
                    print 'waiting some seconds for stop_servers ...'
                    time.sleep(10.)   
                if launch:
                    for server in servers:
                        try:
                            api.servers.start_servers(server,centralized or host,wait=30.)
                            print 'waiting some seconds for start_servers ...'
                            time.sleep(10.)
                            api.servers.set_server_level(server,centralized or host,4)
                            time.sleep(1.)
                        except Exception,e:
                            if not force: 
                                raise e
                            else:
                                print 'UNABLE TO RESTART %s'%server
                                [[failed.append(a) for a in assigned.get(d,[]) if a not in failed] for d in api.servers[server].get_device_list()]
            time.sleep(10.)
            if launch: api.servers.start_servers(manager,wait=30.)
            print 'waiting some seconds after ArchivingManager restart ...'
            time.sleep(10.)
            print 'Dedicated archiving configuration finished ...'
        except Exception,e:
            print traceback.format_exc()
            print 'Dedicated Archiving failed! ... restarting the ArchivingManager'
            api.servers.start_servers(manager,wait=60.)
            raise Exception('Dedicated Archiving failed!')
    else:
        print 'Dedicated archiving not changed ...'
    api.load_all(values=False)    
    return hosts
    
class ModeCheckNotImplemented(Exception): 
    pass    
    
def CheckArchivingConfiguration(filename,schema,api=None,check_modes=False,check_conf=True,period=3600,filters=None,exclude=None,restart=False):
    """
    :param schema: hdb/tdb
    
    Returns the archiving status for each attribute:
    Returns for each attribute in the file the actual status:
    { a: None, #attribute not archived
    { a: t>0, #True if measure in period, if not last time read, usable to check values
    { a: exception, #unable to get values
    """
    print "In CheckArchivingConfiguration(%s,restart=%s)" % ((filename,schema,period,filters,exclude),restart)
    filters = fun.notNone(filters,{})
    exclude = fun.notNone(exclude,{})
    api = api or PyTangoArchiving.ArchivingAPI(schema)
    result,now = {},time.time()
    
    if schema and schema.lower() not in filters.get('type','').lower(): 
        filters['type'] = filters.get('type') and '(%s|%s)'%(filters['type'],schema) or schema
    attributes = dict((k.lower(),v['modes']) for k,v in ParseCSV(filename,schema=(api.schema),filters=filters,exclude=exclude).items())
    
    devices = [a.rsplit('/',1)[0] for a in attributes]
    ok,unavailable,late,missing,triable,diff,retried,lost,dedicated = [],[],[],[],[],[],[],[],[]
    print "Checking %s attributes ..." % len(attributes)
    for att,modes in attributes.items():
        try:
            available = utils.check_attribute(att,readable=False)#True) #I do not want to exclude piranis!!!
            archived = att in api and api[att].archiver
            vals = api.load_last_values(att) if att in api else []
            if archived and vals:
                if not available: 
                    unavailable.append(att)
                if archived and available: 
                    if api[att].dedicated: dedicated.append(att)
                    max_period = max([period]+[mode[0]/1000. for mode in api[att].modes.values()]) 
                    date = utils.date2time(vals[0][0])
                    if date>=(now-max_period): 
                        ok.append(att)
                    elif date>=(now-5*max_period): #(24*3600) 
                        late.append(att)
                    else: 
                        lost.append(att)
                        if restart:
                            #Dedicated configuration is not done here!! ... this is just for restarting temporarily unavailable attributes
                            print 'Restarting archiving for %s' % att
                            if api.start_archiving([att],api.check_modes(modes),load=False):
                                retried.append(att)                        
                    if check_conf:
                        m1,m2 = utils.modes_to_string(api.check_modes(modes)),utils.modes_to_string(api[att].modes)
                        if m1!=m2:
                            print '%s.modes differ: file:%s != db:%s' % (att,m1,m2)
                            diff.append(att)
                    if check_modes:
                        raise ModeCheckNotImplemented
            else:
                restarted = False
                if vals and available:
                    lost.append(att)                    
                    if restart:
                        #Dedicated configuration is not done here!! ... this is just for restarting temporarily unavailable attributes
                        print 'Restarting archiving for %s' % att
                        if api.start_archiving([att],api.check_modes(modes),load=False):
                            retried.append(att)
                else:
                    missing.append(att)
                    if available: triable.append(att)
                    
        except ModeCheckNotImplemented,e: 
                raise e
        except Exception,e: 
                print '%s failed!: %s' % (att,e)
                missing.append(att)
    polizon = [a for a in api.attributes if a.rsplit('/',1)[0] in devices and api[a].archiver and a not in attributes]
    if polizon: print 'Attributes not in list but archived from same devices: %s'%polizon
    RESULT = {'all':sorted(attributes.keys()),'unavailable':sorted(unavailable),'ok':sorted(ok),'late':sorted(late),'missing':sorted(missing),'triable':sorted(triable),
              'diff':sorted(diff),'retried':sorted(retried),'lost':sorted(lost),'dedicated':dedicated,'polizon':sorted(polizon)}
    summary = ', '.join(['%s:%s'%(k.upper(),len(v)) for k,v in sorted(RESULT.items()) if v])
    RESULT['rate'] = (float(len(RESULT['ok']))/len(RESULT['all'])) if (RESULT['ok'] and RESULT['all']) else 0.
    print ('CheckArchivingConfiguration(%s,%s):'%(filename,schema))+'\n\t'+summary
    return RESULT
    
def CheckAttributesAvailability(filename='',attributes=[],readable=False):
    """ 
    Returns for each attribute in the file the actual status:
    { a: None, #attribute not available
    { a: a, #attribute available and readable
    { a: exception, #attribute exists but is not readable
    """
    if not (attributes or filename): raise Exception('ArgumentRequired')
    if fun.isSequence(filename): filename,attributes = '',filename
    attributes = [str(a).lower() for a in (attributes or ParseCSV(filename))]
    RESULT = dict((a,bool(utils.check_attribute(a,readable))) for a in attributes)
    return RESULT
    
def StopArchivingConfiguration(filename,schema,api=None):
    """
    Stops the archiving for the attributes of all the devices appearing in a config file
    :param schema: hdb/tdb
    """
    if not api:
        api = PyTangoArchiving.ArchivingAPI(schema,load=False) #lightweight api
        api.load_attribute_descriptions()
        api.load_attribute_modes()    
    config = ParseCSV(filename,schema=api.schema)
    print 'In StopArchivingConfiguration(%s): %d attributes found' % (filename,len(config))
    devices = set([c.rsplit('/',1)[0].lower() for c in config])
    for dev in devices:
        attrs = [a for a in api if a.startswith(dev) and api[a].archiver]
        if attrs: api.stop_archiving(attrs,load=False)
        else: print 'No attributes currently archived for %s' % dev
        time.sleep(3.)
    api.load_all(values=False)
    return
    
def ExportToCSV(api,mask,filename):
    import re
    #mask = '.*/vc/.*/.*'
    #filename = '/homelocal/sicilia/vacuum.csv'
    
    vcs = dict([(k,v) for k,v in api.attributes.items() if re.match(mask,k)])
    
    f = open(filename,'w')
    
    devices = defaultdict(dict)
    for a,v in vcs.items():
        try: devices[a.rsplit('/',1)[0]][a.rsplit('/',1)[-1]+';'+str(v.extractModeString(v.modes))]=v
        except Exception,e: '%s failed: %s' % (a,str(e))
    
    modes = defaultdict(dict)
    for d in devices:
        modes[str(sorted(devices[d].keys()))][d]=devices[d]
    
    values = []
    values.append(['#dserver host','domain/family/member','attribute','double/long/short/boolean/string/spectrum','periodic/absolute/relative'])
    for mode,devices in sorted(modes.items()):
        model = sorted(devices.keys())[0]
        default = devices[model].values()[0].modes.get('MODE_P',[300000])[0] /1000
        values.append(['',model,'@DEFAULT','','periodic',default])
        for a,v in sorted(devices[model].items()):
            for m,p in sorted(v.modes.items()):
                if m=='MODE_P' and len(v.modes)>1:continue
                line = ['','',v.name.rsplit('/',1)[-1],str(v.data_type)]
                line.append({'MODE_P':'periodic','MODE_R':'relative','MODE_A':'absolute'}[m])
                line.append(str(p[0]/1000))
                line.extend([str(s) for s in p[1:]])
                values.append(line)
        for d in sorted(devices.keys())[1:]:
            values.append(['',d,'@COPY:%s'%model])
        values.append([])
    
    f.write('\n'.join(['\t'.join([str(s) for s in v]) for v in values]))
    f.close()
    RESULT = values
    return len(values)
            
#############################################################################################################
#ParseCSV
def ParseCSV(filename,schema='',filters=None,exclude=None,
        dedicated=None,deletelist=None,context=None):
    ''' 
    Extracts all information from an achiving configuration .csv file
    
    :param filename: is the file from where the Array is loaded
    :param schema: can be used to filter the 'Type' column in the CSVs
    :param filters: is a dictionary {name/type/host/modes:filter} that will add only those attributes where column matches the filter (lowercase)
    :param exclude: elements to exclude
    
    :param dedicated: is a dictionary with key=attribute and value=archiver 
    :param deletelist: is a list, contains the attributes to be removed from archiving
    :param context: is a dictionary, contains the information about the authority of the configuration    
    
    :return: a dictionary like {attribute:{host:str,modes:{str:[int]},type:str}}
    In addition the dedicated, deletelist and context argumens can be used to get more detailed information.
    '''
    dedicated,deletelist,context = \
        fun.notNone(dedicated,{}),fun.notNone(deletelist,[]),fun.notNone(context,{})
    
    filters,exclude = fun.notNone(filters,{}),fun.notNone(exclude,{'type':'stop'})
    
    if schema and schema.lower() not in filters.get('type','').lower(): 
        filters['type'] = filters.get('type') and '(%s|%s)'%(filters['type'],schema) or schema
    
    print 'In ParseCSV(%s,%s,%s,-%s) ...'%(filename,schema,filters,exclude)    
    config = CSVArray()
    config.load(filename,comment='#')
    assert len(config.rows)>1, 'File is empty!'
    
    headers=['Device','Host','Attribute','Type','ArchivingMode','Periode','MinRange','MaxRange']
    print 'Searching headers ...'
    head=config.get(0)
  
    def checkHeaders():
        if not all(h in ''.join(head) for h in headers):
            print 'WRONG FILE HEADERS!'
            exit()
      
    [config.fill(head=h) for h in head]
    config.setOffset(1)
  
    print 'Getting attributes from the file ...'
    # it returns the list of device names and the lines that matches for each
    hosts=config.getAsTree(lastbranch='ArchivingMode')#config.get(head='Device',distinct=True)
    if not hosts: 
        print 'NO HOSTS FOUND IN %s!!!' % filename
        return {}
    
    ## Parsing the params to create a Context
    #-------------------------------------------------
    defaultparams = {'@LABEL':'User_Code-0X','@AUTHOR':'Who?','@DATE':'When?','@DESCRIPTION':'What?',}#'@REASON':'Why?'} ## Reason became deprecated
    transparams = {'@LABEL':'name','@AUTHOR':'author','@DATE':'time','@DESCRIPTION':'description',}#'@REASON':'reason'} ## Reason became deprecated
    for p,v in defaultparams.items():
        if not fun.inCl(p,hosts): raise Exception('PARAMS_ERROR','%s NOT FOUND'%p) 
        elif not hosts[p]:  raise Exception('PARAMS_ERROR','%s IS EMPTY'%p) 
        elif hosts[p].keys()[0]==v: raise Exception('PARAMS_ERROR','%s NOT INITIALIZED (%s)'%(p,v)) 
        defaultparams[p]=hosts.pop(p).keys()[0]
        context[p]=defaultparams[p]
        if p=='@DATE':
            t,time_fmts = None,['%Y-%m-%d', '%Y-%m-%d %H:%M', '%y-%m-%d', '%y-%m-%d %H:%M', 
                                '%d-%m-%Y' ,'%d-%m-%Y %H:%M' ,'%d-%m-%y' ,'%d-%m-%y %H:%M' ,
                                '%d/%m/%Y','%d/%m/%Y %H:%M','%d/%m/%y','%d/%m/%y %H:%M',
                                '%Y/%m/%d','%Y/%m/%d %H:%M','%y/%m/%d','%y/%m/%d %H:%M',
                                '%m/%d/%Y','%m/%d/%Y %H:%M','%m/%d/%y','%m/%d/%y %H:%M',
                                '%m-%d-%Y' ,'%m-%d-%Y %H:%M','%m-%d-%y' ,'%m-%d-%y %H:%M',
                                ]
            for tf in time_fmts:
                try:
                    #print 'trying format %s'%str(tf)
                    t = time.strftime('%Y-%m-%d',time.strptime(context[p],tf))
                    break
                except: pass
            if t is not None: context[transparams[p]]=t
            else: raise Exception('PARAMS_ERROR','@DATE format cannot be parsed!: %s'%str(context[p]))

    ##Reading the archiving modes
    #-------------------------------------------------
    attrslist = {}
    archmodes={'PERIODIC':'MODE_P','ABSOLUTE':'MODE_A','RELATIVE':'MODE_R','THRESHOLD':'MODE_T','CALC':'MODE_C','EXTERNAL':'MODE_E'}
    all_devs = {}
    [all_devs.update(devs) for devs in hosts.values()] #Used for @COPY tag
    for host,devs in sorted(hosts.items()):
        for dev,attributes in sorted(devs.items()):
            #print 'reading device %s:%s'%(dev,str(attributes))
            '''Doing all the checks needed before adding any attribute'''
            for a in [t.strip() for t in attributes if fun.inCl('@COPY',t)]: #COPYing attributes from another device
                    if ':' in a: 
                        dev2 = a.split(':')[1].strip()
                    else:
                        raise Exception('COPY macro must be declared in the way @COPY:a/tango/device')
                    #elif a in attributes: 
                        #dev2 = attributes[a].keys()[0]
                    #else: 
                        #dev2 = ''
                    if dev2:
                        if dev2 not in all_devs: raise Exception('AttributesNotDefinedFor:%s'%dev2)
                        [attributes.__setitem__(k,v) for k,v in all_devs[dev2].items() if k and k not in attributes]
                        attributes.pop(a)
            #if '@DEFAULT' in attributes: 
            #    pass
            if any('@DELETE' in a or '@STOP' in a for a in attributes): #If a @DELETE or @STOP is found as single attribute all dev. attributes are stopped
                deletelist.append(dev)
            
            defaults = [(a,v) for a,v in attributes.items() if '@DEFAULT' in a]
            DEFAULT_MODE = {'MODE_P':[300000]} ##seconds in CSV's are converted to milliseconds
            for a,tipus in defaults:
                if not tipus or not tipus.values()[0]: 
                    print 'Wrong format assigning defaults for %s device' % dev
                    continue
                mode,params = tipus.values()[0].items()[0]
                mode =  archmodes.get(mode.upper(),mode)
                DEFAULT_MODE[mode] = [float(p) for p in params if p]
                #attributes.pop(a)
            #print 'DEFAULT_MODE for archiving is: %s' % DEFAULT_MODE
        
            for attribute,modes in sorted(attributes.items()):
                if '@DEFAULT' in attribute: continue
                attribute = attribute.lower()
                config = dict(DEFAULT_MODE.items())
                if not modes:
                    #print '\treading attribute %s: using default modes %s'%(attribute,str(DEFAULT_MODE))
                    attrslist[dev+'/'+attribute]={'host':host,'type':tipus,'modes':config}
                else:
                    #print '\treading attribute %s:%s'%(attribute,str(modes))
                    tipus=modes.keys()[0]
                    #And modes is overriden by its own member
                    modes=modes.values()[0]
                    firstmode,firstparam=modes.keys()[0],modes.values()[0][0]
                    if any(a.startswith('@') for a in [attribute,firstmode,tipus]):
                            if attribute=='@DEDICATED':
                                dedicated[dev]=tipus
                                #print dev,'.DEDICATED=',tipus
                            elif any(c in ['@STOP','@DELETE'] for c in [firstmode,tipus]):
                                deletelist.append(dev+'/'+attribute)
                                #print dev,'.STOP ARCHIVING'
                    else:
                            #Adding always a default value to the list of modes.
                            #if 'MODE_P' not in config: config.update(DEFAULT_MODE.items())
                            for mode,params in modes.items():
                                if not mode: continue
                                #print '\treading mode %s:%s'%(mode,str(params))
                                mode=mode.upper()
                                if mode in archmodes:
                                        mode=archmodes[mode]
                                elif mode not in archmodes.values():
                                        print 'Unknown mode!: ',mode
                                        continue
                                params = [float(p) for p in params if p]
                                if params: params[0] = 1000.*params[0] #Converting periods from seconds to milliseconds
                                config[mode]=params
                            attrslist[dev+'/'+attribute]={'host':host,'type':tipus,'modes':config}
                            #print 'attrslist[%s/%s]=%s'%(dev,attribute,attrslist[dev+'/'+attribute])
                            
    # applying filters to obtained attributes
    if filters or exclude:
        for k,v in attrslist.items():
            #All attributes that don't match filters or match exclude clauses are pruned
            match_filters = (('name' not in filters or fun.matchCl(fun.toRegexp(filters['name']),k)) and
                all(fun.matchCl(fun.toRegexp(f),str(v[t])) for t,f in filters.items() if t!='name'))
            match_exclude = (('name' in exclude and fun.matchCl(fun.toRegexp(exclude['name']),k)) or
                all(fun.matchCl(fun.toRegexp(f),str(v[t])) for t,f in exclude.items() if t!='name'))
            #print 'In %s: match_filters = %s, match_exclude = %s' %(k,match_filters,match_exclude)
            if (filters and not match_filters) or (exclude and match_exclude):
                attrslist.pop(k)
                if k in dedicated: dedicated.pop(k)
                if k in deletelist: deletelist.remove(k)
    
    print 'Specified %d attributes from %d hosts'%(len(attrslist),len(hosts))
    if dedicated: print '%d devices are dedicated'%(len(dedicated))
    if deletelist: print '%d attributes to delete'%(len(deletelist))
    RESULT = attrslist
    return RESULT
    
#END OF ParseCSV
#############################################################################################################            
# Import/export from mambo

HDBModes={'MODE_P':4,'MODE_R':5,'MODE_A':0,'MODE_D':2,'MODE_T':6}

def export2csv(attrslist,filename):
    raise Exception,'NotImplemented'

def attrs2ac(attrslist,acname):
    '''Converts and attribute list in an AC (mambo xml) file
    attrslist: list of attributes/archiving_modes to be added in the file
    acname: XMLDocument where the configuration is going to be stored
    '''
    impl = minidom.getDOMImplementation()
    newdoc = impl.createDocument(None,'archivingConfiguration',None)
    HDBModes={'MODE_P':4,'MODE_R':5,'MODE_A':0,'MODE_D':2,'MODE_T':6}

    def createAttributeXMLNode(xmldoc,attrname,archmodes,archtype='hdb'):
        '''Creates the appropiated XML structure for an AC file
        xmldoc: xml implemented document, used to generate the nodes
        attrname: name of the attribute
        archtype: HDB or TDB
        modes: dictionary containing the archiving modes and arguments of each one
        result: the AttributeNode element created
        '''
        att=xmldoc.createElement('attribute')
        att.setAttribute('completeName',attrname)
        modes=xmldoc.createElement('%sModes'%archtype.upper())
        modes.setAttribute('dedicatedArchiver','')
        if archtype.lower()=='tdb': modes.setAttribute('exportPeriod',"1800000")
        modes.appendChild(xmldoc.createTextNode('\n'))
        for key,value in archmodes.items():
            if key not in HDBModes: continue
            mode=xmldoc.createElement('mode')
            if key in ['MODE_P','periodic']:
                mode.setAttribute('type',str(HDBModes['MODE_P']))
                mode.setAttribute('period',str(value[0]))
            elif key in ['MODE_R','relative']:
                mode.setAttribute('type',str(HDBModes['MODE_R']))
                mode.setAttribute('period',str(value[0]))
                mode.setAttribute('percent_sup',str(value[2]))
                mode.setAttribute('percent_inf',str(value[1]))
            elif key in ['MODE_A','absolute']:
                mode.setAttribute('type',str(HDBModes['MODE_A']))
                mode.setAttribute('period',str(value[0]))
                mode.setAttribute('val_sup',str(value[2]))
                mode.setAttribute('val_inf',str(value[1]))
            elif key in ['MODE_D','differential']:
                mode.setAttribute('type',str(HDBModes['MODE_D']))
                mode.setAttribute('period',str(value[0]))
            elif key in ['MODE_T','threshold']:
                mode.setAttribute('type',str(HDBModes['MODE_T']))
                mode.setAttribute('period',str(value[0]))
                mode.setAttribute('threshold_sup',str(value[2]))
                mode.setAttribute('threshold_inf',str(value[1]))
            modes.appendChild(mode)
            modes.appendChild(xmldoc.createTextNode('\n'))
        modes.appendChild(xmldoc.createTextNode('\n'))
        
        att.appendChild(xmldoc.createTextNode('\n'))
        att.appendChild(modes)
        att.appendChild(xmldoc.createTextNode('\n'))
        return att

    newdoc.version=u'1.0'
    newdoc.encoding=u'ISO-8859-1'
    args={  u'creationDate': time.strftime('%Y-%m-%d %H:%M:%S.000'),
        u'isHistoric': u'true',
        u'isModified': u'false',
        u'lastUpdateDate': time.strftime('%Y-%m-%d %H:%M:%S.000'),
        u'name': acname,
        u'path': os.path.abspath('')+'/'+acname+'.ac'}
    for k,v in args.items(): newdoc.firstChild.setAttribute(k,v)
    
    fc=newdoc.firstChild
    fc.appendChild(newdoc.createTextNode('\n'))
    for attribute,modes in attrslist.items():
        fc.appendChild(createAttributeNode(newdoc,attribute,modes))
        fc.appendChild(newdoc.createTextNode('\n'))
        
    print newdoc.toxml()
    return newdoc

def readArchivingConfigurationFromDB(htype='hdb'):
    ''' Using the ArchivingAPI this function reads the Status of each attribute from the amt table of the database and returns a dictionary with the actual configurations
    The structure of the dictionary is {start_date_epoch:{attrname:[archivingmodes],...},...}
    '''
    pass            

####################################################################################
