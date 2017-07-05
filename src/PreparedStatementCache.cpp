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

#include "PreparedStatementCache.h"
#include "LibHdb++Defines.h"
#include "Log.h"
#include "tango.h"

#include <iostream>

namespace HDBPP
{
//=============================================================================
//=============================================================================
CassStatement *PreparedStatementCache::statement(Query query)
{
    CassStatement *statement = NULL;
    const string &query_id = query_id_to_str(query);
    auto statement_iter = _prepared_statement_cache.find(query_id);

    // if the prepared object does not exist, we must create it, otherwise
    // we can just retrieve it and create a statement
    if (statement_iter == _prepared_statement_cache.end())
    {
        // find a query string for this query type and create a statement,
        // in the process we will cache a prepared object for this query
        statement = create_and_cache_prepared_object(query_id, look_up_query_string(query));
    }
    else
    {
        // find the prepared object in the cache and create a statement from it
        statement = cass_prepared_bind((*statement_iter).second);
    }

    return statement;
}

//=============================================================================
//=============================================================================
CassStatement *PreparedStatementCache::statement(int data_type, int data_format, int data_write_type)
{
    CassStatement *statement = NULL;
    string query_id = create_query_id_from_data(data_type, data_format, data_write_type);

    auto statement_iter = _prepared_statement_cache.find(query_id);

    // if the prepared object does not exist, we must create it via its unique
    // table attributes
    if (statement_iter == _prepared_statement_cache.end())
    {
        // find a query string for this query type and create a statement,
        // in the process we will cache a prepared object for this query
        statement = create_and_cache_prepared_object(query_id, look_up_query_string(data_type, data_format,
                                                                                    data_write_type));
    }
    else
    {
        // find the prepared object in the cache and create a statement from it
        statement = cass_prepared_bind((*statement_iter).second);
    }

    return statement;
}

//=============================================================================
//=============================================================================
void PreparedStatementCache::free_cache()
{
    for (auto &iter : _prepared_statement_cache)
        cass_prepared_free(iter.second);
}

//=============================================================================
//=============================================================================
const string &PreparedStatementCache::look_up_query_string(Query query)
{
    const string &query_id = query_id_to_str(query);
    auto query_iter = _query_cache.find(query_id);

    // if we have not built the query, then we must first do this
    // before it can be used for prepared statements by the user
    if (query_iter == _query_cache.end())
    {
        ostringstream query_str;

        switch (query)
        {
            case Query::FindAttrIdAndTtlInDb:
            {
                query_str << "SELECT " << CONF_COL_ID << ", " << CONF_COL_TTL << " FROM "
                          << _keyspace_name << "." << CONF_TABLE_NAME << " WHERE " << CONF_COL_NAME
                          << " = ? AND " << CONF_COL_FACILITY << " = ?" << ends;
            }
            break;

            case Query::FindAttrIdTypeAndTtlInDb:
            {
                query_str << "SELECT " << CONF_COL_ID << "," << CONF_COL_TYPE << "," << CONF_COL_TTL
                          << " FROM " << _keyspace_name << "." << CONF_TABLE_NAME << " WHERE "
                          << CONF_COL_NAME << " = ? AND " << CONF_COL_FACILITY << " = ?" << ends;
            }
            break;

            case Query::FindLastEvent:
            {
                query_str << "SELECT " << HISTORY_COL_EVENT << " FROM " << _keyspace_name << "."
                          << HISTORY_TABLE_NAME << " WHERE " << HISTORY_COL_ID << " = ?"
                          << " ORDER BY " << HISTORY_COL_TIME << " DESC LIMIT 1" << ends;
            }
            break;

            case Query::InsertHistoryEvent:
            {
                query_str << "INSERT INTO " << _keyspace_name << "." << HISTORY_TABLE_NAME << " ("
                          << HISTORY_COL_ID << "," << HISTORY_COL_EVENT << "," << HISTORY_COL_TIME
                          << "," << HISTORY_COL_TIME_US << ")"
                          << " VALUES ( ?, ?, ?, ?)" << ends;
            }
            break;

            case Query::InsertParamAttribute:
            {
                query_str << "INSERT INTO " << _keyspace_name << "." << PARAM_TABLE_NAME << " ("
                          << PARAM_COL_ID << "," << PARAM_COL_EV_TIME << "," << PARAM_COL_EV_TIME_US
                          << "," << PARAM_COL_INS_TIME << "," << PARAM_COL_INS_TIME_US << ","
                          << PARAM_COL_LABEL << "," << PARAM_COL_UNIT << "," << PARAM_COL_STANDARDUNIT
                          << "," << PARAM_COL_DISPLAYUNIT << "," << PARAM_COL_FORMAT << ","
                          << PARAM_COL_ARCHIVERELCHANGE << "," << PARAM_COL_ARCHIVEABSCHANGE << ","
                          << PARAM_COL_ARCHIVEPERIOD << "," << PARAM_COL_DESCRIPTION << ")";

                query_str << " VALUES (?,?,?,"
                          << "?,?,"
                          << "?,?,?,"
                          << "?,?,?,"
                          << "?,?,?)" << ends;
            }
            break;

            case Query::InsertAttributeConf:
            {
                query_str << "INSERT INTO " << _keyspace_name << "." << CONF_TABLE_NAME << " ("
                          << CONF_COL_ID << "," << CONF_COL_FACILITY << "," << CONF_COL_NAME << ","
                          << CONF_COL_TYPE << "," << CONF_COL_TTL << ")"
                          << " VALUES (?, ?, ?, ?, ?)" << ends;
            }
            break;

            case Query::InsertDomain:
            {
                query_str << "INSERT INTO " << _keyspace_name << "." << DOMAINS_TABLE_NAME << " ("
                          << DOMAINS_COL_FACILITY << "," << DOMAINS_COL_DOMAIN << ")"
                          << " VALUES (?,?)" << ends;
            }
            break;

            case Query::InsertFamily:
            {
                query_str << "INSERT INTO " << _keyspace_name << "." << FAMILIES_TABLE_NAME << " ("
                          << FAMILIES_COL_FACILITY << "," << FAMILIES_COL_DOMAIN << ","
                          << FAMILIES_COL_FAMILY << ")"
                          << " VALUES (?,?,?)" << ends;
            }
            break;

            case Query::InsertMember:
            {
                query_str << "INSERT INTO " << _keyspace_name << "." << MEMBERS_TABLE_NAME << " ("
                          << MEMBERS_COL_FACILITY << "," << MEMBERS_COL_DOMAIN << ","
                          << MEMBERS_COL_FAMILY << "," << MEMBERS_COL_MEMBER << ")"
                          << " VALUES (?,?,?,?)" << ends;
            }
            break;

            case Query::InsertName:
            {
                query_str << "INSERT INTO " << _keyspace_name << "." << ATT_NAMES_TABLE_NAME << " ("
                          << ATT_NAMES_COL_FACILITY << "," << ATT_NAMES_COL_DOMAIN << ","
                          << ATT_NAMES_COL_FAMILY << "," << ATT_NAMES_COL_MEMBER << ","
                          << ATT_NAMES_COL_NAME << ")"
                          << " VALUES (?,?,?,?,?)" << ends;
            }
            break;

            case Query::UpdateTtl:
            {
                query_str << "UPDATE " << _keyspace_name << "." << CONF_TABLE_NAME << " SET "
                          << CONF_COL_TTL << " = ? WHERE " << CONF_COL_FACILITY << " = ? AND "
                          << CONF_COL_NAME << " = ?" << ends;
            }
            break;

            default:
            {
                stringstream error_desc;
                error_desc << "Unknown query type: << " << static_cast<int>(query) << ends;
                LOG(Error) << error_desc.str() << endl;
                Tango::Except::throw_exception(EXCEPTION_UNKNOWN_QUERY, error_desc.str(), __func__);
            }
        }

        cache_query_string(query_id, query_str.str());
    }

    return _query_cache[query_id];
}

//=============================================================================
//=============================================================================

const string &PreparedStatementCache::look_up_query_string(int data_type, int data_format, int data_write_type)
{
    string query_id = create_query_id_from_data(data_type, data_format, data_write_type);
    auto query_iter = _query_cache.find(query_id);

    // if we have not built the query, then we must first do this
    // before it can be used for prepared statements for by the user
    if (query_iter == _query_cache.end())
    {
        // first create the unique query string for this combination
        string table_name = get_table_name(data_type, data_format, data_write_type);
        ostringstream query_str;

        query_str << "INSERT INTO " << _keyspace_name << "." << table_name << " (" << SC_COL_ID
                  << "," << SC_COL_PERIOD << "," << SC_COL_EV_TIME << "," << SC_COL_EV_TIME_US
                  << "," << SC_COL_RCV_TIME << "," << SC_COL_RCV_TIME_US << "," << SC_COL_INS_TIME
                  << "," << SC_COL_INS_TIME_US << "," << SC_COL_QUALITY << "," << SC_COL_ERROR_DESC;

        // TODO: store dim_x, dim_y for spectrum attributes

        // the query column size may change based on the data_write_type variable
        if (data_write_type != Tango::WRITE)
            query_str << "," << SC_COL_VALUE_R;

        if (data_write_type != Tango::READ)
            query_str << "," << SC_COL_VALUE_W;

        query_str << ") VALUES (?,?,?,?,?,?,?,?,?,?";

        if (data_write_type != Tango::WRITE)
            query_str << ",?";

        if (data_write_type != Tango::READ)
            query_str << ",?";

        query_str << ")"
                  << " USING TTL ?" << ends;

        cache_query_string(query_id, query_str.str());
    }

    return _query_cache[query_id];
}

//=============================================================================
//=============================================================================
const string &PreparedStatementCache::query_string(int data_type, int data_format, int data_write_type)
{
    return look_up_query_string(data_type, data_format, data_write_type);
}

//=============================================================================
//=============================================================================
CassStatement *PreparedStatementCache::create_and_cache_prepared_object(const string &query_id,
                                                                        const string &query_str)
{
    // create a prepared statement for this query
    CassFuture *prepare_future = cass_session_prepare(_session, query_str.c_str());

    // wait for the statement to prepare and get the result
    CassError error = cass_future_error_code(prepare_future);

    if (error != CASS_OK)
    {
        cass_future_free(prepare_future);
        stringstream error_desc;
        error_desc << "Failed to prepare statement for query: " << query_str
                   << ". Error: " << cass_error_desc(error) << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_PREPARED_STATEMENT_ERROR, error_desc.str().c_str(),
                                       __func__);
    }

