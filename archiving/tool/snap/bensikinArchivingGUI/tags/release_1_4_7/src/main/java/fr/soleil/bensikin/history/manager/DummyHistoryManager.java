//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/manager/DummyHistoryManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyHistoryManager.
//						(Claisse Laurent) - 8 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: DummyHistoryManager.java,v $
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
 * A dummy implementation, does nothing.
 *
 * @author CLAISSE
 */
public class DummyHistoryManager implements IHistoryManager
{

	/* (non-Javadoc)
	 * @see bensikin.bensikin.history.manager.IHistoryManager#saveHistory(bensikin.bensikin.history.History, java.lang.String)
	 */
	public void saveHistory(History history , String historyResourceLocation)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.history.manager.IHistoryManager#loadHistory(java.lang.String)
	 */
	public History loadHistory(String historyResourceLocation)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
