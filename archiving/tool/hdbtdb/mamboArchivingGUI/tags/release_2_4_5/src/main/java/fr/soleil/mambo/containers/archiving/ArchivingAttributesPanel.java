//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ArchivingAttributesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingAttributesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ArchivingAttributesPanel.java,v $
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/02/24 12:18:56  ounsy
// modified for HDB/TDB separation
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ArchivingAttributesPanel extends JPanel {
	private static ArchivingAttributesPanel instance = null;

	private ArchivingAttributesTreePanel archivingAttributesTreePanel;
	private ArchivingAttributesDetailPanel archivingAttributesDetailPanel;

	private boolean historic;

	/**
	 * @return 8 juil. 2005
	 */
	public static ArchivingAttributesPanel getInstance(boolean _historic) {
		if (instance == null) {
			instance = new ArchivingAttributesPanel(_historic);
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.ARCHIVING_COLOR);
		}

		return instance;
	}

	/**
     *
     */
	private ArchivingAttributesPanel(boolean _historic) {
		super();
		historic = _historic;

		initComponents();
		addComponents();
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		GridBagConstraints treeConstraints = new GridBagConstraints();
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
		this.add(archivingAttributesTreePanel, treeConstraints);
		this.add(archivingAttributesDetailPanel, graphConstraints);

		String msg = Messages.getMessage("ARCHIVING_ATTRIBUTES_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.CENTER, TitledBorder.TOP);
		this.setBorder(tb);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		archivingAttributesTreePanel = ArchivingAttributesTreePanel
				.getInstance();
		archivingAttributesDetailPanel = ArchivingAttributesDetailPanel
				.getInstance(historic);
	}

}
