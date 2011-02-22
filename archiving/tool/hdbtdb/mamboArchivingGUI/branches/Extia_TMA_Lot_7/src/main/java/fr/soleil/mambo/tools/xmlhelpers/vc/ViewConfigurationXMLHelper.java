// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/tools/xmlhelpers/vc/ViewConfigurationXMLHelper.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class ViewConfigurationXMLHelper.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ViewConfigurationXMLHelper.java,v $
// Revision 1.3 2007/03/27 13:40:53 ounsy
// Better Font management (correction of Mantis bug 4302)
//
// Revision 1.2 2007/03/20 14:15:09 ounsy
// added the possibility to hide a dataview
//
// Revision 1.1 2007/02/01 14:12:36 pierrejoseph
// XmlHelper reorg
//
// Revision 1.7 2006/12/06 15:12:06 ounsy
// added parametrisable sampling
//
// Revision 1.6 2006/10/19 09:21:21 ounsy
// now the color rotation index of vc attributes is saved
//
// Revision 1.5 2006/09/05 14:12:14 ounsy
// updated for sampling compatibility
//
// Revision 1.4 2006/07/05 12:58:59 ounsy
// VC : data synchronization management
//
// Revision 1.3 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:28:26 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:44 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.tools.xmlhelpers.vc;

import java.awt.Color;
import java.io.File;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.comete.widget.util.CometeFont;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeExtractProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.plot.Axis;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.data.view.plot.Interpolation;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.MathPlot;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.data.view.plot.Smoothing;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;

public class ViewConfigurationXMLHelper {

    // MULTI-CONF
    /**
     * @param location
     * @return 26 juil. 2005
     * @throws Exception
     */
    public static ViewConfiguration loadViewConfigurationIntoHash(String location) throws Exception {
        File file = new File(location);
        Node rootNode = XMLUtils.getRootNode(file);

        ViewConfiguration ret = loadViewConfigurationIntoHashFromRoot(rootNode);

        return ret;
    }

    /**
     * @param rootNode
     * @return 26 juil. 2005
     * @throws Exception
     */
    public static ViewConfiguration loadViewConfigurationIntoHashFromRoot(Node rootNode)
            throws Exception {
        ViewConfiguration viewConf = loadViewConfigurationData(rootNode);

        if (rootNode.hasChildNodes()) {
            NodeList childNodes = rootNode.getChildNodes();
            ViewConfigurationAttributes viewConfAttr = new ViewConfigurationAttributes();

            for (int i = 0; i < childNodes.getLength(); i++)
            // as many loops as there are attributes in the VC
            {
                Node currentChild = childNodes.item(i);
                if (XMLUtils.isAFakeNode(currentChild)) {
                    continue;
                }

                String currentChildType = currentChild.getNodeName().trim();
                if (currentChildType.equals(ViewConfigurationAttribute.XML_TAG)) {
                    Hashtable<String, String> attributeProperties = XMLUtils
                            .loadAttributes(currentChild);
                    String attributeCompleteName = attributeProperties
                            .get(ViewConfigurationAttribute.COMPLETE_NAME_PROPERTY_XML_TAG);
                    Double attributeFactor;
                    String factorString = attributeProperties
                            .get(ViewConfigurationAttribute.FACTOR_PROPERTY_XML_TAG);
                    if (factorString == null) {
                        factorString = "1";
                    }
                    attributeFactor = Double.valueOf(factorString);
                    factorString = null;
                    if (attributeFactor == null) {
                        attributeFactor = new Double(1);
                    }
                    ViewConfigurationAttribute currentAttribute = new ViewConfigurationAttribute();
                    currentAttribute.setCompleteName(attributeCompleteName);
                    currentAttribute.setFactor(attributeFactor.doubleValue());

                    currentAttribute = loadCurrentAttribute(currentAttribute, currentChild);
                    // To maintain compatibility with previous versions (< lot6)
                    if (currentAttribute.getProperties().getPlotProperties().getCurve().getName()
                            .equals("null")
                            || currentAttribute.getProperties().getPlotProperties().getCurve()
                                    .getName().equals("")) {
                        currentAttribute.getProperties().getPlotProperties().getCurve().setName(
                                currentAttribute.getName());
                    }
                    viewConfAttr.addAttribute(currentAttribute);
                }
                else if (currentChildType
                        .equals(ViewConfigurationData.GENERIC_PLOT_PARAMETERS_XML_TAG)) {
                    ViewConfigurationData viewConfData = viewConf.getData();
                    viewConfData = loadGenericPlotParameters(viewConfData, currentChild);

                    viewConf.setData(viewConfData);
                }
                else if (currentChildType.equals(ViewConfigurationAttributes.COLOR_INDEX_XML_TAG)) {
                    Hashtable<String, String> colorIndexProperties = XMLUtils
                            .loadAttributes(currentChild);
                    String colorIndexStringValue = colorIndexProperties
                            .get(ViewConfigurationAttributes.COLOR_INDEX_XML_ATTRIBUTE);
                    try {
                        viewConfAttr.setColorIndex(Integer.parseInt(colorIndexStringValue));
                    }
                    catch (NumberFormatException nfe) {
                        // could not get the colorIndex
                        // nothing to do : colorIndex automatically initialized
                        // with 0
                    }
                }
                else if (ExpressionAttribute.XML_TAG.equals(currentChildType)) {
                    // load expressions
                    ExpressionAttribute expressionAttribute = loadExpression(currentChild);
                    viewConf.addExpression(expressionAttribute);
                }
                else {
                    continue;
                }
            }
            viewConf.setAttributes(viewConfAttr);
        }
        return viewConf;
    }

