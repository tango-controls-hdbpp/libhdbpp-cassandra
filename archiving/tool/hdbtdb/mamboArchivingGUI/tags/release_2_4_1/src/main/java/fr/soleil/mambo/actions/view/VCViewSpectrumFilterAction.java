/*
 * Synchrotron Soleil
 * 
 * File : VCViewSpectrumFilterAction.java
 * 
 * Project : Mambo_CVS
 * 
 * Description :
 * 
 * Author : SOLEIL
 * 
 * Original : 1 f�vr. 2006
 * 
 * Revision: Author: Date: State:
 * 
 * Log: VCViewSpectrumFilterAction.java,v
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.ViewSpectrumPanel;

/**
 * 
 * @author SOLEIL
 */
public class VCViewSpectrumFilterAction extends AbstractAction {

    private static final long serialVersionUID = 380616203046089914L;

    private ViewSpectrumPanel panel;
    private boolean           selected;
    private boolean           isRead;

    public VCViewSpectrumFilterAction(String name, ViewSpectrumPanel _panel,
            boolean _selected, boolean isRead) {
        this.isRead = isRead;
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
        panel = _panel;
        selected = _selected;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (isRead) {
            panel.setAllReadSelected(selected);
        } else {
            panel.setAllWriteSelected(selected);
        }
    }

}
