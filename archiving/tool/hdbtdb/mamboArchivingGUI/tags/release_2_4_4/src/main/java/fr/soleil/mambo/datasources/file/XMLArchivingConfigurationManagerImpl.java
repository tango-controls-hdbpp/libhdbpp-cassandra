//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/XMLArchivingConfigurationManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLArchivingConfigurationManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: XMLArchivingConfigurationManagerImpl.java,v $
// Revision 1.4  2007/02/01 14:16:29  pierrejoseph
// XmlHelper reorg
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
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
package fr.soleil.mambo.datasources.file;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;
import fr.soleil.mambo.tools.xmlhelpers.ac.ArchivingConfigurationXMLHelperFactory;

public class XMLArchivingConfigurationManagerImpl implements
		IArchivingConfigurationManager {
	// private String resourceLocation;
	// private static final String DEFAULT_FILE = "default.ac";
	// private static final String DEFAULT_PATH = "C:/mambo/ac";

	private String defaultSaveLocation;
	private String saveLocation;
	private PrintWriter writer;

	protected XMLArchivingConfigurationManagerImpl() {
		startUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * saveArchivingConfiguration(mambo.data.archiving.ArchivingConfiguration,
	 * java.lang.String, java.util.Hashtable)
	 */
	public void saveArchivingConfiguration(ArchivingConfiguration ac,
			Hashtable saveParameters) throws Exception {
		String _location = this.getSaveLocation();

		writer = new PrintWriter(new FileWriter(_location, false));
		GUIUtilities.write2(writer, XMLUtils.XML_HEADER, true);
		GUIUtilities.write2(writer, ac.toString(), true);
		writer.close();
	}

	/**
	 * @return 26 juil. 2005
	 * @throws FileNotFoundException
	 */
	/*
	 * private boolean fileIsEmpty () { try { BufferedReader br = new
	 * BufferedReader( new FileReader( defaultSaveLocation ) ); String s =
	 * br.readLine(); if ( s != null && !s.trim().equals( "" ) ) { return true;
	 * } } catch ( Exception e ) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return false; } return false; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * loadArchivingConfigurations
	 * (fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions)
	 */
	public ArchivingConfiguration[] loadArchivingConfigurations(
			Criterions searchCriterions) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * findArchivingConfigurations
	 * (mambo.data.archiving.ArchivingConfiguration[],
	 * fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions)
	 */
	public ArchivingConfiguration[] findArchivingConfigurations(
			ArchivingConfiguration[] in, Criterions searchCriterions)
			throws Exception {

		return null;
	}

	public void startUp() throws IllegalStateException {
		String resourceLocation = null;
		boolean illegal = false;

		String acPath = Mambo.getPathToResources() + "/ac";

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
		// resourceLocation = acPath + "/" + DEFAULT_FILE;
		resourceLocation = acPath;
		defaultSaveLocation = resourceLocation;

		// System.out.println (
		// "CLA/XMLArchivingConfigurationManagerImpl/startUp/defaultSaveLocation/"+defaultSaveLocation+"/"
		// );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.file.IArchivingConfigurationManager#shutDown()
	 */
	public void shutDown() throws Exception {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * loadArchivingConfiguration(java.lang.String)
	 */
	public ArchivingConfiguration loadArchivingConfiguration( /* String location */)
			throws Exception {
		String _location = this.getSaveLocation();
		ArchivingConfiguration ac = ArchivingConfigurationXMLHelperFactory
				.getCurrentImpl().loadArchivingConfigurationIntoHash(_location);
		// ArchivingConfiguration ac =
		// ArchivingConfigurationXMLHelper.loadArchivingConfigurationIntoHash(
		// _location );

		return ac;
	}

	/**
	 * @return Returns the defaultSaveLocation.
	 */
	public String getDefaultSaveLocation() {
		return defaultSaveLocation;
	}

	/**
	 * @param defaultSaveLocation
	 *            The defaultSaveLocation to set.
	 */
	public void setDefaultSaveLocation(String defaultSaveLocation) {
		this.defaultSaveLocation = defaultSaveLocation;
	}

	/**
	 * @return Returns the saveLocation.
	 */
	public String getNonDefaultSaveLocation() {
		return saveLocation;
	}

	/**
	 * @param saveLocation
	 *            The saveLocation to set.
	 */
	public void setNonDefaultSaveLocation(String saveLocation) {
		this.saveLocation = saveLocation;
	}

	public String getSaveLocation() {
		String _location = this.saveLocation == null ? this.defaultSaveLocation
				: this.saveLocation;
		return _location;
	}
}
