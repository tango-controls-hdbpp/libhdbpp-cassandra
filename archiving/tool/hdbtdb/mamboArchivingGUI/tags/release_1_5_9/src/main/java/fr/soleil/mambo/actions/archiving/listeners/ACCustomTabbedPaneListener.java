//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/listeners/ACCustomTabbedPaneListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACCustomTabbedPaneListener.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: ACCustomTabbedPaneListener.java,v $
// Revision 1.4  2006/05/19 15:01:55  ounsy
// minor changes
//
// Revision 1.3  2006/05/19 13:45:13  ounsy
// minor changes
//
// Revision 1.2  2006/05/16 12:49:41  ounsy
// modified imports
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
package fr.soleil.mambo.actions.archiving.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fr.soleil.mambo.actions.archiving.ACBackAction;
import fr.soleil.mambo.actions.archiving.ACFinishAction;
import fr.soleil.mambo.actions.archiving.ACNextAction;


public class ACCustomTabbedPaneListener implements PropertyChangeListener
{
    private static ACCustomTabbedPaneListener instance = null;
    public static final String SELECTED_PAGE = "SELECTED_PAGE";

    /**
     * 
     */
    private ACCustomTabbedPaneListener ()
    {
        super();
    }

    /**
     * @return 8 juil. 2005
     */
    public static ACCustomTabbedPaneListener getInstance ()
    {
        if ( instance == null )
        {
            instance = new ACCustomTabbedPaneListener();
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

            //ACCustomTabbedPane source = ( ACCustomTabbedPane ) event.getSource();

            ACBackAction backAction = ACBackAction.getInstance();
            ACNextAction nextAction = ACNextAction.getInstance();
            ACFinishAction finishAction = ACFinishAction.getInstance();

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
                    nextAction.setEnabled( false );
                    finishAction.setEnabled( true );
                    break;
            }
        }
    }

}
