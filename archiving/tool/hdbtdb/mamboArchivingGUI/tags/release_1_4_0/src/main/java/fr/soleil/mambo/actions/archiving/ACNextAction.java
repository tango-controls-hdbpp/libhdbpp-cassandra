//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/mambo/actions/archiving/ACNextAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  OpenACEditDialogAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.3 $
//
//$Log: ACNextAction.java,v $
//Revision 1.3  2006/08/23 10:01:32  ounsy
//some optimizations with less tree model reloading
//
//Revision 1.2  2006/06/20 16:01:39  ounsy
//sets the root node of the attributes properties tree selected
//
//Revision 1.1  2005/11/29 18:27:07  chinkumo
//no message
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.actions.archiving;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;

import fr.soleil.mambo.components.archiving.ACAttributesPropertiesTree;
import fr.soleil.mambo.components.archiving.ACCustomTabbedPane;
import fr.soleil.mambo.components.archiving.AttributesSelectTable;
import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.models.AttributesSelectTableModel;


public class ACNextAction extends AbstractAction
{
    private static ACNextAction instance = null;

    /**
     * @return
     */
    public static ACNextAction getInstance ( String name )
    {
        if ( instance == null )
        {
            instance = new ACNextAction( name );
        }

        return instance;
    }

    public static ACNextAction getInstance ()
    {
        return instance;
    }

    /**
     * @param name
     */
    private ACNextAction ( String name )
    {
        super.putValue( Action.NAME , name );
        super.putValue( Action.SHORT_DESCRIPTION , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        ACCustomTabbedPane tabbedPane = ACCustomTabbedPane.getInstance();

        int oldValue = tabbedPane.getSelectedIndex();
        int newValue = oldValue + 1;

        tabbedPane.setEnabledAt( newValue , true );
        tabbedPane.setSelectedIndex( newValue );

        switch ( newValue )
        {
            case 1:
                tabbedPane.setEnabledAt( 0 , false );
                tabbedPane.setEnabledAt( 2 , false );
                break;

            case 2:
                removeNotSelectedAttributes();

                tabbedPane.setEnabledAt( 0 , false );
                tabbedPane.setEnabledAt( 1 , false );

                // select root node in attributes properties tree
                if ( ACAttributesPropertiesTree.getInstance() != null 
                     && ( ACAttributesPropertiesTree.getInstance().getSelectionRows() == null
                          || ACAttributesPropertiesTree.getInstance().getSelectionRows().length == 0
                        )
                   )
                {
                    ACAttributesPropertiesTree.getInstance().setSelectionRow(0);
                    ACAttributesPropertiesTree.getInstance().expandAll(true);
                }
                break;

            default :
                throw new IllegalStateException();
        }
    }

    /**
     * 
     */
    private void removeNotSelectedAttributes ()
    {
        ACEditDialog acEditDialog = ACEditDialog.getInstance();
        if ( !acEditDialog.isAlternateSelectionMode() )
        {
            //nothing to do
            return;
        }

        AttributesSelectTableModel model = AttributesSelectTableModel.getInstance();
        AttributesSelectTable table = AttributesSelectTable.getInstance();

        table.applyChange();
        model.removeNotSelectedRows();
    }

}
