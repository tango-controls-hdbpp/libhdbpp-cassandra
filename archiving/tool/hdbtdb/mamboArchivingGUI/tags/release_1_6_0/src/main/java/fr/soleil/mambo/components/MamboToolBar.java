//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/MamboToolBar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboToolBar.
//						(GIRARDOT Raphael) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: MamboToolBar.java,v $
// Revision 1.8  2006/11/06 09:28:05  ounsy
// icons reorganization
//
// Revision 1.7  2006/07/28 10:07:12  ounsy
// icons moved to "icons" package
//
// Revision 1.6  2006/05/19 13:45:13  ounsy
// minor changes
//
// Revision 1.5  2006/05/16 12:49:41  ounsy
// modified imports
//
// Revision 1.4  2006/04/05 13:39:09  ounsy
// minor changes
//
// Revision 1.3  2006/03/29 10:27:33  ounsy
// corected so that the tree/table selection works in the menu/tool bar too and not only in the bottom buttons
//
// Revision 1.2  2006/02/27 09:34:58  ounsy
// new launch parameter for user rights (1=VC_ONLY, 2=ALL)
//
// Revision 1.1  2005/12/15 10:56:47  ounsy
// "MamboToolbar" renamed as "MamboToolBar"
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.components;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.PrintAction;
import fr.soleil.mambo.actions.ResetAction;
import fr.soleil.mambo.actions.SaveAllToDiskAction;
import fr.soleil.mambo.actions.SaveToDiskAction;
import fr.soleil.mambo.actions.archiving.LoadACAction;
import fr.soleil.mambo.actions.archiving.OpenACEditDialogAction;
import fr.soleil.mambo.actions.view.LoadVCAction;
import fr.soleil.mambo.actions.view.OpenVCEditDialogNewAction;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;

public class MamboToolBar extends JMenuBar
{

    private JMenu newMenu;
    private JMenu loadMenu;
    private JMenu saveMenu;
    private JMenuItem newACItem;
    private JMenuItem newVCItem;
    private JMenuItem loadACItem;
    private JMenuItem loadVCItem;
    private JMenuItem saveACItem;
    private JMenuItem saveVCItem;
    private JMenu printMenu;
    private JMenuItem printACItem;
    private JMenuItem printVCItem;
    private JButton saveAllButton;
    private JButton resetButton;
    private PrintAction printACAction;
    private PrintAction printVCAction;
    private ResetAction resetAction;
    private LoadACAction loadACAction;
    private LoadVCAction loadVCAction;
    private SaveToDiskAction saveACAction;
    private SaveToDiskAction saveVCAction;
    private SaveAllToDiskAction saveAllAction;

    private final static String newActionName = Messages.getMessage( "TOOLBAR_NEW" );
    private final static String loadActionName = Messages.getMessage( "TOOLBAR_LOAD" );
    private final static String ACActionName = Messages.getMessage( "TOOLBAR_ARCHIVING_CONFIGURATION" );
    private final static String VCActionName = Messages.getMessage( "TOOLBAR_VIEW_CONFIGURATION" );
    private final static String saveToDiskActionName = Messages.getMessage( "TOOLBAR_SAVE_TO_DISK" );
    private final static String saveAllToDiskActionName = Messages.getMessage( "TOOLBAR_SAVE_ALL_TO_DISK" );
    private final static String printActionName = Messages.getMessage( "TOOLBAR_PRINT" );
    private final static String resetActionName = Messages.getMessage( "TOOLBAR_RESET" );

    private final static ImageIcon newActionIcon = new ImageIcon( Mambo.class.getResource( "icons/New.gif" ) );
    private final static ImageIcon loadActionIcon = new ImageIcon( Mambo.class.getResource( "icons/Open.gif" ) );
    private final static ImageIcon saveToDiskActionIcon = new ImageIcon( Mambo.class.getResource( "icons/Save.gif" ) );
    private final static ImageIcon saveAllToDiskActionIcon = new ImageIcon( Mambo.class.getResource( "icons/Quick_Save_All.gif" ) );
    private final static ImageIcon printActionIcon = new ImageIcon( Mambo.class.getResource( "icons/Print.gif" ) );
    private final static ImageIcon resetActionIcon = new ImageIcon( Mambo.class.getResource( "icons/Trash_-_Reset.gif" ) );

