// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/data/view/ViewConfigurationAttributes.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewConfigurationAttributes.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.20 $
//
// $Log: ViewConfigurationAttributes.java,v $
// Revision 1.20 2007/03/28 13:24:45 ounsy
// boolean and number scalars displayed on the same panel (Mantis bug 4428)
//
// Revision 1.19 2007/03/20 14:15:09 ounsy
// added the possibility to hide a dataview
//
// Revision 1.18 2007/02/01 14:21:46 pierrejoseph
// XmlHelper reorg
//
// Revision 1.17 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.16 2006/11/14 09:35:55 ounsy
// color rotation bug correction
//
// Revision 1.15 2006/11/09 14:23:42 ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.14 2006/11/07 14:35:17 ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.13 2006/10/19 09:21:21 ounsy
// now the color rotation index of vc attributes is saved
//
// Revision 1.12 2006/09/22 09:34:41 ounsy
// refactoring du package mambo.datasources.db
//
// Revision 1.11 2006/09/05 14:03:12 ounsy
// updated for sampling compatibility
//
// Revision 1.10 2006/08/31 08:37:41 ounsy
// minor changes
//
// Revision 1.9 2006/08/29 14:19:27 ounsy
// now the view dialog is filled through a thread. Closing the dialog will kill
// this thread.
//
// Revision 1.8 2006/08/23 10:03:04 ounsy
// avoided a concurrent modification exception
//
// Revision 1.7 2006/08/07 13:03:07 ounsy
// trees and lists sort
//
// Revision 1.6 2006/07/18 10:27:49 ounsy
// Less time consuming by setting tree expanding on demand only +
// getImageAttributes
//
// Revision 1.5 2006/04/05 13:47:49 ounsy
// get String, state and boolean scalar attributes
//
// Revision 1.4 2006/03/10 12:03:25 ounsy
// state and string support
//
// Revision 1.3 2006/02/01 14:11:19 ounsy
// spectrum management
//
// Revision 1.2 2005/11/29 18:27:07 chinkumo
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
package fr.soleil.mambo.data.view;

