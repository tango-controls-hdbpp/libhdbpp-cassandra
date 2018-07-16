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

#include "AttributeCache.h"
#include "catch.hpp"

using namespace std;
using namespace HDBPP;

SCENARIO("Adding to the cache", "[attribute-cache]")
{
    GIVEN("AttributeCache with no cached items")
    {
        AttributeCache cache;

        CassUuid uuid;
        CassUuidGen *uuid_gen = cass_uuid_gen_new();
        cass_uuid_gen_time(uuid_gen, &uuid);        
        cass_uuid_gen_free(uuid_gen);

        WHEN("An attribute name and details are cached")
        {
            string fqdn_attr_name ="tango://server.domain.world:10000/test/test/1/attribute_name";
            AttributeName attribute_name(fqdn_attr_name);

            THEN("No exception is raised")
            {
                REQUIRE_NOTHROW(cache.cache_attribute(attribute_name, uuid, 0));
            }
            AND_WHEN("Same attribute is cached again")
            {
                // add back to the cache for the text
                REQUIRE_NOTHROW(cache.cache_attribute(attribute_name, uuid, 0));

                THEN("An exception is raised")
                {
                    REQUIRE_THROWS(cache.cache_attribute(attribute_name, uuid, 0));
                }
            }
        }        
        WHEN("An attribute name and details are cached")
        {
            string fqdn_attr_name1 ="tango://server.domain.world:10000/test/test/1/attribute_name1";
            AttributeName attribute_name1(fqdn_attr_name1);
            string fqdn_attr_name2 ="tango://server.domain.world:10000/test/test/1/attribute_name2";
            AttributeName attribute_name2(fqdn_attr_name2);

            REQUIRE_NOTHROW(cache.cache_attribute(attribute_name1, uuid, 0));

            THEN("Cache size increases to 1")
            {
                REQUIRE(cache.cache_size() == 1);
            }
            AND_WHEN("Another attribute name is cached")
            {
                REQUIRE_NOTHROW(cache.cache_attribute(attribute_name2, uuid, 0));

                THEN("Cache size increases to 2")
                {
                    REQUIRE(cache.cache_size() == 2);
                }
                AND_WHEN("The same attribute is added again")
                {
                    REQUIRE_THROWS(cache.cache_attribute(attribute_name2, uuid, 0));

                    THEN("Cache size does not increases")
                    {
                        REQUIRE(cache.cache_size() == 2);
                    }
                }                
            }
        }
        WHEN("An attribute name and details are cached")
        {
            string fqdn_attr_name ="tango://server.domain.world:10000/test/test/1/attribute_name";
            AttributeName attribute_name(fqdn_attr_name);

            REQUIRE_NOTHROW(cache.cache_attribute(attribute_name, uuid, 0));

            THEN("Checking if that attribute name shows it is cached ")
            {
                REQUIRE(cache.cached(attribute_name) == true);
            }
        }        
    }
}

SCENARIO("Retrieving data from the cache", "[attribute-cache]")
{
    GIVEN("AttributeCache with a cached attribute name")
    {
        AttributeCache cache;

        string fqdn_attr_name ="tango://server.domain.world:10000/test/test/1/attribute_name";
        AttributeName attribute_name(fqdn_attr_name);

        CassUuid uuid;
        CassUuidGen *uuid_gen = cass_uuid_gen_new();
        cass_uuid_gen_time(uuid_gen, &uuid);        
        cass_uuid_gen_free(uuid_gen);

        REQUIRE_NOTHROW(cache.cache_attribute(attribute_name, uuid, 10));

        WHEN("Retrieving the attribute names tll value")
        {
            unsigned int ttl;
            REQUIRE_NOTHROW(ttl = cache.find_attr_ttl(attribute_name));

            THEN("Expect it to be the same it was cached with")
            {
                REQUIRE(ttl == 10);
            }
        }
        WHEN("Retrieving the attribute names uuid value")
        {
            CassUuid uuid_returned;
            REQUIRE_NOTHROW(uuid_returned = cache.find_attr_uuid(attribute_name));

            THEN("Expect it to be the same it was cached with")
            {
                char str1[CASS_UUID_STRING_LENGTH];
                cass_uuid_string(uuid, str1);

                char str2[CASS_UUID_STRING_LENGTH];
                cass_uuid_string(uuid_returned, str2);

                REQUIRE(string(str1) == string(str2));
            }
        }        
    }
}

