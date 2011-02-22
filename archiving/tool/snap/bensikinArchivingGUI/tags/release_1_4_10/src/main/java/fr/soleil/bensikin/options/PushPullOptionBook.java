//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/PushPullOptionBook.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PushPullOptionBook.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: PushPullOptionBook.java,v $
// Revision 1.4  2005/11/29 18:25:27  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.options;

/**
 * Represents a sub-set of the application's options, an "option book", that can do two things:
 * <UL>
 * <LI> fill itself from an options dialog
 * <LI> fill the options dialog with the values of its attributes.
 * </UL>
 *
 * @author CLAISSE
 */
public interface PushPullOptionBook
{
	/**
	 * Fills itself from an options dialog
	 */
	public void fillFromOptionsDialog();

	/**
	 * Fills the options dialog with the values of its attributes
	 *
	 * @throws Exception
	 */
	public void push() throws Exception;
}
