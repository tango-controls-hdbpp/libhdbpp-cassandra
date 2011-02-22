//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/models/PossibleAttributesTreeModel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PossibleAttributesTreeModel.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: PossibleAttributesTreeModel.java,v $
// Revision 1.5  2005/12/14 16:43:02  ounsy
// minor changes
//
// Revision 1.4  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:40  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.models;

import java.util.Vector;

import fr.soleil.bensikin.datasources.tango.ITangoManager;
import fr.soleil.bensikin.datasources.tango.TangoManagerFactory;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;

/**
 * A daughter of AttributesTreeModel representing the Tango attributes hierarchy.
 *
 * @author CLAISSE
 */
public class PossibleAttributesTreeModel extends AttributesTreeModel
{

    /**
     * Calls mother constructor, and builds itself with the list all Tango attributes found by the current implementation of <code>ITangoManager</code>.
     */
    public PossibleAttributesTreeModel ()
    {
        super();

        ITangoManager source = TangoManagerFactory.getCurrentImpl();
        this.build( source , null );
    }

    /**
     * Rebuilds itself using all Tango attributes found by the current implementation of <code>ITangoManager</code>, for the given search criterions.
     *
     * @param source           The <code>ITangoManager</code> implementation to use to look up Tango attributes
     * @param searchCriterions The search criterions for this request
     */
    public void build ( ITangoManager source , Criterions searchCriterions )
    {
        Vector _domains = source.loadDomains( searchCriterions );
        this.build( _domains );
    }
}
