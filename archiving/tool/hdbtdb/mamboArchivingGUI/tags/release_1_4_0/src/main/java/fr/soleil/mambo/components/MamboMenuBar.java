//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/MamboMenuBar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboMenuBar.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: MamboMenuBar.java,v $
// Revision 1.8  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.7  2006/05/19 13:45:13  ounsy
// minor changes
//
// Revision 1.6  2006/05/16 12:49:41  ounsy
// modified imports
//
// Revision 1.5  2006/03/29 10:27:33  ounsy
// corected so that the tree/table selection works in the menu/tool bar too and not only in the bottom buttons
//
// Revision 1.4  2006/02/27 09:34:58  ounsy
// new launch parameter for user rights (1=VC_ONLY, 2=ALL)
//
// Revision 1.3  2006/01/23 09:01:10  ounsy
// Avoiding multi click on "start" button
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
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
package fr.soleil.mambo.components;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.ExitAction;
import fr.soleil.mambo.actions.OpenAboutAction;
import fr.soleil.mambo.actions.OpenContentsAction;
import fr.soleil.mambo.actions.OpenOptionsAction;
import fr.soleil.mambo.actions.OpenTipsAction;
import fr.soleil.mambo.actions.PrintAction;
import fr.soleil.mambo.actions.SaveAllToDiskAction;
import fr.soleil.mambo.actions.archiving.ACRecapAction;
import fr.soleil.mambo.actions.archiving.ArchivingStopAction;
import fr.soleil.mambo.actions.archiving.ArchivingTransferAction;
import fr.soleil.mambo.actions.archiving.LoadACAction;
import fr.soleil.mambo.actions.archiving.OpenACEditDialogAction;
import fr.soleil.mambo.actions.archiving.SaveSelectedACAction;
import fr.soleil.mambo.actions.listeners.ArchivingStartListener;
import fr.soleil.mambo.actions.view.LoadVCAction;
import fr.soleil.mambo.actions.view.OpenVCEditDialogAction;
import fr.soleil.mambo.actions.view.SaveSelectedVCAction;
import fr.soleil.mambo.actions.view.VCVariationAction;
import fr.soleil.mambo.actions.view.VCViewAction;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.tools.Messages;



public class MamboMenuBar extends JMenuBar
{
    private static MamboMenuBar instance = null;
    
    private OpenACEditDialogAction editACAction;
    private OpenACEditDialogAction newACAction1;
    private OpenACEditDialogAction newACAction2;
    

    /**
     * @return 8 juil. 2005
     */
    public static MamboMenuBar getInstance ()
    {
        if ( instance == null )
        {
            instance = new MamboMenuBar();
        }

        return instance;
    }

    public static MamboMenuBar getCurrentInstance ()
    {
        return instance;
    }
    
