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

#include "LibHdb++Cassandra.h"
#include "LibHdb++Defines.h"
#include "Log.h"
#include "PreparedStatementCache.h"
#include "TangoEventDataBinder.h"

#include <iostream>

#ifndef LIB_BUILDTIME
#define LIB_BUILDTIME RELEASE " " __DATE__ " " __TIME__
#endif

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
//=============================================================================
//=============================================================================
HdbPPCassandra::HdbPPCassandra(vector<string> configuration)
{
    // LOG(Debug)<<": VERSION: " << version_string << " file:" << __FILE__rev << endl;
    map<string, string> libhdb_conf;
    string local_dc;
    string config;

    // defaults
    Log::LogLevel() = Error;
    cass_log_set_level(CASS_LOG_DISABLED);

    // convert the config vector to a map
    string_vector2map(configuration, "=", &libhdb_conf);

    // ---- logging_enabled optional config parameter ----
    if(!(config = get_config_param(libhdb_conf, "logging_enabled", false)).empty())
        config == "true" ? Log::LogLevel() = Debug : Log::LogLevel() = Error;

    // ---- cassandra_driver_log_level optional config parameter ----
    if(!(config = get_config_param(libhdb_conf, "cassandra_driver_log_level", false)).empty())
        set_cassandra_logging_level(config);

    // ---- user optional config parameter ----
    auto user = get_config_param(libhdb_conf, "user", false);

    // ---- password optional config parameter ----
    auto password = get_config_param(libhdb_conf, "password", false);

    // ---- local_dc optional config parameter ----
    local_dc = get_config_param(libhdb_conf, "local_dc", false);

    if(local_dc.empty())
    {
        local_dc = LOCAL_DC_DEFAULT;

        LOG(Debug) << "Library configuration parameter local_dc is not defined. Defaulting to: " 
                   << LOCAL_DC_DEFAULT << endl;
    }

    // ---- store diag times optional config parameter ----
    auto store_diag_time_str = get_config_param(libhdb_conf, "store_diag_time", false);
    store_diag_time_str == "true" ? _store_diag_times = true : _store_diag_times = false;

    // ---- consistency mandatory config parameter ----
    if(!(config = get_config_param(libhdb_conf, "consistency", true)).empty())
        set_cassandra_consistency_level(config);

    // ---- contact_points mandatory config parameter ----
    auto contact_points = get_config_param(libhdb_conf, "contact_points", true);

    // ---- keyspace mandatory config parameter ----
    m_keyspace_name = get_config_param(libhdb_conf, "keyspace", true);

    mp_cluster = cass_cluster_new();
    cass_cluster_set_contact_points(mp_cluster, contact_points.c_str());

    if (user.empty() && password.empty())
    {
        // No authentication
    }
    else if (user.empty() && !password.empty())
    {
        LOG(Error) << "A password was provided for the Cassandra connection, but no user name was provided!"
                   << endl;

        LOG(Error) << "Will try to connect anonymously" << endl;
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
    TRACE_ENTER;

    if (mp_cluster)
    {
        // free up any used prepared statements
        _prepared_statements->free_cache();
        delete _prepared_statements;

        CassFuture *close_future = NULL;
        close_future = cass_session_close(mp_session);
        cass_future_wait(close_future);
        cass_future_free(close_future);
        cass_cluster_free(mp_cluster);
        cass_session_free(mp_session);
    }

    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::connect_session()
{
    TRACE_ENTER;

    mp_session = cass_session_new();
    CassFuture *future = cass_session_connect(mp_session, mp_cluster);
    cass_future_wait(future);

    CassError rc = cass_future_error_code(future);
    cass_future_free(future);

    if (rc != CASS_OK)
    {
        LOG(Error) << "Cassandra connect session error: " << cass_error_desc(rc) << endl;
        stringstream error_desc;
        error_desc << "Error connecting to the Cassandra Cluster: " << cass_error_desc(rc) << ends;
        cass_cluster_free(mp_cluster);
        cass_session_free(mp_session);
        mp_cluster = NULL;
        mp_session = NULL;
        Tango::Except::throw_exception(EXCEPTION_TYPE_CONNECTION, error_desc.str(), __func__);
    }
    else
    {
        LOG(Debug) << "Cassandra connection OK" << endl;
    }

    // create a prepared statement manager
    _prepared_statements = new PreparedStatementCache(mp_session, m_keyspace_name);

    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_attr_id(AttributeName &attr_name, CassUuid &ID)
{
    TRACE_ENTER;

    // First look into the cache
    map<string, AttributeParams>::iterator it =
        attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (it == attribute_cache.end())
    {
        // if not already present in cache, look for ID in the DB
        unsigned int ttl = 0;
        if (!find_attr_id_and_ttl_in_db(attr_name, ID, ttl))
        {
            LOG(Debug) << "ID not found for attr=" << attr_name << endl;
            TRACE_EXIT;
            return false;
        }
    }
    else
    {
        ID = it->second.id;
    }

    TRACE_EXIT;
    return true;
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_attr_id_and_ttl(AttributeName &attr_name, CassUuid &ID, unsigned int &ttl)
{
    TRACE_ENTER;

    // First look into the cache
    map<string, AttributeParams>::iterator it =
        attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (it == attribute_cache.end())
    {
        // if not already present in cache, look for ID in the DB
        if (!find_attr_id_and_ttl_in_db(attr_name, ID, ttl))
        {
            LOG(Debug) << "ID not found for attr=" << attr_name << endl;
            TRACE_EXIT;
            return false;
        }
    }
    else
    {
        ID = it->second.id;
        ttl = it->second.ttl;
    }

    TRACE_EXIT;
    return true;
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_attr_id_and_ttl_in_db(AttributeName &attr_name, CassUuid &ID, unsigned int &ttl)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::FindAttrIdAndTtlInDb);
    cass_statement_set_consistency(statement, consistency);
    cass_statement_bind_string_by_name(statement, CONF_COL_NAME.c_str(), attr_name.full_attribute_name().c_str());
    cass_statement_bind_string_by_name(statement, CONF_COL_FACILITY.c_str(), attr_name.tango_host_with_domain().c_str());

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    if (rc != CASS_OK)
    {
        cass_future_free(future);
        cass_statement_free(statement);

        throw_execute_exception(EXCEPTION_TYPE_QUERY,
                                _prepared_statements->query_string(Query::FindAttrIdAndTtlInDb), rc,
                                __func__);
    }

    const CassResult *result = cass_future_get_result(future);
    CassIterator *iterator = cass_iterator_from_result(result);
    bool found = false;

    if (cass_iterator_next(iterator))
    {
        LOG(Debug) << "SUCCESS in query: "
                   << _prepared_statements->query_id_to_str(Query::FindAttrIdAndTtlInDb) << " ("
                   << CONF_COL_NAME << "=\'" << attr_name.full_attribute_name() << "\',"
                   << CONF_COL_FACILITY << "=\'" << attr_name.tango_host_with_domain() << "\')" << endl;

        const CassRow *row = cass_iterator_get_row(iterator);
        cass_value_get_uuid(cass_row_get_column(row, 0), &ID);
        cass_int32_t ttl_val = 0;

        const CassValue *cass_ttl_val = cass_row_get_column_by_name(row, CONF_COL_TTL.c_str());

        if (cass_ttl_val == NULL)
        {
            LOG(Debug) << "Column ttl does not exist" << endl;
        }
        else
        {
            LOG(Debug) << "OK . value_type() = " << cass_value_type(cass_ttl_val) << endl;
        }

        CassError get_ttl_cass_error =
            cass_value_get_int32(cass_row_get_column_by_name(row, CONF_COL_TTL.c_str()), &ttl_val);

        if (get_ttl_cass_error != CASS_OK)
        {
            if (get_ttl_cass_error == CASS_ERROR_LIB_NULL_VALUE)
            {
                // The ttl was null, perhaps it was removed, or missing due to an
                // upgrade. This is not an error condition
                LOG(Debug) << "TTL value is NULL" << endl;
            }
            else
            {
                cass_result_free(result);
                cass_iterator_free(iterator);
                cass_future_free(future);
                cass_statement_free(statement);

                stringstream error_desc;

                error_desc << "An unexpected database error occured when trying to retrieve the TTL: "
                           << cass_error_desc(get_ttl_cass_error) << " for attribute: " << attr_name
                           << ends;

                LOG(Error) << error_desc.str() << endl;
                Tango::Except::throw_exception(EXCEPTION_TYPE_QUERY, error_desc.str(), __func__);
            }

            ttl = 0;
        }
        else
        {
            LOG(Debug) << "cass_value_get_int32 returned " << ttl_val << endl;
            ttl = ttl_val;
        }

        LOG(Debug) << "(" << attr_name << "): ID found " << endl;
        LOG(Debug) << "(" << attr_name << "): TTL = " << ttl << endl;

        found = true;

        attribute_cache.insert(
            make_pair(attr_name.fully_qualified_attribute_name(), AttributeParams(ID, ttl)));

        map<string, AttributeParams>::iterator it =
            attribute_cache.find(attr_name.fully_qualified_attribute_name());

        if (it == attribute_cache.end())
        {
            char uuid_str[CASS_UUID_STRING_LENGTH];
            cass_uuid_string(ID, uuid_str);

            LOG(Error) << "ID (" << uuid_str
                       << ") could not be added into cache for attr =" << attr_name << endl;
        }
    }

    cass_result_free(result);
    cass_iterator_free(iterator);
    cass_future_free(future);
    cass_statement_free(statement);

    if (!found)
        LOG(Debug) << "(" << attr_name << "): NO RESULT in query: "
                   << _prepared_statements->query_id_to_str(Query::FindAttrIdAndTtlInDb) << endl;

    TRACE_EXIT;
    return found;
}

//=============================================================================
//=============================================================================
HdbPPCassandra::FindAttrResult HdbPPCassandra::find_attr_id_type_and_ttl(AttributeName &attr_name,
                                                                         CassUuid &ID,
                                                                         string attr_type,
                                                                         unsigned int &conf_ttl)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::FindAttrIdTypeAndTtlInDb);
    cass_statement_set_consistency(statement, consistency);
    cass_statement_bind_string_by_name(statement, CONF_COL_NAME.c_str(),attr_name.full_attribute_name().c_str());
    cass_statement_bind_string_by_name(statement, CONF_COL_FACILITY.c_str(),attr_name.tango_host_with_domain().c_str());

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    if (rc != CASS_OK)
    {
        cass_future_free(future);
        cass_statement_free(statement);
        throw_execute_exception(EXCEPTION_TYPE_QUERY,
                                _prepared_statements->query_string(Query::FindAttrIdTypeAndTtlInDb),
                                rc, __func__);
    }

    const CassResult *result = cass_future_get_result(future);
    CassIterator *iterator = cass_iterator_from_result(result);
    bool found = false;
    string db_type = "";

    if (cass_iterator_next(iterator))
    {
        LOG(Debug) << "SUCCESS in query: "
                   << _prepared_statements->query_id_to_str(Query::FindAttrIdTypeAndTtlInDb) << endl;

        /// @todo Improve error handling
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
        LOG(Warning) << "NO RESULT in query: "
                     << _prepared_statements->query_id_to_str(Query::FindAttrIdTypeAndTtlInDb) << endl;
        TRACE_EXIT;
        return AttrNotFound;
    }
    else if (db_type != attr_type)
    {
        LOG(Debug) << "FOUND ID for " << attr_name.tango_host_with_domain() << "/"
                   << attr_name.full_attribute_name() << " but different type: attr_type=" << attr_type
                   << "-db_type=" << db_type << endl;

        TRACE_EXIT;
        return FoundAttrWithDifferentType;
    }

    LOG(Debug) << "FOUND ID for " << attr_name.tango_host_with_domain() << "/"
               << attr_name.full_attribute_name() << " with SAME type: attr_type=" << attr_type
               << "-db_type=" << db_type << endl;

    TRACE_EXIT;
    return FoundAttrWithSameType;
}

//=============================================================================
//=============================================================================
bool HdbPPCassandra::find_last_event(const CassUuid &id, string &last_event, AttributeName &attr_name)
{
    TRACE_ENTER;
    last_event = "??";

    CassStatement *statement = _prepared_statements->statement(Query::FindLastEvent);
    cass_statement_set_consistency(statement, consistency);
    cass_statement_bind_uuid_by_name(statement, HISTORY_COL_ID.c_str(), id);

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    if (rc != CASS_OK)
    {
        cass_future_free(future);
        cass_statement_free(statement);
        throw_execute_exception(EXCEPTION_TYPE_QUERY,
                                _prepared_statements->query_string(Query::FindLastEvent), rc, __func__);
    }

    bool found = false;
    const CassResult *result = cass_future_get_result(future);
    CassIterator *iterator = cass_iterator_from_result(result);

    if (cass_iterator_next(iterator))
    {
        LOG(Debug) << "SUCCESS in query: " << _prepared_statements->query_id_to_str(Query::FindLastEvent)
                   << endl;

        const CassRow *row = cass_iterator_get_row(iterator);
        const char *last_event_res;
        size_t last_event_res_length;
        cass_value_get_string(cass_row_get_column(row, 0), &last_event_res, &last_event_res_length);
        last_event = string(last_event_res, last_event_res_length);

        found = true;
        LOG(Debug) << "(" << attr_name << "): last event = " << last_event << endl;
    }

    cass_result_free(result);
    cass_iterator_free(iterator);
    cass_future_free(future);
    cass_statement_free(statement);

    if (!found)
        LOG(Debug) << "(" << attr_name << "): NO RESULT in query: "
                   << _prepared_statements->query_id_to_str(Query::FindLastEvent) << endl;

    TRACE_EXIT;
    return found;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type)
{
    TRACE_ENTER;

    if (data == NULL)
    {
        stringstream error_desc;
        error_desc << "Unexpected null Tango::EventData" << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }

    AttributeName attr_name(data->attr_name);

    int64_t ev_time;
    int ev_time_us;
    int64_t rcv_time = ((int64_t)data->get_date().tv_sec) * 1000;
    int rcv_time_us = data->get_date().tv_usec;
    int quality = (int)data->attr_value->get_quality();
    string error_desc("");

    Tango::AttributeDimension attr_w_dim;
    Tango::AttributeDimension attr_r_dim;
    int data_type = ev_data_type.data_type;
    Tango::AttrDataFormat data_format = ev_data_type.data_format;
    int write_type = ev_data_type.write_type;
    // int max_dim_x = ev_data_type.max_dim_x;
    // int max_dim_y = ev_data_type.max_dim_y;

    bool is_null = false;

    if (data->err)
    {
        LOG(Debug) << "Attribute in error:" << error_desc << endl;

        is_null = true;
        // Store the error description
        error_desc = data->errors[0].desc;
    }

    data->attr_value->reset_exceptions(
        Tango::DeviceAttribute::isempty_flag); // disable is_empty exception

    if (data->attr_value->is_empty())
    {
        LOG(Debug) << "no value will be archived... (Attr Value is empty)" << endl;
        is_null = true;
    }

    if (quality == Tango::ATTR_INVALID)
    {
        LOG(Debug) << "no value will be archived... (Invalid Attribute)" << endl;
        is_null = true;
    }

    if (!is_null)
    {
        attr_w_dim = data->attr_value->get_w_dimension();
        attr_r_dim = data->attr_value->get_r_dimension();
        ev_time = ((int64_t)data->attr_value->get_date().tv_sec) * 1000;
        ev_time_us = data->attr_value->get_date().tv_usec;
    }
    else
    {
        /// @todo Check this is ok?
        attr_r_dim.dim_x = 0;
        attr_w_dim.dim_x = 0;
        attr_r_dim.dim_y = 0;
        attr_w_dim.dim_y = 0;
        ev_time = rcv_time;
        ev_time_us = rcv_time_us;
    }

    CassUuid id;
    unsigned int ttl = 0;

    if (!find_attr_id_and_ttl(attr_name, id, ttl))
    {
        LOG(Error) << "Could not find ID for attribute " << attr_name << endl;
        stringstream error_desc;
        error_desc << "ERROR Could not find ID for attribute  \"" << attr_name << "\": " << ends;
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str().c_str(), __func__);
    }

    CassStatement *statement = _prepared_statements->statement(data_type, data_format, write_type);
    cass_statement_set_consistency(statement, consistency);
    cass_statement_bind_uuid_by_name(statement, SC_COL_ID.c_str(), id);

    // Compute the period based on the month of the event time
    struct tm *tms;
    time_t ev_time_s = ev_time / 1000;

    if ((tms = localtime(&ev_time_s)) == NULL)
        perror("localtime");

    char period[11];
    snprintf(period, 11, "%04d-%02d-%02d", tms->tm_year + 1900, tms->tm_mon + 1, tms->tm_mday);

    cass_statement_bind_string_by_name(statement, SC_COL_PERIOD.c_str(), period);
    cass_statement_bind_int64_by_name(statement, SC_COL_EV_TIME.c_str(), ev_time);
    cass_statement_bind_int32_by_name(statement, SC_COL_EV_TIME_US.c_str(), ev_time_us);

    // Get the current time
    struct timespec ts;
    if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
    {
        /// @todo handle this error?
        perror("clock_gettime()");
    }

    if (_store_diag_times)
    {
        int64_t insert_time = ((int64_t)ts.tv_sec) * 1000;
        int insert_time_us = ts.tv_nsec / 1000;

        cass_statement_bind_int64_by_name(statement, SC_COL_RCV_TIME.c_str(), rcv_time);
        cass_statement_bind_int32_by_name(statement, SC_COL_RCV_TIME_US.c_str(), rcv_time_us);
        cass_statement_bind_int64_by_name(statement, SC_COL_INS_TIME.c_str(), insert_time);
        cass_statement_bind_int32_by_name(statement, SC_COL_INS_TIME_US.c_str(), insert_time_us);
    }

    cass_statement_bind_int32_by_name(statement, SC_COL_QUALITY.c_str(), quality);
    cass_statement_bind_string_by_name(statement, SC_COL_ERROR_DESC.c_str(), error_desc.c_str());

    TangoEventDataBinder event_binder;
    event_binder(statement, SC_COL_VALUE_R, data_type, write_type, data_format, data);
    event_binder(statement, SC_COL_VALUE_W, data_type, write_type, data_format, data);

    if (ttl > 0)
    {
        cass_statement_bind_int32(statement,
                                  _prepared_statements->get_insert_attr_ttl_bind_position(write_type),
                                  ttl * 3600);
    }

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(data_type, data_format, write_type),
                                rc, __func__);

    LOG(Debug) << "SUCCESS in query: "
               << _prepared_statements->query_id_to_str(data_type, data_format, write_type)
               << " with ttl = " << ttl << " data_type = " << data_type
               << " data_format = " << data_format << " write_type = " << write_type << endl;

    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::insert_history_event(const string &history_event_name, CassUuid att_conf_id)
{
    TRACE_ENTER;

    char att_conf_id_str[CASS_UUID_STRING_LENGTH];
    cass_uuid_string(att_conf_id, &att_conf_id_str[0]);
    LOG(Debug) << "(" << history_event_name << "," << att_conf_id_str << ": entering... " << endl;

    struct timespec ts;
    if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
    {
        /// @todo handle this error?
        perror("clock_gettime()");
    }
    int64_t current_time = ((int64_t)ts.tv_sec) * 1000;
    int current_time_us = ts.tv_nsec / 1000;

    CassStatement *statement = _prepared_statements->statement(Query::InsertHistoryEvent);
    cass_statement_set_consistency(statement, consistency);
    cass_statement_bind_uuid_by_name(statement, HISTORY_COL_ID.c_str(), att_conf_id);
    cass_statement_bind_string_by_name(statement, HISTORY_COL_EVENT.c_str(), history_event_name.c_str());
    cass_statement_bind_int64_by_name(statement, HISTORY_COL_TIME.c_str(), current_time);
    cass_statement_bind_int32_by_name(statement, HISTORY_COL_TIME_US.c_str(), current_time_us);

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(Query::InsertHistoryEvent), rc,
                                __func__);

    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::insert_param_Attr(Tango::AttrConfEventData *data, HdbEventDataType ev_data_type)
{
    TRACE_ENTER;

    if (data == NULL)
    {
        stringstream error_desc;
        error_desc << "Unexpected null Tango::AttrConfEventData" << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }

    if (data->attr_conf == NULL)
    {
        stringstream error_desc;
        error_desc << "Unexpected null in Tango::AttrConfEventData field attr_conf" << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }

    AttributeName attr_name(data->attr_name);
    int64_t ev_time = ((int64_t)data->get_date().tv_sec) * 1000;
    int ev_time_us = data->get_date().tv_usec;

    CassUuid uuid;

    if (!find_attr_id(attr_name, uuid))
    {
        LOG(Error) << "Could not find ID for attribute " << attr_name << endl;
        stringstream error_desc;
        error_desc << "ERROR Could not find ID for attribute  \"" << attr_name << "\": " << ends;
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str().c_str(), __func__);
    }
    else
    {
        char uuid_str[CASS_UUID_STRING_LENGTH];
        cass_uuid_string(uuid, uuid_str);
        LOG(Debug) << "ID found for attribute " << attr_name << " = " << uuid_str << endl;
    }

    CassStatement *statement = _prepared_statements->statement(Query::InsertParamAttribute);
    cass_statement_set_consistency(statement, consistency);
    cass_statement_bind_uuid_by_name(statement, PARAM_COL_ID.c_str(), uuid);

    // Get the current time
    struct timespec ts;
    if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
    {
        /// @todo Is this a bad error?
        perror("clock_gettime()");
    }

    cass_statement_bind_int64_by_name(statement, PARAM_COL_EV_TIME.c_str(), ev_time); // recv_time
    cass_statement_bind_int32_by_name(statement, PARAM_COL_EV_TIME_US.c_str(), ev_time_us); // recv_time_us

    if (_store_diag_times)
    {
        int64_t insert_time = ((int64_t)ts.tv_sec) * 1000;
        int insert_time_us = ts.tv_nsec / 1000;

        cass_statement_bind_int64_by_name(statement, PARAM_COL_INS_TIME.c_str(), insert_time);
        cass_statement_bind_int32_by_name(statement, PARAM_COL_INS_TIME_US.c_str(), insert_time_us);
    }

    LOG(Debug) << " label: \"" << data->attr_conf->label.c_str() << "\"" << endl;
    LOG(Debug) << " unit: \"" << data->attr_conf->unit.c_str() << "\"" << endl;
    LOG(Debug) << " standard unit: \"" << data->attr_conf->standard_unit.c_str() << "\"" << endl;
    LOG(Debug) << " display unit: \"" << data->attr_conf->display_unit.c_str() << "\"" << endl;
    LOG(Debug) << " format: \"" << data->attr_conf->format.c_str() << "\"" << endl;

    LOG(Debug) << " archive rel change: \""
               << data->attr_conf->events.arch_event.archive_rel_change.c_str() << "\"" << endl;

    LOG(Debug) << " archive abs change: \""
               << data->attr_conf->events.arch_event.archive_abs_change.c_str() << "\"" << endl;

    LOG(Debug) << " archive period: \"" << data->attr_conf->events.arch_event.archive_period.c_str()
               << "\"" << endl;

    LOG(Debug) << " description: \"" << data->attr_conf->description.c_str() << "\"" << endl;
    LOG(Debug) << " after binding description" << endl;

    cass_statement_bind_string_by_name(statement, PARAM_COL_LABEL.c_str(), data->attr_conf->label.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_UNIT.c_str(), data->attr_conf->unit.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_STANDARDUNIT.c_str(),
                                       data->attr_conf->standard_unit.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_DISPLAYUNIT.c_str(),
                                       data->attr_conf->display_unit.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_FORMAT.c_str(),
                                       data->attr_conf->format.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_ARCHIVERELCHANGE.c_str(),
                                       data->attr_conf->events.arch_event.archive_rel_change.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_ARCHIVEABSCHANGE.c_str(),
                                       data->attr_conf->events.arch_event.archive_abs_change.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_ARCHIVEPERIOD.c_str(),
                                       data->attr_conf->events.arch_event.archive_period.c_str());

    cass_statement_bind_string_by_name(statement, PARAM_COL_DESCRIPTION.c_str(),
                                       data->attr_conf->description.c_str());

    cass_statement_set_consistency(statement, consistency);

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(Query::InsertParamAttribute), rc,
                                __func__);

    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::configure_Attr(string name,
                                    int type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                    int format /*SCALAR, SPECTRUM, ..*/,
                                    int write_type /*READ, READ_WRITE, ..*/,
                                    unsigned int ttl /* hours, 0=infinity*/)
{
    TRACE_ENTER;

    AttributeName attr_name(name);

    if (attr_name.validate_domain_family_member_name() != AttributeName::NoError)
    {
        stringstream error_desc;
        error_desc << "ERROR parsing attribute name  \"" << attr_name << "\": " << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_ATTR_FORMAT, error_desc.str().c_str(), __func__);
    }

    LOG(Debug) << "name=" << name << " -> tango_host=" << attr_name.tango_host_with_domain()
               << " attr_name=" << attr_name << endl;

    string data_type = _prepared_statements->get_table_name(type, format, write_type);

    CassUuid id;
    unsigned int conf_ttl = 0;
    FindAttrResult find_attr_result = find_attr_id_type_and_ttl(attr_name, id, data_type, conf_ttl);

    // ID already present but different configuration (attribute type)
    if (find_attr_result == FoundAttrWithDifferentType)
    {
        stringstream error_desc;

        error_desc << attr_name.tango_host_with_domain() << "/" << attr_name
                   << " already configured with different configuration."
                   << "Please contact your administrator for this special case" << ends;

        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_NULL_POINTER, error_desc.str(), __func__);
    }
    else
    {
        // ID found and same configuration (attribute type): do nothing
        if (find_attr_result == FoundAttrWithSameType)
        {
            LOG(Debug) << "ALREADY CONFIGURED with same configuration: "
                       << attr_name.tango_host_with_domain() << "/" << attr_name << endl;

            if (conf_ttl != ttl)
            {
                LOG(Debug) << ".... BUT different ttl: updating " << conf_ttl << " to " << ttl << endl;
                update_ttl(ttl, attr_name);
            }
            // If the last event was EVENT_REMOVE, add it again
            string last_event;
            if (find_last_event(id, last_event, attr_name) && last_event == EVENT_REMOVE)
            {
                // An attribute which was removed needs to be added again
                insert_history_event(EVENT_ADD, id);
            }
        }
        else
        {
            // insert into configuration table
            CassUuid uuid;
            insert_attr_conf(attr_name, data_type, uuid, ttl);

            // Add ADD event into history table
            insert_history_event(EVENT_ADD, uuid);
        }
    }

    // Insert into domains table
    insert_domain(attr_name);

    // Insert into families table
    insert_family(attr_name);

    // Insert into members table
    insert_member(attr_name);

    // Insert into att_names table
    insert_attr_name(attr_name);

    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::updateTTL_Attr(string fqdn_attr_name, unsigned int ttl /* hours, 0=infinity*/)
{
    TRACE_ENTER;
    LOG(Debug) << "Update: " << fqdn_attr_name << " TTL with parameter ttl = " << ttl << endl;

    AttributeName attr_name(fqdn_attr_name);
    CassUuid uuid;

    if (!find_attr_id(attr_name, uuid))
    {
        stringstream error_desc;
        error_desc << "ERROR Attribute " << attr_name << " NOT FOUND in HDB++ configuration table" << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str(), __func__);
    }

    update_ttl(ttl, attr_name);
    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::event_Attr(string fqdn_attr_name, unsigned char event)
{
    TRACE_ENTER;

    AttributeName attr_name(fqdn_attr_name);
    CassUuid uuid;

    if (!find_attr_id(attr_name, uuid))
    {
        stringstream error_desc;
        error_desc << "ERROR Attribute " << attr_name << " NOT FOUND in HDB++ configuration table" << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str().c_str(), __func__);
    }

    string event_name = "";

    switch (event)
    {
        case DB_START:
        {
            string last_event;
            bool ret = find_last_event(uuid, last_event, attr_name);
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
            error_desc << "ERROR for " << attr_name << " event=" << (int)event << " NOT SUPPORTED" << ends;
            LOG(Error) << error_desc.str() << endl;
            Tango::Except::throw_exception(EXCEPTION_TYPE_MISSING_ATTR, error_desc.str(), __func__);
        }
    }

    insert_history_event(event_name, uuid);
    TRACE_EXIT;
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
/**
 * insert_attr_conf(): Insert the provided attribute into the configuration table (add it)
 *
 * @param attr_name: Attribute name class containing the fully qualified domain name
 * @param data_type: attribute data type (e.g. scalar_devdouble_rw)
 * @param uuid: uuid generated during the insertion process for this attribute
 * @param ttl: TTL value for this attribute (default = 0)
 **/
