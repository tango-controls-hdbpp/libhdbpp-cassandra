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

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.thread.VCRefreshThread;
import fr.soleil.mambo.thread.manager.VCRefreshThreadManager;

public class VCRefreshAction extends AbstractAction {

    private static final long     serialVersionUID = 6638407679145612251L;
    private boolean               started;
    private ViewConfigurationBean viewConfigurationBean;

    /**
     * @param name
     */
    public VCRefreshAction(String name,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
        started = false;
        this.viewConfigurationBean = viewConfigurationBean;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if ((VCRefreshThreadManager.getInstance().getRefreshThread() == null)
                && !started) {
            setEnabled(false);
            started = true;
            WaitingDialog.openInstance();
            try {
                VCRefreshThread refreshThread = new VCRefreshThread(
                        viewConfigurationBean);
                refreshThread.start();
                VCRefreshThreadManager.getInstance().setRefreshThread(
                        refreshThread);
                refreshThread.join();
                refreshThread = null;
            }
            catch (InterruptedException e) {
                // Nothing to do: the Thread was interrupted by cancel action
            }
            catch (Throwable t) {
                // This is a real problem: trace it
                t.printStackTrace();
            }
            finally {
                VCRefreshThreadManager.getInstance().setRefreshThread(null);
                started = false;
                WaitingDialog.closeInstance();
                setEnabled(true);
            }
        }
    }

}
