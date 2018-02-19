cmake_minimum_required(VERSION 3.0)

# source files
set(SRC_FILES ${SRC_FILES}
    ${CMAKE_CURRENT_SOURCE_DIR}/AttributeName.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/AttributeCache.cpp    
    ${CMAKE_CURRENT_SOURCE_DIR}/PreparedStatementCache.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/TangoEventDataBinder.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/LibHdb++Cassandra.cpp    
    PARENT_SCOPE)