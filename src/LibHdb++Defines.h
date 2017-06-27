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

#ifndef _HDBPP_LIBHDBPP_DEFINES_H
#define _HDBPP_LIBHDBPP_DEFINES_H

#include <string>

/** 
 * @namespace HDBPP
 * @brief HDBPP encapsulates all the various classes required for the libhdbpp-cassandra 
 * shared library
 *
 * The namaspace currently contains both the exposed HdbPPCassandra class and a number 
 * of helper classes that break out functionality from HdbPPCassandra. In future it 
 * may be the case we hide some of these helper classes inside a inner namespace,
 * to unclutter the HDBPP namespace.
 */

/**
 * @defgroup HDBPP-Interface HDBPP Interface
 * External interface used to archive tango event data into cassandra.
 */

/**
 * @defgroup HDBPP-Implementation HDBPP Implementation
 * Internal implementation classes for this shared library
 */

const std::string TYPE_SCALAR = "scalar";
const std::string TYPE_ARRAY = "array";

const std::string TYPE_DEV_BOOLEAN = "devboolean";
const std::string TYPE_DEV_UCHAR = "devuchar";
const std::string TYPE_DEV_SHORT = "devshort";
const std::string TYPE_DEV_USHORT = "devushort";
const std::string TYPE_DEV_LONG = "devlong";
const std::string TYPE_DEV_ULONG = "devulong";
const std::string TYPE_DEV_LONG64 = "devlong64";
const std::string TYPE_DEV_ULONG64 = "devulong64";
const std::string TYPE_DEV_FLOAT = "devfloat";
const std::string TYPE_DEV_DOUBLE = "devdouble";
const std::string TYPE_DEV_STRING = "devstring";
const std::string TYPE_DEV_STATE = "devstate";
const std::string TYPE_DEV_ENCODED = "devencoded";

const std::string TYPE_RO = "ro";
const std::string TYPE_RW = "rw";

const std::string EVENT_ADD = "add";
const std::string EVENT_REMOVE = "remove";
const std::string EVENT_START = "start";
const std::string EVENT_STOP = "stop";
const std::string EVENT_CRASH = "crash";
const std::string EVENT_PAUSE = "pause";

//######## att_conf ########
const std::string CONF_TABLE_NAME = "att_conf";
const std::string CONF_COL_ID = "att_conf_id";
const std::string CONF_COL_FACILITY = "cs_name";
const std::string CONF_COL_NAME = "att_name";
const std::string CONF_COL_TYPE = "data_type";
const std::string CONF_COL_TTL = "ttl";

//######## domains ########
const std::string DOMAINS_TABLE_NAME = "domains";
const std::string DOMAINS_COL_FACILITY = "cs_name";
const std::string DOMAINS_COL_DOMAIN = "domain";

//######## families ########
const std::string FAMILIES_TABLE_NAME = "families";
const std::string FAMILIES_COL_FACILITY = "cs_name";
const std::string FAMILIES_COL_DOMAIN = "domain";
const std::string FAMILIES_COL_FAMILY = "family";

//######## members ########
const std::string MEMBERS_TABLE_NAME = "members";
const std::string MEMBERS_COL_FACILITY = "cs_name";
const std::string MEMBERS_COL_DOMAIN = "domain";
const std::string MEMBERS_COL_FAMILY = "family";
const std::string MEMBERS_COL_MEMBER = "member";

//######## att_names ########
const std::string ATT_NAMES_TABLE_NAME = "att_names";
const std::string ATT_NAMES_COL_FACILITY = "cs_name";
const std::string ATT_NAMES_COL_DOMAIN = "domain";
const std::string ATT_NAMES_COL_FAMILY = "family";
const std::string ATT_NAMES_COL_MEMBER = "member";
const std::string ATT_NAMES_COL_NAME = "name";

