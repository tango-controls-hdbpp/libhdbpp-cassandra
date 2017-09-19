
# Building and Installation

## Dependencies

Libhdbpp-Cassandra has dependencies on Libhdpp, the Datastax Cassandra C++ Driver and some Tango Controls related libraries and dependencies. If you install any library or include file in non-standard location (i.e. local build environment for testing), then use standard CMake flags below to inform the build where to search for them.

It is dependent on a Tango Controls install via debian package or source distribution. Supported Tango Controls release is 9.2.5a.

CMake 3.0.0 or greater is required to perform the build.

### Libtango.so and LibomniX.so

To build Libhdbpp-cassandra, ensure the `libtango.so`, `libomnithread.so` and `libomniORB4.so` are installed either via source or package manager.

### Datastax CPP Driver

Download the [DataStax Cassandra C++ Driver](https://github.com/datastax/cpp-driver) from github and install. This is dependent on having [Libuv](https://github.com/libuv/libuv_) built and installed on the system. Datastax provide detailed [instructions](http://datastax.github.io/cpp-driver/topics/building/) to build the C++ Driver.

Libhdbpp-Cassandra was developed on a debian system, so both libuv and the Datastax C++ Driver were installed from source. If you build and use libuv from a custom location (e.g. local test build environment, then you will need to inform the C++ Driver CMake build system where to search, example:

```bash
cmake -DLIBUV_INCLUDE_DIR=/my/custom/include -DLIBUV_LIBRARY=/my/custom/lib ..
```

### Libhdbpp

Build and install [Libhdbpp](tango-build-end/jessie/9.2.5a) according to the build instructions found at its github page.

## Standard flags

The build system is CMake therefore standard CMake flags can be used to influence the build and installation process. Several custom flags are defined to build the correct library. They are:

| Flag | Default | Use |
|------|---------|-----|
| HDBPP_CASS_BUILD_SHARED | ON | Build the shared library. This will also be installed if make install is run. |
| HDBPP_CASS_BUILD_STATIC | OFF | Build the static library. This will also be installed if make install is run. |
| HDBPP_CASS_INSTALL_HEADERS | OFF | On running make install, install the header files. |
| HDBPP_CASS_BUILD_TESTS | OFF | Build unit tests |

The following is a list of common useful CMake flags and their use:

| Flag | Use |
|------|-----|
| CMAKE_INSTALL_PREFIX | Standard CMake flag to modify the install prefix. |
| CMAKE_INCLUDE_PATH | Standard CMake flag to add include paths to the search path. |
| CMAKE_LIBRARY_PATH | Standard CMake flag to add paths to the library search path |

Using the above CMake flags above its possible to use tango and other libraries from non-standard locations. This is the preferred method, i.e. add all library and include paths to the above flags. 

For convenience there also exists four path flags to define the install location of various dependencies. This is to aid moving from the older environment variable based system to the new CMake system. These can be passed in like normal CMake flags using the -D syntax, or exported into the local bash environment where they will be picked up by the build system.

| Path Flag | Description |
|-----------|-------------|
| HDBPP_CASS_TANGO_INSTALL_DIR | Install root for Tango Controls |
| HDBPP_CASS_OMIORB_INSTALL_DIR| Install root for Omniorb |
| HDBPP_CASS_CPP_INSTALL_DRIVER_DIR | Install root for the Datastax CPP Driver | 
| HDBPP_CASS_LIBHDBPP_INSTALL_DIR | Install root for Libhdbpp |

### Passing CMake Lists

Note: to pass multiple paths (i.e. a string list to cmake), either an escaped semi colon must be used, or the list must be enclosed in quotes. Examples: 

* `-DCMAKE_INCLUDE_PATH=/here/there\;/some/where/else`
* `"-DCMAKE_INCLUDE_PATH=/here/there;/some/where/else"`
* `'-DCMAKE_INCLUDE_PATH=/here/tehre;/some/where/else'`

## Building

Build using CMake version 3.0.0 or greater. An out of source build is required by the CMakeLists file.

### **Important Note:** - Building Against Standard Tango Controls 9.2.5a

Tango Controls 9.2.5a as source distribution or debian package on Debian Stretch installs the header files into `/usr/include/tango`, but `tango.h` includes tango headers (example `tango_config.h`) from  `/usr/include`. This means the build fails. To work around this until the next release of Tango Controls (which fixes this problem) add `/usr/include/tango` to the CMAKE_INCLUDE_PATH, eg:

```bash
cmake -DCMAKE_INCLUDE_PATH=/usr/include/tango ..
```

### Example Build Sequence

First clone the repository:

```
git clone http://github.com/tango-controls/libhdbpp-cassandra.git
```

Create a build directory to run CMake from:

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