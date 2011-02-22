//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/ValidateOptionsAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ValidateOptionsAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ValidateOptionsAction.java,v $
// Revision 1.3  2006/10/13 12:45:54  ounsy
// OptionsDialog moved to mambo.containers.sub.dialogs.options instead of mambo.containers.sub.dialogs
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.sub.dialogs.options.OptionsDialog;
import fr.soleil.mambo.options.Options;



public class ValidateOptionsAction extends AbstractAction
{
    /**
     * @param name
     */
    public ValidateOptionsAction ( String name )
    {
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
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

        menuDialog.setVisible( false );
    }

}
