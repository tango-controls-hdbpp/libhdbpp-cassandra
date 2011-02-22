package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCExpressionHelpAction;
import fr.soleil.mambo.bean.view.ViewConfigurationBean;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.sub.dialogs.MamboErrorDialog;
import fr.soleil.mambo.containers.view.ViewExpressionVariablesEditPanel;
import fr.soleil.mambo.data.view.ExpressionAttribute;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ExpressionEditDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 8908275498480033625L;
	protected String name;
	protected String expression;
	protected JButton generateButton;
	protected JButton helpButton;
	protected JButton okButton;
	protected JButton cancelButton;
	protected JLabel expressionLabel;
	protected JLabel variableHeaderLabel;
	protected JTextField expressionField;
	protected JScrollPane variableScrollPane;
	protected JPanel mainPanel;
	protected JPanel detailPanel;
	protected JPanel generatePanel;
	protected JPanel actionPanel;
	protected final static Insets noMargin = new Insets(0, 0, 0, 0);
	private final static ImageIcon okIcon = new ImageIcon(Mambo.class
			.getResource("icons/validate.gif"));
	private final static ImageIcon cancelIcon = new ImageIcon(Mambo.class
			.getResource("icons/cancel.gif"));
	protected MamboErrorDialog errorDialog;
	protected ExpressionNamesChooser expressionNamesChooser;
	protected ViewExpressionVariablesEditPanel viewExpressionVariablesEditPanel;
	private VCEditDialog editDialog;
	private ExpressionTab expressionTab;

	public ExpressionEditDialog(VCEditDialog editDialog,
			ExpressionTab expressionTab,
			ViewConfigurationBean viewConfigurationBean) {
		super(editDialog, Messages.getMessage("EXPRESSION_EXPRESSION_EDIT"),
				true);
		this.editDialog = editDialog;
		this.expressionTab = expressionTab;
		viewExpressionVariablesEditPanel = new ViewExpressionVariablesEditPanel(
				this);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowKiller());
		expressionNamesChooser = new ExpressionNamesChooser(
				viewConfigurationBean, editDialog);
	}

	public void reload() {
		initComponents();
		addComponents();
		initLayout();
		initBounds();
		generateVariable();
		expressionTab.getViewExpressionVariablesDetailPanel().loadPanel();
		for (int i = 0; i < expressionTab
				.getViewExpressionVariablesDetailPanel().getVariables().length; i++) {
			viewExpressionVariablesEditPanel.setVariableName(expressionTab
					.getViewExpressionVariablesDetailPanel().getVariables()[i],
					i);
		}
	}

	protected void initComponents() {
		mainPanel = new JPanel();

		detailPanel = new JPanel();
		expressionLabel = new JLabel(Messages
				.getMessage("EXPRESSION_EXPRESSION"));
		expressionLabel.setFont(GUIUtilities.labelFont);
		expressionLabel.setForeground(GUIUtilities.fColor);
		expressionField = new JTextField(expression);
		expressionField.setToolTipText(Messages
				.getMessage("EXPRESSION_EXPRESSION_HELP"));
		Dimension bestDim = new Dimension(100, expressionField
				.getPreferredSize().height);
		expressionField.setPreferredSize(bestDim);
		expressionField.setMinimumSize(bestDim);
		expressionField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
				bestDim.height));
		bestDim = null;
		helpButton = new JButton(new VCExpressionHelpAction(this));
		helpButton.setMargin(noMargin);
		detailPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

		generatePanel = new JPanel();
		generateButton = new JButton(Messages
				.getMessage("EXPRESSION_VARIABLE_GENERATE"));
		generateButton.addActionListener(this);

		variableScrollPane = new JScrollPane(viewExpressionVariablesEditPanel);
		variableHeaderLabel = new JLabel(Messages
				.getMessage("EXPRESSION_VARIABLE_HEADER"), JLabel.CENTER);
		variableScrollPane.setColumnHeaderView(variableHeaderLabel);

		actionPanel = new JPanel();
		okButton = new JButton(Messages.getMessage("EXPRESSION_OK"));
		okButton.setIcon(okIcon);
		okButton.setMargin(noMargin);
		okButton.addActionListener(this);
		cancelButton = new JButton(Messages.getMessage("EXPRESSION_CANCEL"));
		cancelButton.setIcon(cancelIcon);
		cancelButton.setMargin(noMargin);
		cancelButton.addActionListener(this);
	}

	protected void addComponents() {
		detailPanel.add(expressionLabel);
		detailPanel.add(helpButton);
		detailPanel.add(expressionField);
		mainPanel.add(detailPanel);

		generatePanel.add(Box.createHorizontalGlue());
		generatePanel.add(generateButton);
		generatePanel.add(Box.createHorizontalGlue());
		mainPanel.add(generatePanel);

		mainPanel.add(variableScrollPane);

		actionPanel.add(okButton);
		actionPanel.add(Box.createHorizontalGlue());
		actionPanel.add(cancelButton);
		mainPanel.add(actionPanel);
	}

	protected void initLayout() {
		detailPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(detailPanel, 1, detailPanel
				.getComponentCount(), 0, 0, 0, 0, true);

		generatePanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(generatePanel, 1, generatePanel
				.getComponentCount(), 0, 0, 0, 0, true);

		actionPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(actionPanel, 1, actionPanel
				.getComponentCount(), 0, 0, 0, 0, true);

		mainPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(mainPanel, mainPanel
				.getComponentCount(), 1, 0, 0, 0, 0, true);
		this.setContentPane(mainPanel);
	}

	protected void initBounds() {
		this.setContentPane(new JScrollPane(mainPanel));
		this.setSize(400, 700);
		this.setResizable(false);
		int x = MamboFrame.getInstance().getX()
				+ MamboFrame.getInstance().getWidth() - 450;
		if (x < 0)
			x = 0;
		int y = MamboFrame.getInstance().getY()
				+ MamboFrame.getInstance().getHeight() - 450;
		if (y < 0)
			y = 0;
		int y1 = editDialog.getY() + 50;
		if (y > y1 && y != 0)
			y = y1;
		this.setLocation(x, y);
	}

	public void generateVariable() {
		expression = expressionField.getText();
		expressionNamesChooser.reset();
		viewExpressionVariablesEditPanel.setExpression(expression);
		viewExpressionVariablesEditPanel.loadPanel();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void ok() {
		expression = expressionField.getText();
		if (expression == null || "".equals(expression.trim())) {
			errorDialog = new MamboErrorDialog(editDialog, Messages
					.getMessage("EXPRESSION_ERROR_TITLE"), Messages
					.getMessage("EXPRESSION_ATTRIBUTE_NAME_ERROR"));
			int width = errorDialog.getWidth();
			int height = errorDialog.getHeight();
			int x = editDialog.getX() + 50;
			int y = editDialog.getY() + 50;
			errorDialog.setBounds(x, y, width, height);
			errorDialog.setVisible(true);
			errorDialog = null;
			return;
		} else if (viewExpressionVariablesEditPanel.getVariables() == null
				|| viewExpressionVariablesEditPanel.getVariables().length == 0) {
			errorDialog = new MamboErrorDialog(editDialog, Messages
					.getMessage("EXPRESSION_ERROR_TITLE"), Messages
					.getMessage("EXPRESSION_VARIABLE_ERROR"));
			int width = errorDialog.getWidth();
			int height = errorDialog.getHeight();
			int x = editDialog.getX() + 50;
			int y = editDialog.getY() + 50;
			errorDialog.setBounds(x, y, width, height);
			errorDialog.setVisible(true);
			errorDialog = null;
			return;
		} else {
			for (int i = 0; i < viewExpressionVariablesEditPanel.getVariables().length; i++) {
				String variable = viewExpressionVariablesEditPanel
						.getVariables()[i];
				if (variable == null || "".equals(variable.trim())) {
					errorDialog = new MamboErrorDialog(editDialog, Messages
							.getMessage("EXPRESSION_ERROR_TITLE"), Messages
							.getMessage("EXPRESSION_VARIABLE_ERROR"));
					int width = errorDialog.getWidth();
					int height = errorDialog.getHeight();
					int x = editDialog.getX() + 50;
					int y = editDialog.getY() + 50;
					errorDialog.setBounds(x, y, width, height);
					errorDialog.setVisible(true);
					errorDialog = null;
					variable = null;
					return;
				}
				variable = null;
			}
		}
		ExpressionAttribute attribute = new ExpressionAttribute(name,
				expression, viewExpressionVariablesEditPanel.getVariables(),
				viewExpressionVariablesEditPanel.isX());
		expressionTab.setParametersLight(attribute);
		if (expressionTab.getExpressionTree().getSelectedAttribute() != null) {
			expressionTab.getApplyAction().setEnabled(true);
		}
		cancel();
	}

	public void cancel() {
		setVisible(false);
		clean();
		dispose();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void clean() {
		viewExpressionVariablesEditPanel.clean();
		if (mainPanel != null) {
			mainPanel.removeAll();
			mainPanel = null;
		}
		if (detailPanel != null) {
			detailPanel.removeAll();
			detailPanel = null;
		}
		if (generatePanel != null) {
			generatePanel.removeAll();
			generatePanel = null;
		}
		if (actionPanel != null) {
			actionPanel.removeAll();
			actionPanel = null;
		}
		name = null;
		expression = null;
		expressionField = null;
		expressionLabel = null;
		variableScrollPane = null;
		generateButton = null;
		helpButton = null;
		okButton = null;
		cancelButton = null;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == generateButton) {
			generateVariable();
		} else if (e.getSource() == okButton) {
			ok();
		} else if (e.getSource() == cancelButton) {
			cancel();
		}
	}

	public ExpressionNamesChooser getExpressionNamesChooser() {
		return expressionNamesChooser;
	}

	protected class WindowKiller extends WindowAdapter {

		public WindowKiller() {
			super();
		}

		public void windowClosing(WindowEvent e) {
			cancel();
		}
	}

	public ViewExpressionVariablesEditPanel getViewExpressionVariablesEditPanel() {
		return viewExpressionVariablesEditPanel;
	}

}
