/*	Synchrotron Soleil 
 *  
 *   File          :  ViewEditCopyDialog.java
 *  
 *   Project       :  Mambo_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  8 mars 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: ViewEditCopyDialog.java,v 
 *
 */
package fr.soleil.mambo.containers.view.dialogs;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import fr.soleil.mambo.actions.view.ViewEditCopyValidateAction;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class ViewEditCopyDialog extends JDialog
{

    private JTextArea copyText;
    private JButton copyButton;
    //private JDialog parent;
    private final static int defaultWidth = 300;
    private final static int defaultHeight = 300;

    public ViewEditCopyDialog(String name, String textEdit)
    {
        super(MamboFrame.getInstance(), name, true);
        init( textEdit );
    }
    public ViewEditCopyDialog(JDialog dialog, String name, String textEdit)
    {
        super(dialog, name, true);
        init( textEdit );
    }
    public ViewEditCopyDialog(JFrame frame, String name, String textEdit)
    {
        super(frame, name, true);
        init( textEdit );
    }

    private void init(String text)
    {
        copyText = new JTextArea(text);
        JScrollPane scrollpane = new JScrollPane(copyText);
        copyButton = new JButton(  new ViewEditCopyValidateAction( Messages.getMessage("DIALOGS_VARIATION_EDIT_COPY_VALIDATE"), copyText, this )  );
        GUIUtilities.setObjectBackground( copyButton , GUIUtilities.VIEW_COPY_COLOR );
        GUIUtilities.setObjectBackground( scrollpane , GUIUtilities.VIEW_COLOR );
        JPanel mainPanel = new JPanel();
        GUIUtilities.setObjectBackground( mainPanel , GUIUtilities.VIEW_COLOR );
        mainPanel.setLayout(new SpringLayout());
        mainPanel.add(scrollpane);
        mainPanel.add(copyButton);
        SpringUtilities.makeCompactGrid(
                mainPanel ,
                mainPanel.getComponentCount() , 1 , // rows, cols
                5 , 5 , // initX, initY
                5 , 5 , // xPad, yPad
                true // every component same size
        );
        this.setContentPane(mainPanel);
        GUIUtilities.setObjectBackground( this , GUIUtilities.VIEW_COLOR );
        this.setBounds(
                this.getParent().getX() + this.getParent().getWidth() - (defaultWidth+50),
                this.getParent().getY() + this.getParent().getHeight() - (defaultHeight+50),
                defaultWidth,
                defaultHeight
        );
    }

}
