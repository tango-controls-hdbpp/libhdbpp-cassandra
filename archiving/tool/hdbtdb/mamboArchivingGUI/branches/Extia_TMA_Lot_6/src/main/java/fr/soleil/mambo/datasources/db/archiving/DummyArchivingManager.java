//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/db/archiving/DummyArchivingManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyDBManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: DummyArchivingManager.java,v $
// Revision 1.3  2007/03/05 16:27:33  ounsy
// non-static DataBase
//
// Revision 1.2  2006/11/20 09:37:41  ounsy
// all the methodss now throw only ArchivingExceptions
//
// Revision 1.1  2006/09/22 09:32:19  ounsy
// moved from mambo.datasources.db
//
// Revision 1.4  2006/06/20 16:04:25  ounsy
// minor changes
//
// Revision 1.3  2006/06/15 15:40:47  ounsy
// minor changes
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

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.Archiver;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

public class DummyArchivingManager implements IArchivingManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.db.IDBManager#startArchiving(mambo.data.archiving.
	 * ArchivingConfiguration)
	 */
	public void startArchiving(ArchivingConfiguration AC)
			throws ArchivingException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.db.IDBManager#stopArchiving(mambo.data.archiving.
	 * ArchivingConfiguration)
	 */
	public void stopArchiving(ArchivingConfiguration AC)
			throws ArchivingException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.db.IDBManager#getArchivingMode(java.lang.String)
	 */
	public Mode getArchivingMode(String completeName, boolean historic)
			throws ArchivingException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.db.IDBManager#isArchived(java.lang.String,
	 * boolean)
	 */
	public boolean isArchived(String completeName, boolean historic)
			throws ArchivingException {
		// TODO Auto-generated method stub
		return false;
	}

	public Archiver getCurrentArchiverForAttribute() {
		// TODO Auto-generated method stub
		return null;
	}

	public Archiver getCurrentArchiverForAttribute(String completeName,
			boolean _historic) throws ArchivingException {
		// TODO Auto-generated method stub
		return null;
	}

	public void exportData2Tdb(String attributeName, String endDate) {
		// TODO Auto-generated method stub

	}

}
