// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/components/view/CustomJLChart.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class CustomJLChart.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: CustomJLChart.java,v $
// Revision 1.5 2007/05/10 14:46:34 ounsy
// expressions are no more accessible through chart menu (only accessible
// through VC edit dialog)
//
// Revision 1.4 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.3 2006/08/23 10:02:11 ounsy
// use of the bug correction in JLChart that allows to have a JDialog for
// "show table" instead of a JFrame
//
// Revision 1.2 2005/11/29 18:28:12 chinkumo
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
package fr.soleil.mambo.components.view;

import fr.esrf.tangoatk.widget.util.chart.math.StaticChartMathExpression;

public class CustomJLChart extends StaticChartMathExpression {

    public CustomJLChart() {
        super();
        getY1Axis().clearDataView();
        getY2Axis().clearDataView();
        getXAxis().clearDataView();
        this.setPreferDialogForTable(true, true);
        this.removeUserAction("Evaluate an expression");
    }

}
