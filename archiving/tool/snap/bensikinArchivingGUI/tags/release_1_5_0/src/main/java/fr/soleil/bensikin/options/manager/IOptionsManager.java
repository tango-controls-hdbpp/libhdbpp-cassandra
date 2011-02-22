//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/manager/IOptionsManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IOptionsManager.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: IOptionsManager.java,v $
// Revision 1.4  2005/11/29 18:25:08  chinkumo
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
package fr.soleil.bensikin.options.manager;

import fr.soleil.bensikin.options.Options;

/**
 * Defines save/load operations to a file representation of a Options.
 *
 * @author CLAISSE
 */
public interface IOptionsManager
{
	/**
	 * Saves options to the desired location.
	 *
	 * @param options                 The Options to save
	 * @param optionsResourceLocation The Options save location
	 * @throws Exception
	 */
	public void saveOptions(Options options , String optionsResourceLocation) throws Exception;

	/**
	 * Loads options from the desired location.
	 *
	 * @param optionsResourceLocation The Options load location
	 * @return The loaded Options
	 * @throws Exception
	 */
	public Options loadOptions(String optionsResourceLocation) throws Exception;
}
