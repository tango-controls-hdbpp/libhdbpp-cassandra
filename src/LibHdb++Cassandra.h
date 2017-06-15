/* Copyright (C) : 2017
   European Synchrotron Radiation Facility
   BP 220, Grenoble 38043, FRANCE

   This file is part of libhdb++cassandra.

   libhdb++cassandra is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   libhdb++cassandra is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Foobar.  If not, see <http://www.gnu.org/licenses/>. */

#ifndef _HDBPP_CASSANDRA_H
#define _HDBPP_CASSANDRA_H

#include "LibHdb++.h"
#include "tango.h"

#include <cassandra.h>
#include <map>
#include <string>
#include <vector>

namespace HDBPP
{
/**
 * @class HdbPPCassandra
 * @ingroup HDB++
 * @brief HdbPPCassandra implements the AbstractDB interface to store tango event data in a
 * cassandra database cluster.
 *
  *The HdbPPCassandra driver is loaded dynamically from libhdbpp (@see HdbClient)
 * when the class or device configuration requests this archiver library. A valid configuration
 * must be passed as the constructor parameter, @see HdbPPCassandra() for configuration parameter
 * documentation
 */
class HdbPPCassandra : public AbstractDB
{
private:
    enum FindAttrResult
    {
        AttrNotFound,
        FoundAttrWithDifferentType,
        FoundAttrWithSameType
    };

    enum extract_t
    {
        EXTRACT_READ,
        EXTRACT_SET
    };

    struct ConfParams
    {
        CassUuid id;
        unsigned int ttl;
    };

    CassCluster *mp_cluster;
    CassSession *mp_session;
    string m_keyspace_name;
    map<string, struct ConfParams> attr_ID_map;
    bool logging_enabled;
    CassLogLevel cassandra_logging_level;

public:
    /**
     * @brief HdbPPCassandra destructor
     *
     * The destructor will attempt to disconnect an open cassandra session
     */
    ~HdbPPCassandra();

    /**
     * @brief HdbPPCassandra constructor
     *
     * The configuration parameters must contain the following strings:
     *
     * - Mandatory:
     *     - contact_points: Cassandra cluster contract host, eg cassandra_db
     *     - keyspace: Keyspace to use within the cluster, eg, hdb_test
     * - Optional:
     *      - user: Cluster log in user name
     *      - password: Password for above user name
     *      - local_dc: Datacenter name
     * - Debug:
     *     - logging_enabled: Either true to enable command line debug, or false to disable
     *     - cassandra_driver_log_level  Cassandra logging level, see CassLogLevel in Datastax
     * documentation. This
     *       must be one of the follow values:
     *          - TRACE: Equivalant CASS_LOG_TRACE
     *          - DEBUG: Equivalant CASS_LOG_DEBUG
     *          - INFO: Equivalant CASS_LOG_INFO
     *          - WARN: Equivalant CASS_LOG_WARN
     *          - ERROR: Equivalant CASS_LOG_ERROR
     *          - CRITICAL: Equivalant CASS_LOG_CRITICAL
     *          - DISABLED: Equivalant CASS_LOG_DISABLED
     *
     * @param configuration A list of configuration parameters to start the driver with.
     */
    HdbPPCassandra(vector<string> configuration);

    /**
     * @brief Insert an attribute insert event into the database
     *
     * Inserts an attribute insert event for the EventData into the database. Neither empty
     * or invalid attributes will be archived, and if the attribute does not exist an
     * exception will be raised.
     *
     * @param data Tango event data about the attibute.
     * @param ev_data_type HDB event data for the attibute.
     * @throw Tango::DevFailed
     */
    virtual void insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type);

    /**
     * @brief Inserts an attibute parameter event into the database
     *
     * Inserts the parameter data (attribute event data) about an existing attribute
     * into the database. The attribute must exist, otherwise an exception is thrown.
     *
     * @param data Tango event data about the attibute.
     * @param ev_data_type HDB event data for the attibute.
     * @throw Tango::DevFailed
     */
    virtual void insert_param_Attr(Tango::AttrConfEventData *data, HdbEventDataType ev_data_type);

    /**
     * @brief Add and configure an attribute in the database.
     *
     * Trying to reconfigure an existing attribute will result in an exception, and if an
     * attribute already exists with the same configuration then the ttl will be updated if
     * different.
     *
     * @param name Attribute name.
     * @param type The type of the attribute.
     * @param format The format of the attribute.
     * @param write_type The read/write access of the type.
     * @param  ttl The time to live in hour, 0 for infinity
     * @throw Tango::DevFailed
     */
    virtual void configure_Attr(string name,
                                int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                int format /*SCALAR, SPECTRUM, ..*/,
                                int write_type /*READ, READ_WRITE, ..*/,
                                unsigned int ttl);

    /**
     * @brief Update the ttl value for an attribute.
     *
     * The attribute must exist, otherwise an exception is raised.
     *
     * @param name Attribute name.
     * @param ttl The time to live in hour, 0 for infinity
     * @throw Tango::DevFailed
     */
    virtual void updateTTL_Attr(string name, unsigned int ttl);

