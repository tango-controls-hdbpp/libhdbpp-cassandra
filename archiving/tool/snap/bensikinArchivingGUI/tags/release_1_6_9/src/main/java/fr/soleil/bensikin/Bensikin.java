// +======================================================================
// $Source: $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Bensikin.
// (Claisse Laurent) - 10 juin 2005
//
// $Author: ounsy $
//
// $Revision: $
//
// $Log: Bensikin.java,v $
// Revision 1.12 2007/03/14 15:49:11 ounsy
// the user/password is no longer hard-coded in the APIs, Bensikin takes two
// more parameters
//
// Revision 1.11 2007/02/01 13:59:15 pierrejoseph
// Connection profiles are now viewer, expert
//
// Revision 1.10 2006/12/12 13:17:49 ounsy
// minor changees
//
// Revision 1.9 2006/04/26 14:41:45 ounsy
// splash with soleil logo
//
// Revision 1.8 2006/04/26 12:38:01 ounsy
// splash added
//
// Revision 1.7 2006/04/10 08:48:38 ounsy
// added the launch parameter that describes user rights
//
// Revision 1.6 2005/12/14 15:56:22 ounsy
// added session management
//
// Revision 1.5 2005/11/29 18:25:08 chinkumo
// no message
//
// Revision 1.1.1.2 2005/08/22 11:58:32 chinkumo
// First commit
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.bensikin;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import fr.soleil.application.user.manager.AccountManager;
import fr.soleil.archiving.gui.logs.GUILoggerFactory;
import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.containers.BensikinFrame;
import fr.soleil.bensikin.lifecycle.LifeCycleManager;
import fr.soleil.bensikin.lifecycle.LifeCycleManagerFactory;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.commonarchivingapi.ArchivingTools.Diary.ILogger;
import fr.soleil.util.component.Splash;

/**
 * The Main class of Bensikin.
 * 
 * @author CLAISSE
 */
public class Bensikin {

    final static ILogger logger = GUILoggerFactory.getLogger();

    private static String pathToResources;
    private static boolean hires;
    private static ImageIcon appliStart = new ImageIcon(Bensikin.class
	    .getResource("icons/appli_start.jpg"));

    private static Splash splash;

    private static int userRights;
    private static final String USER_PROFILE_VIEWER = "Viewer";
    private static final String USER_PROFILE_EXPERT = "Expert";
    public static final int USER_RIGHTS_RESTRICTED = 1;
    public static final int USER_RIGHTS_ALL = 2;

    public static final String USER_PARAMETER_KEY = "USER";
    public static final String PASSWORD_PARAMETER_KEY = "PASSWORD";

    private static AccountManager accountManager;

    /**
     * Creates the main frame and the LifeCycleManager, launches the application
     * 
     * @param password
     * @param user
     */
    private static void createAndShowGUI(final String user, final String password) {
	// String title = "Bensikin v" + Messages.getMessage("ABOUT_RELEASE") +
	// " (" + Messages.getMessage("ABOUT_RELEASE_DATE") + ")";
	final String title = Messages.getAppMessage("project.name") + " v"
		+ Messages.getAppMessage("project.version") + " ("
		+ Messages.getAppMessage("build.date") + ")";
	splash = new Splash(appliStart);
	splash.setTitle(title);
	splash.setCopyright("Synchrotron SOLEIL");
	splash.setMaxProgress(18);
	splash.progress(1);

	splash.setMessage("Building environment...");
	LifeCycleManagerFactory.setUser(user);
	LifeCycleManagerFactory.setPassword(password);
	final LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
		.getImpl(LifeCycleManagerFactory.DEFAULT_LIFE_CYCLE);
	splash.progress(2);
	splash.setMessage("Setting up environment...");

	lifeCycleManager.applicationWillStart(null, splash);

	// Create and set up the window.
	splash.progress(16);
	splash.setMessage("Building frame...");
	final JFrame frame = BensikinFrame.getInstance(lifeCycleManager);
	// frame.setUndecorated(false);
	splash.progress(17);
	splash.setMessage("Setting up frame...");
	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	// Display the window.
	splash.progress(18);
	splash.setMessage("Preparing to display application...");
	frame.pack();
	splash.setVisible(false);
	splash.dispose();
	frame.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
	frame.setVisible(true);
	logger.trace(ILogger.LEVEL_DEBUG, Messages.getLogMessage("APPLICATION_STARTED_OK"));
    }

