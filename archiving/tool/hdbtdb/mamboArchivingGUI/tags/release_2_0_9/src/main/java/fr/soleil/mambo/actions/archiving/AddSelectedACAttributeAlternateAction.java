//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/AddSelectedACAttributeAlternateAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  AddSelectedACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.8 $
//
//$Log: AddSelectedACAttributeAlternateAction.java,v $
//Revision 1.8  2006/11/23 14:24:57  ounsy
//corrected a bug when a domain in selected
//
//Revision 1.7  2006/10/19 12:35:13  ounsy
//minor changes
//
//Revision 1.6  2006/09/20 12:52:15  ounsy
//changed imports
//
//Revision 1.5  2006/09/19 14:02:18  ounsy
//small bug correction
//
//Revision 1.4  2006/08/07 13:03:07  ounsy
//trees and lists sort
//
//Revision 1.3  2006/07/18 10:21:56  ounsy
//Less time consuming by setting tree expanding on demand only
//
//Revision 1.2  2006/03/07 14:38:41  ounsy
//"Domain *" bug correction
//
//Revision 1.1  2005/11/29 18:27:07  chinkumo
//no message
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
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.AttributesSelectComboBox;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesTabAlternate;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.attributes.Domain;
import fr.soleil.mambo.data.attributes.MamboDeviceClass;
import fr.soleil.mambo.datasources.tango.alternate.ITangoAlternateSelectionManager;
import fr.soleil.mambo.datasources.tango.alternate.TangoAlternateSelectionManagerFactory;
import fr.soleil.mambo.models.AttributesSelectTableModel;



public class AddSelectedACAttributeAlternateAction extends AbstractAction
{
    /**
     * @param name
     */
    public AddSelectedACAttributeAlternateAction ( String name )
    {
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        AttributesTabAlternate attributesTabAlternate = AttributesTabAlternate.getInstance();
        AttributesSelectComboBox domainComboBox = attributesTabAlternate.getDomainComboBox();
        AttributesSelectComboBox deviceClassComboBox = attributesTabAlternate.getDeviceClassComboBox();
        AttributesSelectComboBox attributesSelectComboBox = attributesTabAlternate.getAttributeComboBox();
        
        Domain selectedDomain = domainComboBox.getSelectedDomain();
        MamboDeviceClass selectedDeviceClass = deviceClassComboBox.getSelectedDeviceClass();
        if ( selectedDeviceClass == null )
        {
            return;
        }
        ArchivingConfigurationAttribute selectedAttribute = attributesSelectComboBox.getSelectedAttribute();
        if ( selectedAttribute == null )
        {
            return;
        }
        
        ITangoAlternateSelectionManager manager = TangoAlternateSelectionManagerFactory.getCurrentImpl();
        ArchivingConfigurationAttribute[] selectedAttributes = manager.loadACAttributes( selectedDeviceClass , selectedAttribute );

        AttributesSelectTableModel attributesSelectTableModel = AttributesSelectTableModel.getInstance();
        for ( int i = 0 ; i < selectedAttributes.length ; i++ )
        {
            ArchivingConfigurationAttribute nextAttr = selectedAttributes[ i ];
            String nextAttrDomainName = nextAttr.getDomainName ();
            //System.out.println ( "AddSelectedACAttributeAlternateAction/actionPerformed/i|"+i+"|nextAttrDomainName|"+nextAttrDomainName );
            if ( selectedDomain == null || selectedDomain.getName ().equals ( nextAttrDomainName ) )
            {
                attributesSelectTableModel.updateList( nextAttr );   
            }
        }
    }
}
