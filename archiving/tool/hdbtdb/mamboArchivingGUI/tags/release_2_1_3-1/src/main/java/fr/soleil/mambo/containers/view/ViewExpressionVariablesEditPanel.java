package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import fr.soleil.mambo.actions.view.listeners.ExpressionNameMouseListerner;
import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.containers.view.dialogs.ExpressionEditDialog;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewExpressionVariablesEditPanel extends MamboCleanablePanel {

	private static final long serialVersionUID = -1231984413888937037L;
	protected String[] variables;
	protected JLabel[] title, name;
	protected String expression;
	protected boolean x = false;
	protected ExpressionEditDialog editDialog;

	public ViewExpressionVariablesEditPanel(ExpressionEditDialog editDialog) {
		super();
		this.editDialog = editDialog;
		expression = "";
	}

	public void clean() {
		removeAll();
		this.setLayout(null);
		expression = null;
		if (variables != null) {
			for (int i = 0; i < variables.length; i++) {
				variables[i] = null;
				title[i] = null;
				name[i] = null;
			}
			variables = null;
		}
		name = null;
		title = null;

		editDialog.getExpressionNamesChooser().clean();
		editDialog.getExpressionNamesChooser().dispose();
	}

	public void loadPanel() {
		this.removeAll();
		if (expression.indexOf("x1") != -1) {
			x = false;
			int count = 0;
			while (true) {
				if (expression.indexOf("x" + (count + 1)) != -1) {
					count++;
				} else {
					break;
				}
			}
			variables = new String[count];
			title = new JLabel[count];
			name = new JLabel[count];
			for (int i = 0; i < count; i++) {
				title[i] = new JLabel("x" + (i + 1) + ":");
				title[i].setMaximumSize(title[i].getPreferredSize());
				this.add(title[i]);
				name[i] = new JLabel("");
				name[i].setBackground(Color.WHITE);
				name[i].setBorder(new LineBorder(Color.BLACK, 1));
				name[i].setOpaque(true);
				name[i].addMouseListener(new ExpressionNameMouseListerner(i,
						editDialog.getExpressionNamesChooser()));
				name[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
				this.add(name[i]);
			}
		} else {
			if (expression.replaceAll("x", "_x_").replaceAll("e_x_p", "exp")
					.indexOf("_x_") != -1) {
				x = true;
				variables = new String[1];
				title = new JLabel[1];
				name = new JLabel[1];
				title[0] = new JLabel("x:");
				title[0].setMaximumSize(title[0].getPreferredSize());
				this.add(title[0]);
				name[0] = new JLabel("");
				name[0].setBackground(Color.WHITE);
				name[0].setBorder(new LineBorder(Color.BLACK, 1));
				name[0].setOpaque(true);
				name[0].addMouseListener(new ExpressionNameMouseListerner(0,
						editDialog.getExpressionNamesChooser()));
				name[0].setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
				this.add(name[0]);
			} else {
				x = false;
				variables = new String[0];
				title = new JLabel[0];
				name = new JLabel[0];
			}
		}
		this.add(Box.createVerticalGlue());
		this.add(Box.createVerticalGlue());
		this.setLayout();
	}

	public String getName() {
		return expression;
	}

	public String getFullName() {
		return expression;
	}

	public String[] getVariables() {
		return variables;
	}

	public void setExpression(String expression) {
		if (expression == null)
			this.expression = "";
		else
			this.expression = expression;
	}

	public void setVariableName(String _name, int index) {
		if (variables != null && index < variables.length) {
			variables[index] = new String(_name);
			name[index].setText(new String(_name));
		}
	}

	protected void setLayout() {
		if (this.getComponentCount() != 0) {
			this.setLayout(new SpringLayout());
			SpringUtilities.makeCompactGrid(this, variables.length + 1, 2, 0,
					0, 0, 0, true);
		} else {
			this.setLayout(null);
		}
		Dimension size = new Dimension(300, 25 * variables.length);
		this.setPreferredSize(size);
		this.setSize(size);
		this.setMinimumSize(size);
		this.setMaximumSize(size);
		this.revalidate();
		this.repaint();
		size = null;
	}

	public boolean isX() {
		return x;
	}

	public void setX(boolean x) {
		this.x = x;
	}

	@Override
	public void lightClean() {
		removeAll();
	}
}
