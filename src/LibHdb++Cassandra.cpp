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
#ifdef _CASS_LIB_DEBUG
  	cout << __func__<<": VERSION: " << version_string << " file:" << __FILE__rev << endl;
#endif
	cass_cluster_set_contact_points(mp_cluster,contact_points.c_str());
	
	CassError rc = CASS_OK;
	rc = connect_session();
	if(rc != CASS_OK) 
	{
		cout << __func__<< ": Cassandra connect session error"<< endl;
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
}

void HdbPPCassandra::print_error(CassFuture* future) 
{
	CassString message = cass_future_error_message(future);
	fprintf(stderr, "Cassandra Error: %.*s\n", (int)message.length, message.data);
}

CassError HdbPPCassandra::connect_session() 
{
	CassError rc = CASS_OK;
	CassFuture* future = cass_cluster_connect(mp_cluster);
	mp_session = NULL;
	
	cass_future_wait(future);
	rc = cass_future_error_code(future);
	if(rc != CASS_OK) 
	{
    	print_error(future);
	}
	else 
	{
		mp_session = cass_future_get_session(future);
  	}
	cass_future_free(future);
	return rc;
}

CassError HdbPPCassandra::execute_query(const char* query) 
{
	CassError rc = CASS_OK;
	CassFuture* future = NULL;
	CassStatement* statement = cass_statement_new(cass_string_init(query), 0);

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
	map<string,UuidWrapper>::iterator it = attr_ID_map.find(fqdn_attr_name);
	if(it == attr_ID_map.end())
	{
		// if not already present in cache, look for ID in the DB*/
		if(find_attr_id_in_db(fqdn_attr_name, ID) != 0)
		{
			cout << "(" << __FILE__ << "," << __LINE__ << ") "<< __func__<< ": ID not found for attr="<< fqdn_attr_name << endl;
			return false;
		}
	}
	else
	{
		memcpy(ID,it->second.getUuid(),sizeof(CassUuid));
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
			" WHERE " << CONF_COL_NAME << " = ? AND " << CONF_COL_FACILITY << " = ?";
	
	CassError rc = CASS_OK;
	CassStatement* statement = NULL;
	CassFuture* future = NULL;
	CassString query = cass_string_init(query_str.str().c_str());
	
	statement = cass_statement_new(query, 2);
	cass_statement_bind_string(statement, 0, cass_string_init(attr_name.c_str()));
	cass_statement_bind_string(statement, 1, cass_string_init(facility.c_str()));
	
	future = cass_session_execute(mp_session, statement);
	cass_future_wait(future);

	rc = cass_future_error_code(future);
	bool found = false;
	if(rc != CASS_OK) 
	{
		cout << __func__ << ": ERROR in query=" << query_str.str() << endl;
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
			cass_value_get_uuid(cass_row_get_column(row, 0), ID);
#ifdef _CASS_LIB_DEBUG		
			cout << __func__<< "(" << fqdn_attr_name << "): ID = " << ID << endl;
#endif
			found = true;
			UuidWrapper IDWrap = ID;
			attr_ID_map.insert(make_pair(fqdn_attr_name,IDWrap));
			map<string,UuidWrapper>::iterator it = attr_ID_map.find(fqdn_attr_name);
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
		cout << __func__<< "(" << fqdn_attr_name << "): NO RESULT in query: " << query_str.str() << endl;
		return -1;
	}
	return 0;
}

int HdbPPCassandra::find_attr_id_type(string facility, string attr, CassUuid & ID, string attr_type)
{
	ostringstream query_str;
	//string facility_no_domain = remove_domain(facility);
	//string facility_with_domain = add_domain(facility);
	
	query_str << "SELECT " << CONF_COL_ID << "," << CONF_COL_TYPE << " FROM " << m_keyspace_name << "." << CONF_TABLE_NAME <<
			" WHERE " << CONF_COL_NAME << " = ? AND " << CONF_COL_FACILITY << " = ?";
	
	CassError rc = CASS_OK;
	CassStatement* statement = NULL;
	CassFuture* future = NULL;
	CassString query = cass_string_init(query_str.str().c_str());
	
	statement = cass_statement_new(query, 2);
	cass_statement_bind_string(statement, 0, cass_string_init(attr.c_str()));
	cass_statement_bind_string(statement, 1, cass_string_init(facility.c_str()));
	
	future = cass_session_execute(mp_session, statement);
	cass_future_wait(future);

	rc = cass_future_error_code(future);
	bool found = false;
	CassString db_type_res = cass_string_init("");
	char db_type_buffer[256];
	if(rc != CASS_OK) 
	{
		cout<< __func__ << ": ERROR in query=" << query_str.str() << endl;
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
			cass_value_get_uuid(cass_row_get_column(row, 1), ID);
			cass_value_get_string(cass_row_get_column(row, 2), &db_type_res);
			memcpy(db_type_buffer, db_type_res.data, db_type_res.length);
      		db_type_buffer[db_type_res.length] = '\0';
			found = true;
    	}
		cass_result_free(result);
		cass_iterator_free(iterator);
	}
	
	cass_future_free(future);
	cass_statement_free(statement);
	if(!found)
	{
		cout << __func__<< ": NO RESULT in query: " << query_str.str() << endl;
		return -1;
	}
	string db_type = db_type_buffer;
	if(db_type != attr_type)
	{
		cout << __func__<< ": FOUND ID="<<ID<<" but different type: attr_type="<<attr_type<<"-db_type="<<db_type_buffer << endl;
		return -2;
	}
	else
	{
		cout << __func__<< ": FOUND ID="<<ID<<" with SAME type: attr_type="<<attr_type<<"-db_type="<<db_type << endl;
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
			cass_statement_bind_string(statement, param_index, cass_string_init(val[0].c_str()));
		}
		else
		{
			// Store the array into a CQL list
			CassCollection* readValuesList = NULL;
			readValuesList = cass_collection_new(CASS_COLLECTION_TYPE_LIST, val.size());
			for(unsigned int i = 0; i < val.size(); i++) 
			{
				cass_collection_append_string(readValuesList, cass_string_init(val[i].c_str()));
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
		cout << __func__ << "extract_read failed for attribute "<< data->attr_name << "! (" << __FILE__ << ":" << __LINE__ << ")" << endl;
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
	cout << __func__<< ": DevEncoded type is not yet supported..." << endl;
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
		// There is a read value
		if(isNull)
			cass_statement_bind_null(statement,param_index); // value_r
		else
		{
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
					// TODO
				}
			} // switch(data_type)
		}
		param_index++;
	}
	if(write_type != Tango::READ)
	{
		// There is a write value
		if(isNull)
			cass_statement_bind_null(statement,param_index); // value_w
		else
		{
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
					// TODO
				}
			} // switch(data_type)
		}
		param_index++;
	}
}

