//+======================================================================
//$Source$
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ContextDataComparator.
//						(Claisse Laurent) - 13 juil. 2005
//
//$Author$
//
//$Revision$
//
//$Log$
//Revision 1.3  2006/11/09 14:23:42  ounsy
//domain/family/member/attribute refactoring
//
//Revision 1.2  2006/10/19 12:36:54  ounsy
//refactoring
//
//Revision 1.1  2006/08/07 13:01:53  ounsy
//usefull for sorting trees and lists
//
//Revision 1.1  2005/11/29 18:27:24  chinkumo
//no message
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
package fr.soleil.mambo.comparators;

import java.util.Comparator;

import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;


/**
 * Implements a Comparator on ContextData objects, useful to sort the contexts list.
 * The comparison can be on any one of the ContextData fields, depending on the type specified on constructing the comparator.
 */
public class ArchivingConfigurationAttributeComparator implements Comparator
{
    public static final int COMPARE_DEVICE_CLASS = 0;
    public static final int COMPARE_DEVICE = 1;
    public static final int COMPARE_NAME = 2;

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
    public ArchivingConfigurationAttributeComparator ( int _fieldToCompare ) throws IllegalArgumentException
    {
        super();
        this.fieldToCompare = _fieldToCompare;

        if
        (
                _fieldToCompare != COMPARE_DEVICE &&
                _fieldToCompare != COMPARE_DEVICE_CLASS &&
                _fieldToCompare != COMPARE_NAME
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

        if ( formerSortType == ArchivingConfigurationAttributeComparator.NO_SORT || formerSortType == ArchivingConfigurationAttributeComparator.SORT_DOWN )
        {
            ret = ArchivingConfigurationAttributeComparator.SORT_UP;
        }
        else
        {
            ret = ArchivingConfigurationAttributeComparator.SORT_DOWN;
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare ( Object arg0 , Object arg1 )
    {
        ArchivingConfigurationAttribute data1 = ( ArchivingConfigurationAttribute ) arg0;
        ArchivingConfigurationAttribute data2 = ( ArchivingConfigurationAttribute ) arg1;

        int ret = 0;

        String criterion1 = null;
        String criterion2 = null;
        
        switch ( fieldToCompare )
        {
            case COMPARE_DEVICE:
                criterion1 = data1.getDeviceName();
                criterion2 = data2.getDeviceName();
            break;

            case COMPARE_DEVICE_CLASS:
                criterion1 = data1.getDeviceClass();
                criterion2 = data2.getDeviceClass();
            break;

            case COMPARE_NAME:
                criterion1 = data1.getName();
                criterion2 = data2.getName();
            break;
                
            default:
                throw new IllegalArgumentException();
        }

        ret = criterion1.compareTo( criterion2 );
        return ret;
    }
}
