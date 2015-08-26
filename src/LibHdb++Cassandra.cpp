//=============================================================================
//
// file :        LibHdb++Cassandra.cpp
//
// description : Source file for the LibHdb++Cassandra library.
//
// Author: Reynald Bourtembourg
//
// $Revision: $
//
// $Log: $
//
//=============================================================================

#include "LibHdb++Cassandra.h"
#include <stdlib.h>
#include <cassandra.h>
#include <iostream>
#include <sstream>
#include <iomanip>
#include <cmath>
#include <netdb.h> //for getaddrinfo


#ifndef LIB_BUILDTIME
#define LIB_BUILDTIME   RELEASE " " __DATE__ " "  __TIME__
#endif

const char version_string[] = "$Build: " LIB_BUILDTIME " $";
static const char __FILE__rev[] = __FILE__ " $Id: $";

using namespace HDBPP;

//#define _CASS_LIB_DEBUG
/**
 * @param contact_points string containing comma separated hostnames or IP address (Cassandra contact points)
 */
HdbPPCassandra::HdbPPCassandra(string contact_points, string user, string password, string keyspace_name, int port)
{
	m_keyspace_name = keyspace_name;
	mp_cluster = cass_cluster_new();
  	cout << __func__<<": VERSION: " << version_string << " file:" << __FILE__rev << endl;
#ifdef _CASS_LIB_DEBUG
  	//cass_log_set_level(CASS_LOG_DEBUG);
#endif
	cass_cluster_set_contact_points(mp_cluster,contact_points.c_str());
	
	/* Latency-aware routing enabled with the default settings */
	cass_cluster_set_latency_aware_routing(mp_cluster, cass_true);

	CassError rc = CASS_OK;
	rc = connect_session();
	if(rc != CASS_OK)
	{
		cerr << __func__<< ": Cassandra connect session error"<< endl;
  	}
	else
	{
#ifdef _CASS_LIB_DEBUG
		cout << __func__<< ": Cassandra connection OK" << endl;
#endif
	}

}

HdbPPCassandra::~HdbPPCassandra()
{
	CassFuture* close_future = NULL;
	close_future = cass_session_close(mp_session);
	cass_future_wait(close_future);
	cass_future_free(close_future);
	cass_cluster_free(mp_cluster);
	cass_session_free(mp_session);
}

void HdbPPCassandra::print_error(CassFuture* future)
{
	const char * message;
	size_t message_length;
	cass_future_error_message(future,&message,&message_length);
	fprintf(stderr, "Cassandra Error: %.*s\n", (int)message_length, message);
}

CassError HdbPPCassandra::connect_session()
{
	CassError rc = CASS_OK;
	mp_session = cass_session_new();
	CassFuture* future = cass_session_connect(mp_session, mp_cluster);


	cass_future_wait(future);
	rc = cass_future_error_code(future);
	if(rc != CASS_OK)
	{
    	print_error(future);
	}
	cass_future_free(future);
	return rc;
}

CassError HdbPPCassandra::execute_query(const char* query)
{
	CassError rc = CASS_OK;
	CassFuture* future = NULL;
	CassStatement* statement = cass_statement_new(query, 0);

	future = cass_session_execute(mp_session, statement);
	cass_future_wait(future);

	rc = cass_future_error_code(future);
	if(rc != CASS_OK)
	{
		print_error(future);
	}

	cass_future_free(future);
	cass_statement_free(statement);

	return rc;
}

bool HdbPPCassandra::find_attr_id(string fqdn_attr_name, CassUuid & ID)
{
	// First look into the cache
	map<string,CassUuid>::iterator it = attr_ID_map.find(fqdn_attr_name);
	if(it == attr_ID_map.end())
	{
		// if not already present in cache, look for ID in the DB*/
		if(find_attr_id_in_db(fqdn_attr_name, ID) != 0)
		{
#ifdef _CASS_LIB_DEBUG
			cout << "(" << __FILE__ << "," << __LINE__ << ") "<< __func__<< ": ID not found for attr="<< fqdn_attr_name << endl;
#endif
			return false;
		}
	}
	else
	{
		ID = it->second;
	}
	return true;
}

int HdbPPCassandra::find_attr_id_in_db(string fqdn_attr_name, CassUuid & ID)
{
	ostringstream query_str;
	//string facility_no_domain = remove_domain(facility);
	//string facility_with_domain = add_domain(facility);
	string facility = get_only_tango_host(fqdn_attr_name);
	string attr_name = get_only_attr_name(fqdn_attr_name);
	facility = add_domain(facility);

	query_str << "SELECT " << CONF_COL_ID << " FROM " << m_keyspace_name << "." << CONF_TABLE_NAME <<
			" WHERE " << CONF_COL_NAME << " = ? AND " << CONF_COL_FACILITY << " = ?" << ends;

	CassError rc = CASS_OK;
	CassStatement* statement = NULL;
	CassFuture* future = NULL;

	statement = cass_statement_new(query_str.str().c_str(), 2);
	cass_statement_bind_string(statement, 0, attr_name.c_str());
	cass_statement_bind_string(statement, 1, facility.c_str());

	future = cass_session_execute(mp_session, statement);
	cass_future_wait(future);

	rc = cass_future_error_code(future);
	bool found = false;
	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR in query=" << query_str.str() << endl;
    	print_error(future);
	}
	else
	{

		const CassResult* result = cass_future_get_result(future);
		CassIterator* iterator = cass_iterator_from_result(result);
		if(cass_iterator_next(iterator))
		{
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": SUCCESS in query: " << query_str.str() << endl;
#endif
			const CassRow* row = cass_iterator_get_row(iterator);
			cass_value_get_uuid(cass_row_get_column(row, 0), &ID);
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< "(" << fqdn_attr_name << "): ID found " << endl;
#endif
			found = true;
			attr_ID_map.insert(make_pair(fqdn_attr_name,ID));
			map<string,CassUuid>::iterator it = attr_ID_map.find(fqdn_attr_name);
			if(it == attr_ID_map.end())
			{
				char uuidStr[CASS_UUID_STRING_LENGTH];
				cass_uuid_string(ID,uuidStr);
				cout << "WARNING: (" << __FILE__ << "," << __LINE__ << ") "<< __func__<< ": ID (" << uuidStr << ") could not be added into cache for attr ="<< fqdn_attr_name << endl;
			}
    	}
		cass_result_free(result);
		cass_iterator_free(iterator);
	}

	cass_future_free(future);
	cass_statement_free(statement);
	if(!found)
	{
#ifdef _CASS_LIB_DEBUG
		cout << __func__<< "(" << fqdn_attr_name << "): NO RESULT in query: " << query_str.str() << endl;
#endif
		return -1;
	}
	return 0;
}

