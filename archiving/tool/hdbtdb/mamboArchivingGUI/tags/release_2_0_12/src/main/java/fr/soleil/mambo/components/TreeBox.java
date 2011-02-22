//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/TreeBox.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TreeBox.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: TreeBox.java,v $
// Revision 1.3  2006/10/02 14:13:25  ounsy
// minor changes (look and feel)
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.mambo.tools.SpringUtilities;

public class TreeBox extends JPanel {
	private JTextField regexpField;
	private JScrollPane scrollPane;
	private Box patternBox;

	/**
     * 
     */
	public TreeBox() {
		super();
	}

	public String getRegExp() {
		return this.regexpField.getText();
	}

	/**
	 * @param tree
	 * @param leftRegexpField
	 * @param leftSearchButton
	 *            8 juil. 2005
	 */
	public void build(AttributesTree tree, JTextField _regexpField,
			JButton searchButton) {
		this.initComponents(tree, _regexpField, searchButton);
		this.addComponents();
		this.initLayout();
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		this.add(scrollPane);
		this.add(Box.createVerticalStrut(5));
		this.add(patternBox);
	}

	private void initComponents(AttributesTree tree, JTextField _regexpField,
			JButton searchButton) {
		scrollPane = new JScrollPane(tree);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setMinimumSize(new Dimension(100, 50));
		scrollPane.setPreferredSize(new Dimension(200, 50));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

		// searchButton.setPreferredSize ( new Dimension ( 63 , 20 ) );
		searchButton.setPreferredSize(new Dimension(69, 20));

		patternBox = new Box(BoxLayout.X_AXIS);
		this.regexpField = _regexpField;
		patternBox.add(_regexpField);
		patternBox.add(Box.createHorizontalStrut(3));
		patternBox.add(searchButton);
		patternBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
	}

	private void initLayout() {
		this.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, 0,
				0, 0, 0, true);
	}

	/**
	 * 19 juil. 2005
	 */
	/*
	 * before size tweaks private void initComponents( AttributesTree tree ,
	 * JTextField _regexpField , JButton searchButton ) { scrollPane = new
	 * JScrollPane ( tree ); scrollPane.setVerticalScrollBarPolicy (
	 * JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
	 * scrollPane.setHorizontalScrollBarPolicy (
	 * JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
	 * scrollPane.setMinimumSize(new Dimension ( 100 , 50 ) );
	 * scrollPane.setPreferredSize(new Dimension ( 200 , 50 ) );
	 * scrollPane.setMaximumSize(new Dimension ( Integer.MAX_VALUE , 500 ) );
	 * 
	 * searchButton.setPreferredSize ( new Dimension ( 63 , 20 ) );
	 * 
	 * patternBox = new Box ( BoxLayout.X_AXIS ); this.regexpField =
	 * _regexpField; patternBox.add ( _regexpField ); patternBox.add (
	 * Box.createHorizontalStrut ( 3 ) ); patternBox.add ( searchButton );
	 * patternBox.setMaximumSize(new Dimension (270 , 25 ) ); }
	 */

}
