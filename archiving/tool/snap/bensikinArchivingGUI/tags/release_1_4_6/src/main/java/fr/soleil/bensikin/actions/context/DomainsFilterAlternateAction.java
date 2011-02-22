package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.AttributesSelectComboBox;
import fr.soleil.bensikin.containers.context.ContextAttributesPanelAlternate;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.datasources.tango.ITangoAlternateSelectionManager;
import fr.soleil.bensikin.datasources.tango.TangoAlternateSelectionManagerFactory;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class DomainsFilterAlternateAction extends BensikinAction
{

	 public DomainsFilterAlternateAction ( String name )
	 {
	     super.putValue( Action.NAME , name );
	     super.putValue( Action.SHORT_DESCRIPTION , name );
	 }
	
	 public void actionPerformed ( ActionEvent arg0 )
	 {
	     ITangoAlternateSelectionManager manager = TangoAlternateSelectionManagerFactory.getCurrentImpl();
	     ILogger logger = LoggerFactory.getCurrentImpl();
	     String msg;
	     
	     ContextAttributesPanelAlternate attributesTabAlternate = ContextAttributesPanelAlternate.getInstance();
	     String domainsRegExp = attributesTabAlternate.getDomainsRegExp ();
	     Domain [] domains = null;
       try 
       {
          domains = manager.loadDomains ( domainsRegExp );
       } 
       catch (SnapshotingException e) 
       {
           msg = Messages.getLogMessage( "DOMAINS_FILTER_ACTION_KO" );
           
           logger.trace( ILogger.LEVEL_WARNING , msg );
           logger.trace( ILogger.LEVEL_WARNING , e );
       }
       AttributesSelectComboBox attributeComboBox = attributesTabAlternate.getAttributeComboBox();
       attributeComboBox.reset();

       AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox();
       try 
       {
          deviceClassComboBox.setDeviceClasses( domains );
          
          msg = Messages.getLogMessage( "DOMAINS_FILTER_ACTION_OK" );
          logger.trace( ILogger.LEVEL_DEBUG , msg );
       } 
       catch (SnapshotingException e1) 
       {
           msg = Messages.getLogMessage( "DOMAINS_FILTER_ACTION_KO" );
           
           logger.trace( ILogger.LEVEL_WARNING , msg );
           logger.trace( ILogger.LEVEL_WARNING , e1 );
       }
	 }
}
