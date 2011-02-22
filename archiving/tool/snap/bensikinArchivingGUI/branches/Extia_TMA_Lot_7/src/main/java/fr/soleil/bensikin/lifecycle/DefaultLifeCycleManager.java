// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/bensikin/bensikin/lifecycle/DefaultLifeCycleManager.java,v
// $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class DefaultLifeCycleManager.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.11 $
//
// $Log: DefaultLifeCycleManager.java,v $
// Revision 1.11 2007/06/14 15:22:25 pierrejoseph
// comments addition
//
// Revision 1.10 2007/03/15 14:26:35 ounsy
// corrected the table mode add bug and added domains buffer
//
// Revision 1.9 2007/03/14 15:49:11 ounsy
// the user/password is no longer hard-coded in the APIs, Bensikin takes two
// more parameters
//
// Revision 1.8 2006/06/28 12:51:51 ounsy
// minor changes
//
// Revision 1.7 2006/05/03 13:06:56 ounsy
// modified the limited operator rights
//
// Revision 1.6 2006/04/26 12:38:01 ounsy
// splash added
//
// Revision 1.5 2006/04/10 08:48:05 ounsy
// added RightsManagerFactory in startFactories
//
// Revision 1.4 2005/12/14 16:41:03 ounsy
// added methods necessary for alternate attribute selection
//
// Revision 1.3 2005/11/29 18:25:13 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:41 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin.lifecycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JFrame;

import fr.esrf.tangoatk.widget.util.Splash;
import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.roles.IRightsManager;
import fr.soleil.bensikin.actions.roles.RightsManagerFactory;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.data.context.manager.ContextManagerFactory;
import fr.soleil.bensikin.data.snapshot.manager.SnapshotManagerFactory;
import fr.soleil.bensikin.datasources.snapdb.SnapManagerFactory;
import fr.soleil.bensikin.datasources.tango.TangoManagerFactory;
import fr.soleil.bensikin.favorites.Favorites;
import fr.soleil.bensikin.favorites.FavoritesManagerFactory;
import fr.soleil.bensikin.favorites.IFavoritesManager;
import fr.soleil.bensikin.history.History;
import fr.soleil.bensikin.history.manager.HistoryManagerFactory;
import fr.soleil.bensikin.history.manager.IHistoryManager;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.options.Options;
import fr.soleil.bensikin.options.manager.IOptionsManager;
import fr.soleil.bensikin.options.manager.OptionsManagerFactory;
import fr.soleil.bensikin.options.sub.LogsOptions;
import fr.soleil.bensikin.options.sub.SaveOptions;
import fr.soleil.bensikin.tools.Messages;

/**
 * The default implementation.
 * 
 * @author CLAISSE
 */
public class DefaultLifeCycleManager implements LifeCycleManager {

	private String historyPath;
	private String optionsPath;
	private String favoritesPath;

	boolean hasHistorySave = true;
	private String user;
	private String password;

	public DefaultLifeCycleManager(String _user, String _password) {
		this.user = _user;
		this.password = _password;
	}

	/**
	 * Called before the GUI graphics containers are instantiated. Loads from
	 * files :
	 * <UL>
	 * <LI>the application's state from the history file
	 * <LI>the application's options
	 * </UL>
	 * And :
	 * <UL>
	 * <LI>initializes the application's ressources
	 * <LI>instantiates static implementations of various managers
	 * </UL>
	 * 
	 * @param startParameters
	 *            Not used
	 */
	public void applicationWillStart(Hashtable startParameters, Splash splash) {
		startFactories();
		setUpLookAndFeel();

		Locale currentLocale = new Locale("en", "US");
		try {
			Messages.initResourceBundle(currentLocale);
		} catch (Exception e) {
			e.printStackTrace();
			// we refuse to launch the application without at least those
			// resources
			System.exit(1);
		}

		// No exception can be raised
		loadOptions(splash);
		// No exception can be raised
		loadFavorites();

		if (this.hasHistorySave) {
			// No exception can be raised
			loadHistory(splash);
		}
	}

	/**
	 * Sets the trace level early so that the preloading logs respect the log
	 * level. (ie. if the log level was ERROR we don't want to display DEBUG
	 * logs )
	 */
	private void setTraces() {
		Options options = Options.getOptionsInstance();
		LogsOptions logsOptions = options.getLogsOptions();
		logsOptions.push();
	}

