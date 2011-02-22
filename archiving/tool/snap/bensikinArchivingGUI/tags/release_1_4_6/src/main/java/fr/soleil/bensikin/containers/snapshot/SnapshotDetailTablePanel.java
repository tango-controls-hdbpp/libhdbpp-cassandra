//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/snapshot/SnapshotDetailTablePanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotDetailTablePanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: SnapshotDetailTablePanel.java,v $
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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.soleil.bensikin.tools.GUIUtilities;


/**
 * The panel containing the SnapshotDetailTabbedPane
 *
 * @author CLAISSE
 */
public class SnapshotDetailTablePanel extends JPanel
{
	private static SnapshotDetailTablePanel snapshotDetailTablePanelInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static SnapshotDetailTablePanel getInstance()
	{
		if ( snapshotDetailTablePanelInstance == null )
		{
			snapshotDetailTablePanelInstance = new SnapshotDetailTablePanel();
		}

		return snapshotDetailTablePanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private SnapshotDetailTablePanel()
	{
		BoxLayout layout = new BoxLayout(this , BoxLayout.X_AXIS);
		setLayout(layout);
		this.add(SnapshotDetailTabbedPane.getInstance());

		GUIUtilities.setObjectBackground(this , GUIUtilities.SNAPSHOT_COLOR);
	}
}