    // get the prepared object and store it in our cache
    const CassPrepared *prepared = cass_future_get_prepared(prepare_future);
    _prepared_statement_cache[query_id] = prepared;

    LOG(Debug) << "Prepared and cached a new statement" << endl;
    LOG(Debug) << "Prepared statement cache is now: " << _prepared_statement_cache.size() << endl;

    // free the future
    cass_future_free(prepare_future);

    // we can now create a statement from the prepared object and return it
    return cass_prepared_bind(prepared);
}

//=============================================================================
//=============================================================================
string PreparedStatementCache::get_table_name(int data_type, int data_format, int data_write_type) const
{
    string table_name = "att_";

    // first add the type
    table_name.append(data_format == Tango::SCALAR ? TYPE_SCALAR : TYPE_ARRAY).append("_");

    // add the data type
    switch (data_type)
    {
        case Tango::DEV_BOOLEAN:
            table_name.append(TYPE_DEV_BOOLEAN).append("_");
            break;
        case Tango::DEV_UCHAR:
            table_name.append(TYPE_DEV_UCHAR).append("_");
            break;
        case Tango::DEV_SHORT:
            table_name.append(TYPE_DEV_SHORT).append("_");
            break;
        case Tango::DEV_USHORT:
            table_name.append(TYPE_DEV_USHORT).append("_");
            break;
        case Tango::DEV_LONG:
            table_name.append(TYPE_DEV_LONG).append("_");
            break;
        case Tango::DEV_ULONG:
            table_name.append(TYPE_DEV_ULONG).append("_");
            break;
        case Tango::DEV_LONG64:
            table_name.append(TYPE_DEV_LONG64).append("_");
            break;
        case Tango::DEV_ULONG64:
            table_name.append(TYPE_DEV_ULONG64).append("_");
            break;
        case Tango::DEV_FLOAT:
            table_name.append(TYPE_DEV_FLOAT).append("_");
            break;
        case Tango::DEV_DOUBLE:
            table_name.append(TYPE_DEV_DOUBLE).append("_");
            break;
        case Tango::DEV_STRING:
            table_name.append(TYPE_DEV_STRING).append("_");
            break;
        case Tango::DEV_STATE:
            table_name.append(TYPE_DEV_STATE).append("_");
            break;
        case Tango::DEV_ENCODED:
            table_name.append(TYPE_DEV_ENCODED).append("_");
            break;
        default:
            stringstream error_desc;
            error_desc << "(" << data_type << ", ...): Type not supported" << ends;
            LOG(Error) << error_desc.str() << endl;
            Tango::Except::throw_exception(EXCEPTION_TYPE_UNSUPPORTED_ATTR, error_desc.str(), __func__);
    }

    // finally the write type
    table_name.append(data_write_type == Tango::READ ? TYPE_RO : TYPE_RW);

    return table_name;
}

