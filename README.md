# Libhdbpp-Cassandra

[![TangoControls](https://img.shields.io/badge/-Tango--Controls-7ABB45.svg?style=flat&logo=%20data%3Aimage%2Fpng%3Bbase64%2CiVBORw0KGgoAAAANSUhEUgAAACAAAAAkCAYAAADo6zjiAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALEwAACxMBAJqcGAAAAsFJREFUWIXtl01IFVEYht9zU%2FvTqOxShLowlOgHykWUGEjUKqiocB1FQURB0KJaRdGiaFM7gzZRLWpTq2olhNQyCtpYCP1gNyIoUTFNnxZzRs8dzvw4Q6564XLnfOf73vedc2a%2BmZEKALgHrC3CUUR8CxZFeEoFalsdM4uLmMgFoIlZLJp3A9ZE4S2oKehhlaR1BTnyg2ocnW%2FxsxEDhbYij4EPVncaeASMAavnS%2FwA8NMaqACNQCew3f4as3KZOYh2SuqTVJeQNiFpn6QGSRVjTH9W%2FiThvcCn6H6n4BvQDvQWFT%2BSIDIFDAKfE3KOAQeBfB0XGPeQvgE67P8ZoB44DvTHmFgJdOQRv%2BUjc%2BavA9siNTWemgfA3TwGquCZ3w8szFIL1ALngIZorndvgJOR0GlP2gtJkzH%2Bd0fGFxW07NqY%2FCrx5QRXcYjbCbmxF1dkBSbi8kpACah3Yi2Sys74cVyxMWY6bk5BTwgRe%2BYlSzLmxNpU3aBeJogk4XWWpJKUeiap3RJYCpQj4QWZDQCuyIAk19Auj%2BAFYGZZjTGjksaBESB8P9iaxUBIaJzjZcCQcwHdj%2BS2Al0xPOeBYYKHk4vfmQ3Y8YkIwRUb7wQGU7j2ePrA1URx93ayd8UpD8klyPbSQfCOMIO05MbI%2BDvwBbjsMdGTwlX21AAMZzEerkaI9zFkP4AeYCPBg6gNuEb6I%2FthFgN1KSQupqzoRELOSed4DGiJala1UmOMr2U%2Bl%2FTWEy9Japa%2Fy41IWi%2FJ3d4%2FkkaAw0Bz3AocArqApwTvet3O3GbgV8qqjAM7bf4N4KMztwTodcYVyelywKSCD5V3xphNXoezuTskNSl4bgxJ6jPGVJJqbN0aSV%2Bd0M0aO7FCs19Jo2lExphXaTkxdRVgQFK7DZVDZ8%2BcpdmQh3wuILh7ut3AEyt%2B51%2BL%2F0cUfwFOX0t0StltmQAAAABJRU5ErkJggg%3D%3D)](http://www.tango-controls.org) [![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0) [![GitHub release](https://img.shields.io/github/release/tango-controls-hdbpp/libhdbpp-cassandra.svg)](https://github.com/tango-controls-hdbpp/libhdbpp-cassandra/releases)  [![Download](https://api.bintray.com/packages/tango-controls/debian/libhdb%2B%2Bcassandra7/images/download.svg)](https://bintray.com/tango-controls/debian/libhdb%2B%2Bcassandra7/_latestVersion)


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