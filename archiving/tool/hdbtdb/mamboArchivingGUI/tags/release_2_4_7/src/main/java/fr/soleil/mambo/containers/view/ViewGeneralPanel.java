// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewGeneralPanel.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewGeneralPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: ViewGeneralPanel.java,v $
// Revision 1.9 2008/04/09 18:00:00 achouri
// add new button
//
// Revision 1.8 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.7 2006/11/06 09:28:05 ounsy
// icons reorganization
//
// Revision 1.6 2006/08/31 12:19:32 ounsy
// informations about forcing export only in case of tdb
//
// Revision 1.5 2006/08/31 09:53:54 ounsy
// User always knows whether data export will be forced
//
// Revision 1.4 2006/07/28 10:07:12 ounsy
// icons moved to "icons" package
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view;

import java.awt.Font;
import java.sql.Timestamp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewGeneralPanel extends JPanel {

	private static final long serialVersionUID = 1982144874715441049L;
	private JLabel nameLabel;
	private JLabel nameField;
	private JLabel creationDateLabel;
	private JLabel lastUpdateDateLabel;
	private JLabel creationDateField;
	private JLabel lastUpdateDateField;
	private JLabel pathLabel;
	private JLabel pathField;

	public ViewGeneralPanel() {
		super();

		initComponents();
		addComponents();

		// ViewAttributesGraphPanel.getInstance().addPropertyChangeListener(this);
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {

		Box topBox = new Box(BoxLayout.X_AXIS);
		topBox.add(nameLabel);
		topBox.add(Box.createHorizontalStrut(5));
		topBox.add(nameField);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(creationDateLabel);
		topBox.add(Box.createHorizontalStrut(5));
		topBox.add(creationDateField);
		topBox.add(Box.createHorizontalStrut(10));
		topBox.add(lastUpdateDateLabel);
		topBox.add(Box.createHorizontalStrut(5));
		topBox.add(lastUpdateDateField);
		topBox.add(Box.createHorizontalGlue());
		topBox.setAlignmentX(LEFT_ALIGNMENT);

		Box bottomBox = new Box(BoxLayout.X_AXIS);
		bottomBox.add(pathLabel);
		bottomBox.add(Box.createHorizontalStrut(5));
		bottomBox.add(pathField);
		bottomBox.setAlignmentX(LEFT_ALIGNMENT);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(topBox);
		this.add(Box.createVerticalStrut(5));
		this.add(bottomBox);

		String msg = Messages.getMessage("VIEW_GENERAL_BORDER");
		TitledBorder titledBorder = BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.CENTER, TitledBorder.TOP);
		CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
				titledBorder, BorderFactory.createEmptyBorder(1, 5, 2, 5));
		this.setBorder(compoundBorder);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		String msg = "";

		msg = Messages.getMessage("VIEW_GENERAL_NAME");
		nameLabel = new JLabel(msg);
		Font newFont = nameLabel.getFont().deriveFont(Font.ITALIC);
		nameLabel.setFont(newFont);

		msg = Messages.getMessage("VIEW_GENERAL_CREATION_DATE");
		creationDateLabel = new JLabel(msg);
		creationDateLabel.setFont(newFont);

		msg = Messages.getMessage("VIEW_GENERAL_LAST_UPDATE_DATE");
		lastUpdateDateLabel = new JLabel(msg);
		lastUpdateDateLabel.setFont(newFont);

		pathLabel = new JLabel();
		pathLabel.setFont(newFont);

		creationDateField = new JLabel();
		lastUpdateDateField = new JLabel();
		nameField = new JLabel();
		pathField = new JLabel();
		GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
	}

	public void setCreationDate(Timestamp creationDate) {
		if (creationDate != null) {
			creationDateField.setText(creationDate.toString());
		} else {
			creationDateField.setText("");
		}
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		if (lastUpdateDate != null) {
			lastUpdateDateField.setText(lastUpdateDate.toString());
		} else {
			lastUpdateDateField.setText("");
		}
	}

	@Override
	public void setName(String name) {
		if (name != null) {
			nameField.setText(name);
		} else {
			nameField.setText("");
		}
	}

	/**
	 * @param path
	 */
	public void setPath(String path) {
		if (path == null) {
			pathLabel.setText("");
			pathField.setText("");
		} else {
			pathLabel.setText(Messages.getMessage("VIEW_GENERAL_PATH"));
			pathField.setText(path);
		}
	}

	// public void propertyChange(PropertyChangeEvent evt) {
	// if (evt.getSource() instanceof ViewAttributesGraphPanel) {
	// String property = evt.getPropertyName();
	// if (ViewAttributesGraphPanel.CANCELING.equals(property)
	// || ViewAttributesGraphPanel.CANCELED.equals(property)
	// || ViewAttributesGraphPanel.PANELS_LOADED.equals(property)) {
	// enableSelection();
	// }
	// else if (ViewAttributesGraphPanel.SETTING_PANELS.equals(property)
	// || ViewAttributesGraphPanel.PANELS_SET.equals(property)
	// || ViewAttributesGraphPanel.ADDING_PANELS.equals(property)
	// || ViewAttributesGraphPanel.PANELS_ADDED.equals(property)
	// || ViewAttributesGraphPanel.LOADING_PANELS.equals(property)) {
	// disableSelection();
	// }
	// }
	// }
	//
	// public void enableSelection() {
	// synchronized (this) {
	// OpenVCEditDialogNewAction.getInstance().setEnabled(true);
	// CloseSelectedVCAction.getInstance().setEnabled(true);
	// OpenedVCComboBox.getInstance().setEnabled(true);
	// repaint();
	// }
	// }
	//
	// public void disableSelection() {
	// synchronized (this) {
	// OpenVCEditDialogNewAction.getInstance().setEnabled(false);
	// CloseSelectedVCAction.getInstance().setEnabled(false);
	// OpenedVCComboBox.getInstance().setEnabled(false);
	// repaint();
	// }
	// }

}
