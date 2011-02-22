//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/MatchPossibleVCAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MatchPossibleVCAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: MatchPossibleVCAttributesAction.java,v $
// Revision 1.5  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.4  2006/07/18 10:23:16  ounsy
// Less time consuming by setting tree expanding on demand only
//
// Revision 1.3  2006/05/19 15:03:05  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/15 10:30:05  chinkumo
// Third commit !
//
// Revision 1.1.2.1  2005/09/14 15:40:16  chinkumo
// First commit !
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

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.actions.MatchCAttributesAction;
import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.containers.view.dialogs.AttributesTab;
import fr.soleil.mambo.datasources.db.attributes.AttributeManagerFactory;
import fr.soleil.mambo.datasources.db.attributes.IAttributeManager;
import fr.soleil.mambo.models.VCPossibleAttributesTreeModel;


public class MatchPossibleVCAttributesAction extends MatchCAttributesAction
{

    /**
     * @param name
     */
    public MatchPossibleVCAttributesAction ( String name )
    {
        super( name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        AttributesTab attributesTab = AttributesTab.getInstance();
        String pattern = attributesTab.getLeftRegexp();

        Criterions searchCriterions = super.getAttributesSearchCriterions( pattern );
        //String traceSearchCriterions = searchCriterions == null ? "null" : searchCriterions.toString();
        IAttributeManager manager = AttributeManagerFactory.getCurrentImpl();

        VCPossibleAttributesTreeModel model = VCPossibleAttributesTreeModel.getInstance();
        boolean historic = model.isHistoric();
        model.build( manager , searchCriterions , historic );

        VCPossibleAttributesTree leftTree = VCPossibleAttributesTree.getInstance();
        leftTree.setModel( model );

        //leftTree.expandAll( true );
    }
}
