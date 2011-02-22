//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/profile/NewProfileValidateAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NewProfileValidateAction.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: NewProfileValidateAction.java,v $
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
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.containers.profile.ProfileNewPanel;
import fr.soleil.bensikin.containers.profile.ProfileSelectionListPanel;
import fr.soleil.bensikin.containers.sub.dialogs.ProfileNewDialog;
import fr.soleil.bensikin.containers.sub.dialogs.ProfileSelectionDialog;
import fr.soleil.bensikin.profile.manager.ProfileManagerFactory;
import fr.soleil.bensikin.tools.Messages;


/**
 * @author SOLEIL
 */
public class NewProfileValidateAction extends AbstractAction
{

	/**
	 * Standard action constructor that sets the action's name
	 *
	 * @param name The name of the action
	 */
	public NewProfileValidateAction(String name)
	{
		super(name);
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		String name = ProfileNewPanel.getInstance().getName();
		String path = ProfileNewPanel.getInstance().getPath();
		if ( validate(name , path) )
		{
			ProfileManagerFactory.getCurrentImpl().addProfile(name , path);
			ProfileSelectionListPanel.getInstance().reload();
			ProfileNewDialog.getInstance().setVisible(false);
		}
		else
		{
			JOptionPane.showMessageDialog(ProfileSelectionDialog.getInstance() ,
			                              Messages.getMessage("PROFILE_ERROR_NEW") ,
			                              Messages.getMessage("PROFILE_ERROR_TITLE") ,
			                              JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean validate(String name , String path)
	{
		if ( ( !"".equals(name.trim()) ) && ( !"".equals(path.trim()) ) )
		{
			File pathFile = new File(path.trim());
			return ( pathFile.exists() && pathFile.isDirectory() );
		}
		return false;
	}

}
