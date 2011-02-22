//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/snapshot/SelectAllOrNoneAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  RemoveSelectedACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.3 $
//
//$Log: SelectAllOrNoneAction.java,v $
//Revision 1.3  2006/04/10 08:47:14  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.2  2006/02/15 09:13:14  ounsy
//minor changes
//
//Revision 1.1  2005/12/14 16:51:42  ounsy
//added a selectAllOrNone method
//
//Revision 1.1.2.2  2005/09/14 15:41:20  chinkumo
//Second commit !
//
//
//copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.bensikin.actions.snapshot;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.snapshot.detail.SnapshotDetailTable;



public class SelectAllOrNoneAction extends BensikinAction 
{
	private int type;
	
	private SnapshotDetailTable table;
	
	public static final int SELECT_REVERSE_TYPE = 0;
	public static final int SELECT_ALL_TYPE = 1;
	public static final int SELECT_NONE_TYPE = 2;

	 /**
	  * @param name
	  */
	 public SelectAllOrNoneAction ( String name , int _type , SnapshotDetailTable _table )
	 {
	     super ( name );
	     
	     if ( _type != SELECT_REVERSE_TYPE && _type != SELECT_ALL_TYPE && _type != SELECT_NONE_TYPE )
	     {
	         throw new IllegalArgumentException ( "Expected either of " + SELECT_REVERSE_TYPE + "," + SELECT_ALL_TYPE + "," + SELECT_NONE_TYPE + " as a parameter. Received " + type + " instead." );
	     }
	     
	     this.putValue( Action.NAME , name );
	     this.type = _type;
	     this.table = _table;
	 }
	 
	 /* (non-Javadoc)
	  * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	  */
	 public void actionPerformed ( ActionEvent event ) 
	 {
	     this.table.applyChange (); 
	    switch ( this.type )
	    {
	  		case SELECT_ALL_TYPE :
	  		    table.selectAllOrNone ( true );
	  		break;
	  		
	  		case SELECT_NONE_TYPE :
	  		    table.selectAllOrNone ( false );
	  		break;    		
	    }
	 }

}
