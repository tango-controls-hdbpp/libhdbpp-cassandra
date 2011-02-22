//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/SaveOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SaveOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: SaveOptions.java,v $
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

import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsSaveOptionsTab;
import fr.soleil.bensikin.lifecycle.LifeCycleManager;
import fr.soleil.bensikin.lifecycle.LifeCycleManagerFactory;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;


/**
 * The save options of the application.
 * Contains:
 * -whether the application saves on shutdown/loads on startup
 *
 * @author CLAISSE
 */
public class SaveOptions extends ReadWriteOptionBook implements PushPullOptionBook
{
	/**
	 * The history saving property name
	 */
	public static final String HISTORY = "HISTORY";

	/**
	 * The code for the history saving property value of "Do save history"
	 */
	public static final int HISTORY_YES = 1;

	/**
	 * The code for the history saving property value of "Don't save history"
	 */
	public static final int HISTORY_NO = 0;

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "save";

	/**
	 * Default constructor
	 */
	public SaveOptions()
	{
		super(XML_TAG);
	}

	/* (non-Javadoc)
	 * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
	 */
	public void fillFromOptionsDialog()
	{
		OptionsSaveOptionsTab saveTab = OptionsSaveOptionsTab.getInstance();
		ButtonModel selectedModel = saveTab.getButtonGroup().getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		this.content.put(HISTORY , selectedActionCommand);
	}


	/* (non-Javadoc)
	 * @see bensikin.bensikin.options.PushPullOptionBook#push()
	 */
	public void push()
	{
		String val_s = ( String ) this.content.get(HISTORY);
		if ( val_s != null )
		{
			int val = Integer.parseInt(val_s);
			LifeCycleManager lifeCycleManager = LifeCycleManagerFactory.getCurrentImpl();

			switch ( val )
			{
				case HISTORY_YES:
					lifeCycleManager.setHasHistorySave(true);
					break;

				case HISTORY_NO:
					lifeCycleManager.setHasHistorySave(false);
					break;
			}

			OptionsSaveOptionsTab saveTab = OptionsSaveOptionsTab.getInstance();
			saveTab.selectHasSaveButton(val);
		}
	}
}
