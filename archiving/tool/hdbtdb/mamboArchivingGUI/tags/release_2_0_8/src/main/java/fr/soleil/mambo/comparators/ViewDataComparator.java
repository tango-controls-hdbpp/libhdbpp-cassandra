//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewDataComparator.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.1  2006/08/07 13:01:08  ounsy
// comparators moved to "comparators" package
//
// Revision 1.1  2005/11/29 18:28:12  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.comparators;

import java.util.Comparator;

public class ViewDataComparator implements Comparator
{

    public ViewDataComparator ()
    {

    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare ( Object arg0 , Object arg1 )
    {
        Object[] data0 = ( Object[] ) arg0;
        Object[] data1 = ( Object[] ) arg1;
        Double d0 = ( Double ) data0[ 1 ];
        Double d1 = ( Double ) data1[ 1 ];
        return d0.compareTo( d1 );
    }

}
