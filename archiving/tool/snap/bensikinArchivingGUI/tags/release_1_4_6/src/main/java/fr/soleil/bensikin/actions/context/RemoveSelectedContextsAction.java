//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/RemoveSelectedContextsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  RemoveSelectedContextsAction.
//						(Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: RemoveSelectedContextsAction.java,v $
// Revision 1.2  2006/04/10 08:46:54  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2005/12/14 14:07:17  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.list.ContextListTable;
import fr.soleil.bensikin.models.ContextListTableModel;


/**
 * Removes the selected contexts from the current list
 * <UL>
 * <LI>Gets the list of selected rows from the ContextListTable instance.
 * <LI>Removes those rows from the table's model.
 * </UL>
 *
 * @author CLAISSE
 */
public class RemoveSelectedContextsAction extends BensikinAction
{
	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param name The action name
	 */
	public RemoveSelectedContextsAction(String name)
	{
		putValue(Action.SHORT_DESCRIPTION , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event)
	{
		ContextListTable source = ContextListTable.getInstance();
		int[] rows = source.getSelectedRows();

		ContextListTableModel sourceModel = ( ContextListTableModel ) source.getModel();
		sourceModel.removeRows(rows);
	}

}
