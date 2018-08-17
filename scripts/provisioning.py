#!/usr/bin/env python
from __future__ import print_function
import sys
import argparse
import os
from cassandra import ConsistencyLevel
from cassandra.cluster import Cluster, ExecutionProfile

version_major = 0
version_minor = 1
verbose = False

access_map = {
    "rw": "ReadWrite",
    "ro": "ReadOnly"}

# This lookup table maps a tango type to a cassandra data type
data_type_map = {
    "scalar": {
        "boolean": "boolean",
        "uchar": "int",
        "short": "int",
        "ushort": "int",
        "long": "int",
        "ulong": "bigint",
        "long64": "bigint",
        "ulong64": "bigint",
        "float": "float",
        "double": "double",
        "state": "int",
        "string": "text",
        "encoded": "frozen<devencoded>"},
    "array": {
        "boolean": "boolean",
        "uchar": "int",
        "short": "int",
        "ushort": "int",
        "long": "int",
        "ulong": "bigint",
        "long64": "bigint",
        "ulong64": "bigint",
        "float": "float",
        "double": "double",
        "state": "int",
        "string": "text",
        "encoded": "frozen<devencoded>"}}


def create_table_string(keyspace, storage_type, data_type, access_type):
    """ With the given parameters, construct a string to create the table for the data_type.
        The string mimics those found in create_hdb_cassandra.cql

    Arguments:
        keyspace {string} -- Keyspace to hold the table
        storage_type {string} -- Type - scalar or array
        data_type {string} -- Tango type to map from
        access_type {string} -- Read/Write access type

    Returns:
        string -- The completed string to use as a query
    """

    value_string = "value_r " + data_type_map[storage_type][data_type] + ","

    if access_type == "rw":
        value_string += "value_w " + data_type_map[storage_type][data_type] + ","

    data_type_cap = data_type.capitalize()

    if data_type_cap[0] == "U":
        data_type_cap = "U" + data_type_cap[1].capitalize() + data_type_cap[2:]

    create_str = "CREATE TABLE IF NOT EXISTS " + keyspace + ".att_" + storage_type + "_dev" + data_type + "_" + access_type + "(" + \
        "att_conf_id timeuuid,period text,data_time timestamp,data_time_us int,recv_time timestamp,recv_time_us int," + \
        "insert_time timestamp,insert_time_us int," + value_string + "quality int,error_desc text," + \
        "PRIMARY KEY ((att_conf_id ,period),data_time,data_time_us)) " + \
        "WITH comment='" + storage_type.capitalize() + " Dev" + data_type_cap + " " + access_map[access_type] + " Values Table'"

    return create_str


def create_scalar_table(session, keyspace, data_type, access_type):
    session.execute(create_table_string(keyspace, "scalar", data_type, access_type))


def create_spectrum_table(session, keyspace, data_type, access_type):
    session.execute(create_table_string(keyspace, "array", data_type, access_type))


def drop_tables(ip, keyspace):
    print("Attempt to drop the keyspace:", keyspace, "on cluster at:", ip)

    cluster = Cluster(ip)
    session = cluster.connect()

    if verbose:
        print("Created cassandra connection on:", ip)

    # drop the entire keyspace
    session.execute("DROP KEYSPACE IF EXISTS " + keyspace)

    session.shutdown()
    cluster.shutdown()

    if verbose:
        print("Closed connection to cluster at:", ip)