    /**
     * Load expression's informations from the xml node
     * 
     * @param expressionNode the expression node
     * @return a new ExpressionAttribute filled with xml informations
     * @throws Exception if there is an unknown child node
     */
    private static ExpressionAttribute loadExpression(Node expressionNode) throws Exception {

        // look for node's attributes
        Hashtable<String, String> expressionProperties = XMLUtils.loadAttributes(expressionNode);

        String name = expressionProperties.get(ExpressionAttribute.NAME_PROPERTY_XML_TAG);
        String id = expressionProperties.get(ExpressionAttribute.ID_PROPERTY_XML_TAG);
        String expression = expressionProperties.get(ExpressionAttribute.VALUE_PROPERTY_XML_TAG);

        String factorString = expressionProperties.get(ExpressionAttribute.FACTOR_PROPERTY_XML_TAG);
        if (factorString == null) {
            factorString = "1";
        }
        Double factor = Double.valueOf(factorString);
        factorString = null;

        String variablesAttr = expressionProperties
                .get(ExpressionAttribute.VARIABLES_PROPERTY_XML_TAG);
        Vector<String> variables = new Vector<String>(3);
        StringTokenizer st = new StringTokenizer(variablesAttr,
                ExpressionAttribute.VARIABLES_PROPERTY_DELIMITER);
        while (st.hasMoreTokens()) {
            variables.add(st.nextToken().trim());
        }

        String xAttr = expressionProperties.get(ExpressionAttribute.X_PROPERTY_XML_TAG);
        Boolean isX = new Boolean(xAttr);

        // look for node's children
        ViewConfigurationAttributePlotProperties plotProperties = null;

        if (expressionNode.hasChildNodes()) {
            NodeList expressionChildren = expressionNode.getChildNodes();
            for (int j = 0; j < expressionChildren.getLength(); j++) {
                Node currentChild = expressionChildren.item(j);
                if (XMLUtils.isAFakeNode(currentChild)) {
                    continue;
                }
                if (ViewConfigurationAttributePlotProperties.XML_TAG.equals(currentChild
                        .getNodeName())) {
                    plotProperties = loadPlotParams(currentChild);
                }
                else {
                    continue;
                }
            }
        }
        if (plotProperties.getCurve().getName().equals("")) {
            plotProperties.getCurve().setName(name);
        }

        ExpressionAttribute currentExpression = new ExpressionAttribute(name, expression, variables
                .toArray(new String[0]), isX.booleanValue());
        if (id != null) {
            currentExpression.setId(id);
        }
        else {
            currentExpression.setId(name);
        }
        currentExpression.setFactor(factor.doubleValue());
        currentExpression.setProperties(plotProperties);

        return currentExpression;
    }

