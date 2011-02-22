//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextAttributesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextAttributesPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ContextAttributesPanel.java,v $
// Revision 1.10  2006/11/29 09:58:52  ounsy
// minor changes
//
// Revision 1.9  2006/05/03 13:04:14  ounsy
// modified the limited operator rights
//
// Revision 1.8  2006/03/28 15:09:12  ounsy
// Commented the setEnabled ( false ) on the attributes adding and removing actions.
// WARNING: this isn't the 1st time this correction is commited?!?
//
// Revision 1.7  2006/03/27 14:03:55  ounsy
// replaced deprecated enable and diasble calls
//
// Revision 1.6  2005/12/14 16:20:52  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:08  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:35  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.containers.context;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.context.AddSelectedContextAttributesAction;
import fr.soleil.bensikin.actions.context.MatchContextAttributesAction;
import fr.soleil.bensikin.actions.context.MatchPossibleContextAttributesAction;
import fr.soleil.bensikin.actions.context.RemoveSelectedContextAttributesAction;
import fr.soleil.bensikin.components.context.detail.ContextAttributesTree;
import fr.soleil.bensikin.components.context.detail.PossibleAttributesTree;
import fr.soleil.bensikin.components.context.detail.TreeBox;
import fr.soleil.bensikin.models.ContextAttributesTreeModel;
import fr.soleil.bensikin.models.PossibleAttributesTreeModel;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;

/**
 * Contains the attributes information about the current text:
 * -its attributes tree
 * -the tree of possible attributes
 *
 * @author CLAISSE
 */
public class ContextAttributesPanel extends JPanel
{
    private static ContextAttributesPanel contextAttributesPanelInstance = null;
    private JButton addSelectedAttributesButton;
    private JButton removeSelectedAttributesButton;

    private ContextAttributesTree rightTree;
    private TreeBox rightTreeBox;
    private TreeBox leftTreeBox;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static ContextAttributesPanel getInstance ()
    {
        if ( contextAttributesPanelInstance == null )
        {
            contextAttributesPanelInstance = new ContextAttributesPanel();
        }

        return contextAttributesPanelInstance;
    }


    /**
     * Builds the panel
     */
    private ContextAttributesPanel ()
    {
        super();
        Font font = new Font( "Arial" , Font.PLAIN , 11 );
        String msg;
        Box box = new Box( BoxLayout.X_AXIS ); //box containing the 2 TreeBoxes

        PossibleAttributesTree leftTree = PossibleAttributesTree.getInstance( new PossibleAttributesTreeModel() );

        JTextField leftRegexpField = new JTextField();
        msg = Messages.getMessage( "CONTEXT_DETAIL_MATCH" );
        JButton leftSearchButton = new JButton( new MatchPossibleContextAttributesAction( msg ) );
        leftSearchButton.setFocusPainted( false );
        leftSearchButton.setFocusable( false );
        leftSearchButton.setFont(font);
        GUIUtilities.setObjectBackground( leftSearchButton , GUIUtilities.CONTEXT_COLOR );

        leftTreeBox = new TreeBox();
        leftTreeBox.build( leftTree , leftRegexpField , leftSearchButton );
        box.add( leftTreeBox );

        //Box that contains the add and remove attributes buttons BEGIN
        Box transferBox = new Box( BoxLayout.Y_AXIS );

        ImageIcon plusIcon = new ImageIcon( Bensikin.class.getResource( "icons/right.gif" ) );
        ImageIcon minusIcon = new ImageIcon( Bensikin.class.getResource( "icons/delete.gif" ) );

        int maxIconHeight = Math.max( plusIcon.getIconHeight() , minusIcon.getIconHeight() );
        //int maxIconWidth = Math.max( plusIcon.getIconWidth() , minusIcon.getIconWidth() );
        //Dimension buttonDimension = new Dimension( maxIconWidth + 7 , maxIconHeight + 7 );

        msg = Messages.getMessage( "CONTEXT_DETAIL_ADD_SELECTED_ATTR" );
        /*addSelectedAttributesButton = new JButton( plusIcon );
        addSelectedAttributesButton.addActionListener( new AddSelectedContextAttributesAction( msg ) );*/
        addSelectedAttributesButton = new JButton( new AddSelectedContextAttributesAction( msg ) );
        addSelectedAttributesButton.setIcon ( plusIcon );
        addSelectedAttributesButton.setText ( "" );

        addSelectedAttributesButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        addSelectedAttributesButton.setBackground( Color.BLACK );
        addSelectedAttributesButton.setForeground( Color.BLACK );
        addSelectedAttributesButton.setFocusPainted( false );
        addSelectedAttributesButton.setFocusable( false );

        
        msg = Messages.getMessage( "CONTEXT_DETAIL_REMOVE_SELECTED_ATTR" );
        /*removeSelectedAttributesButton = new JButton( minusIcon );
        removeSelectedAttributesButton.addActionListener( new RemoveSelectedContextAttributesAction( msg ) );*/
        removeSelectedAttributesButton = new JButton( new RemoveSelectedContextAttributesAction( msg ) );
        removeSelectedAttributesButton.setIcon ( minusIcon );
        removeSelectedAttributesButton.setText ( "" );
        
        removeSelectedAttributesButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        removeSelectedAttributesButton.setBackground( Color.BLACK );
        removeSelectedAttributesButton.setForeground( Color.BLACK );
        removeSelectedAttributesButton.setFocusPainted( false );
        removeSelectedAttributesButton.setFocusable( false );

        transferBox.add( addSelectedAttributesButton );
        transferBox.add( Box.createVerticalStrut( maxIconHeight ) );
        transferBox.add( removeSelectedAttributesButton );
        box.add( transferBox );

        rightTree = ContextAttributesTree.getInstance( ContextAttributesTreeModel.getInstance ( false ) );

        JTextField rightRegexpField = new JTextField();
        msg = Messages.getMessage( "CONTEXT_DETAIL_MATCH" );
        JButton rightSearchButton = new JButton( MatchContextAttributesAction.getInstance( msg ) );
        rightSearchButton.setFocusPainted( false );
        rightSearchButton.setFocusable( false );
        rightSearchButton.setFont(font);
        GUIUtilities.setObjectBackground( rightSearchButton , GUIUtilities.CONTEXT_COLOR );

        rightTreeBox = new TreeBox();
        rightTreeBox.build( rightTree , rightRegexpField , rightSearchButton );
        box.add( rightTreeBox );

        this.setLayout( new BoxLayout( this , BoxLayout.X_AXIS ) );
        this.add( box );

        GUIUtilities.setObjectBackground( this , GUIUtilities.CONTEXT_COLOR );
    }

    /**
     * Enables the attributes transfer buttons
     */
    public void enableButtons ()
    {
        /*addSelectedAttributesButton.enable();
        removeSelectedAttributesButton.enable();*/
        addSelectedAttributesButton.setEnabled ( true );
        removeSelectedAttributesButton.setEnabled ( true );
    }


    /**
     * @return Returns the leftTreeBox.
     */
    public TreeBox getLeftTreeBox ()
    {
        return leftTreeBox;
    }

    /**
     * @return Returns the rightTreeBox.
     */
    public TreeBox getRightTreeBox ()
    {
        return rightTreeBox;
    }
}