def create_tables(ip, keyspace, strategy, dc, replication):
    print("Attempt to provision the cassandra cluster at:", ip, "using keyspace:", keyspace,
          "and strategy:", strategy, "and replication:", replication)

    cluster = Cluster(ip)
    session = cluster.connect()

    if verbose:
        print("Created cassandra connection on:", ip)

    if verbose:
        print("Creating keyspace:", keyspace, "with strategy:", strategy, "dc:", dc, "replication:", replication)

    if strategy == "SimpleStrategy":
        session.execute("CREATE KEYSPACE IF NOT EXISTS " +
                        keyspace + " WITH REPLICATION = { 'class' : \'" + strategy + "\', \'replication_factor\' : " + replication + "}")

    else:
        session.execute("CREATE KEYSPACE IF NOT EXISTS " +
                        keyspace + " WITH REPLICATION = { 'class' : \'" + strategy + "\', \'" + dc + "\' : " + replication + "}")

    session.execute("CREATE TYPE IF NOT EXISTS " +
                    keyspace + ".devencoded (encoded_format text, encoded_data blob)")

    if verbose:
        print("Creating att_conf table")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".att_conf "
                    "(cs_name text,att_name text,att_conf_id timeuuid,data_type text,ttl int,PRIMARY KEY (cs_name, att_name)) "
                    "WITH comment='Attribute Configuration Table' "
                    "AND caching = {'keys' : 'NONE', 'rows_per_partition': 'ALL' }")

    if verbose:
        print("Creating indexes")

    session.execute("CREATE INDEX IF NOT EXISTS ON " + keyspace + ".att_conf(data_type)")
    session.execute("CREATE INDEX IF NOT EXISTS ON " + keyspace + ".att_conf(att_conf_id)")

    if verbose:
        print("Creating att_history table")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".att_history"
                    "(att_conf_id timeuuid,time timestamp,time_us int,event text,"
                    "PRIMARY KEY(att_conf_id, time, time_us)) "
                    "WITH comment='Attribute Configuration Events History Table'")

    if verbose:
        print("Creating attribute tables")

    # Create all the attribute type tables
    create_scalar_table(session, keyspace, "boolean", "ro")
    create_scalar_table(session, keyspace, "boolean", "rw")
    create_scalar_table(session, keyspace, "uchar", "ro")
    create_scalar_table(session, keyspace, "uchar", "rw")
    create_scalar_table(session, keyspace, "short", "ro")
    create_scalar_table(session, keyspace, "short", "rw")
    create_scalar_table(session, keyspace, "ushort", "ro")
    create_scalar_table(session, keyspace, "ushort", "rw")
    create_scalar_table(session, keyspace, "long", "ro")
    create_scalar_table(session, keyspace, "long", "rw")
    create_scalar_table(session, keyspace, "ulong", "ro")
    create_scalar_table(session, keyspace, "ulong", "rw")
    create_scalar_table(session, keyspace, "long64", "ro")
    create_scalar_table(session, keyspace, "long64", "rw")
    create_scalar_table(session, keyspace, "ulong64", "ro")
    create_scalar_table(session, keyspace, "ulong64", "rw")
    create_scalar_table(session, keyspace, "float", "ro")
    create_scalar_table(session, keyspace, "float", "rw")
    create_scalar_table(session, keyspace, "double", "ro")
    create_scalar_table(session, keyspace, "double", "rw")
    create_scalar_table(session, keyspace, "string", "ro")
    create_scalar_table(session, keyspace, "string", "rw")
    create_scalar_table(session, keyspace, "state", "ro")
    create_scalar_table(session, keyspace, "state", "rw")
    create_scalar_table(session, keyspace, "encoded", "ro")
    create_scalar_table(session, keyspace, "encoded", "rw")

    create_spectrum_table(session, keyspace, "boolean", "ro")
    create_spectrum_table(session, keyspace, "boolean", "rw")
    create_spectrum_table(session, keyspace, "uchar", "ro")
    create_spectrum_table(session, keyspace, "uchar", "rw")
    create_spectrum_table(session, keyspace, "short", "ro")
    create_spectrum_table(session, keyspace, "short", "rw")
    create_spectrum_table(session, keyspace, "ushort", "ro")
    create_spectrum_table(session, keyspace, "ushort", "rw")
    create_spectrum_table(session, keyspace, "long", "ro")
    create_spectrum_table(session, keyspace, "long", "rw")
    create_spectrum_table(session, keyspace, "ulong", "ro")
    create_spectrum_table(session, keyspace, "ulong", "rw")
    create_spectrum_table(session, keyspace, "long64", "ro")
    create_spectrum_table(session, keyspace, "long64", "rw")
    create_spectrum_table(session, keyspace, "ulong64", "ro")
    create_spectrum_table(session, keyspace, "ulong64", "rw")
    create_spectrum_table(session, keyspace, "float", "ro")
    create_spectrum_table(session, keyspace, "float", "rw")
    create_spectrum_table(session, keyspace, "double", "ro")
    create_spectrum_table(session, keyspace, "double", "rw")
    create_spectrum_table(session, keyspace, "string", "ro")
    create_spectrum_table(session, keyspace, "string", "rw")
    create_spectrum_table(session, keyspace, "state", "ro")
    create_spectrum_table(session, keyspace, "state", "rw")
    create_spectrum_table(session, keyspace, "encoded", "ro")
    create_spectrum_table(session, keyspace, "encoded", "rw")

    if verbose:
        print("Creating att_parameter table")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".att_parameter ("
                    "att_conf_id timeuuid,recv_time timestamp,recv_time_us int,insert_time timestamp,insert_time_us int,"
                    "label text,unit text,standard_unit text,display_unit text,format text,archive_rel_change text,"
                    "archive_abs_change text,archive_period text,description text,"
                    "PRIMARY KEY((att_conf_id), recv_time, recv_time_us)) WITH COMMENT='Attribute configuration parameters'")

    if verbose:
        print("Creating browse tables")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".domains ("
                    "cs_name text,domain text,PRIMARY KEY (cs_name, domain)) "
                    "WITH CLUSTERING ORDER BY (domain ASC) AND comment='Domains Table' AND caching = {'keys' : 'NONE', 'rows_per_partition': 'ALL' }")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".families ("
                    "cs_name text,domain text,family text,PRIMARY KEY ((cs_name, domain),family)) "
                    "WITH CLUSTERING ORDER BY (family ASC) AND comment='Families Table' AND caching = {'keys' : 'NONE', 'rows_per_partition': 'ALL' }")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".members ("
                    "cs_name text,domain text,family text,member text,PRIMARY KEY ((cs_name, domain,family),member)) "
                    "WITH CLUSTERING ORDER BY (member ASC) AND comment='Members Table' AND caching = {'keys' : 'NONE', 'rows_per_partition': 'ALL' }")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".att_names ("
                    "cs_name text,domain text,family text,member text,name text,PRIMARY KEY ((cs_name, domain,family,member),name)) "
                    "WITH CLUSTERING ORDER BY (name ASC) AND comment='Attributes Names Table' AND caching = {'keys' : 'NONE', 'rows_per_partition': 'ALL' }")

    if verbose:
        print("Creating time_stats table")

    session.execute("CREATE TABLE IF NOT EXISTS " + keyspace + ".time_stats ("
                    "att_conf_id timeuuid,period text,data_time timestamp,data_time_us int,recv_time timestamp,"
                    "recv_time_us int,insert_time timestamp,insert_time_us int,PRIMARY KEY ((att_conf_id ,period),data_time,data_time_us)) "
                    "WITH comment='Time Statistics Table'")

    session.shutdown()
    cluster.shutdown()

    if verbose:
        print("Closed connection to cluster at:", ip)


