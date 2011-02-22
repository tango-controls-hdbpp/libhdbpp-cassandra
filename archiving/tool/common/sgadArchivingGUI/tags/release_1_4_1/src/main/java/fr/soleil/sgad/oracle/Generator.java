//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/Generator.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Generator.
//						(Chinkumo Jean) - Nov 16, 2004
// $Author: chinkumo $
//
// $Revision: 1.7 $
//
// $Log: Generator.java,v $
// Revision 1.7  2006/02/17 09:32:50  chinkumo
// Since the structure and the name of some SNAPSHOT database's table changed, this was reported here.
//
// Revision 1.6  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.5  2005/08/19 14:03:32  chinkumo
// no message
//
// Revision 1.4  2005/06/14 10:46:20  chinkumo
// Branch (sgad_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.3.4.1  2005/04/21 18:51:49  chinkumo
// Missing grants added to the archiver and the manager when SNAP schema.
// The managing of modes (BASE, SPLIT, PARTITION) was integrated.
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

package fr.soleil.sgad.oracle;

import java.io.*;

public class Generator
{
	/**
	 * Chemin où seront générés les scripts
	 */
	public static String _script_Path = "";

	/**
	 * Chemin désignant la variable ORACLE_BASE
	 */
	public static String _oracleBase_Path = "";
	/**
	 * Chemin désignant la variable ORACLE_HOME
	 */
	public static String _oracleHome_Path = "";
	/**
	 * Chemin désignant les REDO_LOG
	 */
	public static String _redo_path = "";

	/**
	 * Nom de la base
	 */
	public static String _oracle_sid = "hdb";
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

	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;

	private static StringBuffer heading = new StringBuffer();
	private static StringBuffer roles_gen = new StringBuffer();
	private static StringBuffer passiveUsers_gen = new StringBuffer();
	private static StringBuffer objects_gen = new StringBuffer();
	private static StringBuffer grants_gen = new StringBuffer();
	private static StringBuffer activeUsers_gen = new StringBuffer();
	private static StringBuffer user2role_gen = new StringBuffer();
	private static StringBuffer ending = new StringBuffer();
	public static String[] FilesPath = new String[ 6 ];
	public static String[] DirsPath0 = new String[ 3 ];
	public static String[] DirsPath1 = new String[ 9 ];

	public Generator()
	{
	}


	public Generator(boolean ui)
	{
		if ( !ui )
		{
			initialisation();
			printConfig();

			heading_generation();				// 5 a
			roles_generation();					//	5
			passiveUsers_generation();	//	6
			objects_gen();						//	7
			grants_gen();							//	8
			activeUsers_generation();		// 9
			user2role_generation(); 			//	10a
			ending_generation(); 				// 10 b
			//printFile();
			writeFile();
		}
	}

	public void generate()
	{
		printConfig();

		heading_generation();				// 5 a
		roles_generation();					//	5
		passiveUsers_generation();	//	6
		objects_gen();						//	7
		grants_gen();							//	8
		activeUsers_generation();		// 9
		user2role_generation(); 			//	10a
		ending_generation(); 				// 10 b
		//printFile();
		writeFile();

	}

	public void reinitialise()
	{
		heading = new StringBuffer();
		roles_gen = new StringBuffer();
		passiveUsers_gen = new StringBuffer();
		objects_gen = new StringBuffer();
		grants_gen = new StringBuffer();
		activeUsers_gen = new StringBuffer();
		user2role_gen = new StringBuffer();
		ending = new StringBuffer();
	}

	/**
	 * ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	 * PART 1: CONFIGURATION
	 * ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	 */
	public void initialisation()
	{
		inputStreamReader = new InputStreamReader(System.in);
		bufferedReader = new BufferedReader(inputStreamReader);
		try
		{
			query_oracle_sid();
			pathTuning();
			databaseTuning();
			bufferedReader.close();
			inputStreamReader.close();
		}
		catch ( IOException e )
		{
			System.err.println("ERROR !! " + fr.soleil.sgad.Constants.newLine +
			                   "\t Origin : \t " + "ScriptGenerator.query" + fr.soleil.sgad.Constants.newLine +
			                   "\t Reason : \t " + "I/O_FAILURE" + fr.soleil.sgad.Constants.newLine +
			                   "\t Description : \t " + e.getMessage());
		}
	}

