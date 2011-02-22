// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/AbsRefreshAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AbsRefreshAction.
// (Pigeon Xavier) - 03 octobre 2007
//
// $Author: soleilarc $
//
// $Revision: 1.1 $
//
// $Log: AbsRefreshAction.java,v $
// Revision 1.1 2007/10/03 16:01:07 soleilarc
// Author: XP
// Mantis bug ID: 6594
// Comment: Add this abstract class between the BensikinAction class and the
// FilterSnapshotsAction and LaunchSnapshotAction classes, to be inherited by
// the 2 last ones. Thanks to AbsRefreshAction, it is possible to refresh the
// snapshot list.
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================

package fr.soleil.bensikin.actions.context;

import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.exceptions.FieldFormatException;
import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.archiving.gui.tools.DateUtils;
import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.components.snapshot.list.SnapshotListTable;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.containers.snapshot.SnapshotFilterPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.snapArchivingApi.SnapExtractorApi.tools.Tools;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.DateUtil;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public abstract class AbsRefreshAction extends BensikinAction {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = 8414487313593601279L;

    /**
     * Verifies the fields validity.
     * 
     * @param source
     *            The dialog to get the fields values from
     */
    private void verifyFields(final SnapshotFilterPanel source) throws FieldFormatException {
	final String id = source.getTextId().getText();
	if (id != null && !id.trim().equals("")) {
	    try {
		// int i =
		Integer.parseInt(id);
	    } catch (final NumberFormatException e) {
		throw new FieldFormatException(FieldFormatException.FILTER_SNAPSHOTS_ID);
	    }
	}

	final String startTime = source.getTextStartTime().getText();
	if (startTime != null && !startTime.trim().equals("")) {
	    DateUtils.stringToTimestamp(startTime, true,
		    FieldFormatException.FILTER_SNAPSHOTS_START_TIME);
	}

	final String endTime = source.getTextEndTime().getText();
	if (endTime != null && !endTime.trim().equals("")) {
	    DateUtils.stringToTimestamp(endTime, true,
		    FieldFormatException.FILTER_SNAPSHOTS_START_TIME);
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
	String _text = text;
	// Date Formating
	if (GlobalConst.TAB_SNAP[2].equals(id_field_key2)) {
	    if (!"".equals(_text)) {
		try {
		    final long milli = DateUtil.stringToMilli(_text);
		    final String date = Tools.formatDate(milli);
		    _text = date;
		} catch (final SnapshotingException e) {
		    e.printStackTrace();
		}
	    }
	}
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

    /**
     * Returns a Criterions object built from the current values of fields.
     * 
     * @return The Criterions object built from the current values of fields
     */
    private Criterions getSnapshotsSearchCriterions() {
	final SnapshotFilterPanel source = SnapshotFilterPanel.getInstance();
	// Context selectedContext = Context.getSelectedContext();
	// ContextData selectedContextData = selectedContext.getContextData();

	try {
	    verifyFields(source);
	} catch (final FieldFormatException e) {
	    String msg = "ERROR";
	    final String title = Messages.getLogMessage("FILTER_SNAPSHOTS_FIELD_ERROR");

	    switch (e.getCode()) {
	    case FieldFormatException.FILTER_SNAPSHOTS_ID:
		msg = Messages.getLogMessage("FILTER_SNAPSHOTS_FIELD_ERROR_ID");
		break;

	    case FieldFormatException.FILTER_SNAPSHOTS_START_TIME:
		msg = Messages.getLogMessage("FILTER_SNAPSHOTS_FIELD_ERROR_START_TIME");
		break;

	    case FieldFormatException.FILTER_SNAPSHOTS_END_TIME:
		msg = Messages.getLogMessage("FILTER_SNAPSHOTS_FIELD_ERROR_END_TIME");
		break;
	    }

	    JOptionPane.showMessageDialog(BensikinFrame.getInstance(), msg, title,
		    JOptionPane.ERROR_MESSAGE);
	    return null;
	}

	final Criterions ret = new Criterions();
	Condition cond;

	cond = getCondition(source.getSelectId().getSelectedItem(), source.getTextId().getText(),
		GlobalConst.TAB_SNAP[0]);
	ret.addCondition(cond);

	cond = getCondition(source.getSelectStartTime().getSelectedItem(), source
		.getTextStartTime().getText(), GlobalConst.TAB_SNAP[2]);
	ret.addCondition(cond);

	cond = getCondition(source.getSelectEndTime().getSelectedItem(), source.getTextEndTime()
		.getText(), GlobalConst.TAB_SNAP[2]);
	ret.addCondition(cond);

	cond = getCondition(source.getSelectComment().getSelectedItem(), source.getTextComment()
		.getText(), GlobalConst.TAB_SNAP[3]);
	ret.addCondition(cond);

	return ret;
    }

    public void refreshSnapList() {
	final Context selectedContext = Context.getSelectedContext();
	final Criterions searchCriterions = getSnapshotsSearchCriterions();
	if (searchCriterions == null) {
	    // System.out.println("FilterSnapshotsAction/actionPerformed/searchCriterions==NULL");
	    return;
	}

	final SnapshotListTableModel modelToUpdate = (SnapshotListTableModel) SnapshotListTable
		.getInstance().getModel();
	if (selectedContext != null) {
	    try {
		selectedContext.loadSnapshots(searchCriterions);
		modelToUpdate.updateList(selectedContext.getSnapshots());

		final String msg = Messages.getLogMessage("FILTER_SNAPSHOTS_ACTION_OK");
		logger.trace(ILogger.LEVEL_DEBUG, msg);
	    } catch (final SnapshotingException e) {
		String msg;
		if (e.computeIsDueToATimeOut()) {
		    msg = Messages.getLogMessage("FILTER_SNAPSHOTS_ACTION_TIMEOUT");
		} else {
		    msg = Messages.getLogMessage("FILTER_SNAPSHOTS_ACTION_KO");
		}
		logger.trace(ILogger.LEVEL_ERROR, msg);
		logger.trace(ILogger.LEVEL_ERROR, e);
	    } catch (final Exception e) {
		final String msg = Messages.getLogMessage("FILTER_SNAPSHOTS_ACTION_KO");
		logger.trace(ILogger.LEVEL_ERROR, msg);
		logger.trace(ILogger.LEVEL_ERROR, e);
	    }
	}
    }
}
