package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewSpectrumStackPanel;
import fr.soleil.mambo.containers.view.dialogs.ViewSpectrumStackChartDialog;
import fr.soleil.mambo.tools.Messages;

/**
 * @author awo
 */
public class VCViewSpectrumStackChartAction extends AbstractAction {

    private static final long                    serialVersionUID = 4538641746822520887L;

    private ViewSpectrumStackPanel               specPanel;
    private ViewSpectrumStackChartDialog         viewSpectrumChartDialog;
    private WeakReference<ViewConfigurationBean> viewReference;

    public VCViewSpectrumStackChartAction(ViewSpectrumStackPanel panel,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        specPanel = panel;
        viewReference = new WeakReference<ViewConfigurationBean>(
                viewConfigurationBean);
        viewSpectrumChartDialog = null;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().getClass() == JCheckBox.class) {
            if (!specPanel.getReadCheckBox() && !specPanel.getWriteCheckBox()) {
                specPanel.enableViewButton(false, true);
            } else {
                specPanel.enableViewButton(true, true);
            }
        } else if (evt.getSource().getClass() == JButton.class) {
            specPanel.enableViewButton(false, false);
            String firstMessage = WaitingDialog.getFirstMessage();
            boolean wasOpen = WaitingDialog.isInstanceVisible();
            try {
                WaitingDialog.changeFirstMessage(Messages
                        .getMessage("DIALOGS_WAITING_PREPARING_VIEW_TITLE"));
                WaitingDialog.openInstance();
                ViewConfigurationBean viewConfigurationBean = viewReference
                        .get();
                try {
                    if (viewConfigurationBean != null) {
                        viewSpectrumChartDialog = new ViewSpectrumStackChartDialog(
                                specPanel, viewConfigurationBean);
                        viewSpectrumChartDialog.updateContent();
                    }
                } finally {
                    specPanel.enableViewButton(true, false);
                    if (WaitingDialog.isInstanceVisible()) {
                        if (wasOpen) {
                            WaitingDialog.changeFirstMessage(firstMessage);
                        } else {
                            WaitingDialog.closeInstance();
                        }
                    }
                }
                viewSpectrumChartDialog.setVisible(true);
                viewSpectrumChartDialog.clean();
                viewConfigurationBean = null;
            } catch (OutOfMemoryError oome) {
                specPanel.outOfMemoryErrorManagement();
            }
        }
    }

    public ViewSpectrumStackChartDialog getViewSpectrumChartDialog() {
        return viewSpectrumChartDialog;
    }

}
