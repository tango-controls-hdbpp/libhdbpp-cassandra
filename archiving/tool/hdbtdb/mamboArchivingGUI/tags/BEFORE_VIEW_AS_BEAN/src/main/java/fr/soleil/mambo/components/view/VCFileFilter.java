//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/components/view/VCFileFilter.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  VCFileFilter.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: VCFileFilter.java,v $
// Revision 1.2  2005/11/29 18:28:12  chinkumo
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
package fr.soleil.mambo.components.view;

import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.tools.Messages;

public class VCFileFilter extends ConfigurationFileFilter
{
    /**
     * 
     */
    public VCFileFilter ()
    {
        super( "vc" );
        description = Messages.getMessage( "FILE_CHOOSER_VIEW_CONFIGURATION" );
    }

}
