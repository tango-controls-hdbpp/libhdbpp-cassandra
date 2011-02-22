/*
 * Synchrotron Soleil File : VCVariationCopyAction.java Project : mambo
 * Description : Author : SOLEIL Original : 13 d�c. 2005 Revision: Author: Date:
 * State: Log: VCVariationCopyAction.java,v
 */
package fr.soleil.mambo.actions.view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.containers.view.VCVariationPanel;

/**
 * @author SOLEIL
 */
public class VCVariationCopyAction extends AbstractAction {

    private static final long serialVersionUID = -6897630046258245064L;
    private final VCVariationPanel variationPanel;

    /**
     * @param name
     */
    public VCVariationCopyAction(final String name, final VCVariationPanel variationPanel) {
	super();
	putValue(Action.NAME, name);
	putValue(Action.SHORT_DESCRIPTION, name);
	this.variationPanel = variationPanel;
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
	final Toolkit toolkit = Toolkit.getDefaultToolkit();
	final Clipboard clipboard = toolkit.getSystemClipboard();
	final StringSelection stringSelection = new StringSelection(variationPanel
		.selectedToString().replaceAll("\n", GUIUtilities.CRLF));
	clipboard.setContents(stringSelection, stringSelection);
    }
}
