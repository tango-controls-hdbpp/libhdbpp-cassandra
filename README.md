# Libhdbpp-Cassandra

[HDB++](http://www.tango-controls.org/community/projects/hdbplus) library for Apache Cassandra backend. This library is loaded by [Libhdbpp](https://github.com/tango-controls-hdbpp/libhdbpp) to archive events from a Tango Controls system.

## Version

The current release version is 0.10.0.

### **Important Changes** 0.9.1 -> 0.10.0

This release version of the library has made a few changes to standardize its use and deployment.
* The build system has been moved to CMake. Details on building it using CMake are below.
* The include file LibHdb++.h has changed from `include "LibHdb++.h"` to `include "libhdb++/LibHdb++.h"`. This reflects the standard install path of the headers. If building against [libhdbpp](https://github.com/tango-controls-hdbpp/libhdbpp) from a standard system install, then this change makes no difference. If libhdbpp is installed to a custom location, ensure you add the path to the CMAKE_INCLUDE_PATH.

## Documentation

* See the tango documentation [here](http://tango-controls.readthedocs.io/en/latest/administration/services/hdbpp/index.html#hdb-an-archiving-historian-service) for broader information about the HB++ archiving system and its integration into Tango Controls
* Libhdbpp-Cassandra [CHANGELOG.md](https://github.com/tango-controls-hdbpp/libhdbpp/blob/master/CHANGELOG.md) contains the latest changes both released and in development.

## Bugs Reports

Please file bug reports above in the issues section.

## Building and Installation

To compile this library, you will need to download and install the [DataStax Cassandra C++ Driver](https://github.com/datastax/cpp-driver). The Datastax Cassandra C++ Driver depends on [Libuv](https://github.com/libuv/libuv) and this will need installing also.

See the [INSTALL.md](https://github.com/tango-controls-hdbpp/libhdbpp-cassandra/blob/master/INSTALL.md) file for  detailed instructions on how to build and install libhdbpp-cassandra.

## Running Tests

The library is now partially covered with unit tests built on the Catch Unit Test framework. These are still under active development. The unit test binary is output to the bin directory.

To run all the tests a running cassandra node is required to test against. See the [INSTALL.md](https://github.com/tango-controls-hdbpp/libhdbpp/blob/master/INSTALL.md) for the flags to build the unit tests. Once built, they can be run from the command line as follows. 

```bash
./bin/unit-tests
```

To look at the available tests and tags, should you wish to run a subset of the test suite (for example, you do not have a cassandra node to test against), then tests and be listed:

```bash
./bin/unit-tests --list-tests
```

Or:

```bash
./bin/unit-tests --list-tags
```

To see more options for the unit-test command line binary:

```bash
./bin/unit-tests --help
```

## Compatibility

The library has been built and tested against certain versions of its dependencies. While it may be possible to run with newer versions, this is not supported currently since there may be unknown compatibility problems. As newer versions are tested they will be added to the matrix below.

The compatibility matrix with dependencies is as follows:

| Libhdbpp-Cassandra Version | Cassandra Version | Datastax C++ Driver | Libuv |
|---------|-------------------|------------|-------|
| 0.10.0 | 2.2.9 | 2.2.1 | 1.4.2 |

Soname version mapping to libhdbpp.so:

| libhdbpp-cassandra.so | libhdbpp.so |
|-----------------------|-------------|
| 7.X.X | 6.X.X |

This version requires a schema having a ttl column in att_conf table. If you need to upgrade from a previous version of the database schema, you can add this column simply by executing the following CQL commands using cqlsh for instance:

```SQL
USE hdb;
ALTER TABLE att_conf ADD ttl int;
```

## License

The source code is released under the LGPL3 license and a copy of this license is provided with the code. 
