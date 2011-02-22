//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/XMLViewConfigurationManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  XMLViewConfigurationManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: XMLViewConfigurationManagerImpl.java,v $
// Revision 1.4  2007/02/01 14:15:38  pierrejoseph
// XmlHelper reorg
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
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
package fr.soleil.mambo.datasources.file;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Hashtable;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;
import fr.soleil.mambo.tools.xmlhelpers.vc.ViewConfigurationXMLHelper;

public class XMLViewConfigurationManagerImpl implements IViewConfigurationManager
{
    private String resourceLocation;
    //private static final String DEFAULT_FILE = "default.vc";

    private String defaultSaveLocation;
    private String saveLocation;
    private PrintWriter writer;

    protected XMLViewConfigurationManagerImpl ()
    {
        startUp();
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#saveViewConfiguration(mambo.data.view.ViewConfiguration, java.lang.String, java.util.Hashtable)
     */
    public void saveViewConfiguration ( ViewConfiguration vc , Hashtable saveParameters ) throws Exception
    {
        String _location = this.getSaveLocation();
        //System.out.println ( "XMLViewConfigurationManagerImpl/saveViewConfiguration/_location/"+_location+"/" );

        writer = new PrintWriter( new FileWriter( _location , false ) );
        GUIUtilities.write2( writer , XMLUtils.XML_HEADER , true );
        GUIUtilities.write2( writer , vc.toString() , true );
        writer.close();
    }

    /**
     * @return 26 juil. 2005
     * @throws FileNotFoundException
     */
    /*private boolean fileIsEmpty ()
    {
        try
        {
            BufferedReader br = new BufferedReader( new FileReader( defaultSaveLocation ) );
            String s = br.readLine();
            if ( s != null && !s.trim().equals( "" ) )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return false;
    }*/

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#loadViewConfigurations(fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions)
     */
    public ViewConfiguration[] loadViewConfigurations ( Criterions searchCriterions ) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#findViewConfigurations(mambo.data.view.ViewConfiguration[], fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions)
     */
    public ViewConfiguration[] findViewConfigurations ( ViewConfiguration[] in , Criterions searchCriterions ) throws Exception
    {

        return null;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#startUp(java.lang.Object)
     */
    public void startUp () throws IllegalStateException
    {
        resourceLocation = null;
        boolean illegal = false;

        String vcPath = Mambo.getPathToResources() + "/vc";

        File f = null;
        try
        {
            f = new File( vcPath );
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
        //resourceLocation = vcPath + "/" + DEFAULT_FILE;
        resourceLocation = vcPath;
        defaultSaveLocation = resourceLocation;

    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#shutDown()
     */
    public void shutDown () throws Exception
    {


    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#loadViewConfiguration(java.lang.String)
     */
    public ViewConfiguration loadViewConfiguration () throws Exception
    {
        String _location = this.getSaveLocation();
        ViewConfiguration vc = ViewConfigurationXMLHelper.loadViewConfigurationIntoHash( _location );

        return vc;
    }

    /**
     * @return Returns the defaultSaveLocation.
     */
    public String getDefaultSaveLocation ()
    {
        return defaultSaveLocation;
    }

    /**
     * @param defaultSaveLocation The defaultSaveLocation to set.
     */
    public void setDefaultSaveLocation ( String defaultSaveLocation )
    {
        this.defaultSaveLocation = defaultSaveLocation;
    }


    /**
     * @return Returns the saveLocation.
     */
    public String getNonDefaultSaveLocation ()
    {
        return saveLocation;
    }

    /**
     * @param saveLocation The saveLocation to set.
     */
    public void setNonDefaultSaveLocation ( String saveLocation )
    {
        this.saveLocation = saveLocation;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#getSaveLocation()
     */
    public String getSaveLocation ()
    {
        String _location = this.saveLocation == null ? this.defaultSaveLocation : this.saveLocation;
        return _location;
    }

}
