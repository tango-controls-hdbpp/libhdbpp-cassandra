//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/CreateDB.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  CreateDB.
//						(Chinkumo Jean) - Nov 22, 2004
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: CreateDB.java,v $
// Revision 1.4  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.3  2005/03/09 09:47:39  chinkumo
// Index tablespaces were removed since indexes are now directly included inside  tables.
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

import fr.soleil.sgad.DateUtil;

public class CreateDB {

	public static String getText() {
		String create_db = "";
		create_db = "#rem ---------------------------------------------"
				+ fr.soleil.sgad.Constants.newLine
				+ "#rem --						--"
				+ fr.soleil.sgad.Constants.newLine
				+ "#rem --      This script create the database   	--"
				+ fr.soleil.sgad.Constants.newLine
				+ "#rem -- 					--"
				+ fr.soleil.sgad.Constants.newLine
				+ "#rem --        "
				+ DateUtil.getTime()
				+ ", SYNCHROTRON SOLEIL	--"
				+ fr.soleil.sgad.Constants.newLine
				+ "#rem -- 		   			--"
				+ fr.soleil.sgad.Constants.newLine
				+ "#rem -- 		  Jérémy GUYOT		--"
				+ fr.soleil.sgad.Constants.newLine
				+ "#rem ---------------------------------------------"
				+ fr.soleil.sgad.Constants.newLine
				+ fr.soleil.sgad.Constants.newLine
				+ "connect sys/change_on_install as SYSDBA"
				+ fr.soleil.sgad.Constants.newLine
				+ "set echo on"
				+ fr.soleil.sgad.Constants.newLine
				+ "spool "
				+ fr.soleil.sgad.oracle.Generator._oracleBase_Path
				+ "\\admin\\"
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ "\\create\\CreateDB.log"
				+ fr.soleil.sgad.Constants.newLine
				+ "startup nomount pfile=\""
				+ fr.soleil.sgad.oracle.Generator._oracleBase_Path
				+ "\\admin\\"
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ "\\pfile\\init.ora\";"
				+ fr.soleil.sgad.Constants.newLine
				+ "CREATE DATABASE "
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ ""
				+ fr.soleil.sgad.Constants.newLine
				+ "MAXINSTANCES 2"
				+ fr.soleil.sgad.Constants.newLine
				+ "MAXLOGHISTORY 1000"
				+ fr.soleil.sgad.Constants.newLine
				+ "MAXLOGFILES 5"
				+ fr.soleil.sgad.Constants.newLine
				+ "MAXLOGMEMBERS 5"
				+ fr.soleil.sgad.Constants.newLine
				+ "MAXDATAFILES 100"
				+ fr.soleil.sgad.Constants.newLine
				+ "DATAFILE '"
				+ fr.soleil.sgad.oracle.Generator._oracleBase_Path
				+ "\\oradata\\"
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ "\\system01.dbf' SIZE 500M REUSE AUTOEXTEND ON NEXT  10240K MAXSIZE UNLIMITED"
				+ fr.soleil.sgad.Constants.newLine
				+ "EXTENT MANAGEMENT LOCAL"
				+ fr.soleil.sgad.Constants.newLine
				+ ((!fr.soleil.sgad.oracle.Generator.hdb_generation
						&& fr.soleil.sgad.oracle.Generator.tdb_generation && !fr.soleil.sgad.oracle.Generator.snap_generation) ? "NOARCHIVELOG"
						: "ARCHIVELOG")
				+ fr.soleil.sgad.Constants.newLine
				+ "UNDO TABLESPACE \"UNDOTBS\""
				+ fr.soleil.sgad.Constants.newLine
				+ "DATAFILE '"
				+ fr.soleil.sgad.oracle.Generator._oracleBase_Path
				+ "\\oradata\\"
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ "\\undo01.dbf' SIZE 40M REUSE AUTOEXTEND ON NEXT"
				+ fr.soleil.sgad.Constants.newLine
				+ "102400k MAXSIZE UNLIMITED"
				+ fr.soleil.sgad.Constants.newLine
				+ "DEFAULT TEMPORARY TABLESPACE TEMP TEMPFILE '"
				+ fr.soleil.sgad.oracle.Generator._oracleBase_Path
				+ "\\oradata\\"
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ "\\temp01.dbf' SIZE 40M REUSE AUTOEXTEND ON NEXT  640K MAXSIZE UNLIMITED"
				+ fr.soleil.sgad.Constants.newLine
				+ "CHARACTER SET WE8ISO8859P1"
				+ fr.soleil.sgad.Constants.newLine
				+ "NATIONAL CHARACTER SET UTF8"
				+ fr.soleil.sgad.Constants.newLine
				+ "LOGFILE "
				+ fr.soleil.sgad.Constants.newLine
				+ "\t GROUP 1 ('"
				+ fr.soleil.sgad.oracle.Generator._oracleBase_Path
				+ "\\oradata\\"
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ "\\redo01.log') SIZE "
				+ ((!fr.soleil.sgad.oracle.Generator.hdb_generation
						&& fr.soleil.sgad.oracle.Generator.tdb_generation && !fr.soleil.sgad.oracle.Generator.snap_generation) ? "50000K"
						: "200000K")
				+ ","
				+ fr.soleil.sgad.Constants.newLine
				+ "\t GROUP 2 ('"
				+ fr.soleil.sgad.oracle.Generator._oracleBase_Path
				+ "\\oradata\\"
				+ fr.soleil.sgad.oracle.Generator._oracle_sid
				+ "\\redo02.log') SIZE "
				+ ((!fr.soleil.sgad.oracle.Generator.hdb_generation
						&& fr.soleil.sgad.oracle.Generator.tdb_generation && !fr.soleil.sgad.oracle.Generator.snap_generation) ? "50000K"
						: "200000K") + ";" + fr.soleil.sgad.Constants.newLine
				+ "spool off" + fr.soleil.sgad.Constants.newLine + "exit;"
				+ fr.soleil.sgad.Constants.newLine;

		return create_db;
	}
}
