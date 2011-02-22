//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/SNAP_Template.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  SNAP_Template.
//						(Chinkumo Jean) - Nov 16, 2004
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: SNAP_Template.java,v $
// Revision 1.7  2006/11/29 10:10:13  ounsy
// minor changes
//
// Revision 1.6  2006/02/17 09:32:50  chinkumo
// Since the structure and the name of some SNAPSHOT database's table changed, this was reported here.
//
// Revision 1.5  2005/11/29 18:25:48  chinkumo
// no message
//
// Revision 1.4  2005/08/19 14:03:33  chinkumo
// no message
//
// Revision 1.3.6.1  2005/08/04 07:53:55  chinkumo
// The 'comment' field of snap table was renamed into 'snap_comment' ('comment' is a reserved word on the Oracle side).
// Thi was reported on the both side (Oracle and MySQL). It was also reported on fr.soleil.sgad.icons.
//
// Revision 1.3  2005/06/14 10:46:19  chinkumo
// Branch (sgad_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.2.4.2  2005/04/29 19:07:35  chinkumo
// Snap tablespaces renamed.
// The bug into the snap.Register_snapshot function was fixed. (This bug was due to the 1.2.4.1 revision change)
//
// Revision 1.2.4.1  2005/04/21 19:26:47  chinkumo
// A new field 'comment' was added into the table 'snapshot' to give the possibility to add a comment to a recorded snapshot.
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


public class SNAP_Template
{
	public static void objects_gen()
	{
		fr.soleil.sgad.oracle.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t OBJETS " + "(Schema " + fr.soleil.sgad.oracle.Constants.schema[ 2 ] + ")" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine);

		sequence_gen();
		//types_gen();
		tables_gen();
		index_gen();
		vues_gen();
		procedure_gen();
	}

	private static void sequence_gen()
	{
		fr.soleil.sgad.oracle.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t Sequence ID " + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("CREATE SEQUENCE " + fr.soleil.sgad.oracle.Constants.schema[ 2 ] + "." + "id_snap" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "INCREMENT BY 1" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "START WITH 1" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "NOMAXVALUE" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "NOCACHE ;" + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE SEQUENCE " + fr.soleil.sgad.oracle.Constants.schema[ 2 ] + "." + "id_context" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "INCREMENT BY 1" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "START WITH 1" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "MAXVALUE 99999" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "CYCLE" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "NOCACHE ;" + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE SEQUENCE " + fr.soleil.sgad.oracle.Constants.schema[ 2 ] + "." + "id" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "INCREMENT BY 1" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "START WITH 1" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "MAXVALUE 99999" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "CYCLE" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "NOCACHE ;" + fr.soleil.sgad.Constants.newLine);

	}

	/*private static void types_gen()
	{

	}*/

