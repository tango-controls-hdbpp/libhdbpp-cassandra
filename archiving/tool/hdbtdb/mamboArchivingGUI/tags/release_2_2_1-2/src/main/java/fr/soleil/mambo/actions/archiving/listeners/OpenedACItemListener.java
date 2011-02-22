//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/listeners/OpenedACItemListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenedACItemListener.
//						(Claisse Laurent) - nov. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: OpenedACItemListener.java,v $
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
package fr.soleil.mambo.actions.archiving.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import fr.soleil.mambo.data.archiving.ArchivingConfiguration;

/**
 * 
 *  
 */
public class OpenedACItemListener implements ItemListener {
	// MULTI-CONF
	/**
     * 
     */
	public OpenedACItemListener() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent event) {
		ArchivingConfiguration selected = (ArchivingConfiguration) event
				.getItem();
		if (selected == null) {
			return;
		}

		selected.push();
	}

}
