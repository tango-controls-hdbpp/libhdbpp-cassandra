//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextListPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextListPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: ContextListPanel.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:36  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.context;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.context.RemoveSelectedContextsAction;
import fr.soleil.bensikin.components.context.list.ContextListTable;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;


/**
 * Contains the current list of contexts (ContextListTable).
 *
 * @author CLAISSE
 */
public class ContextListPanel extends JPanel
{

	private static ContextListPanel contextListPanelInstance = null;
	private final static ImageIcon deleteIcon = new ImageIcon(Bensikin.class.getResource("icons/delete_small.gif"));

	/**
	 * Instantiates itself if necessary, returns the instance.
	 *
	 * @return The instance
	 */
	public static ContextListPanel getInstance()
	{
		if ( contextListPanelInstance == null )
		{
			contextListPanelInstance = new ContextListPanel();
		}

		return contextListPanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private ContextListPanel()
	{
		JScrollPane scrollpane = new JScrollPane(ContextListTable.getInstance());
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		String tooltip = Messages.getMessage("CONTEXT_LIST_REMOVE_SELECTED");
		JButton button = new JButton(new RemoveSelectedContextsAction(tooltip));
		button.setBackground(Color.BLACK);
		button.setForeground(Color.BLACK);
		button.setIcon(deleteIcon);
		button.setFocusPainted(false);
		button.setFocusable(false);

		scrollpane.setCorner(JScrollPane.UPPER_RIGHT_CORNER , button);

		this.setLayout(new GridLayout(1 , 0));
		this.add(scrollpane);

		String msg = Messages.getMessage("CONTEXT_LIST_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder
		        (BorderFactory.createEtchedBorder(EtchedBorder.LOWERED) ,
		         msg ,
		         TitledBorder.CENTER ,
		         TitledBorder.TOP ,
		         GUIUtilities.getTitleFont());
		this.setBorder(tb);

		GUIUtilities.setObjectBackground(this , GUIUtilities.CONTEXT_COLOR);
		GUIUtilities.setObjectBackground(scrollpane , GUIUtilities.CONTEXT_COLOR);
		GUIUtilities.setObjectBackground(scrollpane.getViewport() , GUIUtilities.CONTEXT_COLOR);
	}
}
