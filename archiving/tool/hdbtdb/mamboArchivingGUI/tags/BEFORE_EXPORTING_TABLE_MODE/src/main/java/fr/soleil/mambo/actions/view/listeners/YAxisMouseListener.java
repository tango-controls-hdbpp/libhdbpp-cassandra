//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/YAxisMouseListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  YAxisMouseListener.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: YAxisMouseListener.java,v $
// Revision 1.2  2005/11/29 18:27:07  chinkumo
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
package fr.soleil.mambo.actions.view.listeners;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;

import fr.soleil.mambo.containers.view.dialogs.AxisPanel;
import fr.soleil.mambo.containers.view.dialogs.Y1AxisPanel;
import fr.soleil.mambo.containers.view.dialogs.Y2AxisPanel;


public class YAxisMouseListener extends MouseAdapter
{
    private int type;

    /**
     * @param i
     */
    public YAxisMouseListener ( int _type )
    {
        this.type = _type;
    }

    public void mouseClicked ( MouseEvent e )
    {
        Color c;

        switch ( this.type )
        {
            case AxisPanel.Y1_TYPE:
                Y1AxisPanel pane1 = Y1AxisPanel.getInstance();
                c = JColorChooser.showDialog( pane1 , "Choose Color" , pane1.getColor() );
                pane1.setColor( c );
                break;

            case AxisPanel.Y2_TYPE:
                Y2AxisPanel pane2 = Y2AxisPanel.getInstance();
                c = JColorChooser.showDialog( pane2 , "Choose Color" , pane2.getColor() );
                pane2.setColor( c );
                break;

            default:
                throw new IllegalStateException();
        }

    }
}
