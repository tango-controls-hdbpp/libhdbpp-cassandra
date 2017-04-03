LIBHDBPP_DIR = .libhdbpp
LIBHDBPP_INC = ./$(LIBHDBPP_DIR)/src

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

CXXFLAGS += -std=gnu++0x -Wall -DRELEASE='"$HeadURL$ "' -I$(DBIMPL_INC) $(INC_DIR) -I$(LIBHDBPP_INC)

##############################################
# support for shared libray versioning
#
LFLAGS_SONAME = $(DBIMPL_LIB) -Wl,-soname,
SHLDFLAGS = -shared
BASELIBNAME =  libhdb++cassandra
SHLIB_SUFFIX = so

#  release numbers for libraries
#
 LIBVERSION    = 6
 LIBRELEASE    = 1
 LIBSUBRELEASE = 0
#

LIBRARY       = $(BASELIBNAME).a
DT_SONAME     = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION)
DT_SHLIB      = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION).$(LIBRELEASE).$(LIBSUBRELEASE)
SHLIB         = $(BASELIBNAME).$(SHLIB_SUFFIX)

TANGO_LIB = ${TANGO_DIR}/lib
OMNIORB_LIB = ${OMNIORB_DIR}/lib

.PHONY : clean

lib/LibHdb++cassandra: lib obj obj/LibHdb++Cassandra.o
	$(CXX) obj/LibHdb++Cassandra.o $(SHLDFLAGS) $(LFLAGS_SONAME)$(DT_SONAME) -o lib/$(DT_SHLIB)
	ln -sf $(DT_SHLIB) lib/$(SHLIB)
	ln -sf $(SHLIB) lib/$(DT_SONAME)
	ar rcs lib/$(LIBRARY) obj/LibHdb++Cassandra.o

obj/LibHdb++Cassandra.o: src/LibHdb++Cassandra.cpp src/LibHdb++Cassandra.h $(LIBHDBPP_INC)/LibHdb++.h
	$(CXX) $(CXXFLAGS) -fPIC -c src/LibHdb++Cassandra.cpp -o $@

tests: bin/TestFindAttrIdType bin/Test_updateTTL_Attr

obj/TestFindAttrIdType.o: test/TestFindAttrIdType.cpp src/LibHdb++Cassandra.h
	$(CXX) $(CXXFLAGS) -ggdb3 -Isrc -c -o $@ $<

obj/Test_updateTTL_Attr.o: test/Test_updateTTL_Attr.cpp src/LibHdb++Cassandra.h
	$(CXX) $(CXXFLAGS) -ggdb3 -Isrc -c -o $@ $<

bin/TestFindAttrIdType: bin obj/TestFindAttrIdType.o lib/LibHdb++cassandra
	$(CXX) $(CXXFLAGS) -ggdb3 obj/TestFindAttrIdType.o -o $@ $(LDFLAGS) \
	$(DBIMPL_LIB) -Llib -lhdb++cassandra -L${TANGO_LIB} -L${OMNIORB_LIB} \
	-L/usr/local/lib -ltango -llog4tango -lomniORB4 -lomniDynamic4 \
	-lCOS4 -lomnithread -lzmq -Wl,-rpath=$(PWD)/lib

bin/Test_updateTTL_Attr: bin obj/Test_updateTTL_Attr.o lib/LibHdb++cassandra
	$(CXX) $(CXXFLAGS) -ggdb3 obj/Test_updateTTL_Attr.o -o $@ $(LDFLAGS) \
	$(DBIMPL_LIB) -Llib -lhdb++cassandra -L${TANGO_LIB} -L${OMNIORB_LIB} \
	-L/usr/local/lib -ltango -llog4tango -lomniORB4 -lomniDynamic4 \
	-lCOS4 -lomnithread -lzmq -Wl,-rpath=$(PWD)/lib

clean:
	rm -f obj/*.o lib/*.so* lib/*.a bin/*

lib:
	@mkdir $@
obj:
	@mkdir $@
bin:
	@mkdir $@
