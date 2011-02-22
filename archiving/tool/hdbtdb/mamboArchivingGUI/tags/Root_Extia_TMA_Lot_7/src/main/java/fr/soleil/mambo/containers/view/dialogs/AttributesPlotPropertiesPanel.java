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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import chart.temp.chart.JLDataViewOption;
import fr.soleil.comete.widget.properties.PlotProperties;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.actions.view.dialogs.listeners.FactorFieldListener;
import fr.soleil.mambo.actions.view.dialogs.listeners.PlotPropertyComboListener;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributeProperties;
import fr.soleil.mambo.data.view.plot.Bar;
import fr.soleil.mambo.data.view.plot.Curve;
import fr.soleil.mambo.data.view.plot.Interpolation;
import fr.soleil.mambo.data.view.plot.Marker;
import fr.soleil.mambo.data.view.plot.MathPlot;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.data.view.plot.Smoothing;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.tools.ColorGenerator;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class AttributesPlotPropertiesPanel extends JLDataViewOption {

    private static final long         serialVersionUID                      = -6195594433841922162L;

    private final static int          totalColors                           = 31;
    private final static Color[]      defaultColor                          = ColorGenerator
                                                                                    .generateColors(
                                                                                            totalColors,
                                                                                            0xFFFFFF);
    private int                       colorIndex;

    // balancing factor management
    private JTextField                factorField;
    private FactorFieldListener       factorFieldListener;
    // private JLabel factorLabel;

    // general objects
    private JPanel                    centerPanel;
    private JPanel                    topPanel;
    private JPanel                    generalPanel;
    private DefaultComboBoxModel      axisChoiceComboModel;

    // private JComboBox axisChoiceCombo;
    private PlotPropertyComboListener plotPropertyChoiceComboListener;
    private DefaultComboBoxModel      spectrumViewTypeComboModel;
    private JComboBox                 spectrumViewTypeCombo;
    private JCheckBox                 hiddenCheckBox;
    private GridBagConstraints        gbc;

    private ViewConfigurationBean     viewConfigurationBean;
    private VCEditDialog              editDialog;

    // Curve
    private static final int          DEFAULT_CURVE_WIDTH                   = 1;                      // 1
    private static final int          DEFAULT_CURVE_LINE_STYLE              = 0;                      // solid
    // bar
    private static final int          DEFAULT_BAR_FILL_STYLE                = 0;                      // no
    // marker
    private static final int          DEFAULT_MARKER_SIZE                   = 5;                      // 5
    private static final int          DEFAULT_MARKER_STYLE                  = 0;                      // None
    // transform
    private static final double       DEFAULT_TRANSFORM_A0                  = 0;
    private static final double       DEFAULT_TRANSFORM_A1                  = 1;
    private static final double       DEFAULT_TRANSFORM_A2                  = 0;

    // Interpolation
    private static final int          DEFAULT_INTERPOLATION_METHOD          = 0;
    private static final int          DEFAULT_INTERPOLATION_STEP            = 10;
    private static final double       DEFAULT_INTERPOLATION_HERMITE_BIAS    = 0;
    private static final double       DEFAULT_INTERPOLATION_HERMITE_TENSION = 0;

    // Smoothing
    private static final int          DEFAULT_SMOOTHING_METHOD              = 0;
    private static final int          DEFAULT_SMOOTHING_NEIGHBORS           = 3;
    private static final double       DEFAULT_SMOOTHING_GAUSS_SIGMA         = 0.5;
    private static final int          DEFAULT_SMOOTHING_EXTRAPOLATION       = 2;

    // Math
    private static final int          DEFAULT_MATH_FUNCTION                 = 0;

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

    private void addComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void initComponents() {
        // This one became the upper pannel and the centerpanel became the lower
        // pannel
        super.initComponents();
        gbc = new GridBagConstraints();
        initTopPanel();
        initTabPane();
        initCenterPanel();
    }

    private void initTopPanel() {
        topPanel = new JPanel(new GridBagLayout());

        plotPropertyChoiceComboListener = new PlotPropertyComboListener(
                viewConfigurationBean, editDialog);

        JPanel axisPanel = initAxisChoicePanel();
        GridBagConstraints axisConstraints = new GridBagConstraints();
        axisConstraints.fill = GridBagConstraints.BOTH;
        axisConstraints.gridx = 0;
        axisConstraints.gridy = 0;
        axisConstraints.weightx = 0.5;
        axisConstraints.weighty = 0;
        axisConstraints.anchor = GridBagConstraints.NORTH;
        topPanel.add(axisPanel, axisConstraints);

        JPanel balFactorPanel = initFactorPanel();
        GridBagConstraints balFactorConstraints = new GridBagConstraints();
        balFactorConstraints.fill = GridBagConstraints.BOTH;
        balFactorConstraints.gridx = 1;
        balFactorConstraints.gridy = 0;
        balFactorConstraints.weightx = 0.5;
        balFactorConstraints.weighty = 0;
        balFactorConstraints.anchor = GridBagConstraints.NORTH;
        topPanel.add(balFactorPanel, balFactorConstraints);

        JPanel spectrumViewTypePanel = initSpectrumViewTypePanel();
        GridBagConstraints spectrumViewTypeConstraints = new GridBagConstraints();
        spectrumViewTypeConstraints.fill = GridBagConstraints.BOTH;
        spectrumViewTypeConstraints.gridx = 0;
        spectrumViewTypeConstraints.gridy = 1;
        spectrumViewTypeConstraints.weightx = 1;
        spectrumViewTypeConstraints.weighty = 0;
        spectrumViewTypeConstraints.gridwidth = GridBagConstraints.REMAINDER;
        spectrumViewTypeConstraints.anchor = GridBagConstraints.SOUTH;
        topPanel.add(spectrumViewTypePanel, spectrumViewTypeConstraints);
    }

    private JPanel initSpectrumViewTypePanel() {
        JPanel viewTypePanel = new JPanel();
        String msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_CHART_VIEW_TYPE");
        TitledBorder viewTypePanelBorder = GUIUtilities
                .getPlotSubPanelsLineBorder(msg, new Color(100, 100, 100));
        CompoundBorder cb = BorderFactory.createCompoundBorder(
                viewTypePanelBorder, BorderFactory
                        .createEmptyBorder(2, 4, 4, 4));
        viewTypePanel.setBorder(cb);
        String[] msgListItem = new String[3];
        msgListItem[0] = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_CHART_VIEW_TYPE_INDICE");
        msgListItem[1] = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_CHART_VIEW_TYPE_TIME");
        msgListItem[2] = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_CHART_VIEW_TYPE_TIME_STACK");
        spectrumViewTypeComboModel = new DefaultComboBoxModel(msgListItem);
        spectrumViewTypeCombo = new JComboBox(spectrumViewTypeComboModel);
        spectrumViewTypeCombo
                .addActionListener(plotPropertyChoiceComboListener);
        viewTypePanel.setLayout(new GridBagLayout());
        setConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        viewTypePanel.add(spectrumViewTypeCombo, gbc);
        setSpectrumViewTypeChoice(Options.getInstance().getVcOptions()
                .getSpectrumViewType());
        return viewTypePanel;
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
        super.axisBox = new JComboBox(axisChoiceComboModel);
        super.axisBox.setFont(GUIUtilities.labelFont);
        super.axisBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        super.axisBox.addActionListener(plotPropertyChoiceComboListener);
        axisChoicePanel.setLayout(new GridBagLayout());
        setConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        axisChoicePanel.add(super.axisBox, gbc);
        return axisChoicePanel;
    }

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

        this.getCloseButton().setVisible(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(hiddenCheckBox);
        centerPanel.add(this.tabPane);
        hiddenCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        this.tabPane.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void initTabPane() {
        initGeneralPanel();
        this.tabPane.remove(linePanel);
        this.tabPane.remove(barPanel);
        this.tabPane.remove(markerPanel);
        this.tabPane.remove(transformPanel);
        this.tabPane.add(generalPanel, 0);
        this.tabPane.setTitleAt(0, "General");
        this.tabPane.setSelectedIndex(0);
    }

    private void initGeneralPanel() {
        generalPanel = new JPanel();
        String msg;
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_TITLE");
        TitledBorder curvePanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder(msg);
        linePanel.setBorder(curvePanelBorder);

        TitledBorder barPanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder("Bar");
        barPanel.setBorder(barPanelBorder);
        TitledBorder markerPanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder("Marker");
        markerPanel.setBorder(markerPanelBorder);
        TitledBorder transformPanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder("Transform");
        transformPanel.setBorder(transformPanelBorder);
        generalPanel.setLayout(new GridBagLayout());
        linePanel.setPreferredSize(new Dimension(150, 230));
        GridBagConstraints linePanelConstraints = new GridBagConstraints();
        linePanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        linePanelConstraints.gridx = 0;
        linePanelConstraints.gridy = 0;
        linePanelConstraints.weightx = 1;
        linePanelConstraints.weighty = 1;
        generalPanel.add(linePanel, linePanelConstraints);

        barPanel.setPreferredSize(new Dimension(150, 80));
        GridBagConstraints barPanelConstraints = new GridBagConstraints();
        barPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        barPanelConstraints.gridx = 0;
        barPanelConstraints.gridy = 1;
        barPanelConstraints.weightx = 1;
        barPanelConstraints.weighty = 1;
        generalPanel.add(barPanel, barPanelConstraints);

        markerPanel.setPreferredSize(new Dimension(150, 140));
        GridBagConstraints markerPanelConstraints = new GridBagConstraints();
        markerPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        markerPanelConstraints.gridx = 0;
        markerPanelConstraints.gridy = 2;
        markerPanelConstraints.weightx = 1;
        markerPanelConstraints.weighty = 1;
        generalPanel.add(markerPanel, markerPanelConstraints);

        transformPanel.setPreferredSize(new Dimension(150, 170));
        GridBagConstraints transformPanelConstraints = new GridBagConstraints();
        transformPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        transformPanelConstraints.gridx = 0;
        transformPanelConstraints.gridy = 3;
        transformPanelConstraints.weightx = 1;
        transformPanelConstraints.weighty = 1;
        generalPanel.add(transformPanel, transformPanelConstraints);
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
        factorPanel.setLayout(new GridBagLayout());
        setConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        factorPanel.add(factorField, gbc);
        return factorPanel;
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

    public void setSpectrumViewTypeChoice(int spectrumViewTypeChoice) {
        spectrumViewTypeCombo
                .removeActionListener(plotPropertyChoiceComboListener);
        spectrumViewTypeCombo.setSelectedIndex(spectrumViewTypeChoice);
        spectrumViewTypeCombo
                .addActionListener(plotPropertyChoiceComboListener);
    }

    public static int getColorIndexForSize(int size) {
        return size * 5;
    }

    public void setColorIndex(int index) {
        colorIndex = index % defaultColor.length;
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
        PlotProperties plotProperties = super.getPlotProperties();
        plotProperties.getCurve().setColor(
                CometeColor.getColor(defaultColor[colorIndex]));
        plotProperties.getBar().setFillColor(
                CometeColor.getColor(defaultColor[colorIndex]));
        plotProperties.getMarker().setColor(
                CometeColor.getColor(defaultColor[colorIndex]));
        setPlotProperties(plotProperties);
        applyProperties();
    }

    public static Color getColorFor(int index) {
        return defaultColor[index % defaultColor.length];
    }

    public static int getDefaultNextColorIndex(int index) {
        return (index + 5) % defaultColor.length;
    }

    public int getAxisChoice() {
        if ("---".equals(super.axisBox.getItemAt(0))) {
            return super.axisBox.getSelectedIndex() - 1;
        }
        return super.axisBox.getSelectedIndex();
    }

    public int getSpectrumViewTypeChoice() {
        if ("---".equals(spectrumViewTypeCombo.getItemAt(0))) {
            return spectrumViewTypeCombo.getSelectedIndex() - 1;
        }
        return spectrumViewTypeCombo.getSelectedIndex();
    }

    public String getAxisChoiceItem() {
        return (String) super.axisBox.getSelectedItem();
    }

    public double getFactor() {
        double factor;
        try {
            factor = Double.parseDouble(factorField.getText());
            if (factor == Double.NaN || factor == Double.POSITIVE_INFINITY
                    || factor == Double.NEGATIVE_INFINITY) {
                factor = 1;
            }
        } catch (Exception e) {
            factor = 1;
        }
        return factor;
    }

    public void setFactor(double factor) {
        factorField.setText(factor + "");
    }

    public JComboBox getSpectrumViewTypeCombo() {
        return spectrumViewTypeCombo;
    }

    public DefaultComboBoxModel getSpectrumViewTypeComboModel() {
        return spectrumViewTypeComboModel;
    }

    public DefaultComboBoxModel getAxisChoiceComboModel() {
        return axisChoiceComboModel;
    }

    public PlotPropertyComboListener getPlotPropertyChoiceComboListener() {
        return plotPropertyChoiceComboListener;
    }

    public JTextField getFactorField() {
        return factorField;
    }

    public PlotProperties getPlotProperties() {
        return super.getPlotProperties();
    }

    public boolean isHidden() {
        return hiddenCheckBox.isSelected();
    }

    public JComboBox getAxisChoiceCombo() {
        return super.axisBox;
    }

    public void setViewConfigurationAttributePlotProperties(
            ViewConfigurationAttributePlotProperties plotProperties) {
        super.setPlotProperties(plotProperties);
        hiddenCheckBox.setSelected(plotProperties.isHidden());
        spectrumViewTypeCombo.setSelectedIndex(plotProperties
                .getSpectrumViewType());
        super.axisBox.setSelectedIndex(plotProperties.getAxisChoice());
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.barWidthSpinner.setEnabled(enabled);
        this.biasText.setEnabled(enabled);
        this.cosineBtn.setEnabled(enabled);
        this.cubicBtn.setEnabled(enabled);
        this.derivativeBtn.setEnabled(enabled);
        this.fftModBtn.setEnabled(enabled);
        this.fftPhaseBtn.setEnabled(enabled);
        this.fillColorBtn.setEnabled(enabled);
        this.fillMethodCombo.setEnabled(enabled);
        this.fillStyleCombo.setEnabled(enabled);
        this.flatExtBtn.setEnabled(enabled);
        this.flatSmoothBtn.setEnabled(enabled);
        this.gaussianSmoothBtn.setEnabled(enabled);
        this.hermiteBtn.setEnabled(enabled);
        this.integralBtn.setEnabled(enabled);
        this.labelVisibleCheck.setEnabled(enabled);
        this.linearBtn.setEnabled(enabled);
        this.linearExtBtn.setEnabled(enabled);
        this.lineColorBtn.setEnabled(enabled);
        this.lineDashCombo.setEnabled(enabled);
        this.lineNameText.setEnabled(enabled);
        this.lineWidthSpinner.setEnabled(enabled);
        this.markerColorBtn.setEnabled(enabled);
        this.markerSizeSpinner.setEnabled(enabled);
        this.markerStyleCombo.setEnabled(enabled);
        this.neighborSpinner.setEnabled(enabled);
        this.noExtBtn.setEnabled(enabled);
        this.noInterpBtn.setEnabled(enabled);
        this.noMathBtn.setEnabled(enabled);
        this.noSmoothBtn.setEnabled(enabled);
        this.sigmaText.setEnabled(enabled);
        this.stepSpinner.setEnabled(enabled);
        this.tensionText.setEnabled(enabled);
        this.transformA0Text.setEnabled(enabled);
        this.transformA1Text.setEnabled(enabled);
        this.transformA2Text.setEnabled(enabled);
        this.transformHelpLabel.setEnabled(enabled);
        this.triangularSmoothBtn.setEnabled(enabled);
        this.viewTypeCombo.setEnabled(enabled);
        this.setAllButton.setEnabled(enabled);
        hiddenCheckBox.setEnabled(enabled);
    }

    public static Bar getDefaultBar(Color color) {
        return new Bar(CometeColor.getColor(color), 1, DEFAULT_BAR_FILL_STYLE,
                0);
    }

    public static Curve getDefaultCurve(Color color, String name) {
        return new Curve(CometeColor.getColor(color), DEFAULT_CURVE_WIDTH,
                DEFAULT_CURVE_LINE_STYLE, name);
    }

    public static Marker getDefaultMarker(Color color) {
        return new Marker(CometeColor.getColor(color), DEFAULT_MARKER_SIZE,
                DEFAULT_MARKER_STYLE, true);
    }

    public static Polynomial2OrderTransform getDefaultTransform() {
        return new Polynomial2OrderTransform(DEFAULT_TRANSFORM_A0,
                DEFAULT_TRANSFORM_A1, DEFAULT_TRANSFORM_A2);
    }

    public static Interpolation getDefaultInterpolation() {
        return new Interpolation(DEFAULT_INTERPOLATION_METHOD,
                DEFAULT_INTERPOLATION_STEP, DEFAULT_INTERPOLATION_HERMITE_BIAS,
                DEFAULT_INTERPOLATION_HERMITE_TENSION);
    }

    public static Smoothing getDefaultSmoothing() {
        return new Smoothing(DEFAULT_SMOOTHING_METHOD,
                DEFAULT_SMOOTHING_NEIGHBORS, DEFAULT_SMOOTHING_GAUSS_SIGMA,
                DEFAULT_SMOOTHING_EXTRAPOLATION);
    }

    public static MathPlot getDefaultMath() {
        return new MathPlot(DEFAULT_MATH_FUNCTION);
    }
}
