// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/OpenedVCItemListener.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class OpenedVCItemListener.
// (Claisse Laurent) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: OpenedVCItemListener.java,v $
// Revision 1.2 2008/04/09 10:45:46 achouri
// no message
//
// Revision 1.1 2005/11/29 18:27:07 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.data.view.ViewConfiguration;

public class OpenedVCItemListener implements ItemListener {

    /**
     * 
     */
    public OpenedVCItemListener() {
        super();
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        // ViewConfiguration selected = (ViewConfiguration) event.getItem();
        // if (selected != null) {
        // selected.push();
        // VCAttributesRecapTree.getInstance().expandAll(true);
        // ViewAttributesPanel.getInstance().requestToRefresh();
        // }
        if ((event != null) && (event.getStateChange() == ItemEvent.SELECTED)
                && (event.getItem() instanceof ViewConfiguration)) {
            ViewConfigurationBeanManager.getInstance()
                    .setSelectedConfiguration(
                            (ViewConfiguration) event.getItem());
        }
    }

}
