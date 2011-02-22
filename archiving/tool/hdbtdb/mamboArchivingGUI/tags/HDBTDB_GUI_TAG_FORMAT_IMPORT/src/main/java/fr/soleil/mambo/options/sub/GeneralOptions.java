// +======================================================================
// $Source:
// /cvsroot/tango-cs/tango/tools/mambo/options/sub/GeneralOptions.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class GeneralOptions.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: GeneralOptions.java,v $
// Revision 1.5 2006/09/20 12:50:29 ounsy
// changed imports
//
// Revision 1.4 2006/05/29 15:48:43 ounsy
// bug correction on attribute-level buffering
//
// Revision 1.3 2006/05/16 12:00:14 ounsy
// added a Tango buffering option
//
// Revision 1.2 2006/03/10 12:03:25 ounsy
// state and string support
//
// Revision 1.1 2005/12/15 11:46:23 ounsy
// "copy table to clipboard" management
//
// Revision 1.1.2.2 2005/09/14 15:41:44 chinkumo
// Second commit !
//
//
// copyleft : Synchrotron SOLEIL
// L'Orme des Merisiers
// Saint-Aubin - BP 48
// 91192 GIF-sur-YVETTE CEDEX
//
// -======================================================================
package fr.soleil.mambo.options.sub;

import javax.swing.ButtonModel;

import fr.soleil.mambo.containers.archiving.dialogs.ACEditDialog;
import fr.soleil.mambo.containers.sub.dialogs.options.OptionsGeneralTab;
import fr.soleil.mambo.datasources.tango.standard.TangoManagerFactory;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;
import fr.soleil.tango.util.entity.bean.AttributeTableSelectionBean;
import fr.soleil.tango.util.entity.datasource.ITangoEntitiesSelectionManager;

public class GeneralOptions extends ReadWriteOptionBook implements
		PushPullOptionBook {

	public static final String TABLE_SEPARATOR = "TABLE_SEPARATOR";

	// XML save and load
	public static final String KEY = "general"; // for

	public static final String BUFFER_TANGO_ATTRIBUTES = "BUFFER_TANGO_ATTRIBUTES";
	public static final int BUFFER_TANGO_ATTRIBUTES_NO = 0;
	public static final int BUFFER_TANGO_ATTRIBUTES_YES = 1;

	public final static ITangoEntitiesSelectionManager BUFFERED_TANGO_SELECTION_MANAGER = AttributeTableSelectionBean
			.generateTangoManager(AttributeTableSelectionBean.BUFFERED_AND_SORTED);
	public final static ITangoEntitiesSelectionManager CLASSIC_TANGO_SELECTION_MANAGER = AttributeTableSelectionBean
			.generateTangoManager(AttributeTableSelectionBean.SORTED);

	// This way, we are sure domains are loaded only once
	static {
		AttributeTableSelectionBean tempBean = new AttributeTableSelectionBean();
		tempBean.setTangoManager(BUFFERED_TANGO_SELECTION_MANAGER);
		tempBean.start();
		tempBean.setTangoManager(AttributeTableSelectionBean
				.generateTangoManager(AttributeTableSelectionBean.DUMMY));
		tempBean = null;
	}

	/**
     * 
     */
	public GeneralOptions() {
		super(KEY);
	}

	@Override
	public void fillFromOptionsDialog() {
		OptionsGeneralTab generalTab = OptionsGeneralTab.getInstance();
		String separator = generalTab.getSeparator();
		this.content.put(TABLE_SEPARATOR, separator);

		ButtonModel selectedModel = generalTab
				.getBufferTangoAttributesButtonGroup().getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();
		this.content.put(BUFFER_TANGO_ATTRIBUTES, selectedActionCommand);
	}

	@Override
	public void push() {
		OptionsGeneralTab optionsGeneralTab = OptionsGeneralTab.getInstance();

		String separator = (String) this.content.get(TABLE_SEPARATOR);
		if (separator != null) {
			optionsGeneralTab.setSeparator(separator);
		}

		String bufferTangoAttributes_s = (String) this.content
				.get(BUFFER_TANGO_ATTRIBUTES);
		boolean buffered = true;
		if (bufferTangoAttributes_s != null) {
			int bufferTangoAttributes = Integer
					.parseInt(bufferTangoAttributes_s);
			switch (bufferTangoAttributes) {
			case BUFFER_TANGO_ATTRIBUTES_NO:
				buffered = false;
				break;
			case BUFFER_TANGO_ATTRIBUTES_YES:
			default:
				buffered = true;
				break;
			}
		}
		optionsGeneralTab.setBufferTangoAttributes(buffered);
		TangoManagerFactory.setBuffered(buffered);
		if (buffered) {
			ACEditDialog
					.setDefaultTangoManager(BUFFERED_TANGO_SELECTION_MANAGER);
			if (ACEditDialog.getInstance() != null) {
				ACEditDialog.getInstance().getAttributeTableSelectionBean()
						.setTangoManager(BUFFERED_TANGO_SELECTION_MANAGER);
			}
		} else {
			ACEditDialog
					.setDefaultTangoManager(CLASSIC_TANGO_SELECTION_MANAGER);
			if (ACEditDialog.getInstance() != null) {
				ACEditDialog.getInstance().getAttributeTableSelectionBean()
						.setTangoManager(CLASSIC_TANGO_SELECTION_MANAGER);
			}
		}
	}

	public String getSeparator() {
		String separator = (String) this.content.get(TABLE_SEPARATOR);
		if (separator == null || "".equals(separator)) {
			return ";";
		}
		return separator;
	}
}
