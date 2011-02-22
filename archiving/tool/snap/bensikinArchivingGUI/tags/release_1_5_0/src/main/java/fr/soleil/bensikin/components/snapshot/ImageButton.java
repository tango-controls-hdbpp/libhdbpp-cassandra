package fr.soleil.bensikin.components.snapshot;

import javax.swing.JLabel;

import fr.soleil.bensikin.containers.sub.dialogs.ImageAttibuteDialog;
import fr.soleil.bensikin.tools.Messages;

public class ImageButton extends JLabel
{

    private ImageAttibuteDialog dialog;

    public ImageButton (String name, Object value, int data_type, String displayFormat)
    {
        super(Messages.getMessage("DIALOGS_IMAGE_ATTRIBUTE_VIEW"));
        dialog = new ImageAttibuteDialog(name, value, data_type, displayFormat);
    }

    public void actionPerformed()
    {
        dialog.setVisible(true);
    }

}
