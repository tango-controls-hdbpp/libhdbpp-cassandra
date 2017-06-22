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

#ifndef _HDBPP_LOG_H
#define _HDBPP_LOG_H

#include <chrono>
#include <iostream>
#include <sstream>
#include <string>
#include <vector>

namespace HDBPP
{
// available log levels, the values are important, since they will be used
// to quick deference a vector inside the logging class to find the string
// value of each level
const int Disabled = 0;
const int Error = 1;
const int Warning = 2;
const int Debug = 3;
const int Trace = 4;

class Log
{
public:
    typedef std::ostream &(*ManipFn)(std::ostream &);
    typedef std::ios_base &(*FlagsFn)(std::ios_base &);

    Log(int level) : _at_level(level) {}
    virtual ~Log() {}

    // endl, flush, setw, setfill, etc.
    Log &operator<<(ManipFn manip)
    {
        manip(_str_stream);

        if (manip == static_cast<ManipFn>(std::flush) || manip == static_cast<ManipFn>(std::endl))
            flush();

        return *this;
    }

    // setiosflags, resetiosflags
    Log &operator<<(FlagsFn manip)
    {
        manip(_str_stream);
        return *this;
    }

    template <typename T> Log &operator<<(const T &item)
    {
        _str_stream << item;
        return *this;
    }

    static int &LogLevel()
    {
        // default state for the logger is disabled, the user must enable debugging
        static int level = Disabled;
        return level;
    }

    static std::string ToString(int level)
    {
        // this vector of strings must match the const ints above
        static std::vector<std::string> as_strings = {"Disabled", "Error", "Warning", "Debug", "Trace"};
        return as_strings[Log::LogLevel()];
    }

private:
    void flush()
    {
        // generate a timestamp (found this code on stackexchange!)
        auto now = std::chrono::system_clock::now();
        auto now_time_t = std::chrono::system_clock::to_time_t(now);
        auto now_tm = std::localtime(&now_time_t);

        // prefix it to our message, we could add any level of detail here
        // and push the message to multiple sinks
        std::cout << "(" << now_tm->tm_hour << ":" << now_tm->tm_min << ":" << now_tm->tm_sec
                  << ") " << Log::ToString(_at_level) << " " << _str_stream.str();

        _str_stream.clear();
    }

    Log(const Log &) {}
    Log &operator=(const Log &) { return *this; }

    std::stringstream _str_stream;
    int _at_level;
};

// This is the main logging macro, we use a macro like this to allow the level,
// line and function to be prefixed to each line of debug
#define LOG(level)                                                                                 \
    if (level <= Log::LogLevel())                                                                  \
    Log(level) << "(" << __func__ << ":" << __LINE__ << ") "

// To trace function calls, enable this define, this will produce a lot of
// additional debug
//#define TRACE_FUNCTIONS

#ifdef TRACE_FUNCTIONS
#define TRACE_ENTER                                                                                \
    Log(Trace) << "(" < < < __func__ << ":" << __LINE__ << ") "                                    \
                                     << "Enter" << endl
#define TRACE_EXIT                                                                                 \
    Log(Trace) << "(" << __func__ << ":" << __LINE__ << ") "                                       \
               << "Exit" << endl
#else
#define TRACE_ENTER
#define TRACE_EXIT
#endif

}; // namespace HDBPP
#endif
