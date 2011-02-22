// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/VCVariationDialog.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationDialog.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCVariationDialog.java,v $
// Revision 1.4 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.3 2006/02/01 14:09:13 ounsy
// minor changes
//
// Revision 1.2 2005/12/15 11:32:40 ounsy
// minor changes
//
// Revision 1.1 2005/11/29 18:27:45 chinkumo
// no message
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;

import javax.swing.JDialog;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.view.VCVariationPanel;
import fr.soleil.mambo.tools.Messages;

public class VCVariationDialog extends JDialog {

	private static final long serialVersionUID = -7637117265789617930L;
	private VCVariationPanel variationPanel;
	private ViewConfigurationBean viewConfigurationBean;

	public VCVariationDialog(ViewConfigurationBean viewConfigurationBean) {
		super(MamboFrame.getInstance(), Messages
				.getMessage("DIALOGS_VARIATION_TITLE"), true);
		this.viewConfigurationBean = viewConfigurationBean;
		initComponents();
		addComponents();
		Dimension size = new Dimension();
		size.width = variationPanel.getPreferredSize().width + 50;
		size.height = 400;
		setSize(size);
		int mainWidth = MamboFrame.getInstance().getWidth();
		int width = getWidth();
		setLocation(mainWidth - width - 10, 200);

	}

	private void initComponents() {
		variationPanel = new VCVariationPanel(viewConfigurationBean);
	}

	private void addComponents() {
		this.getContentPane().add(variationPanel);
		variationPanel.getResultPanel().getResultTable().repaint();
		variationPanel.getRankingPanel().getRankingTable().repaint();
	}

	public VCVariationPanel getVariationPanel() {
		return variationPanel;
	}

}
