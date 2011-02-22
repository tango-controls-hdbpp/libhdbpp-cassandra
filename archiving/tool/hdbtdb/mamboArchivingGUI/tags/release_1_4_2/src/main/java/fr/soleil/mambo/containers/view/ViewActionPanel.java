//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewActionPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewActionPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.10 $
//
// $Log: ViewActionPanel.java,v $
// Revision 1.10  2008/04/09 18:00:00  achouri
// change place of new button
//
// Revision 1.9  2008/04/09 10:45:46  achouri
// change View button to Refresh button
//
// Revision 1.8  2007/04/06 14:27:06  ounsy
// *** empty log message ***
//
// Revision 1.7  2007/01/09 16:25:48  ounsy
// look & feel with "expand all" buttons in main frame
//
// Revision 1.6  2006/11/06 09:28:05  ounsy
// icons reorganization
//
// Revision 1.5  2006/08/09 16:12:54  ounsy
// No more automatic tree expanding : user has to click on a button to fully expand a tree
//
// Revision 1.4  2006/07/28 10:07:12  ounsy
// icons moved to "icons" package
//
// Revision 1.3  2006/04/05 13:46:41  ounsy
// new types full support
//
// Revision 1.2  2005/11/29 18:27:07  chinkumo
// no message
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
package fr.soleil.mambo.containers.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.CancelFutureTaskAction;
import fr.soleil.mambo.actions.DoFutureTaskAction;
import fr.soleil.mambo.actions.view.LoadVCAction;
import fr.soleil.mambo.actions.view.OpenVCEditDialogAction;
import fr.soleil.mambo.actions.view.SaveSelectedVCAction;
import fr.soleil.mambo.actions.view.VCRefreshAction;
import fr.soleil.mambo.actions.view.VCVariationAction;
import fr.soleil.mambo.components.MamboActivableButton;
import fr.soleil.mambo.containers.view.dialogs.DateRangeBox;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class ViewActionPanel extends JPanel
{
    private static ViewActionPanel instance = null;

    private JButton refreshButton;
    private JButton variationButton;
    private JButton modifyButton;
    private JButton quickSaveButton;
    private JButton quickLoadButton;
    private Border  refreshButtonDefaultBorder;
    
    private DateRangeBox dateRangeBox;
//    private JLabel    dateRangeLabel;
//    private JComboBox dateRangeComboBox;
//    private JLabel     startDateLabel;
//    private JTextField startDateField;
//    private JLabel     endDateLabel;
//    private JTextField endDateField;
//    private JLabel    dynamicDateRangeLabel;
//    private JCheckBox dynamicDateRangeCheckBox;
//    private DateRangeComboBoxListener dateRangeComboBoxListener;

    private final static ImageIcon quickLoadIcon = new ImageIcon( Mambo.class.getResource( "icons/Quick_Open.gif" ) );
    private final static ImageIcon quickLoadDisabledIcon = new ImageIcon( Mambo.class.getResource( "icons/Quick_Open_Disabled.gif" ) );
    private final static ImageIcon quickSaveIcon = new ImageIcon( Mambo.class.getResource( "icons/Quick_Save.gif" ) );
    private final static ImageIcon quickSaveDisabledIcon = new ImageIcon( Mambo.class.getResource( "icons/Quick_Save_Disabled.gif" ) );
    private final static ImageIcon viewIcon = new ImageIcon( Mambo.class.getResource( "icons/View.gif" ) );
    private final static ImageIcon viewDisabledIcon = new ImageIcon( Mambo.class.getResource( "icons/View_Disabled.gif" ) );
    private final static ImageIcon variationIcon = new ImageIcon( Mambo.class.getResource( "icons/Variation.gif" ) );
    private final static ImageIcon variationDisabledIcon = new ImageIcon( Mambo.class.getResource( "icons/Variation_Disabled.gif" ) );
    private final static ImageIcon modifyIcon = new ImageIcon( Mambo.class.getResource( "icons/Modify.gif" ) );
    private final static ImageIcon modifyDisabledIcon = new ImageIcon( Mambo.class.getResource( "icons/Modify_Disabled.gif" ) );

    /**
     * @return 8 juil. 2005
     */
    public static ViewActionPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ViewActionPanel();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.VIEW_COLOR );
        }

        return instance;
    }

    /**
     * 
     */
    private ViewActionPanel ()
    {
        super();

        initComponents();
        addComponents();
        setLayout();
        initBorder();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout ()
    {
        this.setLayout( new SpringLayout() );

        //Lay out the panel.
        SpringUtilities.makeCompactGrid( this ,
                                         1 , 3 , //rows, cols
                                         3 , 3 , //initX, initY
                                         3 , 3 ,
                                         true );       //xPad, yPad

        this.setMaximumSize( new Dimension( Integer.MAX_VALUE , 150 ) );
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
//        this.add( dateRangeBox );
        this.add( refreshButton );
        this.add( variationButton );
        this.add( modifyButton );
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {

        DoFutureTaskAction doFutureTaskAction = new DoFutureTaskAction ( "FUTURE"  );
        CancelFutureTaskAction cancelFutureTaskAction = CancelFutureTaskAction.getInstance ( "CANCEL" );
        
        String msg = "";

        /////
        msg = Messages.getMessage( "VIEW_ACTION_REFRESH_BUTTON" );
        VCRefreshAction refreshAction = new VCRefreshAction( msg );
        refreshButton = new MamboActivableButton( refreshAction , viewIcon , viewDisabledIcon );
        //viewButton = new JButton( doFutureTaskAction );
        GUIUtilities.setObjectBackground( refreshButton , GUIUtilities.VIEW_COLOR );
        refreshButtonDefaultBorder = refreshButton.getBorder();

        msg = Messages.getMessage( "VIEW_ACTION_VARIATION_BUTTON" );
        VCVariationAction variationAction = new VCVariationAction( msg );
        variationButton = new MamboActivableButton( variationAction , variationIcon , variationDisabledIcon );
        //variationButton = new JButton( cancelFutureTaskAction );
        GUIUtilities.setObjectBackground( variationButton , GUIUtilities.VIEW_COLOR );

        msg = Messages.getMessage( "VIEW_ACTION_MODIFY_BUTTON" );
        OpenVCEditDialogAction modifyAction = new OpenVCEditDialogAction( msg , false );
        modifyButton = new MamboActivableButton( modifyAction , modifyIcon , modifyDisabledIcon );
        GUIUtilities.setObjectBackground( modifyButton , GUIUtilities.VIEW_COLOR );

        msg = Messages.getMessage( "VIEW_ACTION_QUICK_SAVE_BUTTON" );
        SaveSelectedVCAction quickSaveAction = new SaveSelectedVCAction( msg , true );
        quickSaveButton = new MamboActivableButton( quickSaveAction , quickSaveIcon , quickSaveDisabledIcon );
        GUIUtilities.setObjectBackground( quickSaveButton , GUIUtilities.VIEW_COLOR );

        msg = Messages.getMessage( "VIEW_ACTION_QUICK_LOAD_BUTTON" );
        LoadVCAction quickLoadAction = new LoadVCAction( msg , true );
        quickLoadButton = new MamboActivableButton( quickLoadAction , quickLoadIcon , quickLoadDisabledIcon );
        GUIUtilities.setObjectBackground( quickLoadButton , GUIUtilities.VIEW_COLOR );
        
    }

    /**
     * 19 juil. 2005
     */
    private void initBorder ()
    {
        String msg = Messages.getMessage( "VIEW_ACTION_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        Border border = ( Border ) ( tb );
        this.setBorder( border );

    }

	public DateRangeBox getDateRangeBox() {
		return dateRangeBox;
	}

	public void setDateRangeBox(DateRangeBox dateRangeBox) {
		this.dateRangeBox = dateRangeBox;
	}

	public void refreshButtonChangeColor(){
		refreshButton.setBorder( new LineBorder(Color.RED, 1) );
	}

	public void refreshButtonDefaultColor(){
		refreshButton.setBorder(refreshButtonDefaultBorder);
	}
}
