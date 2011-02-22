//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboBooleanViewer.
//						(GIRARDOT Raphael) - nov. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.2  2006/12/06 12:35:43  ounsy
// minor changes
//
// Revision 1.1  2006/04/05 13:43:47  ounsy
// boolean support
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
package fr.soleil.mambo.components;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.tools.Messages;

/**
 * A JCheckbox that looks like a couple of LEDs
 * 
 * @author SOLEIL
 */
public class MamboBooleanViewer extends JCheckBox {
	protected final static ImageIcon trueIcon = new ImageIcon(Mambo.class
			.getResource("icons/true.gif"));
	protected final static ImageIcon falseIcon = new ImageIcon(Mambo.class
			.getResource("icons/false.gif"));
	protected final static ImageIcon offIcon = new ImageIcon(Mambo.class
			.getResource("icons/double_off.gif"));

	/**
	 * Constructor
	 * 
	 * @param value
	 *            a Boolean to select/unselect the checkbox
	 */
	public MamboBooleanViewer(Boolean value) {
		super((value == null) ? Messages.getMessage("VIEW_ATTRIBUTES_NO_DATA")
				: value.toString());
		setIcon(falseIcon);
		setPressedIcon(offIcon);
		setSelectedIcon(trueIcon);
		setDisabledIcon(offIcon);
		setSelected(value);
		setOpaque(false);
		repaint();
	}

	protected void setSelected(Boolean value) {
		if (value == null) {
			setSelected(false);
			setEnabled(false);
		} else {
			setIcon(falseIcon);
			setSelected(value.booleanValue());
		}
	}

}
