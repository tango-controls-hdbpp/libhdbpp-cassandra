//+======================================================================
//$Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/components/context/ContextFileFilter.java,v $
//
//Project:      Tango Archiving Service
//
//Description:  Java source code for the class  ACFileFilter.
//						(Claisse Laurent) - 5 juil. 2005
//
//$Author: chinkumo $
//
//$Revision: 1.1 $
//
//$Log: ContextFileFilter.java,v $
//Revision 1.1  2005/11/29 18:25:27  chinkumo
//no message
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
package fr.soleil.bensikin.components.context;

import fr.soleil.bensikin.components.DataFileFilter;
import fr.soleil.bensikin.tools.Messages;

/**
 * A file filter used for save/load of the context files
 *
 * @author CLAISSE
 */
public class ContextFileFilter extends DataFileFilter
{
	/**
	 * The contexts files extension "ctx"
	 */
	public static final String FILE_EXTENSION = "ctx";

	/**
	 * Builds with FILE_EXTENSION extension and a description found in resources.
	 */
	public ContextFileFilter()
	{
		super(FILE_EXTENSION);
		description = Messages.getMessage("FILE_CHOOSER_CONTEXT");
	}

}
