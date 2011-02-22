/*	Synchrotron Soleil 
 *  
 *   File          :  VCVariationEditCopyPanel.java
 *  
 *   Project       :  mambo
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  13 déc. 2005 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: VCVariationEditCopyPanel.java,v 
 *
 */
package fr.soleil.mambo.containers.view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import fr.soleil.mambo.actions.view.VCVariationEditCopyValidateAction;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


/**
 * 
 * @author SOLEIL
 */
public class VCVariationEditCopyPanel extends JPanel {
    private JTextArea copyText;
    private JButton copyButton;
    private JDialog parent;

    public VCVariationEditCopyPanel(JDialog dialog) {
        parent = dialog;
        copyText = new JTextArea(VCVariationPanel.getInstance(false).selectedToString());
        JScrollPane scrollpane = new JScrollPane(copyText);
        copyButton = new JButton(VCVariationEditCopyValidateAction.getInstance(Messages.getMessage("DIALOGS_VARIATION_EDIT_COPY_VALIDATE"), copyText, parent));
        GUIUtilities.setObjectBackground( copyButton , GUIUtilities.VIEW_COPY_COLOR );
        GUIUtilities.setObjectBackground( scrollpane , GUIUtilities.VIEW_COLOR );
        this.setLayout(new SpringLayout());
        this.add(scrollpane);
        this.add(copyButton);
        SpringUtilities.makeCompactGrid( this ,
                2 , 1 , // rows, cols
                0 , 5 , // initX, initY
                0 , 5 , // xPad, yPad
                true ); // every component same size
        GUIUtilities.setObjectBackground( this , GUIUtilities.VIEW_COLOR );
    }

    public String getCopyText() {
        return copyText.getText().replaceAll("\n", GUIUtilities.CRLF);
    }

}