int HdbPPCassandra::insert_Attr(Tango::EventData *data, HdbEventDataType ev_data_type)
{
#ifdef _CASS_LIB_DEBUG
//	cout << __func__<< ": entering..." << endl;
#endif

	CassStatement* statement = NULL;
	CassFuture* future = NULL;

	try
	{
		string fqdn_attr_name = data->attr_name;
		int64_t	ev_time;
		int ev_time_us;
		int64_t rcv_time = ((int64_t) data->get_date().tv_sec) * 1000;
		int rcv_time_us = data->get_date().tv_usec;

		Tango::AttributeDimension attr_w_dim;
		Tango::AttributeDimension attr_r_dim;
		int data_type = ev_data_type.data_type;
		Tango::AttrDataFormat data_format = ev_data_type.data_format;
		int write_type = ev_data_type.write_type;
		//int max_dim_x = ev_data_type.max_dim_x;
		//int max_dim_y = ev_data_type.max_dim_y;

		bool isNull = false;
		if(data->err)
		{
			// TODO Store the error description
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": going to archive as NULL... (Attribute in error)" << endl;
#endif
			isNull = true;
		}
		data->attr_value->reset_exceptions(Tango::DeviceAttribute::isempty_flag);
		if(data->attr_value->is_empty())
		{
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": going to archive as NULL... (Attr Value is empty)" << endl;
#endif
			isNull = true;
		}
		if(data->attr_value->get_quality() == Tango::ATTR_INVALID)
		{
			// TODO Store attribute quality
#ifdef _CASS_LIB_DEBUG
			cout << __func__<< ": going to archive as NULL... (Invalid Attribute)" << endl;
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
			attr_r_dim.dim_x = 1;//max_dim_x;//TODO: OK?
			attr_w_dim.dim_x = 0;//max_dim_x;//TODO: OK?
			attr_r_dim.dim_y = 0;//max_dim_y;//TODO: OK?
			attr_w_dim.dim_y = 0;//max_dim_y;//TODO: OK?
			ev_time = rcv_time;		//TODO: OK?
			ev_time_us = rcv_time_us;
		}
		
		CassUuid ID;
		string facility = get_only_tango_host(fqdn_attr_name);
		string attr_name = get_only_attr_name(fqdn_attr_name);
		facility = add_domain(facility);
		if(!find_attr_id(fqdn_attr_name, ID))
		{
			cout << __func__<< ": Could not find ID for attribute " << fqdn_attr_name << endl;
			return -1;
		}
	
		int nbQueryParams = 0;
		string query_str = get_insert_query_str(data_type,data_format,write_type, nbQueryParams);
	
		CassError rc = CASS_OK;
		CassString query = cass_string_init(query_str.c_str());
		
		statement = cass_statement_new(query, nbQueryParams);
		cass_statement_bind_uuid(statement, 0, ID); // att_conf_id
		// Compute the period based on the month of the event time
		struct tm *tms; 
		time_t ev_time_s = ev_time / 1000;
		if ((tms = localtime (&ev_time_s)) == NULL)
			perror ("localtime");
		char period[8];
		snprintf(period,8,"%04d-%02d", tms -> tm_year + 1900,tms -> tm_mon + 1);
		cass_statement_bind_string(statement, 1, cass_string_init(period)); // period 
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
		int param_index = 8;
		
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
		cout << "Exception on " << data->attr_name << ":" << endl;
		
		for (unsigned int i=0; i<e.errors.length(); i++)
		{
			cout << e.errors[i].reason << endl;
			cout << e.errors[i].desc << endl;
			cout << e.errors[i].origin << endl;
		}
 
		cout << endl;
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
	
	CassString history_query = cass_string_init(insert_event_str.str().c_str());
	CassStatement* statement = NULL;
	CassFuture* future = NULL;
	statement = cass_statement_new(history_query, 4);
	cass_statement_bind_uuid(statement, 0, att_conf_id);
	cass_statement_bind_string(statement, 1, cass_string_init(history_event_name.c_str()));
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
		cout<< __func__ << ": ERROR executing query=" << insert_event_str.str() << endl;
		return false;
	}

	return true;
}

