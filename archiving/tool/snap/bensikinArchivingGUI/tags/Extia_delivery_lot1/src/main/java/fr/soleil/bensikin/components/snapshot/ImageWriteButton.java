package fr.soleil.bensikin.components.snapshot;

import javax.swing.JLabel;
import javax.swing.JTable;

import fr.soleil.bensikin.containers.sub.dialogs.ImageWriteAttibuteDialog;
import fr.soleil.bensikin.tools.Messages;

public class ImageWriteButton extends JLabel {
	private ImageWriteAttibuteDialog dialog;

	public ImageWriteButton(JTable table, int row, String name, Object value,
			int data_type, String displayFormat) {
		super(Messages.getMessage("DIALOGS_IMAGE_ATTRIBUTE_VIEW"));
		dialog = new ImageWriteAttibuteDialog(name, value, data_type,
				displayFormat);
	}

	public void actionPerformed() {
		dialog.setVisible(true);
	}
}
