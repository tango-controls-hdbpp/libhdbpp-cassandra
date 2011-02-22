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

import java.sql.Timestamp;

import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevVarLongStringArray;
import fr.soleil.snapArchivingApi.SnapExtractorApi.tools.Tools;
import fr.soleil.snapArchivingApi.SnapManagerApi.ISnapManager;
import fr.soleil.snapArchivingApi.SnapManagerApi.SnapManagerImpl;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Condition;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.DateUtil;
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

    /* (non-Javadoc)
     * @see snapextractor.api.datasources.db.ISnapReader#getSnapshotID(int)
     */
    public int[]  getSnapshotsID(int ctxID, String[] criterions) 
    {
		// ---Add your Own code to control device here ---
    	int[] argout = null;
    	
		Criterions ret = getInputCriterion(criterions);
		if(ret == null)
			return null;
        ret.addCondition( new Condition( GlobalConst.TAB_SNAP[ 1 ] , GlobalConst.OP_EQUALS , "" + ctxID ) );

       
        SnapShotLight[] newList;
		try {
			newList = manager.findSnapshots( ret );
	        if(ret.getConditions("first")!=null){
	        	argout = new int[1];
	        	argout[0] = newList[0].getId_snap();
	        }else if(ret.getConditions("last")!=null){
	        	argout = new int[1];
	        	argout[0] = newList[newList.length-1].getId_snap();
	        }else {
	        	argout = new int[newList.length];
	        	for (int i = 0; i < newList.length; i++) {
	        		argout[i] = newList[i].getId_snap();
	        	}
	        }
		} catch (SnapshotingException e) {
			// TODO Auto-generated catch block
			return null;
		}


		//------------------------------------------------
		
		return argout;
	}
	private Criterions getInputCriterion(String[] criterions) {
		// TODO Auto-generated method stub
		Criterions ret = new Criterions();
		try{
			Condition cond;
			String criterion_type = null;
			String criterion_op = null;
			String criterion_value = null;
			for(int i=0; i<criterions.length;i++){
				if(criterions[i].equals("first") || criterions[i].equals("last")){
					cond = getCondition(criterions[i] , criterions[i] , criterions[i]);
					ret.addCondition(cond);

				} else if(criterions[i].equals("id_snap") || criterions[i].equals("comment") ||  criterions[i].equals("time")){
					criterion_type = criterions[i].substring(0, criterions[i].indexOf(" "));
					criterions[i] = criterions[i].substring(criterions[i].indexOf(criterion_type)+ criterion_type.length()+1);
					criterion_op = criterions[i].substring(0, criterions[i].indexOf(" "));
					criterion_value = criterions[i].substring(criterions[i].indexOf(criterion_op)+ criterion_op.length()).trim();
				
					cond = getCondition(criterion_op , criterion_value , criterion_type);
					ret.addCondition(cond);
				
			}
				else return null;
		}
		}catch(Exception e){
			
			return null;
		}
		

		return ret;
	}
	//------------------------------------------------
	/**
	 * If both operator and threshold value are filled, builds a Condition from them. Otherwise returns null.
	 *
	 * @param selectedItem  The operator
	 * @param text          The value
	 * @param id_field_key2 The field's id
	 * @return The resulting Condition or null
	 */
	public Condition getCondition(String selectedItem , String text , String id_field_key2)
	{
		// Date Formating
		if(id_field_key2.equals("time")){
			text = Tools.formatDate(Tools.stringToMilli(text));
		}else if(id_field_key2.contains("comment")){
			id_field_key2 = "snap_comment";
			selectedItem = getCommentCondition(selectedItem);
		}
		
		
		boolean isACriterion = true;
		
		if ( selectedItem == null || selectedItem.equals("") )
		{
			isACriterion = false;
		}

		if ( text == null || text.trim().equals("") )
		{
			isACriterion = false;
		}

		if ( isACriterion || selectedItem.equals("first") || selectedItem.equals("last"))
		{
			return new Condition(id_field_key2 , selectedItem , text.trim());
		}

		return null;
	}

	private String getCommentCondition(String selectedItem) {
		// TODO Auto-generated method stub
		if(selectedItem.equalsIgnoreCase("starts"))
			return "Starts with";
		else if(selectedItem.equalsIgnoreCase("ends"))
			return "Ends with";
		else return "Contains";
	}
	    
 
}
