// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/Mambo.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class Mambo.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.20 $
//
// $Log: Mambo.java,v $
// Revision 1.20 2007/12/17 18:27:16 pierrejoseph
// HDB available is inserted in the mambo args
//
// Revision 1.19 2007/12/12 17:49:51 pierrejoseph
// HdbAvailable is stored in Mambo class.
//
// Revision 1.18 2007/09/13 09:56:54 ounsy
// DBA :
// Modification in LifecycleManagerFactory to manage multiple LifeCycleManager.
// In ArchivingManagerFactory, AttributeManagerFactory, ExtractingManagerFactory
// add setCurrentImpl methods.
//
// Revision 1.17 2007/02/01 14:17:47 pierrejoseph
// Connection profiles are now viewer, expert, admin
//
// Revision 1.16 2006/10/25 08:00:45 ounsy
// replaced calls to show() by calls to setVisible(true)
//
// Revision 1.15 2006/08/31 13:22:00 ounsy
// small exit bug correction
//
// Revision 1.14 2006/08/29 14:02:27 ounsy
// added a JVM killing listener to correctly end application in case of forced
// killing
//
// Revision 1.13 2006/07/28 10:07:13 ounsy
// icons moved to "icons" package
//
// Revision 1.12 2006/06/28 12:24:02 ounsy
// db attributes buffering
//
// Revision 1.11 2006/06/15 15:37:11 ounsy
// added a new role USER_RIGHTS_ADMIN = 3, the only role able to map attributes
// to specific archivers
//
// Revision 1.10 2006/05/19 14:56:58 ounsy
// minor changes
//
// Revision 1.9 2006/04/26 14:41:06 ounsy
// splash with soleil logo
//
// Revision 1.8 2006/04/26 11:52:43 ounsy
// splash added
//
// Revision 1.7 2006/04/05 13:38:52 ounsy
// minor changes
//
// Revision 1.6 2006/03/29 10:26:49 ounsy
// Correctred the message in case of Incorrect arguments
//
// Revision 1.5 2006/03/20 10:32:46 ounsy
// the user rights parameter is no longer mandatory.
// if it is missing, the default is VC_ONLY
//
// Revision 1.4 2006/02/27 09:34:58 ounsy
// new launch parameter for user rights (1=VC_ONLY, 2=ALL)
//
// Revision 1.3 2005/12/15 10:37:20 ounsy
// multi sessions management
//
// Revision 1.2 2005/11/29 18:27:56 chinkumo
// no message
//
// Revision 1.1.2.2 2005/09/14 15:41:32 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import fr.esrf.tangoatk.widget.util.Splash;
import fr.soleil.application.user.manager.AccountManager;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.datasources.db.properties.PropertyManagerFactory;
import fr.soleil.mambo.lifecycle.LifeCycleManager;
import fr.soleil.mambo.lifecycle.LifeCycleManagerFactory;
import fr.soleil.mambo.tools.Messages;

public class Mambo {

    public static LifeCycleManager lifeCycleManager;
    private static String          pathToResources;

    private static String          HDBuser;
    private static String          HDBpassword;
    private static String          TDBuser;
    private static String          TDBpassword;
    private static Splash          splash;
    private static ImageIcon       appliStart           = new ImageIcon(
                                                                Mambo.class
                                                                        .getResource("icons/appli_start.jpg"));

    private static final String    USER_PROFILE_VIEWER  = "Viewer";
    private static final String    USER_PROFILE_EXPERT  = "Expert";
    private static final String    USER_PROFILE_ADMIN   = "Admin";
    public static final int        USER_RIGHTS_VC_ONLY  = 1;
    public static final int        USER_RIGHTS_ALL      = 2;
    public static final int        USER_RIGHTS_ADMIN    = 3;
    private static int             userRights           = USER_RIGHTS_VC_ONLY;

    private static final String    DEPLOYED_DB_TDB_ONLY = "TdbOnly";
    private static final String    DEPLOYED_DB_ALL      = "AllDB";
    private static boolean         hdbAvailable         = true;
    private static AccountManager  accountManager;

