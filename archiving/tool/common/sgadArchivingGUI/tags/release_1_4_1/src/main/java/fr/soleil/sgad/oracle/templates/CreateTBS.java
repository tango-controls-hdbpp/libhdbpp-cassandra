//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/CreateTBS.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  CreateTBS.
//						(Chinkumo Jean) - Nov 23, 2004
// $Author: chinkumo $
//
// $Revision: 1.5 $
//
// $Log: CreateTBS.java,v $
// Revision 1.5  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.4  2005/06/14 10:46:19  chinkumo
// Branch (sgad_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.3.4.1  2005/04/21 19:12:37  chinkumo
// The tablespaces generation was modified to take into account the modes BASE, SPLIT and PARTITION.
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
import fr.soleil.sgad.oracle.Generator;


public class CreateTBS
{
	public static String getText()
	{
		String create_tbs = "";
		create_tbs =
		"#rem ---------------------------------------------" + fr.soleil.sgad.Constants.newLine +
		"#rem --						--" + fr.soleil.sgad.Constants.newLine +
		"#rem --      This script create all tablespaces	--" + fr.soleil.sgad.Constants.newLine +
		"#rem -- 	change directory if necessary	--" + fr.soleil.sgad.Constants.newLine +
		"#rem --      " + DateUtil.getTime() + ", SYNCHROTRON SOLEIL	--" + fr.soleil.sgad.Constants.newLine +
		"#rem -- 		   			--" + fr.soleil.sgad.Constants.newLine +
		"#rem -- 		  Jérémy GUYOT		--" + fr.soleil.sgad.Constants.newLine +
		"#rem ---------------------------------------------" + fr.soleil.sgad.Constants.newLine +
		fr.soleil.sgad.Constants.newLine +
		"connect sys/change_on_install as sysdba" + fr.soleil.sgad.Constants.newLine +
		"set echo on" + fr.soleil.sgad.Constants.newLine +
		fr.soleil.sgad.Constants.newLine +
		"alter user sys temporary tablespace TEMP;" + fr.soleil.sgad.Constants.newLine +
		fr.soleil.sgad.Constants.newLine + generateTablespaces_Common();

		if ( fr.soleil.sgad.oracle.Constants.generationMode == 0 )
		{
			create_tbs = create_tbs + fr.soleil.sgad.Constants.newLine + generateTablespaces_StandardMode();
		}
		else if ( fr.soleil.sgad.oracle.Constants.generationMode == 1 )
		{
			if ( fr.soleil.sgad.oracle.Generator.tdb_generation )
			{
				create_tbs = create_tbs + fr.soleil.sgad.Constants.newLine + generateTablespaces_StandardMode();
			}
			else
			{
				create_tbs = create_tbs + fr.soleil.sgad.Constants.newLine + generateTablespaces_SplitMode();
			}
		}
		else if ( fr.soleil.sgad.oracle.Constants.generationMode == 2 )
		{
			if ( fr.soleil.sgad.oracle.Generator.tdb_generation )
			{
				create_tbs = create_tbs + fr.soleil.sgad.Constants.newLine + generateTablespaces_StandardMode();
			}
		}

		create_tbs = create_tbs + "EXIT;" + fr.soleil.sgad.Constants.newLine;

		return create_tbs;
	}

	private static String generateTablespaces_Common()
	{
		String create_tbs = "";
		create_tbs =
		"CREATE TABLESPACE conf" + fr.soleil.sgad.Constants.newLine +
		"\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\conf01.dbf'" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "SIZE 10M" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		"\t\t\t " + "NEXT		50M" + fr.soleil.sgad.Constants.newLine +
		"\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		"\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine +
		"CREATE TABLESPACE conf_ind" + fr.soleil.sgad.Constants.newLine +
		"\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\conf_ind01.dbf'" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "SIZE 20M" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		"\t\t\t " + "NEXT		50M" + fr.soleil.sgad.Constants.newLine +
		"\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		"\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine +
		fr.soleil.sgad.Constants.newLine +
		"CREATE TABLESPACE TOOLS" + fr.soleil.sgad.Constants.newLine +
		"\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\tools01.dbf'" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "SIZE 50M" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		"\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		"\t\t\t " + "NEXT		50M" + fr.soleil.sgad.Constants.newLine +
		"\t\t\t " + "MAXSIZE		5000M" + fr.soleil.sgad.Constants.newLine +
		"\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine;

		if ( fr.soleil.sgad.oracle.Generator.hdb_generation )
		{
			create_tbs = create_tbs +
			             "CREATE TABLESPACE SNAP" + fr.soleil.sgad.Constants.newLine +
			             "\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\snap01.dbf'" + fr.soleil.sgad.Constants.newLine +
			             "\t\t " + "SIZE 50M" + fr.soleil.sgad.Constants.newLine +
			             "\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
			             "\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
			             "\t\t\t " + "NEXT		50M" + fr.soleil.sgad.Constants.newLine +
			             "\t\t\t " + "MAXSIZE		5000M" + fr.soleil.sgad.Constants.newLine +
			             "\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine;
		}
		create_tbs = create_tbs + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine;
		return create_tbs;
	}

