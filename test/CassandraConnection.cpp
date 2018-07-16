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

#include "CassandraConnection.h"
#include <cassert>

using namespace std;

//=============================================================================
//=============================================================================
CassandraConnection::CassandraConnection()
{
    _cluster = NULL;
    _session = NULL;
}

//=============================================================================
//=============================================================================
CassandraConnection::~CassandraConnection()
{
    disconnect();
}

//=============================================================================
//=============================================================================
bool CassandraConnection::connect(std::string hosts)
{
    assert(_session == NULL);
    assert(_cluster == NULL);
    assert(!hosts.empty());

    create_cluster(hosts.c_str());
    _session = cass_session_new();
    
    // connect the session, if we fail clean up so another attempt 
    // may be made with this object
    if (!connect_session())
    {
        cass_cluster_free(_cluster);
        _cluster = NULL;
        return false;
    }

    return true;
}

//=============================================================================
//=============================================================================
bool CassandraConnection::disconnect()
{
    if (_cluster)
    {
        CassFuture *close_future = NULL;
        close_future = cass_session_close(_session);
        cass_future_wait(close_future);
        cass_future_free(close_future);
        cass_cluster_free(_cluster);
        cass_session_free(_session);
        _cluster = NULL;
        _session = NULL;
    }

    return true;
}

//=============================================================================
//=============================================================================
bool CassandraConnection::execute_query(std::string query) 
{
    assert(_session != NULL);
    assert(_cluster != NULL);
    assert(!query.empty());

    CassError rc = CASS_OK;
    CassStatement *statement = cass_statement_new(query.c_str(), 0);
    CassFuture *future = cass_session_execute(_session, statement);

    cass_future_wait(future);
    rc = cass_future_error_code(future);

    if (rc != CASS_OK) 
    {
        print_error(future);
        cass_future_free(future);
        cass_statement_free(statement);
        return false;
    }

    cass_future_free(future);
    cass_statement_free(statement);
    return true;
}

//=============================================================================
//=============================================================================
bool CassandraConnection::execute_statement(CassStatement *statement) 
{
    assert(_session != NULL);
    assert(_cluster != NULL);
    assert(statement != NULL);

    CassError rc = CASS_OK;
    CassFuture* future = cass_session_execute(_session, statement);

    cass_future_wait(future);
    rc = cass_future_error_code(future);

    if (rc != CASS_OK)
    {
        print_error(future);
        cass_future_free(future);
        return false;
    }

    cass_future_free(future);
    return true;
}    

//=============================================================================
//=============================================================================
void CassandraConnection::create_cluster(const char *hosts) 
{
    _cluster = cass_cluster_new();
    cass_cluster_set_contact_points(_cluster, hosts);
}

//=============================================================================
//=============================================================================
bool CassandraConnection::connect_session() 
{
    CassError rc = CASS_OK;
    CassFuture *future = cass_session_connect(_session, _cluster);

    cass_future_wait(future);
    rc = cass_future_error_code(future);

    if (rc != CASS_OK) 
    {
        print_error(future);
        cass_future_free(future);
        cass_session_free(_session);
        _session = NULL;   
        return false;
    }

    cass_future_free(future);
    return true;
}

//=============================================================================
//=============================================================================
void CassandraConnection::print_error(CassFuture *future) 
{
    const char *message;
    size_t message_length;
    cass_future_error_message(future, &message, &message_length);
    fprintf(stderr, "Error: %.*s\n", (int)message_length, message);
}
