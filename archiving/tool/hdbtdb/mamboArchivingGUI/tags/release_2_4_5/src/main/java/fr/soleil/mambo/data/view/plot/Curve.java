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

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.comete.widget.awt.AwtColorTool;
import fr.soleil.comete.widget.properties.CurveProperties;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Curve extends CurveProperties {

    public static final String XML_TAG = "curve";
    public static final String COLOR_PROPERTY_XML_TAG = "color";
    public static final String WIDTH_PROPERTY_XML_TAG = "width";
    public static final String STYLE_PROPERTY_XML_TAG = "style";
    public static final String NAME_PROPERTY_XML_TAG = "name";

    public Curve() {
        super();
    }

    public Curve(CometeColor cometeColor, int defaultCurveWidth, int defaultCurveLineStyle,
            String name) {
        super(cometeColor, defaultCurveWidth, defaultCurveLineStyle, name);
    }

    public Curve(CurveProperties curve) {
        super();
        if (curve != null) {
            try {
                CurveProperties clone = (CurveProperties) curve.clone();
                setColor(clone.getColor());
                setLineStyle(clone.getLineStyle());
                setName(clone.getName());
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
                .getColor(getColor())));
        openingLine.setAttribute(WIDTH_PROPERTY_XML_TAG, String.valueOf(getWidth()));
        openingLine.setAttribute(STYLE_PROPERTY_XML_TAG, String.valueOf(getLineStyle()));
        openingLine.setAttribute(NAME_PROPERTY_XML_TAG, getName());

        return openingLine.toString();
    }
}
