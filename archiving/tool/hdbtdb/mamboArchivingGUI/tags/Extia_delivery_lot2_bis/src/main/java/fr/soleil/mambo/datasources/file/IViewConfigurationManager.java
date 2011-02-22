//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/IViewConfigurationManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IViewConfigurationManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: IViewConfigurationManager.java,v $
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

public interface IViewConfigurationManager {
	public void startUp() throws Exception;

	public void shutDown() throws Exception;

	public void saveViewConfiguration(ViewConfiguration ac,
			Hashtable saveParameters) throws Exception;

	public ViewConfiguration loadViewConfiguration() throws Exception;

	public ViewConfiguration[] loadViewConfigurations(
			Criterions searchCriterions) throws Exception;

	public ViewConfiguration[] findViewConfigurations(ViewConfiguration[] in,
			Criterions searchCriterions) throws Exception;

	public String getDefaultSaveLocation();

	public String getNonDefaultSaveLocation();

	public void setNonDefaultSaveLocation(String location);

	public String getSaveLocation();
}
