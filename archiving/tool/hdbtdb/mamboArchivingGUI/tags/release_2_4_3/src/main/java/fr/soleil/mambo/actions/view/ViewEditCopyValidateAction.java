/*	Synchrotron Soleil 
 *  
 *   File          :  ViewEditCopyValidateAction.java
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
 *   Log: ViewEditCopyValidateAction.java,v 
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

import fr.soleil.archiving.gui.tools.GUIUtilities;

public class ViewEditCopyValidateAction extends AbstractAction {
    private final JTextArea text;
    private final JDialog dialog;

    /**
     * @param name
     */
    public ViewEditCopyValidateAction(final String name, final JTextArea copyText,
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
