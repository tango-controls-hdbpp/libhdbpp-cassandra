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

#ifndef _HDBPP_DATABASESCHEMALOADER_H
#define _HDBPP_DATABASESCHEMALOADER_H

#include <string>
#include <vector>

class DbCommands
{
public:

    DbCommands();
    virtual ~DbCommands() { }

    bool load_setup_from_file(std::string file);

    const std::vector<std::string>& setup_commands() const { return _setup_commands; } 
    const std::vector<std::string>& teardown_commands() const { return _teardown_commands; } 

    void clear_setup_commands() { _setup_commands.clear(); }
    void clear_teardown_commands() { _teardown_commands.clear(); }

private:

    std::vector<std::string> split(std::string text, char delim);

    std::vector<std::string> _setup_commands;
    std::vector<std::string> _teardown_commands;
};

#endif