//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/sub/WordlistOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  WordlistOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: WordlistOptions.java,v $
// Revision 1.4  2006/05/19 15:05:29  ounsy
// minor changes
//
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

import javax.swing.ButtonModel;

import fr.soleil.mambo.containers.sub.dialogs.options.OptionsWordlistTab;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;

/*import mambo.lifecycle.LifeCycleManager;
import mambo.lifecycle.LifeCycleManagerFactory;*/

public class WordlistOptions extends ReadWriteOptionBook implements PushPullOptionBook
{
    public static final String WORDLIST = "WORDLIST";
    public static final int WORDLIST_SOLEIL = 0;
    public static final int WORDLIST_TANGO = 1;
    public static final int WORDLIST_CUSTOM = 2;

    public static final String KEY = "wordlist"; //for XML save and load

    /**
     * 
     */
    public WordlistOptions ()
    {
        super( KEY );
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
     */
    public void fillFromOptionsDialog ()
    {
        OptionsWordlistTab wordlistTab = OptionsWordlistTab.getInstance();
        ButtonModel selectedModel = wordlistTab.getButtonGroup().getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();
        //GUIUtilities.trace ( 9 , "WordlistOptions/fillFromOptionsDialog/selectedActionCommand/"+selectedActionCommand+"/" );

        this.content.put( WORDLIST , selectedActionCommand );
    }


    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#push()
     */
    public void push ()
    {
        String val_s = ( String ) this.content.get( WORDLIST );
        if ( val_s != null )
        {
            int val = Integer.parseInt( val_s );
            //LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl();

            switch ( val )
            {
                case WORDLIST_SOLEIL:
                    //do nothing for now
                    break;

                case WORDLIST_TANGO:
                    //do nothing for now
                    break;

                case WORDLIST_CUSTOM:
                    //do nothing for now
                    break;
            }

            OptionsWordlistTab saveTab = OptionsWordlistTab.getInstance();
            saveTab.selectWordlistButton( val );
        }
    }

}
