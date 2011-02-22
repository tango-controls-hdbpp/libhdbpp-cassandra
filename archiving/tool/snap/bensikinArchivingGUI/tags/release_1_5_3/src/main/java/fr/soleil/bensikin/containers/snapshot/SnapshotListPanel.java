//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotListPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotListPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotListPanel.java,v $
// Revision 1.6  2005/12/14 16:26:51  ounsy
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

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * Contains the snapshots filtering panel and the resulting snapshots list
 * panel.
 * 
 * @author CLAISSE
 */
public class SnapshotListPanel extends JPanel {

	private static SnapshotListPanel snapshotListPanelInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static SnapshotListPanel getInstance() {
		if (snapshotListPanelInstance == null) {
			snapshotListPanelInstance = new SnapshotListPanel();
		}

		return snapshotListPanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private SnapshotListPanel() {
		this.setLayout(new SpringLayout());
		this.add(SnapshotFilterPanel.getInstance());
		Dimension dimP = SnapshotFilterPanel.getInstance().getPreferredSize();
		Dimension dimM = SnapshotFilterPanel.getInstance().getMaximumSize();
		dimM.height = dimP.height;
		SnapshotFilterPanel.getInstance().setMaximumSize(dimM);
		this.add(SnapshotTablePanel.getInstance());
		SpringUtilities.makeCompactGrid(this, 2, 1, // rows, cols
				6, 6, // initX, initY
				6, 6, // xPad, yPad
				true);

		String msg = Messages.getMessage("SNAPSHOT_LIST_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.CENTER, TitledBorder.TOP, GUIUtilities
						.getTitleFont());
		this.setBorder(tb);

		GUIUtilities.setObjectBackground(this, GUIUtilities.SNAPSHOT_COLOR);
	}
}
