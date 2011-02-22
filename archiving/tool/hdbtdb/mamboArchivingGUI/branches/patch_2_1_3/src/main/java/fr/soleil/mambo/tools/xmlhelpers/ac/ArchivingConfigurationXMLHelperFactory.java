package fr.soleil.mambo.tools.xmlhelpers.ac;

public class ArchivingConfigurationXMLHelperFactory {
	public static final int STANDARD_IMPL_TYPE = 1;
	public static final int FORCED_EXPORT_PERIOD_IMPL_TYPE = 2;

	private static IArchivingConfigurationXMLHelper currentImpl = null;

	/**
	 * @param typeOfImpl
	 * @return 8 juil. 2005
	 */
	public static IArchivingConfigurationXMLHelper getImpl(int typeOfImpl)
			throws IllegalStateException {
		switch (typeOfImpl) {
		case STANDARD_IMPL_TYPE:
			currentImpl = new ArchivingConfigurationXMLHelper();
			break;

		case FORCED_EXPORT_PERIOD_IMPL_TYPE:
			currentImpl = new ArchivingConfigurationXMLHelperWithForceExportPeriod();
			break;

		default:
			throw new IllegalStateException(
					"Expected either STANDARD_IMPL_TYPE (1) or FORCED_EXPORT_PERIOD_IMPL_TYPE (2), got "
							+ typeOfImpl + " instead.");
		}

		return currentImpl;
	}

	/**
	 * @return 28 juin 2005
	 */
	public static IArchivingConfigurationXMLHelper getCurrentImpl() {
		return currentImpl;
	}
}
