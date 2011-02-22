/*	Synchrotron Soleil 
 *  
 *   File          :  VCVariationEditCopyAction.java
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
 *   Log: VCVariationEditCopyAction.java,v 
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.dialogs.VCVariationEditCopyDialog;


/**
 * 
 * @author SOLEIL
 */
public class VCVariationEditCopyAction extends AbstractAction {
    private static VCVariationEditCopyAction instance = null;
    
    /**
     * @return
     */
    public static VCVariationEditCopyAction getInstance ( String name )
    {
       if ( instance == null )
       {
           instance = new VCVariationEditCopyAction ( name );
       }
        
       return instance; 
    }
    
    public static VCVariationEditCopyAction getInstance ()
    {
       return instance; 
    }
 
	 /**
	  * @param name
	  */
     private VCVariationEditCopyAction ( String name )
	 {
	     super.putValue ( Action.NAME , name  );
	     super.putValue ( Action.SHORT_DESCRIPTION , name  );
	 }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        new VCVariationEditCopyDialog().setVisible(true);
    }
}
