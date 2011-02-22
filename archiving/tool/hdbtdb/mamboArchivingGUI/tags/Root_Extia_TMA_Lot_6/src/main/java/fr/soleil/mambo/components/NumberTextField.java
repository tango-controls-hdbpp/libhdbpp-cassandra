package fr.soleil.mambo.components;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.Messages;

public class NumberTextField extends JTextField {

	protected byte numberType;
	protected String formerText = "0";

	public final static byte POSITIVE_INTEGER_LIKE = 0;
	public final static byte POSITIVE_FLOAT_LIKE = 1;
	public final static byte INTEGER_LIKE = 2;
	public final static byte FLOAT_LIKE = 3;

	public NumberTextField(byte _numberType) {
		super("0");
		this.numberType = _numberType;
		this.addFocusListener(new TextFocusAdapter());
	}

	public byte getNumberType() {
		return numberType;
	}

	protected class TextFocusAdapter extends FocusAdapter {
		private String msg = null;
		boolean inError = false;

		public TextFocusAdapter() {
			super();
		}

		public void focusLost(FocusEvent f) {
			if (((NumberTextField) f.getComponent()).getNumberType() == NumberTextField.POSITIVE_INTEGER_LIKE) {
				msg = Messages
						.getMessage("STANDARD_MESSAGES_POSITIVE_INTEGER_ERROR");
				if (!((NumberTextField) f.getComponent()).getText().matches(
						"[0-9]+")) {
					inError = true;
				}
			} else if (((NumberTextField) f.getComponent()).getNumberType() == NumberTextField.POSITIVE_FLOAT_LIKE) {
				msg = Messages
						.getMessage("STANDARD_MESSAGES_POSITIVE_FLOAT_ERROR");
				if (!((NumberTextField) f.getComponent()).getText().matches(
						"[0-9]+")
						&& !((NumberTextField) f.getComponent()).getText()
								.matches("[0-9]+\\.[0-9]+")) {
					inError = true;
				}
			} else if (((NumberTextField) f.getComponent()).getNumberType() == NumberTextField.INTEGER_LIKE) {
				msg = Messages.getMessage("STANDARD_MESSAGES_INTEGER_ERROR");
				if (!((NumberTextField) f.getComponent()).getText().matches(
						"[0-9]+")
						&& !((NumberTextField) f.getComponent()).getText()
								.matches("\\-[0-9]+")) {
					inError = true;
				}
			} else if (((NumberTextField) f.getComponent()).getNumberType() == NumberTextField.FLOAT_LIKE) {
				msg = Messages.getMessage("STANDARD_MESSAGES_FLOAT_ERROR");
				if (!((NumberTextField) f.getComponent()).getText().matches(
						"[0-9]+")
						&& !((NumberTextField) f.getComponent()).getText()
								.matches("[0-9]+\\.[0-9]+")
						&& !((NumberTextField) f.getComponent()).getText()
								.matches("\\-[0-9]+")
						&& !((NumberTextField) f.getComponent()).getText()
								.matches("\\-[0-9]+\\.[0-9]+")) {
					inError = true;
				}
			}

			if (inError) {
				JOptionPane.showMessageDialog(MamboFrame.getInstance(), msg,
						Messages.getMessage("STANDARD_MESSAGES_ERROR"),
						JOptionPane.ERROR_MESSAGE);
				((NumberTextField) f.getComponent())
						.setText(((NumberTextField) f.getComponent()).formerText);
			} else {
				((NumberTextField) f.getComponent()).formerText = ((NumberTextField) f
						.getComponent()).getText();
			}
			msg = null;
			inError = false;
		} // end focusLost()

	} // end class TextFocusAdapter

} // end class NumberTextField
