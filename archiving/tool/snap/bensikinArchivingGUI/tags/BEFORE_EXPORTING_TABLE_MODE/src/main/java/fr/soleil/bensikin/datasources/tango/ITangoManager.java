//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/datasources/tango/ITangoManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ITangoManager.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: ITangoManager.java,v $
// Revision 1.1  2005/12/14 16:56:05  ounsy
// has been renamed
//
// Revision 1.1.1.2  2005/08/22 11:58:39  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.datasources.tango;

import java.util.ArrayList;
import java.util.Vector;

import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;

/**
 * Defines operations on Tango attributes.
 * Currently used only for searching those attributes.
 *
 * @author CLAISSE
 */
public interface ITangoManager
{
    /**
     * Loads Tango attributes into a Vector containing Domain objects.
     *
     * @param searchCriterions The search criterions
     * @return A Vector containing Domain objects
     */
    public Vector loadDomains ( Criterions searchCriterions );

    /**
     * Loads the Tango attributes of a given device.
     *
     * @param device_name The complete name of the device to load attributes for
     * @return An ArrayList containing the names of the device's attributes
     */
    public ArrayList dbGetAttributeList ( String device_name );
}
