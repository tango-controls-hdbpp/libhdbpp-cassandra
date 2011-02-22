//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ArchivingPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: ArchivingPanel.java,v $
// Revision 1.6  2007/12/12 17:49:51  pierrejoseph
// HdbAvailable is stored in Mambo class.
//
// Revision 1.5  2007/10/30 17:57:56  soleilarc
// Author: XP
// Mantis bug ID: 6961
// Comment: In the initComponents method, one choose the value of the historic boolean, according to the property file mambo.properties.
//
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/02/24 12:19:17  ounsy
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
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.tools.Messages;

public class ArchivingPanel extends JPanel {
	// private static final int INITIAL_DETAIL_SPLIT_POSITION = 200;

	private static ArchivingPanel instance = null;

	private ArchivingGeneralPanel archivingGeneralPanel;
	private ArchivingActionPanel archivingActionPanel;
	private ArchivingAttributesPanel archivingAttributesPanel;

	/**
	 * @return 8 juil. 2005
	 */
	public static ArchivingPanel getInstance() {
		if (instance == null) {
			instance = new ArchivingPanel();
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.ARCHIVING_COLOR);
		}

		return instance;
	}

	/**
     *
     */
	private ArchivingPanel() {
		super(new GridBagLayout());
		initComponents();
		addComponents();
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {
		GridBagConstraints generalConstraints = new GridBagConstraints();
		generalConstraints.fill = GridBagConstraints.BOTH;
		generalConstraints.gridx = 0;
		generalConstraints.gridy = 0;
		generalConstraints.weightx = 1;
		generalConstraints.weighty = 0;
		this.add(archivingGeneralPanel, generalConstraints);

		GridBagConstraints attrConstraints = new GridBagConstraints();
		attrConstraints.fill = GridBagConstraints.BOTH;
		attrConstraints.gridx = 0;
		attrConstraints.gridy = 1;
		attrConstraints.weightx = 1;
		attrConstraints.weighty = 1;
		this.add(archivingAttributesPanel, attrConstraints);

		GridBagConstraints actionConstraints = new GridBagConstraints();
		actionConstraints.fill = GridBagConstraints.BOTH;
		actionConstraints.gridx = 0;
		actionConstraints.gridy = 2;
		actionConstraints.weightx = 1;
		actionConstraints.weighty = 0;
		this.add(archivingActionPanel, actionConstraints);

		String msg = Messages.getMessage("ARCHIVING_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getTitleFont());
		this.setBorder(tb);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		archivingGeneralPanel = ArchivingGeneralPanel.getInstance();
		archivingActionPanel = ArchivingActionPanel.getInstance();
		archivingAttributesPanel = ArchivingAttributesPanel.getInstance(Mambo
				.isHdbAvailable());
	}

}
