/*	Synchrotron Soleil 
 *  
 *   File          :  ACRecapEditCopyValidateAction.java
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
 *   Log: ACRecapEditCopyValidateAction.java,v 
 *
 */
package fr.soleil.mambo.actions.archiving;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JTextArea;

import fr.soleil.archiving.gui.tools.GUIUtilities;

/**
 * 
 * @author SOLEIL
 */
public class ACRecapEditCopyValidateAction extends AbstractAction {
    private static ACRecapEditCopyValidateAction instance = null;
    private final JTextArea text;
    private final JDialog dialog;

    /**
     * @return
     */
    public static ACRecapEditCopyValidateAction getInstance(final String name,
	    final JTextArea copyText, final JDialog _dialog) {
	instance = new ACRecapEditCopyValidateAction(name, copyText, _dialog);
	return instance;
    }

    public static ACRecapEditCopyValidateAction getInstance() {
	return instance;
    }

    /**
     * @param name
     */
    private ACRecapEditCopyValidateAction(final String name, final JTextArea copyText,
	    final JDialog _dialog) {
	super.putValue(Action.NAME, name);
	super.putValue(Action.SHORT_DESCRIPTION, name);
	text = copyText;
	dialog = _dialog;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent arg0) {
	final Toolkit toolkit = Toolkit.getDefaultToolkit();
	final Clipboard clipboard = toolkit.getSystemClipboard();
	final StringSelection stringSelection = new StringSelection(text.getText().replaceAll("\n",
		GUIUtilities.CRLF));
	clipboard.setContents(stringSelection, stringSelection);
	dialog.setVisible(false);
    }

}
