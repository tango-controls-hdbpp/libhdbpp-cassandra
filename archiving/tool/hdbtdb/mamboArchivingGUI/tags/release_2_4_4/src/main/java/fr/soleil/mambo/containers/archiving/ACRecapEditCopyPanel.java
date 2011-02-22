/*	Synchrotron Soleil 
 *  
 *   File          :  ACRecapEditCopyPanel.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  8 d�c. 2005 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ACRecapEditCopyPanel.java,v 
 *
 */
package fr.soleil.mambo.containers.archiving;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.actions.archiving.ACRecapEditCopyValidateAction;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

/**
 * 
 * @author SOLEIL
 */
public class ACRecapEditCopyPanel extends JPanel {
	private JTextArea copyText;
	private JButton copyButton;
	private JDialog parent;

	public ACRecapEditCopyPanel(JDialog dialog) {
		parent = dialog;
		copyText = new JTextArea(ACRecapPanel.getInstance(false)
				.selectedToString());
		JScrollPane scrollpane = new JScrollPane(copyText);
		copyButton = new JButton(
				ACRecapEditCopyValidateAction
						.getInstance(
								Messages
										.getMessage("ARCHIVING_ACTION_EDIT_COPY_VALIDATE_BUTTON"),
								copyText, parent));
		GUIUtilities.setObjectBackground(copyButton,
				GUIUtilities.ARCHIVING_COPY_COLOR);
		GUIUtilities.setObjectBackground(scrollpane,
				GUIUtilities.ARCHIVING_COLOR);
		this.setLayout(new SpringLayout());
		this.add(scrollpane);
		this.add(copyButton);
		SpringUtilities.makeCompactGrid(this, 2, 1, // rows, cols
				0, 5, // initX, initY
				0, 5, // xPad, yPad
				true); // every component same size
		GUIUtilities.setObjectBackground(this, GUIUtilities.ARCHIVING_COLOR);
	}

	public String getCopyText() {
		return copyText.getText().replaceAll("\n", GUIUtilities.CRLF);
	}
}
