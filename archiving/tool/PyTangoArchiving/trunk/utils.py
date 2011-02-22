# by Sergi Rubio Manrique, srubio@cells.es
# ALBA Synchrotron Control Group

import PyTango
import time,datetime,os,re,traceback,xml,sys
from random import randrange

try: import fandango
except ImportError: import PyTango_utils as fandango

from fandango.db import FriendlyDB
import fandango.functional as fun
import fandango.linos as linos
    
import MySQLdb,sys

#########################################################################################
## These methods have been moved from here to fandango

from fandango.functional import date2time,date2str
from fandango.device import get_matching_attributes,check_device,check_attribute,get_distinct_devices
from fandango.device import get_distinct_domains,get_distinct_members,get_distinct_families,get_distinct_attributes

########################################################################################
import __builtin__

def decimate(seq,cmp=None):
    """ It will remove all values from a list that doesn't provide information.
    In a set of X consecutive identical values it will remove all except the first and the last.
    :param seq: a list of (timestamp,value) values
    """
    if len(seq)<3: return seq
    if len(seq[0])<2: return seq
    cmp = cmp or __builtin__.cmp
    pops = []
    x0,x1,x2 = seq[0],seq[1],seq[2]
    for i in range(len(seq)-2):
        if not cmp(seq[i][1],seq[i+1][1]) and not cmp(seq[i+1][1],seq[i+2][1]):
            pops.append(i+1)
    for i in reversed(pops):
        seq.pop(i)
    return seq

#########################################################################################

def RepairDomainNames(db_name,host,user,passwd,attrlist=None,update=False):
    """ This method sets all domain/family/member names to upper case in the ADT table """
    db = FriendlyDB(db_name,host,user,passwd)
    allnames = db.Query('SELECT full_name,device,att_name,ID FROM adt',export=True)
    failed = 0
    device,attrs = '',[]
    if attrlist: attrlist = [a.lower() for a in attrlist]
    for line in sorted(allnames):
        fname,dev,att_name,ID = line
        if attrlist and fname.lower() not in attrlist:
            continue
        if dev.lower()!=device.lower():
            try:
                dp = PyTango.DeviceProxy(dev)
                device = dp.name()
                attrs = dp.get_attribute_list() #Getting real attribute names
            except:
                attrs = []
            
        if attrs:
            try:
                att_name = [a for a in attrs if a.lower()==att_name.lower()][0]  
                full_name = device+'/'+str(att_name)
                q = "UPDATE adt SET domain = '%s',family = '%s',member = '%s',device = '%s',full_name = '%s', att_name = '%s' WHERE ID=%s" % (domain,family,member,device,full_name,att_name,ID)
                print q
                if update: db.Query(q) 
            except:
                failed += 1
        else:
            failed += 1
    if update: db.Query('COMMIT')
    ok = len(allnames)-failed
    print '%d/%d names updated' % (ok,len(allnames))
    return ok

def check_attribute_modes(old_value,new_value,modes,tolerance=0.):
    """ it returns a dictionary {'MODE': True/False } to verify if the value should have been archived for each mode
    old_value is a tuple (epoch,value)
    new_value is a tuple (epoch,value)
    modes is a dictionary: {'MODE':[args]}
    """
    result = {}
    ellapsed = (new_value[0]-old_value[0])
    if None in (new_value[1],old_value[1]): diff = 0
    elif fun.isNumber(new_value[1]): diff = abs(new_value[1]-old_value[1])
    else: diff = [0,1e9][new_value[1]!=old_value[1]] # If not a number then any difference must be archived

    for mode,args in modes.items():
        period = (1.+tolerance)*args[0]/1000.
        if 'MODE_P' in mode:
            result['MODE_P'] = ellapsed<period
        elif 'MODE_A' in mode:
            result['MODE_A'] = ellapsed<period or diff<=args[1]
        elif 'MODE_R' in mode:
            result['MODE_R'] = ellapsed<period or diff<=(fun.isNumber(new_value[1]) and abs(args[1]*old_value[1]) or 1e8)
    return result

