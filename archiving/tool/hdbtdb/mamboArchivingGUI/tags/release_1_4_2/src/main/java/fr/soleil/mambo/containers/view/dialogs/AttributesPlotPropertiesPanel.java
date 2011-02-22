//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/AttributesPlotPropertiesPanel.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AttributesPlotPropertiesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.13 $
//
//$Log: AttributesPlotPropertiesPanel.java,v $
//Revision 1.13  2007/08/24 09:23:00  ounsy
//Color index reset on new VC (Mantis bug 5210 part1)
//
//Revision 1.12  2007/03/20 14:15:09  ounsy
//added the possibility to hide a dataview
//
//Revision 1.11  2007/02/27 10:06:32  ounsy
//corrected the transformation property bug
//
//Revision 1.10  2007/01/11 14:05:47  ounsy
//Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
//Revision 1.9  2006/10/19 09:21:21  ounsy
//now the color rotation index of vc attributes is saved
//
//Revision 1.8  2006/10/17 14:31:06  ounsy
//extended color management
//
//Revision 1.7  2006/10/02 15:00:10  ounsy
//JLChart has now a white background : dataviews can not have white color
//
//Revision 1.6  2006/10/02 14:13:25  ounsy
//minor changes (look and feel)
//
//Revision 1.5  2006/09/20 13:03:29  chinkumo
//Mantis 2230: Separated Set addition in the "Attributes plot properties" tab of the New/Modify VC View
//
//Revision 1.4  2006/05/19 15:05:29  ounsy
//minor changes
//
//Revision 1.3  2005/12/15 11:31:15  ounsy
//minor changes
//
//Revision 1.2  2005/11/29 18:27:45  chinkumo
//no message
//
//Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
//Third commit !
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.actions.view.SetAttributesGroupProperties;
import fr.soleil.mambo.actions.view.VCSetBalancingFactorAttributesGroupProperties;
import fr.soleil.mambo.actions.view.dialogs.listeners.AxisChoiceComboListener;
import fr.soleil.mambo.actions.view.dialogs.listeners.FactorFieldListener;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.tools.ColorGenerator;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.Observable;
import fr.soleil.mambo.tools.Observateur;
import fr.soleil.mambo.tools.SpringUtilities;

public class AttributesPlotPropertiesPanel extends JPanel implements MouseListener, Observable
{
    private static int           colorIndex;
    private final static int     totalColors   = 31;
    private final static Color[] defaultColor  = ColorGenerator.generateColors(totalColors, 0xFFFFFF);

    //balancing factor management
    private JTextField factorField;
    private FactorFieldListener factorFieldListener;
    //private JLabel factorLabel;

    //general objects
//    private JButton setButton;
    private JPanel centerPanel;
    private JPanel topPanel;
//    private JButton setAxisButton;
//    private JButton setBalancingFactorButton;
    private JComboBox viewTypeCombo;
    private DefaultComboBoxModel axisChoiceComboModel;
    private JComboBox axisChoiceCombo;
    private AxisChoiceComboListener axisChoiceComboListener;
    private JCheckBox hiddenCheckBox;
    //general objects

    //curve objects
    private JLabel lineColorView;
    private JSpinner lineWidthSpinner;
    private JComboBox lineDashCombo;
    private JButton lineColorBtn;
    //curve objects

    //bar objects
    private JSpinner barWidthSpinner;
    private JComboBox fillStyleCombo;
    private JLabel fillColorView;
    private JComboBox fillMethodCombo;
    private JButton fillColorBtn;
    //bar objects

    //marker objects
    private JLabel markerColorView;
    private JSpinner markerSizeSpinner;
    private JComboBox markerStyleCombo;
    private JCheckBox labelVisibleCheck;
    private JButton markerColorBtn;
    //marker objects

    //transform objects
    private JTextField transformA0Text;
    private JTextField transformA1Text;
    private JTextField transformA2Text;
    //transform objects

    private static final int DEFAULT_VIEW_TYPE = 0;//line
    private static final int DEFAULT_CURVE_WIDTH = 1;//1
    private static final int DEFAULT_CURVE_LINE_STYLE = 0;//solid
    private static final int DEFAULT_BAR_FILL_STYLE = 0;//no fill
    private static final int DEFAULT_MARKER_SIZE = 5;//5
    private static final int DEFAULT_MARKER_STYLE = 1;//dot
    private static final String DEFAULT_TRANSFORM_A0 = "";
    private static final String DEFAULT_TRANSFORM_A1 = "";
    private static final String DEFAULT_TRANSFORM_A2 = "";
    private boolean expression;
	
    /**
     *
     */
    public AttributesPlotPropertiesPanel (boolean _expression)
    {
        this.expression = _expression;
        this.initComponents();
        this.initLayout();
        this.addComponents();
        colorIndex = 0;
        ViewConfigurationAttributeProperties currentProperties = new ViewConfigurationAttributeProperties();
        ViewConfigurationAttributeProperties.setCurrentProperties( currentProperties );
    }

    /**
     * 19 juil. 2005
     */
    private void initLayout ()
    {
        this.setLayout( new BorderLayout() );
    }

