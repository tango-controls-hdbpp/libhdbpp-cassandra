// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/models/VCAttributesTreeModel.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCAttributesTreeModel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.13 $
//
// $Log: VCAttributesTreeModel.java,v $
// Revision 1.13 2007/12/12 17:49:51 pierrejoseph
// HdbAvailable is stored in Mambo class.
//
// Revision 1.12 2007/11/08 18:13:54 soleilarc
// Author: XP
// Mantis bug ID: 6961
// Comment: In the VCAttributesTreeModel builder, the historic data is
// initialized according to the HDB_AVAILABLE property.
//
// Revision 1.11 2006/11/09 14:23:42 ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.10 2006/11/07 14:34:28 ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.9 2006/09/22 09:34:41 ounsy
// refactoring du package mambo.datasources.db
//
// Revision 1.8 2006/08/29 14:13:44 ounsy
// minor changes
//
// Revision 1.7 2006/08/09 16:12:54 ounsy
// No more automatic tree expanding : user has to click on a button to fully
// expand a tree
//
// Revision 1.6 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.5 2006/06/28 12:31:50 ounsy
// db attributes buffering
//
// Revision 1.4 2006/05/16 12:03:57 ounsy
// minor changes
//
// Revision 1.3 2006/03/02 15:00:40 ounsy
// refreshing view icons on start and stop
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.4 2005/09/19 08:00:22 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:44 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.models;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.esrf.Tango.DevFailed;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.comparators.MamboToStringObjectComparator;
import fr.soleil.mambo.components.view.VCAttributesSelectTree;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.tango.util.entity.data.Attribute;
import fr.soleil.tango.util.entity.data.Domain;
import fr.soleil.tango.util.entity.data.Family;
import fr.soleil.tango.util.entity.data.Member;

public class VCAttributesTreeModel extends AttributesTreeModel {

    private VCAttributesSelectTree       treeInstance = null;
    private static VCAttributesTreeModel instance     = null;
    private boolean                      historic;

    private VCAttributesTreeModel() {
        super();
        historic = Mambo.isHdbAvailable();
        /*
         * if ( Mambo.isHdbAvailable() == false ) historic = false; else
         * historic = true;
         */
        setRootName(historic);
    }

    public static VCAttributesTreeModel getInstance() {
        if (instance == null) {
            instance = new VCAttributesTreeModel();
        }

        return instance;
    }

    /**
     * @param tree
     *            8 juil. 2005
     */
    public void setTree(VCAttributesSelectTree tree) {
        this.treeInstance = tree;
    }

    public void addSelectedAttibutes(Vector validSelected) {
        TreeMap possibleAttr = VCPossibleAttributesTreeModel.getInstance()
                .getAttributes();
        TreeMap htAttr = this.getAttributes();

        Enumeration enumeration = validSelected.elements();
        while (enumeration.hasMoreElements()) {
            TreePath aPath = (TreePath) enumeration.nextElement();

            // START CURRENT SELECTED PATH
            DefaultMutableTreeNode currentTreeObject = null;
            for (int depth = 0; depth < CONTEXT_TREE_DEPTH; depth++) {
                Object currentPathObject = aPath.getPathComponent(depth);

                if (depth == 0) {
                    currentTreeObject = (DefaultMutableTreeNode) this.getRoot();
                    continue;
                }

                int numOfChildren = this.getChildCount(currentTreeObject);
                Vector vect = new Vector();
                for (int childIndex = 0; childIndex < numOfChildren; childIndex++) {
                    Object childAt = this.getChild(currentTreeObject,
                            childIndex);
                    vect.add(childAt.toString());
                }

                if (vect.contains(currentPathObject.toString())) {
                    int pos = vect.indexOf(currentPathObject.toString());
                    currentTreeObject = (DefaultMutableTreeNode) this.getChild(
                            currentTreeObject, pos);
                }
                else {
                    vect.add(currentPathObject.toString());
                    Collections.sort(vect, new MamboToStringObjectComparator());
                    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(
                            currentPathObject.toString());
                    // int index = 0;
                    int index = vect.indexOf(currentPathObject.toString());
                    this.insertNodeInto(newChild, currentTreeObject, index);

                    if (depth == CONTEXT_TREE_DEPTH - 1) {
                        TreeNode[] path = newChild.getPath();
                        // ----------------
                        TreePath treePath = new TreePath(path);
                        treeInstance.makeVisible(treePath);
                        // ----------------

                        // ViewConfigurationAttribute attribute =
                        // (ViewConfigurationAttribute) possibleAttr.get (
                        // translatePathIntoKey ( path ) );
                        Attribute _attribute = (Attribute) possibleAttr
                                .get(translatePathIntoKey(path));
                        // System.out.println (
                        // "VCAttributesTreeModel/addSelectedAttibutes/adding|"+translatePathIntoKey
                        // ( path )+"|" );
                        ViewConfigurationAttribute attribute = new ViewConfigurationAttribute(
                                _attribute);

                        if (attribute != null) {
                            htAttr.put(translatePathIntoKey(path), attribute);
                            super.addToDomains(_attribute);
                            // System.out.println (
                            // "ACAttributesTreeModel/addSelectedAttibutes/adding|"+translatePathIntoKey
                            // ( path )+"|" );
                        }

                        /*
                         * ContextAttributesPanel panel =
                         * ContextAttributesPanel.getInstance ();
                         * panel.makeVisible ( path );
                         */
                    }

                    currentTreeObject = newChild;
                }
            }
            // END CURRENT SELECTED PATH
        }
        // traceHtAttr ();
    }

