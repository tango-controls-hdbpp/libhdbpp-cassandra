//+======================================================================
//$Source$
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ACFileFilter.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author$
//
//$Revision$
//
//$Log$
//Revision 1.1  2006/10/03 14:51:36  ounsy
//ACDefaultsFileFilter moved in mambo.components.archiving instead of mambo.components
//
//Revision 1.1  2005/11/29 18:27:24  chinkumo
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
package fr.soleil.mambo.components.archiving;

import fr.soleil.mambo.components.ConfigurationFileFilter;
import fr.soleil.mambo.tools.Messages;

public class ACDefaultsFileFilter extends ConfigurationFileFilter
{
    /**
     * The contexts files extension "acd"
     */
    public static final String FILE_EXTENSION = "acd";

    /**
     * Builds with FILE_EXTENSION extension and a description found in resources.
     */
    public ACDefaultsFileFilter ()
    {
        super( FILE_EXTENSION );
        description = Messages.getMessage( "FILE_CHOOSER_AC_DEFAULTS" );
    }

}
