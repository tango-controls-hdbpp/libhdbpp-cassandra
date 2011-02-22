//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ArchivingAttributesTreePanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingAttributesTreePanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ArchivingAttributesTreePanel.java,v $
// Revision 1.4  2007/01/10 14:28:18  ounsy
// minor changes (look&feel)
//
// Revision 1.3  2007/01/09 16:25:49  ounsy
// look & feel with "expand all" buttons in main frame
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
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
package fr.soleil.mambo.containers.archiving;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.archiving.ACAttributesRecapTreeExpandAllAction;
import fr.soleil.mambo.components.archiving.ACAttributesRecapTree;
import fr.soleil.mambo.models.ACAttributesTreeModel;

public class ArchivingAttributesTreePanel extends JPanel {
	private static ArchivingAttributesTreePanel instance = null;

	private JScrollPane scrollPane;
	private ACAttributesRecapTree tree;
	private final static ImageIcon expandAllIcon = new ImageIcon(Mambo.class
			.getResource("icons/expand_all.gif"));
	private JButton expandAllButton;

	/**
	 * @return 8 juil. 2005
	 */
	public static ArchivingAttributesTreePanel getInstance() {
		if (instance == null) {
			instance = new ArchivingAttributesTreePanel();
		}

		return instance;
	}

	/**
     * 
     */
	private ArchivingAttributesTreePanel() {
		super();

		initComponents();
		addComponents();
		setLayout();
	}

	/**
	 * 19 juil. 2005
	 */
	private void setLayout() {
		this.setLayout(new GridLayout(1, 0));
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {

		this.add(scrollPane);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
		tree = ACAttributesRecapTree.getInstance(model);

		expandAllButton = new JButton(
				new ACAttributesRecapTreeExpandAllAction());
		expandAllButton.setIcon(expandAllIcon);
		expandAllButton.setMargin(new Insets(0, 0, 0, 0));
		expandAllButton.setBackground(Color.WHITE);
		expandAllButton.setBorderPainted(false);
		expandAllButton.setFocusable(false);
		expandAllButton.setFocusPainted(false);
		expandAllButton.setHorizontalAlignment(JButton.LEFT);
		expandAllButton.setFont(GUIUtilities.expandButtonFont);
		expandAllButton.setForeground(Color.BLACK);
		scrollPane = new JScrollPane(tree);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setMinimumSize(new Dimension(100, 50));
		scrollPane.setPreferredSize(new Dimension(200, 50));
		scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));
		scrollPane.setColumnHeaderView(expandAllButton);

	}

	/**
	 * @return Returns the tree.
	 */
	public ACAttributesRecapTree getTree() {
		return tree;
	}

	/**
	 * @param tree
	 *            The tree to set.
	 */
	public void setTree(ACAttributesRecapTree tree) {
		this.tree = tree;
	}
}
