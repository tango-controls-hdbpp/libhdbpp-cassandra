//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/manager/IContextManager.java,v $
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
//$Log: IContextManager.java,v $
//Revision 1.2  2005/12/14 16:34:51  ounsy
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
package fr.soleil.bensikin.data.context.manager;

import java.util.Hashtable;

import fr.soleil.bensikin.data.context.Context;

/**
 * Defines save/load operations to a file representation of a Context
 * 
 * @author CLAISSE
 */
public interface IContextManager {
	/**
	 * Sets up the working path for context files
	 * 
	 * @throws Exception
	 */
	public void startUp() throws Exception;

	/**
	 * Saves a context to the correct location.
	 * 
	 * @param context
	 *            The Context to save
	 * @param saveParameters
	 *            Additional parameters if needed
	 * @throws Exception
	 */
	public void saveContext(Context context, Hashtable saveParameters)
			throws Exception;

	/**
	 * Loads a context from the correct location.
	 * 
	 * @return The loaded Context
	 * @throws Exception
	 */
	public Context loadContext() throws Exception;

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
