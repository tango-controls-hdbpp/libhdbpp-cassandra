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

#include "Log.h"
#include "tango.h"
#include <cassandra.h>

namespace HDBPP
{
class TangoEventDataBinder
{
public:
    TangoEventDataBinder(){};
    ~TangoEventDataBinder(){};

    void operator()(CassStatement *statement,
                    const string &bind_name,
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

    template <typename T>
    void extract_and_bind_value(CassStatement *statement,
                                const string &bind_name,
                                int write_type,
                                int data_format,
                                Tango::EventData *data);

    template <typename T>
    void do_extract_and_bind_value(CassStatement *statement,
                                   const string &bind_name,
                                   int data_format,
                                   Tango::EventData *data,
                                   ExtractType extract_type);

    template <typename T>
    bool extract_value(Tango::EventData *data, int data_format, ExtractType extract_type, vector<T> &val);

    template <typename T>
    void statement_bind(CassStatement *statement, const string &bind_name, T val);

    template <typename T> void collection_append(CassCollection *read_values_list, T val);
};

//=============================================================================
//=============================================================================
template <typename T>
void TangoEventDataBinder::extract_and_bind_value(CassStatement *statement,
                                                  const string &bind_name,
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
                                                     const string &bind_name,
                                                     int data_format,
                                                     Tango::EventData *data,
                                                     ExtractType extract_type)
{
    vector<T> val;

    if (!extract_value<T>(data, data_format, extract_type, val))
    {
        cass_statement_bind_null_by_name(statement, bind_name.c_str());
        stringstream error_desc;

        error_desc << "Failed to extract the attribute " << data->attr_name
                   << " from the Tango EventData. Possible type mismatch?" << ends;

        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception("TODO", error_desc.str(), __func__);
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
    bool extract_success = false;

    if (extract_type == ExtractType::ExtractRead)
        extract_success = data->attr_value->extract_read(val);
    else
        extract_success = data->attr_value->extract_set(val);

    return extract_success;
}

//=============================================================================
//=============================================================================
template <typename T>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, T val)
{
    // This should never get called, since we direct all calls in TangoEventDataBinder
    // to a known specialization. Put an exception here just in case, we want to see
    // this error if it does happen

    stringstream error_desc;
    error_desc << "Unhandled bind request type, this type (unknown) is not supported" << ends;
    LOG(Error) << error_desc.str() << endl;
    Tango::Except::throw_exception("TODO", error_desc.str(), __func__);
}

//=============================================================================
//=============================================================================
template <typename T>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, T val)
{
    // This should never get called, since we direct all calls in TangoEventDataBinder
    // to a known specialization. Put an exception here just in case, we want to see
    // this error if it does happen

    stringstream error_desc;
    error_desc << "Unhandled collection bind request type, this type (unknown) is not supported" << ends;
    LOG(Error) << error_desc.str() << endl;
    Tango::Except::throw_exception("TODO", error_desc.str(), __func__);
}

//=============================================================================
//=============================================================================
template <>
bool TangoEventDataBinder::extract_value(Tango::EventData *data,
                                         int data_format,
                                         ExtractType extract_type,
                                         vector<Tango::DevState> &val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, bool val);

//=============================================================================
//=============================================================================
template <> void TangoEventDataBinder::collection_append(CassCollection *read_values_list, bool val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, unsigned char val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned char val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, short val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, short val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, unsigned short val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned short val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, int val);

//=============================================================================
//=============================================================================
template <> void TangoEventDataBinder::collection_append(CassCollection *read_values_list, int val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, unsigned int val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned int val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, int64_t val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, int64_t val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, uint64_t val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, uint64_t val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, string val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, string val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, Tango::DevState val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, Tango::DevState val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, float val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, float val);
//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, double val);

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, double val);

}; // namespace HDBPP
#endif