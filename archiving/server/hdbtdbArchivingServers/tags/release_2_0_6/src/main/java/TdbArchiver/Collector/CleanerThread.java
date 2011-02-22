//+======================================================================
// $Source: /cvsroot/tango-cs/tango/jserver/archiving/TdbArchiver/Collector/CleanerThread.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  CleanerThread.
//						(Chinkumo Jean) - 13 mai 2004
//
// $Author: ounsy $
//
// $Revision: 1.4 $
//
// $Log: CleanerThread.java,v $
// Revision 1.4  2006/05/16 09:27:15  ounsy
// minor changes
//
// Revision 1.3  2005/11/29 17:34:14  chinkumo
// no message
//
// Revision 1.2.14.3  2005/11/29 16:15:11  chinkumo
// Code reformated (pogo compatible)
//
// Revision 1.2.14.2  2005/11/15 13:45:38  chinkumo
// ...
//
// Revision 1.2.14.1  2005/09/26 08:01:54  chinkumo
// Minor changes !
//
// Revision 1.2  2005/02/04 17:10:39  chinkumo
// The trouble with the grouped stopping strategy was fixed.
//
// Revision 1.1  2004/12/06 16:43:24  chinkumo
// First commit (new architecture).
//
// Revision 1.4  2004/09/14 07:04:58  chinkumo
// Minor change.
//
// Revision 1.3  2004/09/01 15:31:57  chinkumo
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

public class CleanerThread extends Thread
{

	public CleanerThread()
	{
        super( "CleanerThread" );
	}

	public void run()
	{
		System.gc();
		System.out.println("DONE ! : " + getClass() + " (" + getName() + ")");
	}
}
