// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/GeneralPanelMouseListener.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class GeneralPanelMouseListener.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: GeneralPanelMouseListener.java,v $
// Revision 1.3 2007/03/27 13:40:53 ounsy
// Better Font management (correction of Mantis bug 4302)
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
package fr.soleil.mambo.actions.view.listeners;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;

import fr.esrf.tangoatk.widget.util.ATKFontChooser;
import fr.soleil.mambo.containers.view.dialogs.ChartGeneralTabbedPane;

public class GeneralPanelMouseListener extends MouseAdapter {

	private ChartGeneralTabbedPane chartGeneralTabbedPane;

	public GeneralPanelMouseListener(
			ChartGeneralTabbedPane chartGeneralTabbedPane) {
		super();
		this.chartGeneralTabbedPane = chartGeneralTabbedPane;
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == chartGeneralTabbedPane.getGeneralFontHeaderBtn()) {
			Font f = ATKFontChooser.getNewFont(chartGeneralTabbedPane,
					"Choose Header Font", chartGeneralTabbedPane
							.getHeaderFont());
			if (f != null) {
				chartGeneralTabbedPane.setHeaderFont(f);
			}
		} else if (e.getSource() == chartGeneralTabbedPane
				.getGeneralFontLabelBtn()) {
			Font f = ATKFontChooser.getNewFont(chartGeneralTabbedPane,
					"Choose label Font", chartGeneralTabbedPane.getLabelFont());
			if (f != null) {
				chartGeneralTabbedPane.setLabelFont(f);
			}
		} else if (e.getSource() == chartGeneralTabbedPane
				.getGeneralBackColorBtn()) {
			Color c = JColorChooser.showDialog(chartGeneralTabbedPane,
					"Choose marker Color", chartGeneralTabbedPane
							.getBackgroundColor());
			if (c != null) {
				chartGeneralTabbedPane.setBackgroundColor(c);
			}
		}
	}
}
