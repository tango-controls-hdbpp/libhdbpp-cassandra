//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/detail/TreeBox.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TreeBox.
//						(Claisse Laurent) - 23 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: TreeBox.java,v $
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

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fr.soleil.bensikin.Bensikin;


/**
 * A Box container containing an attributes tree and a "Match" field and button.
 * Used twice in the application, once for Tango attributes (PossibleAttributesTree)
 * and once for the current context's attributes (ContextAttributesTree)
 *
 * @author CLAISSE
 */
public class TreeBox extends Box
{
	private JTextField regexpField;

	/**
	 * Inits an empty TreeBox
	 */
	public TreeBox()
	{
		super(BoxLayout.Y_AXIS);
	}

	/**
	 * Returns the contents of the pattern text field
	 *
	 * @return The contents of the pattern text field
	 */
	public String getRegExp()
	{
		return this.regexpField.getText();
	}

	/**
	 * Completely builds the content of this container.
	 *
	 * @param tree         The attributes tree to display
	 * @param _regexpField The attributes filtering pattern field
	 * @param searchButton The button used to launch the attributes filtering
	 */
	public void build(AttributesTree tree , JTextField _regexpField , JButton searchButton)
	{
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		boolean _hires = Bensikin.isHires();
		if ( _hires )
		{
			scrollPane.setMinimumSize(new Dimension(100 , 50));
			scrollPane.setPreferredSize(new Dimension(200 , 50));
			scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE , 500));
		}
		else
		{
			scrollPane.setPreferredSize(new Dimension(100 , 180));
			scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE , 180));
		}

		searchButton.setPreferredSize(new Dimension(70 , 20));

		Box patternBox = new Box(BoxLayout.X_AXIS);
		this.regexpField = _regexpField;
		patternBox.add(_regexpField);
		patternBox.add(Box.createHorizontalStrut(3));
		patternBox.add(searchButton);
		patternBox.setMaximumSize(new Dimension(270 , 25));

		this.add(scrollPane);
		this.add(Box.createVerticalStrut(5));
		this.add(patternBox);
	}
}
