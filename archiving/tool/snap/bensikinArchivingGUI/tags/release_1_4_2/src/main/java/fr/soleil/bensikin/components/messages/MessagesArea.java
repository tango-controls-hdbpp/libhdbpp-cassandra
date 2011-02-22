//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/messages/MessagesArea.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MessagesArea.
//						(Claisse Laurent) - 13 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: MessagesArea.java,v $
// Revision 1.5  2005/11/29 18:25:27  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.messages;

import javax.swing.JTextArea;

/**
 * A JTextArea component with preset rows and columns
 *
 * @author CLAISSE
 */
public class MessagesArea extends JTextArea
{
	private static int ROWS = 5;
	private static int COLUMNS = 150;

	/**
	 * Default constructor, sets up rows and columns
	 */
	public MessagesArea()
	{
		super(MessagesArea.ROWS , MessagesArea.COLUMNS);
		this.setLineWrap(true);
	}

}
