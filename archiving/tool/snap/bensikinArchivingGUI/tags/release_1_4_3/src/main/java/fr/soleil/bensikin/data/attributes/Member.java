//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/attributes/Member.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Member.
//						(Claisse Laurent) - 23 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: Member.java,v $
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:37  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.attributes;

import java.util.Vector;

import fr.soleil.bensikin.data.context.ContextAttribute;


/**
 * Represents a Tango member, containing attributes and owned by a family
 *
 * @author CLAISSE
 */
public class Member
{
	private String name;
	private Vector attributes;

	/**
	 * Builds a Member of name <code>_name</code>
	 *
	 * @param _name The name of the Member
	 */
	public Member(String _name)
	{
		attributes = new Vector();
		this.name = _name;
	}

	/**
	 * Adds a ContextAttribute.
	 *
	 * @param attribute The attribute to add
	 */
	public void addAttribute(ContextAttribute attribute)
	{
		attributes.add(attribute);
	}

	/**
	 * @return Returns the attributes.
	 */
	public Vector getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes The attributes to set.
	 */
	public void setAttributes(Vector attributes)
	{
		this.attributes = attributes;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
