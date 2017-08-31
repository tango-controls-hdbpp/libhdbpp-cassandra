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

#ifndef _HDBPP_DBCONNECTION_H
#define _HDBPP_DBCONNECTION_H

#include "CassandraConnection.h"
#include "DbCommands.h"
#include "catch.hpp"
#include <cassandra.h>
#include <string>

const std::string DB_FILENAME = "etc/create_hdb_cassandra.cql";
const std::string CONTACT_POINT = "acu-cassandra";
const std::string KEYSPACE = "hdb";

class DbConnection 
{
public:

    CassandraConnection* get(bool set_tables_loaded = true) 
    {
        if (!connection())
        {
            connection() = new CassandraConnection();

            if (!connection()->connect("acu-cassandra"))
            {
                FAIL("Database connection failed");
                delete connection();
                connection() = NULL;
                return NULL;
            }
            
            WARN("Database connection alive");

            // always recycle the tables for the test
            drop_tables();

            if(set_tables_loaded)
                load_tables();
        }

        if(set_tables_loaded && !tables_loaded())
            load_tables();

        if(!set_tables_loaded && tables_loaded())
            drop_tables();

        return connection();
    }

private:

    void load_tables()
    {
        if(db_commands().setup_commands().empty())
            if(!db_commands().load_setup_from_file(DB_FILENAME)) 
                FAIL("Unable to load DB Schema from " << DB_FILENAME);

        for (auto query : db_commands().setup_commands())
        {
            WARN(query);
            if (!connection()->execute_query(query)) 
                FAIL("Failed to execute query " << query);
        }

        WARN("Database tables loaded");
        tables_loaded() = true;
    }

    void drop_tables()
    {
        for (auto query : db_commands().teardown_commands())
            if (!connection()->execute_query(query)) 
                FAIL("Failed to execute query " << query);

        WARN("Database tables dropped");
        tables_loaded() = false;        
    }

    static CassandraConnection*& connection()
    {
        static CassandraConnection *connection = NULL;
        return connection;
    }

    static bool& tables_loaded()
    {
        static bool tables_loaded_state = false;
        return tables_loaded_state;
    }

    static DbCommands& db_commands()
    {
        static DbCommands db_cmds;
        return db_cmds;
    }
};

#endif