package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.thread.manager.VCRefreshThreadManager;

public class CancelGraphPanelAction extends AbstractAction {

    private static final long serialVersionUID              = -2205899496326542285L;
    private final static int  timeToWaitForRefreshingThread = 2000;

    public CancelGraphPanelAction(String name) {
        this.putValue(Action.NAME, name);
    }

    public void actionPerformed(ActionEvent e) {
        cancelRefreshingThread();
        ViewAttributesGraphPanel.getInstance().clean();
        ViewAttributesGraphPanel.getInstance().revalidate();
        ViewAttributesGraphPanel.getInstance().repaint();
    }

    private void cancelRefreshingThread() {
        if (VCRefreshThreadManager.getInstance().getRefreshThread() != null) {
            VCRefreshThreadManager.getInstance().getRefreshThread().cancel();
            try {
                VCRefreshThreadManager.getInstance().getRefreshThread().join(
                        timeToWaitForRefreshingThread);
                if (VCRefreshThreadManager.getInstance().getRefreshThread()
                        .isAlive()) {
                    VCRefreshThreadManager.getInstance().getRefreshThread()
                            .interrupt();
                }
            }
            catch (InterruptedException e) {
                // nothing to do : ignore
            }
            VCRefreshThreadManager.getInstance().setRefreshThread(null);
        }
    }

}
