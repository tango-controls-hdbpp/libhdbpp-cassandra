//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ACRecapPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACRecapPanel.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: ACRecapPanel.java,v $
// Revision 1.7  2006/08/09 10:35:27  ounsy
// Time formating in assessments + differences between database and archiving configuration highlighted
//
// Revision 1.6  2006/05/19 13:45:13  ounsy
// minor changes
//
// Revision 1.5  2006/05/16 12:49:41  ounsy
// modified imports
//
// Revision 1.4  2006/03/14 11:11:52  ounsy
// Separated HDB/TDB optimisation
//
// Revision 1.3  2006/03/07 14:40:00  ounsy
// either the HDB or the TDB panel are displayed, depending on the
// seleted AC, but no longer both
//
// Revision 1.2  2005/12/15 11:21:52  ounsy
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

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.actions.archiving.ACRecapCopyAction;
import fr.soleil.mambo.actions.archiving.ACRecapEditCopyAction;
import fr.soleil.mambo.components.archiving.ACRecapTable;
import fr.soleil.mambo.components.renderers.ACRecapTableRenderer;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.models.ACRecapTableModel;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ACRecapPanel extends JPanel {

	private static ACRecapPanel instance;

	JLabel titleLabel;
	JTabbedPane tabbedPane;
	ACRecapHdbPanel hdbPanel;
	ACRecapTdbPanel tdbPanel;
	ACRecapTable titleTableHdb;
	ACRecapTable titleTableTdb;
	JScrollPane scrollHdb;
	JScrollPane scrollTdb;
	JButton copyButton;
	JButton editCopyButton;

	protected ACRecapPanel() {
		super();
		initComponents();
		initLayout();
		GUIUtilities.setObjectBackground(this, GUIUtilities.ARCHIVING_COLOR);
		this.setDoubleBuffered(true);
	}

	public static ACRecapPanel getInstance(boolean forceReload) {
		if (instance == null || forceReload) {
			instance = new ACRecapPanel();
		}
		return instance;
	}

	public void initComponents() {
		boolean nothingToDisplay = (ArchivingConfiguration
				.getSelectedArchivingConfiguration() == null
				|| ArchivingConfiguration.getSelectedArchivingConfiguration()
						.getData() == null
				|| ArchivingConfiguration.getSelectedArchivingConfiguration()
						.getAttributes() == null
				|| ArchivingConfiguration.getSelectedArchivingConfiguration()
						.getAttributes().getAttributesMap() == null || ArchivingConfiguration
				.getSelectedArchivingConfiguration().getAttributes()
				.getAttributesMap().isEmpty());

		titleLabel = new JLabel(Messages
				.getMessage("ARCHIVING_ASSESSMENT_TITLE"), JLabel.CENTER);
		GUIUtilities.setObjectBackground(titleLabel,
				GUIUtilities.ARCHIVING_COLOR);
		add(titleLabel);

		if (nothingToDisplay) {
			JLabel label = new JLabel(Messages
					.getMessage("ARCHIVING_ASSESSMENT_EMPTY"));
			GUIUtilities.setObjectBackground(label,
					GUIUtilities.ARCHIVING_COLOR);
			add(label);
		} else {
			boolean isHistoric = ArchivingConfiguration
					.getSelectedArchivingConfiguration().isHistoric();
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new SpringLayout());
			copyButton = new JButton(ACRecapCopyAction.getInstance(Messages
					.getMessage("ARCHIVING_ACTION_COPY_BUTTON")));
			copyButton.setMargin(new Insets(0, 0, 0, 0));
			copyButton.setBackground(new Color(255, 150, 200));
			editCopyButton = new JButton(ACRecapEditCopyAction
					.getInstance(Messages
							.getMessage("ARCHIVING_ACTION_EDIT_COPY_BUTTON")));
			editCopyButton.setMargin(new Insets(0, 0, 0, 0));
			GUIUtilities.setObjectBackground(copyButton,
					GUIUtilities.ARCHIVING_COPY_COLOR);
			GUIUtilities.setObjectBackground(editCopyButton,
					GUIUtilities.ARCHIVING_COPY_COLOR);
			buttonPanel.add(copyButton);
			buttonPanel.add(editCopyButton);
			SpringUtilities.makeCompactGrid(buttonPanel, 1, buttonPanel
					.getComponentCount(), // rows, cols
					5, 0, // initX, initY
					5, 0, // xPad, yPad
					true); // every component same size
			GUIUtilities.setObjectBackground(buttonPanel,
					GUIUtilities.ARCHIVING_COLOR);
			add(buttonPanel);

			tabbedPane = new JTabbedPane();
			GUIUtilities.setObjectBackground(tabbedPane,
					GUIUtilities.ARCHIVING_COLOR);

			if (isHistoric) {
				hdbPanel = new ACRecapHdbPanel();
				scrollHdb = new JScrollPane(hdbPanel);
				titleTableHdb = new ACRecapTable();
				titleTableHdb.setModel(new ACRecapTableModel(true));
				titleTableHdb.setDefaultRenderer(Object.class,
						new ACRecapTableRenderer(true));
				GUIUtilities.setObjectBackground(titleTableHdb,
						GUIUtilities.ARCHIVING_COLOR);
				scrollHdb.setColumnHeaderView(titleTableHdb);
				scrollHdb.getColumnHeader().setPreferredSize(
						titleTableHdb.getMinimumSize());
				titleTableHdb.setDoubleBuffered(true);
				GUIUtilities.setObjectBackground(scrollHdb,
						GUIUtilities.ARCHIVING_COLOR);
				GUIUtilities.setObjectBackground(scrollHdb.getViewport(),
						GUIUtilities.ARCHIVING_COLOR);
				GUIUtilities.setObjectBackground(scrollHdb.getColumnHeader(),
						GUIUtilities.ARCHIVING_COLOR);
				scrollHdb.setDoubleBuffered(true);
				tabbedPane.add(Messages
						.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_HDB_LABEL"),
						scrollHdb);
			} else {
				tdbPanel = new ACRecapTdbPanel();
				scrollTdb = new JScrollPane(tdbPanel);
				titleTableTdb = new ACRecapTable();
				titleTableTdb.setDefaultRenderer(Object.class,
						new ACRecapTableRenderer(true));
				titleTableTdb.setModel(new ACRecapTableModel(false));
				GUIUtilities.setObjectBackground(titleTableTdb,
						GUIUtilities.ARCHIVING_COLOR);
				scrollTdb.setColumnHeaderView(titleTableTdb);
				scrollTdb.getColumnHeader().setPreferredSize(
						titleTableTdb.getMinimumSize());
				titleTableTdb.setDoubleBuffered(true);
				GUIUtilities.setObjectBackground(scrollTdb,
						GUIUtilities.ARCHIVING_COLOR);
				GUIUtilities.setObjectBackground(scrollTdb.getViewport(),
						GUIUtilities.ARCHIVING_COLOR);
				GUIUtilities.setObjectBackground(scrollTdb.getColumnHeader(),
						GUIUtilities.ARCHIVING_COLOR);
				scrollTdb.setDoubleBuffered(true);
				tabbedPane.add(Messages
						.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_TDB_LABEL"),
						scrollTdb);
			}

			tabbedPane.setDoubleBuffered(true);
			add(tabbedPane);
		}
	}

	public void initLayout() {
		setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(this, this.getComponentCount(), 1, // rows,
																			// cols
				0, 5, // initX, initY
				0, 5, // xPad, yPad
				false); // every component same size
	}

	/**
	 * Returns the String representation of the selected Assessment table (hdb
	 * or tdb)
	 * 
	 * @return The String representation of the selected Assessment table (hdb
	 *         or tdb)
	 */
	public String selectedToString() {
		if (tabbedPane.getSelectedComponent() == scrollHdb) {
			return titleTableHdb.toString() + "\n" + hdbPanel.toString();
		} else {
			return titleTableTdb.toString() + "\n" + tdbPanel.toString();
		}
	}
}
