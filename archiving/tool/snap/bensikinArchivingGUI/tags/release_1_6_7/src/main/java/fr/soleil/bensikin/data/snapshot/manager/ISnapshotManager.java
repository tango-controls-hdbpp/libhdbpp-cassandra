//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/manager/ISnapshotManager.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  IArchivingConfigurationManager.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: ISnapshotManager.java,v $
//Revision 1.2  2005/12/14 16:38:19  ounsy
//added methods necessary for the new Word-like file management
//
//Revision 1.1  2005/12/14 14:07:18  ounsy
//first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
//under "bensikin.bensikin" and removing the same from their former locations
//
//Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.snapshot.manager;

import java.util.Hashtable;

import fr.soleil.bensikin.data.snapshot.Snapshot;

/**
 * Defines save/load operations to a file representation of a Snapshot
 * 
 * @author CLAISSE
 */
public interface ISnapshotManager {
	/**
	 * Sets up the working path for snapshot files
	 * 
	 * @throws Exception
	 */
	public void startUp() throws Exception;

	/**
	 * Saves a snapshot to the correct location.
	 * 
	 * @param snapshot
	 *            The Snapshot to save
	 * @param saveParameters
	 *            Additional parameters if needed
	 * @throws Exception
	 */
	public void saveSnapshot(Snapshot snapshot, Hashtable saveParameters)
			throws Exception;

	/**
	 * Returns the default save location used if the save location is null
	 * 
	 * @return the default save location
	 */
	// public String getDefaultSaveLocation ();
	/**
	 * Returns the save location
	 * 
	 * @return the save location
	 */
	// public String getSaveLocation ();
	/**
	 * Sets the save location
	 * 
	 * @param location
	 */
	// public void setSaveLocation ( String location );
	/**
	 * Loads a snapshot from the correct location.
	 * 
	 * @return The loaded Snapshot
	 * @throws Exception
	 */
	public Snapshot loadSnapshot() throws Exception;

	/**
	 * Returns the file name of the current save location, without the path part
	 * nor the file extension.
	 * 
	 * @return The current save location, without the path part nor the file
	 *         extension.
	 */
	public String getFileName();

	/**
	 * Returns the default save location used if the save location is null
	 * 
	 * @return the default save location
	 */
	public String getDefaultSaveLocation();

	/**
	 * Returns the non default save location
	 * 
	 * @return the default save location
	 */
	public String getNonDefaultSaveLocation();

	/**
	 * Sets the non default save location
	 * 
	 * @param location
	 *            the non default save location
	 */
	public void setNonDefaultSaveLocation(String location);

	/**
	 * Returns the save location
	 * 
	 * @return the default save location
	 */
	public String getSaveLocation();
}
