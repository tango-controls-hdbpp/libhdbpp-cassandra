//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/mysql/Generator.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Generator.
//						(Chinkumo Jean) - Dec 4, 2004
// $Author: pierrejoseph $
//
// $Revision: 1.11 $
//
// $Log: Generator.java,v $
// Revision 1.11  2007/06/27 15:46:34  pierrejoseph
// FLUSH PRIVILEGES at the end of the script fro MySql
//
// Revision 1.10  2007/03/16 08:53:38  pierrejoseph
// add a TdbCleaner to remove the old att rows
//
// Revision 1.9  2007/02/16 08:32:52  pierrejoseph
// minor change
//
// Revision 1.8  2007/02/08 16:16:39  pierrejoseph
// Generation of a .sh script in case of Linux Os.
//
// Revision 1.7  2007/02/01 15:03:55  pierrejoseph
// Mantis 2685 : Grants on t_sc_str_2val for the archiver were missing.
//
// Revision 1.6  2006/11/29 10:10:13  ounsy
// minor changes
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

package fr.soleil.sgad.mysql;


import java.io.*;

import fr.soleil.sgad.Constants;
import fr.soleil.sgad.DateUtil;

public class Generator
{
	/**
	 * Chemin où seront générés les scripts
	 */
	public static String _script_Path = "";
	/**
	 * Variable précisant la prise en compte (ou non) du schéma HDB
	 */
	public static boolean hdb_generation = false;
	/**
	 * Variable précisant la prise en compte (ou non) du schéma TDB
	 */
	public static boolean tdb_generation = false;
	/**
	 * Variable précisant la prise en compte (ou non) du schéma SNAP
	 */
	public static boolean snap_generation = false;

	//private static InputStreamReader inputStreamReader;
	//private static BufferedReader bufferedReader;

	private static StringBuffer heading = new StringBuffer();
	private static StringBuffer objects_gen = new StringBuffer();
	private static StringBuffer grants_gen = new StringBuffer();
	public static String[] FilesPath = new String[ 2 ];

	public Generator()
	{
	}

	public static void add_object(String str)
	{
		objects_gen.append(str);
	}

	public void generate()
	{
		printConfig();

		heading_generation();
		objects_gen();
		grants_gen();

		//printFile();
		writeFile();
	}

	public void reinitialise()
	{
		heading = new StringBuffer();
		objects_gen = new StringBuffer();
		grants_gen = new StringBuffer();
	}

	public void printConfig()
	{
		System.out.println("Chemin des scripts : ");
		System.out.println("\t_script_Path = " + _script_Path);
		System.out.println("\thdb_generation = " + hdb_generation);
		System.out.println("\ttdb_generation = " + tdb_generation);
		System.out.println("\tsnap_generation = " + snap_generation);
	}

	private void heading_generation()
	{
		heading.append(fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine +
		               "#" + "\t\t\t" + DateUtil.getTime() + ", SYNCHROTRON SOLEIL" + fr.soleil.sgad.Constants.newLine +
		               "#" + "\t\t\t" + "Jean CHINKUMO" + fr.soleil.sgad.Constants.newLine +
		               fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);
	}

	private void objects_gen()
	{
		objects_gen.append("");

		if ( hdb_generation )
			fr.soleil.sgad.mysql.templates.HDB_TDB_Template.objects_gen(0);
		if ( tdb_generation )
			fr.soleil.sgad.mysql.templates.HDB_TDB_Template.objects_gen(1);
		if ( snap_generation )
			fr.soleil.sgad.mysql.templates.SNAP_Template.objects_gen();
	}

