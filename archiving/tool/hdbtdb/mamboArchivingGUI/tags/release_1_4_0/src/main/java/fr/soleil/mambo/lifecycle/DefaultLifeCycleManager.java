//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/lifecycle/DefaultLifeCycleManager.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DefaultLifeCycleManager.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.34 $
//
// $Log: DefaultLifeCycleManager.java,v $
// Revision 1.34  2007/02/01 14:14:33  pierrejoseph
// Export Period is sometimes forced to 30 min.
// XmlHelper reorg
//
// Revision 1.33  2006/11/20 09:35:14  ounsy
// bufferTangoAttributes called only if Mambo is in mode 2 or 3
//
// Revision 1.32  2006/11/14 09:01:35  ounsy
// commented the useless call to getAttributesToDedicatedArchiver in bufferDbAttributes
//
// Revision 1.31  2006/11/09 14:23:42  ounsy
// domain/family/member/attribute refactoring
//
// Revision 1.30  2006/11/07 14:35:08  ounsy
// Domain/Family/Member/Attribute refactoring
//
// Revision 1.29  2006/10/19 12:45:54  ounsy
// added the loading of dedicated archivers status in bufferTangoAttributes()
//
// Revision 1.28  2006/10/17 14:33:34  ounsy
// minor changes
//
// Revision 1.27  2006/09/27 08:30:21  ounsy
// added extractingSource.openConnection () in connectToDatabase
//
// Revision 1.26  2006/09/26 15:07:41  ounsy
// commented extractingManager.cancel(); in applicationWillClose
//
// Revision 1.25  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.24  2006/09/20 12:50:44  ounsy
// changed imports
//
// Revision 1.23  2006/09/19 09:48:27  ounsy
// minor changes
//
// Revision 1.22  2006/09/15 14:06:58  ounsy
// the TangoAlternateSelectionManager implementation instantiated in startFactories is no longer a buffered one
//
// Revision 1.21  2006/09/14 11:28:56  ounsy
// startFacotries now instantiates a BUFFERED_AND_ORDERED TangoAlternateSelectionManager
//
// Revision 1.20  2006/08/31 13:22:00  ounsy
// small exit bug correction
//
// Revision 1.19  2006/08/30 12:27:23  ounsy
// Mambo closing more stable
//
// Revision 1.18  2006/08/29 14:12:57  ounsy
// added a JVM killing listener to correctly end application in case of forced killing
//
// Revision 1.17  2006/08/09 16:12:54  ounsy
// No more automatic tree expanding : user has to click on a button to fully expand a tree
//
// Revision 1.16  2006/07/27 12:45:21  ounsy
// minor changes
//
// Revision 1.15  2006/07/18 10:29:47  ounsy
// possibility to reload tango attributes
//
// Revision 1.14  2006/06/28 12:24:23  ounsy
// db attributes buffering
//
// Revision 1.13  2006/05/29 15:48:10  ounsy
// bug correction on attribute-level buffering
//
// Revision 1.12  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.11  2006/05/17 09:25:30  ounsy
// the instance of ITangoAlternateSelectionManager called is now OrderedTangoAlternateSelectionManagerImpl
//
// Revision 1.10  2006/05/16 12:03:34  ounsy
// the connection to the archiving Database is now done during startup
//
// Revision 1.9  2006/04/26 12:29:54  ounsy
// minor changes
//
// Revision 1.8  2006/04/26 12:29:01  ounsy
// minor changes
//
// Revision 1.7  2006/04/26 12:13:28  ounsy
// minor changes
//
// Revision 1.6  2006/04/26 11:53:01  ounsy
// splash added
//
// Revision 1.5  2006/04/05 13:48:42  ounsy
// close work centralization
//
// Revision 1.4  2006/02/27 09:34:58  ounsy
// new launch parameter for user rights (1=VC_ONLY, 2=ALL)
//
// Revision 1.3  2005/12/15 11:39:37  ounsy
// "copy table to clipboard" management
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.lifecycle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JFrame;

