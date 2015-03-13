
include ../../Make-hdb++.in

CXXFLAGS += -Wall -DRELEASE='"$HeadURL: $ "' \
		-I$(CASSANDRA_INC) -I$(TANGO_INC) -I$(OMNI_INC) -I$(LIBHDB_INC)
CXX = g++


##############################################
# support for shared libray versioning
#
LFLAGS_SONAME = $(CASSANDRA_LIB) -Wl,-soname,
SHLDFLAGS = -shared
BASELIBNAME       =  libhdb++cassandra
SHLIB_SUFFIX = so

#  release numbers for libraries
#
 LIBVERSION    = 3
 LIBRELEASE    = 0
 LIBSUBRELEASE = 0
#

LIBRARY       = $(BASELIBNAME).a
DT_SONAME     = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION)
DT_SHLIB      = $(BASELIBNAME).$(SHLIB_SUFFIX).$(LIBVERSION).$(LIBRELEASE).$(LIBSUBRELEASE)
SHLIB         = $(BASELIBNAME).$(SHLIB_SUFFIX)

# TODO remove these dependencies?
LIBHDBIMPL_INC = ../../libhdb++cassandra/trunk/src
TANGO_DIR ?= /segfs/tango/release/debian7
RUNTIME_DIR ?= /opt/dserver

OMNIORB_INC = ${OMNIORB_DIR}/include
RUNTIME_INC = ${RUNTIME_DIR}/include

INC_DIR = -I${TANGO_INC} -I${OMNIORB_INC} -I${RUNTIME_INC}

TANGO_LIB = ${TANGO_DIR}/lib
OMNIORB_LIB = ${OMNIORB_DIR}/lib
RUNTIME_LIB = ${RUNTIME_DIR}/lib

.PHONY : install clean

lib/LibHdb++cassandra: lib obj obj/LibHdb++Cassandra.o
	$(CXX) obj/LibHdb++Cassandra.o $(SHLDFLAGS) $(LFLAGS_SONAME)$(DT_SONAME) -o lib/$(DT_SHLIB)
	ln -sf $(DT_SHLIB) lib/$(SHLIB)
	ln -sf $(SHLIB) lib/$(DT_SONAME)
	ar rcs lib/$(LIBRARY) obj/LibHdb++Cassandra.o

obj/LibHdb++Cassandra.o: src/LibHdb++Cassandra.cpp src/LibHdb++Cassandra.h $(LIBHDB_INC)/LibHdb++.h
	$(CXX) $(CXXFLAGS) -fPIC -c src/LibHdb++Cassandra.cpp -o $@

obj/TestFindAttrIdType.o: test/TestFindAttrIdType.cpp $(LIBHDBIMPL_INC)/LibHdb++Cassandra.h
	$(CXX) $(CXXFLAGS) -ggdb3 -I$(LIBHDBIMPL_INC) -c -o $@ $<

test/TestFindAttrIdType: obj/TestFindAttrIdType.o lib/LibHdb++cassandra
	$(CXX) $(CXXFLAGS) -ggdb3 obj/TestFindAttrIdType.o -o $@ $(LDFLAGS) -l$(LIBHDBIMPL) -L$(LIBHDBIMPL_LIB) -L${TANGO_LIB} -L${OMNIORB_LIB} -L${RUNTIME_LIB} -L/usr/local/lib -ltango -llog4tango -lomniORB4 -lomniDynamic4 \
	-lCOS4 -lomnithread -lzmq

clean:
	rm -f obj/*.o lib/*.so* lib/*.a test/TestFindAttrIdType

lib obj:
	@mkdir $@


