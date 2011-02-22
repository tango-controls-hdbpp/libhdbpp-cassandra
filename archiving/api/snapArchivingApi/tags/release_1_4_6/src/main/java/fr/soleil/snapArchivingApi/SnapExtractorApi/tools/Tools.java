package fr.soleil.snapArchivingApi.SnapExtractorApi.tools;

import java.sql.Timestamp;

import fr.esrf.Tango.DevError;
import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapManagerApi.SnapManagerApi;
import fr.soleil.snapArchivingApi.SnapshotingApi.ConfigConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.DateUtil;

/**
 * A class with a few exception handling methods.
 * @author CLAISSE 
 */
public class Tools 
{
	public static final String FR_DATE_PATTERN = "dd-MM-yyyy HH:mm:ss.SSS";
	public static final String US_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	static final java.util.GregorianCalendar calendar = new java.util.GregorianCalendar();
	static final java.text.SimpleDateFormat genFormatFR = new java.text.SimpleDateFormat(FR_DATE_PATTERN);
	static final java.text.SimpleDateFormat genFormatUS = new java.text.SimpleDateFormat(US_DATE_PATTERN);

	/**
	 * Returns a date as a String formated for the database.
	 * @param milliseconds the date in milliseconds
	 * @return A date as a String formated for the database.
	 */
	public static String formatDate(long milliseconds) {
        int type = SnapManagerApi.getSnapDbType();
        String date = "";
        switch (type) {
            case ConfigConst.BD_ORACLE :
                date = DateUtil.milliToString(milliseconds,
                        DateUtil.FR_DATE_PATTERN);
                break;
            case ConfigConst.BD_MYSQL :
            default :
                date = DateUtil.milliToString(milliseconds,
                        DateUtil.FR_DATE_PATTERN);
        }
        return date;
	}

    /**
     * Converts a Throwable to a DevFailed and throws it.
     * @param exception The exception to convert
     * @return The DevFailed representation
     * @throws DevFailed Always thrown by definition of the method
     */
    public static void throwDevFailed(Throwable exception) throws DevFailed 
    {
	    DevFailed devFailed = new DevFailed ();
	    devFailed.initCause ( exception );
	    throw devFailed;
    }

    /**
     * Extracts causes from a Throwable if it is an instance of DevFailed, and prints it.
     * @param t The exception to log
     */
    public static void printIfDevFailed(Throwable t) 
    {
        t.printStackTrace ();    
        
        if (t instanceof DevFailed)
		{
            if ( ( (DevFailed) t ).getCause() != null )
            {
                System.out.println ( "CAUSE---------------START" );
			    ( (DevFailed) t ).getCause().printStackTrace (); 
			    System.out.println ( "CAUSE---------------END" );
            }
            
            DevError [] errors = ( (DevFailed) t ).errors;
		    if ( errors != null && errors.length !=0 )
		    {
		        System.out.println ( "ERRORS---------------START" );
		        for (  int i = 0 ; i < errors.length ; i ++ )
		        {
		            DevError error = errors [ i ];
		            System.out.println ( "desc/"+error.desc+"/origin/"+error.origin+"/reason/"+error.reason );
		        }
		        System.out.println ( "ERRORS---------------END" );
		    }
		}    
    }
    
    
	/**
	 * Cast a string format date (dd-MM-yyyy HH:mm:ss or yyyy-MM-dd HH:mm:ss) into long (number of milliseconds since  January 1, 1970)
	 *
	 * @param date
	 * @return
	 * @throws ArchivingException
	 */
	static public long stringToMilli(String date) 
	{
	
        boolean isFr = ( date.indexOf("-") != 4 );
        int currentLength = date.length ();
        //String toTheSecond = "yyyy-MM-dd HH:mm:ss";
        String toTheDay = "yyyy-MM-dd";
        
        //System.out.println ( "CLA/stringToMilli/BEFORE/date|"+date+"|" );
        
        if ( isFr )
        {
            
        }
        else
        {
            if ( currentLength == toTheDay.length () )
            {
                date += " 00:00:00";
            }
            if ( currentLength == ( toTheDay.length () + 1 ) )
            {
                date += "00:00:00";
            }
            if ( currentLength == ( toTheDay.length () + 2 ) )
            {
                date += "0:00:00";
            }
            if ( currentLength == ( toTheDay.length () + 3 ) )
            {
                date += ":00:00";
            }
            if ( currentLength == ( toTheDay.length () + 4 ) )
            {
                date += "00:00";
            }
            if ( currentLength == ( toTheDay.length () + 5 ) )
            {
                date += "0:00";
            }
            if ( currentLength == ( toTheDay.length () + 6 ) )
            {
                date += ":00";
            }
            if ( currentLength == ( toTheDay.length () + 7 ) )
            {
                date += "00";
            }
            if ( currentLength == ( toTheDay.length () + 8 ) )
            {
                date += "0";
            }
        }
        
        //System.out.println ( "CLA/stringToMilli/AFTER 1/date|"+date+"|" );
        
        if ( date.indexOf(".") == -1 )
        {
			date = date + ( ".000" );
        }
        //System.out.println ( "CLA/stringToMilli/AFTER 2/date|"+date+"|" );
        
        try
		{
			if ( isFr )
			{
				genFormatFR.parse(date);
				return genFormatFR.getCalendar().getTimeInMillis();
			}
			else
			{
				genFormatUS.parse(date);
				return genFormatUS.getCalendar().getTimeInMillis();
			}
		}
		catch ( Exception e1 )
		{
			try
            {
                //System.out.println ( "CLA/!!!!!!!!!!!!!!!!!!!!!!/date|"+date+"|" );
                e1.printStackTrace ();
                
                Timestamp ts = Timestamp.valueOf ( date );
                return ts.getTime ();
            }
            catch ( Exception e )
            {
                System.out.println ( "----------------------" );
                e.printStackTrace ();
                System.out.println ( "----------------------" );
                e.printStackTrace();
                return -1;
            }
		}
	}

}
