// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ViewPanel.java,v $
// Revision 1.4 2008/04/09 10:45:46 achouri
// remove viewActionPanel
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

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ViewPanel extends JPanel {

	private static final long serialVersionUID = -3834427618013055362L;
	private static ViewPanel instance = null;
	private JSplitPane splitPane;

	/**
	 * @return 8 juil. 2005
	 */
	public static ViewPanel getInstance() {
		if (instance == null) {
			instance = new ViewPanel();
		}

		return instance;
	}

	/**
     *
     */
	private ViewPanel() {
		super(new GridLayout());
		initComponents();
		addComponents();
	}

	private void initComponents() {
		GUIUtilities.setObjectBackground(this, GUIUtilities.VIEW_COLOR);
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		Border selectionBorder = ViewSelectionPanel.getInstance().getBorder();
		ViewSelectionPanel.getInstance().setBorder(null);
		JScrollPane selectionScrollPane = new JScrollPane(ViewSelectionPanel
				.getInstance());
		selectionScrollPane.setBorder(selectionBorder);
		selectionBorder = null;
		GUIUtilities.setObjectBackground(selectionScrollPane,
				GUIUtilities.VIEW_COLOR);
		GUIUtilities.setObjectBackground(selectionScrollPane.getViewport(),
				GUIUtilities.VIEW_COLOR);
		GUIUtilities.setObjectBackground(selectionScrollPane
				.getHorizontalScrollBar(), GUIUtilities.VIEW_COLOR);
		GUIUtilities.setObjectBackground(selectionScrollPane
				.getVerticalScrollBar(), GUIUtilities.VIEW_COLOR);
		splitPane.setTopComponent(selectionScrollPane);
		splitPane.setBottomComponent(ViewConfigurationBeanManager.getInstance()
				.getDockingManager().getDockingArea());
		splitPane.setDividerSize(8);
		splitPane.setOneTouchExpandable(true);
		GUIUtilities.setObjectBackground(splitPane, GUIUtilities.VIEW_COLOR);
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		add(splitPane);

		String msg = Messages.getMessage("VIEW_BORDER");
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getTitleFont());
		setBorder(border);
	}

}
