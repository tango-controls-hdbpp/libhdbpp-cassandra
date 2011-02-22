package fr.soleil.mambo.tools;

import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.awt.AwtColorTool;
import fr.soleil.comete.widget.awt.AwtFontTool;
import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ChartPropertiesUtils {

    public static final int AXISGRID_NONE = 0;
    public static final int AXISGRID_ONX = 1;
    public static final int AXISGRID_ONY1 = 2;
    public static final int AXISGRID_ONY2 = 3;
    public static final int AXISGRID_ONXANDY1 = 4;
    public static final int AXISGRID_ONXANDY2 = 5;

    public static final String XML_TAG = "mainParams";
    public static final String LEGENDS_ARE_VISIBLE_PROPERTY_XML_TAG = "legendsAreVisible";
    public static final String LEGENDS_PLACEMENT_PROPERTY_XML_TAG = "legendsPlacement";
    public static final String BACKGROUND_COLOR_PROPERTY_XML_TAG = "backgroundColor";
    public static final String HEADER_FONT_PROPERTY_XML_TAG = "headerFont";
    public static final String LABEL_FONT_PROPERTY_XML_TAG = "labelFont";
    public static final String AXIS_GRID_PROPERTY_XML_TAG = "axisGrid";
    public static final String AXIS_GRID_STYLE_PROPERTY_XML_TAG = "axisGridStyle";
    public static final String TITLE_PROPERTY_XML_TAG = "title";
    public static final String DISPLAY_DURATION_PROPERTY_XML_TAG = "displayDuration";
    public static final String TIME_PRECISION_XML_TAG = "timePrecision";
    public static final String DATA_SENDING_ENABLED_XML_TAG = "dataSendingEnabled";
    public static final String AUTO_HIGHLIGHT_ON_LEGEND_XML_TAG = "autoHighlightOnLegend";

    public static int getAxisGrid(ChartProperties chartProperties) {
        int res = AXISGRID_NONE;
        if ((chartProperties.getXAxisProperties().isGridVisible())
                && (!chartProperties.getY1AxisProperties().isGridVisible())
                && (!chartProperties.getY2AxisProperties().isGridVisible())) {
            // On X
            res = AXISGRID_ONX;
        }
        else if ((!chartProperties.getXAxisProperties().isGridVisible())
                && (chartProperties.getY1AxisProperties().isGridVisible())
                && (!chartProperties.getY2AxisProperties().isGridVisible())) {
            // On Y1
            res = AXISGRID_ONY1;
        }
        else if ((!chartProperties.getXAxisProperties().isGridVisible())
                && (!chartProperties.getY1AxisProperties().isGridVisible())
                && (chartProperties.getY2AxisProperties().isGridVisible())) {
            // On Y2
            res = AXISGRID_ONY2;
        }
        else if ((chartProperties.getXAxisProperties().isGridVisible())
                && (chartProperties.getY1AxisProperties().isGridVisible())
                && (!chartProperties.getY2AxisProperties().isGridVisible())) {
            // On X,Y1
            res = AXISGRID_ONXANDY1;
        }
        else if ((chartProperties.getXAxisProperties().isGridVisible())
                && (!chartProperties.getY1AxisProperties().isGridVisible())
                && (chartProperties.getY2AxisProperties().isGridVisible())) {
            // On X,Y2
            res = AXISGRID_ONXANDY2;
        }
        return res;
    }

    public static void setAxisGrid(int axisGrid, ChartProperties chartProperties) {
        switch (axisGrid) {
            case AXISGRID_NONE:
                chartProperties.getXAxisProperties().setGridVisible(false);
                chartProperties.getY1AxisProperties().setGridVisible(false);
                chartProperties.getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONX:
                chartProperties.getXAxisProperties().setGridVisible(true);
                chartProperties.getY1AxisProperties().setGridVisible(false);
                chartProperties.getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONXANDY1:
                chartProperties.getXAxisProperties().setGridVisible(true);
                chartProperties.getY1AxisProperties().setGridVisible(true);
                chartProperties.getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONXANDY2:
                chartProperties.getXAxisProperties().setGridVisible(true);
                chartProperties.getY1AxisProperties().setGridVisible(false);
                chartProperties.getY2AxisProperties().setGridVisible(true);
                break;
            case AXISGRID_ONY1:
                chartProperties.getXAxisProperties().setGridVisible(false);
                chartProperties.getY1AxisProperties().setGridVisible(true);
                chartProperties.getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONY2:
                chartProperties.getXAxisProperties().setGridVisible(false);
                chartProperties.getY1AxisProperties().setGridVisible(false);
                chartProperties.getY2AxisProperties().setGridVisible(true);
                break;
            default:
                chartProperties.getXAxisProperties().setGridVisible(false);
                chartProperties.getY1AxisProperties().setGridVisible(false);
                chartProperties.getY2AxisProperties().setGridVisible(false);
                break;
        }
    }

    public static String toXMLLine(ChartProperties chartProperties) {
        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttributeIfNotNull(LEGENDS_ARE_VISIBLE_PROPERTY_XML_TAG, String
                .valueOf(chartProperties.isLegendsAreVisible()));
        openingLine.setAttributeIfNotNull(LEGENDS_PLACEMENT_PROPERTY_XML_TAG, String
                .valueOf(chartProperties.getLegendsPlacement()));
        openingLine.setAttribute(AUTO_HIGHLIGHT_ON_LEGEND_XML_TAG, String.valueOf(chartProperties
                .isAutoHighlightOnLegend()));
        openingLine.setAttributeIfNotNull(BACKGROUND_COLOR_PROPERTY_XML_TAG, GUIUtilities
                .colorToString(AwtColorTool.getColor(chartProperties.getBackgroundColor())));
        openingLine.setAttributeIfNotNull(HEADER_FONT_PROPERTY_XML_TAG, GUIUtilities
                .fontToString(AwtFontTool.getFont(chartProperties.getHeaderFont())));
        openingLine.setAttributeIfNotNull(LABEL_FONT_PROPERTY_XML_TAG, GUIUtilities
                .fontToString(AwtFontTool.getFont(chartProperties.getLabelFont())));
        openingLine.setAttributeIfNotNull(AXIS_GRID_PROPERTY_XML_TAG, String
                .valueOf(getAxisGrid(chartProperties)));
        openingLine.setAttributeIfNotNull(AXIS_GRID_STYLE_PROPERTY_XML_TAG, String
                .valueOf(chartProperties.getXAxisProperties().getGridStyle()));
        openingLine.setAttributeIfNotNull(TITLE_PROPERTY_XML_TAG, chartProperties.getTitle());
        openingLine.setAttributeIfNotNull(DISPLAY_DURATION_PROPERTY_XML_TAG, String
                .valueOf(chartProperties.getDisplayDuration()));
        openingLine.setAttributeIfNotNull(TIME_PRECISION_XML_TAG, String.valueOf(chartProperties
                .getTimePrecision()));
        openingLine.setAttributeIfNotNull(DATA_SENDING_ENABLED_XML_TAG, String
                .valueOf(chartProperties.isDataSendingEnabled()));

        return openingLine.toString();
    }

    public static ChartViewer getAsJLChart(ChartProperties chartProperties) {
        ChartViewer chart = new ChartViewer();
        IChartViewer chartviewerComponent = chart.getComponent();
        chartProperties.setNoValueString(Options.getInstance().getVcOptions().getNoValueString());
        chartviewerComponent.setChartProperties(chartProperties);
        chartviewerComponent.setMathExpressionEnabled(false);
        chartviewerComponent.setManagementPanelVisible(false);
        chartviewerComponent.setFreezePanelVisible(false);
        return chart;
    }

}
