//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/AttributesSelectTableModel.java,v $
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
//Revision 1.5  2007/08/24 14:09:12  ounsy
//Context attributes ordering (Mantis bug 3912)
//
//Revision 1.4  2006/06/28 12:53:20  ounsy
//minor changes
//
//Revision 1.3  2006/04/13 14:42:01  ounsy
//removed useless logs
//
//Revision 1.2  2006/01/12 12:58:56  ounsy
//avoiding NullPointerException
//
//Revision 1.1  2005/12/14 16:57:38  ounsy
//added methods necessary for alternate attribute selection
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
package fr.soleil.bensikin.models;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.components.context.ContextAttributeComparator;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.tools.Messages;





/**
* The table model used by ContextListTable, this model lists the current list of contexts.
* Its rows are ContextData objects.
* A singleton class.
*/
public class AttributesSelectTableModel extends DefaultTableModel
{
  private ContextAttribute[] rows;
  private String[] columnsNames;
  private Hashtable htAttr;

  private int idSort = ContextAttributeComparator.NO_SORT;

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
      String msgDomain = Messages.getMessage( "CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_DOMAIN" );
      String msgDeviceClass = Messages.getMessage( "CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_DEVICE_CLASS" );
      String msgAttribute = Messages.getMessage( "CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_ATTRIBUTE" );

      columnsNames = new String[ this.getColumnCount() ];
      columnsNames[ 0 ] = msgDomain;
      columnsNames[ 1 ] = msgDeviceClass;
      columnsNames[ 2 ] = msgAttribute;
      columnsNames[ 3 ] = " ";

      htAttr = new Hashtable();
      //ACPossibleAttributesTreeModel.getInstance ();
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
          this.htAttr = new Hashtable();

