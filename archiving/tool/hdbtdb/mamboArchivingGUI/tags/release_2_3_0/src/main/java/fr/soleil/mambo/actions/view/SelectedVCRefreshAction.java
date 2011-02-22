// +======================================================================
// $Source:
// /cvsroot/tango-cs/archiving/tool/hdbtdb/mamboArchivingGUI/src/main/java/fr/soleil/mambo/actions/view/VCRefreshAction.java,v
// $

// Project: Tango Archiving Service

// Description: Java source code for the class VCViewAction.
// (Claisse Laurent) - 5 juil. 2005

// $Author$

// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX

// -======================================================================
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.thread.VCRefreshThread;
import fr.soleil.mambo.thread.manager.VCRefreshThreadManager;

public class SelectedVCRefreshAction extends AbstractAction {

    private static final long              serialVersionUID = 6638407679145612251L;
    private static SelectedVCRefreshAction instance         = null;

    public static SelectedVCRefreshAction getInstance(String name) {
        if (instance == null) {
            instance = new SelectedVCRefreshAction(name);
        }
        return instance;
    }

    public static SelectedVCRefreshAction getInstance() {
        return instance;
    }

    /**
     * @param name
     */
    public SelectedVCRefreshAction(String name) {
        super();
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
        putValue(SelectedVCRefreshAction.SMALL_ICON, new ImageIcon(Mambo.class
                .getResource("icons/View.gif")));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (VCRefreshThreadManager.getInstance().getRefreshThread() == null) {
            ViewConfigurationBean viewConfigurationBean = ViewConfigurationBeanManager
                    .getInstance().getBeanFor(
                            ViewConfigurationBeanManager.getInstance()
                                    .getSelectedConfiguration());

            if (viewConfigurationBean != null) {
                viewConfigurationBean.initBeforeRefresh();
                VCRefreshThread refreshThread = new VCRefreshThread(
                        ViewConfigurationBeanManager.getInstance().getBeanFor(
                                ViewConfigurationBeanManager.getInstance()
                                        .getSelectedConfiguration()));
                refreshThread.start();
                VCRefreshThreadManager.getInstance().setRefreshThread(
                        refreshThread);
            }
        }
    }
}
