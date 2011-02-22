package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewAttributesPanel;
import fr.soleil.mambo.thread.VCRefreshThread;
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
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                cancelRefreshingThread();
            }
        });
        attributesPanel.getViewAttributesGraphPanel().clean();
        attributesPanel.getViewAttributesGraphPanel().revalidate();
        attributesPanel.getViewAttributesGraphPanel().repaint();
    }

    private void cancelRefreshingThread() {
        VCRefreshThread refreshThread = VCRefreshThreadManager.getInstance()
                .getRefreshThread();
        if (refreshThread != null) {
            refreshThread.cancel();
            try {
                refreshThread.join(timeToWaitForRefreshingThread);
                if (refreshThread.isAlive()) {
                    refreshThread.interrupt();
                }
            } catch (InterruptedException e) {
                // nothing to do : ignore
            }
            VCRefreshThreadManager.getInstance().setRefreshThread(null);
        }
        attributesPanel.getViewAttributesTreePanel().getViewActionPanel()
                .putRefreshButton();
        WaitingDialog.closeInstance();
    }

}
