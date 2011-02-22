//+======================================================================
// $Source: /cvsroot/tango-cs/tango/tools/fr.soleil.sgad.icons/oracle/templates/Administrator.java,v $
//
// Project:      Tango Archiving Service
//
// Description:  Java source code for the class  Administrator.
//						(Chinkumo Jean) - Nov 19, 2004
//
// $Author: ounsy $
//
// $Revision: 1.7 $
//
// $Log: Administrator.java,v $
// Revision 1.7  2006/11/29 10:10:14  ounsy
// minor changes
//
// Revision 1.6  2005/11/29 18:25:48  chinkumo
// no message
//
// Revision 1.5  2005/08/19 14:03:32  chinkumo
// no message
//
// Revision 1.4.6.1  2005/08/01 15:39:15  chinkumo
// Correct the bug that appear at the end of the month while new partitions must be created.
// (months that do not have 30 days).
//
// Revision 1.4  2005/06/14 10:46:19  chinkumo
// Branch (sgad_1_0_1-branch_0)  and HEAD merged.
//
// Revision 1.3.4.3  2005/06/13 09:49:13  chinkumo
// Minor changes made to improve the monitoring of the database.
//
// Revision 1.3.4.2  2005/05/11 18:34:41  chinkumo
// Minor change in the procedure named 'feed_delay_hdb': "create table administrator" was replaced by "create temporary table administrator"
//
// Revision 1.3.4.1  2005/04/21 19:05:49  chinkumo
// The managing of modes (BASE, SPLIT, PARTITION) was integrated.
// Indentations (generated scripts) improved.
// Procedure administrator.feed_expanse improved.
// Procedure administrator.feed_delay_hdb improved.
// Procedure administrator.crtbs (partionning) added.
// Procedure administrator.addpart (partionning) added.
// 
// Procedure administrator.jobalive (administration only) added.
// Procedure administrator.jobalive (administration only) added.
// Procedure administrator.jobcharge (administration only) added.
// Procedure administrator.jobdelay (administration only) added.
// Procedure administrator.jobexpanse (administration only) added.
// Procedure administrator.jobtbs (administration only) added.
// Procedure administrator.joberror (administration only) added.
// Procedure administrator.jobSGR (administration only) added.
// Procedure administrator.removejob (administration only) added.
// Procedure administrator.changejob (administration only) added.
// Procedure administrator.CleanAll (administration only) added.
// Procedure administrator.jobalive (administration only) added.
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

import fr.soleil.sgad.oracle.Generator;

public class Administrator {
	public static void objects_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t PARTIE ADMIN"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine);

