//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/sub/DisplayOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DisplayOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: DisplayOptions.java,v $
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

import fr.soleil.mambo.containers.sub.dialogs.options.OptionsDisplayTab;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;


public class DisplayOptions extends ReadWriteOptionBook implements PushPullOptionBook
{
    public static final String PLAF = "PLAF";
    public static final int PLAF_WIN = 0;
    public static final int PLAF_JAVA = 1;

    public static final String KEY = "display"; //for XML save and load

    /**
     * 
     */
    public DisplayOptions ()
    {
        super( KEY );
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
     */
    public void fillFromOptionsDialog ()
    {
        OptionsDisplayTab displayTab = OptionsDisplayTab.getInstance();
        ButtonModel selectedModel = displayTab.getButtonGroup().getSelection();
        String selectedActionCommand = selectedModel.getActionCommand();
        //GUIUtilities.trace ( 9 , "DisplayOptions/fillFromOptionsDialog/selectedActionCommand/"+selectedActionCommand+"/" );
        
        this.content.put( PLAF , selectedActionCommand );
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.options.PushPullOptionBook#push()
     */
    
    public void push () 
    {
        /*String plaf_s = (String) this.content.get ( PLAF );
        if ( plaf_s != null )
        {
            int plaf = Integer.parseInt ( plaf_s );
            MamboFrame frame = MamboFrame.getInstance ();
            OptionsDialog.resetInstance ();
    	    frame.dispose ();
            
            switch ( plaf )
            {
            	case PLAF_WIN :
            	    UIManager.setLookAndFeel ( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
            	    frame.setUndecorated ( false );
            	break;
            	
            	case PLAF_JAVA :
            	    UIManager.setLookAndFeel ( UIManager.getCrossPlatformLookAndFeelClassName () );
            	    frame.setUndecorated ( true );
                break;
            }
             
            SwingUtilities.updateComponentTreeUI ( frame );
            frame.setVisible ( true );
            
            OptionsDisplayTab displayTab = OptionsDisplayTab.getInstance ();
            displayTab.selectPlafButton ( plaf );
            
            OptionsDialog.resetInstance ();
        }*/

    }


}
