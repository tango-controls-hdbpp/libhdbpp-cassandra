package fr.soleil.mambo.tools.xmlhelpers.ac;

import java.io.File;
import java.sql.Timestamp;
import java.util.Hashtable;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.soleil.archiving.gui.tools.GUIUtilities;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeAbsolu;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeCalcul;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeDifference;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModePeriode;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeRelatif;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.ModeSeuil;
import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationData;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationMode;
import fr.soleil.mambo.options.manager.IACDefaultsManager;
import fr.soleil.mambo.tools.xmlhelpers.XMLUtils;

public class ArchivingConfigurationXMLHelperStandard {
	/**
	 * @param location
	 * @return 26 juil. 2005
	 * @throws Exception
	 */
	public ArchivingConfiguration loadArchivingConfigurationIntoHash(
			String location) throws Exception {
		File file = new File(location);
		Node rootNode = XMLUtils.getRootNode(file);

		ArchivingConfiguration ret = loadArchivingConfigurationIntoHashFromRoot(rootNode);

		return ret;
	}

	/**
	 * @param rootNode
	 * @return 26 juil. 2005
	 * @throws Exception
	 */
	public ArchivingConfiguration loadArchivingConfigurationIntoHashFromRoot(
			Node rootNode) throws Exception {
		ArchivingConfiguration archConf = null;

		if (rootNode.hasChildNodes()) {
			NodeList attributesNodes = rootNode.getChildNodes();
			archConf = new ArchivingConfiguration();

			Hashtable ACProperties = XMLUtils.loadAttributes(rootNode);
			try {
				String creationDate_s = (String) ACProperties
						.get(ArchivingConfigurationData.CREATION_DATE_PROPERTY_XML_TAG);
				String lastUpdateDate_s = (String) ACProperties
						.get(ArchivingConfigurationData.LAST_UPDATE_DATE_PROPERTY_XML_TAG);
				String name_s = (String) ACProperties
						.get(ArchivingConfigurationData.NAME_PROPERTY_XML_TAG);
				String path_s = (String) ACProperties
						.get(ArchivingConfigurationData.PATH_PROPERTY_XML_TAG);
				String isModified_s = (String) ACProperties
						.get(ArchivingConfigurationData.IS_MODIFIED_PROPERTY_XML_TAG);
				String isHistoric_s = (String) ACProperties
						.get(ArchivingConfigurationData.IS_HISTORIC_PROPERTY_XML_TAG);

				Timestamp creationDate = Timestamp.valueOf(creationDate_s);
				Timestamp lastUpdateDate = Timestamp.valueOf(lastUpdateDate_s);
				boolean isModified = GUIUtilities.StringToBoolean(isModified_s);
				boolean isHistoric = GUIUtilities.StringToBoolean(isHistoric_s);

				archConf.setCreationDate(creationDate);
				archConf.setLastUpdateDate(lastUpdateDate);
				archConf.setName(name_s);
				archConf.setPath(path_s);
				archConf.setModified(isModified);
				archConf.setHistoric(isHistoric);
			} catch (Exception e) {
				// do nothing (not obligatory fields)
			}

			ArchivingConfigurationAttributes archConfAttr = new ArchivingConfigurationAttributes();
			archConfAttr.setArchivingConfiguration(archConf);

			for (int i = 0; i < attributesNodes.getLength(); i++)
			// as many loops as there are attributes in the AC
			{
				Node currentAttributeNode = attributesNodes.item(i);
				if (XMLUtils.isAFakeNode(currentAttributeNode)) {
					continue;
				}

				String currentAttributeType = currentAttributeNode
						.getNodeName().trim();// has to be "attribute"
				if (!currentAttributeType
						.equals(ArchivingConfigurationAttribute.XML_TAG)) {
					// System.out.println (
					// "currentAttributeType/"+currentAttributeType+"/" );
					throw new Exception();
				}

				Hashtable attributeProperties = XMLUtils
						.loadAttributes(currentAttributeNode);
				String attributeCompleteName = (String) attributeProperties
						.get(ArchivingConfigurationAttribute.COMPLETE_NAME_PROPERTY_XML_TAG);
				ArchivingConfigurationAttribute currentAttribute = new ArchivingConfigurationAttribute();
				currentAttribute.setCompleteName(attributeCompleteName);
				currentAttribute.setGroup(archConfAttr);

				currentAttribute = loadCurrentAttributeModes(currentAttribute,
						currentAttributeNode);
				archConfAttr.addAttribute(currentAttribute);
			}

			archConf.setAttributes(archConfAttr);
		}

		/*
		 * System.out.println (
		 * "----------loadArchivingConfigurationIntoHashFromRoot------------" );
		 * System.out.println ( archConf.toString () ); System.out.println (
		 * "----------loadArchivingConfigurationIntoHashFromRoot------------" );
		 */

		return archConf;
	}

