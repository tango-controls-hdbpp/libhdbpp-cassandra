//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/WordlistOptions.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  WordlistOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: WordlistOptions.java,v $
// Revision 1.5  2006/06/28 12:54:09  ounsy
// minor changes
//
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

import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsWordlistTab;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;

/**
 * The wordlisy options of the application. Not used yet
 * 
 * @author CLAISSE
 */
public class WordlistOptions extends ReadWriteOptionBook implements
		PushPullOptionBook {
	/**
	 * The wordlist saving property name
	 */
	public static final String WORDLIST = "WORDLIST";
	/**
	 * Use the Soleil wordlist
	 */
	public static final int WORDLIST_SOLEIL = 0;
	/**
	 * Use the Tango wordlist
	 */
	public static final int WORDLIST_TANGO = 1;
	/**
	 * Use a custom wordlist
	 */
	public static final int WORDLIST_CUSTOM = 2;

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "wordlist";

	/**
	 * Default constructor
	 */
	public WordlistOptions() {
		super(XML_TAG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
	 */
	public void fillFromOptionsDialog() {
		OptionsWordlistTab wordlistTab = OptionsWordlistTab.getInstance();
		ButtonModel selectedModel = wordlistTab.getButtonGroup().getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();

		this.content.put(WORDLIST, selectedActionCommand);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#push()
	 */
	public void push() throws Exception {
		String val_s = (String) this.content.get(WORDLIST);
		if (val_s != null) {
			int val = Integer.parseInt(val_s);
			// LifeCycleManager lifeCycleManager =
			// LifeCycleManagerFactory.getCurrentImpl();

			switch (val) {
			case WORDLIST_SOLEIL:
				// do nothing for now
				break;

			case WORDLIST_TANGO:
				// do nothing for now
				break;

			case WORDLIST_CUSTOM:
				// do nothing for now
				break;
			}

			OptionsWordlistTab saveTab = OptionsWordlistTab.getInstance();
			saveTab.selectWordlistButton(val);
		}
	}

}
