//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/sub/dialogs/MamboErrorDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboErrorDialog.
//						(Claisse Laurent) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: MamboErrorDialog.java,v $
// Revision 1.1  2005/12/15 11:27:18  ounsy
// multi sessions management
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.sub.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.mambo.tools.SpringUtilities;

/**
 * @author SOLEIL
 */
public class MamboErrorDialog extends JDialog {
	String message;

	/**
	 * Constructor of the error dialog. No parent.
	 * 
	 * @param title
	 *            title of the dialog
	 * @param message
	 *            message of the dialog
	 */
	public MamboErrorDialog(String title, String message) {
		super((Frame) null, title, true);
		this.message = message;
		initialize();
	}

	/**
	 * Constructor of the error dialog. Frame as parent.
	 * 
	 * @param frame
	 *            the parent frame
	 * @param title
	 *            title of the dialog
	 * @param message
	 *            message of the dialog
	 */
	public MamboErrorDialog(Frame frame, String title, String message) {
		super(frame, title, true);
		this.message = message;
		initialize();
	}

	/**
	 * Constructor of the error dialog. JDialog as parent.
	 * 
	 * @param dialog
	 *            the parent JDialog
	 * @param title
	 *            title of the dialog
	 * @param message
	 *            message of the dialog
	 */
	public MamboErrorDialog(JDialog dialog, String title, String message) {
		super(dialog, title, true);
		this.message = message;
		initialize();
	}

	private void initialize() {
		int nbComponents = 0;
		String[] messages = message.split("\n");
		JPanel panel = new JPanel();
		panel.setLayout(new SpringLayout());

		JLabel[] fields = new JLabel[messages.length];
		this.setContentPane(panel);
		for (nbComponents = 0; nbComponents < messages.length; nbComponents++) {
			JLabel label = new JLabel(messages[nbComponents], JLabel.CENTER);
			fields[nbComponents] = label;
			panel.add(fields[nbComponents]);
		}
		JButton ok = new JButton("ok");
		ok.setOpaque(false);
		ok.addActionListener(new MamboErrorDialogListener(this));
		panel.add(ok);
		nbComponents++;
		SpringUtilities.makeCompactGrid(panel, nbComponents, 1, // rows, cols
				0, 0, // initX, initY
				0, 0, // xPad, yPad
				true); // every component same size
		Dimension size = panel.getPreferredSize();
		size.height += 30;
		size.width += 10;
		this.setSize(size);
		panel.setBackground(new Color(255, 255, 150));
		panel.setOpaque(true);
		panel.repaint();
		this.repaint();
	}

}

class MamboErrorDialogListener implements ActionListener {

	private MamboErrorDialog dialog;

	public MamboErrorDialogListener(MamboErrorDialog error) {
		dialog = error;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		dialog.dispose();
		dialog.setVisible(false);
	}

}