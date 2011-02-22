// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCVariationViewAction.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationViewAction.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: VCVariationViewAction.java,v $
// Revision 1.5 2006/10/25 08:00:46 ounsy
// replaced calls to show() by calls to setVisible(true)
//
// Revision 1.4 2006/09/05 13:43:21 ounsy
// updated for sampling compatibility
//
// Revision 1.3 2006/02/01 14:05:38 ounsy
// minor changes (small date bug corrected)
//
// Revision 1.2 2005/12/15 10:53:11 ounsy
// table selection for "variations"
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.sql.Timestamp;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;
import fr.soleil.mambo.containers.view.dialogs.VCVariationViewDialog;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.ViewConfigurationData;

public class VCVariationViewAction extends AbstractAction {

    private static final long serialVersionUID = 1990327147847619419L;
    private final ViewConfigurationBean viewConfigurationBean;

    /**
     * @param name
     */
    public VCVariationViewAction(final String name,
	    final ViewConfigurationBean viewConfigurationBean) {
	putValue(Action.NAME, name);
	putValue(Action.SHORT_DESCRIPTION, name);
	this.viewConfigurationBean = viewConfigurationBean;
    }

    @Override
    public void actionPerformed(final ActionEvent arg0) {
	prepareLegends();
	final ViewConfiguration svc = viewConfigurationBean.getViewConfiguration();
	final ViewConfiguration vc = new ViewConfiguration();
	final ViewConfigurationData data = svc.getData().cloneData();
	data.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
	vc.setData(data);
	vc.computeCurrentId();
	final ViewConfigurationAttributes vcas = new ViewConfigurationAttributes();
	final ViewConfigurationAttributes svcas = svc.getAttributes().cloneAttrs();
	if (viewConfigurationBean.getVariationDialog().getVariationPanel().getWorkingTable()) {
	    final int[] rows = viewConfigurationBean.getVariationDialog().getVariationPanel()
		    .getResultPanel().getResultTable().getSelectedRows();
	    for (final int row : rows) {
		final String name = (String) viewConfigurationBean.getVariationDialog()
			.getVariationPanel().getResultPanel().getResultTable().getModel()
			.getValueAt(row, 0);
		final ViewConfigurationAttribute vca = svcas.getAttribute(name);
		if (vca != null) {
		    vcas.addAttribute(vca);
		}
	    }
	} else {
	    final int[] rows = viewConfigurationBean.getVariationDialog().getVariationPanel()
		    .getRankingPanel().getRankingTable().getSelectedRows();
	    for (final int row : rows) {
		final String name = (String) viewConfigurationBean.getVariationDialog()
			.getVariationPanel().getRankingPanel().getRankingTable().getModel()
			.getValueAt(row, 0);
		final ViewConfigurationAttribute vca = svcas.getAttribute(name);
		if (vca != null) {
		    vcas.addAttribute(vca);
		}
	    }
	}
	vc.setAttributes(vcas);
	final VCVariationViewDialog dialog = new VCVariationViewDialog(vc);
	dialog.setVisible(true);
    }

    /**
     * 6 sept. 2005
     */
    private void prepareLegends() {
	// I don't know why but this makes the curve legends appear correctly..

	final ChartGeneralTabbedPane generalTabbedPane = viewConfigurationBean.getEditDialog()
		.getGeneralTab().getChartGeneralTabbedPane();
	final ChartProperties chartProperties = generalTabbedPane.getProperties();

	final ViewConfigurationData newData = new ViewConfigurationData(chartProperties);

	final ViewConfiguration selectedVC = viewConfigurationBean.getViewConfiguration();
	final ViewConfigurationData oldData = selectedVC.getData();
	newData.setCreationDate(oldData.getCreationDate());
	newData.setPath(oldData.getPath());

	// --setting the edit dates
	newData.setLastUpdateDate(GUIUtilities.now());
	// --setting the edit dates

	// --do dnot remove
	final boolean dynamic = oldData.isDynamicDateRange();
	if (dynamic) {
	    final Timestamp[] range = oldData.getDynamicStartAndEndDates();
	    newData.setStartDate(range[0]);
	    newData.setEndDate(range[1]);
	} else {
	    newData.setStartDate(oldData.getStartDate());
	    newData.setEndDate(oldData.getEndDate());
	}
	newData.setDynamicDateRange(dynamic);
	newData.setDateRange(oldData.getDateRange());
	newData.setHistoric(oldData.isHistoric());
	newData.setName(oldData.getName());
	newData.setSamplingType(oldData.getSamplingType());

	selectedVC.setData(newData);
	// --do not remove
    }

}
