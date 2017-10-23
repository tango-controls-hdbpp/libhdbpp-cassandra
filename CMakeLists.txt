# Functions and Pre-build  -----------------------------------

# Stop messy in source builds
set(CMAKE_DISABLE_IN_SOURCE_BUILD ON)
set(CMAKE_DISABLE_SOURCE_CHANGES  ON)

if ( ${CMAKE_SOURCE_DIR} STREQUAL ${CMAKE_BINARY_DIR} )
    message( FATAL_ERROR "In-source builds not allowed. Please make a new directory (called a build directory) and run CMake from there. You may need to remove CMakeCache.txt." )
endif()

# Function to examine if the variable has been set locally or via 
# environment variable, and if so, add it to the path list
function(append_path_if_set VAR_NAME INCLUDE_LIST LIBRARY_LIST)

    # Variable set locally or via the command line
    if(${VAR_NAME})
        message(STATUS ${VAR_NAME} " set via command line to: " ${${VAR_NAME}})
        set(${INCLUDE_LIST} ${${INCLUDE_LIST}} "${${VAR_NAME}}/include" PARENT_SCOPE)
        set(${LIBRARY_LIST} ${${LIBRARY_LIST}} "${${VAR_NAME}}/lib" PARENT_SCOPE)
    else()
        set(${VAR_NAME} $ENV{${VAR_NAME}} )

        # Variable may be set in the environment
        if(${VAR_NAME})
            message(STATUS ${VAR_NAME} " set via env to: " ${${VAR_NAME}})
            set(${INCLUDE_LIST} ${${INCLUDE_LIST}} "$ENV{${VAR_NAME}}/include" PARENT_SCOPE)
            set(${LIBRARY_LIST} ${${LIBRARY_LIST}} "$ENV{${VAR_NAME}}/lib" PARENT_SCOPE)
        endif()
    endif()
endfunction()

# Start Build Config -----------------------------------
cmake_minimum_required(VERSION 3.0)
set(CMAKE_VERBOSE_MAKEFILE OFF)
set(CMAKE_COLOR_MAKEFILE   ON)

# To build both the static and shared version, we require two projects, we do
# this by just defining the name appended with the target
project(shared_library)
project(static_library)

# Include the version strings
include(cmake/ReleaseVersion.cmake)

# Check any custom flags
option(HDBPP_CASS_BUILD_SHARED "Build shared library" ON)
option(HDBPP_CASS_BUILD_STATIC "Build static library" OFF)
option(HDBPP_CASS_BUILD_TESTS "Build unit tests" OFF)
option(HDBPP_CASS_INSTALL_SCRIPTS "Install scripts and utility files on install" OFF)
option(HDBPP_CASS_DEV_INSTALL "Install development files" OFF)

# Debug the flags
if(HDBPP_CASS_DEV_INSTALL)
    message(STATUS "Development install requested")
endif(HDBPP_CASS_DEV_INSTALL)

if(HDBPP_CASS_INSTALL_SCRIPTS)
    message(STATUS "Script/utility file install requested")
endif(HDBPP_CASS_INSTALL_SCRIPTS)

if(HDBPP_CASS_BUILD_TESTS)
    message(STATUS "Unit tests will be built, requires static library also")
    set(HDBPP_CASS_BUILD_STATIC ON)
endif(HDBPP_CASS_BUILD_TESTS)

if(NOT HDBPP_CASS_BUILD_SHARED AND NOT HDBPP_CASS_BUILD_STATIC)
    message(FATAL "Must select to build static and/or shared library")
endif(NOT HDBPP_CASS_BUILD_SHARED AND NOT HDBPP_CASS_BUILD_STATIC)

# arch install definitions
include(GNUInstallDirs)

# Configure the compiler flags here
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11 -Wall")

# Warn on any unresolved symbols in the shared library 
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,--no-undefined")

# Set the output directory to lib to stay consistent with the old build
set(OUTPUT_DIR "lib")
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/${OUTPUT_DIR})
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/${OUTPUT_DIR})

# set up a configuration file to pass variables into the build
configure_file(
    "${PROJECT_SOURCE_DIR}/cmake/LibHdb++CassandraConfig.h.in"
    "${PROJECT_BINARY_DIR}/LibHdb++CassandraConfig.h")

message(STATUS "Building include paths...")

# Add any include paths from the command line
set(INCLUDE_PATHS ${INCLUDE_PATHS} ${CMAKE_INCLUDE_PATH})

foreach(INC_PATH ${CMAKE_INCLUDE_PATH})
    message(STATUS "Extra include path: " ${INC_PATH})
endforeach()

message(STATUS "Building library paths...")

foreach(LIB_PATH ${CMAKE_LIBRARY_PATH})
    message(STATUS "Extra library search path: " ${LIB_PATH})
endforeach()

