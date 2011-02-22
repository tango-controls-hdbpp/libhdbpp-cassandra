//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/MatchPossibleContextAttributesAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MatchPossibleContextAttributesAction.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: MatchPossibleContextAttributesAction.java,v $
// Revision 1.3  2006/04/10 08:46:54  ounsy
// Bensikin action now all inherit from BensikinAction for easy rights management
//
// Revision 1.2  2005/12/14 16:02:52  ounsy
// packages names have changed
//
// Revision 1.1  2005/12/14 14:07:17  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
// Revision 1.1.1.2  2005/08/22 11:58:33  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.actions.exceptions.FieldFormatException;
import fr.soleil.bensikin.components.context.detail.PossibleAttributesTree;
import fr.soleil.bensikin.containers.context.ContextAttributesPanel;
import fr.soleil.bensikin.datasources.tango.ITangoManager;
import fr.soleil.bensikin.datasources.tango.TangoManagerFactory;
import fr.soleil.bensikin.logs.ILogger;
import fr.soleil.bensikin.logs.LoggerFactory;
import fr.soleil.bensikin.models.PossibleAttributesTreeModel;
import fr.soleil.bensikin.tools.Messages;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;

/**
 * Filters Tango attributes, keeping only those matching the pattern
 * <UL>
 * <LI>Gets the pattern from the display area and builds a Criterions object with it
 * <LI>Uses the application's ITangoManager to load the attributes for this Criterions
 * <LI>Refreshes the current PossibleAttributesTreeModel instance with this new list of attributes
 * <LI>Logs the action's success or failure
 * </UL>
 *
 * @author CLAISSE
 */
public class MatchPossibleContextAttributesAction extends BensikinAction
{

    /**
     * Standard action constructor that sets the action's name.
     *
     * @param name The action name
     */
    public MatchPossibleContextAttributesAction ( String name )
    {
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        String pattern = ContextAttributesPanel.getInstance().getLeftTreeBox().getRegExp();

        Criterions searchCriterions = null;
        try
        {
            searchCriterions = MatchContextAttributesAction.getAttributesSearchCriterions( pattern );
        }
        catch ( FieldFormatException e1 )
        {
            e1.printStackTrace();
            return;
        }

        ILogger logger = LoggerFactory.getCurrentImpl();
        try
        {
            PossibleAttributesTree tree = PossibleAttributesTree.getInstance();
            PossibleAttributesTreeModel model = ( PossibleAttributesTreeModel ) tree.getModel();

            ITangoManager source = TangoManagerFactory.getCurrentImpl();

            DefaultMutableTreeNode root = ( DefaultMutableTreeNode ) model.getRoot();
            root.removeAllChildren();
            model.setRoot( root );

            model.build( source , searchCriterions );

            tree.setModel( model );
            String msg = Messages.getLogMessage( "LOAD_POSSIBLE_ATTRIBUTES_OK" );
            logger.trace( ILogger.LEVEL_DEBUG , msg );
            PossibleAttributesTree.getInstance().expandAll(true);
        }
        catch ( Exception e )
        {
            String msg = Messages.getLogMessage( "LOAD_POSSIBLE_ATTRIBUTES_KO" );
            logger.trace( ILogger.LEVEL_ERROR , msg );
            logger.trace( ILogger.LEVEL_ERROR , e );
            return;
        }
    }
}
