//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/FilterSnapshotsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  FilterSnapshotsAction.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: soleilarc $
//
// $Revision: 1.5 $
//
// $Log: FilterSnapshotsAction.java,v $
// Revision 1.5  2007/10/03 16:04:48  soleilarc
// Author: XP
// Mantis bug ID: 6594
// Comment: The code nessecary to refresh the snapshot list has been factorized in the AbsRefreshAction class with the RefreshSnapList method.
//
// Revision 1.4  2006/11/29 09:57:00  ounsy
// minor changes
//
// Revision 1.3  2006/04/10 08:47:14  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.2  2006/01/09 12:51:51  ounsy
// Date filter updated
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.actions.context.AbsRefreshAction;
import fr.soleil.bensikin.actions.exceptions.FieldFormatException;
import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.components.snapshot.list.SnapshotListTable;
import fr.soleil.bensikin.containers.snapshot.SnapshotFilterPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.tools.BensikinDate;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.DateUtil;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

/**
 * Looks up snapshots according to the filtering criterions filled in the filter area.
 * <UL>
 * <LI> Builds a Criterions object with the criterions fields; generates an error message if the fields are incorrect
 * <LI> Uses loadSnapshots on the current context to load the list of snapshots for those fields
 * <LI> Logs the action's success or failure
 * <LI> Updates the SnapshotListTable instance's model
 * </UL>
 *
 * @author CLAISSE
 */
public class FilterSnapshotsAction extends AbsRefreshAction
{
	private static FilterSnapshotsAction instance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @param name The action's name
	 * @return The instance
	 */
	public static FilterSnapshotsAction getInstance(String name)
	{
		if ( instance == null )
		{
			instance = new FilterSnapshotsAction(name);
		}

		return instance;
	}

	/**
	 * Returns the existing instance
	 *
	 * @return The existing instance
	 */
	public static FilterSnapshotsAction getInstance()
	{
		return instance;
	}

	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param name The action name
	 */
	private FilterSnapshotsAction(String name)
	{
		this.putValue(Action.NAME , name);
		this.setEnabled(false);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		SnapshotFilterPanel source = SnapshotFilterPanel.getInstance();

		if ( arg0.getSource() instanceof JButton )
		{
			JButton buttonFrom = ( JButton ) arg0.getSource();
			if ( buttonFrom == source.getResetButton() )
			{
				source.resetFields();
				return;
			}
		}

		refreshSnapList();
	}
}
