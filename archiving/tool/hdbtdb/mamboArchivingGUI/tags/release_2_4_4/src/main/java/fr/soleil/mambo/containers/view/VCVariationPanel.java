// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/VCVariationPanel.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationPanel.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCVariationPanel.java,v $
// Revision 1.4 2006/11/06 09:28:05 ounsy
// icons reorganization
//
// Revision 1.3 2006/07/28 10:07:12 ounsy
// icons moved to "icons" package
//
// Revision 1.2 2005/12/15 11:30:28 ounsy
// "copy table to clipboard" management
//
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

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCVariationCopyAction;
import fr.soleil.mambo.actions.view.VCVariationEditCopyAction;
import fr.soleil.mambo.actions.view.VCVariationViewAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.MamboActivableButton;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class VCVariationPanel extends JPanel {

	private static final long serialVersionUID = 4930710519724172392L;
	private final static ImageIcon viewIcon = new ImageIcon(Mambo.class
			.getResource("icons/View.gif"));
	private final static ImageIcon viewDisabledIcon = new ImageIcon(Mambo.class
			.getResource("icons/View_Disabled.gif"));
	private JRadioButton resultRadio;
	private JRadioButton rankingRadio;
	private VCVariationResultPanel resultPanel;
	private VCVariationRankingPanel rankingPanel;
	private VCVariationEditCopyAction editCopyAction;

	public VCVariationPanel(ViewConfigurationBean viewConfigurationBean) {
		super();
		setLayout(new SpringLayout());
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		GUIUtilities.setObjectBackground(splitPane, GUIUtilities.VIEW_COLOR);
		splitPane.setForeground(splitPane.getBackground());
		splitPane.updateUI();
		splitPane.setDividerSize(8);
		splitPane.setResizeWeight(0.5);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		resultPanel = new VCVariationResultPanel(viewConfigurationBean);
		rankingPanel = new VCVariationRankingPanel(viewConfigurationBean);
		JScrollPane scrollpane1 = new JScrollPane(resultPanel);
		GUIUtilities.setObjectBackground(scrollpane1, GUIUtilities.VIEW_COLOR);
		GUIUtilities.setObjectBackground(scrollpane1.getViewport(),
				GUIUtilities.VIEW_COLOR);
		JScrollPane scrollpane2 = new JScrollPane(rankingPanel);
		GUIUtilities.setObjectBackground(scrollpane2, GUIUtilities.VIEW_COLOR);
		GUIUtilities.setObjectBackground(scrollpane2.getViewport(),
				GUIUtilities.VIEW_COLOR);
		splitPane.add(scrollpane1);
		splitPane.add(scrollpane2);
		add(splitPane);
		String msg = Messages.getMessage("DIALOGS_VARIATION_VIEW");
		VCVariationViewAction action = new VCVariationViewAction(msg,
				viewConfigurationBean);
		MamboActivableButton viewButton = new MamboActivableButton(action,
				viewIcon, viewDisabledIcon);
		GUIUtilities.setObjectBackground(viewButton, GUIUtilities.VIEW_COLOR);
		msg = Messages.getMessage("DIALOGS_VARIATION_COPY");
		JButton copyButton = new JButton(new VCVariationCopyAction(msg, this));
		copyButton.setMargin(new Insets(0, 0, 0, 0));
		GUIUtilities.setObjectBackground(copyButton,
				GUIUtilities.VIEW_COPY_COLOR);
		msg = Messages.getMessage("DIALOGS_VARIATION_EDIT_COPY");
		editCopyAction = new VCVariationEditCopyAction(msg,
				viewConfigurationBean);
		JButton editCopyButton = new JButton(editCopyAction);
		editCopyButton.setMargin(new Insets(0, 0, 0, 0));
		GUIUtilities.setObjectBackground(editCopyButton,
				GUIUtilities.VIEW_COPY_COLOR);

		JPanel selectPanel = new JPanel();
		msg = Messages.getMessage("DIALOGS_VARIATION_SELECT");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.CENTER, TitledBorder.TOP);
		Border border = (Border) (tb);
		selectPanel.setBorder(border);
		ButtonGroup selectGroup = new ButtonGroup();
		resultRadio = new JRadioButton(Messages
				.getMessage("DIALOGS_VARIATION_RESULT"));
		resultRadio.setSelected(true);
		resultRadio.setOpaque(false);
		rankingRadio = new JRadioButton(Messages
				.getMessage("DIALOGS_VARIATION_RANKING"));
		rankingRadio.setSelected(true);
		rankingRadio.setOpaque(false);
		selectGroup.add(resultRadio);
		selectGroup.add(rankingRadio);
		selectPanel.setLayout(new SpringLayout());
		selectPanel.add(resultRadio);
		selectPanel.add(rankingRadio);
		GUIUtilities.setObjectBackground(selectPanel, GUIUtilities.VIEW_COLOR);
		SpringUtilities.makeCompactGrid(selectPanel, 1, 2, // rows, cols
				0, 0, // initX, initY
				5, 0, // xPad, yPad
				false // same size for everyone
				);
		add(selectPanel);

		JPanel detailPanel = new JPanel();
		detailPanel.setLayout(new SpringLayout());
		GUIUtilities.setObjectBackground(detailPanel, GUIUtilities.VIEW_COLOR);
		String dates = "Between dates ["
				+ viewConfigurationBean.getViewConfiguration().getData()
						.getStartDate()
				+ "] and ["
				+ viewConfigurationBean.getViewConfiguration().getData()
						.getEndDate() + "]";
		JLabel datesLabel = new JLabel(dates);
		GUIUtilities.setObjectBackground(datesLabel, GUIUtilities.VIEW_COLOR);
		detailPanel.add(viewButton);
		detailPanel.add(copyButton);
		detailPanel.add(editCopyButton);
		detailPanel.add(datesLabel);
		SpringUtilities.makeCompactGrid(detailPanel, 1, 4, // rows, cols
				0, 0, // initX, initY
				5, 0, // xPad, yPad
				false // same size for everyone
				);
		add(detailPanel);
		SpringUtilities.makeCompactGrid(this, 3, 1, // rows, cols
				0, 0, // initX, initY
				0, 0, // xPad, yPad
				true // same size for everyone
				);
		Dimension size = splitPane.getPreferredSize();
		size.width += splitPane.getDividerSize() + 100;
		size.height += 100 + viewButton.getHeight();
		setPreferredSize(size);

	}

	public boolean getWorkingTable() {
		return resultRadio.isSelected();
	}

	public String selectedToString() {
		if (getWorkingTable()) {
			return resultPanel.getResultTable().toString();
		} else {
			return rankingPanel.getRankingTable().toString();
		}
	}

	public VCVariationResultPanel getResultPanel() {
		return resultPanel;
	}

	public VCVariationRankingPanel getRankingPanel() {
		return rankingPanel;
	}

}
