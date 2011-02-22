package fr.soleil.sgad;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Messages {

	public static void emptyField(Component parentComponent, String field) {
		Object[] options = { "OK" };
		ImageIcon myIcon = new ImageIcon(Messages.class
				.getResource("exclam25.gif"));
		JOptionPane.showOptionDialog(parentComponent, "The field named \""
				+ field + "\" is empty !", "Empty field...",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				myIcon, options, options[0]);
	}

	public static void noSchema(Component parentComponent) {
		Object[] options = { "OK" };
		ImageIcon myIcon = new ImageIcon(Messages.class
				.getResource("exclam25.gif"));
		JOptionPane.showOptionDialog(parentComponent,
				"At least one schema must be selected !",
				"No schema selected...", JOptionPane.DEFAULT_OPTION,
				JOptionPane.WARNING_MESSAGE, myIcon, options, options[0]);
	}

	public static void about(Component parentComponent) {
		Object[] options = { "OK" };
		ImageIcon myIcon = new ImageIcon(Messages.class
				.getResource("icons/soleil_logo-150.gif"));

		ResourceBundle rb = ResourceBundle
				.getBundle("fr.soleil.sgad.resources.application");

		JOptionPane.showOptionDialog(parentComponent,
				"Script Generator for Archiving Databases "
						+ fr.soleil.sgad.Constants.newLine
						+
						// " Rev : " + fr.soleil.sgad.Constants.release + "" +
						// fr.soleil.sgad.Constants.newLine +
						" Rev : " + rb.getString("project.version")
						+ ""
						+ fr.soleil.sgad.Constants.newLine
						+
						// "Jean CHINKUMO (" + fr.soleil.sgad.Constants.date +
						// ")" + fr.soleil.sgad.Constants.newLine +
						"Jean CHINKUMO (" + rb.getString("build.date") + ")"
						+ fr.soleil.sgad.Constants.newLine
						+ "Synchrotron SOLEIL", "About",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
				myIcon, options, options[0]);
	}

}