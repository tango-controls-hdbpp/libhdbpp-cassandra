package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.models.SupportedFunctionsTableModel;
import fr.soleil.mambo.models.SupportedOperatorsTableModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class ExpressionHelpDialog extends JDialog {

	private static final long serialVersionUID = 2128932058590988945L;
	protected JLabel[] introLabel;
	protected JLabel operatorLabel, functionLabel;
	protected JTable operatorTable, functionTable;
	protected JScrollPane operatorScrollPane, functionScrollPane;
	protected JPanel mainPanel;

	protected final static Dimension bestDim = new Dimension(600, 750);

	public ExpressionHelpDialog(ExpressionEditDialog expressionEditDialog) {
		super(expressionEditDialog, Messages
				.getMessage("EXPRESSION_SUPPORT_TITLE"), true);
		initComponents();
		addComponents();
		initLayout();
	}

	protected void initComponents() {
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.WHITE);

		introLabel = new JLabel[6];
		introLabel[0] = new JLabel(Messages
				.getMessage("EXPRESSION_SUPPORT_INTRODUCTION_HOWTO"));
		introLabel[1] = new JLabel(Messages
				.getMessage("EXPRESSION_SUPPORT_INTRODUCTION_ENTER"));
		introLabel[2] = new JLabel(Messages
				.getMessage("EXPRESSION_SUPPORT_INTRODUCTION_VARIABLE_X"));
		introLabel[3] = new JLabel(Messages
				.getMessage("EXPRESSION_SUPPORT_INTRODUCTION_VARIABLE_XN"));
		introLabel[4] = new JLabel(Messages
				.getMessage("EXPRESSION_SUPPORT_INTRODUCTION_ASSOCIATION"));
		introLabel[5] = new JLabel(Messages
				.getMessage("EXPRESSION_SUPPORT_INTRODUCTION_EVALUATION"));
		operatorLabel = new JLabel(Messages
				.getMessage("EXPRESSION_OPERATOR_TITLE"));
		operatorLabel.setForeground(GUIUtilities.getExpressionTitleColor());
		operatorLabel.setFont(GUIUtilities.getExpressionTitleFont());
		functionLabel = new JLabel(Messages
				.getMessage("EXPRESSION_FUNCTION_TITLE"));
		functionLabel.setForeground(GUIUtilities.getExpressionTitleColor());
		functionLabel.setFont(GUIUtilities.getExpressionTitleFont());

		operatorTable = new JTable(new SupportedOperatorsTableModel());
		operatorTable.setEnabled(false);
		operatorTable.getTableHeader().setResizingAllowed(false);
		operatorTable.getTableHeader().setReorderingAllowed(false);
		functionTable = new JTable(new SupportedFunctionsTableModel());
		functionTable.setEnabled(false);
		functionTable.getTableHeader().setResizingAllowed(false);
		functionTable.getTableHeader().setReorderingAllowed(false);

		operatorScrollPane = new JScrollPane(operatorTable);
		operatorScrollPane
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, 211));
		operatorScrollPane.setPreferredSize(new Dimension(500, 211));
		operatorScrollPane.setBackground(Color.WHITE);
		operatorScrollPane.getViewport().setBackground(Color.WHITE);
		functionScrollPane = new JScrollPane(functionTable);
		functionScrollPane
				.setMaximumSize(new Dimension(Integer.MAX_VALUE, 355));
		functionScrollPane.setPreferredSize(new Dimension(500, 355));
		functionScrollPane.setBackground(Color.WHITE);
		functionScrollPane.getViewport().setBackground(Color.WHITE);
	}

	protected void addComponents() {
		for (int i = 0; i < introLabel.length; i++) {
			mainPanel.add(introLabel[i]);
		}
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(operatorLabel);
		mainPanel.add(operatorScrollPane);
		mainPanel.add(Box.createVerticalGlue());
		mainPanel.add(functionLabel);
		mainPanel.add(functionScrollPane);
	}

	protected void initLayout() {
		mainPanel.setLayout(new SpringLayout());
		SpringUtilities.makeCompactGrid(mainPanel, mainPanel
				.getComponentCount(), 1, 0, 0, 0, 0, true);
	}

	protected void initBounds() {
		mainPanel.setPreferredSize(bestDim);
		this.setContentPane(new JScrollPane(mainPanel));
		this.setSize(bestDim.width + 30, 700);
		this.setResizable(false);
		int x = MamboFrame.getInstance().getX()
				+ MamboFrame.getInstance().getWidth() - (bestDim.width + 50);
		if (x < 0)
			x = 0;
		int y = MamboFrame.getInstance().getY()
				+ MamboFrame.getInstance().getHeight() - (bestDim.height + 50);
		if (y < 0)
			y = 0;
		int y1 = getOwner().getY() + 10;
		if (y > y1 && y != 0)
			y = y1;
		this.setLocation(x, y);
	}

	public void setVisible(boolean visible) {
		if (visible) {
			initBounds();
		}
		super.setVisible(visible);
	}

}
