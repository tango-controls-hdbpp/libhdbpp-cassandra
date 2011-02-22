package fr.soleil.mambo.actions.view.listeners;

import java.util.EventListener;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.event.ViewConfigurationBeanEvent;

public interface IViewConfigurationBeanListener extends EventListener {

    /**
     * Happens when only the date part of the {@link ViewConfiguration} of a
     * {@link ViewConfigurationBean} changed
     * 
     * @param event
     *            a {@link ViewConfigurationBeanEvent}
     */
    public void configurationDateChanged(ViewConfigurationBeanEvent event);

    /**
     * Happens when the {@link ViewConfiguration} of a
     * {@link ViewConfigurationBean} changed
     * 
     * @param event
     *            a {@link ViewConfigurationBeanEvent}
     */
    public void configurationStructureChanged(ViewConfigurationBeanEvent event);

    /**
     * Happens when a hidable panel was hidden or displayed
     * 
     * @param event
     *            a {@link ViewConfigurationBeanEvent}
     */
    public void visibilityChanged(ViewConfigurationBeanEvent event);

}
