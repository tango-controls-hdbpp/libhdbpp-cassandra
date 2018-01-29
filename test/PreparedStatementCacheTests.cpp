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
#include "CassandraConnection.h"
#include "DbConnection.h"
#include "LibHdb++Defines.h"
#include "Log.h"
#include "catch.hpp"
#include "tango.h"

using namespace std;
using namespace HDBPP;
using namespace Catch;

DbConnection db;

SCENARIO("Query string cache can grow when query requests are made", "[prepared statements]")
{
    // ensure the database connection is up
    db.get();

    GIVEN("A query string cache of size zero")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);
        REQUIRE(cache.query_cache_size() == 0);

        WHEN("Query string is requested")
        {
            REQUIRE(cache.query_string(Query::GetAttrIdAndTtl).size() != 0);

            THEN("Query string cache grows by 1")
            {
                REQUIRE(cache.query_cache_size() == 1);
            }
            AND_WHEN("The same query string is requested again")
            {
                REQUIRE(cache.query_string(Query::GetAttrIdAndTtl).size() != 0);

                THEN("Query string cache does not grow")
                {
                    REQUIRE(cache.query_cache_size() == 1);
                }
            }
            AND_WHEN("A second unique query string is requested")
            {
                REQUIRE(cache.query_string(Query::FindLastEvent).size() != 0);

                THEN("Query string cache grows by 1 again")
                {
                    REQUIRE(cache.query_cache_size() == 2);
                }
            }                         
        }
    }
}

SCENARIO("Statement cache can grow when statement requests are made", "[prepared statements]")
{
    INFO("BRING UP")

    // ensure the database connection is up
    db.get();

    GIVEN("A prepared statement cache of size zero")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);
        REQUIRE(cache.statement_cache_size() == 0);

        WHEN("A prepared statement is requested")
        {
            CassStatement *statement = NULL;
            
            REQUIRE_NOTHROW(statement = cache.statement(Query::GetAttrIdAndTtl));
            REQUIRE(statement != NULL);

            THEN("Statement cache grows by 1")
            {
                REQUIRE(cache.statement_cache_size() == 1);
            }
            AND_WHEN("The same statement is requested twice")
            {
                REQUIRE_NOTHROW(statement = cache.statement(Query::GetAttrIdAndTtl));
                REQUIRE(statement != NULL);

                THEN("Statement cache remains size 1")
                {
                    REQUIRE(cache.statement_cache_size() == 1);
                }            
            }             
            AND_WHEN("A second unique statement is requested")
            {
                REQUIRE_NOTHROW(statement = cache.statement(Query::FindLastEvent));
                REQUIRE(statement != NULL);

                THEN("Statement cache grows should be 2")
                {
                    REQUIRE(cache.statement_cache_size() == 2);
                }
            }                
        }                              
    }
}

SCENARIO("Statement cache can grow both query string and statement cache when requests are made", "[prepared statements]")
{
    // ensure the database connection is up
    db.get();

    GIVEN("A prepared statement cache of size zero and a query string cache of size zero")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);
        REQUIRE(cache.statement_cache_size() == 0);
        REQUIRE(cache.query_cache_size() == 0);

        WHEN("A prepared statement is requested")
        {
            REQUIRE(cache.statement(Query::GetAttrIdAndTtl) != NULL);

            THEN("Statement cache and query string cache grows by 1")
            {
                REQUIRE(cache.statement_cache_size() == 1);
                REQUIRE(cache.query_cache_size() == 1);
            }
            AND_WHEN("A new query string is requested")
            {
                REQUIRE(cache.query_string(Query::FindLastEvent).size() != 0);            

                THEN("Statement cache does not grow, but query cache does grow by 1")
                {
                    REQUIRE(cache.statement_cache_size() == 1);
                    REQUIRE(cache.query_cache_size() == 2);
                }
                AND_WHEN("A statement which has a cached query string is requested")
                {
                    CassStatement *statement = NULL;
                    REQUIRE_NOTHROW(statement = cache.statement(Query::FindLastEvent));
                    REQUIRE(statement != NULL);                    

                    THEN("Statement cache grows by 1, but the query string does not")
                    {
                        REQUIRE(cache.statement_cache_size() == 2);
                        REQUIRE(cache.query_cache_size() == 2); 
                    }
                }                                
            }           
        }
    }
}

