// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/SetAttributesGroupProperties.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class SetAttributesGroupProperties.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: SetAttributesGroupProperties.java,v $
// Revision 1.8 2006/12/07 16:45:39 ounsy
// removed keeping period
//
// Revision 1.7 2006/06/15 15:37:54 ounsy
// Takes into account the new dedicated archiver property
//
// Revision 1.6 2006/05/19 14:59:52 ounsy
// minor changes
//
// Revision 1.5 2006/03/27 14:06:02 ounsy
// added a global mode control before setting properties
//
// Revision 1.4 2006/03/20 10:34:27 ounsy
// removed useless logs
//
// Revision 1.3 2006/02/24 12:15:29 ounsy
// modified for HDB/TDB separation
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/19 08:00:22 chinkumo
// Miscellaneous changes...
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
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.mambo.components.archiving.ACAttributesPropertiesTree;
import fr.soleil.mambo.components.renderers.ACTreeRenderer;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;

public class SetAttributesGroupProperties extends AbstractAction {
    /**
     * @param name
     */
    public SetAttributesGroupProperties(String name) {
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent actionEvent) {
        AttributesPropertiesPanel panel = AttributesPropertiesPanel
                .getInstance();
        ArchivingConfiguration currentAC = ArchivingConfiguration
                .getCurrentArchivingConfiguration();

        if (!panel.verifyValues()) {
            return;
        }

        try {
            ArchivingConfiguration.updateCurrentModes(currentAC.isHistoric());
        } catch (Exception e) {
            String title = Messages
                    .getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_PERIODIC_MODE_TITLE");
            String msg = Messages
                    .getMessage("DIALOGS_EDIT_AC_ATTRIBUTES_PROPERTIES_INVALID_HDB_PERIODIC_MODE_MESSAGE");
            JOptionPane.showMessageDialog(null, msg, title,
                    JOptionPane.ERROR_MESSAGE);
        }

        ILogger logger = LoggerFactory.getCurrentImpl();

        long exportPeriod = 0;
        // long keepingPeriod = 0;
        int HDBPeriod = 0;
        int TDBPeriod = 0;
        String dedicatedArchiver = panel.getDedicatedArchiver();

        exportPeriod = panel.getExportPeriod();
        // keepingPeriod = panel.getKeepingPeriod();

        try {
            if (panel.isHistoric()) {
                HDBPeriod = panel.getHDBperiod();
                // System.out.println (
                // "SetAttributesGroupProperties/HDBPeriod/"+HDBPeriod );
            }
        } catch (Exception e) {
            e.printStackTrace();

            String msg = Messages
                    .getLogMessage("SET_ATTRIBUTES_GROUP_INVALID_VALUES");
            logger.trace(ILogger.LEVEL_WARNING, msg);
            logger.trace(ILogger.LEVEL_WARNING, e);

            panel
                    .setHDBPeriod(Integer
                            .parseInt(AttributesPropertiesPanel.DEFAULT_HDB_PERIOD_VALUE));

            return;
        }

        try {
            if (!panel.isHistoric()) {
                TDBPeriod = panel.getTDBperiod();
                // System.out.println (
                // "SetAttributesGroupProperties/TDBPeriod/"+TDBPeriod );
            }
        } catch (Exception e) {
            e.printStackTrace();

            String msg = Messages
                    .getLogMessage("SET_ATTRIBUTES_GROUP_INVALID_VALUES");
            logger.trace(ILogger.LEVEL_WARNING, msg);
            logger.trace(ILogger.LEVEL_WARNING, e);

            panel
                    .setTDBPeriod(Integer
                            .parseInt(AttributesPropertiesPanel.DEFAULT_TDB_PERIOD_VALUE));

            return;
        }

        ArchivingConfigurationAttributeProperties currentProperties = ArchivingConfigurationAttributeProperties
                .getCurrentProperties();
        /*
         * System.out.println ( "CLA A---------------" ); System.out.println (
         * currentProperties.toString() ); System.out.println (
         * "CLA A---------------" );
         */

        ArchivingConfigurationAttributeHDBProperties currentHDBProperties = currentProperties
                .getHDBProperties();
        ArchivingConfigurationAttributeTDBProperties currentTDBProperties = currentProperties
                .getTDBProperties();
        currentTDBProperties.setExportPeriod(exportPeriod);
        // currentTDBProperties.setKeepingPeriod( keepingPeriod );

        ACAttributesPropertiesTree tree = ACAttributesPropertiesTree
                .getInstance();
        Vector attributes = tree.getListOfAttributesToSet();
        if (attributes == null) {
            return;// nothing to set
        }
        Enumeration enumer = attributes.elements();
        ArchivingConfigurationAttributes currentACAttributes = currentAC
                .getAttributes();
        while (enumer.hasMoreElements()) {
            ArchivingConfigurationAttribute next = (ArchivingConfigurationAttribute) enumer
                    .nextElement();

            String name = next.getCompleteName();
            next = currentACAttributes.getAttribute(name);
            if (next == null) {
                next = new ArchivingConfigurationAttribute(currentACAttributes);
                next.setCompleteName(name);
            }

            if (currentHDBProperties.isEmpty()
                    && currentTDBProperties.isEmpty()) {
            } else {
                if (currentAC.isHistoric()) {
                    currentProperties
                            .setTDBProperties(new ArchivingConfigurationAttributeTDBProperties());
                } else {
                    currentProperties
                            .setHDBProperties(new ArchivingConfigurationAttributeHDBProperties());
                }

                next.addProperties(currentProperties);
            }

            /*
             * System.out.println ( "CLA 0---------------" ); System.out.println
             * ( next.toString() ); System.out.println ( "CLA 0---------------"
             * );
             */

            next.setHDBPeriod(HDBPeriod);
            next.setTDBPeriod(TDBPeriod);
            // next.setKeepingPeriod( keepingPeriod );
            next.setExportPeriod(exportPeriod);

            next
                    .setDedicatedArchiver(currentAC.isHistoric(),
                            dedicatedArchiver);

            // System.out.println (
            // "CLA 0 A/"+exportPeriod+"/keepingPeriod/"+keepingPeriod );
            removeUnwantedProperties(next);

            /*
             * System.out.println ( "CLA 1---------------" ); System.out.println
             * ( next.toString() ); System.out.println ( "CLA 1---------------"
             * );
             */

            currentACAttributes.addAttribute(next);
        }
        tree.setCellRenderer(new ACTreeRenderer());

        ArchivingConfigurationAttributeProperties.resetCurrentProperties();
    }

