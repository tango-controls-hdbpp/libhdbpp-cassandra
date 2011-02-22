//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/listeners/ACAttributesRecapTreeSelectionListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributesRecapTreeSelectionListener.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ACAttributesRecapTreeSelectionListener.java,v $
// Revision 1.4  2006/05/19 15:01:06  ounsy
// minor changes
//
// Revision 1.3  2006/02/24 12:17:04  ounsy
// small modifications
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
package fr.soleil.mambo.actions.archiving.listeners;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import fr.soleil.mambo.components.archiving.ACAttributesRecapTree;
import fr.soleil.mambo.containers.archiving.ACAttributeDetailHdbPanel;
import fr.soleil.mambo.containers.archiving.ACAttributeDetailTdbPanel;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.models.AttributesTreeModel;

//import mambo.models.ACAttributesTreeModel;

public class ACAttributesRecapTreeSelectionListener implements
		TreeSelectionListener {
	// private static DefaultMutableTreeNode currentNode;
	// private static ACAttributesTreeModel model;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent event) {
		// System.out.println
		// ("ACAttributesRecapTreeSelectionListener/valueChanged/START" );

		ACAttributesRecapTree tree = (ACAttributesRecapTree) event.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		// model = ( ACAttributesTreeModel ) tree.getModel();
		// if ( node == null || ! node.isLeaf () )
		if (node == null || node.getLevel() != 4) {
			// System.out.println ("out 1" );
			return;
		}

		// currentNode = node;

		TreeNode[] path = node.getPath();
		// String completeName =
		// AttributesTreeModel.translatePathIntoCompleteName ( path );
		String completeName = AttributesTreeModel.translatePathIntoKey(path);
		// System.out.println
		// ("ACAttributesRecapTreeSelectionListener/valueChanged/completeName|"+completeName+"|"
		// );

		ArchivingConfiguration selectedArchivingConfiguration = ArchivingConfiguration
				.getSelectedArchivingConfiguration();
		if (selectedArchivingConfiguration == null) {
			return;
		}
		ArchivingConfigurationAttributes attributes = selectedArchivingConfiguration
				.getAttributes();
		if (attributes == null) {
			return;
		}
		// System.out.println
		// ("ACAttributesRecapTreeSelectionListener/valueChanged/1" );
		ArchivingConfigurationAttribute selectedAttribute = attributes
				.getAttribute(completeName);

		if (selectedAttribute == null) {
			// System.out.println ("ACAttributesRecapTreeSelectionListener/null"
			// );
		} else {
			/*
			 * System.out.println
			 * ("ACAttributesRecapTreeSelectionListener/NOT null" );
			 * System.out.println ("----------------------------" );
			 * System.out.println ( selectedAttribute.toString() );
			 * System.out.println ("----------------------------" );
			 */

			ACAttributeDetailHdbPanel.getInstance().load(selectedAttribute);
			ACAttributeDetailTdbPanel.getInstance().load(selectedAttribute);
		}

	}

}