SCENARIO("All enum queries requests provide valid query strings, statements and name conversions", "[prepared statements]")
{
    // ensure the database connection is up
    db.get();

    GIVEN("A prepared statement cache")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);
        REQUIRE(cache.statement_cache_size() == 0);
        REQUIRE(cache.query_cache_size() == 0);

        WHEN("GetAttrIdAndTtl")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::GetAttrIdAndTtl));
                REQUIRE(statement != NULL);                     
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::GetAttrIdAndTtl).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::GetAttrIdAndTtl).empty() == false);
            }            
        }
        WHEN("GetAttrDataType")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::GetAttrDataType));
                REQUIRE(statement != NULL);
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::GetAttrDataType).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::GetAttrDataType).empty() == false);
            }
        }
        WHEN("FindLastEvent")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::FindLastEvent));
                REQUIRE(statement != NULL);                
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::FindLastEvent).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::FindLastEvent).empty() == false);
            }
        }   
        WHEN("InsertHistoryEvent")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::InsertHistoryEvent));
                REQUIRE(statement != NULL);                   
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::InsertHistoryEvent).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::InsertHistoryEvent).empty() == false);
            }
        }  
        WHEN("InsertParamAttribute")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::InsertParamAttribute));
                REQUIRE(statement != NULL);                   
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::InsertParamAttribute).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::InsertParamAttribute).empty() == false);
            }
        }      
        WHEN("InsertAttributeConf")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::InsertAttributeConf));
                REQUIRE(statement != NULL);                   
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::InsertAttributeConf).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::InsertAttributeConf).empty() == false);
            }
        }   
        WHEN("InsertDomain")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::InsertDomain));
                REQUIRE(statement != NULL);      
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::InsertDomain).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::InsertDomain).empty() == false);
            }
        }     
        WHEN("InsertFamily")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::InsertFamily));
                REQUIRE(statement != NULL);      
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::InsertFamily).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::InsertFamily).empty() == false);
            }
        }     
        WHEN("InsertMember")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::InsertMember));
                REQUIRE(statement != NULL);    
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::InsertMember).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::InsertMember).empty() == false);
            }
        }   
        WHEN("InsertName")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::InsertName));
                REQUIRE(statement != NULL);    
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::InsertName).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::InsertName).empty() == false);
            }
        }    
        WHEN("UpdateTtl")
        {
            THEN("Statement is not null")
            {
                CassStatement *statement = NULL;
                REQUIRE_NOTHROW(statement = cache.statement(Query::UpdateTtl));
                REQUIRE(statement != NULL);    
            }
            THEN("Query is not empty")
            {
                REQUIRE(cache.query_string(Query::UpdateTtl).empty() == false);
            }            
            THEN("Query has debug string name")
            {
                REQUIRE(cache.query_id_to_str(Query::UpdateTtl).empty() == false);
            }
        }                                                                         
    }
}

SCENARIO("Finding the TTL bind position", "[prepared statements]")
{
    // ensure the database connection is up
    db.get();

    GIVEN("A prepared statement cache")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);

        WHEN("Data write type is Read")
        {
            THEN("Bind position is 11")
            {
                REQUIRE(cache.get_insert_attr_ttl_bind_position(Tango::READ) == 11);
            }
        }
        WHEN("Data write type is Read/Write")
        {
            THEN("Bind position is 12")
            {
                REQUIRE(cache.get_insert_attr_ttl_bind_position(Tango::READ_WRITE) == 12);
            }
        }           
    }
}

SCENARIO("Freeing the cache", "[prepared statements]")
{
    // ensure the database connection is up
    db.get();

    GIVEN("A prepared statement cache with some cached query strings and statements")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);
        REQUIRE(cache.statement(Query::GetAttrIdAndTtl) != NULL);
        REQUIRE(cache.statement(Query::UpdateTtl) != NULL);

        WHEN("Cache freed")
        {
            cache.free_cache();

            THEN("Query string and statement cache are zero")
            {
                REQUIRE(cache.query_cache_size() == 0);
                REQUIRE(cache.statement_cache_size() == 0);
            }
        }          
    }
}

