// +======================================================================
// $Source$
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class GeneralTabbedPane.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// $Revision$
//
// $Log$
// Revision 1.5.2.1 2010/04/30 13:04:03 extia-soleil
// * EXTIA: Using Comet to configure plot properties attributes
//
// Revision 1.5 2010/04/19 17:01:08 extia-soleil
// * EXTIA: TMA Part 5 delivery
//
// Revision 1.3.6.1 2010/04/19 08:15:57 extia-soleil
// * EXTIA: Management title of chart if is null
//
// Revision 1.4 2010/03/23 11:15:57 extia-soleil
// * extia : removed a useless "null" in chart title
//
// Revision 1.3 2009/12/17 12:50:57 pierrejoseph
// CheckStyle: Organize imports / Format
//
// Revision 1.2 2009/11/24 09:53:30 soleilarc
// * Rapha�l GIRARDOT: VC details UI exported as a new Bean
//
// Revision 1.1 2008/06/11 14:25:02 soleilatk
// * GIRARDOT: GeneralTabbedPane --> ChartGeneralTabbedPane
//
// Revision 1.10 2007/05/11 07:24:00 ounsy
// * minor bug correction with Fonts
//
// Revision 1.9 2007/03/28 13:23:27 ounsy
// minor bug with Fonts correction
//
// Revision 1.8 2007/03/27 13:40:53 ounsy
// Better Font management (correction of Mantis bug 4302)
//
// Revision 1.7 2007/03/07 14:47:14 ounsy
// correction of Mantis bug 3273 (chart properties panel size)
//
// Revision 1.6 2006/10/02 14:13:25 ounsy
// minor changes (look and feel)
//
// Revision 1.5 2006/07/25 09:49:46 ounsy
// commented a useless log
//
// Revision 1.4 2006/07/05 12:58:59 ounsy
// VC : data synchronization management
//
// Revision 1.3 2005/12/15 11:32:01 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
//
// Revision 1.1.2.4 2005/09/26 07:52:25 chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
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
package fr.soleil.mambo.containers.view.dialogs;

import javax.swing.JPanel;

import chart.temp.chart.AxisPanel;
import chart.temp.chart.JLChartOption;
import fr.soleil.comete.widget.properties.ChartProperties;

/**
 * A class to display global graph settings dialog.
 */
public class ChartGeneralTabbedPane extends JLChartOption {
    private static final long serialVersionUID = 7599956107468871211L;

    public ChartGeneralTabbedPane() {
        super();
        this.setChart(null);
        this.getCloseButton().setVisible(false);
        setScalePanelEnabled(false, AxisPanel.X_TYPE);
    }

    public ChartProperties getGeneralChartProperties() {
        return this.getProperties();
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.add(this);
        return panel;
    }
}
