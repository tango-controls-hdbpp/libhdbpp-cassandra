package fr.soleil.mambo.tools;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class PopupUtils {

    public static void showOptionErrorDialog(final Component parentComponent, final String message) {
	final JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
	final JDialog dialog = pane.createDialog(parentComponent, "MAMBO");
	dialog.setLocationRelativeTo(parentComponent);
	dialog.setVisible(true);
    }
}
