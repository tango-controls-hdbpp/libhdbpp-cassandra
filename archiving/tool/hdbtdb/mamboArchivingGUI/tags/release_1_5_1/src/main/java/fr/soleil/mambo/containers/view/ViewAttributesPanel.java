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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import fr.soleil.mambo.components.view.VCPopupMenu;
import fr.soleil.mambo.containers.view.dialogs.ViewAttributesGraphPanel;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.Messages;


public class ViewAttributesPanel extends JPanel 
{
    private static ViewAttributesPanel instance = null;
    private GridBagConstraints treeConstraints;

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
        super( new GridBagLayout() );
        initComponents();
        addComponents();
        initBorder();
    }

    /**
     * 19 juil. 2005
     */
    private void addComponents ()
    {
        treeConstraints = new GridBagConstraints();
        treeConstraints.fill = GridBagConstraints.BOTH;
        treeConstraints.gridx = 0;
        treeConstraints.gridy = 0;
        treeConstraints.weightx = 0;
        treeConstraints.weighty = 1;
        GridBagConstraints graphConstraints = new GridBagConstraints();
        graphConstraints.fill = GridBagConstraints.BOTH;
        graphConstraints.gridx = 1;
        graphConstraints.gridy = 0;
        graphConstraints.weightx = 1;
        graphConstraints.weighty = 1;

        this.add( ViewAttributesTreePanel.getInstance(), treeConstraints );
    	this.add( ViewAttributesGraphPanel.getInstance(), graphConstraints );
    }

    /**
     * 19 juil. 2005
     */
    private void initComponents ()
    {
        GUIUtilities.setObjectBackground( ViewAttributesGraphPanel.getInstance() , GUIUtilities.VIEW_COLOR );
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

	public void requestToRefresh(){
		try{
			ViewAttributesGraphPanel.getInstance().clean();
		}catch(Exception e){
			System.out.println("");
		}
		ViewAttributesGraphPanel.getInstance().repaint();
        ViewAttributesGraphPanel.getInstance().validate();
        ViewActionPanel.getInstance().refreshButtonChangeColor();
	}

	public void showTreePanel() {
	    for (int i = 0; i < getComponentCount(); i++) {
	        if (ViewAttributesTreePanel.getInstance().equals( getComponent(i) )) {
	            return;
	        }
	    }
	    add( ViewAttributesTreePanel.getInstance(), treeConstraints );
	    revalidate();
	    repaint();
	}

	public void hideTreePanel() {
	    remove( ViewAttributesTreePanel.getInstance() );
        revalidate();
        repaint();
	}

}
