//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/HDB_TDB_Template.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  HDB_TDB_Template.
//						(Chinkumo Jean) - Nov 16, 2004
// $Author: chinkumo $
//
// $Revision: 1.9 $
//
// $Log: HDB_TDB_Template.java,v $
// Revision 1.9  2006/02/16 14:45:24  chinkumo
// The spectrums and images database table's fields were renamed.
// This was reported here.
//
// Revision 1.8  2006/02/15 11:22:13  chinkumo
// The mechanism (procedure) which import file from files to database (temporary archiving) was simplified.
//
// Revision 1.7  2005/11/29 18:25:49  chinkumo
// no message
//
// Revision 1.6  2005/08/19 14:03:33  chinkumo
// no message
//
// Revision 1.5.6.1  2005/08/01 15:39:16  chinkumo
// Correct the bug that appear at the end of the month while new partitions must be created.
// (months that do not have 30 days).
//
// Revision 1.5  2005/06/14 10:46:19  chinkumo
// Branch (sgad_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.4.4.2  2005/06/13 09:44:48  chinkumo
// Minor changes made to improve the partitionning management
//
// Revision 1.4.4.1  2005/04/21 19:19:38  chinkumo
// Errors handling improved for the following procedures : 
// 	ins_sc_ro_num, ins_sc_ro_str, ins_sc_wo_num, ins_sc_wo_str, ins_sc_rw_num, ins_sc_rw_str.
// Procedure 'CRTAB' modified to take into account the modes BASE, SPLIT and PARTITION.
//
// Revision 1.4  2005/03/09 09:47:39  chinkumo
// Index tablespaces were removed since indexes are now directly included inside  tables.
//
// Revision 1.3  2005/01/31 17:04:25  chinkumo
// A 'GRANT DELETE ' was added to tdbArchivers, to allow them to delete records on 'att_xxxx' named tables.
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

import fr.soleil.sgad.oracle.Constants;
import fr.soleil.sgad.oracle.Generator;

public class HDB_TDB_Template {

