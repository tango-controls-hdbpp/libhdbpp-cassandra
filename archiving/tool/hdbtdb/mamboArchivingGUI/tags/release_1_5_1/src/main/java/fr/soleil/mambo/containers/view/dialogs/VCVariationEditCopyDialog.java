/*	Synchrotron Soleil 
 *  
 *   File          :  VCVariationEditCopyDialog.java
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
 *   Log: VCVariationEditCopyDialog.java,v 
 *
 */
package fr.soleil.mambo.containers.view.dialogs;

import javax.swing.JDialog;

import fr.soleil.mambo.containers.view.VCVariationEditCopyPanel;


/**
 * 
 * @author SOLEIL
 */
public class VCVariationEditCopyDialog extends JDialog {
    public VCVariationEditCopyDialog() {
        super(VCVariationDialog.getInstance(false),"",true);
        this.setContentPane(new VCVariationEditCopyPanel(this));
        this.setBounds(VCVariationDialog.getInstance(false).getX(),VCVariationDialog.getInstance(false).getY(),500,300);
    }
}
