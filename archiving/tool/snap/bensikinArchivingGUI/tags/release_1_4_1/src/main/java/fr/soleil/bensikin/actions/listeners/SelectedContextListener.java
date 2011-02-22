//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/listeners/SelectedContextListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SelectedContextListener.
//						(Claisse Laurent) - 7 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.6 $
//
// $Log: SelectedContextListener.java,v $
// Revision 1.6  2005/12/14 16:06:47  ounsy
// minor changes
//
// Revision 1.5  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:34  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.listeners;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;

import fr.soleil.bensikin.actions.context.LaunchSnapshotAction;
import fr.soleil.bensikin.actions.context.MatchContextAttributesAction;
import fr.soleil.bensikin.actions.context.RegisterContextAction;
import fr.soleil.bensikin.actions.snapshot.FilterSnapshotsAction;
import fr.soleil.bensikin.containers.context.ContextDataPanel;


/**
 * Listens to changes in the content of the selected context's ID field.
 * If the value changes to an empty or null value, all actions (launch snapshot, look up snapshots) on the current context are disabled
 * while the context registering action is enabled.
 * If the value changes to an non empty value, the opposite happens: all actions (launch snapshot, look up snapshots) on the current context are enabled
 * while the context registering action is disabled.
 *
 * @author CLAISSE
 */
public class SelectedContextListener implements PropertyChangeListener
{
    public static final String ID_TEXT_PROPERTY = "idText";
    public final static String INFO_TEXT_PROPERTY = "info";


    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange ( PropertyChangeEvent event )
    {
        String prop = event.getPropertyName();
        if ( ID_TEXT_PROPERTY.equals( prop ) )
        {
            LaunchSnapshotAction launchSnapshotAction = LaunchSnapshotAction.getInstance();
            FilterSnapshotsAction filterSnapshotsAction = FilterSnapshotsAction.getInstance();
            RegisterContextAction registerContextAction = RegisterContextAction.getInstance();
            MatchContextAttributesAction matchContextAttributesAction = MatchContextAttributesAction.getInstance();
            
            JTextField idField = ( JTextField ) event.getSource();
            String id = idField.getText();

            if ( id == null || id.trim().equals( "" ) )
            {
                launchSnapshotAction.setEnabled( false );
                filterSnapshotsAction.setEnabled( false );

                ContextDataPanel contextDataPanel = ContextDataPanel.getInstance();
                registerContextAction.setEnabled( contextDataPanel.isEnabled() );

                //--CLA added 23/09/05 for loaded contexts
                registerContextAction.setEnabled( true );
                
                //--CLA 16/11/05
                if ( matchContextAttributesAction != null )
                {
                    matchContextAttributesAction.setEnabled( false );    
                }
            }
            else
            {
                launchSnapshotAction.setEnabled( true );
                filterSnapshotsAction.setEnabled( true );
                registerContextAction.setEnabled( false );

                //--CLA 16/11/05
                if ( matchContextAttributesAction != null )
                {
                    matchContextAttributesAction.setEnabled( true );
                }
            }

        }
        if ( INFO_TEXT_PROPERTY.equals( prop ) )
        {
            RegisterContextAction registerContextAction = RegisterContextAction.getInstance();

            JTextField idField = ( JTextField ) event.getSource();
            String id = idField.getText();

            if ( id == null || id.trim().equals( "" ) )
            {
                registerContextAction.setEnabled( false );
            }
            else
            {
                registerContextAction.setEnabled( true );
            }
        }
    }
}
