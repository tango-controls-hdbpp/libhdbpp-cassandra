//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/data/archiving/ArchivingConfigurationAttributeTDBProperties.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  ArchivingConfigurationAttributeTDBProperties.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: pierrejoseph $
//
// $Revision: 1.7 $
//
// $Log: ArchivingConfigurationAttributeTDBProperties.java,v $
// Revision 1.7  2007/02/01 14:16:29  pierrejoseph
// XmlHelper reorg
//
// Revision 1.6  2006/12/07 16:45:39  ounsy
// removed keeping period
//
// Revision 1.5  2006/09/22 09:34:41  ounsy
// refactoring du package  mambo.datasources.db
//
// Revision 1.4  2006/06/15 15:39:11  ounsy
// added support for dedicate archivers definition
//
// Revision 1.3  2006/02/24 12:21:19  ounsy
// small modifications
//
// Revision 1.2  2005/11/29 18:27:56  chinkumo
// no message
//
// Revision 1.1.2.3  2005/09/19 08:00:22  chinkumo
// Miscellaneous changes...
//
// Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.data.archiving;

import java.util.Vector;

import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.mambo.containers.archiving.dialogs.AttributesPropertiesPanel;
import fr.soleil.mambo.datasources.db.archiving.ArchivingManagerFactory;
import fr.soleil.mambo.datasources.db.archiving.IArchivingManager;
import fr.soleil.mambo.tools.GUIUtilities;
import fr.soleil.mambo.tools.xmlhelpers.XMLLine;

