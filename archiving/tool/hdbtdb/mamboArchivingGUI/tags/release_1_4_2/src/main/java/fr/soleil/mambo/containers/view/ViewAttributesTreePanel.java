//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewAttributesTreePanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewAttributesTreePanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: ViewAttributesTreePanel.java,v $
// Revision 1.5  2008/04/09 10:45:46  achouri
// add DateRangeBox and ActionPanel
//
// Revision 1.4  2007/01/10 14:28:18  ounsy
// minor changes (look&feel)
//
// Revision 1.3  2007/01/09 16:25:48  ounsy
// look & feel with "expand all" buttons in main frame
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
import java.awt.Insets;
import java.awt.event.ItemEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import fr.soleil.mambo.Mambo;
import fr.soleil.mambo.actions.view.VCAttributesRecapTreeExpandAllAction;
import fr.soleil.mambo.components.view.VCAttributesRecapTree;
import fr.soleil.mambo.containers.view.dialogs.DateRangeBox;
import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.data.view.ViewConfigurationData;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.SpringUtilities;


public class ViewAttributesTreePanel extends JPanel
{
    private static ViewAttributesTreePanel instance = null;

    private final static Dimension dim  = new Dimension( 255 , 400 );
    private final static Dimension dim2  = new Dimension( 350 , 400 );

    private JScrollPane scrollPane;
    private VCAttributesRecapTree tree;
    private final static ImageIcon expandAllIcon = new ImageIcon( Mambo.class.getResource( "icons/expand_all.gif" ) );
    private JButton expandAllButton;

    private DateRangeBox dateRangeBox;

	private ViewActionPanel viewActionPanel;

    /**
     * @return 8 juil. 2005
     */
    public static ViewAttributesTreePanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ViewAttributesTreePanel();
            GUIUtilities.setObjectBackground( instance , GUIUtilities.VIEW_COLOR );
            instance.dateRangeBox.getDateRangeComboBoxListener().itemStateChanged( 
            		new ItemEvent( instance.dateRangeBox.getDateRangeComboBox() , 
            		ItemEvent.ITEM_STATE_CHANGED , 
            		instance.dateRangeBox.getDateRangeComboBoxListener().getLast1h() , 
            		ItemEvent.SELECTED ) );
        }

        return instance;
    }

    /**
     * 
     */
    private ViewAttributesTreePanel ()
    {
        super();

        initComponents();
        addComponents();
        setLayout();
    }

    /**
     * 19 juil. 2005
     */
    private void setLayout ()
    {
        this.setLayout( new SpringLayout() );

        //Lay out the panel.
        SpringUtilities.makeCompactGrid( this ,
                                         3 , 1 , //rows, cols
                                         0 , 0 , //initX, initY
                                         0 , 0 , //xPad, yPad
                                         true );

    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {

        this.add( scrollPane );
        this.add( dateRangeBox );
        this.add( viewActionPanel );
        
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        VCAttributesTreeModel model = VCAttributesTreeModel.getInstance();
        tree = VCAttributesRecapTree.getInstance( model );

        expandAllButton = new JButton( new VCAttributesRecapTreeExpandAllAction() );
        expandAllButton.setIcon( expandAllIcon );
        expandAllButton.setMargin( new Insets(0,0,0,0) );
        expandAllButton.setBackground( Color.WHITE );
        expandAllButton.setBorderPainted( false );
        expandAllButton.setFocusable( false );
        expandAllButton.setFocusPainted( false );
        expandAllButton.setHorizontalAlignment( JButton.LEFT );
        expandAllButton.setFont( GUIUtilities.expandButtonFont );
        expandAllButton.setForeground( Color.BLACK );
        scrollPane = new JScrollPane( tree );
        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
//        scrollPane.setMinimumSize( new Dimension( 100 , 50 ) );
//        scrollPane.setPreferredSize( new Dimension( 200 , 50 ) );
//        scrollPane.setMaximumSize( new Dimension( Integer.MAX_VALUE , 500 ) );
        scrollPane.setColumnHeaderView( expandAllButton );
        
    	dateRangeBox = new DateRangeBox( this );
    	GUIUtilities.setObjectBackground( dateRangeBox , GUIUtilities.VIEW_COLOR );
        GUIUtilities.setObjectBackground( dateRangeBox.getDynamicDateRangeCheckBox() , GUIUtilities.VIEW_COLOR );
        
        ViewConfiguration selectedVC = ViewConfiguration.getSelectedViewConfiguration();
        if( selectedVC != null ){
        	ViewConfigurationData vcData = selectedVC.getData();
        	dateRangeBox.setStartDate( vcData.getStartDate().toString() );
        }
//        ViewConfigurationData vcd = ViewConfiguration.getSelectedViewConfiguration().getData();
//        dateRangeBox.getDateRangeComboBox().setSelectedItem(vcd.getDateRange());
//        dateRangeBox.getStartDateField().setText( vcd.getStartDate().toString() );
//        dateRangeBox.getEndDateField().setText( vcd.getEndDate().toString() );
//        dateRangeBox.getDynamicDateRangeCheckBox().setSelected(vcd.getDynamicStartAndEndDates());
        
        viewActionPanel = ViewActionPanel.getInstance();

        this.setMinimumSize( dim );
        this.setMaximumSize( dim );
        this.setPreferredSize( dim );

    }

    /**
     * @return Returns the tree.
     */
    public VCAttributesRecapTree getTree ()
    {
        return tree;
    }

    /**
     * @param tree The tree to set.
     */
    public void setTree ( VCAttributesRecapTree tree )
    {
        this.tree = tree;
    }
    

	public DateRangeBox getDateRangeBox() {
		return dateRangeBox;
	}

	public void setDateRangeBox(DateRangeBox dateRangeBox) {
		this.dateRangeBox = dateRangeBox;
	}
}
