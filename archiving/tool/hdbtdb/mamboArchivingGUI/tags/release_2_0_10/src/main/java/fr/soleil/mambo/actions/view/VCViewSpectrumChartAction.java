/*
 * Synchrotron Soleil File : VCViewSpectrumChartAction.java Project : Mambo_CVS
 * Description : Author : SOLEIL Original : 27 janv. 2006 Revision: Author:
 * Date: State: Log: VCViewSpectrumChartAction.java,v
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.containers.view.dialogs.ViewSpectrumChartDialog;

/**
 * @author SOLEIL
 */
public class VCViewSpectrumChartAction extends AbstractAction {

    private static final long       serialVersionUID = 4538641746822520887L;
    private ViewSpectrumPanel       specPanel;
    private ViewSpectrumChartDialog viewSpectrumChartDialog;

    public VCViewSpectrumChartAction(ViewSpectrumPanel panel,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        specPanel = panel;
        viewSpectrumChartDialog = new ViewSpectrumChartDialog(specPanel,
                viewConfigurationBean);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        specPanel.disposeTable();
        viewSpectrumChartDialog.updateContent();
        viewSpectrumChartDialog.setVisible(true);
        viewSpectrumChartDialog.clean();
    }

    public ViewSpectrumChartDialog getViewSpectrumChartDialog() {
        return viewSpectrumChartDialog;
    }

}
