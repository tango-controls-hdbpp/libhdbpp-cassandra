// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Curve.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Curve.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.5 $
//
// $Log: Curve.java,v $
// Revision 1.5 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.4 2007/01/11 14:05:45 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
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

import java.awt.Color;

import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Curve {

    private Color              color;
    private int                width;
    private int                lineStyle;

    public static final String XML_TAG                = "curve";
    public static final String COLOR_PROPERTY_XML_TAG = "color";
    public static final String WIDTH_PROPERTY_XML_TAG = "width";
    public static final String STYLE_PROPERTY_XML_TAG = "style";
    private boolean            isEmpty                = true;

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(COLOR_PROPERTY_XML_TAG, GUIUtilities
                .colorToString(color));
        openingLine.setAttribute(WIDTH_PROPERTY_XML_TAG, String.valueOf(width));
        openingLine.setAttribute(STYLE_PROPERTY_XML_TAG, String
                .valueOf(lineStyle));

        return openingLine.toString();
    }

    public Curve() {
        isEmpty = true;
    }

    public Curve(Color _color, int _width, int _lineStyle) {
        this.color = _color;
        this.width = _width;
        this.lineStyle = _lineStyle;

        isEmpty = false;
    }

    /**
     * @return Returns the color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color
     *            The color to set.
     */
    public void setColor(Color color) {
        this.color = color;
        isEmpty = false;
    }

    /**
     * @return Returns the lineStyle.
     */
    public int getLineStyle() {
        return lineStyle;
    }

    /**
     * @param lineStyle
     *            The lineStyle to set.
     */
    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
        isEmpty = false;
    }

    /**
     * @return Returns the width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width
     *            The width to set.
     */
    public void setWidth(int width) {
        this.width = width;
        isEmpty = false;
    }

    public JLDataView applyProperties(JLDataView in) {
        in.setLineWidth(this.width);
        in.setStyle(this.lineStyle);
        in.setColor(this.color);

        return in;
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return isEmpty;
    }

}