    /**
     * Returns the working directory.
     * 
     * @return Returns the pathToResources.
     */
    public static String getPathToResources() {
	return pathToResources;
    }

    /**
     * Returns true if Bensikin is running in hires.
     * 
     * @return Returns the hires.
     */
    public static boolean isHires() {
	return hires;
    }

    /**
     * Detects the screen resolution and sets the hires attribute consequently
     */
    private static void initResolutionMode() {
	final Toolkit toolkit = Toolkit.getDefaultToolkit();
	final Dimension dim = toolkit.getScreenSize();
	System.out
		.println("Resolution-------------------------------------------------------------");
	System.out.println("Resolution: " + dim.width + "*" + dim.height);

	boolean _hires = true;
	if (dim.width <= 1024) {
	    _hires = false;
	}

	hires = _hires;
    }

    protected static void gettingStarted() {
	accountManager.launchAccountSelector();
	if (accountManager.getSelectedAccount() == AccountManager.ACCOUNT_SELECTION_CANCELED) {
	    System.exit(0);
	} else if (accountManager.getSelectedAccount() == AccountManager.NO_ACCOUNT_SELECTED) {
	    accountManager.launchAccountSelector();
	} else {
	    Runtime.getRuntime().addShutdownHook(new Thread() {
		@Override
		public void run() {
		    accountManager.clearLock(accountManager.getSelectedAccountWorkingPath());
		}

	    });
	}
    }

    public static boolean isRestricted() {
	return userRights != Bensikin.USER_RIGHTS_ALL;
    }

    public static AccountManager getAccountManager() {
	return accountManager;
    }

    public static void setAccountManager(final AccountManager accountManager) {
	Bensikin.accountManager = accountManager;
    }

    /**
     * Launches Bensikin.
     * 
     * @param args
     *            Must have 1 parameter, the working directory, which will be
     *            created if it doesn't exist
     */
    public static void main(final String[] args) {
	// Schedule a job for the event-dispatching thread:
	// creating and showing this application's GUI.
	if (args == null || args.length < 1 || args.length > 4) {
	    System.out
		    .println("Incorrect arguments. Correct syntax: java Bensikin pathToResources userdb passdb userRights");
	    System.exit(1);
	}

	pathToResources = System.getProperty(GUIUtilities.WORKING_DIR);
	try {
	    final File f = new File(pathToResources);
	    if (!f.isDirectory()) {
		System.out.println("Path did not exist: creating it");
		f.mkdirs();
		// throw new Exception("Not a directory");
	    }
	    if (!f.canRead()) {
		throw new Exception("Invalid path");
	    }
	    if (!f.canWrite()) {
		throw new Exception("The path is read only");
	    }
	} catch (final Exception e) {
	    System.out.println("Incorrect arguments. The parameter " + pathToResources
		    + " is not a valid directory");
	    e.printStackTrace();
	    System.exit(1);
	}

	final String user = args[0];
	final String password = args[1];

	final String userRights_s = args.length == 3 ? args[2] : USER_PROFILE_VIEWER;
	try {
	    if (userRights_s.compareToIgnoreCase(USER_PROFILE_VIEWER) == 0) {
		userRights = USER_RIGHTS_RESTRICTED;
	    } else if (userRights_s.compareToIgnoreCase(USER_PROFILE_EXPERT) == 0) {
		userRights = USER_RIGHTS_ALL;
	    } else {
		throw new Exception("The userRights parameter has to be either "
			+ USER_PROFILE_VIEWER + " or " + USER_PROFILE_EXPERT);
	    }
	} catch (final Exception e) {
	    System.out.println("Incorrect arguments. The userRights parameter has to be either "
		    + USER_PROFILE_VIEWER + " or " + USER_PROFILE_EXPERT);
	    e.printStackTrace();
	    System.exit(1);
	}

	setAccountManager(new AccountManager(pathToResources, "Bensikin", null, false));

	final Locale currentLocale = new Locale("en", "US");
	try {
	    Messages.initResourceBundle(currentLocale);
	} catch (final Exception e) {
	    e.printStackTrace();
	    // we refuse to launch the application without at least those
	    // resources
	    System.exit(1);
	}
	gettingStarted();
	System.out.println("Bensikin start");
	initResolutionMode();

	javax.swing.SwingUtilities.invokeLater(new Runnable() {

	    public void run() {
		createAndShowGUI(user, password);
	    }
	});
    }

}
