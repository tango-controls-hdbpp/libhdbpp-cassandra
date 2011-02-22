//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/roles/RightsManagerFactory.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  LifeCycleManagerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: RightsManagerFactory.java,v $
//Revision 1.2  2006/05/03 13:04:14  ounsy
//modified the limited operator rights
//
//Revision 1.1  2006/04/10 08:45:47  ounsy
//creation
//
//Revision 1.3  2005/11/29 18:25:13  chinkumo
//no message
//
//Revision 1.1.1.2  2005/08/22 11:58:41  chinkumo
//First commit
//
//
//copyleft :		Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.roles;

/**
* A factory used to instantiate LifeCycleManager of 1 type: DEFAULT_LIFE_CYCLE
*
* @author CLAISSE
*/
public class RightsManagerFactory
{
	/**
	 * Code for the read only implementation
	 */
	public static final int READ_ONLY_OPERATOR = 0;
    /**
     * Code for the snapshots only implementation
     */
    public static final int SNAPSHOTS_ONLY_OPERATOR = 1;
	
    private static IRightsManager currentImpl = null;

	/**
	 * Instantiates and returns an implementation the <code>LifeCycleManager<code> described by <code>typeOfImpl<code>
	 *
	 * @param typeOfImpl The type of implementation of LifeCycleManager
	 * @return An implementation of LifeCycleManager
	 * @throws IllegalArgumentException If <code>typeOfImpl<code> isn't in (DEFAULT_LIFE_CYCLE) (Only one implementation so far)
	 */
	public static IRightsManager getImpl(int typeOfImpl)
	{
		switch ( typeOfImpl )
		{
			case READ_ONLY_OPERATOR:
				currentImpl = new ReadOnlyRightsManager();
			break;
            case SNAPSHOTS_ONLY_OPERATOR:
                currentImpl = new SnapshotsOnlyRightsManager();
            break;
			default:
				throw new IllegalStateException("Expected READ_ONLY_OPERATOR (0), got " + typeOfImpl + " instead.");
		}

		return currentImpl;
	}

	/**
	 * Returns the current implementation as precedently instantiated.
	 *
	 * @return The current implementation as precedently instantiated
	 *         28 juin 2005
	 */
	public static IRightsManager getCurrentImpl()
	{
		return currentImpl;
	}

}
