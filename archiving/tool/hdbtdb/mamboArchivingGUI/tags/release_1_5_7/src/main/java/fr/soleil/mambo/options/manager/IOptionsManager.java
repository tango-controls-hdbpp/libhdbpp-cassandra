//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/manager/IOptionsManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IOptionsManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: IOptionsManager.java,v $
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


public interface IOptionsManager
{
    /**
     * @param options
     * @param optionsResourceLocation
     * @throws Exception 8 juil. 2005
     */
    public void saveOptions ( Options options , String optionsResourceLocation ) throws Exception;

    /**
     * @param optionsResourceLocation
     * @return
     * @throws Exception 8 juil. 2005
     */
    public Options loadOptions ( String optionsResourceLocation ) throws Exception;
}
