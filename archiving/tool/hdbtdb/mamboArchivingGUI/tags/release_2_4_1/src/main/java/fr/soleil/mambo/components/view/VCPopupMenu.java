// +======================================================================
// $Source:
// /cvsroot/tango-cs/archiving/tool/hdbtdb/mamboArchivingGUI/src/main/java/fr/soleil/mambo/components/view/VCPopupMenu.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewAttributesTreePanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================

package fr.soleil.mambo.components.view;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import fr.soleil.mambo.actions.view.VCHideAction;
import fr.soleil.mambo.actions.view.VCHideGeneralAction;
import fr.soleil.mambo.actions.view.VCRefreshAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.tools.Messages;

public class VCPopupMenu extends JPopupMenu {

    private static final long     serialVersionUID = -2311485134580310450L;
    private VCHideGeneralAction   hideShowGeneralMenuItem;
    private VCHideAction          hideShowTreeMenuItem;
    private VCRefreshAction       refreshGraphAction;
    private ViewConfigurationBean viewConfigurationBean;

    public VCPopupMenu(ViewConfigurationBean viewConfigurationBean) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        initComponents();
        addComponents();
    }

    private void addComponents() {
        this.add(refreshGraphAction);
        this.add(new JSeparator());
        this.add(hideShowGeneralMenuItem);
        this.add(hideShowTreeMenuItem);
    }

    private void initComponents() {
        String msg = Messages.getMessage("VIEW_ACTION_HIDE_GENERAL_VIEW");
        hideShowGeneralMenuItem = new VCHideGeneralAction(msg,
                viewConfigurationBean);

        msg = Messages.getMessage("VIEW_ACTION_HIDE_TREE_VIEW");
        hideShowTreeMenuItem = new VCHideAction(msg, viewConfigurationBean);

        msg = Messages.getMessage("VIEW_ACTION_REFRESH_GRAPH_VIEW");
        refreshGraphAction = new VCRefreshAction(msg, viewConfigurationBean);
    }

    public void setNameTreeMenuItem(String name) {
        hideShowTreeMenuItem.putValue(VCHideAction.NAME, name);
    }

    public void setNameGeneralMenuItem(String name) {
        hideShowGeneralMenuItem.putValue(VCHideGeneralAction.NAME, name);
    }

    public void setEnableRefreshItem(boolean enable) {
        refreshGraphAction.setEnabled(enable);
    }
}
