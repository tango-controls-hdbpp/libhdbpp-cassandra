//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbExtractor/Proxy/DbProxy.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DbProxy.
//						(Chinkumo Jean) - 1 févr. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: DbProxy.java,v $
// Revision 1.5  2007/03/05 16:25:20  ounsy
// non-static DataBase
//
// Revision 1.4  2007/02/01 13:50:18  pierrejoseph
// ArchivingConfigureWithoutArchiverListInit instead of ArchivingConfigure
//
// Revision 1.3  2005/11/29 17:33:38  chinkumo
// no message
//
// Revision 1.2.10.2  2005/11/29 16:17:38  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.2.10.1  2005/11/15 13:45:51  chinkumo
// ...
//
// Revision 1.2  2005/06/14 10:36:05  chinkumo
// Branch (hdbExtractor_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.1.4.1  2005/06/13 14:08:05  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.1  2005/02/01 12:39:03  chinkumo
// This class replaces the HdbDbProxy class.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package HdbExtractor.Proxy;

import fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApiRefFactory;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.IArchivingManagerApiRef;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class DbProxy {
	private IArchivingManagerApiRef manager;

	public DbProxy() {
		try {
			this.manager = ArchivingManagerApiRefFactory
					.getInstance(ConfigConst.HDB);
			this.manager.ArchivingConfigureWithoutArchiverListInit(
					HdbExtractor.HdbExtractor.myDbHost,
					HdbExtractor.HdbExtractor.myDbName,
					HdbExtractor.HdbExtractor.myDbUser,
					HdbExtractor.HdbExtractor.myDbPassword,
					HdbExtractor.HdbExtractor.myRacConnection, false, false);
		} catch (ArchivingException e) {
			System.err.println(e.toString());
		}
	}

	public boolean is_db_connected() {
		return this.manager.is_db_connected();
	}

	public DataBaseManager getDataBase() {
		return this.manager.getDataBase();
	}
}
