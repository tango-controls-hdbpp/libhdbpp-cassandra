//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/data/snapshot/manager/DummySnapshotManagerImpl.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  DummySnapshotManagerImpl.
//						(Claisse Laurent) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: DummySnapshotManagerImpl.java,v $
// Revision 1.2  2005/12/14 16:38:10  ounsy
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
package fr.soleil.bensikin.data.snapshot.manager;

import java.util.Hashtable;

import fr.soleil.bensikin.data.snapshot.Snapshot;


/**
 * Dummy implementation. Does nothing.
 *
 * @author CLAISSE
 */
public class DummySnapshotManagerImpl implements ISnapshotManager
{

    /**
     * 
     */
    public DummySnapshotManagerImpl ()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#startUp()
     */
    public void startUp () throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#saveSnapshot(bensikin.bensikin.data.snapshot.Snapshot, java.util.Hashtable)
     */
    public void saveSnapshot ( Snapshot ac , Hashtable saveParameters )
            throws Exception
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#getDefaultSaveLocation()
     */
    public String getDefaultSaveLocation ()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#getSaveLocation()
     */
    public String getSaveLocation ()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#setSaveLocation(java.lang.String)
     */
    public void setSaveLocation ( String location )
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#loadSnapshot()
     */
    public Snapshot loadSnapshot () throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#getFileName()
     */
    public String getFileName ()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#getNonDefaultSaveLocation()
     */
    public String getNonDefaultSaveLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see bensikin.bensikin.data.snapshot.manager.ISnapshotManager#setNonDefaultSaveLocation(java.lang.String)
     */
    public void setNonDefaultSaveLocation(String location) {
        // TODO Auto-generated method stub
        
    }

}
