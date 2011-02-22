package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.dialogs.ViewImageDialog;
import fr.soleil.mambo.data.attributes.FakeNumberImage;
import fr.soleil.mambo.tools.Messages;

public class ViewImageAction extends AbstractAction {
	private FakeNumberImage image;
	private ViewImageDialog dialog;

	public ViewImageAction(String name, double[][] value, String displayFormat) {
		super();
		this.putValue(Action.NAME, Messages.getMessage("VIEW_IMAGE_VIEW"));
		image = null;
		image = new FakeNumberImage();
		image.setName(name);
		dialog = new ViewImageDialog(image);

		double[][] val = new double[0][0];
		if (value != null) {
			val = value;
		}
		image.setValue(val);
		image.setFormat(displayFormat);
	}

	public void actionPerformed(ActionEvent e) {
		dialog.setVisible(true);
	}
}
