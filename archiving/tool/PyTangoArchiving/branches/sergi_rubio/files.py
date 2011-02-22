# by Sergi Rubio Manrique, srubio@cells.es
# ALBA Synchrotron Control Group

import time
from xml.dom import minidom
from fandango.arrays import CSVArray
from os.path import abspath
import PyTangoArchiving
from . import utils

def LoadArchivingConfiguration(CSV_FILE,schema='hdb',launch=False,force=False):
    """ This is the function used to load an archiving configuration from a CSV file and start archiving. """
    
    api = PyTangoArchiving.ArchivingAPI(schema,load=False) #lightweight api
    api.load_attribute_descriptions()
    config = loadCSVfile(CSV_FILE)
    failed = [attr for attr in config if not utils.check_attribute(attr)]
    if failed:
        if force: [config.pop(att) for att in failed]
        else: raise Exception, 'Attributes not available: %s'%failed    
    
    from collections import defaultdict
    modes = defaultdict(list)
    
    #Attributes classified by config
    for k,v in config.items(): 
        mode = utils.modes_to_string(api.check_modes(v['modes']))
        modes[mode].append(k)
        
    if failed: print 'Attributes not available: %s'%failed
    
    failed = []
    if launch:
        #Archiving started in groups of 10 attributes
        for mode,alist in modes.items(): 
            devs = defaultdict(list)
            [devs[a.rsplit('/')[0]].append(a) for a in alist] #The devices will be inserted separately for each device
            for dev,attributes in devs.items():
                for i in range(1+int((len(attributes)-1)/10)): 
                    if not api.start_archiving(attributes[i*10:(i+1)*10],utils.modes_to_dict(mode)):
                        if force: failed.extend(attributes[i*10:(i+1)*10])
                        else: raise Exception,'Archiving start failed for: %s'%(attributes[i*10:(i+1)*10])
    if failed:
        print 'Attributes unable to start archiving: %s' % failed
    
    if not launch: 'THE ARCHIVING OF THE ATTRIBUTES HAS NOT BEEN STARTED, EXECUTE LoadArchivingConfiguration(%s,launch=True) TO DO IT'%CSV_FILE
    return dict(modes)
            
#############################################################################################################
#loadCSVFile
def loadCSVfile(filename,dedicated={},deletelist=[],context={}):
  ''' returns attrslist, dedicated, deletelist and a context_info object
  filename is the file from where the Array is loaded
  dedicated is a dictionary with key=attribute and value=archiver 
  deletelist is a list, contains the attributes to be removed from archiving
  context is a dictionary, contains the information about the authority of the configuration
  '''
  print 'Loading CSV/XML file ...',filename
  config = CSVArray()
  config.load(filename,comment='#')
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
  #it returns the list of device names and the lines that matches for each
  hosts=config.getAsTree(lastbranch='ArchivingMode')#config.get(head='Device',distinct=True)
  
  ##Parsing the params to create a Context
  #-------------------------------------------------
  defaultparams = {'@LABEL':'User_Code-0X','@AUTHOR':'Who?','@DATE':'When?','@DESCRIPTION':'What?','@REASON':'Why?'}
  transparams = {'@LABEL':'name','@AUTHOR':'author','@DATE':'time','@DESCRIPTION':'description','@REASON':'reason'}
  for p,v in defaultparams.items():
    if p not in hosts.keys() or not hosts[p] or hosts[p].keys()[0]==v:
      raise Exception('PARAMS_ERROR','All these defaultparams are MANDATORY!: %s'%str(defaultparams.keys()))
    defaultparams[p]=hosts.pop(p).keys()[0]
    context[p]=defaultparams[p]
    if p=='@DATE':
      t,time_fmts = None,['%Y-%m-%d','%Y-%m-%d %H:%M','%y-%m-%d','%y-%m-%d %H:%M','%d-%m-%Y','%d-%m-%Y %H:%M','%m-%d-%Y','%m-%d-%Y %H:%M',
        '%Y/%m/%d','%Y/%m/%d %H:%M','%y/%m/%d','%y/%m/%d %H:%M','%d/%m/%Y','%d/%m/%Y %H:%M','%m/%d/%Y','%m/%d/%Y %H:%M  ',]
      for tf in time_fmts:
        try:
          #print 'trying format %s'%str(tf)
          t = time.strftime('%Y-%m-%d',time.strptime(context[p],tf))
          break
        except: pass
      if t is not None: context[transparams[p]]=t
      else: raise Exception('PARAMS_ERROR','@DATE format should be YYYY-MM-DD!: %s'%str(context[p]))

  ##Reading the archiving modes
  #-------------------------------------------------
  attrslist = {}
  archmodes={'PERIODIC':'MODE_P','ABSOLUTE':'MODE_A','RELATIVE':'MODE_R','THRESHOLD':'MODE_T','CALC':'MODE_C','EXTERNAL':'MODE_E'}
  all_devs = {}
  [all_devs.update(devs) for devs in hosts.values()] #Used for @COPY tag
  for host,devs in sorted(hosts.items()):
    for dev,attributes in sorted(devs.items()):
      print 'reading device %s:%s'%(dev,str(attributes))
      template=[a for a in attributes if '@COPY' in a]
      if template:
        if ':' in template[0]:
            dev2 = template[0].split(':')[1]
        elif template[0] in attributes:
            dev2 = attributes[template[0]].keys()[0]
        #else: dev2 = ''
        [attributes.__setitem__(k,v) for k,v in all_devs[dev2].items() if k and k not in attributes]
        [attributes.pop(t) for t in template]
      
      #If a @DELETE or @STOP is found as single attribute all dev. attributes are stopped
      elif '@DELETE' in attributes.keys() or '@STOP' in attributes.keys():
        deletelist.append(dev)
      
      defaults = [(a,v) for a,v in attributes.items() if '@DEFAULT' in a and v]
      DEFAULT_MODE = defaults and {} or {'MODE_P':[300000]}
      for a,tipus in defaults:
        mode,params = tipus.values()[0].items()[0]
        mode =  archmodes.get(mode.upper(),mode)
        DEFAULT_MODE[mode] = params
        print dev,'.DEFAULT_MODE=',DEFAULT_MODE
        attributes.pop(a)
      
      for attribute,modes in sorted(attributes.items()):
        config = dict(DEFAULT_MODE.items())
        if not modes:
          print '\treading attribute %s: using default modes %s'%(attribute,str(DEFAULT_MODE))
        else:
          print '\treading attribute %s:%s'%(attribute,str(modes))
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
            if 'MODE_P' not in config: config.update(DEFAULT_MODE.items())
            for mode,params in modes.items():
              if not mode: continue
              print '\treading mode %s:%s'%(mode,str(params))
              mode=mode.upper()
              if mode in archmodes.keys():
                mode=archmodes[mode]
              elif mode not in archmodes.values():
                print 'Unknown mode!: ',mode
                continue
              params = [float(p) for p in params if p]
              config[mode]=params
            attrslist[dev+'/'+attribute]={'host':host,'type':tipus,'modes':config}
            #print 'attrslist[%s/%s]=%s'%(dev,attribute,attrslist[dev+'/'+attribute])
    
  print 'Specified %d attributes from %d hosts'%(len(attrslist),len(hosts))
  if dedicated: print '%d devices are dedicated'%(len(dedicated))
  if deletelist: print '%d attributes to delete'%(len(deletelist))
  
  return attrslist
#END OF loadCSVFile
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
