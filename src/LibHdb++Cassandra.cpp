/* Copyright (C) : 2017
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
   along with Foobar.  If not, see <http://www.gnu.org/licenses/>. */

#include "LibHdb++Cassandra.h"

#include <cassandra.h>
#include <iostream>
#include <netdb.h> //for getaddrinfo

#ifndef LIB_BUILDTIME
#define LIB_BUILDTIME RELEASE " " __DATE__ " " __TIME__
#endif

// const char version_string[] = "$Build: " LIB_BUILDTIME " $";
// static const char __FILE__rev[] = __FILE__ " $Id: $";

// trace logging for just about every function can be compiled in for
// debugging work. Normally this is switched off
// #define TRACE_ENABLED

#ifdef TRACE_ENABLED
#define TRACE_MSG(str)                                                                                  \
    do                                                                                                  \
    {                                                                                                   \
        std::cout << "(" << __FILE__ << ":" << __LINE__ << ":" << __func__ << ") " << str << std::endl; \
    } while (false)
#else
#define TRACE_MSG(str)                                                                             \
    do                                                                                             \
    {                                                                                              \
    } while (false)
#endif

// debug can be compiled out no matter how the library is configured, this
// is just a fail safe option, and this option should be enabled
#define DEBUG_ENABLED

#ifdef DEBUG_ENABLED
#define DEBUG_MSG(str)                                                                             \
    do                                                                                             \
    {                                                                                              \
        if (logging_enabled)                                                                       \
            std::cout << "(" << __FILE__ << ":" << __LINE__ << ":" << __func__ << ") " << str;     \
    } while (false)
#else
#define DEBUG_MSG(str)                                                                             \
    do                                                                                             \
    {                                                                                              \
    } while (false)
#endif

// finally an error message, this is never compiled out
#define ERROR_MSG(str)                                                                             \
    do                                                                                             \
    {                                                                                              \
        std::cerr << "(" << __FILE__ << ":" << __LINE__ << ":" << __func__ << ") " << str;         \
    } while (false)

// Some values that define how the driver functions. Moved to here to allow easy
// tracking and updating in the future

// Use up to 2 remote datacenter nodes for remote consistency levels
// or when `allow_remote_dcs_for_local_cl` is enabled.
const unsigned USED_HOSTS_PER_REMOTE_DC = 2;

// Don't use remote datacenter nodes for local consistency levels
const cass_bool_t ALLOW_REMOTE_DCS_FOR_LOCAL_CL = cass_false;

// Local DataCenter default value
const string LOCAL_DC_DEFAULT = "DC1";

namespace HDBPP
{
const string TYPE_SCALAR = "scalar";
const string TYPE_ARRAY = "array";

const string TYPE_DEV_BOOLEAN = "devboolean";
const string TYPE_DEV_UCHAR = "devuchar";
const string TYPE_DEV_SHORT = "devshort";
const string TYPE_DEV_USHORT = "devushort";
const string TYPE_DEV_LONG = "devlong";
const string TYPE_DEV_ULONG = "devulong";
const string TYPE_DEV_LONG64 = "devlong64";
const string TYPE_DEV_ULONG64 = "devulong64";
const string TYPE_DEV_FLOAT = "devfloat";
const string TYPE_DEV_DOUBLE = "devdouble";
const string TYPE_DEV_STRING = "devstring";
const string TYPE_DEV_STATE = "devstate";
const string TYPE_DEV_ENCODED = "devencoded";

const string TYPE_RO = "ro";
const string TYPE_RW = "rw";

const string EVENT_ADD = "add";
const string EVENT_REMOVE = "remove";
const string EVENT_START = "start";
const string EVENT_STOP = "stop";
const string EVENT_CRASH = "crash";
const string EVENT_PAUSE = "pause";

//######## att_conf ########
const string CONF_TABLE_NAME = "att_conf";
const string CONF_COL_ID = "att_conf_id";
const string CONF_COL_FACILITY = "cs_name";
const string CONF_COL_NAME = "att_name";
const string CONF_COL_TYPE = "data_type";
const string CONF_COL_TTL = "ttl";

//######## domains ########
const string DOMAINS_TABLE_NAME = "domains";
const string DOMAINS_COL_FACILITY = "cs_name";
const string DOMAINS_COL_DOMAIN = "domain";

//######## families ########
const string FAMILIES_TABLE_NAME = "families";
const string FAMILIES_COL_FACILITY = "cs_name";
const string FAMILIES_COL_DOMAIN = "domain";
const string FAMILIES_COL_FAMILY = "family";

//######## members ########
const string MEMBERS_TABLE_NAME = "members";
const string MEMBERS_COL_FACILITY = "cs_name";
const string MEMBERS_COL_DOMAIN = "domain";
const string MEMBERS_COL_FAMILY = "family";
const string MEMBERS_COL_MEMBER = "member";

//######## att_names ########
const string ATT_NAMES_TABLE_NAME = "att_names";
const string ATT_NAMES_COL_FACILITY = "cs_name";
const string ATT_NAMES_COL_DOMAIN = "domain";
const string ATT_NAMES_COL_FAMILY = "family";
const string ATT_NAMES_COL_MEMBER = "member";
const string ATT_NAMES_COL_NAME = "name";

//######## att_history ########
const string HISTORY_TABLE_NAME = "att_history";
const string HISTORY_COL_ID = "att_conf_id";
const string HISTORY_COL_EVENT = "event";
const string HISTORY_COL_TIME = "time";
const string HISTORY_COL_TIME_US = "time_us";

//######## att_scalar_... ########
const string SC_COL_ID = "att_conf_id";
const string SC_COL_PERIOD = "period";
const string SC_COL_INS_TIME = "insert_time";
const string SC_COL_RCV_TIME = "recv_time";
const string SC_COL_EV_TIME = "data_time";
const string SC_COL_INS_TIME_US = "insert_time_us";
const string SC_COL_RCV_TIME_US = "recv_time_us";
const string SC_COL_EV_TIME_US = "data_time_us";
const string SC_COL_VALUE_R = "value_r";
const string SC_COL_VALUE_W = "value_w";
const string SC_COL_QUALITY = "quality";
const string SC_COL_ERROR_DESC = "error_desc";

//######## att_array_double_ro ########
const string ARR_DOUBLE_RO_TABLE_NAME = "att_array_devdouble_ro";
const string ARR_DOUBLE_RW_TABLE_NAME = "att_array_devdouble_rw";

const string ARR_COL_ID = "att_conf_id";
const string ARR_COL_RCV_TIME = "recv_time";
const string ARR_COL_RCV_TIME_US = "recv_time_us";
const string ARR_COL_EV_TIME = "event_time";
const string ARR_COL_EV_TIME_US = "event_time_us";
const string ARR_COL_VALUE_R = "value_r";
const string ARR_COL_VALUE_W = "value_w";
const string ARR_COL_IDX = "idx";
const string ARR_COL_DIMX = "dim_x";
const string ARR_COL_DIMY = "dim_y";
const string ARR_COL_QUALITY = "quality";

//######## att_parameter ########
const string PARAM_TABLE_NAME = "att_parameter";
const string PARAM_COL_ID = "att_conf_id";
const string PARAM_COL_INS_TIME = "insert_time";
const string PARAM_COL_INS_TIME_US = "insert_time_us";
const string PARAM_COL_EV_TIME = "recv_time";
const string PARAM_COL_EV_TIME_US = "recv_time_us";
const string PARAM_COL_LABEL = "label";
const string PARAM_COL_UNIT = "unit";
const string PARAM_COL_STANDARDUNIT = "standard_unit";
const string PARAM_COL_DISPLAYUNIT = "display_unit";
const string PARAM_COL_FORMAT = "format";
const string PARAM_COL_ARCHIVERELCHANGE = "archive_rel_change";
const string PARAM_COL_ARCHIVEABSCHANGE = "archive_abs_change";
const string PARAM_COL_ARCHIVEPERIOD = "archive_period";
const string PARAM_COL_DESCRIPTION = "description";

//######## ERRORS #########
const int ERR_NO_SLASH_IN_ATTR = -1;
const int ERR_ONLY_ONE_SLASH_IN_ATTR = -2;
const int ERR_ONLY_TWO_SLASHES_IN_ATTR = -3;
const int ERR_TOO_MANY_SLASHES_IN_ATTR = -4;
const int ERR_EMPTY_DOMAIN_IN_ATTR = -5;
const int ERR_EMPTY_FAMILY_IN_ATTR = -6;
const int ERR_EMPTY_MEMBER_IN_ATTR = -7;
const int ERR_EMPTY_ATTR_NAME_IN_ATTR = -8;

const string EXCEPTION_TYPE_CONFIG = "LibHDB++ Configuration Error";
const string EXCEPTION_TYPE_CONNECTION = "Cassandra Connection Error";
const string EXCEPTION_TYPE_QUERY = "Cassandra Query Error";
const string EXCEPTION_TYPE_EXTRACT = "Attribute Extraction Error";
const string EXCEPTION_TYPE_UNSUPPORTED_ATTR = "Unsupported Attribute Type";
const string EXCEPTION_TYPE_MISSING_ATTR = "Missing Attribute";
const string EXCEPTION_TYPE_NULL_POINTER = "Null Pointer Error";
const string EXCEPTION_TYPE_ATTR_FORMAT = "Attribute Format Error";

//=============================================================================
//=============================================================================
HdbPPCassandra::HdbPPCassandra(vector<string> configuration)
{
    // cout << __func__<<": VERSION: " << version_string << " file:" << __FILE__rev << endl;

    mp_cluster = NULL;
    map<string, string> libhdb_conf;
    string_vector2map(configuration, "=", &libhdb_conf);
    string contact_points, user, password;
    string local_dc = LOCAL_DC_DEFAULT;

    // ---- logging_enabled optional config parameter ----
    try
    {
        string logging_enabled_state = libhdb_conf.at("logging_enabled");
        logging_enabled_state == "true" ? logging_enabled = true : logging_enabled = false;
    }
    catch (const std::out_of_range &e)
    {
        // setting logging to false by default
        logging_enabled = false;
    }

    // ---- cassandra_driver_log_level optional config parameter ----
    try
    {
        string cassandra_driver_log_level = libhdb_conf.at("cassandra_driver_log_level");
        set_cassandra_logging_level(cassandra_driver_log_level);
    }
    catch (const std::out_of_range &e)
    {
        // default is disabled
        DEBUG_MSG("cassandra_driver_log_level configuration parameter is not defined. Default value will be used."
                  << endl);

        cass_log_set_level(CASS_LOG_DISABLED);
    }

    // ---- Mandatory configuration parameters ----
    try
    {
        contact_points = libhdb_conf.at("contact_points");
        DEBUG_MSG("Configuration \"contact_points\" set to: " << contact_points << ends);
    }
    catch (const std::out_of_range &e)
    {
        stringstream error_desc;

        error_desc << "Configuration parsing error: \"contact_points\" mandatory configuration parameter not found"
                   << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_CONFIG, error_desc.str(), __func__);
    }