    private OpenACEditDialogAction newActionAC;

    private static MamboToolBar instance;

    public static MamboToolBar getInstance ()
    {
        if ( instance == null )
        {
            instance = new MamboToolBar();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.TOOLBAR_COLOR );
        }
        return instance;
    }

    public static MamboToolBar getCurrentInstance ()
    {
        return instance;
    }

    private MamboToolBar ()
    {
        newMenu = new ToolBarMenu(newActionName, newActionIcon);
        GUIUtilities.setObjectBackground( newMenu , GUIUtilities.TOOLBAR_COLOR );

        Options options = Options.getInstance();
        ACOptions acOptions = options.getAcOptions();
        newActionAC = new OpenACEditDialogAction( ACActionName , true , acOptions.isAlternateSelectionMode() );
        OpenVCEditDialogNewAction newActionVC = OpenVCEditDialogNewAction.getInstance( VCActionName );

        newACItem = new JMenuItem( newActionAC );
        GUIUtilities.setObjectBackground( newACItem , GUIUtilities.TOOLBAR_COLOR );

        newVCItem = new JMenuItem( newActionVC );
        newVCItem.setText(
                Messages.getMessage("ACTION_NEW_VC")
        );
        GUIUtilities.setObjectBackground( newVCItem , GUIUtilities.TOOLBAR_COLOR );

        add( newMenu );

        if ( Mambo.hasACs () )
        {
            newMenu.add( newACItem );
        }
        newMenu.add( newVCItem );

        loadMenu = new ToolBarMenu(loadActionName, loadActionIcon);
        GUIUtilities.setObjectBackground( loadMenu , GUIUtilities.TOOLBAR_COLOR );

        loadACItem = new JMenuItem( ACActionName );
        loadACAction = new LoadACAction( ACActionName , false );
        loadACItem.addActionListener( loadACAction );
        GUIUtilities.setObjectBackground( loadACItem , GUIUtilities.TOOLBAR_COLOR );

        loadVCItem = new JMenuItem( VCActionName );
        loadVCAction = new LoadVCAction( VCActionName , false );
        loadVCItem.addActionListener( loadVCAction );
        GUIUtilities.setObjectBackground( loadVCItem , GUIUtilities.TOOLBAR_COLOR );

        if ( Mambo.hasACs () )
        {
            loadMenu.add( loadACItem );
        }
        loadMenu.add( loadVCItem );

        add( loadMenu );

        saveMenu = new ToolBarMenu(saveToDiskActionName, saveToDiskActionIcon);
        GUIUtilities.setObjectBackground( saveMenu , GUIUtilities.TOOLBAR_COLOR );

        saveACItem = new JMenuItem( ACActionName );
        saveACAction = new SaveToDiskAction( ACActionName , saveToDiskActionIcon , SaveToDiskAction.AC_TYPE );
        saveACItem.addActionListener( saveACAction );
        GUIUtilities.setObjectBackground( saveACItem , GUIUtilities.TOOLBAR_COLOR );

        saveVCItem = new JMenuItem( VCActionName );
        saveVCAction = new SaveToDiskAction( VCActionName , saveToDiskActionIcon , SaveToDiskAction.VC_TYPE );
        saveVCItem.addActionListener( saveVCAction );
        GUIUtilities.setObjectBackground( saveVCItem , GUIUtilities.TOOLBAR_COLOR );

        if ( Mambo.hasACs () )
        {
            saveMenu.add( saveACItem );
        }
        saveMenu.add( saveVCItem );

        add( saveMenu );

        saveAllAction = new SaveAllToDiskAction( saveAllToDiskActionName , saveAllToDiskActionIcon , SaveAllToDiskAction.BOTH_TYPE );
        saveAllButton = new ToolBarButton( saveAllAction, true );
        GUIUtilities.setObjectBackground( saveAllButton , GUIUtilities.TOOLBAR_COLOR );
        add( saveAllButton );

        printMenu = new ToolBarMenu(printActionName, printActionIcon);
        GUIUtilities.setObjectBackground( printMenu , GUIUtilities.TOOLBAR_COLOR );

        printACItem = new JMenuItem( ACActionName );
        printACAction = new PrintAction( ACActionName , printActionIcon , PrintAction.AC_TYPE );
        printACItem.addActionListener( printACAction );
        GUIUtilities.setObjectBackground( printACItem , GUIUtilities.TOOLBAR_COLOR );

        printVCItem = new JMenuItem( VCActionName );
        printVCAction = new PrintAction( VCActionName , printActionIcon , PrintAction.VC_TYPE );
        printVCItem.addActionListener( printVCAction );
        GUIUtilities.setObjectBackground( printVCItem , GUIUtilities.TOOLBAR_COLOR );

        if ( Mambo.hasACs () )
        {
            printMenu.add( printACItem );
        }
        printMenu.add( printVCItem );

        add( printMenu );

        resetAction = new ResetAction( resetActionName , resetActionIcon );
        resetButton = new ToolBarButton( resetAction, true );
        GUIUtilities.setObjectBackground( resetButton , GUIUtilities.TOOLBAR_COLOR );
        add( resetButton );

    }

    public void setAlternateSelection ( boolean isAlternate )
    {
        newActionAC.setAlternateSelectionMode ( isAlternate );
    }

    /**
     * A toolbar item, whether it directly calls an action or not.
     *
     * @author CLAISSE
     */
    private class ToolBarButton extends JButton
    {
        /**
         * @param a           The action to put on the button
         * @param doSomething True if the action has to be called upon button press; false if the button has sub-menus
         */
        public ToolBarButton ( Action a , boolean doSomething )
        {
            super( ( Icon ) a.getValue( Action.SMALL_ICON ) );

            String toolTip = ( String ) a.getValue( Action.SHORT_DESCRIPTION );
            if ( toolTip == null )
            {
                toolTip = ( String ) a.getValue( Action.NAME );
            }

            if ( toolTip != null )
            {
                setToolTipText( toolTip );
            }

            if ( doSomething )
            {
                addActionListener( a );
            }
            addMouseListener( new ToolBarMouseListener() );

            setMargin( new Insets( 0 , 3 , 0 , 3 ) );
            setBorderPainted( false );
            setFocusable( false );
        }
    }

    /**
     * A toolbar item, whether it directly calls an action or not.
     *
     * @author CLAISSE
     */
    private class ToolBarMenu extends JMenu
    {
        /**
         * @param a           The action to put on the button
         * @param doSomething True if the action has to be called upon button press; false if the button has sub-menus
         */
        public ToolBarMenu ( String text , ImageIcon icon )
        {
            super();

            setToolTipText( text );
            setIcon( icon );

            addMouseListener( new ToolBarMouseListener() );
            setFocusable( false );

            setMargin( new Insets( 0 , 1 , 0 , 1 ) );
            setBorderPainted( false );
            repaint();
        }
    }

    private class ToolBarMouseListener extends MouseAdapter
    {
        public ToolBarMouseListener ()
        {

        }

        @Override
		public void mouseEntered ( MouseEvent e )
        {
            if ( e.getSource() instanceof AbstractButton )
            {
                ( ( AbstractButton ) e.getSource() ).setBackground( Color.darkGray );
            }
        }

        @Override
		public void mouseExited ( MouseEvent e )
        {
            if ( e.getSource() instanceof AbstractButton )
            {
                ( ( AbstractButton ) e.getSource() ).setBackground( GUIUtilities.getToolBarColor() );
            }
        }
    }

}
