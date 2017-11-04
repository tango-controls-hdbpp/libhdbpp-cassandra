cmake_minimum_required(VERSION 3.0)

set(TEST_NAME "unit-tests")
project(${TEST_NAME})

# Prepare "Catch" library for other executables
set(CATCH_INCLUDE_DIR ${CMAKE_CURRENT_SOURCE_DIR})
add_library(catch_library INTERFACE)
target_include_directories(catch_library INTERFACE ${CATCH_INCLUDE_DIR})

# Make test executable
set(TEST_SOURCES 
    ${CMAKE_CURRENT_SOURCE_DIR}/LibHdb++Cassandra-Tests.cpp)

add_executable(${TEST_NAME} ${TEST_SOURCES})
target_link_libraries(${TEST_NAME} PRIVATE catch_library static_library)
set_target_properties(${TEST_NAME} PROPERTIES RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_LIST_DIR}/../bin")