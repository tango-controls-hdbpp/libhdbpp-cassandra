//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/snapshot/OpenedSnapshotRef.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenedSnapshotRef.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: OpenedSnapshotRef.java,v $
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
 * The daughter class of SnapshotRef for opened snapshots.
 *
 * @author CLAISSE
 */
public class OpenedSnapshotRef extends SnapshotRef
{
	private String TAG_NAME = "openedSnapshot";

	/**
	 * Same as mother constructor
	 *
	 * @param _id The opened snapshot id
	 */
	public OpenedSnapshotRef(int _id)
	{
		super(_id);
	}

	/**
	 * Uses the id of <code>snapshot</code> as the reference.
	 *
	 * @param snapshot The referenced opened snapshot
	 */
	public OpenedSnapshotRef(Snapshot snapshot)
	{
		super(snapshot.getSnapshotData().getId());
	}

	/**
	 * Returns a XML representation of the opened snapshot reference
	 *
	 * @return a XML representation of the opened snapshot reference
	 */
	public String toString()
	{
		return super.toString(TAG_NAME);
	}
}
