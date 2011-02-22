//+======================================================================
// $Source: /cvsroot/tango-cs/tango/api/java/fr/soleil/TangoArchiving/ArchivingTools/Diary/LoggerFactory.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  LoggerFactory.
//						(Claisse Laurent) - 5 juil. 2005
//
// $Author: ounsy $
//
// $Revision: 1.2 $
//
// $Log: LoggerFactory.java,v $
// Revision 1.2  2006/06/29 08:42:23  ounsy
// added a hasDiary parameter  to getImpl, if true returns a DoNothingLogger instance
//
// Revision 1.1  2006/06/16 09:24:03  ounsy
// moved from the TdbArchiving project
//
// Revision 1.2  2006/06/13 13:30:04  ounsy
// minor changes
//
// Revision 1.1  2006/06/08 08:34:49  ounsy
// creation
//
// Revision 1.2  2005/11/29 18:28:12  chinkumo
// no message
//
// Revision 1.1.2.2  2005/09/14 15:41:32  chinkumo
// Second commit !
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================
package fr.soleil.commonarchivingapi.ArchivingTools.Diary;

public class LoggerFactory {
    // public static final int DEFAULT_TYPE = 1;
    // public static final int REAL_IMPL_TYPE = 2;

    private static ILogger currentImpl = null;

    /**
     * @param typeOfImpl
     * @return 8 juil. 2005
     */
    public static synchronized ILogger getImpl(final String archiver, final String path,
	    final boolean hasDiary) {
	if (!hasDiary) {
	    currentImpl = new DoNothingLogger();
	} else {
	    currentImpl = new DefaultLogger(archiver, path);
	}
	return currentImpl;
    }

    /**
     * @return 8 juil. 2005
     */
    // public static ILogger getCurrentImpl() {
    // return currentImpl;
    // }

}
