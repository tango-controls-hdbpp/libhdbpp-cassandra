package fr.soleil.mambo.event;

import java.util.EventObject;

import fr.soleil.mambo.containers.view.dialogs.DateRangeBox;

public class DateRangeBoxEvent extends EventObject {

    private static final long serialVersionUID = -8250446051256792967L;

    public DateRangeBoxEvent(DateRangeBox source) {
        super(source);
    }

    @Override
    public DateRangeBox getSource() {
        return (DateRangeBox) super.getSource();
    }

}
