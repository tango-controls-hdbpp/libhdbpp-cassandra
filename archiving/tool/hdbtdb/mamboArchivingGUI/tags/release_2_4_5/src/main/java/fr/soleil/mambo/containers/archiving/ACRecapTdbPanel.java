//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ACRecapTdbPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACRecapTdbPanel.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ACRecapTdbPanel.java,v $
// Revision 1.3  2006/05/05 08:11:21  ounsy
// "Archiving Assessment" not displayable for TDB bug correction
//
// Revision 1.2  2005/12/15 11:22:00  ounsy
// "copy table to clipboard" management
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

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.components.archiving.ACRecapTable;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.models.ACRecapTableModel;
import fr.soleil.mambo.tools.SpringUtilities;

public class ACRecapTdbPanel extends JPanel {

	private int nbTables;

	public ACRecapTdbPanel() {
		super();
		nbTables = 0;
		GUIUtilities.setObjectBackground(this, GUIUtilities.ARCHIVING_COLOR);
		initComponents();
		initLayout();
		this.setDoubleBuffered(true);
	}

	private void initComponents() {
		ArchivingConfiguration ac = ArchivingConfiguration
				.getSelectedArchivingConfiguration();
		ArchivingConfigurationAttributes acAttrs;
		ArchivingConfigurationAttribute[] attrs;
		if (ac == null) {
			acAttrs = null;
			attrs = new ArchivingConfigurationAttribute[0];
		} else {
			acAttrs = ac.getAttributes();
			attrs = acAttrs.getAttributesList();
		}
		for (int i = 0; i < attrs.length; i++) {
			ACRecapTable tableG = new ACRecapTable();
			// ACRecapTableModel modelG = ( ACRecapTableModel )
			// tableG.getModel();
			ACRecapTableModel modelG = new ACRecapTableModel();

			modelG.load(attrs[i], false, ACRecapTableModel.titleOrGeneral);
			if (modelG.getRowCount() > 0) {
				GUIUtilities.setObjectBackground(tableG,
						GUIUtilities.ARCHIVING_COLOR);
				add(tableG);
				nbTables++;
			}

			tableG.setModel(modelG);
			tableG.setDoubleBuffered(true);

			ACRecapTable tableP = new ACRecapTable();
			// ACRecapTableModel modelP = ( ACRecapTableModel )
			// tableP.getModel();
			ACRecapTableModel modelP = new ACRecapTableModel();

			modelP.load(attrs[i], false, ACRecapTableModel.modeP);
			if (modelP.getRowCount() > 0) {
				tableP.setModel(modelP);
				add(tableP);
				nbTables++;
			}

			tableP.setModel(modelP);
			tableP.setDoubleBuffered(true);

			ACRecapTable tableA = new ACRecapTable();
			// ACRecapTableModel modelA = ( ACRecapTableModel )
			// tableA.getModel();
			ACRecapTableModel modelA = new ACRecapTableModel();

			modelA.load(attrs[i], false, ACRecapTableModel.modeA);
			if (modelA.getRowCount() > 0) {
				add(tableA);
				nbTables++;
			}

			tableA.setModel(modelA);
			tableA.setDoubleBuffered(true);

			ACRecapTable tableR = new ACRecapTable();
			// ACRecapTableModel modelR = ( ACRecapTableModel )
			// tableR.getModel();
			ACRecapTableModel modelR = new ACRecapTableModel();

			modelR.load(attrs[i], false, ACRecapTableModel.modeR);
			if (modelR.getRowCount() > 0) {
				add(tableR);
				nbTables++;
			}

			tableR.setModel(modelR);
			tableR.setDoubleBuffered(true);

			ACRecapTable tableT = new ACRecapTable();
			// ACRecapTableModel modelT = ( ACRecapTableModel )
			// tableT.getModel();
			ACRecapTableModel modelT = new ACRecapTableModel();

			modelT.load(attrs[i], false, ACRecapTableModel.modeT);
			if (modelT.getRowCount() > 0) {
				add(tableT);
				nbTables++;
			}

			tableT.setModel(modelT);
			tableT.setDoubleBuffered(true);

			ACRecapTable tableD = new ACRecapTable();
			// ACRecapTableModel modelD = ( ACRecapTableModel )
			// tableD.getModel();
			ACRecapTableModel modelD = new ACRecapTableModel();

			modelD.load(attrs[i], false, ACRecapTableModel.modeD);
			if (modelD.getRowCount() > 0) {
				add(tableD);
				nbTables++;
			}

			tableD.setModel(modelD);
			tableD.setDoubleBuffered(true);

			ACRecapTable tableC = new ACRecapTable();
			// ACRecapTableModel modelC = ( ACRecapTableModel )
			// tableC.getModel();
			ACRecapTableModel modelC = new ACRecapTableModel();

			modelC.load(attrs[i], false, ACRecapTableModel.modeC);
			if (modelC.getRowCount() > 0) {
				add(tableC);
				nbTables++;
			}

			tableC.setModel(modelC);
			tableC.setDoubleBuffered(true);

			ACRecapTable tableE = new ACRecapTable();
			// ACRecapTableModel modelE = ( ACRecapTableModel )
			// tableE.getModel();
			ACRecapTableModel modelE = new ACRecapTableModel();

			modelE.load(attrs[i], false, ACRecapTableModel.modeE);
			if (modelE.getRowCount() > 0) {
				add(tableE);
				nbTables++;
			}
			tableE.setModel(modelE);
			tableE.setDoubleBuffered(true);
		}
	}

	public void initLayout() {
		setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(this, nbTables, 1, // rows, cols
				0, 0, // initX, initY
				0, 0, // xPad, yPad
				true); // every component same size
	}

	public String toString() {
		String stringValue = "";
		for (int i = 0; i < nbTables; i++) {
			stringValue += this.getComponent(i).toString() + "\n";
		}
		if ("".equals(stringValue)) {
			return stringValue;
		} else {
			return stringValue.substring(0, stringValue.lastIndexOf("\n"));
		}
	}

}
