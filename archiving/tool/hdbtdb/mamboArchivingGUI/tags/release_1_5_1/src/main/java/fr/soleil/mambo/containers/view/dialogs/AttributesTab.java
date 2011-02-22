//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/AttributesTab.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesTab.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: AttributesTab.java,v $
// Revision 1.10  2007/01/11 14:05:47  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.9  2006/11/06 09:28:05  ounsy
// icons reorganization
//
// Revision 1.8  2006/10/02 14:13:25  ounsy
// minor changes (look and feel)
//
// Revision 1.7  2006/07/28 10:07:12  ounsy
// icons moved to "icons" package
//
// Revision 1.6  2006/07/18 10:26:10  ounsy
// Less time consuming by setting tree expanding on demand only
//
// Revision 1.5  2006/07/05 12:36:38  ounsy
// minor changes
//
// Revision 1.4  2006/06/28 12:28:51  ounsy
// do not expand VCPossibleAttributesTree directly : click on a button instead to do so
//
// Revision 1.3  2006/02/24 12:20:05  ounsy
// small modifications
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
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.AddSelectedVCAttributesAction;
import fr.soleil.mambo.actions.view.MatchPossibleVCAttributesAction;
import fr.soleil.mambo.actions.view.MatchVCAttributesAction;
import fr.soleil.mambo.actions.view.RemoveSelectedVCAttributesAction;
import fr.soleil.mambo.actions.view.VCAttributesSelectTreeExpandAllAction;
import fr.soleil.mambo.actions.view.VCAttributesSelectTreeExpandOneAction;
import fr.soleil.mambo.actions.view.VCAttributesSelectTreeExpandSelectedAction;
import fr.soleil.mambo.actions.view.VCPossibleAttributesTreeExpandAllAction;
import fr.soleil.mambo.actions.view.VCPossibleAttributesTreeExpandOneAction;
import fr.soleil.mambo.actions.view.VCPossibleAttributesTreeExpandSelectedAction;
import fr.soleil.mambo.actions.view.VCRefreshPossibleAttributesAction;
import fr.soleil.mambo.components.TreeBox;
import fr.soleil.mambo.components.view.VCAttributesSelectTree;
import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.models.VCPossibleAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class AttributesTab extends JPanel
{
    private static AttributesTab instance = null;
    private final static ImageIcon refreshLeftIcon = new ImageIcon(Mambo.class.getResource("icons/update.gif"));
    private final static ImageIcon expandAllLeftIcon = new ImageIcon(Mambo.class.getResource("icons/expand_all.gif"));
    private final static ImageIcon expandSelectedLeftIcon = new ImageIcon(Mambo.class.getResource("icons/expand_selected.gif"));
    private final static ImageIcon expandFirstLeftIcon = new ImageIcon(Mambo.class.getResource("icons/expand.gif"));
    private final static ImageIcon expandAllRightIcon = new ImageIcon(Mambo.class.getResource("icons/expand_all.gif"));
    private final static ImageIcon expandSelectedRightIcon = new ImageIcon(Mambo.class.getResource("icons/expand_selected.gif"));
    private final static ImageIcon expandFirstRightIcon = new ImageIcon(Mambo.class.getResource("icons/expand.gif"));
    private final static ImageIcon warningIcon1 = new ImageIcon(Mambo.class.getResource("icons/warningBig.gif"));
    private final static ImageIcon warningIcon2 = new ImageIcon(Mambo.class.getResource("icons/warningBig.gif"));

    private JButton refreshLeftButton, expandAllLeftButton, expandSelectedLeftButton, expandFirstLeftButton;
    private JButton expandAllRightButton, expandSelectedRightButton, expandFirstRightButton;
    private JButton addSelectedAttributesButton;
    private JButton removeSelectedAttributesButton;

    private JLabel warningLabel1, warningLabel2;

    private VCAttributesSelectTree rightTree;
    private TreeBox rightTreeBox;
    private TreeBox leftTreeBox;
    private JPanel transferBox;
    private JPanel treeActionPanel;

    private JPanel box;

    private JTextField rightRegexpField;
    private JTextField leftRegexpField;

    /**
     * @return 8 juil. 2005
     */
    public static AttributesTab getInstance ()
    {
        if ( instance == null )
        {
            instance = new AttributesTab();
        }

        return instance;
    }

    /**
     * 
     */
    private AttributesTab ()
    {
        //this.add ( new JLabel ( "AttributesTab" ) );
        this.initComponents();
        this.addComponents();
        this.initLayout();
    }

    /**
     * 19 juil. 2005
     */
    private void initLayout ()
    {
        this.setLayout( new SpringLayout() );
        SpringUtilities.makeCompactGrid(
                this,
                this.getComponentCount(), 1,
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
        this.add(treeActionPanel);
        this.add( Box.createVerticalStrut(5) );
        this.add( box );
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        initLeftTreeBox();
        initTransferBox();
        initRightTreeBox();
        initMainBox();
        initActionPanel();
    }

    private void initActionPanel()
    {
        treeActionPanel = new JPanel();

        refreshLeftButton = new JButton(VCRefreshPossibleAttributesAction.getInstance());
        refreshLeftButton.setIcon(refreshLeftIcon);

        expandAllLeftButton = new JButton(new VCPossibleAttributesTreeExpandAllAction());
        expandAllLeftButton.setIcon(expandAllLeftIcon);
        expandAllLeftButton.setMargin(new Insets(0,0,0,0));

        expandFirstLeftButton = new JButton(new VCPossibleAttributesTreeExpandOneAction());
        expandFirstLeftButton.setIcon(expandFirstLeftIcon);
        expandFirstLeftButton.setMargin(new Insets(0,0,0,0));

        expandSelectedLeftButton = new JButton(new VCPossibleAttributesTreeExpandSelectedAction());
        expandSelectedLeftButton.setIcon(expandSelectedLeftIcon);
        expandSelectedLeftButton.setMargin(new Insets(0,0,0,0));

        expandAllRightButton = new JButton(new VCAttributesSelectTreeExpandAllAction());
        expandAllRightButton.setIcon(expandAllRightIcon);
        expandAllRightButton.setMargin(new Insets(0,0,0,0));

        expandFirstRightButton = new JButton(new VCAttributesSelectTreeExpandOneAction());
        expandFirstRightButton.setIcon(expandFirstRightIcon);
        expandFirstRightButton.setMargin(new Insets(0,0,0,0));

        expandSelectedRightButton = new JButton(new VCAttributesSelectTreeExpandSelectedAction());
        expandSelectedRightButton.setIcon(expandSelectedRightIcon);
        expandSelectedRightButton.setMargin(new Insets(0,0,0,0));

        warningLabel1 = new JLabel(Messages.getMessage("VIEW_ACTION_EXPAND_WARNING"), JLabel.CENTER);
        warningLabel1.setIcon(warningIcon1);

        warningLabel2 = new JLabel(Messages.getMessage("VIEW_ACTION_EXPAND_WARNING"), JLabel.CENTER);
        warningLabel2.setIcon(warningIcon2);

        treeActionPanel.add(warningLabel1);
        treeActionPanel.add(Box.createHorizontalGlue());
        treeActionPanel.add(warningLabel2);

        treeActionPanel.add(refreshLeftButton);
        treeActionPanel.add(Box.createHorizontalGlue());
        treeActionPanel.add(Box.createHorizontalGlue());

        treeActionPanel.add(expandAllLeftButton);
        treeActionPanel.add(Box.createHorizontalGlue());
        treeActionPanel.add(expandAllRightButton);

        treeActionPanel.add(expandSelectedLeftButton);
        treeActionPanel.add(Box.createHorizontalGlue());
        treeActionPanel.add(expandSelectedRightButton);

        treeActionPanel.add(expandFirstLeftButton);
        treeActionPanel.add(Box.createHorizontalGlue());
        treeActionPanel.add(expandFirstRightButton);

        treeActionPanel.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(
                treeActionPanel,
                5, 3,
                30, 0,
                30, 0,
                true
        );
        treeActionPanel.setBorder(
                GUIUtilities.getPlotSubPanelsLineBorder(
                        Messages.getMessage("VIEW_ACTION_BORDER"), 
                        Color.BLACK,
                        Color.BLACK
                )
        );
    }

    /**
     * 19 juil. 2005
     */
    private void initMainBox ()
    {

        //box = new Box( BoxLayout.X_AXIS ); //box containing the 2 TreeBoxes
        box = new JPanel(); //panel containing the 2 TreeBoxes
        box.add( leftTreeBox );
        box.add( transferBox );
        box.add( rightTreeBox );
        box.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(
                box,
                1, box.getComponentCount(),
                0, 0,
                0, 0,
                true
        );
    }

    /**
     * 19 juil. 2005
     */
    private void initRightTreeBox ()
    {
        rightTree = VCAttributesSelectTree.getInstance( VCAttributesTreeModel.getInstance() );
        //rightTree.expandAll( true );

        rightRegexpField = new JTextField();
        String msg = Messages.getMessage( "ACTIONS_MATCH" );
        JButton rightSearchButton = new JButton( new MatchVCAttributesAction( msg ) );

        rightTreeBox = new TreeBox();
        rightTreeBox.build( rightTree , rightRegexpField , rightSearchButton );

    }

    /**
     * 19 juil. 2005
     */
    private void initTransferBox ()
    {
        //transferBox = new Box( BoxLayout.Y_AXIS );
        transferBox = new JPanel();

        ImageIcon plusIcon = new ImageIcon( Mambo.class.getResource( "icons/right.gif" ) );
        ImageIcon minusIcon = new ImageIcon( Mambo.class.getResource( "icons/remove.gif" ) );

        addSelectedAttributesButton = new JButton( plusIcon );
        String msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_ADD_SELECTED_ATTR" );
        addSelectedAttributesButton.addActionListener( new AddSelectedVCAttributesAction( msg ) );
        addSelectedAttributesButton.setMargin(new Insets(10,0,10,0));

        removeSelectedAttributesButton = new JButton( minusIcon );
        msg = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_REMOVE_SELECTED_ATTR" );
        removeSelectedAttributesButton.addActionListener( new RemoveSelectedVCAttributesAction( msg ) );
        removeSelectedAttributesButton.setMargin(new Insets(10,0,10,0));

        transferBox.add( Box.createVerticalGlue() );
        transferBox.add( addSelectedAttributesButton );
        transferBox.add( removeSelectedAttributesButton );
        transferBox.add( Box.createVerticalGlue() );
        transferBox.setLayout(new SpringLayout());
        SpringUtilities.makeCompactGrid(
                transferBox,
                transferBox.getComponentCount(), 1,
                0, 20,
                0, 20,
                true
        );
    }

    /**
     * 19 juil. 2005
     */
    private void initLeftTreeBox ()
    {
        VCPossibleAttributesTree leftTree = VCPossibleAttributesTree.getInstance( VCPossibleAttributesTreeModel.getInstance() );

        leftRegexpField = new JTextField();
        String msg = Messages.getMessage( "ACTIONS_MATCH" );
        //JButton leftSearchButton = new JButton ( new MatchPossibleAttributesAction ( msg ) );
        JButton leftSearchButton = new JButton( new MatchPossibleVCAttributesAction( msg ) );

        leftTreeBox = new TreeBox();
        leftTreeBox.build( leftTree , leftRegexpField , leftSearchButton );
    }

    /**
     * @param aPath 8 juil. 2005
     */
    public void makeVisible ( TreePath aPath )
    {
        rightTree.makeVisible( aPath );
    }

    /**
     * 8 juil. 2005
     */
    public void enableButtons ()
    {
        addSelectedAttributesButton.setEnabled ( true );
        removeSelectedAttributesButton.setEnabled ( true );
    }

    public TreeBox getRightTreeBox ()
    {
        return rightTreeBox;
    }

    /**
     * @return 11 juil. 2005
     */
    public TreeBox getLeftTreeBox ()
    {

        return leftTreeBox;
    }

    public String getRightRegexp ()
    {
        return rightRegexpField.getText();
    }

    public String getLeftRegexp ()
    {
        return leftRegexpField.getText();
    }
}
