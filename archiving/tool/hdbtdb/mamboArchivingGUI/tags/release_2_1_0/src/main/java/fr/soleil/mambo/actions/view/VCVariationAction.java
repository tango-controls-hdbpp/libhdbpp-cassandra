// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/VCVariationAction.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class VCVariationAction.
// (GIRARDOT Raphael) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: VCVariationAction.java,v $
// Revision 1.2 2006/02/01 14:05:23 ounsy
// minor changes (small date bug corrected)
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

import fr.soleil.mambo.bean.view.ViewConfigurationBean;

public class VCVariationAction extends AbstractAction {

	private static final long serialVersionUID = -1334214425229714228L;
	private ViewConfigurationBean viewConfigurationBean;

	/**
	 * @param name
	 */
	public VCVariationAction(String name,
			ViewConfigurationBean viewConfigurationBean) {
		super();
		this.putValue(Action.NAME, name);
		this.viewConfigurationBean = viewConfigurationBean;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (viewConfigurationBean.getViewConfiguration() != null) {
			if (viewConfigurationBean.getViewConfiguration().getData()
					.isDynamicDateRange()) {
				Timestamp[] range = viewConfigurationBean
						.getViewConfiguration().getData()
						.getDynamicStartAndEndDates();
				viewConfigurationBean.getViewConfiguration().getData()
						.setStartDate(range[0]);
				viewConfigurationBean.getViewConfiguration().getData()
						.setEndDate(range[1]);
			}

			viewConfigurationBean.getVariationDialog().getVariationPanel()
					.getResultPanel().getResultTable().getModel().update();
			viewConfigurationBean.getVariationDialog().getVariationPanel()
					.getRankingPanel().getRankingTable().getModel().update();
			viewConfigurationBean.getVariationDialog().setVisible(true);
		}
	}

}
