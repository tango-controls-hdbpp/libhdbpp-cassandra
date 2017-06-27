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

#ifndef _HDBPP_ATTRIBUTE_NAME_H
#define _HDBPP_ATTRIBUTE_NAME_H

#include <iostream>
#include <string>

namespace HDBPP
{
/**
 * @class AttributeName
 * @ingroup HDBPP-Implementation
 * @brief Represents a fully qualified domain attribute name in libhdbpp-cassandra.
 *
 * The AttributeName class must be primed with a valid fully qualified domain
 * attribute name. From this name the class can extract various fields for the
 * user. Each field is cached, so if it is asked for multiple times we only
 * extract it once. To not degrade performance, we return each cached value
 * as a const reference, rather than value.
 */
class AttributeName
{
public:
    /**
    * @enum AttrNameErrors
    * @brief Represents a range of errors that may occur when calling operations
    * on the AttributeName class
    */
    enum AttrNameErrors
    {
        NoError,
        NoSlashInAttribute,
        OnlyOneSlashInAttribute,
        OnlyTwoSlashesInAttribute,
        TooManySlashesInAttribute,
        EmptyDomainInAttribute,
        EmptyFamilyInAttribute,
        EmptyMemberInAttribute,
        EmptyNameInAttribute
    };

    /**
     * @brief Construct an AttributeName object
     *
     * @todo Check the fqdn_attr_name is correctly formed
     * @param fqdn_attr_name Fully qualified attribute name
     * @throw Tango::DevFailed
    */
    AttributeName(std::string fqdn_attr_name) : _fqdn_attr_name(fqdn_attr_name) {}

    /**
     * @brief Return the fully qualified attribute name the object was created with.
    */
    const std::string &fully_qualified_attribute_name() const;

    /**
     * @brief Return the full attribute name extracted from the fully qualified
     * attribute name.
     *
     * This is of the form domain/family/member/name
     */
    const std::string &full_attribute_name();

    /**
     * @brief Return the tango host extracted from the fully qualified
     * attribute name.
     */
    const std::string &tango_host();

    /**
     * @brief Return the tango host with the domain, i.e. "esrf.fr", appended
     */
    const std::string &tango_host_with_domain();

    /**
     * @brief Attempt to cache the domain/family/member/name values and report
     * any errors encounter.
     *
     * This should be called before using any of the individual functions to access
     * the values it caches, otherwise any errors will go undetected
     */
    AttrNameErrors validate_domain_family_member_name();

    /**
     * @brief Return the domain element of the full attribute name
     */
    const std::string &domain() { return fetch_domain_family_member_name(_domain_cache); }

    /**
     * @brief Return the family element of the full attribute name
     */
    const std::string &family() { return fetch_domain_family_member_name(_family_cache); }

    /**
     * @brief Return the member element of the full attribute name
     */
    const std::string &member() { return fetch_domain_family_member_name(_member_cache); }

    /**
     * @brief Return the name element of the full attribute name
     */
    const std::string &name() { return fetch_domain_family_member_name(_attribute_name_cache); }

    /**
     * @brief Dumps just the fully qualified domain attribute name to the stream
     */
    friend std::ostream &operator<<(std::ostream &os, const AttributeName &attr_name);

private:
    // extract the tango host from the fqdm attribute name, returns unknown
    // if unable to find the tango host
    std::string get_attr_tango_host(const std::string &fqdn_attr_name);

    // extract the full attribute name, i.e. domain/family/member/name
    std::string get_full_attribute_name(const std::string &fqdn_attr_name);

    // takes the fqdn and breaks out the various component parts, such
    // as domain, family etc
    AttrNameErrors set_domain_family_member_name(const std::string &fqdn_attr_name);

    // generic function used to fetch attribute name sub parts, such as
    // family, name etc
    const std::string &fetch_domain_family_member_name(std::string &item);

    // combine the local domain and tango host as a string
    std::string add_domain_to_tango_host(const std::string &tango_host);

    // the fully qualified domain name string
    std::string _fqdn_attr_name;

    // each string is a cache, and generated only once to save
    // on performance.
    std::string _full_attribute_name_cache;
    std::string _tango_host_cache;
    std::string _tango_host_with_domain_cache;
    std::string _domain_cache;
    std::string _family_cache;
    std::string _member_cache;
    std::string _attribute_name_cache;
};

}; // namespace HDBPP

#endif