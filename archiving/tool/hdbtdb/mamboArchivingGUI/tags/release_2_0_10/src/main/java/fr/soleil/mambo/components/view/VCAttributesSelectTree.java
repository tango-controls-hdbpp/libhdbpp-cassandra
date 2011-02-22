// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/VCAttributesSelectTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCAttributesSelectTree.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: VCAttributesSelectTree.java,v $
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

import javax.swing.tree.TreePath;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.models.VCAttributesTreeModel;

public class VCAttributesSelectTree extends AttributesTree {

    private static final long serialVersionUID = 5099940571534300052L;

    public VCAttributesSelectTree(VCAttributesTreeModel newModel,
            ViewConfigurationBean viewConfigurationBean) {
        super(newModel);
        setCellRenderer(new VCTreeRenderer(viewConfigurationBean, true));
        setExpandsSelectedPaths(true);
        setScrollsOnExpand(true);
        setShowsRootHandles(true);
        setToggleClickCount(1);
    }

    public void setExpandedState(TreePath path, boolean state) {
        super.setExpandedState(path, state);
    }

    @Override
    public VCAttributesTreeModel getModel() {
        return (VCAttributesTreeModel) super.getModel();
    }

}
