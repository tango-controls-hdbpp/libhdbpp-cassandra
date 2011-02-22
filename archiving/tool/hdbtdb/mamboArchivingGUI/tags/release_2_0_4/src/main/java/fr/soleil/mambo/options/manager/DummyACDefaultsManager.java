//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/manager/DummyACDefaultsManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyACDefaultsManager.
//						(Claisse Laurent) - oct. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.2 $
//
// $Log: DummyACDefaultsManager.java,v $
// Revision 1.2  2007/02/01 14:14:33  pierrejoseph
// Export Period is sometimes forced to 30 min.
// XmlHelper reorg
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.options.manager;

import fr.soleil.mambo.options.sub.ACOptions;

public class DummyACDefaultsManager implements IACDefaultsManager
{

    public DummyACDefaultsManager ()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see mambo.options.manager.IACDefaultsManager#saveACDefaults(mambo.options.sub.ACOptions, java.lang.String)
     */
    public void saveACDefaults ( ACOptions options )
            throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see mambo.options.manager.IACDefaultsManager#loadACDefaults(java.lang.String)
     */
    public ACOptions loadACDefaults ()
            throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see mambo.options.manager.IACDefaultsManager#setSaveLocation(java.lang.String)
     */
    public void setSaveLocation ( String location )
    {
        // TODO Auto-generated method stub

    }


    /* (non-Javadoc)
     * @see mambo.options.manager.IACDefaultsManager#setDefault(boolean)
     */
    public void setDefault ( boolean _default )
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see mambo.options.manager.IACDefaultsManager#getDefaultSaveLocation()
     */
    public String getDefaultSaveLocation ()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getACDefaultsPropertyValue(String key)
    {
        // TODO Auto-generated method stub
        return null;
    }   
}
