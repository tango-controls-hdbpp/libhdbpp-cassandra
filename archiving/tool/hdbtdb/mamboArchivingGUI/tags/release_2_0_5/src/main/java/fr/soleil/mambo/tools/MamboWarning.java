//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/tools/MamboWarning.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  MamboWarning.
//						(Claisse Laurent) - 19 sept. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: MamboWarning.java,v $
// Revision 1.2  2005/11/29 18:28:26  chinkumo
// no message
//
// Revision 1.1.2.1  2005/09/26 07:50:49  chinkumo
// First commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.tools;


public class MamboWarning extends Exception
{

    

    /**
     * @param arg0
     */
    public MamboWarning ( String arg0 )
    {
        super( arg0 );
        // TODO Auto-generated constructor stub
    }

    
   

    public void printStackTrace ()
    {
        //do nothing so that the end user doesn't think there is a bug
    }

}
