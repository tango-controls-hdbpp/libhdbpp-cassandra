//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/lifecycle/LifeCycleManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LifeCycleManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: LifeCycleManager.java,v $
// Revision 1.4  2006/08/31 13:22:00  ounsy
// small exit bug correction
//
// Revision 1.3  2006/04/26 11:53:01  ounsy
// splash added
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.lifecycle;

import java.util.Hashtable;

import fr.esrf.tangoatk.widget.util.Splash;

public interface LifeCycleManager {
	/**
	 * @param startParameters
	 *            8 juil. 2005
	 */
	public void applicationWillStart(Hashtable startParameters, Splash splash);

	/**
	 * @param startParameters
	 *            8 juil. 2005
	 */
	public void applicationStarted(Hashtable startParameters);

	/**
	 * @param endParameters
	 *            8 juil. 2005
	 */
	public void applicationClosed(Hashtable endParameters);

	/**
	 * @param endParameters
	 *            8 juil. 2005
	 */
	public int applicationWillClose(Hashtable endParameters);

	/**
	 * @param b
	 *            8 juil. 2005
	 */
	public void setHasHistorySave(boolean b);

	/**
	 * @return 8 juil. 2005
	 */
	public boolean hasHistorySave();

}