int HdbPPCassandra::configure_Attr(string name, int type/*DEV_DOUBLE, DEV_STRING, ..*/, int format/*SCALAR, SPECTRUM, ..*/, int write_type/*READ, READ_WRITE, ..*/)
{
	ostringstream insert_str;
	ostringstream insert_event_str;
	string facility = get_only_tango_host(name);
	facility = add_domain(facility);
	string attr_name = get_only_attr_name(name);
#ifdef _CASS_LIB_DEBUG
	cout<< __func__ << ": name="<<name<<" -> facility="<<facility<<" attr_name="<<attr_name<< endl;
#endif
	CassUuid id;
	string data_type = get_data_type(type, format, write_type);
	int ret = find_attr_id_type(facility, attr_name, id, data_type);
	//ID already present but different configuration (attribute type)
	if(ret == -2)
	{
		cout<< __func__ << ": ERROR "<<facility<<"/"<<attr_name<<" already configured with ID="<<id << endl;
		return -1;
	}

	//ID found and same configuration (attribute type): do nothing
	if(ret == 0)
	{
		cout<< __func__ << ": ALREADY CONFIGURED with same configuration: "<<facility<<"/"<<attr_name<<" with ID="<<id << endl;
		return 0;
	}

	//add domain name to fqdn
	name = string("tango://")+facility+string("/")+attr_name;
	insert_str << "INSERT INTO " << m_keyspace_name << "." << CONF_TABLE_NAME 
	           << " ("  << CONF_COL_ID << "," << CONF_COL_FACILITY << "," << CONF_COL_NAME << "," << CONF_COL_TYPE << ")" 
			   << " VALUES (?, ?, ?, ?)" << ends;
	
	CassString query = cass_string_init(insert_str.str().c_str());
	CassStatement* statement = NULL;
	CassFuture* future = NULL;
	statement = cass_statement_new(query, 4);
	CassUuid uuid;
	cass_uuid_generate_time(uuid);
	cass_statement_bind_uuid(statement, 0, uuid);
	cass_statement_bind_string(statement, 1, cass_string_init(facility.c_str()));
	cass_statement_bind_string(statement, 2, cass_string_init(attr_name.c_str()));
	cass_statement_bind_string(statement, 3, cass_string_init(data_type.c_str()));
	
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
		cout<< __func__ << ": ERROR executing query=" << insert_str.str() << endl;
		return -1;
	}
	
	if(!insert_history_event(EVENT_ADD, uuid))
	{
		cout << __func__ << "Error adding ADD event to history table for attribute " << name 
		     << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
		return -2;
	}
	
	return 0;
}

