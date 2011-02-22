//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/Batch.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Batch.
//						(Chinkumo Jean) - Nov 19, 2004
//
// $Author: chinkumo $
//
// $Revision: 1.3 $
//
// $Log: Batch.java,v $
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

public class Batch
{
	public static String getText()
	{
		String batchText = "";
		batchText =
		"SET ORACLE_SID=" + fr.soleil.sgad.oracle.Generator._oracle_sid + fr.soleil.sgad.Constants.newLine +
		"SET ORACLE_BASE=" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + fr.soleil.sgad.Constants.newLine +
		"SET ORACLE_HOME=" + fr.soleil.sgad.oracle.Generator._oracleHome_Path + fr.soleil.sgad.Constants.newLine +
		"ECHO ." + fr.soleil.sgad.Constants.newLine +
		"oradim -new -sid " + fr.soleil.sgad.oracle.Generator._oracle_sid + " -intpwd oracle -startmode manual -pfile \"" + fr.soleil.sgad.oracle.Generator.FilesPath[ 0 ] + "\"" + fr.soleil.sgad.Constants.newLine +
		fr.soleil.sgad.Constants.newLine +

		"%ORACLE_HOME%\\bin\\sqlplus /nolog @" + fr.soleil.sgad.oracle.Generator.FilesPath[ 1 ] + fr.soleil.sgad.Constants.newLine +
		"PAUSE" + fr.soleil.sgad.Constants.newLine +
		"%ORACLE_HOME%\\bin\\sqlplus /nolog @" + fr.soleil.sgad.oracle.Generator.FilesPath[ 2 ] + fr.soleil.sgad.Constants.newLine +
		"PAUSE" + fr.soleil.sgad.Constants.newLine +
		"%ORACLE_HOME%\\bin\\sqlplus /nolog @" + fr.soleil.sgad.oracle.Generator.FilesPath[ 3 ] + fr.soleil.sgad.Constants.newLine +
		"PAUSE" + fr.soleil.sgad.Constants.newLine +
		"%ORACLE_HOME%\\bin\\sqlplus /nolog @" + fr.soleil.sgad.oracle.Generator.FilesPath[ 4 ] + fr.soleil.sgad.Constants.newLine +
		"PAUSE" + fr.soleil.sgad.Constants.newLine +
		"%ORACLE_HOME%\\bin\\oradim -edit -sid " + fr.soleil.sgad.oracle.Generator._oracle_sid + " -startmode auto" + fr.soleil.sgad.Constants.newLine +
		"PAUSE" + fr.soleil.sgad.Constants.newLine;

		return batchText;
	}
}
