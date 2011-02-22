//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/history/archiving/ACHistory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACHistory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: ACHistory.java,v $
// Revision 1.4  2007/02/01 14:15:38  pierrejoseph
// XmlHelper reorg
//
// Revision 1.3  2006/08/29 14:12:26  ounsy
// avoided a NullPointerException
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.history.archiving;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.components.archiving.LimitedACStack;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ACHistory {
	private ArchivingConfiguration selectedAC;
	private LimitedACStack openedACs;

	public static final String AC_KEY = "archivingConfigurations";
	public static final String SELECTED_AC_KEY = "selectedAC";
	public static final String OPENED_AC_KEY = "openedAC";
	public static final String OPENED_ACS_KEY = "openedACs";

	public ACHistory() {

	}

	public ACHistory(ArchivingConfiguration _selectedAC) {
		this.selectedAC = _selectedAC;
	}

	/**
	 * @param selectedAc2
	 * @param openedAc
	 */
	public ACHistory(ArchivingConfiguration _selectedAC,
			LimitedACStack _openedACs) {

		this.selectedAC = _selectedAC;
		this.openedACs = _openedACs;
	}

	/**
	 * 2 sept. 2005
	 */
	public void push() {
		/*
		 * if ( this.selectedAC != null ) { this.selectedAC.setModified ( false
		 * ); this.selectedAC.push (); }
		 */
		if (this.openedACs == null) {
			this.openedACs = new LimitedACStack();
		}
		OpenedACComboBox openedACComboBox = OpenedACComboBox
				.getInstance(this.openedACs);
		openedACComboBox.setElements(this.openedACs);

		if (openedACComboBox.getSelectedAC() != null) {
			openedACComboBox.getSelectedAC().push();
		}

	}

	public String toString() {
		String ret = "";

		ret += new XMLLine(ACHistory.AC_KEY, XMLLine.OPENING_TAG_CATEGORY);
		ret += GUIUtilities.CRLF;

		if (selectedAC != null) {
			ret += new XMLLine(ACHistory.SELECTED_AC_KEY,
					XMLLine.OPENING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;

			ret += selectedAC.toString();
			ret += GUIUtilities.CRLF;

			ret += new XMLLine(ACHistory.SELECTED_AC_KEY,
					XMLLine.CLOSING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;
		}
		if (openedACs != null) {
			ret += new XMLLine(ACHistory.OPENED_ACS_KEY,
					XMLLine.OPENING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;

			ret += openedACs.toString();
			ret += GUIUtilities.CRLF;

			ret += new XMLLine(ACHistory.OPENED_ACS_KEY,
					XMLLine.CLOSING_TAG_CATEGORY);
			ret += GUIUtilities.CRLF;
		}

		ret += new XMLLine(ACHistory.AC_KEY, XMLLine.CLOSING_TAG_CATEGORY);
		ret += GUIUtilities.CRLF;

		return ret;
	}

	/**
	 * @return Returns the openedACs.
	 */
	public LimitedACStack getOpenedACs() {
		return openedACs;
	}

	/**
	 * @param openedACs
	 *            The openedACs to set.
	 */
	public void setOpenedACs(LimitedACStack openedACs) {
		this.openedACs = openedACs;
	}
}
