//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/listeners/SnapshotTableHeaderListener.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ContextTableHeaderListener.
//						(Claisse Laurent) - 22 juin 2005
//
//$Author: chinkumo $
//
//$Revision: 1.1 $
//
//$Log: SnapshotTableHeaderListener.java,v $
//Revision 1.1  2005/11/29 18:25:13  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.bensikin.actions.listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.JTableHeader;

import fr.soleil.bensikin.models.SnapshotListTableModel;


/**
 * Listens to double clicks on the snapshots list table header. Responds by sorting the clicked column.
 * <UL>
 * <LI> Checks the click is not a single click, if it is does nothing
 * <LI> Gets the index of the clicked column
 * <LI> Sorts the SnapshotListTableModel instance for this column index
 * </UL>
 *
 * @author CLAISSE
 */
public class SnapshotTableHeaderListener extends MouseAdapter
{
	/**
	 * Does nothing
	 */
	public SnapshotTableHeaderListener()
	{
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent event)
	{
		int clickCount = event.getClickCount();
		if ( clickCount == 1 )
		{
			return;
			//so that the use can resize columns
		}

		Point point = event.getPoint();
		JTableHeader header = ( JTableHeader ) event.getSource();
		int clickedColunIndex = header.columnAtPoint(point);

		SnapshotListTableModel model = SnapshotListTableModel.getInstance();
		model.sort(clickedColunIndex);
	}

}
