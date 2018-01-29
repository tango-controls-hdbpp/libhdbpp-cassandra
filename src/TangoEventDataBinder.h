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

#ifndef _HDBPP_TANGO_EVENT_BINDER_H
#define _HDBPP_TANGO_EVENT_BINDER_H

#include "LibHdb++Defines.h"
#include "Log.h"
#include "tango.h"
#include <cassandra.h>

namespace HDBPP
{
/**
 * @class TangoEventDataBinder
 * @ingroup HDBPP-Implementation
 * @brief Functor to wrap up the extraction then binding of Tango::EventData to statements
 *
 * We extract and wrap this functionality since it is both sufficiently complex and
 * extensive to warrent its own class. The binder is a simple functor that first extracts
 * the event data value, then finds the correct cassandra binding statement via template
 * specialization. This makes the point of use similar to the cassandra bind statements,
 * we simply pass the event data to the functor and allow it to bind it.
 */
class TangoEventDataBinder
{
public:
    /**
     * @brief Construct a TangoEventDataBinder functor object
     *
     * The TangoEventDataBinder is a simple functor and provides a single API call to
     * operator(). This call will bind the value in a Tango::EventData object to a
     * CassStatement object.
     */
    TangoEventDataBinder(){};

    /**
     * @brief TangoEventDataBinder destructor
     */
    ~TangoEventDataBinder(){};

    /**
     * @brief operator() to bind the tango event data to the cassandra statement
     *
     * Carry out the binding of the data in the Tango::EventData object to the
     * given cassandra statement. The operator uses a series of template specializations
     * select the correct type and bind statement for the EventData. This reduces
     * repeated code and maintains the ability to specialize for certain types. We can
     * also easily add more supported types in future.
     *
     * @param statement CassStatement object we are going to bind the tango event data to.
     * @param bind_name The name of the bind point in the CassStatement to bind the data to.
     * @param data_type The type of the tango event data
     * @param write_type The read/write type of the tango event data
     * @param data_format The format, SCALAR/SPECTRUM, of the tango event data
     * @param data The tango event data to be bound
     * @throw Tango::DevFailed For unsupported datatypes and data type mismatches
     */
    void operator()(CassStatement *statement,
                    const std::string &bind_name,
                    int data_type,
                    int write_type,
                    int data_format,
                    Tango::EventData *data);

private:
    enum ExtractType
    {
        ExtractRead,
        ExtractSet
    };

    // entry point into the template functions, we sort parameters based
    // on the write_type parameter
    template <typename T>
    void extract_and_bind_value(CassStatement *statement,
                                const std::string &bind_name,
                                int write_type,
                                int data_format,
                                Tango::EventData *data);

    // second call in the templated functions, here we carry out an extration
    // and bind it to a statement
    template <typename T>
    void do_extract_and_bind_value(CassStatement *statement,
                                   const std::string &bind_name,
                                   int data_format,
                                   Tango::EventData *data,
                                   ExtractType extract_type);

    // extract a value from the event data, it is returned in the vector
    template <typename T>
    bool extract_value(Tango::EventData *data, int data_format, ExtractType extract_type, vector<T> &val);

    // these two methods are specialized for each type to do the binding to the to
    // a cassandra statement. We specialize since each type requires different
    // cassandra calls
    template <typename T>
    void statement_bind(CassStatement *statement, const std::string &bind_name, T val);

