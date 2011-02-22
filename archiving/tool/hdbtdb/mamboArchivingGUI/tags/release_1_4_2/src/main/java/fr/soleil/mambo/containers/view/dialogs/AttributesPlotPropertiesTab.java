//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/AttributesPlotPropertiesTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesPlotPropertiesTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: AttributesPlotPropertiesTab.java,v $
// Revision 1.5  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.4  2006/10/02 14:13:25  ounsy
// minor changes (look and feel)
//
// Revision 1.3  2006/07/18 10:25:47  ounsy
// Less time consuming by setting tree expanding on demand only
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCAttributesPropertiesTreeExpandAllAction;
import fr.soleil.mambo.actions.view.VCAttributesPropertiesTreeExpandOneAction;
import fr.soleil.mambo.actions.view.VCAttributesPropertiesTreeExpandSelectedAction;
import fr.soleil.mambo.components.view.VCAttributesPropertiesTree;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;

public class AttributesPlotPropertiesTab extends JPanel
{
    private static AttributesPlotPropertiesTab instance = null;

    private VCAttributesPropertiesTree tree;
    private JScrollPane scrollPane;
    private JButton expandAllButton, expandSelectedButton, expandFirstButton;
    private JLabel warningLabel;
    private JPanel leftPanel, actionPanel;

    //private JPanel propertiesPanel;
    private AttributesPlotPropertiesPanel propertiesPanel;
    private final static ImageIcon expandAllIcon = new ImageIcon(Mambo.class.getResource("icons/expand_all.gif"));
    private final static ImageIcon expandSelectedIcon = new ImageIcon(Mambo.class.getResource("icons/expand_selected.gif"));
    private final static ImageIcon expandFirstIcon = new ImageIcon(Mambo.class.getResource("icons/expand.gif"));
    private final static ImageIcon warningIcon = new ImageIcon(Mambo.class.getResource("icons/warningBig.gif"));

    /**
     * @return 8 juil. 2005
     */
    public static AttributesPlotPropertiesTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new AttributesPlotPropertiesTab();
        }

        return instance;
    }

    /**
     * 
     */
    private AttributesPlotPropertiesTab ()
    {
        //this.add ( new JLabel ( "AttributesPropertiesTab" ) );
        this.initComponents();
        this.addComponents();
        this.initLayout();
    }

    /**
     * 19 juil. 2005
     */
    private void initLayout ()
    {
        actionPanel.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid(
                actionPanel,
                actionPanel.getComponentCount(), 1,
                0, 0,
                0, 5,
                true
        );
        leftPanel.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid(
                leftPanel,
                leftPanel.getComponentCount(), 1,
                0, 0,
                0, 0,
                true
        );
        this.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid(
                this,
                1, this.getComponentCount(),
                0, 0,
                0, 0,
                true
        );
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        actionPanel.add( warningLabel );
        actionPanel.add( expandAllButton );
        actionPanel.add( expandSelectedButton );
        actionPanel.add( expandFirstButton );
        leftPanel.add( actionPanel );
        leftPanel.add( scrollPane );
        this.add(leftPanel);
        this.add( propertiesPanel );
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        VCAttributesTreeModel model = VCAttributesTreeModel.getInstance();
        tree = VCAttributesPropertiesTree.getInstance( model );

        expandAllButton = new JButton(new VCAttributesPropertiesTreeExpandAllAction());
        expandAllButton.setIcon(expandAllIcon);

        expandFirstButton = new JButton(new VCAttributesPropertiesTreeExpandOneAction());
        expandFirstButton.setIcon(expandFirstIcon);

        expandSelectedButton = new JButton(new VCAttributesPropertiesTreeExpandSelectedAction());
        expandSelectedButton.setIcon(expandSelectedIcon);

        warningLabel = new JLabel(Messages.getMessage("VIEW_ACTION_EXPAND_WARNING"), JLabel.CENTER);
        warningLabel.setIcon(warningIcon);

        scrollPane = new JScrollPane( tree );
        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setMinimumSize( new Dimension( 100 , 50 ) );
        scrollPane.setPreferredSize( new Dimension( 200 , 50 ) );
        scrollPane.setMaximumSize( new Dimension( Integer.MAX_VALUE , 700 ) );
        leftPanel = new JPanel();
        actionPanel = new JPanel();
        actionPanel.setBorder(
                GUIUtilities.getPlotSubPanelsLineBorder(
                        Messages.getMessage("VIEW_ACTION_BORDER"), 
                        Color.BLACK,
                        Color.BLACK
                )
        );

        propertiesPanel = new AttributesPlotPropertiesPanel(false);

    }

    public AttributesPlotPropertiesPanel getPropertiesPanel ()
    {
        return propertiesPanel;
    }
}