def run_provisioning(args):
    """Start provisioning the cassandra cluster based on the various arguments

    Arguments:
        args {args} -- Command line arguments

    Returns:
        boolean -- True if successful, False otherwise
    """

    if args.CLUSTER_IP is None:
        print("A valid IP address must be passed")

    if args.drop_tables:
        return drop_tables(args.CLUSTER_IP, args.keyspace[0])

    elif args.create_tables:
        if args.simple_replication:
            dc = "replication_factor"
            replication = str(args.simple_replication[0])
            strategy = "SimpleStrategy"

        elif args.network_replication:
            dc = args.network_replication[0]
            replication = str(args.network_replication[1])
            strategy = "NetworkTopologyStrategy"

        return create_tables(args.CLUSTER_IP, args.keyspace, strategy, dc, replication)

    return False


def main():
    """ Main function to handle entry and script arguments

    Returns:
        bool -- False if the script hit an error, True otherwise.
    """

    version = str(version_major) + "." + str(version_minor)

    parser = argparse.ArgumentParser(description="Python script to provision the Cassandra Cluster. Current version " + version,
                                     epilog="Use with caution. Ensure no other cqsl is running and trying to create tables at the same time.")

    parser.add_argument("-v", "--verbose", action="store_true", help="verbose output")
    parser.add_argument("--keyspace", default="hdb", help="keyspace to use, default is hdb")
    parser.add_argument("CLUSTER_IP", nargs=1, help="IP address to connect to use to connect to the cluster")

    action_group = parser.add_mutually_exclusive_group()
    action_group.add_argument("--drop-tables", action="store_true", help="drop all tables in the cluster")
    action_group.add_argument("--create-tables", action="store_true", help="create all tables in the cluster")

    rep_group = parser.add_mutually_exclusive_group()
    rep_group.add_argument("--simple-replication", nargs=1, metavar=("REPLICATION"), help="Simple replication strategy")
    rep_group.add_argument("--network-replication", nargs=2, metavar=("DATACENTRE", "REPLICATION"), help="Network replication strategy")

    args = parser.parse_args() 

    if args.verbose:
        global verbose
        verbose = True

    return run_provisioning(args)


if __name__ == "__main__":
    if main() == False:
        sys.stdout.write("Command failed\n")
