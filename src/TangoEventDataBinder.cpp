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

#include "TangoEventDataBinder.h"

#include <cassandra.h>
#include <cmath>
#include <iostream>

using namespace std;
using namespace Utils;

namespace HDBPP
{
//=============================================================================
//=============================================================================
void TangoEventDataBinder::operator()(CassStatement *statement,
                                      const string &bind_name,
                                      int data_type,
                                      int write_type,
                                      int data_format,
                                      Tango::EventData *data)
{
    TRACE_LOGGER;

    // after selecting the correct type, the next functions start the template selection
    // and specialization to find the correct cassandra bind function
    switch (data_type)
    {
        case Tango::DEV_BOOLEAN:
            extract_and_bind_value<bool>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_UCHAR:
            extract_and_bind_value<unsigned char>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_SHORT:
            extract_and_bind_value<short>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_USHORT:
            extract_and_bind_value<unsigned short>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_LONG:
            extract_and_bind_value<int>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_ULONG:
            extract_and_bind_value<unsigned int>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_LONG64:
            extract_and_bind_value<int64_t>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_ULONG64:
            extract_and_bind_value<uint64_t>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_FLOAT:
            extract_and_bind_value<float>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_DOUBLE:
            extract_and_bind_value<double>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_STRING:
            extract_and_bind_value<string>(statement, bind_name, write_type, data_format, data);
            break;
        case Tango::DEV_STATE:
            extract_and_bind_value<Tango::DevState>(statement, bind_name, write_type, data_format, data);
            break;

        /// @todo Add support for DEV_ENCODED
        case Tango::DEV_ENCODED:
        // extract_and_bind_value<Tango::DevEncoded>(statement, bind_name, write_type, data_format,
        // data);
        // break;

        default:
        {
            stringstream error_desc;

            error_desc << "Attribute " << data->attr_name << " type (" << (int)(data_type)
                       << ")) not supported" << ends;

            LOG(Error) << error_desc.str() << endl;
            Tango::Except::throw_exception(EXCEPTION_TYPE_UNSUPPORTED_ATTR, error_desc.str(), __func__);
        }
    } // switch(data_type)
}

//=============================================================================
//=============================================================================
template <>
bool TangoEventDataBinder::extract_value(Tango::EventData *data,
                                         int data_format,
                                         ExtractType extract_type,
                                         vector<Tango::DevState> &val)
{
    bool extract_success = false;

    if (extract_type == ExtractType::ExtractRead)
    {
        if (data_format == Tango::SCALAR)
        {
            // We cannot use the extract_read() method for the "State" attribute
            Tango::DevState st;
            *data->attr_value >> st;
            val.push_back(st);
            extract_success = true;
        }
        else
            extract_success = data->attr_value->extract_read(val);
    }
    else
        extract_success = data->attr_value->extract_set(val);

    return extract_success;
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, bool val)
{
    cass_statement_bind_bool_by_name(statement, bind_name.c_str(), val ? cass_true : cass_false);
}

//=============================================================================
//=============================================================================
template <> void TangoEventDataBinder::collection_append(CassCollection *read_values_list, bool val)
{
    cass_collection_append_bool(read_values_list, val ? cass_true : cass_false);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, unsigned char val)
{
    cass_statement_bind_int32_by_name(statement, bind_name.c_str(), val & 0xFF);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned char val)
{
    cass_collection_append_int32(read_values_list, val & 0xFF);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, short val)
{
    cass_statement_bind_int32_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, short val)
{
    cass_collection_append_int32(read_values_list, val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, unsigned short val)
{
    cass_statement_bind_int32_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned short val)
{
    cass_collection_append_int32(read_values_list, val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, int val)
{
    cass_statement_bind_int32_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <> void TangoEventDataBinder::collection_append(CassCollection *read_values_list, int val)
{
    cass_collection_append_int32(read_values_list, val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, unsigned int val)
{
    cass_statement_bind_int64_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, unsigned int val)
{
    cass_collection_append_int64(read_values_list, val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, int64_t val)
{
    cass_statement_bind_int64_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, int64_t val)
{
    cass_collection_append_int64(read_values_list, val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, uint64_t val)
{
    /// @todo Test extreme values!
    cass_statement_bind_int64_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, uint64_t val)
{
    /// @todo Test extreme values!
    cass_collection_append_int64(read_values_list, val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, string val)
{
    cass_statement_bind_string_by_name(statement, bind_name.c_str(), val.c_str());
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, string val)
{
    cass_collection_append_string(read_values_list, val.c_str());
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, Tango::DevState val)
{
    cass_statement_bind_int32_by_name(statement, bind_name.c_str(), (int8_t)val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, Tango::DevState val)
{
    cass_collection_append_int32(read_values_list, (int8_t)val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, float val)
{
    if (std::isnan(val))
    {
        /// @todo Review quiet/signalling NaN in this context
        cass_statement_bind_float_by_name(statement, bind_name.c_str(), 0);

        /*if(numeric_limits<float>::has_quiet_NaN(val) == true)
            cass_statement_bind_float_by_name(statement, bind_name.c_str(),
        numeric_limits<float>::quiet_NaN());
        else
            cass_statement_bind_float_by_name(statement, bind_name.c_str(),
        numeric_limits<float>::signaling_NaN());*/
    }
    else
        cass_statement_bind_float_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, float val)
{
    if (std::isnan(val))
    {
        /// @todo Review quiet/signalling NaN in this context
        cass_collection_append_float(read_values_list, 0);

        /*if(numeric_limits<float>::has_quiet_NaN(val) == true)
            cass_collection_append_float(read_values_list, numeric_limits<float>::quiet_NaN());
        else
            cass_collection_append_float(read_values_list,
        numeric_limits<float>::signaling_NaN());*/
    }
    else
        cass_collection_append_float(read_values_list, val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::statement_bind(CassStatement *statement, const string &bind_name, double val)
{
    if (std::isnan(val))
    {
        /// @todo Review quiet/signalling NaN in this context
        cass_statement_bind_double_by_name(statement, bind_name.c_str(), 0);

        /*if(numeric_limits<double>::has_quiet_NaN(val))
            cass_statement_bind_double_by_name(statement, bind_name.c_str(),
        numeric_limits<double>::quiet_NaN());
        else
            cass_statement_bind_double_by_name(statement, bind_name.c_str(),
        numeric_limits<double>::signaling_NaN());*/
    }
    else
        cass_statement_bind_double_by_name(statement, bind_name.c_str(), val);
}

//=============================================================================
//=============================================================================
template <>
void TangoEventDataBinder::collection_append(CassCollection *read_values_list, double val)
{
    if (std::isnan(val))
    {
        /// @todo Review quiet/signalling NaN in this context
        cass_collection_append_double(read_values_list, 0);

        /*if(numeric_limits<double>::has_quiet_NaN(val))
            cass_collection_append_double(read_values_list, numeric_limits<double>::quiet_NaN());
        else
            cass_collection_append_double(read_values_list,
        numeric_limits<double>::signaling_NaN());*/
    }
    else
        cass_collection_append_double(read_values_list, val);
}
}; // namespace HDBPP