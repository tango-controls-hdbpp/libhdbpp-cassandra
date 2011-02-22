// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/GeneralChartProperties.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class GeneralChartProperties.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: GeneralChartProperties.java,v $
// Revision 1.10 2007/08/24 09:02:44 ounsy
// Axis grid selection now corresponds to JLChart axis grid (Mantis bug 6129)
//
// Revision 1.9 2007/05/11 07:24:00 ounsy
// * minor bug correction with Fonts
//
// Revision 1.8 2007/05/10 14:48:16 ounsy
// possibility to change "no value" String in chart data file (default is "*")
// through vc option
//
// Revision 1.7 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.6 2007/01/11 14:05:45 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.5 2006/10/04 09:58:45 ounsy
// added a cloneGeneralChartProperties() method
//
// Revision 1.4 2006/07/05 12:58:58 ounsy
// VC : data synchronization management
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

import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.properties.ChartProperties;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.comete.widget.util.CometeFont;
import fr.soleil.mambo.containers.sub.dialogs.options.OptionsVCTab;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class GeneralChartProperties extends ChartProperties {

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

    public GeneralChartProperties() {
        super();
        getXAxisProperties().setAnnotation(IChartViewer.TIME_ANNO);
        setLegendsAreVisible(false);
        setAutoHighlightOnLegend(true);
    }

    public GeneralChartProperties(ChartProperties chartProperties) {
        super();
        if (chartProperties != null) {
            try {
                GeneralChartProperties clone = (GeneralChartProperties) chartProperties.clone();
                setLegendsAreVisible(clone.isLegendsAreVisible());
                setLegendsPlacement(clone.getLegendsPlacement());
                setBackgroundColor(clone.getBackgroundColor());
                setHeaderFont(clone.getHeaderFont());
                setLabelFont(clone.getLabelFont());
                setTitle(clone.getTitle());
                setDisplayDuration(clone.getDisplayDuration());
                setTimePrecision(clone.getTimePrecision());
                setNoValueString(clone.getNoValueString());
                setXAxisProperties(clone.getXAxisProperties());
                setY1AxisProperties(clone.getY1AxisProperties());
                setY2AxisProperties(clone.getY2AxisProperties());
                setDataSendingEnabled(clone.isDataSendingEnabled());
                setAutoHighlightOnLegend(clone.isAutoHighlightOnLegend());
            }
            catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public String toString() {

        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttributeIfNotNull(LEGENDS_ARE_VISIBLE_PROPERTY_XML_TAG, String
                .valueOf(isLegendsAreVisible()));
        openingLine.setAttributeIfNotNull(LEGENDS_PLACEMENT_PROPERTY_XML_TAG, String
                .valueOf(getLegendsPlacement()));
        openingLine.setAttribute(AUTO_HIGHLIGHT_ON_LEGEND_XML_TAG, String
                .valueOf(isAutoHighlightOnLegend()));
        openingLine.setAttributeIfNotNull(BACKGROUND_COLOR_PROPERTY_XML_TAG, GUIUtilities
                .colorToString(getBackgroundColor().getAwtColor()));
        openingLine.setAttributeIfNotNull(HEADER_FONT_PROPERTY_XML_TAG, GUIUtilities
                .fontToString(getHeaderFont().getAwtFont()));
        openingLine.setAttributeIfNotNull(LABEL_FONT_PROPERTY_XML_TAG, GUIUtilities
                .fontToString(getLabelFont().getAwtFont()));
        openingLine
                .setAttributeIfNotNull(AXIS_GRID_PROPERTY_XML_TAG, String.valueOf(getAxisGrid()));
        openingLine.setAttributeIfNotNull(AXIS_GRID_STYLE_PROPERTY_XML_TAG, String
                .valueOf(getXAxisProperties().getGridStyle()));
        openingLine.setAttributeIfNotNull(TITLE_PROPERTY_XML_TAG, getTitle());
        openingLine.setAttributeIfNotNull(DISPLAY_DURATION_PROPERTY_XML_TAG, String
                .valueOf(getDisplayDuration()));
        openingLine.setAttributeIfNotNull(TIME_PRECISION_XML_TAG, String
                .valueOf(getTimePrecision()));
        openingLine.setAttributeIfNotNull(DATA_SENDING_ENABLED_XML_TAG, String
                .valueOf(isDataSendingEnabled()));

        return openingLine.toString();
    }

    public ChartViewer getAsJLChart() {
        ChartViewer chart = new ChartViewer();

        IChartViewer chartviewerComponent = chart.getComponent();
        chartviewerComponent.setMathExpressionEnabled(false);
        chartviewerComponent.setManagementPanelVisible(false);
        chartviewerComponent.setFreezePanelVisible(false);
        chartviewerComponent.setChartProperties(this);
        chartviewerComponent.setNoValueString(OptionsVCTab.getInstance().getNoValueString());
        return chart;
    }

    public GeneralChartProperties cloneGeneralChartProperties() {
        GeneralChartProperties ret = new GeneralChartProperties();

        ret.setBackgroundColor(this.getBackgroundColor() == null ? null : new CometeColor(this
                .getBackgroundColor().getRed(), this.getBackgroundColor().getGreen(), this
                .getBackgroundColor().getBlue()));
        ret.setDisplayDuration(this.getDisplayDuration());
        ret.setHeaderFont(this.getHeaderFont() == null ? null : new CometeFont(this.getHeaderFont()
                .getName(), this.getHeaderFont().getStyle(), this.getHeaderFont().getSize()));
        ret.setLabelFont(this.getLabelFont() == null ? null : new CometeFont(this.getLabelFont()
                .getName(), this.getLabelFont().getStyle(), this.getLabelFont().getSize()));
        ret.setLegendsAreVisible(isLegendsAreVisible());
        ret.setLegendsPlacement(getLegendsPlacement());
        ret.setNoValueString(getNoValueString());
        ret.setTimePrecision(getTimePrecision());
        ret.setTitle(this.getTitle() == null ? null : new String(this.getTitle()));
        ret.setXAxisProperties(getXAxisProperties());
        ret.setY1AxisProperties(getY1AxisProperties());
        ret.setY2AxisProperties(getY2AxisProperties());
        ret.setAutoHighlightOnLegend(isAutoHighlightOnLegend());

        return ret;
    }

    public void setGeneralChartProperties(ChartProperties generalChartProperties) {
        if (generalChartProperties != null) {
            try {
                ChartProperties clone = (ChartProperties) generalChartProperties.clone();
                setLegendsAreVisible(clone.isLegendsAreVisible());
                setLegendsPlacement(clone.getLegendsPlacement());
                setBackgroundColor(clone.getBackgroundColor());
                setHeaderFont(clone.getHeaderFont());
                setLabelFont(clone.getLabelFont());
                setTitle(clone.getTitle());
                setDisplayDuration(clone.getDisplayDuration());
                setTimePrecision(clone.getTimePrecision());
                setNoValueString(clone.getNoValueString());
                setXAxisProperties(clone.getXAxisProperties());
                setY1AxisProperties(clone.getY1AxisProperties());
                setY2AxisProperties(clone.getY2AxisProperties());
                setDataSendingEnabled(clone.isDataSendingEnabled());
                setAutoHighlightOnLegend(clone.isAutoHighlightOnLegend());
            }
            catch (CloneNotSupportedException e) {
                // should never happen
                e.printStackTrace();
            }
        }
    }

    public int getAxisGrid() {
        int res = AXISGRID_NONE;
        if ((getXAxisProperties().isGridVisible()) && (!getY1AxisProperties().isGridVisible())
                && (!getY2AxisProperties().isGridVisible())) {
            // On X
            res = AXISGRID_ONX;
        }
        else if ((!getXAxisProperties().isGridVisible()) && (getY1AxisProperties().isGridVisible())
                && (!getY2AxisProperties().isGridVisible())) {
            // On Y1
            res = AXISGRID_ONY1;
        }
        else if ((!getXAxisProperties().isGridVisible())
                && (!getY1AxisProperties().isGridVisible())
                && (getY2AxisProperties().isGridVisible())) {
            // On Y2
            res = AXISGRID_ONY2;
        }
        else if ((getXAxisProperties().isGridVisible()) && (getY1AxisProperties().isGridVisible())
                && (!getY2AxisProperties().isGridVisible())) {
            // On X,Y1
            res = AXISGRID_ONXANDY1;
        }
        else if ((getXAxisProperties().isGridVisible()) && (!getY1AxisProperties().isGridVisible())
                && (getY2AxisProperties().isGridVisible())) {
            // On X,Y2
            res = AXISGRID_ONXANDY2;
        }
        return res;
    }

    public void setAxisGrid(int axisGrid) {
        switch (axisGrid) {
            case AXISGRID_NONE:
                getXAxisProperties().setGridVisible(false);
                getY1AxisProperties().setGridVisible(false);
                getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONX:
                getXAxisProperties().setGridVisible(true);
                getY1AxisProperties().setGridVisible(false);
                getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONXANDY1:
                getXAxisProperties().setGridVisible(true);
                getY1AxisProperties().setGridVisible(true);
                getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONXANDY2:
                getXAxisProperties().setGridVisible(true);
                getY1AxisProperties().setGridVisible(false);
                getY2AxisProperties().setGridVisible(true);
                break;
            case AXISGRID_ONY1:
                getXAxisProperties().setGridVisible(false);
                getY1AxisProperties().setGridVisible(true);
                getY2AxisProperties().setGridVisible(false);
                break;
            case AXISGRID_ONY2:
                getXAxisProperties().setGridVisible(false);
                getY1AxisProperties().setGridVisible(false);
                getY2AxisProperties().setGridVisible(true);
                break;
            default:
                getXAxisProperties().setGridVisible(false);
                getY1AxisProperties().setGridVisible(false);
                getY2AxisProperties().setGridVisible(false);
                break;
        }
    }
}