    /**
     * Method to set the curve color to a color selected in
     * default colors.
     * @return the next available index in the color table (for dataview color rotation)
     */
    public int rotateCurveColor ()
    {
        lineColorView.setBackground( defaultColor[ colorIndex ] );
        fillColorView.setBackground( defaultColor[ colorIndex ] );
        markerColorView.setBackground( defaultColor[ colorIndex ] );
        colorIndex = ( colorIndex + 5 ) % defaultColor.length;
        return colorIndex;
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        //this.add( centerPanel , BorderLayout.CENTER );
    	//this.add( setButton , BorderLayout.PAGE_END );
 
    	this.add( topPanel ); 
    	this.add( centerPanel );
 
    	this.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( 
                this ,
                this.getComponentCount() , 1 , //rows, cols
                7 , 0 , //initX, initY
                6 , 0 , //xpad, ypad
                true
        );   
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        String msg;
        
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_SET" );
//        setButton = new JButton( msg );
//        setButton.setMaximumSize( new Dimension( Integer.MAX_VALUE , 20 ) );
    	
    	// This one became the upper pannel and the centerpanel became the lower pannel 
        initOrganizePanel(); 
        initCenterPanel();
    }

    public int getViewType ()
    {
        return viewTypeCombo.getSelectedIndex();
    }

    public int getAxisChoice ()
    {
        return axisChoiceCombo.getSelectedIndex();
    }
    
    public String getAxisChoiceItem ()
    {
        return (String) axisChoiceCombo.getSelectedItem();
    }

    public double getFactor ()
    {
        double factor;
        try
        {
            factor = Double.parseDouble( factorField.getText() );
            if ( factor == Double.NaN
                 || factor == Double.POSITIVE_INFINITY
                 || factor == Double.NEGATIVE_INFINITY )
            {
                factor = 1;
            }
        }
        catch ( Exception e )
        {
            factor = 1;
        }
        return factor;
    }

    public void setFactor ( double factor )
    {
    	factorField.setText( factor + "" );
    }

    public Curve getCurve ()
    {
        Color _color = null;
        int _width = -1;
        int _lineStyle = -1;

        _color = lineColorView.getBackground();
        _lineStyle = lineDashCombo.getSelectedIndex() - 1;
        Integer val = ( Integer ) lineWidthSpinner.getValue();
        _width = val.intValue();
        if ( _lineStyle == -1 )
        {
            _width = 0;
        }

        return new Curve( _color , _width , _lineStyle );
    }

    public Bar getBar ()
    {
        Color _fillColor = null;
        int _width = -1;
        int _fillStyle = -1;
        int _fillingMethod = -1;

        _fillColor = fillColorView.getBackground();
        Integer val = ( Integer ) barWidthSpinner.getValue();
        _width = val.intValue();
        _fillStyle = fillStyleCombo.getSelectedIndex();
        _fillingMethod = fillMethodCombo.getSelectedIndex();

        return new Bar( _fillColor , _width , _fillStyle , _fillingMethod );
    }

    public Marker getMarker ()
    {
        Color _color = null;
        int _size = -1;
        int _style = -1;
        boolean _isLegendVisible = false;

        _color = markerColorView.getBackground();
        Integer val = ( Integer ) markerSizeSpinner.getValue();
        _size = val.intValue();
        _style = markerStyleCombo.getSelectedIndex();
        _isLegendVisible = labelVisibleCheck.isSelected();

        return new Marker( _color , _size , _style , _isLegendVisible );
    }
    //JComboBox fillStyleCombo;

    public Polynomial2OrderTransform getTransform ()
    {
        String a0_s = transformA0Text.getText();
        String a1_s = transformA1Text.getText();
        String a2_s = transformA2Text.getText();

        try
        {
            double a0 = Double.parseDouble( a0_s );
            double a1 = Double.parseDouble( a1_s );
            double a2 = Double.parseDouble( a2_s );

            return new Polynomial2OrderTransform( a0 , a1 , a2 );
        }
        catch ( NumberFormatException nfe )
        {
            //return null;
            return new Polynomial2OrderTransform( Double.NaN , Double.NaN , Double.NaN );
        }
    }

    public void reset ()
    {
        this.setViewType( AttributesPlotPropertiesPanel.DEFAULT_VIEW_TYPE );//line

        this.setCurveWidth( AttributesPlotPropertiesPanel.DEFAULT_CURVE_WIDTH );//2
        this.setCurveLineStyle( AttributesPlotPropertiesPanel.DEFAULT_CURVE_LINE_STYLE );//solid

        this.setBarFillStyle( AttributesPlotPropertiesPanel.DEFAULT_BAR_FILL_STYLE );//no fill

        this.setMarkerSize( AttributesPlotPropertiesPanel.DEFAULT_MARKER_SIZE );//5
        this.setMarkerStyle( AttributesPlotPropertiesPanel.DEFAULT_MARKER_STYLE );//dot

        transformA0Text.setText( AttributesPlotPropertiesPanel.DEFAULT_TRANSFORM_A0 );
        transformA1Text.setText( AttributesPlotPropertiesPanel.DEFAULT_TRANSFORM_A1 );
        transformA2Text.setText( AttributesPlotPropertiesPanel.DEFAULT_TRANSFORM_A2 );
        colorIndex = 0;
        rotateCurveColor();
        colorIndex = 0;
    }

    public void setColorIndex(int index)
    {
        colorIndex = index;
    }

