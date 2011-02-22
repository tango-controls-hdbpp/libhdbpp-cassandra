//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/VCCustomTabbedPaneListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCCustomTabbedPaneListener.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: VCCustomTabbedPaneListener.java,v $
// Revision 1.3  2007/01/11 14:05:46  ounsy
// Math Expressions Management (warning ! requires atk 2.7.0 or greater)
//
// Revision 1.2  2006/05/19 15:03:36  ounsy
// minor changes
//
// Revision 1.1  2005/11/29 18:27:07  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.view.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fr.soleil.mambo.actions.view.VCBackAction;
import fr.soleil.mambo.actions.view.VCFinishAction;
import fr.soleil.mambo.actions.view.VCNextAction;


public class VCCustomTabbedPaneListener implements PropertyChangeListener
{
    private static VCCustomTabbedPaneListener instance = null;
    public static final String SELECTED_PAGE = "SELECTED_PAGE";

    /**
     * 
     */
    private VCCustomTabbedPaneListener ()
    {
        super();
    }

    /**
     * @return 8 juil. 2005
     */
    public static VCCustomTabbedPaneListener getInstance ()
    {
        if ( instance == null )
        {
            instance = new VCCustomTabbedPaneListener();
        }

        return instance;
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange ( PropertyChangeEvent event )
    {
        String prop = event.getPropertyName();

        if ( SELECTED_PAGE.equals( prop ) )
        {
            //Integer oldValue_o = ( Integer ) event.getOldValue();
            Integer newValue_o = ( Integer ) event.getNewValue();

            //int oldValue = oldValue_o.intValue();
            int newValue = newValue_o.intValue();

            //VCCustomTabbedPane source = ( VCCustomTabbedPane ) event.getSource();

            VCBackAction backAction = VCBackAction.getInstance();
            VCNextAction nextAction = VCNextAction.getInstance();
            VCFinishAction finishAction = VCFinishAction.getInstance();

            switch ( newValue )
            {
                case 0:
                    backAction.setEnabled( false );
                    nextAction.setEnabled( true );
                    finishAction.setEnabled( false );
                    break;

                case 1:
                    backAction.setEnabled( true );
                    nextAction.setEnabled( true );
                    finishAction.setEnabled( false );
                    break;

                case 2:
                    backAction.setEnabled( true );
                    nextAction.setEnabled( true );
                    finishAction.setEnabled( true );
                    break;

                case 3:
                    backAction.setEnabled( true );
                    nextAction.setEnabled( false );
                    finishAction.setEnabled( true );
                    break;
            }
        }
    }

}
