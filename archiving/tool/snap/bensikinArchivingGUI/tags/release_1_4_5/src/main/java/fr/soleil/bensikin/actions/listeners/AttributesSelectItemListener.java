//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/listeners/AttributesSelectItemListener.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AttributesSelectItemListener.
//						(Claisse Laurent) - oct. 2005
//
//$Author: pierrejoseph $
//
//$Revision: 1.3 $
//
//$Log: AttributesSelectItemListener.java,v $
//Revision 1.3  2007/11/09 16:20:59  pierrejoseph
//Suppression of Mambo references
//
//Revision 1.2  2006/03/08 10:13:28  ounsy
//Domain* bug correction
//
//Revision 1.3  2006/03/07 14:38:41  ounsy
//"Domain *" bug correction
//
//Revision 1.2  2006/02/24 12:17:37  ounsy
//added a log message for action successed/failed
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
package fr.soleil.bensikin.actions.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import fr.esrf.Tango.DevFailed;
import fr.soleil.bensikin.components.context.detail.AttributesSelectComboBox;
import fr.soleil.bensikin.containers.context.ContextAttributesPanelAlternate;
import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class AttributesSelectItemListener implements ItemListener
{

 /**
  * 
  */
 public AttributesSelectItemListener ()
 {

 }

 /* (non-Javadoc)
  * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
  */
 public void itemStateChanged ( ItemEvent event )
 {
     String selected = ( String ) event.getItem();
     if ( selected == null || selected.equals( AttributesSelectComboBox.NO_SELECTION ) )
     {
         return;
     }

     AttributesSelectComboBox source = ( AttributesSelectComboBox ) event.getSource();
     int type = source.getType();
     ILogger logger = LoggerFactory.getCurrentImpl();
     
     switch ( type )
     {
         case AttributesSelectComboBox.DOMAIN_TYPE:
             try 
             {
                 newDomainSelected( selected );
                 
                 String msg = Messages.getLogMessage( "DOMAIN_CHANGED_ACTION_OK" );
	                logger.trace( ILogger.LEVEL_DEBUG , msg );
             } 
	            catch (SnapshotingException e1) 
	            {
	                String msg = Messages.getLogMessage( "DOMAIN_CHANGED_ACTION_KO" );
	                logger.trace( ILogger.LEVEL_WARNING , msg );
	                logger.trace( ILogger.LEVEL_WARNING , e1 );
             }
             break;

         case AttributesSelectComboBox.DEVICE_CLASS_TYPE:
             try
             {
                 newDeviceClassSelected( selected );
                 
                 String msg = Messages.getLogMessage( "DEVICE_CLASS_CHANGED_ACTION_OK" );
	                logger.trace( ILogger.LEVEL_DEBUG , msg );
             }
             catch ( DevFailed e )
             {
                 String msg = Messages.getLogMessage( "DEVICE_CLASS_CHANGED_ACTION_KO" );
	                logger.trace( ILogger.LEVEL_WARNING , msg );
	                logger.trace( ILogger.LEVEL_WARNING , e );
             }
             break;

         case AttributesSelectComboBox.ATTRIBUTE_TYPE:

             break;

         default :
             throw new IllegalStateException();
     }
 }

 /**
  * @param selected
  * @throws DevFailed
  */
 private void newDeviceClassSelected ( String selected ) throws DevFailed
 {
     ContextAttributesPanelAlternate attributesTabAlternate = ContextAttributesPanelAlternate.getInstance();

     AttributesSelectComboBox domainComboBox = attributesTabAlternate.getDomainComboBox();
     String currentDomainName = ( String ) domainComboBox.getSelectedItem();
     AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox ();
     BensikinDeviceClass selectedDeviceClass = deviceClassComboBox.getSelectedDeviceClass ();
     if ( selectedDeviceClass == null )
     {
         //manual domain selection
         selectedDeviceClass = new BensikinDeviceClass( selected );    
     }
     
     Domain correspondingDomain = selectedDeviceClass.getDomain ();
     if ( correspondingDomain == null )
     {
         //manual domain selection
         correspondingDomain = new Domain( currentDomainName );    
     }
     
     AttributesSelectComboBox attributeComboBox = attributesTabAlternate.getAttributeComboBox();        
     attributeComboBox.setACAttributes( correspondingDomain , selectedDeviceClass );
 }

 /**
  * @param selected
  * @throws ArchivingException
  */
 private void newDomainSelected ( String selected ) throws SnapshotingException
 {
     ContextAttributesPanelAlternate attributesTabAlternate = ContextAttributesPanelAlternate.getInstance();

     AttributesSelectComboBox attributeComboBox = attributesTabAlternate.getAttributeComboBox();
     attributeComboBox.reset();

     AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox();
     Domain domain = new Domain( selected );

     deviceClassComboBox.setDeviceClasses( domain );
 }

}
