//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/AbsRefreshAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AbsRefreshAction.
//						(Pigeon Xavier) - 03 octobre 2007
//
// $Author: soleilarc $
//
// $Revision: 1.1 $
//
// $Log: AbsRefreshAction.java,v $
// Revision 1.1  2007/10/03 16:01:07  soleilarc
// Author: XP
// Mantis bug ID: 6594
// Comment: Add this abstract class between the BensikinAction class and the FilterSnapshotsAction and LaunchSnapshotAction classes, to be inherited by the 2 last ones. Thanks to AbsRefreshAction, it is possible to refresh the snapshot list.
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.bensikin.actions.context;

import javax.swing.JOptionPane;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.actions.exceptions.FieldFormatException;
import fr.soleil.bensikin.components.OperatorsList;
import fr.soleil.bensikin.components.snapshot.list.SnapshotListTable;
import fr.soleil.bensikin.containers.snapshot.SnapshotFilterPanel;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.models.SnapshotListTableModel;
import fr.soleil.bensikin.tools.BensikinDate;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapExtractorApi.tools.Tools;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.DateUtil;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

abstract public class AbsRefreshAction extends BensikinAction {
	/**
	 * Verifies the fields validity.
	 *
	 * @param source The dialog to get the fields values from
	 */
	private void verifyFields(SnapshotFilterPanel source) throws FieldFormatException
	{
		String id = source.textId.getText();
		if ( id != null && !id.trim().equals("") )
		{
			try
			{
				//int i = 
                Integer.parseInt(id);
			}
			catch ( NumberFormatException e )
			{
				throw new FieldFormatException(FieldFormatException.FILTER_SNAPSHOTS_ID);
			}
		}

		String startTime = source.textStartTime.getText();
		if ( startTime != null && !startTime.trim().equals("") )
		{
			BensikinDate.stringToTimestamp(startTime , true , FieldFormatException.FILTER_SNAPSHOTS_START_TIME);
		}

		String endTime = source.textEndTime.getText();
		if ( endTime != null && !endTime.trim().equals("") )
		{
			BensikinDate.stringToTimestamp(endTime , true , FieldFormatException.FILTER_SNAPSHOTS_START_TIME);
		}

	}
	
	/**
	 * If both operator and threshold value are filled, builds a Condition from them. Otherwise returns null.
	 *
	 * @param selectedItem  The operator
	 * @param text          The value
	 * @param id_field_key2 The field's id
	 * @return The resulting Condition or null
	 */
	public Condition getCondition(Object selectedItem , String text , String id_field_key2)
	{
		String _selectedItem = ( String ) selectedItem;
		String _text = ( String ) text;
		// Date Formating
		if (GlobalConst.TAB_SNAP[2].equals(id_field_key2)) {
            if (!"".equals(_text)) {
                try {
                    long milli = DateUtil.stringToMilli(_text);
                    String date = Tools.formatDate(milli);
                    _text = date;
                }
                catch (SnapshotingException e) {
                    e.printStackTrace();
                }
            }
        }
		boolean isACriterion = true;

		if ( selectedItem == null || _selectedItem.equals(OperatorsList.NO_SELECTION) )
		{
			isACriterion = false;
		}

		if ( _text == null || _text.trim().equals("") )
		{
			isACriterion = false;
		}

		if ( isACriterion )
		{
			return new Condition(id_field_key2 , _selectedItem , _text.trim());
		}

		return null;
	}
	
	/**
	 * Returns a Criterions object built from the current values of fields.
	 *
	 * @return The Criterions object built from the current values of fields
	 */
	private Criterions getSnapshotsSearchCriterions()
	{
		SnapshotFilterPanel source = SnapshotFilterPanel.getInstance();
		//Context selectedContext = Context.getSelectedContext();
		//ContextData selectedContextData = selectedContext.getContextData();

		try
		{
			verifyFields(source);
		}
		catch ( FieldFormatException e )
		{
			String msg = "ERROR";
			String title = Messages.getLogMessage("FILTER_SNAPSHOTS_FIELD_ERROR");

			switch ( e.getCode() )
			{
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

			JOptionPane.showMessageDialog(null , msg , title , JOptionPane.ERROR_MESSAGE);
			return null;
		}

		Criterions ret = new Criterions();
		Condition cond;

		cond = getCondition(source.selectId.getSelectedItem() , source.textId.getText() , GlobalConst.TAB_SNAP[ 0 ]);
		ret.addCondition(cond);

		cond = getCondition(source.selectStartTime.getSelectedItem() , source.textStartTime.getText() , GlobalConst.TAB_SNAP[ 2 ]);
		ret.addCondition(cond);

		cond = getCondition(source.selectEndTime.getSelectedItem() , source.textEndTime.getText() , GlobalConst.TAB_SNAP[ 2 ]);
		ret.addCondition(cond);

		cond = getCondition(source.selectComment.getSelectedItem() , source.textComment.getText() , GlobalConst.TAB_SNAP[ 3 ]);
		ret.addCondition(cond);

		return ret;
	}
	
	public void RefreshSnapList()
	{
		Context selectedContext = Context.getSelectedContext();
		Criterions searchCriterions = getSnapshotsSearchCriterions();
		if ( searchCriterions == null )
		{
			//System.out.println("FilterSnapshotsAction/actionPerformed/searchCriterions==NULL");
			return;
		}

		SnapshotListTableModel modelToUpdate = ( SnapshotListTableModel ) SnapshotListTable.getInstance().getModel();
		ILogger logger = LoggerFactory.getCurrentImpl();

		try
		{
			selectedContext.loadSnapshots(searchCriterions);
			modelToUpdate.updateList(selectedContext.getSnapshots());

			String msg = Messages.getLogMessage("FILTER_SNAPSHOTS_ACTION_OK");
			logger.trace(ILogger.LEVEL_DEBUG , msg);
		}
		catch ( Exception e )
		{
			String msg = Messages.getLogMessage("FILTER_SNAPSHOTS_ACTION_KO");
			logger.trace(ILogger.LEVEL_ERROR , msg);
			logger.trace(ILogger.LEVEL_ERROR , e);
			return;
		}
	}
}
