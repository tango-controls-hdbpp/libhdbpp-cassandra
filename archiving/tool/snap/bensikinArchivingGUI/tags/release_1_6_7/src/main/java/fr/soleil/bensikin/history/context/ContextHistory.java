//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/history/context/ContextHistory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextHistory.
//						(Claisse Laurent) - 8 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: ContextHistory.java,v $
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:39  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.history.context;

import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.history.History;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.xml.XMLLine;

/**
 * A model for the historic of opened/selected contexts. Contains references to both.
 * 
 * @author CLAISSE
 */
public class ContextHistory {
    private SelectedContextRef selectedContextRef;
    private OpenedContextRef[] openedContextRefs;

    /**
     * Build directly from a reference to the selected context, and references to the opened
     * contexts.
     * 
     * @param _selectedContextRef A reference to the selected context
     * @param _openedContextRefs Reference to the opened contexts
     */
    public ContextHistory(SelectedContextRef _selectedContextRef,
            OpenedContextRef[] _openedContextRefs) {
        this.selectedContextRef = _selectedContextRef;
        this.openedContextRefs = _openedContextRefs;
    }

    /**
     * Build indirectly from the selected context and the opened contexts. The references of those
     * are extracted and used.
     * 
     * @param selectedContext The selected context
     * @param openedContexts The opened contexts
     */
    public ContextHistory(Context selectedContext, Context[] openedContexts) {
        if (selectedContext != null) {
            if (selectedContext.getContextData() != null) {
                if (!selectedContext.isContextFile()) {
                    this.selectedContextRef = new SelectedContextRef(selectedContext);
                }
            }
        }

        if (openedContexts != null) {
            this.openedContextRefs = new OpenedContextRef[openedContexts.length];

            for (int i = 0; i < openedContexts.length; i++) {
                this.openedContextRefs[i] = new OpenedContextRef(openedContexts[i]);
            }
        }

    }

    /**
     * Returns a XML representation of the context history
     * 
     * @return a XML representation of the context history
     */
    public String toString() {
        String ret = "";

        ret += new XMLLine(History.CONTEXTS_KEY, XMLLine.OPENING_TAG_CATEGORY);
        ret += GUIUtilities.CRLF;

        if (selectedContextRef != null) {
            ret += selectedContextRef.toString();
            ret += GUIUtilities.CRLF;
        }

        if (openedContextRefs != null) {
            ret += new XMLLine(History.OPENED_CONTEXTS_KEY, XMLLine.OPENING_TAG_CATEGORY);
            ret += GUIUtilities.CRLF;

            for (int i = 0; i < openedContextRefs.length; i++) {
                ret += openedContextRefs[i].toString();
                ret += GUIUtilities.CRLF;
            }

            ret += new XMLLine(History.OPENED_CONTEXTS_KEY, XMLLine.CLOSING_TAG_CATEGORY);
            ret += GUIUtilities.CRLF;
        }

        ret += new XMLLine(History.CONTEXTS_KEY, XMLLine.CLOSING_TAG_CATEGORY);
        ret += GUIUtilities.CRLF;

        return ret;
    }

    /**
     * @return Returns the openedContextRefs.
     */
    public OpenedContextRef[] getOpenedContextRefs() {
        return openedContextRefs;
    }

    /**
     * @param openedContextRefs The openedContextRefs to set.
     */
    public void setOpenedContextRefs(OpenedContextRef[] openedContextRefs) {
        this.openedContextRefs = openedContextRefs;
    }

    /**
     * @return Returns the selectedContextRef.
     */
    public SelectedContextRef getSelectedContextRef() {
        return selectedContextRef;
    }

    /**
     * @param selectedContextRef The selectedContextRef to set.
     */
    public void setSelectedContextRef(SelectedContextRef selectedContextRef) {
        this.selectedContextRef = selectedContextRef;
    }
}
