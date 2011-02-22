//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/options/manager/IACDefaultsManager.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  IOptionsManager.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: pierrejoseph $
//
//$Revision: 1.2 $
//
//$Log: IACDefaultsManager.java,v $
//Revision 1.2  2007/02/01 14:14:33  pierrejoseph
//Export Period is sometimes forced to 30 min.
//XmlHelper reorg
//
//Revision 1.1  2005/11/29 18:27:07  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.options.manager;

import fr.soleil.mambo.options.sub.ACOptions;


public interface IACDefaultsManager
{
	public static final long DEFAULT_TDB_EXPORT_PERIOD_WHEN_BAD_VALUE = 3600000;
    /**
     * @param options
     * @param optionsResourceLocation
     * @throws Exception 8 juil. 2005
     */
    public void saveACDefaults ( ACOptions options ) throws Exception;

    /**
     * @param optionsResourceLocation
     * @return
     * @throws Exception 8 juil. 2005
     */
    public ACOptions loadACDefaults () throws Exception;

    public void setSaveLocation ( String location );

    public void setDefault ( boolean _default );

    /**
     * @return
     */
    public String getDefaultSaveLocation ();
    
    /** Allow to retrieve a particular default property value
     * @param key
     * @return value
     */
    public String getACDefaultsPropertyValue(String key);
}
