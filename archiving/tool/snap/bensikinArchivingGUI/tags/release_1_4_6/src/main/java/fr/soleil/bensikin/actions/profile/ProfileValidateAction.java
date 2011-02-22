//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/profile/ProfileValidateAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileValidateAction.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ProfileValidateAction.java,v $
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
package fr.soleil.bensikin.actions.profile;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.containers.profile.ProfileSelectionListPanel;
import fr.soleil.bensikin.containers.sub.dialogs.ProfileSelectionDialog;
import fr.soleil.bensikin.profile.manager.ProfileManagerFactory;
import fr.soleil.bensikin.tools.Messages;


/**
 * @author SOLEIL
 */
public class ProfileValidateAction extends AbstractAction
{

	private static ProfileValidateAction instance;

	/**
	 * Returns the currently used ProfileValidateAction, and defines it when necessary
	 *
	 * @param name the name of the ProfileValidateAction in case of first call
	 * @return The currently used ProfileValidateAction, and defines it when necessary
	 */
	public static ProfileValidateAction getInstance(String name)
	{
		if ( instance == null )
		{
			instance = new ProfileValidateAction(name);
		}
		return instance;
	}

	/**
	 * Returns the currently used ProfileValidateAction
	 *
	 * @return The currently used ProfileValidateAction
	 */
	public static ProfileValidateAction getInstance()
	{
		return instance;
	}

	/**
	 * Standard action constructor that sets the action's name
	 *
	 * @param name The name of the action
	 */
	private ProfileValidateAction(String name)
	{
		super(name);
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		if ( validateChoice() )
		{
			ProfileManagerFactory.getCurrentImpl().setSelectedProfile(ProfileSelectionListPanel.getInstance().getSelectedProfile());
			ProfileSelectionDialog.getInstance().setVisible(false);
		}
		else
		{
			JOptionPane.showMessageDialog(ProfileSelectionDialog.getInstance() ,
			                              Messages.getMessage("PROFILE_ERROR_NO_PROFILE") ,
			                              Messages.getMessage("PROFILE_ERROR_TITLE") ,
			                              JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean validateChoice()
	{
		ProfileSelectionListPanel panel = ProfileSelectionListPanel.getInstance();
		return ( panel.getSelectedProfile() != -1 );
	}

}
