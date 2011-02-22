//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/Tools/ConfigBuilder.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ConfigBuilder.
//						(Chinkumo Jean) - Apr 28, 2004
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: ConfigBuilder.java,v $
// Revision 1.6  2007/03/05 16:25:19  ounsy
// non-static DataBase
//
// Revision 1.5  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.4.10.3  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.4.10.2  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.4.10.1  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.4  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.3.6.2  2005/06/13 13:44:39  chinkumo
// Changes made to improve the management of exceptions were reported here.
//
// Revision 1.3.6.1  2005/05/11 15:58:44  chinkumo
// The unused method named 'splitHdbAttributeLightMode' was simply removed.
//
// Revision 1.3  2005/02/04 17:10:37  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/31 15:09:04  chinkumo
// Changes made since the TdbProxy class was changed into the DbProxy class.
//
// Revision 1.1  2004/12/06 16:43:25  chinkumo
// First commit (new architecture).
//
// Revision 1.5  2004/09/14 06:56:09  chinkumo
// Some unused 'import' were removed.
// Some error messages were re-written to fit the 'error policy' recently decided.
//
// Revision 1.4  2004/09/01 15:51:40  chinkumo
// Heading was updated.
// Minor changes were made.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package TdbArchiver.Collector.Tools;

import java.util.ArrayList;

public class ConfigBuilder {

	public ConfigBuilder() {

	}

	public ArrayList getConfig(String[] argin) {
		/*
		 * ArrayList arrayList = new ArrayList();
		 * 
		 * ArchivingConfig archivingConfig = new ArchivingConfig(argin); Mode
		 * mode = archivingConfig.getMode(); String deviceIncharge =
		 * archivingConfig.getDevice_in_charge(); Enumeration attributeKeys =
		 * archivingConfig.getAttributeListKeys();
		 * 
		 * while ( attributeKeys.hasMoreElements() ) { String attributeKey = (
		 * String ) attributeKeys.nextElement(); AttributeLight attributeLight =
		 * archivingConfig.getAttribute(attributeKey); AttributeLightMode
		 * attributeLightMode = new AttributeLightMode(attributeLight ,
		 * deviceIncharge , mode); Timestamp timestamp = new
		 * Timestamp(System.currentTimeMillis());
		 * attributeLightMode.setTrigger_time(timestamp);
		 * 
		 * try { DbProxy.getInstance().insertModeRecord(attributeLightMode);
		 * arrayList.add(attributeLightMode); } catch ( ArchivingException e ) {
		 * Util.out2.println(e.toString()); } } return arrayList;
		 */
		return null;
	}
}