//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/listeners/SnapshotTableModelListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SnapshotTableModelListener.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: SnapshotTableModelListener.java,v $
// Revision 1.4  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.models.listeners;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.snapshot.Snapshot;
import fr.soleil.bensikin.data.snapshot.SnapshotData;
import fr.soleil.bensikin.models.SnapshotListTableModel;

/**
 * Listens to inserts in the SnapshotTableModel instance. When a new
 * SnapshotData row is inserted, notifies the static references of Snapshot.
 * 
 * @author CLAISSE
 */
public class SnapshotTableModelListener implements TableModelListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.swing.event.TableModelListener#tableChanged(javax.swing.event.
	 * TableModelEvent)
	 */
	public void tableChanged(TableModelEvent event) {
		int eventType = event.getType();
		int firstRow = event.getFirstRow();
		int lastRow = event.getLastRow();

		SnapshotListTableModel model = (SnapshotListTableModel) event
				.getSource();

		switch (eventType) {
		case TableModelEvent.INSERT:
			for (int i = firstRow; i <= lastRow; i++) {
				SnapshotData contextData = model.getSnapshotDataAtRow(i);
				Context selectedContext = Context.getSelectedContext();
				Snapshot snapshot = new Snapshot(selectedContext, contextData);
				Snapshot.addOpenedSnapshot(snapshot);
			}
			break;

		case TableModelEvent.DELETE:
			break;

		case TableModelEvent.UPDATE:
			break;

		default:
			break;
		}
	}

}
