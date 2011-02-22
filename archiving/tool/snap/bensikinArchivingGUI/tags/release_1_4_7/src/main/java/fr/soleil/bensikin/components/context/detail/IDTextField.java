//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/detail/IDTextField.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  IDTextField.
//						(Claisse Laurent) - 7 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: IDTextField.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components.context.detail;

import javax.swing.JTextField;

import fr.soleil.bensikin.actions.listeners.SelectedContextListener;


/**
 * Overloads the setText method if JTextField so that it can intercept any change in the content.
 * Fires a PropertyChangeEvent when it happens (key SelectedContextListener.ID_TEXT_PROPERTY)
 *
 * @author CLAISSE
 */
public class IDTextField extends JTextField
{
	/* (non-Javadoc)
	 * @see javax.swing.text.JTextComponent#setText(java.lang.String)
	 */
	public void setText(String text)
	{
		super.setText(text);
		boolean b = true;
		firePropertyChange(SelectedContextListener.ID_TEXT_PROPERTY , b , !b);
	}
}