//=============================================================================
void HdbPPCassandra::insert_attr_conf(AttributeName &attr_name,
                                      const string &data_type,
                                      CassUuid &uuid,
                                      unsigned int ttl)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::InsertAttributeConf);
    cass_statement_set_consistency(statement, consistency);

    CassUuidGen *uuid_gen = cass_uuid_gen_new();
    cass_uuid_gen_time(uuid_gen, &uuid);

    cass_statement_bind_uuid_by_name(statement, CONF_COL_ID.c_str(), uuid);
    cass_statement_bind_string_by_name(statement, CONF_COL_FACILITY.c_str(),
                                       attr_name.tango_host_with_domain().c_str());
    cass_statement_bind_string_by_name(statement, CONF_COL_NAME.c_str(),
                                       attr_name.full_attribute_name().c_str());
    cass_statement_bind_string_by_name(statement, CONF_COL_TYPE.c_str(), data_type.c_str());
    cass_statement_bind_int32_by_name(statement, CONF_COL_TTL.c_str(), ttl);

    CassError rc = execute_statement(statement);
    cass_uuid_gen_free(uuid_gen);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(Query::InsertAttributeConf), rc,
                                __func__);

    TRACE_EXIT;
}

//=============================================================================
/**
 * insert_domain(): Insert a new domain into the domains table,
 * which is a table used to speed up browsing of attributes
 *
 * @param attr_name: Attribute name class containing the fully qualified domain name
 **/