//######## att_history ########
const std::string HISTORY_TABLE_NAME = "att_history";
const std::string HISTORY_COL_ID = "att_conf_id";
const std::string HISTORY_COL_EVENT = "event";
const std::string HISTORY_COL_TIME = "time";
const std::string HISTORY_COL_TIME_US = "time_us";

//######## att_scalar_... ########
const std::string SC_COL_ID = "att_conf_id";
const std::string SC_COL_PERIOD = "period";
const std::string SC_COL_INS_TIME = "insert_time";
const std::string SC_COL_RCV_TIME = "recv_time";
const std::string SC_COL_EV_TIME = "data_time";
const std::string SC_COL_INS_TIME_US = "insert_time_us";
const std::string SC_COL_RCV_TIME_US = "recv_time_us";
const std::string SC_COL_EV_TIME_US = "data_time_us";
const std::string SC_COL_VALUE_R = "value_r";
const std::string SC_COL_VALUE_W = "value_w";
const std::string SC_COL_QUALITY = "quality";
const std::string SC_COL_ERROR_DESC = "error_desc";

//######## att_array_double_ro ########
const std::string ARR_DOUBLE_RO_TABLE_NAME = "att_array_devdouble_ro";
const std::string ARR_DOUBLE_RW_TABLE_NAME = "att_array_devdouble_rw";

const std::string ARR_COL_ID = "att_conf_id";
const std::string ARR_COL_RCV_TIME = "recv_time";
const std::string ARR_COL_RCV_TIME_US = "recv_time_us";
const std::string ARR_COL_EV_TIME = "event_time";
const std::string ARR_COL_EV_TIME_US = "event_time_us";
const std::string ARR_COL_VALUE_R = "value_r";
const std::string ARR_COL_VALUE_W = "value_w";
const std::string ARR_COL_IDX = "idx";
const std::string ARR_COL_DIMX = "dim_x";
const std::string ARR_COL_DIMY = "dim_y";
const std::string ARR_COL_QUALITY = "quality";

//######## att_parameter ########
const std::string PARAM_TABLE_NAME = "att_parameter";
const std::string PARAM_COL_ID = "att_conf_id";
const std::string PARAM_COL_INS_TIME = "insert_time";
const std::string PARAM_COL_INS_TIME_US = "insert_time_us";
const std::string PARAM_COL_EV_TIME = "recv_time";
const std::string PARAM_COL_EV_TIME_US = "recv_time_us";
const std::string PARAM_COL_LABEL = "label";
const std::string PARAM_COL_UNIT = "unit";
const std::string PARAM_COL_STANDARDUNIT = "standard_unit";
const std::string PARAM_COL_DISPLAYUNIT = "display_unit";
const std::string PARAM_COL_FORMAT = "format";
const std::string PARAM_COL_ARCHIVERELCHANGE = "archive_rel_change";
const std::string PARAM_COL_ARCHIVEABSCHANGE = "archive_abs_change";
const std::string PARAM_COL_ARCHIVEPERIOD = "archive_period";
const std::string PARAM_COL_DESCRIPTION = "description";

// Exception types found in the code
const std::string EXCEPTION_TYPE_CONFIG = "LibHDB++ Configuration Error";
const std::string EXCEPTION_TYPE_CONNECTION = "Cassandra Connection Error";
const std::string EXCEPTION_TYPE_QUERY = "Cassandra Query Error";
const std::string EXCEPTION_TYPE_EXTRACT = "Attribute Extraction Error";
const std::string EXCEPTION_TYPE_UNSUPPORTED_ATTR = "Unsupported Attribute Type";
const std::string EXCEPTION_TYPE_MISSING_ATTR = "Missing Attribute";
const std::string EXCEPTION_TYPE_NULL_POINTER = "Null Pointer Error";
const std::string EXCEPTION_TYPE_ATTR_FORMAT = "Attribute Format Error";
const std::string EXCEPTION_TYPE_ATTR_CACHE = "Attribute Cache Error";
const std::string EXCEPTION_TYPE_ATTR_NAME = "Attribute Name Error";

#endif