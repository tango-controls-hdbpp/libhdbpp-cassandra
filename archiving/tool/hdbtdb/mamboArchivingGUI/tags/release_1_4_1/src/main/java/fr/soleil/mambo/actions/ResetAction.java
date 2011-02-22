//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/actions/ResetAction.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ResetAction.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: ResetAction.java,v $
// Revision 1.3  2006/04/05 13:40:04  ounsy
// optimization
//
// Revision 1.2  2005/11/29 18:27:45  chinkumo
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
package fr.soleil.mambo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import fr.soleil.mambo.components.archiving.OpenedACComboBox;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.view.ViewConfiguration;



public class ResetAction extends AbstractAction
{
    /**
     * @param name
     */
    public ResetAction ( String name , Icon icon )
    {
        super( name , icon );
        this.putValue( Action.NAME , name );
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed ( ActionEvent arg0 )
    {
        ViewConfiguration newVC = new ViewConfiguration();
        ViewConfiguration.setCurrentViewConfiguration( newVC );
        ViewConfiguration.setSelectedViewConfiguration( newVC );
        newVC.push();

        ArchivingConfiguration newAC = new ArchivingConfiguration();
        ArchivingConfiguration.setCurrentArchivingConfiguration( newAC );
        ArchivingConfiguration.setSelectedArchivingConfiguration( newAC );
        newAC.push();

        OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
        openedVCComboBox.empty();

        OpenedACComboBox openedACComboBox = OpenedACComboBox.getInstance();
        openedACComboBox.empty();
        System.gc();
    }
}
