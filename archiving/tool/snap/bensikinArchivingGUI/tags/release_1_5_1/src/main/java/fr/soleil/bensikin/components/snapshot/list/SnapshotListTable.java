//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/snapshot/list/SnapshotListTable.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotListTable.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SnapshotListTable.java,v $
// Revision 1.6  2006/01/10 13:28:15  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:27  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.snapshot.list;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import fr.soleil.bensikin.actions.listeners.SnapshotListTableListener;
import fr.soleil.bensikin.actions.listeners.SnapshotTableHeaderListener;
import fr.soleil.bensikin.models.SnapshotListTableModel;


/**
 * A singleton class containing the current list of snapshots.
 * The table's cells are not editable.
 * A SnapshotTableListener is added that listens to line selection events, and
 * a SnapshotTableHeaderListener is added that listens to column double-clicks to sort them.
 *
 * @author CLAISSE
 */
public class SnapshotListTable extends JTable
{
	private static SnapshotListTable snapshotListTableInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static SnapshotListTable getInstance()
	{
		if ( snapshotListTableInstance == null )
		{
			snapshotListTableInstance = new SnapshotListTable();
		}

		return snapshotListTableInstance;
	}

	/**
	 * Default constructor.
	 * <UL>
	 * <LI> Instantiates its table model
	 * <LI> Adds a selection listener on its table body (SnapshotListTableListener)
	 * <LI> Adds a sort request listener on its table header (SnapshotTableHeaderListener)
	 * <LI> Sets its columns sizes	and row height
	 * <LI> Disables the columns auto resize mode
	 * </UL>
	 */
	private SnapshotListTable()
	{
		super(SnapshotListTableModel.getInstance());

		this.getColumn("Time").setPreferredWidth(30);
		this.getColumn("ID").setMaxWidth(30);
		this.getColumn("Time").setPreferredWidth(160);
		this.getColumn("Time").setMaxWidth(160);

		this.setRowHeight(20);

		this.addMouseListener(new SnapshotListTableListener());
		JTableHeader header = this.getTableHeader();
		header.addMouseListener(new SnapshotTableHeaderListener());

		this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row , int column)
	{
		return false;
	}

	public void repaint() {
        try {
            if (this.getColumn("Time") != null) {
                this.getColumn("Time").setPreferredWidth(160);
                this.getColumn("Time").setMaxWidth(160);
            }
            if (this.getColumn("ID") != null) {
                this.getColumn("ID").setMaxWidth(30);
                this.getColumn("ID").setPreferredWidth(30);
            }
        }
        catch (IllegalArgumentException iae) {
            //the columns don't exist yet : do nothing
        }
        super.repaint();
    }
}
