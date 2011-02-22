// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCFinishAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ValidateVCEditAction.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: VCFinishAction.java,v $
// Revision 1.8 2008/04/09 10:45:46 achouri
// update principal windows
//
// Revision 1.7 2007/01/11 14:05:46 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.6 2006/09/05 13:43:09 ounsy
// updated for sampling compatibility
//
// Revision 1.5 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.4 2006/05/19 15:03:05 ounsy
// minor changes
//
// Revision 1.3 2006/05/16 09:33:31 ounsy
// minor changes
//
// Revision 1.2 2005/12/15 10:50:35 ounsy
// avoiding a null pointer exception
//
// Revision 1.1 2005/11/29 18:27:07 chinkumo
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.DateRangeBox;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class VCFinishAction extends AbstractAction {

    private static final long serialVersionUID = -1378092997925243091L;
    private ViewConfigurationBean viewConfigurationBean;

    /**
     * @param name
     */
    public VCFinishAction(String name, ViewConfigurationBean viewConfigurationBean) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, name);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // Save parameters of last attribut
        VCCustomTabbedPane tabbedPane = viewConfigurationBean.getEditDialog()
                .getVcCustomTabbedPane();
        int oldValue = tabbedPane.getSelectedIndex();
        if (oldValue == 2) {
            viewConfigurationBean.getEditDialog().getAttributesPlotPropertiesTab()
                    .getVcAttributesPropertiesTree().saveLastSelectionPath();
            viewConfigurationBean.getEditDialog().getAttributesPlotPropertiesTab()
                    .getVcAttributesPropertiesTree()
                    .getVcAttributesPropertiesTreeSelectionListener().treeSelectionAttributeSave();
        }
        if (oldValue == 3) {
            viewConfigurationBean.getEditDialog().getExpressionTab().getExpressionTree()
                    .saveCurrentSelection();
            viewConfigurationBean.getEditDialog().getExpressionTab().getExpressionTree()
                    .getExpressionTreeListener().treeSelectionSave();
        }

        if (verifyVC(viewConfigurationBean.getEditDialog().getGeneralTab().getDateRangeBox())) {
            // --setting the generic plot data
            ChartGeneralTabbedPane generalTabbedPane = viewConfigurationBean.getEditDialog()
                    .getGeneralTab().getChartGeneralTabbedPane();
            ChartProperties chartProperties = generalTabbedPane.getProperties();

            ViewConfigurationData newData = new ViewConfigurationData(chartProperties);

            ViewConfiguration currentVC = viewConfigurationBean.getEditingViewConfiguration();
            if (currentVC != null) {
                ViewConfigurationData oldData = currentVC.getData();

                // --setting the edit dates
                if (currentVC.isNew()) {
                    newData.setCreationDate(GUIUtilities.now());
                    newData.setPath(null);
                }
                else {
                    newData.setCreationDate(oldData.getCreationDate());
                    newData.setPath(oldData.getPath());
                }

                newData.setLastUpdateDate(GUIUtilities.now());

                // --setting the start and end dates and historic parameter
                boolean dynamic = viewConfigurationBean.getEditDialog().getGeneralTab()
                        .getDateRangeBox().isDynamicDateRange();
                if (dynamic) {
                    Timestamp[] range = viewConfigurationBean.getEditDialog().getGeneralTab()
                            .getDateRangeBox().getDynamicStartAndEndDates();
                    newData.setStartDate(range[0]);
                    newData.setEndDate(range[1]);
                }
                else {
                    newData.setStartDate(viewConfigurationBean.getEditDialog().getGeneralTab()
                            .getDateRangeBox().getStartDate());
                    newData.setEndDate(viewConfigurationBean.getEditDialog().getGeneralTab()
                            .getDateRangeBox().getEndDate());
                }
                newData.setDynamicDateRange(dynamic);
                newData.setDateRange(viewConfigurationBean.getEditDialog().getGeneralTab()
                        .getDateRangeBox().getDateRange());
                // --setting the start and end dates
                GeneralTab generalTab = viewConfigurationBean.getEditDialog().getGeneralTab();
                newData.setHistoric(generalTab.isHistoric());
                newData.setName(generalTab.getName());
                newData.setSamplingType(generalTab.getSamplingType());

                currentVC.setData(newData);
                currentVC.setModified(true);

                viewConfigurationBean.validateEdition();
            }
        }
    }

    /**
     * @return
     */
    public boolean verifyVC(DateRangeBox dateRangeBox) {
        return ViewConfigurationData.verifyDates(dateRangeBox) && verifyAttributesAreSet();
    }

    private boolean verifyAttributesAreSet() {
        if (containsNonSetAttributes()) {
            String msgTitle = Messages
                    .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_TITLE");
            String msgConfirm = Messages
                    .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_LABEL_1");
            msgConfirm += GUIUtilities.CRLF;
            msgConfirm += Messages.getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_LABEL_2");
            String msgCancel = Messages
                    .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_CANCEL");
            String msgValidate = Messages
                    .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_NON_SET_CONFIRM_VALIDATE");
            Object[] options = { msgValidate, msgCancel };

            int confirm = JOptionPane.showOptionDialog(null, msgConfirm, msgTitle,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
                    options[0]);
            return confirm == JOptionPane.OK_OPTION;
        }
        else {
            return true;
        }
    }

    /**
     * @return
     */
    private boolean containsNonSetAttributes() {
        VCAttributesTreeModel model = viewConfigurationBean.getEditDialog().getAttributesTab()
                .getSelectedAttributesTree().getModel();
        TreeMap<String, ViewConfigurationAttribute> htAttr = model.getAttributes();
        if (htAttr == null) {
            return false;
        }

        Set<String> keySet = htAttr.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            ViewConfigurationAttribute nextValue = htAttr.get(keyIterator.next());
            String name = nextValue.getCompleteName();
            ViewConfiguration currentVC = viewConfigurationBean.getEditingViewConfiguration();
            if (!currentVC.containsAttribute(name)) {
                return true;
            }
            nextValue = currentVC.getAttribute(name);

            if (nextValue.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
