//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/db/archiving/BasicArchivingManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DBManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: BasicArchivingManager.java,v $
// Revision 1.6  2007/03/05 16:27:33  ounsy
// non-static DataBase
//
// Revision 1.5  2006/11/20 09:37:41  ounsy
// all the methodss now throw only ArchivingExceptions
//
// Revision 1.4  2006/10/19 12:42:21  ounsy
// minor changes
//
// Revision 1.3  2006/09/27 13:38:21  ounsy
// now uses DbConnectionManager to manage DB connecion
//
// Revision 1.2  2006/09/26 15:54:08  ounsy
// added timeOut management in  stopArchiving
//
// Revision 1.1  2006/09/22 09:32:19  ounsy
// moved from mambo.datasources.db
//
// Revision 1.5  2006/06/20 16:03:56  ounsy
// When an attribute of an archiving configuration fails to stop archiving, it does not avoid the other ones to stop
//
// Revision 1.4  2006/06/15 15:40:27  ounsy
// minor changes
//
// Revision 1.3  2006/03/10 15:49:38  ounsy
// stop only hdb or only tdb
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
// no message
//
// Revision 1.1.2.4  2005/09/26 07:52:25  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
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

import java.util.ArrayList;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseApi;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.datasources.db.DbConnectionManager;
import fr.soleil.mambo.tools.Messages;

public class BasicArchivingManager extends DbConnectionManager implements IArchivingManager
{
    protected final static String errorSeparator = "\n*----------------------------*\n";

    /* (non-Javadoc)
     * @see mambo.datasources.db.IDBManager#startArchiving(mambo.data.archiving.ArchivingConfiguration)
     */
    public void startArchiving ( ArchivingConfiguration AC ) throws ArchivingException
    {
    }
    
    /* (non-Javadoc)
     * @see mambo.datasources.db.IDBManager#stopArchiving(mambo.data.archiving.ArchivingConfiguration)
     */
    public void stopArchiving ( ArchivingConfiguration AC ) throws ArchivingException
    {
        int exceptionCounter = 1;
        String exceptionMessage = "";
        openConnection();
        
        ArchivingConfigurationAttributes ACAttributes = AC.getAttributes();
        ArchivingConfigurationAttribute[] attributesList = ACAttributes.getAttributesList();
        int nbOfAttributes = attributesList.length;
        ArrayList attributeListFinalHDB = new ArrayList();
        ArrayList attributeListFinalTDB = new ArrayList();
        
        ArchivingException toThrow = new ArchivingException ();

        for ( int i = 0 ; i < nbOfAttributes ; i++ )
        {
            ArchivingConfigurationAttribute next = attributesList[ i ];
            String nextName = next.getCompleteName();

            try
            {
                if ( this.manager.isArchived( nextName , true ) && ( AC == null || AC.isHistoric() ) )
                {
                    attributeListFinalHDB.add( nextName );
                }
            }
            catch (ArchivingException e1)
            {
                exceptionMessage = errorSeparator;
                exceptionMessage += Messages.getMessage("LOGS_ERROR_NUMBER") + exceptionCounter++ +"\n";
                exceptionMessage += Messages.getMessage("LOGS_ATTRIBUTE_IN_ERROR") + " " + nextName +"\n";
                exceptionMessage += Messages.getMessage("LOGS_CONCERNED_DATABASE") + " " + "HDB" +"\n";
                exceptionMessage += e1.getMessage();
                toThrow.addStack ( exceptionMessage , e1 );
            }

            try
            {
                if ( this.manager.isArchived( nextName , false ) && ( AC == null || !AC.isHistoric() ) )
                {
                    attributeListFinalTDB.add( nextName );
                }
            }
            catch (ArchivingException e2)
            {
                exceptionMessage = errorSeparator;
                exceptionMessage += Messages.getMessage("LOGS_ERROR_NUMBER") + exceptionCounter++ +"\n";
                exceptionMessage += Messages.getMessage("LOGS_ATTRIBUTE_IN_ERROR") + " " + nextName +"\n";
                exceptionMessage += Messages.getMessage("LOGS_CONCERNED_DATABASE") + " " + "TDB" +"\n";
                exceptionMessage += e2.getMessage();
                
                toThrow.addStack ( exceptionMessage , e2 );
            }
        }

        try
        {
            if ( !attributeListFinalHDB.isEmpty() )
            {
                String[] attributeListFinalHDB_s = new String[ 0 ];
                attributeListFinalHDB_s = ( String[] ) attributeListFinalHDB.toArray( attributeListFinalHDB_s );
                this.manager.ArchivingStop( true , attributeListFinalHDB_s );
            }
        }
        catch (ArchivingException e1)
        {
            exceptionMessage = errorSeparator;
            exceptionMessage += Messages.getMessage("LOGS_ERROR_NUMBER") + exceptionCounter++ +"\n";
            exceptionMessage += Messages.getMessage("LOGS_CONCERNED_DATABASE") + " " + "HDB" +"\n";
            exceptionMessage += e1.getMessage();
            
            toThrow.addStack ( exceptionMessage , e1 );
        }

        try
        {
            if ( !attributeListFinalTDB.isEmpty() )
            {
                String[] attributeListFinalTDB_s = new String[ 0 ];
                attributeListFinalTDB_s = ( String[] ) attributeListFinalTDB.toArray( attributeListFinalTDB_s );
                this.manager.ArchivingStop( false , attributeListFinalTDB_s );
            }
        }
        catch (ArchivingException e2)
        {
            exceptionMessage = errorSeparator;
            exceptionMessage += Messages.getMessage("LOGS_ERROR_NUMBER") + exceptionCounter++ +"\n";
            exceptionMessage += Messages.getMessage("LOGS_CONCERNED_DATABASE") + " " + "TDB" +"\n";
            exceptionMessage += e2.getMessage();
            
            toThrow.addStack ( exceptionMessage , e2 );
        }

        if (exceptionCounter > 1)
        {
            throw toThrow;
        }

    }

    /* (non-Javadoc)
     * @see mambo.datasources.db.IDBManager#getArchivingMode(java.lang.String)
     */
    public Mode getArchivingMode ( String completeName , boolean historic ) throws ArchivingException
    {
        Mode mode = this.manager.GetArchivingMode( completeName , historic );
        return mode;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.db.IDBManager#isArchived(java.lang.String, boolean)
     */
    public boolean isArchived ( String completeName , boolean historic ) throws ArchivingException
    {
        boolean ret = this.manager.isArchived( completeName , historic );
        return ret;
    }
    
    public Archiver getCurrentArchiverForAttribute ( String completeName , boolean _historic ) throws ArchivingException 
    {
        openConnection();
        DataBaseApi database = getDataBaseApi( _historic );
        
        String archiverName = database.getArchiverForAttributeEvenIfTheStopDateIsNotNull ( completeName );
        if ( archiverName == null || archiverName.equals ( "" ) )
        {
            return null;
        }
        else
        {
            Archiver ret = new Archiver ( archiverName , _historic );
            return ret;
        }
    }

    public void exportData2Tdb(String attributeName, String endDate) throws ArchivingException 
    {
        this.manager.ExportData2Tdb(attributeName, endDate);
    }
}
