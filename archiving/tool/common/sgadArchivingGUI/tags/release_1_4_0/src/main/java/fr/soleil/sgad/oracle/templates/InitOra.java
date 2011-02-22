//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/InitOra.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  InitOra.
//						(Chinkumo Jean) - Nov 22, 2004
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: InitOra.java,v $
// Revision 1.5  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.4  2005/03/10 23:08:53  chinkumo
// Defaut time format changed from 'YYYY-MM-DD HH24:MI:SS.FF' (US) to 'DD-MM-YYYY HH24:MI:SS.FF' (french)
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

public class InitOra
{

	public static String getText()
	{
		String io = "";
		io = "db_name = " + fr.soleil.sgad.oracle.Generator._oracle_sid + "" + fr.soleil.sgad.Constants.newLine +
		     "db_domain = \"WORLD\"" + fr.soleil.sgad.Constants.newLine +
		     "instance_name = " + fr.soleil.sgad.oracle.Generator._oracle_sid + "" + fr.soleil.sgad.Constants.newLine +
		     "service_names = " + fr.soleil.sgad.oracle.Generator._oracle_sid + "" + fr.soleil.sgad.Constants.newLine +
		     "compatible = 9.2.0.1.0" + fr.soleil.sgad.Constants.newLine +
		     "db_block_size = 8192" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "##  SGA size" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine +
		     "db_files = 512" + fr.soleil.sgad.Constants.newLine +
		     "db_file_multiblock_read_count=8" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine +
		     "log_buffer = 1024000" + fr.soleil.sgad.Constants.newLine +
		     "db_cache_size=300m" + fr.soleil.sgad.Constants.newLine +
		     "shared_pool_size=400m" + fr.soleil.sgad.Constants.newLine +
		     "java_pool_size=0" + fr.soleil.sgad.Constants.newLine +
		     "large_pool_size=0" + fr.soleil.sgad.Constants.newLine +
		     "##" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "open_cursors=1500" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "background_dump_dest=" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\admin\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\bdump" + fr.soleil.sgad.Constants.newLine +
		     "core_dump_dest=" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\admin\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\cdump" + fr.soleil.sgad.Constants.newLine +
		     "user_dump_dest=" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\admin\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\udump" + fr.soleil.sgad.Constants.newLine +
		     "max_dump_file_size = 5M  # limit trace file size to 5mb" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "control_files=(" + fr.soleil.sgad.Constants.newLine +
		     "\t \"" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\control01.ctl\", " + fr.soleil.sgad.Constants.newLine +
		     "\t \"" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\control02.ctl\", " + fr.soleil.sgad.Constants.newLine +
		     "\t \"" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\control03.ctl\")" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "processes=300" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "timed_statistics=TRUE" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "CURSOR_SHARING=SIMILAR" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "UNDO_MANAGEMENT = AUTO" + fr.soleil.sgad.Constants.newLine +
		     ( ( !fr.soleil.sgad.oracle.Generator.hdb_generation && fr.soleil.sgad.oracle.Generator.tdb_generation && !fr.soleil.sgad.oracle.Generator.snap_generation ) ? "UNDO_RETENTION=300" : "" ) +
		     fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine +

		     "remote_login_passwordfile=EXCLUSIVE" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "nls_date_format = 'DD/MM/YYYY HH24:MI:SS'" + fr.soleil.sgad.Constants.newLine +
		     "JOB_QUEUE_PROCESSES = 10" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +

		     ( !( !fr.soleil.sgad.oracle.Generator.hdb_generation && fr.soleil.sgad.oracle.Generator.tdb_generation && !fr.soleil.sgad.oracle.Generator.snap_generation ) ?
		       "log_archive_dest = " + fr.soleil.sgad.oracle.Generator._redo_path + fr.soleil.sgad.Constants.newLine +
		       "log_archive_format = arch_%s.arc" + fr.soleil.sgad.Constants.newLine +
		       "log_archive_start = TRUE" + fr.soleil.sgad.Constants.newLine : "" ) +

		     fr.soleil.sgad.Constants.newLine +
		     "# dispatchers = \"(PROTOCOL=TCP)(DISPATCHERS=3)(SERVICE=" + fr.soleil.sgad.oracle.Generator._oracle_sid + ")\"" + fr.soleil.sgad.Constants.newLine +
		     fr.soleil.sgad.Constants.newLine +
		     "UTL_FILE_DIR = '*'" + fr.soleil.sgad.Constants.newLine;
		return io;
	}
}
