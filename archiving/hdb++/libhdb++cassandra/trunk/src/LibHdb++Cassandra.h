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
	const string TYPE_SCALAR =   "scalar";
	const string TYPE_ARRAY  =   "array";

	const string TYPE_DEV_BOOLEAN = "devboolean";
	const string TYPE_DEV_UCHAR =	"devuchar";
	const string TYPE_DEV_SHORT =	"devshort";
	const string TYPE_DEV_USHORT =  "devushort";
	const string TYPE_DEV_LONG =	"devlong";
	const string TYPE_DEV_ULONG =	"devulong";
	const string TYPE_DEV_LONG64 =  "devlong64";
	const string TYPE_DEV_ULONG64 = "devulong64";
	const string TYPE_DEV_FLOAT =	"devfloat";
	const string TYPE_DEV_DOUBLE =  "devdouble";
	const string TYPE_DEV_STRING =  "devstring";
	const string TYPE_DEV_STATE =	"devstate";
	const string TYPE_DEV_ENCODED = "devencoded";

	const string TYPE_RO = "ro";
	const string TYPE_RW = "rw";

	const string EVENT_ADD =	"add";
	const string EVENT_REMOVE = "remove";
	const string EVENT_START =  "start";
	const string EVENT_STOP =	"stop";
	const string EVENT_CRASH =  "crash";
	const string EVENT_PAUSE =  "pause";

	//######## att_conf ########
	const string CONF_TABLE_NAME =    "att_conf";
	const string CONF_COL_ID =  	 "att_conf_id";
	const string CONF_COL_FACILITY = "cs_name";
	const string CONF_COL_NAME =	 "att_name";
	const string CONF_COL_TYPE =	 "data_type";
	
	//######## domains ########
	const string DOMAINS_TABLE_NAME =	"domains";
	const string DOMAINS_COL_FACILITY = "cs_name";
	const string DOMAINS_COL_DOMAIN =	"domain";
	
	//######## families ########
	const string FAMILIES_TABLE_NAME =   "families";
	const string FAMILIES_COL_FACILITY = "cs_name";
	const string FAMILIES_COL_DOMAIN =   "domain";
	const string FAMILIES_COL_FAMILY =   "family";

	//######## members ########
	const string MEMBERS_TABLE_NAME =	"members";
	const string MEMBERS_COL_FACILITY = "cs_name";
	const string MEMBERS_COL_DOMAIN =	"domain";
	const string MEMBERS_COL_FAMILY =	"family";
	const string MEMBERS_COL_MEMBER =	"member";
	
	//######## att_names ########
	const string ATT_NAMES_TABLE_NAME =   "att_names";
	const string ATT_NAMES_COL_FACILITY = "cs_name";
	const string ATT_NAMES_COL_DOMAIN =   "domain";
	const string ATT_NAMES_COL_FAMILY =   "family";
	const string ATT_NAMES_COL_MEMBER =   "member";
	const string ATT_NAMES_COL_NAME =     "name";

	//######## att_history ########
	const string HISTORY_TABLE_NAME  = "att_history";
	const string HISTORY_COL_ID =	   "att_conf_id";
	const string HISTORY_COL_EVENT =   "event";
	const string HISTORY_COL_TIME =    "time";
	const string HISTORY_COL_TIME_US = "time_us";

	//######## att_scalar_... ########
	const string SC_COL_ID =		  "att_conf_id";
	const string SC_COL_PERIOD =	  "period";
	const string SC_COL_INS_TIME =    "insert_time";
	const string SC_COL_RCV_TIME =    "recv_time";
	const string SC_COL_EV_TIME  =    "data_time";
	const string SC_COL_INS_TIME_US = "insert_time_us";
	const string SC_COL_RCV_TIME_US = "recv_time_us";
	const string SC_COL_EV_TIME_US =  "data_time_us";
	const string SC_COL_VALUE_R =	  "value_r";
	const string SC_COL_VALUE_W =	  "value_w";
	const string SC_COL_QUALITY =	  "quality";
	const string SC_COL_ERROR_DESC =  "error_desc";

	//######## att_array_double_ro ########
	const string ARR_DOUBLE_RO_TABLE_NAME = "att_array_devdouble_ro";
	const string ARR_DOUBLE_RW_TABLE_NAME = "att_array_devdouble_rw";

	const string ARR_COL_ID =		   "att_conf_id";
	const string ARR_COL_RCV_TIME =    "recv_time";
	const string ARR_COL_RCV_TIME_US = "recv_time_us";
	const string ARR_COL_EV_TIME =     "event_time";
	const string ARR_COL_EV_TIME_US =  "event_time_us";
	const string ARR_COL_VALUE_R =     "value_r";
	const string ARR_COL_VALUE_W =     "value_w";
	const string ARR_COL_IDX =  	   "idx";
	const string ARR_COL_DIMX = 	   "dim_x";
	const string ARR_COL_DIMY = 	   "dim_y";
	const string ARR_COL_QUALITY =     "quality";

	//######## att_parameter ########
	const string PARAM_TABLE_NAME = 		  "att_parameter";
	const string PARAM_COL_ID = 			  "att_conf_id";
	const string PARAM_COL_INS_TIME =		  "insert_time";
	const string PARAM_COL_INS_TIME_US =	  "insert_time_us";
	const string PARAM_COL_EV_TIME =		  "recv_time";
	const string PARAM_COL_EV_TIME_US = 	  "recv_time_us";
	const string PARAM_COL_LABEL =  		  "label";
	const string PARAM_COL_UNIT =			  "unit";
	const string PARAM_COL_STANDARDUNIT =	  "standard_unit";
	const string PARAM_COL_DISPLAYUNIT =	  "display_unit";
	const string PARAM_COL_FORMAT = 		  "format";
	const string PARAM_COL_ARCHIVERELCHANGE = "archive_rel_change";
	const string PARAM_COL_ARCHIVEABSCHANGE = "archive_abs_change";
	const string PARAM_COL_ARCHIVEPERIOD =    "archive_period";
	const string PARAM_COL_DESCRIPTION =	  "description";
	
	//######## ERRORS #########
	const int ERR_NO_SLASH_IN_ATTR =		 -1;
	const int ERR_ONLY_ONE_SLASH_IN_ATTR =   -2;
	const int ERR_ONLY_TWO_SLASHES_IN_ATTR = -3;
	const int ERR_TOO_MANY_SLASHES_IN_ATTR = -4;
	const int ERR_EMPTY_DOMAIN_IN_ATTR =	 -5;
	const int ERR_EMPTY_FAMILY_IN_ATTR =	 -6;
	const int ERR_EMPTY_MEMBER_IN_ATTR =	 -7;
	const int ERR_EMPTY_ATTR_NAME_IN_ATTR =  -8;

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
	virtual int event_Attr(string fqdn_attr_name, unsigned char event);