int HdbPPCassandra::find_attr_id_type(string facility, string attr, CassUuid & ID, string attr_type)
{
#ifdef _CASS_LIB_DEBUG
    cout << __func__ << ": entering... " << endl;
#endif // _CASS_LIB_DEBUG
	ostringstream query_str;
	//string facility_no_domain = remove_domain(facility);
	//string facility_with_domain = add_domain(facility);

	query_str << "SELECT " << CONF_COL_ID << "," << CONF_COL_TYPE << " FROM " << m_keyspace_name << "." << CONF_TABLE_NAME <<
			" WHERE " << CONF_COL_NAME << " = ? AND " << CONF_COL_FACILITY << " = ?" << ends;

#ifdef _CASS_LIB_DEBUG
    cout << __func__ << ": query = " << query_str.str().c_str() << endl;
    cout << "facility = \"" << facility << "\"" << endl;
    cout << "attr = \"" << attr << "\"" << endl;
#endif // _CASS_LIB_DEBUG

	CassError rc = CASS_OK;
	CassStatement* statement = NULL;
	CassFuture* future = NULL;

	statement = cass_statement_new(query_str.str().c_str(), 2);
	cass_statement_bind_string(statement, 0, attr.c_str());
	cass_statement_bind_string(statement, 1, facility.c_str());

	future = cass_session_execute(mp_session, statement);
	cass_future_wait(future);

	rc = cass_future_error_code(future);
	bool found = false;
	string db_type = "";
	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR in query=" << query_str.str() << endl;
    	print_error(future);
	}
	else
	{
		const CassResult* result = cass_future_get_result(future);
		CassIterator* iterator = cass_iterator_from_result(result);
		if(cass_iterator_next(iterator))
		{
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": SUCCESS in query: " << query_str.str() << endl;
#endif
            // TODO Improve error handling
			const CassRow* row = cass_iterator_get_row(iterator);
			cass_value_get_uuid(cass_row_get_column(row, 0), &ID);
			const char * db_type_res;
			size_t db_type_res_length;
			cass_value_get_string(cass_row_get_column(row, 1), &db_type_res, &db_type_res_length);
			db_type = string(db_type_res,db_type_res_length);
			found = true;
    	}
		cass_result_free(result);
		cass_iterator_free(iterator);
	}

	cass_future_free(future);
	cass_statement_free(statement);
	if(!found)
	{
#ifdef _CASS_LIB_DEBUG
		cout << __func__<< ": NO RESULT in query: " << query_str.str() << endl;
#endif
		return -1;
	}
	if(db_type != attr_type)
	{
#ifdef _CASS_LIB_DEBUG
		cout << __func__<< ": FOUND ID for " << facility << "/" << attr << " but different type: attr_type="<<attr_type<<"-db_type="<<db_type << endl;
#endif
		return -2;
	}
	else
	{
#ifdef _CASS_LIB_DEBUG
		cout << __func__<< ": FOUND ID for " << facility << "/" << attr << " with SAME type: attr_type="<<attr_type<<"-db_type="<<db_type << endl;
#endif
		return 0;
	}
}

