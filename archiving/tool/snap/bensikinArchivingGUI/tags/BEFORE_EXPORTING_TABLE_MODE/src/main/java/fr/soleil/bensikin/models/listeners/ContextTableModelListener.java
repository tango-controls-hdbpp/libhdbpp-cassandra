//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/listeners/ContextTableModelListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextTableModelListener.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: ContextTableModelListener.java,v $
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
import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.models.ContextListTableModel;


/**
 * Listens to inserts in the ContextTableModel instance.
 * When a new ContextData row is inserted, notifies the static references of Context.
 *
 * @author CLAISSE
 */
public class ContextTableModelListener implements TableModelListener
{

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	public void tableChanged(TableModelEvent event)
	{
		int eventType = event.getType();
		int firstRow = event.getFirstRow();
		int lastRow = event.getLastRow();

		ContextListTableModel model = ( ContextListTableModel ) event.getSource();

		switch ( eventType )
		{
			case TableModelEvent.INSERT:
				for ( int i = firstRow ; i <= lastRow ; i++ )
				{
					ContextData contextData = model.getContextAtRow(i);
					Context context = new Context(contextData);
					Context.addOpenedContext(context);
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
