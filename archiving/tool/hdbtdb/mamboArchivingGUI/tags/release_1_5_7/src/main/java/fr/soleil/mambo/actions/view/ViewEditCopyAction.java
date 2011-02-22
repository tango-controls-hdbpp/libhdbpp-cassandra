/*	Synchrotron Soleil 
 *  
 *   File          :  ViewEditCopyAction.java
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
 *   Log: ViewEditCopyAction.java,v 
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanScalarPanel;
import fr.soleil.mambo.containers.view.ViewStringStateBooleanSpectrumPanel;
import fr.soleil.mambo.containers.view.dialogs.ViewEditCopyDialog;
import fr.soleil.mambo.tools.Messages;

public class ViewEditCopyAction extends AbstractAction
{

    private MamboCleanablePanel sourcePanel;
    private int                 reference;

    /**
      * @param name
      */
     public ViewEditCopyAction ( String name, MamboCleanablePanel sourcePanel, int reference )
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
            String copyText;
            if (sourcePanel instanceof ViewStringStateBooleanSpectrumPanel)
            {
                switch(reference)
                {
                    case ViewStringStateBooleanSpectrumPanel.READ_REFERENCE:
                        copyText = ((ViewStringStateBooleanSpectrumPanel)sourcePanel).getReadValue();
                        break;
                    case ViewStringStateBooleanSpectrumPanel.WRITE_REFERENCE:
                        copyText = ((ViewStringStateBooleanSpectrumPanel)sourcePanel).getWriteValue();
                        break;
                    default:
                        copyText = "";
                }
            }
            else if (sourcePanel instanceof ViewStringStateBooleanScalarPanel)
            {
                copyText = ((ViewStringStateBooleanScalarPanel)sourcePanel).getSelectedToString(reference);
            }
            else
            {
                copyText = "";
            }
            new ViewEditCopyDialog(
                    MamboFrame.getInstance(),
                    (String)this.getValue(Action.SHORT_DESCRIPTION), copyText
            ).setVisible(true);
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
