package fr.soleil.mambo.containers.view;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SpringLayout;

import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewExpressionVariablesDetailPanel extends MamboCleanablePanel {

    private static final long serialVersionUID = -7467977834296371241L;
    protected String[]        variables;
    protected JLabel[]        title, name;
    protected boolean         x;

    public ViewExpressionVariablesDetailPanel() {
        super();
    }

    @Override
    public void clean() {
        removeAll();
        setLayout(null);
        if (variables != null) {
            for (int i = 0; i < variables.length; i++) {
                variables[i] = null;
                title[i] = null;
                name[i] = null;
            }
            variables = null;
            name = null;
            title = null;
        }
        revalidate();
        repaint();
    }

    @Override
    public void loadPanel() {
        this.removeAll();
        if (variables == null) variables = new String[0];
        if (variables.length > 0 && !x) {
            title = new JLabel[variables.length];
            name = new JLabel[variables.length];
            for (int i = 0; i < variables.length; i++) {
                title[i] = new JLabel("x" + (i + 1) + ":");
                title[i].setMaximumSize(title[i].getPreferredSize());
                add(title[i]);
                name[i] = new JLabel(variables[i]);
                add(name[i]);
            }
        }
        else {
            if (variables.length == 1) {
                title = new JLabel[1];
                name = new JLabel[1];
                title[0] = new JLabel("x:");
                title[0].setMaximumSize(title[0].getPreferredSize());
                add(title[0]);
                name[0] = new JLabel(variables[0]);
                add(name[0]);
            }
            else {
                title = new JLabel[0];
                name = new JLabel[0];
            }
        }
        setLayout();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getFullName() {
        return null;
    }

    public String[] getVariables() {
        return variables;
    }

    protected void setLayout() {
        if (getComponentCount() != 0) {
            setLayout(new SpringLayout());
            SpringUtilities.makeCompactGrid(this, variables.length, 2, 0, 0, 6,
                    0, true);
        }
        else {
            setLayout(null);
        }
        Dimension size = new Dimension(300, 25 * variables.length);
        setPreferredSize(size);
        setSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        size = null;
        revalidate();
        repaint();
    }

    public void setVariables(String[] variables) {
        if (variables == null) this.variables = new String[0];
        else {
            this.variables = new String[variables.length];
            for (int i = 0; i < variables.length; i++) {
                this.variables[i] = new String(variables[i]);
            }
        }
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
