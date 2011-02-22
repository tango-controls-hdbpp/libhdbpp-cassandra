package fr.soleil.mambo.datasources.db.extracting;

public class ExtractingManagerFactory {
	public static final int DUMMY = 1;
	public static final int BASIC = 2;

	private static IExtractingManager currentImpl = null;

	/**
	 * @param typeOfImpl
	 * @return 8 juil. 2005
	 */
	public static IExtractingManager getImpl(int typeOfImpl) {
		switch (typeOfImpl) {
		case DUMMY:
			currentImpl = new DummyExtractingManager();
			break;

		case BASIC:
			currentImpl = new BasicExtractingManager();
			break;

		default:
			throw new IllegalStateException(
					"Expected either DUMMY (1) or BASIC (2), got " + typeOfImpl
							+ " instead.");
		}

		return currentImpl;
	}

	/**
	 * @return 28 juin 2005
	 */
	public static IExtractingManager getCurrentImpl() {
		return currentImpl;
	}

	/**
	 * Change the currentImpl
	 * 
	 * @param currentImpl
	 */
	public static void setCurrentImpl(IExtractingManager currentImpl) {
		ExtractingManagerFactory.currentImpl = currentImpl;
	}

}
