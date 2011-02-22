//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotTablePanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotTablePanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotTablePanel.java,v $
// Revision 1.6  2005/12/14 16:27:10  ounsy
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.snapshot.RemoveSelectedSnapshotsAction;
import fr.soleil.bensikin.components.snapshot.list.SnapshotListTable;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;

/**
 * Contains the list of snapshots, SnapshotListTable.
 * 
 * @author CLAISSE
 */
public class SnapshotTablePanel extends JPanel {
	private static SnapshotTablePanel instance = null;

	private final static ImageIcon deleteIcon = new ImageIcon(Bensikin.class
			.getResource("icons/delete_small.gif"));

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static SnapshotTablePanel getInstance() {
		if (instance == null) {
			instance = new SnapshotTablePanel();
		}

		return instance;
	}

	/**
	 * Builds the panel
	 */
	private SnapshotTablePanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		/* this.setMaximumSize( new Dimension( 640 , 170 ) ); */
		this.setPreferredSize(new Dimension(640, 170));

		Box box = new Box(BoxLayout.Y_AXIS);

		SnapshotListTable table = SnapshotListTable.getInstance();
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		String tooltip = Messages.getMessage("SNAPSHOT_LIST_REMOVE_SELECTED");
		JButton button = new JButton(new RemoveSelectedSnapshotsAction(tooltip));
		button.setBackground(Color.BLACK);
		button.setForeground(Color.BLACK);
		button.setIcon(deleteIcon);
		button.setFocusPainted(false);
		button.setFocusable(false);

		GUIUtilities.setObjectBackground(scrollpane,
				GUIUtilities.SNAPSHOT_COLOR);
		GUIUtilities.setObjectBackground(scrollpane.getViewport(),
				GUIUtilities.SNAPSHOT_COLOR);
		scrollpane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, button);
		box.add(scrollpane);
		this.add(box);

		GUIUtilities.setObjectBackground(this, GUIUtilities.SNAPSHOT_COLOR);
	}
}