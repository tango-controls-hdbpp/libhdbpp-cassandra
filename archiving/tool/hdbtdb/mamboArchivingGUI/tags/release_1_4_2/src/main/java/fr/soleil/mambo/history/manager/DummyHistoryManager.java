//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/history/manager/DummyHistoryManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyHistoryManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: DummyHistoryManager.java,v $
// Revision 1.2  2005/11/29 18:27:45  chinkumo
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
package fr.soleil.mambo.history.manager;

import fr.soleil.mambo.history.History;

public class DummyHistoryManager implements IHistoryManager
{

    /* (non-Javadoc)
     * @see bensikin.bensikin.history.manager.IHistoryManager#saveHistory(bensikin.bensikin.history.History, java.lang.String)
     */
    public void saveHistory ( History history , String historyResourceLocation )
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.history.manager.IHistoryManager#loadHistory(java.lang.String)
     */
    public History loadHistory ( String historyResourceLocation )
    {
        // TODO Auto-generated method stub
        return null;
    }

}
