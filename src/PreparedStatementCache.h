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

#ifndef _HDBPP_PREPARED_STATEMENT_CACHE_H
#define _HDBPP_PREPARED_STATEMENT_CACHE_H

#include <cassandra.h>
#include <iostream>
#include <string>
#include <unordered_map>

namespace HDBPP
{
/**
* @enum Query
* @ingroup HDBPP-Implementation
* @brief Strong typed enum to represent the query types supported by the cache.
*/
enum class Query
{
    FindAttrIdAndTtlInDb,
    FindAttrIdTypeAndTtlInDb,
    FindLastEvent,
    InsertHistoryEvent,
    InsertParamAttribute,
    InsertAttributeConf,
    InsertDomain,
    InsertFamily,
    InsertMember,
    InsertName,
    UpdateTtl
};

/**
 * @class PreparedStatementCache
 * @ingroup HDBPP-Implementation
 * @brief Provide a caching mechanism for prepared statements that are used with Cassandra
 *
 * This class separates out the string building for and storing of cassandra prepared
 * objects. The class is based around to maps that act as a cache for both the
 * actual query strings and the associated prepared objects. The cache supports a simple
 * enum query type request for most queires, but for attribute inserts we need to build
 * a query string and prepared object based around the attribute data, format and write type.
 *
 * Currently it is not possible to bind the ttl in a USING TTL by name, therefore the class
 * provides a dirty workaround, get_insert_attr_ttl_bind_position(), to inform the user
 * where (numerically) the ttl should be bound.
 */
class PreparedStatementCache
{
public:
    /**
     * @brief Create a PreparedStatementCache for the given session and keyspace
     *
     * @param session Session to create the cache for
     * @param keyspace_name Keyspace to create the cache for
     */
    PreparedStatementCache(CassSession *session, std::string keyspace_name)
        : _session(session), _keyspace_name(keyspace_name)
    {
    }

    /**
     * @brief Return a cassandra statement created from a prepared object.
     *
     * Creates and caches the prepared object if it does not exist. Then a statement
     * is generated from to be returned. Caller should bind to this statement and
     * execute the query. The statement MUST be deleted by the user.
     *
     * @param query Selected query.
     * @throw Tango::DevFailed For unsupported query types.
     */
    CassStatement *statement(Query query);

    /**
     * @brief Return a cassandra statement created from a prepared object.
     *
     * Creates and caches the prepared object if it does not exist. Then a statement
     * is generated from to be returned. Caller should bind to this statement and
     * execute the query. The statement MUST be deleted by the user.
     *
     * @param data_type The type of the tango event data.
     * @param data_format The format, SCALAR/SPECTRUM, of the tango event data.
     * @param write_type The read/write type of the tango event data.
     */
    CassStatement *statement(int data_type, int data_format, int data_write_type);

    /**
     * @brief Return ther query string used.
     *
     * This will create the query string and cache it if it does not exist.
     * Then the string is returned.
     *
     * @param query Selected query.
     */
    const std::string &query_string(Query query) { return look_up_query_string(query); }

    /**
     * @brief Return ther query string used.
     *
     * This will create the insert attribute query from the parameters and cache it
     * if it does not exist. Then the string is returned.
     *
     * @param data_type The type of the tango event data.
     * @param data_format The format, SCALAR/SPECTRUM, of the tango event data.
     * @param write_type The read/write type of the tango event data.
     */
    const std::string &query_string(int data_type, int data_format, int data_write_type);

    /**
     * @brief Converts the query enum into a query id string.
     *
     * Mainly helpful for debug output, so the code can elaborate on what
     * query is being when we hit failures etc.
     *
     * @param query Type of query we want the string for.
     * @throw Tango::DevFailed For unsupported query types.
     */
    const std::string &query_id_to_str(Query query) const;

    /**
     * @brief Converts the type, format and write type into a query id string.
     *
     * Mainly helpful for debug output, so the code can elaborate on what
     * query is being when we hit failures etc.
     *
     * @param data_type The type of the tango event data.
     * @param data_format The format, SCALAR/SPECTRUM, of the tango event data.
     * @param write_type The read/write type of the tango event data.
     */
    std::string query_id_to_str(int data_type, int data_format, int data_write_type) const;

    /**
     * @brief Delete all cached items.
     *
     * Clears the cache and deallocates prepared objects. Must be called to avoid memory
     * leaks when finished with the cache.
     */
    void free_cache();

    /**
     * @brief Get the position of the ttl field in the Insert Attribute query.
     *
     * We can not bind the ttl by name in a statement that contains USING TLL ?.
     * Instead we are forced to bind by its position. To work around this we
     * provide this ugly function to tell the the user of query_string(int, int, int)
     * where the position of the ttl so it can be bound by position rather than name.
     *
     * @param write_type The read/write type of the tango event data.
     */
    int get_insert_attr_ttl_bind_position(int data_write_type) const;

    /**
     * @brief Return a unique table name based on the parameters.
     *
     * The table name starts with "att_", then the function uses the format (scalar/spectrum),
     * followed by the type (string based on type) and finally appends a string based on the
     * read/write (r/w/rw). This name is then unique for this combination.
     *
     * @param data_type The type of the tango event data.
     * @param data_format The format, SCALAR/SPECTRUM, of the tango event data.
     * @param write_type The read/write type of the tango event data.
     * @throw Tango::DevFailed For unsupported types.
     */
    std::string get_table_name(int data_type, int data_format, int data_write_type) const;

    /**
     * @brief Return the query string cache size. Helpful debug.
     */
    int query_cache_size() const { return _query_cache.size(); }

    /**
     * @brief Return the prepared statement cache size. Helpful debug.
     */
    int statement_cache_size() const { return _prepared_statement_cache.size(); }

    /**
     * @brief Partially dump the cache to the given stream.
     */
    friend std::ostream &operator<<(std::ostream &os, const PreparedStatementCache &cache);

private:
    // query string look up functions. Both will create and cache missing queries
    const std::string &look_up_query_string(Query query);
    const std::string &look_up_query_string(int data_type, int data_format, int data_write_type);

    std::string create_query_id_from_data(int data_type, int data_format, int data_write_type) const;

    CassStatement *create_and_cache_prepared_object(const std::string &query_id,
                                                    const std::string &query_str);

    // insert the query_string into the query cache, indexed by query id
    void cache_query_string(const std::string &query_id, const std::string &query_string);

    // use unordered maps, since the key is hashed and we only care about look
    // times. Further, we do no insert/deletes on the cache. This should give
    // the cache the best performance
    std::unordered_map<std::string, const CassPrepared *> _prepared_statement_cache;
    std::unordered_map<std::string, std::string> _query_cache;

    // session and keyspace the cache was contructed with
    CassSession *_session;
    std::string _keyspace_name;
};

}; // namespace HDBPP

#endif