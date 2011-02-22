package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.ViewAttributesPanel;
import fr.soleil.mambo.thread.manager.VCRefreshThreadManager;

public class CancelGraphPanelAction extends AbstractAction {

    private static final long   serialVersionUID              = -2205899496326542285L;
    private final static int    timeToWaitForRefreshingThread = 2000;
    private ViewAttributesPanel attributesPanel;

    public CancelGraphPanelAction(String name,
            ViewAttributesPanel attributesPanel) {
        super();
        this.attributesPanel = attributesPanel;
        this.putValue(Action.NAME, name);
    }

    public void actionPerformed(ActionEvent e) {
        cancelRefreshingThread();
        attributesPanel.getViewAttributesGraphPanel().clean();
        attributesPanel.getViewAttributesGraphPanel().revalidate();
        attributesPanel.getViewAttributesGraphPanel().repaint();
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
