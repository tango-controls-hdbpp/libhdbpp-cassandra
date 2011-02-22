//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/ResetAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ResetAction.
//						(Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: ResetAction.java,v $
// Revision 1.9  2007/08/24 14:04:53  ounsy
// bug correction with context printing as text
//
// Revision 1.8  2007/08/24 12:52:56  ounsy
// minor changes
//
// Revision 1.7  2006/04/10 08:46:41  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.6  2005/12/14 15:58:42  ounsy
// modified the reset call to Snapshot.reset ( true ); instead of Snapshot.reset ();
// to wipe out everything snapshot-related
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
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
import javax.swing.Icon;

import fr.soleil.bensikin.components.BensikinMenuBar;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.snapshot.Snapshot;


/**
 * An action that resets the display, removing currently opened and selected contexts and snapshots
 *
 * @author CLAISSE
 */
public class ResetAction extends BensikinAction
{
    /**
     * Standard action constructor that sets the action's name and icon.
     *
     * @param name The action name
     * @param icon The action icon
     */
    public ResetAction ( String name , Icon icon )
    {
        putValue( Action.NAME , name );
        putValue( Action.SMALL_ICON , icon );
        putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        Context.reset();
        Snapshot.reset ( true, true );
        BensikinMenuBar.getInstance().resetRegisterItem();
        ContextActionPanel.getInstance().resetRegisterButton();
        ContextActionPanel.getInstance().allowPrint(false);
    }

}
