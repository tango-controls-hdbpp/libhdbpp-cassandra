//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/view/DynamicDateRangeCheckBoxAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DynamicDateRangeCheckBoxAction.
//						(Claisse Laurent) - oct. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.1 $
//
// $Log: DynamicDateRangeCheckBoxAction.java,v $
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
package fr.soleil.mambo.actions.view;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

import fr.soleil.mambo.containers.view.dialogs.GeneralTab;


public class DynamicDateRangeCheckBoxAction extends AbstractAction
{

    public DynamicDateRangeCheckBoxAction ()
    {
        super();
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        JCheckBox box = ( JCheckBox ) arg0.getSource();
        boolean selected = box.isSelected();
        GeneralTab.getInstance().setStartAndEndDatesFieldsEnabled( !selected );
    }

}
