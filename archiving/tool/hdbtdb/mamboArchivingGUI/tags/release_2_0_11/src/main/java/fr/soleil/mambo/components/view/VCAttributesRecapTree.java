// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/VCAttributesRecapTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCAttributesRecapTree.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: VCAttributesRecapTree.java,v $
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:28:12 chinkumo
// no message
//
// Revision 1.1.2.4 2005/09/19 08:00:22 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
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
package fr.soleil.mambo.components.view;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.tree.TreePath;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.models.VCAttributesTreeModel;

public class VCAttributesRecapTree extends AttributesTree {

    private static final long          serialVersionUID = -5782169654697399327L;
    private HashMap<Integer, TreePath> expandedRowRecapTree;

    public VCAttributesRecapTree(VCAttributesTreeModel newModel,
            ViewConfigurationBean viewConfigurationBean) {
        super(newModel);
        setCellRenderer(new VCTreeRenderer(viewConfigurationBean, false));

        setExpandsSelectedPaths(true);
        setScrollsOnExpand(true);
        setShowsRootHandles(true);
        setToggleClickCount(1);
    }

    public void saveExpandedPath() {
        expandedRowRecapTree = new HashMap<Integer, TreePath>();
        for (int i = 0; i < getRowCount(); i++) {
            if (isExpanded(i)) {
                expandedRowRecapTree.put(new Integer(i), getPathForRow(i));
            }
        }
    }

    public void openExpandedPath() {
        try {
            for (int i = 0; i < getRowCount(); i++) {
                Iterator<Integer> it = expandedRowRecapTree.keySet().iterator();
                int j;
                while (it.hasNext()) {
                    j = it.next().intValue();
                    if (getPathForRow(i).toString().equals(
                            (expandedRowRecapTree.get(j).toString()))) {
                        expandPath(getPathForRow(i));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public VCAttributesTreeModel getModel() {
        return (VCAttributesTreeModel) super.getModel();
    }

}
