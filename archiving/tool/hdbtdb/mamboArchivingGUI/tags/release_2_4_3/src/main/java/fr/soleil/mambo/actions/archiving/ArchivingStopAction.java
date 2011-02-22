// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/ArchivingStopAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ArchivingStopAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.12 $
//
// $Log: ArchivingStopAction.java,v $
// Revision 1.12 2007/08/24 12:51:43 ounsy
// WaitingDialog should allways close now
//
// Revision 1.11 2006/12/06 12:34:56 ounsy
// better error and waiting dialog management
//
// Revision 1.10 2006/11/20 09:36:19 ounsy
// the wrong error message was called when a stop exception was catched (start
// )instead of stop
//
// Revision 1.9 2006/10/11 15:02:31 ounsy
// minor changes
//
// Revision 1.8 2006/10/09 08:35:59 ounsy
// Waiting Dialog updated with the right message
//
// Revision 1.7 2006/10/03 12:33:09 ounsy
// Waiting dialog added for start and stop actions
//
// Revision 1.6 2006/09/26 15:54:23 ounsy
// added timeOut management
//
// Revision 1.5 2006/09/22 09:34:41 ounsy
// refactoring du package mambo.datasources.db
//
// Revision 1.4 2006/08/23 10:01:32 ounsy
// some optimizations with less tree model reloading
//
// Revision 1.3 2006/03/02 15:00:40 ounsy
// refreshing view icons on start and stop
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.AttributesArchivingException;
import fr.soleil.mambo.components.archiving.ACAttributesRecapTree;
import fr.soleil.mambo.containers.sub.dialogs.WaitingDialog;
import fr.soleil.mambo.containers.view.ViewPanel;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.Messages;

public class ArchivingStopAction extends AbstractAction {
    final static ILogger logger = GUILoggerFactory.getLogger();

    private static final long serialVersionUID = 6720336971847812533L;

    public ArchivingStopAction(final String name) {
	super.putValue(Action.NAME, name);
	super.putValue(Action.SHORT_DESCRIPTION, name);
    }

    @Override
    public void actionPerformed(final ActionEvent actionEvent) {
	WaitingDialog.changeFirstMessage(Messages.getMessage("DIALOGS_WAITING_STOPPING_TITLE"));
	WaitingDialog.openInstance();
	// System.out.println ("ArchivingStopAction");
	final IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();

	final ArchivingConfiguration selectedArchivingConfiguration = ArchivingConfiguration
		.getSelectedArchivingConfiguration();
	try {
	    manager.stopArchiving(selectedArchivingConfiguration);

	    final String msg = Messages.getLogMessage("STOP_ARCHIVING_ACTION_OK");
	    logger.trace(ILogger.LEVEL_DEBUG, msg);

	} catch (final ArchivingException ae) {
	    String msg;
	    int level;

	    if (ae.isDueToATimeOut()) {
		msg = Messages.getLogMessage("STOP_ARCHIVING_ACTION_KO_TIMEOUT");
		level = ILogger.LEVEL_WARNING;
	    } else {
		msg = Messages.getLogMessage("STOP_ARCHIVING_ACTION_KO");
		level = ILogger.LEVEL_ERROR;
	    }

	    logger.trace(level, msg);
	    logger.trace(level, ae);
	    if (ae instanceof AttributesArchivingException) {
		final AttributesArchivingException aae = (AttributesArchivingException) ae;
		if (aae.getFaultingAttributes().size() > 0) {
		    logger.trace(level, Messages
			    .getLogMessage("STOP_ARCHIVING_ACTION_EXCEPTION_TRACE_MESSAGE"));
		    for (int i = 0; i < aae.getFaultingAttributes().size(); i++) {
			StringBuffer buffer = new StringBuffer("\t");
			buffer
				.append(Messages
					.getLogMessage("STOP_ARCHIVING_ACTION_EXCEPTION_TRACE_MESSAGE_ATTRIBUTE"));
			buffer.append(i + 1).append(":");
			buffer
				.append("\n\t\t")
				.append(
					Messages
						.getLogMessage("STOP_ARCHIVING_ACTION_EXCEPTION_TRACE_MESSAGE_ATTRIBUTE_NAME"));
			buffer.append(aae.getFaultingAttributes().get(i).getName());
			buffer
				.append("\n\t\t")
				.append(
					Messages
						.getLogMessage("STOP_ARCHIVING_ACTION_EXCEPTION_TRACE_MESSAGE_ATTRIBUTE_REASON"));
			buffer.append(aae.getFaultingAttributes().get(i).getCause());
			logger.trace(level, buffer.toString());
			buffer = null;
		    }
		}
	    }
	    WaitingDialog.closeInstance();
	    return;
	} catch (final Throwable e) {
	    final String msg = Messages.getLogMessage("STOP_ARCHIVING_ACTION_KO");
	    logger.trace(ILogger.LEVEL_ERROR, msg);
	    logger.trace(ILogger.LEVEL_ERROR, e);
	    WaitingDialog.closeInstance();
	    return;
	} finally {
	    if (ACAttributesRecapTree.getInstance() != null) {
		ACAttributesRecapTree.getInstance().revalidate();
		ACAttributesRecapTree.getInstance().repaint();
	    }
	    // if (VCAttributesRecapTree.getInstance() != null)
	    // {
	    // VCAttributesRecapTree.getInstance().revalidate();
	    // VCAttributesRecapTree.getInstance().repaint();
	    // }
	    ViewPanel.getInstance().repaint();
	    WaitingDialog.closeInstance();
	}
    }
}
