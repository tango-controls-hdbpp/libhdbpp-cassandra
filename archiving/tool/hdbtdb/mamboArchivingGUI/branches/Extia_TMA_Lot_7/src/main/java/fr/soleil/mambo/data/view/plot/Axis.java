// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/Axis.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Axis.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: Axis.java,v $
// Revision 1.2 2005/11/29 18:28:12 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
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

import fr.soleil.comete.widget.properties.AxisProperties;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class Axis extends AxisProperties {
    public static final String XML_TAG_1                      = "Y1Axis";
    public static final String XML_TAG_2                      = "Y2Axis";
    public static final String XML_TAG_3                      = "XAxis";
    public static final String GRID_VISIBLE_XML_TAG           = "gridVisible";
    public static final String POSITION_PROPERTY_XML_TAG      = "position";
    public static final String SCALE_MIN_PROPERTY_XML_TAG     = "scaleMin";
    public static final String SCALE_MAX_PROPERTY_XML_TAG     = "scaleMax";
    public static final String SCALE_MODE_PROPERTY_XML_TAG    = "scaleMode";
    public static final String AUTO_SCALE_PROPERTY_XML_TAG    = "autoScale";
    public static final String LABEL_FORMAT_PROPERTY_XML_TAG  = "labelFormat";
    public static final String COLOR_PROPERTY_XML_TAG         = "color";
    public static final String SHOW_SUB_GRID_PROPERTY_XML_TAG = "showSubGrid";
    public static final String IS_VISIBLE_PROPERTY_XML_TAG    = "isVisible";
    public static final String DRAW_OPPOSITE_PROPERTY_XML_TAG = "drawOpposite";
    public static final String TITLE_PROPERTY_XML_TAG         = "title";
    public static final String LABEL_INTERVAL_XML_TAG         = "labelInterval";
    public static final String ANNOTATION_XML_TAG             = "annotation";

    // grid style

    public Axis() {
        super();
    }

    public Axis(AxisProperties axisProperties) {
        super();
        if (axisProperties != null) {
            try {
                AxisProperties clone = (AxisProperties) axisProperties.clone();
                setScaleMax(clone.getScaleMax());
                setScaleMin(clone.getScaleMin());
                setScaleMode(clone.getScaleMode());
                setAutoScale(clone.isAutoScale());
                setLabelFormat(clone.getLabelFormat());
                setTitle(clone.getTitle());
                setColor(clone.getColor());
                setGridStyle(clone.getGridStyle());
                setGridVisible(clone.isGridVisible());
                setSubGridVisible(clone.isSubGridVisible());
                setVisible(clone.isVisible());
                setDrawOpposite(clone.isDrawOpposite());
                setPosition(clone.getPosition());
                setUserLabelInterval(clone.getUserLabelInterval());
                setAnnotation(clone.getAnnotation());
            } catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public String toString(String tag) {
        // String ret = "";

        // String tag = isY1 ? XML_TAG_1 : XML_TAG_2;
        XMLLine openingLine = new XMLLine(tag, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttributeIfNotNull(POSITION_PROPERTY_XML_TAG, String
                .valueOf(getPosition()));
        openingLine.setAttributeIfNotNull(SCALE_MIN_PROPERTY_XML_TAG, String
                .valueOf(getScaleMin()));
        openingLine.setAttributeIfNotNull(SCALE_MAX_PROPERTY_XML_TAG, String
                .valueOf(getScaleMax()));
        openingLine.setAttributeIfNotNull(SCALE_MODE_PROPERTY_XML_TAG, String
                .valueOf(getScaleMode()));
        openingLine.setAttributeIfNotNull(AUTO_SCALE_PROPERTY_XML_TAG, String
                .valueOf(isAutoScale()));
        openingLine.setAttributeIfNotNull(LABEL_FORMAT_PROPERTY_XML_TAG, String
                .valueOf(getLabelFormat()));
        openingLine.setAttributeIfNotNull(COLOR_PROPERTY_XML_TAG, GUIUtilities
                .colorToString(getColor().getAwtColor()));
        openingLine.setAttributeIfNotNull(SHOW_SUB_GRID_PROPERTY_XML_TAG,
                String.valueOf(isSubGridVisible()));
        openingLine.setAttributeIfNotNull(IS_VISIBLE_PROPERTY_XML_TAG, String
                .valueOf(isVisible()));
        openingLine.setAttributeIfNotNull(DRAW_OPPOSITE_PROPERTY_XML_TAG,
                String.valueOf(isDrawOpposite()));
        openingLine.setAttributeIfNotNull(TITLE_PROPERTY_XML_TAG, getTitle());
        openingLine.setAttributeIfNotNull(LABEL_INTERVAL_XML_TAG, String
                .valueOf(getUserLabelInterval()));
        openingLine.setAttributeIfNotNull(ANNOTATION_XML_TAG, String
                .valueOf(getAnnotation()));
        openingLine.setAttributeIfNotNull(GRID_VISIBLE_XML_TAG, String
                .valueOf(isGridVisible()));

        return openingLine.toString();
    }

    public Axis cloneAxis() {
        Axis ret = new Axis();

        ret.setAutoScale(this.isAutoScale());
        ret.setColor(this.getColor() == null ? null : new CometeColor(this
                .getColor().getRed(), this.getColor().getGreen(), this
                .getColor().getBlue()));
        ret.setDrawOpposite(this.isDrawOpposite());
        ret.setLabelFormat(this.getLabelFormat());
        ret.setPosition(this.getPosition());
        ret.setScaleMax(this.getScaleMax());
        ret.setScaleMin(this.getScaleMin());
        ret.setScaleMode(this.getScaleMode());
        ret.setSubGridVisible(this.isSubGridVisible());
        ret.setTitle(this.getTitle() == null ? null : new String(this
                .getTitle()));
        ret.setVisible(this.isVisible());
        ret.setUserLabelInterval(this.getUserLabelInterval());
        ret.setAnnotation(this.getAnnotation());

        return ret;
    }
}