SCENARIO("Updating a cache entry", "[attribute-cache]")
{
    GIVEN("AttributeCache with a cached attribute name")
    {
        AttributeCache cache;

        string fqdn_attr_name ="tango://server.domain.world:10000/test/test/1/attribute_name";
        AttributeName attribute_name(fqdn_attr_name);

        CassUuid uuid;
        CassUuidGen *uuid_gen = cass_uuid_gen_new();
        cass_uuid_gen_time(uuid_gen, &uuid);        
        cass_uuid_gen_free(uuid_gen);

        REQUIRE_NOTHROW(cache.cache_attribute(attribute_name, uuid, 10));

        WHEN("Changing the ttl")
        {
            REQUIRE_NOTHROW(cache.update_attr_ttl(attribute_name, 20));

            THEN("Expect the retrieved ttl value to be the updated value")
            {
                REQUIRE(cache.find_attr_ttl(attribute_name) == 20);
            }
        }      
    }
}

SCENARIO("Requesting multiple times", "[attribute-cache]")
{
    GIVEN("AttributeCache with three cached attribute names")
    {
        AttributeCache cache;

        string fqdn_attr_name1 ="tango://server.domain.world:10000/test/test/1/attribute_name1";
        AttributeName attribute_name1(fqdn_attr_name1);

        CassUuid uuid1;
        CassUuidGen *uuid_gen = cass_uuid_gen_new();
        cass_uuid_gen_time(uuid_gen, &uuid1);        

        string fqdn_attr_name2 ="tango://server.domain.world:10000/test/test/1/attribute_name2";
        AttributeName attribute_name2(fqdn_attr_name2);

        CassUuid uuid2;
        cass_uuid_gen_time(uuid_gen, &uuid2);

        string fqdn_attr_name3 ="tango://server.domain.world:10000/test/test/1/attribute_name3";
        AttributeName attribute_name3(fqdn_attr_name3);

        CassUuid uuid3;
        cass_uuid_gen_time(uuid_gen, &uuid3);   

        cass_uuid_gen_free(uuid_gen);                

        REQUIRE_NOTHROW(cache.cache_attribute(attribute_name1, uuid1, 10));
        REQUIRE_NOTHROW(cache.cache_attribute(attribute_name2, uuid2, 20));
        REQUIRE_NOTHROW(cache.cache_attribute(attribute_name3, uuid3, 30));                

        WHEN("Requesting attribute 1")
        {
            unsigned int ttl;
            REQUIRE_NOTHROW(ttl = cache.find_attr_ttl(attribute_name1));

            THEN("Expect the retrieved ttl value to be same as the original")
            {
                REQUIRE(ttl == 10);
            }
            AND_WHEN("Requesting the same attribute again")
            {
                REQUIRE_NOTHROW(ttl = cache.find_attr_ttl(attribute_name1));

                THEN("Expect the retrieved ttl value to be same as the original")
                {
                    REQUIRE(ttl == 10);
                }
            }
        } 
        WHEN("Requesting attribute 2")
        {
            unsigned int ttl;
            REQUIRE_NOTHROW(ttl = cache.find_attr_ttl(attribute_name2));

            THEN("Expect the retrieved ttl value to be same as the original")
            {
                REQUIRE(ttl == 20);
            }
            AND_WHEN("Requesting the same attribute again")
            {
                REQUIRE_NOTHROW(ttl = cache.find_attr_ttl(attribute_name2));

                THEN("Expect the retrieved ttl value to be same as the original")
                {
                    REQUIRE(ttl == 20);
                }
            }
        }
        WHEN("Requesting attribute 3")
        {
            unsigned int ttl;
            REQUIRE_NOTHROW(ttl = cache.find_attr_ttl(attribute_name3));

            THEN("Expect the retrieved ttl value to be same as the original")
            {
                REQUIRE(ttl == 30);
            }
            AND_WHEN("Requesting the same attribute again")
            {
                REQUIRE_NOTHROW(ttl = cache.find_attr_ttl(attribute_name3));

                THEN("Expect the retrieved ttl value to be same as the original")
                {
                    REQUIRE(ttl == 30);
                }
            }
        }        
    }
}