//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/manager/DummyOptionsManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyOptionsManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: DummyOptionsManager.java,v $
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
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
package fr.soleil.mambo.options.manager;

import fr.soleil.mambo.options.Options;

public class DummyOptionsManager implements IOptionsManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.options.manager.IOptionsManager#saveOptions(bensikin
	 * .bensikin.options.Options, java.lang.String)
	 */
	public void saveOptions(Options options, String optionsResourceLocation)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.options.manager.IOptionsManager#loadOptions(java.lang
	 * .String)
	 */
	public Options loadOptions(String optionsResourceLocation) throws Exception {
		// TODO Auto-generated method stub
		// Options ret =
		Options.getInstance();

		return null;
	}

}
