// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/VCCustomTabbedPane.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCCustomTabbedPane.
// (Claisse Laurent) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: VCCustomTabbedPane.java,v $
// Revision 1.1 2005/11/29 18:28:12 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.components.view;

import javax.swing.JTabbedPane;

import fr.soleil.mambo.actions.view.listeners.VCCustomTabbedPaneListener;
import fr.soleil.mambo.containers.view.dialogs.VCEditDialog;

public class VCCustomTabbedPane extends JTabbedPane {

    private static final long serialVersionUID = -3527775941945132318L;

    public VCCustomTabbedPane(VCEditDialog editDialog) {
        super();
        this.addPropertyChangeListener(new VCCustomTabbedPaneListener(
                editDialog));
    }

    public void setSelectedIndex(int index) {
        int oldValue = this.getSelectedIndex();
        super.setSelectedIndex(index);
        int newValue = this.getSelectedIndex();

        this.firePropertyChange(VCCustomTabbedPaneListener.SELECTED_PAGE,
                oldValue, newValue);
    }
}
