// +======================================================================
// $Source:
// /cvsroot/tango-cs/archiving/tool/hdbtdb/mamboArchivingGUI/src/main/java/fr/soleil/mambo/actions/view/VCHideAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCViewAction.
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;

public class VCHideAction extends AbstractAction {

    private static final long     serialVersionUID = -292176503443738880L;
    private String                name;
    private ViewConfigurationBean viewConfigurationBean;

    public VCHideAction(String name, ViewConfigurationBean viewConfigurationBean) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        this.name = name;
        putValue(Action.NAME, this.name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (viewConfigurationBean != null) {
            viewConfigurationBean.setShowTreePanelManagement();
        }
    }
}
