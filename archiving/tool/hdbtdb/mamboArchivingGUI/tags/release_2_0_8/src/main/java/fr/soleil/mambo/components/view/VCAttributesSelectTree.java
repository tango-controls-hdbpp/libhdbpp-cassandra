//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/view/VCAttributesSelectTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCAttributesSelectTree.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: VCAttributesSelectTree.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.4  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
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
import javax.swing.tree.TreePath;

import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;



public class VCAttributesSelectTree extends AttributesTree
{
    /**
     * @param newModel
     */
    private VCAttributesSelectTree ( TreeModel newModel )
    {
        super( newModel );
        //this.addTreeSelectionListener ( new ACAttributesTreeSelectionListener () );
        this.setCellRenderer( new VCTreeRenderer() );

        this.setExpandsSelectedPaths( true );
        this.setScrollsOnExpand( true );
        this.setShowsRootHandles( true );
        this.setToggleClickCount( 1 );

        //this.addTreeWillExpandListener ( new TestExpansionListener () );
    }

    private static VCAttributesSelectTree instance = null;

    /**
     * @param newModel
     * @return 8 juil. 2005
     */
    public static VCAttributesSelectTree getInstance ( TreeModel newModel )
    {
        if ( instance == null )
        {
            instance = new VCAttributesSelectTree( newModel );
        }

        return instance;
    }

    /**
     * @return 8 juil. 2005
     */
    public static VCAttributesSelectTree getInstance ()
    {
        return instance;
    }

    public void setExpandedState ( TreePath path , boolean state )
    {
        super.setExpandedState( path , state );
    }
}
