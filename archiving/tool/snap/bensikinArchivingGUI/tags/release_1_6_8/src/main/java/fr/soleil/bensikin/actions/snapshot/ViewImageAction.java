package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.bensikin.containers.sub.dialogs.ViewImageDialog;
import fr.soleil.bensikin.tools.Messages;

public class ViewImageAction extends AbstractAction {

    private static final long serialVersionUID = 1613980108125044471L;
    private ViewImageDialog   dialog;
    private String            name;
    private Object            value;
    private int               data_type;
    private String            displayFormat;

    public ViewImageAction(String name, Object value, int data_type,
            String displayFormat) {
        super();
        this.putValue(Action.NAME, Messages
                .getMessage("DIALOGS_IMAGE_ATTRIBUTE_VIEW_IMAGE"));
        this.name = name;
        this.value = value;
        this.data_type = data_type;
        this.displayFormat = displayFormat;
        dialog = null;

    }

    public void actionPerformed(ActionEvent e) {
        if (dialog == null) {
            dialog = new ViewImageDialog(name, value, displayFormat, data_type);
        }
        dialog.updateContent();
        dialog.setVisible(true);
    }
}
