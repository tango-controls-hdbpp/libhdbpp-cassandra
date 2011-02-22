//+======================================================================
// $Source:  $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Criterions.
//						(Garda Laure) - 1 juil. 2005
//
// $Author:  $
//
// $Revision:  $
//
// $Log:  $
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.commonarchivingapi.ArchivingTools.Tools;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Description :
 * A Criterion object describes a set of search criteria for a request into the database
 * A Criterion contains :
 * a set of Condition objects.
 *
 * @author GARDA
 */
public class Criterions implements java.io.Serializable
{
	private Hashtable conditionsHT; // set of Condition objects

	/**
	 * Default constructor.
	 */
	public Criterions()
	{
		conditionsHT = new Hashtable();
	}

	/**
	 * This constructor takes one parameter as inputs.
	 *
	 * @param conditions a set of Conditions
	 */
	public Criterions(Condition[] conditions)
	{
		conditionsHT = new Hashtable();
		// Sets the Conditions into the Criterion.
		if ( conditions != null )
		{
			int nbOfConditions = conditions.length;
			for ( int i = 0 ; i < nbOfConditions ; i++ )
			{
				Vector currentColumnConditionsList;
				String columnName = conditions[ i ].getColumn();
				// The Conditions are referencedby their table's field (columnName).
				if ( conditionsHT.containsKey(columnName) )
				{
					currentColumnConditionsList = ( Vector ) conditionsHT.get(columnName);
				}
				else
				{
					currentColumnConditionsList = new Vector();
				}

				currentColumnConditionsList.add(conditions[ i ]);
				conditionsHT.put(columnName , currentColumnConditionsList);
			}
		}
	}

	/**
	 * Adds a Condition into the Criterion.
	 *
	 * @param condition A Condition
	 */
	public void addCondition(Condition condition)
	{
		if ( condition == null )
		{
			return;
		}

		String columnName = condition.getColumn();
		Vector currentColumnConditionsList;

		if ( !conditionsHT.containsKey(columnName) )
		{
			currentColumnConditionsList = new Vector();
		}
		else
		{
			currentColumnConditionsList = ( Vector ) conditionsHT.get(columnName);
		}

		currentColumnConditionsList.add(condition);
		conditionsHT.put(columnName , currentColumnConditionsList);
	}

	/**
	 * Returns array of all Condition objects (of this Criterion) for the given table's field
	 *
	 * @param columnName
	 * @return array of all Condition objects for the given table's field
	 */
	public Condition[] getConditions(String columnName)
	{
        if ( columnName == null )
		{
			return null;
		}
        if ( conditionsHT == null )
        {
            return null;
        }
		if ( !conditionsHT.containsKey(columnName) )
		{
            return null;
		}

		Vector columnConditionsList = ( Vector ) conditionsHT.get(columnName);
		Enumeration enumer = columnConditionsList.elements();

        int nbOfConditions = columnConditionsList.size();
		int i = 0;
		Condition[] ret = new Condition[ nbOfConditions ];

		while ( enumer.hasMoreElements() )
		{
			Condition nextCondition = ( Condition ) enumer.nextElement();
			ret[ i ] = nextCondition;
			i++;
		}

		return ret;

	}

	/**
	 * Returns a String which represente the object Criterion.
	 *
	 * @return String which represente the object Criterion
	 */
	public String toString()
	{
		String criterions_str = "";
		if ( conditionsHT.isEmpty() )
		{
			return null;
		}

		Enumeration key = conditionsHT.keys();

		while ( key.hasMoreElements() )
		{
			Vector columnConditionsList = ( Vector ) conditionsHT.get(key.nextElement());

			Enumeration enumer = columnConditionsList.elements();

			while ( enumer.hasMoreElements() )
			{
				Condition nextCondition = ( Condition ) enumer.nextElement();
				criterions_str = criterions_str + nextCondition.toString();
			}
		}
		return criterions_str;

	}

	public Hashtable getConditionsHT ()
    {
	    return this.conditionsHT;    
    }
}
