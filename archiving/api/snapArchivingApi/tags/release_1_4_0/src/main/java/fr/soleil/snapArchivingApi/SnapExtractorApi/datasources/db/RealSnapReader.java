//+======================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoSnapshoting/SnapExtractorApi/datasources/db/RealSnapReader.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class RealSnapReader.
//					(CLAISSE - 23/01/2006)
//
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: RealSnapReader.java,v $
// Revision 1.5  2008/01/08 15:32:19  chinkumo
// minor changes
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.snapArchivingApi.SnapExtractorApi.datasources.db;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevVarLongStringArray;
import fr.soleil.snapArchivingApi.SnapExtractorApi.tools.Tools;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapManagerApi.SnapManagerImpl;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;


/**
 * An implementation that loads data from the Snap Database
 * @author CLAISSE 
 */
public class RealSnapReader implements ISnapReader 
{
    private ISnapManager manager;
    private boolean isReady = false;

    RealSnapReader() 
    {
        super();
    }

    public void openConnection (String snapUser,String snapPassword) throws DevFailed
    {
        try
        {
	        if ( !this.isReady )
	        {
	            this.manager = new SnapManagerImpl ( snapUser,snapPassword );
	            this.isReady = true;
	        }
        }
	    catch ( Exception e )
	    {
	        Tools.throwDevFailed ( e );
	    }
    }
    
    public void closeConnection ()
    {
        this.isReady = false;
    }
    
    /* (non-Javadoc)
     * @see snapextractor.api.datasources.db.ISnapReader#getSnap(java.lang.String[])
     */
    public SnapAttributeExtract[] getSnap ( int id ) throws DevFailed  
    {
        /*Condition condition = new Condition( GlobalConst.TAB_SNAP[ 0 ] , "=" , String.valueOf( id ) );
        Criterions searchCriterions = new Criterions();
        searchCriterions.addCondition( condition );
     
        try
        {
            SnapShotLight[] snapshots = this.manager.findSnapshots( searchCriterions );
            if ( snapshots == null || snapshots.length == 0 )
            {
                return null;
            }
            SnapShotLight snapshotLight = snapshots[ 0 ];
         
            SnapAttributeExtract[] sae = this.manager.findSnapshotAttributes( snapshotLight );
            if ( sae == null || sae.length == 0 )
            {
                return null;
            }
            return sae;
        }
	    catch ( Exception e )
	    {
	        Tools.throwDevFailed ( e );
	    }
        return null;*/
    	SnapAttributeExtract[] sae = null;
    	try {
    		sae = this.manager.getSnap(id);
    	}
    	catch(SnapshotingException e)
    	{
    		throw e.toTangoException();
    	}
	    catch ( Exception e )
	    {
	        Tools.throwDevFailed ( e );
	    }
	    return sae;
    }



    /* (non-Javadoc)
     * @see snapextractor.api.datasources.db.ISnapReader#getSnapshotsForContext(int)
     */
    public DevVarLongStringArray getSnapshotsForContext(int contextId) throws DevFailed 
    {
        Criterions searchCriterions = new Criterions ();
        searchCriterions.addCondition( new Condition( GlobalConst.TAB_SNAP[ 1 ] , GlobalConst.OP_EQUALS , "" + contextId ) );

        SnapShotLight [] snapshots = null;
        try
        {
            snapshots = this.manager.findSnapshots( searchCriterions );
        }
	    catch ( Exception e )
	    {
	        Tools.throwDevFailed ( e );
	    }
	    if ( snapshots == null || snapshots.length == 0 )
	    {
	        return null;
	    }
	    int numberOfSnapshots = snapshots.length;
	    
	    DevVarLongStringArray ret = new DevVarLongStringArray ();
	    int [] lvalue = new int [ numberOfSnapshots ];
	    java.lang.String [] svalue = new java.lang.String [ numberOfSnapshots ]; 
	    for ( int i = 0; i < numberOfSnapshots ; i ++ )
	    {
	        SnapShotLight currentSnapshot = snapshots [ i ];
	        lvalue [ i ] = currentSnapshot.getId_snap ();
	        svalue [ i ] = currentSnapshot.getSnap_date () + " , " + currentSnapshot.getComment ();
	    }
	    ret.lvalue = lvalue;
	    ret.svalue = svalue;
	    
        return ret;
    }

    
}
