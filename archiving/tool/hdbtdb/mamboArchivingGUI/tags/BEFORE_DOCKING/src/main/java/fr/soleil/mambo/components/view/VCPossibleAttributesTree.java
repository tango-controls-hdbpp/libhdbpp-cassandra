// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/VCPossibleAttributesTree.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCPossibleAttributesTree.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: VCPossibleAttributesTree.java,v $
// Revision 1.2 2005/11/29 18:28:12 chinkumo
// no message
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

import fr.soleil.mambo.actions.view.listeners.VCPossibleAttributesTreeSelectionListener;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.models.VCPossibleAttributesTreeModel;

public class VCPossibleAttributesTree extends AttributesTree {

    private static final long serialVersionUID = 8430555379170875989L;

    public VCPossibleAttributesTree(VCPossibleAttributesTreeModel newModel,
            ViewConfigurationBean viewConfigurationBean) {
        super(newModel);
        addTreeSelectionListener(new VCPossibleAttributesTreeSelectionListener());
        setCellRenderer(new VCTreeRenderer(viewConfigurationBean, true));
        setExpandsSelectedPaths(true);
        setScrollsOnExpand(true);
        setShowsRootHandles(true);
        setToggleClickCount(1);
    }

    @Override
    public VCPossibleAttributesTreeModel getModel() {
        return (VCPossibleAttributesTreeModel) super.getModel();
    }

}
