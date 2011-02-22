//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/messages/MessagesArea.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MessagesArea.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: MessagesArea.java,v $
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
package fr.soleil.mambo.components.messages;

import javax.swing.JTextArea;

public class MessagesArea extends JTextArea
{
    private static int ROWS = 5;
    private static int COLUMNS = 150;

    /**
     * @param rows
     * @param columns
     */
    public MessagesArea ( int rows , int columns )
    {
        super( rows , columns );
        this.setLineWrap( true );
        this.setWrapStyleWord( true );
    }

    /**
     * 
     */
    public MessagesArea ()
    {
        super( MessagesArea.ROWS , MessagesArea.COLUMNS );
        this.setLineWrap( true );
        this.setWrapStyleWord( true );
    }

}
