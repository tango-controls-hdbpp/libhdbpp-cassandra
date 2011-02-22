//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/OpenedVCItemListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  OpenedVCItemListener.
//						(Claisse Laurent) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: OpenedVCItemListener.java,v $
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
package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import fr.soleil.mambo.data.view.ViewConfiguration;


public class OpenedVCItemListener implements ItemListener
{
//  MULTI-CONF
    /**
     * 
     */
    public OpenedVCItemListener ()
    {

    }

    /* (non-Javadoc)
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged ( ItemEvent event )
    {
        ViewConfiguration selected = ( ViewConfiguration ) event.getItem();
        if ( selected == null )
        {
            return;
        }

        //OpenedVCComboBox source = (OpenedVCComboBox) event.getSource ();
        //ViewConfiguration vc = (ViewConfiguration) source.getSelectedItem ();
        //source.selectElement ( selected );

        selected.push();
    }

}
