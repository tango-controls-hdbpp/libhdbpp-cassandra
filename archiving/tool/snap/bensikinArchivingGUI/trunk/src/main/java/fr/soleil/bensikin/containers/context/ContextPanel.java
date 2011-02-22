//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ContextPanel.java,v $
// Revision 1.8  2006/01/12 13:53:22  ounsy
// minor changes
//
// Revision 1.7  2006/01/12 10:27:48  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:22:43  ounsy
// minor changes
//
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

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.tools.Messages;

/**
 * Contains the context half of the application, ie. a list of contexts
 * (ContextListPanel) and details about the current context
 * (ContextDetailPanel).
 * 
 * @author CLAISSE
 */
public class ContextPanel extends JSplitPane {
	private static final int INITIAL_DETAIL_SPLIT_POSITION_HIRES = 180;
	private static final int INITIAL_DETAIL_SPLIT_POSITION_LORES = 100;
	private static final int INITIAL_DETAIL_SPLIT_SIZE = 8;

	private static ContextPanel contextPanelInstance = null;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static ContextPanel getInstance() {
		if (contextPanelInstance == null) {
			contextPanelInstance = new ContextPanel();
		}

		return contextPanelInstance;
	}

	/**
	 * Builds the panel
	 */
	private ContextPanel() {
		super(JSplitPane.VERTICAL_SPLIT, true);

		boolean _hires = Bensikin.isHires();
		int initialDetailSplitPosition = _hires ? INITIAL_DETAIL_SPLIT_POSITION_HIRES
				: INITIAL_DETAIL_SPLIT_POSITION_LORES;
		// detailSplit.setOneTouchExpandable( true );
		this.setDividerLocation(initialDetailSplitPosition);
		this.setDividerSize(INITIAL_DETAIL_SPLIT_SIZE);

		this.setTopComponent(ContextListPanel.getInstance());
		this.setBottomComponent(ContextDetailPanel.getInstance());

		String msg = Messages.getMessage("CONTEXT_BORDER");
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED), msg,
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
				GUIUtilities.getTitleFont());
		this.setBorder(tb);

		GUIUtilities.setObjectBackground(this, GUIUtilities.CONTEXT_COLOR);
	}
}