    private JPanel initCurvePanel ()
    {
        JPanel curvePanel = new JPanel();

        String msg;

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_TITLE" );
        TitledBorder curvePanelBorder = GUIUtilities.getPlotSubPanelsEtchedBorder( msg );
        curvePanel.setBorder( curvePanelBorder );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_VIEW_TYPE" );
        JLabel viewTypeLabel = new JLabel( msg );
        viewTypeLabel.setFont( GUIUtilities.labelFont );
        viewTypeLabel.setForeground( GUIUtilities.fColor );

        viewTypeCombo = new JComboBox();
        viewTypeCombo.setFont( GUIUtilities.labelFont );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE" );
        viewTypeCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_BAR_GRAPH" );
        viewTypeCombo.addItem( msg );

        lineColorView = new JLabel( "                " );
        //lineColorView.setMinimumSize ( new Dimension ( 80, 20 ) );
        lineColorView.setBackground( Color.RED );
        lineColorView.setOpaque( true );
        lineColorView.setBorder( BorderFactory.createLineBorder( Color.black ) );

        lineColorBtn = new JButton( "..." );
        //lineColorBtn.setMaximumSize ( new Dimension ( 30, 20 ) );
        lineColorBtn.addMouseListener( this );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_COLOR" );
        JLabel lineColorLabel = new JLabel( msg );
        lineColorLabel.setFont( GUIUtilities.labelFont );
        lineColorLabel.setForeground( GUIUtilities.fColor );

        fillColorView = new JLabel( "                " );
        //fillColorView.setMinimumSize ( new Dimension ( 80, 20 ) );
        fillColorView.setBackground( Color.RED );
        fillColorView.setOpaque( true );
        fillColorView.setBorder( BorderFactory.createLineBorder( Color.black ) );
        fillColorBtn = new JButton( "..." );
        //fillColorBtn.setMaximumSize ( new Dimension ( 30, 20 ) );
        fillColorBtn.addMouseListener( this );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_COLOR" );
        JLabel fillColorLabel = new JLabel( msg );
        fillColorLabel.setFont( GUIUtilities.labelFont );
        fillColorLabel.setForeground( GUIUtilities.fColor );
        Box box2 = new Box( BoxLayout.X_AXIS );
        box2.add( fillColorView );
        box2.add( Box.createHorizontalStrut( 5 ) );
        box2.add( fillColorBtn );
        box2.add( Box.createHorizontalGlue() );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_WIDTH" );
        JLabel lineWidthLabel = new JLabel( msg );
        lineWidthLabel.setFont( GUIUtilities.labelFont );
        lineWidthLabel.setForeground( GUIUtilities.fColor );
        lineWidthSpinner = new JSpinner();
        Integer value = new Integer( 2 );
        Integer min = new Integer( 0 );
        Integer max = new Integer( 10 );
        Integer step = new Integer( 1 );
        SpinnerNumberModel spModel = new SpinnerNumberModel( value , min , max , step );
        lineWidthSpinner.setModel( spModel );


        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE" );
        JLabel lineDashLabel = new JLabel( msg );
        lineDashLabel.setFont( GUIUtilities.labelFont );
        lineDashLabel.setForeground( GUIUtilities.fColor );
        lineDashCombo = new JComboBox();
        lineDashCombo.setFont( GUIUtilities.labelFont );


        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_NONE" );
        lineDashCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_SOLID" );
        lineDashCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_POINT_DASH" );
        lineDashCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_SHORT_DASH" );
        lineDashCombo.addItem( msg );
        /*msg = Messages.getMessage ( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_LONG_DASH" );
        lineDashCombo.addItem ( msg );
        This style removed, reserved for write values in case of read/write attributes
        **/
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_DOT_DASH" );
        lineDashCombo.addItem( msg );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE" );
        JLabel fillStyleLabel = new JLabel( msg );
        fillStyleLabel.setFont( GUIUtilities.labelFont );
        fillStyleLabel.setForeground( GUIUtilities.fColor );
        fillStyleCombo = new JComboBox();
        fillStyleCombo.setFont( GUIUtilities.labelFont );


        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_NO_FILL" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_SOLID" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_LARGE_LEFT_HATCH" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_LARGE_RIGHT_HATCH" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_LARGE_CROSS_HATCH" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_SMALL_LEFT_HATCH" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_SMALL_RIGHT_HATCH" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_SMALL_CROSS_HATCH" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_DOT_PATTERN1" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_DOT_PATTERN2" );
        fillStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_DOT_PATTERN3" );
        fillStyleCombo.addItem( msg );

        Box box1 = new Box( BoxLayout.X_AXIS );
        box1.add( lineColorView );
        box1.add( Box.createHorizontalStrut( 5 ) );
        box1.add( lineColorBtn );
        box1.add( Box.createHorizontalGlue() );

        viewTypeCombo.setMaximumSize( new Dimension( 80 , 20 ) );
        lineDashCombo.setMaximumSize( new Dimension( 80 , 20 ) );
        fillStyleCombo.setMaximumSize( new Dimension( 80 , 20 ) );
        viewTypeCombo.setPreferredSize( new Dimension( 80 , 20 ) );
        lineDashCombo.setPreferredSize( new Dimension( 80 , 20 ) );
        fillStyleCombo.setPreferredSize( new Dimension( 80 , 20 ) );

        box1.setPreferredSize( new Dimension( 80 , 20 ) );
        box1.setMaximumSize( new Dimension( 80 , 20 ) );
        box2.setPreferredSize( new Dimension( 80 , 20 ) );
        box2.setMaximumSize( new Dimension( 80 , 20 ) );

