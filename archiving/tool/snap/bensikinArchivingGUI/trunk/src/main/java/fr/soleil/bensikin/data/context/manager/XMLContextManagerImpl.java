//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/manager/XMLContextManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLContextManagerImpl.
//						(Claisse Laurent) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: XMLContextManagerImpl.java,v $
// Revision 1.4  2007/04/19 13:59:02  ounsy
// added /ctx to the default save location
//
// Revision 1.3  2006/11/29 10:00:45  ounsy
// minor changes
//
// Revision 1.2  2005/12/14 16:35:03  ounsy
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
package fr.soleil.bensikin.data.context.manager;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.xml.XMLUtils;

/**
 * An XML implementation. Saves and loads contexts to/from XML files.
 * 
 * @author CLAISSE
 */
public class XMLContextManagerImpl implements IContextManager {

	/*
	 * private String resourceLocation; private static final String DEFAULT_FILE
	 * = "default.ctx";
	 */

	private String defaultSaveLocation;
	private String saveLocation;
	private PrintWriter writer;

	/**
	 * Default constructor. Calls startUp ().
	 */
	protected XMLContextManagerImpl() {
		startUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.data.context.manager.IContextManager#startUp()
	 */
	public void startUp() throws IllegalStateException {
		String resourceLocation = null;
		boolean illegal = false;

		String acPath = Bensikin.getPathToResources() + "/ctx";

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
		defaultSaveLocation = resourceLocation + "/ctx";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.context.manager.IContextManager#saveContext(bensikin
	 * .bensikin.data.context.Context, java.util.Hashtable)
	 */
	public void saveContext(Context _context, Hashtable saveParameters)
			throws Exception {
		String _location = this.getSaveLocation();

		// System.out.println (
		// "XMLContextManagerImpl/saveContext/_location/"+_location+"/" );
		writer = new PrintWriter(new FileWriter(_location, false));
		GUIUtilities.write2(writer, XMLUtils.XML_HEADER, true);
		GUIUtilities.write2(writer, _context.toString(), true);
		writer.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.data.context.manager.IContextManager#shutDown()
	 */
	public void shutDown() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.data.context.manager.IContextManager#loadContext()
	 */
	public Context loadContext() throws Exception {
		String _location = this.getSaveLocation();
		Context ac = ContextXMLHelper.loadContextIntoHash(_location);

		return ac;
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
