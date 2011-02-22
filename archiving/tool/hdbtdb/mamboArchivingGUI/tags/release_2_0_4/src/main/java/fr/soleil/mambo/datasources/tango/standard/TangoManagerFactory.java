//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/tango/standard/TangoManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TangoManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: TangoManagerFactory.java,v $
// Revision 1.1  2006/09/20 12:47:45  ounsy
// moved from mambo.datasources.tango
//
// Revision 1.3  2006/05/16 12:02:43  ounsy
// added new buffered implementations
//
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
package fr.soleil.mambo.datasources.tango.standard;

public class TangoManagerFactory
{
    public static final int DUMMY_IMPL_TYPE = 1;
    public static final int BASIC_IMPL_TYPE = 2;
    public static final int BUFFERED_IMPL_TYPE = 3;

    private static ITangoManager currentImpl = null;
    private static int currentImplType;
    
    private static boolean canBeBuffered = true;

    /**
     * @param _typeOfImpl
     * @return 8 juil. 2005
     */
    public static ITangoManager getImpl ( int _typeOfImpl )
    {
        currentImplType = _typeOfImpl;
        
        switch ( _typeOfImpl )
        {
            case DUMMY_IMPL_TYPE:
                currentImpl = new DummyTangoManagerImpl();
            break;

            case BASIC_IMPL_TYPE:
                currentImpl = new BasicTangoManagerImpl();
            break;

            case BUFFERED_IMPL_TYPE:
                if ( canBeBuffered )
                {
                    currentImpl = new BufferedTangoManagerImpl();
                }
                else
                {
                    currentImpl = new BasicTangoManagerImpl();   
                }
            break;

            default:
                throw new IllegalStateException( "Expected either DUMMY_IMPL_TYPE (1) or REAL_IMPL_TYPE (2), got " + _typeOfImpl + " instead." );
        }

        return currentImpl;
    }

    /**
     * @return 28 juin 2005
     */
    public static ITangoManager getCurrentImpl ()
    {
        return currentImpl;
    }
    
    public static void setBuffered ( boolean _canBeBuffered )
    {
        TangoManagerFactory.canBeBuffered = _canBeBuffered;        
        getImpl ( currentImplType ); 
    }

}
