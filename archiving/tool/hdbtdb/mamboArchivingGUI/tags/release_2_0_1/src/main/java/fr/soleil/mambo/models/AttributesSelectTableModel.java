//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/models/AttributesSelectTableModel.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ContextListTableModel.
//						(Claisse Laurent) - 30 juin 2005
//
//$Author: ounsy $
//
//$Revision: 1.5 $
//
//$Log: AttributesSelectTableModel.java,v $
//Revision 1.5  2006/11/09 14:23:42  ounsy
//domain/family/member/attribute refactoring
//
//Revision 1.4  2006/10/19 12:46:26  ounsy
//corrected the columns
//
//Revision 1.3  2006/08/07 13:03:07  ounsy
//trees and lists sort
//
//Revision 1.2  2006/05/16 12:03:57  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:27:07  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.models;

import java.text.Collator;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.comparators.ArchivingConfigurationAttributeComparator;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.tools.Messages;


/**
 * The table model used by ContextListTable, this model lists the current list of contexts.
 * Its rows are ContextData objects.
 * A singleton class.
 */
public class AttributesSelectTableModel extends DefaultTableModel
{
    private ArchivingConfigurationAttribute[] rows;
    private String[] columnsNames;
    private TreeMap htAttr;

    private int idSort = ArchivingConfigurationAttributeComparator.NO_SORT;

    private static AttributesSelectTableModel instance = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static AttributesSelectTableModel getInstance ()
    {
        if ( instance == null )
        {
            instance = new AttributesSelectTableModel();
        }

        return instance;
    }

    /**
     * Forces a new instantiation, used to reset the model.
     *
     * @return The new instance
     */
    public static AttributesSelectTableModel forceReset ()
    {
        instance = new AttributesSelectTableModel();
        return instance;
    }


    /**
     * Initializes the columns titles, and adds a ContextTableModelListener to itself.
     */
    private AttributesSelectTableModel ()
    {
        String msgDeviceClass = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_DEVICE_CLASS" );
        String msgAttribute = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_ATTRIBUTE" );
        String msgDevice = Messages.getMessage( "DIALOGS_EDIT_AC_ATTRIBUTES_ALTERNATE_DEVICE" );

        columnsNames = new String[ this.getColumnCount() ];
        columnsNames[ 0 ] = msgDeviceClass;
        columnsNames[ 1 ] = msgDevice;
        columnsNames[ 2 ] = msgAttribute;
        columnsNames[ 3 ] = " ";

        htAttr = new TreeMap(Collator.getInstance(Locale.FRENCH));
    }

    /**
     * Removes all rows and refreshes the model.
     */
    public void reset ()
    {
        if ( this.getRowCount() != 0 )
        {
            int firstRemoved = 0;
            int lastRemoved = this.getRowCount() - 1;

            this.rows = null;
            this.htAttr = new TreeMap(Collator.getInstance(Locale.FRENCH));

            this.fireTableRowsDeleted( firstRemoved , lastRemoved );
        }

        ACAttributesTreeModel treeModel = ACAttributesTreeModel.getInstance();
        treeModel.removeAll();
    }

