//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/mysql/Constants.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Constants.
//						(Chinkumo Jean) - Dec 4, 2004
// $Author: pierrejoseph $
//
// $Revision: 1.6 $
//
// $Log: Constants.java,v $
// Revision 1.6  2007/03/16 08:53:38  pierrejoseph
// add a TdbCleaner to remove the old att rows
//
// Revision 1.5  2006/02/17 09:32:50  chinkumo
// Since the structure and the name of some SNAPSHOT database's table changed, this was reported here.
//
// Revision 1.4  2006/02/16 14:45:49  chinkumo
// The management of spectrums WRITE_ONLY and READ_WRITE was added.
// The management of images (READ_ONLY, WRITE_ONLY and READ_WRITE) was added.
//
// Revision 1.3  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.2  2005/02/01 18:50:40  chinkumo
// Changes made to support the InnoDB Storage Engine.
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

package fr.soleil.sgad.mysql;

public class Constants
{
	/**
	 * Les roles
	 */
	public static String[] roles = {"manager", "archiver", "browser", "cleaner"};
	/**
	 * Schema
	 */
	public static String[] schema = {"hdb", "tdb", "snap"};


	/**
	 * HDB OBjects
	 */
	public static String[] hdbObjects = {"adt", "apt", "amt"};
	public static String[] tdbObjects = {"adt", "apt", "amt"};
	public static String[] snapObjects = {"ast", "context", "list", "snapshot", //  0 -> 3
	                                     "t_im_1val", "t_im_2val",              //  4 -> 5
	                                     "t_sp_1val","t_sp_2val",               //  6 -> 7
	                                     "t_sc_num_1val", "t_sc_num_2val",      //  8 -> 9
	                                     "t_sc_str_1val", "t_sc_str_2val"};     //  10 -> 11

	public static String[] storage_engines = {"MyISAM", "InnoDB"}; // Inno
	public static String storage_engine = "MyISAM";

	public static String separator = "# --------------------------------------------------------";
}
