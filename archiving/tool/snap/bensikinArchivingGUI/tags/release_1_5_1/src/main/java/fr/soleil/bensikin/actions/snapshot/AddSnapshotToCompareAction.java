//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/AddSnapshotToCompareAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AddSnapshotToCompareAction.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: AddSnapshotToCompareAction.java,v $
// Revision 1.3  2006/06/28 12:45:27  ounsy
// minor changes
//
// Revision 1.2  2006/04/10 08:47:14  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JTextField;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailComparePanel;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPane;
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailTabbedPaneContent;
import fr.soleil.bensikin.data.snapshot.Snapshot;


/**
 * Adds the selected snapshot to one of the two spots for snapshots comparison.
 * <UL>
 * <LI>Gets the currently selected context, and its title.
 * <LI>Checks where the title will be set: in the first spot if available, otherwise in the second spot
 * <LI>Sets the title at this spot
 * <LI>Adds a static reference to this snapshot in Snapshot for future use
 * </UL>
 *
 * @author CLAISSE
 */
public class AddSnapshotToCompareAction extends BensikinAction
{
	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param name The action name
	 */
	public AddSnapshotToCompareAction(String name)
	{
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		SnapshotDetailTabbedPane tabbedPane = SnapshotDetailTabbedPane.getInstance();
		SnapshotDetailTabbedPaneContent content = ( SnapshotDetailTabbedPaneContent ) tabbedPane.getSelectedComponent();
		Snapshot snapshotToCompare = content.getSnapshot();

		int idx = tabbedPane.indexOfComponent(content);
		String idToAdd = tabbedPane.getTitleAt(idx);

		SnapshotDetailComparePanel comparePanel = SnapshotDetailComparePanel.getInstance();
		JTextField field1 = comparePanel.getSn1TextField();
		JTextField field2 = comparePanel.getSn2TextField();
		boolean isField1Empty = field1.getText() == null || field1.getText().trim().equals("");
		//boolean isField2Empty = field2.getText() == null || field2.getText().trim().equals("");
		JTextField fieldToFill = field1;
		boolean isFirst = true;
		if ( !isField1Empty )
		{
			fieldToFill = field2;
			isFirst = false;
		}
		fieldToFill.setText(idToAdd);

		if ( isFirst )
		{
			Snapshot.setFirstSnapshotOfComparison(snapshotToCompare);
			Snapshot.setFirstSnapshotOfComparisonTitle(idToAdd);
		}
		else
		{
			Snapshot.setSecondSnapshotOfComparison(snapshotToCompare);
			Snapshot.setSecondSnapshotOfComparisonTitle(idToAdd);
		}
	}

}