# Since the library has a large number of dependencies we allow them
# to be passed either from the command line or as install paths
append_path_if_set(HDBPP_CASS_TANGO_INSTALL_DIR INCLUDE_PATHS LIBRARY_PATHS)
append_path_if_set(HDBPP_CASS_OMIORB_INSTALL_DIR INCLUDE_PATHS LIBRARY_PATHS)
append_path_if_set(HDBPP_CASS_CPP_INSTALL_DRIVER_DIR INCLUDE_PATHS LIBRARY_PATHS)
append_path_if_set(HDBPP_CASS_LIBHDBPP_INSTALL_DIR INCLUDE_PATHS LIBRARY_PATHS)
append_path_if_set(HDBPP_CASS_ZMQ_INSTALL_DIR INCLUDE_PATHS LIBRARY_PATHS)

message(STATUS "Searching for libraries...")

# Variable to contain a list of all the libs we depend on
set(LIBRARIES 
    libcassandra.so
    libtango.so
    libomnithread.so
    libomniORB4.so) 

# Now load the libraries and check each is found, if not
# this is an error condition, so halt the cmake config
foreach(LIB ${LIBRARIES})
    # try the user provided paths first
    find_library(FOUND_LIB_${LIB} ${LIB} PATHS ${LIBRARY_PATHS} NO_DEFAULT_PATH)

    # if we could not find it, drop to the system paths
    if (NOT FOUND_LIB_${LIB})
        find_library(FOUND_LIB_${LIB} ${LIB})
    endif()

    list(APPEND LIBS_DIR ${FOUND_LIB_${LIB}})

    if (FOUND_LIB_${LIB})
        message(STATUS "Found " ${LIB} " at: " ${FOUND_LIB_${LIB}})
    else()
        message(FATAL "Could not find " ${LIB})
    endif()
endforeach(LIB)

# Include the source
add_subdirectory(src)

# Build Targets  -----------------------------------

# This is the "object library" target: compiles the sources only once
# for both the static and shared library
add_library(object_library OBJECT ${SRC_FILES})
target_compile_options(object_library PRIVATE -fPIC)
set_property(TARGET ${OBJECT_LIBRARY} PROPERTY POSITION_INDEPENDENT_CODE 1)

target_compile_options(object_library 
    PUBLIC -std=c++11 -Wall -Wextra)

target_include_directories(object_library 
    PUBLIC ${INCLUDE_PATHS}
    PRIVATE "${PROJECT_BINARY_DIR}")

if(HDBPP_CASS_BUILD_SHARED)
    add_library(shared_library SHARED $<TARGET_OBJECTS:object_library>)
    target_link_libraries(shared_library PUBLIC ${LIBS_DIR})

    set_target_properties(shared_library PROPERTIES 
        OUTPUT_NAME hdb++cassandra
        LINK_FLAGS "-Wl,--no-undefined")

    # Setting the target includes will allow us to link to this shared
    # library via the cmake project and not require us to include all
    # its dependencies
    target_include_directories(shared_library 
        PUBLIC ${INCLUDE_PATHS}
        PUBLIC ${CMAKE_CURRENT_LIST_DIR}/src
        PUBLIC "${PROJECT_BINARY_DIR}")

    # This will version the shared library
    set_target_properties(shared_library
        PROPERTIES VERSION ${LIBRARY_VERSION_STRING} 
        SOVERSION ${LIBRARY_VERSION_MAJOR})
endif(HDBPP_CASS_BUILD_SHARED)

if(HDBPP_CASS_BUILD_STATIC)
    add_library(static_library STATIC $<TARGET_OBJECTS:object_library>)
    target_link_libraries(static_library PUBLIC ${LIBS_DIR})
    set_target_properties(static_library PROPERTIES OUTPUT_NAME hdb++cassandra)

    # Like the shared project, define dependencies, the static library is
    # used in the tests and requires no additional includes due to this
    target_include_directories(static_library 
        PUBLIC ${INCLUDE_PATHS}
        PUBLIC ${CMAKE_CURRENT_LIST_DIR}/src
        PUBLIC "${PROJECT_BINARY_DIR}")    
endif(HDBPP_CASS_BUILD_STATIC)

# Install Config -----------------------------------
if(HDBPP_CASS_BUILD_SHARED)

    if(HDBPP_CASS_DEV_INSTALL)
        install(TARGETS shared_library LIBRARY 
            DESTINATION ${CMAKE_INSTALL_LIBDIR})

        install(FILES src/LibHdb++Cassandra.h 
            DESTINATION ${CMAKE_INSTALL_INCLUDEDIR}/libhdb++cassandra)            
    else()
        install(TARGETS shared_library 
            LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
            NAMELINK_SKIP)
    endif(HDBPP_CASS_DEV_INSTALL)

endif(HDBPP_CASS_BUILD_SHARED)

if(HDBPP_CASS_BUILD_STATIC)
    install(TARGETS static_library ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR})
endif(HDBPP_CASS_BUILD_STATIC)

if(HDBPP_CASS_INSTALL_SCRIPTS)
    install(FILES etc/create_hdb_cassandra.cql DESTINATION ${CMAKE_INSTALL_DATADIR}/libhdb++cassandra)
endif(HDBPP_CASS_INSTALL_SCRIPTS)

# Tests -----------------------------------
if(HDBPP_CASS_BUILD_TESTS)
    add_subdirectory(test)
endif(HDBPP_CASS_BUILD_TESTS)
