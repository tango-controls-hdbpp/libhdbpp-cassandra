package fr.soleil.mambo.tools;

import fr.soleil.comete.widget.properties.AxisProperties;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class AxisPropertiesUtils {
    public static final String XML_TAG_1 = "Y1Axis";
    public static final String XML_TAG_2 = "Y2Axis";
    public static final String XML_TAG_3 = "XAxis";
    public static final String GRID_VISIBLE_XML_TAG = "gridVisible";
    public static final String POSITION_PROPERTY_XML_TAG = "position";
    public static final String SCALE_MIN_PROPERTY_XML_TAG = "scaleMin";
    public static final String SCALE_MAX_PROPERTY_XML_TAG = "scaleMax";
    public static final String SCALE_MODE_PROPERTY_XML_TAG = "scaleMode";
    public static final String AUTO_SCALE_PROPERTY_XML_TAG = "autoScale";
    public static final String LABEL_FORMAT_PROPERTY_XML_TAG = "labelFormat";
    public static final String COLOR_PROPERTY_XML_TAG = "color";
    public static final String SHOW_SUB_GRID_PROPERTY_XML_TAG = "showSubGrid";
    public static final String IS_VISIBLE_PROPERTY_XML_TAG = "isVisible";
    public static final String DRAW_OPPOSITE_PROPERTY_XML_TAG = "drawOpposite";
    public static final String TITLE_PROPERTY_XML_TAG = "title";
    public static final String LABEL_INTERVAL_XML_TAG = "labelInterval";
    public static final String ANNOTATION_XML_TAG = "annotation";

    public static String toXMLLine(String tag, AxisProperties axisProperties) {
        XMLLine openingLine = new XMLLine(tag, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttributeIfNotNull(POSITION_PROPERTY_XML_TAG, String.valueOf(axisProperties
                .getPosition()));
        openingLine.setAttributeIfNotNull(SCALE_MIN_PROPERTY_XML_TAG, String.valueOf(axisProperties
                .getScaleMin()));
        openingLine.setAttributeIfNotNull(SCALE_MAX_PROPERTY_XML_TAG, String.valueOf(axisProperties
                .getScaleMax()));
        openingLine.setAttributeIfNotNull(SCALE_MODE_PROPERTY_XML_TAG, String
                .valueOf(axisProperties.getScaleMode()));
        openingLine.setAttributeIfNotNull(AUTO_SCALE_PROPERTY_XML_TAG, String
                .valueOf(axisProperties.isAutoScale()));
        openingLine.setAttributeIfNotNull(LABEL_FORMAT_PROPERTY_XML_TAG, String
                .valueOf(axisProperties.getLabelFormat()));
        openingLine.setAttributeIfNotNull(COLOR_PROPERTY_XML_TAG, GUIUtilities
                .colorToString(axisProperties.getColor().getAwtColor()));
        openingLine.setAttributeIfNotNull(SHOW_SUB_GRID_PROPERTY_XML_TAG, String
                .valueOf(axisProperties.isSubGridVisible()));
        openingLine.setAttributeIfNotNull(IS_VISIBLE_PROPERTY_XML_TAG, String
                .valueOf(axisProperties.isVisible()));
        openingLine.setAttributeIfNotNull(DRAW_OPPOSITE_PROPERTY_XML_TAG, String
                .valueOf(axisProperties.isDrawOpposite()));
        openingLine.setAttributeIfNotNull(TITLE_PROPERTY_XML_TAG, axisProperties.getTitle());
        openingLine.setAttributeIfNotNull(LABEL_INTERVAL_XML_TAG, String.valueOf(axisProperties
                .getUserLabelInterval()));
        openingLine.setAttributeIfNotNull(ANNOTATION_XML_TAG, String.valueOf(axisProperties
                .getAnnotation()));
        openingLine.setAttributeIfNotNull(GRID_VISIBLE_XML_TAG, String.valueOf(axisProperties
                .isGridVisible()));

        return openingLine.toString();
    }

}