	/**
	 * @param _currentAttribute
	 * @param _currentAttributeNode
	 * @return
	 * @throws Exception
	 */
	private ArchivingConfigurationAttribute loadCurrentAttributeModes(
			ArchivingConfigurationAttribute _currentAttribute,
			Node _currentAttributeNode) throws Exception {
		ArchivingConfigurationAttribute ret = _currentAttribute;

		if (_currentAttributeNode.hasChildNodes()) {
			NodeList attributeModesNodes = _currentAttributeNode
					.getChildNodes();
			ArchivingConfigurationAttributeProperties currentAttributeProperties = new ArchivingConfigurationAttributeProperties();
			currentAttributeProperties.setAttribute(ret);

			for (int i = 0; i < attributeModesNodes.getLength(); i++)
			// as many loops as there are modes types for this attributes ( at
			// most 2, HDBModes and TDBModes )
			{
				Node currentModesNode = attributeModesNodes.item(i);
				if (XMLUtils.isAFakeNode(currentModesNode)) {
					continue;
				}

				String currentModesType = currentModesNode.getNodeName().trim();// has
																				// to
																				// be
																				// "HDBModes"
																				// or
																				// "TDBModes"
				boolean currentModeIsHDB = currentModesType
						.equals(ArchivingConfigurationAttributeHDBProperties.XML_TAG);
				boolean currentModeIsTDB = currentModesType
						.equals(ArchivingConfigurationAttributeTDBProperties.XML_TAG);
				Hashtable DBmodeProperties = XMLUtils
						.loadAttributes(currentModesNode);

				if (currentModeIsHDB) {
					ArchivingConfigurationAttributeHDBProperties HDBProperties = new ArchivingConfigurationAttributeHDBProperties();

					String dedicatedArchiver_s = (String) DBmodeProperties
							.get(ArchivingConfigurationAttributeDBProperties.DEDICATED_ARCHIVER_PROPERTY_XML_TAG);
					if (dedicatedArchiver_s == null) {
						dedicatedArchiver_s = "";
					}
					HDBProperties.setDedicatedArchiver(dedicatedArchiver_s);

					HDBProperties = (ArchivingConfigurationAttributeHDBProperties) loadCurrentDBTypeModes(
							HDBProperties, currentModesNode);
					currentAttributeProperties.setHDBProperties(HDBProperties);
				} else if (currentModeIsTDB) {
					ArchivingConfigurationAttributeTDBProperties TDBProperties = new ArchivingConfigurationAttributeTDBProperties();

					/*
					 * String exportPeriod_s = ( String ) DBmodeProperties.get(
					 * ArchivingConfigurationAttributeTDBProperties
					 * .TDB_SPEC_EXPORT_PERIOD_XML_TAG ); long exportPeriod =
					 * exportPeriod_s==null ? 0 : Long.parseLong( exportPeriod_s
					 * ); TDBProperties.setExportPeriod( exportPeriod );
					 */

					TDBProperties
							.setExportPeriod(defineTdbExportPeriodForCurrentAttributeModes(DBmodeProperties));

					/*
					 * String keepingPeriod_s = ( String ) DBmodeProperties.get(
					 * ArchivingConfigurationAttributeTDBProperties
					 * .TDB_SPEC_KEEPING_PERIOD_XML_TAG ); long keepingPeriod =
					 * keepingPeriod_s==null ? 0 : Long.parseLong(
					 * keepingPeriod_s );
					 */
					// TDBProperties.setKeepingPeriod( keepingPeriod );
					String dedicatedArchiver_s = (String) DBmodeProperties
							.get(ArchivingConfigurationAttributeDBProperties.DEDICATED_ARCHIVER_PROPERTY_XML_TAG);
					if (dedicatedArchiver_s == null) {
						dedicatedArchiver_s = "";
					}
					TDBProperties.setDedicatedArchiver(dedicatedArchiver_s);

					TDBProperties = (ArchivingConfigurationAttributeTDBProperties) loadCurrentDBTypeModes(
							TDBProperties, currentModesNode);
					currentAttributeProperties.setTDBProperties(TDBProperties);
				} else {
					throw new Exception();
				}
			}

			ret.setProperties(currentAttributeProperties);
		}

		/*
		 * System.out.println (
		 * "----------loadCurrentAttributeModes------------" );
		 * System.out.println ( ret.toString () ); System.out.println (
		 * "----------loadCurrentAttributeModes------------" );
		 */

		return ret;
	}

	protected long defineTdbExportPeriodForCurrentAttributeModes(
			Hashtable DBmodeProperties) {
		String exportPeriod_s = (String) DBmodeProperties
				.get(ArchivingConfigurationAttributeTDBProperties.TDB_SPEC_EXPORT_PERIOD_XML_TAG);

		return (exportPeriod_s == null ? IACDefaultsManager.DEFAULT_TDB_EXPORT_PERIOD_WHEN_BAD_VALUE
				: Long.parseLong(exportPeriod_s));
	}