SCENARIO("Query string and statement caches grown when InsertAttribute query requests are made", "[prepared statements]")
{
    // ensure the database connection is up
    db.get();

    GIVEN("A query string cache of size zero")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);
        REQUIRE(cache.query_cache_size() == 0);

        WHEN("InsertAttribute prepared statement is requested")
        {
            CassStatement *statement = NULL;
            REQUIRE_NOTHROW(statement = cache.statement(Tango::DEV_BOOLEAN, Tango::SCALAR, Tango::READ));
            REQUIRE(statement != NULL);  

            THEN("Statement cache and query string cache grows by 1")
            {
                REQUIRE(cache.statement_cache_size() == 1);
                REQUIRE(cache.query_cache_size() == 1);
            }
            AND_WHEN("A new InsertAttribute query string is requested")
            {
                REQUIRE(cache.query_string(Tango::DEV_DOUBLE, Tango::SCALAR, Tango::READ).size() != 0);            

                THEN("Statement cache does not grow, but query cache does grow by 1")
                {
                    REQUIRE(cache.statement_cache_size() == 1);
                    REQUIRE(cache.query_cache_size() == 2);
                }
                AND_WHEN("An InsertAttribute statement which has a cached query string is requested")
                {
                    statement = NULL;
                    REQUIRE_NOTHROW(statement = cache.statement(Tango::DEV_DOUBLE, Tango::SCALAR, Tango::READ));
                    REQUIRE(statement != NULL);  

                    THEN("Statement cache grows by 1, but the query string does not")
                    {
                        REQUIRE(cache.statement_cache_size() == 2);
                        REQUIRE(cache.query_cache_size() == 2); 
                    }
                }                                
            }           
        }
    }
}

SCENARIO("Insert Attribute table name is constructed with correct strings when called", "[prepared statements]")
{    
    // ensure the database connection is up
    db.get();

    GIVEN("A prepared statement cache")
    {
        PreparedStatementCache cache(db.get()->session(), KEYSPACE);

        WHEN("InsertAttribute prepared statement is requested with Tango::SCALAR format")
        {
            string str = cache.get_table_name(Tango::DEV_DOUBLE, Tango::SCALAR, Tango::READ);

            THEN("Returned string contains " + TYPE_SCALAR)
            {
                REQUIRE_THAT(str, Contains(TYPE_SCALAR));
            }
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::SPECTRUM format")
        {
            string str = cache.get_table_name(Tango::DEV_DOUBLE, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_ARRAY)
            {
                REQUIRE_THAT(str, Contains(TYPE_ARRAY));
            }            
        }     
        WHEN("InsertAttribute prepared statement is requested with Tango::READ write type")
        {
            string str = cache.get_table_name(Tango::DEV_DOUBLE, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_RO)
            {
                REQUIRE_THAT(str, EndsWith(TYPE_RO));
            }             
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::READ_WRITE write type")
        {
            string str = cache.get_table_name(Tango::DEV_DOUBLE, Tango::SPECTRUM, Tango::READ_WRITE);

            THEN("Returned string contains " + TYPE_RW)
            {
                REQUIRE_THAT(str, EndsWith(TYPE_RW));
            }               
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_BOOLEAN type")
        {
            string str = cache.get_table_name(Tango::DEV_BOOLEAN, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_BOOLEAN)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_BOOLEAN));
            }               
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_UCHAR type")
        {
            string str = cache.get_table_name(Tango::DEV_UCHAR, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_UCHAR)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_UCHAR));
            }               
        }          
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_SHORT type")
        {
            string str = cache.get_table_name(Tango::DEV_SHORT, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_SHORT)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_SHORT));
            }               
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_USHORT type")
        {
            string str = cache.get_table_name(Tango::DEV_USHORT, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_USHORT)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_USHORT));
            }             
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_LONG type")
        {
            string str = cache.get_table_name(Tango::DEV_LONG, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_LONG)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_LONG));
            }             
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_ULONG type")
        {
            string str = cache.get_table_name(Tango::DEV_ULONG, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_ULONG)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_ULONG));
            }                
        }          
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_LONG64 type")
        {
            string str = cache.get_table_name(Tango::DEV_LONG64, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_LONG64)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_LONG64));
            }                
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_ULONG64 type")
        {
            string str = cache.get_table_name(Tango::DEV_ULONG64, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_ULONG64)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_ULONG64));
            }                
        }  
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_FLOAT type")
        {
            string str = cache.get_table_name(Tango::DEV_FLOAT, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_FLOAT)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_FLOAT));
            }                
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_DOUBLE type")
        {
            string str = cache.get_table_name(Tango::DEV_DOUBLE, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_DOUBLE)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_DOUBLE));
            }                
        }          
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_STRING type")
        {
            string str = cache.get_table_name(Tango::DEV_STRING, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_STRING)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_STRING));
            }                
        }
        WHEN("InsertAttribute prepared statement is requested with Tango::DEV_STATE type")
        {
            string str = cache.get_table_name(Tango::DEV_STATE, Tango::SPECTRUM, Tango::READ);

            THEN("Returned string contains " + TYPE_DEV_STATE)
            {
                REQUIRE_THAT(str, Contains(TYPE_DEV_STATE));
            }    
        }                
    }
}