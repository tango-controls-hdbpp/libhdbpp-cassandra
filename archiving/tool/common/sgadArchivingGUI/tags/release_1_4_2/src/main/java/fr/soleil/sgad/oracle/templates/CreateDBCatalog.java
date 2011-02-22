//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/CreateDBCatalog.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  CreateDBCatalog.
//						(Chinkumo Jean) - Nov 22, 2004
// $Author: chinkumo $
//
// $Revision: 1.3 $
//
// $Log: CreateDBCatalog.java,v $
// Revision 1.3  2005/11/29 18:25:49  chinkumo
// no message
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

package fr.soleil.sgad.oracle.templates;

public class CreateDBCatalog {
	public static String getText() {
		String create_db_catalog = "";
		create_db_catalog = "connect sys/change_on_install as SYSDBA"
				+ fr.soleil.sgad.Constants.newLine + "set echo on"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\catalog.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\standard.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\catexp7.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\catblock.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\catproc.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\catoctk.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\catplug.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\caths.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\owminst.plb;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\dbmsplts.sql;"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\rdbms\\admin\\prvtplts.plb;"
				+ fr.soleil.sgad.Constants.newLine
				+ fr.soleil.sgad.Constants.newLine + "connect SYSTEM/manager"
				+ fr.soleil.sgad.Constants.newLine + "@"
				+ fr.soleil.sgad.oracle.Generator._oracleHome_Path
				+ "\\sqlplus\\admin\\pupbld.sql;"
				+ fr.soleil.sgad.Constants.newLine + "spool off"
				+ fr.soleil.sgad.Constants.newLine + "exit;"
				+ fr.soleil.sgad.Constants.newLine;

		return create_db_catalog;
	}
}