private:
	string get_only_attr_name(string str);
	string get_only_tango_host(string str);
	/**
	 * This method parses attr_name and extracts the domain, 
	 * family, member and attribute name from the provided string
	 * @param attr_name attribute name under the form <domain>/<family>/<member>/<name>
	 * @param domain the domain extracted from the provided attr_name parameter (<domain>)
	 * @param family the family extracted from the provided attr_name parameter (<family>)
	 * @param member the member extracted from the provided attr_name parameter (<member>)
	 * @param name the attribute name extracted from the provided attr_name parameter (<name>)
	 * @return returns 1 in case of success, a negative value in case of failure
	 */
	int getDomainFamMembName(const string & attr_name, string & domain, string & family, string & member, string & name);
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
	/**
	 * insert_attr_conf(): Insert the provided attribute into the configuration table (add it)
	 * 
	 * @param facility: control system name (TANGO_HOST)
	 * @param attr_name: attribute name with its device name like domain/family/member/name
	 * @param data_type: attribute data type (e.g. scalar_devdouble_rw)
	 * @param uuid: uuid generated during the insertion process for this attribute
	 * @return 0 in case of success
	 *         -1 in case of error during the query execution
	 **/
	int insert_attr_conf(const string & facility, const string & attr_name, const string & data_type, CassUuid & uuid);
	/**
	 * insert_domain(): Insert a new domain into the domains table,
	 * which is a table used to speed up browsing of attributes
	 * 
	 * @param facility: control system name (TANGO_HOST)
	 * @param domain: domain name
	 * @return 0 in case of success
	 *         -1 in case of error during the query execution
	 **/
	int insert_domain(const string & facility, const string & domain);
	/**
	 * insert_family(): Insert a new family into the families table, 
	 * which is a table used to speed up browsing of attributes
	 * 
	 * @param facility: control system name (TANGO_HOST)
	 * @param domain: domain name
	 * @param family: family name
	 * @return 0 in case of success
	 *         -1 in case of error during the query execution
	 **/
	int insert_family(const string & facility, const string & domain, const string & family);
	/**
	 * insert_member(): Insert a new member into the members table, 
	 * which is a table used to speed up browsing of attributes
	 * 
	 * @param facility: control system name (TANGO_HOST)
	 * @param domain: domain name
	 * @param family: family name
	 * @param member: member name
	 * @return 0 in case of success
	 *         -1 in case of error during the query execution
	 **/
	int insert_member(const string & facility, const string & domain, const string & family, const string & member);
	/**
	 * insert_attr_name(): Insert a new attribute name into the attribute names table, 
	 * which is a table used to speed up browsing of attributes
	 * 
	 * @param facility: control system name (TANGO_HOST)
	 * @param domain: domain name
	 * @param family: family name
	 * @param member: member name
	 * @param attribute_name: attribute name
	 * @return 0 in case of success
	 *         -1 in case of error during the query execution
	 **/
	int insert_attr_name(const string & facility, const string & domain, const string & family, 
	                      const string & member, const string & attribute_name);
};

class HdbPPCassandraFactory : public DBFactory
{

public:
	virtual AbstractDB* create_db(string host, string user, string password, string dbname, int port);
	virtual ~HdbPPCassandraFactory(){}

};

}; // namespace HDBPP

#endif