int HdbPPCassandra::remove_Attr(string name)
{
	//TODO: implement
	return 0;
}

int HdbPPCassandra::start_Attr(string fqdn_attr_name)
{
	ostringstream insert_event_str;

	CassUuid uuid;
	int ret = find_attr_id(fqdn_attr_name, uuid);
	if(ret < 0)
	{
		cout<< __func__ << ": ERROR "<< fqdn_attr_name<<" NOT FOUND" << endl;
		return -1;
	}
	
	if(!insert_history_event(EVENT_START, uuid))
	{
		cout << __func__ << "Error adding START event to history table for attribute " << fqdn_attr_name 
		     << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
		return -2;
	}

	return 0;
}

int HdbPPCassandra::stop_Attr(string fqdn_attr_name)
{
	ostringstream insert_event_str;
	CassUuid uuid;
	int ret = find_attr_id(fqdn_attr_name, uuid);
	if(ret < 0)
	{
		cout<< __func__ << ": ERROR "<< fqdn_attr_name <<" NOT FOUND" << endl;
		return -1;
	}
	
	if(!insert_history_event(EVENT_STOP, uuid))
	{
		cout << __func__ << "Error adding STOP event to history table for attribute " << fqdn_attr_name 
		     << " (" << __FILE__ << ":" << __LINE__ << ")" << endl;
		return -2;
	}
	return 0;
}

string HdbPPCassandra::get_insert_query_str(int tango_data_type /*DEV_DOUBLE, DEV_STRING, ..*/,
                                            int data_format /* SCALAR, SPECTRUM */,
                                            int write_type/*READ, READ_WRITE, ..*/,
											int & nbQueryParams) const
{
	string table_name = get_table_name(tango_data_type, data_format, write_type);
	ostringstream query_str;
	nbQueryParams = 8;
	query_str << "INSERT INTO " << m_keyspace_name << "." << table_name 
	          << " (" 
	          << SC_COL_ID << ","  
			  << SC_COL_PERIOD << ","  
			  << SC_COL_EV_TIME << "," 
	          << SC_COL_EV_TIME_US  << "," 
	          << SC_COL_RCV_TIME << "," 
	          << SC_COL_RCV_TIME_US << "," 
			  << SC_COL_INS_TIME << "," 
			  << SC_COL_INS_TIME_US;
	          
			
	if(write_type != Tango::WRITE) // RO or RW
		query_str << "," << SC_COL_VALUE_R;

	if(write_type != Tango::READ)	// RW or WO
		query_str << "," << SC_COL_VALUE_W;

	query_str << ") VALUES (?,?,?,?,?,?,?,?";
	
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

	query_str << ")";

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
			cout << __func__ <<": found domain -> " << with_domain<<endl;
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
			cout << __func__ << "(" << type << ", ...): Type not supported (" << __FILE__ << ":" << __LINE__ << ")" << endl;
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

