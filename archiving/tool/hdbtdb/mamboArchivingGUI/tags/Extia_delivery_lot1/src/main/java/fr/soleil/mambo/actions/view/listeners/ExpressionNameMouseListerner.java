package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import fr.soleil.mambo.containers.view.dialogs.ExpressionNamesChooser;

public class ExpressionNameMouseListerner extends MouseAdapter {

	private int reference;
	private ExpressionNamesChooser chooser;

	public ExpressionNameMouseListerner(int _reference,
			ExpressionNamesChooser chooser) {
		super();
		this.reference = _reference;
		this.chooser = chooser;
	}

	public void mouseClicked(MouseEvent e) {
		chooser.setNameIndex(reference);
		chooser.setVisible(true);
	}
}