    public void reload() {
        super.reload();
        /*
         * if (VCAttributesRecapTree.getInstance () != null) {
         * VCAttributesRecapTree.getInstance ().expandAll(true); }
         */
    }

    public void removeSelectedAttributes(Vector validSelected) {
        Enumeration enumeration = validSelected.elements();
        while (enumeration.hasMoreElements()) {
            TreePath aPath = (TreePath) enumeration.nextElement();
            TreeMap htAttr = this.getAttributes();

            String dom_s = aPath.getPathComponent(1).toString();
            String fam_s = aPath.getPathComponent(2).toString();
            String mem_s = aPath.getPathComponent(3).toString();
            String att_s = aPath.getPathComponent(4).toString();
            String path = dom_s + GUIUtilities.TANGO_DELIM + fam_s
                    + GUIUtilities.TANGO_DELIM + mem_s
                    + GUIUtilities.TANGO_DELIM + att_s;

            htAttr.remove(path);
            super.removeFromDomains(path);

            MutableTreeNode node = (MutableTreeNode) aPath
                    .getLastPathComponent();

            try {
                this.removeNodeFromParent(node);
            }
            catch (Exception e) {
                // do nothing
            }
        }

        this.traceHtAttr();
    }

    public void setRootName(boolean b) {
        historic = b;

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.getRoot();
        if (b) {
            root.setUserObject("HDB");
        }
        else {
            root.setUserObject("TDB");
        }

    }

    public TreeMap getAttributes() {
        return this.attributesHT;
    }

    /**
     * @return Returns the historic.
     */
    public boolean isHistoric() {
        return historic;
    }

    /**
     * @param manager
     * @param searchCriterions
     * @param _historic
     */
    public void match(IAttributeManager manager, Criterions searchCriterions,
            boolean _historic) {
        Vector<Domain> requestDomains = null;
        setRootName(historic);
        try {
            requestDomains = manager.loadDomains(searchCriterions, historic,
                    false);
        }
        catch (DevFailed e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ArchivingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // don't build immediately, first do the intersect with the current
        // model
        Vector<Domain> newDomains = intersectDomains(super.domains,
                requestDomains);

        super.removeAll();
        super.build(newDomains);
    }

    private static Vector<Domain> intersectDomains(Vector<Domain> before,
            Vector<Domain> requestDomains) {
        Vector<Domain> after = before;
        Enumeration<Domain> enumD = after.elements();
        while (enumD.hasMoreElements()) {
            Domain nextD = enumD.nextElement();
            Domain requestDomain = getDomain(requestDomains, nextD.getName());
            if (requestDomain == null) {
                after.remove(nextD);
            }
            else {
                Map<String, Family> fams = nextD.getFamilies();
                Map<String, Family> requestFams = requestDomain.getFamilies();

                Iterator<Family> enumF = fams.values().iterator();
                while (enumF.hasNext()) {
                    Family nextF = enumF.next();
                    Family requestFamily = getFamily(requestFams, nextF
                            .getName());
                    if (requestFamily == null) {
                        fams.remove(nextF);
                    }
                    else {
                        Map<String, Member> mems = nextF.getMembers();
                        Map<String, Member> requestMems = requestFamily
                                .getMembers();

                        Iterator<Member> enumM = mems.values().iterator();
                        while (enumM.hasNext()) {
                            Member nextM = enumM.next();
                            Member requestMember = getMember(requestMems, nextM
                                    .getName());
                            if (requestMember == null) {
                                mems.remove(nextM);
                            }
                            else {
                                // ----------
                                // System.out.println (
                                // "intersectDomains/requestMember.getName()/"+requestMember.getName()+"/"
                                // );
                                Map<String, Attribute> attrs = nextM
                                        .getAttributes();
                                Vector<Attribute> attrsToRemove = new Vector<Attribute>();

                                // System.out.println (
                                // "intersectDomains/attrs.size ()/"+attrs.size
                                // ()+"/" );
                                Map<String, Attribute> requestAttrs = requestMember
                                        .getAttributes();

                                Iterator<Attribute> enumA = attrs.values()
                                        .iterator();
                                while (enumA.hasNext()) {
                                    Attribute nextA = enumA.next();
                                    // System.out.println (
                                    // "intersectDomains/nextA.getName()/"+nextA.getName()+"/"
                                    // );
                                    Attribute requesteAttribute = getAttribute(
                                            requestAttrs, nextA.getName());
                                    if (requesteAttribute == null) {
                                        // attrs.remove ( nextA );
                                        attrsToRemove.add(nextA);
                                    }
                                    else {
                                        // attrs2.add ( nextA );

                                    }
                                }

                                GUIUtilities.removeVectorElements(attrs,
                                        attrsToRemove);
                            }
                        }
                    }
                }
            }
        }

        return after;
    }

    /**
     * @param requestAttrs
     * @param toTest
     * @return
     */
    private static Attribute getAttribute(Map<String, Attribute> requestAttrs,
            String toTest) {
        return requestAttrs.get(toTest);
    }

    /**
     * @param requestMems
     * @param toTest
     * @return
     */
    private static Member getMember(Map<String, Member> requestMems,
            String toTest) {
        return requestMems.get(toTest);
    }

    /**
     * @param requestDomains
     * @param toTest
     * @return
     */
    private static Domain getDomain(Vector<Domain> requestDomains, String toTest) {
        Enumeration<Domain> enumD = requestDomains.elements();
        while (enumD.hasMoreElements()) {
            Domain next = (Domain) enumD.nextElement();
            if (next.getName().equals(toTest)) {
                return next;
            }
        }

        return null;
    }

    private static Family getFamily(Map<String, Family> requestFams,
            String toTest) {
        return requestFams.get(toTest);
    }
}
