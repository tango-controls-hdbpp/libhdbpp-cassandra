package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewSpectrumStackPanel;
import fr.soleil.mambo.containers.view.dialogs.ViewSpectrumStackChartDialog;

/**
 * @author awo
 */
public class VCViewSpectrumStackChartAction extends AbstractAction {

    private static final long            serialVersionUID = 4538641746822520887L;

    private ViewSpectrumStackPanel       specPanel;
    private ViewSpectrumStackChartDialog viewSpectrumChartDialog;

    public VCViewSpectrumStackChartAction(ViewSpectrumStackPanel panel,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        specPanel = panel;
        viewSpectrumChartDialog = new ViewSpectrumStackChartDialog(specPanel,
                viewConfigurationBean);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            WaitingDialog.openInstance();
            viewSpectrumChartDialog.updateContent();
            WaitingDialog.closeInstance();
            viewSpectrumChartDialog.setVisible(true);
            viewSpectrumChartDialog.clean();
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
