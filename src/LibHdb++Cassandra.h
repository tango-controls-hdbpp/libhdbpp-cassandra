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

#include "LibHdb++.h"
#include "tango.h"

#include <cassandra.h>
#include <string>
#include <vector>
#include <map>

namespace HDBPP
{

class HdbPPCassandra : public AbstractDB
{
private:

	enum FindAttrResult { AttrNotFound, FoundAttrWithDifferentType, FoundAttrWithSameType };
	enum extract_t { EXTRACT_READ, EXTRACT_SET };

	struct ConfParams
	{
		CassUuid id;
		unsigned int ttl;
	};

	CassCluster* mp_cluster;
	CassSession* mp_session;
	string m_keyspace_name;
	map<string,struct ConfParams> attr_ID_map;
	bool logging_enabled;
	CassLogLevel cassandra_logging_level;

public:
	~HdbPPCassandra();

	HdbPPCassandra(vector<string> configuration);

	virtual void insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type);
	virtual void insert_param_Attr(Tango::AttrConfEventData *data, HdbEventDataType ev_data_type);

	virtual void configure_Attr(string name,
	                           	int type/*DEV_DOUBLE, DEV_STRING, ..*/,
	                            int format/*SCALAR, SPECTRUM, ..*/,
	                            int write_type/*READ, READ_WRITE, ..*/,
	                            unsigned int ttl/*hours, 0=infinity*/);

	virtual void updateTTL_Attr(string name, unsigned int ttl/*hours, 0=infinity*/);
	virtual void event_Attr(string fqdn_attr_name, unsigned char event);

private:

    void connect_session();

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
	void string_vector2map(vector<string> str, string separator, map<string,string>* results);

	/**
	 * This method will retrieve the last event from the history table
	 * for the given ID and will return it in the event string given as parameter
	 * @param ID the ID of the attribute we want to check
	 * @param last_event the string where the event type will be stored if found
	 * @param fqdn_attr_name FQDN attribute name (used only on debug trace messages)
	 * @return true if the request succeeded or false in case of error
	 **/
	bool find_last_event(const CassUuid & ID, string &last_event, const string & fqdn_attr_name);

	/**
	 * This method will try to get the corresponding attribute ID from its name and facility name (Tango Host)
	 **/
	bool find_attr_id(string fqdn_attr_name, CassUuid & ID);
	bool find_attr_id_and_ttl(string fqdn_attr_name, CassUuid & ID, unsigned int & ttl);
	bool find_attr_id_and_ttl_in_db(string fqdn_attr_name, CassUuid & ID, unsigned int & ttl);
	FindAttrResult find_attr_id_type_and_ttl(string facility, string attr_name, CassUuid & ID, string attr_type, unsigned int &conf_ttl);

	string get_data_type(int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/) const;
	string get_table_name(int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/) const;

	string get_insert_query_str(int tango_data_type /*DEV_DOUBLE, DEV_STRING, ..*/,
	                            int format/*SCALAR, SPECTRUM, ..*/,
	                            int write_type/*READ, READ_WRITE, ..*/,
								int & nbQueryParams,
								bool isNull,
								unsigned int ttl) const;

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

	void insert_history_event(const string & history_event_name, CassUuid att_conf_id);

	/**
	 * insert_attr_conf(): Insert the provided attribute into the configuration table (add it)
	 *
	 * @param facility: control system name (TANGO_HOST)
	 * @param attr_name: attribute name with its device name like domain/family/member/name
	 * @param data_type: attribute data type (e.g. scalar_devdouble_rw)
	 * @param uuid: uuid generated during the insertion process for this attribute
	 * @param ttl: TTL value for this attribute (default = 0)
	 * @return 0 in case of success
	 *         -1 in case of error during the query execution
	 **/
	void insert_attr_conf(const string & facility,
	                     const string & attr_name,
	                     const string & data_type,
	                     CassUuid & uuid,
	                     unsigned int ttl = 0);
	/**
	 * insert_domain(): Insert a new domain into the domains table,
	 * which is a table used to speed up browsing of attributes
	 *
	 * @param facility: control system name (TANGO_HOST)
	 * @param domain: domain name
	 * @return 0 in case of success
	 *         -1 in case of error during the query execution
	 **/
	void insert_domain(const string & facility, const string & domain);
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
	void insert_family(const string & facility, const string & domain, const string & family);
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
	void insert_member(const string & facility, const string & domain, const string & family, const string & member);
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
	void insert_attr_name(const string & facility, const string & domain, const string & family,
	                      const string & member, const string & attribute_name);
	/**
	 * update_ttl(): Execute the query to update the TTL for the attribute having the
	 * specified ID
	 *
	 * @param ttl: the new ttl value
	 * @param facility: control system name (TANGO_HOST)
	 * @param attr_name: attribute name including device name (domain/family/member/att_name)
	 * @throws Tango::DevFailed exceptions in case of error during the query
	 *         execution
	 */
	void update_ttl(unsigned int ttl, const string & facility, const string & attr_name);

	/**
 	 * Utility to set the cassandra logging level based on the configuration parameter
 	 **/
	void set_cassandra_logging_level(string level);

	CassError execute_statement(CassStatement * statement);
	void throw_execute_exception(string message, string query, CassError error, const char * origin);
	void log_error_message(string message, const char * origin);
};

class HdbPPCassandraFactory : public DBFactory
{

public:
	virtual AbstractDB* create_db(vector<string> configuration);
	virtual ~HdbPPCassandraFactory(){}

};

}; // namespace HDBPP

#endif

