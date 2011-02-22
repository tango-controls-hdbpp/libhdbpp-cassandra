//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/PushPullOptionBook.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PushPullOptionBook.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: PushPullOptionBook.java,v $
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.options;

public interface PushPullOptionBook {
	/**
	 * 8 juil. 2005
	 */
	public void fillFromOptionsDialog();

	/**
	 * @throws Exception
	 *             8 juil. 2005
	 */
	public void push() throws Exception;
}
