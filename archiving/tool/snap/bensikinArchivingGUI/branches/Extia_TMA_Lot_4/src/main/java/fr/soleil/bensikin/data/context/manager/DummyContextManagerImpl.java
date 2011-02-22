//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/context/manager/DummyContextManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummyContextManagerImpl.
//						(Claisse Laurent) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: DummyContextManagerImpl.java,v $
// Revision 1.2  2005/12/14 16:34:41  ounsy
// added methods necessary for the new Word-like file management
//
// Revision 1.1  2005/12/14 14:07:18  ounsy
// first commit  including the new  "tools,xml,lifecycle,profile" sub-directories
// under "bensikin.bensikin" and removing the same from their former locations
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.data.context.manager;

import java.util.Hashtable;

import fr.soleil.bensikin.data.context.Context;

/**
 * A dummy implementation (does nothing)
 * 
 * @author CLAISSE
 */
public class DummyContextManagerImpl implements IContextManager {

	/**
     * 
     */
	public DummyContextManagerImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.data.context.manager.IContextManager#startUp()
	 */
	public void startUp() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.data.context.manager.IContextManager#shutDown()
	 */
	public void shutDown() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.context.manager.IContextManager#saveContext(bensikin
	 * .bensikin.data.context.Context, java.util.Hashtable)
	 */
	public void saveContext(Context ac, Hashtable saveParameters)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see bensikin.bensikin.data.context.manager.IContextManager#loadContext()
	 */
	public Context loadContext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.context.manager.IContextManager#getDefaultSaveLocation
	 * ()
	 */
	public String getDefaultSaveLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.context.manager.IContextManager#getSaveLocation()
	 */
	public String getSaveLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * bensikin.bensikin.data.context.manager.IContextManager#setSaveLocation
	 * (java.lang.String)
	 */
	public void setSaveLocation(String location) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebensikin.bensikin.data.context.manager.IContextManager#
	 * getNonDefaultSaveLocation()
	 */
	public String getNonDefaultSaveLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seebensikin.bensikin.data.context.manager.IContextManager#
	 * setNonDefaultSaveLocation(java.lang.String)
	 */
	public void setNonDefaultSaveLocation(String location) {
		// TODO Auto-generated method stub

	}

}
