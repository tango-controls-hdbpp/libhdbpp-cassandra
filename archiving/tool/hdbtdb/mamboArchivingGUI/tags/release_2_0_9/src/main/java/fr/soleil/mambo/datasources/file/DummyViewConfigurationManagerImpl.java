//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/DummyViewConfigurationManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyViewConfigurationManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: DummyViewConfigurationManagerImpl.java,v $
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

import java.util.Hashtable;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.data.view.ViewConfiguration;

public class DummyViewConfigurationManagerImpl implements IViewConfigurationManager
{

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#saveViewConfiguration(mambo.data.view.ViewConfiguration, java.lang.String, java.util.Hashtable)
     */
    public void saveViewConfiguration ( ViewConfiguration ac , Hashtable saveParameters ) throws Exception
    {
        // TODO Auto-generated method stub

    }

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
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#startUp(java.lang.Object)
     */
    public void startUp ( /*Object resource*/ ) throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#shutDown()
     */
    public void shutDown () throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#loadViewConfiguration(java.lang.String)
     */
    public ViewConfiguration loadViewConfiguration ( /*String location*/ ) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#getDefaultSaveLocation()
     */
    public String getDefaultSaveLocation ()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#getSaveLocation()
     */
    public String getNonDefaultSaveLocation ()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#setSaveLocation(java.lang.String)
     */
    public void setNonDefaultSaveLocation ( String location )
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see mambo.datasources.file.IViewConfigurationManager#getSaveLocation()
     */
    public String getSaveLocation ()
    {
        // TODO Auto-generated method stub
        return null;
    }


}
