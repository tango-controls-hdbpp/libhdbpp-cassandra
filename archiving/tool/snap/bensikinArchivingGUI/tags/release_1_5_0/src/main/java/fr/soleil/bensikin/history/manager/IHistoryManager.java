//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/manager/IHistoryManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IHistoryManager.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: IHistoryManager.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:39  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.history.manager;

import fr.soleil.bensikin.history.History;

/**
 * Defines save/load operations to a file representation of a History
 *
 * @author CLAISSE
 */
public interface IHistoryManager
{
	/**
	 * Saves history to the desired location.
	 *
	 * @param history                 The History to save
	 * @param historyResourceLocation The History save location
	 * @throws Exception
	 */
	public void saveHistory(History history , String historyResourceLocation) throws Exception;

	/**
	 * Loads history from the desired location.
	 *
	 * @param historyResourceLocation The History load location
	 * @return The loaded History
	 * @throws Exception
	 */
	public History loadHistory(String historyResourceLocation) throws Exception;
}
