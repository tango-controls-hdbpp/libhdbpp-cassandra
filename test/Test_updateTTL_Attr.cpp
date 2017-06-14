#include "LibHdb++Cassandra.h"
#include <iostream>

using namespace HDBPP;
using namespace std;

int main(int argc, char ** argv)
{

    string attr_name = "sr/d-halo/id7/halo";
    string facility="orion.esrf.fr:10000";
    string typestr = "scalar_devdouble_ro";

    if(argc == 2)
    {
        attr_name = argv[1];
    }
    if(argc == 3)
    {
        facility = argv[1];
        attr_name = argv[2];
    }
    if(argc == 4)
    {
        facility = argv[1];
        attr_name = argv[2];
        typestr = argv[3];
    }
	
	vector<string> lib_config;
	lib_config.push_back("contact_points=cassandra2");
	lib_config.push_back("keyspace=hdbtest");
    HdbPPCassandra myDB = HdbPPCassandra(lib_config);

	try
	{
	    CassUuid ID;
		unsigned int ttl_at_start = 0;
		unsigned int ttl_at_end = 0;
		
		string fqdn_attr_name = "tango://" + facility + "/" + attr_name;

	    cout << "attr name = " << fqdn_attr_name << endl;
	    int err = myDB.find_attr_id_and_ttl_in_db(fqdn_attr_name, ID, ttl_at_start);

		cout << "find_attr_id_and_ttl_in_db(" << fqdn_attr_name << ") returned " << err << endl;
		/*if(err != 0)
			return err;*/
		cout << "ttl = " << ttl_at_start << endl;
	
		// Update TTL
		int err_update_ttl = myDB.updateTTL_Attr(fqdn_attr_name,ttl_at_start + 1);
	
		cout << "updateTTL_Attr(" << fqdn_attr_name << "," <<  (ttl_at_start + 1) 
    	     << ") returned " << err_update_ttl << endl;

		if(err_update_ttl != 0)
				return err_update_ttl;
 		err = myDB.find_attr_id_and_ttl_in_db(fqdn_attr_name, ID, ttl_at_end);

		cout << "find_attr_id_and_ttl_in_db(" << fqdn_attr_name << ") returned " << err << endl;
		if(err != 0)
			return err;
		cout << "ttl = " << ttl_at_end << endl;

		assert(ttl_at_end == (ttl_at_start + 1));
	}
	catch(Tango::DevFailed e)
	{
		Tango::Except::print_exception(e);
		return -2;
	}
    return 0;
}
