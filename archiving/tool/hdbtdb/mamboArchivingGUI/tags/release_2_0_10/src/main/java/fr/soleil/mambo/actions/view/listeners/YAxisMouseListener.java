// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/YAxisMouseListener.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class YAxisMouseListener.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: YAxisMouseListener.java,v $
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;

import fr.soleil.mambo.containers.view.dialogs.AxisPanel;

public class YAxisMouseListener extends MouseAdapter {

    private AxisPanel axisPanel;

    /**
     * @param i
     */
    public YAxisMouseListener(AxisPanel axisPanel) {
        super();
        this.axisPanel = axisPanel;
    }

    public void mouseClicked(MouseEvent e) {
        axisPanel.setColor(JColorChooser.showDialog(axisPanel, "Choose Color",
                axisPanel.getColor()));
    }
}
