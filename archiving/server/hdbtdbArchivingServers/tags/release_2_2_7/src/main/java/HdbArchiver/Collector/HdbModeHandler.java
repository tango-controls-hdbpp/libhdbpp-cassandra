//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/HdbModeHandler.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  HdbModeHandler.
//						(Chinkumo Jean) - Apr 27, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.15 $
//
// $Log: HdbModeHandler.java,v $
// Revision 1.15  2007/06/13 13:13:23  pierrejoseph
// the mode counter are managed attribute by attribute in each collector
//
// Revision 1.14  2007/04/24 14:29:28  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.13  2007/04/03 15:40:41  ounsy
// took null values into account
//
// Revision 1.12  2007/03/20 10:46:52  ounsy
// minor changes
//
// Revision 1.11  2006/11/30 14:17:32  ounsy
// added rounding in the initFactor method, as was already the case with TDB
//
// Revision 1.10  2006/11/08 10:10:46  ounsy
// corrected isDataArchivable:
// -added management of the String and State types
// -removed the test that prevented Nan values from being archived
//
// Revision 1.9  2006/07/27 12:37:20  ounsy
// corrected the absolute and relative modes
//
// Revision 1.8  2006/06/30 08:28:25  ounsy
// added a protection against non numeric values where they should be
// REMOVE THIS LATER
//
// Revision 1.7  2006/03/13 15:27:12  ounsy
// Long as an int management
//
// Revision 1.6  2006/03/10 11:58:29  ounsy
// state and string support
//
// Revision 1.5  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.4.12.3  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.4.12.2  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.4.12.1  2005/09/09 10:12:08  chinkumo
// Since the collecting politic was simplified and improved this class was modified.
//
// Revision 1.4  2005/06/14 10:30:27  chinkumo
// Branch (hdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.3.4.1  2005/05/11 15:05:13  chinkumo
// The 'absolute mode' just behaved like the 'threshold mode'. This was corrected.
//
// Revision 1.3  2005/02/04 17:10:15  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:14  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:14  chinkumo
// First commit (new architecture).
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package HdbArchiver.Collector;

import Common.Archiver.Collector.ModeHandler;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;

public class HdbModeHandler extends ModeHandler {
	/**
	 * Creates a new instance of HdbModeHandler
	 */
	public HdbModeHandler(Mode hdbMode) {
		super(hdbMode);
	}
}