    /**
     * @param viewConfData
     * @param currentAttributeNode
     * @return 24 ao�t 2005
     * @throws Exception
     */
    private static ViewConfigurationData loadGenericPlotParameters(
            ViewConfigurationData viewDataIn, Node genericPlotParametersNode) throws Exception {
        if (genericPlotParametersNode.hasChildNodes()) {
            NodeList genericPlotParametersSubNodes = genericPlotParametersNode.getChildNodes();
            int axisGrid = 0;
            int gridStyle = 0;
            for (int i = 0; i < genericPlotParametersSubNodes.getLength(); i++) {
                // as many loops as there are sub nodes types in a
                // genericPlotParameters node (3)
                Node currentSubNode = genericPlotParametersSubNodes.item(i);
                if (XMLUtils.isAFakeNode(currentSubNode)) {
                    continue;
                }

                String currentSubNodeType = currentSubNode.getNodeName().trim();
                // has to be "mainParams" or "Y1Axis" or "Y2Axis"

                Hashtable<String, String> attributeProperties = XMLUtils
                        .loadAttributes(currentSubNode);

                if (currentSubNodeType.equals(GeneralChartProperties.XML_TAG)) {
                    GeneralChartProperties generalChartProperties = loadGeneralChartProperties(attributeProperties);
                    axisGrid = generalChartProperties.getAxisGrid();
                    gridStyle = generalChartProperties.getXAxisProperties().getGridStyle();
                    viewDataIn.setGeneralChartProperties(generalChartProperties);
                }
                else if (currentSubNodeType.equals(Axis.XML_TAG_1)) {
                    Axis Y1Axis = loadAxis(attributeProperties, gridStyle);
                    viewDataIn.getGeneralChartProperties().setY1AxisProperties(Y1Axis);
                }
                else if (currentSubNodeType.equals(Axis.XML_TAG_2)) {
                    Axis Y2Axis = loadAxis(attributeProperties, gridStyle);
                    viewDataIn.getGeneralChartProperties().setY2AxisProperties(Y2Axis);
                }
                else if (currentSubNodeType.equals(Axis.XML_TAG_3)) {
                    Axis XAxis = loadAxis(attributeProperties, gridStyle);
                    viewDataIn.getGeneralChartProperties().setXAxisProperties(XAxis);
                }
                else {
                    // ignore unknown params
                    continue;
                }
            }
            // To maintain compatibility with previous versions (< lot6)
            viewDataIn.getGeneralChartProperties().setAxisGrid(axisGrid);
        }
        return viewDataIn;
    }

    /**
     * @param attributeProperties
     * @param gridStyle
     * @return 24 ao�t 2005
     */
    private static Axis loadAxis(Hashtable<String, String> attributeProperties, int gridStyle) {
        Axis ret = new Axis();

        String position_s = attributeProperties.get(Axis.POSITION_PROPERTY_XML_TAG);
        String scaleMode_s = attributeProperties.get(Axis.SCALE_MODE_PROPERTY_XML_TAG);
        String labelFormat_s = attributeProperties.get(Axis.LABEL_FORMAT_PROPERTY_XML_TAG);
        String showSubGrid_s = attributeProperties.get(Axis.SHOW_SUB_GRID_PROPERTY_XML_TAG);
        String drawOpposite_s = attributeProperties.get(Axis.DRAW_OPPOSITE_PROPERTY_XML_TAG);
        String isVisible_s = attributeProperties.get(Axis.IS_VISIBLE_PROPERTY_XML_TAG);
        String autoScale_s = attributeProperties.get(Axis.AUTO_SCALE_PROPERTY_XML_TAG);
        String color_s = attributeProperties.get(Axis.COLOR_PROPERTY_XML_TAG);
        String scaleMax_s = attributeProperties.get(Axis.SCALE_MAX_PROPERTY_XML_TAG);
        String scaleMin_s = attributeProperties.get(Axis.SCALE_MIN_PROPERTY_XML_TAG);
        String title_s = attributeProperties.get(Axis.TITLE_PROPERTY_XML_TAG);
        String labelInterval_s = attributeProperties.get(Axis.LABEL_INTERVAL_XML_TAG);
        String annotation_s = attributeProperties.get(Axis.ANNOTATION_XML_TAG);
        String gridVisible_s = attributeProperties.get(Axis.GRID_VISIBLE_XML_TAG);

        int position = GUIUtilities.StringToInt(position_s);
        int scaleMode = GUIUtilities.StringToInt(scaleMode_s);
        int labelFormat = GUIUtilities.StringToInt(labelFormat_s);
        double scaleMax = GUIUtilities.StringToDouble(scaleMax_s);
        double scaleMin = GUIUtilities.StringToDouble(scaleMin_s);
        boolean showSubGrid = GUIUtilities.StringToBoolean(showSubGrid_s);
        boolean drawOpposite = GUIUtilities.StringToBoolean(drawOpposite_s);
        boolean isVisible = GUIUtilities.StringToBoolean(isVisible_s);
        boolean autoScale = GUIUtilities.StringToBoolean(autoScale_s);
        Color color = GUIUtilities.StringToColor(color_s);
        double labelInterval = GUIUtilities.StringToDouble(labelInterval_s);
        int annotation = GUIUtilities.StringToInt(annotation_s);
        boolean gribVisible = GUIUtilities.StringToBoolean(gridVisible_s);

        ret.setAutoScale(autoScale);
        ret.setColor(CometeColor.getColor(color));
        ret.setDrawOpposite(drawOpposite);
        ret.setLabelFormat(labelFormat);
        ret.setPosition(position);
        ret.setScaleMax(scaleMax);
        ret.setScaleMin(scaleMin);
        ret.setScaleMode(scaleMode);
        ret.setSubGridVisible(showSubGrid);
        ret.setTitle(title_s);
        ret.setVisible(isVisible);
        ret.setUserLabelInterval(labelInterval);
        ret.setAnnotation(annotation < 1 ? 2 : annotation);
        ret.setGridVisible(gribVisible);
        ret.setGridStyle(gridStyle);

        return ret;
    }

