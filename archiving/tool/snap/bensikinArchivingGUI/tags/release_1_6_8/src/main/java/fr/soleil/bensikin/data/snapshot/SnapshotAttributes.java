// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/SnapshotAttributes.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SnapshotAttributes.
// (Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: SnapshotAttributes.java,v $
// Revision 1.9 2007/08/23 12:59:54 ounsy
// minor changes
//
// Revision 1.8 2006/10/31 16:54:08 ounsy
// milliseconds and null values management
//
// Revision 1.7 2006/02/15 09:20:46 ounsy
// minor changes : uncomment to debug
//
// Revision 1.6 2005/12/14 16:37:04 ounsy
// modifications to add asymmetrical snapshots comparisons
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:38 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.data.snapshot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;

/**
 * A group of snapshot attributes, rattached to a snapshot.
 * 
 * @author CLAISSE
 */
public class SnapshotAttributes {
    private SnapshotAttribute[] snapshotAttributes;
    private Snapshot            snapshot;

    /**
     * Initializes both the list of snapshot attributes and the reference to the
     * mother Snapshot.
     * 
     * @param _snapshotAttributes
     *            The list of attributes
     * @param _snapshot
     *            The mother Snapshot
     */
    public SnapshotAttributes(SnapshotAttribute[] _snapshotAttributes,
            Snapshot _snapshot) {
        this.snapshotAttributes = _snapshotAttributes;
        this.snapshot = _snapshot;
    }

    /**
     * Initializes only the reference to the mother Snapshot.
     * 
     * @param _snapshot
     *            The mother Snapshot
     */
    public SnapshotAttributes(Snapshot _snapshot) {
        this.snapshot = _snapshot;
    }

    Hashtable intersectWith(SnapshotAttributes this2) {
        Hashtable<Integer, Hashtable> ret = new Hashtable<Integer, Hashtable>(3);

        SnapshotAttribute[] attrs1 = this.snapshotAttributes;
        SnapshotAttribute[] attrs2 = this2.snapshotAttributes;
        int size1 = attrs1.length;
        int size2 = attrs2.length;
        // int intersectionSize = 0;

        Hashtable<String, SnapshotAttribute> c1 = new Hashtable<String, SnapshotAttribute>();
        Hashtable<String, SnapshotAttribute> c2 = new Hashtable<String, SnapshotAttribute>();

        Hashtable<String, SnapshotAttribute> c1Only = new Hashtable<String, SnapshotAttribute>();
        Hashtable<String, SnapshotAttribute> c2Only = new Hashtable<String, SnapshotAttribute>();
        Hashtable<String, SnapshotAttribute[]> cBoth = new Hashtable<String, SnapshotAttribute[]>();

        for (int i = 0; i < size1; i++) {
            c1.put(attrs1[i].getAttribute_complete_name(), attrs1[i]);
        }
        for (int j = 0; j < size2; j++) {
            c2.put(attrs2[j].getAttribute_complete_name(), attrs2[j]);
        }

        for (int i = 0; i < size1; i++) {
            SnapshotAttribute attr1 = attrs1[i];
            String completeName = attr1.getAttribute_complete_name();
            SnapshotAttribute attr2 = c2.get(completeName);

            if (attr2 != null) {
                SnapshotAttribute[] attrBoth = new SnapshotAttribute[2];
                attrBoth[0] = attr1;
                attrBoth[1] = attr2;
                cBoth.put(completeName, attrBoth);
            } else {
                c1Only.put(completeName, attr1);
            }
        }

        for (int j = 0; j < size2; j++) {
            SnapshotAttribute attr2 = attrs2[j];
            String completeName = attr2.getAttribute_complete_name();
            SnapshotAttribute attr1 = c1.get(completeName);

            if (attr1 != null) {
                // do nothing
            } else {
                c2Only.put(completeName, attr2);
            }
        }

        ret.put(new Integer(SnapshotComparison.ATTR_BELONGS_TO_SN1_ONLY),
                c1Only);
        ret.put(new Integer(SnapshotComparison.ATTR_BELONGS_TO_SN2_ONLY),
                c2Only);
        ret.put(new Integer(SnapshotComparison.ATTR_BELONGS_TO_BOTH), cBoth);

        /*
         * int size1Only = size1 - intersectionSize; int size2Only = size2 -
         * intersectionSize;
         */

        /*
         * These algorithms test some aspect of the composition of one or more
         * collections:
         * 
         * frequency: Counts the number of times the specified element occurs in
         * the specified collection. -->static int frequency(Collection<?> c,
         * Object o) disjoint: Determines whether two collections are disjoint,
         * in other words, whether they contain no elements in common. -->static
         * boolean disjoint(Collection<?> c1, Collection<?> c2)
         */

        // The sort algorithm can be used to sort the List prior to calling
        // binarySearch.
        // static int Collections.binarySearch(List list, Object key) ;
        return ret;
    }

