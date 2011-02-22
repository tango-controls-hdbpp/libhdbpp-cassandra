//+======================================================================
// $Source:  $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Condition.
//						(Garda Laure) - 1 juil. 2005
//
// $Author:  $
//
// $Revision: $
//
// $Log: $
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.commonarchivingapi.ArchivingTools.Tools;

/**
 * Description :
 * A Condition object describes a search criterion for a request into the database.
 * A Condition contains :
 * a name of a table's field
 * an operator
 * a value.
 *
 * @author GARDA
 */
public class Condition
{
	private String column;		// Name of the table's field.
	private String operator;	// Operator of the condition.
	private String value;		// Value of the condition.

	/**
	 * This constructor takes three parameters as inputs.
	 *
	 * @param _column
	 * @param _operator
	 * @param _value
	 */
	public Condition(String _column , String _operator , String _value)
	{
		this.column = _column;
		this.operator = _operator;
		this.value = _value;
	}

	/**
	 * Returns the name of the table's field.
	 *
	 * @return Name of the table's field
	 */
	public String getColumn()
	{
		return column;
	}

	/**
	 * Sets the name of the table's field.
	 *
	 * @param column Name of the table's field
	 */
	public void setColumn(String column)
	{
		this.column = column;
	}

	/**
	 * Returns the operator of the condition.
	 *
	 * @return Operator of the condition
	 */
	public String getOperator()
	{
		return operator;
	}

	/**
	 * Sets the operator of the condition.
	 *
	 * @param operator Operator of the condition
	 */
	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	/**
	 * Returns the value of the condition.
	 *
	 * @return Value of the condition
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Sets the value of the condition.
	 *
	 * @param value Value of the condition
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
	 * Returns a String which represente the object Condition.
	 *
	 * @return String which represente the object Condition
	 */
	public String toString()
	{
		String condition_str = "";
		condition_str = getColumn() + " " + getOperator() + " " + getValue() + "\r\n";
		return condition_str;
	}

}
