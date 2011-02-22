//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/ACFinishAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ValidateACEditAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: ACFinishAction.java,v $
// Revision 1.5  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.4  2006/05/19 14:58:40  ounsy
// minor changes
//
// Revision 1.3  2006/02/24 12:15:44  ounsy
// small modifications
//
// Revision 1.2  2005/12/15 10:40:45  ounsy
// avoiding a null pointer exception
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
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
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.containers.archiving.dialogs.GeneralTab;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.models.ACAttributesTreeModel;
import fr.soleil.mambo.tools.Messages;

public class ACFinishAction extends AbstractAction {
    private static ACFinishAction instance = null;

    /**
     * @return
     */
    public static ACFinishAction getInstance(final String name) {
	if (instance == null) {
	    instance = new ACFinishAction(name);
	}

	return instance;
    }

    public static ACFinishAction getInstance() {
	return instance;
    }

    /**
     * @param name
     */
    private ACFinishAction(final String name) {
	putValue(Action.NAME, name);
	putValue(Action.SHORT_DESCRIPTION, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent actionEvent) {
	if (!verifyAC()) {
	    return;
	}
	final ACEditDialog dialog = ACEditDialog.getInstance();
	dialog.setVisible(false);

	final ArchivingConfiguration currentAC = ArchivingConfiguration
		.getCurrentArchivingConfiguration();
	if (currentAC.isNew()) {
	    currentAC.setCreationDate(GUIUtilities.now());
	}

	currentAC.setLastUpdateDate(GUIUtilities.now());

	final GeneralTab generalTab = GeneralTab.getInstance();
	currentAC.setName(generalTab.getName());
	currentAC.setModified(true);

	/*
	 * ArchivingConfiguration.setSelectedArchivingConfiguration( currentAC
	 * ); currentAC.push();
	 */
	ArchivingConfiguration ac = ArchivingConfiguration.getSelectedArchivingConfiguration();
	if (ac == null) {
	    ac = new ArchivingConfiguration();
	    ArchivingConfiguration.setSelectedArchivingConfiguration(ac);
	}

	ac.setData(currentAC.getData());
	ac.setAttributes(currentAC.getAttributes());
	ac.setModified(true);
	ac.push();

    }

    /**
     * @return
     */
    public boolean verifyAC() {
	return verifyAttributesAreSet() /* && verifyModes () */;
    }

    /**
     * @return
     */
    /*
     * private boolean verifyModes() { ArchivingConfiguration currentAC =
     * ArchivingConfiguration.getCurrentArchivingConfiguration();
     * 
     * if ( containsBadModes () ) { String msgTitle = Messages.getMessage(
     * "DIALOGS_EDIT_AC_BAD_MODES_CONFIRM_TITLE" ); String msgConfirm =
     * Messages.getMessage( "DIALOGS_EDIT_AC_BAD_MODES_CONFIRM_LABEL_1" );
     * msgConfirm += GUIUtilities.CRLF; msgConfirm += Messages.getMessage(
     * "DIALOGS_EDIT_AC_BAD_MODES_CONFIRM_LABEL_2" ); String msgCancel =
     * Messages.getMessage( "DIALOGS_EDIT_AC_BAD_MODES_CONFIRM_CANCEL" ); String
     * msgValidate = Messages.getMessage(
     * "DIALOGS_EDIT_AC_BAD_MODES_CONFIRM_VALIDATE" ); Object[] options =
     * {msgValidate, msgCancel};
     * 
     * int confirm = JOptionPane.showOptionDialog ( null , msgConfirm , msgTitle
     * , JOptionPane.DEFAULT_OPTION , JOptionPane.WARNING_MESSAGE , null ,
     * options , options[ 0 ] ); return confirm == JOptionPane.OK_OPTION; } else
     * { return true; } }
     */

    /**
     * @return
     */
    /*
     * private boolean containsBadModes() { ACAttributesTreeModel model =
     * ACAttributesTreeModel.getInstance(); Hashtable htAttr =
     * model.getAttributes(); if ( htAttr == null ) { return false; }
     * 
     * Enumeration enum = htAttr.keys(); while ( enum.hasMoreElements() ) {
     * String nextKey = ( String ) enum.nextElement();
     * ArchivingConfigurationAttribute nextValue = (
     * ArchivingConfigurationAttribute ) htAttr.get( nextKey );
     * 
     * ArchivingConfiguration currentAC =
     * ArchivingConfiguration.getCurrentArchivingConfiguration();
     * //System.out.println ( "CLA/currentAC--------------------------------" );
     * //System.out.println ( currentAC.toString() ); //System.out.println (
     * "CLA/currentAC--------------------------------" ); if (
     * !currentAC.containsAttribute( nextKey ) ) { return true; } nextValue =
     * currentAC.getAttribute( nextKey );
     * 
     * ArchivingConfigurationAttributeDBProperties prop; long exportPeriod = 0;
     * long keepingPeriod = 0; if ( currentAC.isHistoric() ) { prop =
     * nextValue.getProperties ().getHDBProperties (); } else { prop =
     * nextValue.getProperties ().getTDBProperties (); keepingPeriod =
     * nextValue.getProperties ().getTDBProperties ().getKeepingPeriod ();
     * exportPeriod = nextValue.getProperties ().getTDBProperties
     * ().getExportPeriod (); } ArchivingConfigurationMode [] modes =
     * prop.getModes (); if ( modes == null || modes.length == 0 ) { return
     * false; } for ( int i = 0 ; i < modes.length ; i ++ ) { Mode mode = modes
     * [ i ].getMode (); try { if ( ! currentAC.isHistoric() ) { mode.setTdbSpec
     * ( new TdbSpec ( exportPeriod , keepingPeriod ) ); } mode.checkMode (
     * currentAC.isHistoric () ); } catch ( Exception e ) { e.printStackTrace
     * (); System.out.println ( "CLA/ERR for/"+nextValue.getCompleteName () );
     * return true; } } } return false; }
     */

    /**
     * @return
     */
    private boolean verifyAttributesAreSet() {
	// ArchivingConfiguration currentAC =
	// ArchivingConfiguration.getCurrentArchivingConfiguration();

	if (containsNonSetAttributes()) {
	    final String msgTitle = Messages
		    .getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_NON_SET_CONFIRM_TITLE");
	    String msgConfirm = Messages
		    .getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_NON_SET_CONFIRM_LABEL_1");
	    msgConfirm += GUIUtilities.CRLF;
	    msgConfirm += Messages.getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_NON_SET_CONFIRM_LABEL_2");
	    final String msgCancel = Messages
		    .getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_NON_SET_CONFIRM_CANCEL");
	    final String msgValidate = Messages
		    .getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_NON_SET_CONFIRM_VALIDATE");
	    final Object[] options = { msgValidate, msgCancel };

	    final int confirm = JOptionPane.showOptionDialog(null, msgConfirm, msgTitle,
		    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
		    options[0]);
	    return confirm == JOptionPane.OK_OPTION;
	} else {
	    return true;
	}
    }

    /**
     * @return
     */
    private boolean containsNonSetAttributes() {
	final ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
	final TreeMap htAttr = model.getAttributes();
	if (htAttr == null) {
	    return false;
	}

	final Set keySet = htAttr.keySet();
	final Iterator keyIterator = keySet.iterator();
	while (keyIterator.hasNext()) {
	    final String nextKey = (String) keyIterator.next();
	    ArchivingConfigurationAttribute nextValue = (ArchivingConfigurationAttribute) htAttr
		    .get(nextKey);

	    final ArchivingConfiguration currentAC = ArchivingConfiguration
		    .getCurrentArchivingConfiguration();
	    if (!currentAC.containsAttribute(nextKey)) {
		return true;
	    }
	    nextValue = currentAC.getAttribute(nextKey);

	    if (nextValue.isEmpty()) {
		return true;
	    }
	}
	return false;
    }

}
