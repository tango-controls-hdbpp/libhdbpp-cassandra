//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/MatchACAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MatchACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: MatchACAttributesAction.java,v $
// Revision 1.5  2006/09/20 12:49:40  ounsy
// changed imports
//
// Revision 1.4  2006/08/07 13:03:07  ounsy
// trees and lists sort
//
// Revision 1.3  2006/07/18 10:21:55  ounsy
// Less time consuming by setting tree expanding on demand only
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
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.Criterions;
import fr.soleil.mambo.actions.MatchCAttributesAction;
import fr.soleil.mambo.components.archiving.ACAttributesSelectTree;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesTab;
import fr.soleil.mambo.datasources.tango.standard.ITangoManager;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;
import fr.soleil.mambo.models.ACAttributesTreeModel;

public class MatchACAttributesAction extends MatchCAttributesAction {

	/**
     * 
     */
    private static final long serialVersionUID = 4791182517435015100L;

    /**
	 * @param name
	 */
	public MatchACAttributesAction(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// System.out.println ( "MatchACAttributesAction/actionPerformed/START"
		// );

		AttributesTab attributesTab = AttributesTab.getInstance();
		String pattern = attributesTab.getRightRegexp();
		Criterions searchCriterions = super
				.getAttributesSearchCriterions(pattern);

		ITangoManager source = TangoManagerFactory.getCurrentImpl();
		ACAttributesTreeModel model = ACAttributesTreeModel.getInstance();
		model.match(source, searchCriterions);

		// We expand the results from 1 level of depth
		ACAttributesSelectTree tree = ACAttributesSelectTree.getInstance();
        tree.expandAll1Level(true);
	}
}
