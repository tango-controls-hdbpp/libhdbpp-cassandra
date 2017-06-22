LIBHDBPP_DIR ?= .libhdbpp
LIBHDBPP_INC ?= $(LIBHDBPP_DIR)/src

CASS_CPP_DRIVER_DIR ?= ../../cassandra/cppDriver/cpp-driver
LIBUV_ROOT_DIR ?= ../../cassandra/cppDriver/cpp-driver/lib/libuv

DBIMPL_INC := ${CASS_CPP_DRIVER_DIR}/include
DBIMPL_LIB = -L${LIBUV_ROOT_DIR}/lib \
             -L${CASS_CPP_DRIVER_DIR}/lib \
             -lcassandra -luv

TANGO_INC := ${TANGO_DIR}/include/tango
OMNIORB_INC := ${OMNIORB_DIR}/include
ZMQ_INC :=  ${ZMQ_DIR}/include

INC_DIR = -I${TANGO_INC} -I${OMNIORB_INC} -I${ZMQ_INC}
CXXFLAGS += -std=gnu++0x -Wall -DRELEASE='"$HeadURL$ "' -I$(DBIMPL_INC) $(INC_DIR) -I$(LIBHDBPP_INC)  -fPIC

TANGO_LIB = ${TANGO_DIR}/lib
OMNIORB_LIB = ${OMNIORB_DIR}/lib

TARGET  = libhdb++cassandra

SOURCES = src/LibHdb++Cassandra.cpp src/AttributeName.cpp
HEADERS = src/LibHdb++Cassandra.h src/AttributeName.h Log.h
OBJECTS = $(patsubst src%,obj%,$(SOURCES:.cpp=.o))

##############################################
# support for shared libray versioning
#
LFLAGS_SONAME = $(DBIMPL_LIB) -Wl,-soname,
SHLDFLAGS = -shared
BASELIBNAME =  $(TARGET)
SHLIB_SUFFIX = so

#  release numbers for libraries
#
 LIBVERSION    = 7
 LIBRELEASE    = 0
 LIBSUBRELEASE = 0
#

LIBRARY       = $(BASELIBNAME).a
DT_SONAME     = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION)
DT_SHLIB      = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION).$(LIBRELEASE).$(LIBSUBRELEASE)
SHLIB         = $(BASELIBNAME).$(SHLIB_SUFFIX)

##############################################
# targets

.PHONY : clean install

all: $(TARGET)

$(TARGET): lib obj $(OBJECTS)
	$(CXX) $(OBJECTS) $(SHLDFLAGS) $(LFLAGS_SONAME)$(DT_SONAME) -o lib/$(DT_SHLIB)
	ln -sf $(DT_SHLIB) lib/$(SHLIB)
	ln -sf $(SHLIB) lib/$(DT_SONAME)
	ar rcs lib/$(LIBRARY) obj/LibHdb++Cassandra.o

obj/%.o: src/%.cpp 
	$(CXX) $(CXXFLAGS) -c $< -o $@

#tests: bin/libhdbpp-cassandra-tests

#obj/libhdbpp-cassandra-tests.o: test/LibHdb++Cassandra-Tests.cpp
#	$(CXX) $(CXXFLAGS) -ggdb3 -Isrc -c -o $@ $<

#obj/Test_updateTTL_Attr.o: test/Test_updateTTL_Attr.cpp src/LibHdb++Cassandra.h
#	$(CXX) $(CXXFLAGS) -ggdb3 -Isrc -c -o $@ $<

#bin/libhdbpp-cassandra-tests: bin obj/libhdbpp-cassandra-tests.o
#	$(CXX) $(CXXFLAGS) -ggdb3 obj/libhdbpp-cassandra-tests.o -o $@ $(LDFLAGS) \
#	$(DBIMPL_LIB) -Llib -lhdb++cassandra -L${TANGO_LIB} -L${OMNIORB_LIB} \
#	-L/usr/local/lib -ltango -llog4tango -lomniORB4 -lomniDynamic4 \
#	-lCOS4 -lomnithread -lzmq -Wl,-rpath=$(PWD)/lib

clean:
	rm -f obj/*.o lib/*.so* lib/*.a bin/*

lib:
	@mkdir $@
obj:
	@mkdir $@
bin:
	@mkdir $@