    /**
     * @param attributeProperties
     * @return 24 ao�t 2005
     */
    private static GeneralChartProperties loadGeneralChartProperties(
            Hashtable<String, String> attributeProperties) {
        GeneralChartProperties ret = new GeneralChartProperties();

        String axisGrid_s = attributeProperties
                .get(GeneralChartProperties.AXIS_GRID_PROPERTY_XML_TAG);
        String axisGridStyle_s = attributeProperties
                .get(GeneralChartProperties.AXIS_GRID_STYLE_PROPERTY_XML_TAG);
        String backgroundColor_s = attributeProperties
                .get(GeneralChartProperties.BACKGROUND_COLOR_PROPERTY_XML_TAG);
        String displayDuration_s = attributeProperties
                .get(GeneralChartProperties.DISPLAY_DURATION_PROPERTY_XML_TAG);
        String headerFont_s = attributeProperties
                .get(GeneralChartProperties.HEADER_FONT_PROPERTY_XML_TAG);
        String labelFont_s = attributeProperties
                .get(GeneralChartProperties.LABEL_FONT_PROPERTY_XML_TAG);
        String legendsAreVisible_s = attributeProperties
                .get(GeneralChartProperties.LEGENDS_ARE_VISIBLE_PROPERTY_XML_TAG);
        String legendsPlacement_s = attributeProperties
                .get(GeneralChartProperties.LEGENDS_PLACEMENT_PROPERTY_XML_TAG);
        String title_s = attributeProperties.get(GeneralChartProperties.TITLE_PROPERTY_XML_TAG);
        String timePrecision_s = attributeProperties
                .get(GeneralChartProperties.TIME_PRECISION_XML_TAG);
        String dataSendingEnabled_s = attributeProperties
                .get(GeneralChartProperties.DATA_SENDING_ENABLED_XML_TAG);
        String autoHighLightOnLegend_s = attributeProperties
                .get(GeneralChartProperties.AUTO_HIGHLIGHT_ON_LEGEND_XML_TAG);
        if (timePrecision_s == null) {
            timePrecision_s = "0";
        }

        int axisGrid = GUIUtilities.StringToInt(axisGrid_s);
        int axisGridStyle = GUIUtilities.StringToInt(axisGridStyle_s);
        int legendsPlacement = GUIUtilities.StringToInt(legendsPlacement_s);
        CometeColor backgroundColor = CometeColor.getColor(GUIUtilities
                .StringToColor(backgroundColor_s));
        boolean autoHighLightOnLegend = GUIUtilities.StringToBoolean(autoHighLightOnLegend_s);
        boolean legendsAreVisible = GUIUtilities.StringToBoolean(legendsAreVisible_s);
        double displayDuration = GUIUtilities.StringToDouble(displayDuration_s);
        CometeFont headerFont = CometeFont.getFont(GUIUtilities.StringToFont(headerFont_s));
        CometeFont labelFont = CometeFont.getFont(GUIUtilities.StringToFont(labelFont_s));
        boolean dataSendingEnabled = GUIUtilities.StringToBoolean(dataSendingEnabled_s);

        ret.setTitle(title_s);
        ret.setAxisGrid(axisGrid);
        ret.getXAxisProperties().setGridStyle(axisGridStyle);
        ret.getY1AxisProperties().setGridStyle(axisGridStyle);
        ret.getY2AxisProperties().setGridStyle(axisGridStyle);
        ret.setLegendsPlacement(legendsPlacement);
        ret.setAutoHighlightOnLegend(autoHighLightOnLegend);
        ret.setBackgroundColor(backgroundColor);
        ret.setLegendsAreVisible(legendsAreVisible);
        ret.setDisplayDuration(displayDuration);
        ret.setHeaderFont(headerFont);
        ret.setLabelFont(labelFont);
        try {
            ret.setTimePrecision(Integer.parseInt(timePrecision_s));
        }
        catch (NumberFormatException e) {
            ret.setTimePrecision(0);
        }
        ret.setDataSendingEnabled(dataSendingEnabled);

        return ret;
    }