import fr.esrf.tangoatk.widget.util.Splash;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.ViewConfigurationManagerFactory;
import fr.soleil.mambo.datasources.tango.alternate.TangoAlternateSelectionManagerFactory;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;
import fr.soleil.mambo.history.History;
import fr.soleil.mambo.history.manager.HistoryManagerFactory;
import fr.soleil.mambo.history.manager.IHistoryManager;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.options.Options;
import fr.soleil.mambo.options.manager.ACDefaultsManagerFactory;
import fr.soleil.mambo.options.manager.IACDefaultsManager;
import fr.soleil.mambo.options.manager.IOptionsManager;
import fr.soleil.mambo.options.manager.OptionsManagerFactory;
import fr.soleil.mambo.options.sub.ACOptions;
import fr.soleil.mambo.options.sub.SaveOptions;
import fr.soleil.mambo.profile.manager.ProfileManagerFactory;
import fr.soleil.mambo.session.manager.SessionManagerFactory;
import fr.soleil.mambo.tools.MamboWarning;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.xmlhelpers.ac.ArchivingConfigurationXMLHelperFactory;


public class DefaultLifeCycleManager implements LifeCycleManager
{
    private String historyPath;
    private String optionsPath;
    //private String favoritesPath;

    private boolean hasHistorySave = true;
    private boolean closing = false;
    //private static final String SnapContext = null;
    
    /* (non-Javadoc)
     * @see mambo.lifecycle.LifeCycleManager#applicationWillStart(java.util.Hashtable)
     */
    public void applicationWillStart ( Hashtable startParameters, Splash splash )
    {
        startFactories ();
        setUpLookAndFeel ();
        disableActions ();

        Locale currentLocale = new Locale( "en" , "US" );
        try
        {
            Messages.initResourceBundle( currentLocale );
        }
        catch ( Exception e2 )
        {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        try
        {
            loadOptions(splash);
        }
        catch ( Exception e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if ( this.hasHistorySave )
        {
            try
            {
                loadHistory(splash);
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }

        connectToDatabase ( splash );    
        
        if ( Mambo.hasACs () )
        {
            bufferTangoAttributes ( splash );
        }
        
        bufferDBAttributes ( splash );
    }

    private void bufferTangoAttributes ( Splash splash ) 
    {
        ILogger logger = LoggerFactory.getCurrentImpl();
        
        try
        {
            splash.progress(18);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_TANGO_ATTRIBUTES") );   
            
            ITangoManager source = TangoManagerFactory.getCurrentImpl();
            source.loadDomains ( new Criterions (), true );
            
            /*IAttributeManager source2 = AttributeManagerFactory.getCurrentImpl();
            source2.getAttributesToDedicatedArchiver ( false, true );
            source2.getAttributesToDedicatedArchiver ( true, true );
            why did i do that???//CLA 10/11/06
            */
            
            splash.progress(19);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_TANGO_ATTRIBUTES_OK") );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_BUFFERING_TANGO_ATTRIBUTES_KO" );
            splash.progress(19);
            splash.setMessage(msg);
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }

    private void bufferDBAttributes ( Splash splash ) 
    {
        ILogger logger = LoggerFactory.getCurrentImpl();

        try
        {
            splash.progress(20);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_HDB_ATTRIBUTES") );   

            IAttributeManager source = AttributeManagerFactory.getCurrentImpl();
            source.loadDomains ( true, false );

            splash.progress(21);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_HDB_ATTRIBUTES_OK") );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_HDB_ATTRIBUTES_KO");
            splash.progress(21);
            splash.setMessage(msg);
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
        }

        try
        {
            splash.progress(22);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_TDB_ATTRIBUTES") );   

            IAttributeManager source = AttributeManagerFactory.getCurrentImpl();
            source.loadDomains ( false, false );

            splash.progress(23);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_BUFFERING_TDB_ATTRIBUTES_OK") );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_BUFFERING_TDB_ATTRIBUTES_KO" );
            splash.progress(23);
            splash.setMessage(msg);
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }        
    }
    
    private void connectToDatabase ( Splash splash ) 
    {
        ILogger logger = LoggerFactory.getCurrentImpl();
        
        try
        {
            splash.progress(16);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_CONNECT_DB") );   
            
            IAttributeManager attributeSource = AttributeManagerFactory.getCurrentImpl();
            attributeSource.openConnection ();
            
            IExtractingManager extractingSource = ExtractingManagerFactory.getCurrentImpl();
            extractingSource.openConnection ();
            
            splash.progress(17);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_CONNECT_DB_OK") );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_CONNECT_DB_KO" );
            splash.progress(17);
            splash.setMessage(msg);
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }

    /**
     * 
     */
    private void disableActions() 
    {
        if ( ! Mambo.hasACs () )
        {
            
        }
    }

    /**
     * 5 juil. 2005
     */
    private void loadOptions (Splash splash) throws Exception
    {
        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            splash.progress(3);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_PREPARE_OPTIONS") );
            optionsPath = Mambo.getPathToResources() + "/options";
            File f = new File( optionsPath );
            if ( !f.canWrite() )
            {
                //boolean b = 
                f.mkdir();
            }
            optionsPath += "/options.xml";

            String optionsLoadPath = ProfileManagerFactory.getCurrentImpl().getSelectedProfilePath() + "/options/options.xml";

            splash.progress(4);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_INIT_OPTIONS") );
            IOptionsManager optionsManager = OptionsManagerFactory.getCurrentImpl();

            //Options options = optionsManager.loadOptions( optionsPath );
            splash.progress(5);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_LOAD_BASIC_OPTIONS") );
            Options options = optionsManager.loadOptions( optionsLoadPath );
            splash.progress(6);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_LOAD_BASIC_OPTIONS_OK")
                               + " - "
                               + Messages.getLogMessage("APPLICATION_WILL_START_APPLY_BASIC_OPTIONS")
                             );

            Options.setOptionsInstance( options );
            splash.progress(7);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_APPLY_BASIC_OPTIONS_OK")
                               + " - "
                               + Messages.getLogMessage("APPLICATION_WILL_START_LOAD_LOG_OPTIONS") 
                             );

