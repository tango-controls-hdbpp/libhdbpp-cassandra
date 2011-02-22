//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/lifecycle/LifeCycleManager.java,v $
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
// Revision 1.4  2006/04/26 12:38:01  ounsy
// splash added
//
// Revision 1.3  2005/11/29 18:25:13  chinkumo
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
package fr.soleil.bensikin.lifecycle;

import java.util.Hashtable;
import fr.esrf.tangoatk.widget.util.Splash;

/**
 * An important interface that controls the application's startup/shutdown cycle.
 *
 * @author CLAISSE
 */
public interface LifeCycleManager
{
	/**
	 * Called before the GUI graphics containers are instantiated.
	 * Can be used to load :
	 * <UL>
	 * <LI> the application's state from the history file
	 * <LI> the application's options
	 * </UL>
	 * And :
	 * <UL>
	 * <LI> to initialize the application's ressources
	 * <LI> to instantiate static implementations of various managers
	 * <LI> etc..
	 * </UL>
	 *
	 * @param startParameters Can contain parameters if necessary
	 */
	public void applicationWillStart(Hashtable startParameters, Splash splash);

	/**
	 * Called just after the GUI graphics containers are instantiated.
	 * Must be used for operations that need the containers to already exist:
	 * <UL>
	 * <LI> pushing the pre loaded history and options to the display components
	 * <LI> setting the window size
	 * <LI> etc..
	 * <UL>
	 *
	 * @param startParameters Can contain parameters if necessary
	 */
	public void applicationStarted(Hashtable startParameters);

	/**
	 * Called when the application detects a shutdown request, be it through the close icon or through the menu's Exit option.
	 * Can be used to:
	 * <UL>
	 * <LI> save everything that has to be saved
	 * <LI> close resources
	 * </UL>
	 * And finally, that's where the application's shutdown must be done.
	 *
	 * @param endParameters Can contain parameters if necessary
	 */
	public void applicationClosed(Hashtable endParameters);

	/**
	 * Sets whether the application must save/load history
	 *
	 * @param b Whether the application must save/load history
	 */
	public void setHasHistorySave(boolean b);

	/**
	 * Gets whether the application must save/load history
	 *
	 * @return Whether the application must save/load history
	 */
	public boolean hasHistorySave();

}
