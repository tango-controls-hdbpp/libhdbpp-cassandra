//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/components/renderers/ACTreeRenderer.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  TreeRenderer.
//						(Chinkumo Jean) - Mar 3, 2004
//
//$Author: ounsy $
//
//$Revision: 1.4 $
//
//$Log: ACTreeRenderer.java,v $
//Revision 1.4  2006/08/07 13:03:07  ounsy
//trees and lists sort
//
//Revision 1.3  2006/07/28 10:06:45  ounsy
//a bit simplified
//
//Revision 1.2  2005/11/29 18:27:45  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//Revision 1.5  2005/06/24 12:06:33  chinkumo
//Some constants were moved from fr.soleil.hdbtdbArchivingApi.ArchivingApi.ConfigConst to fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.GlobalConst.
//This change was reported here.
//
//Revision 1.4  2005/06/14 10:49:05  chinkumo
//Branch (archivingGUI_1_0_1-branch_0)  and HEAD merged.
//
//Revision 1.3.4.1  2005/06/13 12:16:43  chinkumo
//Changes made to improve the management of exceptions were reported here.
//
//Revision 1.3  2005/02/04 17:00:29  chinkumo
//The icons building strategy was modified.
//
//Revision 1.2  2005/01/26 16:24:51  chinkumo
//Export of the new application source code
//
//Revision 1.1  2004/12/06 16:52:24  chinkumo
//First commit (new architecture).
//
//Revision 1.2  2004/09/01 16:09:34  chinkumo
//Heading was updated.
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.mambo.components.renderers;

import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import fr.soleil.hdbtdbArchivingApi.ArchivingManagerApi.ArchivingManagerApi;
import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;

public class ACTreeRenderer extends DefaultTreeCellRenderer
{

    protected final ImageIcon rootIcon = new ImageIcon( Mambo.class.getResource( "icons/host.gif" ) );
    protected final ImageIcon disabledIcon = new ImageIcon( Mambo.class.getResource( "icons/bulbDisabled.gif" ) );
    protected final ImageIcon enabledHIcon = new ImageIcon( Mambo.class.getResource( "icons/bulbEnabled.gif" ) );
    protected final ImageIcon enabledTIcon = new ImageIcon( Mambo.class.getResource( "icons/bulbEnabledTemp.gif" ) );
    protected final ImageIcon enabledBothIcon = new ImageIcon( Mambo.class.getResource( "icons/bulbBoth.gif" ) );

    public ACTreeRenderer ()
    {
        super();
    }

    public Component getTreeCellRendererComponent ( JTree tree ,
                                                    Object value ,
                                                    boolean sel ,
                                                    boolean expanded ,
                                                    boolean leaf ,
                                                    int row ,
                                                    boolean hasFocus )
    {
        Font font;
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;
        JLabel label = (JLabel)super.getTreeCellRendererComponent( tree , value , sel , expanded , leaf , row , hasFocus );
        if ( node.getLevel() == 4 )
        {
            boolean iah = false , iat = false;
            String nameH = getFullAttributeNameFromNode( true , node );
            String nameT = getFullAttributeNameFromNode( false , node );
            ArchivingConfiguration currentArchivingConfiguration = ArchivingConfiguration.getCurrentArchivingConfiguration();
            String name;
            if (currentArchivingConfiguration.isHistoric())
            {
                name = nameH;
            }
            else
            {
                name = nameT;
            }
            try
            {
                ArchivingConfigurationAttribute attribute = new ArchivingConfigurationAttribute();
                attribute.setCompleteName( nameH );
                iah = attribute.isArchived( true );

                attribute.setCompleteName( nameT );
                iat = attribute.isArchived( false );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

            if ( iah && iat )
            {
                label.setIcon( enabledBothIcon );
            }
            else if ( iah )
            {
                label.setIcon( enabledHIcon );
            }
            else if ( iat )
            {
                label.setIcon( enabledTIcon );
            }
            else
            {
                label.setIcon( disabledIcon );
            }


            if ( currentArchivingConfiguration.containsAttribute( name ) )
            {
                if ( currentArchivingConfiguration.getAttributes().getAttribute( name ).isEmpty() )
                {
                    font = new Font( "SansSerif" , Font.ITALIC , 12 );
                }
                else
                {
                    font = new Font( "SansSerif" , Font.BOLD , 12 );
                }
            }
            else
            {
                font = new Font( "SansSerif" , Font.ITALIC , 12 );
            }

        }
        else
        {
            if ( node.isRoot() )
            {
                label.setIcon( rootIcon );
            }
            else
            {
                label.setIcon( UIManager.getIcon( "Tree.closedIcon" ) );
            }
            font = new Font( "SansSerif" , Font.PLAIN , 12 );
        }

        label.setFont(font);
        label.setToolTipText(label.getText());

        return label;
    }


    /**
     * This method checks the complete name of an attribute according to a database.
     *
     * @param historic a boolean to know to which database the name of the attribute shoud be asked
     * @param value the node object that represent an attribute
     * @return The complete name of the attribute according to the database.
     */
    public static String getFullAttributeNameFromNode ( boolean historic , Object value )
    {
        String name = "";
        boolean facility = ( historic ? ArchivingManagerApi.getHdbFacility() : ArchivingManagerApi.getTdbFacility() );
        DefaultMutableTreeNode node = ( DefaultMutableTreeNode ) value;
        TreeNode[] treeNode = node.getPath();
        name = ( ( facility ) ? "//" + treeNode[ 0 ].toString() + "/" : "" ) +
               treeNode[ 1 ].toString() + "/" +
               treeNode[ 2 ].toString() + "/" +
               treeNode[ 3 ].toString() + "/" +
               treeNode[ 4 ].toString();
        return name;
    }
}
