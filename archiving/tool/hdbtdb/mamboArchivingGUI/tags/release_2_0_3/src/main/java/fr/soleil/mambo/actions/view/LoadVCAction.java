//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/LoadVCAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LoadVCAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: LoadVCAction.java,v $
// Revision 1.3  2006/05/05 14:39:43  ounsy
// added the same optimisation on loading VCs as for ACs
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;

import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.components.view.VCFileFilter;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.datasources.file.ViewConfigurationManagerFactory;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;


public class LoadVCAction extends AbstractAction
{
    private boolean isDefault;

    /**
     * @param name
     */
    public LoadVCAction ( String name , boolean _isDefault )
    {
        super.putValue( Action.NAME , name );
        super.putValue( Action.SHORT_DESCRIPTION , name );

        this.isDefault = _isDefault;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        IViewConfigurationManager manager = ViewConfigurationManagerFactory.getCurrentImpl();
        if ( !this.isDefault )
        {
            //open file chooser
            JFileChooser chooser = new JFileChooser();
            VCFileFilter VCfilter = new VCFileFilter();
            chooser.addChoosableFileFilter( VCfilter );
            chooser.setCurrentDirectory( new File( manager.getDefaultSaveLocation() ) );

            int returnVal = chooser.showOpenDialog( MamboFrame.getInstance() );
            if ( returnVal == JFileChooser.APPROVE_OPTION )
            {
                /*String path = chooser.getSelectedFile ().getPath ();
                manager.setSaveLocation ( path );*/
                File f = chooser.getSelectedFile();
                String path = f.getAbsolutePath();

                if ( f != null )
                {
                    String extension = ConfigurationFileFilter.getExtension( f );
                    String expectedExtension = VCfilter.getExtension();

                    if ( extension == null || !extension.equalsIgnoreCase( expectedExtension ) )
                    {
                        path += ".";
                        path += expectedExtension;
                    }
                }
                manager.setNonDefaultSaveLocation( path );
            }
            else
            {
                return;
            }
        }
        else
        {
            manager.setNonDefaultSaveLocation( null );
        }

        loadVCOnceThePathIsSet ();
    }

    /**
     * 
     */
    private void loadVCOnceThePathIsSet() 
    {
        IViewConfigurationManager manager = ViewConfigurationManagerFactory.getCurrentImpl();
        ILogger logger = LoggerFactory.getCurrentImpl();
        
        try
        {
            ViewConfiguration selectedViewConfiguration = manager.loadViewConfiguration();
            selectedViewConfiguration.setModified( false );
            selectedViewConfiguration.setPath( manager.getSaveLocation() );
            
            //selectedViewConfiguration.push();
            OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
            if ( openedVCComboBox != null )
            {
                openedVCComboBox.selectElement( selectedViewConfiguration );
            }

            String msg = Messages.getLogMessage( "LOAD_VIEW_CONFIGURATION_ACTION_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
        }
        catch ( FileNotFoundException fnfe )
        {
            String msg = Messages.getLogMessage( "LOAD_VIEW_CONFIGURATION_ACTION_WARNING" );
            logger.trace( ILogger.LEVEL_WARNING , msg );
            logger.trace( ILogger.LEVEL_WARNING , fnfe );
            //e.printStackTrace();
            return;
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "LOAD_VIEW_CONFIGURATION_ACTION_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            //e.printStackTrace();
            return;
        }    
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    /*public void actionPerformed ( ActionEvent actionEvent )
    {
        IViewConfigurationManager manager = ViewConfigurationManagerFactory.getCurrentImpl();

        if ( !this.isDefault )
        {
            //open file chooser
            JFileChooser chooser = new JFileChooser();
            VCFileFilter VCfilter = new VCFileFilter();
            chooser.addChoosableFileFilter( VCfilter );
            chooser.setCurrentDirectory( new File( manager.getDefaultSaveLocation() ) );

            int returnVal = chooser.showOpenDialog( MamboFrame.getInstance() );
            if ( returnVal == JFileChooser.APPROVE_OPTION )
            {
                File f = chooser.getSelectedFile();
                String path = f.getAbsolutePath();

                if ( f != null )
                {
                    String extension = ConfigurationFileFilter.getExtension( f );
                    String expectedExtension = VCfilter.getExtension();

                    if ( extension == null || !extension.equalsIgnoreCase( expectedExtension ) )
                    {
                        path += ".";
                        path += expectedExtension;
                    }
                }
                manager.setNonDefaultSaveLocation( path );
            }
            else
            {
                return;
            }
        }
        else
        {
            manager.setNonDefaultSaveLocation( null );
        }

        ILogger logger = LoggerFactory.getCurrentImpl();

        try
        {
            ViewConfiguration selectedViewConfiguration = manager.loadViewConfiguration();
            selectedViewConfiguration.setModified( false );
            selectedViewConfiguration.setPath( manager.getSaveLocation() );
            selectedViewConfiguration.push();

            String msg = Messages.getLogMessage( "LOAD_VIEW_CONFIGURATION_ACTION_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
        }
        catch ( FileNotFoundException fnfe )
        {
            String msg = Messages.getLogMessage( "LOAD_VIEW_CONFIGURATION_ACTION_WARNING" );
            logger.trace( ILogger.LEVEL_WARNING , msg );
            logger.trace( ILogger.LEVEL_WARNING , fnfe );
            return;
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "LOAD_VIEW_CONFIGURATION_ACTION_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }*/
}
