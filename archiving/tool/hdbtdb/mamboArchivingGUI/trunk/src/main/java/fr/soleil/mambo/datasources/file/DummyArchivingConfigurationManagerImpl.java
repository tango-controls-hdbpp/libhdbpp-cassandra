//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/DummyArchivingConfigurationManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyArchivingConfigurationManagerImpl.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: DummyArchivingConfigurationManagerImpl.java,v $
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
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

public class DummyArchivingConfigurationManagerImpl implements
		IArchivingConfigurationManager {

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * saveArchivingConfiguration(mambo.data.archiving.ArchivingConfiguration,
	 * java.lang.String, java.util.Hashtable)
	 */
	public void saveArchivingConfiguration(ArchivingConfiguration ac,
			Hashtable saveParameters) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * loadArchivingConfigurations
	 * (fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions)
	 */
	public ArchivingConfiguration[] loadArchivingConfigurations(
			Criterions searchCriterions) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * findArchivingConfigurations
	 * (mambo.data.archiving.ArchivingConfiguration[],
	 * fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions)
	 */
	public ArchivingConfiguration[] findArchivingConfigurations(
			ArchivingConfiguration[] in, Criterions searchCriterions)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mambo.datasources.file.IArchivingConfigurationManager#startUp(java.lang
	 * .Object)
	 */
	public void startUp( /* Object resource */) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mambo.datasources.file.IArchivingConfigurationManager#shutDown()
	 */
	public void shutDown() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seemambo.datasources.file.IArchivingConfigurationManager#
	 * loadArchivingConfiguration(java.lang.String)
	 */
	public ArchivingConfiguration loadArchivingConfiguration( /* String location */)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mambo.datasources.file.IArchivingConfigurationManager#getDefaultSaveLocation
	 * ()
	 */
	public String getDefaultSaveLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mambo.datasources.file.IArchivingConfigurationManager#getSaveLocation()
	 */
	public String getNonDefaultSaveLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mambo.datasources.file.IArchivingConfigurationManager#setSaveLocation
	 * (java.lang.String)
	 */
	public void setNonDefaultSaveLocation(String location) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mambo.datasources.file.IArchivingConfigurationManager#getSaveLocation()
	 */
	public String getSaveLocation() {
		// TODO Auto-generated method stub
		return null;
	}

}
