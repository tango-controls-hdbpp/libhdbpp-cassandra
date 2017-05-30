#include "LibHdb++Cassandra.h"
#include <iostream>

using namespace HDBPP;
using namespace std;

int main(int argc, char ** argv)
{

    string attr_name = "elin/v-rga/4/mass44";
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

    CassUuid ID;
    unsigned int ttl;

    cout << "device name = " << attr_name << endl;
    int err = myDB.find_attr_id_type_and_ttl(facility, attr_name, ID, typestr, ttl);

    cout << "find_attr_id_type_and_ttl(" << facility << "," << attr_name << ") returned " << err << endl;
	if(!err)
	{
		cout << "ttl = " << ttl << endl;
	}

    cout << "Bye" << endl;

    return 0;


}
