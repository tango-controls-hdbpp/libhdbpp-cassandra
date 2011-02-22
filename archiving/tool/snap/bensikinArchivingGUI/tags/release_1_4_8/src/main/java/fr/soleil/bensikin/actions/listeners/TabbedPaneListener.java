//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/listeners/TabbedPaneListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TabbedPaneListener.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: TabbedPaneListener.java,v $
// Revision 1.1  2005/11/29 18:25:13  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.listeners;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPaneListener implements ChangeListener
{
	public TabbedPaneListener()
	{

	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent change)
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