def modes_to_string(modestring):
    modes,nmodes=modestring,[]
    #Building a sorted list of modes
    porder = ['MODE_P','MODE_A','MODE_R','MODE_T','MODE_C','MODE_D','MODE_E']
    for o in porder: 
        if o in modes.keys(): 
            #nmodes+=[o]+['%d'%int(n) for n in modes[o]]
            nmodes+=[o]+map(str,modes[o])
    return ','.join(nmodes)
        
def modes_to_dict(modestring):
    """ Converts an string of modes and params separated by commas in a dictionary """
    modestring=modestring.replace(' ','')
    if not ':' in modestring: params=modestring
    else: attrib,params=modestring.split(':')
    modes={}
    for m in params.split('MODE_'):
        if not m: continue
        modes['MODE_'+m.split(',')[0]]=[]
        for p in [v for v in m.split(',')[1:] if v]:
            modes['MODE_'+m.split(',')[0]].append(float(p))   
    return modes

###############################################################################
# SPECIAL CHECKS
###############################################################################

def get_duplicated_archivings(schema='hdb'):
    api = PyTangoArchiving.ArchivingAPI(schema)
    check = dict((a,list()) for a in api)
    archis = dict((h,check_device(h,command='StateDetailed')) for h in sorted(api.get_archivers()))
    for h in [a for a,v in archis.items() if v]:
        sd = api.get_archiver(h).StateDetailed().lower()
        for c in check:
            if c.lower() in sd.split():
                check[c].append(h)
    return sorted([(c,v) for c,v in check.items() if len(v)>1])

def reportArchiving():
    db = Database()

    rateDS = 5
    nDS = 20
    a=0
    for m in range(1,nDS+1):
        for n in range(1,rateDS+1):
            member = 'archiving/hdbarchiver/'+'%02d'%m+'-'+'%02d'%n
            print 'Reporting HdbArchiver: ',member
            dp=DeviceProxy(member)
            #print '\tStatus is:\n\t', dp.command_inout('Status')
            #print '\tState detailed is:\n\t', dp.command_inout('StateDetailed')
            print '\tScalar Charge is:\n\t', dp.read_attribute('scalar_charge').value
            a=a+dp.read_attribute('scalar_charge').value
    
    print 'Total Scalar Charge is ... ',a

## @name Methods for repairing the databases
# @{
        
def RemoveWrongValues(db,table,column,null_value,ranges,dates,extra_clauses='',check=False):
    ''' Sets the specified null_value for all values in columnd out of specified ranges
    Usage (for removing all temperatures above 200 degrees): 
     * RemoveWrongValues('hdb','att_00001','value',None,[0,200])
    @remark Values cannot be deleted from archiving tables, NULL values must be inserted instead
    
    #EXAMPLE: 
    #In [42]:tables = [v.table for k,v in api.attributes.items() if re.match('ws/ct/plctest3/.*',k)]
    #In [44]:[PyTangoArchiving.utils.RemoveWrongValues('hdb',t,'value',None,[0,500],['2009-03-26','2009-04-07']) for t in tables]
    #In [48]:[PyTangoArchiving.utils.RemoveWrongValues('hdb',t,'value',None,[50,150],['2009-03-30 19:00:00','2009-04-01 19:00:00']) for t in tables]
    '''
    result = False
    start,stop=dates
    if type(start) is not str: start=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(start))
    if type(stop) is not str: stop=time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(stop))
    ranges = type(ranges) in (list,set,tuple,dict) and ranges or list(ranges)
    max_,min_ = max(ranges),(len(ranges)>1 and min(ranges) or None)
       
    db = MySQLdb.connect(db='hdb',user='root')
    q = db.cursor()
    if check: query = "SELECT count(*) FROM %s" % (table)
    else: query = "UPDATE %s SET %s=%s" % (table,column,null_value is None and 'NULL' or null_value)
    where = " WHERE (%s" % ("%s > %s"%(column,max_))
    where += min_ is not None and " OR %s < %s)" % (column,min_) or ")"
    where += " AND (time BETWEEN '%s' AND '%s')" % (start,stop)
    if extra_clauses:
        where = " AND (%s)" % extra_clauses

    print 'the query is : ', query+where
    q.execute(query+where)
    if check:
        result = q.fetchone()[0]
        print 'result is %s; type is %s'%(result,type(result))
        print 'Values to remove: %d'%int(result)
    else:
        result = True
    db.close()
    return result
    
    
    #adt=q.fetchall()    
    #print 'There are %d attribute_ID registered in the database'%len(adt)
    #done=0
    #for line in adt:
        #ID = line[0]
        #full_name = line[1]
        #writable = line[2]
        #print 'ID %05d: %s, w=%d'%(ID,full_name,writable)
        #q.execute('describe att_%05d'%ID)
        #describe=q.fetchall()
        #col_name=describe[1][0]
        #col_type=describe[1][1]
        #if writable==int(PyTango.AttrWriteType.READ) and col_name!='value':
            #query='ALTER TABLE att_%05d CHANGE COLUMN %s value %s AFTER time'%(ID,col_name,col_type)
            #print 'query: ',query
            #q.execute(query)
            #done+=1
            
