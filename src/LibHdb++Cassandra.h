//=============================================================================
//
// file :        LibHdb++Cassandra.h
//
// description : Include for the LibHdb++Cassandra library.
//
// Author: Reynald Bourtembourg
//
// $Revision: $
//
// $Log: $
//
//=============================================================================

#ifndef _HDBPP_CASSANDRA_H
#define _HDBPP_CASSANDRA_H

#include <cassandra.h>
#include "LibHdb++.h"

#include <string>
#include <iostream>
#include <sstream>
#include <vector>
#include <map>
#include <stdint.h>

//Tango:
#include <tango.h>
//#include <event.h>

namespace HDBPP
{

class HdbPPCassandra : public AbstractDB
{
public:
	const string TYPE_SCALAR = 	"scalar";
	const string TYPE_ARRAY	= 	"array";

	const string TYPE_DEV_BOOLEAN = "devboolean";
	const string TYPE_DEV_UCHAR = 	"devuchar";
	const string TYPE_DEV_SHORT = 	"devshort";
	const string TYPE_DEV_USHORT = 	"devushort";
	const string TYPE_DEV_LONG = 	"devlong";
	const string TYPE_DEV_ULONG = 	"devulong";
	const string TYPE_DEV_LONG64 = 	"devlong64";
	const string TYPE_DEV_ULONG64 = "devulong64";
	const string TYPE_DEV_FLOAT = 	"devfloat";
	const string TYPE_DEV_DOUBLE = 	"devdouble";
	const string TYPE_DEV_STRING = 	"devstring";
	const string TYPE_DEV_STATE = 	"devstate";
	const string TYPE_DEV_ENCODED = "devencoded";

	const string TYPE_RO = 	"ro";
	const string TYPE_RW = 	"rw";

	const string EVENT_ADD = 	"add";
	const string EVENT_REMOVE = "remove";
	const string EVENT_START = 	"start";
	const string EVENT_STOP = 	"stop";
	const string EVENT_CRASH =  "crash";

	//######## att_conf ########
	const string CONF_TABLE_NAME = 	"att_conf";
	const string CONF_COL_ID = 	"att_conf_id";
	const string CONF_COL_FACILITY = "cs_name";
	const string CONF_COL_NAME = 	"att_name";
	const string CONF_COL_TYPE = 	"data_type";

	//######## att_history ########
	const string HISTORY_TABLE_NAME	=	"att_history";
	const string HISTORY_COL_ID =    	"att_conf_id";
	const string HISTORY_COL_EVENT =	"event";
	const string HISTORY_COL_TIME =		"time";
	const string HISTORY_COL_TIME_US = 	"time_us";

	//######## att_scalar_... ########
	const string SC_COL_ID = 	"att_conf_id";
	const string SC_COL_PERIOD = 	"period";
	const string SC_COL_INS_TIME = 	"insert_time";
	const string SC_COL_RCV_TIME = 	"recv_time";
	const string SC_COL_EV_TIME  = 	"data_time";
	const string SC_COL_INS_TIME_US = "insert_time_us";
	const string SC_COL_RCV_TIME_US = "recv_time_us";
	const string SC_COL_EV_TIME_US  = "data_time_us";
	const string SC_COL_VALUE_R = 	"value_r";
	const string SC_COL_VALUE_W = 	"value_w";
	const string SC_COL_QUALITY = 	"quality";
	const string SC_COL_ERROR_DESC = "error_desc";

	//######## att_array_double_ro ########
	const string ARR_DOUBLE_RO_TABLE_NAME = "att_array_devdouble_ro";
	const string ARR_DOUBLE_RW_TABLE_NAME = "att_array_devdouble_rw";

	const string ARR_COL_ID = 	"att_conf_id";
	// insert time can be obtained with the WRITETIME Cassandra function
	//const string ARR_COL_INS_TIME = "insert_time";
	//const string ARR_COL_INS_TIME_US = "insert_time_us";
	const string ARR_COL_RCV_TIME = "recv_time";
	const string ARR_COL_RCV_TIME_US = "recv_time_us";
	const string ARR_COL_EV_TIME = 	"event_time";
	const string ARR_COL_EV_TIME_US = "event_time_us";
	const string ARR_COL_VALUE_R = 	"value_r";
	const string ARR_COL_VALUE_W = 	"value_w";
	const string ARR_COL_IDX = 	"idx";
	const string ARR_COL_DIMX = "dim_x";
	const string ARR_COL_DIMY =	"dim_y";
	const string ARR_COL_QUALITY =	"quality";

