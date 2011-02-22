/*
 * Synchrotron Soleil File : VCViewSpectrumChartAction.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 27 janv. 2006 Revision: Author:
 * Date: State: Log: VCViewSpectrumChartAction.java,v
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.containers.view.dialogs.ViewSpectrumChartDialog;
import fr.soleil.mambo.tools.Messages;

/**
 * @author SOLEIL
 */
public class VCViewSpectrumChartAction extends AbstractAction {

    private static final long       serialVersionUID = 4538641746822520887L;
    private ViewSpectrumPanel       specPanel;
    private ViewSpectrumChartDialog viewSpectrumChartDialog;

    public VCViewSpectrumChartAction(ViewSpectrumPanel panel) {
        super();
        specPanel = panel;
        viewSpectrumChartDialog = null;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            specPanel.disposeTable();
            specPanel.enableViewButton(false, false);

            String firstMessage = WaitingDialog.getFirstMessage();
            boolean wasOpen = WaitingDialog.isInstanceVisible();
            WaitingDialog.changeFirstMessage(Messages
                    .getMessage("DIALOGS_WAITING_PREPARING_VIEW_TITLE"));
            WaitingDialog.openInstance();

            try {
                viewSpectrumChartDialog = new ViewSpectrumChartDialog(
                        specPanel);
                viewSpectrumChartDialog.updateContent();
            } finally {
                specPanel.enableViewButton(true, false);
                if (WaitingDialog.isInstanceVisible()) {
                    if (wasOpen) {
                        WaitingDialog.changeFirstMessage(firstMessage);
                    } else {
                        // Close WaitingDialog before displaying
                        // viewSpectrumChartDialog, even in case of error
                        WaitingDialog.closeInstance();
                    }
                }
            }
            viewSpectrumChartDialog.setVisible(true);
            viewSpectrumChartDialog.clean();
        } catch (OutOfMemoryError oome) {
            // Out of memory error on loading spectrum chart
            // destroy spectrum char dialog and clean memory with garbage
            // collector
            viewSpectrumChartDialog = null;
            System.gc();
            specPanel.outOfMemoryErrorManagement();
        }
    }

    public ViewSpectrumChartDialog getViewSpectrumChartDialog() {
        return viewSpectrumChartDialog;
    }

}
