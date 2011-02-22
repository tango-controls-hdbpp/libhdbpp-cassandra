//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/attributes/Family.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Family.
//						(Claisse Laurent) - 23 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: Family.java,v $
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

/**
 * Represents a Tango family, containing members and owned by a domain
 *
 * @author CLAISSE
 */
public class Family
{
	private String name;
	private Vector members;

	/**
	 * Builds a Family of name <code>_name</code>
	 *
	 * @param _name The name of the Family
	 */
	public Family(String _name)
	{
		members = new Vector();
		this.name = _name;
	}

	/**
	 * Adds a member.
	 *
	 * @param member The member to add
	 */
	public void addMember(Member member)
	{
		members.add(member);
	}

	/**
	 * @return Returns the members.
	 */
	public Vector getMembers()
	{
		return members;
	}

	/**
	 * @param members The members to set.
	 */
	public void setMembers(Vector members)
	{
		this.members = members;
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
