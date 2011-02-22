// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/ArchivingTransferAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ArchivingTransferAction.
// (GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ArchivingTransferAction.java,v $
// Revision 1.10 2007/08/24 09:23:00 ounsy
// Color index reset on new VC (Mantis bug 5210 part1)
//
// Revision 1.9 2006/11/14 09:35:05 ounsy
// minor changes
//
// Revision 1.8 2006/08/23 10:01:32 ounsy
// some optimizations with less tree model reloading
//
// Revision 1.7 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.6 2006/07/05 12:58:58 ounsy
// VC : data synchronization management
//
// Revision 1.5 2006/06/20 08:40:27 ounsy
// by default, y1 is visible and autoscaled
//
// Revision 1.4 2006/05/19 14:59:14 ounsy
// minor changes
//
// Revision 1.3 2006/02/24 12:13:57 ounsy
// modified so that the Ac's isHistoric is automatically used
//
// Revision 1.2 2005/12/15 10:43:17 ounsy
// minor changes
//
// Revision 1.1 2005/11/29 18:27:07 chinkumo
// no message
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
import java.sql.Timestamp;
import java.text.Collator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.models.ACAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.tango.util.entity.data.Attribute;

public class ArchivingTransferAction extends AbstractAction {

    private static final long serialVersionUID = -2689224947879853717L;
    private static ArchivingTransferAction instance;

    public static ArchivingTransferAction getInstance() {
        return instance;
    }

    public static ArchivingTransferAction getInstance(String name) {
        if (instance == null) {
            instance = new ArchivingTransferAction(name);
        }
        return instance;
    }

    private ArchivingTransferAction(String name) {
        super.putValue(Action.NAME, name);
        super.putValue(Action.SHORT_DESCRIPTION, name);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        ArchivingConfiguration selectedAC = ArchivingConfiguration
                .getSelectedArchivingConfiguration();

        if (selectedAC != null) {
            ACAttributesTreeModel acModel = ACAttributesTreeModel.getInstance();

            // adding attributes
            TreeMap<String, ArchivingConfigurationAttribute> acTable = acModel.getAttributes();
            Set<String> attrSet = acTable.keySet();
            Iterator<String> it = attrSet.iterator();
            TreeMap<String, ViewConfigurationAttribute> vcTable = new TreeMap<String, ViewConfigurationAttribute>(
                    Collator.getInstance());

            ViewConfigurationData newData = new ViewConfigurationData();

            int timePrecision = 0;
            if (selectedAC != null) {
                ArchivingConfigurationAttribute[] attributes = selectedAC.getAttributes()
                        .getAttributesList();
                if (attributes.length > 0) {
                    if (selectedAC.isHistoric()) {
                        try {
                            timePrecision = attributes[0].getProperties().getHDBProperties()
                                    .getMode(ArchivingConfigurationMode.TYPE_P).getMode()
                                    .getModeP().getPeriod() / 2;
                        }
                        catch (Exception e) {
                            timePrecision = 0;
                        }
                    }
                    else {
                        try {
                            timePrecision = attributes[0].getProperties().getTDBProperties()
                                    .getMode(ArchivingConfigurationMode.TYPE_P).getMode()
                                    .getModeP().getPeriod() / 2;
                        }
                        catch (Exception e) {
                            timePrecision = 0;
                        }
                    }
                }
            }
            newData.getChartProperties().setTimePrecision(timePrecision);
            newData.getChartProperties().setLegendsAreVisible(false);

            ViewConfiguration transferedVC = new ViewConfiguration();
            Timestamp now = GUIUtilities.now();
            Timestamp before = new Timestamp(now.getTime() - 3600000);
            newData.setCreationDate(now);
            newData.setLastUpdateDate(now);
            newData.setStartDate(before);
            newData.setEndDate(now);
            newData.setHistoric(selectedAC.isHistoric());
            newData.setDynamicDateRange(true);
            newData
                    .setDateRange(Messages
                            .getMessage("DIALOGS_EDIT_VC_GENERAL_DATES_RANGE_LAST_1H"));

            if (selectedAC != null) {
                newData.setName(selectedAC.getName());
            }

            transferedVC.setData(newData);
            while (it.hasNext()) {
                String key = it.next();
                ViewConfigurationAttribute vca = new ViewConfigurationAttribute((Attribute) acTable
                        .get(key));
                vcTable.put(key, vca);
            }
            transferedVC.getAttributes().getAttributes().clear();
            transferedVC.getAttributes().addAttributes(vcTable);
            OpenedVCComboBox.getInstance().selectElement(transferedVC);
        }

    }

}