	private static void tables_gen()
	{
		String schema = fr.soleil.sgad.oracle.Constants.schema[ 2 ];
		String fullRefTableName0 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 1 ];	//	context
		String fullRefTableName1 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 2 ];	//	list
		String fullRefTableName2 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 3 ];	//	snapshot
		String fullRefTableName3 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 0 ];	//	ast

		fr.soleil.sgad.oracle.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t Tables " + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + fullRefTableName0 + " (" + fr.soleil.sgad.Constants.newLine +
		                                 "\t ID_context number(5)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t time date," + fr.soleil.sgad.Constants.newLine +
		                                 "\t name varchar2(256)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t author varchar2(64)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t reason varchar2(640)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t description varchar2(1256));" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + fullRefTableName1 + "(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t ID_context number(5)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t ID_att number(5));" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + fullRefTableName2 + "(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t ID_snap number(5)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t ID_context number(5)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t time timestamp(3)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t snap_comment varchar2(2500));" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + fullRefTableName3 + " (" + fr.soleil.sgad.Constants.newLine +
		                                 "\t ID number(5)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t time date," + fr.soleil.sgad.Constants.newLine +
		                                 "\t full_name varchar2(80) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t device varchar2(160) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t domain varchar2(15) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t family varchar2(15) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t member varchar2(15) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t att_name varchar2(40) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t data_type number(1) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t data_format number(1) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t writable number(1) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t max_dim_x number," + fr.soleil.sgad.Constants.newLine +
		                                 "\t max_dim_y number," + fr.soleil.sgad.Constants.newLine +
		                                 "\t levelg number(1)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t facility varchar2(100) NOT NULL," + fr.soleil.sgad.Constants.newLine +
		                                 "\t archivable number(1)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t substitute number(5))" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE conf" + fr.soleil.sgad.Constants.newLine +
		                                 "\t PCTFREE 2" + fr.soleil.sgad.Constants.newLine +
		                                 "\t PCTUSED 95" + fr.soleil.sgad.Constants.newLine +
		                                 "\t STORAGE(INITIAL 1064960" + fr.soleil.sgad.Constants.newLine +
		                                 "\t 	NEXT 106496" + fr.soleil.sgad.Constants.newLine +
		                                 "\t 	PCTINCREASE 50);" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sc_ro_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t  id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value number)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t STORAGE ( INITIAL 40K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t NEXT 70K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t PCTINCREASE 50) " + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sc_wo_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value number)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t STORAGE ( INITIAL 40K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t NEXT 70K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t PCTINCREASE 50) " + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sc_rw_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t read_value number," + fr.soleil.sgad.Constants.newLine +
		                                 "\t write_value number)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t STORAGE ( INITIAL 40K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t NEXT 70K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t PCTINCREASE 50)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95 ;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sc_ro_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value varchar2(150))" + fr.soleil.sgad.Constants.newLine +
		                                 "\t STORAGE ( INITIAL 40K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t NEXT 70K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t PCTINCREASE 50)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sc_wo_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value varchar2(150))" + fr.soleil.sgad.Constants.newLine +
		                                 "\t STORAGE ( INITIAL 40K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t NEXT 70K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t PCTINCREASE 50)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sc_rw_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t read_value varchar2(150)," + fr.soleil.sgad.Constants.newLine +
		                                 "\t write_value varchar2(150))" + fr.soleil.sgad.Constants.newLine +
		                                 "\t STORAGE ( INITIAL 40K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t NEXT 70K " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t PCTINCREASE 50)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		//  SPECTRUM

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sp_ro_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number," + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value hdb.sp_num)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE value.value STORE AS tab_sp_ro_n;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sp_wo_num(	" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value hdb.sp_num)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE value.value STORE AS tab_sp_wo_n;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sp_rw_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t read_value HDB.sp_num," + fr.soleil.sgad.Constants.newLine +
		                                 "\t write_value HDB.sp_num)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE read_value.value STORE AS tab_sp_rw_nr," + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE write_value.value STORE AS tab_sp_rw_nw;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sp_ro_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value HDB.sp_str)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE value.value STORE AS tab_sp_ro_s;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sp_wo_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value HDB.sp_str)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE value.value STORE AS tab_sp_wo_s;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_sp_rw_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t read_value HDB.sp_str," + fr.soleil.sgad.Constants.newLine +
		                                 "\t write_value HDB.sp_str)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE read_value.value STORE AS tab_sp_rw_sr," + fr.soleil.sgad.Constants.newLine +
		                                 "\t NESTED TABLE write_value.value STORE AS tab_sp_rw_sw;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		// IMAGE
		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_im_ro_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value bfile)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_im_wo_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value bfile)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_im_rw_num(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t read_value bfile," + fr.soleil.sgad.Constants.newLine +
		                                 "\t write_value bfile)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_im_ro_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value bfile)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_im_wo_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t value bfile)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE TABLE " + schema + ".t_im_rw_str(" + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_snap number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t id_att number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t read_value bfile," + fr.soleil.sgad.Constants.newLine +
		                                 "\t write_value bfile)" + fr.soleil.sgad.Constants.newLine +
		                                 "\t TABLESPACE SNAP " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctfree 2 " + fr.soleil.sgad.Constants.newLine +
		                                 "\t pctused 95;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

	}

	private static void index_gen()
	{
		String schema = fr.soleil.sgad.oracle.Constants.schema[ 2 ];
		String fullRefTableName0 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 1 ];	//	context
		String fullRefTableName1 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 2 ];	//	list
		String fullRefTableName2 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 3 ];	//	snapshot
		String fullRefTableName3 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 0 ];	//	ast

		fr.soleil.sgad.oracle.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t Index et Containtes" + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("-- Ajout de contrainte d'intégrité" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_idcontext  ON " + fullRefTableName0 + "(id_context) 	TABLESPACE CONF_IND PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_idAST ON " + fullRefTableName3 + "(id) TABLESPACE CONF_IND PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_idsnapshot ON " + fullRefTableName2 + "(id_snap) 	TABLESPACE CONF_IND PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_idlist ON " + fullRefTableName1 + "(id_context) 		TABLESPACE CONF_IND PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + fullRefTableName0 + " ADD CONSTRAINT pk_context 	PRIMARY KEY (id_context);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + fullRefTableName3 + " ADD CONSTRAINT pk_ast 		PRIMARY KEY (id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + fullRefTableName2 + " 	ADD CONSTRAINT pk_snapshot PRIMARY KEY (id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + fullRefTableName1 + " ADD CONSTRAINT fk_idcontextfromlist 		FOREIGN KEY (id_context) 	REFERENCES " + fullRefTableName0 + "(id_context);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + fullRefTableName1 + " ADD CONSTRAINT fk_idattfromlist 				FOREIGN KEY (id_att) 		REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sc_ro_num ON " + schema + ".t_sc_ro_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sc_wo_num ON " + schema + ".t_sc_wo_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sc_rw_num ON " + schema + ".t_sc_rw_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sc_ro_str ON " + schema + ".t_sc_ro_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sc_wo_str ON " + schema + ".t_sc_wo_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sc_rw_str ON " + schema + ".t_sc_rw_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sp_ro_num ON " + schema + ".t_sp_ro_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sp_wo_num ON " + schema + ".t_sp_wo_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sp_rw_num ON " + schema + ".t_sp_rw_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sp_ro_str ON " + schema + ".t_sp_ro_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sp_wo_str ON " + schema + ".t_sp_wo_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_sp_rw_str ON " + schema + ".t_sp_rw_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_im_ro_num ON " + schema + ".t_im_ro_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_im_wo_num ON " + schema + ".t_im_wo_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_im_rw_num ON " + schema + ".t_im_rw_num(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_im_ro_str ON " + schema + ".t_im_ro_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_im_wo_str ON " + schema + ".t_im_wo_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE INDEX " + schema + ".ind_im_rw_str ON " + schema + ".t_im_rw_str(id_att) TABLESPACE SNAP PCTFREE 5 STORAGE ( INITIAL 40K NEXT 70K PCTINCREASE 50 );" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_ro_num ADD CONSTRAINT fksnap_sc_ro_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_wo_num ADD CONSTRAINT fksnap_sc_wo_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_rw_num ADD CONSTRAINT fksnap_sc_rw_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_ro_str ADD CONSTRAINT fksnap_sc_ro_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_wo_str ADD CONSTRAINT fksnap_sc_wo_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_rw_str ADD CONSTRAINT fksnap_sc_rw_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_ro_num ADD CONSTRAINT fksnap_sp_ro_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_wo_num ADD CONSTRAINT fksnap_sp_wo_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_rw_num ADD CONSTRAINT fksnap_sp_rw_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_ro_str ADD CONSTRAINT fksnap_sp_ro_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_wo_str ADD CONSTRAINT fksnap_sp_wo_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_rw_str ADD CONSTRAINT fksnap_sp_rw_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_ro_num ADD CONSTRAINT fksnap_im_ro_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_wo_num ADD CONSTRAINT fksnap_im_wo_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_rw_num ADD CONSTRAINT fksnap_im_rw_num_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_ro_str ADD CONSTRAINT fksnap_im_ro_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_wo_str ADD CONSTRAINT fksnap_im_wo_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_rw_str ADD CONSTRAINT fksnap_im_rw_str_snapid FOREIGN KEY (id_snap) REFERENCES " + fullRefTableName2 + "(id_snap);" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_ro_num ADD CONSTRAINT fksnap_sc_ro_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_wo_num ADD CONSTRAINT fksnap_sc_wo_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_rw_num ADD CONSTRAINT fksnap_sc_rw_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_ro_str ADD CONSTRAINT fksnap_sc_ro_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_wo_str ADD CONSTRAINT fksnap_sc_wo_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sc_rw_str ADD CONSTRAINT fksnap_sc_rw_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_ro_num ADD CONSTRAINT fksnap_sp_ro_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_wo_num ADD CONSTRAINT fksnap_sp_wo_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_rw_num ADD CONSTRAINT fksnap_sp_rw_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_ro_str ADD CONSTRAINT fksnap_sp_ro_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_wo_str ADD CONSTRAINT fksnap_sp_wo_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_sp_rw_str ADD CONSTRAINT fksnap_sp_rw_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_ro_num ADD CONSTRAINT fksnap_im_ro_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_wo_num ADD CONSTRAINT fksnap_im_wo_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_rw_num ADD CONSTRAINT fksnap_im_rw_num_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_ro_str ADD CONSTRAINT fksnap_im_ro_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_wo_str ADD CONSTRAINT fksnap_im_wo_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine +
		                                 "ALTER TABLE " + schema + ".t_im_rw_str ADD CONSTRAINT fksnap_im_rw_str_attid FOREIGN KEY (id_att)  REFERENCES " + fullRefTableName3 + "(id);" + fr.soleil.sgad.Constants.newLine);

	}

	private static void vues_gen()
	{
		String schema = fr.soleil.sgad.oracle.Constants.schema[ 2 ];
		String fullRefTableName0 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 1 ];	//	context
		String fullRefTableName1 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 2 ];	//	list
		//String fullRefTableName2 = schema + "." + fr.soleil.sgad.icons.oracle.Constants.snapObjects[ 3 ];	//	snapshot
		String fullRefTableName3 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 0 ];	//	ast

		String fullRefTableName0bis = fr.soleil.sgad.oracle.Constants.schema[ 0 ] + "." + fr.soleil.sgad.oracle.Constants.hdbObjects[ 0 ];

		fr.soleil.sgad.oracle.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t VUES" + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("-- vue pour la correspondance entre les attributs snapshotable et les attributs snapshoté" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE VIEW " + schema + ".acv AS " + fr.soleil.sgad.Constants.newLine +
		                                 "\t SELECT adt.full_name, adt.id HDB_id, ast.id SNAP_id " + fr.soleil.sgad.Constants.newLine +
		                                 "\t FROM " + fullRefTableName0bis + ", " + fullRefTableName3 + "" + fr.soleil.sgad.Constants.newLine +
		                                 "\t WHERE ast.full_name (+)= adt.full_name" + fr.soleil.sgad.Constants.newLine +
		                                 "\t UNION" + fr.soleil.sgad.Constants.newLine +
		                                 "\t SELECT ast.full_name, adt.id HDB_id, ast.id SNAP_id " + fr.soleil.sgad.Constants.newLine +
		                                 "\t FROM " + fullRefTableName0bis + ", " + fullRefTableName3 + fr.soleil.sgad.Constants.newLine +
		                                 "\t WHERE adt.full_name (+)= ast.full_name" + fr.soleil.sgad.Constants.newLine +
		                                 "\t AND ast.full_name not in (select full_name from " + fullRefTableName0bis + ");" + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("-- vue pour la liste d'attribut d'un context." + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE VIEW " + fullRefTableName1 + "Att AS " + fr.soleil.sgad.Constants.newLine +
		                                 "\t SELECT context.name, ast.full_name " + fr.soleil.sgad.Constants.newLine +
		                                 "\t FROM " + fullRefTableName3 + ", " + fullRefTableName1 + " , " + fullRefTableName0 + " " + fr.soleil.sgad.Constants.newLine +
		                                 "\t WHERE context.id_context= list.id_context " + fr.soleil.sgad.Constants.newLine +
		                                 "\t AND ast.id = list.id_att;" + fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_gen()
	{
		String schema = fr.soleil.sgad.oracle.Constants.schema[ 2 ];
		String fullRefTableName0 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 1 ];	//	context
		String fullRefTableName1 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 2 ];	//	list
		String fullRefTableName2 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 3 ];	//	snapshot
		String fullRefTableName3 = schema + "." + fr.soleil.sgad.oracle.Constants.snapObjects[ 0 ];	//	ast


		fr.soleil.sgad.oracle.Generator.add_object(fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.oracle.Constants.separator + fr.soleil.sgad.Constants.newLine + "--\t\t\t Procedures " + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("-- procedure pour l'insertion d'un scalaire" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE OR REPLACE PROCEDURE " + schema + ".sn_sc_ro_num(var_snap number, att varchar2, val number) IS" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "CURSOR C1 IS SELECT id FROM " + fullRefTableName3 + " WHERE full_name LIKE att;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "var number;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "req varchar2(150);" + fr.soleil.sgad.Constants.newLine +
		                                 "BEGIN    " + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "OPEN C1;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "fetch C1 into var;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "req := 'insert into " + schema + ".T_sc_ro_num values (:1, :2, :3)';" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "execute immediate req using var_snap, var, val;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "CLOSE C1;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "COMMIT;" + fr.soleil.sgad.Constants.newLine +
		                                 "END;" + fr.soleil.sgad.Constants.newLine +
		                                 "/" + fr.soleil.sgad.Constants.newLine +
		                                 "SHOW ERROR;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator.add_object("-- procedure pour l'enregistrement des attributs snapshoté" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE OR REPLACE PROCEDURE " + schema + ".RSA (" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vtime timestamp, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vfull_name varchar2, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vdevice varchar2, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vdomain varchar2, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vfamily varchar2," + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vmember varchar2, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vatt_name varchar2, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vdata_type number," + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vdata_format number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vwritable number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vmax_dim_x number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vmax_dim_y number," + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vlevel number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vfacility varchar2, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "varchivable number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vsubstitute number) IS" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "seq number;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "req varchar2(512);" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "var_id number;" + fr.soleil.sgad.Constants.newLine +
		                                 "BEGIN" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "SELECT COUNT(ID) INTO var_id FROM " + fullRefTableName3 + " WHERE full_name = vfull_name;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "IF var_id <> 0" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "THEN NULL;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "ELSE" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "INSERT INTO " + fullRefTableName3 + " VALUES (" + schema + ".id.nextval, to_date(substr(to_char(vtime), 1, 17), 'DD/MM/YY HH24:MI:SS'), vfull_name, vdevice, vdomain, vfamily, vmember, vatt_name, vdata_type, vdata_format, vwritable, vmax_dim_x, vmax_dim_y, vlevel, vfacility, varchivable, vsubstitute);" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "END IF;" + fr.soleil.sgad.Constants.newLine +
		                                 "END;" + fr.soleil.sgad.Constants.newLine +
		                                 "/" + fr.soleil.sgad.Constants.newLine +
		                                 "SHOW ERROR;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("-- Attributs From Context" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE OR REPLACE PROCEDURE " + schema + ".AFC ( " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "var_id_context number, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "var_att varchar2) IS" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "var number;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "req varchar2(150);" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "CURSOR C1 IS SELECT id FROM " + fullRefTableName3 + " WHERE full_name LIKE var_att;" + fr.soleil.sgad.Constants.newLine +
		                                 "BEGIN" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "OPEN C1;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "FETCH C1 INTO var;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "req := 'INSERT INTO " + fullRefTableName1 + " VALUES (:1, :2)';" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "execute immediate req using var_id_context, var;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "CLOSE C1;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "COMMIT;" + fr.soleil.sgad.Constants.newLine +
		                                 "END;" + fr.soleil.sgad.Constants.newLine +
		                                 "/" + fr.soleil.sgad.Constants.newLine +
		                                 "SHOW ERROR;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("-- register a new snapshot" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE OR REPLACE FUNCTION " + schema + ".Register_snapshot (vid_context number, time timestamp) RETURN NUMBER IS" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "var number;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "var_id number;" + fr.soleil.sgad.Constants.newLine +
		                                 "BEGIN" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "SELECT " + schema + ".id_snap.nextval INTO var_id FROM dual;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "INSERT INTO " + fullRefTableName2 + "(ID_snap, ID_context, time) VALUES (var_id, vid_context, time);" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "COMMIT;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "RETURN var_id;" + fr.soleil.sgad.Constants.newLine +
		                                 "END;" + fr.soleil.sgad.Constants.newLine +
		                                 "/" + fr.soleil.sgad.Constants.newLine +
		                                 "SHOW ERROR;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("-- register a new context" + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE OR REPLACE FUNCTION " + schema + ".Register_Context (" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vtime date, " + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vname varchar2," + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vauthor varchar2," + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vreason varchar2," + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vdescription varchar2) RETURN NUMBER IS" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "var_id number;" + fr.soleil.sgad.Constants.newLine +
		                                 "BEGIN" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "SELECT " + schema + ".id_context.nextval INTO var_id FROM dual;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "INSERT INTO " + fullRefTableName0 + " VALUES (var_id, vtime, vname, vauthor, vreason, vdescription);" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "COMMIT;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "RETURN var_id;" + fr.soleil.sgad.Constants.newLine +
		                                 "END;" + fr.soleil.sgad.Constants.newLine +
		                                 "/" + fr.soleil.sgad.Constants.newLine +
		                                 "SHOW ERROR;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("-- procedure pour créer un fichier de consigne" + fr.soleil.sgad.Constants.newLine +
		                                 "-- en parametre : ID SNAP  from " + fullRefTableName2 + " " + fr.soleil.sgad.Constants.newLine +
		                                 "CREATE OR REPLACE PROCEDURE " + schema + ".fichier_consigne (var_id_snap number, varloc varchar2, varfile varchar2) IS " + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "CURSOR C01 is select A.full_name as full_name, B.value as value FROM " + fullRefTableName3 + " A, " + schema + "." + "T_SC_RO_NUM" + " B WHERE A.id = B.id_att AND B.id_snap = var_id_snap;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "file_handle	UTL_FILE.FILE_TYPE;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "vartext		varchar2(1024);" + fr.soleil.sgad.Constants.newLine +
		                                 "BEGIN" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "-- ouvrir le fichier" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "file_handle := UTL_FILE.FOPEN(varloc, varfile,'W'); " + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "-- ecrire dans le fichier " + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "FOR vv IN C01 LOOP" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "vartext := vv.full_name || ';' || vv.value;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t\t" + "UTL_FILE.PUT_LINE(file_handle, vartext); " + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "END LOOP;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "-- Close the file. " + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "UTL_FILE.FCLOSE(file_handle); " + fr.soleil.sgad.Constants.newLine +
		                                 "END;" + fr.soleil.sgad.Constants.newLine +
		                                 "/" + fr.soleil.sgad.Constants.newLine +
		                                 "SHOW ERROR;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("CREATE OR REPLACE PROCEDURE " + schema + ".cleanSnap IS" + fr.soleil.sgad.Constants.newLine +
		                                 "BEGIN    " + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sc_ro_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sc_wo_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sc_rw_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sc_ro_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sc_wo_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sc_rw_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sp_ro_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sp_wo_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sp_rw_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sp_ro_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sp_wo_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_sp_rw_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_im_ro_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_im_wo_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_im_rw_num;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_im_ro_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_im_wo_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM t_im_rw_str;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM " + fullRefTableName1 + " ;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM " + fullRefTableName2 + " ;" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE FROM " + fullRefTableName0 + ";" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "DELETE " + fullRefTableName3 + ";" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "execute immediate 'drop sequence " + schema + ".id_snap';" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "execute immediate 'drop sequence " + schema + ".id_context';" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "execute immediate 'drop sequence " + schema + ".id';" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "execute immediate 'CREATE SEQUENCE " + schema + ".id_snap INCREMENT BY 1 START WITH 1 NOMAXVALUE NOCACHE';" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "execute immediate 'CREATE SEQUENCE " + schema + ".id_context INCREMENT BY 1 START WITH 1 MAXVALUE 999999 CYCLE NOCACHE';" + fr.soleil.sgad.Constants.newLine +
		                                 "\t" + "execute immediate 'CREATE SEQUENCE " + schema + ".id INCREMENT BY 1 START WITH 1 MAXVALUE 999999 CYCLE NOCACHE';" + fr.soleil.sgad.Constants.newLine +
		                                 "END;" + fr.soleil.sgad.Constants.newLine +
		                                 "/" + fr.soleil.sgad.Constants.newLine +
		                                 "SHOW ERROR;" + fr.soleil.sgad.Constants.newLine + fr.soleil.sgad.Constants.newLine);
	}

}
