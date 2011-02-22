//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/bensikin/bensikin/tools/BensikinWarning.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  BensikinWarning.
//						(Claisse Laurent) - sept. 2005
//
// $Author: ounsy $
//
// $Revision: 1.1 $
//
// $Log: BensikinWarning.java,v $
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
package fr.soleil.bensikin.tools;

/**
 * This daughter of Exception is a "mute" Exception, that overrides
 * printStackTrace with a do-nothing method. Used when an Exeption is normal and
 * expected but should be hidden from the end user.
 * 
 * @author CLAISSE
 */
public class BensikinWarning extends Exception {
	/**
	 * Standard exception constructor
	 * 
	 * @param arg0
	 *            The exception's message
	 */
	public BensikinWarning(String arg0) {
		super(arg0);
	}

	/**
	 * Overloads printStackTrace to do nothing
	 */
	public void printStackTrace() {
		// do nothing so that the end user doesn't think there is a bug
	}
}