//=============================================================================
void HdbPPCassandra::insert_domain(AttributeName &attr_name)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::InsertDomain);
    cass_statement_set_consistency(statement, consistency);

    cass_statement_bind_string_by_name(statement, DOMAINS_COL_FACILITY.c_str(),
                                       attr_name.tango_host_with_domain().c_str());
                                       
    cass_statement_bind_string_by_name(statement, DOMAINS_COL_DOMAIN.c_str(), attr_name.domain().c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(Query::InsertDomain), rc, __func__);

    TRACE_EXIT;
}

//=============================================================================
/**
 * insert_family(): Insert a new family into the families table,
 * which is a table used to speed up browsing of attributes
 *
 * @param attr_name: Attribute name class containing the fully qualified domain name
 **/
//=============================================================================
void HdbPPCassandra::insert_family(AttributeName &attr_name)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::InsertFamily);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);
    cass_statement_bind_string_by_name(statement, FAMILIES_COL_FACILITY.c_str(),
                                       attr_name.tango_host_with_domain().c_str());
    cass_statement_bind_string_by_name(statement, FAMILIES_COL_DOMAIN.c_str(), attr_name.domain().c_str());
    cass_statement_bind_string_by_name(statement, FAMILIES_COL_FAMILY.c_str(), attr_name.family().c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(Query::InsertFamily), rc, __func__);

    TRACE_EXIT;
}

