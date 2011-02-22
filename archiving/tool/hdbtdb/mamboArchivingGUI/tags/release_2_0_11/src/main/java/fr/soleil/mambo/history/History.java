// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/history/History.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class History.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: History.java,v $
// Revision 1.4 2007/02/01 14:15:38 pierrejoseph
// XmlHelper reorg
//
// Revision 1.3 2006/08/23 10:03:28 ounsy
// avoided a NullPointerException
//
// Revision 1.2 2005/11/29 18:28:12 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
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
package fr.soleil.mambo.history;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.components.archiving.LimitedACStack;
import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.components.view.LimitedVCStack;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.history.archiving.ACHistory;
import fr.soleil.mambo.history.view.VCHistory;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class History {

    // MULTI-CONF
    private ACHistory          aCHistory;
    private VCHistory          vCHistory;

    private static History     currentHistory;
    public static final String XML_KEY = "history";
    public static final String ID_KEY  = "id";

    /**
     * @param _snapshotHistory
     * @param _contextHistory
     */
    public History(ACHistory _aCHistory, VCHistory _vCHistory) {
        this.aCHistory = _aCHistory;
        this.vCHistory = _vCHistory;
    }

    /**
     * @return
     * @throws SnapshotingException
     * @throws Exception
     *             8 juil. 2005
     */
    /*
     * public ArchivingConfiguration loadSelectedAC () { //nothing to do }
     */

    /**
     * @return 8 juil. 2005
     */
    public static History getCurrentHistory() {
        // build an history object representing the current state of the
        // application
        ArchivingConfiguration selectedAc = ArchivingConfiguration
                .getSelectedArchivingConfiguration();
        ViewConfiguration selectedVc = ViewConfigurationBeanManager
                .getInstance().getSelectedConfiguration();

        ACHistory _aCHistory = new ACHistory(selectedAc);
        VCHistory _vCHistory = new VCHistory(selectedVc);

        OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
        LimitedVCStack _VCstack = openedVCComboBox.getVCElements();
        _vCHistory.setOpenedVCs(_VCstack);
        // System.out.println ("History/getCurrentHistory/"+_stack.size ());

        if (Mambo.hasACs()) {
            OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
            LimitedACStack _ACstack = openedACComboBox.getACElements();
            _aCHistory.setOpenedACs(_ACstack);
        }

        currentHistory = new History(_aCHistory, _vCHistory);
        return currentHistory;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String ret = "";

        ret += new XMLLine(XML_KEY, XMLLine.OPENING_TAG_CATEGORY);
        ret += GUIUtilities.CRLF;

        if (this.aCHistory != null) {
            ret += this.aCHistory.toString();
            ret += GUIUtilities.CRLF;
        }

        if (this.vCHistory != null) {
            ret += this.vCHistory.toString();
            ret += GUIUtilities.CRLF;
        }

        ret += new XMLLine(XML_KEY, XMLLine.CLOSING_TAG_CATEGORY);

        // ret = XMLUtils.replaceXMLChars ( ret );

        return ret;
    }

    /**
     * 8 juil. 2005
     */
    public void push() {
        if (this.aCHistory != null) {
            this.aCHistory.push();
        }
        if (this.vCHistory != null) {
            this.vCHistory.push();
        }
    }

    /**
     * @param currentHistory
     *            The currentHistory to set.
     */
    public static void setCurrentHistory(History currentHistory) {
        History.currentHistory = currentHistory;
    }
}
