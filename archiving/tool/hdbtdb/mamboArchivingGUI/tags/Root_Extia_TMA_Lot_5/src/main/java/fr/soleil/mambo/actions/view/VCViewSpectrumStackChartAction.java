package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;

import javax.swing.AbstractAction;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewSpectrumStackPanel;
import fr.soleil.mambo.containers.view.dialogs.ViewSpectrumStackChartDialog;

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
        try {
            WaitingDialog.openInstance();
            ViewConfigurationBean viewConfigurationBean = viewReference.get();
            if (viewConfigurationBean != null) {
                if (viewSpectrumChartDialog == null) {
                    viewSpectrumChartDialog = new ViewSpectrumStackChartDialog(
                            specPanel, viewConfigurationBean);
                }
                viewSpectrumChartDialog.updateContent();
                WaitingDialog.closeInstance();
                viewSpectrumChartDialog.setVisible(true);
                viewSpectrumChartDialog.clean();
            }
            viewConfigurationBean = null;
        } catch (OutOfMemoryError oome) {
            specPanel.outOfMemoryErrorManagement();
        } finally {
            WaitingDialog.closeInstance();
        }
    }

    public ViewSpectrumStackChartDialog getViewSpectrumChartDialog() {
        return viewSpectrumChartDialog;
    }

}
