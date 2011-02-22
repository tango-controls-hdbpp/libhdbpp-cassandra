//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/SaveSelectedVCAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SaveSelectedVCAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: SaveSelectedVCAction.java,v $
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.data.view.ViewConfiguration;
import fr.soleil.mambo.datasources.file.IViewConfigurationManager;
import fr.soleil.mambo.datasources.file.ViewConfigurationManagerFactory;



public class SaveSelectedVCAction extends AbstractAction
{
    private boolean isSaveAs;

    /**
     * @param name
     */
    public SaveSelectedVCAction ( String name , boolean _isSaveAs )
    {
        super.putValue( Action.NAME , name );
        super.putValue( Action.SHORT_DESCRIPTION , name );

        this.isSaveAs = _isSaveAs;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        IViewConfigurationManager manager = ViewConfigurationManagerFactory.getCurrentImpl();
        ViewConfiguration selectedViewConfiguration = ViewConfiguration.getSelectedViewConfiguration();

        if ( selectedViewConfiguration == null )
        {
            return;
        }

        selectedViewConfiguration.save( manager , this.isSaveAs );
    }

}