        curvePanel.add( viewTypeLabel );
        curvePanel.add( Box.createHorizontalStrut( 27 ) );
        curvePanel.add( viewTypeCombo );
        curvePanel.add( Box.createHorizontalGlue() );

        curvePanel.add( lineColorLabel );
        curvePanel.add( Box.createHorizontalStrut( 27 ) );
        curvePanel.add( box1 );
        curvePanel.add( Box.createHorizontalGlue() );

        curvePanel.add( fillColorLabel );
        curvePanel.add( Box.createHorizontalStrut( 27 ) );
        curvePanel.add( box2 );
        curvePanel.add( Box.createHorizontalGlue() );

        curvePanel.add( lineWidthLabel );
        curvePanel.add( Box.createHorizontalStrut( 27 ) );
        curvePanel.add( lineWidthSpinner );
        curvePanel.add( Box.createHorizontalGlue() );

        curvePanel.add( lineDashLabel );
        curvePanel.add( Box.createHorizontalStrut( 27 ) );
        curvePanel.add( lineDashCombo );
        curvePanel.add( Box.createHorizontalGlue() );

        curvePanel.add( fillStyleLabel );
        curvePanel.add( Box.createHorizontalStrut( 27 ) );
        curvePanel.add( fillStyleCombo );
        curvePanel.add( Box.createHorizontalGlue() );

        curvePanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( curvePanel ,
                                         6 , 4 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad
        return curvePanel;

    }

    private JPanel initFactorPanel ()
    {
        String msg;
        JPanel factorPanel = new JPanel();
        
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_FACTOR" );
        TitledBorder factorPanelBorder = GUIUtilities.getPlotSubPanelsLineBorder( msg, Color.RED );
        factorPanel.setBorder( factorPanelBorder );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_FACTOR" );
        factorField = new JTextField( "1.0" );
        factorField.setMaximumSize( new Dimension(Integer.MAX_VALUE, 25) );
        factorFieldListener = new FactorFieldListener();
        factorField.addActionListener( factorFieldListener );
        factorField.setToolTipText("Press Enter");
        //factorField.setColumns( 10 );
        
        /*factorLabel = new JLabel( "Balancing Factor" );
        factorLabel = new JLabel(msg );
        factorLabel.setFont( GUIUtilities.labelFont );
        factorLabel.setForeground( GUIUtilities.fColor );*/
        
//        SetButton Creation
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_SET" );
//        setBalancingFactorButton = new JButton( msg );
//        setBalancingFactorButton.setMargin(new Insets(0,0,0,0));
//        setBalancingFactorButton.setMaximumSize( new Dimension( Integer.MAX_VALUE , 20 ) );
 
        factorPanel.add(factorField);
        factorPanel.add(Box.createVerticalStrut(5));
// 		factorPanel.add( setBalancingFactorButton );
 		
        factorPanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( 
                factorPanel ,
                factorPanel.getComponentCount() , 1 , //rows, cols
                6 , 0 , //initX, initY
                6 , 0 , //xpad, ypad
                true    //make cells same width
        );
            
        /*Dimension size = factorLabel.getPreferredSize();
        size.height = 20;
        factorLabel.setPreferredSize( size );
        factorPanel.add( factorLabel );
        size = factorField.getPreferredSize();
        size.height = 20;
        factorField.setPreferredSize( size );
        factorField.setMaximumSize( size );
        factorPanel.add( factorField );
        factorPanel.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid( factorPanel ,
                                         1 , 2 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );*/
        return factorPanel;
    }

    private JPanel initAxisChoicePanel ()
    {
        JPanel axisChoicePanel = new JPanel();

        String msg;

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE" );
        TitledBorder axisPanelBorder = GUIUtilities.getPlotSubPanelsLineBorder( msg, new Color(0,150,0) );
        axisChoicePanel.setBorder( axisPanelBorder );
        
        /*JLabel axisChoiceLabel = new JLabel( msg );
        axisChoiceLabel.setFont( GUIUtilities.labelFont );
        axisChoiceLabel.setForeground( GUIUtilities.fColor );*/
        
        String[] msgListItem = new String[3];
        msgListItem[0] = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_Y1" );
        msgListItem[1] = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_Y2" );
        msgListItem[2] = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_X" );
        axisChoiceComboModel = new DefaultComboBoxModel( msgListItem );
        axisChoiceCombo = new JComboBox( axisChoiceComboModel );
        axisChoiceCombo.setFont( GUIUtilities.labelFont );
        axisChoiceCombo.setMaximumSize( new Dimension(Integer.MAX_VALUE, 25) );
        axisChoiceComboListener = new AxisChoiceComboListener();
        axisChoiceCombo.addActionListener( axisChoiceComboListener );
  
          // SetButton Creation
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_SET" );
//        setAxisButton = new JButton( msg );
//        setAxisButton.setMaximumSize( new Dimension( Integer.MAX_VALUE , 20 ) );
//        setAxisButton.setMargin(new Insets(0,0,0,0));
        
        axisChoicePanel.add(axisChoiceCombo);
        axisChoicePanel.add(Box.createVerticalStrut(5));
//        axisChoicePanel.add( setAxisButton );
        
        axisChoicePanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( 
                axisChoicePanel ,
                axisChoicePanel.getComponentCount() , 1 , //rows, cols
                6 , 0 , //initX, initY
                6 , 0 , //xpad, ypad
                true    //make cells same width
        );   
        
        
        
        /*Dimension size = axisChoiceLabel.getPreferredSize();
        size.height = 20;
        axisChoiceLabel.setPreferredSize( size );
        //axisChoiceLabel.setPreferredSize( new Dimension (100,20) );
        axisChoiceCombo.setMaximumSize( new Dimension( 125 , 20 ) );
        axisChoiceCombo.setPreferredSize( new Dimension( 125 , 20 ) );

        axisChoicePanel.add( axisChoiceLabel );
        axisChoicePanel.add( axisChoiceCombo );

        axisChoicePanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( axisChoicePanel ,
                                         1 , 2 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad*/

        return axisChoicePanel;

    }

