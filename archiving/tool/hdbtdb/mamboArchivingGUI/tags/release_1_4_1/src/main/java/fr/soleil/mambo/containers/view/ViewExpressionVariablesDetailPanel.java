package fr.soleil.mambo.containers.view;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

import fr.soleil.mambo.containers.MamboCleanablePanel;
import fr.soleil.mambo.tools.SpringUtilities;

public class ViewExpressionVariablesDetailPanel extends MamboCleanablePanel
{
    private static ViewExpressionVariablesDetailPanel instance = null;
    protected String[] variables;
    protected JLabel[] title, name;
    protected boolean x;

    public static ViewExpressionVariablesDetailPanel getInstance ()
    {
        if (instance == null)
        {
            instance = new ViewExpressionVariablesDetailPanel();
        }
        return instance;
    }

    public ViewExpressionVariablesDetailPanel ()
    {
        super();
    }

    public void clean ()
    {
        removeAll();
        this.setLayout(null);
        if (variables != null)
        {
            for (int i = 0; i < variables.length; i++)
            {
                variables[i] = null;
                title[i] = null;
                name[i] = null;
            }
            variables = null;
            name = null;
            title = null;
        }
    }

    public void loadPanel ()
    {
        this.removeAll();
        if (variables == null) variables = new String[0];
        if (variables.length > 0 && !x)
        {
            title = new JLabel[variables.length];
            name = new JLabel[variables.length];
            for (int i = 0; i < variables.length; i++)
            {
                title[i] = new JLabel("x" + (i+1) + ":");
                title[i].setMaximumSize(title[i].getPreferredSize());
                this.add(title[i]);
                name[i] = new JLabel(variables[i]);
                this.add(name[i]);
            }
        }
        else
        {
            if (variables.length == 1)
            {
                title = new JLabel[1];
                name = new JLabel[1];
                title[0] = new JLabel("x:");
                title[0].setMaximumSize(title[0].getPreferredSize());
                this.add(title[0]);
                name[0] = new JLabel(variables[0]);
                this.add(name[0]);
            }
            else
            {
                title = new JLabel[0];
                name = new JLabel[0];
            }
        }
        this.setLayout();
    }

    public String getName ()
    {
        return null;
    }

    public String getFullName ()
    {
        return null;
    }

    public String[] getVariables ()
    {
        return variables;
    }

    protected void setLayout()
    {
        if (this.getComponentCount() != 0)
        {
            this.setLayout(new SpringLayout());
            SpringUtilities.makeCompactGrid(
                    this,
                    variables.length, 2,
                    0, 0,
                    0, 0,
                    true
            );
        }
        else
        {
            this.setLayout(null);
        }
        Dimension size = new Dimension(300, 25*variables.length);
        this.setPreferredSize( size );
        this.setSize( size );
        this.setMinimumSize( size );
        this.setMaximumSize( size );
        size = null;
        this.revalidate();
        this.repaint();
    }

    public void setVariables (String[] variables)
    {
        if (variables == null) this.variables = new String[0];
        else
        {
            this.variables = new String[variables.length];
            for (int i = 0; i < variables.length; i++)
            {
                this.variables[i] = new String(variables[i]);
            }
        }
    }

    public boolean isX ()
    {
        return x;
    }

    public void setX (boolean x)
    {
        this.x = x;
    }
}