    /**
     * Converts to an ArrayList of SnapAttributeExtract objects.
     * 
     * @return An ArrayList of SnapAttributeExtract objects.
     */
    ArrayList<SnapAttributeExtract> toArrayList() {
        if (snapshotAttributes == null) {
            // System.out.println("snapshotAttributes null !!!!");
            return null;
        }
        int nbOfAttributes = snapshotAttributes.length;
        ArrayList<SnapAttributeExtract> ret = new ArrayList<SnapAttributeExtract>();
        for (int i = 0; i < nbOfAttributes; i++) {
            SnapshotAttribute currentSnapshotAttribute = snapshotAttributes[i];
            SnapAttributeExtract transformedAttr = currentSnapshotAttribute
                    .toSnapAttributeExtrac();
            /*
             * if (transformedAttr.getValue() == null) {
             * System.out.println(transformedAttr.getAttribute_complete_name() +
             * " a une value null !!!!"); } else { Object[] stupidVal =
             * (Object[]) transformedAttr.getValue();
             * System.out.println("SnapshotAttributes.toArrayList()");
             * System.out.println("'''''''''''''" +
             * transformedAttr.getAttribute_complete_name());
             * System.out.println(stupidVal[0].getClass());
             * 
             * for (int j = 0; j < stupidVal.length; j++) { if (stupidVal[j] ==
             * null) { System.out.println(transformedAttr
             * .getAttribute_complete_name() +
             * "a une value null !!!! : c'est val[" + j + "]"); } else {
             * System.out.println(transformedAttr .getAttribute_complete_name()
             * + "a une value non null *** : c'est val[" + j + "]"); } }
             * 
             * }
             */
            ret.add(transformedAttr);
            // System.out.println("transformedAttr added");
        }
        return ret;
    }

    /**
     * Returns a XML representation of the attributes group.
     * 
     * @return a XML representation of the attributes group
     */
    public String toString() {
        String ret = "";

        if (this.snapshotAttributes != null) {
            for (int i = 0; i < this.snapshotAttributes.length; i++) {
                SnapshotAttribute nextValue = this.snapshotAttributes[i];

                ret += nextValue.toString();
                if (i < snapshotAttributes.length - 1) {
                    ret += GUIUtilities.CRLF;
                }
            }
        }

        return ret;
    }

    /**
     * Tests whether both groups have the same attributes
     * 
     * @param snapshotAttributes2
     *            The attributes to compare to
     * @return True if both groupss have the same attributes (including a null
     *         list)
     */
    boolean hasSameAttributesAs(SnapshotAttributes snapshotAttributes2) {
        SnapshotAttribute[] snapshotAttributesTab1 = this
                .getSnapshotAttributes();
        SnapshotAttribute[] snapshotAttributesTab2 = snapshotAttributes2
                .getSnapshotAttributes();

        if (snapshotAttributesTab1 == null) {
            return snapshotAttributesTab2 == null;
        }
        if (snapshotAttributesTab1.length != snapshotAttributesTab2.length) {
            return false;
        }

        Vector<String> v1 = new Vector<String>();
        for (int i = 0; i < snapshotAttributesTab1.length; i++) {
            String completeName = snapshotAttributesTab1[i]
                    .getAttribute_complete_name();
            v1.add(completeName);
        }

        for (int i = 0; i < snapshotAttributesTab2.length; i++) {
            String completeName = snapshotAttributesTab2[i]
                    .getAttribute_complete_name();
            if (!v1.contains(completeName)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets the modified state of the attributes group, by calling setModified
     * on each attribute
     * 
     * @param b
     *            True if the attributes are to be marked modified
     */
    void setModified(boolean b) {
        if (this.snapshotAttributes == null) {
            return;
        }
        if (this.snapshotAttributes.length == 0) {
            return;
        }

        for (int i = 0; i < this.snapshotAttributes.length; i++) {
            this.snapshotAttributes[i].setModified(b);
        }
    }

    /**
     * @return Returns the snapshot.
     */
    public Snapshot getSnapshot() {
        return snapshot;
    }

    /**
     * @param snapshot
     *            The snapshot to set.
     */
    public void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * @return Returns the snapshotAttributes.
     */
    public SnapshotAttribute[] getSnapshotAttributes() {
        return snapshotAttributes;
    }

    /**
     * @param snapshotAttributes
     *            The snapshotAttributes to set.
     */
    public void setSnapshotAttributes(SnapshotAttribute[] snapshotAttributes) {
        this.snapshotAttributes = snapshotAttributes;
    }

    /**
     * @return
     */
    public String toUserFriendlyString() {
        String ret = "";

        if (this.snapshotAttributes != null) {
            for (int i = 0; i < this.snapshotAttributes.length; i++) {
                SnapshotAttribute nextValue = this.snapshotAttributes[i];

                ret += nextValue.toUserFriendlyString();
                if (i < snapshotAttributes.length - 1) {
                    ret += GUIUtilities.CRLF;
                }
            }
        }

        return ret;
    }
}
