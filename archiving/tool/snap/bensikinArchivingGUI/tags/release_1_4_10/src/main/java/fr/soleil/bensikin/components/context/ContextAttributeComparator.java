//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/ContextAttributeComparator.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ContextDataComparator.
//						(Claisse Laurent) - 13 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.1 $
//
//$Log: ContextAttributeComparator.java,v $
//Revision 1.1  2005/12/14 16:52:23  ounsy
//added methods necessary for alternate attribute selection
//
//Revision 1.1.1.2  2005/08/22 11:58:32  chinkumo
//First commit
//
//
//copyleft :		    Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.context;

import java.util.Comparator;

import fr.soleil.bensikin.data.context.ContextAttribute;


/**
* Implements a Comparator on ContextData objects, useful to sort the contexts list.
* The comparison can be on any one of the ContextData fields, depending on the type specified on constructing the comparator.
*/
public class ContextAttributeComparator implements Comparator
{

  /**
   * The comparison will be on the ContextData's id field
   */
  public static final int COMPARE_DOMAIN = 0;

  /**
   * The comparison will be on the ContextData's creation date field
   */
  public static final int COMPARE_DEVICE_CLASS = 1;

  /**
   * The comparison will be on the ContextData's name field
   */
  public static final int COMPARE_NAME = 2;

  /**
   * The comparison will be on the ContextData's author field
   */
  public static final int COMPARE_SELECTED = 3;


  /**
   * The ContextData list isn't sorted relative to <code>fieldToCompare</code> yet
   */
  public static final int NO_SORT = 0;
  /**
   * The ContextData list is sorted by ascending <code>fieldToCompare</code>.
   */
  public static final int SORT_UP = 1;
  /**
   * The ContextData list is sorted by descending <code>fieldToCompare</code>.
   */
  public static final int SORT_DOWN = 2;

  private int fieldToCompare;

  /**
   * Builds a comparator on the desired ContextData field
   *
   * @param _fieldToCompare The field on which the comparison will be done
   * @throws IllegalArgumentException If _fieldToCompare isn't in (COMPARE_ID, COMPARE_TIME
   *                                  COMPARE_NAME, COMPARE_AUTHOR, COMPARE_REASON, COMPARE_DESCRIPTION)
   */
  public ContextAttributeComparator ( int _fieldToCompare ) throws IllegalArgumentException
  {
      super();
      this.fieldToCompare = _fieldToCompare;

      if
      (
              _fieldToCompare != COMPARE_DOMAIN &&
              _fieldToCompare != COMPARE_DEVICE_CLASS &&
              _fieldToCompare != COMPARE_NAME &&
              _fieldToCompare != COMPARE_SELECTED
      )
      {
          throw new IllegalArgumentException();
      }
  }

  /**
   * Returns the new sort type to switch to on user request, given the current one.
   *
   * @param formerSortType The current sort type
   * @return The new sort type
   */
  public static int getNewSortType ( int formerSortType )
  {
      int ret;

      if ( formerSortType == ContextAttributeComparator.NO_SORT || formerSortType == ContextAttributeComparator.SORT_DOWN )
      {
          ret = ContextAttributeComparator.SORT_UP;
      }
      else
      {
          ret = ContextAttributeComparator.SORT_DOWN;
      }

      return ret;
  }

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare ( Object arg0 , Object arg1 )
  {
      ContextAttribute data1 = ( ContextAttribute ) arg0;
      ContextAttribute data2 = ( ContextAttribute ) arg1;

      int ret = 0;

      switch ( fieldToCompare )
      {

          case COMPARE_DOMAIN:
              String domain1 = data1.getDomain();
              String domain2 = data2.getDomain();
              ret = domain1.compareTo( domain2 );
              break;

          case COMPARE_DEVICE_CLASS:
              String deviceClass1 = data1.getDevice();
              String deviceClass2 = data2.getDevice();
              ret = deviceClass1.compareTo( deviceClass2 );
              break;

          case COMPARE_NAME:
              String name1 = data1.getName();
              String name2 = data2.getName();
              ret = name1.compareTo( name2 );
              break;

          case COMPARE_SELECTED:
              boolean selected1 = data1.isSelected();
              boolean selected2 = data2.isSelected();
              if ( selected1 )
              {
                  if ( selected2 )
                  {
                      ret = 0;
                  }
                  else
                  {
                      ret = 1;
                  }
              }
              else
              {
                  if ( selected2 )
                  {
                      ret = -1;
                  }
                  else
                  {
                      ret = 0;
                  }
              }
              break;

          default:
              throw new IllegalArgumentException();
      }

      return ret;
  }
}
