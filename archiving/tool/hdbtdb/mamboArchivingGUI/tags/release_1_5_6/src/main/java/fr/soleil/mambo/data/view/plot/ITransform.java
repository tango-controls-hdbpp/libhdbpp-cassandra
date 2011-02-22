//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/ITransform.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ITransform.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ITransform.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.view.plot;

import fr.esrf.tangoatk.widget.util.chart.JLDataView;

public interface ITransform
{
    public double transform ( double in );

    /**
     * 25 août 2005
     */
    public void push ();

    /**
     * @param in
     * @return 30 août 2005
     */
    public JLDataView applyProperties ( JLDataView in );

    /**
     * @return
     */
    public boolean isEmpty ();
}
