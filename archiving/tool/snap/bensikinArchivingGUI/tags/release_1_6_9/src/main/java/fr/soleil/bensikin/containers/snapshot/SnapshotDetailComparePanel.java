//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotDetailComparePanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotDetailComparePanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotDetailComparePanel.java,v $
// Revision 1.6  2005/12/14 16:24:49  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.snapshot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.snapshot.CompareSnapshotsAction;
import fr.soleil.bensikin.actions.snapshot.CompareSnapshotsPurgeAction;
import fr.soleil.bensikin.tools.Messages;

/**
 * The small panel containing references to the snapshots to be compared, a
 * button to reset those fields, and a button to launch the comparison.
 * 
 * @author CLAISSE
 */
public class SnapshotDetailComparePanel extends JPanel {
	private ImageIcon compareIcon = new ImageIcon(Bensikin.class
			.getResource("icons/compare.gif"));
	private ImageIcon resetIcon = new ImageIcon(Bensikin.class
			.getResource("icons/reset_compare.gif"));
	private static SnapshotDetailComparePanel snapshotDetailComparePanelInstance = null;
	private JTextField sn1TextField;
	private JTextField sn2TextField;

	/**
	 * @return The sn1TextField attribute
	 */
	public JTextField getSn1TextField() {
		return sn1TextField;
	}

	/**
	 * @return The sn2TextField attribute
	 */
	public JTextField getSn2TextField() {
		return sn2TextField;
	}

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static SnapshotDetailComparePanel getInstance() {
		if (snapshotDetailComparePanelInstance == null) {
			snapshotDetailComparePanelInstance = new SnapshotDetailComparePanel();
		}

		return snapshotDetailComparePanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private SnapshotDetailComparePanel() {
		super();
		Font font = new Font("Arial", Font.PLAIN, 11);
		String msg = Messages.getMessage("SNAPSHOT_DETAIL_COMPARE_1ST");
		JLabel sn1Label = new JLabel(msg, JLabel.RIGHT);
		sn1Label.setFont(font);

		msg = Messages.getMessage("SNAPSHOT_DETAIL_COMPARE_2ND");
		JLabel sn2Label = new JLabel(msg, JLabel.RIGHT);
		sn2Label.setFont(font);

		Color disabledColor = Color.lightGray;
		sn1TextField = new JTextField(10);
		sn1TextField.setEnabled(false);
		sn1TextField.setDisabledTextColor(Color.black);
		sn1TextField.setBackground(disabledColor);
		sn2TextField = new JTextField(10);
		sn2TextField.setEnabled(false);
		sn2TextField.setDisabledTextColor(Color.black);
		sn2TextField.setBackground(disabledColor);

		msg = Messages.getMessage("SNAPSHOT_DETAIL_COMPARE");
		CompareSnapshotsAction compareSnapshotsAction = CompareSnapshotsAction
				.getInstance(msg);
		JButton compareButton = new JButton(compareSnapshotsAction);
		compareButton.setIcon(compareIcon);
		compareButton.setMargin(new Insets(0, 0, 0, 0));
		compareButton.setFocusPainted(false);
		compareButton.setFocusable(false);
		compareButton.setFont(font);
		GUIUtilities.setObjectBackground(compareButton,
				GUIUtilities.SNAPSHOT_COLOR);

		msg = Messages.getMessage("SNAPSHOT_DETAIL_COMPARE_PURGE");
		CompareSnapshotsPurgeAction compareSnapshotsPurgeAction = CompareSnapshotsPurgeAction
				.getInstance(msg);
		JButton purgeButton = new JButton(compareSnapshotsPurgeAction);
		purgeButton.setIcon(resetIcon);
		purgeButton.setMargin(new Insets(0, 0, 0, 0));
		purgeButton.setFocusPainted(false);
		purgeButton.setFocusable(false);
		purgeButton.setFont(font);
		GUIUtilities.setObjectBackground(purgeButton,
				GUIUtilities.SNAPSHOT_COLOR);

		Box box = new Box(BoxLayout.X_AXIS);

		box.add(sn1Label);
		box.add(Box.createHorizontalStrut(2));
		box.add(sn1TextField);

		box.add(Box.createHorizontalStrut(20));
		box.add(Box.createHorizontalGlue());

		box.add(sn2Label);
		box.add(Box.createHorizontalStrut(2));
		box.add(sn2TextField);

		box.add(Box.createHorizontalStrut(20));
		box.add(compareButton);
		box.add(Box.createHorizontalStrut(10));
		box.add(purgeButton);

		this.add(box);

		this.setPreferredSize(new Dimension(600, 30));
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

		GUIUtilities.setObjectBackground(this, GUIUtilities.SNAPSHOT_COLOR);
	}

	/**
	 * Empties the 2 fields
	 */
	public void reset() {
		sn1TextField.setText("");
		sn2TextField.setText("");
	}

}
