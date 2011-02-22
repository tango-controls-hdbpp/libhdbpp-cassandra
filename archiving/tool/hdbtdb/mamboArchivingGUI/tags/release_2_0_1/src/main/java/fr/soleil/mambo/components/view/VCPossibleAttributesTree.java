//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/view/VCPossibleAttributesTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCPossibleAttributesTree.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: VCPossibleAttributesTree.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
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
package fr.soleil.mambo.components.view;

import javax.swing.tree.TreeModel;

import fr.soleil.mambo.actions.view.listeners.VCPossibleAttributesTreeSelectionListener;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;


public class VCPossibleAttributesTree extends AttributesTree
{
    /**
     * @param newModel
     */
    private VCPossibleAttributesTree ( TreeModel newModel )
    {
        super( newModel );
        this.addTreeSelectionListener( new VCPossibleAttributesTreeSelectionListener() );
        this.setCellRenderer( new VCTreeRenderer() );

        this.setExpandsSelectedPaths( true );
        this.setScrollsOnExpand( true );
        this.setShowsRootHandles( true );
        this.setToggleClickCount( 1 );
    }

    private static VCPossibleAttributesTree possibleAttributesTreeInstance = null;

    /**
     * @param newModel
     * @return 8 juil. 2005
     */
    public static VCPossibleAttributesTree getInstance ( TreeModel newModel )
    {
        if ( possibleAttributesTreeInstance == null )
        {
            possibleAttributesTreeInstance = new VCPossibleAttributesTree( newModel );
        }

        return possibleAttributesTreeInstance;
    }

    public static VCPossibleAttributesTree forceInstance ( TreeModel newModel )
    {
        possibleAttributesTreeInstance = new VCPossibleAttributesTree( newModel );

        return possibleAttributesTreeInstance;
    }

    /**
     * @return 8 juil. 2005
     */
    public static VCPossibleAttributesTree getInstance ()
    {
        return possibleAttributesTreeInstance;
    }
}
