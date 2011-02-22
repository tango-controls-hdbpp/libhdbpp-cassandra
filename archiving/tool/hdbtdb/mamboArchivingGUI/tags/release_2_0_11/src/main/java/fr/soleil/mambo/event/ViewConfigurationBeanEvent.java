package fr.soleil.mambo.event;

import java.util.EventObject;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;

public class ViewConfigurationBeanEvent extends EventObject {

    private static final long serialVersionUID = 6477736148599577151L;

    public ViewConfigurationBeanEvent(ViewConfigurationBean source) {
        super(source);
    }

    @Override
    public ViewConfigurationBean getSource() {
        return (ViewConfigurationBean) super.getSource();
    }

}
