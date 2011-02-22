//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/MatchCAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MatchCAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: MatchCAttributesAction.java,v $
// Revision 1.4  2006/09/20 12:49:11  ounsy
// minor changes
//
// Revision 1.3  2006/05/19 14:57:17  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
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
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Condition;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;

public class MatchCAttributesAction extends AbstractAction {

    /**
     * @param name
     */
    public MatchCAttributesAction(final String name) {
	putValue(Action.NAME, name);

	setEnabled(true);
    }

    /**
     * @param pattern
     * @return 11 juil. 2005
     */
    public static Criterions getAttributesSearchCriterions(final String pattern) // throws
    // FieldFormatException
    {
	final Criterions ret = new Criterions();
	Condition cond;

	final StringTokenizer st = new StringTokenizer(pattern, GUIUtilities.TANGO_DELIM);
	/* int nbOfTokens = st.countTokens(); */

	String domain = GUIUtilities.TANGO_JOKER;
	String family = GUIUtilities.TANGO_JOKER;
	String member = GUIUtilities.TANGO_JOKER;
	String attribute = GUIUtilities.TANGO_JOKER;

	try {
	    domain = st.nextToken();
	    family = st.nextToken();
	    member = st.nextToken();
	    attribute = st.nextToken();
	} catch (final NoSuchElementException e) {
	    // do nothing
	}

	cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_DOMAIN, ITangoManager.OP_EQUALS, domain);
	ret.addCondition(cond);

	cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_FAMILY, ITangoManager.OP_EQUALS, family);
	ret.addCondition(cond);

	cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_MEMBER, ITangoManager.OP_EQUALS, member);
	ret.addCondition(cond);

	cond = new Condition(ITangoManager.FIELD_ATTRIBUTE_NAME, ITangoManager.OP_EQUALS, attribute);
	ret.addCondition(cond);

	return ret;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent arg0) {
	// TODO Auto-generated method stub

    }

}
