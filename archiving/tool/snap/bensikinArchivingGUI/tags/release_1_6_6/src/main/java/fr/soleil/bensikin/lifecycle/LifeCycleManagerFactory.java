//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/lifecycle/LifeCycleManagerFactory.java,v $
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
// Revision 1.4  2007/03/14 15:49:11  ounsy
// the user/password is no longer hard-coded in the APIs, Bensikin takes two more parameters
//
// Revision 1.3  2005/11/29 18:25:13  chinkumo
// no message
//
// Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
// First commit
//
//
// copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.lifecycle;

/**
 * A factory used to instantiate LifeCycleManager of 1 type: DEFAULT_LIFE_CYCLE
 * 
 * @author CLAISSE
 */
public class LifeCycleManagerFactory {
	/**
	 * Code for the default (and so far only) implementation
	 */
	public static final int DEFAULT_LIFE_CYCLE = 0;
	private static LifeCycleManager currentImpl = null;

	private static String snapUser;
	private static String snapPassword;

	/**
	 * Instantiates and returns an implementation the
	 * <code>LifeCycleManager<code> described by <code>typeOfImpl<code>
	 * 
	 * @param typeOfImpl
	 *            The type of implementation of LifeCycleManager
	 * @return An implementation of LifeCycleManager
	 * @throws IllegalArgumentException
	 *             If <code>typeOfImpl<code> isn't in (DEFAULT_LIFE_CYCLE) (Only
	 *             one implementation so far)
	 */
	public static LifeCycleManager getImpl(int typeOfImpl) {
		switch (typeOfImpl) {
		case DEFAULT_LIFE_CYCLE:
			currentImpl = new DefaultLifeCycleManager(snapUser, snapPassword);
			break;

		default:
			throw new IllegalStateException(
					"Expected DEFAULT_LIFE_CYCLE (0), got " + typeOfImpl
							+ " instead.");
		}

		return currentImpl;
	}

	/**
	 * Returns the current implementation as precedently instantiated.
	 * 
	 * @return The current implementation as precedently instantiated 28 juin
	 *         2005
	 */
	public static LifeCycleManager getCurrentImpl() {
		return currentImpl;
	}

	public static void setUser(String _snapUser) {
		snapUser = _snapUser;
	}

	public static void setPassword(String _snapPassword) {
		snapPassword = _snapPassword;
	}
}
