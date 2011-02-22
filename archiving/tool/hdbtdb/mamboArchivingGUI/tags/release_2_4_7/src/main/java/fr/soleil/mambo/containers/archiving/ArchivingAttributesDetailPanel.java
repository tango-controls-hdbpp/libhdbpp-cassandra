//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/ArchivingAttributesDetailPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingAttributesDetailPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ArchivingAttributesDetailPanel.java,v $
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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ArchivingAttributesDetailPanel extends JPanel {
	private static ArchivingAttributesDetailPanel instance = null;

	/*
	 * private JLabel TDBLabel; private JLabel HDBLabel; private JLabel
	 * isArchivedLabel; private JLabel modeLabel;
	 * 
	 * private JLabel isArchivedHDBLabel; private JLabel isArchivedTDBLabel;
	 * private JLabel modeHDBLabel; private JLabel modeTDBLabel;
	 * 
	 * private Box titleBox;
	 */
	private JTabbedPane tabbedPane;

	private boolean historic;

	/**
	 * @return 8 juil. 2005
	 */
	public static ArchivingAttributesDetailPanel getInstance(boolean _historic) {
		if (instance == null) {
			instance = new ArchivingAttributesDetailPanel(_historic);
			GUIUtilities.setObjectBackground(instance,
					GUIUtilities.ARCHIVING_COLOR);
		}

		return instance;
	}

	public static ArchivingAttributesDetailPanel getInstance() {
		return instance;
	}

	public static void resetInstance() {
		instance = null;
	}

	/**
     *  
     */
	private ArchivingAttributesDetailPanel(boolean _historic) {
		super();
		this.historic = _historic;

		initComponents();
		addComponents();
		setLayout();

		// GUIUtilities.addDebugBorderToPanel ( this , true , Color.BLACK , 3 );
	}

	/**
	 * 19 juil. 2005
	 */
	private void setLayout() {
		/*
		 * this.setLayout( new SpringLayout () );
		 * 
		 * //Lay out the panel. SpringUtilities.makeCompactGrid(this, 3, 3,
		 * //rows, cols 6, 6, //initX, initY 6, 6, true); //xPad, yPad
		 */

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// this.setLayout( new BorderLayout () );
	}

	/**
	 * 19 juil. 2005
	 */
	private void addComponents() {

		// this.add ( new ACAttributeDetailTable () , BorderLayout.PAGE_START );
		// this.add ( ACAttributeDetailTable.getInstance() , BorderLayout.CENTER
		// );
		// this.add ( titleBox );
		this.add(tabbedPane);
	}

	/**
	 * 19 juil. 2005
	 */
	private void initComponents() {
		tabbedPane = new JTabbedPane();

		if (this.historic) {
			// System.out.println (
			// "CLA/ArchivingAttributesDetailPanel/initComponents/HDB" );
			tabbedPane.add("HDB", ACAttributeDetailHdbPanel.getInstance());
		} else {
			// System.out.println (
			// "CLA/ArchivingAttributesDetailPanel/initComponents/TDB" );
			tabbedPane.add("TDB", ACAttributeDetailTdbPanel.getInstance());
		}
		tabbedPane.setBackground(new Color(210, 190, 190));

		tabbedPane.revalidate();
		tabbedPane.repaint();

		initBorder();
	}

	/**
	 * 19 juil. 2005
	 */
	private void initBorder() {
		String msg = Messages.getMessage("ARCHIVING_ATTRIBUTES_DETAIL_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.CENTER, TitledBorder.TOP);
		Border border = (Border) (tb);
		this.setBorder(border);

	}

	/**
	 * @param b
	 */
	public void refresh(boolean b) {
		tabbedPane.removeAll();
		if (b) {
			tabbedPane.add("HDB", ACAttributeDetailHdbPanel.getInstance());
		} else {
			tabbedPane.add("TDB", ACAttributeDetailTdbPanel.getInstance());
		}
	}

}
