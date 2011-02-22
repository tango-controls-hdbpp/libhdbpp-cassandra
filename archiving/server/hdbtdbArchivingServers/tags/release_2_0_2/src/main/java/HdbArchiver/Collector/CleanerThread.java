//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/HdbArchiver/Collector/CleanerThread.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  CleanerThread.
//						(Chinkumo Jean) - 13 mai 2004
//
// $Author: ounsy $
//
// $Revision: 1.5 $
//
// $Log: CleanerThread.java,v $
// Revision 1.5  2006/05/16 09:26:56  ounsy
// minor changes
//
// Revision 1.4  2005/11/29 17:33:53  chinkumo
// no message
//
// Revision 1.3.16.3  2005/11/29 16:16:05  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.3.16.2  2005/11/15 13:46:08  chinkumo
// ...
//
// Revision 1.3.16.1  2005/09/26 08:01:20  chinkumo
// Minor changes !
//
// Revision 1.3  2005/02/04 17:10:14  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.2  2005/01/26 16:38:15  chinkumo
// Ultimate synchronization before real sharing.
//
// Revision 1.1  2004/12/06 16:43:13  chinkumo
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

public class CleanerThread extends Thread
{

	public CleanerThread()
	{
	    super ( "CleanerThread" );
    }

	public void run()
	{
		System.gc();
		System.out.println("DONE ! : " + getClass() + " (" + getName() + ")");
	}
}
