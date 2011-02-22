//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/ArchivingConfigurationManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ArchivingConfigurationManagerFactory.java,v $
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


public class ArchivingConfigurationManagerFactory
{
    public static final int DUMMY_IMPL_TYPE = 1;
    public static final int XML_IMPL_TYPE = 2;

    private static IArchivingConfigurationManager currentImpl = null;

    /**
     * @param typeOfImpl
     * @return 8 juil. 2005
     */
    public static IArchivingConfigurationManager getImpl ( int typeOfImpl )
    {
        switch ( typeOfImpl )
        {
            case DUMMY_IMPL_TYPE:
                currentImpl = new DummyArchivingConfigurationManagerImpl();
                break;

            case XML_IMPL_TYPE:
                currentImpl = new XMLArchivingConfigurationManagerImpl();
                break;

            default:
                throw new IllegalStateException( "Expected either DUMMY_IMPL_TYPE (1) or XML_IMPL_TYPE (2), got " + typeOfImpl + " instead." );
        }

        return currentImpl;
    }

    /**
     * @return 28 juin 2005
     */
    public static IArchivingConfigurationManager getCurrentImpl ()
    {
        return currentImpl;
    }
}