    try
    {
        m_keyspace_name = libhdb_conf.at("keyspace");
        DEBUG_MSG("contact_points=\"" << contact_points << "\"" << endl);
        DEBUG_MSG("keyspace=\"" << m_keyspace_name << "\"" << endl);
    }
    catch (const std::out_of_range &e)
    {
        stringstream error_desc;

        error_desc << "Configuration parsing error: \"keyspace\" mandatory configuration parameter not found"
                   << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_CONFIG, error_desc.str(), __func__);
    }

    // ---- optional config parameters ----
    try
    {
        user = libhdb_conf.at("user");
    }
    catch (const std::out_of_range &e)
    {
        DEBUG_MSG("Library configuration parameter \"user\" is not defined. " << endl);
    }

    try
    {
        password = libhdb_conf.at("password");
    }
    catch (const std::out_of_range &e)
    {
        DEBUG_MSG("Library configuration parameter \"password\" is not defined. " << endl);
    }

    try
    {
        local_dc = libhdb_conf.at("local_dc");
    }
    catch (const std::out_of_range &e)
    {
        DEBUG_MSG("Library configuration parameter \"local_dc\" is not defined. Default value DC1 will be used."
                  << endl);
    }

    mp_cluster = cass_cluster_new();
    cass_cluster_set_contact_points(mp_cluster, contact_points.c_str());

    if (user.empty() && password.empty())
    {
        // No authentication
    }
    else if (user.empty() && !password.empty())
    {
        ERROR_MSG("A password was provided for the Cassandra connection, but no user name was provided!" << endl);
        ERROR_MSG("Will try to connect anonymously" << endl);
    }
    else
    {
        cass_cluster_set_credentials(mp_cluster, user.c_str(), password.c_str());
    }

    /* Latency-aware routing enabled with the default settings */
    cass_cluster_set_latency_aware_routing(mp_cluster, cass_true);

    cass_cluster_set_load_balance_dc_aware(mp_cluster, local_dc.c_str(), USED_HOSTS_PER_REMOTE_DC,
                                           ALLOW_REMOTE_DCS_FOR_LOCAL_CL);

    // connect to the cassandra session
    connect_session();
}

