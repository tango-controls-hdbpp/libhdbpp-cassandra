//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotDetailPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotDetailPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotDetailPanel.java,v $
// Revision 1.6  2005/12/14 16:25:15  ounsy
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

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.bensikin.tools.SpringUtilities;

/**
 * Contains the selected snapshots and the comparison launch panel.
 * 
 * @author CLAISSE
 */
public class SnapshotDetailPanel extends JPanel {
	private static SnapshotDetailPanel snapshotDetailPanelInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static SnapshotDetailPanel getInstance() {
		if (snapshotDetailPanelInstance == null) {
			snapshotDetailPanelInstance = new SnapshotDetailPanel();
		}

		return snapshotDetailPanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private SnapshotDetailPanel() {

		this.setLayout(new SpringLayout());
		this.add(SnapshotDetailTablePanel.getInstance());
		this.add(SnapshotDetailComparePanel.getInstance());
		SpringUtilities.makeCompactGrid(this, 2, 1, // rows, cols
				6, 6, // initX, initY
				6, 6, // xPad, yPad
				true);

		String msg = Messages.getMessage("SNAPSHOT_DETAIL_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.CENTER, TitledBorder.TOP, GUIUtilities
						.getTitleFont());
		this.setBorder(tb);

		GUIUtilities.setObjectBackground(this, GUIUtilities.SNAPSHOT_COLOR);
	}
}
