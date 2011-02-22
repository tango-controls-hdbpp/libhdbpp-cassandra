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

import fr.soleil.comete.widget.properties.TransformationProperties;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Polynomial2OrderTransform extends TransformationProperties {
    public static final String XML_TAG             = "transform";
    public static final String A0_PROPERTY_XML_TAG = "a0";
    public static final String A1_PROPERTY_XML_TAG = "a1";
    public static final String A2_PROPERTY_XML_TAG = "a2";

    public Polynomial2OrderTransform() {
        super();
    }

    public Polynomial2OrderTransform(double _a0, double _a1, double _a2) {
        super(_a0, _a1, _a2);
    }

    public Polynomial2OrderTransform(TransformationProperties transformation) {
        super();
        if (transformation != null) {
            try {
                TransformationProperties clone = (TransformationProperties) transformation
                        .clone();
                setA0(clone.getA0());
                setA1(clone.getA1());
                setA2(clone.getA2());
            } catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttribute(A0_PROPERTY_XML_TAG, String.valueOf(getA0()));
        openingLine.setAttribute(A1_PROPERTY_XML_TAG, String.valueOf(getA1()));
        openingLine.setAttribute(A2_PROPERTY_XML_TAG, String.valueOf(getA2()));

        return openingLine.toString();
    }
}
