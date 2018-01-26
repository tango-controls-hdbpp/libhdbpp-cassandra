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

            bool result;
            REQUIRE((result = cache.cache_attribute(attribute_name, uuid, 0)) == true);

            THEN("Return is true")
            {
                REQUIRE(result == true);
            }
            AND_WHEN("Same attribute is cached again")
            {
                THEN("Return is false")
                {
                    REQUIRE(cache.cache_attribute(attribute_name, uuid, 0) == false);
                }
            }
        }        
        WHEN("An attribute name and details are cached")
        {
            string fqdn_attr_name1 ="tango://server.domain.world:10000/test/test/1/attribute_name1";
            AttributeName attribute_name1(fqdn_attr_name1);
            string fqdn_attr_name2 ="tango://server.domain.world:10000/test/test/1/attribute_name2";
            AttributeName attribute_name2(fqdn_attr_name2);

            REQUIRE(cache.cache_attribute(attribute_name1, uuid, 0) == true);

            THEN("Cache size increases to 1")
            {
                REQUIRE(cache.cache_size() == 1);
            }
            AND_WHEN("Another attribute name is cached")
            {
                REQUIRE(cache.cache_attribute(attribute_name2, uuid, 0) == true);

                THEN("Cache size increases to 2")
                {
                    REQUIRE(cache.cache_size() == 2);
                }
                AND_WHEN("The same attribute is added again")
                {
                    REQUIRE(cache.cache_attribute(attribute_name2, uuid, 0) == false);

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

            REQUIRE(cache.cache_attribute(attribute_name, uuid, 0) == true);

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

        REQUIRE(cache.cache_attribute(attribute_name, uuid, 10) == true);

        WHEN("Retrieving the attribute names tll value")
        {
            unsigned int ttl;
            REQUIRE(cache.find_attr_ttl(attribute_name, ttl) == true);

            THEN("Expect it to be the same it was cached with")
            {
                REQUIRE(ttl == 10);
            }
        }
        WHEN("Retrieving the attribute names uuid value")
        {
            CassUuid uuid_returned;
            REQUIRE(cache.find_attr_uuid(attribute_name, uuid_returned) == true);

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

        REQUIRE(cache.cache_attribute(attribute_name, uuid, 10) == true);

        WHEN("Changing the ttl")
        {
            REQUIRE(cache.update_attr_ttl(attribute_name, 20) == true);

            THEN("Expect the retrieved ttl value to be the updated value")
            {
                unsigned int ttl;
                REQUIRE(cache.find_attr_ttl(attribute_name, ttl) == true);
                REQUIRE(ttl == 20);
            }
        }      
    }
}

