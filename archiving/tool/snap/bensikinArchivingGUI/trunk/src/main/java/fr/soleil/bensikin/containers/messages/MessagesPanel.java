// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/messages/MessagesPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class MessagesPanel.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: MessagesPanel.java,v $
// Revision 1.5 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:36 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.containers.messages;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.components.messages.MessagesArea;
import fr.soleil.bensikin.tools.Messages;

/**
 * Panel containing the application's logs
 * 
 * @author CLAISSE
 */
public class MessagesPanel extends JPanel {

    private static final long serialVersionUID = 4278193220674888900L;
    private final MessagesArea messagesArea;
    private static final MessagesPanel messagesPanelInstance = new MessagesPanel();

    /**
     * Instantiates itself if necessary, returns the instance.
     * 
     * @return The instance
     */
    public static MessagesPanel getInstance() {
	return messagesPanelInstance;
    }

    // public static MessagesPanel getCurrentInstance() {
    // return messagesPanelInstance;
    // }

    /**
     * Builds the panel
     */
    private MessagesPanel() {
	setLayout(new BorderLayout());
	messagesArea = new MessagesArea();
	this.add(messagesArea, BorderLayout.CENTER);

	final String msg = Messages.getMessage("LOGS_BORDER");
	final TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
		.createEtchedBorder(EtchedBorder.LOWERED), msg, TitledBorder.DEFAULT_JUSTIFICATION,
		TitledBorder.TOP);
	setBorder(tb);

	// GUIUtilities.setObjectBackground(this, GUIUtilities.MESSAGE_COLOR);
    }

    /**
     * @return The messagesArea attribute.
     */
    public MessagesArea getMessagesArea() {
	return messagesArea;
    }
}
