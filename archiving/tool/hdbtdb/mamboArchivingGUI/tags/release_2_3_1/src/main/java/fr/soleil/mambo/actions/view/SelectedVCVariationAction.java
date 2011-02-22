// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCVariationAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationAction.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.2 2009/12/17 12:50:57 pierrejoseph
// CheckStyle: Organize imports / Format
//
// Revision 1.1 2009/11/24 09:53:30 soleilarc
// * Rapha�l GIRARDOT: VC details UI exported as a new Bean
//
// Revision 1.2 2006/02/01 14:05:23 ounsy
// minor changes (small date bug corrected)
//
// Revision 1.1 2005/11/29 18:27:07 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfiguration;

public class SelectedVCVariationAction extends AbstractAction {

    private static final long                serialVersionUID = -1334214425229714228L;
    private static SelectedVCVariationAction instance         = null;

    public static SelectedVCVariationAction getInstance(String name) {
        if (instance == null) {
            instance = new SelectedVCVariationAction(name);
        }
        return instance;
    }

    public static SelectedVCVariationAction getCurrentInstance() {
        return instance;
    }

    /**
     * @param name
     */
    private SelectedVCVariationAction(String name) {
        super();
        this.putValue(Action.NAME, name);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        ViewConfiguration viewConfiguration = ViewConfigurationBeanManager
                .getInstance().getSelectedConfiguration();
        ViewConfigurationBean viewConfigurationBean = ViewConfigurationBeanManager
                .getInstance().getBeanFor(viewConfiguration);
        if (viewConfigurationBean != null) {
            if (viewConfigurationBean.getViewConfiguration() != null) {
                if (viewConfigurationBean.getViewConfiguration().getData()
                        .isDynamicDateRange()) {
                    Timestamp[] range = viewConfigurationBean
                            .getViewConfiguration().getData()
                            .getDynamicStartAndEndDates();
                    viewConfigurationBean.getViewConfiguration().getData()
                            .setStartDate(range[0]);
                    viewConfigurationBean.getViewConfiguration().getData()
                            .setEndDate(range[1]);
                }

                viewConfigurationBean.getVariationDialog().getVariationPanel()
                        .getResultPanel().getResultTable().getModel().update();
                viewConfigurationBean.getVariationDialog().getVariationPanel()
                        .getRankingPanel().getRankingTable().getModel()
                        .update();
                viewConfigurationBean.getVariationDialog().setVisible(true);
            }
        }
    }

}