    private static ViewConfiguration loadViewConfigurationData(Node rootNode) throws Exception {
        ViewConfiguration viewConf = new ViewConfiguration();
        ViewConfigurationData viewConfData = new ViewConfigurationData();

        Hashtable<String, String> VCProperties = XMLUtils.loadAttributes(rootNode);

        String creationDate_s = VCProperties
                .get(ViewConfigurationData.CREATION_DATE_PROPERTY_XML_TAG);
        String lastUpdateDate_s = VCProperties
                .get(ViewConfigurationData.LAST_UPDATE_DATE_PROPERTY_XML_TAG);
        String startDate_s = VCProperties.get(ViewConfigurationData.START_DATE_PROPERTY_XML_TAG);
        String endDate_s = VCProperties.get(ViewConfigurationData.END_DATE_PROPERTY_XML_TAG);
        String historic_s = VCProperties.get(ViewConfigurationData.HISTORIC_PROPERTY_XML_TAG);
        String name_s = VCProperties.get(ViewConfigurationData.NAME_PROPERTY_XML_TAG);
        String path_s = VCProperties.get(ViewConfigurationData.PATH_PROPERTY_XML_TAG);
        String isModified_s = VCProperties.get(ViewConfigurationData.IS_MODIFIED_PROPERTY_XML_TAG);
        String samplingType_s = VCProperties
                .get(ViewConfigurationData.SAMPLING_TYPE_PROPERTY_XML_TAG);
        String samplingFactor_s = VCProperties
                .get(ViewConfigurationData.SAMPLING_FACTOR_PROPERTY_XML_TAG);

        viewConfData.setName(name_s);
        viewConfData.setPath(path_s);

        boolean historic = GUIUtilities.StringToBoolean(historic_s);
        viewConfData.setHistoric(historic);

        boolean isModified = GUIUtilities.StringToBoolean(isModified_s);
        viewConf.setModified(isModified);

        boolean dynamicDateRange = false;
        String dynamicDateRange_s = VCProperties
                .get(ViewConfigurationData.DYNAMIC_DATE_RANGE_PROPERTY_XML_TAG);
        if (dynamicDateRange_s != null && !"".equals(dynamicDateRange_s.trim())) {
            dynamicDateRange = GUIUtilities.StringToBoolean(dynamicDateRange_s);
        }
        viewConfData.setDynamicDateRange(dynamicDateRange);

        String dateRange = VCProperties.get(ViewConfigurationData.DATE_RANGE_PROPERTY_XML_TAG);
        if (dateRange != null && !"".equals(dateRange.trim())) {
            viewConfData.setDateRange(dateRange);
        }

        if (creationDate_s != null) {
            Timestamp creationDate = Timestamp.valueOf(creationDate_s);
            viewConfData.setCreationDate(creationDate);
        }
        if (lastUpdateDate_s != null) {
            Timestamp lastUpdateDate = Timestamp.valueOf(lastUpdateDate_s);
            viewConfData.setLastUpdateDate(lastUpdateDate);
        }

        try {
            Timestamp startDate = Timestamp.valueOf(startDate_s);
            viewConfData.setStartDate(startDate);
        }
        catch (Exception e) {
        }

        try {
            Timestamp endDate = Timestamp.valueOf(endDate_s);
            viewConfData.setEndDate(endDate);
        }
        catch (Exception e) {
        }

        if (samplingType_s != null) {
            SamplingType samplingType = SamplingType.fromString(samplingType_s);
            if (samplingFactor_s != null) {
                // default value is 1
                short samplingFactor = 1;
                try {
                    samplingFactor = Short.parseShort(samplingFactor_s);
                }
                catch (Exception e) {

                }

                if (samplingFactor > 0) {
                    samplingType.setAdditionalFilteringFactor(samplingFactor);
                    samplingType.setHasAdditionalFiltering(true);
                }
                else {
                    samplingType.setHasAdditionalFiltering(false);
                }
            }
            viewConfData.setSamplingType(samplingType);
        }

        viewConf.setData(viewConfData);

        return viewConf;
    }

