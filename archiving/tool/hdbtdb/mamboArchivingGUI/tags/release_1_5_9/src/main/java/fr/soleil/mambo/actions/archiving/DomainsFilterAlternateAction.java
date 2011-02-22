//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/DomainsFilterAlternateAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ArchivingTransferAction.
//						(GIRARDOT Raphael) - sept. 2005
//
//$Author: ounsy $
//
//$Revision: 1.5 $
//
//$Log: DomainsFilterAlternateAction.java,v $
//Revision 1.5  2006/10/19 12:35:36  ounsy
//minor changes
//
//Revision 1.4  2006/10/17 14:27:45  ounsy
//minor changes
//
//Revision 1.3  2006/09/20 12:49:40  ounsy
//changed imports
//
//Revision 1.2  2006/09/14 10:20:16  ounsy
//refactoring
//
//Revision 1.1  2006/02/24 12:12:23  ounsy
//creation
//
//Revision 1.2  2005/12/15 10:43:17  ounsy
//minor changes
//
//Revision 1.1  2005/11/29 18:27:07  chinkumo
//no message
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.AttributesSelectComboBox;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesTabAlternate;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.datasources.tango.alternate.ITangoAlternateSelectionManager;
import fr.soleil.mambo.datasources.tango.alternate.TangoAlternateSelectionManagerFactory;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;


public class DomainsFilterAlternateAction extends AbstractAction
{
	 public DomainsFilterAlternateAction ( String name )
	 {
	     super.putValue( Action.NAME , name );
	     super.putValue( Action.SHORT_DESCRIPTION , name );
	 }
	
	 public void actionPerformed ( ActionEvent arg0 )
	 {
         //long before = System.currentTimeMillis ();
         //System.out.println ( "DomainsFilterAlternateAction|actionPerformed|START" );
         
         ITangoAlternateSelectionManager manager = TangoAlternateSelectionManagerFactory.getCurrentImpl();
	     ILogger logger = LoggerFactory.getCurrentImpl();
	     String msg;
	     
	     AttributesTabAlternate attributesTabAlternate = AttributesTabAlternate.getInstance();
	     String domainsRegExp = attributesTabAlternate.getDomainsRegExp ();
         AttributesSelectComboBox attributeComboBox = attributesTabAlternate.getAttributeComboBox();
         AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox();
         AttributesSelectComboBox domainComboBox = attributesTabAlternate.getDomainComboBox();
         
         attributeComboBox.reset();
         domainComboBox.selectNeutralElement ();
         
         Domain [] domains = null;
         try 
         {
            domains = manager.loadDomains ( domainsRegExp );
            deviceClassComboBox.setDeviceClasses( domains );
            
            msg = Messages.getLogMessage( "DOMAINS_FILTER_ACTION_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
         } 
         catch (Exception e) 
         {
             msg = Messages.getLogMessage( "DOMAINS_FILTER_ACTION_KO" );
             
             logger.trace( ILogger.LEVEL_WARNING , msg );
             logger.trace( ILogger.LEVEL_WARNING , e );
         }
         
         //long after = System.currentTimeMillis ();
         //long delta = after - before;
         //System.out.println ( "DomainsFilterAlternateAction|actionPerformed|END|delta|"+delta+"|" );
	 }
}
