/**
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;

public class VCHideGeneralAction extends AbstractAction {

    private static final long     serialVersionUID = -7168421633427580151L;
    private String                name;
    private ViewConfigurationBean viewConfigurationBean;

    public VCHideGeneralAction(String name,
            ViewConfigurationBean viewConfigurationBean) {
        super();
        this.name = name;
        putValue(Action.NAME, this.name);
        this.viewConfigurationBean = viewConfigurationBean;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (viewConfigurationBean != null) {
            viewConfigurationBean.setShowGeneralPanel();
        }
    }
}