string PreparedStatementCache::create_query_id_from_data(int data_type, int data_format, int data_write_type) const
{
    // since this is a special case not using the enums, we need to build a string to
    // cache against. In this case, uwe use a prefix of InsertAttr combined with the
    // table name
    return string("InsertAttr_").append(get_table_name(data_type, data_format, data_write_type));
}

//=============================================================================
//=============================================================================
void PreparedStatementCache::cache_query_string(const string &query_id, const string &query_string)
{
    _query_cache[query_id] = query_string;
    LOG(Debug) << "Cached the query string: " << query_string << endl;
    LOG(Debug) << "Query cache is now: " << _query_cache.size() << endl;
}

//=============================================================================
//=============================================================================
const string &PreparedStatementCache::query_id_to_str(Query query) const
{
    switch (query)
    {
        case Query::FindAttrIdAndTtlInDb:
        {
            static string find_attr_id_and_ttl_in_db = "FindAttrIdAndTtlInDb";
            return find_attr_id_and_ttl_in_db;
        }
        break;

        case Query::FindAttrIdTypeAndTtlInDb:
        {
            static string find_attr_id_type_and_ttl_in_db = "FindAttrIdTypeAndTtlInDb";
            return find_attr_id_type_and_ttl_in_db;
        }
        break;

        case Query::FindLastEvent:
        {
            static string find_last_event = "FindLastEvent";
            return find_last_event;
        }
        break;

        case Query::InsertHistoryEvent:
        {
            static string insert_history_event = "InsertHistoryEvent";
            return insert_history_event;
        }
        break;

        case Query::InsertParamAttribute:
        {
            static string insert_parameter_attribute = "InsertParamAttribute";
            return insert_parameter_attribute;
        }
        break;

        case Query::InsertAttributeConf:
        {
            static string insert_attribute_conf = "InsertAttributeConf";
            return insert_attribute_conf;
        }
        break;

        case Query::InsertDomain:
        {
            static string insert_domain = "InsertDomain";
            return insert_domain;
        }
        break;

        case Query::InsertFamily:
        {
            static string insert_family = "InsertFamily";
            return insert_family;
        }
        break;

        case Query::InsertMember:
        {
            static string insert_member = "InsertMember";
            return insert_member;
        }
        break;

        case Query::InsertName:
        {
            static string insert_name = "InsertName";
            return insert_name;
        }
        break;

        case Query::UpdateTtl:
        {
            static string update_ttl = "UpdateTtl";
            return update_ttl;
        }
        break;

        default:
        {
            stringstream error_desc;
            error_desc << "Unknown query type: << " << static_cast<int>(query) << ends;
            LOG(Error) << error_desc.str() << endl;
            Tango::Except::throw_exception(EXCEPTION_UNKNOWN_QUERY, error_desc.str(), __func__);
        }
    }

    // to keep the compiler happy, we should never reach here
    static string error = "Unknown";
    return error;
}

