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
#include "Log.h"

using namespace std;

namespace HDBPP
{
//=============================================================================
//=============================================================================
bool AttributeCache::find_attr_uuid(const AttributeName &attr_name, CassUuid &uuid)
{
    // First look into the cached attribute
    if(_last_lookup_params != nullptr && attr_name.fully_qualified_attribute_name() == _last_lookup_name)
    {
        uuid = _last_lookup_params->_uuid;
        return true;
    }

    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (result != _attribute_cache.end())
    {
        // cache this lookup
        _last_lookup_params = &((*result).second);
        _last_lookup_name = attr_name.fully_qualified_attribute_name();

        //return the uuid
        uuid = (*result).second._uuid;
        return true;
    }

    return false;
}

//=============================================================================
//=============================================================================
bool AttributeCache::find_attr_ttl(const AttributeName &attr_name, unsigned int &ttl)
{
    // First look into the cached attribute
    if(_last_lookup_params != nullptr && attr_name.fully_qualified_attribute_name() == _last_lookup_name)
    {
        ttl = _last_lookup_params->_ttl;
        return true;
    }

    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (result != _attribute_cache.end())
    {
        // cache this lookup
        _last_lookup_params = &((*result).second);
        _last_lookup_name = attr_name.fully_qualified_attribute_name();        

        // return the ttl
        ttl = (*result).second._ttl;
        return true;
    }

    return false;
}

//=============================================================================
//=============================================================================
bool AttributeCache::update_attr_ttl(const AttributeName &attr_name, unsigned int new_ttl)
{
    // First look into the cached attribute
    if(_last_lookup_params != nullptr && attr_name.fully_qualified_attribute_name() == _last_lookup_name)
    {
        _last_lookup_params->_ttl = new_ttl;
        return true;
    }

    auto result = _attribute_cache.find(attr_name.fully_qualified_attribute_name());

    if (result != _attribute_cache.end())
    {
        // cache this lookup
        _last_lookup_params = &((*result).second);
        _last_lookup_name = attr_name.fully_qualified_attribute_name();        

        // return the ttl
        (*result).second._ttl = new_ttl;
        return true;
    }

    return false;    
}

//=============================================================================
//=============================================================================
bool AttributeCache::cache_attribute(const AttributeName &attr_name, const CassUuid &uuid, unsigned int ttl)
{
    if (_attribute_cache.find(attr_name.fully_qualified_attribute_name()) == _attribute_cache.end())
    {
        _attribute_cache.insert(
                make_pair(attr_name.fully_qualified_attribute_name(), AttributeParams(uuid, ttl)));

        return true;
    }

    return false;
}

//=============================================================================
//=============================================================================
bool AttributeCache::cached(const AttributeName &attr_name) const
{
    return _attribute_cache.find(attr_name.fully_qualified_attribute_name()) == _attribute_cache.end() ? false : true;
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