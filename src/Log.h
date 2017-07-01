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
#include <iomanip>
#include <sstream>
#include <string>
#include <vector>

namespace HDBPP
{
/** 
 * @enum LoggingLevel
 * \brief Possible logging levels for the Log class
 *
 * Available log levels, the values are important, since they will be used
 * to quick deference a vector inside the logging class to find the string
 * value of each level
 */
enum LoggingLevel 
{ 
    Disabled = 0, /// Disable all logging
    Error = 1, /// Only log messages sent at the Error level
    Warning = 2, /// Only log messages sent at the Error and Warning level
    Debug = 3, /// Only log messages sent at the Debug level and above
    Trace = 4 /// Trace is used for the trace macro to trace function call
 };

/**
 * @class Log
 * @ingroup HDBPP-Implementation
 * @brief Simple logger to emulate stream based logging.
 *
 * This simple logger both provides a clean stream like logging interface and
 * future proofs changes to the logging system to changes. The logging can be
 * redirected from this class to, for example, a file, another stream or even
 * the tango logging system.
 */
class Log
{
public:
    typedef std::ostream &(*ManipFn)(std::ostream &);
    typedef std::ios_base &(*FlagsFn)(std::ios_base &);

    /**
     * @brief Construct an Log object
     *
     * The logger is a functor, and its logging is carried out in a private class
     * after its stream has been filled with string data
     * @param level Intended logging level
    */
    Log(LoggingLevel level) : _at_level(level) {}

    /**
     * @brief Destroy a Log object
     *
     * The logger is a functor, and its logging is carried out in a private class
     * after its stream has been filled with string data
    */
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

    /**
     * @brief Streaming operator
     *
     * The item is streamed into the internal string stream to be flushed when we
     * hit an endl.
     *
     * @param item Item to be output
    */
    template <typename T> Log &operator<<(const T &item)
    {
        _str_stream << item;
        return *this;
    }

    /**
     * @brief Return a reference to a static log level, allowing the level to be set
    */
    static LoggingLevel &LogLevel()
    {
        // default state for the logger is disabled, the user must enable debugging
        static LoggingLevel level = Disabled;
        return level;
    }

    /**
     * @brief Convert log level to string via internal static variables
    */
    static std::string ToString(LoggingLevel level)
    {
        // this vector of strings must match the const ints above
        static std::vector<std::string> as_strings = {"Disabled", "Error", "Warning", "Debug", "Trace"};
        return as_strings[static_cast<int>(Log::LogLevel())];
    }

private:

    // flush() actually does the logging. It prefixes some basic time stamp information
    // to the line, just down to the seconds for now.
    void flush()
    {
        // generate a timestamp (found this code on stackexchange!)
        auto now = std::chrono::system_clock::now();
        auto now_time_t = std::chrono::system_clock::to_time_t(now);
        auto now_tm = std::localtime(&now_time_t);

        auto tse = now.time_since_epoch();
        auto milliseconds = std::chrono::duration_cast<std::chrono::milliseconds>(tse).count() % 1000;        

        // prefix it to our message, we could add any level of detail here
        // and push the message to multiple sinks
        std::cout << now_tm->tm_hour << ":" 
                  << now_tm->tm_min << ":" 
                  << now_tm->tm_sec << ":"
                  << std::setfill('0') << std::setw(3) << milliseconds << " "
                  << Log::ToString(_at_level) << " " << _str_stream.str();

        _str_stream.clear();
    }

    Log(const Log &) {}
    Log &operator=(const Log &) { return *this; }

    std::stringstream _str_stream;
    LoggingLevel _at_level;
};

// This is the main logging macro, we use a macro like this to allow the level,
// line and function to be prefixed to each line of debug. Also filters out 
// unnecessary logging level calls.
#define LOG(level)                                                                                 \
    if (level <= Log::LogLevel())                                                                  \
        Log(level) << "(" << __func__ << ":" << __LINE__ << ") "

// To trace function calls, enable this define, this will produce a lot of
// additional debug and should be kept turned off by default
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
