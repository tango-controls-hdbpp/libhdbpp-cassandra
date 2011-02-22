//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/ContextListTableModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextListTableModel.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: ContextListTableModel.java,v $
// Revision 1.5  2006/06/28 12:53:20  ounsy
// minor changes
//
// Revision 1.4  2005/11/29 18:25:13  chinkumo
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
package fr.soleil.bensikin.models;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import fr.soleil.bensikin.components.context.ContextDataComparator;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.models.listeners.ContextTableModelListener;
import fr.soleil.bensikin.tools.Messages;


/**
 * The table model used by ContextListTable, this model lists the current list of contexts.
 * Its rows are ContextData objects.
 * A singleton class.
 *
 * @author CLAISSE
 */
public class ContextListTableModel extends DefaultTableModel
{
	private ContextData[] rows;
	private String[] columnsNames;

	private static ContextListTableModel instance = null;
	private int idSort = ContextDataComparator.NO_SORT;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static ContextListTableModel getInstance()
	{
		if ( instance == null )
		{
			instance = new ContextListTableModel();
		}

		return instance;
	}

	/**
	 * Forces a new instantiation, used to reset the model.
	 *
	 * @return The new instance
	 */
	public static ContextListTableModel forceReset()
	{
		instance = new ContextListTableModel();
		return instance;
	}

	/**
	 * Returns the ContextData located at row <code>rowIndex</code>.
	 *
	 * @param rowIndex The specified row
	 * @return The ContextData located at row <code>rowIndex</code>
	 */
	public ContextData getContextAtRow(int rowIndex)
	{
		if ( rows != null )
		{
			return rows[ rowIndex ];
		}
		else
		{
			return null;
		}
	}

	/**
	 * Initializes the columns titles, and adds a ContextTableModelListener to itself.
	 */
	private ContextListTableModel()
	{
		String msgId = Messages.getMessage("CONTEXT_LIST_COLUMNS_ID");
		String msgTime = Messages.getMessage("CONTEXT_LIST_COLUMNS_TIME");
		String msgName = Messages.getMessage("CONTEXT_LIST_COLUMNS_NAME");
		String msgAuthor = Messages.getMessage("CONTEXT_LIST_COLUMNS_AUTHOR");
		String msgReason = Messages.getMessage("CONTEXT_LIST_COLUMNS_REASON");
		String msgDescription = Messages.getMessage("CONTEXT_LIST_COLUMNS_DESCRIPTION");

		columnsNames = new String[ this.getColumnCount() ];
		columnsNames[ 0 ] = msgId;
		columnsNames[ 1 ] = msgTime;
		columnsNames[ 2 ] = msgName;
		columnsNames[ 3 ] = msgAuthor;
		columnsNames[ 4 ] = msgReason;
		columnsNames[ 5 ] = msgDescription;

		this.addTableModelListener(new ContextTableModelListener());
	}

	/**
	 * Removes all rows and refreshes the model.
	 */
	public void reset()
	{
		if ( this.getRowCount() != 0 )
		{
			int firstRemoved = 0;
			int lastRemoved = this.getRowCount() - 1;

			this.rows = null;

			this.fireTableRowsDeleted(firstRemoved , lastRemoved);
		}
	}

	/**
	 * Removes all rows which indexes are found in <code>indexesToRemove</code>.
	 *
	 * @param indexesToRemove The list of rows to remove
	 */
	public void removeRows(int[] indexesToRemove)
	{
		int numberOfLinesToRemove = indexesToRemove.length;
		ContextData[] newRows = new ContextData[ rows.length - numberOfLinesToRemove ];

		Vector idsToRemoveList = new Vector(numberOfLinesToRemove);
		for ( int i = 0 ; i < numberOfLinesToRemove ; i++ )
		{
			int idOfLineToRemove = this.getContextAtRow(indexesToRemove[ i ]).getId();
			idsToRemoveList.add(new Integer(idOfLineToRemove));
		}

		int j = 0;
		for ( int i = 0 ; i < rows.length ; i++ )
		{
			Integer idOfCurrentLine = new Integer(this.getContextAtRow(i).getId());
			if ( !idsToRemoveList.contains(idOfCurrentLine) )
			{
				newRows[ j ] = rows[ i ];
				j++;
			}
			else
			{
				Context.removeOpenedContext(idOfCurrentLine.intValue());
			}
		}

		rows = newRows;

		for ( int i = 0 ; i < numberOfLinesToRemove ; i++ )
		{
			this.fireTableRowsDeleted(indexesToRemove[ i ] , indexesToRemove[ i ]);
		}
	}