//=============================================================================
/**
 * insert_member(): Insert a new member into the members table,
 * which is a table used to speed up browsing of attributes
 *
 * @param attr_name: Attribute name class containing the fully qualified domain name
 **/
//=============================================================================
void HdbPPCassandra::insert_member(AttributeName &attr_name)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::InsertMember);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);

    cass_statement_bind_string_by_name(statement, MEMBERS_COL_FACILITY.c_str(),
                                       attr_name.tango_host_with_domain().c_str());
                                       
    cass_statement_bind_string_by_name(statement, MEMBERS_COL_DOMAIN.c_str(), attr_name.domain().c_str());
    cass_statement_bind_string_by_name(statement, MEMBERS_COL_FAMILY.c_str(), attr_name.family().c_str());
    cass_statement_bind_string_by_name(statement, MEMBERS_COL_MEMBER.c_str(), attr_name.member().c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(Query::InsertMember), rc, __func__);

    TRACE_EXIT;
}

//=============================================================================
/**
 * insert_attr_name(): Insert a new attribute name into the attribute names table,
 * which is a table used to speed up browsing of attributes
 *
 * @param attr_name: Attribute name class containing the fully qualified domain name
 **/
//=============================================================================
void HdbPPCassandra::insert_attr_name(AttributeName &attr_name)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::InsertName);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);
    cass_statement_bind_string_by_name(statement, ATT_NAMES_COL_FACILITY.c_str(), attr_name.tango_host_with_domain().c_str());
    cass_statement_bind_string_by_name(statement, ATT_NAMES_COL_DOMAIN.c_str(), attr_name.domain().c_str());
    cass_statement_bind_string_by_name(statement, ATT_NAMES_COL_FAMILY.c_str(), attr_name.family().c_str());
    cass_statement_bind_string_by_name(statement, ATT_NAMES_COL_MEMBER.c_str(), attr_name.member().c_str());
    cass_statement_bind_string_by_name(statement, ATT_NAMES_COL_NAME.c_str(), attr_name.name().c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing insert query",
                                _prepared_statements->query_string(Query::InsertName), rc, __func__);

    TRACE_EXIT;
}

