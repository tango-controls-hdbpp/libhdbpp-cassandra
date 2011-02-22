//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/Constants.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Constants.
//						(Chinkumo Jean) - Dec 15, 2004
// $Author: chinkumo $
//
// $Revision: 1.8 $
//
// $Log: Constants.java,v $
// Revision 1.8  2006/02/16 14:45:49  chinkumo
// The management of spectrums WRITE_ONLY and READ_WRITE was added.
// The management of images (READ_ONLY, WRITE_ONLY and READ_WRITE) was added.
//
// Revision 1.7  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.6  2005/08/19 14:03:32  chinkumo
// no message
//
// Revision 1.5.4.1.2.1  2005/08/11 08:02:52  chinkumo
// The 'update snapshot comment' bug was fixed.
// (When the snapshot comment was changed, the snapshot time was also updated !)
//
// Revision 1.5.4.1  2005/08/01 15:41:24  chinkumo
// Correct the bug that appear at the end of the month while new partitions must be created.
// (months that do not have 30 days)
// 
// The 'comment' field was added in the 'snap' table for the MySQL side.
//
// Revision 1.5  2005/06/16 08:43:26  chinkumo
// release and date updated.
//
// Revision 1.4  2005/03/09 09:47:39  chinkumo
// Index tablespaces were removed since indexes are now directly included inside  tables.
//
// Revision 1.3  2005/02/01 18:50:40  chinkumo
// Changes made to support the InnoDB Storage Engine.
//
// Revision 1.2  2005/01/31 10:25:39  chinkumo
// The application now takes care of the carriage return when on UNIX (\r) or WINDOWS (\r\n) Operating System type. An argument must be given when launching the application (0 -> UNIX ; 1 -> WINDOWS)
//
// Revision 1.1  2005/01/26 10:24:58  chinkumo
// First commit
//
//
// copyleft :	Synchrotron SOLEIL
//					L'Orme des Merisiers
//					Saint-Aubin - BP 48
//					91192 GIF-sur-YVETTE CEDEX
//
//-======================================================================

package fr.soleil.sgad;

public class Constants {
	public static String release = "1_005";
	public static String date = "16/02/2006";

	public static String target_OS = "UNIX";

	public static String newLine = "";

}
