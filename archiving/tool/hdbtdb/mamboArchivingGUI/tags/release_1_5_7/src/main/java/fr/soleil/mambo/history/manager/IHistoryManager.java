//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/history/manager/IHistoryManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IHistoryManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: IHistoryManager.java,v $
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


public interface IHistoryManager
{
    /**
     * @param history
     * @param historyResourceLocation
     * @throws Exception 8 juil. 2005
     */
    public void saveHistory ( History history , String historyResourceLocation ) throws Exception;

    /**
     * @param historyResourceLocation
     * @return
     * @throws Exception 8 juil. 2005
     */
    public History loadHistory ( String historyResourceLocation ) throws Exception;
}
