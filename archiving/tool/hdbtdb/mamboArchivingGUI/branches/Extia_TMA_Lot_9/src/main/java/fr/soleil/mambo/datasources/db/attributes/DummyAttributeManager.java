// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/datasources/db/attributes/DummyAttributeManager.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class DummyAttrDBManager.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: DummyAttributeManager.java,v $
// Revision 1.6 2007/01/11 14:05:46 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.5 2006/11/29 10:08:39 ounsy
// minor changes
//
// Revision 1.4 2006/11/07 14:35:08 ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.3 2006/10/30 14:57:18 ounsy
// minor change
//
// Revision 1.2 2006/10/19 12:42:48 ounsy
// added getAttributesToDedicatedArchiver ( boolean historic , boolean
// refreshArchivers )
//
// Revision 1.1 2006/09/22 09:32:19 ounsy
// moved from mambo.datasources.db
//
// Revision 1.9 2006/07/24 07:37:51 ounsy
// image support with partial loading
//
// Revision 1.8 2006/06/28 12:30:49 ounsy
// db attributes buffering
//
// Revision 1.7 2006/06/15 15:39:51 ounsy
// Added a getCurrentArchiverForAttribute method
//
// Revision 1.6 2006/05/16 12:04:37 ounsy
// added a openConnection() method
//
// Revision 1.5 2006/03/10 12:07:00 ounsy
// state and string support
//
// Revision 1.4 2006/03/07 14:37:00 ounsy
// added a isImage method
//
// Revision 1.3 2006/02/01 14:12:30 ounsy
// spectrum management
//
// Revision 1.2 2005/11/29 18:27:24 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:32 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.datasources.db.attributes;

import java.util.ArrayList;
import java.util.Vector;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManager;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.tango.util.entity.data.Domain;

public class DummyAttributeManager implements IAttributeManager {

	// private boolean isReady = false;

	public void openConnection() throws ArchivingException {
		// this.isReady = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mambo.datasources.db.IAttrDBManager#loadDomains(fr.soleil.snapArchivingApi
	 * .SnapshotingTools.Tools.Criterions, boolean, boolean)
	 */
	public Vector<Domain> loadDomains(Criterions searchCriterions,
			boolean _historic, boolean forceReload) {
		Vector<Domain> ret = new Vector<Domain>();
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.db.IAttrDBManager#getAttributes(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public ArrayList<String> getAttributes(String domain, String family,
			String member, boolean _historic) throws DevFailed,
			ArchivingException {
		return null;
	}

	// /**
	// * @param string
	// * @return 26 ao�t 2005
	// */
	/*
	 * private String parseCompleteName ( String completeName ) {
	 * StringTokenizer st = new StringTokenizer( completeName , "/" ); String
	 * ret = ""; while ( true ) { try { ret = st.nextToken(); } catch (
	 * NoSuchElementException nsee ) { break; } } return ret; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.db.IAttrDBManager#isScalar(java.lang.String,
	 * boolean)
	 */
	public boolean isScalar(String completeName, boolean _historic) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.db.IAttrDBManager#isSpectrum(java.lang.String,
	 * boolean)
	 */
	public boolean isSpectrum(String completeName, boolean _historic) {
		return false;
	}

	public int getDataType(String completeName, boolean _historic) {
		return -1;
	}

	public int getDataWritable(String completeName, boolean _historic) {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.db.IAttrDBManager#isImage(java.lang.String,
	 * boolean)
	 */
	public boolean isImage(String completeName, boolean _historic) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getDisplayFormat(String completeName, boolean _historic) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFormat(String name, boolean historic) {
		// TODO Auto-generated method stub
		return 0;
	}

	public DataBaseManager getDataBaseApi(boolean historic) throws DevFailed,
			ArchivingException {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector<Domain> loadDomains(boolean b, boolean c) {
		// TODO Auto-generated method stub
		return null;
	}

}
