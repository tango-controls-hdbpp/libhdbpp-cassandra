// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/listeners/PossibleAttributesTreeSelectionListener.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class
// PossibleAttributesTreeSelectionListener.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: PossibleAttributesTreeSelectionListener.java,v $
// Revision 1.3 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions.archiving.listeners;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.models.ACPossibleAttributesTreeModel;
import fr.soleil.tango.util.entity.data.Attribute;

public class PossibleAttributesTreeSelectionListener implements
        TreeSelectionListener {

    private static DefaultMutableTreeNode        currentNode;
    private static ACPossibleAttributesTreeModel model;

    /*
     * (non-Javadoc)
     * @see
     * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
     * .TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent event) {
        // ACPossibleAttributesTree tree = (ACPossibleAttributesTree)
        // event.getSource ();
        AttributesTree tree = (AttributesTree) event.getSource();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();
        model = (ACPossibleAttributesTreeModel) tree.getModel();
        if (node == null) {
            return;
        }
        if (!node.isLeaf()) {
            // System.out.println(node.toString() + " is not a leaf !");
            return;
        }

        currentNode = node;
        // Object nodeInfo = node.getUserObject ();

        query(node.toString(), node.getLevel());
    }

    // Called whenever each time a node needs to be rebuilt.
    // (A query is made to the database to the current node children)
    /**
     * @param st
     * @param level
     *            8 juil. 2005
     */
    public void query(String st, int level) {
        // System.out.println("CLA/DynamicTree/MyTreeSelectionListener/query/START");
        // my statement
        if (level == 3) {
            DefaultMutableTreeNode parent = ((DefaultMutableTreeNode) currentNode
                    .getParent());
            DefaultMutableTreeNode grdparent = (DefaultMutableTreeNode) parent
                    .getParent();

            String device_name = grdparent.toString() + "/" + parent.toString()
                    + "/" + currentNode.toString();
            // System.out.println("device_name = " + device_name);

            ArrayList<String> attribute_list = dbGetAttributeList(device_name);
            if (attribute_list != null) {
                // System.out.println("Result : ");
                for (int i = 0; i < attribute_list.size(); i++) {
                    DefaultMutableTreeNode attributeNode = new DefaultMutableTreeNode(
                            (String) attribute_list.get(i));
                    currentNode.add(attributeNode);

                    TreeNode[] path = new TreeNode[5];
                    path[0] = (DefaultMutableTreeNode) model.getRoot();
                    path[1] = grdparent;
                    path[2] = parent;
                    path[3] = currentNode;
                    path[4] = attributeNode;
                    Attribute attribute = new Attribute((String) attribute_list
                            .get(i));
                    //
                    model.addAttribute(path,
                            new ArchivingConfigurationAttribute(attribute));
                    // System.out.println("[" + i + "] : " + (String)
                    // attribute_list.get(i));
                }
            }
            else {
                // System.out.println("Result : null");
            }
        }
    }

    /**
     * @param device_name
     * @return 8 juil. 2005
     */
    public ArrayList<String> dbGetAttributeList(String device_name) {
        // System.out.println("CLA/DynamicTree/MyTreeSelectionListener/dbGetAttributeList/START");
        ArrayList<String> liste_att = new ArrayList<String>(32);

        try {
            DeviceProxy deviceProxy = new DeviceProxy(device_name);
            deviceProxy.ping();
            DeviceData device_data_argin = new DeviceData();
            String[] device_data_argout;
            device_data_argin.insert("*" + device_name + "*");
            device_data_argout = deviceProxy.get_attribute_list();

            for (int i = 0; i < device_data_argout.length; i++) {
                liste_att.add(device_data_argout[i]);
            }

            liste_att.trimToSize();
            Collections.sort(liste_att, Collator.getInstance());
            return liste_att;
        }
        catch (DevFailed e) {
            return null;
        }
    }
}
