//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/tools/xmlhelpers/vc/ViewConfigurationXMLHelper.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewConfigurationXMLHelper.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ViewConfigurationXMLHelper.java,v $
// Revision 1.3  2007/03/27 13:40:53  ounsy
// Better Font management (correction of Mantis bug 4302)
//
// Revision 1.2  2007/03/20 14:15:09  ounsy
// added the possibility to hide a dataview
//
// Revision 1.1  2007/02/01 14:12:36  pierrejoseph
// XmlHelper reorg
//
// Revision 1.7  2006/12/06 15:12:06  ounsy
// added parametrisable sampling
//
// Revision 1.6  2006/10/19 09:21:21  ounsy
// now the color rotation index of vc attributes is saved
//
// Revision 1.5  2006/09/05 14:12:14  ounsy
// updated for sampling compatibility
//
// Revision 1.4  2006/07/05 12:58:59  ounsy
// VC : data synchronization management
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.tools.xmlhelpers.vc;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.SamplingType;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeExtractProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.GeneralChartProperties;
import fr.soleil.mambo.data.view.plot.ITransform;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.data.view.plot.YAxis;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;



public class ViewConfigurationXMLHelper
{
//  MULTI-CONF
    /**
     * @param location
     * @return 26 juil. 2005
     * @throws Exception
     */
    public static ViewConfiguration loadViewConfigurationIntoHash ( String location ) throws Exception
    {
        File file = new File( location );
        Node rootNode = XMLUtils.getRootNode( file );

        ViewConfiguration ret = loadViewConfigurationIntoHashFromRoot( rootNode );

        return ret;
    }

    /**
     * @param rootNode
     * @return 26 juil. 2005
     * @throws Exception
     */
    public static ViewConfiguration loadViewConfigurationIntoHashFromRoot ( Node rootNode ) throws Exception
    {
        ViewConfiguration viewConf = loadViewConfigurationData( rootNode );

        if ( rootNode.hasChildNodes() )
        {
            NodeList childNodes = rootNode.getChildNodes();
            ViewConfigurationAttributes viewConfAttr = new ViewConfigurationAttributes();

            for ( int i = 0 ; i < childNodes.getLength() ; i++ )
//as many loops as there are attributes in the VC
            {
                Node currentChild = childNodes.item( i );
                if ( XMLUtils.isAFakeNode( currentChild ) )
                {
                    continue;
                }

                String currentChildType = currentChild.getNodeName().trim();
                if ( currentChildType.equals( ViewConfigurationAttribute.XML_TAG ) )
                {
                    Hashtable attributeProperties = XMLUtils.loadAttributes( currentChild );
                    String attributeCompleteName = ( String ) attributeProperties.get( ViewConfigurationAttribute.COMPLETE_NAME_PROPERTY_XML_TAG );
                    Double attributeFactor;
                    attributeFactor = ( Double ) attributeProperties.get( ViewConfigurationAttribute.FACTOR_PROPERTY_XML_TAG );
                    if ( attributeFactor == null )
                    {
                        attributeFactor = new Double( 1 );
                    }
                    ViewConfigurationAttribute currentAttribute = new ViewConfigurationAttribute();
                    currentAttribute.setCompleteName( attributeCompleteName );
                    currentAttribute.setFactor( attributeFactor.doubleValue() );

                    currentAttribute = loadCurrentAttribute( currentAttribute , currentChild );
                    viewConfAttr.addAttribute( currentAttribute );
                }
                else if ( currentChildType.equals( ViewConfigurationData.GENERIC_PLOT_PARAMETERS_XML_TAG ) )
                {
                    ViewConfigurationData viewConfData = viewConf.getData();
                    viewConfData = loadGenericPlotParameters( viewConfData , currentChild );

                    viewConf.setData( viewConfData );
                }
                else if ( currentChildType.equals( ViewConfigurationAttributes.COLOR_INDEX_XML_TAG ) )
                {
                    Hashtable colorIndexProperties = XMLUtils.loadAttributes( currentChild );
                    String colorIndexStringValue = ( String ) colorIndexProperties.get(ViewConfigurationAttributes.COLOR_INDEX_XML_ATTRIBUTE);
                    try
                    {
                        viewConfAttr.setColorIndex( Integer.parseInt(colorIndexStringValue) );
                    }
                    catch(NumberFormatException nfe)
                    {
                        //could not get the colorIndex
                        //nothing to do : colorIndex automatically initialized with 0
                    }
                }
                else if (ExpressionAttribute.XML_TAG.equals(currentChildType)) {
                	//load expressions
					ExpressionAttribute expressionAttribute = loadExpression(currentChild);
					viewConf.addExpression(expressionAttribute);
				}
                else
                {
                	//Sylvain: unknown tag, should be correctly reported to the user
                    throw new Exception();
                }

            }

            viewConf.setAttributes( viewConfAttr );
        }


        return viewConf;
    }

