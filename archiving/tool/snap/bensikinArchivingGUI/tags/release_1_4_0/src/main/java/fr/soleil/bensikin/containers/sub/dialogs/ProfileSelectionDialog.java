//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/sub/dialogs/ProfileSelectionDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ProfileSelectionDialog.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: ProfileSelectionDialog.java,v $
// Revision 1.5  2007/08/27 14:22:30  pierrejoseph
// MultiSession display suppression at the connection step
//
// Revision 1.4  2006/06/08 09:18:46  ounsy
// "show profile path" option added
//
// Revision 1.3  2006/03/10 12:12:11  ounsy
// minor changes
//
// Revision 1.2  2005/12/16 16:29:22  ounsy
// minor changes (dialog size)
//
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

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import fr.soleil.bensikin.containers.profile.ProfileSelectionPanel;
import fr.soleil.bensikin.tools.Messages;


/**
 * A JDialog to select the profile you want to use the application with
 *
 * @author SOLEIL
 */
public class ProfileSelectionDialog extends JDialog
{

	private static ProfileSelectionDialog instance;

	public static ProfileSelectionDialog getInstance()
	{
		if ( instance == null )
		{
			instance = new ProfileSelectionDialog();
		}
		return instance;
	}

	private ProfileSelectionDialog()
	{
		super(( Frame ) null , Messages.getMessage("PROFILE_TITLE") , true);
		this.setContentPane(ProfileSelectionPanel.getInstance());
		// Size avec Multiselection
		//this.setSize(500 , 230);
		// Size sans MultiSelection
		this.setSize(450 , 200);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new KillerWindowListener());
	}

	private class KillerWindowListener extends WindowAdapter
	{
		public KillerWindowListener()
		{
			super();
		}

		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}

		public void windowClosed(WindowEvent e)
		{
			System.exit(0);
		}
	}
}

