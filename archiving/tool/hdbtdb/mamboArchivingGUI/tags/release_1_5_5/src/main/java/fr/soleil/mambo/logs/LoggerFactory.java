//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/logs/LoggerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LoggerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: LoggerFactory.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.logs;

public class LoggerFactory
{
    public static final int DEFAULT_TYPE = 1;
    //public static final int REAL_IMPL_TYPE = 2;
    
    private static ILogger currentImpl = null;

    /**
     * @param typeOfImpl
     * @return 8 juil. 2005
     */
    public static ILogger getImpl ( int typeOfImpl )
    {
        switch ( typeOfImpl )
        {
            case DEFAULT_TYPE:
                currentImpl = new DefaultLogger();
                break;

            default:
                throw new IllegalStateException( "Expected either DEFAULT_TYPE (1), got " + typeOfImpl + " instead." );
        }

        return currentImpl;
    }


    /**
     * @return 8 juil. 2005
     */
    public static ILogger getCurrentImpl ()
    {
        return currentImpl;
    }

}
