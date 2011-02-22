//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/ProfileNewDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileNewDialog.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: ProfileNewDialog.java,v $
// Revision 1.1  2005/11/29 18:25:08  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.sub.dialogs;

import java.awt.Dimension;

import javax.swing.JDialog;

import fr.soleil.bensikin.containers.profile.ProfileNewPanel;
import fr.soleil.bensikin.tools.Messages;


/**
 * JDialog for new profile editing
 *
 * @author SOLEIL
 */
public class ProfileNewDialog extends JDialog
{

	private static ProfileNewDialog instance;

	public static ProfileNewDialog getInstance()
	{
		if ( instance == null )
		{
			instance = new ProfileNewDialog();
		}
		return instance;
	}

	private ProfileNewDialog()
	{
		super(ProfileSelectionDialog.getInstance() ,
		      Messages.getMessage("PROFILE_TITLE")
		      + " : "
		      + Messages.getMessage("PROFILE_NEW") ,
		      true);
		this.setContentPane(ProfileNewPanel.getInstance());
		this.setSize(new Dimension(300 , 120));
	}

}