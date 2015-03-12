#include "LibHdb++Cassandra.h"
#include <iostream>

using namespace HDBPP;
using namespace std;

int main(int argc, char ** argv)
{

    string device_name = "elin/v-rga/4/mass44";
    string facility="orion.esrf.fr:10000";
    string typestr = "scalar_devdouble_ro";

    if(argc == 2)
    {
        device_name = argv[1];
    }
    if(argc == 3)
    {
        facility = argv[1];
        device_name = argv[2];
    }
    if(argc == 4)
    {
        facility = argv[1];
        device_name = argv[2];
        typestr = argv[3];
    }
    HdbPPCassandra myDB = HdbPPCassandra("cassandra2,cassandra3","","","hdb",4658);
    CassError ret = myDB.connect_session();
	if(ret != CASS_OK)
	{
		cout << "Cassandra connect session error"<< endl;
		return -1;
  	}
    CassUuid ID;

    cout << "device name = " << device_name << endl;
    int err = myDB.find_attr_id_type(facility, device_name, ID, typestr);

    cout << "find_att_id_type(" << device_name << ") returned " << err << endl;
    cout << "Bye" << endl;

    return 0;


}
