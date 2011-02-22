//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/ACRecapAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACRecapAction.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ACRecapAction.java,v $
// Revision 1.3  2006/10/25 08:00:46  ounsy
// replaced calls to show() by calls to setVisible(true)
//
// Revision 1.2  2005/12/15 10:41:45  ounsy
// attributes informations refreshment
//
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
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.archiving.dialogs.ACRecapDialog;


public class ACRecapAction extends AbstractAction
{

    /**
     * @param name the name to set to this action
     */
    public ACRecapAction ( String name )
    {
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        ACRecapDialog.getInstance(true).setVisible(true);
    }

}
