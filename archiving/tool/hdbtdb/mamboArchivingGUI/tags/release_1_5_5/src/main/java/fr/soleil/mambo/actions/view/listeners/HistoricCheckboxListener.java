//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/listeners/HistoricCheckboxListener.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  HistoricCheckboxListener.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: HistoricCheckboxListener.java,v $
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
package fr.soleil.mambo.actions.view.listeners;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import fr.soleil.mambo.components.view.VCPossibleAttributesTree;
import fr.soleil.mambo.containers.view.dialogs.GeneralTab;
import fr.soleil.mambo.models.VCAttributesTreeModel;
import fr.soleil.mambo.models.VCPossibleAttributesTreeModel;
import fr.soleil.mambo.tools.Messages;


public class HistoricCheckboxListener extends AbstractAction
{

    public HistoricCheckboxListener ( /*String name*/ )
    {
        /*super.putValue ( Action.NAME , name  );
        super.putValue ( Action.SHORT_DESCRIPTION , name  );*/
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent actionEvent )
    {
        //System.out.println ("HistoricCheckboxListener!!");

        JCheckBox historicCheck = ( JCheckBox ) actionEvent.getSource();
        boolean wasChecked = !historicCheck.isSelected();
        //we apply a not operator since the user already clicked on the checkbox

        String msgTitle = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_HISTORIC_CONFIRM_TITLE" );
        String msgConfirm = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_HISTORIC_CONFIRM_LABEL" );
        String msgCancel = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_HISTORIC_CONFIRM_CANCEL" );
        String msgValidate = Messages.getMessage( "DIALOGS_EDIT_VC_GENERAL_HISTORIC_CONFIRM_VALIDATE" );
        Object[] options = {msgValidate, msgCancel};

        int confirm = JOptionPane.showOptionDialog
                ( null ,
                  msgConfirm , //message
                  msgTitle , //title
                  JOptionPane.DEFAULT_OPTION ,
                  JOptionPane.WARNING_MESSAGE ,
                  null ,
                  options ,
                  options[ 0 ] );

        if ( confirm != JOptionPane.OK_OPTION )
        {
            //System.out.println ( "CLA/sdqsdqsdqsd/wasChecked/"+wasChecked+"/" );
            GeneralTab generalTab = GeneralTab.getInstance();
            generalTab.setHistoric( wasChecked );
            return;
        }


        VCAttributesTreeModel vCAttributesTreeModel = VCAttributesTreeModel.getInstance();
        VCPossibleAttributesTreeModel vCPossibleAttributesTreeModel = VCPossibleAttributesTreeModel.getInstance();

        vCAttributesTreeModel.removeAll();
        vCPossibleAttributesTreeModel.removeAll();

        vCPossibleAttributesTreeModel = VCPossibleAttributesTreeModel.forceGetInstance( historicCheck.isSelected() );
        VCPossibleAttributesTree tree = VCPossibleAttributesTree.getInstance();
        tree.setModel( vCPossibleAttributesTreeModel );

        vCPossibleAttributesTreeModel.setRootName( historicCheck.isSelected() );
        vCAttributesTreeModel.setRootName( historicCheck.isSelected() );

    }


}
