/**
 *
 */
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.tools.Messages;

/**
 * @author MAINGUY
 */
public class SelectedVCHideGeneralAction extends AbstractAction {

    private static final long                  serialVersionUID = -7168421633427580151L;
    private boolean                            visible          = true;
    private String                             name;
    private static SelectedVCHideGeneralAction instance         = null;

    public static SelectedVCHideGeneralAction getInstance(String name) {
        if (instance == null) {
            instance = new SelectedVCHideGeneralAction(name);
        }
        return instance;
    }

    private SelectedVCHideGeneralAction(String name) {
        super();
        this.name = name;
        putValue(Action.NAME, this.name);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ViewConfigurationBean viewConfigurationBean = ViewConfigurationBeanManager
                    .getInstance().getBeanFor(
                            ViewConfigurationBeanManager.getInstance()
                                    .getSelectedConfiguration());
            if (viewConfigurationBean != null) {
                if (visible == true) {
                    visible = false;
                    name = Messages.getMessage("VIEW_ACTION_SHOW_GENERAL_VIEW");
                    putValue(Action.NAME, name);
                }
                else {
                    visible = true;
                    name = Messages.getMessage("VIEW_ACTION_HIDE_GENERAL_VIEW");
                    putValue(Action.NAME, name);
                }
                viewConfigurationBean.getGeneralPanel().setVisible(visible);
                viewConfigurationBean.getViewDisplayPanel().revalidate();
                viewConfigurationBean.getViewDisplayPanel().repaint();
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean isVisible() {
        return visible;
    }

}
