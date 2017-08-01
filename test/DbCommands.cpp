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

#include "DbCommands.h"
#include <cassert>
#include <fstream>
#include <iostream>

using namespace std;

//=============================================================================
//=============================================================================
DbCommands::DbCommands() 
{
    // single command to clean out the database
    _teardown_commands.push_back("drop keyspace if exists hdb;");
}

//=============================================================================
//=============================================================================
bool DbCommands::load_setup_from_file(std::string file)
{
    assert(file.empty() == false);

    // load the file
    ifstream infile { file };
    string hdb_schema { istreambuf_iterator<char>(infile), istreambuf_iterator<char>() };

    if (hdb_schema.empty())
        return false;

    // now blow this up into commands
    _setup_commands = split(hdb_schema, ';');

    return true;
}

//=============================================================================
//=============================================================================
vector<string> DbCommands::split(string text, char delim)
{
    vector<string> tokens;
    size_t start = 0, end = 0;

    while ((end = text.find(delim, start)) != string::npos) 
    {
        if (end != start) 
            tokens.push_back(text.substr(start, end - start + 1));

        start = end + 1;
    }

    return tokens;
}