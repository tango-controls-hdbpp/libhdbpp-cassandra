/*
 * Synchrotron Soleil File : VCVariationEditCopyAction.java Project : mambo
 * Description : Author : SOLEIL Original : 13 d�c. 2005 Revision: Author: Date:
 * State: Log: VCVariationEditCopyAction.java,v
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;

/**
 * @author SOLEIL
 */
public class VCVariationEditCopyAction extends AbstractAction {

    private static final long     serialVersionUID = -7595247882824542798L;
    private ViewConfigurationBean viewConfigurationBean;

    /**
     * @param name
     */
    public VCVariationEditCopyAction(String name,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
        this.viewConfigurationBean = viewConfigurationBean;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        viewConfigurationBean.getVariationEditCopyDialog().getEditCopyPanel()
                .updateCopyText();
        viewConfigurationBean.getVariationEditCopyDialog().setVisible(true);
    }
}
