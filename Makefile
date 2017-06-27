TARGET  = libhdb++cassandra

##############################################
# setup paths
#

LIBHDBPP_DIR ?= .libhdbpp
LIBHDBPP_INC ?= $(LIBHDBPP_DIR)/src

CASS_CPP_DRIVER_DIR ?= ../../cassandra/cppDriver/cpp-driver
LIBUV_ROOT_DIR ?= ../../cassandra/cppDriver/cpp-driver/lib/libuv

CASSANDRA_INC := ${CASS_CPP_DRIVER_DIR}/include
TANGO_INC := ${TANGO_DIR}/include/tango
OMNIORB_INC := ${OMNIORB_DIR}/include
ZMQ_INC :=  ${ZMQ_DIR}/include

INCLUDE_PATH = -Isrc -I${TANGO_INC} -I${OMNIORB_INC} -I${ZMQ_INC} -I${CASSANDRA_INC} -I${LIBHDBPP_INC}

TANGO_LIB = -L${TANGO_DIR}/lib -ltango -llog4tango
OMNIORB_LIB = -L${OMNIORB_DIR}/lib -lomniORB4 -lomniDynamic4 -lCOS4 -lomnithread 
ZMQ_LIB =  -L${ZMQ_DIR}/lib -lzmq
CASSANDRA_LIB = -L${CASS_CPP_DRIVER_DIR}/lib -lcassandra 
LIBUV_LIB = -L${LIBUV_ROOT_DIR}/lib -luv

SOURCES = src/LibHdb++Cassandra.cpp src/TangoEventDataBinder.cpp src/AttributeName.cpp
HEADERS = src/LibHdb++Cassandra.h src/TangoEventDataBinder.h src/AttributeName.h Log.h
OBJECTS = $(patsubst src%,obj%,$(SOURCES:.cpp=.o))

UNIT_TESTS_SOURCE = test/LibHdb++Cassandra-Tests.cpp
UNIT_TESTS_OBJECTS = $(patsubst test%,test%,$(UNIT_TESTS_SOURCE:.cpp=.o))

##############################################
# compile and link options
#
CXXFLAGS += -std=gnu++0x -Wall -DRELEASE='"$HeadURL$ "'

##############################################
# support for shared libray versioning
#
LFLAGS_SONAME   = ${LIBUV_LIB} ${CASSANDRA_LIB} -Wl,-soname,
SHLDFLAGS       = -shared
BASELIBNAME     = $(TARGET)
SHLIB_SUFFIX    = so

#  release numbers for libraries
#
 LIBVERSION     = 7
 LIBRELEASE     = 0
 LIBSUBRELEASE  = 0
#

STATIC_LIBRARY  = $(BASELIBNAME).a
DT_SONAME       = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION)
DT_SHLIB        = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION).$(LIBRELEASE).$(LIBSUBRELEASE)
SHLIB           = $(BASELIBNAME).$(SHLIB_SUFFIX)

##############################################
# Library targets

.PHONY : clean install 

all: $(TARGET)

$(TARGET): lib obj $(OBJECTS)
	$(CXX) $(OBJECTS) $(SHLDFLAGS) $(LFLAGS_SONAME)$(DT_SONAME) -o lib/$(DT_SHLIB)
	ln -sf $(DT_SHLIB) lib/$(SHLIB)
	ln -sf $(SHLIB) lib/$(DT_SONAME)
	ar rcs lib/$(STATIC_LIBRARY) ${OBJECTS}

obj/%.o: src/%.cpp 
	$(CXX) $(CXXFLAGS) -fPIC $(INCLUDE_PATH) -c $< -o $@

##############################################
# Unit test targets

UNIT_TEST_TARGET = unit-tests

tests: test bin/${UNIT_TEST_TARGET}

bin/${UNIT_TEST_TARGET}: bin ${UNIT_TESTS_OBJECTS}
	$(CXX) $(CXXFLAGS) $(INCLUDE_PATH) ${UNIT_TESTS_OBJECTS} -o $@ $(LDFLAGS) \
	${TANGO_LIB} ${OMNIORB_LIB} ${ZMQ_LIB} ${LIBUV_LIB} ${CASSANDRA_LIB} \
	lib/$(STATIC_LIBRARY) -Wl,-rpath=$(PWD)/lib

test/%.o: test/%.cpp
	$(CXX) $(CXXFLAGS) $(INCLUDE_PATH) -c $< -o $@

clean-tests:
	@rm -f ${UNIT_TESTS_OBJECTS}
	@rm -f bin/${UNIT_TEST_TARGET}

run-tests: tests
	@./bin/${UNIT_TEST_TARGET}

test:
	@mkdir $@

##############################################
# Documentation

docs: doc
	@doxygen .doxygen-config

##############################################
# Other targets

clean:
	@rm -f obj/*.o lib/*.so* lib/*.a bin/*

lib:
	@mkdir $@

obj:
	@mkdir $@

bin:
	@mkdir $@

doc: 
	@mkdir $@


ifdef LIBHDBPPCASSANDRA_INSTALL_DIR
install:
	@echo "Install to $(LIBHDBPPCASSANDRA_INSTALL_DIR)"
	install -d $(LIBHDBPPCASSANDRA_INSTALL_DIR)/lib
	install -d $(LIBHDBPPCASSANDRA_INSTALL_DIR)/include
	install -m 644 lib/libhdb++cassandra.so $(LIBHDBPPCASSANDRA_INSTALL_DIR)/lib
	install -m 644 lib/libhdb++cassandra.so.7 $(LIBHDBPPCASSANDRA_INSTALL_DIR)/lib
	install -m 644 lib/libhdb++cassandra.so.7.0.0 $(LIBHDBPPCASSANDRA_INSTALL_DIR)/lib
	install -m 644 lib/libhdb++cassandra.a $(LIBHDBPPCASSANDRA_INSTALL_DIR)/lib
	install -m 644 src/LibHdb++Cassandra.h $(LIBHDBPPCASSANDRA_INSTALL_DIR)/include
else
install:
	echo "No target defined by LIBHDBPPCASSANDRA_INSTALL_DIR, doing nothing"
endif