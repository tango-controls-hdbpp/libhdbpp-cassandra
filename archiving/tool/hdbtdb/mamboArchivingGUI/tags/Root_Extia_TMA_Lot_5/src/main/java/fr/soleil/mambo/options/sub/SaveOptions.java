//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/sub/SaveOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SaveOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: chinkumo $
//
// $Revision: 1.2 $
//
// $Log: SaveOptions.java,v $
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

import fr.soleil.mambo.containers.sub.dialogs.options.OptionsSaveOptionsTab;
import fr.soleil.mambo.lifecycle.LifeCycleManager;
import fr.soleil.mambo.lifecycle.LifeCycleManagerFactory;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;

public class SaveOptions extends ReadWriteOptionBook implements
		PushPullOptionBook {
	public static final String HISTORY = "HISTORY";
	public static final int HISTORY_YES = 1;
	public static final int HISTORY_NO = 0;

	public static final String KEY = "save"; // for XML save and load

	/**
     * 
     */
	public SaveOptions() {
		super(KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
	 */
	public void fillFromOptionsDialog() {
		OptionsSaveOptionsTab saveTab = OptionsSaveOptionsTab.getInstance();
		ButtonModel selectedModel = saveTab.getButtonGroup().getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();
		// GUIUtilities.trace ( 9 ,
		// "SaveOptions/fillFromOptionsDialog/selectedActionCommand/"+selectedActionCommand+"/"
		// );

		this.content.put(HISTORY, selectedActionCommand);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#push()
	 */
	public void push() {
		String val_s = (String) this.content.get(HISTORY);
		if (val_s != null) {
			int val = Integer.parseInt(val_s);
			LifeCycleManager lifeCycleManager = LifeCycleManagerFactory
					.getCurrentImpl();

			switch (val) {
			case HISTORY_YES:
				// GUIUtilities.trace ( 9 , "SAVE_HISTORY_YES !!!" );
				lifeCycleManager.setHasHistorySave(true);
				break;

			case HISTORY_NO:
				// GUIUtilities.trace ( 9 , "SAVE_HISTORY_NO !!!" );
				lifeCycleManager.setHasHistorySave(false);
				break;
			}

			OptionsSaveOptionsTab saveTab = OptionsSaveOptionsTab.getInstance();
			saveTab.selectHasSaveButton(val);
		}

	}

}
