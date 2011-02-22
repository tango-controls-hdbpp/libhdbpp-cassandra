//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/archiving/ACFileFilter.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ACFileFilter.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: ACFileFilter.java,v $
// Revision 1.2  2005/11/29 18:27:24  chinkumo
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
package fr.soleil.mambo.components.archiving;

import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.tools.Messages;

public class ACFileFilter extends ConfigurationFileFilter
{
    /**
     * 
     */
    public ACFileFilter ()
    {
        super( "ac" );
        description = Messages.getMessage( "FILE_CHOOSER_ARCHIVING_CONFIGURATION" );
    }

}
