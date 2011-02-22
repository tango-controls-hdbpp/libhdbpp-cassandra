//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/snapshot/SnapshotHistory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotHistory.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: SnapshotHistory.java,v $
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
import fr.soleil.bensikin.history.History;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;

/**
 * A model for the historic of opened/selected snapshot. Contains references to
 * both.
 * 
 * @author CLAISSE
 */
public class SnapshotHistory {
	private SelectedSnapshotRef[] selectedSnapshotRefs;
	private OpenedSnapshotRef[] openedSnapshotRefs;

	/**
	 * Build directly from references to the selected and opened snapshots
	 * 
	 * @param _selectedSnapshotRefs
	 * @param _openedSnapshotRefs
	 */
	public SnapshotHistory(SelectedSnapshotRef[] _selectedSnapshotRefs,
			OpenedSnapshotRef[] _openedSnapshotRefs) {
		this.selectedSnapshotRefs = _selectedSnapshotRefs;
		this.openedSnapshotRefs = _openedSnapshotRefs;
	}

	/**
	 * Build indirectly from the selected snapshots and the opened snapshots.
	 * The references of those are extracted and used.
	 * 
	 * @param selectedSnapshots
	 * @param openedSnapshots
	 */
	public SnapshotHistory(Snapshot[] selectedSnapshots,
			Snapshot[] openedSnapshots) {
		if (selectedSnapshots != null) {
			this.selectedSnapshotRefs = new SelectedSnapshotRef[selectedSnapshots.length];

			for (int i = 0; i < selectedSnapshots.length; i++) {
				this.selectedSnapshotRefs[i] = new SelectedSnapshotRef(
						selectedSnapshots[i]);
			}

		}

		if (openedSnapshots != null) {
			this.openedSnapshotRefs = new OpenedSnapshotRef[openedSnapshots.length];

			for (int i = 0; i < openedSnapshots.length; i++) {
				this.openedSnapshotRefs[i] = new OpenedSnapshotRef(
						openedSnapshots[i]);
			}
		}
	}

	/**
	 * Returns a XML representation of the snapshot history
	 * 
	 * @return a XML representation of the snapshot history
	 */
	public String toString() {
		String ret = "";

		ret += new XMLLine(History.SNAPSHOTS_KEY, XMLLine.OPENING_TAG_CATEGORY);
		ret += GUIUtilities.CRLF;

		if (selectedSnapshotRefs != null) {
			ret += new XMLLine(History.SELECTED_SNAPSHOTS_KEY,
					XMLLine.OPENING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;

			for (int i = 0; i < selectedSnapshotRefs.length; i++) {
				ret += selectedSnapshotRefs[i].toString();
				ret += GUIUtilities.CRLF;
			}

			ret += new XMLLine(History.SELECTED_SNAPSHOTS_KEY,
					XMLLine.CLOSING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;
		}

		if (openedSnapshotRefs != null) {
			ret += new XMLLine(History.OPENED_SNAPSHOTS_KEY,
					XMLLine.OPENING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;

			for (int i = 0; i < openedSnapshotRefs.length; i++) {
				ret += openedSnapshotRefs[i].toString();
				ret += GUIUtilities.CRLF;
			}

			ret += new XMLLine(History.OPENED_SNAPSHOTS_KEY,
					XMLLine.CLOSING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;
		}

		ret += new XMLLine(History.SNAPSHOTS_KEY, XMLLine.CLOSING_TAG_CATEGORY);
		ret += GUIUtilities.CRLF;

		return ret;
	}

	/**
	 * @return Returns the openedSnapshotRefs.
	 */
	public OpenedSnapshotRef[] getOpenedSnapshotRefs() {
		return openedSnapshotRefs;
	}

	/**
	 * @param openedSnapshotRefs
	 *            The openedSnapshotRefs to set.
	 */
	public void setOpenedSnapshotRefs(OpenedSnapshotRef[] openedSnapshotRefs) {
		this.openedSnapshotRefs = openedSnapshotRefs;
	}

	/**
	 * @return Returns the selectedSnapshotRefs.
	 */
	public SelectedSnapshotRef[] getSelectedSnapshotRefs() {
		return selectedSnapshotRefs;
	}

	/**
	 * @param selectedSnapshotRefs
	 *            The selectedSnapshotRefs to set.
	 */
	public void setSelectedSnapshotRefs(
			SelectedSnapshotRef[] selectedSnapshotRefs) {
		this.selectedSnapshotRefs = selectedSnapshotRefs;
	}
}
