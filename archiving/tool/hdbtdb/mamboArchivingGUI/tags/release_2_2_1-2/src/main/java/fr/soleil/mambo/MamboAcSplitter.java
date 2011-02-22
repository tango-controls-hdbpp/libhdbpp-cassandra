package fr.soleil.mambo;

import fr.soleil.mambo.data.archiving.ArchivingConfiguration;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttribute;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeHDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributeTDBProperties;
import fr.soleil.mambo.data.archiving.ArchivingConfigurationAttributes;
import fr.soleil.mambo.datasources.file.ArchivingConfigurationManagerFactory;
import fr.soleil.mambo.datasources.file.IArchivingConfigurationManager;
import fr.soleil.mambo.tools.xmlhelpers.ac.ArchivingConfigurationXMLHelperFactory;

public class MamboAcSplitter {

	/**
	 * @param args
	 *            8 juil. 2005
	 */
	public static void main(String[] args) {
		// System.out.println( "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" );
		if (args == null || args.length != 1) {
			System.out
					.println("Incorrect arguments. Correct syntax: java MamboAcSplitter pathToMixedAC");
			System.exit(1);
		}
		String pathToMixedAC = args[0];
		System.out.println("pathToMixedAC/" + pathToMixedAC);

		try {
			// ArchivingConfiguration ac =
			// ArchivingConfigurationXMLHelper.loadArchivingConfigurationIntoHash(
			// pathToMixedAC );
			ArchivingConfigurationXMLHelperFactory
					.getImpl(ArchivingConfigurationXMLHelperFactory.STANDARD_IMPL_TYPE);
			ArchivingConfiguration ac = ArchivingConfigurationXMLHelperFactory
					.getCurrentImpl().loadArchivingConfigurationIntoHash(
							pathToMixedAC);
			String pathToAcHdb = getSplitPath(pathToMixedAC, true);
			String pathToAcTdb = getSplitPath(pathToMixedAC, false);
			System.out.println("pathToAcHdb|" + pathToAcHdb + "|");
			System.out.println("pathToAcTdb|" + pathToAcTdb + "|");

			/*
			 * System.out.println( "-------------------------------");
			 * System.out.println( ac.toString()); System.out.println(
			 * "-------------------------------");
			 */

			ArchivingConfiguration[] split = split(ac);
			ArchivingConfiguration ac_hdb = split[0];
			ac_hdb.setHistoric(true);
			ArchivingConfiguration ac_tdb = split[1];
			ac_tdb.setHistoric(false);

			IArchivingConfigurationManager manager = ArchivingConfigurationManagerFactory
					.getImpl(ArchivingConfigurationManagerFactory.XML_IMPL_TYPE);

			manager.setNonDefaultSaveLocation(pathToAcHdb);
			manager.saveArchivingConfiguration(ac_hdb, null);

			manager.setNonDefaultSaveLocation(pathToAcTdb);
			manager.saveArchivingConfiguration(ac_tdb, null);

		} catch (Throwable t) {
			System.out.println("Throwable!!!/" + t.getMessage());
			t.printStackTrace();
			// StackTraceElement [] el = t.getStackTrace();
			// System.out.println(
			// "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ZZZZZZZZZZ2" );
		}

	}

	/**
	 * @param ac
	 * @return
	 */
	private static ArchivingConfiguration[] split(ArchivingConfiguration ac) {
		ArchivingConfiguration[] ret = new ArchivingConfiguration[2];
		ArchivingConfiguration hdbAc = new ArchivingConfiguration();
		ArchivingConfiguration tdbAc = new ArchivingConfiguration();
		ArchivingConfigurationAttributes hdbAcAttrs = new ArchivingConfigurationAttributes();
		ArchivingConfigurationAttributes tdbAcAttrs = new ArchivingConfigurationAttributes();

		hdbAc.setHistoric(true);
		hdbAc.setData(ac.getData());
		hdbAc.setHistoric(ac.isHistoric());
		hdbAc.setModified(ac.isModified());

		tdbAc.setHistoric(false);
		tdbAc.setData(ac.getData());
		tdbAc.setHistoric(ac.isHistoric());
		tdbAc.setModified(ac.isModified());

		ArchivingConfigurationAttribute[] attrs = ac.getAttributes()
				.getAttributesList();
		int numberOfAttrs = attrs.length;
		for (int i = 0; i < numberOfAttrs; i++) {
			ArchivingConfigurationAttribute currentAttr = attrs[i];
			ArchivingConfigurationAttributeHDBProperties HDBProperties = currentAttr
					.getProperties().getHDBProperties();
			ArchivingConfigurationAttributeTDBProperties TDBProperties = currentAttr
					.getProperties().getTDBProperties();

			if (!HDBProperties.isEmpty()) {
				ArchivingConfigurationAttribute hdbAttr = new ArchivingConfigurationAttribute();
				ArchivingConfigurationAttributeProperties propOfHdbAttr = new ArchivingConfigurationAttributeProperties();
				hdbAttr.setCompleteName(currentAttr.getCompleteName());
				propOfHdbAttr.setHDBProperties(HDBProperties);
				hdbAttr.setProperties(propOfHdbAttr);

				hdbAcAttrs.addAttribute(hdbAttr);
			}

			if (!TDBProperties.isEmpty()) {
				ArchivingConfigurationAttribute tdbAttr = new ArchivingConfigurationAttribute();
				ArchivingConfigurationAttributeProperties propOfTdbAttr = new ArchivingConfigurationAttributeProperties();
				tdbAttr.setCompleteName(currentAttr.getCompleteName());
				propOfTdbAttr.setTDBProperties(TDBProperties);
				tdbAttr.setProperties(propOfTdbAttr);

				tdbAcAttrs.addAttribute(tdbAttr);
			}
		}

		hdbAc.setAttributes(hdbAcAttrs);
		tdbAc.setAttributes(tdbAcAttrs);

		ret[0] = hdbAc;
		ret[1] = tdbAc;
		return ret;
	}

	/**
	 * @param pathToMixedAC
	 * @param b
	 * @return
	 */
	private static String getSplitPath(String pathToMixedAC, boolean isHistoric) {
		int posOfFileExtension = pathToMixedAC.lastIndexOf(".");
		String beforeFileExtension = pathToMixedAC.substring(0,
				posOfFileExtension);
		// System.out.println( "beforeFileExtension/"+beforeFileExtension+"/" );
		String afterFileExtension = pathToMixedAC.substring(posOfFileExtension,
				pathToMixedAC.length());
		// System.out.println( "afterFileExtension/"+afterFileExtension+"/" );
		String suffix = isHistoric ? "_HDB" : "_TDB";
		return beforeFileExtension + suffix + afterFileExtension;
	}

}
