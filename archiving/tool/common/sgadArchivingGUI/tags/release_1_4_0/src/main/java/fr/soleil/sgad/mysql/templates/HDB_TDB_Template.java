//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/mysql/templates/HDB_TDB_Template.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  HDB_TDB_Template.
//						(Chinkumo Jean) - Dec 4, 2004
// $Author: chinkumo $
//
// $Revision: 1.4 $
//
// $Log: HDB_TDB_Template.java,v $
// Revision 1.4  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.3  2005/02/01 18:50:39  chinkumo
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

package fr.soleil.sgad.mysql.templates;

public class HDB_TDB_Template
{
	public static void objects_gen(int schema)
	{
		fr.soleil.sgad.mysql.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine + "# \t\t\t OBJETS " + "(Schema " + fr.soleil.sgad.mysql.Constants.schema[ schema ] + ")" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine);
		tables_gen(schema);
	}

	private static void tables_gen(int schema)
	{
		String db = fr.soleil.sgad.mysql.Constants.schema[ schema ];
		fr.soleil.sgad.mysql.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine + "# \t\t\t Tables " + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.mysql.Generator.add_object("#" + fr.soleil.sgad.Constants.newLine +
		                                "# Database name: " + db + fr.soleil.sgad.Constants.newLine +
		                                "#" + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.mysql.Generator.add_object("DROP DATABASE IF EXISTS " + db + ";" + fr.soleil.sgad.Constants.newLine +
		                                "CREATE DATABASE " + db + ";" + fr.soleil.sgad.Constants.newLine +
		                                "USE " + db + ";" + fr.soleil.sgad.Constants.newLine);


		tableADT_gen(schema);
		tableAPT_gen(schema);
		tableAMT_gen(schema);
	}

	private static void tableADT_gen(int schema)
	{
		String table = ( 0 == schema ? fr.soleil.sgad.mysql.Constants.hdbObjects[ 0 ] : fr.soleil.sgad.mysql.Constants.tdbObjects[ 0 ] );

		fr.soleil.sgad.mysql.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine +
		                                "#" + fr.soleil.sgad.Constants.newLine +
		                                "# Table structure :  `" + table + "`" + fr.soleil.sgad.Constants.newLine +
		                                "#" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.mysql.Generator.add_object("CREATE TABLE `" + table + "` (" + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`ID` smallint(5) unsigned zerofill NOT NULL auto_increment," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`time` datetime default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`full_name` varchar(200) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`device` varchar(150) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`domain` varchar(35) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`family` varchar(35) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`member` varchar(35) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`att_name` varchar(50) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`data_type` tinyint(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`data_format` tinyint(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`writable` tinyint(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`max_dim_x` smallint(6) unsigned NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`max_dim_y` smallint(6) unsigned NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`levelg` tinyint(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`facility` varchar(45) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`archivable` tinyint(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`substitute` smallint(9) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "PRIMARY KEY  (`ID`,`full_name`)," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "UNIQUE KEY `ID_2` (`ID`)," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "UNIQUE KEY `full_name` (`full_name`)," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "KEY `ID` (`ID`)" + fr.soleil.sgad.Constants.newLine +
		                                ") TYPE=" + fr.soleil.sgad.mysql.Constants.storage_engine + " COMMENT='Attribute Definition Table' AUTO_INCREMENT=1 ;" + fr.soleil.sgad.Constants.newLine);
	}

	private static void tableAPT_gen(int schema)
	{
		String table = ( 0 == schema ? fr.soleil.sgad.mysql.Constants.hdbObjects[ 1 ] : fr.soleil.sgad.mysql.Constants.tdbObjects[ 1 ] );
		fr.soleil.sgad.mysql.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine +
		                                "#" + fr.soleil.sgad.Constants.newLine +
		                                "# Table structure :  `" + table + "`" + fr.soleil.sgad.Constants.newLine +
		                                "#" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.mysql.Generator.add_object("CREATE TABLE `" + table + "` (" + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`ID` int(5) unsigned zerofill NOT NULL default '00000'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`time` datetime default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`description` varchar(255) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`label` varchar(64) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`unit` varchar(64) NOT NULL default '1'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`standard_unit` varchar(64) NOT NULL default '1'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`display_unit` varchar(64) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`format` varchar(64) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`min_value` varchar(64) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`max_value` varchar(64) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`min_alarm` varchar(64) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`max_alarm` varchar(64) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "PRIMARY KEY  (`ID`)," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "UNIQUE KEY `ID` (`ID`)," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "KEY `att_ID` (`ID`)" + fr.soleil.sgad.Constants.newLine +
		                                ") TYPE=" + fr.soleil.sgad.mysql.Constants.storage_engine + " COMMENT='Attribute Properties table';" + fr.soleil.sgad.Constants.newLine);

	}

	private static void tableAMT_gen(int schema)
	{
		String table = ( 0 == schema ? fr.soleil.sgad.mysql.Constants.hdbObjects[ 2 ] : fr.soleil.sgad.mysql.Constants.tdbObjects[ 2 ] );
		fr.soleil.sgad.mysql.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.mysql.Constants.separator + fr.soleil.sgad.Constants.newLine +
		                                "#" + fr.soleil.sgad.Constants.newLine +
		                                "# Table structure :  `" + table + "`" + fr.soleil.sgad.Constants.newLine +
		                                "#" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.mysql.Generator.add_object("CREATE TABLE `" + table + "` (" + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`ID` smallint(5) unsigned zerofill NOT NULL default '00000'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`archiver` varchar(255) NOT NULL default ''," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`start_date` datetime default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`stop_date` datetime default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`per_mod` int(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`per_per_mod` int(5) default NULL," + fr.soleil.sgad.Constants.newLine +

		                                "\t" + "`abs_mod` int(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`per_abs_mod` int(5) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`dec_del_abs_mod` double default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`gro_del_abs_mod` double default NULL," + fr.soleil.sgad.Constants.newLine +

		                                "\t" + "`rel_mod` int(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`per_rel_mod` int(5) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`n_percent_rel_mod` double default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`p_percent_rel_mod` double default NULL," + fr.soleil.sgad.Constants.newLine +

		                                "\t" + "`thr_mod` int(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`per_thr_mod` int(5) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`min_val_thr_mod` double default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`max_val_thr_mod` double default NULL," + fr.soleil.sgad.Constants.newLine +

		                                "\t" + "`cal_mod` int(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`per_cal_mod` int(5) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`val_cal_mod` int(3) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`type_cal_mod` int(2) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`algo_cal_mod` varchar(20) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`dif_mod` int(1) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`per_dif_mod` int(5) default NULL," + fr.soleil.sgad.Constants.newLine +
		                                "\t" + "`ext_mod` int(1) NOT NULL default '0'" +
				// Specificités liées à lla TDB
		                                ( ( 1 == schema ) ?
		                                  ", " + fr.soleil.sgad.Constants.newLine +
		                                  "\t" + "`export_per` int(11) NOT NULL default '0'," + fr.soleil.sgad.Constants.newLine +
		                                  "\t" + "`keeping_per` int(11) NOT NULL default '0'" + fr.soleil.sgad.Constants.newLine :
		                                  fr.soleil.sgad.Constants.newLine ) +
				//--
		                                ") TYPE=" + fr.soleil.sgad.mysql.Constants.storage_engine + " COMMENT='Attribute Mode Table';" + fr.soleil.sgad.Constants.newLine);

	}
}
