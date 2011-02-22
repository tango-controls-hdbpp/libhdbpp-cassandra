//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/ValidateOptionsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OptionsAction.
//						(Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ValidateOptionsAction.java,v $
// Revision 1.2  2006/04/10 08:46:41  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.1  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsDialog;
import fr.soleil.bensikin.options.Options;


/**
 * An action called when the user validates the options popup, modifying the applications properties.
 *
 * @author CLAISSE
 */
public class ValidateOptionsAction extends BensikinAction
{
	/**
	 * Standard action constructor that sets the action's name.
	 *
	 * @param name The action name
	 */
	public ValidateOptionsAction(String name)
	{
		this.putValue(Action.NAME , name);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent actionEvent)
	{
		OptionsDialog menuDialog = OptionsDialog.getInstance();

		//loading the options in the current Options instance before closing
		Options options = Options.getInstance();
		options.fillFromOptionsDialog();
		try
		{
			options.push();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

		menuDialog.setVisible(false);
	}

}
