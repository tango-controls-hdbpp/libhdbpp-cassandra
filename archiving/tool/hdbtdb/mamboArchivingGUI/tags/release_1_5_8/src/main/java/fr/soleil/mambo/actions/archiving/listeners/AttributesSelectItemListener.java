//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/listeners/AttributesSelectItemListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttributesSelectItemListener.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: AttributesSelectItemListener.java,v $
// Revision 1.5  2006/10/19 12:36:25  ounsy
// minor changes
//
// Revision 1.4  2006/06/02 09:04:58  ounsy
// small protection added: added a control on stateChangeType, the type of item selection event
//
// Revision 1.3  2006/03/07 14:38:41  ounsy
// "Domain *" bug correction
//
// Revision 1.2  2006/02/24 12:17:37  ounsy
// added a log message for action successed/failed
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.archiving.listeners;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import fr.esrf.Tango.DevFailed;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;
import fr.soleil.mambo.components.archiving.AttributesSelectComboBox;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesTabAlternate;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;
import fr.soleil.mambo.logs.ILogger;
import fr.soleil.mambo.logs.LoggerFactory;
import fr.soleil.mambo.tools.Messages;

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
        int stateChangeType = event.getStateChange ();
        if ( stateChangeType != ItemEvent.SELECTED )
        {
            return;
        }
        
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
	            catch (ArchivingException e1) 
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
        //System.out.println ( "AttributesSelectItemListener/newDeviceClassSelected/selected|"+selected+"|" );
        AttributesTabAlternate attributesTabAlternate = AttributesTabAlternate.getInstance();

        AttributesSelectComboBox domainComboBox = attributesTabAlternate.getDomainComboBox();
        String currentDomainName = ( String ) domainComboBox.getSelectedItem();
        AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox ();
        MamboDeviceClass selectedDeviceClass = deviceClassComboBox.getSelectedDeviceClass ();
        
        //System.out.println ( "AttributesSelectItemListener/newDeviceClassSelected/currentDomainName|"+currentDomainName+"|selectedDeviceClass|"+selectedDeviceClass.getName () );
        if ( selectedDeviceClass == null )
        {
            //System.out.println ( "AttributesSelectItemListener/newDeviceClassSelected/selectedDeviceClass == null !" );
            //manual domain selection
            selectedDeviceClass = new MamboDeviceClass( selected );    
        }
        
        Domain correspondingDomain = selectedDeviceClass.getDomain ();
        if ( correspondingDomain == null )
        {
            //System.out.println ( "AttributesSelectItemListener/newDeviceClassSelected/correspondingDomain == null !" );
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
    private void newDomainSelected ( String selected ) throws ArchivingException
    {
        AttributesTabAlternate attributesTabAlternate = AttributesTabAlternate.getInstance();

        AttributesSelectComboBox attributeComboBox = attributesTabAlternate.getAttributeComboBox();
        attributeComboBox.reset();

        AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox();
        Domain domain = new Domain( selected );

        deviceClassComboBox.setDeviceClasses( domain );
    }

}
