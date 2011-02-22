// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewAttributesPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewAttributesPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: ViewAttributesPanel.java,v $
// Revision 1.6 2008/04/09 18:00:00 achouri
// add popup menu right click
//
// Revision 1.5 2008/04/09 10:45:46 achouri
// add ViewAttributesGraphePanel
//
// Revision 1.4 2006/10/12 13:22:53 ounsy
// ViewAttributesDetailPanel removed (useless)
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.tools.GUIUtilities;

public class ViewAttributesPanel extends JPanel {

	private static final long serialVersionUID = 12152976101874111L;
	private GridBagConstraints treeConstraints;
	private ViewAttributesTreePanel viewAttributesTreePanel = null;
	private ViewAttributesGraphPanel viewAttributesGraphPanel = null;

	private ViewConfigurationBean viewConfigurationBean;

	/**
     *
     */
	public ViewAttributesPanel(ViewConfigurationBean viewConfigurationBean) {
		super();
		this.viewConfigurationBean = viewConfigurationBean;
		initComponents();
		addComponents();
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		treeConstraints = new GridBagConstraints();
		treeConstraints.fill = GridBagConstraints.BOTH;
		treeConstraints.gridx = 0;
		treeConstraints.gridy = 0;
		treeConstraints.weightx = 0;
		treeConstraints.weighty = 1;

		GridBagConstraints graphConstraints = new GridBagConstraints();
		graphConstraints.fill = GridBagConstraints.BOTH;
		graphConstraints.gridx = 1;
		graphConstraints.gridy = 0;
		graphConstraints.weightx = 1;
		graphConstraints.weighty = 1;

		this.setLayout(new GridBagLayout());
		this.add(viewAttributesTreePanel, treeConstraints);
		this.add(viewAttributesGraphPanel, graphConstraints);

		// String msg = Messages.getMessage("VIEW_ATTRIBUTES_BORDER");
		// TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
		// .createEtchedBorder(EtchedBorder.LOWERED), msg,
		// TitledBorder.CENTER, TitledBorder.TOP);
		// this.setBorder(tb);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		viewAttributesGraphPanel = new ViewAttributesGraphPanel(
				viewConfigurationBean);
		viewAttributesTreePanel = new ViewAttributesTreePanel(
				viewConfigurationBean, this);

		GUIUtilities.setObjectBackground(viewAttributesGraphPanel,
				GUIUtilities.VIEW_COLOR);
		GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
	}

	public void showTreePanel() {
		for (int i = 0; i < getComponentCount(); i++) {
			if (viewAttributesTreePanel.equals(getComponent(i))) {
				return;
			}
		}
		add(viewAttributesTreePanel, treeConstraints);
		revalidate();
		repaint();
	}

	public void hideTreePanel() {
		remove(viewAttributesTreePanel);
		revalidate();
		repaint();
	}

	public ViewAttributesTreePanel getViewAttributesTreePanel() {
		return viewAttributesTreePanel;
	}

	public ViewAttributesGraphPanel getViewAttributesGraphPanel() {
		return viewAttributesGraphPanel;
	}

}
