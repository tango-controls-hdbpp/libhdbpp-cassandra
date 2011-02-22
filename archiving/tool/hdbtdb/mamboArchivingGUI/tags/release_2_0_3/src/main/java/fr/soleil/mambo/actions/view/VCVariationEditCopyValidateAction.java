/*	Synchrotron Soleil 
 *  
 *   File          :  VCVariationEditCopyValidateAction.java
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
 *   Log: VCVariationEditCopyValidateAction.java,v 
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JTextArea;

import fr.soleil.mambo.tools.GUIUtilities;


/**
 * 
 * @author SOLEIL
 */
public class VCVariationEditCopyValidateAction extends AbstractAction {
    private static VCVariationEditCopyValidateAction instance = null;
    private JTextArea text;
    private JDialog dialog;
    
    /**
     * @return
     */
    public static VCVariationEditCopyValidateAction getInstance ( String name, JTextArea copyText, JDialog _dialog )
    {
       instance = new VCVariationEditCopyValidateAction ( name, copyText, _dialog );
       return instance; 
    }
    
    public static VCVariationEditCopyValidateAction getInstance ()
    {
       return instance; 
    }
 
	 /**
	  * @param name
	  */
     private VCVariationEditCopyValidateAction ( String name, JTextArea copyText, JDialog _dialog )
	 {
	     super.putValue ( Action.NAME , name  );
	     super.putValue ( Action.SHORT_DESCRIPTION , name  );
	     text = copyText;
	     dialog = _dialog;
	 }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        Toolkit toolkit = Toolkit.getDefaultToolkit (); 
        Clipboard clipboard = toolkit.getSystemClipboard ();
        StringSelection stringSelection = new StringSelection ( text.getText().replaceAll("\n", GUIUtilities.CRLF) );
        clipboard.setContents ( stringSelection , stringSelection );
        dialog.setVisible(false);
    }
}