    /**
     * @param currentAttribute
     * @param currentAttributeNode
     * @return 26 juil. 2005
     * @throws Exception
     */
    private static ViewConfigurationAttribute loadCurrentAttribute(
            ViewConfigurationAttribute _currentAttribute, Node _currentAttributeNode)
            throws Exception {
        ViewConfigurationAttribute ret = _currentAttribute;

        if (_currentAttributeNode.hasChildNodes()) {
            NodeList attributeParamsNodes = _currentAttributeNode.getChildNodes();
            ViewConfigurationAttributeProperties currentAttributeProperties = new ViewConfigurationAttributeProperties();

            for (int i = 0; i < attributeParamsNodes.getLength(); i++) {
                // as many loops as there are modes types for this attributes (
                // at most 2, plotParams and extractParams )
                Node currentAttributeParamNode = attributeParamsNodes.item(i);
                if (XMLUtils.isAFakeNode(currentAttributeParamNode)) {
                    continue;
                }

                String currentAttributeParamType = currentAttributeParamNode.getNodeName().trim();// has
                                                                                                  // to
                                                                                                  // be
                                                                                                  // "plotParams"
                                                                                                  // or
                // "extractParams"
                boolean currentParamsIsPlot = currentAttributeParamType
                        .equals(ViewConfigurationAttributePlotProperties.XML_TAG);
                boolean currentParamsIsExtract = currentAttributeParamType
                        .equals(ViewConfigurationAttributeExtractProperties.XML_TAG);
                if (currentParamsIsPlot) {
                    // ViewConfigurationAttributePlotProperties plotProperties =
                    // new ViewConfigurationAttributePlotProperties ();

                    // plotProperties =
                    // (ViewConfigurationAttributeHDBProperties)
                    // loadCurrentDBTypeModes ( HDBProperties , currentModesNode
                    // );
                    ViewConfigurationAttributePlotProperties plotProperties = loadPlotParams(currentAttributeParamNode);
                    currentAttributeProperties.setPlotProperties(plotProperties);
                }
                else if (currentParamsIsExtract) {

                }
                else {
                    continue;
                }
            }
            ret.setProperties(currentAttributeProperties);
        }
        return ret;
    }

    /**
     * @param properties
     * @param currentModesNode
     * @return 26 juil. 2005
     * @throws Exception
     */
    private static ViewConfigurationAttributePlotProperties loadPlotParams(Node currentNode)
            throws Exception {
        ViewConfigurationAttributePlotProperties ret = new ViewConfigurationAttributePlotProperties();
        Hashtable<String, String> hash = XMLUtils.loadAttributes(currentNode);

        String viewType_s = hash
                .get(ViewConfigurationAttributePlotProperties.VIEW_TYPE_PROPERTY_XML_TAG);
        String axisChoice_s = hash
                .get(ViewConfigurationAttributePlotProperties.AXIS_CHOICE_PROPERTY_XML_TAG);
        String spectrumViewType_s = hash
                .get(ViewConfigurationAttributePlotProperties.SPECTRUM_VIEW_TYPE_PROPERTY_XML_TAG);
        String hidden_s = hash
                .get(ViewConfigurationAttributePlotProperties.HIDDEN_PROPERTY_XML_TAG);

        if (viewType_s != null) {
            int viewType = Integer.parseInt(viewType_s);
            ret.setViewType(viewType);
        }
        if (axisChoice_s != null) {
            int axisChoice = Integer.parseInt(axisChoice_s);
            ret.setAxisChoice(axisChoice);
        }
        if (spectrumViewType_s != null) {
            int spectrumViewType = Integer.parseInt(spectrumViewType_s);
            ret.setSpectrumViewType(spectrumViewType);
        }
        if (hidden_s != null) {
            boolean hidden = "true".equalsIgnoreCase(hidden_s.trim());
            ret.setHidden(hidden);
        }

        if (currentNode.hasChildNodes()) {
            NodeList subNodes = currentNode.getChildNodes();

            for (int i = 0; i < subNodes.getLength(); i++) {
                // as many loops as there are sub nodes types for a plotParams
                // Node (4)
                Node currentSubNode = subNodes.item(i);
                if (XMLUtils.isAFakeNode(currentSubNode)) {
                    continue;
                }

                String currentSubNodeType = currentSubNode.getNodeName().trim();// has

                if (currentSubNodeType.equals(Bar.XML_TAG)) {
                    Bar bar = loadBar(currentSubNode);
                    ret.setBar(bar);
                }
                else if (currentSubNodeType.equals(Curve.XML_TAG)) {
                    Curve curve = loadCurve(currentSubNode);
                    ret.setCurve(curve);
                }
                else if (currentSubNodeType.equals(Marker.XML_TAG)) {
                    Marker marker = loadMarker(currentSubNode);
                    ret.setMarker(marker);
                }
                else if (currentSubNodeType.equals(Polynomial2OrderTransform.XML_TAG)) {
                    Polynomial2OrderTransform transform = loadTransform(currentSubNode);
                    ret.setTransform(transform);
                }
                else if (currentSubNodeType.equals(Interpolation.XML_TAG)) {
                    Interpolation interpolation = loadInterpolation(currentSubNode);
                    ret.setInterpolation(interpolation);
                }
                else if (currentSubNodeType.equals(Smoothing.XML_TAG)) {
                    Smoothing smoothing = loadSmoothing(currentSubNode);
                    ret.setSmoothing(smoothing);
                }
                else if (currentSubNodeType.equals(MathPlot.XML_TAG)) {
                    MathPlot mathPlot = loadMathPlot(currentSubNode);
                    ret.setMath(mathPlot);
                }
                else {
                    // ignore unknown params
                    continue;
                }
            }
        }
        return ret;
    }