          this.fireTableRowsDeleted( firstRemoved , lastRemoved );
      }

      //------
      ContextAttributesTreeModel treeModel = ContextAttributesTreeModel.getInstance( false );
      treeModel.removeAll();

  }

  public ContextAttribute getACAttributeAtRow ( int rowIndex )
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
      //System.out.println ( "numberOfLinesToRemove/"+numberOfLinesToRemove+"/" );
      ContextAttribute[] newRows = new ContextAttribute[ rows.length - numberOfLinesToRemove ];

      Vector idsToRemoveList = new Vector( numberOfLinesToRemove );
      for ( int i = 0 ; i < numberOfLinesToRemove ; i++ )
      {
          String idOfLineToRemove = this.getACAttributeAtRow( indexesToRemove[ i ] ).getCompleteName();
          //idsToRemoveList.add(new Integer(idOfLineToRemove));
          idsToRemoveList.add( idOfLineToRemove );
          //System.out.println ( "idsToRemoveList/"+idsToRemoveList+"/" );
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
              //--
              TreePath aPath = this.getACAttributeAtRow( i ).getTreePath();
              listToAddToTreeModel.add( aPath );
              //System.out.println ( "aPath/"+aPath+"/" );
              //---
              htAttr.remove( idOfCurrentLine );
          }
      }
      ContextAttributesTreeModel treeModel = ContextAttributesTreeModel.getInstance ( false );

      //System.out.println ( "removeRows/BEFORE/"+treeModel.getAttributes().size()+"/" );
      treeModel.removeSelectedAttributes2( listToAddToTreeModel );
      //System.out.println ( "removeRows/AFTER/"+treeModel.getAttributes().size()+"/" );

      //----
      TreeMap attrs = treeModel.getAttributes();
      Context currentContext = Context.getSelectedContext ();
      //avoiding NullPointerException
      if ( (currentContext != null) && (currentContext.getContextAttributes() != null) ) {
          currentContext.getContextAttributes().removeAttributesNotInList( attrs );
      }
      //----

      rows = newRows;

      /*for (int i = 0; i < numberOfLinesToRemove; i++)
      {
          this.fireTableRowsDeleted(indexesToRemove[i], indexesToRemove[i]);
      }*/
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
              value = rows[ rowIndex ].getDomain();
              break;

          case 1:
              value = rows[ rowIndex ].getDevice();
              break;

          case 2:
              value = rows[ rowIndex ].getName();
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
      //System.out.println ( "setValueAt/rowIndex|"+rowIndex+"|columnIndex|"+columnIndex+"|" );
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
  public void updateList ( ContextAttribute newACAttribute )
  {

      String newACAttributeName = newACAttribute.getCompleteName();
      //System.out.println ( "updateList 0/newACAttributeName|"+newACAttributeName+"|" );
      if ( htAttr.containsKey( newACAttributeName ) )
      {
          return;
      }
      //System.out.println ( "updateList 1" );
      ContextAttribute[] before = this.rows;
      int countBefore = this.getRowCount();
      rows = new ContextAttribute[ countBefore + 1 ];

      for ( int i = 0 ; i < countBefore ; i++ )
      {
          rows[ i ] = before[ i ];
      }

      rows[ countBefore ] = newACAttribute;
      newACAttribute.setNew( true );
      htAttr.put( newACAttributeName , newACAttribute );
      //--------
      Vector listToAddToTreeModel = new Vector( 1 );

      TreePath aPath = newACAttribute.getTreePath();
      listToAddToTreeModel.add( aPath );

      ContextAttributesTreeModel treeModel = ContextAttributesTreeModel.getInstance( false );
      treeModel.addSelectedAttributes( listToAddToTreeModel , false );
      //---------
      this.fireTableRowsInserted( countBefore , countBefore );
  }

  /**
   * @return Returns the rows.
   */
  public ContextAttribute[] getRows ()
  {
      return rows;
  }

  /**
   * @param rows The rows to set.
   */
  public void setRows ( ContextAttribute[] _rows , boolean updateTreeToo )
  {
      //System.out.println ( "TOTO 1" );
      
      this.rows = _rows;

      if ( _rows == null || _rows.length == 0 )
      {
          if ( updateTreeToo )
          {
    	      ContextAttributesTreeModel treeModel = ContextAttributesTreeModel.getInstance( false );
    	      treeModel.removeAll();
          }
          return;
      }
      //System.out.println ( "TOTO 2" );
      Vector listToAddToTreeModel = new Vector();
      for ( int i = 0 ; i < _rows.length ; i++ )
      {
          htAttr.put( _rows[ i ].getCompleteName() , _rows[ i ] );

          //-------
          if ( updateTreeToo )
          {
	          TreePath aPath = _rows[ i ].getTreePath();
	          listToAddToTreeModel.add( aPath );
          }
          //-------
      }

      this.fireTableRowsInserted( 0 , _rows.length - 1 );

      //----
      if ( updateTreeToo )
      {
	      ContextAttributesTreeModel treeModel = ContextAttributesTreeModel.getInstance( false );
	      treeModel.addSelectedAttributes( listToAddToTreeModel , false );
      }
      //----
      //System.out.println ( "TOTO 3/this.rows/"+this.rows.length );
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
          ContextAttribute attr = this.getACAttributeAtRow( selectedRows[ i ] );
          attr.reverseSelection();

          this.fireTableRowsUpdated( selectedRows[ i ] , selectedRows[ i ] );
      }
  }

  /**
   * @param b
   */
  public void selectAllOrNone ( boolean b )
  {
      //System.out.println ( "selectAllOrNone/b/"+b+"/" );
      if ( rows == null )
      {
          return;
      }

      for ( int i = 0 ; i < rows.length ; i++ )
      {
          ContextAttribute attr = this.getACAttributeAtRow( i );
          attr.setSelected( b );
      }

      //this.fireTableDataChanged ();
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
              sortByColumn( ContextAttributeComparator.COMPARE_DOMAIN );
              break;

          case 1:
              sortByColumn( ContextAttributeComparator.COMPARE_DEVICE_CLASS );
              break;

          case 2:
              sortByColumn( ContextAttributeComparator.COMPARE_NAME );
              break;

          case 3:
              //sortByColumn(ArchivingConfigurationAttributeComparator.COMPARE_SELECTED);
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
      int newSortType = ContextAttributeComparator.getNewSortType( this.idSort );

      Vector v = new Vector();
      for ( int i = 0 ; i < rows.length ; i++ )
      {
          v.add( this.getACAttributeAtRow( i ) );
      }

      Collections.sort( v , new ContextAttributeComparator( compareCase ) );
      if ( newSortType == ContextAttributeComparator.SORT_DOWN )
      {
          Collections.reverse( v );
      }

      ContextAttribute[] newRows = new ContextAttribute[ rows.length ];
      Enumeration enumer = v.elements();
      int i = 0;
      while ( enumer.hasMoreElements() )
      {
          newRows[ i ] = ( ContextAttribute ) enumer.nextElement();
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
      //System.out.println ( "removeNotSelectedRows" );
      for ( int i = 0 ; i < this.getRowCount() ; i++ )
      {
          ContextAttribute attr = this.getACAttributeAtRow( i );
          if ( !attr.isSelected() )
          {
              listOfIndexesToRemove.add( new Integer( i ) );
              //System.out.println ( "removeNotSelectedRows/! attr.isSelected/attr|"+attr.getCompleteName ()+"|" );
          }
          else
          {
              //System.out.println ( "removeNotSelectedRows/attr.isSelected/attr|"+attr.getCompleteName ()+"|" );
          }
      }

      int[] indexesToRemove = new int[ listOfIndexesToRemove.size() ];
      Enumeration enumer = listOfIndexesToRemove.elements();
      int j = 0;
      while ( enumer.hasMoreElements() )
      {
          Integer next = ( Integer ) enumer.nextElement();
          indexesToRemove[ j ] = next.intValue();
          j++;
      }

      removeRows( indexesToRemove );


  }

	/**
	 * @param attributes
	 */
	public void setRows(TreeMap attributes) 
	{
	    ContextAttribute [] res = new ContextAttribute [ attributes.size () ];
	    Iterator enumer = attributes.keySet().iterator();
	    int i = 0;
	    
	    while ( enumer.hasNext() )
	    {
	        String key = (String) enumer.next();
	        ContextAttribute attribute = ( ContextAttribute ) attributes.get( key );
	        res [ i ] = attribute;
	        i++;
	    }
	 
	    this.setRows ( res , false );
	}


}
