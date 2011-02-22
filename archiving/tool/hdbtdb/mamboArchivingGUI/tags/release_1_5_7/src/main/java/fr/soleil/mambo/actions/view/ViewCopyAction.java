/*	Synchrotron Soleil 
 *  
 *   File          :  ViewCopyAction.java
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
 *   Log: ViewCopyAction.java,v 
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanScalarPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanSpectrumPanel;
import fr.soleil.mambo.tools.Messages;

public class ViewCopyAction extends AbstractAction
{

    private MamboCleanablePanel sourcePanel;
    private int                 reference;

    /**
      * @param name
      */
     public ViewCopyAction ( String name, MamboCleanablePanel sourcePanel, int reference )
     {
         super.putValue ( Action.NAME , name  );
         super.putValue ( Action.SHORT_DESCRIPTION , name  );
         this.sourcePanel = sourcePanel;
         this.reference = reference;
     }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0)
    {
        WaitingDialog.changeFirstMessage(Messages.getMessage("DIALOGS_WAITING_COPY_TABLE_TITLE"));
        WaitingDialog.changeSecondMessage(Messages.getMessage("DIALOGS_WAITING_WAIT_TABLE_TITLE"));
        WaitingDialog.openInstance();
        try
        {
            Toolkit toolkit = Toolkit.getDefaultToolkit (); 
            Clipboard clipboard = toolkit.getSystemClipboard ();
            StringSelection stringSelection;
            if (sourcePanel instanceof ViewStringStateBooleanSpectrumPanel)
            {
                switch(reference)
                {
                    case ViewStringStateBooleanSpectrumPanel.READ_REFERENCE:
                        stringSelection = new StringSelection ( ((ViewStringStateBooleanSpectrumPanel)sourcePanel).getReadValue() );
                        break;
                    case ViewStringStateBooleanSpectrumPanel.WRITE_REFERENCE:
                        stringSelection = new StringSelection ( ((ViewStringStateBooleanSpectrumPanel)sourcePanel).getWriteValue() );
                        break;
                    default:
                        stringSelection = new StringSelection ( "" );
                }
            }
            else if (sourcePanel instanceof ViewStringStateBooleanScalarPanel)
            {
                stringSelection = new StringSelection ( ((ViewStringStateBooleanScalarPanel)sourcePanel).getSelectedToString(reference) );
            }
            else
            {
                stringSelection = new StringSelection ( "" );
            }
            clipboard.setContents ( stringSelection , stringSelection );
        }
        catch(Throwable t)
        {
            if (t instanceof OutOfMemoryError)
            {
                sourcePanel.outOfMemoryErrorManagement();
            }
            else
            {
                t.printStackTrace();
            }
        }
        WaitingDialog.closeInstance();
    }
}
