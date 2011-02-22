//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/BensikinMenuBar.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BensikinMenuBar.
//						(Claisse Laurent) - 10 juin 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.9 $
//
// $Log: BensikinMenuBar.java,v $
// Revision 1.9  2007/06/14 15:33:36  pierrejoseph
// Mantis 5352 : mettre DB avant file dans le menu Load
//
// Revision 1.8  2006/11/29 09:57:25  ounsy
// minor changes
//
// Revision 1.7  2006/02/15 09:14:12  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:10:01  ounsy
// new Word-like file management menus
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.components;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.soleil.bensikin.actions.ExitAction;
import fr.soleil.bensikin.actions.OpenAboutAction;
import fr.soleil.bensikin.actions.OpenContentsAction;
import fr.soleil.bensikin.actions.OpenOptionsAction;
import fr.soleil.bensikin.actions.OpenTipsAction;
import fr.soleil.bensikin.actions.SaveAllToDiskAction;
import fr.soleil.bensikin.actions.context.LaunchSnapshotAction;
import fr.soleil.bensikin.actions.context.LoadContextAction;
import fr.soleil.bensikin.actions.context.NewContextAction;
import fr.soleil.bensikin.actions.context.OpenAddFavoriteContextAction;
import fr.soleil.bensikin.actions.context.OpenSearchContextsAction;
import fr.soleil.bensikin.actions.context.RegisterContextAction;
import fr.soleil.bensikin.actions.context.SaveSelectedContextAction;
import fr.soleil.bensikin.actions.snapshot.FilterSnapshotsAction;
import fr.soleil.bensikin.actions.snapshot.LoadSnapshotAction;
import fr.soleil.bensikin.actions.snapshot.SaveSelectedSnapshotAction;
import fr.soleil.bensikin.favorites.Favorites;
import fr.soleil.bensikin.tools.Messages;

/**
 * The application's JMenuBar; a singleton class.
 * 
 * @author CLAISSE
 */
public class BensikinMenuBar extends JMenuBar {
	private static BensikinMenuBar instance = null;

	// private JMenuItem favorites_contexts_add;
	// private JMenuItem favorites_snapshots;
	// private JMenuItem favorites_config;
	private JMenu favorites_contexts;
	private JMenu favoritesMenu;

	private JMenuItem contextsMenu_register;

	/**
	 * Instantiates itself if necessary, returns the instance.
	 * 
	 * @return The instance
	 */
	public static BensikinMenuBar getInstance() {
		if (instance == null) {
			instance = new BensikinMenuBar();
		}

		return instance;
	}

	/**
	 * Updates the text of the "register" menu item
	 */
	public void updateRegisterItem() {
		String msg = Messages.getMessage("CONTEXT_DETAIL_REGISTER_CONTEXT_AS");
		contextsMenu_register.setText(msg);
		contextsMenu_register.setToolTipText(msg);
		RegisterContextAction.getInstance().setEnabled(false);
	}

	/**
	 * Resets the text of the "register" menu item to the default text
	 */
	public void resetRegisterItem() {
		String msg = Messages.getMessage("CONTEXT_DETAIL_REGISTER_CONTEXT");
		contextsMenu_register.setText(msg);
		contextsMenu_register.setToolTipText(msg);
		RegisterContextAction.getInstance().setEnabled(false);
	}