    /**
     * 20 juil. 2005
     */
    private void initCenterPanel ()
    {
        centerPanel = new JPanel();

        String msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_GRAPHICAL_TITLE" );
        TitledBorder centerPanelBorder = GUIUtilities.getPlotSubPanelsLineBorder( msg, Color.BLUE );
        centerPanel.setBorder( centerPanelBorder );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_HIDDEN" );
        hiddenCheckBox = new JCheckBox( msg );
        hiddenCheckBox.setSelected( false );

        JPanel curvePanel = initCurvePanel();
        JPanel markerPanel = initMarkerPanel();
        JPanel barPanel = initBarPanel();
        JPanel transformPanel = initTransformPanel();
        //JPanel organizePanel = initOrganizePanel();
        
        //String msg;
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_SET" );
//        setButton = new JButton( new SetAttributesGroupProperties( msg, expression ) );
//        setButton.setMaximumSize( new Dimension( Integer.MAX_VALUE , 20 ) );
//        setButton.setMargin( new Insets(0,0,0,0) );
        
        //centerPanel.add( organizePanel );
        centerPanel.add( hiddenCheckBox );
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add( curvePanel );
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add( barPanel );
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add( markerPanel );
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add( transformPanel );
        centerPanel.add(Box.createVerticalGlue());
//		centerPanel.add( setButton );

        centerPanel.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid
                ( centerPanel ,
                  centerPanel.getComponentCount() , 1 , //rows, cols
                  6 , 0 , //initX, initY
                  6 , 0 , //xPad, yPad
                  true    //make cells same width
        );
    }