void HdbPPCassandra::extract_and_bind_bool(CassStatement* statement,
                                           int & param_index,
                                           int data_format /*SCALAR, SPECTRUM, ..*/,
                                           Tango::EventData *data,
                                           enum extract_t extract_type)
{
	vector<bool> vbool;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(vbool);
	else
		extract_success = data->attr_value->extract_set(vbool);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_bool(statement, param_index, vbool[0]?cass_true: cass_false);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, vbool.size());
			for(unsigned int i = 0; i < vbool.size(); i++)
			{
				cass_collection_append_bool(readValuesList, vbool[i]?cass_true:cass_false);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_uchar(CassStatement* statement,
                                            int & param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
	vector<unsigned char> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int32(statement, param_index, val[0] & 0xFF);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int32(readValuesList, val[i] & 0xFF);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_short(CassStatement* statement,
                                            int & param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
	vector<short> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int32(statement, param_index, val[0]);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int32(readValuesList, val[i]);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_ushort(CassStatement* statement,
                                             int & param_index,
                                             int data_format /*SCALAR, SPECTRUM, ..*/,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
	vector<unsigned short> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int32(statement, param_index, val[0]);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int32(readValuesList, val[i]);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_long(CassStatement* statement,
                                           int & param_index,
                                           int data_format /*SCALAR, SPECTRUM, ..*/,
                                           Tango::EventData *data,
                                           enum extract_t extract_type)
{
	vector<int> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int32(statement, param_index, val[0]);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int32(readValuesList, val[i]);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_ulong(CassStatement* statement,
                                            int & param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
	vector<unsigned int> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int64(statement, param_index, val[0]); // TODO? Bind to int32?
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int64(readValuesList, val[i]); // TODO? Bind to int32?
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_long64(CassStatement* statement,
                                             int & param_index,
                                             int data_format /*SCALAR, SPECTRUM, ..*/,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
	vector<int64_t> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int64(statement, param_index, val[0]);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int64(readValuesList, val[i]);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_ulong64(CassStatement* statement,
                                              int & param_index,
                                              int data_format /*SCALAR, SPECTRUM, ..*/,
                                              Tango::EventData *data,
                                              enum extract_t extract_type)
{
	vector<uint64_t> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int64(statement, param_index, val[0]); // TODO? Test extreme values!
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int64(readValuesList, val[i]); // TODO? Test extreme values!
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_string(CassStatement* statement,
                                             int & param_index,
                                             int data_format /*SCALAR, SPECTRUM, ..*/,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
	vector<string> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_string(statement, param_index, val[0].c_str());
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_string(readValuesList, val[i].c_str());
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_state(CassStatement* statement,
                                            int & param_index,
                                            int data_format /*SCALAR, SPECTRUM, ..*/,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
	vector<Tango::DevState> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
	{
		if(data_format == Tango::SCALAR)
		{
			// We cannot use the extract_read() method for the "State" attribute
			Tango::DevState	st;
			*data->attr_value >> st;
			cass_statement_bind_int32(statement, param_index, (int8_t)st);
			return;
		}
		// ARRAY case:
		extract_success = data->attr_value->extract_read(val);
	}
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			cass_statement_bind_int32(statement, param_index, (int8_t)val[0]);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				cass_collection_append_int32(readValuesList, (int8_t)val[i]);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList);
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cerr << __func__ << "extract_read failed for attribute "<< data->attr_name << "! (" << __FILE__ << ":" << __LINE__ << ")" << endl;
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_encoded(CassStatement* statement,
                                              int & param_index,
                                              int data_format /*SCALAR, SPECTRUM, ..*/,
                                              Tango::EventData *data,
                                              enum extract_t extract_type)
{
	// TODO: Not yet supported
	cerr << __func__<< ": DevEncoded type is not yet supported..." << endl;
// 	vector<Tango::DevEncoded> val;
// 	bool extract_success = false;
//
// 	if(extract_type == EXTRACT_READ)
// 		extract_success = data->attr_value->extract_read(val);
// 	else
// 		extract_success = data->attr_value->extract_set(val);
//
// 	if(extract_success)
// 	{
// 		if(data_format == Tango::SCALAR)
// 		{
// 			cass_statement_bind_int32(statement, param_index, val[0]);
// 		}
// 		else
// 		{
// 			cout << __func__<< ": DevEncoded type is not yet supported..." << endl;
// 			// Store the array into a CQL list
// 			CassCollection* readValuesList = NULL;
// 			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
// 			for(unsigned int i = 0; i < val.size(); i++)
// 			{
// 			  cass_collection_append_int32(readValuesList, (int8_t)val[i]);
// 			}
// 			cass_statement_bind_collection(statement, param_index, readValuesList);
// 			cass_collection_free(readValuesList);
// 		}
// 	}
// 	else
// 	{
// 		cass_statement_bind_null(statement,param_index);
// 	}
}

void HdbPPCassandra::extract_and_bind_float(CassStatement* statement,
                                            int & param_index,
                                            int data_format /* Tango::SCALAR, ... */,
                                            Tango::EventData *data,
                                            enum extract_t extract_type)
{
	vector<float> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			if(std::isnan(val[0]))
				cass_statement_bind_float(statement, param_index, std::numeric_limits<float>::quiet_NaN());
			else if (std::isinf(val[0]))
				cass_statement_bind_float(statement, param_index, std::numeric_limits<float>::infinity());
			else
				cass_statement_bind_float(statement, param_index, val[0]);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				if(std::isnan(val[i]))
					cass_collection_append_float(readValuesList, std::numeric_limits<float>::quiet_NaN());
				else if (std::isinf(val[i]))
					cass_collection_append_float(readValuesList, std::numeric_limits<float>::infinity());
				else
					cass_collection_append_float(readValuesList, val[i]);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList); // value_r
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_double(CassStatement* statement,
                                             int & param_index,
                                             int data_format /* Tango::SCALAR, ... */,
                                             Tango::EventData *data,
                                             enum extract_t extract_type)
{
	vector<double> val;
	bool extract_success = false;

	if(extract_type == EXTRACT_READ)
		extract_success = data->attr_value->extract_read(val);
	else
		extract_success = data->attr_value->extract_set(val);

	if(extract_success)
	{
		if(data_format == Tango::SCALAR)
		{
			if(std::isnan(val[0]))
				cass_statement_bind_double(statement, param_index, std::numeric_limits<double>::quiet_NaN());
			else if (std::isinf(val[0]))
				cass_statement_bind_double(statement, param_index, std::numeric_limits<double>::infinity());
			else
				cass_statement_bind_double(statement, param_index, val[0]);
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++)
			{
				if(std::isnan(val[i]))
					cass_collection_append_double(readValuesList, std::numeric_limits<double>::quiet_NaN());
				else if (std::isinf(val[i]))
					cass_collection_append_double(readValuesList, std::numeric_limits<double>::infinity());
				else
					cass_collection_append_double(readValuesList, val[i]);
			}
			cass_statement_bind_collection(statement, param_index, readValuesList); // value_r
			cass_collection_free(readValuesList);
		}
	}
	else
	{
		cass_statement_bind_null(statement,param_index);
	}
}

void HdbPPCassandra::extract_and_bind_rw_values(CassStatement* statement,
                                                int & param_index,
												int data_type,
                                                int write_type /*READ, READ_WRITE, ..*/,
                                                int data_format /*SCALAR, SPECTRUM, ..*/,
                                                Tango::EventData *data,
                                                bool isNull)
{
	if(write_type != Tango::WRITE)
	{
		if(isNull)
		{
			// There is no value to bind. 
			// This is to avoid storing NULL values into Cassandra (<=> tombstones)
			return; 
		}
		else
		{
			// There is a read value
			switch(data_type)
			{
				case Tango::DEV_BOOLEAN:
					extract_and_bind_bool(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_UCHAR:
					extract_and_bind_uchar(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_SHORT:
					extract_and_bind_short(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_USHORT:
					extract_and_bind_ushort(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_LONG:
					extract_and_bind_long(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_ULONG:
					extract_and_bind_ulong(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_LONG64:
					extract_and_bind_long64(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_ULONG64:
					extract_and_bind_ulong64(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_FLOAT:
					extract_and_bind_float(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_DOUBLE:
					extract_and_bind_double(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_STRING:
					extract_and_bind_string(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_STATE:
					extract_and_bind_state(statement,param_index, data_format, data, EXTRACT_READ); break;
				case Tango::DEV_ENCODED:
					extract_and_bind_encoded(statement,param_index, data_format, data, EXTRACT_READ); break;
				default:
				{
					TangoSys_MemStream os;
					os << "Attribute " << data->attr_name << " type (" << (int)(data_type) << ")) not supported";
					cerr << __func__ << ": " << os.str() << endl;
					return;
				}
			} // switch(data_type)
		}
		param_index++;
	}
	if(write_type != Tango::READ)
	{
		if(isNull)
		{
			// There is no value to bind. 
			// This is to avoid storing NULL values into Cassandra (<=> tombstones)
			return; 
		}
		else
		{
			// There is a write value
			switch(data_type)
			{
				case Tango::DEV_BOOLEAN:
					extract_and_bind_bool(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_UCHAR:
					extract_and_bind_uchar(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_SHORT:
					extract_and_bind_short(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_USHORT:
					extract_and_bind_ushort(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_LONG:
					extract_and_bind_long(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_ULONG:
					extract_and_bind_ulong(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_LONG64:
					extract_and_bind_long64(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_ULONG64:
					extract_and_bind_ulong64(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_FLOAT:
					extract_and_bind_float(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_DOUBLE:
					extract_and_bind_double(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_STRING:
					extract_and_bind_string(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_STATE:
					extract_and_bind_state(statement,param_index, data_format, data, EXTRACT_SET); break;
				case Tango::DEV_ENCODED:
					extract_and_bind_encoded(statement,param_index, data_format, data, EXTRACT_SET); break;
				default:
				{
					TangoSys_MemStream os;
					os << "Attribute " << data->attr_name << " type (" << (int)(data_type) << ")) not supported";
					cerr << __func__ << ": " << os.str() << endl;
					return;
				}
			} // switch(data_type)
		}
		param_index++;
	}
}

int HdbPPCassandra::find_last_event(const CassUuid & id, string &last_event, const string & fqdn_attr_name)
{
	ostringstream query_str;
	CassStatement* statement = NULL;
	CassFuture* future = NULL;
	last_event="??";

	query_str   << "SELECT " << HISTORY_COL_EVENT
                << " FROM " << m_keyspace_name << "." << HISTORY_TABLE_NAME
                << " WHERE " << HISTORY_COL_ID << " = ?"
                << " ORDER BY " << HISTORY_COL_TIME << " DESC LIMIT 1" << ends;


    CassError rc = CASS_OK;

	statement = cass_statement_new(query_str.str().c_str(), 1);
	cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable?
	cass_statement_bind_uuid(statement, 0, id); // att_conf_id

    future = cass_session_execute(mp_session, statement);
	cass_future_wait(future);

	rc = cass_future_error_code(future);
	bool found = false;

	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR in query=" << query_str.str() << endl;
    	print_error(future);
	}
	else
	{
		const CassResult* result = cass_future_get_result(future);
		CassIterator* iterator = cass_iterator_from_result(result);
		if(cass_iterator_next(iterator))
		{
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": SUCCESS in query: " << query_str.str() << endl;
#endif
			const CassRow* row = cass_iterator_get_row(iterator);
			const char * last_event_res;
			size_t last_event_res_length;
			cass_value_get_string(cass_row_get_column(row, 0), &last_event_res, &last_event_res_length);
			last_event = string(last_event_res,last_event_res_length);
			found = true;
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< "(" << fqdn_attr_name << "): last event = " << last_event << endl;
#endif
    	}
		cass_result_free(result);
		cass_iterator_free(iterator);
	}

	cass_future_free(future);
	cass_statement_free(statement);
	if(!found)
	{
#ifdef _CASS_LIB_DEBUG
		cout << __func__<< "(" << fqdn_attr_name << "): NO RESULT in query: " << query_str.str() << endl;
#endif
		return -1;
	}
	return 0;
}

int HdbPPCassandra::insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type)
{
#ifdef _CASS_LIB_DEBUG
//	cout << __func__<< ": entering..." << endl;
#endif

    if(data == 0)
    {
        cerr << "HdbPPCassandra::insert_Attr(): data is a null pointer!" << endl;
        return -1;
    }

	CassStatement* statement = NULL;
	CassFuture* future = NULL;

	try
	{
		string fqdn_attr_name = data->attr_name;
		int64_t	ev_time;
		int ev_time_us;
		int64_t rcv_time = ((int64_t) data->get_date().tv_sec) * 1000;
		int rcv_time_us = data->get_date().tv_usec;
		int quality = (int)data->attr_value->get_quality();
		string error_desc("");

		Tango::AttributeDimension attr_w_dim;
		Tango::AttributeDimension attr_r_dim;
		int data_type = ev_data_type.data_type; // data->attr_value->get_type()
		Tango::AttrDataFormat data_format = ev_data_type.data_format;
		int write_type = ev_data_type.write_type;
		//int max_dim_x = ev_data_type.max_dim_x;
		//int max_dim_y = ev_data_type.max_dim_y;

		bool isNull = false;
		if(data->err)
		{
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": Attribute in error:" << error_desc << endl;
#endif
			isNull = true;
			// Store the error description
			error_desc = data->errors[0].desc;
		}
		data->attr_value->reset_exceptions(Tango::DeviceAttribute::isempty_flag); // disable is_empty exception
		if(data->attr_value->is_empty())
		{
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": no value will be archived... (Attr Value is empty)" << endl;
#endif
			isNull = true;
		}
		if(quality == Tango::ATTR_INVALID)
		{
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": no value will be archived... (Invalid Attribute)" << endl;
#endif
			isNull = true;
		}
#ifdef _CASS_LIB_DEBUG
		cout << __func__<< ": data_type="<<data_type<<" data_format="<<data_format<<" write_type="<<write_type << endl;
#endif
		if(!isNull)
		{
			attr_w_dim = data->attr_value->get_w_dimension();
			attr_r_dim = data->attr_value->get_r_dimension();
			ev_time = ((int64_t) data->attr_value->get_date().tv_sec)*1000;
            ev_time_us = data->attr_value->get_date().tv_usec;
		}
		else
		{
			attr_r_dim.dim_x = 0;//max_dim_x;//TODO: OK?
			attr_w_dim.dim_x = 0;//max_dim_x;//TODO: OK?
			attr_r_dim.dim_y = 0;//max_dim_y;//TODO: OK?
			attr_w_dim.dim_y = 0;//max_dim_y;//TODO: OK?
			ev_time = rcv_time;
			ev_time_us = rcv_time_us;
		}

		CassUuid ID;
		string facility = get_only_tango_host(fqdn_attr_name);
		string attr_name = get_only_attr_name(fqdn_attr_name);
		facility = add_domain(facility);
		if(!find_attr_id(fqdn_attr_name, ID))
		{
			cerr << __func__<< ": Could not find ID for attribute " << fqdn_attr_name << endl;
			return -1;
		}

		int nbQueryParams = 0;
		string query_str = get_insert_query_str(data_type,data_format,write_type, nbQueryParams, isNull);

		CassError rc = CASS_OK;

		statement = cass_statement_new(query_str.c_str(), nbQueryParams);
		cass_statement_bind_uuid(statement, 0, ID); // att_conf_id
		// Compute the period based on the month of the event time
		struct tm *tms;
		time_t ev_time_s = ev_time / 1000;
		if ((tms = localtime (&ev_time_s)) == NULL)
			perror ("localtime");
		char period[11];
		snprintf(period,11,"%04d-%02d-%02d", tms -> tm_year + 1900,tms -> tm_mon + 1, tms -> tm_mday);
		cass_statement_bind_string(statement, 1, period); // period
		cass_statement_bind_int64(statement, 2, ev_time); // event_time
		cass_statement_bind_int32(statement, 3, ev_time_us); // event_time_us
  		cass_statement_bind_int64(statement, 4, rcv_time); // recv_time
		cass_statement_bind_int32(statement, 5, rcv_time_us); // recv_time_us
		// Get the current time
		struct timespec ts;
		if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
		{
			perror("clock_gettime()");
		}
		int64_t insert_time = ((int64_t) ts.tv_sec) * 1000;
		int insert_time_us = ts.tv_nsec / 1000;
		cass_statement_bind_int64(statement, 6, insert_time); // insert_time
		cass_statement_bind_int32(statement, 7, insert_time_us); // insert_time_us
		cass_statement_bind_int32(statement, 8, quality); // quality
		cass_statement_bind_string(statement,9, error_desc.c_str()); // error description
		int param_index = 10;

		extract_and_bind_rw_values(statement,param_index,data_type,write_type,data_format,data,isNull);

		future = cass_session_execute(mp_session, statement);
		cass_future_wait(future);

		rc = cass_future_error_code(future);
		if(rc != CASS_OK)
			print_error(future);

		cass_future_free(future);
  		cass_statement_free(statement);

		if(rc != CASS_OK)
			return -1;
#ifdef _CASS_LIB_DEBUG
		else
			cout << __func__<< ": SUCCESS in query: " << query_str << endl;
#endif
	}
	catch(Tango::DevFailed &e)
	{
		cerr << "Exception on " << data->attr_name << ":" << endl;

		for (unsigned int i=0; i<e.errors.length(); i++)
		{
			cerr << e.errors[i].reason << endl;
			cerr << e.errors[i].desc << endl;
			cerr << e.errors[i].origin << endl;
		}

		cerr << endl;
		if(future != NULL)
			cass_future_free(future);
		if(statement != NULL)
	  		cass_statement_free(statement);
		return -1;
	}
#ifdef _CASS_LIB_DEBUG
//	cout << __func__<< ": exiting... ret="<<ret << endl;
#endif
	return 0;
}

bool HdbPPCassandra::insert_history_event(const string & history_event_name, CassUuid att_conf_id)
{
#ifdef _CASS_LIB_DEBUG
	char att_conf_id_str[CASS_UUID_STRING_LENGTH];
	cass_uuid_string(att_conf_id, &att_conf_id_str[0]);
	cout << __func__<< "(" << history_event_name << "," << att_conf_id_str << ": entering... " << endl;
#endif
	struct timespec ts;
	if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
	{
		perror("clock_gettime()");
	}
	int64_t current_time = ((int64_t) ts.tv_sec) * 1000;
	int current_time_us = ts.tv_nsec / 1000;

	ostringstream insert_event_str;
	insert_event_str <<	"INSERT INTO " << m_keyspace_name << "." << HISTORY_TABLE_NAME
	                 << " (" << HISTORY_COL_ID <<","<<HISTORY_COL_EVENT<<","<<HISTORY_COL_TIME<<","<<HISTORY_COL_TIME_US<<")"
					 << " VALUES ( ?, ?, ?, ?)" << ends;

	CassStatement* statement = NULL;
	CassFuture* future = NULL;
	statement = cass_statement_new(insert_event_str.str().c_str(), 4);
	cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable?
	cass_statement_bind_uuid(statement, 0, att_conf_id);
	cass_statement_bind_string(statement, 1, history_event_name.c_str());
	cass_statement_bind_int64(statement, 2, current_time);
	cass_statement_bind_int32(statement, 3, current_time_us);

	future = cass_session_execute(mp_session, statement);
	cass_future_wait(future);

	CassError rc = CASS_OK;
	rc = cass_future_error_code(future);
	if(rc != CASS_OK)
		print_error(future);

	cass_future_free(future);
  	cass_statement_free(statement);

	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR executing query=" << insert_event_str.str() << endl;
		return false;
	}

	return true;
}


int HdbPPCassandra::insert_param_Attr(Tango::AttrConfEventData *data, HdbEventDataType ev_data_type)
{
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< ": entering..." << endl;
#endif
    if(data == 0)
    {
        cerr << "HdbPPCassandra::insert_param_Attr(): data is a null pointer!" << endl;
		return -1;
    }
    if(data->attr_conf == 0)
    {
        cerr << __func__ << ": data->attr_conf is a null pointer!" << endl;
    }
    string fqdn_attr_name = data->attr_name;
    int64_t ev_time = 0;
    int ev_time_us = 0;
	try
	{
		ev_time = ((int64_t) data->get_date().tv_sec) * 1000;
		ev_time_us = data->get_date().tv_usec;
	}
	catch(Tango::DevFailed &e)
	{
		cerr << "Exception on " << data->attr_name << ":" << endl;
		for (unsigned int i=0; i<e.errors.length(); i++)
		{
			cerr << e.errors[i].reason << endl;
			cerr << e.errors[i].desc << endl;
			cerr << e.errors[i].origin << endl;
		}
		cerr << endl;
		return -1;
	}
	CassUuid uuid;
	string facility = get_only_tango_host(fqdn_attr_name);
	string attr_name = get_only_attr_name(fqdn_attr_name);
	facility = add_domain(facility);
    if(!find_attr_id(fqdn_attr_name, uuid))
	{
		cerr << __func__<< ": Could not find ID for attribute " << fqdn_attr_name << endl;
		return -1;
	}
	else
    {
        char uuidStr[CASS_UUID_STRING_LENGTH];
		cass_uuid_string(uuid,uuidStr);
#ifdef _CASS_LIB_DEBUG
        cout << __func__ << "ID found for attribute " << fqdn_attr_name << " = " << uuidStr << endl;
#endif
    }

	ostringstream query_str;
	query_str << "INSERT INTO " << m_keyspace_name << "." << PARAM_TABLE_NAME
              << " (" << PARAM_COL_ID << "," << PARAM_COL_EV_TIME << "," << PARAM_COL_EV_TIME_US << ","
              << PARAM_COL_INS_TIME << "," << PARAM_COL_INS_TIME_US << ","
              << PARAM_COL_LABEL << "," << PARAM_COL_UNIT << "," << PARAM_COL_STANDARDUNIT << ","
			  << PARAM_COL_DISPLAYUNIT << "," << PARAM_COL_FORMAT << "," << PARAM_COL_ARCHIVERELCHANGE << ","
			  << PARAM_COL_ARCHIVEABSCHANGE << "," << PARAM_COL_ARCHIVEPERIOD << "," << PARAM_COL_DESCRIPTION << ")";

    query_str << " VALUES (?,?,?,"
			  << "?,?,"
			  << "?,?,?,"
			  << "?,?,?,"
			  << "?,?,?)"
			  << ends;

    CassStatement* statement = NULL;
    CassFuture* future = NULL;
    CassError rc = CASS_OK;
    statement = cass_statement_new(query_str.str().c_str(), 14);
    cass_statement_bind_uuid(statement, 0, uuid);
    // Get the current time
    struct timespec ts;
    if (clock_gettime(CLOCK_REALTIME, &ts) != 0)
    {
        perror("clock_gettime()");
    }
    int64_t insert_time = ((int64_t) ts.tv_sec) * 1000;
    int insert_time_us = ts.tv_nsec / 1000;
    cass_statement_bind_int64(statement, 1, ev_time); // recv_time
    cass_statement_bind_int32(statement, 2, ev_time_us); // recv_time_us
    cass_statement_bind_int64(statement, 3, insert_time); // insert_time
    cass_statement_bind_int32(statement, 4, insert_time_us); // insert_time_us


#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " label: \"" << data->attr_conf->label.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement, 5, data->attr_conf->label.c_str()); // label
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " unit: \"" << data->attr_conf->unit.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement, 6, data->attr_conf->unit.c_str()); // unit
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " standard unit: \"" << data->attr_conf->standard_unit.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement, 7, data->attr_conf->standard_unit.c_str()); // standard unit
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " display unit: \"" << data->attr_conf->display_unit.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement, 8, data->attr_conf->display_unit.c_str()); // display unit
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " format: \"" << data->attr_conf->format.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement, 9, data->attr_conf->format.c_str()); // format
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " archive rel change: \"" << data->attr_conf->events.arch_event.archive_rel_change.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement,10, data->attr_conf->events.arch_event.archive_rel_change.c_str()); // archive relative range
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " archive abs change: \"" << data->attr_conf->events.arch_event.archive_abs_change.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement,11, data->attr_conf->events.arch_event.archive_abs_change.c_str()); // archive abs change
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " archive period: \"" << data->attr_conf->events.arch_event.archive_period.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement,12, data->attr_conf->events.arch_event.archive_period.c_str()); // archive period
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " description: \"" << data->attr_conf->description.c_str() << "\"" << endl;
#endif
    cass_statement_bind_string(statement,13, data->attr_conf->description.c_str()); // description
#ifdef _CASS_LIB_DEBUG
	cout << __func__<< " after binding description"<< endl;
#endif
    cass_statement_set_consistency(statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable? => would be useful!
    future = cass_session_execute(mp_session, statement);
    cass_future_wait(future);

    rc = cass_future_error_code(future);
    if(rc != CASS_OK)
    {
        print_error(future);
    }

    cass_future_free(future);
    cass_statement_free(statement);

    if(rc != CASS_OK)
    {
        cerr << __func__ << ": ERROR executing query=" << query_str.str().c_str() << endl;
        return -1;
    }
    return 0;
}


int HdbPPCassandra::configure_Attr(string name, int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/)
{
	string facility = get_only_tango_host(name);
	facility = add_domain(facility);
	string attr_name = get_only_attr_name(name);
	string domain = "";
	string family = "";
	string member = "";
	string attribute_name = "";
	int parsingError = getDomainFamMembName(attr_name,domain,family,member,attribute_name);
	if(parsingError)
	{
		cerr << "Error parsing attribute name \"" << attr_name << "\"" << endl;
		return -5;
	}
#ifdef _CASS_LIB_DEBUG
	cout<< __func__ << ": name="<<name<<" -> facility="<<facility<<" attr_name="<<attr_name<< endl;
#endif
	string data_type = get_data_type(type, format, write_type);
	CassUuid id;
	int ret = find_attr_id_type(facility, attr_name, id, data_type);
	//ID already present but different configuration (attribute type)
	if(ret == -2)
	{
		cout << __func__ << ": "<< facility << "/" << attr_name << " already configured with different configuration" << endl;
		cout << "Please contact your administrator for this special case" << endl;
		// Need to contact an administrator in this case!?
	}
	else
	{
		//ID found and same configuration (attribute type): do nothing
		if(ret == 0)
		{
			cout << __func__ << ": ALREADY CONFIGURED with same configuration: "<<facility<<"/"<<attr_name << endl;
			// If the last event was EVENT_REMOVE, add it again
			string last_event;
			ret = find_last_event(id, last_event, name);
			if(ret == 0 && last_event == EVENT_REMOVE)
			{
				// An attribute which was removed needs to be added again
				if(!insert_history_event(EVENT_ADD, id))
				{
					cerr << __func__ << "Error adding ADD event to history table for attribute " << name
				    	 << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
					return -1;
				}
			}
		}
		else
		{
			// insert into configuration table
			CassUuid uuid;
			int error_insert_att_conf = insert_attr_conf(facility,attr_name,data_type,uuid);
			if ( error_insert_att_conf )
			{
				cerr << __func__ << "(" << name 
				     << "): Error inserting into " << CONF_TABLE_NAME << " table (error = " 
					 << error_insert_att_conf << ")" << endl;
				return error_insert_att_conf;
			}
			// Add ADD event into history table
			if(!insert_history_event(EVENT_ADD, uuid))
			{
				cerr << __func__ << "Error adding ADD event to history table for attribute " << name
			    	 << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
				return -1;
			}
		}
	}
	
	// Insert into domains table
	int error_insert_domain = insert_domain(facility,domain);
	if(error_insert_domain)
	{
		cerr << __func__ << "(" << name 
		     << "): Error inserting into " << DOMAINS_TABLE_NAME << " table (error = " 
			 << error_insert_domain << ")"
			 << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
	}
	
	// Insert into families table
	int error_insert_family = insert_family(facility,domain,family);
	if(error_insert_family)
	{
		cerr << __func__ << "(" << name 
		     << "): Error inserting into " << FAMILIES_TABLE_NAME << " table (error = " 
			 << error_insert_family << ")"
			 << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
	}
	
	// Insert into members table
	int error_insert_member = insert_member(facility,domain,family,member);
	if(error_insert_member)
	{
		cerr << __func__ << "(" << name 
		     << "): Error inserting into " << MEMBERS_TABLE_NAME << " table (error = " 
			 << error_insert_member << ")"
			 << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
	}
	
	// Insert into att_names table
	int error_insert_attr_name = insert_attr_name(facility,domain,family,member,attribute_name);
	if(error_insert_attr_name)
	{
		cerr << __func__ << "(" << name 
		     << "): Error inserting into " << ATT_NAMES_TABLE_NAME << " table (error = " 
			 << error_insert_attr_name << ")"
			 << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
	}
	
	if(error_insert_domain || error_insert_family || error_insert_member || error_insert_attr_name)
	{
		return -1;
	}
	return 0;
}

int HdbPPCassandra::event_Attr(string fqdn_attr_name, unsigned char event)
{
	ostringstream remove_event_str;
	CassUuid uuid;
	int ret = find_attr_id(fqdn_attr_name, uuid);
	if(ret < 0)
	{
		cerr << __func__ << ": ERROR "<< fqdn_attr_name <<" NOT FOUND" << endl;
		return -1;
	}
	
	string event_name = "";
	
	switch(event)
	{
		case DB_START:
		{
			string last_event;
			ret = find_last_event(uuid, last_event, fqdn_attr_name);
			if(ret == 0 && last_event == EVENT_START)
			{
	    		// It seems there was a crash
        		if(!insert_history_event(EVENT_CRASH, uuid))
        		{
					cerr << __func__ << "Error adding CRASH event to history table for attribute " << fqdn_attr_name
					     << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
				}
			}
			event_name = EVENT_START;
			break;
		}
		case DB_STOP:
		{
			event_name = EVENT_STOP;
			break;
		}
		case DB_REMOVE:
		{
			event_name = EVENT_REMOVE;
			break;
		}
		case DB_PAUSE:
		{
			event_name = EVENT_PAUSE;
			break;
		}
		default:
		{
			cerr << __func__ << ": ERROR for "<< fqdn_attr_name << " event=" << (int)event << " NOT SUPPORTED" << endl;
			return -1;
		}
	}
	
	if(!insert_history_event(event_name, uuid))
	{
		cerr << __func__ << "Error adding "<< event_name << " event to history table for attribute " << fqdn_attr_name
		     << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
		return -2;
	}
	return 0;
}


string HdbPPCassandra::get_insert_query_str(int tango_data_type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                            int data_format /* SCALAR, SPECTRUM */,
                                            int write_type/*READ, READ_WRITE, ..*/,
                                            int & nbQueryParams,
                                            bool isNull) const
{
	string table_name = get_table_name(tango_data_type, data_format, write_type);
	ostringstream query_str;
	nbQueryParams = 10;
	query_str << "INSERT INTO " << m_keyspace_name << "." << table_name
	          << " ("
	          << SC_COL_ID << ","
	          << SC_COL_PERIOD << ","
	          << SC_COL_EV_TIME << ","
	          << SC_COL_EV_TIME_US  << ","
	          << SC_COL_RCV_TIME << ","
	          << SC_COL_RCV_TIME_US << ","
	          << SC_COL_INS_TIME << ","
	          << SC_COL_INS_TIME_US << ","
	          << SC_COL_QUALITY << ","
	          << SC_COL_ERROR_DESC;
	
	// TODO: store dim_x, dim_y for spectrum attributes
	
    // We don't insert the value if the value is null because
    // it would create an unnecessary tombstone in Cassandra
    if(!isNull)
    {
        if(write_type != Tango::WRITE) // RO or RW
            query_str << "," << SC_COL_VALUE_R;

        if(write_type != Tango::READ)	// RW or WO
            query_str << "," << SC_COL_VALUE_W;
    }

	query_str << ") VALUES (?,?,?,?,?,?,?,?,?,?";

	if(!isNull)
    {
        if(write_type != Tango::WRITE) // RO or RW
        {
            query_str << ",?";
            nbQueryParams++;
        }
        if(write_type != Tango::READ)	// RW or WO
        {
            query_str << ",?";
            nbQueryParams++;
        }
    }

    query_str << ")" << ends;

#ifdef _CASS_LIB_DEBUG
	cout << __func__<< "Query = \"" << query_str.str() << "\"" << endl;
#endif
	return query_str.str();

}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_only_attr_name(string str)
{
	string::size_type	start = str.find("tango://");
	if (start == string::npos)
		return str;
	else
	{
		start += 8; //	"tango://" length
		start = str.find('/', start);
		start++;
		string	signame = str.substr(start);
		return signame;
	}
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_only_tango_host(string str)
{
	string::size_type	start = str.find("tango://");
	if (start == string::npos)
	{
		return "unknown";
	}
	else
	{
		start += 8; //	"tango://" length
		string::size_type	end = str.find('/', start);
		string th = str.substr(start, end-start);
		return th;
	}
}

//=============================================================================
/**
 * This method parses attr_name and extracts the domain, 
 * family, member and attribute name from the provided string
 * @param attr_name attribute name under the form <domain>/<family>/<member>/<name>
 * @param domain the domain extracted from the provided attr_name parameter (<domain>)
 * @param family the family extracted from the provided attr_name parameter (<family>)
 * @param member the member extracted from the provided attr_name parameter (<member>)
 * @param name the attribute name extracted from the provided attr_name parameter (<name>)
 */
int HdbPPCassandra::getDomainFamMembName(const string & attr_name,
                                          string & domain,
                                          string & family,
                                          string & member,
                                          string & name)
{
	string::size_type	first_slash = attr_name.find("/");
	if (first_slash == string::npos)
	{
		cerr << "getDomainFamMembName(" << attr_name << "): Error: there is no slash in attribute name" << endl;
		return ERR_NO_SLASH_IN_ATTR;
	}
	string::size_type	second_slash = attr_name.find("/", first_slash+1);
	if (second_slash == string::npos)
	{
		cerr << "getDomainFamMembName(" << attr_name << "): Error: there is only one slash in attribute name" << endl;
		return ERR_ONLY_ONE_SLASH_IN_ATTR;
	}
	string::size_type	third_slash = attr_name.find("/", second_slash+1);
	if (third_slash == string::npos)
	{
		cerr << "getDomainFamMembName(" << attr_name << "): Error: there are only two slashes in attribute name" << endl;
		return ERR_ONLY_TWO_SLASHES_IN_ATTR;
	}
	string::size_type last_slash = attr_name.rfind("/");
	if(last_slash != third_slash)
	{
		// Too many slashes provided!
		cerr << "getDomainFamMembName(" << attr_name << "): Too many slashes provided in attribute name" << endl;
		return ERR_TOO_MANY_SLASHES_IN_ATTR;
	}
	if(first_slash == 0)
	{
		// empty domain
		cerr << "getDomainFamMembName(" << attr_name << "): empty domain" << endl;
		return ERR_EMPTY_DOMAIN_IN_ATTR;
	}
	if(second_slash-first_slash-1 == 0)
	{
		// empty family
		cerr << "getDomainFamMembName(" << attr_name << "): empty family" << endl;
		return ERR_EMPTY_FAMILY_IN_ATTR;
	}
	if(third_slash-second_slash-1 == 0)
	{
		// empty member
		cerr << "getDomainFamMembName(" << attr_name << "): empty member" << endl;
		return ERR_EMPTY_MEMBER_IN_ATTR;
	}
	if(third_slash+1 == attr_name.length())
	{
		// empty atribute name
		cerr << "getDomainFamMembName(" << attr_name << "): empty attribute name" << endl;
		return ERR_EMPTY_ATTR_NAME_IN_ATTR;
	}
	domain = attr_name.substr(0,first_slash);
	family = attr_name.substr(first_slash+1,second_slash-first_slash-1);
	member = attr_name.substr(second_slash+1,third_slash-second_slash-1);
	name = attr_name.substr(third_slash+1);
	return 0;
}
//=============================================================================
//=============================================================================
string HdbPPCassandra::remove_domain(string str)
{
	string::size_type	end1 = str.find(".");
	if (end1 == string::npos)
	{
		return str;
	}
	else
	{
		string::size_type	start = str.find("tango://");
		if (start == string::npos)
		{
			start = 0;
		}
		else
		{
			start = 8;	//tango:// len
		}
		string::size_type	end2 = str.find(":", start);
		if(end1 > end2)	//'.' not in the tango host part
			return str;
		string th = str.substr(0, end1);
		th += str.substr(end2, str.size()-end2);
		return th;
	}
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::add_domain(string str)
{
	string::size_type	end1 = str.find(".");
	if (end1 == string::npos)
	{
		//get host name without tango://
		string::size_type	start = str.find("tango://");
		if (start == string::npos)
		{
			start = 0;
		}
		else
		{
			start = 8;	//tango:// len
		}
		string::size_type	end2 = str.find(":", start);

		string th = str.substr(start, end2);
		string with_domain = str;;
		struct addrinfo hints;
//		hints.ai_family = AF_INET; // use AF_INET6 to force IPv6
//		hints.ai_flags = AI_CANONNAME|AI_CANONIDN;
		memset(&hints, 0, sizeof hints);
		hints.ai_family = AF_UNSPEC; /*either IPV4 or IPV6*/
		hints.ai_socktype = SOCK_STREAM;
		hints.ai_flags = AI_CANONNAME;
		struct addrinfo *result, *rp;
		int ret = getaddrinfo(th.c_str(), NULL, &hints, &result);
		if (ret != 0)
		{
			fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(ret));
			return str;
		}

		for (rp = result; rp != NULL; rp = rp->ai_next)
		{
			with_domain = string(rp->ai_canonname) + str.substr(end2);
#ifdef _CASS_LIB_DEBUG
			cout << __func__ <<": found domain -> " << with_domain<<endl;
#endif
		}
		freeaddrinfo(result); // all done with this structure
		return with_domain;
	}
	else
	{
		return str;
	}
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_data_type(int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/) const
{
	ostringstream data_type;
	if(format==Tango::SCALAR)
	{
		data_type << TYPE_SCALAR << "_";
	}
	else
	{
		data_type << TYPE_ARRAY << "_";
	}

	switch (type)
	{
		case Tango::DEV_BOOLEAN:
			data_type << TYPE_DEV_BOOLEAN << "_"; break;
		case Tango::DEV_UCHAR:
			data_type << TYPE_DEV_UCHAR << "_"; break;
		case Tango::DEV_SHORT:
			data_type << TYPE_DEV_SHORT << "_"; break;
		case Tango::DEV_USHORT:
			data_type << TYPE_DEV_USHORT << "_"; break;
		case Tango::DEV_LONG:
			data_type << TYPE_DEV_LONG << "_"; break;
		case Tango::DEV_ULONG:
			data_type << TYPE_DEV_ULONG << "_"; break;
		case Tango::DEV_LONG64:
			data_type << TYPE_DEV_LONG64 << "_"; break;
		case Tango::DEV_ULONG64:
			data_type << TYPE_DEV_ULONG64 << "_"; break;
		case Tango::DEV_FLOAT:
			data_type << TYPE_DEV_FLOAT << "_"; break;
		case Tango::DEV_DOUBLE:
			data_type << TYPE_DEV_DOUBLE << "_"; break;
		case Tango::DEV_STRING:
			data_type << TYPE_DEV_STRING << "_"; break;
		case Tango::DEV_STATE:
			data_type << TYPE_DEV_STATE << "_"; break;
		case Tango::DEV_ENCODED:
			data_type << TYPE_DEV_ENCODED << "_"; break;
		default:
			cerr << __func__ << "(" << type << ", ...): Type not supported (" << __FILE__ << ":" << __LINE__ << ")" << endl;
	}

	if(write_type==Tango::READ)
	{
		data_type << TYPE_RO;
	}
	else
	{
		data_type << TYPE_RW;
	}
	return data_type.str();
}

//=============================================================================
//=============================================================================
string HdbPPCassandra::get_table_name(int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/) const
{
	ostringstream table_name;
	table_name << "att_" << get_data_type(type,format,write_type);
	return table_name.str();
}

/*
 * insert_attr_conf(): Insert the provided attribute into the configuration table (add it)
 * 
 * @param facility: control system name (TANGO_HOST)
 * @param attr_name: attribute name with its device name like domain/family/member/name
 * @param data_type: attribute data type (e.g. scalar_devdouble_rw)
 * @returns 0 in case of success
 *          -1 in case of error during the query execution
 */
int HdbPPCassandra::insert_attr_conf(const string & facility, 
                                     const string & attr_name,
                                     const string & data_type,
									 CassUuid & uuid)
{
	ostringstream insert_str;
	insert_str << "INSERT INTO " << m_keyspace_name << "." << CONF_TABLE_NAME
	           << " ("  << CONF_COL_ID << "," << CONF_COL_FACILITY << "," << CONF_COL_NAME << "," << CONF_COL_TYPE << ")"
			   << " VALUES (?, ?, ?, ?)" << ends;

	CassStatement* att_conf_statement = NULL;
	CassFuture* att_conf_future = NULL;
	att_conf_statement = cass_statement_new(insert_str.str().c_str(), 4); // TODO Reuse prepared statement!
	cass_statement_set_consistency(att_conf_statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable?
	CassUuidGen* uuid_gen = cass_uuid_gen_new();
	cass_uuid_gen_time(uuid_gen,&uuid);
	cass_statement_bind_uuid(att_conf_statement, 0, uuid);
	cass_statement_bind_string(att_conf_statement, 1, facility.c_str());
	cass_statement_bind_string(att_conf_statement, 2, attr_name.c_str());
	cass_statement_bind_string(att_conf_statement, 3, data_type.c_str());
	
	att_conf_future = cass_session_execute(mp_session, att_conf_statement);
	cass_future_wait(att_conf_future);

	CassError rc = CASS_OK;
	rc = cass_future_error_code(att_conf_future);
	if(rc != CASS_OK)
		print_error(att_conf_future);

	cass_future_free(att_conf_future);
  	cass_statement_free(att_conf_statement);
	cass_uuid_gen_free(uuid_gen);

	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR executing query=" << insert_str.str() << endl;
		return -1;
	}
	return 0;
}

/**
 * insert_domain(): Insert a new domain into the domains table,
 * which is a table used to speed up browsing of attributes
 * 
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 * @return 0 in case of success
 *         -1 in case of error during the query execution
 **/
int HdbPPCassandra::insert_domain(const string & facility, const string & domain)
{
	ostringstream insert_domains_str;
	insert_domains_str << "INSERT INTO " << m_keyspace_name << "." << DOMAINS_TABLE_NAME
	                   << " ("  << DOMAINS_COL_FACILITY << "," << DOMAINS_COL_DOMAIN << ")"
			           << " VALUES (?,?)" << ends;
	CassStatement* domains_statement = NULL;
	CassFuture* domains_future = NULL;
	domains_statement = cass_statement_new(insert_domains_str.str().c_str(), 2); // TODO Reuse prepared statement?
	cass_statement_set_consistency(domains_statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable?
	cass_statement_bind_string(domains_statement, 0, facility.c_str());
	cass_statement_bind_string(domains_statement, 1, domain.c_str());
	
	domains_future = cass_session_execute(mp_session, domains_statement);
	cass_future_wait(domains_future);

	CassError rc = CASS_OK;
	rc = cass_future_error_code(domains_future);
	if(rc != CASS_OK)
		print_error(domains_future);

	cass_future_free(domains_future);
  	cass_statement_free(domains_statement);

	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR executing query=" << insert_domains_str.str() << endl;
		return -1;
	}
	return 0;
}

/**
 * insert_family(): Insert a new family into the families table, 
 * which is a table used to speed up browsing of attributes
 * 
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 * @param family: family name
 * @return 0 in case of success
 *         -1 in case of error during the query execution
 **/
int HdbPPCassandra::insert_family(const string & facility, const string & domain, const string & family)
{
	ostringstream insert_families_str;
	insert_families_str << "INSERT INTO " << m_keyspace_name << "." << FAMILIES_TABLE_NAME
	                   << " ("  << FAMILIES_COL_FACILITY << "," << FAMILIES_COL_DOMAIN << "," << FAMILIES_COL_FAMILY << ")"
			           << " VALUES (?,?,?)" << ends;
	CassStatement* families_statement = NULL;
	CassFuture* families_future = NULL;
	families_statement = cass_statement_new(insert_families_str.str().c_str(), 3); // TODO Reuse prepared statement?
	cass_statement_set_consistency(families_statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable?
	cass_statement_bind_string(families_statement, 0, facility.c_str());
	cass_statement_bind_string(families_statement, 1, domain.c_str());
	cass_statement_bind_string(families_statement, 2, family.c_str());
	
	families_future = cass_session_execute(mp_session, families_statement);
	cass_future_wait(families_future);

	CassError rc = CASS_OK;
	rc = cass_future_error_code(families_future);
	if(rc != CASS_OK)
		print_error(families_future);

	cass_future_free(families_future);
  	cass_statement_free(families_statement);

	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR executing query=" << insert_families_str.str() << endl;
		return -1;
	}
	return 0;
}

/**
 * insert_member(): Insert a new member into the members table, 
 * which is a table used to speed up browsing of attributes
 * 
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 * @param family: family name
 * @param member: member name
 * @return 0 in case of success
 *         -1 in case of error during the query execution
 **/
int HdbPPCassandra::insert_member(const string & facility, const string & domain, const string & family, const string & member)
{
	ostringstream insert_members_str;
	insert_members_str << "INSERT INTO " << m_keyspace_name << "." << MEMBERS_TABLE_NAME
	                   << " ("  << MEMBERS_COL_FACILITY << "," << MEMBERS_COL_DOMAIN << "," 
					   << MEMBERS_COL_FAMILY << "," << MEMBERS_COL_MEMBER << ")"
			           << " VALUES (?,?,?,?)" << ends;
	CassStatement* members_statement = NULL;
	CassFuture* members_future = NULL;
	members_statement = cass_statement_new(insert_members_str.str().c_str(), 4); // TODO Reuse prepared statement?
	cass_statement_set_consistency(members_statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable?
	cass_statement_bind_string(members_statement, 0, facility.c_str());
	cass_statement_bind_string(members_statement, 1, domain.c_str());
	cass_statement_bind_string(members_statement, 2, family.c_str());
	cass_statement_bind_string(members_statement, 3, member.c_str());
	
	members_future = cass_session_execute(mp_session, members_statement);
	cass_future_wait(members_future);

	CassError rc = CASS_OK;
	rc = cass_future_error_code(members_future);
	if(rc != CASS_OK)
		print_error(members_future);

	cass_future_free(members_future);
  	cass_statement_free(members_statement);

	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR executing query=" << insert_members_str.str() << endl;
		return -1;
	}
	return 0;
}

/**
 * insert_attr_name(): Insert a new attribute name into the attribute names table, 
 * which is a table used to speed up browsing of attributes
 * 
 * @param facility: control system name (TANGO_HOST)
 * @param domain: domain name
 * @param family: family name
 * @param member: member name
 * @param name: attribute name
 * @return 0 in case of success
 *         -1 in case of error during the query execution
 **/
int HdbPPCassandra::insert_attr_name(const string & facility, const string & domain, const string & family, 
                      const string & member, const string & attribute_name)
{
	ostringstream insert_att_names_str;
	insert_att_names_str << "INSERT INTO " << m_keyspace_name << "." << ATT_NAMES_TABLE_NAME
	                   << " ("  << ATT_NAMES_COL_FACILITY << "," << ATT_NAMES_COL_DOMAIN << "," 
					   << ATT_NAMES_COL_FAMILY << "," << ATT_NAMES_COL_MEMBER << ","
					   << ATT_NAMES_COL_NAME << ")"
			           << " VALUES (?,?,?,?,?)" << ends;
	CassStatement* att_names_statement = NULL;
	CassFuture* att_names_future = NULL;
	att_names_statement = cass_statement_new(insert_att_names_str.str().c_str(), 5); // TODO Reuse prepared statement?
	cass_statement_set_consistency(att_names_statement, CASS_CONSISTENCY_LOCAL_QUORUM); // TODO: Make the consistency tunable?
	cass_statement_bind_string(att_names_statement, 0, facility.c_str());
	cass_statement_bind_string(att_names_statement, 1, domain.c_str());
	cass_statement_bind_string(att_names_statement, 2, family.c_str());
	cass_statement_bind_string(att_names_statement, 3, member.c_str());
	cass_statement_bind_string(att_names_statement, 4, attribute_name.c_str());

	att_names_future = cass_session_execute(mp_session, att_names_statement);
	cass_future_wait(att_names_future);

	CassError rc = CASS_OK;
	rc = cass_future_error_code(att_names_future);
	if(rc != CASS_OK)
		print_error(att_names_future);

	cass_future_free(att_names_future);
  	cass_statement_free(att_names_statement);

	if(rc != CASS_OK)
	{
		cerr << __func__ << ": ERROR executing query=" << insert_att_names_str.str() << endl;
		return -1;
	}
	return 0;		  
}

//=============================================================================
//=============================================================================
AbstractDB* HdbPPCassandraFactory::create_db(string host, string user, string password, string dbname, int port)
{
	return new HdbPPCassandra(host, user, password, dbname, port);
}

//=============================================================================
//=============================================================================
DBFactory *HdbClient::getDBFactory()
{
	HdbPPCassandraFactory *db_mysql_factory = new HdbPPCassandraFactory();
	return static_cast<DBFactory*>(db_mysql_factory);//TODO
}

