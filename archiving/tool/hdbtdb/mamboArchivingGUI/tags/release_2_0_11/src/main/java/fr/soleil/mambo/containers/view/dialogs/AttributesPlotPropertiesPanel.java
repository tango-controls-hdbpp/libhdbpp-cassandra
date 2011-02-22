// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/AttributesPlotPropertiesPanel.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class AttributesPlotPropertiesPanel.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.13 $
//
// $Log: AttributesPlotPropertiesPanel.java,v $
// Revision 1.13 2007/08/24 09:23:00 ounsy
// Color index reset on new VC (Mantis bug 5210 part1)
//
// Revision 1.12 2007/03/20 14:15:09 ounsy
// added the possibility to hide a dataview
//
// Revision 1.11 2007/02/27 10:06:32 ounsy
// corrected the transformation property bug
//
// Revision 1.10 2007/01/11 14:05:47 ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.9 2006/10/19 09:21:21 ounsy
// now the color rotation index of vc attributes is saved
//
// Revision 1.8 2006/10/17 14:31:06 ounsy
// extended color management
//
// Revision 1.7 2006/10/02 15:00:10 ounsy
// JLChart has now a white background : dataviews can not have white color
//
// Revision 1.6 2006/10/02 14:13:25 ounsy
// minor changes (look and feel)
//
// Revision 1.5 2006/09/20 13:03:29 chinkumo
// Mantis 2230: Separated Set addition in the "Attributes plot properties" tab
// of the New/Modify VC View
//
// Revision 1.4 2006/05/19 15:05:29 ounsy
// minor changes
//
// Revision 1.3 2005/12/15 11:31:15 ounsy
// minor changes
//
// Revision 1.2 2005/11/29 18:27:45 chinkumo
// no message
//
// Revision 1.1.2.3 2005/09/15 10:30:05 chinkumo
// Third commit !
//
// Revision 1.1.2.2 2005/09/14 15:41:20 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import fr.esrf.tangoatk.widget.util.chart.JLAxis;
import fr.esrf.tangoatk.widget.util.chart.JLDataView;
import fr.soleil.mambo.actions.view.dialogs.listeners.AxisChoiceComboListener;
import fr.soleil.mambo.actions.view.dialogs.listeners.FactorFieldListener;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.tools.ColorGenerator;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class AttributesPlotPropertiesPanel extends JPanel implements
        ActionListener {

    private static final long serialVersionUID = 5730694350280414275L;

    /**
     * @author MAINGUY enum that designates the possible line styles for data
     *         drawing.
     */
    public static enum LineStyle {
        // name is a key suffix for properties' entries
        // value is meant to reflect JLDataView constants
        NONE(-1), SOLID(JLDataView.STYLE_SOLID), DOT(JLDataView.STYLE_DOT), DASH(
                JLDataView.STYLE_DASH), LONG_DASH(JLDataView.STYLE_LONG_DASH), DASH_DOT(
                JLDataView.STYLE_DASH_DOT);

        private final int    value;
        private final String text;
        private final Image  image;

        private LineStyle(int value) {
            this.value = value;
            text = Messages
                    .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_"
                            + name());
            image = getImage(value);
        }

        private static Image getImage(int value) {
            Image result = null;
            BasicStroke bs = null;
            switch (value) {
                case JLDataView.STYLE_SOLID:
                    bs = new BasicStroke();
                    break;
                case JLDataView.STYLE_DOT:
                    bs = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER, 10.0f, new float[] { 1.0f,
                                    3.0f }, 0.0f);
                    break;
                case JLDataView.STYLE_DASH:
                    bs = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER, 10.0f,
                            new float[] { 3.0f }, 0.0f);
                    break;
                case JLDataView.STYLE_LONG_DASH:
                    bs = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER, 10.0f,
                            new float[] { 6.0f }, 0.0f);
                    break;
                case JLDataView.STYLE_DASH_DOT:
                    bs = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                            BasicStroke.JOIN_MITER, 10.0f, new float[] { 3.0f,
                                    3.0f, 1.0f, 3.0f }, 0.0f);
                    break;
                default:
                    break;
            }

            result = createImage(bs, 70, 16);

            return result;
        }

        /**
         * Given a BasicStroke, create an ImageIcon that shows it.
         * 
         * @param stroke
         *            the BasicStroke to draw on the Icon.
         * @param width
         *            the width of the icon.
         * @param height
         *            the height of the icon.
         */
        private static Image createImage(Stroke stroke, int width, int height) {
            BufferedImage bigImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) bigImage.getGraphics();

            if (stroke != null) {
                g.setPaint(Color.black);
                g.setStroke(stroke);
                // horizontal drawing
                g.drawLine(0, height / 2, width, height / 2);
            }
            return bigImage;
        }

        public int getValue() {
            return value;
        }

        public Image getImage() {
            return image;
        }

        @Override
        public String toString() {
            return text;
        }

        public static LineStyle getStyleFromValue(int value) {
            LineStyle result = NONE;
            switch (value) {
                case JLDataView.STYLE_SOLID:
                    result = SOLID;
                    break;

                case JLDataView.STYLE_DOT:
                    result = DOT;
                    break;

                case JLDataView.STYLE_DASH:
                    result = DASH;
                    break;

                case JLDataView.STYLE_LONG_DASH:
                    result = LONG_DASH;
                    break;

                case JLDataView.STYLE_DASH_DOT:
                    result = DASH_DOT;
                    break;

                default:
                    break;
            }
            return result;
        }
    }

    private int                     refWidth                 = 80;
    
    private final static int        totalColors              = 31;
    private final static Color[]    defaultColor             = ColorGenerator
                                                                     .generateColors(
                                                                             totalColors,
                                                                             0xFFFFFF);
    private int                     colorIndex;

    // balancing factor management
    private JTextField              factorField;
    private FactorFieldListener     factorFieldListener;
    // private JLabel factorLabel;

    // general objects
    private JPanel                  centerPanel;
    private JPanel                  topPanel;
    private JComboBox               viewTypeCombo;
    private DefaultComboBoxModel    axisChoiceComboModel;
    private JComboBox               axisChoiceCombo;
    private AxisChoiceComboListener axisChoiceComboListener;
    private JCheckBox               hiddenCheckBox;
    private GridBagConstraints      gbc                      = new GridBagConstraints();
    // general objects

    // curve objects
    private JLabel                  lineColorView;
    private JSpinner                lineWidthSpinner;
    private JComboBox               lineDashCombo;
    private JButton                 lineColorBtn;
    private JButton                 applyColorToAllBtn;
    // curve objects

    // bar objects
    private JSpinner                barWidthSpinner;
    private JComboBox               fillStyleCombo;
    private JLabel                  fillColorView;
    private JComboBox               fillMethodCombo;
    private JButton                 fillColorBtn;
    // bar objects

    // marker objects
    private JLabel                  markerColorView;
    private JSpinner                markerSizeSpinner;
    private JComboBox               markerStyleCombo;
    private JCheckBox               labelVisibleCheck;
    private JButton                 markerColorBtn;
    // marker objects

    // transform objects
    private JTextField              transformA0Text;
    private JTextField              transformA1Text;
    private JTextField              transformA2Text;
    // transform objects
    
    // labels
    private JLabel viewTypeLabel;
    private JLabel lineWidthLabel;
    private JLabel lineColorLabel;
    private JLabel lineDashLabel;
    private JLabel fillStyleLabel;
    private JLabel barWidthLabel;
    private JLabel fillMethodLabel;
    private JLabel markerColorLabel;
    private JLabel markerSizeLabel;
    private JLabel markerStyleLabel;
    // labels

    private static final int        DEFAULT_VIEW_TYPE        = 0;                       // line
    private static final int        DEFAULT_CURVE_WIDTH      = 1;                       // 1
    private static final int        DEFAULT_CURVE_LINE_STYLE = 0;                       // solid
    private static final int        DEFAULT_BAR_FILL_STYLE   = 0;                       // no
    // fill
    private static final int        DEFAULT_MARKER_SIZE      = 5;                       // 5
    private static final int        DEFAULT_MARKER_STYLE     = 1;                       // dot
    private static final String     DEFAULT_TRANSFORM_A0     = "";
    private static final String     DEFAULT_TRANSFORM_A1     = "";
    private static final String     DEFAULT_TRANSFORM_A2     = "";

    private ViewConfigurationBean   viewConfigurationBean;
    private VCEditDialog            editDialog;

    /**
	 *
	 */
    public AttributesPlotPropertiesPanel(
            ViewConfigurationBean viewConfigurationBean, VCEditDialog editDialog) {
        super();
        this.viewConfigurationBean = viewConfigurationBean;
        this.editDialog = editDialog;
        this.initComponents();
        this.addComponents();
        colorIndex = 0;
        ViewConfigurationAttributeProperties currentProperties = new ViewConfigurationAttributeProperties();
        ViewConfigurationAttributeProperties
                .setCurrentProperties(currentProperties);
    }

    /**
     * Method to set the curve color to a color selected in default colors.
     * 
     * @return the next available index in the color table (for dataview color
     *         rotation)
     */
    public int rotateCurveColor() {
        applyColorIndex();
        colorIndex = getDefaultNextColorIndex(colorIndex) % defaultColor.length;
        return colorIndex;
    }

    public void applyColorIndex() {
        lineColorView.setBackground(defaultColor[colorIndex]);
        applyLineColorToAll();
    }

    private void applyLineColorToAll() {
        fillColorView.setBackground(lineColorView.getBackground());
        markerColorView.setBackground(lineColorView.getBackground());
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(topPanel);
        this.add(centerPanel);

        topPanel.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.setAlignmentX(CENTER_ALIGNMENT);
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents() {
        // This one became the upper pannel and the centerpanel became the lower
        // pannel
        initTopPanel();
        initCenterPanel();
    }

    public int getViewType() {
        return viewTypeCombo.getSelectedIndex();
    }

    public int getAxisChoice() {
        return axisChoiceCombo.getSelectedIndex();
    }

    public String getAxisChoiceItem() {
        return (String) axisChoiceCombo.getSelectedItem();
    }

    public double getFactor() {
        double factor;
        try {
            factor = Double.parseDouble(factorField.getText());
            if (factor == Double.NaN || factor == Double.POSITIVE_INFINITY
                    || factor == Double.NEGATIVE_INFINITY) {
                factor = 1;
            }
        }
        catch (Exception e) {
            factor = 1;
        }
        return factor;
    }

    public void setFactor(double factor) {
        factorField.setText(factor + "");
    }

    public Curve getCurve() {
        Color _color = null;
        int _width = -1;
        LineStyle _lineStyle = LineStyle.NONE;

        _color = lineColorView.getBackground();
        _lineStyle = (LineStyle) lineDashCombo.getSelectedItem();
        Integer val = (Integer) lineWidthSpinner.getValue();
        _width = val.intValue();
        if (_lineStyle.equals(LineStyle.NONE)) {
            _width = 0;
        }
        else if (_width == 0) {
            _width = 1;
        }

        return new Curve(_color, _width, _lineStyle.getValue());
    }

    public static Curve getDefaultCurve(Color color) {
        return new Curve(color, DEFAULT_CURVE_WIDTH, DEFAULT_CURVE_LINE_STYLE);
    }

    public Bar getBar() {
        Color _fillColor = null;
        int _width = -1;
        int _fillStyle = -1;
        int _fillingMethod = -1;

        _fillColor = fillColorView.getBackground();
        Integer val = (Integer) barWidthSpinner.getValue();
        _width = val.intValue();
        _fillStyle = fillStyleCombo.getSelectedIndex();
        _fillingMethod = fillMethodCombo.getSelectedIndex();

        return new Bar(_fillColor, _width, _fillStyle, _fillingMethod);
    }

    public static Bar getDefaultBar(Color color) {
        return new Bar(color, 1, DEFAULT_BAR_FILL_STYLE, 0);
    }

    public Marker getMarker() {
        Color _color = null;
        int _size = -1;
        int _style = -1;
        boolean _isLegendVisible = false;

        _color = markerColorView.getBackground();
        Integer val = (Integer) markerSizeSpinner.getValue();
        _size = val.intValue();
        _style = markerStyleCombo.getSelectedIndex();
        _isLegendVisible = labelVisibleCheck.isSelected();

        return new Marker(_color, _size, _style, _isLegendVisible);
    }

    public static Marker getDefaultMarker(Color color) {
        return new Marker(color, DEFAULT_MARKER_SIZE, DEFAULT_VIEW_TYPE, false);
    }

    public Polynomial2OrderTransform getTransform() {
        String a0_s = transformA0Text.getText();
        String a1_s = transformA1Text.getText();
        String a2_s = transformA2Text.getText();

        try {
            double a0 = Double.parseDouble(a0_s);
            double a1 = Double.parseDouble(a1_s);
            double a2 = Double.parseDouble(a2_s);

            return new Polynomial2OrderTransform(a0, a1, a2);
        }
        catch (NumberFormatException nfe) {
            return new Polynomial2OrderTransform(Double.NaN, Double.NaN,
                    Double.NaN);
        }
    }

    public static Polynomial2OrderTransform getDefaultTransform() {
        return new Polynomial2OrderTransform(0, 1, 0);
    }

    public void reset() {
        this.setViewType(AttributesPlotPropertiesPanel.DEFAULT_VIEW_TYPE);

        this.setCurveWidth(AttributesPlotPropertiesPanel.DEFAULT_CURVE_WIDTH);
        this
                .setCurveLineStyle(AttributesPlotPropertiesPanel.DEFAULT_CURVE_LINE_STYLE);

        this
                .setBarFillStyle(AttributesPlotPropertiesPanel.DEFAULT_BAR_FILL_STYLE);

        this.setMarkerSize(AttributesPlotPropertiesPanel.DEFAULT_MARKER_SIZE);
        this.setMarkerStyle(AttributesPlotPropertiesPanel.DEFAULT_MARKER_STYLE);

        transformA0Text
                .setText(AttributesPlotPropertiesPanel.DEFAULT_TRANSFORM_A0);
        transformA1Text
                .setText(AttributesPlotPropertiesPanel.DEFAULT_TRANSFORM_A1);
        transformA2Text
                .setText(AttributesPlotPropertiesPanel.DEFAULT_TRANSFORM_A2);
        colorIndex = 0;
        rotateCurveColor();
        colorIndex = 0;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int index) {
        colorIndex = index % defaultColor.length;
    }

    private JPanel initCurvePanel() {
        JPanel curvePanel = new JPanel();

        String msg;

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_TITLE");
        TitledBorder curvePanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder(msg);
        curvePanel.setBorder(curvePanelBorder);

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_VIEW_TYPE");
        viewTypeLabel = new JLabel(msg);
        viewTypeLabel.setFont(GUIUtilities.labelFont);
        viewTypeLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(viewTypeLabel.getPreferredSize().width, refWidth);
        
        viewTypeCombo = new JComboBox();
        viewTypeCombo.setFont(GUIUtilities.labelFont);

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE");
        viewTypeCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_BAR_GRAPH");
        viewTypeCombo.addItem(msg);

        lineColorView = new JLabel("                ");
        lineColorView.setBackground(Color.RED);
        lineColorView.setOpaque(true);
        lineColorView.setBorder(BorderFactory.createLineBorder(Color.black));

        lineColorBtn = new JButton("...");
        lineColorBtn.addActionListener(this);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_COLOR");
        lineColorLabel = new JLabel(msg);
        lineColorLabel.setFont(GUIUtilities.labelFont);
        lineColorLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(lineColorLabel.getPreferredSize().width, refWidth);
        
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_COLOR_SET_ALL");
        applyColorToAllBtn = new JButton(msg);
        applyColorToAllBtn.setMargin(new Insets(0, 0, 0, 0));
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_COLOR_SET_ALL_TOOLTIP");
        applyColorToAllBtn.setToolTipText(msg);
        applyColorToAllBtn.addActionListener(this);

        fillColorView = new JLabel("                ");
        fillColorView.setBackground(Color.RED);
        fillColorView.setOpaque(true);
        fillColorView.setBorder(BorderFactory.createLineBorder(Color.black));
        fillColorBtn = new JButton("...");
        fillColorBtn.addActionListener(this);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_COLOR");
        JLabel fillColorLabel = new JLabel(msg);
        fillColorLabel.setFont(GUIUtilities.labelFont);
        fillColorLabel.setForeground(GUIUtilities.fColor);

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_WIDTH");
        lineWidthLabel = new JLabel(msg);
        lineWidthLabel.setFont(GUIUtilities.labelFont);
        lineWidthLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(lineWidthLabel.getPreferredSize().width, refWidth);
        
        lineWidthSpinner = new JSpinner();
        Integer value = new Integer(2);
        Integer min = new Integer(0);
        Integer max = new Integer(10);
        Integer step = new Integer(1);
        SpinnerNumberModel spModel = new SpinnerNumberModel(value, min, max,
                step);
        lineWidthSpinner.setModel(spModel);

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE");
        lineDashLabel = new JLabel(msg);
        lineDashLabel.setFont(GUIUtilities.labelFont);
        lineDashLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(lineDashLabel.getPreferredSize().width, refWidth);
        
        lineDashCombo = new JComboBox();
        lineDashCombo.setFont(GUIUtilities.labelFont);

        lineDashCombo.addItem(LineStyle.NONE);
        lineDashCombo.addItem(LineStyle.SOLID);
        lineDashCombo.addItem(LineStyle.DOT);
        lineDashCombo.addItem(LineStyle.DASH);
        /*
         * This style removed, reserved for write values in case of read/write
         * attributes lineDashCombo.addItem( LineStyle.LONG_DASH );
         */
        lineDashCombo.addItem(LineStyle.DASH_DOT);
        lineDashCombo.setSelectedItem(LineStyle.SOLID);

        lineDashCombo.setRenderer(new DefaultListCellRenderer() {

            private static final long serialVersionUID = -7699076664442005809L;

            /*
             * Displays an icon representing the style, except for NONE
             * @see
             * javax.swing.DefaultListCellRenderer#getListCellRendererComponent
             * (javax.swing.JList, java.lang.Object, int, boolean, boolean)
             */
            @Override
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                LineStyle style = (LineStyle) value;
                Object theValue = value;
                if (!style.equals(LineStyle.NONE)) {
                    if (lineDashCombo.isEnabled()) {
                        theValue = new ImageIcon(style.getImage());
                    }
                    else {
                        theValue = new ImageIcon(GrayFilter
                                .createDisabledImage(style.getImage()));
                    }
                }
                super.getListCellRendererComponent(list, theValue, index,
                        isSelected, cellHasFocus);

                if (!style.equals(LineStyle.NONE)) {
                    setText(style.toString());
                }

                return this;
            }
        });

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE");
        fillStyleLabel = new JLabel(msg);
        fillStyleLabel.setFont(GUIUtilities.labelFont);
        fillStyleLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(fillStyleLabel.getPreferredSize().width, refWidth);
        
        fillStyleCombo = new JComboBox();
        fillStyleCombo.setFont(GUIUtilities.labelFont);

        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_NO_FILL");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_LINE_STYLE_SOLID");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_LARGE_LEFT_HATCH");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_LARGE_RIGHT_HATCH");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_LARGE_CROSS_HATCH");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_SMALL_LEFT_HATCH");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_SMALL_RIGHT_HATCH");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_SMALL_CROSS_HATCH");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_DOT_PATTERN1");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_DOT_PATTERN2");
        fillStyleCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_FILL_STYLE_DOT_PATTERN3");
        fillStyleCombo.addItem(msg);

        viewTypeCombo.setMaximumSize(new Dimension(80, 20));
        lineDashCombo.setMaximumSize(new Dimension(80, 20));
        fillStyleCombo.setMaximumSize(new Dimension(80, 20));
        viewTypeCombo.setPreferredSize(new Dimension(80, 20));
        lineDashCombo.setPreferredSize(new Dimension(80, 20));
        fillStyleCombo.setPreferredSize(new Dimension(80, 20));

        lineColorBtn.setMaximumSize(new Dimension(30, 20));
        lineColorBtn.setPreferredSize(new Dimension(30, 20));
        fillColorBtn.setMaximumSize(new Dimension(30, 20));
        fillColorBtn.setPreferredSize(new Dimension(30, 20));

        curvePanel.setLayout(new GridBagLayout());
        // For components on the right of this JPanel, we set a margin of 5 px
        // on the right and on the left
        Insets curvePanelLeftAndRightInset = new Insets(5, 5, 0, 5);
        // For other components, we set a left-margin of 5 px
        Insets curvePanelLeftInset = new Insets(5, 5, 0, 0);

        setConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, curvePanelLeftInset);
        curvePanel.add(viewTypeLabel, gbc);
        setConstraints(1, 0, 2, 1, 0, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(viewTypeCombo, gbc);

        setConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, curvePanelLeftInset);
        curvePanel.add(lineColorLabel, gbc);
        setConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(lineColorView, gbc);
        setConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(lineColorBtn, gbc);
        setConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.VERTICAL,
                GridBagConstraints.CENTER, curvePanelLeftAndRightInset);
        curvePanel.add(applyColorToAllBtn, gbc);

        setConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, curvePanelLeftInset);
        curvePanel.add(fillColorLabel, gbc);
        setConstraints(1, 2, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(fillColorView, gbc);
        setConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(fillColorBtn, gbc);

        setConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, curvePanelLeftInset);
        curvePanel.add(lineWidthLabel, gbc);
        setConstraints(1, 3, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(lineWidthSpinner, gbc);

        setConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, curvePanelLeftInset);
        curvePanel.add(lineDashLabel, gbc);
        setConstraints(1, 4, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(lineDashCombo, gbc);

        setConstraints(0, 5, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, curvePanelLeftInset);
        curvePanel.add(fillStyleLabel, gbc);
        setConstraints(1, 5, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, curvePanelLeftInset);
        curvePanel.add(fillStyleCombo, gbc);

        return curvePanel;
    }

    private JPanel initFactorPanel() {
        JPanel factorPanel = new JPanel();
        String msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_FACTOR");
        TitledBorder factorPanelBorder = GUIUtilities
                .getPlotSubPanelsLineBorder(msg, Color.RED);
        CompoundBorder cb = BorderFactory.createCompoundBorder(
                factorPanelBorder, BorderFactory.createEmptyBorder(2, 4, 4, 4));
        factorPanel.setBorder(cb);
        factorField = new JTextField("1.0");
        factorField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        factorFieldListener = new FactorFieldListener(viewConfigurationBean,
                editDialog);
        factorField.addActionListener(factorFieldListener);
        factorField.setToolTipText("Press Enter");
        factorPanel.setLayout(new BoxLayout(factorPanel, BoxLayout.X_AXIS));
        factorPanel.add(factorField);
        return factorPanel;
    }

    private JPanel initAxisChoicePanel() {
        JPanel axisChoicePanel = new JPanel();
        String msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE");
        TitledBorder axisPanelBorder = GUIUtilities.getPlotSubPanelsLineBorder(
                msg, new Color(0, 150, 0));
        CompoundBorder cb = BorderFactory.createCompoundBorder(axisPanelBorder,
                BorderFactory.createEmptyBorder(2, 4, 4, 4));
        axisChoicePanel.setBorder(cb);

        String[] msgListItem = new String[3];
        msgListItem[0] = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_Y1");
        msgListItem[1] = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_Y2");
        msgListItem[2] = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_AXIS_CHOICE_X");
        axisChoiceComboModel = new DefaultComboBoxModel(msgListItem);
        axisChoiceCombo = new JComboBox(axisChoiceComboModel);
        axisChoiceCombo.setFont(GUIUtilities.labelFont);
        axisChoiceCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        axisChoiceComboListener = new AxisChoiceComboListener(
                viewConfigurationBean, editDialog);
        axisChoiceCombo.addActionListener(axisChoiceComboListener);
        axisChoicePanel.setLayout(new BoxLayout(axisChoicePanel,
                BoxLayout.X_AXIS));
        axisChoicePanel.add(axisChoiceCombo);

        return axisChoicePanel;
    }

    /**
     * 20 juil. 2005
     */
    private void initCenterPanel() {
        centerPanel = new JPanel();

        String msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_GRAPHICAL_TITLE");
        TitledBorder centerPanelBorder = GUIUtilities
                .getPlotSubPanelsLineBorder(msg, Color.BLUE);
        CompoundBorder cb = BorderFactory.createCompoundBorder(
                centerPanelBorder, BorderFactory.createEmptyBorder(0, 4, 0, 4));
        centerPanel.setBorder(cb);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_HIDDEN");
        hiddenCheckBox = new JCheckBox(msg);
        hiddenCheckBox.setSelected(false);
        JPanel curvePanel = initCurvePanel();
        JPanel markerPanel = initMarkerPanel();
        JPanel barPanel = initBarPanel();
        JPanel transformPanel = initTransformPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(hiddenCheckBox);
        centerPanel.add(curvePanel);
        centerPanel.add(barPanel);
        centerPanel.add(markerPanel);
        centerPanel.add(transformPanel);
        hiddenCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        curvePanel.setAlignmentX(LEFT_ALIGNMENT);
        barPanel.setAlignmentX(LEFT_ALIGNMENT);
        markerPanel.setAlignmentX(LEFT_ALIGNMENT);
        transformPanel.setAlignmentX(LEFT_ALIGNMENT);
        
        adjustLabelsWidth();
    }

    /**
     * @return
     */
    private void initTopPanel() {
        topPanel = new JPanel();
        JPanel axisPanel = initAxisChoicePanel();
        JPanel balFactorPanel = initFactorPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(axisPanel);
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(balFactorPanel);
    }

    /**
     * @return 22 ao�t 2005
     */
    private JPanel initTransformPanel() {
        JPanel transformPanel = new JPanel();
        JPanel transformFieldsPanel = new JPanel();
        TitledBorder transformPanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder("Transform");
        transformPanel.setBorder(transformPanelBorder);
        JLabel transformHelpLabel1 = new JLabel(
                "Apply a polynomial transform to the data view:");
        JLabel transformHelpLabel2 = new JLabel("y' = A0 + A1*y + A2*y^2");
        transformHelpLabel1.setFont(GUIUtilities.labelFont);
        transformHelpLabel1.setForeground(GUIUtilities.fColor);
        transformHelpLabel2.setFont(GUIUtilities.labelFont);
        transformHelpLabel2.setForeground(GUIUtilities.fColor);
        JLabel transformA0Label = new JLabel("A0");
        transformA0Label.setFont(GUIUtilities.labelFont);
        transformA0Label.setForeground(GUIUtilities.fColor);
        transformA0Text = new JTextField();
        transformA0Text.setEditable(true);
        JLabel transformA1Label = new JLabel("A1");
        transformA1Label.setFont(GUIUtilities.labelFont);
        transformA1Label.setForeground(GUIUtilities.fColor);
        transformA1Text = new JTextField();
        transformA1Text.setEditable(true);
        JLabel transformA2Label = new JLabel("A2");
        transformA2Label.setFont(GUIUtilities.labelFont);
        transformA2Label.setForeground(GUIUtilities.fColor);
        transformA2Text = new JTextField();
        transformA2Text.setEditable(true);
        transformFieldsPanel.add(transformA0Label);
        transformFieldsPanel.add(Box.createHorizontalStrut(74));
        transformFieldsPanel.add(transformA0Text);
        transformFieldsPanel.add(Box.createHorizontalGlue());
        transformFieldsPanel.add(transformA1Label);
        transformFieldsPanel.add(Box.createHorizontalStrut(74));
        transformFieldsPanel.add(transformA1Text);
        transformFieldsPanel.add(Box.createHorizontalGlue());
        transformFieldsPanel.add(transformA2Label);
        transformFieldsPanel.add(Box.createHorizontalStrut(74));
        transformFieldsPanel.add(transformA2Text);
        transformFieldsPanel.add(Box.createHorizontalGlue());
        transformFieldsPanel.setLayout(new SpringLayout());
        // Lay out the panel.
        SpringUtilities.makeCompactGrid(transformFieldsPanel, 3, 4, // rows,
                // cols
                6, 6, // initX, initY
                6, 6, true); // xPad, yPad

        transformPanel
                .setLayout(new BoxLayout(transformPanel, BoxLayout.Y_AXIS));
        transformPanel.add(transformHelpLabel1);
        transformPanel.add(transformHelpLabel2);
        transformPanel.add(transformFieldsPanel);
        return transformPanel;
    }

    /**
     * @return 22 ao�t 2005
     */
    private JPanel initBarPanel() {
        JPanel barPanel = new JPanel();
        String msg;
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_TITLE");
        TitledBorder barPanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder("Bar");
        barPanel.setBorder(barPanelBorder);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_WIDTH");
        barWidthLabel = new JLabel("Bar Width");
        barWidthLabel.setFont(GUIUtilities.labelFont);
        barWidthLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(barWidthLabel.getPreferredSize().width, refWidth);
        
        barWidthSpinner = new JSpinner();
        Integer value = new Integer(50);
        Integer min = new Integer(0);
        Integer max = new Integer(100);
        Integer step = new Integer(1);
        SpinnerNumberModel spModel = new SpinnerNumberModel(value, min, max,
                step);
        barWidthSpinner.setModel(spModel);
        barWidthSpinner.setMaximumSize(new Dimension(125, 20));
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD");
        fillMethodLabel = new JLabel(msg);
        fillMethodLabel.setFont(GUIUtilities.labelFont);
        fillMethodLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(fillMethodLabel.getPreferredSize().width, refWidth);
        
        fillMethodCombo = new JComboBox();
        fillMethodCombo.setFont(GUIUtilities.labelFont);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD_FROM_UP");
        fillMethodCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD_FROM_ZERO");
        fillMethodCombo.addItem(msg);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_BAR_FILL_METHOD_FROM_BOTTOM");
        fillMethodCombo.addItem(msg);
        fillMethodCombo.setMaximumSize(new Dimension(125, 20));
        fillMethodCombo.setPreferredSize(new Dimension(125, 20));

        Component glue1 = Box.createGlue();
        glue1.setPreferredSize(applyColorToAllBtn.getPreferredSize());
        glue1.setMinimumSize(applyColorToAllBtn.getPreferredSize());
        Component glue2 = Box.createGlue();
        glue2.setPreferredSize(applyColorToAllBtn.getPreferredSize());
        glue2.setMinimumSize(applyColorToAllBtn.getPreferredSize());

        barPanel.setLayout(new GridBagLayout());
        Insets barPanelLeftInset = new Insets(5, 5, 0, 0);
        Insets barPanelLeftAndRightInset = new Insets(5, 5, 0, 5);
        setConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, barPanelLeftInset);
        barPanel.add(barWidthLabel, gbc);
        setConstraints(1, 0, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, barPanelLeftInset);
        barPanel.add(barWidthSpinner, gbc);
        setConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.CENTER, barPanelLeftAndRightInset);
        barPanel.add(glue1, gbc);
        setConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, barPanelLeftInset);
        barPanel.add(fillMethodLabel, gbc);
        setConstraints(1, 1, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, barPanelLeftInset);
        barPanel.add(fillMethodCombo, gbc);
        setConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.CENTER, barPanelLeftAndRightInset);
        barPanel.add(glue2, gbc);

        return barPanel;
    }

    /**
     * @return 22 ao�t 2005
     */
    private JPanel initMarkerPanel() {
        JPanel markerPanel = new JPanel();
        String msg;
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_TITLE");
        TitledBorder markerPanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder("Marker");
        markerPanel.setBorder(markerPanelBorder);
        markerColorView = new JLabel("                ");
        markerColorView.setBackground(Color.RED);
        markerColorView.setOpaque(true);
        markerColorView.setBorder(BorderFactory.createLineBorder(Color.black));
        markerColorBtn = new JButton("...");
        markerColorBtn.setMaximumSize(new Dimension(30, 20));
        markerColorBtn.setPreferredSize(new Dimension(30, 20));
        markerColorBtn.addActionListener(this);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_COLOR");
        markerColorLabel = new JLabel("Color");
        markerColorLabel.setFont(GUIUtilities.labelFont);
        markerColorLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(markerColorLabel.getPreferredSize().width, refWidth);
        
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_SIZE");
        markerSizeLabel = new JLabel("Size");
        markerSizeLabel.setFont(GUIUtilities.labelFont);
        markerSizeLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(markerSizeLabel.getPreferredSize().width, refWidth);
        
        markerSizeSpinner = new JSpinner();
        // value = new Integer(dataView.getMarkerSize());
        Integer value = new Integer(5);
        Integer min = new Integer(0);
        Integer max = new Integer(100);
        Integer step = new Integer(1);
        SpinnerNumberModel spModel = new SpinnerNumberModel(value, min, max,
                step);
        markerSizeSpinner.setModel(spModel);
        markerSizeSpinner.setMaximumSize(new Dimension(125, 20));
        // markerSizeSpinner.addChangeListener(this);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE");
        markerStyleLabel = new JLabel(msg);
        markerStyleLabel.setFont(GUIUtilities.labelFont);
        markerStyleLabel.setForeground(GUIUtilities.fColor);
        refWidth = Math.max(markerStyleLabel.getPreferredSize().width, refWidth);
        
        markerStyleCombo = new JComboBox();
        JLabel labelMarker = null;
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_NONE");
        labelMarker = new JLabel(msg, JLabel.LEFT);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_DOT");
        labelMarker = createMarkerItem(JLDataView.MARKER_DOT, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_BOX");
        labelMarker = createMarkerItem(JLDataView.MARKER_BOX, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_TRIANGLE");
        labelMarker = createMarkerItem(JLDataView.MARKER_TRIANGLE, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_DIAMOND");
        labelMarker = createMarkerItem(JLDataView.MARKER_DIAMOND, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_STAR");
        labelMarker = createMarkerItem(JLDataView.MARKER_STAR, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_VERTLINE");
        labelMarker = createMarkerItem(JLDataView.MARKER_VERT_LINE, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_HORZLINE");
        labelMarker = createMarkerItem(JLDataView.MARKER_HORIZ_LINE, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_CROSS");
        labelMarker = createMarkerItem(JLDataView.MARKER_CROSS, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_CIRCLE");
        labelMarker = createMarkerItem(JLDataView.MARKER_CIRCLE, msg);
        markerStyleCombo.addItem(labelMarker);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_STYLE_SQUARE");
        labelMarker = createMarkerItem(JLDataView.MARKER_SQUARE, msg);
        markerStyleCombo.addItem(labelMarker);
        markerStyleCombo.setMaximumSize(new Dimension(125, 20));
        markerStyleCombo.setPreferredSize(new Dimension(125, 20));
        markerStyleCombo.setRenderer(new DefaultListCellRenderer() {

            private static final long serialVersionUID = -8584156969032858682L;

            @Override
            public Component getListCellRendererComponent(JList list,
                    Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                // the use of the super method avoid us to manage selection
                // colors
                super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);

                JLabel label = (JLabel) value;
                setText(label.getText());
                if (markerStyleCombo.isEnabled()) {
                    setIcon(label.getIcon());
                }
                else {
                    setIcon(label.getDisabledIcon());
                }
                return this;
            }
        });

        Component glue = Box.createGlue();
        glue.setPreferredSize(applyColorToAllBtn.getPreferredSize());
        glue.setMinimumSize(applyColorToAllBtn.getPreferredSize());

        labelVisibleCheck = new JCheckBox();
        labelVisibleCheck.setFont(GUIUtilities.labelFont);
        labelVisibleCheck.setForeground(GUIUtilities.fColor);
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_MARKER_LEGEND_VISIBLE");
        JLabel labelVisibleLabel = new JLabel(msg);
        labelVisibleLabel.setFont(GUIUtilities.labelFont);
        labelVisibleLabel.setForeground(GUIUtilities.fColor);

        markerPanel.setLayout(new GridBagLayout());
        Insets markerPanelLeftInset = new Insets(5, 5, 0, 0);
        Insets markerPanelLeftAndRightInset = new Insets(5, 5, 0, 5);

        setConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, markerPanelLeftInset);
        markerPanel.add(markerColorLabel, gbc);
        setConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, markerPanelLeftInset);
        markerPanel.add(markerColorView, gbc);
        setConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, markerPanelLeftAndRightInset);
        markerPanel.add(markerColorBtn, gbc);
        setConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, markerPanelLeftAndRightInset);
        markerPanel.add(glue, gbc);

        setConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, markerPanelLeftInset);
        markerPanel.add(markerSizeLabel, gbc);
        setConstraints(1, 1, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, markerPanelLeftAndRightInset);
        markerPanel.add(markerSizeSpinner, gbc);

        setConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, markerPanelLeftInset);
        markerPanel.add(markerStyleLabel, gbc);
        setConstraints(1, 2, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, markerPanelLeftAndRightInset);
        markerPanel.add(markerStyleCombo, gbc);

        setConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, markerPanelLeftInset);
        markerPanel.add(labelVisibleLabel, gbc);
        setConstraints(1, 3, 2, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST, new Insets(5, 0, 0, 0));
        markerPanel.add(labelVisibleCheck, gbc);

        return markerPanel;
    }

    private JLabel createMarkerItem(int mType, String msg) {
        Image image = createMarkerImage(mType);
        JLabel labelMarker = new JLabel(msg, new ImageIcon(image), JLabel.LEFT);
        labelMarker.setDisabledIcon(new ImageIcon(GrayFilter
                .createDisabledImage(image)));
        return labelMarker;
    }

    /**
     * Generates an image that shows a marker
     * 
     * @param mType
     *            the type of marker, as defined in JLAxis
     * @return the generated image
     */
    private Image createMarkerImage(int mType) {
        int width = 16;
        int height = 16;
        int mSize = 8;

        BufferedImage bigImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bigImage.getGraphics();
        g.setPaint(Color.black);
        JLAxis.paintMarker(g, mType, mSize, width / 2, height / 2);

        return bigImage;
    }

    /**
     * @param viewType
     *            25 ao�t 2005
     */
    public void setViewType(int viewType) {
        viewTypeCombo.setSelectedIndex(viewType);
    }

    public void setAxisChoice(int axisChoice) {
        axisChoiceCombo.removeActionListener(axisChoiceComboListener);
        axisChoiceCombo.setSelectedIndex(axisChoice);
        axisChoiceCombo.addActionListener(axisChoiceComboListener);
    }

    /**
     * @param a0
     *            25 ao�t 2005
     */
    public void setTransformA0(double a0) {
        if (!Double.isNaN(a0)) {
            transformA0Text.setText(String.valueOf(a0));
        }
        else {
            transformA0Text.setText("");
        }
    }

    /**
     * @param a0
     *            25 ao�t 2005
     */
    public void setTransformA1(double a1) {
        if (!Double.isNaN(a1)) {
            transformA1Text.setText(String.valueOf(a1));
        }
        else {
            transformA1Text.setText("");
        }
    }

    /**
     * @param a0
     *            25 ao�t 2005
     */
    public void setTransformA2(double a2) {
        if (!Double.isNaN(a2)) {
            transformA2Text.setText(String.valueOf(a2));
        }
        else {
            transformA2Text.setText("");
        }
    }

    /**
     * @param fillColor
     *            25 ao�t 2005
     */
    public void setBarFillColor(Color _fillColor) {
        fillColorView.setBackground(_fillColor);
    }

    /**
     * @param fillingMethod
     *            25 ao�t 2005
     */
    public void setBarFillingMethod(int _fillingMethod) {
        fillMethodCombo.setSelectedIndex(_fillingMethod);
    }

    /**
     * @param fillStyle
     *            25 ao�t 2005
     */
    public void setBarFillStyle(int _fillStyle) {
        fillStyleCombo.setSelectedIndex(_fillStyle);
    }

    /**
     * @param width
     *            25 ao�t 2005
     */
    public void setBarWidth(int width) {
        barWidthSpinner.setValue(new Integer(width));
    }

    /**
     * @param color
     *            25 ao�t 2005
     */
    public void setCurveColor(Color _color) {
        lineColorView.setBackground(_color);
    }

    /**
     * @param lineStyle
     *            25 ao�t 2005
     */
    public void setCurveLineStyle(int _lineStyle) {
        lineDashCombo.setSelectedItem(LineStyle.getStyleFromValue(_lineStyle));
    }

    /**
     * @param width
     *            25 ao�t 2005
     */
    public void setCurveWidth(int _width) {
        lineWidthSpinner.setValue(new Integer(_width));
    }

    /**
     * @param color
     *            25 ao�t 2005
     */
    public void setMarkerColor(Color _color) {
        markerColorView.setBackground(_color);
    }

    /**
     * @param size
     *            25 ao�t 2005
     */
    public void setMarkerSize(int _size) {
        markerSizeSpinner.setValue(new Integer(_size));
    }

    /**
     * @param style
     *            25 ao�t 2005
     */
    public void setMarkerStyle(int _style) {
        markerStyleCombo.setSelectedIndex(_style);
    }

    public boolean isHidden() {
        return hiddenCheckBox.isSelected();
    }

    public void setHidden(boolean hidden) {
        hiddenCheckBox.setSelected(hidden);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lineColorBtn) {
            Color c = JColorChooser.showDialog(this, "Choose Line Color",
                    lineColorView.getBackground());
            if (c != null) {
                lineColorView.setBackground(c);
            }
        }
        else if (e.getSource() == applyColorToAllBtn) {
            applyLineColorToAll();
        }
        else if (e.getSource() == fillColorBtn) {
            Color c = JColorChooser.showDialog(this, "Choose Fill Color",
                    fillColorView.getBackground());
            if (c != null) {
                fillColorView.setBackground(c);
            }
        }
        else if (e.getSource() == markerColorBtn) {
            Color c = JColorChooser.showDialog(this, "Choose marker Color",
                    markerColorView.getBackground());
            if (c != null) {
                markerColorView.setBackground(c);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        hiddenCheckBox.setEnabled(enabled);

        viewTypeCombo.setEnabled(enabled);
        lineColorBtn.setEnabled(enabled);
        fillColorBtn.setEnabled(enabled);
        lineWidthSpinner.setEnabled(enabled);
        lineWidthSpinner.setFocusable(enabled);
        lineDashCombo.setEnabled(enabled);
        fillStyleCombo.setEnabled(enabled);

        barWidthSpinner.setEnabled(enabled);
        barWidthSpinner.setFocusable(enabled);
        fillMethodCombo.setEnabled(enabled);

        markerColorBtn.setEnabled(enabled);
        markerSizeSpinner.setEnabled(enabled);
        markerSizeSpinner.setFocusable(enabled);
        markerStyleCombo.setEnabled(enabled);
        labelVisibleCheck.setEnabled(enabled);

        transformA0Text.setEnabled(enabled);
        transformA1Text.setEnabled(enabled);
        transformA2Text.setEnabled(enabled);
    }

    public JComboBox getAxisChoiceCombo() {
        return axisChoiceCombo;
    }

    public DefaultComboBoxModel getDefaultComboBoxModel() {
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

    public static Color getColorFor(int index) {
        return defaultColor[index % defaultColor.length];
    }

    public static int getDefaultNextColorIndex(int index) {
        return (index + 5) % defaultColor.length;
    }

    public static int getColorIndexForSize(int size) {
        return size * 5;
    }

    private void setConstraints(int gridX, int gridY, int gridWidth,
            int gridHeight, double weightX, double weightY, int fill,
            int anchor, Insets insets) {
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.gridwidth = gridWidth;
        gbc.gridheight = gridHeight;
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        gbc.insets = insets;
        gbc.fill = fill;
        gbc.anchor = anchor;
    }
    
    private void adjustLabelsWidth() {
        Dimension refSize = new Dimension(refWidth,20);
        viewTypeLabel.setPreferredSize(refSize);
        lineWidthLabel.setPreferredSize(refSize);
        lineColorLabel.setPreferredSize(refSize);
        lineDashLabel.setPreferredSize(refSize);
        fillStyleLabel.setPreferredSize(refSize);
        barWidthLabel.setPreferredSize(refSize);
        fillMethodLabel.setPreferredSize(refSize);
        markerColorLabel.setPreferredSize(refSize);
        markerSizeLabel.setPreferredSize(refSize);
        markerStyleLabel.setPreferredSize(refSize);
    }
}
