// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/VCVariationResultPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationResultPanel.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: VCVariationResultPanel.java,v $
// Revision 1.1 2005/11/29 18:27:07 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCVariationResultTable;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class VCVariationResultPanel extends JPanel {

	private static final long serialVersionUID = -7591541721591304385L;
	private VCVariationResultTable resultTable;

	public VCVariationResultPanel(ViewConfigurationBean viewConfigurationBean) {
		super();
		resultTable = new VCVariationResultTable(viewConfigurationBean);
		JLabel label = new JLabel(Messages
				.getMessage("DIALOGS_VARIATION_RESULT"), JLabel.CENTER);
		label.getPreferredSize().height += 10;
		setLayout(new SpringLayout());
		add(label);
		add(resultTable.getTableHeader());
		resultTable.setRowHeight(resultTable.getRowHeight() + 2);
		add(resultTable);
		SpringUtilities.makeCompactGrid(this, 3, 1, // rows, cols
				6, 0, // initX, initY
				6, 0, // xPad, yPad
				true // same size for everyone
				);
	}

	public VCVariationResultTable getResultTable() {
		return resultTable;
	}

}
