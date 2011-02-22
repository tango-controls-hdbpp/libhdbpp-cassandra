//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/ValidateClipboardEditionAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  SetEquipmentsAction.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.3 $
//
//$Log: ValidateClipboardEditionAction.java,v $
//Revision 1.3  2007/08/22 08:01:37  ounsy
//SnapshotToTextDialog moved to the right package
//
//Revision 1.2  2006/04/10 08:47:14  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.1  2005/12/14 16:52:08  ounsy
//added methods necessary for direct clipboard edition
//
//Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.sub.dialogs.SnapshotToTextDialog;
import fr.soleil.bensikin.tools.GUIUtilities;


/**
* Sets the write values of all Tango attributes contained in the selected snapshot, with the write values of the its attributes.
* <UL>
* <LI> Opens a confirmation popup; if the user cancels, does nothing
* <LI> Gets the selected snapshot
* <LI> Calls setEquipments on it
* <LI> Logs the action's success or failure
* </UL>
*
* @author CLAISSE
*/
public class ValidateClipboardEditionAction extends BensikinAction
{ 
     private SnapshotToTextDialog dialog;
    
     /**
	  * Standard action constructor that sets the action's name.
	  *
	  * @param _name The action name
	  */
	 public ValidateClipboardEditionAction ( String _name , SnapshotToTextDialog _dialog )
	 {
	     this.putValue( Action.NAME , _name );
	     
	     this.dialog = _dialog;
	 }
	
	 /* (non-Javadoc)
	  * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	  */
	 public void actionPerformed ( ActionEvent arg0 )
	 {
	     String newContent = this.dialog.getText ();
	     GUIUtilities.setClipboardContent ( newContent );
	     
	     this.dialog.setVisible ( false );
	 }
}
