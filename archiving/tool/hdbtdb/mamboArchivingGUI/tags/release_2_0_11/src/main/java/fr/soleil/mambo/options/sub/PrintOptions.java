//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/sub/PrintOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  PrintOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: PrintOptions.java,v $
// Revision 1.3  2006/03/29 10:28:03  ounsy
// removed useless "throws Exception "
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.options.sub;

import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;


public class PrintOptions extends ReadWriteOptionBook implements PushPullOptionBook
{
    /*public static final String PLAF = "PLAF";
    public static final int PLAF_WIN = 0;
    public static final int PLAF_JAVA = 1;*/
    
    public static final String KEY = "print"; //for XML save and load

    /**
     * 
     */
    public PrintOptions ()
    {
        super( KEY );
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
     */
    public void fillFromOptionsDialog ()
    {

    }


    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#push()
     */
    public void push ()
    {

    }

}