	//######## att_parameter ########
	const string PARAM_TABLE_NAME 	= "att_parameter";
	const string PARAM_COL_ID 		= "att_conf_id";
	const string PARAM_COL_INS_TIME = "insert_time";
	const string PARAM_COL_INS_TIME_US	= "insert_time_us";
	const string PARAM_COL_EV_TIME		= "recv_time";
	const string PARAM_COL_EV_TIME_US	= "recv_time_us";
	const string PARAM_COL_LABEL		= "label";
	const string PARAM_COL_UNIT			= "unit";
	const string PARAM_COL_STANDARDUNIT		= "standard_unit";
	const string PARAM_COL_DISPLAYUNIT		= "display_unit";
	const string PARAM_COL_FORMAT			= "format";
	const string PARAM_COL_ARCHIVERELCHANGE	= "archive_rel_change";
	const string PARAM_COL_ARCHIVEABSCHANGE	= "archive_abs_change";
	const string PARAM_COL_ARCHIVEPERIOD	= "archive_period";
	const string PARAM_COL_DESCRIPTION		= "description";

private:
	enum extract_t { EXTRACT_READ, EXTRACT_SET };
	CassCluster* mp_cluster;
	CassSession* mp_session;
	string m_keyspace_name;
	map<string,CassUuid> attr_ID_map;

public:
	~HdbPPCassandra();

	HdbPPCassandra(string contact_points, string user, string password, string keyspace_name, int port);

	//void connect_db(string host, string user, string password, string dbname);
	CassError connect_session();
	CassError execute_query(const char* query);
	void print_error(CassFuture* future);
	/**
	 * This method will try to get the corresponding attribute ID from its name and facility name (Tango Host)
	 **/
	bool find_attr_id(string fqdn_attr_name, CassUuid & ID);
	int find_attr_id_in_db(string fqdn_attr_name, CassUuid & ID);
	int find_attr_id_type(string facility, string attr_name, CassUuid & ID, string attr_type);
	/**
	 * This method will retrieve the last event from the history table
	 * for the given ID and will return it in the event string given as parameter
	 * @param ID the ID of the attribute we want to check
	 * @param last_event the string where the event type will be stored if found
	 * @param fqdn_attr_name FQDN attribute name (used only on debug trace messages)
	 * @return 0 if the request succeeded or -1 in case of error
	 **/
	int find_last_event(const CassUuid & ID, string &last_event, const string & fqdn_attr_name);
	virtual int insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type);
	virtual int insert_param_Attr(Tango::AttrConfEventData *data, HdbEventDataType ev_data_type);
	virtual int configure_Attr(string name, int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/);
	virtual int remove_Attr(string name);
	virtual int start_Attr(string name);
	virtual int stop_Attr(string name);

private:
	string get_only_attr_name(string str);
	string get_only_tango_host(string str);
	string remove_domain(string facility);
	string add_domain(string facility);
	string get_data_type(int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/) const;
	string get_table_name(int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/) const;
	string get_insert_query_str(int tango_data_type /*DEV_DOUBLE, DEV_STRING, ..*/,
	                            int format/*SCALAR, SPECTRUM, ..*/,
	                            int write_type/*READ, READ_WRITE, ..*/,
								int & nbQueryParams,
								bool isNull) const;
	void extract_and_bind_bool(CassStatement* statement,
                               int & param_index,
                               int data_format /*SCALAR, SPECTRUM, ..*/,
                               Tango::EventData *data,
                               enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_uchar(CassStatement* statement,
                                int & param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_short(CassStatement* statement,
                                int & param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_ushort(CassStatement* statement,
                                 int & param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_long(CassStatement* statement,
                               int & param_index,
                               int data_format /*SCALAR, SPECTRUM, ..*/,
                               Tango::EventData *data,
                               enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_ulong(CassStatement* statement,
                                int & param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_long64(CassStatement* statement,
                                 int & param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_ulong64(CassStatement* statement,
                                  int & param_index,
                                  int data_format /*SCALAR, SPECTRUM, ..*/,
                                  Tango::EventData *data,
                                  enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_float(CassStatement* statement,
                                int & param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_double(CassStatement* statement,
                                 int & param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_string(CassStatement* statement,
                                 int & param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_state(CassStatement* statement,
                                int & param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_encoded(CassStatement* statement,
                                  int & param_index,
                                  int data_format /*SCALAR, SPECTRUM, ..*/,
                                  Tango::EventData *data,
                                  enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);
	void extract_and_bind_rw_values(CassStatement* statement,
                                    int & param_index,
	                                int data_type,
                                    int write_type /*READ, READ_WRITE, ..*/,
                                    int data_format /*SCALAR, SPECTRUM, ..*/,
                                    Tango::EventData *data,
                                    bool isNull);
	bool insert_history_event(const string & history_event_name, CassUuid att_conf_id);
};

class HdbPPCassandraFactory : public DBFactory
{

public:
	virtual AbstractDB* create_db(string host, string user, string password, string dbname, int port);
	virtual ~HdbPPCassandraFactory(){}

};

}; // namespace HDBPP

#endif

