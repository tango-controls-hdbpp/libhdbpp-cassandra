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

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCHideAction;
import fr.soleil.mambo.actions.view.VCHideGeneralAction;
import fr.soleil.mambo.actions.view.VCRefreshAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.tools.Messages;

public class VCPopupMenu extends JPopupMenu {

    private static final long      serialVersionUID = -2311485134580310450L;
    private JMenuItem              hideShowGeneralMenuItem;
    private JMenuItem              hideShowTreeMenuItem;
    private JMenuItem              refreshGraphMenuItem;
    private ViewConfigurationBean  viewConfigurationBean;

    private final static ImageIcon viewIcon         = new ImageIcon(
                                                            Mambo.class
                                                                    .getResource("icons/View.gif"));

    public VCPopupMenu(ViewConfigurationBean viewConfigurationBean) {
        super();

        this.viewConfigurationBean = viewConfigurationBean;

        initComponents();
        addComponents();
    }

    private void addComponents() {
        this.add(refreshGraphMenuItem);
        this.add(new JSeparator());
        this.add(hideShowGeneralMenuItem);
        this.add(hideShowTreeMenuItem);
    }

    private void initComponents() {
        String msg = Messages.getMessage("VIEW_ACTION_HIDE_GENERAL_VIEW");
        hideShowGeneralMenuItem = new JMenuItem(new VCHideGeneralAction(msg,
                viewConfigurationBean));

        msg = Messages.getMessage("VIEW_ACTION_HIDE_TREE_VIEW");
        hideShowTreeMenuItem = new JMenuItem(new VCHideAction(msg,
                viewConfigurationBean.getAttributesPanel()));

        msg = Messages.getMessage("VIEW_ACTION_REFRESH_GRAPH_VIEW");
        refreshGraphMenuItem = new JMenuItem(new VCRefreshAction(msg,
                viewConfigurationBean));
        refreshGraphMenuItem.setIcon(viewIcon);
    }

}
