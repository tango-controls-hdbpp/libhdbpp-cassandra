//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/mambo/lifecycle/LifeCycleManagerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LifeCycleManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: LifeCycleManagerFactory.java,v $
// Revision 1.4  2007/10/30 16:12:38  ounsy
// change the name of the file which define the life cycle to use : mambo_conf.properties instead of mambo.properties.
//
// Revision 1.3  2007/09/13 09:56:54  ounsy
// DBA :
// Modification in LifecycleManagerFactory to manage multiple LifeCycleManager.
// In ArchivingManagerFactory, AttributeManagerFactory, ExtractingManagerFactory add setCurrentImpl methods.
//
// Revision 1.2  2005/11/29 18:28:26  chinkumo
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
package fr.soleil.mambo.lifecycle;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class LifeCycleManagerFactory {
	public static final int DEFAULT_LIFE_CYCLE = 0;
	public static final String MAMBO_PROPERTIES = "mambo_conf.properties";
	public static final String LIFE_CYCLE_MANAGER_PROPERTY = "LIFE_CYCLE_MANAGER";

	private static LifeCycleManager currentImpl = null;

	/**
	 * Create and return an implementation of the LifeCycleManager
	 * 
	 * @param typeOfImpl
	 *            : it's the default implementation if no properties file will
	 *            be found
	 * @return 8 juil. 2005
	 */
	public static LifeCycleManager getImpl(int typeOfImpl) {
		// we load the property file
		Properties properties = getPropertiesFile();

		if (properties != null) {
			// we get the name of the Life Cycle Manager used by Mambo
			String lifeCycleName = properties
					.getProperty(LIFE_CYCLE_MANAGER_PROPERTY);
			// we create an instance of it, it's the current implementation
			if (lifeCycleName != null)
				currentImpl = getLifeCycleManager(lifeCycleName);
			// if the currentImpl is not equal to null, we sucessfully create a
			// new instance
			if (currentImpl != null)
				return currentImpl;
		}

		switch (typeOfImpl) {
		case DEFAULT_LIFE_CYCLE:
			currentImpl = new DefaultLifeCycleManager();
			break;

		default:
			throw new IllegalStateException(
					"Expected DEFAULT_LIFE_CYCLE (0), got " + typeOfImpl
							+ " instead.");
		}

		return currentImpl;
	}

	/**
	 * We get the properties file which contains default properties
	 * 
	 * @return Properties
	 */
	private static Properties getPropertiesFile() {
		InputStream stream = null;
		BufferedInputStream bufStream = null;
		try {

			// We use the class loader to load the properties file.
			// This compatible with unix and windows.
			stream = LifeCycleManagerFactory.class.getClassLoader()
					.getResourceAsStream(MAMBO_PROPERTIES);
			Properties properties = new Properties();

			// We read the data in the properties file.
			if (stream != null) {
				// We need to use a Buffered Input Stream to load the datas
				bufStream = new BufferedInputStream(stream);
				properties.clear();
				properties.load(bufStream);
			}
			return properties;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * We instanciate the LifeCycleManager
	 * 
	 * @param lifeCycleName
	 * @return LifeCycleManager
	 */
	private static LifeCycleManager getLifeCycleManager(String lifeCycleName) {

		try {
			// we get the class coresponding to the life cycle name
			Class clazz = Class.forName(lifeCycleName);

			// we get the default constructor (with no parameter)
			Constructor contructor = clazz.getConstructor(new Class[] {});

			// we create an instance of the class using the constructor
			Object object = contructor.newInstance(new Object[] {});

			if (object instanceof LifeCycleManager)
				return (LifeCycleManager) object;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return 28 juin 2005
	 */
	public static LifeCycleManager getCurrentImpl() {
		return currentImpl;
	}

	/**
	 * Set the current Impl for LifeCycle
	 * 
	 * @param currentImpl
	 */
	public static void setCurrentImpl(LifeCycleManager currentImpl) {
		LifeCycleManagerFactory.currentImpl = currentImpl;
	}

}
