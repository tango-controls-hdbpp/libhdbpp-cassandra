// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/SearchContextsAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SearchContextsAction.
// (Claisse Laurent) - 15 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: SearchContextsAction.java,v $
// Revision 1.3 2006/06/28 12:44:51 ounsy
// minor changes
//
// Revision 1.2 2006/04/10 08:46:54 ounsy
// Bensikin action now all inherit from BensikinAction for easy rights
// management
//
// Revision 1.1 2005/12/14 14:07:17 ounsy
// first commit including the new "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2 2005/08/22 11:58:34 chinkumo
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

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.exceptions.FieldFormatException;
import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.archiving.gui.tools.DateUtils;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.components.context.list.ContextListTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.sub.dialogs.open.SearchContextsInDBDialog;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.data.context.ContextData;
import fr.soleil.bensikin.models.ContextListTableModel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;

/**
 * Looks up contexts according to the filtering criterions filled in the search
 * popup.
 * <UL>
 * <LI>Gets the SearchContextsInDBDialog instance where the action was called
 * from
 * <LI>If the action is called from the reset button, resets the criterions
 * fields and returns
 * <LI>Builds a Criterions object with the criterions fields; generates an error
 * message if the fields are incorrect
 * <LI>Uses Context.loadContexts to load the list of contexts for those fields
 * <LI>Logs the action's success or failure
 * <LI>Updates the ContextListTable instance's model
 * <LI>Closes the search dialog
 * </UL>
 * 
 * @author CLAISSE
 */
public class SearchContextsAction extends BensikinAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private final SearchContextsInDBDialog dialogFrom;

    /**
     * Standard action constructor that sets the action's name, plus initializes
     * the reference to the popup to close on <code>actionPerformed</code>
     * 
     * @param name
     *            The action name
     * @param _dialogFrom
     *            The dialog where the user filled the filtering criterions
     */
    public SearchContextsAction(final String name, final SearchContextsInDBDialog _dialogFrom) {
	putValue(Action.NAME, name);
	dialogFrom = _dialogFrom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
	final SearchContextsInDBDialog source = SearchContextsInDBDialog.getInstance();
	final JButton buttonFrom = (JButton) actionEvent.getSource();
	if (buttonFrom == source.getResetButton()) {
	    source.resetFields();
	    return;
	}

	final Criterions searchCriterions = getContextsSearchCriterions();
	if (searchCriterions == null) {
	    return;
	}

	ContextData[] contexts = null;

	try {
	    contexts = Context.loadContexts(searchCriterions);

	    final String msg = Messages.getLogMessage("LOAD_CONTEXTS_ACTION_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);
	} catch (final Exception e) {
	    final String msg = Messages.getLogMessage("LOAD_CONTEXTS_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    return;
	}

	final ContextListTable tableToUpdate = ContextListTable.getInstance();
	final ContextListTableModel modelToUpdate = (ContextListTableModel) tableToUpdate
		.getModel();
	modelToUpdate.setRows(contexts);

	dialogFrom.setVisible(false);
    }

    /**
     * Returns a Criterions object built from the current values of fields.
     * 
     * @return The Criterions object built from the current values of fields
     */
    private Criterions getContextsSearchCriterions() {
	final SearchContextsInDBDialog source = SearchContextsInDBDialog.getInstance();

	try {
	    verifyFields(source);
	} catch (final FieldFormatException e) {
	    String msg = "ERROR";
	    final String title = Messages.getLogMessage("SEARCH_CONTEXTS_FIELD_ERROR");

	    switch (e.getCode()) {
	    case FieldFormatException.SEARCH_CONTEXTS_ID:
		msg = Messages.getLogMessage("SEARCH_CONTEXTS_FIELD_ERROR_ID");
		break;

	    case FieldFormatException.SEARCH_CONTEXTS_START_TIME:
		msg = Messages.getLogMessage("SEARCH_CONTEXTS_FIELD_ERROR_START_TIME");
		break;

	    case FieldFormatException.SEARCH_CONTEXTS_END_TIME:
		msg = Messages.getLogMessage("SEARCH_CONTEXTS_FIELD_ERROR_END_TIME");
		break;
	    }

	    JOptionPane.showMessageDialog(BensikinFrame.getInstance(), msg, title,
		    JOptionPane.ERROR_MESSAGE);
	    return null;
	}

	final Criterions ret = new Criterions();
	Condition cond;

	cond = getCondition(source.selectId.getSelectedItem(), source.textId.getText(),
		GlobalConst.TAB_CONTEXT[0]);
	ret.addCondition(cond);

	cond = getCondition(source.selectStartTime.getSelectedItem(), source.textStartTime
		.getText(), GlobalConst.TAB_CONTEXT[1]);
	ret.addCondition(cond);

	cond = getCondition(source.selectEndTime.getSelectedItem(), source.textEndTime.getText(),
		GlobalConst.TAB_CONTEXT[1]);
	ret.addCondition(cond);

	cond = getCondition(source.selectName.getSelectedItem(), source.textName.getText(),
		GlobalConst.TAB_CONTEXT[2]);
	ret.addCondition(cond);

	cond = getCondition(source.selectAuthor.getSelectedItem(), source.textAuthor.getText(),
		GlobalConst.TAB_CONTEXT[3]);
	ret.addCondition(cond);

	cond = getCondition(source.selectReason.getSelectedItem(), source.textReason.getText(),
		GlobalConst.TAB_CONTEXT[4]);
	ret.addCondition(cond);

	cond = getCondition(source.selectDescription.getSelectedItem(), source.textDescription
		.getText(), GlobalConst.TAB_CONTEXT[5]);
	ret.addCondition(cond);

	return ret;
    }

    /**
     * Verifies the fields validity.
     * 
     * @param source
     *            The dialog to get the fields values from
     */
    private void verifyFields(final SearchContextsInDBDialog source) throws FieldFormatException {
	final String id = source.textId.getText();
	if (id != null && !id.trim().equals("")) {
	    try {
		// int i =
		Integer.parseInt(id);
	    } catch (final NumberFormatException e) {
		throw new FieldFormatException(FieldFormatException.SEARCH_CONTEXTS_ID);
	    }
	}

	final String startTime = source.textStartTime.getText();
	if (startTime != null && !startTime.trim().equals("")) {
	    DateUtils.stringToTimestamp(startTime, false,
		    FieldFormatException.SEARCH_CONTEXTS_START_TIME);
	}

	final String endTime = source.textEndTime.getText();
	if (endTime != null && !endTime.trim().equals("")) {
	    DateUtils.stringToTimestamp(startTime, false,
		    FieldFormatException.SEARCH_CONTEXTS_END_TIME);
	}
    }

    /**
     * If both operator and threshold value are filled, builds a Condition from
     * them. Otherwise returns null.
     * 
     * @param selectedItem
     *            The operator
     * @param text
     *            The value
     * @param id_field_key2
     *            The field's id
     * @return The resulting Condition or null
     */
    public Condition getCondition(final Object selectedItem, final String text,
	    final String id_field_key2) {
	final String _selectedItem = (String) selectedItem;
	final String _text = text;
	boolean isACriterion = true;

	if (selectedItem == null || _selectedItem.equals(OperatorsList.NO_SELECTION)) {
	    isACriterion = false;
	}

	if (_text == null || _text.trim().equals("")) {
	    isACriterion = false;
	}

	if (isACriterion) {
	    return new Condition(id_field_key2, _selectedItem, _text.trim());
	}

	return null;
    }

}