	/**
	 * @param properties
	 * @param currentModesNode
	 * @return 26 juil. 2005
	 * @throws Exception
	 */
	private ArchivingConfigurationAttributeDBProperties loadCurrentDBTypeModes(
			ArchivingConfigurationAttributeDBProperties properties,
			Node currentModesNode) throws Exception {
		ArchivingConfigurationAttributeDBProperties ret = properties;

		if (currentModesNode.hasChildNodes()) {
			NodeList modeNodes = currentModesNode.getChildNodes();

			for (int i = 0; i < modeNodes.getLength(); i++)
			// as many loops as there are modes types for the current mode type
			// for the current attribute
			{
				Node currentModeNode = modeNodes.item(i);
				if (XMLUtils.isAFakeNode(currentModeNode)) {
					continue;
				}

				String currentModeType = currentModeNode.getNodeName().trim();// has
																				// to
																				// be
																				// "modes"
				if (!currentModeType.equals(ArchivingConfigurationMode.XML_TAG)) {
					throw new Exception();
				}

				Hashtable modeProperties = XMLUtils
						.loadAttributes(currentModeNode);
				Mode mode = getMode(modeProperties);
				ArchivingConfigurationMode currentMode = new ArchivingConfigurationMode(
						mode);
				ret.addMode(currentMode);
			}
		}

		/*
		 * System.out.println ( "----------loadCurrentDBTypeModes------------"
		 * ); System.out.println ( ret.toString () ); System.out.println (
		 * "----------loadCurrentDBTypeModes------------" );
		 */

		return ret;
	}

	/**
	 * @param modeProperties
	 * @return 26 juil. 2005
	 */
	private Mode getMode(Hashtable modeProperties) {
		String type_s = (String) modeProperties
				.get(ArchivingConfigurationMode.TYPE_PROPERTY_XML_TAG);
		String period_s = (String) modeProperties
				.get(ArchivingConfigurationMode.PERIOD_PROPERTY_XML_TAG);
		int type = Integer.parseInt(type_s);
		int period = Integer.parseInt(period_s);

		Mode mode = new Mode();
		switch (type) {
		case ArchivingConfigurationMode.TYPE_A:
			String inf_s = (String) modeProperties
					.get(ArchivingConfigurationMode.VAL_INF_PROPERTY_XML_TAG);
			String sup_s = (String) modeProperties
					.get(ArchivingConfigurationMode.VAL_SUP_PROPERTY_XML_TAG);
			String is_ab_slow_drift = (String) modeProperties
					.get(ArchivingConfigurationMode.VAL_ABS_SLOW_DRIFT_XML_TAG);
			double inf = Double.parseDouble(inf_s);
			double sup = Double.parseDouble(sup_s);
			boolean ab_slow_drift = Boolean.parseBoolean(is_ab_slow_drift);

			ModeAbsolu ModeA = new ModeAbsolu(period, inf, sup, ab_slow_drift);
			mode.setModeA(ModeA);
			break;

		case ArchivingConfigurationMode.TYPE_C:
			String range_s = (String) modeProperties
					.get(ArchivingConfigurationMode.RANGE_PROPERTY_XML_TAG);
			String calculationType_s = (String) modeProperties
					.get(ArchivingConfigurationMode.CALC_TYPE_PROPERTY_XML_TAG);

			int range = Integer.parseInt(range_s);
			int calculationType = Integer.parseInt(calculationType_s);

			ModeCalcul ModeC = new ModeCalcul(period, range, calculationType);
			mode.setModeC(ModeC);
			break;

		case ArchivingConfigurationMode.TYPE_R:
			String inf_s1 = (String) modeProperties
					.get(ArchivingConfigurationMode.PERCENT_INF_PROPERTY_XML_TAG);
			String sup_s1 = (String) modeProperties
					.get(ArchivingConfigurationMode.PERCENT_SUP_PROPERTY_XML_TAG);
			String is_rel_slow_drift = (String) modeProperties
					.get(ArchivingConfigurationMode.VAL_REL_SLOW_DRIFT_XML_TAG);

			double inf1 = Double.parseDouble(inf_s1);
			double sup1 = Double.parseDouble(sup_s1);
			boolean rel_slow_drift = Boolean.parseBoolean(is_rel_slow_drift);

			ModeRelatif ModeR = new ModeRelatif(period, inf1, sup1,
					rel_slow_drift);
			mode.setModeR(ModeR);
			break;

		case ArchivingConfigurationMode.TYPE_T:
			String inf_s2 = (String) modeProperties
					.get(ArchivingConfigurationMode.THRESHOLD_INF_PROPERTY_XML_TAG);
			String sup_s2 = (String) modeProperties
					.get(ArchivingConfigurationMode.THRESHOLD_SUP_PROPERTY_XML_TAG);

			double inf2 = Double.parseDouble(inf_s2);
			double sup2 = Double.parseDouble(sup_s2);

			ModeSeuil ModeT = new ModeSeuil(period, inf2, sup2);
			mode.setModeT(ModeT);
			break;

		case ArchivingConfigurationMode.TYPE_D:

			ModeDifference ModeD = new ModeDifference(period);
			mode.setModeD(ModeD);
			break;

		case ArchivingConfigurationMode.TYPE_P:
			ModePeriode ModeP = new ModePeriode(period);
			mode.setModeP(ModeP);
			break;
		}

		return mode;
	}

}