def RepairColumnNames():
    db = MySQLdb.connect(db='hdb',user='root')
    q = db.cursor()
    
    q.execute('select ID,full_name,writable from adt')
    adt=q.fetchall()
    
    print 'There are %d attribute_ID registered in the database'%len(adt)
    done=0
    for line in adt:
        ID = line[0]
        full_name = line[1]
        writable = line[2]
        print 'ID %05d: %s, w=%d'%(ID,full_name,writable)
        q.execute('describe att_%05d'%ID)
        describe=q.fetchall()
        col_name=describe[1][0]
        col_type=describe[1][1]
        if writable==int(PyTango.AttrWriteType.READ) and col_name!='value':
            query='ALTER TABLE att_%05d CHANGE COLUMN %s value %s AFTER time'%(ID,col_name,col_type)
            print 'query: ',query
            q.execute(query)
            done+=1
            
    print 'Attributes repaired: %d'%done    
                
##@}

def listLastTdbTime():
    db = MySQLdb.connect(db='tdb')
    q = db.cursor()
    q.execute('show tables')
    #It returns a TUPLE of TUPLES!!!, not a list!
    alltables = q.fetchall()
    
    q.execute('select ID,archiver from amt where stop_date is NULL')
    attribs = q.fetchall()
    
    attrtables = [ 'att_%05d'%i[0] for i in attribs ]
    print str(len(attribs))+' attributes being archived.'
    
    print 'Searching newest/oldest timestamps on attribute tables ...'
    results = []
    tmin,tmax = None,None
    
    for i,a in enumerate(attrtables):
        q.execute('select max(time),min(time) from '+a);
        #q.execute('select time from '+a+' order by time desc limit 1')
        #type returned is datetime.datetime
        row = q.fetchone()
        date,date2 = row[0],row[1]
        if tmax is None or date>tmax:
            tmax = date
        if tmin is None or date2<tmin:
            tmin = date2
        results.append((date,date2,a))
        print '\r%05d/%05d:\tOldest:%s;\tNewest:%s'%(i,len(attrtables),str(tmin),str(tmax)),
        sys.stdout.flush()
        
    results.sort()
    """
    print 'The last updated time found in database is '+str(results[0][1])+'-'+str(results[0][0])
    print 'Difference with newest is '+str(results.pop()[0]-results[0][0])
    
    print '\n'
    """

def KillAllServers(klass = 'HdbArchiver'):
    processes = linos.shell_command('ps uax').split('\n')
    archivers = [s for s in processes if '%s.%s'%(klass,klass) in s]
    for a in archivers:
        print 'Killing %s' % a[1:]
        pid = a.split()[1]
        linos.shell_command('kill -9 %s'%pid)