	public void pathTuning() throws IOException
	{
		while ( _script_Path.equals("") )
		{
			System.out.println("Chemin de génération des fichiers ?");
			_script_Path = bufferedReader.readLine();
		}
		while ( _oracleBase_Path.equals("") )
		{
			System.out.println("Chemin ORACLE_BASE ?");
			_oracleBase_Path = bufferedReader.readLine();
		}
		while ( _oracleHome_Path.equals("") )
		{
			System.out.println("Chemin ORACLE_HOME ?");
			_oracleHome_Path = bufferedReader.readLine();
		}
		_redo_path = _oracleBase_Path + File.separator + "redo" + File.separator + _oracle_sid;
		/*while (_redo_path.equals("")) {
		  System.out.println("Chemin ORACLE_REDO ?");
		  _redo_path = bufferedReader.readLine();
		}*/
	}

	public void databaseTuning() throws IOException
	{
		for ( int i = 0 ; i < fr.soleil.sgad.oracle.Constants.schema.length ; i++ )
		{
			query_schema(i);
		}
	}

	private void query_oracle_sid() throws IOException
	{
		while ( _oracle_sid.equals("") )
		{
			System.out.println("SID ?");
			_oracle_sid = bufferedReader.readLine();
		}
	}

	private void query_schema(int i) throws IOException
	{
		System.out.println("Creation du schéma " + "'" + fr.soleil.sgad.oracle.Constants.schema[ i ] + "'" + " ?");
		String rep = bufferedReader.readLine();
		if ( rep.equalsIgnoreCase("y") || rep.equalsIgnoreCase("yes") ||
		     rep.equalsIgnoreCase("o") || rep.equalsIgnoreCase("oui") ||
		     rep.equalsIgnoreCase("1") )
		{
			switch ( i )
			{
				case 0:
					hdb_generation = true;
					break;
				case 1:
					tdb_generation = true;
					break;
				case 2:
					snap_generation = true;
					break;
			}
		}
	}

	public void printConfig()
	{
		System.out.println("Chemin des scripts : ");
		System.out.println("\t_script_Path = " + _script_Path);

		System.out.println("Chemins 'ORACLE' : ");
		System.out.println("\t_oracleBase_Path = " + _oracleBase_Path);
		System.out.println("\t_oracleHome_Path = " + _oracleHome_Path);
		System.out.println("\t_redo_path = " + _redo_path);

		System.out.println("Configuration : ");
		System.out.println("\t_oracle_sid = " + _oracle_sid);
		System.out.println("\thdb_generation = " + hdb_generation);
		System.out.println("\ttdb_generation = " + tdb_generation);
		System.out.println("\tsnap_generation = " + snap_generation);
	}

	/**
	 * ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	 * PART 2: GENERATION
	 * ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	 */
	private void heading_generation()
	{
		heading.append("CONNECT sys/change_on_install AS sysdba;");
	}

	private void ending_generation()
	{
		ending.append("EXIT;");
	}

