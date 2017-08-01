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
#include "catch.hpp"

using namespace std;

const string CONTACT_POINT = "acu-cassandra";

TEST_CASE("CassandraConnection connectivity", "[test connection]")
{
    CassandraConnection connection;

    SECTION("Can connect to cassandra test cluster")
    {
        REQUIRE(connection.connect(CONTACT_POINT) == true);

        SECTION("Can disconnect from a valid connection")
        {
            REQUIRE(connection.disconnect() == true);
            REQUIRE(connection.session() == NULL);
        }
    }
}