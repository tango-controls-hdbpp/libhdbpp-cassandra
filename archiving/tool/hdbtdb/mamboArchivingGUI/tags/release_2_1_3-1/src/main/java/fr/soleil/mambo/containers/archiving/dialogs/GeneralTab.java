//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/dialogs/GeneralTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  GeneralTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: GeneralTab.java,v $
// Revision 1.3  2006/02/24 12:25:38  ounsy
// modified for HDB/TDB separation
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.archiving.dialogs;

import java.awt.Dimension;
import java.sql.Timestamp;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class GeneralTab extends JPanel {
	private static GeneralTab instance = null;

	private JLabel creationDateLabel;
	private JLabel creationDateValue;
	private JLabel lastUpdateDateLabel;
	private JLabel lastUpdateDateValue;
	private JLabel nameLabel;
	private JTextField nameValue;
	private JLabel dbLabel;
	private JLabel dbValue;

	/**
	 * @return 8 juil. 2005
	 */
	public static GeneralTab getInstance() {
		if (instance == null) {
			instance = new GeneralTab();
		}

		return instance;
	}

	/**
     * 
     */
	private GeneralTab() {
		// this.add ( new JLabel ( "GeneralTab" ) );

		initComponents();
		addComponents();
		setLayout();
	}

	/**
	 * 19 juil. 2005
	 */
	private void setLayout() {
		this.setLayout(new SpringLayout());

		// Lay out the panel.
		SpringUtilities.makeCompactGrid(this, 5, 3, // rows, cols
				6, 6, // initX, initY
				6, 6, true); // xPad, yPad

	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		// START LINE 1
		this.add(creationDateLabel);
		this.add(creationDateValue);
		this.add(Box.createHorizontalGlue());
		// END LINE 1

		// START LINE 2
		this.add(lastUpdateDateLabel);
		this.add(lastUpdateDateValue);
		this.add(Box.createHorizontalGlue());
		// END LINE 2

		// START LINE 3
		this.add(nameLabel);
		this.add(nameValue);
		this.add(Box.createHorizontalGlue());
		// END LINE 3

		// START LINE 3
		this.add(dbLabel);
		this.add(dbValue);
		this.add(Box.createHorizontalGlue());
		// END LINE 3

		// START LINE 4
		this.add(Box.createVerticalGlue());
		this.add(Box.createVerticalGlue());
		this.add(Box.createVerticalGlue());
		// END LINE 4
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		String msg = "";

		msg = Messages.getMessage("DIALOGS_EDIT_AC_GENERAL_CREATION_DATE");
		creationDateLabel = new JLabel(msg);

		msg = Messages.getMessage("DIALOGS_EDIT_AC_GENERAL_LAST_UPDATE_DATE");
		lastUpdateDateLabel = new JLabel(msg);

		msg = Messages.getMessage("DIALOGS_EDIT_AC_GENERAL_NAME");
		nameLabel = new JLabel(msg);

		msg = Messages.getMessage("DIALOGS_EDIT_AC_GENERAL_DB");
		dbLabel = new JLabel(msg);

		creationDateValue = new JLabel();
		lastUpdateDateValue = new JLabel();
		dbValue = new JLabel();

		nameValue = new JTextField();
		nameValue.setMinimumSize(new Dimension(70, 20));
		nameValue.setMaximumSize(new Dimension(70, 20));
		nameValue.setPreferredSize(new Dimension(70, 20));
	}

	public void setCreationDate(Timestamp creationDate) {
		if (creationDate != null) {
			creationDateValue.setText(creationDate.toString());
		} else {
			creationDateValue.setText("");
		}
	}

	public void setHistoric(boolean isHistoric) {
		String val = isHistoric ? "HDB" : "TDB";
		dbValue.setText(val);
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		if (lastUpdateDate != null) {
			lastUpdateDateValue.setText(lastUpdateDate.toString());
		} else {
			lastUpdateDateValue.setText("");
		}
	}

	public void setName(String name) {
		if (name != null) {
			nameValue.setText(name);
		} else {
			nameValue.setText("");
		}
	}

	public String getName() {
		return nameValue.getText();
	}
}
