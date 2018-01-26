cmake_minimum_required(VERSION 3.0)

project(unit-tests)

# Prepare "Catch" library for other executables
set(CATCH_INCLUDE_DIR ${CMAKE_CURRENT_SOURCE_DIR})
add_library(catch_library INTERFACE)
target_include_directories(catch_library INTERFACE ${CATCH_INCLUDE_DIR})

# Make test executable
set(TEST_SOURCES 
    ${CMAKE_CURRENT_SOURCE_DIR}/main.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/AttributeNameTests.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/AttributeCacheTests.cpp    
    ${CMAKE_CURRENT_SOURCE_DIR}/CassandraConnection.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/CassandraConnectionTests.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/DbCommands.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/PreparedStatementCacheTests.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/TangoEventDataBinderTests.cpp)

add_executable(unit-tests ${TEST_SOURCES})
target_compile_options(unit-tests PRIVATE -std=c++11 -Wall -Wextra)
target_link_libraries(unit-tests PRIVATE catch_library static_library)