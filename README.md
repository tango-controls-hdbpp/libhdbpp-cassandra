# libhdbpp-cassandra
[HDB++](http://www.tango-controls.org/community/projects/hdbplus) library for Apache Cassandra backend.

## Dependencies
To compile this library, you will need to download and install the [DataStax Cassandra C++ Driver](https://github.com/datastax/cpp-driver).

## How to build?

```bash
git clone --recursive http://github.com/ELETTRA-SincrotroneTrieste/libhdbpp-cassandra.git

cd libhdbpp-cassandra
export TANGO_DIR=/usr/local/tango-9.2.5a
export OMNIORB_DIR=/usr/local/omniorb-4.2.1
export ZMQ_DIR=/usr/local/zeromq-4.0.7
make
```

By default, the Makefile assumes the Cassandra C+ driver root directory is ../../cassandra/cppDriver/cpp-driver
You can override this default by setting CASS_CPP_DRIVER_DIR environment variable to the root directory where the Datastax driver is installed.
E.g.:
```bash
export CASS_CPP_DRIVER_DIR=~/cpp-driver
```

The datastax Cassandra C++ driver has a dependency with libuv library.
By default, libhdbpp-cassandra Makefile assumes this libuv library can be found under ${CASS_DRIVER_DIR}/lib/libuv.
You can override this default by setting LIBUV_ROOT_DIR environment variable to the root directory where libuv is installed.
E.g:
```bash
export LIBUV_ROOT_DIR=/usr/local/libuv
```

## Compatibility
This version was tested with Cassandra 2.2.4 and 2.2.6 and with the version 2.2.1 of the Datastax Cassandra C++ driver and with its dependency with libuv version 1.4.2.
This version is compatible with libhdb++ library version 4.

This version requires a schema having a ttl column in att_conf table.
If you need to upgrade from a previous version of the database schema, you can add this column simply by executing the following CQL commands using cqlsh for instance:

```SQL
USE hdb;
ALTER TABLE att_conf ADD ttl int;
```

