//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/DisplayOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DisplayOptions.
//						(Claisse Laurent) - 30 juin 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: DisplayOptions.java,v $
// Revision 1.4  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.options.sub;

import javax.swing.ButtonModel;

import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsDisplayTab;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;


/**
 * The display options of the application.
 * Contains:
 * -the look and feel
 *
 * @author CLAISSE
 */
public class DisplayOptions extends ReadWriteOptionBook implements PushPullOptionBook
{
	/**
	 * The pluggable look-and-feel property name
	 */
	public static final String PLAF = "PLAF";

	/**
	 * The code for the pluggable look-and-feel property value of "Windows look-and-feel"
	 */
	public static final int PLAF_WIN = 0;

	/**
	 * The code for the pluggable look-and-feel property value of "JAVA look-and-feel"
	 */
	public static final int PLAF_JAVA = 1;

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "display";

	/**
	 * Default constructor
	 */
	public DisplayOptions()
	{
		super(XML_TAG);
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
	 */
	public void fillFromOptionsDialog()
	{
		OptionsDisplayTab displayTab = OptionsDisplayTab.getInstance();
		ButtonModel selectedModel = displayTab.getButtonGroup().getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		this.content.put(PLAF , selectedActionCommand);
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.options.PushPullOptionBook#push()
	 */
	public void push() throws Exception
	{
		/*String plaf_s = ( String ) this.content.get( PLAF );
		if ( plaf_s != null )
		{
		    int plaf = Integer.parseInt( plaf_s );
		    switch ( plaf )
		    {
		        case PLAF_WIN:
		            UIManager.setLookAndFeel( "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" );
		            break;

		        case PLAF_JAVA:
		            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		            break;
		    }

		    BensikinFrame frame = BensikinFrame.getInstance();
		    SwingUtilities.updateComponentTreeUI( frame );

		    OptionsDisplayTab displayTab = OptionsDisplayTab.getInstance();
		    displayTab.selectPlafButton( plaf );

		    OptionsDialog.resetInstance();
		}*/

	}
}
