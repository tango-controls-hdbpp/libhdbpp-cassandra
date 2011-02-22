package HdbArchiver;

import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;

public class HdbStateDetailed
{
    
    private static final int NUMBER_OF_DSERVERS = 20; 
    private static final int NUMBER_OF_DEVICES_BY_DSERVER = 5;
 
      /**
      * @param args 8 juil. 2005
      */
     public static void main ( String[] args )
     {
         String archiverPrefix = "archiving/hdbarchiver/";
         String archiver = archiverPrefix;
         
         for ( int iDevices = 1 ; iDevices <= NUMBER_OF_DSERVERS ; iDevices++ )
         {
             String device = leftPad ( iDevices );
                 
             for ( int iServers = 1 ; iServers <= NUMBER_OF_DEVICES_BY_DSERVER ; iServers++ )
             {
                 String server = leftPad ( iServers );
                 
                 archiver = archiverPrefix + device + "_" + server;
                 
                 System.out.println ( "-----------------------------------------------------" + archiver + "-----------------------" );
                 deviceAssessment ( archiver );
                 System.out.println ( "--------------------------------------------------------------------------------------" );
             }
         }
         
            
         
     }

    private static String leftPad(int i) 
    {
        String ret = "" + i;
        if ( i < 10 )
        {
            ret = "0" + ret;
        }
        return ret;
    }

    private static void deviceAssessment(String archiver) 
    {
        try 
        {
            DeviceProxy proxy = new DeviceProxy ( archiver );
            proxy.set_timeout_millis ( 10000 );
            DeviceData argin = new DeviceData ();
            //argin.insert ( parameters );
            String CMD_NAME = "StateDetailed";
            
            DeviceData argout = proxy.command_inout ( CMD_NAME , argin );
            String assessment = argout.extractString ();
            
            System.out.println ( assessment );
        } 
        catch (Throwable t) 
        {
            t.printStackTrace();
        }
    }
}