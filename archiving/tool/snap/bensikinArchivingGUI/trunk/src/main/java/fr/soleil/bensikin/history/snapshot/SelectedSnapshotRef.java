// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/snapshot/SelectedSnapshotRef.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SelectedSnapshotRef.
// (Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: SelectedSnapshotRef.java,v $
// Revision 1.4 2005/11/29 18:25:27 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:39 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.history.snapshot;

import java.util.Hashtable;

import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.history.History;
import fr.soleil.bensikin.xml.XMLLine;

/**
 * The daughter class of SnapshotRef for selected snapshots.
 * 
 * @author CLAISSE
 */
public class SelectedSnapshotRef extends SnapshotRef {

    private String  TAG_NAME     = "selectedSnapshot";

    private boolean hightlighted = false;

    /**
     * Same as mother constructor
     * 
     * @param _id
     *            The selected context id
     */
    public SelectedSnapshotRef(int _id) {
        super(_id);
    }

    public SelectedSnapshotRef(int _id, boolean highlighted) {
        super(_id);
        this.hightlighted = highlighted;
    }

    /**
     * Uses the id of <code>snapshot</code> as the reference.
     * 
     * @param snapshot
     *            The referenced selected snapshot
     */
    public SelectedSnapshotRef(Snapshot snapshot) {
        super(snapshot.getSnapshotData().getId());
    }

    /**
     * Returns a XML representation of the selected snapshot reference
     * 
     * @return a XML representation of the selected snapshot reference
     */
    public String toString() {
        return super.toString(TAG_NAME);
    }

    /**
     * Returns a XML representation of the selected snapshot reference
     * 
     * @param isHighlighted
     *            Whether it's highlighted or not
     * @return a XML representation of the selected snapshot reference
     */
    public String toString(boolean isHighlighted) {
        Hashtable<String, String> attributes = new Hashtable<String, String>();

        if (id != -1) {
            attributes.put(History.ID_KEY, String.valueOf(this.id));
            if (isHighlighted) {
                attributes.put(History.HIGHLIGHTED_KEY, String
                        .valueOf(isHighlighted));
            }
        }

        XMLLine ret = new XMLLine(TAG_NAME, attributes);
        return ret.toString();
    }

    public boolean isHightlighted() {
        return hightlighted;
    }

    public void setHightlighted(boolean hightlighted) {
        this.hightlighted = hightlighted;
    }
}