import java.awt.Color;
import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import fr.esrf.Tango.DevFailed;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.comete.widget.ChartViewer;
import fr.soleil.comete.widget.IChartViewer;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.containers.view.dialogs.AttributesPlotPropertiesPanel;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Interpolation;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.MathPlot;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.data.view.plot.Smoothing;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ViewConfigurationAttributes {

    private TreeMap<String, ViewConfigurationAttribute> attributes;
    private int                                         colorIndex                = 0;
    public final static String                          COLOR_INDEX_XML_TAG       = "MamboVCAttributesColorIndex";
    public final static String                          COLOR_INDEX_XML_ATTRIBUTE = "VALUE";
    private ViewConfiguration                           viewConfiguration;

    @Override
    public String toString() {
        String ret = "";

        XMLLine colorIndexEmptyLine = new XMLLine(COLOR_INDEX_XML_TAG,
                XMLLine.EMPTY_TAG_CATEGORY);
        colorIndexEmptyLine.setAttribute(COLOR_INDEX_XML_ATTRIBUTE, Integer
                .toString(colorIndex));
        ret += colorIndexEmptyLine.toString();

        if (attributes != null) {
            Set<String> keySet = attributes.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            int i = 0;
            while (keyIterator.hasNext()) {
                String nextKey = keyIterator.next();
                ViewConfigurationAttribute nextValue = attributes.get(nextKey);

                ret += nextValue.toString();
                if (i < attributes.size() - 1) {
                    ret += GUIUtilities.CRLF;
                }

                i++;
            }
        }

        return ret;
    }

    public boolean equals(ViewConfigurationAttributes this2) {
        return this.attributes.size() == this2.attributes.size();
    }

    public ViewConfigurationAttributes() {
        this.attributes = new TreeMap<String, ViewConfigurationAttribute>(
                Collator.getInstance());
    }

    public void addAttribute(ViewConfigurationAttribute _attribute) {
        String completeName = _attribute.getCompleteName();

        attributes.put(completeName, _attribute);
    }

    public ViewConfigurationAttribute getAttribute(String completeName) {
        return attributes.get(completeName);
    }

    /**
     * @return Returns the attributes.
     */
    public TreeMap<String, ViewConfigurationAttribute> getAttributes() {
        return attributes;
    }

    /**
     * Returns the spectrum attributes
     * 
     * @param historic
     *            to know in which database you want to check if the attributes
     *            are spectrum or not.
     * @return The spectrum attributes
     */
    public TreeMap<String, ViewConfigurationAttribute> getSpectrumAttributes(
            boolean historic) {
        TreeMap<String, ViewConfigurationAttribute> spectrums = new TreeMap<String, ViewConfigurationAttribute>(
                Collator.getInstance());
        if (attributes != null) {
            Set<String> keySet = attributes.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            while (keyIterator.hasNext()) {
                String nextKey = keyIterator.next();
                ViewConfigurationAttribute nextValue = attributes.get(nextKey);
                if (nextValue.isSpectrum(historic)) {
                    spectrums.put(nextKey, nextValue);
                }
            }
        }
        return spectrums;
    }

    /**
     * Returns the string and state scalar attributes
     * 
     * @param historic
     *            to know in which database you want to check the attributes
     * @return The string scalar attributes
     */
    public TreeMap<String, ViewConfigurationAttribute> getStringStateScalarAttributes(
            boolean historic) {
        TreeMap<String, ViewConfigurationAttribute> strings = new TreeMap<String, ViewConfigurationAttribute>(
                Collator.getInstance());
        if (attributes != null) {
            Set<String> keySet = attributes.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            while (keyIterator.hasNext()) {
                String nextKey = keyIterator.next();
                ViewConfigurationAttribute nextValue = attributes.get(nextKey);
                if (nextValue.isScalar(historic)) {
                    if (nextValue.getDataType(historic) == TangoConst.Tango_DEV_STRING
                            || nextValue.getDataType(historic) == TangoConst.Tango_DEV_STATE) {
                        strings.put(nextKey, nextValue);
                    }
                }
            }
        }
        return strings;
    }

    /**
     * Returns the image attributes
     * 
     * @param historic
     *            to know in which database you want to check the attributes
     * @return The image attributes
     */
    public TreeMap<String, ViewConfigurationAttribute> getImageAttributes(
            boolean historic) {
        TreeMap<String, ViewConfigurationAttribute> images = new TreeMap<String, ViewConfigurationAttribute>(
                Collator.getInstance());
        if (attributes != null) {
            Set<String> keySet = attributes.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            while (keyIterator.hasNext()) {
                String nextKey = keyIterator.next();
                ViewConfigurationAttribute nextValue = attributes.get(nextKey);
                if (nextValue.isImage(historic)) {
                    images.put(nextKey, nextValue);
                }
            }
        }
        return images;
    }

    /**
     * @param attributes
     *            The attributes to set.
     */
    public void setAttributes(
            TreeMap<String, ViewConfigurationAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @param chart
     * @param historic
     * @param samplingType
     * @param string2
     * @param
     * @return 29 ao�t 2005
     * @throws ParseException
     * @throws DevFailed
     */
    public ChartViewer setChartAttributes(ChartViewer chart, String startDate,
            String endDate, boolean historic, SamplingType samplingType)
            throws DevFailed, ParseException {
        IExtractingManager extractingManager = ExtractingManagerFactory
                .getCurrentImpl();

        if (extractingManager.isCanceled())
            return chart;
        if (attributes != null) {
            Set<String> keySet = attributes.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            while (keyIterator.hasNext()) {
                if (extractingManager.isCanceled())
                    return chart;
                String nextKey = keyIterator.next();
                ViewConfigurationAttribute nextValue = attributes.get(nextKey);
                if (nextValue.isScalar(historic)) {
                    switch (nextValue.getDataType(historic)) {
                        case TangoConst.Tango_DEV_BOOLEAN:
                        case TangoConst.Tango_DEV_CHAR:
                        case TangoConst.Tango_DEV_UCHAR:
                        case TangoConst.Tango_DEV_LONG:
                        case TangoConst.Tango_DEV_ULONG:
                        case TangoConst.Tango_DEV_SHORT:
                        case TangoConst.Tango_DEV_USHORT:
                        case TangoConst.Tango_DEV_FLOAT:
                        case TangoConst.Tango_DEV_DOUBLE:
                            nextValue.addToChart(chart, startDate, endDate,
                                    historic, samplingType);
                            break;
                    }
                }
                // chart.repaint();
            }
            if (viewConfiguration != null) {
                TreeMap<String, ExpressionAttribute> expressions = viewConfiguration
                        .getExpressions();

                keySet = expressions.keySet();
                keyIterator = keySet.iterator();

                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    ExpressionAttribute attr = expressions.get(key);
                    if (attr.getVariables() != null) {
                        int axis = -1;
                        switch (attr.getProperties().getAxisChoice()) {
                            case ViewConfigurationAttributePlotProperties.AXIS_CHOICE_Y1:
                                axis = IChartViewer.Y1;
                                break;
                            case ViewConfigurationAttributePlotProperties.AXIS_CHOICE_Y2:
                                axis = IChartViewer.Y2;
                                break;
                            case ViewConfigurationAttributePlotProperties.AXIS_CHOICE_X:
                                axis = IChartViewer.X;
                                break;
                        }
                        attr.prepareView(chart);
                        chart.getComponent().addExpression(attr.getId(),
                                attr.getExpression(), axis,
                                attr.getVariables(), attr.isX());
                    }
                }
            }
        }
        return chart;
    }

    /**
     * @param attrs
     *            1 sept. 2005
     */
    public void removeAttributesNotInList(TreeMap<String, ?> attrs) {
        Set<String> keySet = this.attributes.keySet();
        ArrayList<String> toRemove = new ArrayList<String>();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            String next = keyIterator.next();
            if (!attrs.containsKey(next)) {
                toRemove.add(next);
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            attributes.remove(toRemove.get(i));
        }
        toRemove.clear();
        toRemove = null;
    }

    /**
     * @param attrs
     *            1 sept. 2005
     */
    public void addAttributes(TreeMap<String, ViewConfigurationAttribute> attrs) {
        Set<String> keySet = attrs.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            String nextKey = keyIterator.next();
            ViewConfigurationAttribute nextValue = attrs.get(nextKey);

            boolean needsColor = false;
            if (!this.attributes.containsKey(nextKey)) {
                this.attributes.put(nextKey, nextValue);

                needsColor = true;
                ViewConfigurationAttributeProperties currentProperties = new ViewConfigurationAttributeProperties();
                nextValue.setProperties(currentProperties);
            } else {
                ViewConfigurationAttributePlotProperties plotProperties = nextValue
                        .getProperties().getPlotProperties();
                if (plotProperties.getCurve() == null) {
                    needsColor = true;
                }
            }
            if (needsColor) {
                int viewType = 0;
                int axisChoice = 0;
                int spectrumViewType = Options.getInstance().getVcOptions()
                        .getSpectrumViewType();

                Color color = AttributesPlotPropertiesPanel
                        .getColorFor(colorIndex);
                colorIndex = AttributesPlotPropertiesPanel
                        .getDefaultNextColorIndex(colorIndex);

                Bar bar = AttributesPlotPropertiesPanel.getDefaultBar(color);
                Curve curve = AttributesPlotPropertiesPanel.getDefaultCurve(
                        color, nextValue.getName());
                Marker marker = AttributesPlotPropertiesPanel
                        .getDefaultMarker(color);
                Polynomial2OrderTransform transform = AttributesPlotPropertiesPanel
                        .getDefaultTransform();
                Interpolation interpolation = AttributesPlotPropertiesPanel
                        .getDefaultInterpolation();
                Smoothing smoothing = AttributesPlotPropertiesPanel
                        .getDefaultSmoothing();
                MathPlot math = AttributesPlotPropertiesPanel.getDefaultMath();
                boolean hidden = false;
                ViewConfigurationAttributeProperties currentProperties = nextValue
                        .getProperties();
                ViewConfigurationAttributePlotProperties currentPlotProperties = new ViewConfigurationAttributePlotProperties(
                        viewType, axisChoice, bar, curve, marker, transform,
                        interpolation, smoothing, math, spectrumViewType,
                        hidden);
                currentProperties.setPlotProperties(currentPlotProperties);
                nextValue.setProperties(currentProperties);
                bar = null;
                curve = null;
                marker = null;
                transform = null;
                interpolation = null;
                smoothing = null;
                math = null;
                currentProperties = null;
                currentPlotProperties = null;
            }
        }
    }

    /**
     * @return
     */
    @SuppressWarnings("unchecked")
    public ViewConfigurationAttributes cloneAttrs() {
        ViewConfigurationAttributes ret = new ViewConfigurationAttributes();

        ret.setAttributes((TreeMap<String, ViewConfigurationAttribute>) this
                .getAttributes().clone());
        ret.setColorIndex(colorIndex);

        return ret;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public void setViewConfiguration(ViewConfiguration viewConfiguration) {
        this.viewConfiguration = viewConfiguration;
    }

}
