//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/containers/archiving/dialogs/ACRecapDialog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACRecapDialog.
//						(Claisse Laurent) - oct. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: ACRecapDialog.java,v $
// Revision 1.2  2005/12/15 11:23:13  ounsy
// attributes informations refreshment
//
// Revision 1.1  2005/11/29 18:27:56  chinkumo
// no message
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.containers.archiving.dialogs;

import java.awt.Dimension;

import javax.swing.JDialog;

import fr.soleil.mambo.containers.MamboFrame;
import fr.soleil.mambo.containers.archiving.ACRecapPanel;
import fr.soleil.mambo.tools.Messages;


public class ACRecapDialog extends JDialog
{
    private static ACRecapDialog instance;
    
    public static ACRecapDialog getInstance (boolean forceReload) {
        if (instance == null || forceReload) {
            instance = new ACRecapDialog ();
        }
        return instance;
    }

    private ACRecapDialog ()
    {
        super( MamboFrame.getInstance() , Messages.getMessage( "ARCHIVING_ASSESSMENT_TITLE" ) , true );
        this.setContentPane( ACRecapPanel.getInstance(true) );
        setSize( new Dimension( 1000 , 600 ) );
        setLocation( 0 , 0 );
    }

}
