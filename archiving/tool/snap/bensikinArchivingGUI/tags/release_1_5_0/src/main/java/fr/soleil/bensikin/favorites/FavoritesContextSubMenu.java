//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/favorites/FavoritesContextSubMenu.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  FavoritesContextSubMenu.
//						(Claisse Laurent) - 22 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: FavoritesContextSubMenu.java,v $
// Revision 1.6  2006/03/27 14:04:34  ounsy
// favorites contexts now have a label
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:38  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.favorites;

import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import fr.soleil.bensikin.containers.sub.dialogs.open.AddFavoriteContextDialog;
import fr.soleil.bensikin.data.context.Context;
import fr.soleil.bensikin.xml.XMLLine;


/**
 * A particular case of favorites sub-menu, for contexts.
 *
 * @author CLAISSE
 */
public class FavoritesContextSubMenu extends FavoritesSubMenu
{
	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "contexts";

	/**
	 * Builds a FavoritesContextSubMenu of name <code>_menuName</code>
	 *
	 * @param _menuName The name as it will appear in the favorites menu.
	 */
	public FavoritesContextSubMenu(String _menuName)
	{
		super(_menuName , FavoritesSubMenu.CONTEXT_TYPE);
	}

	/**
	 * Adds an context reference to the existing structure.
	 *
	 * @param selectedTreePath The path in the recursive TreeMap where this context reference will be added
	 * @param id               The id to use for this context reference
	 */
	public void addContext(TreePath selectedTreePath , String id , String label) throws IllegalArgumentException
	{
	    /*System.out.println	( "CLA/FavoritesContextSubMenu/addContext/before----------------------" );
	    trace ( this.favoritesSubMenuElements );
	    System.out.println	( "CLA/FavoritesContextSubMenu/addContext/before----------------------");
	    */
	    if ( selectedTreePath == null )
		{
			XMLLine line = new XMLLine(Context.XML_TAG);
			line.setId ( id );
			line.setLabel ( label );

			//this.favoritesSubMenuElements.put(line.getId() , line);
			//WARNING
			this.favoritesSubMenuElements.put(line.getItemName() , line);
				
		}
		else
		{
			Object[] path_o = selectedTreePath.getPath();
			String[] path = new String[ path_o.length - 1 ];
			for ( int i = 0 ; i < path_o.length - 1 ; i++ )
			{
				DefaultMutableTreeNode currentPathElement = ( DefaultMutableTreeNode ) path_o[ i + 1 ];
				//if the node isn't a directory node but a context node
				if ( currentPathElement.getUserObject ().getClass ().equals ( DefaultMutableTreeNode.class ) )
				{
				    throw new IllegalArgumentException ( "The selected node isn't a directory node but a context node" );
				}
				String currentPathElementName = ( String ) currentPathElement.getUserObject();
				path[ i ] = currentPathElementName;
			}

			TreeMap currentFavoritesSubMenuElements = this.favoritesSubMenuElements;

			currentFavoritesSubMenuElements = addSubMenu(currentFavoritesSubMenuElements , path , id , label);
			this.favoritesSubMenuElements = currentFavoritesSubMenuElements;
		}

	    /*System.out.println	( "CLA/FavoritesContextSubMenu/addContext/after----------------------" );
	    trace ( this.favoritesSubMenuElements );
	    System.out.println	( "CLA/FavoritesContextSubMenu/addContext/after----------------------");
	    */
	    this.build(this.favoritesSubMenuElements);
		
		AddFavoriteContextDialog dialog = AddFavoriteContextDialog.getInstance();
		dialog.reBuildTree(this.treeModel);
	}

	/**
	 * Adds a context directory to the existing structure, then refreshes the tree in the favorite contexts dialog
	 *
	 * @param selectedTreePath   The path in the recursive TreeMap where this directory will be added
	 * @param nameOfNewDirectory The name of the new directory
	 */
	public void addDirectory(TreePath selectedTreePath , String nameOfNewDirectory)
	{
		super.addDirectory(selectedTreePath , nameOfNewDirectory);

		AddFavoriteContextDialog dialog = AddFavoriteContextDialog.getInstance();
		dialog.reBuildTree(this.treeModel);
	}

    /**
     * @param selectedTreePath
     */
    public void removeItem(TreePath selectedTreePath) 
    {
        super.removeItem ( selectedTreePath );

		AddFavoriteContextDialog dialog = AddFavoriteContextDialog.getInstance ();
		dialog.reBuildTree ( this.treeModel );        
    }

}
