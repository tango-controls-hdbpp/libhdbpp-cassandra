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

class PreparedStatementCache
{
public:
    PreparedStatementCache(CassSession *session, std::string keyspace_name)
        : _session(session), _keyspace_name(keyspace_name)
    {
    }

    CassStatement *statement(Query query);
    CassStatement *statement(int data_type, int data_format, int data_write_type);

    const std::string &query_string(Query query) { return look_up_query_string(query); }
    const std::string &query_string(int data_type, int data_format, int data_write_type);

    const std::string &query_id_to_str(Query query) const;
    std::string query_id_to_str(int data_type, int data_format, int data_write_type) const;

    void free_cache();

    int get_insert_attr_ttl_bind_position(int data_write_type) const;

    std::string get_table_name(int data_type, int data_format, int data_write_type) const;

    friend std::ostream &operator<<(std::ostream &os, const PreparedStatementCache &cache);

    int query_cache_size() const { return _query_cache.size(); }
    int statement_cache_size() const { return _prepared_statement_cache.size(); }

private:
    const std::string &look_up_query_string(Query query);
    const std::string &look_up_query_string(int data_type, int data_format, int data_write_type);

    std::string create_query_id_from_data(int data_type, int data_format, int data_write_type) const;

    CassStatement *create_and_cache_prepared_object(const std::string &query_id,
                                                    const std::string &query_str);

    void cache_query_string(const std::string &query_id, const std::string &query_string);

    // use unordered maps, since the key is hashed and we only care about look
    // times. Further, we do no insert/deletes on the cache. This should give
    // the cache the best performance
    std::unordered_map<std::string, const CassPrepared *> _prepared_statement_cache;
    std::unordered_map<std::string, std::string> _query_cache;

    CassSession *_session;
    std::string _keyspace_name;
};

}; // namespace HDBPP

#endif