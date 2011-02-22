// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Marker.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Marker.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.4 $
//
// $Log: Marker.java,v $
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

import fr.soleil.comete.widget.awt.AwtColorTool;
import fr.soleil.comete.widget.properties.MarkerProperties;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Marker extends MarkerProperties {
    public static final String XML_TAG = "marker";
    public static final String COLOR_PROPERTY_XML_TAG = "color";
    public static final String SIZE_PROPERTY_XML_TAG = "size";
    public static final String STYLE_PROPERTY_XML_TAG = "style";
    public static final String IS_LEGEND_VISIBLE_PROPERTY_XML_TAG = "isLegendVisible";

    public Marker() {
        super();
    }

    public Marker(CometeColor cometeColor, int size, int style, boolean isLegendVisible) {
        super(cometeColor, size, style, isLegendVisible);
    }

    public Marker(MarkerProperties marker) {
        super();
        if (marker != null) {
            try {
                MarkerProperties clone = (MarkerProperties) marker.clone();
                setColor(clone.getColor());
                setLegendVisible(clone.isLegendVisible());
                setSize(clone.getSize());
                setStyle(clone.getStyle());
            }
            catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(COLOR_PROPERTY_XML_TAG, GUIUtilities.colorToString(AwtColorTool
                .getColor(getColor())));
        openingLine.setAttribute(SIZE_PROPERTY_XML_TAG, String.valueOf(getSize()));
        openingLine.setAttribute(STYLE_PROPERTY_XML_TAG, String.valueOf(getStyle()));
        openingLine.setAttribute(IS_LEGEND_VISIBLE_PROPERTY_XML_TAG, String
                .valueOf(isLegendVisible()));

        return openingLine.toString();
    }

    public boolean isEmpty() {
        return false;
    }
}
