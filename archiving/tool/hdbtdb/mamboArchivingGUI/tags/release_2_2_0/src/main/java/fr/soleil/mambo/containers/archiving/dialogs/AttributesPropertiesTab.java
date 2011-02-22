//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/dialogs/AttributesPropertiesTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesPropertiesTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: AttributesPropertiesTab.java,v $
// Revision 1.7  2006/10/06 16:02:31  ounsy
// minor changes (L&F)
//
// Revision 1.6  2006/08/23 10:03:57  ounsy
// removed an expandAll()
//
// Revision 1.5  2006/07/18 10:24:46  ounsy
// Less time consuming by setting tree expanding on demand only
//
// Revision 1.4  2006/06/20 16:03:36  ounsy
// avoids the bug of hdb/tdb information not transfered to attributes properties panel
//
// Revision 1.3  2006/02/24 12:25:38  ounsy
// modified for HDB/TDB separation
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
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
package fr.soleil.mambo.containers.archiving.dialogs;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.archiving.ACAttributesPropertiesTreeExpandAllAction;
import fr.soleil.mambo.components.archiving.ACAttributesPropertiesTree;
import fr.soleil.mambo.models.ACAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.SpringUtilities;

public class AttributesPropertiesTab extends JPanel {
	private static AttributesPropertiesTab instance = null;

	private JButton expandAllButton;

	private ACAttributesPropertiesTree tree;
	private JScrollPane scrollPane;
	private boolean isHistoric;
	private final static ImageIcon expandAllIcon = new ImageIcon(Mambo.class
			.getResource("icons/expand_all.gif"));

	// private JPanel propertiesPanel;
	private AttributesPropertiesPanel propertiesPanel;

	/**
	 * @return 8 juil. 2005
	 */
	public static AttributesPropertiesTab getInstance(boolean _isHistoric) {
		if (instance == null || instance.isHistoric() != _isHistoric) {
			instance = new AttributesPropertiesTab(_isHistoric);
		}

		return instance;
	}

	public static void resetInstance() {
		instance = null;
	}

	/**
     * 
     */
	private AttributesPropertiesTab(boolean _isHistoric) {
		this.isHistoric = _isHistoric;
		// System.out.println (
		// "CLA/AttributesPropertiesTab/new/_isHistoric/"+_isHistoric );
		this.initComponents(_isHistoric);
		this.addComponents();
		this.initLayout();
	}

	/**
	 * 19 juil. 2005
	 */
	private void initLayout() {
		this.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(this, 1, this.getComponentCount(), 5,
				5, 5, 5, true);
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		this.add(scrollPane);
		this.add(propertiesPanel);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents(boolean _isHistoric) {
		expandAllButton = new JButton(
				new ACAttributesPropertiesTreeExpandAllAction());
		expandAllButton.setIcon(expandAllIcon);
		expandAllButton.setMargin(new Insets(0, 0, 0, 0));
		expandAllButton.setBackground(Color.WHITE);
		expandAllButton.setBorderPainted(false);
		expandAllButton.setFocusable(false);
		expandAllButton.setFocusPainted(false);
		expandAllButton.setHorizontalAlignment(JButton.LEFT);
		expandAllButton.setFont(GUIUtilities.expandButtonFont);
		expandAllButton.setForeground(Color.BLACK);

		// treePanel = new JPanel(new BorderLayout());

		ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
		tree = ACAttributesPropertiesTree.getInstance(model);

		scrollPane = new JScrollPane(tree);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// scrollPane.setMinimumSize( new Dimension( 100 , 50 ) );
		// scrollPane.setPreferredSize( new Dimension( 200 , 50 ) );
		// scrollPane.setMaximumSize( new Dimension( Integer.MAX_VALUE , 500 )
		// );
		scrollPane.setColumnHeaderView(expandAllButton);

		// AttributesPropertiesPanel.resetInstance ();
		propertiesPanel = AttributesPropertiesPanel.getInstance(_isHistoric);
	}

	public boolean isHistoric() {
		return this.isHistoric;
	}
}
