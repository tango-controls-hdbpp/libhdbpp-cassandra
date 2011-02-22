package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.sub.dialogs.SetEquipmentArgumentsDialog;


/**
 * Sets the  values of all Tango attributes contained in the selected snapshot, to the given command.
 * <UL>
 * <LI> Opens a confirmation popup; if the user cancels, does nothing
 * <LI> Gets the selected snapshot
 * <LI> Calls setEquipmentWithCommand on it
 * <LI> Logs the action's success or failure
 * </UL>
 *
 * @author Slim Ayadi
 */
public class SetEquipmentwithCommandAction extends BensikinAction
{

	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param _name The action name
	 */
	public SetEquipmentwithCommandAction(String _name)
	{
		this.putValue(Action.NAME , _name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		SetEquipmentArgumentsDialog commandDialog = new SetEquipmentArgumentsDialog();

		commandDialog.pack();
		commandDialog.setVisible(true);

	}
}