	/**
	 * Load expression's informations from the xml node
	 * @param expressionNode the expression node
	 * @return a new ExpressionAttribute filled with xml informations
	 * @throws Exception if there is an unknown child node
	 */
	private static ExpressionAttribute loadExpression(Node expressionNode) throws Exception {

		// look for node's attributes
		Hashtable expressionProperties = XMLUtils.loadAttributes(expressionNode);

		String name = (String) expressionProperties.get(ExpressionAttribute.NAME_PROPERTY_XML_TAG);
		String expression = (String) expressionProperties
				.get(ExpressionAttribute.VALUE_PROPERTY_XML_TAG);

		Double factor = (Double) expressionProperties
				.get(ExpressionAttribute.FACTOR_PROPERTY_XML_TAG);
		if (factor == null) {
			factor = new Double(1);
		}

		String variablesAttr = (String) expressionProperties
				.get(ExpressionAttribute.VARIABLES_PROPERTY_XML_TAG);
		Vector<String> variables = new Vector<String>(3);
		StringTokenizer st = new StringTokenizer(variablesAttr,
				ExpressionAttribute.VARIABLES_PROPERTY_DELIMITER);
		while (st.hasMoreTokens()) {
			variables.add(st.nextToken().trim());
		}

		String xAttr = (String) expressionProperties.get(ExpressionAttribute.X_PROPERTY_XML_TAG);
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
					throw new Exception();
				}
			}
		}


		ExpressionAttribute currentExpression = new ExpressionAttribute(name, expression,
				variables.toArray(new String[0]), isX.booleanValue());
		currentExpression.setFactor(factor.doubleValue());
		currentExpression.setProperties(plotProperties);

		return currentExpression;
	}



    /**
	 * @param viewConfData
	 * @param currentAttributeNode
	 * @return 24 août 2005
	 * @throws Exception
	 */
    private static ViewConfigurationData loadGenericPlotParameters ( ViewConfigurationData viewDataIn , Node genericPlotParametersNode ) throws Exception
    {
        if ( genericPlotParametersNode.hasChildNodes() )
        {
            NodeList genericPlotParametersSubNodes = genericPlotParametersNode.getChildNodes();

            for ( int i = 0 ; i < genericPlotParametersSubNodes.getLength() ; i++ )
//as many loops as there are sub nodes types in a genericPlotParameters node (3)
            {
                Node currentSubNode = genericPlotParametersSubNodes.item( i );
                if ( XMLUtils.isAFakeNode( currentSubNode ) )
                {
                    continue;
                }

                String currentSubNodeType = currentSubNode.getNodeName().trim();//has to be "mainParams" or "Y1Axis" or "Y2Axis"
                Hashtable attributeProperties = XMLUtils.loadAttributes( currentSubNode );

                if ( currentSubNodeType.equals( GeneralChartProperties.XML_TAG ) )
                {
                    GeneralChartProperties generalChartProperties = loadGeneralChartProperties( attributeProperties );
                    viewDataIn.setGeneralChartProperties( generalChartProperties );
                }
                else if ( currentSubNodeType.equals( YAxis.XML_TAG_1 ) )
                {
                    YAxis Y1Axis = loadYAxis( attributeProperties );
                    viewDataIn.setY1( Y1Axis );
                }
                else if ( currentSubNodeType.equals( YAxis.XML_TAG_2 ) )
                {
                    YAxis Y2Axis = loadYAxis( attributeProperties );
                    viewDataIn.setY2( Y2Axis );
                }
                else
                {
                    throw new Exception();
                }

            }
        }

        return viewDataIn;
    }

    /**
     * @param attributeProperties
     * @return 24 août 2005
     */
    private static YAxis loadYAxis ( Hashtable attributeProperties )
    {
        YAxis ret = new YAxis();

        String position_s = ( String ) attributeProperties.get( YAxis.POSITION_PROPERTY_XML_TAG );
        String scaleMode_s = ( String ) attributeProperties.get( YAxis.SCALE_MODE_PROPERTY_XML_TAG );
        String labelFormat_s = ( String ) attributeProperties.get( YAxis.LABEL_FORMAT_PROPERTY_XML_TAG );
        String showSubGrid_s = ( String ) attributeProperties.get( YAxis.SHOW_SUB_GRID_PROPERTY_XML_TAG );
        String drawOpposite_s = ( String ) attributeProperties.get( YAxis.DRAW_OPPOSITE_PROPERTY_XML_TAG );
        String isVisible_s = ( String ) attributeProperties.get( YAxis.IS_VISIBLE_PROPERTY_XML_TAG );
        String autoScale_s = ( String ) attributeProperties.get( YAxis.AUTO_SCALE_PROPERTY_XML_TAG );
        String color_s = ( String ) attributeProperties.get( YAxis.COLOR_PROPERTY_XML_TAG );
        String scaleMax_s = ( String ) attributeProperties.get( YAxis.SCALE_MAX_PROPERTY_XML_TAG );
        String scaleMin_s = ( String ) attributeProperties.get( YAxis.SCALE_MIN_PROPERTY_XML_TAG );
        String title_s = ( String ) attributeProperties.get( YAxis.TITLE_PROPERTY_XML_TAG );

        int position = GUIUtilities.StringToInt( position_s );
        int scaleMode = GUIUtilities.StringToInt( scaleMode_s );
        int labelFormat = GUIUtilities.StringToInt( labelFormat_s );
        double scaleMax = GUIUtilities.StringToDouble( scaleMax_s );
        double scaleMin = GUIUtilities.StringToDouble( scaleMin_s );
        boolean showSubGrid = GUIUtilities.StringToBoolean( showSubGrid_s );
        boolean drawOpposite = GUIUtilities.StringToBoolean( drawOpposite_s );
        boolean isVisible = GUIUtilities.StringToBoolean( isVisible_s );
        boolean autoScale = GUIUtilities.StringToBoolean( autoScale_s );
        Color color = GUIUtilities.StringToColor( color_s );

        ret.setAutoScale( autoScale );
        ret.setColor( color );
        ret.setDrawOpposite( drawOpposite );
        ret.setLabelFormat( labelFormat );
        ret.setPosition( position );
        ret.setScaleMax( scaleMax );
        ret.setScaleMin( scaleMin );
        ret.setScaleMode( scaleMode );
        ret.setShowSubGrid( showSubGrid );
        ret.setTitle( title_s );
        ret.setVisible( isVisible );

        return ret;
    }

    /**
     * @param attributeProperties
     * @return 24 août 2005
     */
    private static GeneralChartProperties loadGeneralChartProperties ( Hashtable attributeProperties )
    {
        GeneralChartProperties ret = new GeneralChartProperties();

        String axisGrid_s = ( String ) attributeProperties.get( GeneralChartProperties.AXIS_GRID_PROPERTY_XML_TAG );
        String axisGridStyle_s = ( String ) attributeProperties.get( GeneralChartProperties.AXIS_GRID_STYLE_PROPERTY_XML_TAG );
        String backgroundColor_s = ( String ) attributeProperties.get( GeneralChartProperties.BACKGROUND_COLOR_PROPERTY_XML_TAG );
        String displayDuration_s = ( String ) attributeProperties.get( GeneralChartProperties.DISPLAY_DURATION_PROPERTY_XML_TAG );
        String headerFont_s = ( String ) attributeProperties.get( GeneralChartProperties.HEADER_FONT_PROPERTY_XML_TAG );
        String labelFont_s = ( String ) attributeProperties.get( GeneralChartProperties.LABEL_FONT_PROPERTY_XML_TAG );
        String legendsAreVisible_s = ( String ) attributeProperties.get( GeneralChartProperties.LEGENDS_ARE_VISIBLE_PROPERTY_XML_TAG );
        String legendsPlacement_s = ( String ) attributeProperties.get( GeneralChartProperties.LEGENDS_PLACEMENT_PROPERTY_XML_TAG );
        String title_s = ( String ) attributeProperties.get( GeneralChartProperties.TITLE_PROPERTY_XML_TAG );
        String timePrecision = ( String ) attributeProperties.get( GeneralChartProperties.TIME_PRECISION_XML_TAG );
        if (timePrecision == null)
        {
            timePrecision = "0";
        }

        int axisGrid = GUIUtilities.StringToInt( axisGrid_s );
        int axisGridStyle = GUIUtilities.StringToInt( axisGridStyle_s );
        int legendsPlacement = GUIUtilities.StringToInt( legendsPlacement_s );
        Color backgroundColor = GUIUtilities.StringToColor( backgroundColor_s );
        boolean legendsAreVisible = GUIUtilities.StringToBoolean( legendsAreVisible_s );
        long displayDuration = GUIUtilities.StringToLong( displayDuration_s );
        Font headerFont = GUIUtilities.StringToFont( headerFont_s );
        Font labelFont = GUIUtilities.StringToFont( labelFont_s );

        ret.setTitle( title_s );
        ret.setAxisGrid( axisGrid );
        ret.setAxisGridStyle( axisGridStyle );
        ret.setLegendsPlacement( legendsPlacement );
        ret.setBackgroundColor( backgroundColor );
        ret.setLegendsAreVisible( legendsAreVisible );
        ret.setDisplayDuration( displayDuration );
        ret.setHeaderFont( headerFont );
        ret.setLabelFont( labelFont );
        try
        {
            ret.setTimePrecision( Integer.parseInt(timePrecision) );
        }
        catch (NumberFormatException e)
        {
            ret.setTimePrecision( 0 );
        }

        return ret;
    }

    private static ViewConfiguration loadViewConfigurationData ( Node rootNode ) throws Exception
    {
        ViewConfiguration viewConf = new ViewConfiguration();
        ViewConfigurationData viewConfData = new ViewConfigurationData();

        Hashtable VCProperties = XMLUtils.loadAttributes( rootNode );

        String creationDate_s = ( String ) VCProperties.get( ViewConfigurationData.CREATION_DATE_PROPERTY_XML_TAG );
        String lastUpdateDate_s = ( String ) VCProperties.get( ViewConfigurationData.LAST_UPDATE_DATE_PROPERTY_XML_TAG );
        String startDate_s = ( String ) VCProperties.get( ViewConfigurationData.START_DATE_PROPERTY_XML_TAG );
        String endDate_s = ( String ) VCProperties.get( ViewConfigurationData.END_DATE_PROPERTY_XML_TAG );
        String historic_s = ( String ) VCProperties.get( ViewConfigurationData.HISTORIC_PROPERTY_XML_TAG );
        String name_s = ( String ) VCProperties.get( ViewConfigurationData.NAME_PROPERTY_XML_TAG );
        String path_s = ( String ) VCProperties.get( ViewConfigurationData.PATH_PROPERTY_XML_TAG );
        String isModified_s = ( String ) VCProperties.get( ViewConfigurationData.IS_MODIFIED_PROPERTY_XML_TAG );
        String samplingType_s = ( String ) VCProperties.get( ViewConfigurationData.SAMPLING_TYPE_PROPERTY_XML_TAG );
        String samplingFactor_s = ( String ) VCProperties.get( ViewConfigurationData.SAMPLING_FACTOR_PROPERTY_XML_TAG );
        //System.out.println ( "ViewConfigurationXMLHelper/loadViewConfigurationData/1/samplingType_s/"+samplingType_s+"/" );

        viewConfData.setName( name_s );
        viewConfData.setPath( path_s );

        boolean historic = GUIUtilities.StringToBoolean( historic_s );
        viewConfData.setHistoric( historic );

        boolean isModified = GUIUtilities.StringToBoolean( isModified_s );
        viewConf.setModified( isModified );

        boolean dynamicDateRange = false;
        String dynamicDateRange_s = ( String ) VCProperties.get( ViewConfigurationData.DYNAMIC_DATE_RANGE_PROPERTY_XML_TAG );
        if ( dynamicDateRange_s != null && !"".equals( dynamicDateRange_s.trim() ) )
        {
            dynamicDateRange = GUIUtilities.StringToBoolean( dynamicDateRange_s );
        }
        viewConfData.setDynamicDateRange( dynamicDateRange );

        String dateRange = ( String ) VCProperties.get( ViewConfigurationData.DATE_RANGE_PROPERTY_XML_TAG );
        if ( dateRange != null && !"".equals( dateRange.trim() ) )
        {
            viewConfData.setDateRange( dateRange );
        }

        if ( creationDate_s != null )
        {
            Timestamp creationDate = Timestamp.valueOf( creationDate_s );
            viewConfData.setCreationDate( creationDate );
        }
        if ( lastUpdateDate_s != null )
        {
            Timestamp lastUpdateDate = Timestamp.valueOf( lastUpdateDate_s );
            viewConfData.setLastUpdateDate( lastUpdateDate );
        }

        try
        {
            Timestamp startDate = Timestamp.valueOf( startDate_s );
            viewConfData.setStartDate( startDate );
        }
        catch ( Exception e )
        {
        }

        try
        {
            Timestamp endDate = Timestamp.valueOf( endDate_s );
            viewConfData.setEndDate( endDate );
        }
        catch ( Exception e )
        {
        }

        if ( samplingType_s != null )
        {
            SamplingType samplingType = SamplingType.fromString ( samplingType_s );
            if ( samplingFactor_s != null )
            {
            	//default value is 1
                short samplingFactor = 1;
                try
                {
                    samplingFactor = Short.parseShort ( samplingFactor_s );
                }
                catch ( Exception e )
                {

                }

                if ( samplingFactor > 0 )
                {
                    samplingType.setAdditionalFilteringFactor ( samplingFactor );
                    samplingType.setHasAdditionalFiltering ( true );
                }
                else
                {
                    samplingType.setHasAdditionalFiltering ( false );
                }
            }
            viewConfData.setSamplingType ( samplingType );
        }

        viewConf.setData( viewConfData );

        return viewConf;
    }

    /**
     * @param currentAttribute
     * @param currentAttributeNode
     * @return 26 juil. 2005
     * @throws Exception
     */
    private static ViewConfigurationAttribute loadCurrentAttribute ( ViewConfigurationAttribute _currentAttribute , Node _currentAttributeNode ) throws Exception
    {
        ViewConfigurationAttribute ret = _currentAttribute;

        if ( _currentAttributeNode.hasChildNodes() )
        {
            NodeList attributeParamsNodes = _currentAttributeNode.getChildNodes();
            ViewConfigurationAttributeProperties currentAttributeProperties = new ViewConfigurationAttributeProperties();

            for ( int i = 0 ; i < attributeParamsNodes.getLength() ; i++ )
//as many loops as there are modes types for this attributes ( at most 2, plotParams and extractParams )
            {
                Node currentAttributeParamNode = attributeParamsNodes.item( i );
                if ( XMLUtils.isAFakeNode( currentAttributeParamNode ) )
                {
                    continue;
                }

                String currentAttributeParamType = currentAttributeParamNode.getNodeName().trim();//has to be "plotParams" or "extractParams"
                boolean currentParamsIsPlot = currentAttributeParamType.equals( ViewConfigurationAttributePlotProperties.XML_TAG );
                boolean currentParamsIsExtract = currentAttributeParamType.equals( ViewConfigurationAttributeExtractProperties.XML_TAG );
                if ( currentParamsIsPlot )
                {
                    //ViewConfigurationAttributePlotProperties plotProperties = new ViewConfigurationAttributePlotProperties ();

                    //plotProperties = (ViewConfigurationAttributeHDBProperties) loadCurrentDBTypeModes ( HDBProperties , currentModesNode );
                    ViewConfigurationAttributePlotProperties plotProperties = loadPlotParams( currentAttributeParamNode );
                    currentAttributeProperties.setPlotProperties( plotProperties );
                }
                else if ( currentParamsIsExtract )
                {

                }
                else
                {
                    throw new Exception();
                }
            }

            ret.setProperties( currentAttributeProperties );
        }


        return ret;
    }

    /**
     * @param properties
     * @param currentModesNode
     * @return 26 juil. 2005
     * @throws Exception
     */
    private static ViewConfigurationAttributePlotProperties loadPlotParams ( Node currentNode ) throws Exception
    {
        ViewConfigurationAttributePlotProperties ret = new ViewConfigurationAttributePlotProperties();
        Hashtable hash = XMLUtils.loadAttributes( currentNode );

        String viewType_s = ( String ) hash.get( ViewConfigurationAttributePlotProperties.VIEW_TYPE_PROPERTY_XML_TAG );
        String axisChoice_s = ( String ) hash.get( ViewConfigurationAttributePlotProperties.AXIS_CHOICE_PROPERTY_XML_TAG );
        String hidden_s = ( String ) hash.get( ViewConfigurationAttributePlotProperties.HIDDEN_PROPERTY_XML_TAG );

        if ( viewType_s != null )
        {
            int viewType = Integer.parseInt( viewType_s );
            ret.setViewType( viewType );
        }
        if ( axisChoice_s != null )
        {
            int axisChoice = Integer.parseInt( axisChoice_s );
            ret.setAxisChoice( axisChoice );
        }
        if ( hidden_s != null )
        {
            boolean hidden = "true".equalsIgnoreCase( hidden_s.trim() );
            ret.setHidden( hidden );
        }

        if ( currentNode.hasChildNodes() )
        {
            NodeList subNodes = currentNode.getChildNodes();

            for ( int i = 0 ; i < subNodes.getLength() ; i++ )
//as many loops as there are sub nodes types for a plotParams Node (4)
            {
                Node currentSubNode = subNodes.item( i );
                if ( XMLUtils.isAFakeNode( currentSubNode ) )
                {
                    continue;
                }

                String currentSubNodeType = currentSubNode.getNodeName().trim();//has to be "bar" or "curve" or "marker" or "transform"
                if ( currentSubNodeType.equals( Bar.XML_TAG ) )
                {
                    Bar bar = loadBar( currentSubNode );
                    ret.setBar( bar );
                }
                else if ( currentSubNodeType.equals( Curve.XML_TAG ) )
                {
                    Curve curve = loadCurve( currentSubNode );
                    ret.setCurve( curve );
                }
                else if ( currentSubNodeType.equals( Marker.XML_TAG ) )
                {
                    Marker marker = loadMarker( currentSubNode );
                    ret.setMarker( marker );
                }
                else if ( currentSubNodeType.equals( Polynomial2OrderTransform.XML_TAG ) )
                {
                    ITransform transform = loadTransform( currentSubNode );
                    ret.setTransform( transform );
                }
                else
                {
                    throw new Exception();
                }
            }
        }

        return ret;
    }

    /**
     * @param currentSubNode
     * @return 24 août 2005
     * @throws Exception
     */
    private static Bar loadBar ( Node currentSubNode ) throws Exception
    {
        Bar bar = new Bar();
        Hashtable barProperties = XMLUtils.loadAttributes( currentSubNode );

        String fillColor_s = ( String ) barProperties.get( Bar.COLOR_PROPERTY_XML_TAG );
        String fillingMethod_s = ( String ) barProperties.get( Bar.FILL_METHOD_PROPERTY_XML_TAG );
        String fillStyle_s = ( String ) barProperties.get( Bar.FILL_STYLE_PROPERTY_XML_TAG );
        String width_s = ( String ) barProperties.get( Bar.WIDTH_PROPERTY_XML_TAG );

        int fillingMethod = GUIUtilities.StringToInt( fillingMethod_s );
        int fillStyle = GUIUtilities.StringToInt( fillStyle_s );
        int width = GUIUtilities.StringToInt( width_s );
        Color fillColor = GUIUtilities.StringToColor( fillColor_s );

        bar.setFillColor( fillColor );
        bar.setFillingMethod( fillingMethod );
        bar.setFillStyle( fillStyle );
        bar.setWidth( width );

        return bar;
    }

    /**
     * @param currentSubNode
     * @return 24 août 2005
     * @throws Exception
     */
    private static Curve loadCurve ( Node currentSubNode ) throws Exception
    {
        Curve curve = new Curve();
        Hashtable curveProperties = XMLUtils.loadAttributes( currentSubNode );

        String color_s = ( String ) curveProperties.get( Curve.COLOR_PROPERTY_XML_TAG );
        String style_s = ( String ) curveProperties.get( Curve.STYLE_PROPERTY_XML_TAG );
        String width_s = ( String ) curveProperties.get( Curve.WIDTH_PROPERTY_XML_TAG );

        Color fillColor = GUIUtilities.StringToColor( color_s );
        int width = GUIUtilities.StringToInt( width_s );
        int style = GUIUtilities.StringToInt( style_s );

        curve.setColor( fillColor );
        curve.setLineStyle( style );
        curve.setWidth( width );

        return curve;
    }

    /**
     * @param currentSubNode
     * @return 24 août 2005
     * @throws Exception
     */
    private static Marker loadMarker ( Node currentSubNode ) throws Exception
    {
        Marker marker = new Marker();
        Hashtable markerProperties = XMLUtils.loadAttributes( currentSubNode );

        String color_s = ( String ) markerProperties.get( Marker.COLOR_PROPERTY_XML_TAG );
        String isLegendVisible_s = ( String ) markerProperties.get( Marker.IS_LEGEND_VISIBLE_PROPERTY_XML_TAG );
        String size_s = ( String ) markerProperties.get( Marker.SIZE_PROPERTY_XML_TAG );
        String style_s = ( String ) markerProperties.get( Marker.STYLE_PROPERTY_XML_TAG );

        Color color = GUIUtilities.StringToColor( color_s );
        int style = GUIUtilities.StringToInt( style_s );
        int size = GUIUtilities.StringToInt( size_s );
        boolean isLegendVisible = GUIUtilities.StringToBoolean( isLegendVisible_s );

        marker.setColor( color );
        marker.setLegendVisible( isLegendVisible );
        marker.setSize( size );
        marker.setStyle( style );

        return marker;
    }

    /**
     * @param currentSubNode
     * @return 24 août 2005
     * @throws Exception
     */
    private static Polynomial2OrderTransform loadTransform ( Node currentSubNode ) throws Exception
    {
        Polynomial2OrderTransform transform = new Polynomial2OrderTransform();
        Hashtable transformProperties = XMLUtils.loadAttributes( currentSubNode );

        String a0_s = ( String ) transformProperties.get( Polynomial2OrderTransform.A0_PROPERTY_XML_TAG );
        String a1_s = ( String ) transformProperties.get( Polynomial2OrderTransform.A1_PROPERTY_XML_TAG );
        String a2_s = ( String ) transformProperties.get( Polynomial2OrderTransform.A2_PROPERTY_XML_TAG );

        double a0 = GUIUtilities.StringToDouble( a0_s );
        double a1 = GUIUtilities.StringToDouble( a1_s );
        double a2 = GUIUtilities.StringToDouble( a2_s );

        transform.setA0( a0 );
        transform.setA1( a1 );
        transform.setA2( a2 );

        return transform;
    }


}