	private void grants_gen()
	{
		grants_gen.append(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t PRIVILEGES " + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine);
		grants_gen.append("USE mysql;" + fr.soleil.sgad.Constants.newLine);
		
		int maxLength = fr.soleil.sgad.mysql.Constants.roles.length;
		if (!tdb_generation)  maxLength--;
		
		for ( int i = 0 ; i < maxLength ; i++ )
		{
			if ( hdb_generation )
			{
				grantsSpecific_gen(0 , i);
			}
			if ( tdb_generation )
			{
				grantsSpecific_gen(1 , i);
			}
			if ( snap_generation )
			{
				grantsSpecific_gen(2 , i);
			}
		}
		
		grants_gen.append(fr.soleil.sgad.Constants.newLine + "FLUSH PRIVILEGES;" + fr.soleil.sgad.Constants.newLine);
	}

	/*private void grantsSpecificRemove_gen(int schema , int role)
	{
		String user = fr.soleil.sgad.icons.mysql.Constants.schema[ schema ] + fr.soleil.sgad.icons.mysql.Constants.roles[ role ];
		grants_gen.append(fr.soleil.sgad.icons.Constants.newLine + fr.soleil.sgad.icons.Constants.newLine + fr.soleil.sgad.icons.mysql.Constants.separator + fr.soleil.sgad.icons.Constants.newLine +
		                  "REVOKE ALL PRIVILEGES ON *.* FROM \"" + user + "\"@\"localhost\"" + ";" + fr.soleil.sgad.icons.Constants.newLine +
		                  "REVOKE GRANT OPTION ON *.* FROM \"" + user + "\"@\"localhost\"" + ";" + fr.soleil.sgad.icons.Constants.newLine +
		                  "REVOKE ALL PRIVILEGES ON *.* FROM \"" + user + "\"@\"%\"" + ";" + fr.soleil.sgad.icons.Constants.newLine +
		                  "REVOKE GRANT OPTION ON *.* FROM \"" + user + "\"@\"%\"" + ";" + fr.soleil.sgad.icons.Constants.newLine +
		                  "FLUSH PRIVILEGES ;" + fr.soleil.sgad.icons.Constants.newLine +
		                  "DELETE FROM `user` WHERE `User` = \"" + user + "\";" + fr.soleil.sgad.icons.Constants.newLine);
	}*/

	private void grantsSpecific_gen(int schema , int role)
	{
		String schemaSt = fr.soleil.sgad.mysql.Constants.schema[ schema ];
		String user = schemaSt + fr.soleil.sgad.mysql.Constants.roles[ role ];
		grants_gen.append(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);
		switch ( schema )
		{
			case 0: // HDB
				switch ( role )
				{
					case 0: // manager
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' inserts record in the tables '" + fr.soleil.sgad.mysql.Constants.hdbObjects[ 0 ] + "' and '" + fr.soleil.sgad.mysql.Constants.hdbObjects[ 1 ] + "'." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' creates the tables related to the attributes." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' can make a select on every table of the database." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, CREATE ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, CREATE ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.hdbObjects[ 0 ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.hdbObjects[ 0 ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.hdbObjects[ 1 ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.hdbObjects[ 1 ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
						break;
					case 1: // archiver
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' inserts signals in the tables related to the attributes." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' can insert/update records in the table named " + "'" + fr.soleil.sgad.mysql.Constants.hdbObjects[ 2 ] + "'" + " while an archiving is triggered/stopped." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' can also delete records that are older than a certain amount of time (temporary archiving)." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT FILE ON *.* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT FILE ON *.* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, INSERT, UPDATE, DELETE ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, INSERT, UPDATE, DELETE ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
						break;
					case 2: // browser
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' can make a select on every table of the database." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);


						break;
				}
				break;
			case 1: // TDB
				switch ( role )
				{
					case 0: // manager
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' inserts record in the tables '" + fr.soleil.sgad.mysql.Constants.tdbObjects[ 0 ] + "' and '" + fr.soleil.sgad.mysql.Constants.tdbObjects[ 1 ] + "'." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' creates the tables related to the attributes." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' can make a select on every table of the database." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, CREATE ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, CREATE ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.tdbObjects[ 0 ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.tdbObjects[ 0 ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.tdbObjects[ 1 ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.tdbObjects[ 1 ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
						break;
					case 1: // archiver
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' inserts signals in the tables related to the attributes." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' can insert/update records in the table named " + "'" + fr.soleil.sgad.mysql.Constants.tdbObjects[ 2 ] + "'" + " while an archiving is triggered/stopped." + fr.soleil.sgad.Constants.newLine +
						                  "# The 'archiver' can also delete records that are older than a certain amount of time (temporary archiving)." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT FILE ON *.* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT FILE ON *.* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, INSERT, UPDATE, DELETE ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, INSERT, UPDATE, DELETE ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
						break;
					case 2: // browser
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' can make a select on every table of the database." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);


						break;
					case 3: // cleaner
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' must delete the old records in the attributes tables." + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, DELETE, DROP ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT, DELETE, DROP ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);


						break;
				}
				break;
			case 2: // SNAP
				switch ( role )
				{
					case 0: // manager
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' inserts, update record in the tables " +
						                  "'" + fr.soleil.sgad.mysql.Constants.snapObjects[ 0 ] + "'" + ", " + "'" + fr.soleil.sgad.mysql.Constants.snapObjects[ 1 ] + "'" + ", " +
						                  "'" + fr.soleil.sgad.mysql.Constants.snapObjects[ 2 ] + "'" + " and " + "'" + fr.soleil.sgad.mysql.Constants.snapObjects[ 3 ] + "'" + "." + fr.soleil.sgad.Constants.newLine +
						                  "# The '" + user + "' can make a select on every table of the database." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
						for ( int i = 0 ; i < 4 ; i++ )
						{
							grants_gen.append("GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ i ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine);
							grants_gen.append("GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ i ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);

						}
						grants_gen.append("GRANT UPDATE ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ 3 ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ 3 ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);

						break;
					case 1: // archiver
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' inserts record in the tables ");
						for ( int i = 2 ; i < fr.soleil.sgad.mysql.Constants.snapObjects.length - 1 ; i++ )
							grants_gen.append("'" + fr.soleil.sgad.mysql.Constants.snapObjects[ i ] + "'" + ", ");
						grants_gen.append(" and " + "'" + fr.soleil.sgad.mysql.Constants.snapObjects[ fr.soleil.sgad.mysql.Constants.snapObjects.length - 1 ] + "'" + "." + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("# The '" + user + "' can make a select on every table of the database." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
						for ( int i = 2 ; i < fr.soleil.sgad.mysql.Constants.snapObjects.length ; i++ )
						{
							grants_gen.append("GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ i ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine);
							grants_gen.append("GRANT INSERT ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ i ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
							grants_gen.append("GRANT UPDATE ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ i ] + " TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine);
							grants_gen.append("GRANT UPDATE ON " + schemaSt + "." + fr.soleil.sgad.mysql.Constants.snapObjects[ i ] + " TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);


						}
						break;
					case 2: // browser
						grants_gen.append(fr.soleil.sgad.Constants.newLine + "# The '" + user + "' can make a select on every table of the database." + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'localhost' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT USAGE ON " + schemaSt + ".* TO '" + user + "'@'%' IDENTIFIED BY '" + user + "';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'localhost';" + fr.soleil.sgad.Constants.newLine +
						                  "GRANT SELECT ON " + schemaSt + ".* TO '" + user + "'@'%';" + fr.soleil.sgad.Constants.newLine);
						break;
				}
				break;
		}
	}

	public void writeFile()
	{
		initFilePaths();
		initDirectories();

		writeSchemaFile();

		writeBatchFile();
	}

	private void initFilePaths()
	{
		System.out.println("Generator.initFilePaths");
		FilesPath[ 0 ] = _script_Path + File.separator + "createArchivingDB.sql";
		if (Constants.target_OS.compareTo("UNIX") == 0)	
				FilesPath[ 1 ] = _script_Path + File.separator + "createArchivingDB.sh";
		else 	FilesPath[ 1 ] = _script_Path + File.separator + "createArchivingDB.bat";
	}

	public void initDirectories()
	{
		System.out.println(fr.soleil.sgad.Constants.newLine + "Generator.initDirectories");
		String dirName = _script_Path;
		File file = new File(dirName);
		if ( !file.exists() )
		{
			System.out.println("Création du répertoire '" + dirName + "'");
			file.mkdirs();
		}
	}

	public void writeSchemaFile()
	{
		String fileName = FilesPath[ 0 ];
		try
		{
			FileWriter fileWriter;
			File file = new File(_script_Path);
			if ( !file.exists() )
				file.mkdirs();
			fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);

			printWriter.println(heading);
			printWriter.println(objects_gen);
			printWriter.println(grants_gen);
			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();

		}
		catch ( IOException e )
		{
			System.err.println("ERROR !! " + fr.soleil.sgad.Constants.newLine +
			                   "\t Origin : \t " + "Generator.writeSchemaFile" + fr.soleil.sgad.Constants.newLine +
			                   "\t Reason : \t " + "I/O_FAILURE" + fr.soleil.sgad.Constants.newLine +
			                   "\t Description : \t " + e.getMessage() + fr.soleil.sgad.Constants.newLine +
			                   "\t Précision : " +
			                   "\t\t FileName" + fileName + fr.soleil.sgad.Constants.newLine);
		}
	}

	public void writeBatchFile()
	{
		String fileName = FilesPath[ 1 ];
		try
		{
			FileWriter fileWriter;
			File file = new File(_script_Path);
			if ( !file.exists() )
				file.mkdirs();
			fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);

			StringBuffer stringBuffer = new StringBuffer("");
			
			if (Constants.target_OS.compareTo("UNIX") == 0)
			{
				stringBuffer.append("#!/bin/sh");
				stringBuffer.append("\n");
			}
			
			stringBuffer.append(fr.soleil.sgad.mysql.templates.Batch.getText(FilesPath[ 0 ]));

			printWriter.println(stringBuffer);

			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();

		}
		catch ( IOException e )
		{
			System.err.println("ERROR !! " + fr.soleil.sgad.Constants.newLine +
			                   "\t Origin : \t " + "Generator.writeBatchFile" + fr.soleil.sgad.Constants.newLine +
			                   "\t Reason : \t " + "I/O_FAILURE" + fr.soleil.sgad.Constants.newLine +
			                   "\t Description : \t " + e.getMessage() + fr.soleil.sgad.Constants.newLine +
			                   "\t Précision : " +
			                   "\t\t FileName" + fileName + fr.soleil.sgad.Constants.newLine);
		}
	}


}
