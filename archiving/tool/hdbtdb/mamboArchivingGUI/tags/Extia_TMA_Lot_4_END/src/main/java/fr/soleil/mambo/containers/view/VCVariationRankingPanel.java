// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/VCVariationRankingPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationRankingPanel.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: VCVariationRankingPanel.java,v $
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
import fr.soleil.mambo.components.view.VCVariationRankingTable;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class VCVariationRankingPanel extends JPanel {

	private static final long serialVersionUID = 439157289520809968L;
	private VCVariationRankingTable rankingTable;

	public VCVariationRankingPanel(ViewConfigurationBean viewConfigurationBean) {
		super();
		rankingTable = new VCVariationRankingTable(viewConfigurationBean);
		JLabel label = new JLabel(Messages
				.getMessage("DIALOGS_VARIATION_RANKING"), JLabel.CENTER);
		label.getPreferredSize().height += 10;
		setLayout(new SpringLayout());
		add(label);
		add(rankingTable.getTableHeader());
		rankingTable.setRowHeight(rankingTable.getRowHeight() + 2);
		add(rankingTable);
		SpringUtilities.makeCompactGrid(this, 3, 1, // rows, cols
				6, 0, // initX, initY
				6, 0, // xPad, yPad
				true // same size for everyone
				);
	}

	public VCVariationRankingTable getRankingTable() {
		return rankingTable;
	}

}
