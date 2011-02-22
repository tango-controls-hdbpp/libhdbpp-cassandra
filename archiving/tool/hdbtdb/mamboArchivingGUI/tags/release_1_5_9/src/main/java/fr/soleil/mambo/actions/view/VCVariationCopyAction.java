/*	Synchrotron Soleil 
 *  
 *   File          :  VCVariationCopyAction.java
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
 *   Log: VCVariationCopyAction.java,v 
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.VCVariationPanel;
import fr.soleil.mambo.tools.GUIUtilities;


/**
 * 
 * @author SOLEIL
 */
public class VCVariationCopyAction extends AbstractAction {
    private static VCVariationCopyAction instance = null;
    
    /**
     * @return
     */
    public static VCVariationCopyAction getInstance ( String name )
    {
       if ( instance == null )
       {
           instance = new VCVariationCopyAction ( name );
       }
        
       return instance; 
    }
    
    public static VCVariationCopyAction getInstance ()
    {
       return instance; 
    }
 
	 /**
	  * @param name
	  */
     private VCVariationCopyAction ( String name )
	 {
	     super.putValue ( Action.NAME , name  );
	     super.putValue ( Action.SHORT_DESCRIPTION , name  );
	 }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        Toolkit toolkit = Toolkit.getDefaultToolkit (); 
        Clipboard clipboard = toolkit.getSystemClipboard ();
        StringSelection stringSelection = new StringSelection ( VCVariationPanel.getInstance(false).selectedToString().replaceAll("\n", GUIUtilities.CRLF) );
        clipboard.setContents ( stringSelection , stringSelection );
    }
}
