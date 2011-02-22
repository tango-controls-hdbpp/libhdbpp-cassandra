//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/view/VCAttributesRecapTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCAttributesRecapTree.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: VCAttributesRecapTree.java,v $
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
//
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

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
//import javax.swing.tree.TreePath;



public class VCAttributesRecapTree extends AttributesTree
{
	
	HashMap expandedRowRecapTree;

    /**
     * @param newModel
     */
    private VCAttributesRecapTree ( TreeModel newModel )
    {
        super( newModel );
        //this.addTreeSelectionListener ( new ACAttributesTreeSelectionListener () );
        this.setCellRenderer( new VCTreeRenderer() );

        this.setExpandsSelectedPaths( true );
        this.setScrollsOnExpand( true );
        this.setShowsRootHandles( true );
        this.setToggleClickCount( 1 );

        //this.addTreeWillExpandListener ( new TestExpansionListener () );

        DefaultMutableTreeNode[] test_n = new DefaultMutableTreeNode[ 4 ];
        test_n[ 0 ] = new DefaultMutableTreeNode( "HDB" );
        test_n[ 1 ] = new DefaultMutableTreeNode( "tango" );
        test_n[ 2 ] = new DefaultMutableTreeNode( "tangotest" );
        test_n[ 3 ] = new DefaultMutableTreeNode( "1" );

        //String[] test_s = {"HDB", "tango", "tangotest", "1"};
        //TreePath testPath = new TreePath( test_s );

        //TreePath testPath = new TreePath (test_n);
        //this.makeVisible ( testPath );
        //this.expandPath (testPath);
    }

    private static VCAttributesRecapTree instance = null;

    /**
     * @param newModel
     * @return 8 juil. 2005
     */
    public static VCAttributesRecapTree getInstance ( TreeModel newModel )
    {
        if ( instance == null )
        {
            instance = new VCAttributesRecapTree( newModel );
        }

        return instance;
    }

    /**
     * @return 8 juil. 2005
     */
    public static VCAttributesRecapTree getInstance ()
    {
        return instance;
    }

    public void saveExpandedPath(){
    	expandedRowRecapTree = new HashMap();
    	for(int i = 0; i < instance.getRowCount(); i++){
    		if( instance.isExpanded(i) ){
    			expandedRowRecapTree.put( i , instance.getPathForRow(i) );
    		}
    	}
    }
    
    public void openExpandedPath(){
    	for(int i = 0 ; i < instance.getRowCount() ; i++){
        	Iterator it = expandedRowRecapTree.keySet().iterator();
    		int j;
    		while(it.hasNext()) {
    			j = ( (Integer)it.next() ).intValue();
				if( instance.getPathForRow( i ).toString().equals( ((TreePath)expandedRowRecapTree.get( j )).toString() ) ){
					instance.expandPath( instance.getPathForRow( i ) );
				}
    		}
		}
    }
}
