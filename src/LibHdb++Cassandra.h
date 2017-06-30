/* Copyright (C) : 2014-2017
   European Synchrotron Radiation Facility
   BP 220, Grenoble 38043, FRANCE

   This file is part of libhdb++cassandra.

   libhdb++cassandra is free software: you can redistribute it and/or modify
   it under the terms of the Lesser GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   libhdb++cassandra is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Lesser
   GNU General Public License for more details.

   You should have received a copy of the Lesser GNU General Public License
   along with libhdb++cassandra.  If not, see <http://www.gnu.org/licenses/>. */

#ifndef _HDBPP_CASSANDRA_H
#define _HDBPP_CASSANDRA_H

#include "AttributeName.h"
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
 * @ingroup HDBPP-Interface
 * @brief HdbPPCassandra implements the AbstractDB interface to store tango event data in a
 * cassandra database cluster.
 *
 * The HdbPPCassandra driver is loaded dynamically from libhdbpp (@see HdbClient)
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

    // Parameters for an attribute that can be cached. Mapped to the attribute name
    // in a map below.
    struct AttributeParams
    {
        AttributeParams(CassUuid param_id, unsigned int param_ttl) : id(param_id), ttl(param_ttl) {}
        CassUuid id;
        unsigned int ttl;
    };

    // cache the attribute name to some of its often used data, i.e. ttl and id. This
    // saves it being looked up in the database everytime we request it
    map<string, AttributeParams> attribute_cache;

    CassCluster *mp_cluster;
    CassSession *mp_session;
    string m_keyspace_name;
    CassLogLevel cassandra_logging_level;

public:
    /**
     * @brief HdbPPCassandra destructor
     *
     * The destructor will attempt to disconnect an open Cassandra session
     */
    ~HdbPPCassandra();

    /**
     * @brief HdbPPCassandra constructor
     *
     * The configuration parameters must contain the following strings:
     *
     * - Mandatory:
     *     - contact_points: Cassandra cluster contact point hostname, eg cassandra_db,
     *       given as a comma separated list. The contact points are used to initialize
     *       the driver and it will automatically discover the rest of the nodes in your
     *       Cassandra cluster. Tip: include more than one contact point to be robust
     *       against node failures.
     *     - keyspace: Keyspace to use within the cluster, eg, hdb_test
     * - Optional:
     *      - user: Cluster log in user name
     *      - password: Password for above user name
     *      - local_dc: Datacenter name used for queries with LOCAL consistency
     *        level (e.g. LOCAL_QUORUM). In the current version of this library, all the
     *        statements are executed with LOCAL_QUORUM consistency level.
     * - Debug:
     *     - logging_enabled: Either true to enable command line debug, or false to disable
     *     - cassandra_driver_log_level:  Cassandra logging level, see CassLogLevel in Datastax
     * documentation. This
     *       must be one of the follow values:
     *          - TRACE: Equivalent CASS_LOG_TRACE
     *          - DEBUG: Equivalent CASS_LOG_DEBUG
     *          - INFO: Equivalent CASS_LOG_INFO
     *          - WARN: Equivalent CASS_LOG_WARN
     *          - ERROR: Equivalent CASS_LOG_ERROR
     *          - CRITICAL: Equivalent CASS_LOG_CRITICAL
     *          - DISABLED: Equivalent CASS_LOG_DISABLED
     *
     * @param configuration A list of configuration parameters to start the driver with.
     */
    HdbPPCassandra(vector<string> configuration);

    /**
     * @brief Insert an attribute archive event into the database
     *
     * Inserts an attribute archive event for the EventData into the database. If the attribute
     * does not exist in the database, then an exception will be raised. If the attr_value
     * field of the data parameter if empty, then the attribute is in an error state
     * and the error message will be archived.
     *
     * @param data Tango event data about the attribute.
     * @param ev_data_type HDB event data for the attribute.
     * @throw Tango::DevFailed
     */
    virtual void insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type);

    /**
     * @brief Inserts the attribute configuration data.
     *
     * Inserts the attribute configuration data (Tango Attribute Configuration event data)
     * into the database. The attribute must be configured to be stored in HDB++,
     * otherwise an exception will be thrown.
     *
     * @param data Tango event data about the attribute.
     * @param ev_data_type HDB event data for the attribute.
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
     * @param fqdn_attr_name Fully qualified attribute name
     * @param type The type of the attribute.
     * @param format The format of the attribute.
     * @param write_type The read/write access of the type.
     * @param  ttl The time to live in hour, 0 for infinity
     * @throw Tango::DevFailed
     */
    virtual void configure_Attr(string fqdn_attr_name,
                                int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                int format /*SCALAR, SPECTRUM, ..*/,
                                int write_type /*READ, READ_WRITE, ..*/,
                                unsigned int ttl);

    /**
     * @brief Update the ttl value for an attribute.
     *
     * The attribute must have been configured to be stored in HDB++, otherwise an exception
     * is raised
     *
     * @param fqdn_attr_name Fully qualified attribute nam
     * @param ttl The time to live in hour, 0 for infinity
     * @throw Tango::DevFailed
     */
    virtual void updateTTL_Attr(string fqdn_attr_name, unsigned int ttl);

    /**
    * @brief Record a start, Stop, Pause or Remove history event for an attribute.
    *
    * Inserts a history event for the attribute name passed to the function. The attribute
    * must have been configured to be stored in HDB++, otherwise an exception is raised.
    * This function will also insert an additional CRASH history event before the START
    * history event if the given event parameter is DB_START and if the last history event
    * stored was also a START event.
    *
    * @param fqdn_attr_name Fully qualified attribute name
    * @param event
    * @throw Tango::DevFailed
    */
    virtual void event_Attr(string fqdn_attr_name, unsigned char event);

private:
    void connect_session();
    string remove_domain(string facility);

    bool find_attr_id(AttributeName &attr_name, CassUuid &ID);
    bool find_attr_id_and_ttl(AttributeName &attr_name, CassUuid &ID, unsigned int &ttl);
    bool find_attr_id_and_ttl_in_db(AttributeName &attr_name, CassUuid &ID, unsigned int &ttl);

    FindAttrResult find_attr_id_type_and_ttl(AttributeName &attr_name,
                                             CassUuid &ID,
                                             string attr_type,
                                             unsigned int &conf_ttl);

    bool find_last_event(const CassUuid &ID, string &last_event, AttributeName &attr_name);

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

    void insert_attr_conf(AttributeName &attr_name, const string &data_type, CassUuid &uuid, unsigned int ttl = 0);

    void insert_domain(AttributeName &attr_name);
    void insert_family(AttributeName &attr_name);
    void insert_member(AttributeName &attr_name);
    void insert_attr_name(AttributeName &attr_name);

    void update_ttl(unsigned int ttl, AttributeName &attr_name);

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