//=============================================================================
/**
 * update_ttl(): Execute the query to update the TTL for the attribute having the
 * specified ID
 *
 * @param ttl: the new ttl value
 * @param attr_name: Attribute name class containing the fully qualified domain name
 * @throws Tango::DevFailed exceptions in case of error during the query
 *         execution
 */
//=============================================================================
void HdbPPCassandra::update_ttl(unsigned int ttl, AttributeName &attr_name)
{
    TRACE_ENTER;

    CassStatement *statement = _prepared_statements->statement(Query::UpdateTtl);
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM);
    cass_statement_bind_int32_by_name(statement, CONF_COL_TTL.c_str(), ttl);
    cass_statement_bind_string_by_name(statement, CONF_COL_FACILITY.c_str(), attr_name.tango_host_with_domain().c_str());
    cass_statement_bind_string_by_name(statement, CONF_COL_NAME.c_str(), attr_name.full_attribute_name().c_str());

    CassError rc = execute_statement(statement);

    if (rc != CASS_OK)
        throw_execute_exception("ERROR executing update tll query",
                                _prepared_statements->query_id_to_str(Query::UpdateTtl), rc, __func__);

    // Cassandra command was successfull, so update the ttl in the cache
    map<string, AttributeParams>::iterator it =
        attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (it != attribute_cache.end())
    {
        it->second.ttl = ttl;
    }
    else
    {
        // in this case, the ttl should exist, since the caller loaded the attribute
        // from the database if it was not loaded, this should have cached the ttl
        stringstream error_desc;
        error_desc << "ERROR Attribute " << attr_name << " ttl is missing in the cache" << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_ATTR_CACHE, error_desc.str().c_str(), __func__);
    }

    TRACE_EXIT;
}

