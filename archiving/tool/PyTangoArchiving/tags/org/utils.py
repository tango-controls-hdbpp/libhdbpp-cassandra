# by Sergi Rubio Manrique, srubio@cells.es
# ALBA Synchrotron Control Group

import PyTango
from PyTango import *
import time,os,re,traceback,xml
from random import randrange

#import PyTango_utils
import PyTango_utils.log
import PyTango_utils.objects
import MySQLdb,sys


def get_matching_attributes(dev,exprs):
    result = []
    [result.extend([a for a in PyTango.DeviceProxy(dev).get_attribute_list() if re.match(expr,a)]) for expr in exprs]
    return result

def check_attribute(attr):
    try:
        PyTango.AttributeProxy(attr)
        return attr
    except:
        return []

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

def set_archiving():
    """    
    import fandango.servers as servers
    api = PyTangoArchiving.ArchivingAPI()
    
    attrs = servers.get_matching_attributes(dev,exprs)
    
    api.start_archiving(['%s/%s'%(dev,att) for att in attrs],{MODE_P:[30000]})
    
    #THAT's cool    
    In [131]: archived = api.get_archived_attributes()
    In [132]: larchived = [a.lower() for a in archived]           
    In [135]: dev = 'li/ct/plc3'
    In [136]: exprs = '''..._V|H$'''.split()
    In [138]: attrs = ['%s/%s'%(dev,att) for att in servers.get_matching_attributes(dev,exprs)]
    In [140]: missed = [a for a in attrs if a.lower() not in larchived]
    [api.start_archiving(att,{'MODE_P':[30000]}) for att in missed]
    """
    pass    


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
