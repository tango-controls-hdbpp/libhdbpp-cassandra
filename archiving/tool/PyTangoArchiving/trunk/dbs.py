
import traceback,time
import MySQLdb,sys

try: import fandango
except ImportError: import PyTango_utils as fandango
from fandango.objects import Object
from fandango.log import Logger
from fandango.db import FriendlyDB

class ArchivingDB(FriendlyDB):
    """ Class for managing the direct access to the database """
    #def __init__(self,api,db_name,user='',passwd='', host=''):
        #if not api or not database:
            #self.log.error('ArchivingAPI and database are required arguments for ArchivingDB initialization!')
            #return
        #self.api=api
        #self.db_name=db_name
        #self.host=api.host
        #self.log=Logger('ArchivingDB(%s)'%db_name)
        #self.setUser(user,passwd)
        #self.__initMySQLconection()
    #def setUser(self,user,passwd):
        #self.user=user
        #self.passwd=passwd
    #def cursor(self):
        #return self.db.cursor()
    #def __initMySQLconnection(self):
        #try:
            #self.db=MySQLdb.connect(db=self.db_name,host=self.host,user=self.user,passwd=self.passwd)
        #except Exception,e:
            #self.log.error( 'Unable to create a MySQLdb connection to "%s"@%s.%s: %s'%(self.user,self.host,self.db_name,str(e)))
            
    def get_attribute_descriptions(self):
        return self.Query('SELECT full_name,ID,data_type,data_format,writable from adt')
    
    def get_attribute_properties(self):
        return self.Select('*','apt',asDict=True)    
            
    def get_attribute_modes(self):
        ''' This method reads the contents of the table AMT and returns a dictionary {ID:[ID,MODES]} '''    
        return dict([(line[0],line) for line in self.Select('*','amt','stop_date is NULL')])   
    
    def get_last_attribute_values(self,table,n):
        if 'read_value' in self.getTableCols(table):
            return self.Query('SELECT time,read_value from %s order by time desc limit %d'%(table,n))
        else:
            return self.Query('SELECT time,value from %s order by time desc limit %d'%(table,n))
    
    def get_attribute_values(self,table,start_date,stop_date):
        if 'read_value' in self.getTableCols(table):
            return self.Query('SELECT time,read_value from %s where time between "%s" and "%s" order by time'%(table,start_date,stop_date))
        else:
            return self.Query('SELECT time,value from %s where time between "%s" and "%s" order by time'%(table,start_date,stop_date))
        