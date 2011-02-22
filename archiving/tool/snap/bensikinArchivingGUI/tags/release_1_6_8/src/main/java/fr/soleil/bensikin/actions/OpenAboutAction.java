//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/OpenAboutAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OpenAboutAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: OpenAboutAction.java,v $
//Revision 1.2  2006/04/10 08:46:41  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.1  2005/11/29 18:25:08  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.tools.Messages;

/**
 * Opens the application's "About" popup.
 * 
 * @author CLAISSE
 */
public class OpenAboutAction extends BensikinAction {
	/**
	 * Standard action constructor that sets the action's name.
	 * 
	 * @param name
	 *            The action name
	 */
	public OpenAboutAction(String name) {
		this.putValue(Action.NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent) {
		String ok = Messages.getMessage("ABOUT_OK");
		Object[] options = { ok };
		ImageIcon myIcon = new ImageIcon(Bensikin.class
				.getResource("icons/soleil_logo-150.gif"));

		// String release = Messages.getMessage("ABOUT_RELEASE");
		// String releaseDate = Messages.getMessage("ABOUT_RELEASE_DATE");
		String release = Messages.getAppMessage("project.version");
		String releaseDate = Messages.getAppMessage("build.date");
		String author = Messages.getMessage("ABOUT_AUTHOR_LABEL")
				+ Messages.getMessage("ABOUT_AUTHOR");
		String facility = Messages.getMessage("ABOUT_FACILITY");
		String msg = Messages.getMessage("ABOUT_DESCRIPTION");
		String revision = Messages.getMessage("ABOUT_REVISION");
		String update = Messages.getMessage("ABOUT_UPDATE_LABEL")
				+ Messages.getMessage("ABOUT_UPDATE");

		msg += GUIUtilities.CRLF;
		msg += revision;
		msg += release;
		msg += " (" + releaseDate + ")";
		msg += GUIUtilities.CRLF;
		msg += author;
		msg += GUIUtilities.CRLF;
		msg += update;
		msg += GUIUtilities.CRLF;
		msg += facility;

		String title = Messages.getMessage("MENU_ABOUT");

		JOptionPane.showOptionDialog(BensikinFrame.getInstance(), msg, title,
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				myIcon, options, options[0]);
	}

}
