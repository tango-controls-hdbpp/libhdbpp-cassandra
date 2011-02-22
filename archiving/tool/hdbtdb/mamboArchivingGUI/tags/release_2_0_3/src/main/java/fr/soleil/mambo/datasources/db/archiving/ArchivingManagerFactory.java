//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/db/archiving/ArchivingManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DBManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ArchivingManagerFactory.java,v $
// Revision 1.2  2007/09/13 09:56:54  ounsy
// DBA :
// Modification in LifecycleManagerFactory to manage multiple LifeCycleManager.
// In ArchivingManagerFactory, AttributeManagerFactory, ExtractingManagerFactory add setCurrentImpl methods.
//
// Revision 1.1  2006/09/22 09:32:19  ounsy
// moved from mambo.datasources.db
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
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
package fr.soleil.mambo.datasources.db.archiving;


public class ArchivingManagerFactory
{
    public static final int DUMMY_IMPL_TYPE = 1;
    public static final int REAL_IMPL_TYPE = 2;
    public static final int REAL_IMPL_TYPE_GLOBAL_START_CALL = 3;


    private static IArchivingManager currentImpl = null;

    /**
     * @param typeOfImpl
     * @return 8 juil. 2005
     */
    public static IArchivingManager getImpl ( int typeOfImpl )
    {
        switch ( typeOfImpl )
        {
            case DUMMY_IMPL_TYPE:
                if ( currentImpl == null )
                {
                    currentImpl = new DummyArchivingManager();
                }
                break;

            case REAL_IMPL_TYPE:
                currentImpl = new BasicArchivingManager();
                break;

            case REAL_IMPL_TYPE_GLOBAL_START_CALL:
                currentImpl = new GlobalStartArchivingManager();
                break;

            default:
                throw new IllegalStateException( "Expected either DUMMY_IMPL_TYPE (1) or REAL_IMPL_TYPE (2) or REAL_IMPL_TYPE_GLOBAL_START_CALL (3), got " + typeOfImpl + " instead." );
        }

        return currentImpl;
    }

    /**
     * @return 28 juin 2005
     */
    public static IArchivingManager getCurrentImpl ()
    {
        return currentImpl;
    }

    /**
     * Change the currentImpl 
     * @param currentImpl
     */
	public static void setCurrentImpl(IArchivingManager currentImpl) {
		ArchivingManagerFactory.currentImpl = currentImpl;
	}

}
