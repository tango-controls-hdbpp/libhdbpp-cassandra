//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/datasources/db/attributes/AttributeManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  AttrDBManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.3 $
//
// $Log: AttributeManagerFactory.java,v $
// Revision 1.3  2007/09/13 09:56:54  ounsy
// DBA :
// Modification in LifecycleManagerFactory to manage multiple LifeCycleManager.
// In ArchivingManagerFactory, AttributeManagerFactory, ExtractingManagerFactory add setCurrentImpl methods.
//
// Revision 1.2  2006/09/27 08:28:52  ounsy
// minor changes
//
// Revision 1.1  2006/09/22 09:32:19  ounsy
// moved from mambo.datasources.db
//
// Revision 1.3  2006/05/16 12:04:50  ounsy
// added new buffered implementations
//
// Revision 1.2  2005/11/29 18:27:24  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.mambo.datasources.db.attributes;

public class AttributeManagerFactory {
	public static final int DUMMY = 1;
	public static final int BASIC = 2;
	public static final int BUFFERED_FORMATS = 3;
	public static final int BUFFERED_FORMATS_AND_DOMAINS = 4;

	private static IAttributeManager currentImpl = null;

	/**
	 * @param typeOfImpl
	 * @return 8 juil. 2005
	 */
	public static IAttributeManager getImpl(int typeOfImpl) {
		switch (typeOfImpl) {
		case DUMMY:
			currentImpl = new DummyAttributeManager();
			break;

		case BASIC:
			currentImpl = new BasicAttributeManager();
			break;

		case BUFFERED_FORMATS:
			currentImpl = new BufferedFormatsAttributeManager();
			break;

		case BUFFERED_FORMATS_AND_DOMAINS:
			currentImpl = new BufferedFormatsAndDomainsAttributeManager();
			break;

		default:
			throw new IllegalStateException(
					"Expected either DUMMY_IMPL_TYPE (1) or REAL_IMPL_TYPE (2), got "
							+ typeOfImpl + " instead.");
		}

		return currentImpl;
	}

	/**
	 * @return 28 juin 2005
	 */
	public static IAttributeManager getCurrentImpl() {
		return currentImpl;
	}

	/**
	 * Change the currentImpl
	 * 
	 * @param currentImpl
	 */
	public static void setCurrentImpl(IAttributeManager currentImpl) {
		AttributeManagerFactory.currentImpl = currentImpl;
	}

}
