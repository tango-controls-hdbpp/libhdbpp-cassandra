LIBHDBPP_DIR = .libhdbpp
LIBHDBPP_INC = ./$(LIBHDBPP_DIR)/src
MAKE_INC = .hdbpp-common

DBIMPL_INC = ../../cassandra/cppDriver/cpp-driver/include
DBIMPL_LIB = -L../../cassandra/cppDriver/cpp-driver/lib/libuv/lib \
             -L../../cassandra/cppDriver/cpp-driver \
             -lcassandra -luv
             
include ./$(MAKE_INC)/Make-hdbpp.in

CXXFLAGS += -std=gnu++0x -Wall -DRELEASE='"$HeadURL$ "' $(DBIMPL_INC) $(INC_DIR) -I$(LIBHDBPP_INC)


##############################################
# support for shared libray versioning
#
LFLAGS_SONAME = $(DBIMPL_LIB) -Wl,-soname,
SHLDFLAGS = -shared
BASELIBNAME       =  libhdb++cassandra
SHLIB_SUFFIX = so

#  release numbers for libraries
#
 LIBVERSION    = 6
 LIBRELEASE    = 0
 LIBSUBRELEASE = 0
#

LIBRARY       = $(BASELIBNAME).a
DT_SONAME     = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION)
DT_SHLIB      = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION).$(LIBRELEASE).$(LIBSUBRELEASE)
SHLIB         = $(BASELIBNAME).$(SHLIB_SUFFIX)

# TODO remove these dependencies?
TANGO_DIR ?= /segfs/tango/release/debian7
RUNTIME_DIR ?= /opt/dserver

OMNIORB_INC = ${OMNIORB_DIR}/include

INC_DIR = -I${TANGO_INC} -I${OMNIORB_INC}

TANGO_LIB = ${TANGO_DIR}/lib
OMNIORB_LIB = ${OMNIORB_DIR}/lib

.PHONY : install clean

lib/LibHdb++cassandra: lib obj obj/LibHdb++Cassandra.o
	$(CXX) obj/LibHdb++Cassandra.o $(SHLDFLAGS) $(LFLAGS_SONAME)$(DT_SONAME) -o lib/$(DT_SHLIB)
	ln -sf $(DT_SHLIB) lib/$(SHLIB)
	ln -sf $(SHLIB) lib/$(DT_SONAME)
	ar rcs lib/$(LIBRARY) obj/LibHdb++Cassandra.o

obj/LibHdb++Cassandra.o: src/LibHdb++Cassandra.cpp src/LibHdb++Cassandra.h $(LIBHDBPP_INC)/LibHdb++.h
	$(CXX) $(CXXFLAGS) -fPIC -c src/LibHdb++Cassandra.cpp -o $@

obj/TestFindAttrIdType.o: test/TestFindAttrIdType.cpp src/LibHdb++Cassandra.h
	$(CXX) $(CXXFLAGS) -ggdb3 -Isrc -c -o $@ $<

test/TestFindAttrIdType: obj/TestFindAttrIdType.o lib/LibHdb++cassandra
	$(CXX) $(CXXFLAGS) -ggdb3 obj/TestFindAttrIdType.o -o $@ $(LDFLAGS) \
	-l$(LIBHDBIMPL) -L$(LIBHDBIMPL_LIB) -L${TANGO_LIB} -L${OMNIORB_LIB} \
	-L/usr/local/lib -ltango -llog4tango -lomniORB4 -lomniDynamic4 \
	-lCOS4 -lomnithread -lzmq

clean:
	rm -f obj/*.o lib/*.so* lib/*.a test/TestFindAttrIdType

lib obj:
	@mkdir $@