	private static String generateTablespaces_StandardMode()
	{
		String create_tbs = "";
		create_tbs = "CREATE TABLESPACE SC_DATA" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\sc_data01.dbf'" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "SIZE 100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "NEXT		100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine +
		             "" + fr.soleil.sgad.Constants.newLine +
		             "CREATE TABLESPACE SC_DATA_ind" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\sc_data_ind01.dbf'" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "SIZE 100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "NEXT		100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine +
		             fr.soleil.sgad.Constants.newLine +
		             "CREATE TABLESPACE SP_DATA" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\sp_data01.dbf'" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "SIZE 100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "NEXT		100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine +
		             fr.soleil.sgad.Constants.newLine +
		             "CREATE TABLESPACE SP_DATA_ind" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\sp_data_ind01.dbf'" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "SIZE 100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "NEXT		100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine +
		             fr.soleil.sgad.Constants.newLine +
		             "CREATE TABLESPACE IM_DATA" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\im_data01.dbf'" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "SIZE 60M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "NEXT		100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine +
		             fr.soleil.sgad.Constants.newLine +
		             "CREATE TABLESPACE IM_DATA_ind" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "DATAFILE '" + fr.soleil.sgad.oracle.Generator._oracleBase_Path + "\\oradata\\" + fr.soleil.sgad.oracle.Generator._oracle_sid + "\\im_data_ind01.dbf'" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "SIZE 50M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "REUSE" + fr.soleil.sgad.Constants.newLine +
		             "\t\t " + "AUTOEXTEND ON" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "NEXT		100M" + fr.soleil.sgad.Constants.newLine +
		             "\t\t\t " + "MAXSIZE		8000M" + fr.soleil.sgad.Constants.newLine +
		             "\t " + "EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine;
		return create_tbs;
	}

	private static String generateTablespaces_SplitMode()
	{
		String tbs = "";
		for ( int i = 0 ; i < fr.soleil.sgad.oracle.Constants.domains.length ; i++ )
		{
			for ( int j = 1 ; j < fr.soleil.sgad.oracle.Constants.domainsNum[ i ] ; j++ )
			{
				tbs = tbs +
				      "CREATE TABLESPACE \"" +
				      fr.soleil.sgad.oracle.Constants.types[ 0 ] + "_" +
				      fr.soleil.sgad.oracle.Constants.domains[ i ].replaceAll("-" , "") +
				      ( ( ( fr.soleil.sgad.oracle.Constants.domainsNum[ i ] > 2 ) && ( j < 10 ) ) ? "0" : "" ) +
				      ( ( ( fr.soleil.sgad.oracle.Constants.domainsNum[ i ] > 2 ) ) ? Integer.toString(j) : "" ) +
				      "\"" +
				      " DATAFILE '" +
				      Generator._oracleBase_Path + fr.soleil.sgad.oracle.Constants.datafiles[ i ] + fr.soleil.sgad.oracle.Constants.types[ 0 ] + "_" +
				      fr.soleil.sgad.oracle.Constants.domains[ i ].replaceAll("-" , "") +
				      ( ( ( fr.soleil.sgad.oracle.Constants.domainsNum[ i ] > 2 ) && ( j < 10 ) ) ? "0" : "" ) +
				      ( ( ( fr.soleil.sgad.oracle.Constants.domainsNum[ i ] > 2 ) ) ? Integer.toString(j) : "" ) +
				      ".dbf" + "' SIZE 10M REUSE AUTOEXTEND ON NEXT 10M\tMAXSIZE\t30000M EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;" + fr.soleil.sgad.Constants.newLine;
			}
		}
		return tbs;
	}
}
