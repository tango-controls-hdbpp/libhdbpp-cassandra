//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/profile/NewProfileBrowseAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  NewProfileBrowseAction.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: NewProfileBrowseAction.java,v $
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
import javax.swing.JFileChooser;

import fr.soleil.bensikin.containers.profile.ProfileNewPanel;
import fr.soleil.bensikin.containers.sub.dialogs.ProfileNewDialog;


/**
 * Action to browse folders to select a new profile path
 *
 * @author SOLEIL
 */
public class NewProfileBrowseAction extends AbstractAction
{

	/**
	 * Standard action constructor that sets the action's name
	 *
	 * @param name The name of the action
	 */
	public NewProfileBrowseAction(String name)
	{
		super(name);
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		JFileChooser chooser = new JFileChooser(System.getProperties().getProperty("user.home"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int ok = chooser.showOpenDialog(ProfileNewDialog.getInstance());
		if ( ok == JFileChooser.APPROVE_OPTION )
		{
			ProfileNewPanel.getInstance().setPath(chooser.getSelectedFile().getAbsolutePath());
		}
	}

}