	public static void objects_gen(int schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t OBJETS "
						+ "(Schema "
						+ fr.soleil.sgad.oracle.Constants.schema[schema] + ")"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine);
		sequence_gen(schema);
		types_gen(schema);
		tables_gen(schema);
		index_gen(schema);
		procedure_gen(schema);
	}

	private static void sequence_gen(int schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t Sequence ID "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("" + "CREATE SEQUENCE "
				+ fr.soleil.sgad.oracle.Constants.schema[schema] + "." + "id"
				+ fr.soleil.sgad.Constants.newLine + "\t" + "INCREMENT BY 1"
				+ fr.soleil.sgad.Constants.newLine + "\t" + "START WITH 1"
				+ fr.soleil.sgad.Constants.newLine + "\t" + "MAXVALUE 99999"
				+ fr.soleil.sgad.Constants.newLine + "\t" + "CYCLE"
				+ fr.soleil.sgad.Constants.newLine + "\t" + "NOCACHE ;"
				+ fr.soleil.sgad.Constants.newLine);
	}

	private static void types_gen(int schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t Types "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("" +
				//
						"CREATE TYPE "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "tnum AS TABLE OF number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE TYPE "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "tstr AS TABLE OF varchar2(256);"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE TYPE "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "sp_num AS OBJECT (value Tnum,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "MEMBER FUNCTION vector(ind number) RETURN number);"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE TYPE BODY "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "sp_num AS"
						+ " MEMBER FUNCTION vector(ind number) RETURN number IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "IF value.Exists(ind) THEN RETURN value(ind);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE RETURN NULL;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END vector;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE TYPE "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "sp_str as OBJECT (value Tstr,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "MEMBER FUNCTION vector(ind number) RETURN VARCHAR2);"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE TYPE BODY "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "sp_str AS"
						+ " MEMBER FUNCTION vector(ind number) RETURN varchar2 IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "IF value.Exists(ind) THEN RETURN value(ind);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE RETURN NULL;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END vector;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE OR REPLACE type "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "type_op AS OBJECT ( name varchar2(30), description varchar2(256), value varchar2(30));"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE OR REPLACE type "
						+ fr.soleil.sgad.oracle.Constants.schema[schema] + "."
						+ "Vtype_op AS TABLE OF type_op;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void tables_gen(int schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t Tables "
						+ fr.soleil.sgad.Constants.newLine);
		tableADT_gen(schema);
		tableAPT_gen(schema);
		tableAMT_gen(schema);
		tableACT_gen(schema);
		tableRNT_gen(schema);
		tableProp_gen(schema);
	}

	private static void tableADT_gen(int schema) {
		String tableName = ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[0]
				: fr.soleil.sgad.oracle.Constants.tdbObjects[0]);
		String fullTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "." + tableName;

		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t "
						+ tableName + " Table "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE "
						+ fullTableName
						+ " ("
						+ fr.soleil.sgad.Constants.newLine
						+ "\t ID number(5) primary key USING INDEX TABLESPACE CONF_IND,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t time timestamp(3),"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t full_name varchar2(500) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t device varchar2(500) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t domain varchar2(500) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t family varchar2(500) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t member varchar2(500) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t att_name varchar2(500) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t data_type number(1) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t data_format number(1) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t writable number(1) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t max_dim_x number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t max_dim_y number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t levelg number(1),"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t facility varchar2(100) 	NOT NULL,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t archivable number(1),"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t substitute number(5))"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t TABLESPACE conf"
						+ fr.soleil.sgad.Constants.newLine + "\t PCTFREE 2"
						+ fr.soleil.sgad.Constants.newLine + "\t PCTUSED 95"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t STORAGE(INITIAL 1064960"
						+ fr.soleil.sgad.Constants.newLine + "\t\t NEXT 106496"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t PCTINCREASE 50);"
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void tableAPT_gen(int schema) {
		String tableName = ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[1]
				: fr.soleil.sgad.oracle.Constants.tdbObjects[1]);
		String fullTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "." + tableName;
		String fullRefTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "."
				+ ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[0]
						: fr.soleil.sgad.oracle.Constants.tdbObjects[0]);

		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t "
						+ tableName + " Table "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE "
				+ fullTableName + " (" + fr.soleil.sgad.Constants.newLine
				+ "\t ID constraint fk_apt_id references " + fullRefTableName
				+ "(id)," + fr.soleil.sgad.Constants.newLine
				+ "\t time timestamp(3)," + fr.soleil.sgad.Constants.newLine
				+ "\t description varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine + "\t label varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine + "\t unit varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t standard_unit varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t display_unit varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine + "\t format varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t min_value varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t max_value varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t min_alarm varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t max_alarm varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t optional_properties number)"
				+ fr.soleil.sgad.Constants.newLine + "\t TABLESPACE conf"
				+ fr.soleil.sgad.Constants.newLine + "\t PCTFREE 2"
				+ fr.soleil.sgad.Constants.newLine + "\t PCTUSED 95"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t STORAGE(INITIAL 1064960"
				+ fr.soleil.sgad.Constants.newLine + "\t\t NEXT 106496"
				+ fr.soleil.sgad.Constants.newLine + "\t\t PCTINCREASE 50);"
				+ fr.soleil.sgad.Constants.newLine);
	}

	private static void tableAMT_gen(int schema) {
		String tableName = ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
				: fr.soleil.sgad.oracle.Constants.tdbObjects[2]);
		String fullTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "." + tableName;
		String fullRefTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "."
				+ ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[0]
						: fr.soleil.sgad.oracle.Constants.tdbObjects[0]);

		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t "
						+ tableName + " Table "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE "
				+ fullTableName
				+ " ("
				+ fr.soleil.sgad.Constants.newLine
				+ "\t ID constraint fk_amt_id references "
				+ fullRefTableName
				+ "(id),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t archiver varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t start_date timestamp(3)		NOT NULL,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t stop_date timestamp(3),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t per_mod number(1) 		NOT NULL,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t per_per_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+

				"\t abs_mod number(1) 		NOT NULL,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t per_abs_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t dec_del_abs_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t gro_del_abs_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+

				"\t rel_mod number(1) 		NOT NULL,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t per_rel_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t n_percent_rel_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t p_percent_rel_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+

				"\t thr_mod number(1) 		NOT NULL,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t per_thr_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t min_val_thr_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t max_val_thr_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+

				"\t cal_mod number(1) 		NOT NULL,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t per_cal_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t val_cal_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t type_cal_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t algo_cal_mod varchar2(500),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t dif_mod number(1) 		NOT NULL,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t per_dif_mod number,"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t ext_mod number(1) NOT NULL"
				+
				// Specificités liées à lla TDB
				((1 == schema) ? ", " + fr.soleil.sgad.Constants.newLine
						+ "\t export_per number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t keeping_per number)"
						+ fr.soleil.sgad.Constants.newLine : ")"
						+ fr.soleil.sgad.Constants.newLine)
				+
				// --
				"\t TABLESPACE conf" + fr.soleil.sgad.Constants.newLine
				+ "\t PCTFREE 2" + fr.soleil.sgad.Constants.newLine
				+ "\t PCTUSED 95" + fr.soleil.sgad.Constants.newLine
				+ "\t STORAGE(INITIAL 417792"
				+ fr.soleil.sgad.Constants.newLine + "\t\t NEXT 41779"
				+ fr.soleil.sgad.Constants.newLine + "\t\t PCTINCREASE 50);"
				+ fr.soleil.sgad.Constants.newLine);
	}

	private static void tableACT_gen(int schema) {
		String tableName = ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[4]
				: fr.soleil.sgad.oracle.Constants.tdbObjects[4]);
		String fullTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "." + tableName;
		String fullRefTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "."
				+ ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[0]
						: fr.soleil.sgad.oracle.Constants.tdbObjects[0]);

		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t "
						+ tableName + " Table "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE "
				+ fullTableName + " (" + fr.soleil.sgad.Constants.newLine
				+ "ID constraint fk_act_id references " + fullRefTableName
				+ "(id)," + fr.soleil.sgad.Constants.newLine
				+ "\t nb_pt number," + fr.soleil.sgad.Constants.newLine
				+ "\t start_date timestamp(3),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t stop_date timestamp(3))"
				+ fr.soleil.sgad.Constants.newLine + "\t TABLESPACE conf;"
				+ fr.soleil.sgad.Constants.newLine);
	}

	private static void tableRNT_gen(int schema) {
		String tableName = ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[5]
				: fr.soleil.sgad.oracle.Constants.tdbObjects[5]);
		String fullTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "." + tableName;

		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t "
						+ tableName + " Table "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE "
				+ fullTableName + " (" + fr.soleil.sgad.Constants.newLine
				+ "\t name varchar2(500)," + fr.soleil.sgad.Constants.newLine
				+ "\t start_date timestamp(3),"
				+ fr.soleil.sgad.Constants.newLine
				+ "\t stop_date timestamp(3))"
				+ fr.soleil.sgad.Constants.newLine + "\t TABLESPACE conf;"
				+ fr.soleil.sgad.Constants.newLine);
	}

	private static void tableProp_gen(int schema) {
		String tableName = ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[3]
				: fr.soleil.sgad.oracle.Constants.tdbObjects[3]);
		String fullTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "." + tableName;

		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t "
						+ tableName + " Table "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE "
						+ fullTableName
						+ " ("
						+ fr.soleil.sgad.Constants.newLine
						+ "\t ID constraint fk_properties_id references "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[0]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[0])
						+ "(id)," + fr.soleil.sgad.Constants.newLine
						+ "\t name varchar2(500),"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t value varchar2(500))"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t TABLESPACE conf;"
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void index_gen(int schema) {
		String tableName = ((0 == schema) ? fr.soleil.sgad.oracle.Constants.hdbObjects[0]
				: fr.soleil.sgad.oracle.Constants.tdbObjects[0]);
		String fullTableName = fr.soleil.sgad.oracle.Constants.schema[schema]
				+ "." + tableName;

		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t Index "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE INDEX "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "ind_adtname on "
						+ fullTableName
						+ "(full_name)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t TABLESPACE CONF_IND PCTFREE 10  STORAGE(INITIAL 71680 NEXT 92160 PCTINCREASE 50 ) ;"
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE INDEX "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "ind_adtsignal ON "
						+ fullTableName
						+ "(att_name)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t TABLESPACE CONF_IND PCTFREE 10  STORAGE(INITIAL 71680 NEXT 92160 PCTINCREASE 50 ) ;"
						+ fr.soleil.sgad.Constants.newLine
						+
						//
						"CREATE INDEX "
						+ fr.soleil.sgad.oracle.Constants.schema[schema]
						+ "."
						+ "ind_adtdevice ON "
						+ fullTableName
						+ "(DEVICE)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t TABLESPACE CONF_IND PCTFREE 10  STORAGE(INITIAL 71680 NEXT 36864 PCTINCREASE 50 ) ;"
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_gen(int schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t Procedures "
						+ fr.soleil.sgad.Constants.newLine);
		if (0 == schema)
			procedureHDB_gen();
		if (1 == schema)
			procedureTDB_gen();
	}

	private static void procedureHDB_gen() {
		/*
		 * String tableName = ((0 == schema) ? Constants.hdbObjects[5] :
		 * Constants.tdbObjects[5]); String fullTableName = Constants.schema[0]
		 * + "." + tableName;
		 */
		String fullRefTableName0 = fr.soleil.sgad.oracle.Constants.schema[0]
				+ "." + fr.soleil.sgad.oracle.Constants.hdbObjects[0];
		String fullRefTableName1 = fr.soleil.sgad.oracle.Constants.schema[0]
				+ "." + fr.soleil.sgad.oracle.Constants.hdbObjects[1];
		String fullRefTableName2 = fr.soleil.sgad.oracle.Constants.schema[0]
				+ "." + fr.soleil.sgad.oracle.Constants.hdbObjects[2];
		String schema = fr.soleil.sgad.oracle.Constants.schema[0];

		fr.soleil.sgad.oracle.Generator
				.add_object("-- the procedure will insert a row (with timestamp and value) in the table corresponding to the attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- in parameter : attribut's name, timestamp, value"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ "."
						+ "ins_sc_ro_num (att varchar2, vartime timestamp, val number)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT id FROM "
						+ fullRefTableName0
						+ " WHERE full_name LIKE att;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "noatt exception;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "fetch C1 into var;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF var IS NULL THEN RAISE noatt;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "req :='INSERT INTO "
						+ schema
						+ ".att_' || var || ' VALUES (:1, :2)';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "execute immediate req using vartime, val;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN noatt THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20001, 'attribut ' || att || ' not referenced in "
						+ fullRefTableName0
						+ "');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN OTHERS THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF sqlcode = -01031 then"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "SELECT user INTO req FROM dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20002, req || ' is not allowed to insert in att_' || var);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSIF sqlcode = -00001 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20003, ' timestamp ' || vartime || ' in att_' || var || ' is already registered');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-21000, 'attribut(' || var || ') : ' || att || ' at time ' || vartime || ' has generated the following error : ' || sqlerrm);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("-- the procedure will insert a row (with timestamp and value) in the table corresponding to the attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- in parameter : attribut's name, timestamp, value"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ ".ins_sc_ro_str (att varchar2, vartime timestamp, val varchar2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT id FROM "
						+ fullRefTableName0
						+ " WHERE full_name LIKE att;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "noatt exception;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "FETCH C1 INTO var;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF var IS NULL THEN RAISE noatt;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "req :='INSERT INTO "
						+ schema
						+ ".att_' || var || ' VALUES (:1, :2)';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "execute immediate req using vartime, val;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN noatt THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20001, 'attribut ' || att || ' not referenced in "
						+ fullRefTableName0
						+ "');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ " WHEN OTHERS THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF sqlcode = -01031 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "SELECT user INTO req FROM dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20002, req || ' is not allowed to insert in att_' || var);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSIF sqlcode = -00001 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20003, ' timestamp ' || vartime || ' in att_' || var || ' is already registered');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-21000, 'attribut(' || var || ') : ' || att || ' at time ' || vartime || ' has generated the following error : ' || sqlerrm);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("-- the procedure will insert a row (with timestamp and value) in the table corresponding to the attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- in parameter : attribut's name, timestamp, value"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ ".ins_sc_wo_num (att varchar2, vartime timestamp, val number)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT id FROM "
						+ fullRefTableName0
						+ " WHERE full_name LIKE att;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "noatt exception;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "FETCH C1 INTO var;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF var IS NULL THEN RAISE noatt;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "req :='INSERT INTO "
						+ schema
						+ ".att_' || var || ' VALUES (:1, :2)';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "execute immediate req using vartime, val;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN noatt THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20001, 'attribut ' || att || ' not referenced in "
						+ fullRefTableName0
						+ "');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN OTHERS THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF sqlcode = -01031 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "SELECT user INTO req FROM dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20002, req || ' is not allowed to insert in att_' || var);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSIF sqlcode = -00001 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20003, ' timestamp ' || vartime || ' in att_' || var || ' is already registered');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-21000, 'attribut(' || var || ') : ' || att || ' at time ' || vartime || ' has generated the following error : ' || sqlerrm);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("-- the procedure will insert a row (with timestamp and value) in the table corresponding to the attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- in parameter : attribut's name, timestamp, value"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ ".ins_sc_wo_str (att varchar2, vartime timestamp, val varchar2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT id FROM "
						+ fullRefTableName0
						+ " WHERE full_name LIKE att;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "noatt exception;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "FETCH C1 INTO var;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF var IS NULL THEN RAISE noatt;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "req :='INSERT INTO "
						+ schema
						+ ".att_' || var || ' VALUES (:1, :2)';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "execute immediate req using vartime, val;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN noatt THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20001, 'attribut ' || att || ' not referenced in "
						+ fullRefTableName0
						+ "');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN OTHERS THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF sqlcode = -01031 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "SELECT user INTO req FROM dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20002, req || ' is not allowed to insert in att_' || var);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSIF sqlcode = -00001 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20003, ' timestamp ' || vartime || ' in att_' || var || ' is already registered');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-21000, 'attribut(' || var || ') : ' || att || ' at time ' || vartime || ' has generated the following error : ' || sqlerrm);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("-- the procedure will insert a row (with timestamp, read_value, write_value) in the table corresponding to the attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- in parameter : attribut's name, timestamp, read_value, write_value"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ ".ins_sc_rw_num (att varchar2, vartime timestamp, val number, val2 number)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT id FROM "
						+ fullRefTableName0
						+ " WHERE full_name LIKE att;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "noatt exception;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "FETCH C1 INTO var;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF var IS NULL THEN RAISE noatt;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "req :='INSERT INTO "
						+ schema
						+ ".att_' || var || ' VALUES (:1, :2, :3)';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "execute immediate req using vartime, val, val2;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN noatt THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20001, 'attribut ' || att || ' not referenced in "
						+ fullRefTableName0
						+ "');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN OTHERS THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF sqlcode = -01031 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "SELECT user INTO req FROM dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20002, req || ' is not allowed to insert in att_' || var);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSIF sqlcode = -00001 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20003, ' timestamp ' || vartime || ' in att_' || var || ' is already registered');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-21000, 'attribut(' || var || ') : ' || att || ' at time ' || vartime || ' has generated the following error : ' || sqlerrm);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("-- the procedure will insert a row (with timestamp, read_value, write_value) in the table corresponding to the attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- in parameter : attribut's name, timestamp, read_value, write_value"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ ".ins_sc_rw_str (att varchar2, vartime timestamp, val varchar2, val2 varchar2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT id FROM "
						+ fullRefTableName0
						+ " WHERE full_name LIKE att;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "noatt exception;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "FETCH C1 INTO var;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF var IS NULL THEN RAISE noatt;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "req :='INSERT INTO "
						+ schema
						+ ".att_' || var || ' VALUES (:1, :2, :3)';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "execute immediate req using vartime, val, val2;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN noatt THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20001, 'attribut ' || att || ' not referenced in "
						+ fullRefTableName0
						+ "');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHEN OTHERS THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF sqlcode = -01031 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "SELECT user INTO req FROM dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20002, req || ' is not allowed to insert in att_' || var);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSIF sqlcode = -00001 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-20003, ' timestamp ' || vartime || ' in att_' || var || ' is already registered');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "RAISE_APPLICATION_ERROR(-21000, 'attribut(' || var || ') : ' || att || ' at time ' || vartime || ' has generated the following error : ' || sqlerrm);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		// part crtab hdb
		procCRTAB(schema, fullRefTableName0, fullRefTableName1);

		fr.soleil.sgad.oracle.Generator
				.add_object("-- this procedure clean the "
						+ schema
						+ " schema: drop attribut's table and delete "
						+ fullRefTableName0
						+ ", "
						+ fullRefTableName1
						+ ", "
						+ fullRefTableName2
						+ " rows"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- no in parameter necessary"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ ".cleanHDB IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT table_name FROM user_tables WHERE table_name LIKE 'ATT%';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_tab user_tables.table_name%type;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DELETE FROM "
						+ fullRefTableName1
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DELETE FROM "
						+ fullRefTableName2
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DELETE FROM "
						+ fullRefTableName0
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'drop SEQUENCE "
						+ schema
						+ ".id' ;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'CREATE SEQUENCE "
						+ schema
						+ ".id INCREMENT BY 1 START WITH 1 MAXVALUE 99999 CYCLE NOCACHE';"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "open C1;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t" + "loop"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t"
						+ "fetch C1 into var_tab;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t"
						+ "EXIT WHEN C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t"
						+ "execute immediate 'drop table ' || var_tab;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "END LOOP;" + fr.soleil.sgad.Constants.newLine + "\t"
						+ "CLOSE C1;" + fr.soleil.sgad.Constants.newLine + "\t"
						+ "commit;" + fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine + "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE cre_user (varnom varchar2, mdp varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate 'create user ' || varnom || ' identified by ' || mdp || ' DEFAULT TABLESPACE CONF QUOTA UNLIMITED ON CONF QUOTA UNLIMITED ON CONF_IND QUOTA UNLIMITED ON SC_DATA QUOTA UNLIMITED ON SC_DATA_IND QUOTA UNLIMITED ON SP_DATA QUOTA UNLIMITED ON SP_DATA_IND QUOTA UNLIMITED ON IM_DATA QUOTA UNLIMITED ON IM_DATA_IND';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate 'GRANT CREATE SESSION TO ' || varnom;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate 'GRANT rarchiver TO ' || varnom;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedureTDB_gen() {
		String schema = fr.soleil.sgad.oracle.Constants.schema[1];
		String fullRefTableName0 = schema + "."
				+ fr.soleil.sgad.oracle.Constants.tdbObjects[0];
		String fullRefTableName1 = schema + "."
				+ fr.soleil.sgad.oracle.Constants.tdbObjects[1];
		String fullRefTableName2 = schema + "."
				+ fr.soleil.sgad.oracle.Constants.tdbObjects[2];

		fr.soleil.sgad.oracle.Generator
				.add_object("-- a file is creating for each attribut. the file's name contains table's name of attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- the procedure will OPEN the file and insert rows INTO attribut's table."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- this procedure is used for scalar/spectrums/image, read-only/write-only/read-write/read-with-write attribut."
						+ fr.soleil.sgad.Constants.newLine
						+ "-- in parameter : remotedir, file's name, attribut's name"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ "."
						+ "fast_file_import (emplacement varchar2, nom varchar2, att_name varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t file_handle UTL_FILE.FILE_TYPE;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t col1  NUMBER;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t getline VARCHAR2(32000);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t var_att varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t timest varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t var_value number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t file_handle := UTL_FILE.FOPEN(emplacement, nom,'R', 32767);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t <<INCREMENTATION>>"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t UTL_FILE.GET_LINE (file_handle, getline);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t timest := substr(getline, 1, length(getline)-1);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t EXIT INCREMENTATION when timest is null;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t req := 'insert INTO ' || att_name || ' values (' || timest || ''')';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t execute immediate req;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t COMMIT;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t UTL_FILE.FCLOSE(file_handle);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t WHEN NO_DATA_FOUND THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t UTL_FILE.FCLOSE(file_handle);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine);

		// part crtab tdb
		procCRTAB(schema, fullRefTableName0, fullRefTableName1);

		fr.soleil.sgad.oracle.Generator
				.add_object("-- this procedure clean the TDB schema: drop attribut's table and delete "
						+ fullRefTableName0
						+ ", "
						+ fullRefTableName1
						+ ", "
						+ fullRefTableName2
						+ " rows"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- no in parameter necessary"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ "."
						+ "cleanTDB IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t CURSOR C1 is select table_name from user_tables where table_name like 'ATT%';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t var_tab user_tables.table_name%type;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t DELETE FROM "
						+ fullRefTableName1
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t DELETE FROM "
						+ fullRefTableName2
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t DELETE FROM "
						+ fullRefTableName0
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t execute immediate 'drop SEQUENCE "
						+ schema
						+ "."
						+ "id' ;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t execute immediate 'CREATE SEQUENCE "
						+ schema
						+ "."
						+ "id INCREMENT BY 1 START WITH 1 MAXVALUE 99999 CYCLE NOCACHE';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t         fetch C1 INTO var_tab;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t         EXIT WHEN C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t         execute immediate 'drop table ' || var_tab;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t  COMMIT;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ ""
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE cre_user (varnom varchar2, mdp varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t execute immediate 'create user ' || varnom || ' identified by ' || mdp || ' DEFAULT TABLESPACE CONF QUOTA UNLIMITED ON CONF QUOTA UNLIMITED ON CONF_IND QUOTA UNLIMITED ON SC_DATA QUOTA UNLIMITED ON SC_DATA_IND QUOTA UNLIMITED ON SP_DATA QUOTA UNLIMITED ON SP_DATA_IND QUOTA UNLIMITED ON IM_DATA QUOTA UNLIMITED ON IM_DATA_IND';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t execute immediate 'GRANT CREATE SESSION TO ' || varnom;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t execute immediate 'GRANT rarchiver TO ' || varnom;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t COMMIT;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine);
	}

	public static void procCRTAB(String schema, String fullRefTableName0,
			String fullRefTableName1) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- the procedure will create attribut's table and feed "
						+ fullRefTableName0
						+ " table and "
						+ fullRefTableName1
						+ " table"
						+ fr.soleil.sgad.Constants.newLine
						+ "--in parameter : time, var_full_name, device, domain, family, member, att_name, data_type, data_format, writable,"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- max_dim_x, max_dim_y, level, cs, archivable, substitute, description, label, unit, standard_unit, display_unit,"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- format, min_val, max_val, min_alarm, max_alarm, property"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE "
						+ schema
						+ ".CRTAB ( time date,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_full_name varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "device varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "domain varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "family varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "member varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "att_name varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "data_type number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "data_format number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "writable number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "max_dim_x number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "max_dim_y number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "level number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "cs varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "archivable number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "substitute number,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "description varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "label varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "unit varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "standard_unit varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "display_unit varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "format varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "min_val varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "max_val varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "min_alarm varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "max_alarm varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "property varchar2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "seq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(512);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_id number;" + fr.soleil.sgad.Constants.newLine);
		if (2 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			fr.soleil.sgad.oracle.Generator.add_object("\t"
					+ "tbs varchar2(555);" + fr.soleil.sgad.Constants.newLine
					+ "\t" + "tbs1 varchar2(555);"
					+ fr.soleil.sgad.Constants.newLine + "\t"
					+ "vdate varchar2(555);" + fr.soleil.sgad.Constants.newLine
					+ "\t" + "vdate1 varchar2(555);"
					+ fr.soleil.sgad.Constants.newLine);
		} else {
			fr.soleil.sgad.oracle.Generator.add_object("\t"
					+ "TBSname varchar2(555);"
					+ fr.soleil.sgad.Constants.newLine);
		}
		fr.soleil.sgad.oracle.Generator.add_object("BEGIN"
				+ fr.soleil.sgad.Constants.newLine
				+ fr.soleil.sgad.Constants.newLine + "\t"
				+ "SELECT COUNT(ID) INTO var_id FROM " + fullRefTableName0
				+ " WHERE full_name = var_full_name;"
				+ fr.soleil.sgad.Constants.newLine + "\t"
				+ "IF var_id > 0 THEN" + fr.soleil.sgad.Constants.newLine
				+ "\t\t" + "select ID into var_id from " + fullRefTableName0
				+ " where full_name = var_full_name;"
				+ fr.soleil.sgad.Constants.newLine + "\t" + "ELSE"
				+ fr.soleil.sgad.Constants.newLine);

		if (1 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			fr.soleil.sgad.oracle.Generator.add_object("\t\t"
					+ "TBSname := 'SC_' || replace(domain, '-', '');"
					+ fr.soleil.sgad.Constants.newLine);
		} else if (2 != fr.soleil.sgad.oracle.Constants.generationMode
				|| schema.equals(fr.soleil.sgad.oracle.Constants.schema[1])) {
			fr.soleil.sgad.oracle.Generator.add_object("\t\t"
					+ "TBSname := 'SC_DATA';"
					+ fr.soleil.sgad.Constants.newLine);
		}

		fr.soleil.sgad.oracle.Generator
				.add_object("\t\t"
						+ "INSERT INTO "
						+ fullRefTableName0
						+ " VALUES ("
						+ schema
						+ ".id.nextval, time, var_full_name, device, domain, family, member, att_name, data_type, data_format, writable, max_dim_x, max_dim_y, level, cs, archivable, substitute);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "INSERT INTO "
						+ fullRefTableName1
						+ " VALUES ("
						+ schema
						+ ".id.currval, time, description, label, unit, standard_unit, display_unit, format, min_val, max_val, min_alarm, max_alarm, NULL);"
						+ fr.soleil.sgad.Constants.newLine + "\t\t" + "SELECT "
						+ schema + ".id.currval INTO seq from dual;"
						+ fr.soleil.sgad.Constants.newLine);

		if (2 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			fr.soleil.sgad.oracle.Generator
					.add_object("\t\t"
							+ "select 'm' || to_char(sysdate, 'MM') || 'y' || to_char(sysdate, 'YY'), 	'm' || to_char(ADD_MONTHS(sysdate,1), 'MM') || 'y' || to_char(sysdate, 'YY'), TO_CHAR(LAST_DAY(sysdate), 'DD/MM/YYYY') || ' 23:59:59',	TO_CHAR(LAST_DAY(ADD_MONTHS(sysdate,1)), 'DD/MM/YYYY') || ' 23:59:59' into tbs, tbs1, vdate, vdate1 from dual;"
							+ fr.soleil.sgad.Constants.newLine);
		}
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t"
						+ "si String (data_type=8)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "si scalar (data_format=0)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "si read -> sc_r_st (writable=0 ou 2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "sinon   -> sc_rw_st (writable=1 ou 3)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "si spectrum"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "si read -> sp_r_st (writable=0 ou 2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "sinon   -> sp_rw_st (writable=1 ou 3)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "si matrice"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "si read -> im_r_st (writable=0 ou 2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "sinon   -> im_rw_st (writable=1 ou 3)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t"
						+ "sinon"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "si scalar"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "si read -> sc_r_n (writable=0 ou 2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t\t"
						+ "sinon   -> sc_rw_n (writable=1 ou 3)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t"
						+ "si spectrum"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "si read -> sp_r_n (writable=0 ou 2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "sinon   -> sp_rw_n (writable=1 ou 3)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t"
						+ "si matrice"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "si read -> im_r_n (writable=0 ou 2)"
						+ fr.soleil.sgad.Constants.newLine
						+ "--"
						+ "\t\t\t\t\t"
						+ "sinon   -> im_rw_n (writable=1 ou 3)"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "IF data_type = 8 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF data_format = 0 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+
						// -- si attribut = string / scalar / read
						"\t\t\t\t"
						+ "IF writable in (0,2) THEN --          si attribut = string / scalar / read"
						+ fr.soleil.sgad.Constants.newLine);
		if (2 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), value varchar2(256), CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX pctfree 0 PARTITION BY RANGE (time) ';"
							+ fr.soleil.sgad.Constants.newLine);
		} else {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), value varchar2(256), CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX TABLESPACE \"' || TBSname || '\" pctfree 0 STORAGE (INITIAL 2867200 NEXT 163840) "
							+ ((schema
									.equals(fr.soleil.sgad.oracle.Constants.schema[1])) ? "NOLOGGING"
									: "") + "';"
							+ fr.soleil.sgad.Constants.newLine);
		}
		// -- si attribut = string / scalar / read-write
		Generator.add_object("\t\t\t\t"
				+ "ELSE --          si attribut = string / scalar / read-write"
				+ fr.soleil.sgad.Constants.newLine);
		if (2 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), read_value varchar2(256), write_value varchar2(256), CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX pctfree 0 PARTITION BY RANGE (time) ';"
							+ fr.soleil.sgad.Constants.newLine);
		} else {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), read_value varchar2(256), write_value varchar2(256), CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX TABLESPACE \"' || TBSname || '\" pctfree 0 STORAGE (INITIAL 2867200 NEXT 163840) "
							+ ((schema
									.equals(fr.soleil.sgad.oracle.Constants.schema[1])) ? "NOLOGGING"
									: "") + "';"
							+ fr.soleil.sgad.Constants.newLine);
		}
		Generator
				.add_object("\t\t\t\t" + "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSIF data_format = 1 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+
						// -- si attribut = string / spectrum / read
						"\t\t\t\t"
						+ "IF writable in (0,2) THEN  -- String / spectrum / read"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, value sp_str) TABLESPACE SP_DATA pctfree 2 pctused 95 NESTED TABLE value.value STORE AS tab_' || seq;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+
						// -- si attribut = string / spectrum / read-write
						"\t\t\t\t"
						+ "ELSE  --   ||          si attribut = String / spectrum / read-write           ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, read_value sp_str, write_value sp_str) TABLESPACE SP_DATA pctfree 2 pctused 95 NESTED TABLE read_value.value STORE AS tab_' || seq || 'r, NESTED TABLE write_value.value STORE AS tab_' || seq || 'w';"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSIF data_format = 2 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+
						// -- si attribut = string / image / read
						"\t\t\t\t"
						+ "IF writable in (0,2) THEN                --          si attribut = String / image / read"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, dim_y number, value BFILE) TABLESPACE IM_DATA pctfree 2 pctused 95 NESTED TABLE value.value STORE AS tab_' || seq;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+
						// -- si attribut = string / image / read-write
						"\t\t\t\t"
						+ "ELSE                   --          si attribut = String / image / read-write"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, dim_y number, read_value BFILE) TABLESPACE IM_DATA pctfree 2 pctused 95 NESTED TABLE read_value.value STORE AS tab_' || seq || 'r, NESTED TABLE write_value.value STORE AS tab_' || seq || 'w';"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "ELSE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF data_format = 0 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+
						// -- si attribut = number / scalar / read
						"\t\t\t\t"
						+ "IF writable in (0,2) THEN                --          si attribut = number / scalar / read"
						+ fr.soleil.sgad.Constants.newLine);
		// si attribut = number / scalar / read
		if (2 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), value number, CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX pctfree 0 PARTITION BY RANGE (time) ';"
							+ fr.soleil.sgad.Constants.newLine);
		} else {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), value number, CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX TABLESPACE \"' || TBSname || '\" pctfree 0 STORAGE (INITIAL 2867200 NEXT 163840) "
							+ ((schema.equals(Constants.schema[1])) ? "NOLOGGING"
									: "") + "';"
							+ fr.soleil.sgad.Constants.newLine);
		}

		fr.soleil.sgad.oracle.Generator
				.add_object("\t\t\t\t"
						+ "ELSE                   --          si attribut = number / scalar / read-write"
						+ fr.soleil.sgad.Constants.newLine);
		// si attribut = number / scalar / read-write
		if (2 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), read_value number, write_value number, CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX pctfree 0 PARTITION BY RANGE (time) ';"
							+ fr.soleil.sgad.Constants.newLine);
		} else {
			Generator
					.add_object("\t\t\t\t\t"
							+ "req := 'CREATE TABLE "
							+ schema
							+ ".ATT_' || seq || ' (time timestamp(3), read_value number, write_value number, CONSTRAINT pkatt' || seq || ' PRIMARY KEY(time)) ORGANIZATION INDEX TABLESPACE \"' || TBSname || '\" pctfree 0 STORAGE (INITIAL 2867200 NEXT 163840) "
							+ ((schema
									.equals(fr.soleil.sgad.oracle.Constants.schema[1])) ? "NOLOGGING"
									: "") + "';"
							+ fr.soleil.sgad.Constants.newLine);
		}
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSIF data_format = 1 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF writable in (0,2) THEN                --          si attribut = number / spectrum / read"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, value sp_num) TABLESPACE SP_DATA pctfree 2 pctused 95 NESTED TABLE value.value STORE AS tab_' || seq;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE                   --          si attribut = number / spectrum / read-write"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, read_value sp_num, write_value sp_num) TABLESPACE SP_DATA pctfree 2 pctused 95 NESTED TABLE read_value.value STORE AS tab_' || seq || 'r, NESTED TABLE write_value.value STORE AS tab_' || seq || 'w';"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "ELSIF data_format = 2 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "IF writable in (0,2) THEN                --          si attribut = number / matrice / read"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, dim_y number, value BFILE) TABLESPACE IM_DATA pctfree 2 pctused 95';"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "ELSE                   --          si attribut = number / matrice / read-write"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t"
						+ "req := 'CREATE TABLE "
						+ schema
						+ ".ATT_' || seq || ' (time timestamp(3), dim_x number, dim_y number, read_value BFILE, write_value BFILE) TABLESPACE IM_DATA pctfree 2 pctused 95';"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t"
						+ "END IF;" + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t" + "END IF;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t" + "END IF;"
						+ fr.soleil.sgad.Constants.newLine);
		if (2 == Constants.generationMode
				&& schema.equals(fr.soleil.sgad.oracle.Constants.schema[0])) {
			fr.soleil.sgad.oracle.Generator
					.add_object("\t\t"
							+ "IF data_format = 0 THEN"
							+ fr.soleil.sgad.Constants.newLine
							+ "\t\t\t"
							+ "req := req || '( PARTITION ' || tbs || ' VALUES LESS THAN (to_date(''' || vdate || ''', ''DD/MM/YYYY HH24:MI:SS'')) tablespace ' || tbs || ', PARTITION ' || tbs1 || ' VALUES LESS THAN (to_date(''' || vdate1 || ''', ''DD/MM/YYYY HH24:MI:SS'')) tablespace ' || tbs || ')';"
							+ fr.soleil.sgad.Constants.newLine + "\t\t"
							+ "END IF;" + fr.soleil.sgad.Constants.newLine);
		}
		fr.soleil.sgad.oracle.Generator
				.add_object("\t\t" + "EXECUTE IMMEDIATE req;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXECUTE IMMEDIATE 'GRANT SELECT ON "
						+ schema
						+ ".ATT_' || seq || ' TO "
						+ fr.soleil.sgad.oracle.Constants.roles_prefix
						+ schema
						+ fr.soleil.sgad.oracle.Constants.roles[2]
						+ "';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXECUTE IMMEDIATE 'GRANT SELECT ON "
						+ schema
						+ ".ATT_' || seq || ' TO "
						+ fr.soleil.sgad.oracle.Constants.roles_prefix
						+ schema
						+ fr.soleil.sgad.oracle.Constants.roles[0]
						+ "';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXECUTE IMMEDIATE 'GRANT INSERT ON "
						+ schema
						+ ".ATT_' || seq || ' TO "
						+ fr.soleil.sgad.oracle.Constants.roles_prefix
						+ schema
						+ fr.soleil.sgad.oracle.Constants.roles[1]
						+ "';"
						+ fr.soleil.sgad.Constants.newLine
						+ ((fr.soleil.sgad.oracle.Constants.schema[1] == schema) ? "\t\t"
								+ "EXECUTE IMMEDIATE 'GRANT DELETE ON "
								+ schema
								+ ".ATT_' || seq || ' TO "
								+ fr.soleil.sgad.oracle.Constants.roles_prefix
								+ schema
								+ fr.soleil.sgad.oracle.Constants.roles[1]
								+ "';" + fr.soleil.sgad.Constants.newLine
								: "") + "\t" + "END IF;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "commit;"
						+ fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

}
