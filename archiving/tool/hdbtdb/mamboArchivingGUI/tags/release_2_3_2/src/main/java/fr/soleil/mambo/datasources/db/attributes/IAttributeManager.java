// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/datasources/db/attributes/IAttributeManager.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class IAttrDBManager.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: IAttributeManager.java,v $
// Revision 1.6 2007/02/01 14:01:50 pierrejoseph
// getAttributesToDedicatedArchiver is not useful
//
// Revision 1.5 2007/01/11 14:05:46 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.4 2006/11/09 14:23:42 ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.3 2006/11/07 14:35:08 ounsy
// Domain/Family/Member/Attribute refactoring
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
// Revision 1.8 2006/06/28 12:31:31 ounsy
// db attributes buffering
//
// Revision 1.7 2006/06/15 15:39:51 ounsy
// Added a getCurrentArchiverForAttribute method
//
// Revision 1.6 2006/05/16 12:04:27 ounsy
// added a openConnection() method
//
// Revision 1.5 2006/03/10 12:07:37 ounsy
// state and string support
//
// Revision 1.4 2006/03/07 14:37:00 ounsy
// added a isImage method
//
// Revision 1.3 2006/02/01 14:12:42 ounsy
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

public interface IAttributeManager {

	public void openConnection() throws ArchivingException;

	public Vector<Domain> loadDomains(Criterions searchCriterions,
			boolean historic, boolean forceReload) throws DevFailed,
			ArchivingException;

	public ArrayList<String> getAttributes(String domain, String family,
			String member, boolean historic) throws DevFailed,
			ArchivingException;

	public DataBaseManager getDataBaseApi(boolean historic) throws DevFailed,
			ArchivingException;

	public boolean isScalar(String completeName, boolean _historic);

	public boolean isSpectrum(String completeName, boolean _historic);

	public int getDataType(String completeName, boolean _historic);

	public int getDataWritable(String completeName, boolean _historic);

	public String getDisplayFormat(String completeName, boolean _historic);

	public boolean isImage(String completeName, boolean _historic);

	public int getFormat(String name, boolean historic);

	// public Hashtable getAttributesToDedicatedArchiver ( boolean historic ,
	// boolean refreshArchivers ) throws ArchivingException;

	public Vector<Domain> loadDomains(boolean isHistoric, boolean forceReload);
}
