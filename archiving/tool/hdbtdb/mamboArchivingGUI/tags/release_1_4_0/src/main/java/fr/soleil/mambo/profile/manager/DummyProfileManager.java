//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/profile/manager/DummyProfileManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyProfileManager.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: DummyProfileManager.java,v $
// Revision 1.1  2005/12/15 09:21:07  ounsy
// First Commit including profile management
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.profile.manager;

import fr.soleil.mambo.profile.Profile;

/**
 * A useless Profile ManagerS
 *
 * @author SOLEIL
 */
class DummyProfileManager implements IProfileManager
{

    public DummyProfileManager ()
    {
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#loadProfiles()
     */
    public int loadProfiles ()
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#addProfile()
     */
    public int addProfile ( String name , String path )
    {
        return -1;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#deleteProfile(int)
     */
    public void deleteProfile ( int profileId )
    {
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#getProfiles()
     */
    public Profile[] getProfiles ()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#getNewId()
     */
    public int getNewId ()
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#getSelectedProfile()
     */
    public int getSelectedProfile ()
    {
        return -1;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#setSelectedProfile(int)
     */
    public void setSelectedProfile ( int id )
    {
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#getSelectedProfilePath()
     */
    public String getSelectedProfilePath ()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.profile.manager.IProfileManager#getSelectedProfileName()
     */
    public String getSelectedProfileName ()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
