// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/view/plot/YAxis.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class YAxis.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: YAxis.java,v $
// Revision 1.6 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.5 2006/10/04 09:58:31 ounsy
// adedd a cloneAxis() method
//
// Revision 1.4 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.3 2006/05/16 12:00:45 ounsy
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
import java.util.ArrayList;

import chart.temp.chart.JLAxis;
import chart.temp.chart.JLDataView;

import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class YAxis extends Axis {

    private int                position;

    public static final String XML_TAG_1                      = "Y1Axis";
    public static final String XML_TAG_2                      = "Y2Axis";
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

    public String toString(boolean isY1) {
        // String ret = "";

        String tag = isY1 ? XML_TAG_1 : XML_TAG_2;
        XMLLine openingLine = new XMLLine(tag, XMLLine.EMPTY_TAG_CATEGORY);

        openingLine.setAttributeIfNotNull(POSITION_PROPERTY_XML_TAG, String
                .valueOf(position));
        openingLine.setAttributeIfNotNull(SCALE_MIN_PROPERTY_XML_TAG, String
                .valueOf(scaleMin));
        openingLine.setAttributeIfNotNull(SCALE_MAX_PROPERTY_XML_TAG, String
                .valueOf(scaleMax));
        openingLine.setAttributeIfNotNull(SCALE_MODE_PROPERTY_XML_TAG, String
                .valueOf(scaleMode));
        openingLine.setAttributeIfNotNull(AUTO_SCALE_PROPERTY_XML_TAG, String
                .valueOf(autoScale));
        openingLine.setAttributeIfNotNull(LABEL_FORMAT_PROPERTY_XML_TAG, String
                .valueOf(labelFormat));
        openingLine.setAttributeIfNotNull(COLOR_PROPERTY_XML_TAG, GUIUtilities
                .colorToString(color));
        openingLine.setAttributeIfNotNull(SHOW_SUB_GRID_PROPERTY_XML_TAG,
                String.valueOf(showSubGrid));
        openingLine.setAttributeIfNotNull(IS_VISIBLE_PROPERTY_XML_TAG, String
                .valueOf(isVisible));
        openingLine.setAttributeIfNotNull(DRAW_OPPOSITE_PROPERTY_XML_TAG,
                String.valueOf(drawOpposite));
        openingLine.setAttributeIfNotNull(TITLE_PROPERTY_XML_TAG, title);

        return openingLine.toString();
    }

    public YAxis() {
        super();
    }

    /**
     * @return Returns the position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position
     *            The position to set.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    public void setupJLAxis(JLAxis in, boolean gridVisible, int gridStyle) {
        ArrayList<JLDataView> toRemove = new ArrayList<JLDataView>();
        toRemove.addAll(in.getViewsCopy());
        for (int i = 0; i < toRemove.size(); i++) {
            in.removeDataView(toRemove.get(i));
        }
        toRemove.clear();
        toRemove = null;

        in.setAutoScale(this.autoScale);
        in.setAxisColor(this.color);
        in.setDrawOpposite(this.drawOpposite);
        in.setLabelFormat(this.labelFormat);
        in.setPosition(this.position);
        in.setScale(this.scaleMode);
        in.setSubGridVisible(this.showSubGrid);
        in.setVisible(this.isVisible);
        in.setName(this.title);
        if (this.scaleMax > this.scaleMin) {
            in.setMinimum(this.scaleMin);
            in.setMaximum(this.scaleMax);
        }

        in.setGridStyle(gridStyle);
        in.setGridVisible(gridVisible);

    }

    public void setupJLAxis(IChartViewer chartViewerComponent, int axis,
            boolean gridVisible, int GridStyle) {
        // ArrayList<JLDataView> toRemove = new ArrayList<JLDataView>();
        // toRemove.addAll(in.getViews());
        // for (int i = 0; i < toRemove.size(); i++) {
        // in.removeDataView(toRemove.get(i));
        // }
        // toRemove.clear();
        // toRemove = null;
        chartViewerComponent.setAutoScale(this.autoScale, axis);
        chartViewerComponent.setAxisCometeColor(CometeColor
                .getColor(this.color), axis);
        chartViewerComponent.setAxisDrawOpposite(this.drawOpposite, axis);
        chartViewerComponent.setAxisLabelFormat(this.labelFormat, axis);
        chartViewerComponent.setAxisPosition(this.position, axis);
        // in.setDrawOpposite(this.drawOpposite);
        // in.setLabelFormat(this.labelFormat);
        // in.setPosition(this.position);
        chartViewerComponent.setScale(this.scaleMode, axis);
        chartViewerComponent.setSubGridVisible(this.showSubGrid, axis);
        chartViewerComponent.setAxisVisible(this.isVisible, axis);
        // in.setVisible(this.isVisible);
        chartViewerComponent.setAxisName(this.title, axis);

        if (this.scaleMax > this.scaleMin) {
            chartViewerComponent.setAxisMaximum(this.scaleMax, axis);
            chartViewerComponent.setAxisMinimum(this.scaleMin, axis);
        }
        // if (this.scaleMax > this.scaleMin) {
        // in.setMinimum(this.scaleMin);
        // in.setMaximum(this.scaleMax);
        // }
        chartViewerComponent.setGridStyle(GridStyle, axis);
        chartViewerComponent.setGridVisible(gridVisible, axis);
    }

    public YAxis cloneAxis() {
        YAxis ret = new YAxis();

        ret.setAutoScale(this.isAutoScale());
        ret.setColor(this.getColor() == null ? null : new Color(this.getColor()
                .getRGB()));
        ret.setDrawOpposite(this.isDrawOpposite());
        ret.setLabelFormat(this.getLabelFormat());
        ret.setPosition(this.getPosition());
        ret.setScaleMax(this.getScaleMax());
        ret.setScaleMin(this.getScaleMin());
        ret.setScaleMode(this.getScaleMode());
        ret.setShowSubGrid(this.isShowSubGrid());
        ret.setTitle(this.getTitle() == null ? null : new String(this
                .getTitle()));
        ret.setVisible(this.isVisible());

        return ret;
    }
}
