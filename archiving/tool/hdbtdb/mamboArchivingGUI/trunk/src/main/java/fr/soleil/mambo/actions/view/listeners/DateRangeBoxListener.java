package fr.soleil.mambo.actions.view.listeners;

import java.util.EventListener;

import fr.soleil.mambo.event.DateRangeBoxEvent;

public interface DateRangeBoxListener extends EventListener {

    public void dateRangeBoxChanged(DateRangeBoxEvent event);

}