    /**
    * @brief Record a start, Stop, Pause or Remove history event for an attribute.
    *
    * Inserts a history event for the attibute name passed to the function. The attribute
    * must exists in the database otherwise an exception is raised.
    *
    * @param fqdn_attr_name Attribute name.
    * @param event
    * @throw Tango::DevFailed
    */
    virtual void event_Attr(string fqdn_attr_name, unsigned char event);

    // left exposed while we look into updating and creating new tests
    bool find_attr_id(string fqdn_attr_name, CassUuid &ID);
    bool find_attr_id_and_ttl(string fqdn_attr_name, CassUuid &ID, unsigned int &ttl);
    bool find_attr_id_and_ttl_in_db(string fqdn_attr_name, CassUuid &ID, unsigned int &ttl);

    FindAttrResult find_attr_id_type_and_ttl(
        string facility, string attr_name, CassUuid &ID, string attr_type, unsigned int &conf_ttl);

private:
    void connect_session();

    string get_only_attr_name(string str);
    string get_only_tango_host(string str);

    int get_domain_fam_memb_name(const string &attr_name, string &domain, string &family, string &member, string &name);
    string remove_domain(string facility);
    string add_domain(string facility);

    bool find_last_event(const CassUuid &ID, string &last_event, const string &fqdn_attr_name);

    string get_data_type(int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                         int format /*SCALAR, SPECTRUM, ..*/,
                         int write_type /*READ, READ_WRITE, ..*/) const;

    string get_table_name(int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                          int format /*SCALAR, SPECTRUM, ..*/,
                          int write_type /*READ, READ_WRITE, ..*/) const;

    string get_insert_query_str(int tango_data_type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                int format /*SCALAR, SPECTRUM, ..*/,
                                int write_type /*READ, READ_WRITE, ..*/,
                                int &nbQueryParams,
                                bool isNull,
                                unsigned int ttl) const;

    void extract_and_bind_bool(CassStatement *statement,
                               int &param_index,
                               int data_format /*SCALAR, SPECTRUM, ..*/,
                               Tango::EventData *data,
                               enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_uchar(CassStatement *statement,
                                int &param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_short(CassStatement *statement,
                                int &param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_ushort(CassStatement *statement,
                                 int &param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_long(CassStatement *statement,
                               int &param_index,
                               int data_format /*SCALAR, SPECTRUM, ..*/,
                               Tango::EventData *data,
                               enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_ulong(CassStatement *statement,
                                int &param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_long64(CassStatement *statement,
                                 int &param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_ulong64(CassStatement *statement,
                                  int &param_index,
                                  int data_format /*SCALAR, SPECTRUM, ..*/,
                                  Tango::EventData *data,
                                  enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_float(CassStatement *statement,
                                int &param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_double(CassStatement *statement,
                                 int &param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_string(CassStatement *statement,
                                 int &param_index,
                                 int data_format /*SCALAR, SPECTRUM, ..*/,
                                 Tango::EventData *data,
                                 enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_state(CassStatement *statement,
                                int &param_index,
                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                Tango::EventData *data,
                                enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_encoded(CassStatement *statement,
                                  int &param_index,
                                  int data_format /*SCALAR, SPECTRUM, ..*/,
                                  Tango::EventData *data,
                                  enum extract_t extract_type /* EXTRACT_READ | EXTRACT_SET */);

    void extract_and_bind_rw_values(CassStatement *statement,
                                    int &param_index,
                                    int data_type,
                                    int write_type /*READ, READ_WRITE, ..*/,
                                    int data_format /*SCALAR, SPECTRUM, ..*/,
                                    Tango::EventData *data,
                                    bool isNull);

    void insert_history_event(const string &history_event_name, CassUuid att_conf_id);

    void insert_attr_conf(const string &facility,
                          const string &attr_name,
                          const string &data_type,
                          CassUuid &uuid,
                          unsigned int ttl = 0);

    void insert_domain(const string &facility, const string &domain);

    void insert_family(const string &facility, const string &domain, const string &family);

    void insert_member(const string &facility, const string &domain, const string &family, const string &member);

    void insert_attr_name(const string &facility,
                          const string &domain,
                          const string &family,
                          const string &member,
                          const string &attribute_name);

    void update_ttl(unsigned int ttl, const string &facility, const string &attr_name);

    void set_cassandra_logging_level(string level);
    CassError execute_statement(CassStatement *statement);
    void throw_execute_exception(string message, string query, CassError error, const char *origin);
    void string_vector2map(vector<string> str, string separator, map<string, string> *results);
};

/**
 * @class HdbPPCassandraFactory
 * @ingroup HDB++
 * @brief Factory object to create the database object, in this case an instance of HdbPPCassandra
 */
class HdbPPCassandraFactory : public DBFactory
{
public:
    /**
     * @brief Create a HdbPPCassandra database object
     * @param configuration A list of configuration parameters to start the driver with, see
     * HdbPPCassandra.
     * @throw Tango::DevFailed
     */
    virtual AbstractDB *create_db(vector<string> configuration);

    virtual ~HdbPPCassandraFactory() {}
};

}; // namespace HDBPP

#endif
