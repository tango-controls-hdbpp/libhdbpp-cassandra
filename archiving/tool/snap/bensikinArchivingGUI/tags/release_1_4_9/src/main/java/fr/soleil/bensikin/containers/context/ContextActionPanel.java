//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/containers/context/ContextActionPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ContextActionPanel.
//						(Claisse Laurent) - 16 juin 2005
//
// $Author: ounsy $
//
// $Revision: 1.9 $
//
// $Log: ContextActionPanel.java,v $
// Revision 1.9  2007/08/24 14:06:14  ounsy
// bug correction with context printing as text
//
// Revision 1.8  2007/08/23 15:28:48  ounsy
// Print Context as tree, table or text (Mantis bug 3913)
//
// Revision 1.7  2006/01/12 10:27:39  ounsy
// minor changes
//
// Revision 1.6  2005/12/14 16:19:08  ounsy
// removed the quick save button
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

import java.awt.Font;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import fr.soleil.bensikin.Bensikin;
import fr.soleil.bensikin.actions.context.ContextDetailPrintChooseAction;
import fr.soleil.bensikin.actions.context.LaunchSnapshotAction;
import fr.soleil.bensikin.actions.context.RegisterContextAction;
import fr.soleil.bensikin.actions.context.SaveSelectedContextAction;
import fr.soleil.bensikin.tools.GUIUtilities;
import fr.soleil.bensikin.tools.Messages;


/**
 * Contains the buttons used to launch actions on the current context.
 *
 * @author CLAISSE
 */
public class ContextActionPanel extends JPanel
{
    private ImageIcon registerIcon = new ImageIcon( Bensikin.class.getResource( "icons/register.gif" ) );
    private ImageIcon launchIcon = new ImageIcon( Bensikin.class.getResource( "icons/launch.gif" ) );
    private ImageIcon quickSaveIcon = new ImageIcon( Bensikin.class.getResource( "icons/quick_save.gif" ) );
    private ImageIcon printIcon = new ImageIcon( Bensikin.class.getResource( "icons/print.gif" ) );
    private static ContextActionPanel contextActionPanelInstance = null;
    private LaunchSnapshotAction launchSnapshotAction;
    private JButton registerContextButton;
    private JButton printContextButton;
    private ContextDetailPrintChooseAction printAction = null;

    /**
     * Instantiates itself if necessary, returns the instance.
     *
     * @return The instance
     */
    public static ContextActionPanel getInstance ()
    {
        if ( contextActionPanelInstance == null )
        {
            contextActionPanelInstance = new ContextActionPanel();
        }

        return contextActionPanelInstance;
    }

    /**
     * Updates the text of the "register" button
     */
    public void updateRegisterButton ()
    {
        String msg = Messages.getMessage( "CONTEXT_DETAIL_REGISTER_CONTEXT_AS" );
        registerContextButton.setText( msg );
//        registerContextButton.setToolTipText( msg );
        RegisterContextAction.getInstance().setEnabled( false );
    }

    /**
     * Resets the text of the "register" button to the default text
     */
    public void resetRegisterButton ()
    {
        String msg = Messages.getMessage( "CONTEXT_DETAIL_REGISTER_CONTEXT" );
        registerContextButton.setText( msg );
//        registerContextButton.setToolTipText( msg );
        RegisterContextAction.getInstance().setEnabled( false );
    }

    /**
     * Builds the panel
     */
    private ContextActionPanel ()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        String msg;
        Font font = new Font( "Arial" , Font.PLAIN , 11 );

        msg = Messages.getMessage("CONTEXT_DETAIL_PRINT_TITLE");
        printAction = new ContextDetailPrintChooseAction(msg);

        printContextButton = new JButton(printAction);
        printContextButton.setIcon(printIcon);
        printContextButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        printContextButton.setText("");
        printContextButton.setToolTipText(msg);
        GUIUtilities.setObjectBackground( printContextButton , GUIUtilities.CONTEXT_CLIPBOARD_COLOR );

        msg = Messages.getMessage( "CONTEXT_DETAIL_LAUNCH_SNAPSHOT" );
        launchSnapshotAction = LaunchSnapshotAction.getInstance( msg );
        JButton launchSnapshotButton = new JButton( launchSnapshotAction );
        launchSnapshotButton.setIcon( launchIcon );
        //launchSnapshotButton.setPreferredSize(new Dimension(140, 20));
        launchSnapshotButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        launchSnapshotButton.setFocusable( false );
        launchSnapshotButton.setFocusPainted( false );
        launchSnapshotButton.setFont(font);
        GUIUtilities.setObjectBackground( launchSnapshotButton , GUIUtilities.CONTEXT_COLOR );

        msg = Messages.getMessage( "CONTEXT_DETAIL_REGISTER_CONTEXT" );
        RegisterContextAction registerContextAction = RegisterContextAction.getInstance( msg );
        registerContextButton = new JButton( registerContextAction );
        registerContextButton.setIcon( registerIcon );
        //registerContextButton.setPreferredSize(new Dimension(83, 20));
        registerContextButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        registerContextButton.setFocusable( false );
        registerContextButton.setFocusPainted( false );
        registerContextButton.setFont(font);
        GUIUtilities.setObjectBackground( registerContextButton , GUIUtilities.CONTEXT_COLOR );

        msg = Messages.getMessage( "CONTEXT_DETAIL_SAVE_CONTEXT" );
        SaveSelectedContextAction saveSnapshotAction = new SaveSelectedContextAction( msg , true );
        JButton saveSnapshotButton = new JButton( saveSnapshotAction );
        saveSnapshotButton.setIcon( quickSaveIcon );
        //saveSnapshotButton.setPreferredSize(new Dimension(120, 20));
        saveSnapshotButton.setMargin( new Insets( 0 , 0 , 0 , 0 ) );
        saveSnapshotButton.setFocusable( false );
        saveSnapshotButton.setFocusPainted( false );
        saveSnapshotButton.setFont(font);
        GUIUtilities.setObjectBackground( saveSnapshotButton , GUIUtilities.CONTEXT_COLOR );

        //msg = Messages.getMessage( "CONTEXT_DETAIL_ACTIONS" );
        //JLabel actionsLabel = new JLabel( msg , SwingConstants.RIGHT );

        this.add(Box.createHorizontalGlue());
        this.add(printContextButton);
        this.add(Box.createHorizontalGlue());
        this.add( registerContextButton );
        this.add(Box.createHorizontalGlue());
        this.add( launchSnapshotButton );
        this.add(Box.createHorizontalGlue());
        //this.add( saveSnapshotButton );

//        this.setMaximumSize( new Dimension( Integer.MAX_VALUE , 60 ) );

        GUIUtilities.setObjectBackground( this , GUIUtilities.CONTEXT_COLOR );
        //msg = Messages.getMessage("CONTEXT_DETAIL_ATTRIBUTES_ALTERNATE_TITLE");
        /*TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(EtchedBorder.LOWERED), msg,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.TOP,
                GUIUtilities.getTitleFont());
        this.setBorder(tb);*/

        //GUIUtilities.addDebugBorderToPanel ( this , true , Color.GREEN , 1  );
    }

    /**
     * Calls the printAction
     */
    public void openPrintDialog() {
        if (printAction != null) {
            printAction.actionPerformed(null);
        }
    }

    public void allowPrint(boolean allowed) {
        if (printAction != null) {
            printAction.setEnabled(allowed);
        }
    }

}