		sequence_gen();
		tables_gen();
		triggers_gen();
		index_gen();
		vues_gen();
		package_mail_gen();
		procedure_gen();
	}

	private static void sequence_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t Sequence ID "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE sequence administrator.tab_errors_seq"
						+ fr.soleil.sgad.Constants.newLine + "\t START WITH 1"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t INCREMENT BY 1"
						+ fr.soleil.sgad.Constants.newLine + "\t minvalue 1"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t maxvalue 50000"
						+ fr.soleil.sgad.Constants.newLine + "\t nocache"
						+ fr.soleil.sgad.Constants.newLine + "\t cycle;"
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE SEQUENCE ADMINISTRATOR.id_admin START WITH 1 INCREMENT BY 1 NOMAXVALUE;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void triggers_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t Triggers "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE TRIGGER administrator.tab_errors_trig"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t after servererror on database"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t declare id number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t SELECT administrator.tab_errors_seq.nextval INTO id FROM dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t FOR n IN 1..ora_server_error_depth LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+
						// "\t\t\t INSERT INTO administrator.tab_error VALUES (id, sysdate, ora_login_user, ora_server_error(n), ora_server_error_msg(n));"
						// + fr.soleil.sgad.icons.Constants.newLine +
						"\t\t\t INSERT INTO administrator.tab_error VALUES (id, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), ora_login_user, ora_server_error(n), ora_server_error_msg(n));"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t END tab_errors_trig;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE TRIGGER administrator.logins_trig AFTER LOGON ON DATABASE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t INSERT INTO administrator.logins VALUES(sysdate,ora_client_ip_address,ora_login_user);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void tables_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t Tables "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE administrator.tab_error ("
						+ fr.soleil.sgad.Constants.newLine + "\t id NUMBER,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t log_date DATE,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t log_usr VARCHAR2(30),"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t err_nr NUMBER(10),"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t err_msg   VARCHAR2(4000));"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE TABLE administrator.logins("
						+ fr.soleil.sgad.Constants.newLine
						+ "\t logintime date,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t ipaddr varchar2(20),"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t usr varchar2(20));"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE ADMINISTRATOR.EXPANSE(id number,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "id_att number," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "nb_lines number,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "nb_extents number,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "tabsize number);" + fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE ADMINISTRATOR.CHARGE(id number,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "id_att number," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "start_date date,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "stop_date date," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "nb_lines number);"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE ADMINISTRATOR.ALIVE (  id number,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "id_att number," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "maxtime date);"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE ADMINISTRATOR.DELAY(id number, "
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "id_att number," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "start_date date,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "stop_date date," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "avg_retard number);"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE TABLE ADMINISTRATOR.CONTROL( id number,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "time date," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "what number,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t\t\t"
						+ "nb_att number," + fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t" + "req varchar2(3000));"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("DECLARE"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "type tvTBS IS TABLE OF varchar2(22);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "v_TBS tvTBS;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(30000);"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "SELECT tablespace_name BULK COLLECT INTO v_TBS FROM dba_tablespaces WHERE contents != 'TEMPORARY';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IF SQL%ROWCOUNT > 0 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'CREATE TABLE administrator.TBSused (time timestamp(1), ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "FOR i IN v_tbs.first..v_tbs.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "req := req || v_tbs(i) || ' number ' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t"
						+ "IF i < v_tbs.last THEN"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t\t"
						+ "req := req || ', ';"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t"
						+ "END IF;" + fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "END LOOP;" + fr.soleil.sgad.Constants.newLine
						+ "\t\t" + "req := req || ')';"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "EXECUTE IMMEDIATE req;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "END IF;"
						+ fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void index_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t Index et Containtes"
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE index administrator.ind_TBSused ON administrator.TBSused(time);"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE index administrator.ind_login ON administrator.logins(logintime);"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE index administrator.ind_tab_error ON administrator.tab_error(id);"
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void vues_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine + "--\t\t\t VUES"
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE VIEW administrator.log_error AS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t SELECT id, to_date(log_date, 'DD/MM/YY HH24:MI:SS') AS log_date, log_usr, to_char(logintime, 'DD/MM/YY HH24:MI:SS') AS logintime, ipaddr, err_nr, err_msg"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t FROM administrator.tab_error, administrator.logins"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t WHERE logintime = ("
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t SELECT max(logintime)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t FROM administrator.logins"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t WHERE logintime < log_date AND usr = log_usr) ORDER BY log_date;"
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE VIEW administrator.maxtime AS SELECT * FROM administrator.alive WHERE id IN (SELECT id FROM administrator.control WHERE what=5);"
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE VIEW administrator.mintime AS SELECT * FROM administrator.alive WHERE id IN (SELECT id FROM administrator.control WHERE what=6);"
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void package_mail_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t Procedures "
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PACKAGE administrator.email IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t PROCEDURE send_mail("
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_frmadd in varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_tooadd in varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_subjct in varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_msg in varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pio_status in out nocopy varchar2);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t END email;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PACKAGE body administrator.email IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t g_maicon utl_smtp.connection;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t procedure open_mail_server(pio_status in out nocopy varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t -- serveur de messagerie"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t l_maihst varchar2(100) := 'venus';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pio_status := 'OK';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t g_maicon := utl_smtp.open_connection(l_maihst);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.helo(g_maicon, l_maihst);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t when others then"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pio_status := substr(sqlerrm, 1, 200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t END open_mail_server;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ ""
						+ fr.soleil.sgad.Constants.newLine
						+ "\t procedure close_mail_server(pio_status in out nocopy varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pio_status := 'OK';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.quit(g_maicon);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t when others then"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pio_status := substr(sqlerrm, 1, 200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t END close_mail_server;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ ""
						+ fr.soleil.sgad.Constants.newLine
						+ "\t procedure send_mail("
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_frmadd  in varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_tooadd  in varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_subjct  in            varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pi_msg     in            varchar2,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pio_status in out nocopy varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t errexc  EXCEPTION;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t --< open connection >--"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t pio_status := 'OK';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t open_mail_server(pio_status);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t if pio_status != 'OK' then"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t raise errexc;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t --< assign from and to >--"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.mail(g_maicon, pi_frmadd);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.rcpt(g_maicon, pi_tooadd);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t --< create message text >--"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.open_data(g_maicon);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.write_data(g_maicon, 'From: \"' || pi_frmadd || '\"<' || pi_frmadd || '>' || utl_tcp.crlf);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.write_data(g_maicon, 'To: \"' || pi_tooadd || '\"<'   || pi_tooadd || '>' || utl_tcp.crlf);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.write_data(g_maicon, 'Subject: ' || pi_subjct || utl_tcp.crlf);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.write_data(g_maicon, pi_msg);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t utl_smtp.close_data(g_maicon);"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t --< close connection >--"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t close_mail_server(pio_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t\t\t EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t when errexc then"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t return;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t when others then"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t pio_status := substr(sqlerrm, 1, 200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t END send_mail;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine + "\t END email;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_gen() {
		fr.soleil.sgad.oracle.Generator
				.add_object(fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.oracle.Constants.separator
						+ fr.soleil.sgad.Constants.newLine
						+ "--\t\t\t Procedures "
						+ fr.soleil.sgad.Constants.newLine);
		procedure_modif_TBS_USED();
		procedure_change_redo();
		procedure_feed_TBSused();

		if (fr.soleil.sgad.oracle.Generator.hdb_generation) {
			if (fr.soleil.sgad.oracle.Constants.generationMode == 2)
				procedure_partionnement();
			procedure_feed_alive(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_feed_alive_sec(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_feed_expanse(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_feed_charge(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_feed_delay(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_feed_maxtime(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_feed_mintime(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_job_hdb();
		}
		if (fr.soleil.sgad.oracle.Generator.tdb_generation) {
			procedure_feed_alive(fr.soleil.sgad.oracle.Constants.schema[1]);
			procedure_feed_alive_sec(fr.soleil.sgad.oracle.Constants.schema[1]);
			procedure_feed_expanse(fr.soleil.sgad.oracle.Constants.schema[1]);
			procedure_feed_charge(fr.soleil.sgad.oracle.Constants.schema[1]);
			procedure_feed_delay(fr.soleil.sgad.oracle.Constants.schema[1]);
			procedure_feed_maxtime(fr.soleil.sgad.oracle.Constants.schema[1]);
			procedure_feed_mintime(fr.soleil.sgad.oracle.Constants.schema[1]);
		}

		procedure_send_TBS_size();
		procedure_send_error();

		if (fr.soleil.sgad.oracle.Generator.hdb_generation) {
			procedure_send_expanse(fr.soleil.sgad.oracle.Constants.schema[0]);
			procedure_send_globalreport(fr.soleil.sgad.oracle.Constants.schema[0]);
		}
		if (fr.soleil.sgad.oracle.Generator.tdb_generation) {
			procedure_send_expanse(fr.soleil.sgad.oracle.Constants.schema[1]);
			procedure_send_globalreport(fr.soleil.sgad.oracle.Constants.schema[1]);
		}
	}

	private static void procedure_modif_TBS_USED() {
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE administrator.modif_TBSUSED AS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	type tvTBS IS TABLE OF varchar2(22);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	v_TBS tvTBS;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	vreq varchar2(30000);"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "SELECT tablespace_name BULK COLLECT INTO v_TBS FROM dba_tablespaces WHERE contents != 'TEMPORARY' AND tablespace_name NOT IN (SELECT column_name from dba_tab_columns where table_name = 'TBSUSED' AND column_name != 'TIME');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IF SQL%ROWCOUNT > 0 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "vreq := 'ALTER TABLE ADMINISTRATOR.TBSUSED ADD(';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "FOR i IN v_tbs.first..v_tbs.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "vreq := vreq || v_tbs(i) || ' number' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "IF i < v_tbs.last THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "vreq := vreq || ', ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "vreq := vreq || ')';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXECUTE IMMEDIATE vreq;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_change_redo() {
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE administrator.change_redo"
						+ " IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var1 varchar2(45);"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "var1 := 'alter system switch logfile';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate var1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_feed_TBSused() {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- renseigne la table TBSused"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet de suivre l'évolution des tablespaces"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.feed_TBSused"
						+ " IS" + fr.soleil.sgad.Constants.newLine + "\t"
						+ "CURSOR C1 IS " + fr.soleil.sgad.Constants.newLine
						+ "\t\t" + "SELECT T.tablespace_name,"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "round(SUM(NVL(E.bytes,0)/(1024*1024))) used"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "FROM dba_tablespaces T, sys.dba_extents E"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "WHERE T.tablespace_name = E.tablespace_name (+)"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "AND T.contents != 'TEMPORARY'"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "GROUP BY T.tablespace_name;        "
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "req varchar2(10000);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "req2 varchar2(10000);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "var_tbs varchar2(50);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "var_num number;" + fr.soleil.sgad.Constants.newLine
						+ "BEGIN" + fr.soleil.sgad.Constants.newLine + "\t"
						+ "req := 'insert into administrator.TBSused(time';"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "req2 := 'values (current_timestamp';"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "LOOP"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "FETCH C1 INTO var_tbs, var_num;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "EXIT WHEN C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "req := req || ', ' || var_tbs;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "req2 := req2 || ', ' || var_num;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "req := req || ')';"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "req2 := req2 || ')';"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "req := req || req2;"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "EXECUTE IMMEDIATE req;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "COMMIT;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END feed_TBSused;"
						+ fr.soleil.sgad.Constants.newLine + "/" + "\t\n"
						+ "show error;" + fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_feed_alive(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- renseigne la table alive"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet de savoir si un attribut est toujours en cours d'archivage"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : l'adresse mail du destinataire, la requete pour définir les ID des attributs concernés"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.feed_alive_"
						+ schema
						+ "(multiplicateur number, L_TOOADD varchar2, vreq varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tvarchar is table of varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vtnbatt tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vtnomarch tvarchar;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nomarch varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vnb_att number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "varseq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "frequence number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "total_ecart number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "verif number := 0;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vmaxtime date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_frmadd varchar2(100) := 'Oracle_stat_"
						+ schema
						+ "';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_subjct varchar2(100) := 'ATT NOT ALIVE ???';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg    varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg2   varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_status varchar2(200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select administrator.id_admin.nextval into varseq from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate vreq BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vnb_att := vid.count;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "-- transformation en nombre de : sysdate - max(time)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select max(time), to_number(substr(to_char(sysdate-max(time)), 8,3))*86400 + '; -- nb jour"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'to_number(substr(to_char(sysdate-max(time)), 12,2))*3600 + '; -- nb heure"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'to_number(substr(to_char(sysdate-max(time)), 15,2))*60 + '; -- nb minute"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'to_number(substr(to_char(sysdate-max(time)), 18,2)) '; -- nb seconde"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'from "
						+ schema
						+ ".att_' || vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req into vmaxtime, total_ecart;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "select archiver, per_per_mod/1000 into nomarch, frequence from "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[2])
						+ " where stop_date is null and id = vid(i) and start_date = (select max(start_date) from "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[2])
						+ " where id = vid(i));"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "IF total_ecart>frequence*multiplicateur THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "-- l'archiver n'archive plus"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "insert into administrator.alive(id, id_att, maxtime) values (varseq, vid(i), vmaxtime);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "verif :=1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "insert into administrator.control(id, time, what, nb_att, req) VALUES (varseq, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), 1, vnb_att, 'req');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "if verif = 1 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "select count(a.id_att), m.archiver BULK COLLECT INTO vtnbatt, vtnomarch from administrator.alive a JOIN "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[2])
						+ " m ON (a.id_att = m.ID) where a.id = varseq GROUP BY m.archiver;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "FOR I IN vtnbatt.first..vtnbatt.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || vtnbatt(i) || ' attributs not archived by \"' || vtnomarch(i) || '\" archiver' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "END IF;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "commit;"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "DBMS_OUTPUT.PUT_LINE('id = ' || varseq);"
						+ fr.soleil.sgad.Constants.newLine + "END feed_alive_"
						+ schema + ";" + fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void procedure_feed_alive_sec(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- renseigne la table alive"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet de savoir si un attribut est toujours en cours d'archivage"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : le nombre de seconde entre la derniere valeur archivée et l'heure actuelle"
						+ fr.soleil.sgad.Constants.newLine
						+ "--l'adresse mail du destinataire, la requete pour définir les ID des attributs concernés"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.feed_alive_sec_"
						+ schema
						+ "(nb_seconde number, L_TOOADD varchar2, vreq varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tvarchar is table of varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vtnbatt tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vtnomarch tvarchar;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nomarch varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vnb_att number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "varseq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "frequence number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "total_ecart number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "verif number := 0;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vmaxtime date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_frmadd varchar2(100) := 'Oracle_stat_"
						+ schema
						+ "';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_subjct varchar2(100) := 'ATT NOT ALIVE ???';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg    varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg2   varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_status varchar2(200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select administrator.id_admin.nextval into varseq from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate vreq BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vnb_att := vid.count;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "-- transformation en nombre de : sysdate - max(time)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "--req := 'select max(time), TO_NUMBER(REPLACE(REPLACE(SUBSTR(TO_CHAR(sysdate-MAX(TIME)),12,8),''.'','',''), '':'', '''')) from "
						+ schema
						+ ".att_' || result(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select max(time), to_number(substr(to_char(sysdate-max(time)), 8,3))*86400 + '; -- nb jour"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'to_number(substr(to_char(sysdate-max(time)), 12,2))*3600 + '; -- nb heure"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'to_number(substr(to_char(sysdate-max(time)), 15,2))*60 + '; -- nb minute"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'to_number(substr(to_char(sysdate-max(time)), 18,2)) '; -- nb seconde"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'from "
						+ schema
						+ ".att_' || vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req into vmaxtime, total_ecart;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "IF total_ecart>nb_seconde THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "-- l'archiver n'archive plus"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "insert into administrator.alive(id, id_att, maxtime) values (varseq, vid(i), vmaxtime);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "verif := 1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "insert into administrator.control (id, time, what, nb_att, req) VALUES (varseq, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), 1, vnb_att, 'req');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "if verif = 1 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "select count(a.id_att), m.archiver BULK COLLECT INTO vtnbatt, vtnomarch from administrator.alive a JOIN "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[2])
						+ " m ON (a.id_att = m.ID) where a.id = varseq GROUP BY m.archiver;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "FOR I IN vtnbatt.first..vtnbatt.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || vtnbatt(i) || ' attributs not archived by \"' || vtnomarch(i) || '\" archiver' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "END IF;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "commit;"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "DBMS_OUTPUT.PUT_LINE('id = ' || varseq);"
						+ fr.soleil.sgad.Constants.newLine
						+ "END feed_alive_sec_" + schema + ";"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void procedure_feed_expanse(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- renseigne la table expanse"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet de suivre l'évolution de la taille des table"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : la requete pour définir les ID des attributs concernés"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.feed_expanse_hdb(vreq varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "tot_line number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_extents number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "size_tab number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "varseq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_att number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select administrator.id_admin.nextval into varseq from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate vreq BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_att := vid.count;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "insert into administrator.expanse(id_att, nb_extents, tabsize) select to_number(substr(segment_name, 5)), count(segment_name), sum(bytes)/8 from dba_extents where segment_name LIKE"
						+ " 'ATT\\_%' ESCAPE '\\' "
						+ "and to_number(substr(segment_name, 5)) = vid(i) group by segment_name;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select count(*) from hdb.att_' || vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req into tot_line;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select count(*) from dba_extents where segment_name = ''ATT_' || vid(i) || '''';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req into nb_extents;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "if nb_extents > 0 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "	req := 'select count(segment_name), sum(bytes)/8 from dba_extents where segment_name = ''ATT_' || vid(i) || ''' group by segment_name';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "	execute immediate req into nb_extents, size_tab;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "	update administrator.expanse set nb_lines = tot_line where id_att = vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "update administrator.expanse set id = varseq where id is null;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "insert into administrator.control (id, time, what, nb_att, req) VALUES (varseq, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), 2, nb_att, vreq);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "commit;"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "DBMS_OUTPUT.PUT_LINE('id = ' || varseq);"
						+ fr.soleil.sgad.Constants.newLine
						+ "END feed_expanse_hdb;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void procedure_feed_charge(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- renseigne la table charge"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet de suivre le nombre de transaction effectué entre deux dates"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : la date de début de l'analyse, la date de fin de l'analyse"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- et la requete pour définir les ID des attributs concernés"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.feed_charge_"
						+ schema
						+ "(vstart_date date, vstop_date date, vreq varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid Tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vnb_lines number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req2 varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "varseq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nbval number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select administrator.id_admin.nextval into varseq from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate vreq BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nbval := vid.last;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "insert into administrator.control(id, time, what, nb_att, req) VALUES (varseq, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), 3, nbval, vreq);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "FOR i IN vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req2 := 'select count(*) from "
						+ schema
						+ ".att_' || vid(i) || ' where TIME between :1 AND :2';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req2 into vnb_lines using vstart_date, vstop_date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "insert into administrator.charge(id, id_att, start_date, stop_date, nb_lines) VALUES (varseq, vid(i), vstart_date, vstop_date, vnb_lines);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "commit;"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "DBMS_OUTPUT.PUT_LINE('id = ' || varseq);"
						+ fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void procedure_feed_delay(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- renseigne la table delay"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet de suivre l'impact des dérives temporelles (crée une table avec l'ecart entre chaque transaction)"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en paramètre : la date de début de l'analyse, la date de fin de l'analyse"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- et la requete pour définir les ID des attributs concernés"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.feed_delay_hdb(vstart_date date, vstop_date date, vreq varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid Tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "varseq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_att number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "varecart number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "frequence number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select administrator.id_admin.nextval into varseq from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate vreq BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_att := vid.count;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "select per_per_mod/1000 into frequence from hdb.amt where id = vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'create temporary table administrator.s' || varseq || '_' || vid(i) || ' as select t2.time as time, ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || 'to_number(translate(substr(to_char(t1.time-t2.time),18,6), ''.'', '',''))-' || frequence || ' as ecart from ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || '(select rownum-1 A, time from hdb.att_' || vid(i) || ' where TIME between ''' || vstart_date || ''' AND ''' || vstop_date || ''' order by time) t1, ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || '(select rownum A, time from hdb.att_' || vid(i) || ' where TIME between ''' || vstart_date || ''' AND ''' || vstop_date || ''' order by time) t2 where t1.A=t2.A';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select avg(ecart) from administrator.s' || varseq || '_' || vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req into varecart;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "insert into administrator.delay(id,id_att, start_date, stop_date, avg_retard) VALUES (varseq, vid(i), vstart_date, vstop_date, varecart);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "update administrator.expanse set id = varseq where id is null;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "insert into administrator.control (id, time, what, nb_att, req) VALUES (varseq, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), 4, nb_att, vreq);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "commit;"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "DBMS_OUTPUT.PUT_LINE('premier att = ' || vid(1));"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "DBMS_OUTPUT.PUT_LINE('nombre d''att = ' || nb_att);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "DBMS_OUTPUT.PUT_LINE('id = ' || varseq);"
						+ fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + ""
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_feed_maxtime(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE administrator.feed_maxtime_"
						+ schema
						+ " (vreq varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "tot_line number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_extents number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "size_tab number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "varseq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_att number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vmaxtime date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select administrator.id_admin.nextval into varseq from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate vreq BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_att := vid.count;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select max(time) from "
						+ schema
						+ ".att_' || vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req into vmaxtime;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "insert into administrator.alive(id, id_att, maxtime) values (varseq, vid(i), vmaxtime);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "insert into administrator.control (id, time, what, nb_att, req) VALUES (varseq, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), 5, nb_att, vreq);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_OUTPUT.PUT_LINE('id = ' || varseq);"
						+ fr.soleil.sgad.Constants.newLine
						+ "END feed_maxtime_"
						+ schema
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_feed_mintime(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE administrator.feed_mintime_"
						+ schema
						+ " (vreq varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	vid tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	tot_line number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	nb_extents number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	size_tab number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	varseq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	nb_att number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "	vmaxtime date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select administrator.id_admin.nextval into varseq from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate vreq BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nb_att := vid.count;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select min(time) from "
						+ schema
						+ ".att_' || vid(i);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req into vmaxtime;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "insert into administrator.alive(id, id_att, maxtime) values (varseq, vid(i), vmaxtime);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "insert into administrator.control (id, time, what, nb_att, req) VALUES (varseq, to_date(sysdate, 'DD/MM/YY HH24:MI:SS'), 6, nb_att, vreq);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_OUTPUT.PUT_LINE('id = ' || varseq);"
						+ fr.soleil.sgad.Constants.newLine
						+ "END feed_mintime_"
						+ schema
						+ ";"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_send_TBS_size() {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- envoi d'un mail"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet d'etre renseigné sur la taille actuelle des tablespaces"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : l'adresse mail du destinataire"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.send_TBS_size (l_tooadd varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "SELECT sysdate, f.tablespace_name, a.total,u.used,f.free,round((u.used/a.total)*100) \"% used\" ,round((f.free/a.total)*100) \"% Free\""
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "FROM (SELECT tablespace_name, SUM(bytes/(1024*1024)) total FROM sys.dba_data_files group by tablespace_name) a,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "(SELECT tablespace_name, round(SUM(bytes/(1024*1024))) used FROM sys.dba_extents group by tablespace_name) u,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "(SELECT tablespace_name, round(SUM(bytes/(1024*1024))) free FROM sys.dba_free_space group by tablespace_name) f"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "WHERE a.tablespace_name = f.tablespace_name AND a.tablespace_name = u.tablespace_name;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_frmadd varchar2(100) := '"
						+ fr.soleil.sgad.oracle.Generator._oracle_sid
						+ "_stat';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_subjct varchar2(100) := 'TBS_size';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg    varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg2   varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_status varchar2(200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_time date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_tab varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_tot varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_used varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_free varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_pused varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_pfree varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '| total | used  | free  | %used | %free | tablespace    |' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "fetch C1 into var_time, var_tab, var_tot, var_used, var_free, var_pused, var_pfree;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXIT when C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || '| ' || lpad(var_tot, 5)   || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ " ' ' || lpad(var_used, 5)  || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ " ' ' || lpad(var_free, 5)  || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ " ' ' || lpad(var_pused, 5) || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ " ' ' || lpad(var_pfree, 5) || ' | ' || rpad(var_tab, 12) || '  | ' || utl_tcp.crlf ;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "close C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END send_TBS_size;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void procedure_send_error() {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- envoi d'un mail"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet d'etre renseigné sur les 10 dernières erreurs générées dans la base de données"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : l'adresse mail du destinataire"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.send_error (l_tooadd varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "SELECT log_date, log_usr, ipaddr, err_msg FROM administrator.log_error"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "WHERE ID > (SELECT MAX(ID) - 10 FROM administrator.log_error)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "AND log_date > sysdate - 7;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_frmadd varchar2(100) := '"
						+ fr.soleil.sgad.oracle.Generator._oracle_sid
						+ "_stat';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_subjct varchar2(100) := 'error';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg    varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_status varchar2(200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_log_date date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_log_usr varchar2(50);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_ipaddr varchar2(20);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_err_nr varchar2(500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_err_msg varchar2(1500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "tot_err number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "SELECT COUNT(*) INTO tot_err FROM administrator.log_error WHERE log_date > sysdate - 7;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'nombre derreur en une semaine : ' || tot_err || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "FETCH C1 INTO var_log_date, var_log_usr, var_ipaddr, var_err_msg;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXIT WHEN C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || to_char(var_log_date, 'DD/MM/RR HH24:MI:SS') || ' ' || var_log_usr || ' ' || var_ipaddr || '   ' || var_err_msg;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "IF C1%ROWCOUNT >= 15 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || ' plusieurs autres erreurs sont survenues';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine + "END send_error;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_send_expanse(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- envoi d'un mail"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet d'etre renseigné sur les 10 tables les plus volumineuse (taille en Ko, nombre d'extents"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : l'adresse mail du destinataire"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.send_expanse_"
						+ schema
						+ " (l_tooadd varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " CURSOR C1 IS "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ " SELECT a.id, a.full_name, b.nb_lines, b.nb_extents, b.tabsize from administrator.expanse b, "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[0]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[0])
						+ " a "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ " WHERE a.id = b.id_att"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ " AND rownum <10  --  PB"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ " AND b.ID = (SELECT MAX(ID) FROM administrator.expanse) ORDER BY b.tabsize DESC;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " l_frmadd varchar2(100) := '"
						+ fr.soleil.sgad.oracle.Generator._oracle_sid
						+ "_stat';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " l_subjct varchar2(100) := 'expanse';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " l_msg varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " l_status varchar2(200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " vid number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " vfull_name varchar2(1500);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " vnb_lines number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " vnb_extents number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " vtabsize number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " tot_err number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "FETCH C1 INTO vid,vfull_name, vnb_lines, vnb_extents, vtabsize;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXIT WHEN C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "l_msg := l_msg || lpad(to_char(vid),5) || ':';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "l_msg := l_msg || lpad(vfull_name,70) || ' ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "l_msg := l_msg || lpad(to_char(vnb_lines),10) || ' lines,';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "l_msg := l_msg || lpad(to_char(vnb_extents),3) || ' extents, ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "l_msg := l_msg || lpad(to_char(round(vtabsize/1048576,2)),5) || 'Mo' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END send_expanse_" + schema + ";"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}

	private static void procedure_send_globalreport(String schema) {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- envoi d'un mail"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- permet d'etre renseigné sur l'état général de la base de données"
						+ fr.soleil.sgad.Constants.newLine
						+ "-- en parametre : l'adresse mail du destinataire"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.send_globalreport_"
						+ schema
						+ " (l_tooadd varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE tnumber is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " SELECT sysdate, f.tablespace_name, a.total,u.used,f.free,round((u.used/a.total)*100) \"% used\" ,round((f.free/a.total)*100) \"% Free\""
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ " FROM (SELECT tablespace_name, SUM(bytes/(1024*1024)) total FROM sys.dba_data_files group by tablespace_name) a,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "(SELECT tablespace_name, round(SUM(bytes/(1024*1024))) used FROM sys.dba_extents group by tablespace_name) u,"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "(SELECT tablespace_name, round(SUM(bytes/(1024*1024))) free FROM sys.dba_free_space group by tablespace_name) f"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "WHERE a.tablespace_name = f.tablespace_name AND a.tablespace_name = u.tablespace_name;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_frmadd varchar2(100) := '"
						+ fr.soleil.sgad.oracle.Generator._oracle_sid
						+ "_stat';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_subjct varchar2(100) := 'GLOBAL_REPORT';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg    varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_status varchar2(200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "maxsize number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "minsize number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "avgsize number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vpermod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vabsmod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vrelmod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vcalmod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vperpermod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vperabsmod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vperrelmod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vpercalmod tnumber;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_time date;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_tab varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_tot varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_used varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_free varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_pused varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_pfree varchar2(100);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nbatt_current number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nbatt_fast number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nblignes number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "-- renseignement sur la taille des tables (la + petite, la + grande, taille moyenne)"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select max(tabsize)/2048 , min(tabsize)/2048, round(avg(tabsize)/2048, 2) INTO maxsize, minsize, avgsize from administrator.expanse where id = (select max(id) from administrator.expanse);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "-- nombre d'attribut en cours d'archivage"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select count(*) into nbatt_current from "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[2])
						+ " where stop_date is null;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "-- nombre d'attributs dont la periode d'archivage est inferieure a 30 secondes"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select count(*) into nbatt_fast from "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[2])
						+ " where stop_date is null and per_per_mod <= '30000';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "-- graphique récapitulatif sur les modes d'archivage"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select sum(per_mod), per_per_mod, sum(abs_mod), per_abs_mod, sum(rel_mod), per_rel_mod, sum(cal_mod), per_cal_mod BULK COLLECT INTO vpermod, vperpermod, vabsmod, vperabsmod, vrelmod, vperrelmod, vcalmod, vpercalmod from "
						+ schema
						+ "."
						+ ((fr.soleil.sgad.oracle.Generator.hdb_generation) ? fr.soleil.sgad.oracle.Constants.hdbObjects[2]
								: fr.soleil.sgad.oracle.Constants.tdbObjects[2])
						+ " where stop_date is null group by per_per_mod, per_abs_mod, per_rel_mod, per_cal_mod;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "-- nombre de transaction faites entre le dernier \"feed_expanse\" et l'avant dernier"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "-- si feed_expanse fait quotidiennement, alors, nombre de transaction par jour"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select sum(b.nb_lines-a.nb_lines) into nblignes from administrator.expanse a JOIN administrator.expanse b ON (a.id_att=b.id_att) where b.id = (select max(id) from control where what = 0) and a.id = (select max(id) from control where id < (select max(id) from control) and what = 0);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "OPEN C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'STATISTIQUE SUR L''ARCHIVAGE EN COURS' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'nombre d''attribut en cours d''archivage : ' || nbatt_current || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'il y a ' || nbatt_fast || ' attribut(s) en cours d''archivage et dont la periode d''archivage est inferieure à 30 secondes' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'tableau récapitualtif des attributs en cours d''archivage (avec leur modes):' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|nb_per|frequence|nb_abs|frequence|nb_rel|frequence|nb_cal|frequence|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "FOR i IN vpermod.first..vpermod.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || '| ' ||  lpad(to_char(vpermod(i)), 5) || '| ' || "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ "lpad(to_char(vperpermod(i)), 8) || '| ' || "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ "lpad(to_char(vabsmod(i)), 5) || '| ' || "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ "lpad(to_char(vperabsmod(i)), 8) || '| ' || "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ "lpad(to_char(vrelmod(i)), 5) || '| ' || "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ "lpad(to_char(vperrelmod(i)), 8) || '| ' || "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ "lpad(to_char(vcalmod(i)), 5) || '| ' || "
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t\t\t"
						+ "lpad(to_char(vpercalmod(i)), 8) || '| ' || utl_tcp.crlf;			"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || nblignes || ' insertion(s) effectuée(s) ces 24 dernières heures' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'STATISTIQUE SUR L''ESPACE DISQUE OCCUPE' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'taille de la plus petite table : ' || minsize || ' Ko' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'taille de la plus grande table : ' || maxsize || ' Ko' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || 'taille moyen d''une table : ' 		|| avgsize || ' Ko' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '| total | used  | free  | %used | %free | tablespace    |' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "	 fetch C1 into var_time, var_tab, var_tot, var_used, var_free, var_pused, var_pfree;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "	 EXIT when C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "	 l_msg := l_msg || '| ' || lpad(var_tot, 5)   || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "' ' || lpad(var_used, 5)  || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "' ' || lpad(var_free, 5)  || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "' ' || lpad(var_pused, 5) || ' |' ||"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t\t"
						+ "' ' || lpad(var_pfree, 5) || ' | ' || rpad(var_tab, 12) || '  | ' || utl_tcp.crlf ;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || '|*******************************************************|' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "close C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "END send_globalreport_" + schema + ";"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_partionnement() {

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE administrator.crtbs (vmois number, directory varchar2, l_tooadd varchar2) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_frmadd varchar2(100) := 'ORACLE_TBS';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_subjct varchar2(100) := 'CREATION_DE_TABLESPACE';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg    varchar2(32500) := '';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_status varchar2(200);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "tbs varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select 'M' || to_char(ADD_MONTHS(sysdate,vmois), 'MM') || 'Y' || to_char(sysdate, 'YY') into tbs from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req := 'CREATE TABLESPACE ' || tbs || ' DATAFILE ''' || directory || '' || tbs || '.dbf'' SIZE 10M REUSE AUTOEXTEND ON NEXT 100M	MAXSIZE	30000M EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate req;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req := 'ALTER USER "
						+ fr.soleil.sgad.oracle.Constants.schema[0]
						+ " QUOTA UNLIMITED ON ' || tbs;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate req;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "--DBMS_OUTPUT.PUT_LINE(req);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || ' réussi' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg ||  utl_tcp.crlf ||  utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "l_msg := l_msg || ' le tablespace ' || tbs || ' a bien été crée !!! ' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "EXCEPTION"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "WHEN OTHERS THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := ' a échoué ' || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || utl_tcp.crlf || utl_tcp.crlf;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "l_msg := l_msg || ' erreur = ' || sqlerrm;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "administrator.email.send_mail(l_frmadd, l_tooadd, l_subjct, l_msg, l_status);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "dbms_output.put_line(l_status);"
						+ fr.soleil.sgad.Constants.newLine
						+ "END;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("execute administrator.crtbs(0, '"
						+ Generator._oracleBase_Path + "\\" + "oradata" + "\\"
						+ Generator._oracle_sid + "\\" + "', '"
						+ fr.soleil.sgad.oracle.Constants.adminEmail + "');"
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("execute administrator.crtbs(1, '"
						+ Generator._oracleBase_Path + "\\" + "oradata" + "\\"
						+ Generator._oracle_sid + "\\" + "', '"
						+ fr.soleil.sgad.oracle.Constants.adminEmail + "');"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator.add_object("variable job_no1 number;"
				+ fr.soleil.sgad.Constants.newLine + " BEGIN "
				+ fr.soleil.sgad.Constants.newLine
				+ fr.soleil.sgad.Constants.newLine + "\t"
				+ " DBMS_JOB.SUBMIT(:job_no1, 'begin administrator.crtbs(2, '"
				+ "'" + Generator._oracleBase_Path + "\\" + "oradata" + "\\"
				+ Generator._oracle_sid + "\\" + "''" + ", " + "''"
				+ fr.soleil.sgad.oracle.Constants.adminEmail + "''"
				+ "); end;', sysdate, 'ADD_MONTHS(sysdate,1)');"
				+ fr.soleil.sgad.Constants.newLine + "\t" + " COMMIT;"
				+ fr.soleil.sgad.Constants.newLine + "END;"
				+ fr.soleil.sgad.Constants.newLine + "/"
				+ fr.soleil.sgad.Constants.newLine + "SHOW ERROR;"
				+ fr.soleil.sgad.Constants.newLine
				+ fr.soleil.sgad.Constants.newLine);

		fr.soleil.sgad.oracle.Generator
				.add_object("CREATE OR REPLACE PROCEDURE "
						+ fr.soleil.sgad.oracle.Constants.schemaAdmin
						+ ".addpart (vmois number) IS"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 is select id from "
						+ fr.soleil.sgad.oracle.Constants.schema[0]
						+ "."
						+ fr.soleil.sgad.oracle.Constants.hdbObjects[0]
						+ " where time < sysdate-31;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_tab all_tables.table_name%type;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(2000);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "tbs varchar2(2000);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vdate varchar2(2000);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "seq number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "select 'm' || to_char(ADD_MONTHS(sysdate,vmois), 'MM') || 'y' || to_char(ADD_MONTHS(sysdate, vmois), 'YY'), TO_CHAR(LAST_DAY(ADD_MONTHS(sysdate, vmois)), 'DD/MM/YYYY') || ' 23:59:59' into tbs, vdate from dual;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "open C1;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "loop"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "fetch C1 into var_tab;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "EXIT WHEN C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'alter table "
						+ fr.soleil.sgad.oracle.Constants.schema[0]
						+ ".' || var_tab || ' add ';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := req || ' PARTITION ' || tbs || ' VALUES LESS THAN (to_date(''' || vdate || ''', ''DD/MM/YYYY HH24:MI:SS'')) tablespace ' || tbs || ' pctfree 0';"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "DBMS_OUTPUT.PUT_LINE(req);"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "execute immediate (req);"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "close C1;"
						+ fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
		fr.soleil.sgad.oracle.Generator
				.add_object("variable job_no1 number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(:job_no1, 'BEGIN "
						+ fr.soleil.sgad.oracle.Constants.schemaAdmin
						+ ".addpart(1); END;', last_day(trunc(trunc(sysdate+1, 'DD')+2/24, 'HH24'))+1, 'ADD_MONTHS(sysdate,1)');"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "COMMIT;"
						+ fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "SHOW ERROR;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_job_hdb() {
		procedure_jobalive();
		procedure_jobcharge();
		procedure_jobdelay();
		procedure_jobexpanse();
		procedure_jobtbs();
		procedure_joberror();
		procedure_jobSGR();
		procedure_removejob();
		procedure_changejob();
		procedure_job_CleanAll();
	}

	private static void procedure_jobalive() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.jobalive (start_date varchar2, var_interval varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "toto number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(toto, 'begin administrator.feed_alive_hdb(3,''"
						+ fr.soleil.sgad.oracle.Constants.adminEmail
						+ "'',''select id from HDB.AMT where stop_date is null''); end;', start_date, var_interval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_jobcharge() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.jobcharge (start_date varchar2, var_interval varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "toto number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(toto, 'begin administrator.feed_charge_hdb(sysdate-1 , sysdate, ''select id from HDB.AMT where stop_date is null''); end;', start_date, var_interval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_jobdelay() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.jobdelay (start_date varchar2, var_interval varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "toto number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(toto, 'begin administrator.feed_delay_hdb(sysdate-1 , sysdate, ''select id from HDB.AMT where stop_date is null''); end;', start_date, var_interval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_jobexpanse() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.jobexpanse (start_date varchar2, var_interval varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "toto number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(toto, 'begin administrator.feed_expanse_hdb(''select id from HDB.AMT where stop_date is null''); end;', start_date, var_interval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_jobtbs() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.jobtbs (start_date varchar2, var_interval varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "toto number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(toto, 'begin administrator.feed_tbsused(); end;', start_date, var_interval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_joberror() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.joberror (start_date varchar2, var_interval varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "toto number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(toto, 'begin administrator.send_error(''"
						+ fr.soleil.sgad.oracle.Constants.adminEmail
						+ "''); end;', start_date, var_interval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_jobSGR() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.jobSGR (start_date varchar2, var_interval varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "toto number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "DBMS_JOB.SUBMIT(toto, 'begin administrator.send_globalreport_hdb(''"
						+ fr.soleil.sgad.oracle.Constants.adminEmail
						+ "''); end;', start_date, var_interval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_removejob() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.removejob (what varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nojob number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req := 'select count(job) from user_jobs where what like ''%' || what || '%''';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate req into nojob;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "if nojob > 0 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select job from user_jobs where what like ''%' || what || '%''';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "DBMS_OUTPUT.PUT_LINE(req);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "DBMS_JOB.REMOVE(vid(i));"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "DBMS_OUTPUT.PUT_LINE('job ' || vid(i) || ' supprimé');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_changejob() {
		fr.soleil.sgad.oracle.Generator
				.add_object("create or replace procedure administrator.changejob (datedeb varchar2, varinterval varchar2, what varchar2) is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "TYPE Tid is table of number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "vid tid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nojob number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "begin"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req := 'select count(job) from user_jobs where what like ''%' || what || '%''';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate req into nojob;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "if nojob > 0 THEN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "req := 'select job from user_jobs where what like ''%' || what || '%''';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "execute immediate req BULK COLLECT INTO vid;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "for i in vid.first..vid.last LOOP"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "DBMS_OUTPUT.PUT_LINE(req);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "DBMS_JOB.INTERVAL(vid(i), varinterval);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "DBMS_JOB.NEXT_DATE(vid(i), datedeb);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "commit;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t\t"
						+ "DBMS_OUTPUT.PUT_LINE('job ' || vid(i) || ' modifié');"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t\t"
						+ "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "END IF;"
						+ fr.soleil.sgad.Constants.newLine
						+ "end;"
						+ fr.soleil.sgad.Constants.newLine
						+ "/"
						+ fr.soleil.sgad.Constants.newLine
						+ "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);
	}

	private static void procedure_job_CleanAll() {
		fr.soleil.sgad.oracle.Generator
				.add_object("-- CLEAN ALL"
						+ fr.soleil.sgad.Constants.newLine
						+ "CREATE OR REPLACE PROCEDURE administrator.CleanAll is"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "CURSOR C1 IS SELECT table_name FROM user_tables WHERE table_name LIKE 'ESD%_%';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "nojob number;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "req varchar2(555);"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "var_tab user_tables.table_name%type;"
						+ fr.soleil.sgad.Constants.newLine
						+ "BEGIN"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'truncate table alive';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'truncate table charge';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'truncate table delay';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'truncate table expanse';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'truncate table control';"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'DROP SEQUENCE administrator.id_admin' ;"
						+ fr.soleil.sgad.Constants.newLine
						+ "\t"
						+ "execute immediate 'CREATE SEQUENCE administrator.id_admin INCREMENT BY 1 START WITH 1 MAXVALUE 99999 CYCLE NOCACHE';"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "open C1;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "loop"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "fetch C1 into var_tab;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "EXIT WHEN C1%NOTFOUND;"
						+ fr.soleil.sgad.Constants.newLine + "\t\t"
						+ "execute immediate 'drop table ' || var_tab;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "END LOOP;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "CLOSE C1;"
						+ fr.soleil.sgad.Constants.newLine + "\t" + "commit;"
						+ fr.soleil.sgad.Constants.newLine + "END;"
						+ fr.soleil.sgad.Constants.newLine + "/"
						+ fr.soleil.sgad.Constants.newLine + "show error;"
						+ fr.soleil.sgad.Constants.newLine
						+ fr.soleil.sgad.Constants.newLine);

	}
}
