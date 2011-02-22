// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Polynomial2OrderTransform.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Polynomial2OrderTransform.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: Polynomial2OrderTransform.java,v $
// Revision 1.5 2007/02/27 10:06:32 ounsy
// corrected the transformation property bug
//
// Revision 1.4 2007/02/01 14:21:45 pierrejoseph
// XmlHelper reorg
//
// Revision 1.3 2007/01/11 14:05:45 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.2 2005/11/29 18:28:12 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:32 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.data.view.plot;

import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Polynomial2OrderTransform {

    private double             a0;
    private double             a1;
    private double             a2;

    public static final String XML_TAG             = "transform";
    public static final String A0_PROPERTY_XML_TAG = "a0";
    public static final String A1_PROPERTY_XML_TAG = "a1";
    public static final String A2_PROPERTY_XML_TAG = "a2";
    private boolean            isEmpty             = true;

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(A0_PROPERTY_XML_TAG, String.valueOf(a0));
        openingLine.setAttribute(A1_PROPERTY_XML_TAG, String.valueOf(a1));
        openingLine.setAttribute(A2_PROPERTY_XML_TAG, String.valueOf(a2));

        return openingLine.toString();
    }

    public Polynomial2OrderTransform() {
        isEmpty = true;
    }

    public Polynomial2OrderTransform(double _a0, double _a1, double _a2) {
        this.a0 = _a0;
        this.a1 = _a1;
        this.a2 = _a2;

        isEmpty = false;
    }

    private boolean isInvalid() {
        if (Double.isNaN(a0)) {
            return true;
        }
        if (Double.isNaN(a1)) {
            return true;
        }
        if (Double.isNaN(a2)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * @see mambo.data.view.plot.ITransform#transform(double)
     */
    public double transform(double in) {
        if (this.isInvalid()) {
            return in;
        }

        double in2 = in * in;
        return a0 + a1 * in + a2 * in2;
    }

    /**
     * @return Returns the a0.
     */
    public double getA0() {
        return a0;
    }

    /**
     * @param a0
     *            The a0 to set.
     */
    public void setA0(double a0) {
        this.a0 = a0;
        isEmpty = false;
    }

    /**
     * @return Returns the a1.
     */
    public double getA1() {
        return a1;
    }

    /**
     * @param a1
     *            The a1 to set.
     */
    public void setA1(double a1) {
        this.a1 = a1;
        isEmpty = false;
    }

    /**
     * @return Returns the a2.
     */
    public double getA2() {
        return a2;
    }

    /**
     * @param a2
     *            The a2 to set.
     */
    public void setA2(double a2) {
        this.a2 = a2;
        isEmpty = false;
    }

    public JLDataView applyProperties(JLDataView in) {
        if (!this.isInvalid()) {
            in.setA0(this.a0);
            in.setA1(this.a1);
            in.setA2(this.a2);
        }

        return in;
    }

    /*
     * (non-Javadoc)
     * @see mambo.data.view.plot.ITransform#isEmpty()
     */
    public boolean isEmpty() {
        return isEmpty;
    }
}
