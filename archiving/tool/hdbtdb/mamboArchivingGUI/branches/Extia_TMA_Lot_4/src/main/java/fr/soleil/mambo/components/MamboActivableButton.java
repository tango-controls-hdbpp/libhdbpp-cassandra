//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/MamboActivableButton.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboActivableButton.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: MamboActivableButton.java,v $
// Revision 1.2  2006/01/23 09:00:40  ounsy
// Extended to Avoid multi click on "start" button
//
// Revision 1.1  2005/11/29 18:27:24  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components;

import java.awt.Insets;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A Button that updates its Icon when enabled / disabled
 */
public class MamboActivableButton extends JButton {

	private ImageIcon enabledIcon;
	private ImageIcon disabledIcon;

	/**
	 * Constructor
	 * 
	 * @param a
	 *            Action of the button
	 * @param enabled
	 *            the ImageIcon when button is enabled
	 * @param disabled
	 *            the ImageIcon when button is disabled
	 */
	public MamboActivableButton(Action a, ImageIcon enabled, ImageIcon disabled) {
		super(a);
		setMargin(new Insets(0, 0, 0, 0));
		enabledIcon = enabled;
		disabledIcon = disabled;
		setEnabled(a.isEnabled());
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 *            Text of the button
	 * @param m
	 *            MouseListener of the button
	 * @param enabled
	 *            the ImageIcon when button is enabled
	 * @param disabled
	 *            the ImageIcon when button is disabled
	 */
	public MamboActivableButton(String text, MouseListener m,
			ImageIcon enabled, ImageIcon disabled) {
		super(text);
		setToolTipText(text);
		setMargin(new Insets(0, 0, 0, 0));
		enabledIcon = enabled;
		disabledIcon = disabled;
		setEnabled(true);
		addMouseListener(m);
	}

	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		if (isEnabled) {
			setIcon(enabledIcon);
		} else {
			setIcon(disabledIcon);
		}
	}

}
