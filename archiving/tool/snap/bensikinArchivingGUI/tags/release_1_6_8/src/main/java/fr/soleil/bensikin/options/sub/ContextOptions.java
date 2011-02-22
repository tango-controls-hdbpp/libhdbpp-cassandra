//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/options/sub/ContextOptions.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  DisplayOptions.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: pierrejoseph $
//
//$Revision: 1.3 $
//
//$Log: ContextOptions.java,v $
//Revision 1.3  2007/08/30 14:01:51  pierrejoseph
//* java 1.5 programming
//
//Revision 1.2  2006/06/28 12:54:09  ounsy
//minor changes
//
//Revision 1.1  2005/12/14 16:58:00  ounsy
//added methods necessary for alternate attribute selection
//
//Revision 1.1.2.2  2005/09/14 15:41:44  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.options.sub;

import java.util.Enumeration;

import javax.swing.ButtonModel;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.bensikin.containers.context.ContextDetailPanel;
import fr.soleil.bensikin.containers.sub.dialogs.options.OptionsContextTab;
import fr.soleil.bensikin.options.PushPullOptionBook;
import fr.soleil.bensikin.options.ReadWriteOptionBook;

public class ContextOptions extends ReadWriteOptionBook implements
		PushPullOptionBook {
	public static final String SELECTION_MODE = "SELECTION_MODE";
	public static final int SELECTION_MODE_TREE = 0;
	public static final int SELECTION_MODE_TABLE = 1;

	/**
	 * The XML tag name used in saving/loading
	 */
	public static final String XML_TAG = "context";
	private boolean isAlternateSelectionMode;

	/**
	   *
	   */
	public ContextOptions() {
		super(XML_TAG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#fillFromOptionsDialog()
	 */
	public void fillFromOptionsDialog() {
		fillSelectionMode();

	}

	/**
	   *
	   */
	private void fillSelectionMode() {
		OptionsContextTab acTab = OptionsContextTab.getInstance();

		ButtonModel selectedModel = acTab.getButtonGroup().getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();
		this.content.put(SELECTION_MODE, selectedActionCommand);

		int plaf = Integer.parseInt(selectedActionCommand);
		// boolean isAlternate = false;

		switch (plaf) {
		case SELECTION_MODE_TREE:
			this.isAlternateSelectionMode = false;
			break;

		case SELECTION_MODE_TABLE:
			this.isAlternateSelectionMode = true;
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.options.PushPullOptionBook#push()
	 */
	public void push() throws Exception {
		pushSelectionMode();

	}

	public void pushSelectionMode() throws Exception {
		String plaf_s = (String) this.content.get(SELECTION_MODE);

		if (plaf_s != null) {
			int plaf = Integer.parseInt(plaf_s);
			boolean isAlternate = false;

			switch (plaf) {
			case SELECTION_MODE_TREE:
				isAlternate = false;
				break;

			case SELECTION_MODE_TABLE:
				isAlternate = true;
				break;
			}

			this.isAlternateSelectionMode = isAlternate;
			// TO DO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			/*
			 * ArchivingActionPanel.setAlternateSelectionMode( isAlternate );
			 * 
			 * ACEditDialog dialog = ACEditDialog.getInstance(); if ( dialog !=
			 * null ) { dialog.setAlternateSelectionMode( isAlternate ); }
			 */

			ContextDetailPanel editPanel = ContextDetailPanel.getInstance();
			if (editPanel != null) {
				editPanel.setAlternateSelectionMode(isAlternate);
			}

			OptionsContextTab acTab = OptionsContextTab.getInstance();
			acTab.selectIsAlternate(isAlternate);
		}
	}

	/**
	 * @return
	 */
	public String toPropertiesString() {
		String ret = "";

		Enumeration<String> enumer = content.keys();
		while (enumer.hasMoreElements()) {
			String nextOptionKey = enumer.nextElement();
			String nextOptionValue = content.get(nextOptionKey);
			if (SELECTION_MODE.equals(nextOptionKey)) {
				continue;
			}

			String nextLine = nextOptionKey + " = " + nextOptionValue;

			ret += nextLine;
			ret += GUIUtilities.CRLF;
		}

		return ret;
	}

	/**
	 * @return
	 */
	public boolean isAlternateSelectionMode() {
		return isAlternateSelectionMode;
	}
}
