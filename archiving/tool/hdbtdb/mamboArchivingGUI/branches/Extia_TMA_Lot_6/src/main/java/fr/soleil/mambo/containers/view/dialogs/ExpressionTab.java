package fr.soleil.mambo.containers.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import chart.temp.chart.expression.ExpressionParserOption;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.comete.widget.properties.PlotProperties;
import fr.soleil.comete.widget.util.CometeColor;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCExpressionAddAction;
import fr.soleil.mambo.actions.view.VCExpressionApplyAction;
import fr.soleil.mambo.actions.view.VCExpressionDeleteAction;
import fr.soleil.mambo.actions.view.dialogs.listeners.FactorFieldListener;
import fr.soleil.mambo.actions.view.dialogs.listeners.PlotPropertyComboListener;
import fr.soleil.mambo.actions.view.listeners.ExpressionNameFieldListener;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.containers.sub.dialogs.MamboErrorDialog;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.ViewConfigurationAttributes;
import fr.soleil.mambo.tools.ColorGenerator;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class ExpressionTab extends ExpressionParserOption {

    private static final long             serialVersionUID = 3056661458398960798L;
    private final static int              totalColors      = 31;
    private final static Color[]          defaultColor     = ColorGenerator
                                                                   .generateColors(
                                                                           totalColors,
                                                                           0xFFFFFF);
    private int                           colorIndex;
    protected final static ImageIcon      addIcon          = new ImageIcon(
                                                                   Mambo.class
                                                                           .getResource("icons/add.gif"));
    protected final static ImageIcon      deleteIcon       = new ImageIcon(
                                                                   Mambo.class
                                                                           .getResource("icons/delete.gif"));
    protected final static ImageIcon      applyIcon        = new ImageIcon(
                                                                   Mambo.class
                                                                           .getResource("icons/apply.gif"));
    protected final static Dimension      scrollPaneDim    = new Dimension(200,
                                                                   200);

    private ExpressionAttribute           currentAttribute;
    protected ViewConfigurationBean       viewConfigurationBean;
    private VCEditDialog                  editDialog;
    protected JScrollPane                 treeScrollPane;
    private PlotPropertyComboListener     plotPropertyChoiceComboListener;

    protected ExpressionTree              expressionTree;

    protected JPanel                      attributesPanel;
    protected JPanel                      configurationAttributePanel;
    protected JPanel                      expressionPanel;
    protected ExpressionNameFieldListener expressionNameFieldListener;
    protected JPanel                      propertiesPanel;
    protected JPanel                      generalPropertiesPanel;
    protected JPanel                      actionPanel;

    // protected JTextField nameField;
    protected JLabel                      nameLabel;

    // Action components
    protected JButton                     addButton;
    protected JButton                     applyButton;
    protected JButton                     deleteButton;

    // General option
    private DefaultComboBoxModel          axisChoiceComboModel;

    private JCheckBox                     hiddenCheckBox;

    private GridBagConstraints            gbc;

    // balancing factor management
    private JTextField                    factorField;
    private FactorFieldListener           factorFieldListener;

    protected VCExpressionApplyAction     applyAction;

    protected MamboErrorDialog            errorDialog;

    protected JLabel                      variableName;

    public ExpressionTab(ViewConfigurationBean viewConfigurationBean,
            VCEditDialog editDialog) {
        super();
        this.editDialog = editDialog;
        this.viewConfigurationBean = viewConfigurationBean;
        this.setAlwaysCommit(false);
        super.initComponents();
        initComponents();
        initLayout();
        addComponents();

        setEnabledPropertiesPanel(false);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        this.removeAll();
        initGeneralPropertiesPanel();
        initTabblePane();

        configurationAttributePanel = new JPanel();

        attributesPanel = new JPanel();
        plotPropertyChoiceComboListener = new PlotPropertyComboListener(
                viewConfigurationBean, editDialog);
        // init expressionPanel
        expressionPanel = new JPanel();
        expressionLabel.setFont(GUIUtilities.labelFont);
        expressionLabel.setForeground(GUIUtilities.fColor);
        expressionNameFieldListener = new ExpressionNameFieldListener(this);
        expressionField.getDocument().addDocumentListener(
                new ExpressionNameFieldListener(this));
        expressionField.setToolTipText(Messages
                .getMessage("EXPRESSION_ATTRIBUTE_HELP"));
        // end init expressionPanel

        // init propertiesPanel
        propertiesPanel = new JPanel();
        String msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_GRAPHICAL_TITLE");
        TitledBorder centerPanelBorder = GUIUtilities
                .getPlotSubPanelsLineBorder(msg, Color.BLUE);
        CompoundBorder cb = BorderFactory.createCompoundBorder(
                centerPanelBorder, BorderFactory.createEmptyBorder(0, 4, 0, 4));
        propertiesPanel.setBorder(cb);
        propertiesPanel.setLayout(new BoxLayout(propertiesPanel,
                BoxLayout.Y_AXIS));
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PROPERTIES_HIDDEN");
        hiddenCheckBox = new JCheckBox(msg);
        hiddenCheckBox.setSelected(false);
        // end int propertiesPanel

        // init detailPanel
        nameLabel = new JLabel(Messages.getMessage("EXPRESSION_ATTRIBUTE_NAME"));
        nameLabel.setFont(GUIUtilities.labelFont);
        nameLabel.setForeground(GUIUtilities.fColor);
        lineNameText.setToolTipText(Messages
                .getMessage("EXPRESSION_ATTRIBUTE_HELP"));

        // init actionPanel
        actionPanel = new JPanel();
        addButton = new JButton(new VCExpressionAddAction(this));
        addButton.setIcon(addIcon);
        applyAction = new VCExpressionApplyAction(this);
        applyButton = new JButton(applyAction);
        applyButton.setIcon(applyIcon);
        deleteButton = new JButton(new VCExpressionDeleteAction(this));
        deleteButton.setIcon(deleteIcon);
        // end init actionPanel

        // init treePanel
        expressionTree = new ExpressionTree(this);
        treeScrollPane = new JScrollPane(expressionTree);
        treeScrollPane.setBackground(Color.WHITE);
        treeScrollPane.getViewport().setBackground(Color.WHITE);
        treeScrollPane.setPreferredSize(new Dimension(100, 300));
        // end init treePanel
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
        super.axisBox.removeActionListener(this);
        super.axisBox.addActionListener(plotPropertyChoiceComboListener);
        axisChoicePanel.setLayout(new GridBagLayout());
        setConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));
        axisChoicePanel.add(super.axisBox, gbc);
        return axisChoicePanel;
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
        factorFieldListener = new FactorFieldListener(viewConfigurationBean,
                editDialog);
        factorField.addActionListener(factorFieldListener);
        factorField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        factorField.setToolTipText("Press Enter");
        factorPanel.setLayout(new GridBagLayout());

        setConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER, new Insets(0, 0, 0, 0));

        factorPanel.add(factorField, gbc);
        return factorPanel;
    }

    private void initGeneralPropertiesPanel() {
        generalPropertiesPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();

        JPanel axisPanel = initAxisChoicePanel();
        GridBagConstraints axisConstraints = new GridBagConstraints();
        axisConstraints.fill = GridBagConstraints.BOTH;
        axisConstraints.gridx = 0;
        axisConstraints.gridy = 0;
        axisConstraints.weightx = 0.5;
        axisConstraints.weighty = 0;
        axisConstraints.anchor = GridBagConstraints.NORTH;
        generalPropertiesPanel.add(axisPanel, axisConstraints);

        JPanel balFactorPanel = initFactorPanel();
        GridBagConstraints balFactorConstraints = new GridBagConstraints();
        balFactorConstraints.fill = GridBagConstraints.BOTH;
        balFactorConstraints.gridx = 1;
        balFactorConstraints.gridy = 0;
        balFactorConstraints.weightx = 0.5;
        balFactorConstraints.weighty = 0;
        balFactorConstraints.anchor = GridBagConstraints.NORTH;
        generalPropertiesPanel.add(balFactorPanel, balFactorConstraints);
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

    protected void addComponents() {
        propertiesPanel.add(hiddenCheckBox);
        propertiesPanel.add(tabPane);
        hiddenCheckBox.setAlignmentX(LEFT_ALIGNMENT);
        tabPane.setAlignmentX(LEFT_ALIGNMENT);

        configurationAttributePanel.setLayout(new BorderLayout());
        configurationAttributePanel.add(generalPropertiesPanel,
                BorderLayout.NORTH);
        configurationAttributePanel.add(propertiesPanel, BorderLayout.CENTER);

        // add detailPanel
        GridBagConstraints nameLabelConstraints = new GridBagConstraints();
        nameLabelConstraints.fill = GridBagConstraints.BOTH;
        nameLabelConstraints.gridx = 0;
        nameLabelConstraints.gridy = 0;
        nameLabelConstraints.weightx = 1;
        nameLabelConstraints.weighty = 1;
        nameLabelConstraints.insets = new Insets(5, 5, 0, 0);
        expressionPanel.add(nameLabel, nameLabelConstraints);
        GridBagConstraints lineNameTextConstraints = new GridBagConstraints();
        lineNameTextConstraints.fill = GridBagConstraints.BOTH;
        lineNameTextConstraints.gridx = 1;
        lineNameTextConstraints.gridy = 0;
        lineNameTextConstraints.weightx = 1;
        lineNameTextConstraints.weighty = 1;
        lineNameTextConstraints.insets = new Insets(5, 5, 0, 0);
        expressionPanel.add(lineNameText, lineNameTextConstraints);
        // end add detailPanel

        // add expressionPanel
        GridBagConstraints expressionLabelConstraints = new GridBagConstraints();
        expressionLabelConstraints.fill = GridBagConstraints.BOTH;
        expressionLabelConstraints.gridx = 0;
        expressionLabelConstraints.gridy = 1;
        expressionLabelConstraints.weightx = 1;
        expressionLabelConstraints.weighty = 1;
        expressionLabelConstraints.insets = new Insets(5, 5, 0, 0);
        expressionPanel.add(expressionLabel, expressionLabelConstraints);
        GridBagConstraints expressionFieldConstraints = new GridBagConstraints();
        expressionFieldConstraints.fill = GridBagConstraints.BOTH;
        expressionFieldConstraints.gridx = 1;
        expressionFieldConstraints.gridy = 1;
        expressionFieldConstraints.weightx = 1;
        expressionFieldConstraints.weighty = 1;
        expressionFieldConstraints.insets = new Insets(5, 5, 0, 0);
        expressionPanel.add(expressionField, expressionFieldConstraints);

        GridBagConstraints helpButtonConstraints = new GridBagConstraints();
        helpButtonConstraints.fill = GridBagConstraints.NONE;
        helpButtonConstraints.gridx = 0;
        helpButtonConstraints.gridy = 2;
        helpButtonConstraints.weightx = 1;
        helpButtonConstraints.weighty = 1;
        helpButtonConstraints.insets = new Insets(5, 5, 5, 5);
        expressionPanel.add(helpButton, helpButtonConstraints);
        GridBagConstraints generateButtonConstraints = new GridBagConstraints();
        generateButtonConstraints.fill = GridBagConstraints.NONE;
        generateButtonConstraints.gridx = 1;
        generateButtonConstraints.gridy = 2;
        generateButtonConstraints.weightx = 1;
        generateButtonConstraints.weighty = 1;
        generateButtonConstraints.insets = new Insets(5, 5, 5, 5);
        expressionPanel.add(generateButton, generateButtonConstraints);

        GridBagConstraints expressionPanelConstraints = new GridBagConstraints();
        expressionPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        expressionPanelConstraints.gridx = 0;
        expressionPanelConstraints.gridy = 1;
        expressionPanelConstraints.weightx = 1;
        expressionPanelConstraints.weighty = 1;
        attributesPanel.add(expressionPanel, expressionPanelConstraints);
        // end add expressionPanel

        // add variableScrollPane
        GridBagConstraints variableScollPaneConstraints = new GridBagConstraints();
        variableScollPaneConstraints.fill = GridBagConstraints.HORIZONTAL;
        variableScollPaneConstraints.gridx = 0;
        variableScollPaneConstraints.gridy = 2;
        variableScollPaneConstraints.weightx = 1;
        variableScollPaneConstraints.weighty = 1;
        attributesPanel.add(this.variableScrollPane,
                variableScollPaneConstraints);

        // add actionPanel
        actionPanel.add(addButton);
        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(applyButton);
        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(deleteButton);
        GridBagConstraints actionPanelConstraints = new GridBagConstraints();
        actionPanelConstraints.fill = GridBagConstraints.NONE;
        actionPanelConstraints.gridx = 0;
        actionPanelConstraints.gridy = 3;
        actionPanelConstraints.weightx = 1;
        actionPanelConstraints.weighty = 1;
        attributesPanel.add(actionPanel, actionPanelConstraints);
        // end add actionPanel

        // add treePanel
        GridBagConstraints treeScrollPaneConstraints = new GridBagConstraints();
        treeScrollPaneConstraints.fill = GridBagConstraints.HORIZONTAL;
        treeScrollPaneConstraints.gridx = 0;
        treeScrollPaneConstraints.gridy = 4;
        treeScrollPaneConstraints.weightx = 1;
        treeScrollPaneConstraints.weighty = 2;
        attributesPanel.add(treeScrollPane, treeScrollPaneConstraints);
        // end add treePanel

        this.add(attributesPanel);
        this.add(configurationAttributePanel);

    }

    private void initTabblePane() {
        this.tabPane.remove(linePanel);
        this.tabPane.remove(barPanel);
        this.tabPane.remove(markerPanel);
        this.tabPane.remove(transformPanel);
        this.tabPane.add(initGeneralPanel(), 0);
        this.tabPane.setTitleAt(0, "General");
        this.tabPane.setSelectedIndex(0);
    }

    private JPanel initGeneralPanel() {
        JPanel generalPanel = new JPanel();

        String msg;
        msg = Messages
                .getMessage("DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_CURVE_TITLE");
        TitledBorder curvePanelBorder = GUIUtilities
                .getPlotSubPanelsEtchedBorder(msg);
        linePanel.setBorder(curvePanelBorder);
        linePanel.remove(lineNameText);
        linePanel.remove(lineNameLabel);
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
        linePanel.setPreferredSize(new Dimension(150, 200));
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

        return generalPanel;
    }

    protected void initLayout() {

        // init expressionPanel layout

        expressionPanel.setLayout(new GridBagLayout());

        attributesPanel.setLayout(new GridBagLayout());

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    public void setEnabledPropertiesPanel(boolean enabled) {
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
        hiddenCheckBox.setEnabled(enabled);
        if (!enabled) {
            variablePanel.removeAll();
            variablePanel.revalidate();
            variablePanel.repaint();
            expressionField.setText("");
            lineNameText.setText("");
        }
    }

    public void addExpression() {
        String expression = expressionField.getText().trim();
        if (lineNameText.getText() == null
                || "".equals(lineNameText.getText().trim())) {
            displayErrorDialog(Messages
                    .getMessage("EXPRESSION_ATTRIBUTE_NAME_ERROR"), Messages
                    .getMessage("EXPRESSION_ERROR_TITLE"));
        } else if (expression == null || expression.equals("")
                || expression.isEmpty()) {
            displayErrorDialog(Messages
                    .getMessage("EXPRESSION_EXPRESSION_ERROR"), Messages
                    .getMessage("EXPRESSION_ERROR_TITLE"));
        } else {
            String[] variables = getVariables();
            if (variables == null || variables.length == 0) {
                displayErrorDialog(Messages
                        .getMessage("EXPRESSION_VARIABLE_ERROR"), Messages
                        .getMessage("EXPRESSION_ERROR_TITLE"));
            } else {
                variablePanel.removeAll();
                PlotProperties plotProperties = new PlotProperties();
                setColorIndex(expressionTree.getModel()
                        .getExpressionAttributes().size() * 5);
                int indexColor = rotateCurveColor();
                plotProperties.getCurve().setColor(
                        CometeColor.getColor(defaultColor[indexColor]));
                plotProperties.getBar().setFillColor(
                        CometeColor.getColor(defaultColor[indexColor]));
                plotProperties.getMarker().setColor(
                        CometeColor.getColor(defaultColor[indexColor]));
                plotProperties.getCurve()
                        .setName(lineNameText.getText().trim());
                ViewConfigurationAttributePlotProperties configurationPlotProperties = new ViewConfigurationAttributePlotProperties(
                        0, 0, plotProperties, 0, false);

                currentAttribute = new ExpressionAttribute(lineNameText
                        .getText().trim(), expression, this.getVariables(),
                        this.isX());
                currentAttribute.setProperties(configurationPlotProperties);
                configurationPlotProperties = null;
                variablePanel.removeAll();
                variablePanel.revalidate();
                variablePanel.repaint();
                expressionField.setText("");
                lineNameText.setText("");
                if (!expressionTree.getModel().addAttribute(currentAttribute)) {
                    displayErrorDialog(
                            Messages
                                    .getMessage("EXPRESSION_ATTRIBUTE_NAME_PRESENT_ERROR"),
                            Messages.getMessage("EXPRESSION_ERROR_TITLE"));
                }
            }
        }
    }

    private void displayErrorDialog(String message, String title) {
        errorDialog = new MamboErrorDialog(editDialog, title, message);
        int width = errorDialog.getWidth();
        int height = errorDialog.getHeight();
        int x = editDialog.getX() + 50;
        int y = editDialog.getY() + 50;
        errorDialog.setBounds(x, y, width, height);
        errorDialog.setVisible(true);
        errorDialog = null;
    }

    public void apply() {
        ExpressionAttribute attr = expressionTree.getSelectedAttribute();
        applyAction.setEnabled(false);
        if (attr != null) {
            String name = attr.getName();
            if (lineNameText.getText() == null
                    || "".equals(lineNameText.getText().trim())) {
                displayErrorDialog(Messages
                        .getMessage("EXPRESSION_ATTRIBUTE_NAME_ERROR"),
                        Messages.getMessage("EXPRESSION_ERROR_TITLE"));
            } else if (expressionField.getText() == null
                    || "".equals(expressionField.getText().trim())) {
                displayErrorDialog(Messages
                        .getMessage("EXPRESSION_EXPRESSION_ERROR"), Messages
                        .getMessage("EXPRESSION_ERROR_TITLE"));
            } else {
                String[] variables = getVariables();
                if (variables == null || variables.length == 0) {
                    displayErrorDialog(Messages
                            .getMessage("EXPRESSION_VARIABLE_ERROR"), Messages
                            .getMessage("EXPRESSION_ERROR_TITLE"));
                } else {
                    variablePanel.removeAll();
                    currentAttribute = attr.duplicate();
                    // currentAttribute.setName(lineNameText.getText().trim());
                    currentAttribute.setExpression(expressionField.getText()
                            .trim());
                    currentAttribute.setVariables(this.getVariables());
                    currentAttribute.setX(this.isX());
                    if (!expressionTree.getModel().updateAttribute(name,
                            currentAttribute.duplicate())) {
                        displayErrorDialog(
                                Messages
                                        .getMessage("EXPRESSION_ATTRIBUTE_NAME_PRESENT_ERROR"),
                                Messages.getMessage("EXPRESSION_ERROR_TITLE"));
                    }
                }
                name = null;
            }
            attr = null;
        } else {
            displayErrorDialog(Messages
                    .getMessage("EXPRESSION_SELECTION_ERROR"), Messages
                    .getMessage("EXPRESSION_ERROR_TITLE"));
        }
    }

    public void delete() {
        Vector<ExpressionAttribute> selectedAttributes = expressionTree
                .getSelectedAttributes();
        if (selectedAttributes == null) {
            displayErrorDialog(Messages
                    .getMessage("EXPRESSION_SELECTION_ERROR"), Messages
                    .getMessage("EXPRESSION_ERROR_TITLE"));
            return;
        }
        String[] names = new String[selectedAttributes.size()];
        for (int i = 0; i < selectedAttributes.size(); i++) {
            names[i] = (selectedAttributes.get(i)).getName();
        }
        selectedAttributes = null;
        for (int i = 0; i < names.length; i++) {
            expressionTree.getModel().removeAttribute(names[i]);
        }
    }

    public void setParameters(ExpressionAttribute attr) {
        if (attr == null)
            return;
        currentAttribute = attr.duplicate();
        reloadDetailPanel();
        reloadPropertiesPanel();
    }

    protected void reloadDetailPanel() {

        lineNameText.setText(currentAttribute.getProperties().getCurve()
                .getName());
        expressionField.setText(currentAttribute.getExpression());

        this.factorField.setText(Double.toString(currentAttribute.getFactor()));

        variablePanel.removeAll();
        String[][] attrNamesList = getAttributesNamesList();
        if (attrNamesList != null) {
            this.setExpression(currentAttribute.getExpression(),
                    attrNamesList[0], attrNamesList[1], currentAttribute
                            .getVariables());
        }
        applyAction.setEnabled(false);
        addButton.setEnabled(false);
    }

    protected void reloadPropertiesPanel() {
        ViewConfigurationAttributePlotProperties properties = currentAttribute
                .getProperties();
        this.setPlotProperties(properties);
        hiddenCheckBox.setSelected(properties.isHidden());
        properties = null;
    }

    protected String[][] getAttributesNamesList() {
        ViewConfigurationAttributes attributes = null;
        if ((viewConfigurationBean != null)
                && (viewConfigurationBean.getEditingViewConfiguration() != null)) {
            attributes = viewConfigurationBean.getEditingViewConfiguration()
                    .getAttributes();
        }
        if (attributes != null) {
            TreeMap<String, ViewConfigurationAttribute> attributesList = attributes
                    .getAttributes();
            if (attributesList != null) {
                Set<String> keySet = attributesList.keySet();
                Iterator<String> keyIterator = keySet.iterator();
                boolean historic = viewConfigurationBean
                        .getEditingViewConfiguration().getData().isHistoric();
                ArrayList<String> dataViewsName = new ArrayList<String>();
                ArrayList<String> dataViewsId = new ArrayList<String>();
                while (keyIterator.hasNext()) {
                    ViewConfigurationAttribute attr = attributesList
                            .get(keyIterator.next());
                    if (attr.isScalar(historic)) {
                        int dataType = attr.getDataType(historic);
                        switch (dataType) {
                            case TangoConst.Tango_DEV_CHAR:
                            case TangoConst.Tango_DEV_UCHAR:
                            case TangoConst.Tango_DEV_SHORT:
                            case TangoConst.Tango_DEV_USHORT:
                            case TangoConst.Tango_DEV_LONG:
                            case TangoConst.Tango_DEV_ULONG:
                            case TangoConst.Tango_DEV_FLOAT:
                            case TangoConst.Tango_DEV_DOUBLE:
                                int writable = attr.getDataWritable(historic);
                                String readKey = "/"
                                        + ViewConfigurationAttribute.READ_DATA_VIEW_KEY;
                                String writeKey = "/"
                                        + ViewConfigurationAttribute.WRITE_DATA_VIEW_KEY;
                                switch (writable) {
                                    case AttrWriteType._READ:
                                        dataViewsName.add(attr.getProperties()
                                                .getPlotProperties().getCurve()
                                                .getName()
                                                + readKey);
                                        dataViewsId.add(attr.getCompleteName()
                                                + readKey);
                                        break;
                                    case AttrWriteType._WRITE:
                                        dataViewsName.add(attr.getProperties()
                                                .getPlotProperties().getCurve()
                                                .getName()
                                                + writeKey);
                                        dataViewsId.add(attr.getCompleteName()
                                                + writeKey);
                                        break;
                                    case AttrWriteType._READ_WITH_WRITE:
                                    case AttrWriteType._READ_WRITE:
                                        dataViewsName.add(attr.getProperties()
                                                .getPlotProperties().getCurve()
                                                .getName()
                                                + readKey);
                                        dataViewsId.add(attr.getCompleteName()
                                                + readKey);
                                        dataViewsName.add(attr.getProperties()
                                                .getPlotProperties().getCurve()
                                                .getName()
                                                + writeKey);
                                        dataViewsId.add(attr.getCompleteName()
                                                + writeKey);
                                        break;
                                }
                        }
                    }
                    attr = null;
                }
                String[][] res = new String[2][];
                res[0] = new String[dataViewsName.size()];
                res[1] = new String[dataViewsId.size()];
                for (int i = 0; i < dataViewsName.size(); i++) {
                    res[0][i] = dataViewsName.get(i);
                    res[1][i] = dataViewsId.get(i);
                }
                return res;
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateButton) {
            String[][] nameIdList = getAttributesNamesList();
            if (nameIdList != null) {
                this.variablePanel.setNamesAndIds(nameIdList[0], nameIdList[1]);
                if (expressionTree.getSelectedAttribute() != null) {
                    applyAction.setEnabled(true);
                }
            }
        }
        super.actionPerformed(e);
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
        colorIndex = getDefaultNextColorIndex(colorIndex) % defaultColor.length;
        return colorIndex;
    }

    public int getDefaultNextColorIndex(int index) {
        return (index + 5) % defaultColor.length;
    }

    public ExpressionTree getExpressionTree() {
        return expressionTree;
    }

    public boolean isHidden() {
        return hiddenCheckBox.isSelected();
    }

    public VCExpressionApplyAction getApplyAction() {
        return applyAction;
    }

    public int getAxisChoice() {
        if ("---".equals(super.axisBox.getItemAt(0))) {
            return super.axisBox.getSelectedIndex() - 1;
        }
        return super.axisBox.getSelectedIndex();
    }

    public JComboBox getAxisChoiceCombo() {
        return super.axisBox;
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

    public void setFactor(double factor) {
        factorField.setText(factor + "");
    }

    public void setEnableAddButton(boolean enabled) {
        addButton.setEnabled(enabled);
    }
}
