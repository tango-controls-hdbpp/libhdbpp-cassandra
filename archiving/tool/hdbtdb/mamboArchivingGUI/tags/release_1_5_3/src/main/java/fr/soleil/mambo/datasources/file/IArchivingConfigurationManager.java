//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/file/IArchivingConfigurationManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IArchivingConfigurationManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: IArchivingConfigurationManager.java,v $
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


public interface IArchivingConfigurationManager
{
    public void startUp () throws Exception;

    public void shutDown () throws Exception;

    public void saveArchivingConfiguration ( ArchivingConfiguration ac , Hashtable saveParameters ) throws Exception;

    public ArchivingConfiguration loadArchivingConfiguration () throws Exception;

    public ArchivingConfiguration[] loadArchivingConfigurations ( Criterions searchCriterions ) throws Exception;

    public ArchivingConfiguration[] findArchivingConfigurations ( ArchivingConfiguration[] in , Criterions searchCriterions ) throws Exception;

    public String getDefaultSaveLocation ();

    public String getNonDefaultSaveLocation ();

    public void setNonDefaultSaveLocation ( String location );

    public String getSaveLocation ();
}
