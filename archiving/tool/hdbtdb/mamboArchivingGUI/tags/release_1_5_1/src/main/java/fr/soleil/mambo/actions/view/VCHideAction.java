//+======================================================================
// $Source$
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCViewAction.
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.containers.view.ViewAttributesPanel;
import fr.soleil.mambo.tools.Messages;


public class VCHideAction extends AbstractAction
{
	private boolean visible = true;
	private String name;
	private static VCHideAction Instance = null;
    /**
     * @param name
     */
	
	public static VCHideAction getInstence( String name ) {
		if ( Instance == null )
        {
			Instance = new VCHideAction( name );
        }

		return Instance;
	}

	public static VCHideAction getInstence() {
		return Instance;
	}
	
    public VCHideAction ( String name )
    {
    	Instance = this;
    	this.name = name;
        putValue( Action.NAME , this.name );
        putValue( Action.SHORT_DESCRIPTION , this.name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        try{
        	if(visible == true){
        		visible = false;
        		ViewAttributesPanel.getInstance().hideTreePanel();
        		name = Messages.getMessage( "VIEW_ACTION_SHOWK_GRAPHEVIEW" );
                putValue( Action.NAME , name );
                putValue( Action.SHORT_DESCRIPTION , name );
        	}else{
        		visible = true;
        		ViewAttributesPanel.getInstance().showTreePanel();
        		name = Messages.getMessage( "VIEW_ACTION_EXPEND_GRAPHEVIEW" );
                putValue( Action.NAME , name );
                putValue( Action.SHORT_DESCRIPTION , name );

        	}
        }
        catch(Throwable t)
        {

        }
    }

	public boolean getVisible() {
		return visible;
	}




}
