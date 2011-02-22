//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/view/ViewAttributesPanel.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewAttributesPanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: ViewAttributesPanel.java,v $
// Revision 1.6  2008/04/09 18:00:00  achouri
// add popup menu right click
//
// Revision 1.5  2008/04/09 10:45:46  achouri
// add ViewAttributesGraphePanel
//
// Revision 1.4  2006/10/12 13:22:53  ounsy
// ViewAttributesDetailPanel removed (useless)
//
// Revision 1.3  2006/05/19 15:05:29  ounsy
// minor changes
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.actions.view.VCHideAction;
import fr.soleil.mambo.components.view.VCPopupMenu;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphePanel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;
import fr.soleil.mambo.tools.SpringUtilities;


public class ViewAttributesPanel extends JPanel 
{
    private static ViewAttributesPanel instance = null;

    private ViewAttributesTreePanel viewAttributesTreePanel;

	private ViewAttributesGraphePanel viewAttributesGraphePanel;

//    private JTabbedPane attributesTab;

    /**
     * @return 8 juil. 2005
     */
    public static ViewAttributesPanel getInstance ()
    {
        if ( instance == null )
        {
            instance = new ViewAttributesPanel();

        }

        return instance;
    }
    

    /**
     * 
     */
    private ViewAttributesPanel ()
    {
        super();

        initComponents();
        addComponents();
        setLayout();
        initBorder();
        //attributesTab = VCViewDialog.getInstance(true).attributesTab;
        
    }

    /**
     * 19 juil. 2005
     */
    public void setLayout ()
    {
    	boolean visible = VCHideAction.getInstence().getVisible();
        this.setLayout( new SpringLayout() );

        //Lay out the panel.
        if(visible){
            SpringUtilities.makeCompactGrid( this ,
                    1 , 2 , //rows, cols
                    0 , 0 , //initX, initY
                    0 , 0 , //xPad, yPad
                    true );
        }else{
            SpringUtilities.makeCompactGrid( this ,
                    1 , 1 , //rows, cols
                    0 , 0 , //initX, initY
                    0 , 0 , //xPad, yPad
                    true );
        	
        }

    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
    	this.add( viewAttributesTreePanel );
    	this.add( viewAttributesGraphePanel );
        //this.add ( viewAttributesDetailPanel );
        /*this.add ( new JLabel ( "ViewAttributesPanel, attributes" ) );
        this.add ( new JLabel ( "ViewAttributesPanel, detail" ) );*/
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
    	
        viewAttributesTreePanel   = ViewAttributesTreePanel.getInstance();
        viewAttributesGraphePanel = ViewAttributesGraphePanel.getInstance();
        
        GUIUtilities.setObjectBackground( viewAttributesGraphePanel , GUIUtilities.VIEW_COLOR );
        GUIUtilities.setObjectBackground( this , GUIUtilities.VIEW_COLOR );
        
        
		final VCPopupMenu labelPopupMenu = new VCPopupMenu();
		
        this.addMouseListener( new MouseAdapter()  {
			public void mouseClicked(MouseEvent e) {
				// clic sur le bouton droit   
				if(e.getButton() == MouseEvent.BUTTON3) {
					labelPopupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
//        attributesTab = new JTabbedPane();
//        attributesTab.setBackground(new Color(15,15,15));
    }

    /**
     * 19 juil. 2005
     */
    private void initBorder ()
    {
        String msg = Messages.getMessage( "VIEW_ATTRIBUTES_BORDER" );
        TitledBorder tb = BorderFactory.createTitledBorder
                ( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) ,
                  msg ,
                  TitledBorder.CENTER ,
                  TitledBorder.TOP );
        Border border = ( Border ) ( tb );
        this.setBorder( border );
    }

	public ViewAttributesGraphePanel getViewAttributesGraphePanel() {
		return viewAttributesGraphePanel;
	}

	public void setViewAttributesGraphePanel( ViewAttributesGraphePanel viewAttributesGraphePanel) {
		this.viewAttributesGraphePanel = viewAttributesGraphePanel;
	}

	public void requestToRefresh(){
		try{
			viewAttributesGraphePanel.getInstance().clean();
		}catch(Exception e){
			System.out.println("");
		}
		viewAttributesGraphePanel.getInstance().repaint();
        viewAttributesGraphePanel.getInstance().validate();
        ViewActionPanel.getInstance().refreshButtonChangeColor();
	}

}
