// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/MatchContextAttributesAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class MatchContextAttributesAction.
// (Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: MatchContextAttributesAction.java,v $
// Revision 1.4 2006/06/28 12:44:51 ounsy
// minor changes
//
// Revision 1.3 2006/04/10 08:46:54 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.2 2005/12/14 16:04:07 ounsy
// minor changes
//
// Revision 1.1 2005/12/14 14:07:17 ounsy
// first commit including the new "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2 2005/08/22 11:58:33 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.Action;

import fr.soleil.archiving.gui.exceptions.FieldFormatException;
import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.containers.context.ContextAttributesPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;

/**
 * Filters the current context attributes, keeping only those matching the
 * pattern.
 * <UL>
 * <LI>gets the current context
 * <LI>gets the pattern from the display area and builds a Criterions object
 * with it.
 * <LI>loads the context's attributes for this Criterions
 * <LI>display the new list of attributes
 * <LI>logs the action's success or failure
 * </UL>
 * 
 * @author CLAISSE
 */
public class MatchContextAttributesAction extends BensikinAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = 3076513480943874137L;
    private static MatchContextAttributesAction instance;

    /**
     * Standard action constructor that sets the action's name.
     * 
     * @param name
     *            The action name
     */
    private MatchContextAttributesAction(final String name) {
	putValue(Action.NAME, name);

	final Context selectedContext = Context.getSelectedContext();
	if (selectedContext == null) {
	    setEnabled(false);
	}
	/*
	 * else { this.setEnabled( true ); }
	 */
    }

    public static MatchContextAttributesAction getInstance(final String name) {
	if (instance == null) {
	    instance = new MatchContextAttributesAction(name);
	}

	return instance;
    }

    public static MatchContextAttributesAction getInstance() {
	return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent arg0) {
	final String pattern = ContextAttributesPanel.getInstance().getRightTreeBox().getRegExp();
	final Context selectedContext = Context.getSelectedContext();
	if (selectedContext == null) {
	    return;
	}

	Criterions searchCriterions = null;
	try {
	    searchCriterions = getAttributesSearchCriterions(pattern);
	} catch (final FieldFormatException e1) {
	    e1.printStackTrace();
	    return;
	}
	try {
	    selectedContext.loadAttributes(searchCriterions);
	    final String msg = Messages.getLogMessage("LOAD_CONTEXT_ATTRIBUTES_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final Exception e) {
	    final String msg = Messages.getLogMessage("LOAD_CONTEXT_ATTRIBUTES_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    return;
	}

	if (selectedContext.getContextAttributes() != null) {
	    selectedContext.getContextAttributes().push();
	}

	ContextAttributesTree.getInstance().expandAll1Level(true);
    }

    /**
     * Builds a usable Criterions object from the user defined string pattern.
     * 
     * @param pattern
     *            The user defined string pattern
     * @return The built Criterions object
     */
    static Criterions getAttributesSearchCriterions(final String pattern)
	    throws FieldFormatException {
	final Criterions ret = new Criterions();
	Condition cond;

	final StringTokenizer st = new StringTokenizer(pattern, GUIUtilities.TANGO_DELIM);
	// int nbOfTokens = st.countTokens();

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
	    // do nothing, not a problem (the user doesn't have to type up to
	    // attribute level)
	}

	cond = new Condition(GlobalConst.TAB_DEF[4], GlobalConst.OP_EQUALS, domain);
	ret.addCondition(cond);

	cond = new Condition(GlobalConst.TAB_DEF[5], GlobalConst.OP_EQUALS, family);
	ret.addCondition(cond);

	cond = new Condition(GlobalConst.TAB_DEF[6], GlobalConst.OP_EQUALS, member);
	ret.addCondition(cond);

	cond = new Condition(GlobalConst.TAB_DEF[7], GlobalConst.OP_EQUALS, attribute);
	ret.addCondition(cond);

	return ret;
    }
}
