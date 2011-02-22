//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/TdbModeHandler.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  TdbModeHandler.
//						(Chinkumo Jean) - Apr 27, 2004
//
// $Author: pierrejoseph $
//
// $Revision: 1.17 $
//
// $Log: TdbModeHandler.java,v $
// Revision 1.17  2007/06/13 13:12:03  pierrejoseph
// modeHandler is stored in a common archiver part
//
// Revision 1.16  2007/05/25 12:03:36  pierrejoseph
// Pb mode counter on various mode in the same collector : one ModesCounters object by attribut stored in a hashtable of the ArchiverCollector object (in common part)
//
// Revision 1.15  2007/04/24 14:29:28  ounsy
// added a log in the case of unexpected ClassCast exception on the event's value
//
// Revision 1.14  2007/04/03 15:15:41  ounsy
// corrected isDataArchivable
//
// Revision 1.13  2007/03/20 10:46:51  ounsy
// minor changes
//
// Revision 1.12  2006/11/30 14:17:44  ounsy
// minor changes
//
// Revision 1.11  2006/11/09 14:21:55  ounsy
// minor changes
//
// Revision 1.10  2006/11/08 10:11:11  ounsy
// minor changes
//
// Revision 1.9  2006/11/07 16:13:35  ounsy
// corrected the state/string bug
//
// Revision 1.8  2006/07/27 12:37:20  ounsy
// corrected the absolute and relative modes
//
// Revision 1.7  2006/05/16 09:27:50  ounsy
// added a rounding step to avoid the irrational period ratios problem
//
// Revision 1.6  2006/03/13 15:26:36  ounsy
// Long as an int management
//
// Revision 1.5  2006/03/10 12:01:59  ounsy
// state and string support
//
// Revision 1.4  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.3.10.4  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.3.10.3  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.3.10.2  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.3.10.1  2005/09/09 10:18:58  chinkumo
// Since the collecting politic was simplified and improved this class was modified.
//
// Revision 1.3  2005/06/14 10:39:09  chinkumo
// Branch (tdbArchiver_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.2.6.1  2005/05/11 15:56:38  chinkumo
// The 'absolute mode' just behaved like the 'threshold mode'. This was corrected.
//
// Revision 1.2  2005/02/04 17:10:41  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.1  2004/12/06 16:43:24  chinkumo
// First commit (new architecture).
//
// Revision 1.3  2004/09/07 13:19:15  ho
// Fixe the bug in the relative mode when the value is negative
//
// Revision 1.2  2004/09/01 15:32:53  chinkumo
// Heading was updated.
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package TdbArchiver.Collector;

import Common.Archiver.Collector.ModeHandler;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Mode.Mode;

public class TdbModeHandler extends ModeHandler
{
	/**
	 * Creates a new instance of TdbModeHandler
	 */
	public TdbModeHandler(Mode tdbMode)
	{
		super(tdbMode);
	}
}
