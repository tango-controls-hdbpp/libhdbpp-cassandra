//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/listeners/ContextTextListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextTextListener.
//						(GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ContextTextListener.java,v $
// Revision 1.2  2007/08/24 14:05:46  ounsy
// bug correction with context printing as text
//
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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import fr.soleil.bensikin.actions.context.RegisterContextAction;
import fr.soleil.bensikin.containers.context.ContextActionPanel;


public class ContextTextListener implements DocumentListener
{

	public ContextTextListener()
	{
		// nothing, this is just here to have a constructor
	}

	private void textChanged(DocumentEvent arg0)
	{
		if ( arg0 == null )
		{
			return;
		}
		Document doc = arg0.getDocument();
		if ( doc != null && doc.getLength() > 0 )
		{
			try
			{
				if ( !"".equals(doc.getText(0 , doc.getLength()).trim()) )
				{
					RegisterContextAction register = RegisterContextAction.getInstance();
					if ( register != null )
					{
						register.setEnabled(true);
					}
                    ContextActionPanel.getInstance().allowPrint(false);
				}
			}
			catch ( BadLocationException e )
			{
				// trace here but should never happen.
				e.printStackTrace();
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	public void changedUpdate(DocumentEvent arg0)
	{
		//textChanged(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	public void insertUpdate(DocumentEvent arg0)
	{
		textChanged(arg0);
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	public void removeUpdate(DocumentEvent arg0)
	{
		textChanged(arg0);
	}

}
