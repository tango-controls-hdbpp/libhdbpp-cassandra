//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/manager/PropertiesACDefaultsManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PropertiesACDefaultsManager.
//						(Claisse Laurent) - oct. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.3 $
//
// $Log: PropertiesACDefaultsManager.java,v $
// Revision 1.3  2007/02/01 14:14:33  pierrejoseph
// Export Period is sometimes forced to 30 min.
// XmlHelper reorg
//
// Revision 1.2  2006/05/19 15:05:29  ounsy
// minor changes
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.GUIUtilities;


public class PropertiesACDefaultsManager implements IACDefaultsManager
{
    //private static final String DEFAULT_FILE = "default.acd";

    private String defaultSaveLocation;
    private String saveLocation;
    private PrintWriter writer;
    private boolean isDefault;

    private ResourceBundle defaultBundle;

    /**
     * 
     */
    public PropertiesACDefaultsManager ()
    {
        startUp();
    }

    public void startUp () throws IllegalStateException
    {
        String resourceLocation = null;
        boolean illegal = false;

        String acPath = Mambo.getPathToResources() + "/acd";

        File f = null;
        try
        {
            f = new File( acPath );
            if ( !f.canWrite() )
            {
                illegal = true;
            }
        }
        catch ( Exception e )
        {
            illegal = true;
        }

        if ( illegal )
        {
            //boolean b = 
            f.mkdir();
        }
        //resourceLocation = acPath + "/" + DEFAULT_FILE;
        resourceLocation = acPath;
        defaultSaveLocation = resourceLocation;

        defaultBundle = ResourceBundle.getBundle( "fr.soleil.mambo.resources.defaults.ac" );
    }

    /* (non-Javadoc)
     * @see mambo.options.manager.IACDefaultsManager#saveACDefaults(mambo.options.sub.ACOptions, java.lang.String)
     */
    public void saveACDefaults ( ACOptions options ) throws Exception
    {
        String _location = saveLocation == null ? this.defaultSaveLocation : this.saveLocation;

        writer = new PrintWriter( new FileWriter( _location , false ) );
        GUIUtilities.write2( writer , options.toPropertiesString() , true );
        writer.close();
    }

    /* (non-Javadoc)
     * @see mambo.options.manager.IACDefaultsManager#loadACDefaults(java.lang.String)
     */
    public ACOptions loadACDefaults () throws Exception
    {
        ACOptions ret = new ACOptions();
        Properties prop = new Properties();

        if ( !this.isDefault )
        {
            String _location = saveLocation == null ? this.defaultSaveLocation : this.saveLocation;
            FileInputStream inStream = new FileInputStream( _location );

            prop.load( inStream );
        }
        else
        {
            Enumeration keys = defaultBundle.getKeys();
            while ( keys.hasMoreElements() )
            {
                String nextKey = ( String ) keys.nextElement();
                String nextVal = ( String ) defaultBundle.getString( nextKey );

                prop.put( nextKey , nextVal );
            }
        }

        ret.setContent( prop );
        return ret;
    }

    /**
     * @param saveLocation The saveLocation to set.
     */
    public void setSaveLocation ( String saveLocation )
    {
        this.saveLocation = saveLocation;
    }

    /**
     * @param isDefault The isDefault to set.
     */
    public void setDefault ( boolean isDefault )
    {
        this.isDefault = isDefault;
    }
    
    public String getACDefaultsPropertyValue(String key)
    {
        return defaultBundle.getString( key );
    }   

    /**
     * @return Returns the defaultSaveLocation.
     */
    public String getDefaultSaveLocation ()
    {
        return defaultSaveLocation;
    }
}
