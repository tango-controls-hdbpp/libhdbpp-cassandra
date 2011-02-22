//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/profile/manager/IProfileManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IProfileManager.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: IProfileManager.java,v $
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
package fr.soleil.bensikin.profile.manager;

import fr.soleil.bensikin.profile.Profile;


/**
 * Interface for profile management
 *
 * @author SOLEIL
 */
public interface IProfileManager
{

	/**
	 * Loads profile list from standard file
	 */
	public int loadProfiles();

	/**
	 * Adds a new profile in list (and in file)
	 *
	 * @param name The name of the profile
	 * @param path The path of the profile
	 * @return The id of the new Profile
	 */
	public int addProfile(String name , String path);

	/**
	 * Removes a profile from list (and from file)
	 *
	 * @param profileId ID of the profile to remove
	 */
	public void deleteProfile(int profileId);

	/**
	 * @return The available profiles
	 */
	public Profile[] getProfiles();

	/**
	 * @return An available value for a new profile id
	 */
	public int getNewId();

	/**
	 * Returns the id of the profile selected to run the application
	 *
	 * @return The id of the profile selected to run the application
	 */
	public int getSelectedProfile();

	/**
	 * Sets the id of the profile chosen to run the application
	 */
	public void setSelectedProfile(int id);

	/**
	 * Returns the path of the selected profile
	 *
	 * @return The path of the selected profile
	 */
	public String getSelectedProfilePath();

	/**
	 * Returns the name of the selected profile
	 *
	 * @return The name of the selected profile
	 */
	public String getSelectedProfileName();

}