    public ArchivingConfigurationAttribute getACAttributeAtRow ( int rowIndex )
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
     * Removes all rows which indexes are found in <code>indexesToRemove</code>.
     *
     * @param indexesToRemove The list of rows to remove
     */
    public void removeRows ( int[] indexesToRemove )
    {
        int numberOfLinesToRemove = indexesToRemove.length;
        ArchivingConfigurationAttribute[] newRows = new ArchivingConfigurationAttribute[ rows.length - numberOfLinesToRemove ];

        Vector idsToRemoveList = new Vector( numberOfLinesToRemove );
        for ( int i = 0 ; i < numberOfLinesToRemove ; i++ )
        {
            String idOfLineToRemove = this.getACAttributeAtRow( indexesToRemove[ i ] ).getCompleteName();
            idsToRemoveList.add( idOfLineToRemove );
        }

        int j = 0;
        Vector listToAddToTreeModel = new Vector();
        for ( int i = 0 ; i < rows.length ; i++ )
        {
            String idOfCurrentLine = this.getACAttributeAtRow( i ).getCompleteName();
            if ( !idsToRemoveList.contains( idOfCurrentLine ) )
            {
                newRows[ j ] = rows[ i ];
                j++;
            }
            else
            {
                TreePath aPath = this.getACAttributeAtRow( i ).getTreePath();
                listToAddToTreeModel.add( aPath );
                htAttr.remove( idOfCurrentLine );
            }
        }
        
        ACAttributesTreeModel treeModel = ACAttributesTreeModel.getInstance();
        treeModel.removeSelectedAttributes2( listToAddToTreeModel );

        TreeMap attrs = treeModel.getAttributes();
        ArchivingConfiguration currentArchivingConfiguration = ArchivingConfiguration.getCurrentArchivingConfiguration();
        currentArchivingConfiguration.getAttributes().removeAttributesNotInList( attrs );
        
        rows = newRows;
        this.fireTableDataChanged();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount ()
    {
        return 4;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount ()
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
    public Object getValueAt ( int rowIndex , int columnIndex )
    {
        Object value = null;

        switch ( columnIndex )
        {
            case 0:
                value = rows[ rowIndex ].getDeviceClass ();
            break;

            case 1:
                value = rows[ rowIndex ].getDeviceName ();
            break;
            
            case 2:
                value = rows[ rowIndex ].getName ();
            break;
            
            case 3:
                value = new Boolean( rows[ rowIndex ].isSelected() );
            break;

            default:
                return null;
        }

        return value;
    }

    public void setValueAt ( Object aValue , int rowIndex , int columnIndex )
    {
        if ( rowIndex >= this.getRowCount() )
        {
            return;
        }

        switch ( columnIndex )
        {
            case 3:
                Boolean _isSelected = ( Boolean ) aValue;
                rows[ rowIndex ].setSelected( _isSelected.booleanValue() );
            break;

            default:
                //do nothing, only write values can be edited
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName ( int columnIndex )
    {
        return columnsNames[ columnIndex ];
    }


    /**
     * Adds a line at the end of the current model for the specified context, and refreshes the list.
     *
     * @param newACAttribute The context to add to the current list
     */
    public void updateList ( ArchivingConfigurationAttribute newACAttribute )
    {

        String newACAttributeName = newACAttribute.getCompleteName();
        if ( htAttr.containsKey( newACAttributeName ) )
        {
            return;
        }

        ArchivingConfigurationAttribute[] before = this.rows;
        int countBefore = this.getRowCount();
        rows = new ArchivingConfigurationAttribute[ countBefore + 1 ];

        for ( int i = 0 ; i < countBefore ; i++ )
        {
            rows[ i ] = before[ i ];
        }

        rows[ countBefore ] = newACAttribute;
        newACAttribute.setNew( true );
        htAttr.put( newACAttributeName , newACAttribute );

        Vector listToAddToTreeModel = new Vector( 1 );

        TreePath aPath = newACAttribute.getTreePath();
        listToAddToTreeModel.add( aPath );

        ACAttributesTreeModel treeModel = ACAttributesTreeModel.getInstance();
        treeModel.addSelectedAttibutes( listToAddToTreeModel );
        
        this.fireTableRowsInserted( countBefore , countBefore );
    }

    /**
     * @return Returns the rows.
     */
    public ArchivingConfigurationAttribute[] getRows ()
    {
        return rows;
    }

    /**
     * @param rows The rows to set.
     */
    public void setRows ( ArchivingConfigurationAttribute[] _rows )
    {
        this.rows = _rows;

        if ( _rows == null || _rows.length == 0 )
        {
            return;
        }

        Vector listToAddToTreeModel = new Vector();
        for ( int i = 0 ; i < _rows.length ; i++ )
        {
            htAttr.put( _rows[ i ].getCompleteName() , _rows[ i ] );

            TreePath aPath = _rows[ i ].getTreePath();
            listToAddToTreeModel.add( aPath );
        }

        this.fireTableRowsInserted( 0 , _rows.length - 1 );

        ACAttributesTreeModel treeModel = ACAttributesTreeModel.getInstance();
        treeModel.addSelectedAttibutes( listToAddToTreeModel );
    }

    /**
     * @param selectedRows
     */
    public void reverseSelectionForRows ( int[] selectedRows )
    {
        if ( selectedRows == null )
        {
            return;
        }

        for ( int i = 0 ; i < selectedRows.length ; i++ )
        {
            ArchivingConfigurationAttribute attr = this.getACAttributeAtRow( selectedRows[ i ] );
            attr.reverseSelection();

            this.fireTableRowsUpdated( selectedRows[ i ] , selectedRows[ i ] );
        }
    }

    /**
     * @param b
     */
    public void selectAllOrNone ( boolean b )
    {
        if ( rows == null )
        {
            return;
        }

        for ( int i = 0 ; i < rows.length ; i++ )
        {
            ArchivingConfigurationAttribute attr = this.getACAttributeAtRow( i );
            attr.setSelected( b );
        }

        this.fireTableRowsUpdated( 0 , this.getRowCount() - 1 );
    }

    /**
     * Sorts the table's lines relative to the specified column.
     * If the the table is already sorted relative to this column, reverses the sort.
     *
     * @param clickedColumnIndex The index of the column to sort the lines by
     */
    public void sort ( int clickedColumnIndex )
    {
        switch ( clickedColumnIndex )
        {
            case 0:
                sortByColumn( ArchivingConfigurationAttributeComparator.COMPARE_DEVICE_CLASS );
            break;

            case 1:
                sortByColumn( ArchivingConfigurationAttributeComparator.COMPARE_DEVICE );
            break;

            case 2:
                sortByColumn( ArchivingConfigurationAttributeComparator.COMPARE_NAME );
            break;
        }
    }

    /**
     * Sorts the table's lines relative to the specified field.
     * If the the table is already sorted relative to this column, reverses the sort.
     *
     * @param compareCase The type of field to sort the lines by
     */
    private void sortByColumn ( int compareCase )
    {
        int newSortType = ArchivingConfigurationAttributeComparator.getNewSortType( this.idSort );

        Vector v = new Vector();
        for ( int i = 0 ; i < rows.length ; i++ )
        {
            v.add( this.getACAttributeAtRow( i ) );
        }

        Collections.sort( v , new ArchivingConfigurationAttributeComparator( compareCase ) );
        if ( newSortType == ArchivingConfigurationAttributeComparator.SORT_DOWN )
        {
            Collections.reverse( v );
        }

        ArchivingConfigurationAttribute[] newRows = new ArchivingConfigurationAttribute[ rows.length ];
        Enumeration enumeration = v.elements();
        int i = 0;
        while ( enumeration.hasMoreElements() )
        {
            newRows[ i ] = ( ArchivingConfigurationAttribute ) enumeration.nextElement();
            i++;
        }

        this.rows = newRows;
        this.fireTableDataChanged();

        this.idSort = newSortType;
    }

    /**
     * 
     */
    public void removeNotSelectedRows ()
    {
        Vector listOfIndexesToRemove = new Vector();
        for ( int i = 0 ; i < this.getRowCount() ; i++ )
        {
            ArchivingConfigurationAttribute attr = this.getACAttributeAtRow( i );
            if ( !attr.isSelected() )
            {
                listOfIndexesToRemove.add( new Integer( i ) );
            }
        }

        int[] indexesToRemove = new int[ listOfIndexesToRemove.size() ];
        Enumeration enumeration = listOfIndexesToRemove.elements();
        int j = 0;
        while ( enumeration.hasMoreElements() )
        {
            Integer next = ( Integer ) enumeration.nextElement();
            indexesToRemove[ j ] = next.intValue();
            j++;
        }

        removeRows( indexesToRemove );
    }
}
