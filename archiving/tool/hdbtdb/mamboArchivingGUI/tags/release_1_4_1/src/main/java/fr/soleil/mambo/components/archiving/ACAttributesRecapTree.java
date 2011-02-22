//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACAttributesRecapTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACAttributesRecapTree.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ACAttributesRecapTree.java,v $
// Revision 1.2  2005/11/29 18:27:24  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/15 10:30:05  chinkumo
// Third commit !
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
package fr.soleil.mambo.components.archiving;

import javax.swing.tree.TreeModel;

import fr.soleil.mambo.actions.archiving.listeners.ACAttributesRecapTreeSelectionListener;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.ACTreeRenderer;



public class ACAttributesRecapTree extends AttributesTree
{
    /**
     * @param newModel
     */
    private ACAttributesRecapTree ( TreeModel newModel )
    {
        super( newModel );
        this.addTreeSelectionListener( new ACAttributesRecapTreeSelectionListener() );
        this.setCellRenderer( new ACTreeRenderer() );

        this.setExpandsSelectedPaths( true );
        this.setScrollsOnExpand( true );
        this.setShowsRootHandles( true );
        this.setToggleClickCount( 1 );
    }

    private static ACAttributesRecapTree instance = null;

    /**
     * @param newModel
     * @return 8 juil. 2005
     */
    public static ACAttributesRecapTree getInstance ( TreeModel newModel )
    {
        if ( instance == null )
        {
            instance = new ACAttributesRecapTree( newModel );
        }

        return instance;
    }

    /**
     * @return 8 juil. 2005
     */
    public static ACAttributesRecapTree getInstance ()
    {
        return instance;
    }
}