    /**
     * @param currentSubNode
     * @return 24 ao�t 2005
     * @throws Exception
     */
    private static Bar loadBar(Node currentSubNode) throws Exception {

        Hashtable<String, String> barProperties = XMLUtils.loadAttributes(currentSubNode);

        String fillColor_s = barProperties.get(Bar.COLOR_PROPERTY_XML_TAG);
        String fillingMethod_s = barProperties.get(Bar.FILL_METHOD_PROPERTY_XML_TAG);
        String fillStyle_s = barProperties.get(Bar.FILL_STYLE_PROPERTY_XML_TAG);
        String width_s = barProperties.get(Bar.WIDTH_PROPERTY_XML_TAG);

        int fillingMethod = GUIUtilities.StringToInt(fillingMethod_s);
        int fillStyle = GUIUtilities.StringToInt(fillStyle_s);
        int width = GUIUtilities.StringToInt(width_s);
        Color fillColor = GUIUtilities.StringToColor(fillColor_s);

        Bar bar = new Bar(CometeColor.getColor(fillColor), width, fillStyle, fillingMethod);

        return bar;
    }

    /**
     * @param currentSubNode
     * @return 24 ao�t 2005
     * @throws Exception
     */
    private static Curve loadCurve(Node currentSubNode) throws Exception {
        Curve curve = new Curve();
        Hashtable<String, String> curveProperties = XMLUtils.loadAttributes(currentSubNode);

        String color_s = curveProperties.get(Curve.COLOR_PROPERTY_XML_TAG);
        String style_s = curveProperties.get(Curve.STYLE_PROPERTY_XML_TAG);
        String width_s = curveProperties.get(Curve.WIDTH_PROPERTY_XML_TAG);

        if (curveProperties.containsKey(Curve.NAME_PROPERTY_XML_TAG)) {
            String name_s = curveProperties.get(Curve.NAME_PROPERTY_XML_TAG);
            curve.setName(name_s);
        }

        Color fillColor = GUIUtilities.StringToColor(color_s);
        int width = GUIUtilities.StringToInt(width_s);
        int style = GUIUtilities.StringToInt(style_s);

        curve.setColor(CometeColor.getColor(fillColor));
        curve.setLineStyle(style);
        curve.setWidth(width);

        return curve;
    }

    /**
     * @param currentSubNode
     * @return 24 ao�t 2005
     * @throws Exception
     */
    private static Marker loadMarker(Node currentSubNode) throws Exception {
        Marker marker = new Marker();
        Hashtable<String, String> markerProperties = XMLUtils.loadAttributes(currentSubNode);

        String color_s = markerProperties.get(Marker.COLOR_PROPERTY_XML_TAG);
        // String isLegendVisible_s = markerProperties
        // .get(Marker.IS_LEGEND_VISIBLE_PROPERTY_XML_TAG);
        String size_s = markerProperties.get(Marker.SIZE_PROPERTY_XML_TAG);
        String style_s = markerProperties.get(Marker.STYLE_PROPERTY_XML_TAG);

        Color color = GUIUtilities.StringToColor(color_s);
        int style = GUIUtilities.StringToInt(style_s);
        int size = GUIUtilities.StringToInt(size_s);
        // boolean isLegendVisible = GUIUtilities
        // .StringToBoolean(isLegendVisible_s);

        marker.setColor(CometeColor.getColor(color));
        marker.setLegendVisible(true);
        // marker.setLegendVisible(isLegendVisible);
        marker.setSize(size);
        marker.setStyle(style);

        return marker;
    }