    /**
     * 
     */
    private MamboMenuBar ()
    {
        String fileLabel = Messages.getMessage( "MENU_FILE" );
        //String favoritesLabel = Messages.getMessage( "MENU_FAVORITES" );
        String toolsLabel = Messages.getMessage( "MENU_TOOLS" );
        String helpLabel = Messages.getMessage( "MENU_HELP" );
        String newLabel = Messages.getMessage( "MENU_NEW" );

        String saveLabel = Messages.getMessage( "MENU_SAVE" );
        String loadLabel = Messages.getMessage( "MENU_LOAD" );
        String saveAllLabel = Messages.getMessage( "MENU_SAVE_ALL" );
        //String importLabel = Messages.getMessage( "MENU_IMPORT" );
        //String exportLabel = Messages.getMessage( "MENU_EXPORT" );
        //String recentLabel = Messages.getMessage( "MENU_RECENT" );
        //String configurationLabel = Messages.getMessage( "MENU_CONFIGURATION" );
        //String openLabel = Messages.getMessage( "MENU_OPEN" );
        String exitLabel = Messages.getMessage( "MENU_EXIT" );

        String saveAsLabel = Messages.getMessage( "MENU_SAVE_AS" );
        //String configurationsLabel = Messages.getMessage( "MENU_CONFIGURATIONS" );
        String optionsLabel = Messages.getMessage( "MENU_OPTIONS" );
        String contentsLabel = Messages.getMessage( "MENU_CONTENTS" );
        String tipsLabel = Messages.getMessage( "MENU_TIPS" );
        String aboutLabel = Messages.getMessage( "MENU_ABOUT" );

        String archivingConfigurationLabel = Messages.getMessage( "MENU_ARCHIVING_CONFIGURATION" );
        String viewConfigurationLabel = Messages.getMessage( "MENU_VIEW_CONFIGURATION" );
        String ACLabel = Messages.getMessage( "MENU_AC" );
        String VCLabel = Messages.getMessage( "MENU_VC" );

        String printLabel = Messages.getMessage( "MENU_PRINT" );

        String msg = Messages.getMessage( "ARCHIVING_ACTION_STOP_BUTTON" );
        ArchivingStopAction archivingStopAction = new ArchivingStopAction( msg );

        msg = Messages.getMessage( "ARCHIVING_ACTION_MODIFY_BUTTON" );
        Options options = Options.getInstance();
        options.push ();
        
        ACOptions acOptions = options.getAcOptions();
        editACAction = new OpenACEditDialogAction( msg , false , acOptions.isAlternateSelectionMode() );

        msg = archivingConfigurationLabel;
        newACAction1 = new OpenACEditDialogAction( msg , true , acOptions.isAlternateSelectionMode() );

        msg = Messages.getMessage( "ARCHIVING_ACTION_NEW_BUTTON" );
        newACAction2 = new OpenACEditDialogAction( msg , true , acOptions.isAlternateSelectionMode() );

        msg = Messages.getMessage( "ARCHIVING_ACTION_TRANSFER_BUTTON" );
        ArchivingTransferAction tranferAction = new ArchivingTransferAction( msg );

        msg = Messages.getMessage( "ARCHIVING_ASSESSMENT_TITLE" );
        ACRecapAction recapAction = new ACRecapAction( msg );

        msg = Messages.getMessage( "VIEW_ACTION_VIEW_BUTTON" );
        VCViewAction viewAction = new VCViewAction( msg );

        msg = Messages.getMessage( "VIEW_ACTION_VARIATION_BUTTON" );
        VCVariationAction variationAction = new VCVariationAction( msg );

        msg = Messages.getMessage( "VIEW_ACTION_MODIFY_BUTTON" );
        OpenVCEditDialogAction editVCAction = new OpenVCEditDialogAction( msg , false );


        msg = viewConfigurationLabel;
        OpenVCEditDialogAction newVCAction1 = new OpenVCEditDialogAction( msg , true );

        msg = Messages.getMessage( "VIEW_ACTION_NEW_BUTTON" );
        OpenVCEditDialogAction newVCAction2 = new OpenVCEditDialogAction( msg , true );
        //--

        //BEGIN Top level menus
        JMenu fileMenu = new JMenu( fileLabel );
        JMenu acMenu = new JMenu( ACLabel );
        JMenu vcMenu = new JMenu( VCLabel );
        JMenu toolsMenu = new JMenu( toolsLabel );
        JMenu helpMenu = new JMenu( helpLabel );
        //END Top level menus

        //BEGIN first order sub-menus

        //BEGIN File Menu
        JMenu fileMenu_new = new JMenu( newLabel );
        
        JMenuItem fileMenu_new_AC = new JMenuItem( newACAction1 );
        if ( Mambo.hasACs () )
        {
            fileMenu_new.add( fileMenu_new_AC );
        }

        JMenuItem fileMenu_new_VC = new JMenuItem( newVCAction1 );
        fileMenu_new.add( fileMenu_new_VC );
        JMenu fileMenu_save = new JMenu( saveLabel );
        JMenuItem fileMenu_save_ac = new JMenuItem( new SaveSelectedACAction( archivingConfigurationLabel , false ) );
        if ( Mambo.hasACs () )
        {
            fileMenu_save.add( fileMenu_save_ac );
        }

        JMenuItem fileMenu_save_vc = new JMenuItem( new SaveSelectedVCAction( viewConfigurationLabel , false ) );
        fileMenu_save.add( fileMenu_save_vc );
        //--
        JMenu fileMenu_saveAs = new JMenu( saveAsLabel );
        JMenuItem fileMenu_saveAs_ac = new JMenuItem( new SaveSelectedACAction( archivingConfigurationLabel , true ) );
        if ( Mambo.hasACs () )
        {
            fileMenu_saveAs.add( fileMenu_saveAs_ac );
        }
        
        JMenuItem fileMenu_saveAs_vc = new JMenuItem( new SaveSelectedVCAction( viewConfigurationLabel , true ) );
        fileMenu_saveAs.add( fileMenu_saveAs_vc );
        //---
        JMenu fileMenu_load = new JMenu( loadLabel );
        JMenuItem fileMenu_load_ac = new JMenuItem( new LoadACAction( archivingConfigurationLabel , false ) );
        if ( Mambo.hasACs () )
        {
            fileMenu_load.add( fileMenu_load_ac );
        }
        
        JMenuItem fileMenu_load_vc = new JMenuItem( new LoadVCAction( viewConfigurationLabel , false ) );
        fileMenu_load.add( fileMenu_load_vc );
        
        JMenu fileMenu_print = new JMenu( printLabel );
        JMenuItem fileMenu_print_ac = new JMenuItem( new PrintAction( archivingConfigurationLabel , null , PrintAction.AC_TYPE ) );
        if ( Mambo.hasACs () )
        {
            fileMenu_print.add( fileMenu_print_ac );
        }
        
        JMenuItem fileMenu_print_vc = new JMenuItem( new PrintAction( viewConfigurationLabel , null , PrintAction.VC_TYPE ) );
        fileMenu_print.add( fileMenu_print_vc );
        
        SaveAllToDiskAction saveAllToDiskAction = new SaveAllToDiskAction( saveAllLabel , null , SaveAllToDiskAction.BOTH_TYPE );
        JMenuItem fileMenu_saveAll = new JMenuItem( saveAllToDiskAction );
        //JMenu fileMenu_import = new JMenu( importLabel );
        //JMenu fileMenu_export = new JMenu( exportLabel );
        //JMenu fileMenu_recent = new JMenu( recentLabel );

        ExitAction exitAction = new ExitAction( exitLabel );
        JMenuItem fileMenu_exit = new JMenuItem( exitAction );

        fileMenu.add( fileMenu_new );
        fileMenu.add( fileMenu_save );
        fileMenu.add( fileMenu_saveAs );
        fileMenu.add( fileMenu_saveAll );
        fileMenu.add( fileMenu_load );
        fileMenu.add( fileMenu_print );
        fileMenu.addSeparator();
        fileMenu.add( fileMenu_exit );
        //END File Menu

        //BEGIN AC Menu
        //JMenuItem acMenu_new = new JMenuItem ( new NewAction ( newLabel , null , NewAction.AC_TYPE ) );
        JMenuItem acMenu_save = new JMenuItem( new SaveSelectedACAction( saveLabel , false ) );
        JMenuItem acMenu_saveAs = new JMenuItem( new SaveSelectedACAction( saveAsLabel , true ) );
        JMenuItem acMenu_saveAll = new JMenuItem( new SaveAllToDiskAction( saveAllLabel , null , SaveAllToDiskAction.AC_TYPE ) );
        JMenuItem acMenu_load = new JMenuItem( new LoadACAction( loadLabel , false ) );
        //---



        //JMenuItem acMenu_start = new JMenuItem( archivingStartAction );
        JMenuItem acMenu_start = new JMenuItem(Messages.getMessage( "ARCHIVING_ACTION_START_BUTTON" ));
        acMenu_start.addMouseListener(new ArchivingStartListener());
        JMenuItem acMenu_stop = new JMenuItem( archivingStopAction );
        JMenuItem acMenu_modify = new JMenuItem( editACAction );
        JMenuItem acMenu_new = new JMenuItem( newACAction2 );
        JMenuItem acMenu_transfer = new JMenuItem( tranferAction );
        JMenuItem acMenu_assessment = new JMenuItem( recapAction );

        //acMenu.add( acMenu_new );
        acMenu.add( acMenu_save );
        acMenu.add( acMenu_saveAs );
        acMenu.add( acMenu_saveAll );
        acMenu.add( acMenu_load );
        acMenu.addSeparator();
        acMenu.add( acMenu_new );
        acMenu.add( acMenu_modify );
        acMenu.add( acMenu_start );
        acMenu.add( acMenu_stop );
        acMenu.add( acMenu_transfer );
        acMenu.add( acMenu_assessment );
        //END AC Menu

        //BEGIN VC Menu
        //JMenuItem vcMenu_new = new JMenuItem ( new NewAction ( newLabel , null , NewAction.VC_TYPE ) );
        JMenuItem vcMenu_save = new JMenuItem( new SaveSelectedVCAction( saveLabel , false ) );
        JMenuItem vcMenu_saveAs = new JMenuItem( new SaveSelectedVCAction( saveAsLabel , true ) );
        JMenuItem vcMenu_saveAll = new JMenuItem( new SaveAllToDiskAction( saveAllLabel , null , SaveAllToDiskAction.VC_TYPE ) );
        JMenuItem vcMenu_load = new JMenuItem( new LoadVCAction( loadLabel , false ) );
        //JMenuItem vcMenu_view = new JMenuItem ( new LoadVCAction ( loadLabel , false ) );


        //vcMenu.add( vcMenu_new );
        vcMenu.add( vcMenu_save );
        vcMenu.add( vcMenu_saveAs );
        vcMenu.add( vcMenu_saveAll );
        vcMenu.add( vcMenu_load );
        vcMenu.addSeparator();
        vcMenu.add( newVCAction2 );
        vcMenu.add( editVCAction );
        vcMenu.add( viewAction );
        vcMenu.add( variationAction );
        //END VC Menu

        //BEGIN Options Menu
        OpenOptionsAction optionAction = new OpenOptionsAction( optionsLabel );
        toolsMenu.add( optionAction );

        //END Options Menu

        //BEGIN Help Menu
        OpenContentsAction openContentsAction = new OpenContentsAction( contentsLabel );
        JMenuItem help_contents = new JMenuItem( openContentsAction );
        OpenTipsAction openTipsAction = new OpenTipsAction( tipsLabel );
        JMenuItem help_tips = new JMenuItem( openTipsAction );
        OpenAboutAction openAboutAction = new OpenAboutAction( aboutLabel );
        JMenuItem help_about = new JMenuItem( openAboutAction );

        helpMenu.add( help_contents );
        helpMenu.add( help_tips );
        helpMenu.addSeparator();
        helpMenu.add( help_about );
        //BEGIN Help Menu

        this.add( fileMenu );
        if ( Mambo.hasACs () )
        {
            this.add( acMenu );    
        }
        
        this.add( vcMenu );
        this.add( toolsMenu );
        this.add( helpMenu );
        //END first order sub-menus
    }

    public void setAlternateSelection ( boolean isAlternate )
    {
        this.editACAction.setAlternateSelectionMode ( isAlternate );
        this.newACAction1.setAlternateSelectionMode ( isAlternate );
        this.newACAction2.setAlternateSelectionMode ( isAlternate );
    }
}