            //we have to call it now before the normal options.push () that occurs in applicationStarted
            //because otherwise the loaded desired log level won't be in effect before applicationStarted
            //is called, and we always get applicationWillStart DEBUG level logs.
            setTraces();
            splash.progress(8);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_LOAD_LOG_OPTIONS_OK")
                               + " - "
                               + Messages.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_OPTIONS") 
                             );
            //same for history loading
            setHasToLoadHistory();
            splash.progress(9);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY_OPTIONS_OK") );

            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_OPTIONS_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );

        }
        catch ( FileNotFoundException fnfe )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_OPTIONS_WARNING" );
            logger.trace( ILogger.LEVEL_WARNING , msg );
            logger.trace( ILogger.LEVEL_WARNING , fnfe );
            
            splash.progress(8);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_LOAD_DEFAULT_OPTIONS") );
            loadACDefaults ();
            splash.progress(9);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_LOAD_DEFAULT_OPTIONS_OK") );
            
            return;
        }
        catch ( MamboWarning mwrn )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_OPTIONS_WARNING" );
            logger.trace( ILogger.LEVEL_WARNING , msg );
            logger.trace( ILogger.LEVEL_WARNING , mwrn );
            
            splash.progress(8);
            splash.setMessage("Loading default options");
            loadACDefaults ();
            splash.progress(9);
            splash.setMessage("Loading default options done");
            
            return;
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_OPTIONS_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            splash.progress(9);
            splash.setMessage(msg);
            return;
        }
    }

    /**
     * 
     */
    private void loadACDefaults() 
    {
        IACDefaultsManager manager = ACDefaultsManagerFactory.getCurrentImpl ();
        ILogger logger = LoggerFactory.getCurrentImpl();
        manager.setDefault ( true );
        manager.setSaveLocation ( null );
        
        ACOptions defaultACOptions;
        try 
        {
            defaultACOptions = manager.loadACDefaults ();
            Options.getInstance ().setAcOptions ( defaultACOptions );
        } 
        catch (Exception e) 
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_OPTIONS_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }

    /**
     * 8 juil. 2005
     */
    private void setUpLookAndFeel ()
    {
        JFrame.setDefaultLookAndFeelDecorated( true );
        /*try
        {
            UIManager.setLookAndFeel ( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
        }
        catch (Exception e)
        {

        }*/
    }

    /**
     * 5 juil. 2005
     */
    private void startFactories ()
    {
        TangoManagerFactory.getImpl( TangoManagerFactory.BUFFERED_IMPL_TYPE );
        TangoAlternateSelectionManagerFactory.getImpl( TangoAlternateSelectionManagerFactory.BUFFERED_AND_ORDERED );

        ArchivingConfigurationXMLHelperFactory.getImpl(Mambo.canModifyExportPeriod()? 
        		ArchivingConfigurationXMLHelperFactory.STANDARD_IMPL_TYPE : ArchivingConfigurationXMLHelperFactory.FORCED_EXPORT_PERIOD_IMPL_TYPE);
        ArchivingConfigurationManagerFactory.getImpl( ArchivingConfigurationManagerFactory.XML_IMPL_TYPE );
        ViewConfigurationManagerFactory.getImpl( ViewConfigurationManagerFactory.XML_IMPL_TYPE );

        ArchivingManagerFactory.getImpl( ArchivingManagerFactory.REAL_IMPL_TYPE_GLOBAL_START_CALL );
        AttributeManagerFactory.getImpl( AttributeManagerFactory.BUFFERED_FORMATS_AND_DOMAINS );
        ExtractingManagerFactory.getImpl( ExtractingManagerFactory.BASIC );
        
        HistoryManagerFactory.getImpl( HistoryManagerFactory.XML_IMPL_TYPE );
        OptionsManagerFactory.getImpl( OptionsManagerFactory.XML_IMPL_TYPE );
        ACDefaultsManagerFactory.getImpl( ACDefaultsManagerFactory.PROPERTIES_IMPL_TYPE );
        LoggerFactory.getImpl( LoggerFactory.DEFAULT_TYPE );
    }

    /**
     * 5 juil. 2005
     */
    private void loadHistory (Splash splash) throws Exception
    {
        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            splash.progress(10);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_PREPARE_HISTORY") );
            //historyPath = GUIUtilities.getHistoryPath ();
            historyPath = Mambo.getPathToResources() + "/history";
            File f = new File( historyPath );
            if ( !f.canWrite() )
            {
                //boolean b = 
                f.mkdir();
            }
            historyPath += "/history.xml";
            String historyLoadPath = ProfileManagerFactory.getCurrentImpl().getSelectedProfilePath() + "/history/history.xml";

            splash.progress(11);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_PREPARE_HISTORY") );
            IHistoryManager historyManager = HistoryManagerFactory.getCurrentImpl();

            //History history = historyManager.loadHistory( historyPath );
            splash.progress(12);
            splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_LOAD_HISTORY") );
            History history = historyManager.loadHistory( historyLoadPath );
            if ( history != null )
            {
                splash.progress(13);
                splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_APPLY_HISTORY") );
                history.push();
                splash.setMessage( Messages.getLogMessage("APPLICATION_WILL_START_APPLY_HISTORY_OK") );
                splash.progress(14);
                History.setCurrentHistory( history );
            }
            splash.progress(15);
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_HISTORY_OK" );
            splash.setMessage( msg );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
        }
        catch ( FileNotFoundException fnfe )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_HISTORY_WARNING" );
            splash.progress(15);
            splash.setMessage(msg);
            logger.trace( ILogger.LEVEL_WARNING , msg );
            logger.trace( ILogger.LEVEL_WARNING , fnfe );
            return;
        }
        catch ( MamboWarning mwrn )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_HISTORY_WARNING" );
            splash.progress(15);
            splash.setMessage(msg);
            logger.trace( ILogger.LEVEL_WARNING , msg );
            logger.trace( ILogger.LEVEL_WARNING , mwrn );
            return;
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_START_LOAD_HISTORY_KO" );
            splash.progress(15);
            splash.setMessage(msg);
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }

    /* (non-Javadoc)
     * @see mambo.lifecycle.LifeCycleManager#applicationStarted(java.util.Hashtable)
     */
    public void applicationStarted ( Hashtable startParameters )
    {
        //System.out.println ( "DefaultLifeCycleManager/applicationStarted/START!!!!!!!!!!!!!!!!!!!" );
        Options options = Options.getInstance();
        try
        {
            options.push();
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        /*if ( Mambo.hasACs () )
        {
	        ACAttributesRecapTree ACtree = ACAttributesRecapTree.getInstance();
	        ACtree.expandAll( true );
        }

        VCAttributesRecapTree VCtree = VCAttributesRecapTree.getInstance();
        VCtree.expandAll( true );*/
    }

    /* (non-Javadoc)
     * @see mambo.lifecycle.LifeCycleManager#applicationClosed(java.util.Hashtable)
     */
    public void applicationClosed ( Hashtable endParameters )
    {
        if (! closing) System.exit( applicationWillClose (endParameters) );
    }

    public int applicationWillClose ( Hashtable endParameters )
    {
        try
        {
            if (!closing)
            {
                closing = true;
                
                //begin do stuff
                System.out.println( "Mambo will close !" );

                //IExtractingManager extractingManager = ExtractingManagerFactory.getCurrentImpl (); 
                //extractingManager.cancel();
                
                saveOptions();

                if ( this.hasHistorySave )
                {
                    saveHistory();
                    hasHistorySave = false;
                }

                //end do stuff
                SessionManagerFactory.getCurrentImpl().clearLock();
                System.gc();

                System.out.println( "Mambo closed" );

            }
            return 0 ;
        }
        catch ( Throwable t )
        {
            t.printStackTrace();
            SessionManagerFactory.getCurrentImpl().clearLock();
            System.gc();
            return 1 ;
        }
    }

    /**
     * 5 juil. 2005
     */
    private void saveOptions () throws Exception
    {
        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            Options optionsToSave = Options.getInstance();

            /*System.out.println ( "OPTIONS:" );
            System.out.println ( optionsToSave.toString ().trim () );*/

            IOptionsManager optionsManager = OptionsManagerFactory.getCurrentImpl();
            optionsManager.saveOptions( optionsToSave , optionsPath );

            String msg = Messages.getLogMessage( "APPLICATION_WILL_STOP_SAVE_OPTIONS_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_STOP_SAVE_OPTIONS_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }

    /**
     * 5 juil. 2005
     */
    private void saveHistory () throws Exception
    {
        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            History historyToSave = History.getCurrentHistory();

            /*System.out.println ( "HISTORY:" );
            System.out.println ( historyToSave.toString ().trim () );*/

            IHistoryManager historyManager = HistoryManagerFactory.getCurrentImpl();
            historyManager.saveHistory( historyToSave , historyPath );

            String msg = Messages.getLogMessage( "APPLICATION_WILL_STOP_SAVE_HISTORY_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "APPLICATION_WILL_STOP_SAVE_HISTORY_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }

    /* (non-Javadoc)
     * @see mambo.lifecycle.LifeCycleManager#setHasHistorySave(boolean)
     */
    public void setHasHistorySave ( boolean b )
    {
        this.hasHistorySave = b;
    }

    /* (non-Javadoc)
     * @see mambo.lifecycle.LifeCycleManager#hasHistorySave()
     */
    public boolean hasHistorySave ()
    {
        return this.hasHistorySave;
    }

    /**
     * 20 juil. 2005
     */
    private void setTraces ()
    {
        Options options = Options.getOptionsInstance();
        fr.soleil.mambo.options.sub.LogsOptions logsOptions = options.getLogsOptions();
        logsOptions.push();
    }

    /**
     * 20 juil. 2005
     */
    private void setHasToLoadHistory ()
    {
        Options options = Options.getOptionsInstance();
        SaveOptions saveOptions = options.getSaveOptions();
        saveOptions.push();

    }
}
