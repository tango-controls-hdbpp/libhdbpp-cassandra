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

import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.thread.VCRefreshThread;
import fr.soleil.mambo.thread.manager.VCRefreshThreadManager;

public class VCRefreshAction extends AbstractAction {

    private static final long      serialVersionUID = 6638407679145612251L;
    private static VCRefreshAction instance;
    private boolean                started;

    public static VCRefreshAction getInstance(String name) {
        if (instance == null) {
            instance = new VCRefreshAction(name);
        }
        return instance;
    }

    public static VCRefreshAction getInstance() {
        return instance;
    }

    /**
     * @param name
     */
    private VCRefreshAction(String name) {
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
        started = false;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!started) {
            started = true;
            WaitingDialog.openInstance();
            try {
                VCRefreshThread refreshThread = new VCRefreshThread();
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
            VCRefreshThreadManager.getInstance().setRefreshThread(null);
            started = false;
            WaitingDialog.closeInstance();
        }
    }

}
