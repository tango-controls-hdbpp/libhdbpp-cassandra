
# Building and Installation

- [Building and Installation](#building-and-installation)
    - [Dependencies](#dependencies)
        - [Datastax CPP Driver](#datastax-cpp-driver)
        - [Debian Stetch Packages](#debian-stetch-packages)
            - [Libhdbpp Debian Package](#libhdbpp-debian-package)
            - [Datastax CPP Driver Release 2.2.1 Debian Package](#datastax-cpp-driver-release-221-debian-package)
    - [Build flags](#build-flags)
        - [Unit Tests Build Flags](#unit-tests-build-flags)
            - [CMake Versions Older Than 3.1.0](#cmake-versions-older-than-310)
            - [CMake Version 3.1.0 Or Newer](#cmake-version-310-or-newer)
        - [Passing CMake Lists](#passing-cmake-lists)
    - [Building](#building)
        - [Building Against Tango Controls 9.2.5a](#building-against-tango-controls-925a)
        - [Example Build Sequence](#example-build-sequence)
    - [Installation](#installation)

## Dependencies

Ensure the development version of the dependencies are installed. These are as follows:

* [libhdbpp](https://github.com/tango-controls-hdbpp/libhdbpp) - (libhdb++6). Build and install according its instructions.
* [Datastax CPP Driver](https://github.com/datastax/cpp-driver), version 2.2.1.
* Tango Controls 9 or higher.
* omniORB release 4 - libomniorb4 and libomnithread.
* libzmq - libzmq3-dev or libzmq5-dev.

If they have not been installed in a standard location, then use standard CMake flags below to inform the build where to search for them.

The library can be built with Tango Controls install via debian package or source distribution. Supported Tango Controls release is currently 9.2.5a. Ensure build flags are used if tango is installed to a custom location.

Toolchain dependencies:

* CMake 3.0.2 or greater is required to build the shared library and unit tests.

### Datastax CPP Driver

Download the [DataStax Cassandra C++ Driver](https://github.com/datastax/cpp-driver) from github and install. This is dependent on having [Libuv](https://github.com/libuv/libuv) built and installed on the system. Datastax provides detailed [instructions](http://datastax.github.io/cpp-driver/topics/building/) to build the C++ Driver.

Libhdbpp-Cassandra was developed on a debian system, so both libuv and the Datastax C++ Driver were installed from source. If you build and use libuv from a custom location (e.g. local test build environment,then you will need to inform the C++ Driver CMake build system where to search, example:

```bash
cmake -DLIBUV_INCLUDE_DIR=/my/custom/include -DLIBUV_LIBRARY=/my/custom/lib ..
```

### Debian Stetch Packages

#### Libhdbpp Debian Package

There is also a debian stretch package available to install this [here](https://bintray.com/tango-controls/debian/libhdb%2B%2B6) at bintray. 

#### Datastax CPP Driver Release 2.2.1 Debian Package

It is possible to take a version 2.2.1 cpp-driver debian package we have made available [here](https://bintray.com/tango-controls/debian/cassandra-cpp-driver) on bintray. This has its dependencies set correctly for libuv1. This option is only avilable on debian stretch.

## Build flags

The build system is CMake therefore standard CMake flags can be used to influence the build and installation process. Several custom flags are defined to build the correct library. They are:

| Flag | Default | Use |
|------|---------|-----|
| HDBPP_CASS_BUILD_SHARED | ON | Build the shared library. This will also be installed if make install is run. |
| HDBPP_CASS_BUILD_STATIC | OFF | Build the static library. This will also be installed if make install is run. |
| HDBPP_CASS_BUILD_TESTS | OFF | Build unit tests |
| HDBPP_CASS_DEV_INSTALL | OFF | Install development files and libraries |
| HDBPP_CASS_INSTALL_SCRIPTS | OFF | Install cql scripts |
| HDBPP_CASS_ENABLE_TRACING | OFF | Enabled function tracing debug |

The following is a list of common useful CMake flags and their use:

| Flag | Use |
|------|-----|
| CMAKE_INSTALL_PREFIX | Standard CMake flag to modify the install prefix. |
| CMAKE_INCLUDE_PATH | Standard CMake flag to add include paths to the search path. |
| CMAKE_LIBRARY_PATH | Standard CMake flag to add paths to the library search path |

Using the above CMake flags above its possible to use tango and other libraries from non-standard locations. This is the preferred method, i.e. add all library and include paths to the above flags. 

For convenience there also exists five path flags to define the install location of various dependencies. This is to aid a situation where everything is installed non-standard. These can be passed in like normal CMake flags using the -D syntax, or exported into the local bash environment where they will be picked up by the build system.

| Path Flag | Description |
|-----------|-------------|
| HDBPP_CASS_TANGO_INSTALL_DIR | Install root for Tango Controls |
| HDBPP_CASS_OMIORB_INSTALL_DIR| Install root for Omniorb |
| HDBPP_CASS_CPP_INSTALL_DRIVER_DIR | Install root for the Datastax CPP Driver | 
| HDBPP_CASS_LIBHDBPP_INSTALL_DIR | Install root for Libhdbpp |
| HDBPP_CASS_ZMQ_INSTALL_DIR | Install root for libzmq |

### Unit Tests Build Flags

The unit tests rely on the tango.pc file. If you have tango installed in a non-standard location, then pkg-config will need a search path for its search. There is two choices. 

#### CMake Versions Older Than 3.1.0 

Older CMake versions do not support adding a prefix to the pkg-config search path. Therefore the PKG_CONFIG_PATH must be set either as:

```
export PKG_CONFIG_PATH=/segfs/tango/release/debian9/lib/pkgconfig
cmake ..
```

Or as a temporary environment variable:

```
PKG_CONFIG_PATH=/segfs/tango/release/debian9/lib/pkgconfig cmake ..
```

#### CMake Version 3.1.0 Or Newer

We can use the same as above, or use CMAKE_PREFIX_PATH to append a search path. IMPORTANT: this is a prefix (eg, such as /usr), not the entire path, example:

```
cmake -DCMAKE_PREFIX_PATH=/some/other/place ..
```

### Passing CMake Lists

Note: to pass multiple paths (i.e. a string list to cmake), either an escaped semi colon must be used, or the list must be enclosed in quotes. Examples: 

* `-DCMAKE_INCLUDE_PATH=/here/there\;/some/where/else`
* `-DCMAKE_INCLUDE_PATH="/here/there;/some/where/else"`
* `-DCMAKE_INCLUDE_PATH='/here/tehre;/some/where/else'`

## Building

### Building Against Tango Controls 9.2.5a

**The debian package and source install place the headers under /usr/include/tango, yet the code includes tango via `#include <tango.h>` (to be compatible with Tango Controls 10 when it is released), so its likely you will need to pass at least one path via CMAKE_INCLUDE_PATH. In this case, set CMAKE_INCLUDE_PATH=/usr/include/tango or CMAKE_INCLUDE_PATH=/usr/local/include/tango, depending on your install method. Example:**

```bash
cmake -DCMAKE_INCLUDE_PATH=/usr/include/tango ..
```

### Example Build Sequence

First clone the repository:

```
git clone http://github.com/tango-controls-hdbpp/libhdbpp-cassandra.git
```

An out of source build is required by the CMakeLists file, so create a build directory to run CMake from:

```
mkdir libhdbpp-cassandra/build
cd libhdbpp-cassandra/build
```

Then configure with cmake:

```bash
cmake ..
```

Or for something with non-standard dependencies:

```bash
cmake \
    -DHDBPP_CASS_BUILD_TESTS=ON \
    -DCMAKE_INSTALL_PREFIX=/my/custom/location \
    -DCMAKE_INCLUDE_PATH=/path/to/custom/include \
    -DCMAKE_LIBRARY_PATH=/path/to/custom/library \
    ..
```

Then build:

```bash
make
```

## Installation

After the build has completed, simply run:

```
make install
```

On installation the cql scripts under etc/ will be placed in /CMAKE_INSTALL_PREFIX/shared/libhdb++cassandra/