	/**
	 * Completely sets the model by specifying its rows.
	 *
	 * @param _rows The new rows
	 */
	public void setRows(ContextData[] _rows)
	{
		this.rows = _rows;
		this.fireTableRowsInserted(0 , _rows.length - 1);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount()
	{
		return 6;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount()
	{
		if ( rows == null )
		{
			return 0;
		}

		return rows.length;
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex , int columnIndex)
	{
		Object value = null;

		switch ( columnIndex )
		{
			case 0:
				value = new Integer(rows[ rowIndex ].getId());
				break;

			case 1:
				value = rows[ rowIndex ].getCreation_date();
				break;

			case 2:
				value = rows[ rowIndex ].getName();
				break;

			case 3:
				value = rows[ rowIndex ].getAuthor_name();
				break;

			case 4:
				value = rows[ rowIndex ].getReason();
				break;

			case 5:
				value = rows[ rowIndex ].getDescription();
				break;

			default:
				return null;
		}

		return value;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex)
	{
		return columnsNames[ columnIndex ];
	}

	/**
	 * Sorts the table's lines relative to the specified column.
	 * If the the table is already sorted relative to this column, reverses the sort.
	 *
	 * @param clickedColumnIndex The index of the column to sort the lines by
	 */
	public void sort(int clickedColumnIndex)
	{
		switch ( clickedColumnIndex )
		{
			case 0:
				sortByColumn(ContextDataComparator.COMPARE_ID);
				break;

			case 1:
				sortByColumn(ContextDataComparator.COMPARE_TIME);
				break;

			case 2:
				sortByColumn(ContextDataComparator.COMPARE_NAME);
				break;

			case 3:
				sortByColumn(ContextDataComparator.COMPARE_AUTHOR);
				break;

			case 4:
				sortByColumn(ContextDataComparator.COMPARE_REASON);
				break;

			case 5:
				sortByColumn(ContextDataComparator.COMPARE_DESCRIPTION);
				break;
		}
	}

	/**
	 * Sorts the table's lines relative to the specified field.
	 * If the the table is already sorted relative to this column, reverses the sort.
	 *
	 * @param compareCase The type of field to sort the lines by
	 */
	private void sortByColumn(int compareCase)
	{
		int newSortType = ContextDataComparator.getNewSortType(this.idSort);

		Vector v = new Vector();
		for ( int i = 0 ; i < rows.length ; i++ )
		{
			v.add(this.getContextAtRow(i));
		}

		Collections.sort(v , new ContextDataComparator(compareCase));
		if ( newSortType == ContextDataComparator.SORT_DOWN )
		{
			Collections.reverse(v);
		}

		ContextData[] newRows = new ContextData[ rows.length ];
		Enumeration enumer = v.elements();
		int i = 0;
		while ( enumer.hasMoreElements() )
		{
			newRows[ i ] = ( ContextData ) enumer.nextElement();
			i++;
		}

		this.rows = newRows;
		this.fireTableDataChanged();

		this.idSort = newSortType;
	}

	/**
	 * Adds a line at the end of the current model for the specified context, and refreshes the list.
	 *
	 * @param savedContext The context to add to the current list
	 */
	public void updateList(Context savedContext)
	{
		ContextData[] before = this.rows;
		if ( before == null )
		{
			before = new ContextData[ 0 ];
		}
		rows = new ContextData[ before.length + 1 ];

		for ( int i = 0 ; i < before.length ; i++ )
		{
			rows[ i ] = before[ i ];
		}
		rows[ before.length ] = savedContext.getContextData();

		this.fireTableRowsInserted(before.length , before.length);
	}


}
