// Copyright (C) : 2014-2017
// European Synchrotron Radiation Facility
// BP 220, Grenoble 38043, FRANCE
//
// This file is part of TextToSpeech
//
// TextToSpeech is free software: you can redistribute it and/or modify
// it under the terms of the Lesser GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// TextToSpeech is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Lesser
// GNU General Public License for more details.
//
// You should have received a copy of the Lesser GNU General Public License
// along with TextToSpeech.  If not, see <http://www.gnu.org/licenses/>.

#ifndef _LOG_H
#define _LOG_H

#include <cmath>
#include <functional>
#include <iomanip>
#include <sstream>
#include <string>
#include <thread>
#include <vector>
#include <iostream>

#ifdef TRACE_ENABLED
    #include <cstring>
#endif

namespace Utils
{
/**
 * @class QuickMsTimer
 * @brief Really simple and quick timer for debug and development
 */
class QuickMsTimer
{
private:
    typedef std::chrono::high_resolution_clock high_resolution_clock;
    typedef std::chrono::duration<double, std::milli> milliseconds;

public:
    /**
     * @brief Timer is started when constructed if start_timing is true
     */
    QuickMsTimer(bool start_timing = true)
    {
        if (start_timing)
            start();
    }

    /**
     * @brief Start an idle timer, will reset the start time
     */
    void start()
    {
        _start = high_resolution_clock::now();
        _running = true;
    }

    /**
     * @brief Reset the start time of a running timer
     */
    void reset()
    {
        if (_running)
            _start = high_resolution_clock::now();
    }

    /**
     * @brief Stop the timer and collect the end time
     */
    void stop()
    {
        _end = high_resolution_clock::now();
        _running = false;
    }

    /**
     * @brief Grab the elapsed time in milliseconds
     */
    milliseconds elapsed() const
    {
        if (_running)
            return std::chrono::duration_cast<milliseconds>(high_resolution_clock::now() - _start);

        return std::chrono::duration_cast<milliseconds>(_end - _start);
    }

    /**
     * @brief Output operator (Made friend to save adding cpp file)
     */
    friend std::ostream &operator<<(std::ostream &os, const QuickMsTimer &timer)
    {
        os << timer.elapsed().count();
        return os;
    }

private:
    bool _running;
    high_resolution_clock::time_point _start;
    high_resolution_clock::time_point _end;
};

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
    Info = 3, /// Only log messages sent at the Info level and above
    Debug = 4, /// Only log messages sent at the Debug level and above
    Trace = 5 /// Trace is used for the trace macro to trace function call
};

    // Use this nested namespace to stop Log being used by mistake
    namespace LoggerClass
    {
        /**
         * @class Log
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
            Log(LoggingLevel level, bool allow_redirect = true)
                : _at_level(level), _allow_redirect(allow_redirect)
            {
            }

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
                static std::vector<std::string> as_strings = {"Disabled", "Error", "Warning", "Info", "Debug", "Trace"};
                return as_strings[static_cast<int>(level)];
            }

            /**
             * @brief Set a callback to send debugging information to an additional source.
            *
            */
            static std::function<void(LoggingLevel, const std::string &)> &LoggingRedirectFunct()
            {
                static std::function<void(LoggingLevel, const std::string &)> redirect;
                return redirect;
            }

            /*static std::ostream &LoggingStream()
            {
                static std::ostream
                return redirect;
            }*/

        protected:
            // flush() actually does the logging. It prefixes some basic time stamp information
            // to the line, just down to the seconds for now.
            virtual void flush()
            {
                if (_allow_redirect && Log::LoggingRedirectFunct())
                {
                    // if a callback has been set, then send the logging to this
                    Log::LoggingRedirectFunct()(_at_level, _str_stream.str());
                }
                else
                {
                    // generate a timestamp (found this code on stackexchange!)
                    auto now = std::chrono::system_clock::now();
                    auto now_time_t = std::chrono::system_clock::to_time_t(now);
                    auto now_tm = std::localtime(&now_time_t);

                    auto tse = now.time_since_epoch();
                    auto milliseconds =
                        std::chrono::duration_cast<std::chrono::milliseconds>(tse).count() % 1000;

                    // prefix it to our message, we could add any level of detail here
                    // and push the message to multiple sinks
                    std::cout << now_tm->tm_hour << ":" << now_tm->tm_min << ":" << now_tm->tm_sec
                            << ":" << std::setfill('0') << std::setw(3) << milliseconds << " "
                            << Log::ToString(_at_level) << " " << _str_stream.str();
                }

                _str_stream.clear();
            }

        private:
            Log(const Log &) {}
            Log &operator=(const Log &) { return *this; }

            std::stringstream _str_stream;
            LoggingLevel _at_level;
            bool _allow_redirect;
        };

        /**
         * @class TraceLogger
         * @brief Function Enter/Exit logger
         *
         * Using its constructor and destructor, this class tracks function enter
         * and exit points. Places a burden on the load and so should only be used
         * for debug and development.
         */
        class TraceLogger
        {
        public:
            /**
             * @brief Construct an TraceLogger object
             *
             * The trace logger is like a functor, in the constructor we simply record the
             * message and time, and print the enter message
             * @param msg String to use as function record.
            */
            TraceLogger(std::string msg) : _msg(msg)
            {
                _timer.start();
                Log(Trace, false) << "(" << _msg << ") ENTER" << std::endl;
            }

            /**
             * @brief Destroy a TraceLogger object and log the event.
             *
             * Calculates duration between construction and now as part of the message.
            */
            ~TraceLogger()
            {
                // stop the redirect, tracing is always to console
                Log(Trace, false) << "(" << _msg << ") EXIT after " << std::fixed
                                  << std::setprecision(2) << _timer << "ms" << std::endl;
            }

        private:
            QuickMsTimer _timer;
            std::string _msg;
        };
    } // namespace LoggerClass
} // namespace Utils

// Define this outside of the namespace, so we can get the correct resolution on the
// expanded macros whether we are in a header or cpp file.
#ifdef DEBUG_ENABLED

    // There are the main logging macros, we use a macro like this to allow the level,
    // line and function to be prefixed to each line of debug. Also filters out
    // unnecessary logging level calls. Two versions, the base version avoids the level
    // check and allows the redirect to be overriden, the normal is for in code use
    // since it checks the debug level
    #define LOG_BASE(level, redirect) Utils::LoggerClass::Log(level, redirect)

    #define LOG(level)                                                                                 \
        if (level <= Utils::LoggerClass::Log::LogLevel())                                                     \
            Utils::LoggerClass::Log(level) << "(" << __func__ << ":" << __LINE__ << ") "
#else
    #define LOG_BASE(level, redirect)
    #define LOG(level)
#endif

#ifdef TRACE_ENABLED

    // Macro used so we can inset file/func/line etc
    #define TRACE_LOGGER                                                                               \
        Utils::LoggerClass::TraceLogger _t_r_a_c_e_r_(std::string(std::strrchr("/" __FILE__, '/') + 1) +      \
            ":" + std::string(__func__) + ":" + std::to_string(__LINE__))

#else
    #define TRACE_LOGGER
#endif

#endif // _LOG_H