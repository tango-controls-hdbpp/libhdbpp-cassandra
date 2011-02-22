// +======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/options/sub/VCOptions.java,v $
//
// Project: Tango Archiving Service
//
// Description: Java source code for the class DisplayOptions.
// (Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: VCOptions.java,v $
// Revision 1.7 2007/05/10 14:48:16 ounsy
// possibility to change "no value" String in chart data file (default is "*")
// through vc option
//
// Revision 1.6 2006/09/22 09:34:41 ounsy
// refactoring du package mambo.datasources.db
//
// Revision 1.5 2006/08/31 09:53:54 ounsy
// User always knows whether data export will be forced
//
// Revision 1.4 2006/08/31 08:45:02 ounsy
// forceExport is now false on defaults
//
// Revision 1.3 2006/07/13 12:51:57 ounsy
// added a doForceTdbExport property
//
// Revision 1.2 2006/03/29 10:28:03 ounsy
// removed useless "throws Exception "
//
// Revision 1.1 2005/11/29 18:28:12 chinkumo
// no message
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

import fr.soleil.mambo.bean.manager.ViewConfigurationBeanManager;
import fr.soleil.mambo.components.view.OpenedVCComboBox;
import fr.soleil.mambo.containers.sub.dialogs.options.OptionsVCTab;
import fr.soleil.mambo.containers.view.ViewSelectionPanel;
import fr.soleil.mambo.datasources.db.extracting.ExtractingManagerFactory;
import fr.soleil.mambo.datasources.db.extracting.IExtractingManager;
import fr.soleil.mambo.options.PushPullOptionBook;
import fr.soleil.mambo.options.ReadWriteOptionBook;

