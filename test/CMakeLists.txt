cmake_minimum_required(VERSION 3.0.2)
project(unit-tests)

message(STATUS "Search for TANGO package config...")

# When using CMAKE 3.1.0 or greater, we can use the CMAKE_PREFIX_PATH as 
# a search path for the tango.pc pkgconfig file. On older versions, the
# PKG_CONFIG_PATH has to be set before the build
if((${CMAKE_VERSION} VERSION_EQUAL "3.1.0") OR (${CMAKE_VERSION} VERSION_GREATER "3.1.0"))
    set(PKG_CONFIG_USE_CMAKE_PREFIX_PATH ON)
    list(APPEND CMAKE_PREFIX_PATH "/usr")
endif()

# Unlike the shared library, the unit test binary requires full linkage
# with Tango. To faciliate this we use pkgconfig
find_package(PkgConfig REQUIRED)
pkg_search_module(TANGO REQUIRED tango)

if(TANGO_FOUND)
    message(STATUS "Found tango version ${TANGO_VERSION} at ${TANGO_PREFIX}")
endif(TANGO_FOUND)

list(APPEND UNIT_TEST_LIBRARIES ${TANGO_LIBRARIES})
list(APPEND UNIT_TEST_INCLUDE_PATHS ${TANGO_INCLUDE_DIRS})
list(APPEND UNIT_TEST_LIBRARY_PATHS ${TANGO_LIBDIR})

# Now load the libraries and check each is found, if not
# this is an error condition, so halt the cmake config
foreach(LIB ${UNIT_TEST_LIBRARIES})
    # try the user provided paths first
    find_library(FOUND_LIB_${LIB} ${LIB} PATHS ${UNIT_TEST_LIBRARY_PATHS} NO_DEFAULT_PATH)

    # if we could not find it, drop to the system paths
    if (NOT FOUND_LIB_${LIB})
        find_library(FOUND_LIB_${LIB} ${LIB})
    endif()

    list(APPEND UNIT_TEST_FOUND_LIBS ${FOUND_LIB_${LIB}})

    if (FOUND_LIB_${LIB})
        message(STATUS "Unit Test: Found " ${LIB} " at: " ${FOUND_LIB_${LIB}})
    else()
        message(FATAL "Unit Test: Could not find " ${LIB})
    endif()
endforeach(LIB)

# Prepare "Catch" library for other executables
set(CATCH_INCLUDE_DIR ${CMAKE_CURRENT_SOURCE_DIR})
add_library(catch_library INTERFACE)
target_include_directories(catch_library INTERFACE ${CATCH_INCLUDE_DIR})

# Make test executable
set(TEST_SOURCES 
    ${CMAKE_CURRENT_SOURCE_DIR}/LibHdb++Cassandra-Tests.cpp)

add_executable(unit-tests ${TEST_SOURCES})
target_link_libraries(unit-tests PRIVATE catch_library static_library ${UNIT_TEST_FOUND_LIBS})
target_include_directories(unit-tests PRIVATE ${UNIT_TEST_INCLUDE_PATHS})
target_compile_options(unit-tests PRIVATE ${TANGO_CFLAGS_OTHER})