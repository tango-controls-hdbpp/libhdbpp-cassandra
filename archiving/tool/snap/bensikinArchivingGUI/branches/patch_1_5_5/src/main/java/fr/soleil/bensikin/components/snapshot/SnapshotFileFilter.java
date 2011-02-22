//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/snapshot/SnapshotFileFilter.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ACFileFilter.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: chinkumo $
//
//$Revision: 1.1 $
//
//$Log: SnapshotFileFilter.java,v $
//Revision 1.1  2005/11/29 18:25:13  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.snapshot;

import fr.soleil.bensikin.components.DataFileFilter;
import fr.soleil.bensikin.tools.Messages;

/**
 * A file filter used for save/load of the snapshot files
 * 
 * @author CLAISSE
 */
public class SnapshotFileFilter extends DataFileFilter {
	/**
	 * The contexts files extension "snap"
	 */
	public static final String FILE_EXTENSION = "snap";

	/**
	 * Builds with FILE_EXTENSION extension and a description found in
	 * resources.
	 */
	public SnapshotFileFilter() {
		super(FILE_EXTENSION);
		description = Messages.getMessage("FILE_CHOOSER_SNAPSHOT");
	}

}
