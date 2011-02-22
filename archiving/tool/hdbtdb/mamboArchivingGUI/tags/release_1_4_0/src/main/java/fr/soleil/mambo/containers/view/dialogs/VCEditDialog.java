//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/dialogs/VCEditDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCEditDialog.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.8 $
//
// $Log: VCEditDialog.java,v $
// Revision 1.8  2007/05/11 09:14:01  ounsy
// VCEditDialog resized
//
// Revision 1.7  2007/05/10 14:48:16  ounsy
// possibility to change "no value" String in chart data file (default is "*") through vc option
//
// Revision 1.6  2007/01/11 14:05:46  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.5  2006/10/02 14:13:25  ounsy
// minor changes (look and feel)
//
// Revision 1.4  2006/01/24 12:51:29  ounsy
// Bug of the new VC replacing the former selected VC corrected
//
// Revision 1.3  2005/12/15 11:32:18  ounsy
// minor changes
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
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
package fr.soleil.mambo.containers.view.dialogs;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import fr.soleil.mambo.actions.CancelAction;
import fr.soleil.mambo.actions.view.VCBackAction;
import fr.soleil.mambo.actions.view.VCFinishAction;
import fr.soleil.mambo.actions.view.VCNextAction;
import fr.soleil.mambo.components.view.VCCustomTabbedPane;
import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class VCEditDialog extends JDialog
{
    //private Dimension dim = new Dimension ( 700 , 700 ); 
    
    private GeneralTab generalTab;
    private AttributesTab attributesTab;
    private AttributesPlotPropertiesTab attributesPlotPropertiesTab;
    private ExpressionTab expressionTab;
    private JScrollPane generalScrollPane;
    private JScrollPane attributesScrollPane;
    private JScrollPane attributesPlotPropertiesScrollPane;
    private JScrollPane expressionScrollPane;

    private JPanel myPanel;
    private JButton backButton;
    private JButton nextButton;
    private JButton finishButton;
    private JButton cancelButton;
    private JTabbedPane jTabbedPane;
    private boolean newVC;

    private static VCEditDialog instance = null;

    /**
     * @return 8 juil. 2005
     */
    public static VCEditDialog getInstance ()
    {
        if ( instance == null )
        {
            instance = new VCEditDialog();
        }

        return instance;
    }


    /**
     * 
     */
    private VCEditDialog ()
    {
        super( MamboFrame.getInstance() , Messages.getMessage( "DIALOGS_EDIT_VC_TITLE" ) , true );

        this.initComponents();
        this.addComponents();
        this.initLayout();

        this.setSizeAndLocation();
        
        this.setDefaultCloseOperation ( JFrame.DO_NOTHING_ON_CLOSE );
        this.addWindowListener ( new VCEditWindowAdapter ( this ));
        newVC = false;
    }

    private class VCEditWindowAdapter extends WindowAdapter
    {
        private VCEditDialog toClose;
        public VCEditWindowAdapter ( VCEditDialog _toClose )
        {
            this.toClose = _toClose;
        }
        
        public void windowClosing ( WindowEvent e )
        {
            CancelAction.performCancel ( this.toClose );
        }
    }
    
    /**
     * 13 sept. 2005
     */
    private void setSizeAndLocation ()
    {
        pack();

        int width = this.getWidth() + 50;
        int x = MamboFrame.getInstance().getX() + MamboFrame.getInstance().getWidth() - width;
        if (x < 0) x = 0;
        int y = MamboFrame.getInstance().getY() + 100;
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        if (r.height > 850) setSize( getWidth(), 850 );
        if (y < 0 || y + getHeight() > r.y + r.height) y = 0;

        this.setLocation(x, y);
    }


    /**
     * 5 juil. 2005
     */
    private void initComponents ()
    {
        String msgBack = Messages.getMessage( "DIALOGS_EDIT_VC_BACK" );
        String msgNext = Messages.getMessage( "DIALOGS_EDIT_VC_NEXT" );
        String msgFinish = Messages.getMessage( "DIALOGS_EDIT_VC_FINISH" );
        String msgCancel = Messages.getMessage( "DIALOGS_EDIT_VC_CANCEL" );

        generalTab = GeneralTab.getInstance();
        generalScrollPane = new JScrollPane(generalTab);
        generalScrollPane.setPreferredSize( GUIUtilities.getEditScrollPaneSize() );
        attributesTab = AttributesTab.getInstance();
        attributesScrollPane = new JScrollPane(attributesTab);
        attributesScrollPane.setPreferredSize( GUIUtilities.getEditScrollPaneSize() );
        attributesPlotPropertiesTab = AttributesPlotPropertiesTab.getInstance();
        attributesPlotPropertiesScrollPane = new JScrollPane(attributesPlotPropertiesTab);
        attributesPlotPropertiesScrollPane.setPreferredSize( GUIUtilities.getEditScrollPaneSize() );
        expressionTab = ExpressionTab.getInstance();
        expressionScrollPane = new JScrollPane(expressionTab);
        expressionScrollPane.setPreferredSize( GUIUtilities.getEditScrollPaneSize() );

        backButton = new JButton( VCBackAction.getInstance( msgBack ) );
        nextButton = new JButton( VCNextAction.getInstance( msgNext ) );
        finishButton = new JButton( VCFinishAction.getInstance( msgFinish ) );
        cancelButton = new JButton( new CancelAction( msgCancel , this ) );

        backButton.setPreferredSize( new Dimension( 20 , 30 ) );
        nextButton.setPreferredSize( new Dimension( 20 , 30 ) );
        finishButton.setPreferredSize( new Dimension( 20 , 30 ) );
        cancelButton.setPreferredSize( new Dimension( 20 , 30 ) );
    }

    /**
     * 15 juin 2005
     */
    private void initLayout ()
    {
        Box mainBox = new Box( BoxLayout.Y_AXIS );
        mainBox.add( jTabbedPane );

        mainBox.add( Box.createVerticalStrut( 5 ) );

        Box buttonsBox = new Box( BoxLayout.X_AXIS );
        buttonsBox.add( backButton );
        buttonsBox.add( nextButton );
        buttonsBox.add( finishButton );
        buttonsBox.add( cancelButton );

        mainBox.add( buttonsBox );

        myPanel.add( mainBox );
    }

    /**
     * 8 juil. 2005
     */
    private void addComponents ()
    {
        myPanel = new JPanel();
        myPanel.setLayout( new GridLayout() );

        JScrollPane scrollPane = new JScrollPane( myPanel );
        this.getContentPane().add( scrollPane );
        this.getContentPane().setLayout( new GridLayout() );

        jTabbedPane = VCCustomTabbedPane.getInstance();

        String msgDisplayTab = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_TITLE" );
        String msgPrint = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_TITLE" );
        String msgLogs = Messages.getMessage( "DIALOGS_EDIT_VC_ATTRIBUTES_PLOT_PROPERTIES_TITLE" );
        String msgExpr = Messages.getMessage( "EXPRESSION_EVALUATION_TITLE" );

        jTabbedPane.addTab( msgDisplayTab , generalScrollPane );
        jTabbedPane.addTab( msgPrint , attributesScrollPane );
        jTabbedPane.addTab( msgLogs , attributesPlotPropertiesScrollPane );
        jTabbedPane.addTab( msgExpr , expressionScrollPane );

        initBackAndNextStatus();
    }

    /**
     * 
     */
    private void initBackAndNextStatus ()
    {
        jTabbedPane.setSelectedIndex( 0 );
        jTabbedPane.setEnabledAt( 1 , false );
        jTabbedPane.setEnabledAt( 2 , false );
        jTabbedPane.setEnabledAt( 3 , false );
        VCBackAction backAction = VCBackAction.getInstance();
        VCNextAction nextAction = VCNextAction.getInstance();
        VCFinishAction finishAction = VCFinishAction.getInstance();
        backAction.setEnabled( false );
        nextAction.setEnabled( true );
        finishAction.setEnabled( false );
    }
    
    public void resetTabbedPane() {
        jTabbedPane.setEnabledAt( 0 , true );
        this.initBackAndNextStatus ();
    }

    public void setNewVC(boolean vc) {
        newVC = vc;
    }

    public boolean isNewVC() {
        return newVC;
    }
}