    /**
     * @param next
     *            7 sept. 2005
     */
    private void removeUnwantedProperties(ArchivingConfigurationAttribute next) {
        AttributesPropertiesPanel panel = AttributesPropertiesPanel
                .getInstance();

        boolean hasModeHDB_A = panel.hasModeHDB_A();
        next.removeProperty(hasModeHDB_A, ArchivingConfigurationMode.TYPE_A,
                true);

        boolean hasModeHDB_R = panel.hasModeHDB_R();
        next.removeProperty(hasModeHDB_R, ArchivingConfigurationMode.TYPE_R,
                true);

        boolean hasModeHDB_T = panel.hasModeHDB_T();
        next.removeProperty(hasModeHDB_T, ArchivingConfigurationMode.TYPE_T,
                true);

        boolean hasModeHDB_D = panel.hasModeHDB_D();
        next.removeProperty(hasModeHDB_D, ArchivingConfigurationMode.TYPE_D,
                true);

        boolean hasModeHDB_P = panel.hasModeHDB_P();
        next.removeProperty(hasModeHDB_P, ArchivingConfigurationMode.TYPE_P,
                true);

        boolean hasModeTDB_A = panel.hasModeTDB_A();
        next.removeProperty(hasModeTDB_A, ArchivingConfigurationMode.TYPE_A,
                false);

        boolean hasModeTDB_R = panel.hasModeTDB_R();
        next.removeProperty(hasModeTDB_R, ArchivingConfigurationMode.TYPE_R,
                false);

        boolean hasModeTDB_T = panel.hasModeTDB_T();
        next.removeProperty(hasModeTDB_T, ArchivingConfigurationMode.TYPE_T,
                false);

        boolean hasModeTDB_D = panel.hasModeTDB_D();
        next.removeProperty(hasModeTDB_D, ArchivingConfigurationMode.TYPE_D,
                false);

        boolean hasModeTDB_P = panel.hasModeTDB_P();
        next.removeProperty(hasModeTDB_P, ArchivingConfigurationMode.TYPE_P,
                false);
    }

    /**
     * @param currentSelectedNode
     * @param tree
     * @return 22 juil. 2005
     */
    /*
     * private DefaultMutableTreeNode getCurrentSelectedNodeInContext (
     * DefaultMutableTreeNode currentSelectedNode , ACAttributesPropertiesTree
     * tree ) { DefaultMutableTreeNode root = ( DefaultMutableTreeNode )
     * tree.getModel().getRoot();
     * 
     * Enumeration e = root.breadthFirstEnumeration(); while (
     * e.hasMoreElements() ) { DefaultMutableTreeNode node = (
     * DefaultMutableTreeNode ) e.nextElement(); if (
     * node.getUserObject().equals( currentSelectedNode.getUserObject() ) ) {
     * return node; } }
     * 
     * return null; }
     */

}