    /**
     * 8 juil. 2005
     */
    private static void createAndShowGUI() {
        // String title = "Mambo v" + Messages.getMessage("ABOUT_RELEASE") +
        // " (" + Messages.getMessage("ABOUT_RELEASE_DATE") + ")";
        String title = Messages.getAppMessage("project.name") + " v"
                + Messages.getAppMessage("project.version") + " ("
                + Messages.getAppMessage("build.date") + ")";

        splash = new Splash(appliStart);
        splash.setTitle(title);
        splash.setCopyright("Synchrotron SOLEIL");
        splash.setMaxProgress(26);
        splash.progress(1);

        splash.setMessage(Messages
                .getLogMessage("APPLICATION_WILL_START_BUILD_ENVIRONMENT"));
        LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
                .getImpl(LifeCycleManagerFactory.DEFAULT_LIFE_CYCLE);
        splash.progress(2);
        splash.setMessage(Messages
                .getLogMessage("APPLICATION_WILL_START_SET_ENVIRONMENT"));
        lifeCycleManager.applicationWillStart(null, splash);

        // Create and set up the window.
        splash.progress(24);
        splash.setMessage(Messages
                .getLogMessage("APPLICATION_WILL_START_BUILD_FRAME"));
        JFrame frame = MamboFrame.getInstance(lifeCycleManager);
        splash.progress(25);
        splash.setMessage(Messages
                .getLogMessage("APPLICATION_WILL_START_SET_FRAME"));
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Notifying GUI for degrad mode
        if (PropertyManagerFactory.getCurrentImpl().isHdbDegrad()) {
            if (PropertyManagerFactory.getCurrentImpl().isTdbDegrad()) {
                notifyTitleBarWithHdbAndTdbDegrad(frame);
            }
            else {
                notifyTitleBarWithHdbDegrad(frame);
            }
        }
        else if (PropertyManagerFactory.getCurrentImpl().isTdbDegrad()) {
            notifyTitleBarWithTdbDegrad(frame);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                LifeCycleManagerFactory.getCurrentImpl().applicationWillClose(
                        new Hashtable());
            }
        });

        // Display the window.
        splash.progress(26);
        splash.setMessage(Messages
                .getLogMessage("APPLICATION_WILL_START_PREPARE_DISPLAY"));
        frame.pack();
        splash.setVisible(false);
        splash.dispose();
        frame.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds());
        frame.setVisible(true);
    }

    private void readProperties() {
        File file = new File(pathToResources + File.separator
                + "mambo.properties");
        try {
            if (file.exists()) {

                InputStream strm = new FileInputStream(file);
                Properties props = new Properties();
                try {
                    props.load(strm);
                    hdbAvailable = Boolean.valueOf(props.getProperty(
                            "mambo.hdb.available", Boolean
                                    .toString(hdbAvailable)));
                }
                finally {
                    strm.close();
                }
                // Initializing system
                // PropertyConfigurator.configure(props); from log4j
            } // end if (file.exists())

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 19 juil. 2005
     */
    /*
     * private static void test () { try {
     * ArchivingManagerApi.connectArchivingDatabase( true , HDBuser ,
     * HDBpassword ); //System.out.println ( "Name = " +
     * ArchivingManagerApi.getDbName(true) + "/" );
     * ArchivingManagerApi.connectArchivingDatabase( true , TDBuser ,
     * TDBpassword ); //System.out.println ( "Name = " +
     * ArchivingManagerApi.getDbName(false) + "/" ); } catch (
     * ArchivingException e ) { e.printStackTrace(); } }
     */

    /**
     * @return Returns the pathToResources.
     */
    public static String getPathToResources() {
        return pathToResources;
    }

    /**
     * @return Returns the hDBpassword.
     */
    public static String getHDBpassword() {
        return HDBpassword;
    }

    /**
     * @return Returns the hDBuser.
     */
    public static String getHDBuser() {
        return HDBuser;
    }

    /**
     * @return Returns the tDBpassword.
     */
    public static String getTDBpassword() {
        return TDBpassword;
    }

    /**
     * @return Returns the tDBuser.
     */
    public static String getTDBuser() {
        return TDBuser;
    }

    public static boolean isHdbAvailable() {
        return hdbAvailable;
    }

    protected static void gettingStarted() {
        accountManager.launchAccountSelector();
        if (accountManager.getSelectedAccount() == AccountManager.ACCOUNT_SELECTION_CANCELED) {
            System.exit(0);
        }
        else if (accountManager.getSelectedAccount() == AccountManager.NO_ACCOUNT_SELECTED) {
            accountManager.launchAccountSelector();
        }
        else {
            pathToResources = accountManager.getSelectedAccountWorkingPath();
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    accountManager.clearLock(pathToResources);
                }

            });
        }
    }

    /**
     * @return Returns the userRights.
     */
    public static int getUserRights() {
        return userRights;
    }

    public static boolean hasACs() {
        return (userRights != Mambo.USER_RIGHTS_VC_ONLY);
    }

    public static boolean canChooseArchivers() {
        return (userRights == Mambo.USER_RIGHTS_ADMIN);
    }

    public static boolean canModifyExportPeriod() {
        return (userRights == Mambo.USER_RIGHTS_ADMIN);
    }

    public static void setHDBpassword(String bpassword) {
        HDBpassword = bpassword;
    }

    public static void setHDBuser(String buser) {
        HDBuser = buser;
    }

    public static void setTDBpassword(String bpassword) {
        TDBpassword = bpassword;
    }

    public static void setTDBuser(String buser) {
        TDBuser = buser;
    }

    public static AccountManager getAccountManager() {
        return accountManager;
    }

    public static void setAccountManager(AccountManager accountManager) {
        Mambo.accountManager = accountManager;
    }

    /**
     * @param args
     *            8 juil. 2005
     */
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.

        /*
         * try { ArchivingManagerApi.getHDBArchiverDedicatedAttributes (); }
         * catch (ArchivingException e1) { // TODO Auto-generated catch block
         * e1.printStackTrace(); }
         */

        if (args == null || args.length > 7 || args.length < 5) {
            System.out
                    .println("Incorrect arguments. Correct syntax: java Mambo pathToResources HDBuser HDBpassword TDBuser TDBpassword userRights");
            // Correct syntax: java Mambo pathToResources HDBuser HDBpassword
            // TDBuser TDBpassword userRights DeployedDB
            System.exit(1);
        }

        pathToResources = args[0];
        try {
            File f = new File(pathToResources);
            if (!f.isDirectory()) {
                throw new Exception("Not a directory");
            }
            if (!f.canRead()) {
                throw new Exception("Invalid path");
            }

            if (!f.canWrite()) {
                throw new Exception("The path is read only");
            }
        }
        catch (Exception e) {
            System.out.println("Incorrect arguments. The parameter "
                    + pathToResources + " is not a valid directory");
            e.printStackTrace();
            System.exit(1);
        }

        HDBuser = args[1];
        HDBpassword = args[2];
        TDBuser = args[3];
        TDBpassword = args[4];

        if (args.length == 6) {
            try {
                String argValue = args[5];
                if (argValue.compareToIgnoreCase(USER_PROFILE_VIEWER) == 0) userRights = USER_RIGHTS_VC_ONLY;
                else if (argValue.compareToIgnoreCase(USER_PROFILE_EXPERT) == 0) userRights = USER_RIGHTS_ALL;
                else if (argValue.compareToIgnoreCase(USER_PROFILE_ADMIN) == 0) userRights = USER_RIGHTS_ADMIN;
                else if (argValue.compareToIgnoreCase(DEPLOYED_DB_TDB_ONLY) == 0) hdbAvailable = false;
                else if (argValue.compareToIgnoreCase(DEPLOYED_DB_ALL) == 0) hdbAvailable = true;
                else {
                    throw new Exception(
                            "The userRights parameter has to be either "
                                    + USER_PROFILE_VIEWER + " or "
                                    + USER_PROFILE_EXPERT);
                }
            }
            catch (Exception e) {
                System.out
                        .println("Incorrect arguments. The userRights parameter has to be either "
                                + USER_PROFILE_VIEWER
                                + " or "
                                + USER_PROFILE_EXPERT);
                e.printStackTrace();
                System.exit(1);
            }
        }
        else if (args.length == 7) {
            try {
                String argValue = args[5];
                if (argValue.compareToIgnoreCase(USER_PROFILE_VIEWER) == 0) userRights = USER_RIGHTS_VC_ONLY;
                else if (argValue.compareToIgnoreCase(USER_PROFILE_EXPERT) == 0) userRights = USER_RIGHTS_ALL;
                else if (argValue.compareToIgnoreCase(USER_PROFILE_ADMIN) == 0) userRights = USER_RIGHTS_ADMIN;
                else {
                    throw new Exception(
                            "The userRights parameter has to be either "
                                    + USER_PROFILE_VIEWER + " or "
                                    + USER_PROFILE_EXPERT);
                }

                argValue = args[6];
                if (argValue.compareToIgnoreCase(DEPLOYED_DB_TDB_ONLY) == 0) hdbAvailable = false;
                else if (argValue.compareToIgnoreCase(DEPLOYED_DB_ALL) == 0) hdbAvailable = true;
                else {
                    throw new Exception(
                            "The deployed DB parameter has to be either "
                                    + DEPLOYED_DB_TDB_ONLY + " or "
                                    + DEPLOYED_DB_ALL);
                }
            }
            catch (Exception e) {
                System.out
                        .println("Incorrect arguments. The userRights parameter has to be either "
                                + USER_PROFILE_VIEWER
                                + " or "
                                + USER_PROFILE_EXPERT);
                e.printStackTrace();
                System.exit(1);
            }
        }
        // else is not useful because the defaults values are used for the
        // variables hdbAvailable and userRights.

        /*
         * String userRights_s = args.length == 6 ? args[ 5 ] :
         * USER_PROFILE_VIEWER; try {
         * if(userRights_s.compareToIgnoreCase(USER_PROFILE_VIEWER) == 0)
         * userRights = USER_RIGHTS_VC_ONLY; else if(
         * userRights_s.compareToIgnoreCase(USER_PROFILE_EXPERT) == 0)
         * userRights = USER_RIGHTS_ALL; else if(
         * userRights_s.compareToIgnoreCase(USER_PROFILE_ADMIN) == 0) userRights
         * = USER_RIGHTS_ADMIN; else { throw new Exception(
         * "The userRights parameter has to be either " + USER_PROFILE_VIEWER +
         * " or " + USER_PROFILE_EXPERT ); } } catch ( Exception e ) {
         * System.out.println(
         * "Incorrect arguments. The userRights parameter has to be either " +
         * USER_PROFILE_VIEWER + " or " + USER_PROFILE_EXPERT );
         * e.printStackTrace(); System.exit( 1 ); }
         */
        // NO more true Read the configurating properties in the pathToResources
        // directory
        // readProperties();
        // System.out.println("Is HDB available = " + hdbAvailable );
        setAccountManager(new AccountManager(pathToResources, "Mambo", null,
                false));

        Locale currentLocale = new Locale("en", "US");
        try {
            Messages.initResourceBundle(currentLocale);
        }
        catch (Exception e) {
            e.printStackTrace();
            // we refuse to launch the application without at least those
            // resources
            System.exit(1);
        }

        gettingStarted();

        //System.out.println("Mambo start");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void notifyTitleBarWithHdbAndTdbDegrad(JFrame frame) {
        StringBuffer titleBuffer = new StringBuffer(frame.getTitle());
        titleBuffer.append(" ");
        titleBuffer.append(Messages.getMessage("DEGRADED_NOTIFICATION_ALL"));
        frame.setTitle(titleBuffer.toString());
        titleBuffer = null;
        frame.setIconImage(new ImageIcon(Mambo.class
                .getResource("icons/degrad-all.png")).getImage());
    }

    private static void notifyTitleBarWithHdbDegrad(JFrame frame) {
        StringBuffer titleBuffer = new StringBuffer(frame.getTitle());
        titleBuffer.append(" ");
        titleBuffer.append(Messages.getMessage("DEGRADED_NOTIFICATION_HDB"));
        frame.setTitle(titleBuffer.toString());
        titleBuffer = null;
        frame.setIconImage(new ImageIcon(Mambo.class
                .getResource("icons/degrad-hdb.png")).getImage());
    }

    private static void notifyTitleBarWithTdbDegrad(JFrame frame) {
        StringBuffer titleBuffer = new StringBuffer(frame.getTitle());
        titleBuffer.append(" ");
        titleBuffer.append(Messages.getMessage("DEGRADED_NOTIFICATION_TDB"));
        frame.setTitle(titleBuffer.toString());
        titleBuffer = null;
        frame.setIconImage(new ImageIcon(Mambo.class
                .getResource("icons/degrad-tdb.png")).getImage());
    }
}
