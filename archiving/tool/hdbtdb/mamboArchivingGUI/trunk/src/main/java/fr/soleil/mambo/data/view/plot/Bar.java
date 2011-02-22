// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Bar.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Bar.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: Bar.java,v $
// Revision 1.4 2007/02/01 14:21:46 pierrejoseph
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

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.widget.awt.AwtColorTool;
import fr.soleil.comete.widget.properties.BarProperties;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Bar extends BarProperties {
    public static final String XML_TAG = "bar";
    public static final String COLOR_PROPERTY_XML_TAG = "color";
    public static final String WIDTH_PROPERTY_XML_TAG = "width";
    public static final String FILL_STYLE_PROPERTY_XML_TAG = "fillStyle";
    public static final String FILL_METHOD_PROPERTY_XML_TAG = "fillMethod";

    public Bar() {
        super();
    }

    public Bar(CometeColor color, int width, int fillStyle, int fillingMethod) {
        super(color, width, fillStyle, fillingMethod);
    }

    public Bar(BarProperties bar) {
        super();
        if (bar != null) {
            try {
                BarProperties clone = (BarProperties) bar.clone();
                setFillColor(clone.getFillColor());
                setFillingMethod(clone.getFillingMethod());
                setFillStyle(clone.getFillStyle());
                setWidth(clone.getWidth());
            }
            catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public boolean isEmpty() {
        return false;
    }

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(COLOR_PROPERTY_XML_TAG, GUIUtilities.colorToString(AwtColorTool
                .getColor(getFillColor())));
        openingLine.setAttribute(WIDTH_PROPERTY_XML_TAG, String.valueOf(getWidth()));
        openingLine.setAttribute(FILL_STYLE_PROPERTY_XML_TAG, String.valueOf(getFillStyle()));
        openingLine.setAttribute(FILL_METHOD_PROPERTY_XML_TAG, String.valueOf(getFillingMethod()));

        return openingLine.toString();
    }
}
