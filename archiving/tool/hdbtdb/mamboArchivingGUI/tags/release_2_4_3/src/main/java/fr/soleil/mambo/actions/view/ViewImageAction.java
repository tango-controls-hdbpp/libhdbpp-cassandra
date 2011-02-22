package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.dialogs.ViewImageDialog;
import fr.soleil.mambo.tools.Messages;

public class ViewImageAction extends AbstractAction {

    // Ready to Comete 0.1.0-SNAPSHOT (but not yet tested)
    private static final long serialVersionUID = -2523383457768091422L;
    private ViewImageDialog dialog;
    private String name;
    private double[][] value;
    private String displayFormat;

    public ViewImageAction(String name, double[][] value, String displayFormat) {
        super();
        this.putValue(Action.NAME, Messages.getMessage("VIEW_IMAGE_VIEW"));
        dialog = null;
        this.name = name;
        this.value = value;
        this.displayFormat = displayFormat;
    }

    public void actionPerformed(ActionEvent e) {
        if (dialog == null) {
            dialog = new ViewImageDialog(name, value, displayFormat);
        }
        dialog.updateContent();
        dialog.setVisible(true);
    }

}