//=============================================================================
//=============================================================================
HdbPPCassandra::~HdbPPCassandra()
{
    TRACE_MSG("Entering");

    if (mp_cluster)
    {
        CassFuture *close_future = NULL;
        close_future = cass_session_close(mp_session);
        cass_future_wait(close_future);
        cass_future_free(close_future);
        cass_cluster_free(mp_cluster);
        cass_session_free(mp_session);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::connect_session()
{
    TRACE_MSG("Entering");

    mp_session = cass_session_new();
    CassFuture *future = cass_session_connect(mp_session, mp_cluster);
    cass_future_wait(future);

    CassError rc = cass_future_error_code(future);
    cass_future_free(future);

    if (rc != CASS_OK)
    {
        ERROR_MSG("Cassandra connect session error: " << cass_error_desc(rc) << endl);
        stringstream error_desc;
        error_desc << "Error connecting to the Cassandra Cluster: " << cass_error_desc(rc) << ends;
        cass_cluster_free(mp_cluster);
        mp_cluster = NULL;
        Tango::Except::throw_exception(EXCEPTION_TYPE_CONNECTION, error_desc.str(), __func__);
    }
    else
    {
        DEBUG_MSG("Cassandra connection OK" << endl);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_attr_id(string fqdn_attr_name, CassUuid &ID)
{
    TRACE_MSG("Entering");

    // First look into the cache
    map<string, struct ConfParams>::iterator it = attr_ID_map.find(fqdn_attr_name);
    if (it == attr_ID_map.end())
    {
        // if not already present in cache, look for ID in the DB
        unsigned int ttl = 0;
        if (!find_attr_id_and_ttl_in_db(fqdn_attr_name, ID, ttl))
        {
            DEBUG_MSG("ID not found for attr=" << fqdn_attr_name << endl);
            TRACE_MSG("Leaving");
            return false;
        }
    }
    else
    {
        ID = it->second.id;
    }

    TRACE_MSG("Leaving");
    return true;
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_attr_id_and_ttl(string fqdn_attr_name, CassUuid &ID, unsigned int &ttl)
{
    TRACE_MSG("Entering");

    // First look into the cache
    map<string, struct ConfParams>::iterator it = attr_ID_map.find(fqdn_attr_name);
    if (it == attr_ID_map.end())
    {
        // if not already present in cache, look for ID in the DB
        if (!find_attr_id_and_ttl_in_db(fqdn_attr_name, ID, ttl))
        {
            DEBUG_MSG("ID not found for attr=" << fqdn_attr_name << endl);
            TRACE_MSG("Leaving");
            return false;
        }
    }
    else
    {
        ID = it->second.id;
        ttl = it->second.ttl;
    }
    TRACE_MSG("Leaving");
    return true;
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_attr_id_and_ttl_in_db(string fqdn_attr_name, CassUuid &ID, unsigned int &ttl)
{
    TRACE_MSG("Entering");

    ostringstream query_str;
    string facility = get_only_tango_host(fqdn_attr_name);
    string attr_name = get_only_attr_name(fqdn_attr_name);
    facility = add_domain(facility);

    query_str << "SELECT " << CONF_COL_ID << ", " << CONF_COL_TTL << " FROM " << m_keyspace_name
              << "." << CONF_TABLE_NAME << " WHERE " << CONF_COL_NAME << " = ? AND "
              << CONF_COL_FACILITY << " = ?" << ends;

    CassStatement *statement = cass_statement_new(query_str.str().c_str(), 2);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);
    cass_statement_bind_string(statement, 0, attr_name.c_str());
    cass_statement_bind_string(statement, 1, facility.c_str());

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    if (rc != CASS_OK)
    {
        cass_future_free(future);
        cass_statement_free(statement);
        throw_execute_exception("ERROR executing select query", query_str.str(), rc, __func__);
    }

    const CassResult *result = cass_future_get_result(future);
    CassIterator *iterator = cass_iterator_from_result(result);
    bool found = false;

    if (cass_iterator_next(iterator))
    {
        DEBUG_MSG("SUCCESS in query: " << query_str.str() << "(" << CONF_COL_NAME << "=\'" << attr_name << "\',"
                                       << CONF_COL_FACILITY << "=\'" << facility << "\')" << endl);

        const CassRow *row = cass_iterator_get_row(iterator);
        cass_value_get_uuid(cass_row_get_column(row, 0), &ID);
        cass_int32_t ttl_val = 0;

        const CassValue *cass_ttl_val = cass_row_get_column_by_name(row, CONF_COL_TTL.c_str());

        if (cass_ttl_val == NULL)
        {
            DEBUG_MSG("Column ttl does not exist" << endl);
        }
        else
        {
            DEBUG_MSG("OK . value_type() = " << cass_value_type(cass_ttl_val) << endl);
        }

        CassError get_ttl_cass_error =
            cass_value_get_int32(cass_row_get_column_by_name(row, CONF_COL_TTL.c_str()), &ttl_val);

        if (get_ttl_cass_error != CASS_OK)
        {
            if (get_ttl_cass_error == CASS_ERROR_LIB_NULL_VALUE)
            {
                // The ttl was null, perhaps it was removed, or missing due to an
                // upgrade. This is not an error condition
                DEBUG_MSG("TTL value is NULL" << endl);
            }
            else
            {
                cass_result_free(result);
                cass_iterator_free(iterator);
                cass_future_free(future);
                cass_statement_free(statement);

                stringstream error_desc;

                error_desc << "An unexpected database error occured when trying to retrieve the TTL: "
                           << cass_error_desc(get_ttl_cass_error)
                           << " for attribute: " << fqdn_attr_name << ends;

                ERROR_MSG(error_desc.str() << endl);
                Tango::Except::throw_exception(EXCEPTION_TYPE_QUERY, error_desc.str(), __func__);
            }

            ttl = 0;
        }
        else
        {
            DEBUG_MSG("cass_value_get_int32 returned " << ttl_val << endl);
            ttl = ttl_val;
        }

        struct ConfParams conf_param;
        conf_param.id = ID;
        conf_param.ttl = ttl;

        DEBUG_MSG("(" << fqdn_attr_name << "): ID found " << endl);
        DEBUG_MSG("(" << fqdn_attr_name << "): TTL = " << ttl << endl);

        found = true;
        attr_ID_map.insert(make_pair(fqdn_attr_name, conf_param));
        map<string, struct ConfParams>::iterator it = attr_ID_map.find(fqdn_attr_name);

        if (it == attr_ID_map.end())
        {
            char uuid_str[CASS_UUID_STRING_LENGTH];
            cass_uuid_string(ID, uuid_str);
            ERROR_MSG("ID (" << uuid_str << ") could not be added into cache for attr =" << fqdn_attr_name << endl);
        }
    }

    cass_result_free(result);
    cass_iterator_free(iterator);
    cass_future_free(future);
    cass_statement_free(statement);

    if (!found)
        DEBUG_MSG("(" << fqdn_attr_name << "): NO RESULT in query: " << query_str.str() << endl);
    TRACE_MSG("Leaving");
    return found;
}

//=============================================================================
//=============================================================================
HdbPPCassandra::FindAttrResult HdbPPCassandra::find_attr_id_type_and_ttl(
    string facility, string attr, CassUuid &ID, string attr_type, unsigned int &conf_ttl)
{
    TRACE_MSG("Entering");

    ostringstream query_str;

    query_str << "SELECT " << CONF_COL_ID << "," << CONF_COL_TYPE << "," << CONF_COL_TTL << " FROM "
              << m_keyspace_name << "." << CONF_TABLE_NAME << " WHERE " << CONF_COL_NAME
              << " = ? AND " << CONF_COL_FACILITY << " = ?" << ends;

    DEBUG_MSG(": query = " << query_str.str().c_str() << endl);
    DEBUG_MSG("facility = \"" << facility << "\"" << endl);
    DEBUG_MSG("attr = \"" << attr << "\"" << endl);

    CassStatement *statement = cass_statement_new(query_str.str().c_str(), 2);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);
    cass_statement_bind_string(statement, 0, attr.c_str());
    cass_statement_bind_string(statement, 1, facility.c_str());

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    if (rc != CASS_OK)
    {
        cass_future_free(future);
        cass_statement_free(statement);
        throw_execute_exception(EXCEPTION_TYPE_QUERY, query_str.str(), rc, __func__);
    }

    const CassResult *result = cass_future_get_result(future);
    CassIterator *iterator = cass_iterator_from_result(result);
    bool found = false;
    string db_type = "";

    if (cass_iterator_next(iterator))
    {
        DEBUG_MSG("SUCCESS in query: " << query_str.str() << endl);

        // TODO Improve error handling
        const CassRow *row = cass_iterator_get_row(iterator);
        cass_value_get_uuid(cass_row_get_column(row, 0), &ID);
        const char *db_type_res;
        size_t db_type_res_length;
        cass_value_get_string(cass_row_get_column(row, 1), &db_type_res, &db_type_res_length);
        db_type = string(db_type_res, db_type_res_length);
        cass_int32_t ttl_val = 0;

        if (cass_value_get_int32(cass_row_get_column(row, 2), &ttl_val) == CASS_ERROR_LIB_NULL_VALUE)
            conf_ttl = 0;
        else
            conf_ttl = (unsigned int)ttl_val;

        found = true;
    }

    cass_result_free(result);
    cass_iterator_free(iterator);
    cass_future_free(future);
    cass_statement_free(statement);

    if (!found)
    {
        DEBUG_MSG("NO RESULT in query: " << query_str.str() << endl);
        TRACE_MSG("Leaving");
        return AttrNotFound;
    }
    else if (db_type != attr_type)
    {
        DEBUG_MSG("FOUND ID for " << facility << "/" << attr << " but different type: attr_type=" << attr_type
                                  << "-db_type=" << db_type << endl);
        TRACE_MSG("Leaving");
        return FoundAttrWithDifferentType;
    }

    DEBUG_MSG("FOUND ID for " << facility << "/" << attr << " with SAME type: attr_type=" << attr_type
                              << "-db_type=" << db_type << endl);
    TRACE_MSG("Leaving");
    return FoundAttrWithSameType;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_bool(CassStatement *statement,
                                           int &param_index,
                                           int data_format /*SCALAR, SPECTRUM, ..*/,
                                           Tango::EventData *data,
                                           enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<bool> vbool;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(vbool);
    else
        extract_success = data->attr_value->extract_set(vbool);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_bool(statement, param_index, vbool[0] ? cass_true : cass_false);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, vbool.size());
            for (unsigned int i = 0; i < vbool.size(); i++)
            {
                cass_collection_append_bool(read_values_list, vbool[i] ? cass_true : cass_false);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_uchar(CassStatement *statement,
                                            int &param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<unsigned char> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int32(statement, param_index, val[0] & 0xFF);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int32(read_values_list, val[i] & 0xFF);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_short(CassStatement *statement,
                                            int &param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<short> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int32(statement, param_index, val[0]);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int32(read_values_list, val[i]);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_ushort(CassStatement *statement,
                                             int &param_index,
                                             int data_format /*SCALAR, SPECTRUM, ..*/,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<unsigned short> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int32(statement, param_index, val[0]);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int32(read_values_list, val[i]);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_long(CassStatement *statement,
                                           int &param_index,
                                           int data_format /*SCALAR, SPECTRUM, ..*/,
                                           Tango::EventData *data,
                                           enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<int> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int32(statement, param_index, val[0]);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int32(read_values_list, val[i]);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_ulong(CassStatement *statement,
                                            int &param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<unsigned int> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int64(statement, param_index, val[0]); // TODO? Bind to int32?
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int64(read_values_list, val[i]); // TODO? Bind to int32?
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_long64(CassStatement *statement,
                                             int &param_index,
                                             int data_format /*SCALAR, SPECTRUM, ..*/,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<int64_t> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int64(statement, param_index, val[0]);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int64(read_values_list, val[i]);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_ulong64(CassStatement *statement,
                                              int &param_index,
                                              int data_format /*SCALAR, SPECTRUM, ..*/,
                                              Tango::EventData *data,
                                              enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<uint64_t> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int64(statement, param_index, val[0]); // TODO? Test extreme values!
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int64(read_values_list,
                                             val[i]); // TODO? Test extreme values!
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_string(CassStatement *statement,
                                             int &param_index,
                                             int data_format /*SCALAR, SPECTRUM, ..*/,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<string> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_string(statement, param_index, val[0].c_str());
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_string(read_values_list, val[i].c_str());
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_state(CassStatement *statement,
                                            int &param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<Tango::DevState> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
    {
        if (data_format == Tango::SCALAR)
        {
            // We cannot use the extract_read() method for the "State" attribute
            Tango::DevState st;
            *data->attr_value >> st;
            cass_statement_bind_int32(statement, param_index, (int8_t)st);
            return;
        }
        // ARRAY case:
        extract_success = data->attr_value->extract_read(val);
    }
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            cass_statement_bind_int32(statement, param_index, (int8_t)val[0]);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                cass_collection_append_int32(read_values_list, (int8_t)val[i]);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list);
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_encoded(CassStatement *statement,
                                              int &param_index,
                                              int data_format /*SCALAR, SPECTRUM, ..*/,
                                              Tango::EventData *data,
                                              enum extract_t extract_type)
{
    TRACE_MSG("Entering");

    // TODO: Not yet supported
    ERROR_MSG("DevEncoded type is not yet supported..." << endl);
    // 	vector<Tango::DevEncoded> val;
    // 	bool extract_success = false;
    //
    // 	if(extract_type == EXTRACT_READ)
    // 		extract_success = data->attr_value->extract_read(val);
    // 	else
    // 		extract_success = data->attr_value->extract_set(val);
    //
    // 	if(extract_success)
    // 	{
    // 		if(data_format == Tango::SCALAR)
    // 		{
    // 			cass_statement_bind_int32(statement, param_index, val[0]);
    // 		}
    // 		else
    // 		{
    // 			cout << __func__<< ": DevEncoded type is not yet supported..." << endl;
    // 			// Store the array into a CQL list
    // 			CassCollection* readValuesList = NULL;
    // 			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
    // 			for(unsigned int i = 0; i < val.size(); i++)
    // 			{
    // 			  cass_collection_append_int32(readValuesList, (int8_t)val[i]);
    // 			}
    // 			cass_statement_bind_collection(statement, param_index, readValuesList);
    // 			cass_collection_free(readValuesList);
    // 		}
    // 	}
    // 	else
    // 	{
    // 		cass_statement_bind_null(statement,param_index);
    // 	}
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_float(CassStatement *statement,
                                            int &param_index,
                                            int data_format /* Tango::SCALAR, ... */,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<float> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            if (std::isnan(val[0]))
                cass_statement_bind_float(statement, param_index, std::numeric_limits<float>::quiet_NaN());
            else if (std::isinf(val[0]))
                cass_statement_bind_float(statement, param_index, std::numeric_limits<float>::infinity());
            else
                cass_statement_bind_float(statement, param_index, val[0]);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                if (std::isnan(val[i]))
                    cass_collection_append_float(read_values_list, std::numeric_limits<float>::quiet_NaN());
                else if (std::isinf(val[i]))
                    cass_collection_append_float(read_values_list, std::numeric_limits<float>::infinity());
                else
                    cass_collection_append_float(read_values_list, val[i]);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list); // value_r
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_double(CassStatement *statement,
                                             int &param_index,
                                             int data_format /* Tango::SCALAR, ... */,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
    TRACE_MSG("Entering");
    vector<double> val;
    bool extract_success = false;

    if (extract_type == EXTRACT_READ)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    if (extract_success)
    {
        if (data_format == Tango::SCALAR)
        {
            if (std::isnan(val[0]))
                cass_statement_bind_double(statement, param_index, std::numeric_limits<double>::quiet_NaN());
            else if (std::isinf(val[0]))
                cass_statement_bind_double(statement, param_index, std::numeric_limits<double>::infinity());
            else
                cass_statement_bind_double(statement, param_index, val[0]);
        }
        else
        {
            // Store the array into a CQL list
            CassCollection *read_values_list = NULL;
            read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
            for (unsigned int i = 0; i < val.size(); i++)
            {
                if (std::isnan(val[i]))
                    cass_collection_append_double(read_values_list, std::numeric_limits<double>::quiet_NaN());
                else if (std::isinf(val[i]))
                    cass_collection_append_double(read_values_list, std::numeric_limits<double>::infinity());
                else
                    cass_collection_append_double(read_values_list, val[i]);
            }
            cass_statement_bind_collection(statement, param_index, read_values_list); // value_r
            cass_collection_free(read_values_list);
        }
    }
    else
    {
        cass_statement_bind_null(statement, param_index);
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_EXTRACT, error_desc.str(), __func__);
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::extract_and_bind_rw_values(CassStatement *statement,
                                                int &param_index,
                                                int data_type,
                                                int write_type /*READ, READ_WRITE, ..*/,
                                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                                Tango::EventData *data,
                                                bool isNull)
{
    TRACE_MSG("Entering");

    if (write_type != Tango::WRITE)
    {
        if (isNull)
        {
            // There is no value to bind.
            // This is to avoid storing NULL values into Cassandra (<=> tombstones)
            TRACE_MSG("Leaving");
            return;
        }
        else
        {
            // There is a read value
            switch (data_type)
            {
                case Tango::DEV_BOOLEAN:
                    extract_and_bind_bool(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_UCHAR:
                    extract_and_bind_uchar(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_SHORT:
                    extract_and_bind_short(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_USHORT:
                    extract_and_bind_ushort(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_LONG:
                    extract_and_bind_long(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_ULONG:
                    extract_and_bind_ulong(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_LONG64:
                    extract_and_bind_long64(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_ULONG64:
                    extract_and_bind_ulong64(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_FLOAT:
                    extract_and_bind_float(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_DOUBLE:
                    extract_and_bind_double(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_STRING:
                    extract_and_bind_string(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_STATE:
                    extract_and_bind_state(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                case Tango::DEV_ENCODED:
                    extract_and_bind_encoded(statement, param_index, data_format, data, EXTRACT_READ);
                    break;
                default:
                {
                    stringstream error_desc;
                    error_desc << "Attribute " << data->attr_name << " type (" << (int)(data_type)
                               << ")) not supported" << ends;
                    ERROR_MSG(error_desc.str() << endl);
                    Tango::Except::throw_exception(EXCEPTION_TYPE_UNSUPPORTED_ATTR, error_desc.str(), __func__);
                }
            } // switch(data_type)
        }
        param_index++;
    }

    if (write_type != Tango::READ)
    {
        if (isNull)
        {
            // There is no value to bind.
            // This is to avoid storing NULL values into Cassandra (<=> tombstones)
            TRACE_MSG("Leaving");
            return;
        }
        else
        {
            // There is a write value
            switch (data_type)
            {
                case Tango::DEV_BOOLEAN:
                    extract_and_bind_bool(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_UCHAR:
                    extract_and_bind_uchar(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_SHORT:
                    extract_and_bind_short(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_USHORT:
                    extract_and_bind_ushort(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_LONG:
                    extract_and_bind_long(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_ULONG:
                    extract_and_bind_ulong(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_LONG64:
                    extract_and_bind_long64(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_ULONG64:
                    extract_and_bind_ulong64(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_FLOAT:
                    extract_and_bind_float(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_DOUBLE:
                    extract_and_bind_double(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_STRING:
                    extract_and_bind_string(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_STATE:
                    extract_and_bind_state(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                case Tango::DEV_ENCODED:
                    extract_and_bind_encoded(statement, param_index, data_format, data, EXTRACT_SET);
                    break;
                default:
                {
                    stringstream error_desc;
                    error_desc << "Attribute " << data->attr_name << " type (" << (int)(data_type)
                               << ")) not supported" << ends;
                    ERROR_MSG(error_desc.str() << endl);
                    Tango::Except::throw_exception(EXCEPTION_TYPE_UNSUPPORTED_ATTR, error_desc.str(), __func__);
                }
            } // switch(data_type)
        }
        param_index++;
    }

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_last_event(const CassUuid &id, string &last_event, const string &fqdn_attr_name)
{
    TRACE_MSG("Entering");
    ostringstream query_str;
    last_event = "??";

    query_str << "SELECT " << HISTORY_COL_EVENT << " FROM " << m_keyspace_name << "."
              << HISTORY_TABLE_NAME << " WHERE " << HISTORY_COL_ID << " = ?"
              << " ORDER BY " << HISTORY_COL_TIME << " DESC LIMIT 1" << ends;

    CassStatement *statement = cass_statement_new(query_str.str().c_str(), 1);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable?
    cass_statement_bind_uuid(statement, 0, id); // att_conf_id

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    if (rc != CASS_OK)
    {
        cass_future_free(future);
        cass_statement_free(statement);
        throw_execute_exception("ERROR executing select query", query_str.str(), rc, __func__);
    }

    bool found = false;
    const CassResult *result = cass_future_get_result(future);
    CassIterator *iterator = cass_iterator_from_result(result);

    if (cass_iterator_next(iterator))
    {
        DEBUG_MSG("SUCCESS in query: " << query_str.str() << endl);

        const CassRow *row = cass_iterator_get_row(iterator);
        const char *last_event_res;
        size_t last_event_res_length;
        cass_value_get_string(cass_row_get_column(row, 0), &last_event_res, &last_event_res_length);
        last_event = string(last_event_res, last_event_res_length);

        found = true;
        DEBUG_MSG("(" << fqdn_attr_name << "): last event = " << last_event << endl);
    }

    cass_result_free(result);
    cass_iterator_free(iterator);
    cass_future_free(future);
    cass_statement_free(statement);

    if (!found)
        DEBUG_MSG("(" << fqdn_attr_name << "): NO RESULT in query: " << query_str.str() << endl);
    TRACE_MSG("Leaving");
    return found;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type)
{
    TRACE_MSG("Entering");

    if (data == NULL)
    {
        stringstream error_desc;
        error_desc << "Unexpected null Tango::EventData" << ends;
        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }

    string fqdn_attr_name = data->attr_name;
    int64_t ev_time;
    int ev_time_us;
    int64_t rcv_time = ((int64_t)data->get_date().tv_sec) * 1000;
    int rcv_time_us = data->get_date().tv_usec;
    int quality = (int)data->attr_value->get_quality();
    string error_desc("");

    Tango::AttributeDimension attr_w_dim;
    Tango::AttributeDimension attr_r_dim;
    int data_type = ev_data_type.data_type; // data->attr_value->get_type()
    Tango::AttrDataFormat data_format = ev_data_type.data_format;
    int write_type = ev_data_type.write_type;
    // int max_dim_x = ev_data_type.max_dim_x;
    // int max_dim_y = ev_data_type.max_dim_y;

    bool is_null = false;
    if (data->err)
    {
        DEBUG_MSG("Attribute in error:" << error_desc << endl);

        is_null = true;
        // Store the error description
        error_desc = data->errors[0].desc;
    }

    data->attr_value->reset_exceptions(
        Tango::DeviceAttribute::isempty_flag); // disable is_empty exception
    if (data->attr_value->is_empty())
    {
        DEBUG_MSG("no value will be archived... (Attr Value is empty)" << endl);
        is_null = true;
    }
    if (quality == Tango::ATTR_INVALID)
    {
        DEBUG_MSG("no value will be archived... (Invalid Attribute)" << endl);
        is_null = true;
    }

    DEBUG_MSG("data_type=" << data_type << " data_format=" << data_format << " write_type=" << write_type << endl);

    if (!is_null)
    {
        attr_w_dim = data->attr_value->get_w_dimension();
        attr_r_dim = data->attr_value->get_r_dimension();
        ev_time = ((int64_t)data->attr_value->get_date().tv_sec) * 1000;
        ev_time_us = data->attr_value->get_date().tv_usec;
    }
    else
    {
        attr_r_dim.dim_x = 0; // max_dim_x;//TODO: OK?
        attr_w_dim.dim_x = 0; // max_dim_x;//TODO: OK?
        attr_r_dim.dim_y = 0; // max_dim_y;//TODO: OK?
        attr_w_dim.dim_y = 0; // max_dim_y;//TODO: OK?
        ev_time = rcv_time;
        ev_time_us = rcv_time_us;
    }

    string facility = get_only_tango_host(fqdn_attr_name);
    string attr_name = get_only_attr_name(fqdn_attr_name);
    facility = add_domain(facility);

    CassUuid id;
    unsigned int ttl = 0;

    if (!find_attr_id_and_ttl(fqdn_attr_name, id, ttl))
    {
        ERROR_MSG("Could not find ID for attribute " << fqdn_attr_name << endl);
        stringstream error_desc;
        error_desc << "ERROR Could not find ID for attribute  \"" << fqdn_attr_name << "\": " << ends;
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str().c_str(), __func__);
    }

    int nb_query_params = 0;
    string query_str =
        get_insert_query_str(data_type, data_format, write_type, nb_query_params, is_null, ttl);

    CassStatement *statement = cass_statement_new(query_str.c_str(), nb_query_params);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);
    cass_statement_bind_uuid(statement, 0, id); // att_conf_id

    // Compute the period based on the month of the event time
    struct tm *tms;
    time_t ev_time_s = ev_time / 1000;

    if ((tms = localtime(&ev_time_s)) == NULL)
        perror("localtime");

    char period[11];
    snprintf(period, 11, "%04d-%02d-%02d", tms->tm_year + 1900, tms->tm_mon + 1, tms->tm_mday);

    cass_statement_bind_string(statement, 1, period); // period
    cass_statement_bind_int64(statement, 2, ev_time); // event_time
    cass_statement_bind_int32(statement, 3, ev_time_us); // event_time_us
    cass_statement_bind_int64(statement, 4, rcv_time); // recv_time
    cass_statement_bind_int32(statement, 5, rcv_time_us); // recv_time_us

    // Get the current time
    struct timespec ts;
    if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
    {
        // TODO handle this error?
        perror("clock_gettime()");
    }

    int64_t insert_time = ((int64_t)ts.tv_sec) * 1000;
    int insert_time_us = ts.tv_nsec / 1000;
    cass_statement_bind_int64(statement, 6, insert_time); // insert_time
    cass_statement_bind_int32(statement, 7, insert_time_us); // insert_time_us
    cass_statement_bind_int32(statement, 8, quality); // quality
    cass_statement_bind_string(statement, 9, error_desc.c_str()); // error description
    int param_index = 10;

    extract_and_bind_rw_values(statement, param_index, data_type, write_type, data_format, data, is_null);

    if (ttl != 0)
        cass_statement_bind_uint32(statement, param_index, ttl * 3600); // TTL

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", query_str, rc, __func__);

    DEBUG_MSG("SUCCESS in query: " << query_str << endl);
    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::insert_history_event(const string &history_event_name, CassUuid att_conf_id)
{
    TRACE_MSG("Entering");

    if (logging_enabled)
    {
        char att_conf_id_str[CASS_UUID_STRING_LENGTH];
        cass_uuid_string(att_conf_id, &att_conf_id_str[0]);
        DEBUG_MSG("(" << history_event_name << "," << att_conf_id_str << ": entering... " << endl);
    }

    struct timespec ts;
    if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
    {
        // TODO handle this error?
        perror("clock_gettime()");
    }
    int64_t current_time = ((int64_t)ts.tv_sec) * 1000;
    int current_time_us = ts.tv_nsec / 1000;

    ostringstream insert_event_str;

    insert_event_str << "INSERT INTO " << m_keyspace_name << "." << HISTORY_TABLE_NAME << " ("
                     << HISTORY_COL_ID << "," << HISTORY_COL_EVENT << "," << HISTORY_COL_TIME << ","
                     << HISTORY_COL_TIME_US << ")"
                     << " VALUES ( ?, ?, ?, ?)" << ends;

    CassStatement *statement = cass_statement_new(insert_event_str.str().c_str(), 4);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable?
    cass_statement_bind_uuid(statement, 0, att_conf_id);
    cass_statement_bind_string(statement, 1, history_event_name.c_str());
    cass_statement_bind_int64(statement, 2, current_time);
    cass_statement_bind_int32(statement, 3, current_time_us);

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", insert_event_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::insert_param_Attr(Tango::AttrConfEventData *data, HdbEventDataType ev_data_type)
{
    TRACE_MSG("Entering");

    if (data == NULL)
    {
        stringstream error_desc;
        error_desc << "Unexpected null Tango::AttrConfEventData" << ends;
        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }

    if (data->attr_conf == NULL)
    {
        stringstream error_desc;
        error_desc << "Unexpected null in Tango::AttrConfEventData field attr_conf" << ends;
        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }

    string fqdn_attr_name = data->attr_name;
    int64_t ev_time = ((int64_t)data->get_date().tv_sec) * 1000;
    int ev_time_us = data->get_date().tv_usec;

    CassUuid uuid;
    string facility = get_only_tango_host(fqdn_attr_name);
    string attr_name = get_only_attr_name(fqdn_attr_name);
    facility = add_domain(facility);

    if (!find_attr_id(fqdn_attr_name, uuid))
    {
        ERROR_MSG("Could not find ID for attribute " << fqdn_attr_name << endl);
        stringstream error_desc;
        error_desc << "ERROR Could not find ID for attribute  \"" << fqdn_attr_name << "\": " << ends;
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str().c_str(), __func__);
    }
    else
    {
        char uuid_str[CASS_UUID_STRING_LENGTH];
        cass_uuid_string(uuid, uuid_str);
        DEBUG_MSG("ID found for attribute " << fqdn_attr_name << " = " << uuid_str << endl);
    }

    ostringstream query_str;

    query_str << "INSERT INTO " << m_keyspace_name << "." << PARAM_TABLE_NAME << " (" << PARAM_COL_ID
              << "," << PARAM_COL_EV_TIME << "," << PARAM_COL_EV_TIME_US << "," << PARAM_COL_INS_TIME
              << "," << PARAM_COL_INS_TIME_US << "," << PARAM_COL_LABEL << "," << PARAM_COL_UNIT << ","
              << PARAM_COL_STANDARDUNIT << "," << PARAM_COL_DISPLAYUNIT << "," << PARAM_COL_FORMAT
              << "," << PARAM_COL_ARCHIVERELCHANGE << "," << PARAM_COL_ARCHIVEABSCHANGE << ","
              << PARAM_COL_ARCHIVEPERIOD << "," << PARAM_COL_DESCRIPTION << ")";

    query_str << " VALUES (?,?,?,"
              << "?,?,"
              << "?,?,?,"
              << "?,?,?,"
              << "?,?,?)" << ends;

    CassStatement *statement = cass_statement_new(query_str.str().c_str(), 14);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);
    cass_statement_bind_uuid(statement, 0, uuid);

    // Get the current time
    struct timespec ts;
    if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
    {
        // TODO (SJ) Is this a bad error?
        perror("clock_gettime()");
    }

    int64_t insert_time = ((int64_t)ts.tv_sec) * 1000;
    int insert_time_us = ts.tv_nsec / 1000;
    cass_statement_bind_int64(statement, 1, ev_time); // recv_time
    cass_statement_bind_int32(statement, 2, ev_time_us); // recv_time_us
    cass_statement_bind_int64(statement, 3, insert_time); // insert_time
    cass_statement_bind_int32(statement, 4, insert_time_us); // insert_time_us

    if (logging_enabled)
    {
        cout << __func__ << " label: \"" << data->attr_conf->label.c_str() << "\"" << endl;
        cout << __func__ << " unit: \"" << data->attr_conf->unit.c_str() << "\"" << endl;
        cout << __func__ << " standard unit: \"" << data->attr_conf->standard_unit.c_str() << "\"" << endl;
        cout << __func__ << " display unit: \"" << data->attr_conf->display_unit.c_str() << "\"" << endl;
        cout << __func__ << " format: \"" << data->attr_conf->format.c_str() << "\"" << endl;
        cout << __func__ << " archive rel change: \""
             << data->attr_conf->events.arch_event.archive_rel_change.c_str() << "\"" << endl;
        cout << __func__ << " archive abs change: \""
             << data->attr_conf->events.arch_event.archive_abs_change.c_str() << "\"" << endl;
        cout << __func__ << " archive period: \""
             << data->attr_conf->events.arch_event.archive_period.c_str() << "\"" << endl;
        cout << __func__ << " description: \"" << data->attr_conf->description.c_str() << "\"" << endl;
        cout << __func__ << " after binding description" << endl;
    }

    cass_statement_bind_string(statement, 5, data->attr_conf->label.c_str()); // label
    cass_statement_bind_string(statement, 6, data->attr_conf->unit.c_str()); // unit
    cass_statement_bind_string(statement, 7, data->attr_conf->standard_unit.c_str()); // standard unit
    cass_statement_bind_string(statement, 8, data->attr_conf->display_unit.c_str()); // display unit
    cass_statement_bind_string(statement, 9, data->attr_conf->format.c_str()); // format
    cass_statement_bind_string(statement, 10, data->attr_conf->events.arch_event.archive_rel_change.c_str()); // archive relative range
    cass_statement_bind_string(statement, 11, data->attr_conf->events.arch_event.archive_abs_change.c_str()); // archive abs change
    cass_statement_bind_string(statement, 12, data->attr_conf->events.arch_event.archive_period.c_str()); // archive period
    cass_statement_bind_string(statement, 13, data->attr_conf->description.c_str()); // description
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable? => would
    // be useful!

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", query_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::configure_Attr(string name,
                                    int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                    int format /*SCALAR, SPECTRUM, ..*/,
                                    int write_type /*READ, READ_WRITE, ..*/,
                                    unsigned int ttl /* hours, 0=infinity*/)
{
    TRACE_MSG("Entering");

    string facility = get_only_tango_host(name);
    facility = add_domain(facility);
    string attr_name = get_only_attr_name(name);
    string domain = "";
    string family = "";
    string member = "";
    string attribute_name = "";

    if (get_domain_fam_memb_name(attr_name, domain, family, member, attribute_name) < 0)
    {
        stringstream error_desc;
        error_desc << "ERROR parsing attribute name  \"" << attr_name << "\": " << ends;
        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_ATTR_FORMAT, error_desc.str().c_str(), __func__);
    }

    DEBUG_MSG("name=" << name << " -> facility=" << facility << " attr_name=" << attr_name << endl);

    string data_type = get_data_type(type, format, write_type);
    CassUuid id;
    unsigned int conf_ttl = 0;
    FindAttrResult find_attr_result = find_attr_id_type_and_ttl(facility, attr_name, id, data_type, conf_ttl);
    // ID already present but different configuration (attribute type)
    if (find_attr_result == FoundAttrWithDifferentType)
    {
        stringstream error_desc;

        error_desc << facility << "/" << attr_name << " already configured with different configuration."
                   << "Please contact your administrator for this special case" << ends;

        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }
    else
    {
        // ID found and same configuration (attribute type): do nothing
        if (find_attr_result == FoundAttrWithSameType)
        {
            DEBUG_MSG("ALREADY CONFIGURED with same configuration: " << facility << "/" << attr_name << endl);

            if (conf_ttl != ttl)
            {
                DEBUG_MSG(".... BUT different ttl: updating " << conf_ttl << " to " << ttl << endl);
                update_ttl(ttl, facility, attr_name);
            }
            // If the last event was EVENT_REMOVE, add it again
            string last_event;
            if (find_last_event(id, last_event, name) && last_event == EVENT_REMOVE)
            {
                // An attribute which was removed needs to be added again
                insert_history_event(EVENT_ADD, id);
            }
        }
        else
        {
            // insert into configuration table
            CassUuid uuid;
            insert_attr_conf(facility, attr_name, data_type, uuid, ttl);

            // Add ADD event into history table
            insert_history_event(EVENT_ADD, uuid);
        }
    }

    // Insert into domains table
    insert_domain(facility, domain);

    // Insert into families table
    insert_family(facility, domain, family);

    // Insert into members table
    insert_member(facility, domain, family, member);

    // Insert into att_names table
    insert_attr_name(facility, domain, family, member, attribute_name);

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::updateTTL_Attr(string fqdn_attr_name, unsigned int ttl /* hours, 0=infinity*/)
{
    TRACE_MSG("Entering");

    CassUuid uuid;

    if (!find_attr_id(fqdn_attr_name, uuid))
    {
        stringstream error_desc;
        error_desc << "ERROR Attribute " << fqdn_attr_name << " NOT FOUND in HDB++ configuration table" << ends;
        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str(), __func__);
    }

    string facility = get_only_tango_host(fqdn_attr_name);
    facility = add_domain(facility);
    string attr_name = get_only_attr_name(fqdn_attr_name);
    update_ttl(ttl, facility, attr_name);
    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::event_Attr(string fqdn_attr_name, unsigned char event)
{
    TRACE_MSG("Entering");

    ostringstream remove_event_str;
    CassUuid uuid;

    if (!find_attr_id(fqdn_attr_name, uuid))
    {
        stringstream error_desc;
        error_desc << "ERROR Attribute " << fqdn_attr_name << " NOT FOUND in HDB++ configuration table" << ends;
        ERROR_MSG(error_desc.str() << endl);
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str().c_str(), __func__);
    }

    string event_name = "";

    switch (event)
    {
        case DB_START:
        {
            string last_event;
            bool ret = find_last_event(uuid, last_event, fqdn_attr_name);
            if (ret && last_event == EVENT_START)
            {
                // It seems there was a crash
                insert_history_event(EVENT_CRASH, uuid);
            }
            event_name = EVENT_START;
            break;
        }
        case DB_STOP:
        {
            event_name = EVENT_STOP;
            break;
        }
        case DB_REMOVE:
        {
            event_name = EVENT_REMOVE;
            break;
        }
        case DB_PAUSE:
        {
            event_name = EVENT_PAUSE;
            break;
        }
        default:
        {
            stringstream error_desc;
            error_desc << "ERROR for " << fqdn_attr_name << " event=" << (int)event << " NOT SUPPORTED" << ends;
            ERROR_MSG(error_desc.str() << endl);
            Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str(), __func__);
        }
    }

    insert_history_event(event_name, uuid);
    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_insert_query_str(int tango_data_type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                            int data_format /* SCALAR, SPECTRUM */,
                                            int write_type /*READ, READ_WRITE, ..*/,
                                            int &nbQueryParams,
                                            bool isNull,
                                            unsigned int ttl) const
{
    TRACE_MSG("Entering");

    string table_name = get_table_name(tango_data_type, data_format, write_type);
    ostringstream query_str;
    nbQueryParams = 10;

    query_str << "INSERT INTO " << m_keyspace_name << "." << table_name << " (" << SC_COL_ID << ","
              << SC_COL_PERIOD << "," << SC_COL_EV_TIME << "," << SC_COL_EV_TIME_US << ","
              << SC_COL_RCV_TIME << "," << SC_COL_RCV_TIME_US << "," << SC_COL_INS_TIME << ","
              << SC_COL_INS_TIME_US << "," << SC_COL_QUALITY << "," << SC_COL_ERROR_DESC;

    // TODO: store dim_x, dim_y for spectrum attributes

    // We don't insert the value if the value is null because
    // it would create an unnecessary tombstone in Cassandra
    if (!isNull)
    {
        if (write_type != Tango::WRITE) // RO or RW
            query_str << "," << SC_COL_VALUE_R;

        if (write_type != Tango::READ) // RW or WO
            query_str << "," << SC_COL_VALUE_W;
    }

    query_str << ") VALUES (?,?,?,?,?,?,?,?,?,?";

    if (!isNull)
    {
        if (write_type != Tango::WRITE) // RO or RW
        {
            query_str << ",?";
            nbQueryParams++;
        }
        if (write_type != Tango::READ) // RW or WO
        {
            query_str << ",?";
            nbQueryParams++;
        }
    }

    query_str << ")";

    if (ttl != 0)
    {
        query_str << " USING TTL ?";
        nbQueryParams++;
    }

    query_str << ends;

    DEBUG_MSG("Query = \"" << query_str.str() << "\"" << endl);
    TRACE_MSG("Leaving");
    return query_str.str();
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_only_attr_name(string str)
{
    string::size_type start = str.find("tango://");
    if (start == string::npos)
        return str;
    else
    {
        start += 8; //	"tango://" length
        start = str.find('/', start);
        start++;
        string signame = str.substr(start);
        return signame;
    }
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_only_tango_host(string str)
{
    string::size_type start = str.find("tango://");
    if (start == string::npos)
    {
        return "unknown";
    }
    else
    {
        start += 8; //	"tango://" length
        string::size_type end = str.find('/', start);
        string th = str.substr(start, end - start);
        return th;
    }
}

//=============================================================================
/**
 * This method parses attr_name and extracts the domain,
 * family, member and attribute name from the provided string
 * @param attr_name attribute name under the form <domain>/<family>/<member>/<name>
 * @param domain the domain extracted from the provided attr_name parameter (<domain>)
 * @param family the family extracted from the provided attr_name parameter (<family>)
 * @param member the member extracted from the provided attr_name parameter (<member>)
 * @param name the attribute name extracted from the provided attr_name parameter (<name>)
 */
//=============================================================================
int HdbPPCassandra::get_domain_fam_memb_name(
    const string &attr_name, string &domain, string &family, string &member, string &name)
{
    string::size_type first_slash = attr_name.find("/");
    if (first_slash == string::npos)
    {
        ERROR_MSG("(" << attr_name << "): Error: there is no slash in attribute name" << endl);
        return ERR_NO_SLASH_IN_ATTR;
    }
    string::size_type second_slash = attr_name.find("/", first_slash + 1);
    if (second_slash == string::npos)
    {
        ERROR_MSG("(" << attr_name << "): Error: there is only one slash in attribute name" << endl);
        return ERR_ONLY_ONE_SLASH_IN_ATTR;
    }
    string::size_type third_slash = attr_name.find("/", second_slash + 1);
    if (third_slash == string::npos)
    {
        ERROR_MSG("(" << attr_name << "): Error: there are only two slashes in attribute name" << endl);
        return ERR_ONLY_TWO_SLASHES_IN_ATTR;
    }
    string::size_type last_slash = attr_name.rfind("/");
    if (last_slash != third_slash)
    {
        // Too many slashes provided!
        ERROR_MSG("(" << attr_name << "): Too many slashes provided in attribute name" << endl);
        return ERR_TOO_MANY_SLASHES_IN_ATTR;
    }
    if (first_slash == 0)
    {
        // empty domain
        ERROR_MSG("(" << attr_name << "): empty domain" << endl);
        return ERR_EMPTY_DOMAIN_IN_ATTR;
    }
    if (second_slash - first_slash - 1 == 0)
    {
        // empty family
        ERROR_MSG("(" << attr_name << "): empty family" << endl);
        return ERR_EMPTY_FAMILY_IN_ATTR;
    }
    if (third_slash - second_slash - 1 == 0)
    {
        // empty member
        ERROR_MSG("(" << attr_name << "): empty member" << endl);
        return ERR_EMPTY_MEMBER_IN_ATTR;
    }
    if (third_slash + 1 == attr_name.length())
    {
        // empty atribute name
        ERROR_MSG("(" << attr_name << "): empty attribute name" << endl);
        return ERR_EMPTY_ATTR_NAME_IN_ATTR;
    }

    domain = attr_name.substr(0, first_slash);
    family = attr_name.substr(first_slash + 1, second_slash - first_slash - 1);
    member = attr_name.substr(second_slash + 1, third_slash - second_slash - 1);
    name = attr_name.substr(third_slash + 1);
    return 0;
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::remove_domain(string str)
{
    string::size_type end1 = str.find(".");
    if (end1 == string::npos)
    {
        return str;
    }
    else
    {
        string::size_type start = str.find("tango://");
        if (start == string::npos)
        {
            start = 0;
        }
        else
        {
            start = 8; // tango:// len
        }
        string::size_type end2 = str.find(":", start);
        if (end1 > end2) //'.' not in the tango host part
            return str;
        string th = str.substr(0, end1);
        th += str.substr(end2, str.size() - end2);
        return th;
    }
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::add_domain(string str)
{
    string::size_type end1 = str.find(".");

    if (end1 == string::npos)
    {
        // get host name without tango://
        string::size_type start = str.find("tango://");

        if (start == string::npos)
        {
            start = 0;
        }
        else
        {
            start = 8; // tango:// len
        }

        string::size_type end2 = str.find(":", start);

        string th = str.substr(start, end2);
        string with_domain = str;
        ;
        struct addrinfo hints;
        //		hints.ai_family = AF_INET; // use AF_INET6 to force IPv6
        //		hints.ai_flags = AI_CANONNAME|AI_CANONIDN;
        memset(&hints, 0, sizeof hints);
        hints.ai_family = AF_UNSPEC; /*either IPV4 or IPV6*/
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_flags = AI_CANONNAME;
        struct addrinfo *result, *rp;

        int ret = getaddrinfo(th.c_str(), NULL, &hints, &result);

        if (ret != 0)
        {
            fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(ret));
            return str;
        }

        for (rp = result; rp != NULL; rp = rp->ai_next)
        {
            with_domain = string(rp->ai_canonname) + str.substr(end2);

            if (logging_enabled)
                cout << __func__ << ": found domain -> " << with_domain << endl;
        }
        freeaddrinfo(result); // all done with this structure
        return with_domain;
    }
    else
    {
        return str;
    }
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::string_vector2map(vector<string> str, string separator, map<string, string> *results)
{
    for (vector<string>::iterator it = str.begin(); it != str.end(); it++)
    {
        string::size_type found_eq;
        found_eq = it->find_first_of(separator);
        if (found_eq != string::npos && found_eq > 0)
            results->insert(make_pair(it->substr(0, found_eq), it->substr(found_eq + 1)));
    }
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_data_type(int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                     int format /*SCALAR, SPECTRUM, ..*/,
                                     int write_type /*READ, READ_WRITE, ..*/) const
{
    TRACE_MSG("Entering");

    ostringstream data_type;

    if (format == Tango::SCALAR)
    {
        data_type << TYPE_SCALAR << "_";
    }
    else
    {
        data_type << TYPE_ARRAY << "_";
    }

    switch (type)
    {
        case Tango::DEV_BOOLEAN:
            data_type << TYPE_DEV_BOOLEAN << "_";
            break;
        case Tango::DEV_UCHAR:
            data_type << TYPE_DEV_UCHAR << "_";
            break;
        case Tango::DEV_SHORT:
            data_type << TYPE_DEV_SHORT << "_";
            break;
        case Tango::DEV_USHORT:
            data_type << TYPE_DEV_USHORT << "_";
            break;
        case Tango::DEV_LONG:
            data_type << TYPE_DEV_LONG << "_";
            break;
        case Tango::DEV_ULONG:
            data_type << TYPE_DEV_ULONG << "_";
            break;
        case Tango::DEV_LONG64:
            data_type << TYPE_DEV_LONG64 << "_";
            break;
        case Tango::DEV_ULONG64:
            data_type << TYPE_DEV_ULONG64 << "_";
            break;
        case Tango::DEV_FLOAT:
            data_type << TYPE_DEV_FLOAT << "_";
            break;
        case Tango::DEV_DOUBLE:
            data_type << TYPE_DEV_DOUBLE << "_";
            break;
        case Tango::DEV_STRING:
            data_type << TYPE_DEV_STRING << "_";
            break;
        case Tango::DEV_STATE:
            data_type << TYPE_DEV_STATE << "_";
            break;
        case Tango::DEV_ENCODED:
            data_type << TYPE_DEV_ENCODED << "_";
            break;
        default:
            stringstream error_desc;
            error_desc << "(" << type << ", ...): Type not supported (" << __FILE__ << ":"
                       << __LINE__ << ")" << ends;
            ERROR_MSG(error_desc.str() << endl);
            Tango::Except::throw_exception(EXCEPTION_TYPE_UNSUPPORTED_ATTR, error_desc.str(), __func__);
    }

    if (write_type == Tango::READ)
    {
        data_type << TYPE_RO;
    }
    else
    {
        data_type << TYPE_RW;
    }

    TRACE_MSG("Leaving");
    return data_type.str();
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_table_name(int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                      int format /*SCALAR, SPECTRUM, ..*/,
                                      int write_type /*READ, READ_WRITE, ..*/) const
{
    ostringstream table_name;
    table_name << "att_" << get_data_type(type, format, write_type);
    return table_name.str();
}

//=============================================================================
/**
 * insert_attr_conf(): Insert the provided attribute into the configuration table (add it)
 *
 * @param facility: control system name (TANGO_HOST)
 * @param attr_name: attribute name with its device name like domain/family/member/name
 * @param data_type: attribute data type (e.g. scalar_devdouble_rw)
 * @param uuid: uuid generated during the insertion process for this attribute
 * @param ttl: TTL value for this attribute (default = 0)
 **/
//=============================================================================
void HdbPPCassandra::insert_attr_conf(const string &facility,
                                      const string &attr_name,
                                      const string &data_type,
                                      CassUuid &uuid,
                                      unsigned int ttl)
{
    TRACE_MSG("Entering");

    ostringstream insert_str;

    insert_str << "INSERT INTO " << m_keyspace_name << "." << CONF_TABLE_NAME << " (" << CONF_COL_ID
               << "," << CONF_COL_FACILITY << "," << CONF_COL_NAME << "," << CONF_COL_TYPE << ","
               << CONF_COL_TTL << ")"
               << " VALUES (?, ?, ?, ?, ?)" << ends;

    CassStatement *statement =
        cass_statement_new(insert_str.str().c_str(), 5); // TODO Reuse prepared statement!
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable?
    CassUuidGen *uuid_gen = cass_uuid_gen_new();

    cass_uuid_gen_time(uuid_gen, &uuid);
    cass_statement_bind_uuid(statement, 0, uuid);
    cass_statement_bind_string(statement, 1, facility.c_str());
    cass_statement_bind_string(statement, 2, attr_name.c_str());
    cass_statement_bind_string(statement, 3, data_type.c_str());
    cass_statement_bind_int32(statement, 4, ttl);

    CassError rc = execute_statement(statement);
    cass_uuid_gen_free(uuid_gen);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", insert_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
/**
 * insert_domain(): Insert a new domain into the domains table,
 * which is a table used to speed up browsing of attributes
 *
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 **/
//=============================================================================
void HdbPPCassandra::insert_domain(const string &facility, const string &domain)
{
    TRACE_MSG("Entering");

    ostringstream insert_domains_str;

    insert_domains_str << "INSERT INTO " << m_keyspace_name << "." << DOMAINS_TABLE_NAME << " ("
                       << DOMAINS_COL_FACILITY << "," << DOMAINS_COL_DOMAIN << ")"
                       << " VALUES (?,?)" << ends;

    CassStatement *statement =
        cass_statement_new(insert_domains_str.str().c_str(), 2); // TODO Reuse prepared statement?
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable?
    cass_statement_bind_string(statement, 0, facility.c_str());
    cass_statement_bind_string(statement, 1, domain.c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", insert_domains_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
/**
 * insert_family(): Insert a new family into the families table,
 * which is a table used to speed up browsing of attributes
 *
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 * @param family: family name
 **/
//=============================================================================
void HdbPPCassandra::insert_family(const string &facility, const string &domain, const string &family)
{
    TRACE_MSG("Entering");

    ostringstream insert_families_str;

    insert_families_str << "INSERT INTO " << m_keyspace_name << "." << FAMILIES_TABLE_NAME << " ("
                        << FAMILIES_COL_FACILITY << "," << FAMILIES_COL_DOMAIN << ","
                        << FAMILIES_COL_FAMILY << ")"
                        << " VALUES (?,?,?)" << ends;

    CassStatement *statement =
        cass_statement_new(insert_families_str.str().c_str(), 3); // TODO Reuse prepared statement?
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable?
    cass_statement_bind_string(statement, 0, facility.c_str());
    cass_statement_bind_string(statement, 1, domain.c_str());
    cass_statement_bind_string(statement, 2, family.c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", insert_families_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
/**
 * insert_member(): Insert a new member into the members table,
 * which is a table used to speed up browsing of attributes
 *
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 * @param family: family name
 * @param member: member name
 **/
//=============================================================================
void HdbPPCassandra::insert_member(const string &facility, const string &domain, const string &family, const string &member)
{
    TRACE_MSG("Entering");

    ostringstream insert_members_str;

    insert_members_str << "INSERT INTO " << m_keyspace_name << "." << MEMBERS_TABLE_NAME << " ("
                       << MEMBERS_COL_FACILITY << "," << MEMBERS_COL_DOMAIN << ","
                       << MEMBERS_COL_FAMILY << "," << MEMBERS_COL_MEMBER << ")"
                       << " VALUES (?,?,?,?)" << ends;

    CassStatement *statement =
        cass_statement_new(insert_members_str.str().c_str(), 4); // TODO Reuse prepared statement?
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable?
    cass_statement_bind_string(statement, 0, facility.c_str());
    cass_statement_bind_string(statement, 1, domain.c_str());
    cass_statement_bind_string(statement, 2, family.c_str());
    cass_statement_bind_string(statement, 3, member.c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", insert_members_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
/**
 * insert_attr_name(): Insert a new attribute name into the attribute names table,
 * which is a table used to speed up browsing of attributes
 *
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 * @param family: family name
 * @param member: member name
 * @param name: attribute name
 **/
//=============================================================================
void HdbPPCassandra::insert_attr_name(const string &facility,
                                      const string &domain,
                                      const string &family,
                                      const string &member,
                                      const string &attribute_name)
{
    TRACE_MSG("Entering");

    ostringstream insert_att_names_str;

    insert_att_names_str << "INSERT INTO " << m_keyspace_name << "." << ATT_NAMES_TABLE_NAME << " ("
                         << ATT_NAMES_COL_FACILITY << "," << ATT_NAMES_COL_DOMAIN << "," << ATT_NAMES_COL_FAMILY
                         << "," << ATT_NAMES_COL_MEMBER << "," << ATT_NAMES_COL_NAME << ")"
                         << " VALUES (?,?,?,?,?)" << ends;

    CassStatement *statement =
        cass_statement_new(insert_att_names_str.str().c_str(), 5); // TODO Reuse prepared statement?
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the
    // consistency
    // tunable?
    cass_statement_bind_string(statement, 0, facility.c_str());
    cass_statement_bind_string(statement, 1, domain.c_str());
    cass_statement_bind_string(statement, 2, family.c_str());
    cass_statement_bind_string(statement, 3, member.c_str());
    cass_statement_bind_string(statement, 4, attribute_name.c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query", insert_att_names_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
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
//=============================================================================
void HdbPPCassandra::update_ttl(unsigned int ttl, const string &facility, const string &attr_name)
{
    TRACE_MSG("Entering");

    ostringstream update_ttl_query_str;

    update_ttl_query_str << "UPDATE " << m_keyspace_name << "." << CONF_TABLE_NAME << " SET "
                         << CONF_COL_TTL << " = " << ttl << " WHERE " << CONF_COL_FACILITY
                         << " = ? AND " << CONF_COL_NAME << " = ?" << ends;

    CassStatement *statement =
        cass_statement_new(update_ttl_query_str.str().c_str(), 2); // TODO Reuse prepared statement?

    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the // consistency // tunable?
    cass_statement_bind_string(statement, 0, facility.c_str());
    cass_statement_bind_string(statement, 1, attr_name.c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing update tll query", update_ttl_query_str.str(), rc, __func__);

    TRACE_MSG("Leaving");
}

//=============================================================================
//=============================================================================
CassError HdbPPCassandra::execute_statement(CassStatement *statement)
{
    TRACE_MSG("Entering");

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    cass_future_free(future);
    cass_statement_free(statement);

    TRACE_MSG("Leaving");
    return rc;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::throw_execute_exception(string message, string query, CassError error, const char *origin)
{
    stringstream error_desc;
    error_desc << message << " \"" << query << "\": " << cass_error_desc(error) << ends;
    ERROR_MSG(error_desc.str() << endl);
    Tango::Except::throw_exception(EXCEPTION_TYPE_QUERY, error_desc.str().c_str(), origin);
}

//=============================================================================
/**
 * set_cassandra_logging_level(): Update cassandra driver logging level
 *
 * @param level: the new logging level
 */
//=============================================================================
void HdbPPCassandra::set_cassandra_logging_level(string level)
{
    if (level == "DISABLED")
        cass_log_set_level(cassandra_logging_level = CASS_LOG_DISABLED);

    else if (level == "CRITICAL")
        cass_log_set_level(cassandra_logging_level = CASS_LOG_CRITICAL);

    else if (level == "ERROR")
        cass_log_set_level(cassandra_logging_level = CASS_LOG_ERROR);

    else if (level == "WARN")
        cass_log_set_level(cassandra_logging_level = CASS_LOG_WARN);

    else if (level == "INFO")
        cass_log_set_level(cassandra_logging_level = CASS_LOG_INFO);

    else if (level == "DEBUG")
        cass_log_set_level(cassandra_logging_level = CASS_LOG_DEBUG);

    else if (level == "TRACE")
        cass_log_set_level(cassandra_logging_level = CASS_LOG_TRACE);

    else
    {
        cass_log_set_level(cassandra_logging_level = CASS_LOG_DISABLED);
        ERROR_MSG("ERROR invalid cassandra driver logging level" << endl);
        ERROR_MSG("Log level set by default to: DISABLED" << endl);
    }

    if (logging_enabled)
    {
        DEBUG_MSG("Cassandra driver logging to to: ");

        if (cassandra_logging_level == CASS_LOG_DISABLED)
            DEBUG_MSG("DISABLED" << endl);
        else
            DEBUG_MSG(cass_log_level_string(cassandra_logging_level) << endl);
    }
}

//=============================================================================
//=============================================================================
AbstractDB *HdbPPCassandraFactory::create_db(vector<string> configuration)
{
    return new HdbPPCassandra(configuration);
}

}; // namespace HDBPP

//=============================================================================
//=============================================================================
DBFactory *getDBFactory()
{
    HDBPP::HdbPPCassandraFactory *db_cass_factory = new HDBPP::HdbPPCassandraFactory();
    return static_cast<DBFactory *>(db_cass_factory);
}
