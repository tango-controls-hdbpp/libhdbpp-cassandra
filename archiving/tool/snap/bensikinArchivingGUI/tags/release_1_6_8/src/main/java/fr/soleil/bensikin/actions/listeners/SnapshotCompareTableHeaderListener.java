package fr.soleil.bensikin.actions.listeners;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.JTableHeader;

import fr.soleil.bensikin.models.SnapshotCompareTableModel;

/**
 * Listens to double clicks on the snapshots list table header. Responds by
 * sorting the clicked column.
 * <UL>
 * <LI>Checks the click is not a single click, if it is does nothing
 * <LI>Gets the index of the clicked column
 * <LI>Sorts the SnapshotListTableModel instance for this column index
 * </UL>
 * 
 * @author CLAISSE
 */
public class SnapshotCompareTableHeaderListener extends MouseAdapter {
	private SnapshotCompareTableModel model;

	/**
	 * Does nothing
	 */
	public SnapshotCompareTableHeaderListener(SnapshotCompareTableModel _model) {
		this.model = _model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent event) {
		int clickCount = event.getClickCount();
		if (clickCount == 1) {
			return;
			// so that the use can resize columns
		}

		Point point = event.getPoint();
		JTableHeader header = (JTableHeader) event.getSource();
		int clickedColunIndex = header.columnAtPoint(point);

		this.model.sort(clickedColunIndex);
	}
}
