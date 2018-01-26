/* Copyright (C) : 2014-2017
   European Synchrotron Radiation Facility
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

#ifndef _HDBPP_ATTRIBUTE_CACHE_H
#define _HDBPP_ATTRIBUTE_CACHE_H

#include "AttributeName.h"

#include <cassandra.h>
#include <iostream>
#include <string>
#include <unordered_map>

namespace HDBPP
{
/**
 * @class AttributeCache
 * @ingroup HDBPP-Implementation
 * @brief Operate a small cache for small frequently used attribute values.
 *
 * Using a small cache like this for some fixed values stops excessive database
 * look ups, and gives a small optimisation.
 */
class AttributeCache
{
public:

    /**
     * @brief Construct an AttributeCache object
    */
    AttributeCache() { }

    /**
     * @brief Lookup the attribute uuid in the cache
     * @return True if found, false otherwise
    */
    bool find_attr_uuid(const AttributeName &attr_name, CassUuid &uuid);

    /**
     * @brief Lookup the attribute TTL in the cache
     * @return True if found, false otherwise
    */
    bool find_attr_ttl(const AttributeName &attr_name, unsigned int &ttl);

    /**
     * @brief Lookup the attribute TTL in the cache
     * @return True if successful, false otherwise
    */
    bool update_attr_ttl(const AttributeName &attr_name, unsigned int new_ttl);

    /**
     * @brief Cache an attributes uuid and ttl
     * @return True if cached ok, false otherwise
    */
    bool cache_attribute(const AttributeName &attr_name, const CassUuid &uuid, unsigned int ttl);

    /**
     * @brief Check if an attribute is cached
     * @return True if cached, false otherwise
    */
    bool cached(const AttributeName &attr_name) const;

    /**
     * @brief Check cache size
     * @return Cache size
    */
    int cache_size() const { return _attribute_cache.size(); }

    /**
     * @brief Dumps some details about the cache
     */
    friend std::ostream &operator<<(std::ostream &os, const AttributeCache &attr_cache);

private:

    // Parameters for an attribute that can be cached. Mapped to the attribute name
    // in a map below.
    struct AttributeParams
    {
        AttributeParams(const CassUuid &uuid, unsigned int ttl) : _uuid(uuid), _ttl(ttl) { }
        CassUuid _uuid;
        unsigned int _ttl;
    };

    // Like the statement cache, use an unordered map, its got the best look up
    // times, and we do not plan to do any insertions/deletions
    std::unordered_map<std::string, AttributeParams> _attribute_cache;

    // This is a cache into the cache, the last looked up attribute
    // is cached, this is to save a second search if we retrieve, so example,
    // multiple pieces of data about a single attribute
    AttributeParams *_last_lookup_params;
    std::string _last_lookup_name;
};

}; // namespace HDBPP

#endif // _HDBPP_ATTRIBUTE_CACHE_H