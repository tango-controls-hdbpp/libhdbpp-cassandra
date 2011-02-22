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

	public static VCHideAction getInstance( String name ) {
		if ( Instance == null )
        {
			Instance = new VCHideAction( name );
        }

		return Instance;
	}

	public static VCHideAction getInstance() {
		return Instance;
	}

    public VCHideAction ( String name )
    {
    	Instance = this;
    	this.name = name;
        putValue( Action.NAME , this.name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        try{
        	if(visible == true){
        		visible = false;
        		ViewAttributesPanel.getInstance().hideTreePanel();
        		name = Messages.getMessage( "VIEW_ACTION_SHOW_TREE_VIEW" );
                putValue( Action.NAME , name );
        	}
        	else{
        		visible = true;
        		ViewAttributesPanel.getInstance().showTreePanel();
        		name = Messages.getMessage( "VIEW_ACTION_HIDE_TREE_VIEW" );
                putValue( Action.NAME , name );
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