	/**
	 * Builds the MenuBar
	 */
	private BensikinMenuBar() {
		// System.out.println ( "BensikinMenuBar/repere 1" );
		String fileLabel = Messages.getMessage("MENU_FILE");
		String contextsLabel = Messages.getMessage("MENU_CONTEXTS");
		String snapshotsLabel = Messages.getMessage("MENU_SNAPSHOTS");
		String favoritesLabel = Messages.getMessage("MENU_FAVORITES");
		String toolsLabel = Messages.getMessage("MENU_TOOLS");
		String helpLabel = Messages.getMessage("MENU_HELP");
		String newLabel = Messages.getMessage("MENU_NEW");
		String snapshotLabel = Messages.getMessage("MENU_SNAPSHOT");
		String contextLabel = Messages.getMessage("MENU_CONTEXT");
		String saveLabel = Messages.getMessage("MENU_SAVE");
		String saveAsLabel = Messages.getMessage("MENU_SAVE_AS");
		String saveAllLabel = Messages.getMessage("MENU_SAVE_ALL");
		String importLabel = Messages.getMessage("MENU_IMPORT");
		// String exportLabel = Messages.getMessage( "MENU_EXPORT" );
		String recentLabel = Messages.getMessage("MENU_RECENT");
		String configurationLabel = Messages.getMessage("MENU_CONFIGURATION");
		String openLabel = Messages.getMessage("MENU_OPEN");
		// String quickLoadLabel = Messages.getMessage( "MENU_QUICK_LOAD" );
		String dbLabel = Messages.getMessage("MENU_DB");
		String configurationsLabel = Messages.getMessage("MENU_CONFIGURATIONS");
		String optionsLabel = Messages.getMessage("MENU_OPTIONS");
		String contentsLabel = Messages.getMessage("MENU_CONTENTS");
		String tipsLabel = Messages.getMessage("MENU_TIPS");
		String aboutLabel = Messages.getMessage("MENU_ABOUT");
		// String launchSnapshotLabel = Messages.getMessage(
		// "MENU_LAUNCH_SNAPSHOT" );
		String addContextToFavoritesLabel = Messages
				.getMessage("MENU_FAVORITES_ADD_CONTEXT");
		String exitLabel = Messages.getMessage("MENU_EXIT");

		// BEGIN Top level menus
		JMenu fileMenu = new JMenu(fileLabel);
		JMenu contextsMenu = new JMenu(contextsLabel);
		JMenu snapshotsMenu = new JMenu(snapshotsLabel);
		snapshotsMenu.setEnabled(true);

		favoritesMenu = new JMenu(favoritesLabel);
		JMenu toolsMenu = new JMenu(toolsLabel);
		JMenu helpMenu = new JMenu(helpLabel);
		helpMenu.setEnabled(true);
		// END Top level menus

		// BEGIN first order sub-menus

		// BEGIN File Menu
		JMenu fileMenu_new = new JMenu(newLabel);
		JMenuItem fileMenu_new_context = new JMenuItem(new NewContextAction(
				"Context"));
		JMenuItem fileMenu_new_snapshot = new JMenuItem(snapshotLabel);
		fileMenu_new_snapshot.setEnabled(false);
		fileMenu_new.add(fileMenu_new_context);

		JMenu fileMenu_save = new JMenu(saveLabel);
		SaveSelectedContextAction saveSelectedContextAction = new SaveSelectedContextAction(
				contextLabel, false);
		SaveSelectedSnapshotAction saveSelectedSnapshotAction = new SaveSelectedSnapshotAction(
				snapshotLabel, null, false);
		JMenuItem fileMenu_save_context = new JMenuItem(
				saveSelectedContextAction);
		JMenuItem fileMenu_save_snapshot = new JMenuItem(
				saveSelectedSnapshotAction);

		JMenu fileMenu_saveAs = new JMenu(saveAsLabel);
		SaveSelectedContextAction saveAsSelectedContextAction = new SaveSelectedContextAction(
				contextLabel, true);
		SaveSelectedSnapshotAction saveAsSelectedSnapshotAction = new SaveSelectedSnapshotAction(
				snapshotLabel, null, true);
		JMenuItem fileMenu_saveAs_context = new JMenuItem(
				saveAsSelectedContextAction);
		JMenuItem fileMenu_saveAs_snapshot = new JMenuItem(
				saveAsSelectedSnapshotAction);

		fileMenu_save.add(fileMenu_save_context);
		fileMenu_save.add(fileMenu_save_snapshot);

		fileMenu_saveAs.add(fileMenu_saveAs_context);
		fileMenu_saveAs.add(fileMenu_saveAs_snapshot);

		JMenuItem fileMenu_saveAll = new JMenuItem(new SaveAllToDiskAction(
				saveAllLabel, null));

		JMenu fileMenu_import = new JMenu(importLabel);
		JMenuItem fileMenu_import_context = new JMenuItem(
				new LoadContextAction(contextLabel, false));
		JMenuItem fileMenu_import_snapshot = new JMenuItem(
				new LoadSnapshotAction(snapshotLabel, false));

		fileMenu_import.add(fileMenu_import_context);
		fileMenu_import.add(fileMenu_import_snapshot);

		JMenu fileMenu_recent = new JMenu(recentLabel);
		fileMenu_recent.setEnabled(false);
		JMenuItem fileMenu_recent_context = new JMenuItem(contextLabel);
		JMenuItem fileMenu_recent_snapshot = new JMenuItem(snapshotLabel);
		JMenuItem fileMenu_recent_configuration = new JMenuItem(
				configurationLabel);

		JMenuItem fileMenu_exit = new JMenuItem(new ExitAction(exitLabel));

		fileMenu_recent.add(fileMenu_recent_context);
		fileMenu_recent.add(fileMenu_recent_snapshot);
		fileMenu_recent.add(fileMenu_recent_configuration);
		fileMenu.add(fileMenu_new);
		fileMenu.add(fileMenu_save);
		fileMenu.add(fileMenu_saveAs);
		fileMenu.add(fileMenu_saveAll);
		fileMenu.add(fileMenu_import);
		fileMenu.addSeparator();
		fileMenu.add(fileMenu_exit);
		// END File Menu

		// BEGIN Contexts Menu

		JMenu contextsMenu_open = new JMenu(openLabel);
		LoadContextAction loadContextAction = new LoadContextAction(fileLabel,
				false);
		JMenuItem contextsMenu_open_file = new JMenuItem(loadContextAction);

		// LoadContextAction quickLoadContextAction = new LoadContextAction(
		// quickLoadLabel , true );
		// JMenuItem contextsMenu_quick_load = new JMenuItem(
		// quickLoadContextAction );

		contextsMenu_open_file.setEnabled(true);
		JMenuItem contextsMenu_open_db = new JMenuItem(
				new OpenSearchContextsAction("DB"));
		JMenuItem contextsMenu_open_recent = new JMenuItem(recentLabel);
		contextsMenu_open_recent.setEnabled(false);

		contextsMenu_open.add(contextsMenu_open_db);
		contextsMenu_open.add(contextsMenu_open_file);
		JMenuItem contextsMenu_new = new JMenuItem(new NewContextAction(
				newLabel));
		JMenuItem contextsMenu_save = new JMenuItem(
				new SaveSelectedContextAction(saveLabel, false));

		contextsMenu_register = new JMenuItem(RegisterContextAction
				.getInstance());
		// JMenuItem contextsMenu_save_file = new JMenuItem( fileLabel );
		// JMenuItem contextsMenu_save_db = new JMenuItem( dbLabel );

		JMenuItem contextsMenu_saveAs = new JMenuItem(
				new SaveSelectedContextAction(saveAsLabel, true));

		contextsMenu.add(contextsMenu_new);
		contextsMenu.add(contextsMenu_save);
		contextsMenu.add(contextsMenu_saveAs);
		contextsMenu.add(contextsMenu_open);
		// contextsMenu.add( contextsMenu_quick_load );
		contextsMenu.addSeparator();
		contextsMenu.add(contextsMenu_register);

		// END Contexts Menu

		// BEGIN Snapshots Menu
		JMenu snapshotsMenu_open = new JMenu(openLabel);

		LoadSnapshotAction loadSnapshotAction = new LoadSnapshotAction(
				fileLabel, false);
		JMenuItem snapshotsMenu_open_file = new JMenuItem(loadSnapshotAction);

		/*
		 * LoadSnapshotAction quickLoadSnapshotAction = new LoadSnapshotAction(
		 * quickLoadLabel , true ); JMenuItem snapshotsMenu_quick_load = new
		 * JMenuItem( quickLoadSnapshotAction );
		 */

		JMenuItem snapshotsMenu_open_db = new JMenuItem(FilterSnapshotsAction
				.getInstance(dbLabel));
		snapshotsMenu_open_db.setName(dbLabel);
		// JMenuItem snapshotsMenu_open_recent = new JMenuItem( recentLabel );

		snapshotsMenu_open.add(snapshotsMenu_open_file);
		snapshotsMenu_open.add(snapshotsMenu_open_db);
		JMenuItem snapshotsMenu_new = new JMenuItem(LaunchSnapshotAction
				.getInstance(newLabel));

		JMenuItem snapshotsMenu_save = new JMenuItem(
				new SaveSelectedSnapshotAction(saveLabel, null, false));
		JMenuItem snapshotsMenu_saveAs = new JMenuItem(
				new SaveSelectedSnapshotAction(saveAsLabel, null, true));

		// LaunchSnapshotAction launchSnapshotAction =
		// LaunchSnapshotAction.getInstance( launchSnapshotLabel );
		// JMenuItem snapshotsMenu_launchSnapshot = new JMenuItem(
		// launchSnapshotAction );

		snapshotsMenu.add(snapshotsMenu_save);
		snapshotsMenu.add(snapshotsMenu_saveAs);
		snapshotsMenu.add(snapshotsMenu_open);
		// snapshotsMenu.add( snapshotsMenu_quick_load );
		snapshotsMenu.addSeparator();
		snapshotsMenu.add(snapshotsMenu_new);
		// BEGIN Snapshots Menu

		// BEGIN Favorites Menu
		OpenAddFavoriteContextAction openAddFavoriteContextAction = new OpenAddFavoriteContextAction(
				addContextToFavoritesLabel);
		JMenuItem favorites_contexts_add = new JMenuItem(
				openAddFavoriteContextAction);

		JMenuItem favorites_snapshots = new JMenuItem(snapshotsLabel);
		favorites_snapshots.setEnabled(false);

		JMenuItem favorites_config = new JMenuItem(configurationsLabel);
		favorites_config.setEnabled(false);

		Favorites favorites = Favorites.getInstance();
		if (favorites != null && favorites.getContextSubMenu() != null) {
			favorites_contexts = favorites.getContextSubMenu().getMenuRoot();
			favoritesMenu.add(favorites_contexts_add);
			favoritesMenu.addSeparator();

			favoritesMenu.add(favorites_contexts);
		}

		// BEGIN Favorites Menu

		// BEGIN Options Menu
		OpenOptionsAction optionAction = new OpenOptionsAction(optionsLabel);
		toolsMenu.add(optionAction);

		// BEGIN Options Menu

		// BEGIN Help Menu
		OpenContentsAction openContentsAction = new OpenContentsAction(
				contentsLabel);
		JMenuItem help_contents = new JMenuItem(openContentsAction);
		OpenTipsAction openTipsAction = new OpenTipsAction(tipsLabel);
		JMenuItem help_tips = new JMenuItem(openTipsAction);
		OpenAboutAction openAboutAction = new OpenAboutAction(aboutLabel);
		JMenuItem help_about = new JMenuItem(openAboutAction);
		helpMenu.add(help_contents);
		helpMenu.add(help_tips);
		helpMenu.addSeparator();
		helpMenu.add(help_about);
		// BEGIN Help Menu

		this.add(fileMenu);
		this.add(contextsMenu);
		this.add(snapshotsMenu);
		this.add(favoritesMenu);
		this.add(toolsMenu);
		this.add(helpMenu);
		// END first order sub-menus
	}

	/**
	 * @param _favorites_contexts
	 *            The favorites_contexts to set.
	 */
	public void setFavorites_contexts(JMenu _favorites_contexts) {
		favoritesMenu.remove(favorites_contexts);
		this.favorites_contexts = _favorites_contexts;
		favoritesMenu.add(favorites_contexts);
	}
}
