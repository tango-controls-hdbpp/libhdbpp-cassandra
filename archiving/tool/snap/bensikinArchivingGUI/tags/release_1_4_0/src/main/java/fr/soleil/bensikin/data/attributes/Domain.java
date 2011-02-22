//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/attributes/Domain.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Domain.
//						(Garda Laure) - 23 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: Domain.java,v $
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
 * Represents a Tango domain, containing families
 *
 * @author CLAISSE
 */
public class Domain
{
	private String name;
	private Vector families;

	/**
	 * Builds a Domain of name <code>_name</code>
	 *
	 * @param _name The name of the Domain
	 */
	public Domain(String _name)
	{
		families = new Vector();
		this.name = _name;
	}

	/**
	 * Adds a family.
	 *
	 * @param family The family to add
	 */
	public void addFamily(Family family)
	{
		families.add(family);
	}


	/**
	 * @return Returns the families.
	 */
	public Vector getFamilies()
	{
		return families;
	}

	/**
	 * @param _families The families to set.
	 */
	public void setFamilies(Vector _families)
	{
		this.families = _families;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param _name The name to set.
	 */
	public void setName(String _name)
	{
		this.name = _name;
	}
}
