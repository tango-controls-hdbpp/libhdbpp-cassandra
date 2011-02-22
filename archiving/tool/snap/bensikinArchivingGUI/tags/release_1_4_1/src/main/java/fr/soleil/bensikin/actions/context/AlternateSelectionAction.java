//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/actions/context/AlternateSelectionAction.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  RemoveSelectedACAttributesAction.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: ounsy $
//
//$Revision: 1.2 $
//
//$Log: AlternateSelectionAction.java,v $
//Revision 1.2  2006/04/10 08:46:54  ounsy
//Bensikin action now all inherit from BensikinAction for easy rights management
//
//Revision 1.1  2005/12/14 16:50:33  ounsy
//added methods necessary for alternate attribute selection
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
package fr.soleil.bensikin.actions.context;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import fr.soleil.bensikin.actions.BensikinAction;
import fr.soleil.bensikin.components.context.detail.AttributesSelectTable;



public class AlternateSelectionAction extends BensikinAction 
{
  private int type;
  
  public static final int SELECT_REVERSE_TYPE = 0;
  public static final int SELECT_ALL_TYPE = 1;
  public static final int SELECT_NONE_TYPE = 2;
  
	 /**
	  * @param name
	  */
	 public AlternateSelectionAction ( String name , int _type )
	 {
	     super ( name );
	     
	     if ( _type != SELECT_REVERSE_TYPE && _type != SELECT_ALL_TYPE && _type != SELECT_NONE_TYPE )
	     {
	         throw new IllegalArgumentException ( "Expected either of " + SELECT_REVERSE_TYPE + "," + SELECT_ALL_TYPE + "," + SELECT_NONE_TYPE + " as a parameter. Received " + type + " instead." );
	     }
	     
	     this.putValue( Action.NAME , name );
	     this.type = _type;
	 }
	 
	 /* (non-Javadoc)
	  * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	  */
	 public void actionPerformed ( ActionEvent event ) 
	 {
	     //System.out.println ( "ReverseAlternateSelectionAction" );
	     AttributesSelectTable table = AttributesSelectTable.getInstance ();
	     table.applyChange (); 
	    switch ( this.type )
	    {
	  		case SELECT_REVERSE_TYPE :
	  		    table.reverseSelection ();
	  		break;
	  		
	  		case SELECT_ALL_TYPE :
	  		    table.selectAllOrNone ( true );
	  		break;
	  		
	  		case SELECT_NONE_TYPE :
	  		    table.selectAllOrNone ( false );
	  		break;    		
	    }
	 }

}
