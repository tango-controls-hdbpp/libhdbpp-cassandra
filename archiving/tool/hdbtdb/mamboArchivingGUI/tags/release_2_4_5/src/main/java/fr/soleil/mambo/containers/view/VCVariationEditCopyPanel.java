/*
 * Synchrotron Soleil File : VCVariationEditCopyPanel.java Project : mambo
 * Description : Author : SOLEIL Original : 13 d�c. 2005 Revision: Author: Date:
 * State: Log: VCVariationEditCopyPanel.java,v
 */
package fr.soleil.mambo.containers.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.actions.view.VCVariationEditCopyValidateAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * @author SOLEIL
 */
public class VCVariationEditCopyPanel extends JPanel {

	private static final long serialVersionUID = -6731675947955782622L;
	private JTextArea copyText;
	private JButton copyButton;
	private ViewConfigurationBean viewConfigurationBean;

	public VCVariationEditCopyPanel(ViewConfigurationBean viewConfigurationBean) {
		super();
		this.viewConfigurationBean = viewConfigurationBean;
		copyText = new JTextArea();
		JScrollPane scrollpane = new JScrollPane(copyText);
		copyButton = new JButton(new VCVariationEditCopyValidateAction(Messages
				.getMessage("DIALOGS_VARIATION_EDIT_COPY_VALIDATE"), copyText,
				viewConfigurationBean.getVariationDialog()));
		GUIUtilities.setObjectBackground(copyButton,
				GUIUtilities.VIEW_COPY_COLOR);
		GUIUtilities.setObjectBackground(scrollpane, GUIUtilities.VIEW_COLOR);
		this.setLayout(new SpringLayout());
		this.add(scrollpane);
		this.add(copyButton);
		SpringUtilities.makeCompactGrid(this, 2, 1, // rows, cols
				0, 5, // initX, initY
				0, 5, // xPad, yPad
				true); // every component same size
		GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
	}

	public String getCopyText() {
		return copyText.getText().replaceAll("\n", GUIUtilities.CRLF);
	}

	public void updateCopyText() {
		copyText.setText(viewConfigurationBean.getVariationDialog()
				.getVariationPanel().selectedToString());
	}

}
