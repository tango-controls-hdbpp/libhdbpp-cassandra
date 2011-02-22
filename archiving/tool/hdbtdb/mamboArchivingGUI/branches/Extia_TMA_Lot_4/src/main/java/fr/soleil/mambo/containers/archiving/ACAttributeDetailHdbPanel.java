//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ACAttributeDetailHdbPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributeDetailHdbPanel.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACAttributeDetailHdbPanel.java,v $
// Revision 1.2  2005/12/15 11:20:39  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:28:26  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.archiving;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbATable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbCTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbDTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbETable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbPTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbRTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbTTable;
import fr.soleil.mambo.components.archiving.ACAttributeDetailHdbTitleTable;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.models.ACAttributeDetailHdbATableModel;
import fr.soleil.mambo.models.ACAttributeDetailHdbCTableModel;
import fr.soleil.mambo.models.ACAttributeDetailHdbDTableModel;
import fr.soleil.mambo.models.ACAttributeDetailHdbETableModel;
import fr.soleil.mambo.models.ACAttributeDetailHdbPTableModel;
import fr.soleil.mambo.models.ACAttributeDetailHdbRTableModel;
import fr.soleil.mambo.models.ACAttributeDetailHdbTTableModel;
import fr.soleil.mambo.models.ACAttributeDetailHdbTitleTableModel;
import fr.soleil.mambo.tools.GUIUtilities;

public class ACAttributeDetailHdbPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8770409325234988519L;
	private static ACAttributeDetailHdbPanel instance;

	private ACAttributeDetailHdbPanel() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		removeComponents();
		placeComponents();
		repaint();
	}

	public static ACAttributeDetailHdbPanel getInstance() {
		if (instance == null) {
			instance = new ACAttributeDetailHdbPanel();
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.ARCHIVING_COLOR);
		}

		return instance;
	}

	public void load(ArchivingConfigurationAttribute _attribute) {

		// System.out.println
		// ("ACAttributesRecapTreeSelectionListener/load/completeName|"+_attribute.getCompleteName()+"|"
		// );
		ACAttributeDetailHdbTitleTableModel.getInstance().load(_attribute);
		ACAttributeDetailHdbPTableModel.getInstance().load(_attribute);
		ACAttributeDetailHdbATableModel.getInstance().load(_attribute);
		ACAttributeDetailHdbRTableModel.getInstance().load(_attribute);
		ACAttributeDetailHdbTTableModel.getInstance().load(_attribute);
		ACAttributeDetailHdbDTableModel.getInstance().load(_attribute);
		ACAttributeDetailHdbCTableModel.getInstance().load(_attribute);
		ACAttributeDetailHdbETableModel.getInstance().load(_attribute);
		removeComponents();
		placeComponents();
		repaint();
	}

	private void removeComponents() {
		remove(ACAttributeDetailHdbTitleTable.getInstance());
		remove(ACAttributeDetailHdbPTable.getInstance());
		remove(ACAttributeDetailHdbATable.getInstance());
		remove(ACAttributeDetailHdbRTable.getInstance());
		remove(ACAttributeDetailHdbTTable.getInstance());
		remove(ACAttributeDetailHdbDTable.getInstance());
		remove(ACAttributeDetailHdbCTable.getInstance());
		remove(ACAttributeDetailHdbETable.getInstance());
	}

	private void placeComponents() {
		if (ACAttributeDetailHdbTitleTableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbTitleTable.getInstance());
		}
		if (ACAttributeDetailHdbPTableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbPTable.getInstance());
		}
		if (ACAttributeDetailHdbATableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbATable.getInstance());
		}
		if (ACAttributeDetailHdbRTableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbRTable.getInstance());
		}
		if (ACAttributeDetailHdbTTableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbTTable.getInstance());
		}
		if (ACAttributeDetailHdbDTableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbDTable.getInstance());
		}
		if (ACAttributeDetailHdbCTableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbCTable.getInstance());
		}
		if (ACAttributeDetailHdbETableModel.getInstance().toPaint()) {
			add(ACAttributeDetailHdbETable.getInstance());
		}
	}
}
