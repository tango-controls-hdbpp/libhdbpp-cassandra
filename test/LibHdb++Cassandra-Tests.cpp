/* Copyright (C) : 2014-2017
   European Synchrotron Radiation tango_host
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

#include "AttributeName.h"

// Request catch provide a main for us
#define CATCH_CONFIG_MAIN
#include "catch.hpp"
#include <netdb.h> //for getaddrinfo

using namespace std;
using namespace HDBPP;

SCENARIO("AttributeName supports fully qualified attribute name")
{
    GIVEN("Attribute name: tango://acudebian7.esrf.fr:10000/test/universal/1/devshortrw")
    {
        string fqdn_attr_name =
            "tango://acudebian7.esrf.fr:10000/test/universal/1/devshortrw";

        AttributeName attribute_name(fqdn_attr_name);

        WHEN("fully_qualified_attribute_name() requested")
        {
            THEN("Value is tango://acudebian7.esrf.fr:10000/test/universal/1/devshortrw")
            {
                REQUIRE(attribute_name.fully_qualified_attribute_name() == "tango://acudebian7.esrf.fr:10000/test/universal/1/devshortrw");
            }
        }
        WHEN("full_attribute_name() requested")
        {
            THEN("Value is test/universal/1/devshortrw")
            {
                REQUIRE(attribute_name.full_attribute_name() == "test/universal/1/devshortrw");
            }
        }
        WHEN("tango_host() requested")
        {
            THEN("Value is acudebian7.esrf.fr:10000")
            {
                REQUIRE(attribute_name.tango_host() == "acudebian7.esrf.fr:10000");
            }            
        }
        WHEN("tango_host_with_domain() requested")
        {
            THEN("Value is acudebian7.esrf.fr:10000")
            {
                REQUIRE(attribute_name.tango_host_with_domain() == "acudebian7.esrf.fr:10000");
            }            
        }
        WHEN("Validate the domain/family/member/name")
        {
            THEN("domain/family/member/name is valid")
            {
                REQUIRE(attribute_name.validate_domain_family_member_name() == AttributeName::NoError);
            }            
        }
        WHEN("domain() requested")
        {
            THEN("Value is test")
            {
                REQUIRE(attribute_name.domain() == "test");
            }
        }
        WHEN("family() requested")
        {
            THEN("Value is universal")
            {
                REQUIRE(attribute_name.family() == "universal");
            }
        }    
        WHEN("member() requested")
        {
            THEN("Value is 1")
            {
                REQUIRE(attribute_name.member() == "1");
            }
        }
        WHEN("name() requested")
        {
            THEN("Value is devshortrw")
            {
                REQUIRE(attribute_name.name() == "devshortrw");
            }
        }              
    }
}

SCENARIO("AttributeName supports fully qualified attribute name missing tango prefix")
{
    GIVEN("Attribute name: acudebian7.esrf.fr:10000/test/universal/1/devshortrw")
    {    
        string fqdn_attr_name =
            "acudebian7.esrf.fr:10000/test/universal/1/devshortrw";

        AttributeName attribute_name(fqdn_attr_name);

        WHEN("fully_qualified_attribute_name() requested")
        {
            THEN("Value is acudebian7.esrf.fr:10000/test/universal/1/devshortrw")
            {
                REQUIRE(attribute_name.fully_qualified_attribute_name() == "acudebian7.esrf.fr:10000/test/universal/1/devshortrw");
            }
        }
        WHEN("full_attribute_name() requested")
        {
            THEN("Value is test/universal/1/devshortrw")
            {
                REQUIRE(attribute_name.full_attribute_name() == "test/universal/1/devshortrw");
            }
        }
        WHEN("tango_host() requested")
        {
            THEN("Value is acudebian7.esrf.fr:10000")
            {
                REQUIRE(attribute_name.tango_host() == "acudebian7.esrf.fr:10000");
            }            
        }
        WHEN("tango_host_with_domain() requested")
        {
            THEN("Value is acudebian7.esrf.fr:10000")
            {
                REQUIRE(attribute_name.tango_host_with_domain() == "acudebian7.esrf.fr:10000");
            }            
        }
        WHEN("Validate the domain/family/member/name")
        {
            THEN("domain/family/member/name is valid")
            {
                REQUIRE(attribute_name.validate_domain_family_member_name() == AttributeName::NoError);
            }            
        }
        WHEN("domain() requested")
        {
            THEN("Value is test")
            {
                REQUIRE(attribute_name.domain() == "test");
            }
        }
        WHEN("family() requested")
        {
            THEN("Value is universal")
            {
                REQUIRE(attribute_name.family() == "universal");
            }
        }    
        WHEN("member() requested")
        {
            THEN("Value is 1")
            {
                REQUIRE(attribute_name.member() == "1");
            }
        }
        WHEN("name() requested")
        {
            THEN("Value is devshortrw")
            {
                REQUIRE(attribute_name.name() == "devshortrw");
            }
        }  
    } 
}

SCENARIO("AttributeName supports fully qualified attribute name but no network domain")
{
    GIVEN("Attribute name: acudebian7:10000/test/universal/1/devshortrw")
    {    
        string fqdn_attr_name =
            "acudebian7:10000/test/universal/1/devshortrw";

        AttributeName attribute_name(fqdn_attr_name);

        WHEN("fully_qualified_attribute_name() requested")
        {
            THEN("Value is acudebian7:10000/test/universal/1/devshortrw")
            {
                REQUIRE(attribute_name.fully_qualified_attribute_name() == "acudebian7:10000/test/universal/1/devshortrw");
            }
        }
        WHEN("full_attribute_name() requested")
        {
            THEN("Value is test/universal/1/devshortrw")
            {
                REQUIRE(attribute_name.full_attribute_name() == "test/universal/1/devshortrw");
            }
        }
        WHEN("tango_host() requested")
        {
            THEN("Value is acudebian7:10000")
            {
                REQUIRE(attribute_name.tango_host() == "acudebian7:10000");
            }            
        }
    } 
}