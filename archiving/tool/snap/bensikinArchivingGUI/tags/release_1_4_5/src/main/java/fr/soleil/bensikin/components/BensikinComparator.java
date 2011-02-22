package fr.soleil.bensikin.components;

import java.util.Comparator;

/**
* Implements a Comparator on ContextData objects, useful to sort the contexts list.
* The comparison can be on any one of the ContextData fields, depending on the type specified on constructing the comparator.
*
* @author CLAISSE
*/
public class BensikinComparator implements Comparator
{
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

	protected int fieldToCompare;

	/**
	 * Builds a comparator 
	 *
	 * @param _fieldToCompare The field on which the comparison will be done
	 */
	protected BensikinComparator(int _fieldToCompare)
	{
		this.fieldToCompare = _fieldToCompare;
	}

	/**
	 * Returns the new sort type to switch to on user request, given the current one.
	 *
	 * @param formerSortType The current sort type
	 * @return The new sort type
	 */
	public static int getNewSortType(int formerSortType)
	{
		int ret;

		if ( formerSortType == BensikinComparator.NO_SORT || formerSortType == BensikinComparator.SORT_DOWN )
		{
			ret = BensikinComparator.SORT_UP;
		}
		else
		{
			ret = BensikinComparator.SORT_DOWN;
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object arg0 , Object arg1)
	{
	    String name1 = arg0 + "";
	    String name2 = arg1 + "";

		int ret = name1.compareTo(name2);
		return ret;
	}

    /**
     * @param name1
     * @param name2
     * @return
     */
    public int compareStrings(String name1, String name2) 
    {
        int ret;
        
        if ( name1 == null )
		{
			if ( name2 != null )
			{
				ret = Integer.MAX_VALUE;
			}
			else
			{
				ret = 0;
			}
		}
		else if ( name2 == null )
		{
			ret = Integer.MIN_VALUE;
		}
		else
		{
			ret = name1.compareTo(name2);
		}
        
        return ret;
    }
}
