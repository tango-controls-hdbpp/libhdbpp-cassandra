// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfigurationAttributePlotProperties.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class
// ViewConfigurationAttributePlotProperties.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: ViewConfigurationAttributePlotProperties.java,v $
// Revision 1.8 2007/03/20 14:15:09 ounsy
// added the possibility to hide a dataview
//
// Revision 1.7 2007/02/27 10:05:36 ounsy
// minor changes
//
// Revision 1.6 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.5 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.4 2006/09/27 07:00:16 chinkumo
// correction for Mantis 2230
//
// Revision 1.3 2006/09/20 13:03:29 chinkumo
// Mantis 2230: Separated Set addition in the "Attributes plot properties" tab
// of the New/Modify VC View
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
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
package fr.soleil.mambo.data.view;

import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfigurationAttributePlotProperties {

    // private boolean isEmpty = true;

    private int                       viewType;
    private int                       axisChoice;
    private int                       spectrumViewType;
    private boolean                   hidden                              = false;

    private Bar                       bar;
    private Curve                     curve;
    private Marker                    marker;
    private Polynomial2OrderTransform transform;

    public static final String        XML_TAG                             = "plotParams";
    public static final String        VIEW_TYPE_PROPERTY_XML_TAG          = "viewType";
    public static final String        AXIS_CHOICE_PROPERTY_XML_TAG        = "axisChoice";
    public static final String        SPECTRUM_VIEW_TYPE_PROPERTY_XML_TAG = "spectrumViewType";
    public static final String        HIDDEN_PROPERTY_XML_TAG             = "hidden";

    public static final int           VIEW_TYPE_LINE                      = 0;
    public static final int           VIEW_TYPE_BAR                       = 1;
    public static final int           AXIS_CHOICE_Y1                      = 0;
    public static final int           AXIS_CHOICE_Y2                      = 1;
    public static final int           AXIS_CHOICE_X                       = 2;

    public static final int           SPECTRUM_VIEW_TYPE_INDICE           = 0;
    public static final int           SPECTRUM_VIEW_TYPE_TIME             = 1;
    public static final int           SPECTRUM_VIEW_TYPE_TIME_STACK       = 2;

    public String toString() {
        String ret = "";

        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
        openingLine.setAttribute(VIEW_TYPE_PROPERTY_XML_TAG, String
                .valueOf(this.viewType));
        openingLine.setAttribute(AXIS_CHOICE_PROPERTY_XML_TAG, String
                .valueOf(this.axisChoice));
        openingLine.setAttribute(SPECTRUM_VIEW_TYPE_PROPERTY_XML_TAG, String
                .valueOf(this.spectrumViewType));
        openingLine.setAttribute(HIDDEN_PROPERTY_XML_TAG, String
                .valueOf(this.hidden));
        XMLLine closingLine = new XMLLine(XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if (bar != null) {
            ret += bar.toString();
            ret += GUIUtilities.CRLF;
        }
        if (curve != null) {
            ret += curve.toString();
            ret += GUIUtilities.CRLF;
        }
        if (marker != null) {
            ret += marker.toString();
            ret += GUIUtilities.CRLF;
        }
        if (transform != null) {
            ret += transform.toString();
            ret += GUIUtilities.CRLF;
        }

        ret += closingLine.toString();

        return ret;
    }

    public ViewConfigurationAttributePlotProperties() {
        // isEmpty = true;
    }

    public ViewConfigurationAttributePlotProperties(int _viewType,
            int _axisChoice, int _spectrumViewType, Bar _bar, Curve _curve,
            Marker _marker, Polynomial2OrderTransform _transform, boolean hidden) {
        this.viewType = _viewType;
        this.axisChoice = _axisChoice;
        this.spectrumViewType = _spectrumViewType;

        this.bar = _bar;
        this.curve = _curve;
        this.marker = _marker;
        this.transform = _transform;
        this.hidden = hidden;
    }

    /**
     * @return Returns the bar.
     */
    public Bar getBar() {
        return bar;
    }

    /**
     * @param bar
     *            The bar to set.
     */
    public void setBar(Bar bar) {
        this.bar = bar;
    }

    /**
     * @return Returns the curve.
     */
    public Curve getCurve() {
        return curve;
    }

    /**
     * @param curve
     *            The curve to set.
     */
    public void setCurve(Curve curve) {
        this.curve = curve;
    }

    /**
     * @return Returns the transform.
     */
    public Polynomial2OrderTransform getTransform() {
        return transform;
    }

    /**
     * @param transform
     *            The transform to set.
     */
    public void setTransform(Polynomial2OrderTransform transform) {
        this.transform = transform;
    }

    /**
     * @return Returns the viewType.
     */
    public int getViewType() {
        return viewType;
    }

    /**
     * @param viewType
     *            The viewType to set.
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    /**
     * @return Returns the marker.
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     * @param marker
     *            The marker to set.
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    /**
     * @return 22 ao�t 2005
     */
    public boolean isEmpty() {
        boolean markerIsEmpty = (this.marker == null || this.marker.isEmpty());
        boolean curveIsEmpty = (this.curve == null || this.curve.isEmpty());
        boolean transformIsEmpty = (this.transform == null || this.transform
                .isEmpty());
        boolean barIsEmpty = (this.bar == null || this.bar.isEmpty());

        return markerIsEmpty && curveIsEmpty && transformIsEmpty && barIsEmpty;
    }

    /**
     * @return Returns the axisChoice.
     */
    public int getAxisChoice() {
        return axisChoice;
    }

    /**
     * @param axisChoice
     *            The axisChoice to set.
     */
    public void setAxisChoice(int axisChoice) {
        this.axisChoice = axisChoice;
    }

    /**
     * @return Returns the spectrumViewType.
     */
    public int getSpectrumViewType() {
        return spectrumViewType;
    }

    /**
     * @param spectrumViewType
     *            The spectrumViewType to set.
     */
    public void setSpectrumViewType(int spectrumViewType) {
        this.spectrumViewType = spectrumViewType;
    }

    /**
     * @param theView
     * @return 30 ao�t 2005
     */
    public JLDataView applyProperties(JLDataView in) {
        switch (this.viewType) {
            case VIEW_TYPE_LINE:
                in.setViewType(JLDataView.TYPE_LINE);
                break;

            case VIEW_TYPE_BAR:
                in.setViewType(JLDataView.TYPE_BAR);
                break;
        }

        if (bar != null) {
            in = bar.applyProperties(in);
        }
        if (curve != null) {
            in = curve.applyProperties(in);
        }
        if (marker != null) {
            in = marker.applyProperties(in);
        }
        if (transform != null) {
            in = transform.applyProperties(in);
        }

        if (hidden) {
            in.setLineWidth(0);
            in.setMarker(JLDataView.MARKER_NONE);
            in.setBarWidth(0);
            in.setFill(false);
            in.setLabelVisible(false);
            in.setClickable(false);
        }

        return in;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