//=============================================================================
//=============================================================================
string PreparedStatementCache::query_id_to_str(int data_type, int data_format, int data_write_type) const
{
    return create_query_id_from_data(data_type, data_format, data_write_type);
}

//=============================================================================
//=============================================================================
int PreparedStatementCache::get_insert_attr_ttl_bind_position(int data_write_type) const
{
    // this is ugly ugly ugly, but we can not bind the ttl by name in a
    // statement that contains USING TLL ?. We are forced to bind by its
    // position. To work around this for the InsertAttr_ queries, we simply
    // calculate its point and feed it back to the main library.

    // there are two scenarios (now we have condensed the statement), the
    // InsertAttr_ type query has 10 bind points in the string, then an
    // additional one or two based on data_write_type and finally the
    // ttl bind point. This means the ttl is either 11 or 12 based on the
    // data_write_type value. We can boil it down to a simple return below:

    // 9 bind points before the data write type
    int bind_point = 9;

    if (data_write_type != Tango::WRITE)
        bind_point++;

    if (data_write_type != Tango::READ)
        bind_point++;

    // add one to get us to the ttl bind point
    bind_point++;
    return bind_point;
}

//=============================================================================
//=============================================================================
std::ostream &operator<<(std::ostream &os, const PreparedStatementCache &cache)
{
    os << "_session: " << static_cast<void *>(cache._session) << " "
       << "_keyspace_name: " << cache._keyspace_name << " "
       << "_prepared_statement_cache.size(): " << cache._prepared_statement_cache.size() << " "
       << "_query_cache.size(): " << cache._query_cache.size();

    return os;
}
}