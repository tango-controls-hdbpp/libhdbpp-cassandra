//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/OpenAboutAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenAboutAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: OpenAboutAction.java,v $
// Revision 1.4  2006/07/28 10:07:12  ounsy
// icons moved to "icons" package
//
// Revision 1.3  2005/12/15 10:38:54  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.Messages;

public class OpenAboutAction extends AbstractAction {
    /**
     * @param name
     */
    public OpenAboutAction(final String name) {
	putValue(Action.NAME, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent actionEvent) {
	final String ok = Messages.getMessage("ABOUT_OK");
	final Object[] options = { ok };
	final ImageIcon myIcon = new ImageIcon(Mambo.class.getResource("icons/soleil_logo-150.gif"));

	// String release = Messages.getMessage( "ABOUT_RELEASE" );
	// String releaseDate = Messages.getMessage( "ABOUT_RELEASE_DATE" );
	final String release = Messages.getAppMessage("project.version");
	final String releaseDate = Messages.getAppMessage("build.date");
	final String author = Messages.getMessage("ABOUT_AUTHOR_LABEL")
		+ Messages.getMessage("ABOUT_AUTHOR");
	final String facility = Messages.getMessage("ABOUT_FACILITY");
	String msg = Messages.getMessage("ABOUT_DESCRIPTION");
	final String revision = Messages.getMessage("ABOUT_REVISION");
	final String update = Messages.getMessage("ABOUT_UPDATE_LABEL")
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

	final String title = Messages.getMessage("MENU_ABOUT");

	JOptionPane.showOptionDialog(MamboFrame.getInstance(), msg, title,
		JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, myIcon, options,
		options[0]);
    }

}