    /**
     * @param currentSubNode
     * @return 24 ao�t 2005
     * @throws Exception
     */
    private static Polynomial2OrderTransform loadTransform(Node currentSubNode) throws Exception {
        Polynomial2OrderTransform transform = new Polynomial2OrderTransform();
        Hashtable<String, String> transformProperties = XMLUtils.loadAttributes(currentSubNode);

        String a0_s = transformProperties.get(Polynomial2OrderTransform.A0_PROPERTY_XML_TAG);
        String a1_s = transformProperties.get(Polynomial2OrderTransform.A1_PROPERTY_XML_TAG);
        String a2_s = transformProperties.get(Polynomial2OrderTransform.A2_PROPERTY_XML_TAG);

        double a0 = GUIUtilities.StringToDouble(a0_s);
        double a1 = GUIUtilities.StringToDouble(a1_s);
        double a2 = GUIUtilities.StringToDouble(a2_s);

        // To maintain compatibility with previous versions (< lot6)
        if (Double.isNaN(a0)) {
            a0 = 0;
        }
        if (Double.isNaN(a1)) {
            a1 = 1;
        }
        if (Double.isNaN(a2)) {
            a2 = 0;
        }
        transform.setA0(a0);
        transform.setA1(a1);
        transform.setA2(a2);

        return transform;
    }

    private static Interpolation loadInterpolation(Node currentSubNode) throws Exception {
        Interpolation interpolation = new Interpolation();
        Hashtable<String, String> interpolationProperties = XMLUtils.loadAttributes(currentSubNode);

        String hermliteBias_s = interpolationProperties
                .get(Interpolation.HERMITE_BIAS_PROPERTY_XML_TAG);
        String hermiteTension_s = interpolationProperties
                .get(Interpolation.HERMITE_TENSION_PROPERTY_XML_TAG);
        String interpolationMethod_s = interpolationProperties
                .get(Interpolation.INTERPOLATION_METHOD_PROPERTY_XML_TAG);
        String InterpolationStep_s = interpolationProperties
                .get(Interpolation.INTERPOLATION_STEP_PROPERTY_XML_TAG);

        double hermliteBias = GUIUtilities.StringToDouble(hermliteBias_s);
        double hermiteTension = GUIUtilities.StringToDouble(hermiteTension_s);
        int interpolationMethod = GUIUtilities.StringToInt(interpolationMethod_s);
        int InterpolationStep = GUIUtilities.StringToInt(InterpolationStep_s);

        interpolation.setHermiteBias(hermliteBias);
        interpolation.setHermiteTension(hermiteTension);
        interpolation.setInterpolationMethod(interpolationMethod);
        interpolation.setInterpolationStep(InterpolationStep);

        return interpolation;
    }

    private static Smoothing loadSmoothing(Node currentSubNode) throws Exception {
        Smoothing smoothing = new Smoothing();
        Hashtable<String, String> smoothingProperties = XMLUtils.loadAttributes(currentSubNode);

        String extrapolation_s = smoothingProperties.get(Smoothing.EXTRAPOLATION_PROPERTY_XML_TAG);
        String gaussSigma_s = smoothingProperties.get(Smoothing.GAUSS_SIGMA_PROPERTY_XML_TAG);
        String Method_s = smoothingProperties.get(Smoothing.METHOD_PROPERTY_XML_TAG);
        String neighbors_s = smoothingProperties.get(Smoothing.NEIGHBORS_PROPERTY_XML_TAG);

        int extrapolation = GUIUtilities.StringToInt(extrapolation_s);
        double smoothingGaussSigma = GUIUtilities.StringToDouble(gaussSigma_s);
        int smoothingMethod = GUIUtilities.StringToInt(Method_s);
        int smoothingNeighbors = GUIUtilities.StringToInt(neighbors_s);

        smoothing.setExtrapolation(extrapolation);
        smoothing.setGaussSigma(smoothingGaussSigma);
        smoothing.setMethod(smoothingMethod);
        smoothing.setNeighbors(smoothingNeighbors);

        return smoothing;
    }

    private static MathPlot loadMathPlot(Node currentSubNode) throws Exception {
        MathPlot mathPlot = new MathPlot();
        Hashtable<String, String> mathProperties = XMLUtils.loadAttributes(currentSubNode);

        String function_s = mathProperties.get(MathPlot.FUNCTION_PROPERTY_XML_TAG);

        int function = GUIUtilities.StringToInt(function_s);

        mathPlot.setFunction(function);

        return mathPlot;
    }

}
