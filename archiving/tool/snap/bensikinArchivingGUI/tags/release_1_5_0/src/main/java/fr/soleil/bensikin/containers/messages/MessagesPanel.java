//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/messages/MessagesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MessagesPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: MessagesPanel.java,v $
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.messages;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.components.messages.MessagesArea;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;


/**
 * Panel containing the application's logs
 *
 * @author CLAISSE
 */
public class MessagesPanel extends JPanel
{
	private MessagesArea messagesArea;
	private static MessagesPanel messagesPanelInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static MessagesPanel getInstance()
	{
		if ( messagesPanelInstance == null )
		{
			messagesPanelInstance = new MessagesPanel();
		}

		return messagesPanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private MessagesPanel()
	{
		this.setLayout(new GridLayout(1 , 0));
		messagesArea = new MessagesArea();
		this.add(messagesArea);

		String msg = Messages.getMessage("LOGS_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder
		        (BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) ,
		         msg ,
		         TitledBorder.DEFAULT_JUSTIFICATION ,
		         TitledBorder.TOP);
		this.setBorder(tb);

		GUIUtilities.setObjectBackground(this , GUIUtilities.MESSAGE_COLOR);
	}

	/**
	 * @return The messagesArea attribute.
	 */
	public MessagesArea getMessagesArea()
	{
		return messagesArea;
	}
}
