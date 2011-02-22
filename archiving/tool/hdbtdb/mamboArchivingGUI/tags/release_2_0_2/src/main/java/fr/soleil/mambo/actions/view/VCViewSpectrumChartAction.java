/*	Synchrotron Soleil 
 *  
 *   File          :  VCViewSpectrumChartAction.java
 *  
 *   Project       :  Mambo_CVS
 *  
 *   Description   :  
 *  
 *   Author        :  SOLEIL
 *  
 *   Original      :  27 janv. 2006 
 *  
 *   Revision:  					Author:  
 *   Date: 							State:  
 *  
 *   Log: VCViewSpectrumChartAction.java,v 
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import fr.soleil.mambo.containers.view.ViewSpectrumPanel;
import fr.soleil.mambo.containers.view.dialogs.ViewSpectrumChartDialog;


/**
 * 
 * @author SOLEIL
 */
public class VCViewSpectrumChartAction extends AbstractAction {

    private ViewSpectrumPanel specPanel;

    public VCViewSpectrumChartAction(ViewSpectrumPanel panel) {
        specPanel = panel;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        specPanel.disposeTable();
        ViewSpectrumChartDialog.getInstance(true, specPanel.getFullName(), specPanel.getSelectedReadView(), specPanel.getSelectedWriteView()).setVisible(true);
        ViewSpectrumChartDialog.clearInstance();
    }

}
