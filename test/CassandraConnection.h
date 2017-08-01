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

#ifndef _HDBPP_CASSANDRACONNECTION_H
#define _HDBPP_CASSANDRACONNECTION_H

#include <cassandra.h>
#include <string>

class CassandraConnection
{
public:

    CassandraConnection();
    virtual ~CassandraConnection();

    bool connect(std::string hosts);
    bool disconnect();

    bool execute_query(std::string query);
    bool execute_statement(CassStatement *statement);

    CassSession* session() { return _session; }

private:

    void create_cluster(const char *hosts);
    bool connect_session();

    void print_error(CassFuture *future);

    CassCluster *_cluster;
    CassSession *_session;
};

#endif