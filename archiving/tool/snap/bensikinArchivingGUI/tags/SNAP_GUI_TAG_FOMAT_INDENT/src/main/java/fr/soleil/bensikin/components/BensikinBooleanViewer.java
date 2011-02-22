//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/BensikinBooleanViewer.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BensikinBooleanViewer.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: BensikinBooleanViewer.java,v $
// Revision 1.3  2006/04/13 12:37:33  ounsy
// new spectrum types support
//
// Revision 1.2  2006/03/15 15:07:00  ounsy
// adapted to receive no data
//
// Revision 1.1  2005/11/29 18:25:08  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.tools.Messages;

/**
 * A JCheckbox that looks like a couple of LEDs
 * 
 * @author SOLEIL
 */
public class BensikinBooleanViewer extends JCheckBox {
	private final static ImageIcon trueIcon = new ImageIcon(Bensikin.class
			.getResource("icons/true.gif"));
	private final static ImageIcon falseIcon = new ImageIcon(Bensikin.class
			.getResource("icons/false.gif"));
	private final static ImageIcon offIcon = new ImageIcon(Bensikin.class
			.getResource("icons/double_off.gif"));

	/**
	 * Constructor
	 * 
	 * @param value
	 *            a Boolean to select/unselect the checkbox
	 */
	public BensikinBooleanViewer(Boolean value) {
		super((value == null) ? Messages.getMessage("SNAPSHOT_DETAIL_NO_DATA")
				: value.toString());
		setIcon(falseIcon);
		setPressedIcon(offIcon);
		setSelectedIcon(trueIcon);
		setSelected(value);
		repaint();
	}

	public void setSelected(Boolean value) {
		if (value == null) {
			setSelected(false);
			setIcon(offIcon);
			repaint();
		} else {
			setIcon(falseIcon);
			setSelected(value.booleanValue());
			repaint();
		}
	}

}
