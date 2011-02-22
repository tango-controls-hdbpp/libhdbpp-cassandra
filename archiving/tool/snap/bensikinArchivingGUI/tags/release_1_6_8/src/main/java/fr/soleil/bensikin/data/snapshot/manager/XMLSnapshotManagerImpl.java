//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/manager/XMLSnapshotManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLSnapshotManagerImpl.
//						(Claisse Laurent) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: XMLSnapshotManagerImpl.java,v $
// Revision 1.4  2007/04/19 13:59:13  ounsy
// added /snap to the default save location
//
// Revision 1.3  2006/11/29 10:01:57  ounsy
// minor changes
//
// Revision 1.2  2005/12/14 16:38:43  ounsy
// added methods necessary for the new Word-like file management
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.snapshot.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.components.snapshot.SnapshotFileFilter;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.xml.XMLUtils;

/**
 * An XML implementation. Saves and loads snapshots to/from XML files.
 * 
 * @author CLAISSE
 */
public class XMLSnapshotManagerImpl implements ISnapshotManager {

	/*
	 * private String resourceLocation; private static final String DEFAULT_FILE
	 * = "default.snap";
	 */

	private String defaultSaveLocation;
	private String saveLocation;
	private PrintWriter writer;

	/**
	 * Default constructor. Calls startUp ().
	 */
	protected XMLSnapshotManagerImpl() {
		startUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#startUp()
	 */
	public void startUp() throws IllegalStateException {
		String resourceLocation = null;
		boolean illegal = false;

		String acPath = Bensikin.getPathToResources() + "/snap";

		File f = null;
		try {
			f = new File(acPath);
			if (!f.canWrite()) {
				illegal = true;
			}
		} catch (Exception e) {
			illegal = true;
		}

		if (illegal) {
			// boolean b =
			f.mkdir();
		}
		resourceLocation = acPath/* + "/" + DEFAULT_FILE */;
		defaultSaveLocation = resourceLocation + "/snap";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.snapshot.manager.ISnapshotManager#saveSnapshot
	 * (bensikin.bensikin.data.snapshot.Snapshot, java.util.Hashtable)
	 */
	public void saveSnapshot(Snapshot _snapshot, Hashtable saveParameters)
			throws Exception {
		String _location = this.getSaveLocation();

		writer = new PrintWriter(new FileWriter(_location, false));
		GUIUtilities.write2(writer, XMLUtils.XML_HEADER, true);
		GUIUtilities.write2(writer, _snapshot.toString(), true);
		writer.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.snapshot.manager.ISnapshotManager#loadSnapshot()
	 */
	public Snapshot loadSnapshot() throws Exception {
		String _location = this.getSaveLocation();
		Snapshot ac = SnapshotXMLHelper.loadSnapshotIntoHash(_location);

		return ac;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.snapshot.manager.ISnapshotManager#getFileName()
	 */
	public String getFileName() {
		String _location = this.getSaveLocation();

		StringTokenizer st = new StringTokenizer(_location, System
				.getProperty("file.separator"));
		String fileName = "";
		while (st.hasMoreTokens()) {
			fileName = st.nextToken();
		}

		if (fileName.indexOf("/") != -1) // on windows the file.separator
		// properties is "\", but "/" is
		// also used
		{
			st = new StringTokenizer(fileName, "/");
			while (st.hasMoreTokens()) {
				fileName = st.nextToken();
			}
		}

		fileName = GUIUtilities.replace(fileName, "."
				+ SnapshotFileFilter.FILE_EXTENSION, "");

		return fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.context.manager.IContextManager#getDefaultSaveLocation
	 * ()
	 */
	public String getDefaultSaveLocation() {
		return defaultSaveLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebensikin.bensikin.data.context.manager.IContextManager#
	 * getNonDefaultSaveLocation()
	 */
	public String getNonDefaultSaveLocation() {
		return this.saveLocation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebensikin.bensikin.data.context.manager.IContextManager#
	 * setNonDefaultSaveLocation(java.lang.String)
	 */
	public void setNonDefaultSaveLocation(String _saveLocation) {
		this.saveLocation = _saveLocation;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.context.manager.IContextManager#getSaveLocation()
	 */
	public String getSaveLocation() {
		String _location = this.saveLocation == null ? this.defaultSaveLocation
				: this.saveLocation;
		return _location;
	}
}
