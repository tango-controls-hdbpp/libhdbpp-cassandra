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

import fr.soleil.comete.widget.properties.BarProperties;
import fr.soleil.comete.widget.properties.CurveProperties;
import fr.soleil.comete.widget.properties.InterpolationProperties;
import fr.soleil.comete.widget.properties.MarkerProperties;
import fr.soleil.comete.widget.properties.MathProperties;
import fr.soleil.comete.widget.properties.PlotProperties;
import fr.soleil.comete.widget.properties.SmoothingProperties;
import fr.soleil.comete.widget.properties.TransformationProperties;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Interpolation;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.MathPlot;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.data.view.plot.Smoothing;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfigurationAttributePlotProperties extends PlotProperties {

    private int                spectrumViewType;
    private boolean            hidden;
    private boolean            isEmpty;

    public static final String XML_TAG                             = "plotParams";
    public static final String VIEW_TYPE_PROPERTY_XML_TAG          = "viewType";
    public static final String AXIS_CHOICE_PROPERTY_XML_TAG        = "axisChoice";
    public static final String SPECTRUM_VIEW_TYPE_PROPERTY_XML_TAG = "spectrumViewType";
    public static final String HIDDEN_PROPERTY_XML_TAG             = "hidden";

    public static final int    VIEW_TYPE_LINE                      = 0;
    public static final int    VIEW_TYPE_BAR                       = 1;
    public static final int    AXIS_CHOICE_Y1                      = 0;
    public static final int    AXIS_CHOICE_Y2                      = 1;
    public static final int    AXIS_CHOICE_X                       = 2;

    public static final int    SPECTRUM_VIEW_TYPE_INDEX            = 0;
    public static final int    SPECTRUM_VIEW_TYPE_TIME             = 1;
    public static final int    SPECTRUM_VIEW_TYPE_TIME_STACK       = 2;

    public ViewConfigurationAttributePlotProperties() {
        super();
        isEmpty = true;
    }

    public ViewConfigurationAttributePlotProperties(int _viewType,
            int _axisChoice, BarProperties _bar, CurveProperties _curve,
            MarkerProperties _marker, TransformationProperties _transform,
            InterpolationProperties interpolation,
            SmoothingProperties smoothing, MathProperties math,
            int _spectrumViewType, boolean _hidden) {
        super(_viewType, _axisChoice, _bar, _curve, _marker, _transform,
                interpolation, smoothing, math);
        this.hidden = _hidden;
        this.spectrumViewType = _spectrumViewType;
        isEmpty = false;
    }

    public ViewConfigurationAttributePlotProperties(int viewType,
            int axisChoice, PlotProperties plotProperties,
            int spectrumViewType, boolean hidden) {
        super();
        if (plotProperties != null) {
            try {
                PlotProperties clone = (PlotProperties) plotProperties.clone();
                setViewType(viewType);
                setAxisChoice(axisChoice);
                setBar(clone.getBar());
                setCurve(clone.getCurve());
                setHidden(hidden);
                setInterpolation(clone.getInterpolation());
                setMarker(clone.getMarker());
                setMath(clone.getMath());
                setSmoothing(clone.getSmoothing());
                setSpectrumViewType(spectrumViewType);
                setTransform(clone.getTransform());
                setSpectrumViewType(spectrumViewType);
            } catch (CloneNotSupportedException e) {
                // Should never happens
                e.printStackTrace();
            }

        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public String toString() {
        String ret = "";

        XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
        openingLine.setAttribute(VIEW_TYPE_PROPERTY_XML_TAG, String
                .valueOf(getViewType()));
        openingLine.setAttribute(AXIS_CHOICE_PROPERTY_XML_TAG, String
                .valueOf(this.getAxisChoice()));
        openingLine.setAttribute(SPECTRUM_VIEW_TYPE_PROPERTY_XML_TAG, String
                .valueOf(this.spectrumViewType));
        openingLine.setAttribute(HIDDEN_PROPERTY_XML_TAG, String
                .valueOf(this.hidden));
        XMLLine closingLine = new XMLLine(XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

        ret += openingLine.toString();
        ret += GUIUtilities.CRLF;

        if (getBar() != null) {
            ret += new Bar(getBar()).toString();
            ret += GUIUtilities.CRLF;
        }
        if (getCurve() != null) {
            ret += new Curve(getCurve()).toString();
            ret += GUIUtilities.CRLF;
        }
        if (getMarker() != null) {
            ret += new Marker(getMarker()).toString();
            ret += GUIUtilities.CRLF;
        }
        if (getTransform() != null) {
            ret += new Polynomial2OrderTransform(getTransform()).toString();
            ret += GUIUtilities.CRLF;
        }
        if (getInterpolation() != null) {
            ret += new Interpolation(getInterpolation()).toString();
            ret += GUIUtilities.CRLF;
        }
        if (getSmoothing() != null) {
            ret += new Smoothing(getSmoothing()).toString();
            ret += GUIUtilities.CRLF;
        }
        if (getMath() != null) {
            ret += new MathPlot(getMath()).toString();
            ret += GUIUtilities.CRLF;
        }

        ret += closingLine.toString();

        return ret;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        isEmpty = false;
        this.hidden = hidden;
    }

    public int getSpectrumViewType() {
        return spectrumViewType;
    }

    public void setSpectrumViewType(int spectrumViewType) {
        isEmpty = false;
        this.spectrumViewType = spectrumViewType;
    }

    public void setPlotProperties(PlotProperties plotProterties) {
        if (plotProterties != null) {
            try {
                PlotProperties clone = (PlotProperties) plotProterties.clone();
                setViewType(clone.getViewType());
                setAxisChoice(clone.getAxisChoice());
                setBar(clone.getBar());
                setCurve(clone.getCurve());
                setInterpolation(clone.getInterpolation());
                setMarker(clone.getMarker());
                setMath(clone.getMath());
                setSmoothing(clone.getSmoothing());
                setTransform(clone.getTransform());
            } catch (CloneNotSupportedException e) {
                // Should never happens
                e.printStackTrace();
            }

        }
    }
}
