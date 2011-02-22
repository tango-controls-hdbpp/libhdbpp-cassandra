//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/AddSelectedContextAttributeAlternateAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AddSelectedACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: pierrejoseph $
//
//$Revision: 1.3 $
//
//$Log: AddSelectedContextAttributeAlternateAction.java,v $
//Revision 1.3  2007/11/09 16:20:59  pierrejoseph
//Suppression of Mambo references
//
//Revision 1.2  2007/08/24 14:04:53  ounsy
//bug correction with context printing as text
//
//Revision 1.1  2006/05/03 13:03:46  ounsy
//creation
//
//Revision 1.4  2006/04/13 14:42:02  ounsy
//removed useless logs
//
//Revision 1.3  2006/04/10 08:46:54  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.2  2006/03/08 10:13:28  ounsy
//Domain* bug correction
//
//Revision 1.1  2005/12/14 16:50:15  ounsy
//added methods necessary for alternate attribute selection
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.AttributesSelectComboBox;
import fr.soleil.bensikin.containers.context.ContextActionPanel;
import fr.soleil.bensikin.containers.context.ContextAttributesPanelAlternate;
import fr.soleil.bensikin.data.attributes.BensikinDeviceClass;
import fr.soleil.bensikin.data.attributes.Domain;
import fr.soleil.bensikin.data.context.ContextAttribute;
import fr.soleil.bensikin.datasources.tango.ITangoAlternateSelectionManager;
import fr.soleil.bensikin.datasources.tango.TangoAlternateSelectionManagerFactory;
import fr.soleil.bensikin.models.AttributesSelectTableModel;


public class AddSelectedContextAttributeAlternateAction extends BensikinAction
{
	 /**
	  * @param name
	  */
	 public AddSelectedContextAttributeAlternateAction ( String name )
	 {
	     this.putValue( Action.NAME , name );
	 }
	 
	 /* (non-Javadoc)
	  * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	  */
	 public void actionPerformed ( ActionEvent arg0 ) 
	 {
	     ContextAttributesPanelAlternate attributesTabAlternate = ContextAttributesPanelAlternate.getInstance ();
	     
	     AttributesSelectComboBox domainComboBox = attributesTabAlternate.getDomainComboBox ();
	     AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox ();
	     AttributesSelectComboBox attributesSelectComboBox = attributesTabAlternate.getAttributeComboBox ();
	     
	     Domain selectedDomain = domainComboBox.getSelectedDomain ();
	     if ( selectedDomain == null )
	     {
	         BensikinDeviceClass selectedDeviceClass = deviceClassComboBox.getSelectedDeviceClass ();
	         selectedDomain = selectedDeviceClass.getDomain ();
	     }
	     if ( selectedDomain == null )
	     {
	         return;
	     }
	     
	     BensikinDeviceClass selectedDeviceClass = deviceClassComboBox.getSelectedDeviceClass ();
	     if ( selectedDeviceClass == null )
	     {
	         return;
	     }
	     
	     ContextAttribute selectedAttribute = attributesSelectComboBox.getSelectedAttribute ();
	     if ( selectedAttribute == null )
	     {
	         return;
	     }
	  
	     ITangoAlternateSelectionManager manager =  TangoAlternateSelectionManagerFactory.getCurrentImpl();
	     ContextAttribute [] selectedAttributes = manager.loadACAttributes ( selectedDeviceClass , selectedAttribute );
	     
	     AttributesSelectTableModel attributesSelectTableModel = AttributesSelectTableModel.getInstance ();

	     for ( int i = 0 ; i < selectedAttributes.length ; i ++ )
	     {
	         attributesSelectTableModel.updateList ( selectedAttributes [ i ] );    
	     }

	     
         ContextActionPanel.getInstance().allowPrint(false);
	 }
}