public class VCOptions extends ReadWriteOptionBook implements
		PushPullOptionBook {

	public static final String DISPLAY_WRITE = "DISPLAY_WRITE";
	public static final int DISPLAY_WRITE_NO = 0;
	public static final int DISPLAY_WRITE_YES = 1;

	public static final String DISPLAY_READ = "DISPLAY_READ";
	public static final int DISPLAY_READ_NO = 0;
	public static final int DISPLAY_READ_YES = 1;

	public static final String STACK_DEPTH = "STACK_DEPTH";
	public static final int DEFAULT_STACK_DEPTH = 5;

	public static final String MAX_DISPLAYED_VIEWS = "MAX_DISPLAYED_VIEWS";
	public static final int DEFAULT_MAX_DISPLAYED_VIEWS = 2;

	public static final String FORCE_TDB_EXPORT = "FORCE_TDB_EXPORT";
	public static final int FORCE_TDB_EXPORT_NO = 0;
	public static final int FORCE_TDB_EXPORT_YES = 1;

	public static final String CHART_NO_VALUE_STRING = "CHART_NO_VALUE_STRING";

	public static final String KEY = "VCs"; // for
	// XML
	// save
	// and
	// load

	private boolean doForceTdbExport = false;
	private int stackDepth = DEFAULT_STACK_DEPTH;
	private int maxDisplayedViews = DEFAULT_MAX_DISPLAYED_VIEWS;

	/**
     *
     */
	public VCOptions() {
		super(KEY);
	}

	@Override
	public void fillFromOptionsDialog() {
		OptionsVCTab vcTab = OptionsVCTab.getInstance();

		ButtonModel selectedModel = vcTab.getWriteButtonGroup().getSelection();
		String selectedActionCommand = selectedModel.getActionCommand();
		content.put(DISPLAY_WRITE, selectedActionCommand);

		selectedModel = vcTab.getReadButtonGroup().getSelection();
		selectedActionCommand = selectedModel.getActionCommand();
		content.put(DISPLAY_READ, selectedActionCommand);

		selectedModel = vcTab.getForceTdbExportButtonGroup().getSelection();
		selectedActionCommand = selectedModel.getActionCommand();
		content.put(FORCE_TDB_EXPORT, selectedActionCommand);

		String stackDepth_s = vcTab.getStackDepth();
		try {
			stackDepth = Integer.parseInt(stackDepth_s);
			content.put(STACK_DEPTH, stackDepth_s);
		} catch (NumberFormatException e) {
			// If the new value is bad, we keep the last good one.
			// We could also put the default value:
			// content.put( STACK_DEPTH , String.valueOf(this.getStackDepth())
			// );
		}

		String maxDisplayedViews_s = vcTab.getMaxDisplayedViews();
		try {
			maxDisplayedViews = Integer.parseInt(maxDisplayedViews_s);
			content.put(MAX_DISPLAYED_VIEWS, maxDisplayedViews_s);
		} catch (NumberFormatException e) {
			// If the new value is bad, we keep the last good one.
			// We could also put the default value:
			// content.put( MAX_DISPLAYED_VIEWS ,
			// String.valueOf(this.getMaxDisplayedViews())
			// );
		}
		if (maxDisplayedViews > stackDepth) {
			maxDisplayedViews = stackDepth;
			maxDisplayedViews_s = Integer.toString(maxDisplayedViews);
			content.put(MAX_DISPLAYED_VIEWS, maxDisplayedViews_s);
		}

		String noValueString = vcTab.getNoValueString();
		content.put(CHART_NO_VALUE_STRING, noValueString);
	}

	@Override
	public void push() {
		OptionsVCTab vcTab = OptionsVCTab.getInstance();
		IExtractingManager extractingManager = ExtractingManagerFactory
				.getCurrentImpl();

		String plaf_s = (String) content.get(DISPLAY_WRITE);
		if (plaf_s != null) {
			int plaf = Integer.parseInt(plaf_s);

			switch (plaf) {
			case DISPLAY_WRITE_YES:
				extractingManager.setShowWrite(true);
				break;

			case DISPLAY_WRITE_NO:
				extractingManager.setShowWrite(false);
				break;
			}

			vcTab.selectDisplayWrite(plaf);
		}

		plaf_s = (String) content.get(DISPLAY_READ);
		if (plaf_s != null) {
			int plaf = Integer.parseInt(plaf_s);

			switch (plaf) {
			case DISPLAY_READ_YES:
				extractingManager.setShowRead(true);
				break;

			case DISPLAY_READ_NO:
				extractingManager.setShowRead(false);
				break;
			}

			vcTab.selectDisplayRead(plaf);
		}

		plaf_s = (String) content.get(FORCE_TDB_EXPORT);
		// System.out.println ( "CLA/VCOptions/plaf_s/"+plaf_s );
		if (plaf_s != null) {

			int plaf = Integer.parseInt(plaf_s);

			switch (plaf) {
			case FORCE_TDB_EXPORT_YES:
				this.setDoForceTdbExport(true);
				break;

			case FORCE_TDB_EXPORT_NO:
				this.setDoForceTdbExport(false);
				break;

			default:
				this.setDoForceTdbExport(false);
				break;
			}

			vcTab.selectForceTdbExport(plaf);
		} else {
			this.setDoForceTdbExport(false);
			vcTab.selectForceTdbExport(FORCE_TDB_EXPORT_NO);
		}

		String stackDepth_s = (String) content.get(STACK_DEPTH);
		try {
			stackDepth = Integer.parseInt(stackDepth_s);
		} catch (NumberFormatException e) {
			stackDepth = DEFAULT_STACK_DEPTH;
			stackDepth_s = String.valueOf(stackDepth);
			content.put(STACK_DEPTH, stackDepth_s);
		}
		vcTab.setStackDepth(stackDepth_s);
		OpenedVCComboBox openedVCComboBox = OpenedVCComboBox.getInstance();
		openedVCComboBox.setMaxSize(stackDepth);

		String maxDisplayedViews_s = (String) content.get(MAX_DISPLAYED_VIEWS);
		try {
			maxDisplayedViews = Integer.parseInt(maxDisplayedViews_s);
		} catch (NumberFormatException e) {
			maxDisplayedViews = DEFAULT_MAX_DISPLAYED_VIEWS;
			maxDisplayedViews_s = String.valueOf(maxDisplayedViews);
			content.put(MAX_DISPLAYED_VIEWS, maxDisplayedViews_s);
		}
		if (maxDisplayedViews > stackDepth) {
			maxDisplayedViews = stackDepth;
			maxDisplayedViews_s = Integer.toString(maxDisplayedViews);
			content.put(MAX_DISPLAYED_VIEWS, maxDisplayedViews_s);
		}
		vcTab.setMaxDisplayedViews(maxDisplayedViews_s);
		ViewConfigurationBeanManager.getInstance().setMaximumDisplayedViews(
				maxDisplayedViews);

		String noValueString = (String) content.get(CHART_NO_VALUE_STRING);
		noValueString = noValueString == null ? "*" : noValueString;
		vcTab.setNoValueString(noValueString);
		content.put(CHART_NO_VALUE_STRING, noValueString);

		ViewSelectionPanel.getInstance().updateForceExport();
	}

	/**
	 * @return Returns the doForceTdbExport.
	 */
	public boolean isDoForceTdbExport() {
		return doForceTdbExport;
	}

	private void setDoForceTdbExport(boolean b) {
		// System.out.println ( "CLA/setDoForceTdbExport/b/"+b );
		doForceTdbExport = b;
	}

	/**
	 * @return Returns the stackDepth.
	 */
	public int getStackDepth() {
		return stackDepth;
	}

	/**
	 * @param stackDepth
	 *            The stackDepth to set.
	 */
	public void setStackDepth(int stackDepth) {
		this.stackDepth = stackDepth;
	}

	/**
	 * @return Returns the maxDisplayedViews.
	 */
	public int getMaxDisplayedViews() {
		return maxDisplayedViews;
	}

	/**
	 * @param maxDisplayedViews
	 *            The maxDisplayedViews to set.
	 */
	public void setMaxDisplayedViews(int maxDisplayedViews) {
		this.maxDisplayedViews = maxDisplayedViews;
	}

}