public class ArchivingConfigurationAttributeTDBProperties extends
		ArchivingConfigurationAttributeDBProperties {
	// private ArchivingConfigurationMode TDBMode;

	private long exportPeriod = -1;
	// private long keepingPeriod = -1;

	public static final String XML_TAG = "TDBModes";
	public static final String TDB_SPEC_EXPORT_PERIOD_XML_TAG = "exportPeriod";
	// public static final String TDB_SPEC_KEEPING_PERIOD_XML_TAG =
	// "keepingPeriod";

	private int defaultPeriod;
	private static final long EXPORT_PERIOD_MIN = 1000;
	// private static final long KEEPING_PERIOD_MIN = 1000;
	public static final int EXPORT_PERIOD_TOO_LOW = 2;

	// public static final int KEEPING_PERIOD_TOO_LOW = 3;

	public ArchivingConfigurationAttributeTDBProperties() {

	}

	public String toString() {
		String ret = "";
		// System.out.println (
		// "ArchivingConfigurationAttributeTDBProperties/toString/exportPeriod/"+exportPeriod+"/"
		// );
		XMLLine openingLine = new XMLLine(XML_TAG, XMLLine.OPENING_TAG_CATEGORY);
		openingLine.setAttribute(TDB_SPEC_EXPORT_PERIOD_XML_TAG, String
				.valueOf(exportPeriod));
		// openingLine.setAttribute( TDB_SPEC_KEEPING_PERIOD_XML_TAG ,
		// String.valueOf( keepingPeriod ) );
		openingLine
				.setAttribute(
						ArchivingConfigurationAttributeDBProperties.DEDICATED_ARCHIVER_PROPERTY_XML_TAG,
						super.getDedicatedArchiver());
		// System.out.println (
		// "ArchivingConfigurationAttributeTDBProperties/toString/openingLine/"+openingLine+"/"
		// );
		XMLLine closingLine = new XMLLine(XML_TAG, XMLLine.CLOSING_TAG_CATEGORY);

		ret += openingLine.toString();
		ret += GUIUtilities.CRLF;

		ret += super.toString();
		ret += GUIUtilities.CRLF;

		ret += closingLine.toString();

		return ret;
	}

	/**
	 * @return Returns the tDBMode.
	 */
	/*
	 * public ArchivingConfigurationMode getTDBMode() { return TDBMode; }
	 */
	/**
	 * @param mode
	 *            The tDBMode to set.
	 */
	/*
	 * public void setTDBMode(ArchivingConfigurationMode mode) { TDBMode = mode;
	 * }
	 */
	/**
	 * @return Returns the exportPeriod.
	 */
	public long getExportPeriod() {
		return exportPeriod;
	}

	/**
	 * @param exportPeriod
	 *            The exportPeriod to set.
	 */
	public void setExportPeriod(long exportPeriod) {
		this.exportPeriod = exportPeriod;
	}

	/**
	 * @deprecated
	 */
	public long getKeepingPeriod() {
		// return keepingPeriod;
		return 0;
	}

	/**
	 * @deprecated
	 */
	public void setKeepingPeriod(long keepingPeriod) {
		// this.keepingPeriod = keepingPeriod;
	}

	/**
	 * 25 juil. 2005
	 */
	public void push() {
		super.push();

		Vector modesTypes = super.getModesTypes();
		AttributesPropertiesPanel panel = AttributesPropertiesPanel
				.getInstance();
		panel.setSelectedModes(false, modesTypes);

		ArchivingConfigurationMode periodicalMode = super
				.getMode(ArchivingConfigurationMode.TYPE_P);

		if (periodicalMode != null && periodicalMode.getMode() != null
				&& periodicalMode.getMode().getModeP() != null) {
			int period = periodicalMode.getMode().getModeP().getPeriod();
			panel.setTDBPeriod(period);
		} else {
			panel
					.setTDBPeriod(Integer
							.parseInt(AttributesPropertiesPanel.DEFAULT_TDB_PERIOD_VALUE));
		}

		panel.setExportPeriod(this.exportPeriod);
		// panel.setKeepingPeriod( this.keepingPeriod );

		// MODE A START
		ArchivingConfigurationMode absoluteMode = super
				.getMode(ArchivingConfigurationMode.TYPE_A);
		if (absoluteMode != null && absoluteMode.getMode() != null
				&& absoluteMode.getMode().getModeA() != null) {
			ModeAbsolu modeA = absoluteMode.getMode().getModeA();

			int period = modeA.getPeriod();
			double lower = modeA.getValInf();
			double upper = modeA.getValSup();
			boolean slow_drift = modeA.isSlow_drift();
			panel.setTDBAbsolutePeriod(period);
			panel.setTDBAbsoluteLower(String.valueOf(lower));
			panel.setTDBAbsoluteUpper(String.valueOf(upper));
			panel.setTDBAbsoluteDLCheck(slow_drift);
		} else {
			panel.setTDBAbsolutePeriod(-1);
			panel.setTDBAbsoluteLower("");
			panel.setTDBAbsoluteUpper("");
			panel.setTDBAbsoluteDLCheck(false);
		}
		// MODE A END

		// MODE R START
		ArchivingConfigurationMode relativeMode = super
				.getMode(ArchivingConfigurationMode.TYPE_R);
		if (relativeMode != null && relativeMode.getMode() != null
				&& relativeMode.getMode().getModeR() != null) {
			ModeRelatif modeR = relativeMode.getMode().getModeR();

			int period = modeR.getPeriod();
			double lower = modeR.getPercentInf();
			double upper = modeR.getPercentSup();
			boolean slow_drift = modeR.isSlow_drift();

			panel.setTDBRelativePeriod(period);
			panel.setTDBRelativeLower(String.valueOf(lower));
			panel.setTDBRelativeUpper(String.valueOf(upper));
			panel.setTDBRelativeDLCheck(slow_drift);
		} else {
			panel.setTDBRelativePeriod(-1);
			panel.setTDBRelativeLower("");
			panel.setTDBRelativeUpper("");
			panel.setTDBRelativeDLCheck(false);
		}
		// MODE R END

		// MODE T START
		ArchivingConfigurationMode thresholdMode = super
				.getMode(ArchivingConfigurationMode.TYPE_T);
		if (thresholdMode != null && thresholdMode.getMode() != null
				&& thresholdMode.getMode().getModeT() != null) {
			ModeSeuil modeT = thresholdMode.getMode().getModeT();

			int period = modeT.getPeriod();
			double lower = modeT.getThresholdInf();
			double upper = modeT.getThresholdSup();

			panel.setTDBThresholdPeriod(period);
			panel.setTDBThresholdLower(String.valueOf(lower));
			panel.setTDBThresholdUpper(String.valueOf(upper));
		} else {
			panel.setTDBThresholdPeriod(-1);
			panel.setTDBThresholdLower("");
			panel.setTDBThresholdUpper("");
		}
		// MODE T END

		// MODE D START
		ArchivingConfigurationMode differenceMode = super
				.getMode(ArchivingConfigurationMode.TYPE_D);
		if (differenceMode != null && differenceMode.getMode() != null
				&& differenceMode.getMode().getModeD() != null) {
			ModeDifference modeD = differenceMode.getMode().getModeD();

			int period = modeD.getPeriod();
			panel.setTDBDifferencePeriod(period);
		} else {
			panel.setTDBDifferencePeriod(-1);
		}
		// MODE D END
	}

	/**
	 * @param period
	 *            26 juil. 2005
	 */
	public void setDefaultPeriod(int period) {
		// ----------------
		// period = (int) period/1000;
		// ----------------
		// this.defaultPeriod = period;
		this.defaultPeriod = (int) period / 1000;
		// System.out.println (
		// "TDBProperties/setDefaultPeriod/period/"+period+"/" );
		//

		ArchivingConfigurationMode ACPMode = this
				.getMode(ArchivingConfigurationMode.TYPE_P);
		if (ACPMode != null) {
			ACPMode.getMode().getModeP().setPeriod(period);
		}
	}

	public int getDefaultPeriod() {
		return defaultPeriod;
	}

	/**
	 * @param completeName
	 * @return 28 juil. 2005
	 * @throws Exception
	 */
	public static ArchivingConfigurationAttributeTDBProperties loadTDBProperties(
			String completeName) throws Exception {
		// return ArchivingConfigurationAttributeDBProperties.loadDBProperties (
		// completeName , true );
		IArchivingManager manager = ArchivingManagerFactory.getCurrentImpl();
		Mode mode = manager.getArchivingMode(completeName, false);
		ArchivingConfigurationMode[] ACModes = ArchivingConfigurationMode
				.buildModesList(mode);
		ArchivingConfigurationAttributeTDBProperties ret = new ArchivingConfigurationAttributeTDBProperties();
		if (ret == null) {
			return null;
		}
		ret.setModes(ACModes);
		return ret;
	}

	/**
	 * 9 sept. 2005
	 * 
	 * @throws ArchivingConfigurationException
	 * 
	 */
	public void controlValues() throws ArchivingConfigurationException {
		if (this.isEmpty()) {
			return;
		}

		if (exportPeriod < EXPORT_PERIOD_MIN) {
			// System.out.println (
			// "ArchivingConfigurationAttributeTDBProperties/controlValues/exportPeriod/"+exportPeriod+"/EXPORT_PERIOD_MIN/"+EXPORT_PERIOD_MIN+"/"
			// );
			throw new ArchivingConfigurationException(null,
					EXPORT_PERIOD_TOO_LOW);
		}

		/*
		 * if ( keepingPeriod < KEEPING_PERIOD_MIN ) { throw new
		 * ArchivingConfigurationException( null , KEEPING_PERIOD_TOO_LOW ); }
		 */

		super.controlValues();
	}
}
