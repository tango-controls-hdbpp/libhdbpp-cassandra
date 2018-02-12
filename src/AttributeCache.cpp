/* Copyright (C) : 2014-2017
   European Synchrotron Radiation Radiation Facility
   BP 220, Grenoble 38043, FRANCE

   This file is part of libhdb++cassandra.

   libhdb++cassandra is free software: you can redistribute it and/or modify
   it under the terms of the Lesser GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   libhdb++cassandra is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Lesser
   GNU General Public License for more details.

   You should have received a copy of the Lesser GNU General Public License
   along with libhdb++cassandra.  If not, see <http://www.gnu.org/licenses/>. */

#include "AttributeCache.h"
#include "LibHdb++Defines.h"
#include "Log.h"
#include <tango.h>

using namespace std;
using namespace Utils;

namespace HDBPP
{
//=============================================================================
//=============================================================================
CassUuid AttributeCache::find_attr_uuid(const AttributeName &attr_name)
{
    TRACE_LOGGER;
    LOG(Debug) << "Requesting uuid for attr: " << attr_name.fully_qualified_attribute_name() << endl;

    // First look into the cached attribute
    if(_last_lookup_params != nullptr && attr_name.fully_qualified_attribute_name() == _last_lookup_name)
        return _last_lookup_params->_uuid;

    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (result == _attribute_cache.end())
    {
        stringstream error_desc;
        error_desc << "Error: no cached uuid for attribute: " << attr_name.fully_qualified_attribute_name() << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_ATTR_CACHE, error_desc.str().c_str(), __func__);
    }

    // cache this lookup
    _last_lookup_params = &((*result).second);
    _last_lookup_name = attr_name.fully_qualified_attribute_name();

    //return the uuid
    return (*result).second._uuid;
}

//=============================================================================
//=============================================================================
unsigned int AttributeCache::find_attr_ttl(const AttributeName &attr_name)
{
    TRACE_LOGGER;
    LOG(Debug) << "Requesting ttl for attr: " << attr_name.fully_qualified_attribute_name() << endl;

    // First look into the cached attribute
    if(_last_lookup_params != nullptr && attr_name.fully_qualified_attribute_name() == _last_lookup_name)
        return _last_lookup_params->_ttl;

    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (result == _attribute_cache.end())
    {
        stringstream error_desc;
        error_desc << "Error: no cached ttl for attribute: " << attr_name.fully_qualified_attribute_name() << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_ATTR_CACHE, error_desc.str().c_str(), __func__);
    }

    // cache this lookup
    _last_lookup_params = &((*result).second);
    _last_lookup_name = attr_name.fully_qualified_attribute_name();        

    // return the ttl
    return (*result).second._ttl;
}

//=============================================================================
//=============================================================================
void AttributeCache::update_attr_ttl(const AttributeName &attr_name, unsigned int new_ttl)
{
    TRACE_LOGGER;
    LOG(Debug) << "Updating ttl for attr: " << attr_name.fully_qualified_attribute_name() << endl;

    // First look into the cached attribute
    if(_last_lookup_params != nullptr && attr_name.fully_qualified_attribute_name() == _last_lookup_name)
        _last_lookup_params->_ttl = new_ttl;

    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (result == _attribute_cache.end())
    {
        stringstream error_desc;
        error_desc << "Error: attribute is not cached: " << attr_name.fully_qualified_attribute_name() << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_ATTR_CACHE, error_desc.str().c_str(), __func__);
    }

    // cache this lookup
    _last_lookup_params = &((*result).second);
    _last_lookup_name = attr_name.fully_qualified_attribute_name();        

    LOG(Debug) << "Updated addtribute: " << attr_name << " ttl from: " << (*result).second._ttl 
               << " to: " << new_ttl << endl;    

    // update the ttl
    (*result).second._ttl = new_ttl;
}

//=============================================================================
//=============================================================================
void AttributeCache::cache_attribute(const AttributeName &attr_name, const CassUuid &uuid, unsigned int ttl)
{
    TRACE_LOGGER;

    if (_attribute_cache.find(attr_name.fully_qualified_attribute_name()) != _attribute_cache.end())
    {
        stringstream error_desc;
        error_desc << "Error: attribute is already cached: " << attr_name.fully_qualified_attribute_name() << ends;
        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_ATTR_CACHE, error_desc.str().c_str(), __func__);
    }

    _attribute_cache.insert(
            make_pair(attr_name.fully_qualified_attribute_name(), AttributeParams(uuid, ttl)));

    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    // cache this attribute for quick lookup
    _last_lookup_params = &((*result).second);
    _last_lookup_name = attr_name.fully_qualified_attribute_name();

    char uuid_str[CASS_UUID_STRING_LENGTH];
    cass_uuid_string(uuid, uuid_str);

    LOG(Debug) << "Cached addtribute: " << attr_name << " to cache with uuid: " << uuid_str 
               << " and ttl: " << ttl << endl;    
}

//=============================================================================
//=============================================================================
bool AttributeCache::cached(const AttributeName &attr_name)
{
    TRACE_LOGGER;
    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (result == _attribute_cache.end())
        return false;

    // cache this attribute for quick lookup
    _last_lookup_params = &((*result).second);
    _last_lookup_name = attr_name.fully_qualified_attribute_name();  

    return true;
}

//=============================================================================
//=============================================================================
ostream &operator<<(ostream &os, const AttributeCache &attr_cache)
{
    os << "cache size:  " << attr_cache.cache_size() << " "
       << "_last_lookup_params set: " 
       << (attr_cache._last_lookup_params == nullptr ? "false" : attr_cache._last_lookup_name);

    return os;
}
}