//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/view/VCAttributesPropertiesTree.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCAttributesPropertiesTree.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: VCAttributesPropertiesTree.java,v $
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
// Revision 1.3  2006/05/16 09:36:27  ounsy
// minor changes
//
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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.mambo.actions.view.listeners.VCAttributesPropertiesTreeSelectionListener;
import fr.soleil.mambo.components.AttributesTree;
import fr.soleil.mambo.components.renderers.VCTreeRenderer;
import fr.soleil.mambo.data.view.ViewConfigurationAttribute;
import fr.soleil.mambo.models.AttributesTreeModel;



public class VCAttributesPropertiesTree extends AttributesTree
{
	private TreePath[] lastSelectionsPath;
	private TreePath[] currentSelectionsPath;

    HashMap expandedRowPropTree;

    /**
     * @param newModel
     */
    private VCAttributesPropertiesTree ( TreeModel newModel )
    {
        super( newModel );
        this.addTreeSelectionListener( VCAttributesPropertiesTreeSelectionListener.getInstance() );
//        this.addFocusListener( VCAttributesPropertiesTreeSelectionListener.getInstance() );
        this.setCellRenderer( new VCTreeRenderer() );

        this.setExpandsSelectedPaths( true );
        this.setScrollsOnExpand( true );
        this.setShowsRootHandles( true );
        this.setToggleClickCount( 1 );

        lastSelectionsPath  = null;
        currentSelectionsPath = null;
    }

    private static VCAttributesPropertiesTree instance = null;

    /**
     * @param newModel
     * @return 8 juil. 2005
     */
    public static VCAttributesPropertiesTree getInstance ( TreeModel newModel )
    {
        if ( instance == null )
        {
            instance = new VCAttributesPropertiesTree( newModel );
        }

        return instance;
    }

    /**
     * @return 8 juil. 2005
     */
    public static VCAttributesPropertiesTree getInstance ()
    {
        return instance;
    }

    /**
     * @return 22 juil. 2005
     */
    public Vector getListOfAttributesToSet ()
    {
    	saveLastSelectionPath();

    	TreePath[] selectedPath = currentSelectionsPath;

    	return treePathToVector(selectedPath);

    }

	public Vector getLastListOfAttributesToSet() {

		TreePath[] selectedPath = lastSelectionsPath;

    	return treePathToVector(selectedPath);

	}

	private Vector treePathToVector(TreePath[] selectedPath) {
		if ( selectedPath == null || selectedPath.length == 0 )
        {
            return null;
        }

        Vector attributes = new Vector();
        for ( int i = 0 ; i < selectedPath.length ; i++ )
        {
            TreePath currentSelectedTreePath = selectedPath[ i ];
            DefaultMutableTreeNode currentSelectedNode = ( DefaultMutableTreeNode ) currentSelectedTreePath.getLastPathComponent();
            //String name = ( String ) currentSelectedNode.getUserObject();

            Enumeration enumeration = currentSelectedNode.preorderEnumeration();
            while ( enumeration.hasMoreElements() )
            {
                DefaultMutableTreeNode currentTraversedNode = ( DefaultMutableTreeNode ) enumeration.nextElement();

                if ( currentTraversedNode.getLevel() == AttributesTreeModel.CONTEXT_TREE_DEPTH - 1 )
                {
                    TreeNode[] path = currentTraversedNode.getPath();
                    //String completeName = AttributesTreeModel.translatePathIntoCompleteName ( path );// to do:declare as protected again
                    String completeName = AttributesTreeModel.translatePathIntoKey( path );// to do:declare as protected again

                    ViewConfigurationAttribute attr = new ViewConfigurationAttribute();
                    attr.setCompleteName( completeName );
                    attributes.add( attr );
                }
            }
        }

        return attributes;
	}

	public void saveLastSelectionPath() {
		lastSelectionsPath = currentSelectionsPath;
        currentSelectionsPath = this.getSelectionPaths();
	}


	public void saveExpandedPath(){
		expandedRowPropTree = new HashMap();
    	for(int i = 0; i < instance.getRowCount(); i++){
    		if( instance.isExpanded(i) ){
    			expandedRowPropTree.put( i , instance.getPathForRow(i) );
    		}
    	}
    }

    public void openExpandedPath(){
    	try{
    		for(int i = 0 ; i < instance.getRowCount() ; i++){
    	    	Iterator it = expandedRowPropTree.keySet().iterator();
    			int j;
    			while(it.hasNext()) {
    				j = ( (Integer)it.next() ).intValue();
    				if( instance.getPathForRow( i ).toString().equals( ((TreePath)expandedRowPropTree.get( j )).toString() ) ){
    					instance.expandRow( i );
    				}
    			}
    		}
	    }catch(Exception e){
		}
    }

}
