//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ViewAttributesTreePanel.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author$
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.mambo.components.view;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fr.soleil.mambo.actions.view.VCHideAction;
import fr.soleil.mambo.actions.view.VCRefreshAction;
import fr.soleil.mambo.tools.Messages;

public class VCPopupMenu extends JPopupMenu {
	
	private JMenuItem hideMenuItem;
	private JMenuItem refreshMenuItem;
	
	public VCPopupMenu() {
		super();
		// TODO Auto-generated constructor stub
		initComponente();
		addComponente();
	}

	private void addComponente() {
		// TODO Auto-generated method stub
		this.add( hideMenuItem );
		this.add( refreshMenuItem );
		
	}

	private void initComponente() {
		// TODO Auto-generated method stub
		String msg = Messages.getMessage( "VIEW_ACTION_EXPEND_GRAPHEVIEW" );
        hideMenuItem    = new JMenuItem( VCHideAction.getInstence( msg ) );
        msg = Messages.getMessage( "VIEW_ACTION_REFRESH_GRAPHEVIEW" );
		refreshMenuItem = new JMenuItem( VCRefreshAction.getInstance(msg) );
		
//		hideMenuItem.addActionListener( VCHideAction.getInstence( msg ) );
	}



	
}