	/**
	 * Loads the application's options.
	 */
	private void loadOptions(Splash splash) {
		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			splash.progress(3);
			splash.setMessage("preparing options");
			optionsPath = Bensikin.getPathToResources() + "/options";
			File f = new File(optionsPath);
			if (!f.canWrite()) {
				// boolean b =
				f.mkdir();
			}
			optionsPath += "/options.xml";

			splash.progress(4);
			splash.setMessage("initializing options manager");
			IOptionsManager optionsManager = OptionsManagerFactory
					.getCurrentImpl();

			splash.progress(5);
			splash.setMessage("loading options...");
			Options options = optionsManager.loadOptions(optionsPath);
			splash.progress(6);
			Options.setOptionsInstance(options);
			splash.setMessage("loading log options");
			splash.progress(7);

			// we have to call it now before the normal options.push () that
			// occurs in applicationStarted
			// because otherwise the loaded desired log level won't be in effect
			// before applicationStarted
			// is called, and we always get applicationWillStart DEBUG level
			// logs.
			setTraces();
			splash.progress(8);
			splash
					.setMessage("loading log options done. Loading history options");
			// same for history loading
			setHasToLoadHistory();
			splash.progress(9);
			splash.setMessage("Loading history options done");

			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_OPTIONS_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (FileNotFoundException fnfe) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_OPTIONS_WARNING");
			splash.progress(9);
			splash.setMessage(msg);
			logger.trace(ILogger.LEVEL_WARNING, msg);
			logger.trace(ILogger.LEVEL_WARNING, fnfe);
			return;
		} catch (Exception e) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_OPTIONS_KO");
			splash.progress(9);
			splash.setMessage(msg);
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}

	/**
	 * Sets the load history boolean early so that the history isn't loaded if
	 * it doesn't have to.
	 */
	private void setHasToLoadHistory() {
		Options options = Options.getOptionsInstance();
		SaveOptions saveOptions = options.getSaveOptions();
		saveOptions.push();
	}

	/**
	 * Loads the application's favorites.
	 */
	private void loadFavorites() {
		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			favoritesPath = Bensikin.getPathToResources() + "/favorites";
			File f = new File(favoritesPath);
			if (!f.canWrite()) {
				// boolean b =
				f.mkdir();
			}
			favoritesPath += "/favorites.xml";

			IFavoritesManager favoritesManager = FavoritesManagerFactory
					.getCurrentImpl();

			favoritesManager.loadFavorites(favoritesPath);

			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_FAVORITES_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (FileNotFoundException fnfe) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_FAVORITES_WARNING");
			logger.trace(ILogger.LEVEL_WARNING, msg);
			logger.trace(ILogger.LEVEL_WARNING, fnfe);
			return;
		} catch (Exception e) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_FAVORITES_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}

	/**
	 * Tries to use WindowsLookAndFeel. If it fails, uses the JAVA LookAndFeel
	 * instead.
	 */
	private void setUpLookAndFeel() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		/*
		 * try {UIManager.setLookAndFeel(
		 * "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch
		 * (Exception e) { //do nothing and stay in JAVA plaf }
		 */
	}

	/**
	 * Instantiates the following managers with the following types:
	 * <UL>
	 * <LI>ILogger (DEFAULT_TYPE)
	 * <LI>ITangoManager (REAL_IMPL_TYPE)
	 * <LI>ISnapManager (REAL_IMPL_TYPE)
	 * <LI>IHistoryManager (XML_IMPL_TYPE)
	 * <LI>IOptionsManager (XML_IMPL_TYPE)
	 * <LI>IFavoritesManager (XML_IMPL_TYPE)
	 * <LI>IContextManager (XML_IMPL_TYPE)
	 * <LI>ISnapshotManager (XML_IMPL_TYPE)
	 * </UL>
	 */
	private void startFactories() {
		LoggerFactory.getImpl(LoggerFactory.DEFAULT_TYPE);

		TangoManagerFactory.getImpl(TangoManagerFactory.REAL_IMPL_TYPE);
		// TangoAlternateSelectionManagerFactory
		// .getImpl(TangoAlternateSelectionManagerFactory.BUFFERED_AND_ORDERED);

		SnapManagerFactory.setUser(this.user);
		SnapManagerFactory.setPassword(this.password);
		System.out.println("DefaultLifeCycleManager/getImpl/user/" + user
				+ "/password/" + password);
		SnapManagerFactory.getImpl(SnapManagerFactory.REAL_IMPL_TYPE);

		HistoryManagerFactory.getImpl(HistoryManagerFactory.XML_IMPL_TYPE);
		OptionsManagerFactory.getImpl(OptionsManagerFactory.XML_IMPL_TYPE);
		FavoritesManagerFactory.getImpl(FavoritesManagerFactory.XML_IMPL_TYPE);

		ContextManagerFactory.getImpl(ContextManagerFactory.XML_IMPL_TYPE);
		SnapshotManagerFactory.getImpl(SnapshotManagerFactory.XML_IMPL_TYPE);

		RightsManagerFactory
				.getImpl(RightsManagerFactory.SNAPSHOTS_ONLY_OPERATOR);
	}

	/**
	 * Loads the application's history.
	 */
	private void loadHistory(Splash splash) {
		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			splash.progress(10);
			splash.setMessage("preparing history");
			historyPath = Bensikin.getPathToResources() + "/history";
			File f = new File(historyPath);
			if (!f.canWrite()) {
				// boolean b =
				f.mkdir();
			}
			historyPath += "/history.xml";

			splash.progress(11);
			splash.setMessage("initializing history manager");
			IHistoryManager historyManager = HistoryManagerFactory
					.getCurrentImpl();

			splash.progress(12);
			splash.setMessage("loading history...");
			History history = historyManager.loadHistory(historyPath);

			splash.progress(13);
			splash.setMessage("applying history");
			History.setHistory(history);
			splash.progress(14);
			history.setOpenedAndSelectedEntities();
			splash.progress(15);
			splash.setMessage("history fully loaded");

			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (FileNotFoundException fnfe) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_WARNING");
			splash.progress(15);
			splash.setMessage(msg);
			logger.trace(ILogger.LEVEL_WARNING, msg);
			logger.trace(ILogger.LEVEL_WARNING, fnfe);
			return;
		} catch (Exception e) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_KO");
			splash.progress(15);
			splash.setMessage(msg);
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}

	/**
	 * Called just after the GUI graphics containers are instantiated. Is used
	 * for operations that need the containers to already exist: -pushing the
	 * pre loaded history and options to the display components -setting the
	 * window size
	 * 
	 * @param startParameters
	 *            Not used
	 */
	public void applicationStarted(Hashtable startParameters) {
		Options options = Options.getInstance();
		try {
			options.push();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// We have to push the selected context data only after the data panel
		// is created. Otherwise the fields don't exist yet, hence we can't set
		// their values
		History history = History.getHistory();
		try {
			history.push();
		} catch (Exception e) {
			// No selected context to pre-load
		}

		IRightsManager rightsManager = RightsManagerFactory.getCurrentImpl();
		if (Bensikin.isRestricted()) {
			rightsManager.disableUselessFields();
		} else {
			ContextDetailPanel.getInstance().getAttributeTableSelectionBean()
					.start();
		}
	}

	/**
	 * Called when the application detects a shutdown request, be it through the
	 * close icon or through the menu's Exit option. Is used to: -save
	 * everything that has to be saved -close resources And finally, shutdowns
	 * the application.
	 * 
	 * @param endParameters
	 *            Not used
	 */
	public void applicationClosed(Hashtable endParameters) {
		try {
			// begin do stuff
			System.out.println("Bensikin will close !");

			saveOptions();
			saveFavorites();
			if (this.hasHistorySave) {
				saveHistory();
			}

			System.out.println("Bensikin closed");
			// end do stuff
			System.exit(0);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Saves the application's favorites.
	 * 
	 * @throws Exception
	 */
	private void saveFavorites() throws Exception {
		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			Favorites favoritesToSave = Favorites.getInstance();
			IFavoritesManager favoritesManager = FavoritesManagerFactory
					.getCurrentImpl();
			favoritesManager.saveFavorites(favoritesToSave, favoritesPath);

			String msg = Messages
					.getLogMessage("APPLICATION_WILL_STOP_SAVE_FAVORITES_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (Exception e) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_STOP_SAVE_FAVORITES_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}

	/**
	 * Saves the application's options.
	 * 
	 * @throws Exception
	 */
	private void saveOptions() throws Exception {
		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			Options optionsToSave = Options.getInstance();
			IOptionsManager optionsManager = OptionsManagerFactory
					.getCurrentImpl();
			optionsManager.saveOptions(optionsToSave, optionsPath);

			String msg = Messages
					.getLogMessage("APPLICATION_WILL_STOP_SAVE_OPTIONS_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (Exception e) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_STOP_SAVE_OPTIONS_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}

	/**
	 * Saves the application's history.
	 * 
	 * @throws Exception
	 */
	private void saveHistory() throws Exception {
		ILogger logger = LoggerFactory.getCurrentImpl();
		try {
			History historyToSave = History.getCurrentHistory();
			IHistoryManager historyManager = HistoryManagerFactory
					.getCurrentImpl();
			historyManager.saveHistory(historyToSave, historyPath);

			String msg = Messages
					.getLogMessage("APPLICATION_WILL_STOP_SAVE_HISTORY_OK");
			logger.trace(ILogger.LEVEL_DEBUG, msg);
		} catch (Exception e) {
			String msg = Messages
					.getLogMessage("APPLICATION_WILL_STOP_SAVE_HISTORY_KO");
			logger.trace(ILogger.LEVEL_ERROR, msg);
			logger.trace(ILogger.LEVEL_ERROR, e);
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.lifecycle.LifeCycleManager#setHasHistorySave(boolean)
	 */
	public void setHasHistorySave(boolean b) {
		this.hasHistorySave = b;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.lifecycle.LifeCycleManager#hasHistorySave()
	 */
	public boolean hasHistorySave() {
		return this.hasHistorySave;
	}

}