    /**
     * @return
     */
    //private JPanel initOrganizePanel ()
    private void initOrganizePanel ()
    {
     //JPanel ret;
    JPanel axisPanel;
    JPanel balFactorPanel;
    
    	//  Main panel
    	topPanel = new JPanel();
        axisPanel = initAxisChoicePanel();
        balFactorPanel = initFactorPanel();
        
        topPanel.setLayout(new SpringLayout());
        topPanel.add( axisPanel);
        topPanel.add( balFactorPanel);
        SpringUtilities.makeCompactGrid(
                topPanel,
                1, topPanel.getComponentCount(),
                0, 0,
                5, 0,
                true
        );
        
         
        /*

        // Balancing factor creation
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_FACTOR" );
        factorField = new JTextField( "1.0" );
        //factorField.setColumns(10);
        //factorLabel = new JLabel( "Balancing Factor" );
        factorLabel = new JLabel(msg );
        factorLabel.setFont( GUIUtilities.labelFont );
        factorLabel.setForeground( GUIUtilities.fColor );

        // Axis content
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE" );
        JLabel axisChoiceLabel = new JLabel( msg );
        axisChoiceLabel.setFont( GUIUtilities.labelFont );
        axisChoiceLabel.setForeground( GUIUtilities.fColor );
        axisChoiceCombo = new JComboBox();
        axisChoiceCombo.setFont( GUIUtilities.labelFont );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_Y1" );
        axisChoiceCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_Y2" );
        axisChoiceCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_X" );
        axisChoiceCombo.addItem( msg );

        factorField.setPreferredSize( new Dimension( 50 , 20 ) );

        axisChoiceCombo.setMaximumSize( new Dimension( 50 , 20 ) );
        axisChoiceCombo.setPreferredSize( new Dimension( 50 , 20 ) );

        Box box = new Box( BoxLayout.X_AXIS );

        box.add( axisChoiceLabel );
        box.add( Box.createHorizontalStrut( 5 ) );
        box.add( axisChoiceCombo );
        box.add( Box.createHorizontalGlue() );

        box.add( Box.createHorizontalStrut( 10 ) );

        box.add( factorLabel );
        box.add( Box.createHorizontalStrut( 5 ) );
        box.add( factorField );
        */
              
 
        
       /* topPanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( topPanel ,
                                         1 , 2 , //rows, cols
                                         0 , 0 , //initX, initY
                                         2 , 2 ,
                                         true );       //xPad, yPad*/
        
        //return ret;
    }
    
  
    /**
     * @return 22 août 2005
     */
    private JPanel initTransformPanel ()
    {
        JPanel transformPanel = new JPanel();
        JPanel transformFieldsPanel = new JPanel();

        //transformPanel.setLayout(null);

        TitledBorder transformPanelBorder = GUIUtilities.getPlotSubPanelsEtchedBorder( "Transform" );
        transformPanel.setBorder( transformPanelBorder );

        //JTextArea transformHelpLabel = new JTextArea("Apply a polynomial transform to the data view: y' = A0 + A1*y + A2*y^2");
        /*JTextArea transformHelpLabel1 = new JTextArea("Apply a polynomial transform to the data view:");
        JTextArea transformHelpLabel2 = new JTextArea("y' = A0 + A1*y + A2*y^2");*/
        JLabel transformHelpLabel1 = new JLabel( "Apply a polynomial transform to the data view:" );
        JLabel transformHelpLabel2 = new JLabel( "y' = A0 + A1*y + A2*y^2" );

        transformHelpLabel1.setFont( GUIUtilities.labelFont );
        transformHelpLabel1.setForeground( GUIUtilities.fColor );
        //transformHelpLabel1.setEditable(false);

        transformHelpLabel2.setFont( GUIUtilities.labelFont );
        transformHelpLabel2.setForeground( GUIUtilities.fColor );
        //transformHelpLabel2.setEditable(false);

        JLabel transformA0Label = new JLabel( "A0" );
        transformA0Label.setFont( GUIUtilities.labelFont );
        transformA0Label.setForeground( GUIUtilities.fColor );
        transformA0Text = new JTextField();
        transformA0Text.setEditable( true );
        //transformA0Text.setMaximumSize( new Dimension (100,20) );

        JLabel transformA1Label = new JLabel( "A1" );
        transformA1Label.setFont( GUIUtilities.labelFont );
        transformA1Label.setForeground( GUIUtilities.fColor );
        transformA1Text = new JTextField();
        transformA1Text.setEditable( true );
        //transformA1Text.setMaximumSize( new Dimension (100,20) );

        JLabel transformA2Label = new JLabel( "A2" );
        transformA2Label.setFont( GUIUtilities.labelFont );
        transformA2Label.setForeground( GUIUtilities.fColor );
        transformA2Text = new JTextField();
        transformA2Text.setEditable( true );
        //transformA2Text.setMaximumSize( new Dimension (100,20) );

        transformFieldsPanel.add( transformA0Label );
        transformFieldsPanel.add( Box.createHorizontalStrut( 74 ) );
        transformFieldsPanel.add( transformA0Text );
        transformFieldsPanel.add( Box.createHorizontalGlue() );

        transformFieldsPanel.add( transformA1Label );
        transformFieldsPanel.add( Box.createHorizontalStrut( 74 ) );
        transformFieldsPanel.add( transformA1Text );
        transformFieldsPanel.add( Box.createHorizontalGlue() );

        transformFieldsPanel.add( transformA2Label );
        transformFieldsPanel.add( Box.createHorizontalStrut( 74 ) );
        transformFieldsPanel.add( transformA2Text );
        transformFieldsPanel.add( Box.createHorizontalGlue() );

        transformFieldsPanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( transformFieldsPanel ,
                                         3 , 4 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad

        transformPanel.setLayout( new BoxLayout( transformPanel , BoxLayout.Y_AXIS ) );
        transformPanel.add( transformHelpLabel1 );
        transformPanel.add( transformHelpLabel2 );
        transformPanel.add( transformFieldsPanel );

        return transformPanel;

    }

    /**
     * @return 22 août 2005
     */
    private JPanel initBarPanel ()
    {
        JPanel barPanel = new JPanel();
        //barPanel.setLayout(null);
        String msg;

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_TITLE" );
        TitledBorder barPanelBorder = GUIUtilities.getPlotSubPanelsEtchedBorder( "Bar" );
        barPanel.setBorder( barPanelBorder );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_WIDTH" );
        JLabel barWidthLabel = new JLabel( "Bar Width" );
        barWidthLabel.setFont( GUIUtilities.labelFont );
        barWidthLabel.setForeground( GUIUtilities.fColor );
        barWidthSpinner = new JSpinner();
        //value = new Integer(dataView.getBarWidth());
        Integer value = new Integer( 50 );
        Integer min = new Integer( 0 );
        Integer max = new Integer( 100 );
        Integer step = new Integer( 1 );
        SpinnerNumberModel spModel = new SpinnerNumberModel( value , min , max , step );
        barWidthSpinner.setModel( spModel );
        barWidthSpinner.setMaximumSize( new Dimension( 125 , 20 ) );
        //barWidthSpinner.addChangeListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD" );
        JLabel fillMethodLabel = new JLabel( msg );
        fillMethodLabel.setFont( GUIUtilities.labelFont );
        fillMethodLabel.setForeground( GUIUtilities.fColor );
        fillMethodCombo = new JComboBox();
        fillMethodCombo.setFont( GUIUtilities.labelFont );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD_FROM_UP" );
        fillMethodCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD_FROM_ZERO" );
        fillMethodCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD_FROM_BOTTOM" );
        fillMethodCombo.addItem( msg );
        fillMethodCombo.setMaximumSize( new Dimension( 125 , 20 ) );
        fillMethodCombo.setPreferredSize( new Dimension( 125 , 20 ) );
        //fillMethodCombo.setSelectedIndex(dataView.getFillMethod());
        //fillMethodCombo.addActionListener(this);

        barPanel.add( barWidthLabel );
        barPanel.add( Box.createHorizontalStrut( 10 ) );
        barPanel.add( barWidthSpinner );
        barPanel.add( Box.createHorizontalGlue() );

        barPanel.add( fillMethodLabel );
        barPanel.add( Box.createHorizontalStrut( 10 ) );
        barPanel.add( fillMethodCombo );
        barPanel.add( Box.createHorizontalGlue() );

        barPanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( barPanel ,
                                         2 , 4 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad
        return barPanel;
    }

    /**
     * @return 22 août 2005
     */
    private JPanel initMarkerPanel ()
    {
        JPanel markerPanel = new JPanel();
        //markerPanel.setLayout(null);
        String msg;

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_TITLE" );
        TitledBorder markerPanelBorder = GUIUtilities.getPlotSubPanelsEtchedBorder( "Marker" );
        markerPanel.setBorder( markerPanelBorder );

        markerColorView = new JLabel( "                " );
        markerColorView.setBackground( Color.RED );
        markerColorView.setOpaque( true );
        markerColorView.setBorder( BorderFactory.createLineBorder( Color.black ) );

        markerColorBtn = new JButton( "..." );
        markerColorBtn.setMaximumSize( new Dimension( 30 , 20 ) );
        Box box1 = new Box( BoxLayout.X_AXIS );
        box1.add( markerColorView );
        box1.add( Box.createHorizontalStrut( 5 ) );
        box1.add( markerColorBtn );
        markerColorBtn.addMouseListener( this );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_COLOR" );
        JLabel markerColorLabel = new JLabel( "Color" );
        markerColorLabel.setFont( GUIUtilities.labelFont );
        markerColorLabel.setForeground( GUIUtilities.fColor );

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_SIZE" );
        JLabel markerSizeLabel = new JLabel( "Size" );
        markerSizeLabel.setFont( GUIUtilities.labelFont );
        markerSizeLabel.setForeground( GUIUtilities.fColor );

        markerSizeSpinner = new JSpinner();
        //value = new Integer(dataView.getMarkerSize());
        Integer value = new Integer( 5 );
        Integer min = new Integer( 0 );
        Integer max = new Integer( 100 );
        Integer step = new Integer( 1 );
        SpinnerNumberModel spModel = new SpinnerNumberModel( value , min , max , step );
        markerSizeSpinner.setModel( spModel );
        markerSizeSpinner.setMaximumSize( new Dimension( 125 , 20 ) );
        //markerSizeSpinner.addChangeListener(this);

        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE" );
        JLabel markerStyleLabel = new JLabel( msg );
        markerStyleLabel.setFont( GUIUtilities.labelFont );
        markerStyleLabel.setForeground( GUIUtilities.fColor );

        markerStyleCombo = new JComboBox();
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_NONE" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_DOT" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_BOX" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_TRIANGLE" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_DIAMOND" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_STAR" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_VERTLINE" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_HORZLINE" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_CROSS" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_CIRCLE" );
        markerStyleCombo.addItem( msg );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_SQUARE" );
        markerStyleCombo.addItem( msg );
        markerStyleCombo.setMaximumSize( new Dimension( 125 , 20 ) );
        markerStyleCombo.setPreferredSize( new Dimension( 125 , 20 ) );

        labelVisibleCheck = new JCheckBox();
        labelVisibleCheck.setFont( GUIUtilities.labelFont );
        labelVisibleCheck.setForeground( GUIUtilities.fColor );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_LEGEND_VISIBLE" );
        JLabel labelVisibleLabel = new JLabel( msg );
        labelVisibleLabel.setFont( GUIUtilities.labelFont );
        labelVisibleLabel.setForeground( GUIUtilities.fColor );
        //labelVisibleCheck.setText ( msg );

        markerPanel.add( markerColorLabel );
        markerPanel.add( Box.createHorizontalStrut( 9 ) );
        markerPanel.add( box1 );
        markerPanel.add( Box.createHorizontalGlue() );

        markerPanel.add( markerSizeLabel );
        markerPanel.add( Box.createHorizontalStrut( 9 ) );
        markerPanel.add( markerSizeSpinner );
        markerPanel.add( Box.createHorizontalGlue() );

        markerPanel.add( markerStyleLabel );
        markerPanel.add( Box.createHorizontalStrut( 9 ) );
        markerPanel.add( markerStyleCombo );
        markerPanel.add( Box.createHorizontalGlue() );

        markerPanel.add( labelVisibleLabel );
        markerPanel.add( Box.createHorizontalStrut( 9 ) );
        markerPanel.add( labelVisibleCheck );
        markerPanel.add( Box.createHorizontalGlue() );

        markerPanel.setLayout( new SpringLayout() );
        //Lay out the panel.
        SpringUtilities.makeCompactGrid( markerPanel ,
                                         4 , 4 , //rows, cols
                                         6 , 6 , //initX, initY
                                         6 , 6 ,
                                         true );       //xPad, yPad

        return markerPanel;
    }

    /**
     * @param viewType 25 août 2005
     */
    public void setViewType ( int viewType )
    {
        viewTypeCombo.setSelectedIndex( viewType );
    }

    public void setAxisChoice ( int axisChoice )
    {
    	axisChoiceCombo.removeActionListener( axisChoiceComboListener );
//    	axisChoiceComboModel.setSelectedItem(anObject);
        axisChoiceCombo.setSelectedIndex( axisChoice );
        axisChoiceCombo.addActionListener( axisChoiceComboListener );
    }

    /**
     * @param a0 25 août 2005
     */
    public void setTransformA0 ( double a0 )
    {
        if ( ! Double.isNaN ( a0 ) )
        {
            transformA0Text.setText( String.valueOf( a0 ) );    
        }
        else
        {
            transformA0Text.setText( "" );    
        }
    }

    /**
     * @param a0 25 août 2005
     */
    public void setTransformA1 ( double a1 )
    {
        if ( ! Double.isNaN ( a1 ) )
        {
            transformA1Text.setText( String.valueOf( a1 ) );    
        }
        else
        {
            transformA1Text.setText( "" );    
        }
    }

    /**
     * @param a0 25 août 2005
     */
    public void setTransformA2 ( double a2 )
    {
        if ( ! Double.isNaN ( a2 ) )
        {
            transformA2Text.setText( String.valueOf( a2 ) );    
        }
        else
        {
            transformA2Text.setText( "" );    
        }
    }

    /**
     * @param fillColor 25 août 2005
     */
    public void setBarFillColor ( Color _fillColor )
    {
        fillColorView.setBackground( _fillColor );
    }

    /**
     * @param fillingMethod 25 août 2005
     */
    public void setBarFillingMethod ( int _fillingMethod )
    {
        fillMethodCombo.setSelectedIndex( _fillingMethod );
    }

    /**
     * @param fillStyle 25 août 2005
     */
    public void setBarFillStyle ( int _fillStyle )
    {
        fillStyleCombo.setSelectedIndex( _fillStyle );
    }

    /**
     * @param width 25 août 2005
     */
    public void setBarWidth ( int width )
    {
        barWidthSpinner.setValue( new Integer( width ) );
    }

    /**
     * @param color 25 août 2005
     */
    public void setCurveColor ( Color _color )
    {
        lineColorView.setBackground( _color );
    }

    /**
     * @param lineStyle 25 août 2005
     */
    public void setCurveLineStyle ( int _lineStyle )
    {
        lineDashCombo.setSelectedIndex( _lineStyle + 1 );
    }

    /**
     * @param width 25 août 2005
     */
    public void setCurveWidth ( int _width )
    {
        lineWidthSpinner.setValue( new Integer( _width ) );
    }

    /**
     * @param color 25 août 2005
     */
    public void setMarkerColor ( Color _color )
    {
        markerColorView.setBackground( _color );
    }

    /**
     * @param size 25 août 2005
     */
    public void setMarkerSize ( int _size )
    {
        markerSizeSpinner.setValue( new Integer( _size ) );
    }

    /**
     * @param style 25 août 2005
     */
    public void setMarkerStyle ( int _style )
    {
        markerStyleCombo.setSelectedIndex( _style );
    }

    public boolean isHidden() {
        return hiddenCheckBox.isSelected();
    }

    public void setHidden(boolean hidden) {
        hiddenCheckBox.setSelected(hidden);
    }

    //Mouse Listener
    public void mouseClicked ( MouseEvent e )
    {
        /*if (e.getSource() == closeBtn)
        {
          hide();
          dispose();
        }
        else */
        if ( e.getSource() == lineColorBtn )
        {
            Color c = JColorChooser.showDialog( this , "Choose Line Color" , lineColorView.getBackground() );
            if ( c != null )
            {
                lineColorView.setBackground( c );
            }
        }
        else if ( e.getSource() == fillColorBtn )
        {
            Color c = JColorChooser.showDialog( this , "Choose Fill Color" , fillColorView.getBackground() );
            if ( c != null )
            {
                fillColorView.setBackground( c );
            }
        }
        else if ( e.getSource() == markerColorBtn )
        {
            Color c = JColorChooser.showDialog( this , "Choose marker Color" , markerColorView.getBackground() );
            if ( c != null )
            {
                markerColorView.setBackground( c );
            }
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased ( MouseEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

	public boolean isExpression() {
		return expression;
	}
	
	public void setEnabled(boolean enabled){
	    
		viewTypeCombo.setEnabled( enabled );
	    hiddenCheckBox.setEnabled( enabled );

	    lineWidthSpinner.setEnabled( enabled );
	    lineWidthSpinner.setFocusable( enabled );
	    lineDashCombo.setEnabled( enabled );
	    lineColorBtn.setEnabled( enabled );

	    barWidthSpinner.setEnabled( enabled );
	    barWidthSpinner.setFocusable( enabled );
	    fillStyleCombo.setEnabled( enabled );
	    fillMethodCombo.setEnabled( enabled );
	    fillColorBtn.setEnabled( enabled );

	    markerSizeSpinner.setEnabled( enabled );
	    markerSizeSpinner.setFocusable( enabled );
	    markerStyleCombo.setEnabled( enabled );
	    labelVisibleCheck.setEnabled( enabled );
	    markerColorBtn.setEnabled( enabled );
	    
	    transformA0Text.setEnabled( enabled );
	    transformA1Text.setEnabled( enabled );
	    transformA2Text.setEnabled( enabled );
	    
	    
	    
	}

	public void ajouterObservateur(Observateur o) {
		
	}

	public void supprimerObservateur(Observateur o) {
		
	}
	
	public void notifierObservateurs() {
		// TODO notifierObservateurs
		Vector attributes = VCAttributesPropertiesTree.getInstance().getListOfAttributesToSet();
		if ( attributes == null )
        {
            return;//nothing to set
        }
	}

	public JComboBox getAxisChoiceCombo() {
		return axisChoiceCombo;
	}

	public DefaultComboBoxModel getDefaultComboBoxModel() {
		// TODO Auto-generated method stub
		return axisChoiceComboModel;
	}

	public AxisChoiceComboListener getAxisChoiceComboListener() {
		return axisChoiceComboListener;
	}
	
	public JTextField getFactorField() {
		return factorField;
	}

	public FactorFieldListener getFactorFieldListener() {
		return factorFieldListener;
	}


}
