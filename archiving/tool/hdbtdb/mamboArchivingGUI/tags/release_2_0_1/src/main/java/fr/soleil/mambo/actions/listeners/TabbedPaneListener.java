//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/listeners/TabbedPaneListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TabbedPaneListener.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: TabbedPaneListener.java,v $
// Revision 1.1  2005/12/15 09:21:07  ounsy
// First Commit including profile management
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.listeners;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPaneListener implements ChangeListener
{
    public TabbedPaneListener ()
    {

    }

    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged ( ChangeEvent change )
    {
        if ( change != null )
        {
            Object source = change.getSource();
            if ( source != null && source instanceof JTabbedPane )
            {
                ( ( JTabbedPane ) source ).repaint();
            }
        }
    }

}
