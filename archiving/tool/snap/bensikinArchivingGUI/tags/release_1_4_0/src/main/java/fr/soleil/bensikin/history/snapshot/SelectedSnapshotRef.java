//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/snapshot/SelectedSnapshotRef.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SelectedSnapshotRef.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: SelectedSnapshotRef.java,v $
// Revision 1.4  2005/11/29 18:25:27  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:39  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.history.snapshot;

import fr.soleil.bensikin.data.snapshot.Snapshot;

/**
 * The daughter class of SnapshotRef for selected snapshots.
 *
 * @author CLAISSE
 */
public class SelectedSnapshotRef extends SnapshotRef
{
	private String TAG_NAME = "selectedSnapshot";

	/**
	 * Same as mother constructor
	 *
	 * @param _id The selected context id
	 */
	public SelectedSnapshotRef(int _id)
	{
		super(_id);
	}

	/**
	 * Uses the id of <code>snapshot</code> as the reference.
	 *
	 * @param snapshot The referenced selected snapshot
	 */
	public SelectedSnapshotRef(Snapshot snapshot)
	{
		super(snapshot.getSnapshotData().getId());
	}

	/**
	 * Returns a XML representation of the selected snapshot reference
	 *
	 * @return a XML representation of the selected snapshot reference
	 */
	public String toString()
	{
		return super.toString(TAG_NAME);
	}
}
