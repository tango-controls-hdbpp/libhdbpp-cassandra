// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/snapshot/SnapshotRef.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotRef.
// (Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: SnapshotRef.java,v $
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

import fr.soleil.bensikin.history.ObjectRef;

/**
 * The daughter class of ObjectRef for snapshot. Generic for selected or opened
 * snapshots.
 * 
 * @author CLAISSE
 */
public class SnapshotRef extends ObjectRef {

    /**
     * Same as mother constructor
     * 
     * @param _id
     *            The snapshot id
     */
    protected SnapshotRef(int _id) {
        super(_id);
    }

    @Override
    protected String toString(String tagName) {
        return super.toString(tagName);
    }
}
