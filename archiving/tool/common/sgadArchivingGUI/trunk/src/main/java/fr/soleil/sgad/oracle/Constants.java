//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/Constants.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Constants.
//						(Chinkumo Jean) - Nov 16, 2004
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: Constants.java,v $
// Revision 1.5  2006/02/17 09:32:50  chinkumo
// Since the structure and the name of some SNAPSHOT database's table changed, this was reported here.
//
// Revision 1.4  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.3  2005/06/14 10:46:20  chinkumo
// Branch (sgad_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.2.2.1  2005/04/21 18:44:33  chinkumo
// A new constant named schemaAdmin was added to describe the Administrator's schema.
// Added, the possibility to choose between 3 database pattern:
// 	1) The first named 'BASE' : tablespaces are spitted following the tango format type (scalar/spectrum/image)
// 	2) The second, SOLEIL specific, named 'SPLIT' : tablespaces are spitted following the SOLEIL namming rules (ANS, BOO, LT1, LT2, etc.)
// 	3) The third one, named 'PARTITION' : the database is then made to use the Oracle's partitionning extention.
// Tangotest DServer added for the SPLIT mode.
// A constant was created (adminEmail) to describe the orale admin email adress.
//
// Revision 1.2  2005/03/09 17:10:52  chinkumo
// Unused import removed.
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

package fr.soleil.sgad.oracle;

public class Constants {

	/**
	 * Les roles
	 */
	public static String[] roles = { "manager", "archiver", "browser" };
	public static String roles_prefix = "r";

	/**
	 * Schema
	 */
	public static String[] schema = { "hdb", "tdb", "snap" };

	public static String schemaAdmin = "administrator";

	/**
	 * HDB OBjects
	 */
	public static String[] hdbObjects = { "adt", "apt", "amt", "properties",
			"act", "rnt" };
	public static String[] tdbObjects = { "adt", "apt", "amt", "properties",
			"act", "rnt" };
	// public static String[] snapObjects = {"context", "list", "snapshot",
	// "ast", "rsa", "register_context", "register_snapshot"};
	public static String[] snapObjects = { "ast", "context", "list",
			"snapshot", // 0 -> 3
			"t_im_1val", "t_im_2val", // 4 -> 5
			"t_sp_1val", "t_sp_2val", // 6 -> 7
			"t_sc_num_1val", "t_sc_num_2val", // 8 -> 9
			"t_sc_str_1val", "t_sc_str_2val" }; // 10 -> 11
	/**
	 * Utilisateurs actifs
	 */
	public static String[] activesUser = { "manager", "archiver", "browser" };

	public static String separator = "--------------------------------------------------------------------------------";
	public static String separator2 = "-- \t\t ---------------\t\t";

	public static String[] generationModes = { "BASE", "SPLIT", "PARTITION" };
	public static int generationMode = 0;

	// This should be changed
	public static String adminEmail = "jeremy.guyot@synchrotron-soleil.fr";

	// Specific SOLEIL
	public static String[] domains = { "ANS-C", // i : 1-> 16
			"BOO-C", // j : 1-> 22
			"LT1", // 1
			"LT2", // 1
			"LIN", // 1
			"tango" // 1
	};
	public static int[] domainsNum = { 17, 23, 2, 2, 2, 2 };

	public static String[] datafiles = {
			"\\" + "oradata" + "\\" + Generator._oracle_sid + "\\", // "sc_BOOC01.dbf",
																	// // i :
																	// 1-> 16
			"\\" + "oradata" + "\\" + Generator._oracle_sid + "\\", // j : 1->
																	// 22
			"\\" + "oradata" + "\\" + Generator._oracle_sid + "\\", // 1
			"\\" + "oradata" + "\\" + Generator._oracle_sid + "\\", // 1
			"\\" + "oradata" + "\\" + Generator._oracle_sid + "\\", // 1
			"\\" + "oradata" + "\\" + Generator._oracle_sid + "\\" // 1
	};

	public static String[] types = { "SC", "SP", "IM" };
}
