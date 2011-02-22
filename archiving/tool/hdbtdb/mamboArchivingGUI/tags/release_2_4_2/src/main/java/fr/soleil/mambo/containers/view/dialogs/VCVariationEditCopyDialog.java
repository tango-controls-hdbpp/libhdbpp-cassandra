/*
 * Synchrotron Soleil File : VCVariationEditCopyDialog.java Project : mambo
 * Description : Author : SOLEIL Original : 13 d�c. 2005 Revision: Author: Date:
 * State: Log: VCVariationEditCopyDialog.java,v
 */
package fr.soleil.mambo.containers.view.dialogs;

import javax.swing.JDialog;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.view.VCVariationEditCopyPanel;

/**
 * @author SOLEIL
 */
public class VCVariationEditCopyDialog extends JDialog {

	private static final long serialVersionUID = 5712511184265233935L;
	private VCVariationEditCopyPanel editCopyPanel;

	public VCVariationEditCopyDialog(ViewConfigurationBean viewConfigurationBean) {
		super(viewConfigurationBean.getVariationDialog(), "", true);
		editCopyPanel = new VCVariationEditCopyPanel(viewConfigurationBean);
		this.setContentPane(editCopyPanel);
		this.setBounds(viewConfigurationBean.getVariationDialog().getX(),
				viewConfigurationBean.getVariationDialog().getY(), 500, 300);
	}

	public VCVariationEditCopyPanel getEditCopyPanel() {
		return editCopyPanel;
	}

}