//=============================================================================
//=============================================================================
CassError HdbPPCassandra::execute_statement(CassStatement *statement)
{
    TRACE_ENTER;

    CassFuture *future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);
    CassError rc = cass_future_error_code(future);

    cass_future_free(future);
    cass_statement_free(statement);

    TRACE_EXIT;
    return rc;
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::throw_execute_exception(string message, string query, CassError error, const char *origin)
{
    stringstream error_desc;
    error_desc << message << " \"" << query << "\": " << cass_error_desc(error) << ends;
    LOG(Error) << error_desc.str() << endl;
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
        LOG(Error) << "ERROR invalid cassandra driver logging level" << endl;
        LOG(Error) << "Log level set by default to: DISABLED" << endl;
    }

    LOG(Debug) << "Cassandra driver logging set to: ";

    if (cassandra_logging_level == CASS_LOG_DISABLED)
    {
        LOG(Debug) << "DISABLED" << endl;
    }
    else
    {
        LOG(Debug) << cass_log_level_string(cassandra_logging_level) << endl;
    }
}

//=============================================================================
//=============================================================================
void HdbPPCassandra::set_cassandra_consistency_level(string consistency_level)
{
    if(consistency_level == "ALL")
        consistency = CASS_CONSISTENCY_ALL;
    else if(consistency_level == "EACH_QUORUM")
        consistency = CASS_CONSISTENCY_EACH_QUORUM;
    else if(consistency_level == "QUORUM")
        consistency = CASS_CONSISTENCY_QUORUM;
    else if(consistency_level == "LOCAL_QUORUM")
        consistency = CASS_CONSISTENCY_LOCAL_QUORUM;
    else if(consistency_level == "ONE")
        consistency = CASS_CONSISTENCY_ONE;
    else if(consistency_level == "TWO")
        consistency = CASS_CONSISTENCY_TWO;
    else if(consistency_level == "THREE")
        consistency = CASS_CONSISTENCY_THREE;
    else if(consistency_level == "LOCAL_ONE")
        consistency = CASS_CONSISTENCY_LOCAL_ONE;
    else if(consistency_level == "ANY")
        consistency = CASS_CONSISTENCY_ANY;
    else if(consistency_level == "SERIAL")
        consistency = CASS_CONSISTENCY_SERIAL;
    else if(consistency_level == "LOCAL_SERIAL")
        consistency = CASS_CONSISTENCY_LOCAL_SERIAL;
    else
    {
        stringstream error_desc;
        error_desc << "An invalid consistency configuration was passed: " << consistency_level << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_CONFIG, error_desc.str(), __func__);
    }
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_config_param(const map<string, string> &conf, string param, bool mandatory)
{
    auto iter = conf.find(param);

    if(iter == conf.end() && mandatory)
    {
        stringstream error_desc;

        error_desc << "Configuration parsing error: mandatory configuration parameter: " << param << " not found"
                   << ends;

        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_CONFIG, error_desc.str(), __func__);
    }
    else if(iter != conf.end())
    {
        LOG(Debug) << "Configuration: " << param << " set to: " << (*iter).second << endl;
    }
    else
    {
        LOG(Debug) << "Configuration: " << param << " not found" << endl;
    }

    // for non-mandatory config params that have not been set, just return
    // an empty string
    return iter ==  conf.end() ? "" : (*iter).second;
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
