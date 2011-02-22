//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/messages/MessagesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MessagesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: MessagesPanel.java,v $
// Revision 1.4  2006/10/19 12:39:10  ounsy
// removed the border
//
// Revision 1.3  2006/09/27 09:43:23  ounsy
// added methods to log messages that arrive before the graphical component has been initiated
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
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
package fr.soleil.mambo.containers.messages;

import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JPanel;

import fr.soleil.mambo.components.messages.MessagesArea;
import fr.soleil.mambo.containers.MamboMainPanel;
import fr.soleil.mambo.tools.GUIUtilities;

public class MessagesPanel extends JPanel {
	private MessagesArea messagesArea;
	private static MessagesPanel instance = null;
	private static Vector earlyLogMessages = new Vector();
	private static boolean hasEarlyMessagesToLog = false;

	/**
	 * @return 8 juil. 2005
	 */
	public static MessagesPanel getInstance() {
		if (instance == null) {
			instance = new MessagesPanel();
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.MESSAGE_COLOR);
		}

		return instance;
	}

	/**
     * 
     */
	private MessagesPanel() {
		this.setLayout(new GridLayout(1, 0));
		messagesArea = new MessagesArea();
		this.add(messagesArea);

		/*
		 * String msg = Messages.getMessage( "LOGS_BORDER" ); TitledBorder tb =
		 * BorderFactory.createTitledBorder ( BorderFactory.createEtchedBorder(
		 * EtchedBorder.LOWERED ) , msg , TitledBorder.DEFAULT_JUSTIFICATION ,
		 * TitledBorder.TOP ); Border border = ( Border ) ( tb );
		 * this.setBorder( border );
		 */

		if (MessagesPanel.hasEarlyMessagesToLog) {
			this.pushEarlyMessages();
		}
	}

	private void pushEarlyMessages() {
		Enumeration en = earlyLogMessages.elements();
		while (en.hasMoreElements()) {
			String nextMessage = (String) en.nextElement();
			this.doLog(nextMessage);
		}
	}

	/**
	 * @return 8 juil. 2005
	 */
	public MessagesArea getMessagesArea() {
		return messagesArea;
	}

	public static void log(String message) {
		if (instance != null && instance.getMessagesArea() != null) {
			instance.doLog(message);
		} else {
			MessagesPanel.addEarlyLogMessage(message);
		}
	}

	private void doLog(String message) {
		MessagesArea messagesArea = instance.getMessagesArea();
		messagesArea.append(message);
		MamboMainPanel panel = MamboMainPanel.getInstance();
		panel.scrollDownToLatestMessage();
	}

	private static void addEarlyLogMessage(String earlyLogMessage) {
		MessagesPanel.earlyLogMessages.add(earlyLogMessage);
		MessagesPanel.hasEarlyMessagesToLog = true;
	}
}