    template <typename T> void collection_append(CassCollection *read_values_list, T val);
};

//=============================================================================
//=============================================================================
template <typename T>
void TangoEventDataBinder::extract_and_bind_value(CassStatement *statement,
                                                  const std::string &bind_name,
                                                  int write_type,
                                                  int data_format,
                                                  Tango::EventData *data)
{
    if (write_type != Tango::WRITE)
        do_extract_and_bind_value<T>(statement, bind_name.c_str(), data_format, data,
                                     ExtractType::ExtractRead);

    if (write_type != Tango::READ)
        do_extract_and_bind_value<T>(statement, bind_name.c_str(), data_format, data,
                                     ExtractType::ExtractSet);
}

//=============================================================================
//=============================================================================
template <typename T>
void TangoEventDataBinder::do_extract_and_bind_value(CassStatement *statement,
                                                     const std::string &bind_name,
                                                     int data_format,
                                                     Tango::EventData *data,
                                                     ExtractType extract_type)
{
    vector<T> val;

    if (!extract_value<T>(data, data_format, extract_type, val))
    {
        cass_statement_bind_null_by_name(statement, bind_name.c_str());
        std::stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_TYPE_MISMATCH, error_desc.str(), __func__);
    }

    if (data_format == Tango::SCALAR)
    {
        statement_bind<T>(statement, bind_name.c_str(), val[0]);
    }
    else
    {
        // Store the array into a CQL list
        CassCollection *read_values_list = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());

        for (unsigned int i = 0; i < val.size(); i++)
            collection_append<T>(read_values_list, val[i]);

        cass_statement_bind_collection_by_name(statement, bind_name.c_str(), read_values_list);
        cass_collection_free(read_values_list);
    }
}

//=============================================================================
//=============================================================================
template <typename T>
bool TangoEventDataBinder::extract_value(Tango::EventData *data,
                                         int data_format,
                                         ExtractType extract_type,
                                         vector<T> &val)
{
    (void)data_format; // fix warning
    bool extract_success = false;

    if (extract_type == ExtractType::ExtractRead)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    return extract_success;
}

//=============================================================================
/*
 * This generic version of the function should never be used. All supported
 * types should have been filtered by the specialization and switch in operator()
 */
//=============================================================================
template <typename T>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, T val)
{
    // This should never get called, since we direct all calls in TangoEventDataBinder
    // to a known specialization. Put an exception here just in case, we want to see
    // this error if it does happen

    std::stringstream error_desc;
    error_desc << "Unhandled bind request type, this type (unknown) is not supported" << ends;
    LOG(Error) << error_desc.str() << endl;
    Tango::Except::throw_exception(EXCEPTION_TYPE_UNSUPPORTED_ATTR, error_desc.str(), __func__);
}

//=============================================================================
/*
 * This generic version of the function should never be used. All supported
 * types should have been filtered by the specialization and switch in operator()
 */
//=============================================================================
template <typename T>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, T val)
{
    // This should never get called, since we direct all calls in TangoEventDataBinder
    // to a known specialization. Put an exception here just in case, we want to see
    // this error if it does happen

    std::stringstream error_desc;
    error_desc << "Unhandled collection bind request type, this type (unknown) is not supported" << ends;
    LOG(Error) << error_desc.str() << endl;
    Tango::Except::throw_exception(EXCEPTION_TYPE_UNSUPPORTED_ATTR, error_desc.str(), __func__);
}

//=============================================================================
/*
 * Specialise for the DevState type, since we can not use extract_read to extract
 * its type
 */
//=============================================================================
template <>
bool TangoEventDataBinder::extract_value(Tango::EventData *data,
                                         int data_format,
                                         ExtractType extract_type,
                                         vector<Tango::DevState> &val);

// Export prototypes for all the specialization (this is required to stop the compiler
// exporting these symbols multiple times and causing compile errors).
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, bool val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, bool val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement,
                                          const std::string &bind_name,
                                          unsigned char val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned char val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, short val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, short val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement,
                                          const std::string &bind_name,
                                          unsigned short val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned short val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, int val);

template <> void TangoEventDataBinder::collection_append(CassCollection *read_values_list, int val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement,
                                          const std::string &bind_name,
                                          unsigned int val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned int val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, int64_t val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, int64_t val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, uint64_t val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, uint64_t val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, string val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, string val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement,
                                          const std::string &bind_name,
                                          Tango::DevState val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, Tango::DevState val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, float val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, float val);

template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const std::string &bind_name, double val);

template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, double val);

}; // namespace HDBPP
#endif