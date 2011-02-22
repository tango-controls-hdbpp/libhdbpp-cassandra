//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/listeners/ArchivingStartListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingStartListener.
//						(Claisse Laurent) - jan. 2006
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ArchivingStartListener.java,v $
// Revision 1.4  2006/05/19 13:45:13  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 12:49:41  ounsy
// modified imports
//
// Revision 1.2  2006/01/25 10:37:20  chinkumo
// ...
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import fr.soleil.mambo.actions.archiving.ArchivingStartAction;
import fr.soleil.mambo.tools.Messages;

/**
 * 
 * @author SOLEIL
 */
public class ArchivingStartListener extends MouseAdapter {

	private ArchivingStartAction archivingStartAction;
	private String name;

	public ArchivingStartListener() {
		super();
		name = Messages.getMessage("ARCHIVING_ACTION_START_BUTTON");
		archivingStartAction = ArchivingStartAction.getInstance(name);
	}

	public void mouseClicked(MouseEvent e) {
		if (isValidEvent(e)) {
			archivingStartAction.actionPerformed(new ActionEvent(new JButton(),
					ActionEvent.ACTION_PERFORMED, name));
		}
	}

	private boolean isValidEvent(MouseEvent evt) {
		if (evt.getClickCount() > 1) {
			return false;
		}
		return true;
	}
}