	/**
	 * Création des roles
	 */
	private void roles_generation()
	{
		roles_gen.append(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t ROLES " + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine);
		for ( int i = 0 ; i < fr.soleil.sgad.oracle.Constants.roles.length ; i++ )
		{
			if ( hdb_generation )
				roles_gen.append("CREATE ROLE " + fr.soleil.sgad.oracle.Constants.roles_prefix + fr.soleil.sgad.oracle.Constants.schema[ 0 ] + fr.soleil.sgad.oracle.Constants.roles[ i ] + ";" + fr.soleil.sgad.Constants.newLine);
			if ( tdb_generation )
				roles_gen.append("CREATE ROLE " + fr.soleil.sgad.oracle.Constants.roles_prefix + fr.soleil.sgad.oracle.Constants.schema[ 1 ] + fr.soleil.sgad.oracle.Constants.roles[ i ] + ";" + fr.soleil.sgad.Constants.newLine);
			if ( snap_generation )
				roles_gen.append("CREATE ROLE " + fr.soleil.sgad.oracle.Constants.roles_prefix + fr.soleil.sgad.oracle.Constants.schema[ 2 ] + fr.soleil.sgad.oracle.Constants.roles[ i ] + ";" + fr.soleil.sgad.Constants.newLine);
		}
	}

	/**
	 * Création des utilisateurs passifs (et donc schémas)
	 */
	private void passiveUsers_generation()
	{
		passiveUsers_gen.append(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t SCHEMA" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine);

		passiveUsers_gen.append("CREATE USER " + "administrator" + " IDENTIFIED BY " + "admin" + " DEFAULT TABLESPACE " + "TOOLS" + " TEMPORARY TABLESPACE" + " temp" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT " + "dba" + " TO " + "administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT " + "sysdba" + " TO " + "administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT CONNECT TO " + "administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT SELECT on SYS.DBA_EXTENTS to administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT SELECT on SYS.DBA_TABLESPACES to administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT SELECT on SYS.DBA_DATA_FILES to administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT SELECT on SYS.DBA_FREE_SPACE to administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT SELECT on SYS.DBA_TAB_COLUMNS to administrator" + ";" + fr.soleil.sgad.Constants.newLine);
		passiveUsers_gen.append("GRANT ALL PRIVILEGES to administrator" + ";" + fr.soleil.sgad.Constants.newLine);

		if ( hdb_generation )
			passiveUsers_generation(0);
		if ( tdb_generation )
			passiveUsers_generation(1);
		if ( snap_generation )
			passiveUsers_generation(2);
	}

	private void objects_gen()
	{
		objects_gen.append("");

		if ( hdb_generation )
			fr.soleil.sgad.oracle.templates.HDB_TDB_Template.objects_gen(0);
		if ( tdb_generation )
			fr.soleil.sgad.oracle.templates.HDB_TDB_Template.objects_gen(1);
		if ( snap_generation )
			fr.soleil.sgad.oracle.templates.SNAP_Template.objects_gen();

		fr.soleil.sgad.oracle.templates.Administrator.objects_gen();
	}

	public static void add_object(String str)
	{
		objects_gen.append(str);
	}

	private void grants_gen()
	{
		grants_gen.append(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t PRIVILEGES " + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine);
		for ( int i = 0 ; i < fr.soleil.sgad.oracle.Constants.roles.length ; i++ )
		{
			if ( hdb_generation )
			{
				grantsCommon_gen(0 , i);
				grantsSpecific_gen(0 , i);
			}
			if ( tdb_generation )
			{
				grantsCommon_gen(1 , i);
				grantsSpecific_gen(1 , i);
			}
			if ( snap_generation )
			{
				grantsCommon_gen(2 , i);
				grantsSpecific_gen(2 , i);
			}
		}
	}

	private void grantsCommon_gen(int schema , int role)
	{
		String user = fr.soleil.sgad.oracle.Constants.roles_prefix + fr.soleil.sgad.oracle.Constants.schema[ schema ] + fr.soleil.sgad.oracle.Constants.roles[ role ];
		grants_gen.append("GRANT CREATE SESSION TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
		grants_gen.append("GRANT EXECUTE ANY TYPE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
	}

	private void grantsSpecific_gen(int schema , int role)
	{
		String user = fr.soleil.sgad.oracle.Constants.roles_prefix + fr.soleil.sgad.oracle.Constants.schema[ schema ] + fr.soleil.sgad.oracle.Constants.roles[ role ];
		switch ( schema )
		{
			case 0: // HDB
				switch ( role )
				{
					case 0: // manager
						grants_gen.append("GRANT CREATE ANY INDEX TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT CREATE ANY TABLE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT CREATE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT EXECUTE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 0 ] + " TO " + "public" + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 0 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 0 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						break;
					case 1: // archiver
						grants_gen.append("GRANT ALTER ANY INDEX TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT LOCK ANY TABLE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT EXECUTE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 4 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 4 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						break;
					case 2: // browser
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 0 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 4 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						break;
				}
				break;
			case 1: // TDB
				switch ( role )
				{
					case 0: // manager
						grants_gen.append("GRANT CREATE ANY INDEX TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT CREATE ANY TABLE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT CREATE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT EXECUTE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 0 ] + " TO " + "public" + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 0 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 0 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						break;
					case 1: // archiver
						grants_gen.append("GRANT ALTER ANY INDEX TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT LOCK ANY TABLE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT EXECUTE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT UPDATE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 4 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 4 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						break;
					case 2: // browser
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 0 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 1 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 2 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 3 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.tdbObjects[ 4 ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						break;
				}
				break;
			case 2: // SNAP
				switch ( role )
				{
					case 0: // manager
						grants_gen.append("GRANT CREATE ANY INDEX TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT CREATE ANY TABLE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT CREATE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						//----
						for ( int i = 0 ; i < fr.soleil.sgad.oracle.Constants.snapObjects.length ; i++ )
						{
							grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ i ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
							grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ i ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
							grants_gen.append("GRANT EXECUTE ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ i ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						}

						break;
					case 1: // archiver
						grants_gen.append("GRANT ALTER ANY INDEX TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT LOCK ANY TABLE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
						grants_gen.append("GRANT EXECUTE ANY PROCEDURE TO " + user + ";" + fr.soleil.sgad.Constants.newLine);
                        for ( int i = 0 ; i < 4 ; i++ )
	                        grants_gen.append("GRANT SELECT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ i ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						for ( int i = 4 ; i < fr.soleil.sgad.oracle.Constants.snapObjects.length ; i++ )
							grants_gen.append("GRANT INSERT ON " + fr.soleil.sgad.oracle.Constants.schema[ schema ] + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ i ] + " TO " + user + ";" + fr.soleil.sgad.Constants.newLine);

						break;
					case 2: // browser
						break;
				}
				break;
		}
	}

	private void passiveUsers_generation(int schema)
	{
		String user = "";
		String passU = "";
		switch ( schema )
		{
			case 0:
				user = fr.soleil.sgad.oracle.Constants.schema[ 0 ];
				break;
			case 1:
				user = fr.soleil.sgad.oracle.Constants.schema[ 1 ];
				break;
			case 2:
				user = fr.soleil.sgad.oracle.Constants.schema[ 2 ];
				break;
		}
		if ( schema < 2 )
		{
			passU = passU + fr.soleil.sgad.Constants.newLine +
			        "CREATE USER " + user + " IDENTIFIED BY " + user + " DEFAULT TABLESPACE " + "conf" + " TEMPORARY TABLESPACE " + "temp" + fr.soleil.sgad.Constants.newLine +
			        "\t" + "QUOTA UNLIMITED ON \"CONF\"" + fr.soleil.sgad.Constants.newLine +
			        "\t" + "QUOTA UNLIMITED ON \"CONF_IND\"" + fr.soleil.sgad.Constants.newLine +
			        "\t" + "QUOTA UNLIMITED ON \"TOOLS\"" + fr.soleil.sgad.Constants.newLine;
			if ( 1 == Constants.generationMode && 0 == schema )
			{
				for ( int k = 0 ; k < fr.soleil.sgad.oracle.Constants.domains.length ; k++ )
				{
					for ( int l = 1 ; l < fr.soleil.sgad.oracle.Constants.domainsNum[ k ] ; l++ )
					{
						passU = passU +
						        "\t" + "QUOTA UNLIMITED ON \"" + fr.soleil.sgad.oracle.Constants.types[ 0 ] + "_" +
						        fr.soleil.sgad.oracle.Constants.domains[ k ].replaceAll("-" , "") +
						        ( ( ( fr.soleil.sgad.oracle.Constants.domainsNum[ k ] > 2 ) && ( l < 10 ) ) ? "0" : "" ) +
						        ( ( ( fr.soleil.sgad.oracle.Constants.domainsNum[ k ] > 2 ) ) ? Integer.toString(l) : "" ) +
						        "\"" + fr.soleil.sgad.Constants.newLine;
					}
				}
			}
			else if ( 2 != fr.soleil.sgad.oracle.Constants.generationMode || 1 == schema )
			{
				passU = passU +
				        "\t" + "QUOTA UNLIMITED ON \"SC_DATA\"" + fr.soleil.sgad.Constants.newLine +
				        "\t" + "QUOTA UNLIMITED ON \"SC_DATA_IND\"" + fr.soleil.sgad.Constants.newLine +
				        "\t" + "QUOTA UNLIMITED ON \"SP_DATA\"" + fr.soleil.sgad.Constants.newLine +
				        "\t" + "QUOTA UNLIMITED ON \"SP_DATA_IND\"" + fr.soleil.sgad.Constants.newLine +
				        "\t" + "QUOTA UNLIMITED ON \"IM_DATA\"" + fr.soleil.sgad.Constants.newLine +
				        "\t" + "QUOTA UNLIMITED ON \"IM_DATA_IND\"" + fr.soleil.sgad.Constants.newLine;
			}


			passU = passU + ";" + fr.soleil.sgad.Constants.newLine;
		}
		else if ( schema == 2 )
		{
			passU = passU + fr.soleil.sgad.Constants.newLine +
			        "CREATE USER " + user + " IDENTIFIED BY " + user + " DEFAULT TABLESPACE " + fr.soleil.sgad.oracle.Constants.schema[ 2 ] + " TEMPORARY TABLESPACE " + "temp" + fr.soleil.sgad.Constants.newLine +
			        "\t" + "QUOTA UNLIMITED ON \"" + fr.soleil.sgad.oracle.Constants.schema[ 2 ].toUpperCase() + "\"" + ";" +
			        fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine;
		}
		passU = passU + "GRANT CONNECT TO " + user + ";" + fr.soleil.sgad.Constants.newLine;
		passU = passU + "GRANT CREATE TYPE TO " + user + ";" + fr.soleil.sgad.Constants.newLine;
		passU = passU + "GRANT RESOURCE TO " + user + ";" + fr.soleil.sgad.Constants.newLine;
		passU = passU + "GRANT ALL PRIVILEGES TO " + user + ";" + fr.soleil.sgad.Constants.newLine;
		passiveUsers_gen.append(passU);
	}

	private void activeUsers_generation()
	{
		activeUsers_gen.append(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t UILISATEURS ACTIFS" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine);
		if ( hdb_generation )
			activeUsers_generation(0);
		if ( tdb_generation )
			activeUsers_generation(1);
		if ( snap_generation )
			activeUsers_generation(2);
	}

	private void activeUsers_generation(int i)
	{
		String schema = "";
		String actiU = "";
		switch ( i )
		{
			case 0:
				schema = fr.soleil.sgad.oracle.Constants.schema[ 0 ];
				break;
			case 1:
				schema = fr.soleil.sgad.oracle.Constants.schema[ 1 ];
				break;
			case 2:
				schema = fr.soleil.sgad.oracle.Constants.schema[ 2 ];
				break;
		}
		for ( int j = 0 ; j < fr.soleil.sgad.oracle.Constants.activesUser.length ; j++ )
		{
			String user = schema + fr.soleil.sgad.oracle.Constants.activesUser[ j ];
			actiU = actiU + "CREATE USER " + user + " IDENTIFIED BY " + user + " DEFAULT TABLESPACE " + "conf" + " TEMPORARY TABLESPACE " + "temp" + ";" + fr.soleil.sgad.Constants.newLine;
		}
		activeUsers_gen.append(actiU);
	}

	private void user2role_generation()
	{
		user2role_gen.append(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t ASSOSSIATION DE ROLE AUX UILISATEURS ACTIFS" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine);
		for ( int i = 0 ; i < fr.soleil.sgad.oracle.Constants.roles.length ; i++ )
		{
			if ( hdb_generation )
			{
				user2role_generation(0 , i);
			}
			if ( tdb_generation )
			{
				user2role_generation(1 , i);
			}
			if ( snap_generation )
			{
				user2role_generation(2 , i);
			}
		}
	}

	private void user2role_generation(int schema , int role)
	{
		String my_role = fr.soleil.sgad.oracle.Constants.roles_prefix + fr.soleil.sgad.oracle.Constants.schema[ schema ] + fr.soleil.sgad.oracle.Constants.roles[ role ];
		String my_user = fr.soleil.sgad.oracle.Constants.schema[ schema ] + fr.soleil.sgad.oracle.Constants.roles[ role ];
		user2role_gen.append("GRANT " + my_role + " TO " + my_user + ";" + fr.soleil.sgad.Constants.newLine);
	}

	/**
	 * ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	 * PART 3: OUTPUT
	 * ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	 */
	public void printFile()
	{
		System.out.println("File  : " + fr.soleil.sgad.Constants.newLine +
		                   heading.toString() +
		                   roles_gen.toString() +
		                   passiveUsers_gen.toString() +
		                   objects_gen.toString() +
		                   grants_gen.toString() +
		                   activeUsers_gen.toString() +
		                   user2role_gen.toString() +
		                   ending.toString());
	}


	public void writeFile()
	{
		initFilePaths();
		initDirectories();

		writeInitOraFile();

		writeCreateDB();
		writeCreateTBS();
		writeCatalogues();
		writeSchemaFile();

		writeBatchFile();
	}

	public void writeInitOraFile()
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

			StringBuffer stringBuffer = new StringBuffer("");
			stringBuffer.append(fr.soleil.sgad.oracle.templates.InitOra.getText());

			printWriter.println(stringBuffer);

			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();

		}
		catch ( IOException e )
		{
			System.err.println("ERROR !! " + fr.soleil.sgad.Constants.newLine +
			                   "\t Origin : \t " + "Generator.writeInitOraFile" + fr.soleil.sgad.Constants.newLine +
			                   "\t Reason : \t " + "I/O_FAILURE" + fr.soleil.sgad.Constants.newLine +
			                   "\t Description : \t " + e.getMessage() + fr.soleil.sgad.Constants.newLine +
			                   "\t Précision : " +
			                   "\t\t FileName" + fileName + fr.soleil.sgad.Constants.newLine);
		}
	}

	public void writeCreateDB()
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
			stringBuffer.append(fr.soleil.sgad.oracle.templates.CreateDB.getText());

			printWriter.println(stringBuffer);

			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();

		}
		catch ( IOException e )
		{
			System.err.println("ERROR !! " + fr.soleil.sgad.Constants.newLine +
			                   "\t Origin : \t " + "Generator.writeCreateDB" + fr.soleil.sgad.Constants.newLine +
			                   "\t Reason : \t " + "I/O_FAILURE" + fr.soleil.sgad.Constants.newLine +
			                   "\t Description : \t " + e.getMessage() + fr.soleil.sgad.Constants.newLine +
			                   "\t Précision : " +
			                   "\t\t FileName : " + fileName + fr.soleil.sgad.Constants.newLine);
		}
	}

	public void writeCreateTBS()
	{
		String fileName = FilesPath[ 2 ];
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
			stringBuffer.append(fr.soleil.sgad.oracle.templates.CreateTBS.getText());

			printWriter.println(stringBuffer);

			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();

		}
		catch ( IOException e )
		{
			System.err.println("ERROR !! " + fr.soleil.sgad.Constants.newLine +
			                   "\t Origin : \t " + "Generator.writeCreateTBS" + fr.soleil.sgad.Constants.newLine +
			                   "\t Reason : \t " + "I/O_FAILURE" + fr.soleil.sgad.Constants.newLine +
			                   "\t Description : \t " + e.getMessage() + fr.soleil.sgad.Constants.newLine +
			                   "\t Précision : " +
			                   "\t\t FileName" + fileName + fr.soleil.sgad.Constants.newLine);
		}
	}

	public void writeCatalogues()
	{
		String fileName = FilesPath[ 3 ];
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
			stringBuffer.append(fr.soleil.sgad.oracle.templates.CreateDBCatalog.getText());

			printWriter.println(stringBuffer);

			printWriter.close();
			bufferedWriter.close();
			fileWriter.close();

		}
		catch ( IOException e )
		{
			System.err.println("ERROR !! " + fr.soleil.sgad.Constants.newLine +
			                   "\t Origin : \t " + "Generator.writeCatalogues" + fr.soleil.sgad.Constants.newLine +
			                   "\t Reason : \t " + "I/O_FAILURE" + fr.soleil.sgad.Constants.newLine +
			                   "\t Description : \t " + e.getMessage() + fr.soleil.sgad.Constants.newLine +
			                   "\t Précision : " +
			                   "\t\t FileName" + fileName + fr.soleil.sgad.Constants.newLine);
		}
	}

	public void writeSchemaFile()
	{
		String fileName = FilesPath[ 4 ];
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
			printWriter.println(roles_gen);
			printWriter.println(passiveUsers_gen);
			printWriter.println(objects_gen);
			printWriter.println(grants_gen);
			printWriter.println(activeUsers_gen);
			printWriter.println(user2role_gen);
			printWriter.println(ending);

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
		String fileName = FilesPath[ 5 ];
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
			stringBuffer.append(fr.soleil.sgad.oracle.templates.Batch.getText());

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

	private void initFilePaths()
	{
		System.out.println("Generator.initFilePaths");
		FilesPath[ 0 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid + File.separator + "pfile" + File.separator + "init.ora";
		FilesPath[ 1 ] = _script_Path + File.separator + "createDB.sql";
		FilesPath[ 2 ] = _script_Path + File.separator + "createTBS.sql";
		FilesPath[ 3 ] = _script_Path + File.separator + "createDBcatalog.sql";
		FilesPath[ 4 ] = _script_Path + File.separator + "createSchema.sql";
		FilesPath[ 5 ] = _script_Path + File.separator + "launch_all.bat";

		DirsPath0[ 0 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid;
		DirsPath0[ 1 ] = _oracleBase_Path + File.separator + "oradata" + File.separator + _oracle_sid;
		DirsPath0[ 2 ] = _redo_path;

		DirsPath1[ 0 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid + File.separator + "pfile";
		DirsPath1[ 1 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid + File.separator + "udump";
		DirsPath1[ 2 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid + File.separator + "bdump";
		DirsPath1[ 3 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid + File.separator + "cdump";
		DirsPath1[ 4 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid + File.separator + "create";
		DirsPath1[ 5 ] = _oracleBase_Path + File.separator + "admin" + File.separator + _oracle_sid + File.separator + "archive";
		DirsPath1[ 6 ] = _oracleBase_Path + File.separator + "oradata" + File.separator + _oracle_sid;
		DirsPath1[ 7 ] = _redo_path;
		DirsPath1[ 8 ] = _script_Path;

		System.out.println("Generator.initFilePaths : chemins... " + fr.soleil.sgad.Constants.newLine);
		for ( int i = 0 ; i < FilesPath.length ; i++ )
		{
			String s = FilesPath[ i ];
			System.out.println(i + " : " + s + fr.soleil.sgad.Constants.newLine);
		}
		for ( int i = 0 ; i < DirsPath0.length ; i++ )
		{
			String s = DirsPath0[ i ];
			System.out.println(i + " : " + s + fr.soleil.sgad.Constants.newLine);
		}
		for ( int i = 0 ; i < DirsPath1.length ; i++ )
		{
			String s = DirsPath1[ i ];
			System.out.println(i + " : " + s + fr.soleil.sgad.Constants.newLine);
		}

	}

	public void initDirectories()
	{
		System.out.println(fr.soleil.sgad.Constants.newLine +
		                   "Generator.initDirectories");
		for ( int i = 0 ; i < DirsPath0.length ; i++ )
		{
			String dirName = DirsPath0[ i ];
			File file = new File(dirName);
			if ( file.exists() )
			{
				System.out.println("Suppression du répertoire '" + dirName + "'");
				deleteDir(file);
			}
		}
		for ( int i = 0 ; i < DirsPath1.length ; i++ )
		{
			String dirName = DirsPath1[ i ];
			File file = new File(dirName);
			if ( !file.exists() )
			{
				System.out.println("Création du répertoire '" + dirName + "'");
				file.mkdirs();
			}
		}

	}

// Deletes all files and subdirectories under dir.
// Returns true if all deletions were successful.
// If a deletion fails, the method stops attempting to delete and returns false.
	public static boolean deleteDir(File dir)
	{
		if ( dir.isDirectory() )
		{
			String[] children = dir.list();
			for ( int i = 0 ; i < children.length ; i++ )
			{
				boolean success = deleteDir(new File(dir , children[ i ]));
				if ( !success )
				{
					return false;
				}
			}
		}
// The directory is now empty so delete it
		return dir.delete();
	}

	public static void main(String[] argin)
	{
		new Generator(false);
	}
}
