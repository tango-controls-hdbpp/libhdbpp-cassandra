//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/profile/ProfileNewAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileNewAction.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ProfileNewAction.java,v $
// Revision 1.2  2006/12/12 13:17:49  ounsy
// minor changees
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
package fr.soleil.bensikin.actions.profile;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.bensikin.containers.sub.dialogs.ProfileNewDialog;


/**
 * @author SOLEIL
 */
public class ProfileNewAction extends AbstractAction
{

	/**
	 * Standard action constructor that sets the action's name
	 *
	 * @param name The name of the action
	 */
	public ProfileNewAction(String name)
	{
		super(name);
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		ProfileNewDialog.getInstance().setVisible ( true );
	}

}
