//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/ViewConfigurationManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewConfigurationManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ViewConfigurationManagerFactory.java,v $
// Revision 1.2  2005/11/29 18:27:56  chinkumo
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
package fr.soleil.mambo.datasources.file;


public class ViewConfigurationManagerFactory
{
    public static final int DUMMY_IMPL_TYPE = 1;
    public static final int XML_IMPL_TYPE = 2;

    private static IViewConfigurationManager currentImpl = null;

    /**
     * @param typeOfImpl
     * @return 8 juil. 2005
     */
    public static IViewConfigurationManager getImpl ( int typeOfImpl )
    {
        switch ( typeOfImpl )
        {
            case DUMMY_IMPL_TYPE:
                currentImpl = new DummyViewConfigurationManagerImpl();
                break;

            case XML_IMPL_TYPE:
                currentImpl = new XMLViewConfigurationManagerImpl();
                break;

            default:
                throw new IllegalStateException( "Expected either DUMMY_IMPL_TYPE (1) or XML_IMPL_TYPE (2), got " + typeOfImpl + " instead." );
        }

        return currentImpl;
    }

    /**
     * @return 28 juin 2005
     */
    public static IViewConfigurationManager getCurrentImpl ()
    {
        return currentImpl;
    }
}
