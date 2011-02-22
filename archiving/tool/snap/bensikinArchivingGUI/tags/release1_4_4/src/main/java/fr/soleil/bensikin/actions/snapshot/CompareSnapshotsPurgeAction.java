//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/CompareSnapshotsPurgeAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AddSnapshotToCompareAction.
//						(Claisse Laurent) - 16 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: CompareSnapshotsPurgeAction.java,v $
//Revision 1.2  2006/04/10 08:47:14  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.1  2005/12/14 14:07:18  ounsy
//first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
//under "bensikin.bensikin" and removing the same from their former locations
//
//Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
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
import fr.soleil.bensikin.containers.snapshot.SnapshotDetailComparePanel;
import fr.soleil.bensikin.data.snapshot.Snapshot;


/**
 * Resets all Snapshots comparison references.
 * <UL>
 * <LI> Removes all static references to comparison snapshots in Snapshot
 * <LI> Resets the references display
 * </UL>
 *
 * @author CLAISSE
 */
public class CompareSnapshotsPurgeAction extends BensikinAction
{
	private static CompareSnapshotsPurgeAction instance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @param name The action's name
	 * @return The instance
	 */
	public static CompareSnapshotsPurgeAction getInstance(String name)
	{
		if ( instance == null )
		{
			instance = new CompareSnapshotsPurgeAction(name);
		}

		return instance;
	}

	/**
	 * Returns the existing instance
	 *
	 * @return The existing instance
	 */
	public static CompareSnapshotsPurgeAction getInstance()
	{
		return instance;
	}


	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param name The action name
	 */
	private CompareSnapshotsPurgeAction(String name)
	{
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	* @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	*/
	public void actionPerformed(ActionEvent arg0)
	{
		SnapshotDetailComparePanel comparePanel = SnapshotDetailComparePanel.getInstance();
		comparePanel.reset();

		Snapshot.setFirstSnapshotOfComparison(null);
		Snapshot.setSecondSnapshotOfComparison(null);
		Snapshot.setFirstSnapshotOfComparisonTitle(null);
		Snapshot.setSecondSnapshotOfComparisonTitle(null);
	}

}
