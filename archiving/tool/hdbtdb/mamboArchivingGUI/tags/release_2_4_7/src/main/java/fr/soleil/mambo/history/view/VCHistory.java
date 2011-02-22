// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/history/view/VCHistory.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCHistory.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.3 $
//
// $Log: VCHistory.java,v $
// Revision 1.3 2007/02/01 14:15:38 pierrejoseph
// XmlHelper reorg
//
// Revision 1.2 2005/11/29 18:28:26 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:32 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.history.view;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.components.view.LimitedVCStack;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class VCHistory {

	// MULTI-CONF
	private ViewConfiguration selectedVC;
	private LimitedVCStack openedVCs;

	public static final String VC_KEY = "viewConfigurations";
	public static final String SELECTED_VC_KEY = "selectedVC";
	public static final String OPENED_VCS_KEY = "openedVCs";
	public static final String OPENED_VC_KEY = "openedVC";

	public static int STACK_DEPTH = 3;

	public VCHistory() {

	}

	public VCHistory(ViewConfiguration _selectedVC) {
		this.selectedVC = _selectedVC;
	}

	/**
	 * @param selectedVc2
	 * @param openedVc
	 */
	public VCHistory(ViewConfiguration _selectedVC, LimitedVCStack _openedVCs) {
		this.selectedVC = _selectedVC;
		this.openedVCs = _openedVCs;
	}

	/**
	 * 2 sept. 2005
	 */
	public void push() {
		/*
		 * if ( this.selectedVC != null ) { this.selectedVC.setModified ( false
		 * ); this.selectedVC.push (); }
		 */

		OpenedVCComboBox openedVCComboBox = OpenedVCComboBox
				.getInstance(this.openedVCs);
		openedVCComboBox.setElements(this.openedVCs);
		openedVCComboBox.selectElement(selectedVC);

		// if ( openedVCComboBox.getSelectedVC() != null )
		// {
		// openedVCComboBox.getSelectedVC().push();
		// }
	}

	public String toString() {
		String ret = "";

		ret += new XMLLine(VCHistory.VC_KEY, XMLLine.OPENING_TAG_CATEGORY);
		ret += GUIUtilities.CRLF;

		if (selectedVC != null) {
			ret += new XMLLine(VCHistory.SELECTED_VC_KEY,
					XMLLine.OPENING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;

			ret += selectedVC.toString();
			ret += GUIUtilities.CRLF;

			ret += new XMLLine(VCHistory.SELECTED_VC_KEY,
					XMLLine.CLOSING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;
		}

		if (openedVCs != null) {
			ret += new XMLLine(VCHistory.OPENED_VCS_KEY,
					XMLLine.OPENING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;

			ret += openedVCs.toString();
			ret += GUIUtilities.CRLF;

			ret += new XMLLine(VCHistory.OPENED_VCS_KEY,
					XMLLine.CLOSING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;
		}

		ret += new XMLLine(VCHistory.VC_KEY, XMLLine.CLOSING_TAG_CATEGORY);
		ret += GUIUtilities.CRLF;

		return ret;
	}

	/**
	 * @return Returns the openedVCs.
	 */
	public LimitedVCStack getOpenedVCs() {
		return openedVCs;
	}

	/**
	 * @param openedVCs
	 *            The openedVCs to set.
	 */
	public void setOpenedVCs(LimitedVCStack openedVCs) {
		this.openedVCs = openedVCs;
	}
}
