package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCExpressionAddAction;
import fr.soleil.mambo.actions.view.VCExpressionApplyAction;
import fr.soleil.mambo.actions.view.VCExpressionDeleteAction;
import fr.soleil.mambo.actions.view.listeners.ExpressionNameFieldListener;
import fr.soleil.mambo.actions.view.listeners.OpenEditExpressionListener;
import fr.soleil.mambo.components.view.ExpressionTree;
import fr.soleil.mambo.containers.sub.dialogs.MamboErrorDialog;
import fr.soleil.mambo.containers.view.ViewExpressionVariablesDetailPanel;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.data.view.ViewConfigurationAttributePlotProperties;
import fr.soleil.mambo.data.view.plot.Polynomial2OrderTransform;
import fr.soleil.mambo.models.ExpressionTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ExpressionTab extends JPanel {

    private static ExpressionTab            instance      = null;
    private ExpressionAttribute             currentAttribute;

    protected AttributesPlotPropertiesPanel propertiesPanel;
    protected JPanel                        attributesPanel, detailPanel,
            actionPanel;
    protected JLabel                        nameLabel, expressionLabel,
            expressionField, variableHeaderLabel;
    protected JTextField                    nameField;
    protected JButton                       addButton, applyButton,
            deleteButton;
    protected JScrollPane                   treeScrollPane, variableScrollPane;
    protected MamboErrorDialog              errorDialog;

    protected final static ImageIcon        addIcon       = new ImageIcon(
                                                                  Mambo.class
                                                                          .getResource("icons/add.gif"));
    protected final static ImageIcon        deleteIcon    = new ImageIcon(
                                                                  Mambo.class
                                                                          .getResource("icons/delete.gif"));
    protected final static ImageIcon        applyIcon     = new ImageIcon(
                                                                  Mambo.class
                                                                          .getResource("icons/apply.gif"));
    protected final static Dimension        scrollPaneDim = new Dimension(200,
                                                                  200);

    public static ExpressionTab getInstance() {
        if (instance == null) {
            instance = new ExpressionTab();
        }
        return instance;
    }

    protected ExpressionTab() {
        super();
        initComponents();
        addComponents();
        initLayout();
    }

    protected void initComponents() {
        // init attributesPanel
        attributesPanel = new JPanel();

        // init detailPanel
        detailPanel = new JPanel();
        nameLabel = new JLabel(Messages.getMessage("EXPRESSION_ATTRIBUTE_NAME"));
        nameLabel.setFont(GUIUtilities.labelFont);
        nameLabel.setForeground(GUIUtilities.fColor);
        nameField = new JTextField();
        nameField.getDocument().addDocumentListener(
                new ExpressionNameFieldListener());
        nameField.setToolTipText(Messages
                .getMessage("EXPRESSION_ATTRIBUTE_HELP"));
        expressionLabel = new JLabel(Messages
                .getMessage("EXPRESSION_EXPRESSION"));
        expressionLabel.setFont(GUIUtilities.labelFont);
        expressionLabel.setForeground(GUIUtilities.fColor);
        expressionField = new JLabel("");
        expressionField.setBackground(Color.WHITE);
        expressionField.setOpaque(true);
        expressionField.setBorder(new JTextField().getBorder());
        expressionField.setToolTipText(Messages
                .getMessage("EXPRESSION_EXPRESSION_HELP"));
        expressionField.addMouseListener(new OpenEditExpressionListener());
        expressionField.setPreferredSize(new Dimension(130, 25));
        expressionField.setMinimumSize(new Dimension(130, 25));
        detailPanel.setPreferredSize(new Dimension(130, 50));
        detailPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        // end init detailPanel

        // init actionPanel
        actionPanel = new JPanel();
        addButton = new JButton(VCExpressionAddAction.getInstance());
        addButton.setIcon(addIcon);
        applyButton = new JButton(VCExpressionApplyAction.getInstance());
        applyButton.setIcon(applyIcon);
        deleteButton = new JButton(VCExpressionDeleteAction.getInstance());
        deleteButton.setIcon(deleteIcon);
        // end init actionPanel

        // init treePanel
        treeScrollPane = new JScrollPane(ExpressionTree.getInstance());
        treeScrollPane.setBackground(Color.WHITE);
        treeScrollPane.getViewport().setBackground(Color.WHITE);
        treeScrollPane.setPreferredSize(scrollPaneDim);
        // end init treePanel

        // init variable panel
        variableHeaderLabel = new JLabel(Messages
                .getMessage("EXPRESSION_VARIABLE_HEADER"), JLabel.CENTER);
        variableScrollPane = new JScrollPane(ViewExpressionVariablesDetailPanel
                .getInstance());
        variableScrollPane.setColumnHeaderView(variableHeaderLabel);
        variableScrollPane.setPreferredSize(scrollPaneDim);
        // end init variable panel

        // end init attributesPanel

        // init propertiesPanel
        propertiesPanel = new AttributesPlotPropertiesPanel();
        // end init propertiesPanel
    }

    protected void addComponents() {
        // add attributesPanel

        // add detailPanel
        detailPanel.add(nameLabel);
        detailPanel.add(nameField);
        detailPanel.add(expressionLabel);
        detailPanel.add(expressionField);
        attributesPanel.add(detailPanel);
        // end add detailPanel

        // add actionPanel
        actionPanel.add(addButton);
        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(applyButton);
        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(deleteButton);
        attributesPanel.add(actionPanel);
        // end add actionPanel

        // add treePanel
        attributesPanel.add(treeScrollPane);
        // end add treePanel

        // add variable panel
        attributesPanel.add(variableScrollPane);
        // end add variable panel

        this.add(attributesPanel);
        // end add attributesPanel

        // add propertiesPanel
        this.add(propertiesPanel);
        // end add propertiesPanel
    }

    protected void initLayout() {
        // init this layout

        // init attributesPanel layout

        // init detailPanel layout
        detailPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(detailPanel, 2, 2, 0, 0, 0, 0, true);
        // end init detailPanel layout

        // init actionPanel layout
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        // end init actionPanel layout

        attributesPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(attributesPanel, attributesPanel
                .getComponentCount(), 1, 0, 0, 0, 5, true);
        // end init attributesPanel layout

        this.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(this, 1, 2, 0, 0, 0, 0, true);
        // end init this layout
    }

    public AttributesPlotPropertiesPanel getPropertiesPanel() {
        return propertiesPanel;
    }

    public void addExpression() {
        if (nameField.getText() == null
                || "".equals(nameField.getText().trim())) {
            errorDialog = new MamboErrorDialog(VCEditDialog.getInstance(),
                    Messages.getMessage("EXPRESSION_ERROR_TITLE"), Messages
                            .getMessage("EXPRESSION_ATTRIBUTE_NAME_ERROR"));
            int width = errorDialog.getWidth();
            int height = errorDialog.getHeight();
            int x = VCEditDialog.getInstance().getX() + 50;
            int y = VCEditDialog.getInstance().getY() + 50;
            errorDialog.setBounds(x, y, width, height);
            errorDialog.setVisible(true);
            errorDialog = null;
        }
        else if (expressionField.getText() == null
                || "".equals(expressionField.getText().trim())) {
            errorDialog = new MamboErrorDialog(VCEditDialog.getInstance(),
                    Messages.getMessage("EXPRESSION_ERROR_TITLE"), Messages
                            .getMessage("EXPRESSION_EXPRESSION_ERROR"));
            int width = errorDialog.getWidth();
            int height = errorDialog.getHeight();
            int x = VCEditDialog.getInstance().getX() + 50;
            int y = VCEditDialog.getInstance().getY() + 50;
            errorDialog.setBounds(x, y, width, height);
            errorDialog.setVisible(true);
            errorDialog = null;
        }
        else {

            currentAttribute = new ExpressionAttribute(nameField.getText()
                    .trim(), expressionField.getText().trim(),
                    ViewExpressionVariablesDetailPanel.getInstance()
                            .getVariables(), ViewExpressionVariablesDetailPanel
                            .getInstance().isX());

            AttributesPlotPropertiesPanel.setColorIndex(ExpressionTreeModel
                    .getInstance().getExpressionAttributes().size()*5);
            propertiesPanel.rotateCurveColor();

            currentAttribute.setFactor(propertiesPanel.getFactor());
            ViewConfigurationAttributePlotProperties plotProperties = new ViewConfigurationAttributePlotProperties(
                    propertiesPanel.getViewType(), propertiesPanel
                            .getAxisChoice(), propertiesPanel.getBar(),
                    propertiesPanel.getCurve(), propertiesPanel.getMarker(),
                    propertiesPanel.getTransform(), propertiesPanel.isHidden());
            currentAttribute.setProperties(plotProperties);
            if (!ExpressionTreeModel.getInstance().addAttribute(
                    currentAttribute)) {
                errorDialog = new MamboErrorDialog(
                        VCEditDialog.getInstance(),
                        Messages.getMessage("EXPRESSION_ERROR_TITLE"),
                        Messages
                                .getMessage("EXPRESSION_ATTRIBUTE_NAME_PRESENT_ERROR"));
                int width = errorDialog.getWidth();
                int height = errorDialog.getHeight();
                int x = VCEditDialog.getInstance().getX() + 50;
                int y = VCEditDialog.getInstance().getY() + 50;
                errorDialog.setBounds(x, y, width, height);
                errorDialog.setVisible(true);
                errorDialog = null;
            }
            plotProperties = null;
        }
    }

    public void delete() {
        Vector<ExpressionAttribute> selectedAttributes = ExpressionTree
                .getInstance().getSelectedAttributes();
        if (selectedAttributes == null) {
            errorDialog = new MamboErrorDialog(VCEditDialog.getInstance(),
                    Messages.getMessage("EXPRESSION_ERROR_TITLE"), Messages
                            .getMessage("EXPRESSION_SELECTION_ERROR"));
            int width = errorDialog.getWidth();
            int height = errorDialog.getHeight();
            int x = VCEditDialog.getInstance().getX() + 50;
            int y = VCEditDialog.getInstance().getY() + 50;
            errorDialog.setBounds(x, y, width, height);
            errorDialog.setVisible(true);
            errorDialog = null;
            return;
        }
        String[] names = new String[selectedAttributes.size()];
        for (int i = 0; i < selectedAttributes.size(); i++) {
            names[i] = (selectedAttributes.get(i)).getName();
        }
        selectedAttributes = null;
        for (int i = 0; i < names.length; i++) {
            ExpressionTreeModel.getInstance().removeAttribute(names[i]);
        }
    }

    public void apply() {
        ExpressionAttribute attr = ExpressionTree.getInstance()
                .getSelectedAttribute();
        if (attr != null) {
            String name = attr.getName();
            if (nameField.getText() == null
                    || "".equals(nameField.getText().trim())) {
                errorDialog = new MamboErrorDialog(VCEditDialog.getInstance(),
                        Messages.getMessage("EXPRESSION_ERROR_TITLE"), Messages
                                .getMessage("EXPRESSION_ATTRIBUTE_NAME_ERROR"));
                int width = errorDialog.getWidth();
                int height = errorDialog.getHeight();
                int x = VCEditDialog.getInstance().getX() + 50;
                int y = VCEditDialog.getInstance().getY() + 50;
                errorDialog.setBounds(x, y, width, height);
                errorDialog.setVisible(true);
                errorDialog = null;
            }
            else if (expressionField.getText() == null
                    || "".equals(expressionField.getText().trim())) {
                errorDialog = new MamboErrorDialog(VCEditDialog.getInstance(),
                        Messages.getMessage("EXPRESSION_ERROR_TITLE"), Messages
                                .getMessage("EXPRESSION_EXPRESSION_ERROR"));
                int width = errorDialog.getWidth();
                int height = errorDialog.getHeight();
                int x = VCEditDialog.getInstance().getX() + 50;
                int y = VCEditDialog.getInstance().getY() + 50;
                errorDialog.setBounds(x, y, width, height);
                errorDialog.setVisible(true);
                errorDialog = null;
            }
            else {
                currentAttribute = attr.duplicate();
                currentAttribute.setName(nameField.getText().trim());
                currentAttribute
                        .setExpression(expressionField.getText().trim());
                currentAttribute
                        .setVariables(ViewExpressionVariablesDetailPanel
                                .getInstance().getVariables());
                currentAttribute.setX(ViewExpressionVariablesDetailPanel
                        .getInstance().isX());
                if (!ExpressionTreeModel.getInstance().updateAttribute(name,
                        currentAttribute.duplicate())) {
                    errorDialog = new MamboErrorDialog(
                            VCEditDialog.getInstance(),
                            Messages.getMessage("EXPRESSION_ERROR_TITLE"),
                            Messages
                                    .getMessage("EXPRESSION_ATTRIBUTE_NAME_PRESENT_ERROR"));
                    int width = errorDialog.getWidth();
                    int height = errorDialog.getHeight();
                    int x = VCEditDialog.getInstance().getX() + 50;
                    int y = VCEditDialog.getInstance().getY() + 50;
                    errorDialog.setBounds(x, y, width, height);
                    errorDialog.setVisible(true);
                    errorDialog = null;
                }
                name = null;
            }
            attr = null;
        }
        else {
            errorDialog = new MamboErrorDialog(VCEditDialog.getInstance(),
                    Messages.getMessage("EXPRESSION_ERROR_TITLE"), Messages
                            .getMessage("EXPRESSION_SELECTION_ERROR"));
            int width = errorDialog.getWidth();
            int height = errorDialog.getHeight();
            int x = VCEditDialog.getInstance().getX() + 50;
            int y = VCEditDialog.getInstance().getY() + 50;
            errorDialog.setBounds(x, y, width, height);
            errorDialog.setVisible(true);
            errorDialog = null;
        }
    }

    public void setParameters(ExpressionAttribute attr) {
        if (attr == null) return;
        currentAttribute = attr.duplicate();
        reloadDetailPanel();
        reloadPropertiesPanel();
    }

    public void setParametersLight(ExpressionAttribute attr) {
        if (attr == null) return;
        currentAttribute = attr.duplicate();
        ViewConfigurationAttributePlotProperties plotProperties = new ViewConfigurationAttributePlotProperties(
                propertiesPanel.getViewType(), propertiesPanel.getAxisChoice(),
                propertiesPanel.getBar(), propertiesPanel.getCurve(),
                propertiesPanel.getMarker(), propertiesPanel.getTransform(),
                propertiesPanel.isHidden());
        currentAttribute.setProperties(plotProperties);
        currentAttribute.setFactor(propertiesPanel.getFactor());
        plotProperties = null;
        reloadDetailPanel();
    }

    protected void reloadPropertiesPanel() {
        ViewConfigurationAttributePlotProperties properties = currentAttribute
                .getProperties();
        propertiesPanel.setAxisChoice(properties.getAxisChoice());
        propertiesPanel.setViewType(properties.getViewType());
        propertiesPanel.setFactor(currentAttribute.getFactor());

        propertiesPanel.setBarFillColor(properties.getBar().getFillColor());
        propertiesPanel.setBarFillingMethod(properties.getBar()
                .getFillingMethod());
        propertiesPanel.setBarFillStyle(properties.getBar().getFillStyle());
        propertiesPanel.setBarWidth(properties.getBar().getWidth());

        propertiesPanel.setCurveColor(properties.getCurve().getColor());
        propertiesPanel.setCurveLineStyle(properties.getCurve().getLineStyle());
        propertiesPanel.setCurveWidth(properties.getCurve().getWidth());

        propertiesPanel.setMarkerColor(properties.getMarker().getColor());
        propertiesPanel.setMarkerSize(properties.getMarker().getSize());
        propertiesPanel.setMarkerStyle(properties.getMarker().getStyle());

        Polynomial2OrderTransform transform = (Polynomial2OrderTransform) properties
                .getTransform();
        if (transform != null) {
            propertiesPanel.setTransformA0(transform.getA0());
            propertiesPanel.setTransformA1(transform.getA1());
            propertiesPanel.setTransformA2(transform.getA2());
        }
        propertiesPanel.setHidden(properties.isHidden());
        transform = null;
        properties = null;

    }

    protected void reloadDetailPanel() {
        nameField.setText(currentAttribute.getName());
        expressionField.setText(currentAttribute.getExpression());
        ViewExpressionVariablesDetailPanel.getInstance().setVariables(
                currentAttribute.getVariables());
        ViewExpressionVariablesDetailPanel.getInstance().setX(
                currentAttribute.isX());
        ViewExpressionVariablesDetailPanel.getInstance().loadPanel();
        VCExpressionApplyAction.getInstance().setEnabled(false);
    }

    public void editExpression() {
        ExpressionEditDialog.getInstance().clean();
        ExpressionEditDialog.getInstance().setName(nameField.getText());
        ExpressionEditDialog.getInstance().setExpression(
                expressionField.getText());
        ExpressionEditDialog.getInstance().reload();
        ExpressionEditDialog.getInstance().setVisible(true);
    }

    public void reset() {
        currentAttribute = null;
        ViewExpressionVariablesDetailPanel.getInstance().clean();
        expressionField.setText("");
        nameField.setText("");
        VCExpressionApplyAction.getInstance().setEnabled(false);
    }

    public void resetLight() {
        currentAttribute = null;
        // ViewExpressionVariablesDetailPanel.getInstance().clean();
        expressionField.setText("");
        nameField.setText("");
        VCExpressionApplyAction.getInstance().setEnabled(false);
    }
}
