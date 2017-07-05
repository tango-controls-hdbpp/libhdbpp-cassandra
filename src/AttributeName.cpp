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

#include "AttributeName.h"
#include "LibHdb++Defines.h"
#include "Log.h"
#include "tango.h"

#include <cstring>
#include <netdb.h> //for getaddrinfo

using namespace std;

namespace HDBPP
{
//=============================================================================
/**
 * This is the root function, so we run a check on the _fqdn_attr_name variable,
 * if it is not valid we throw an exception. This is a severe error case, since
 * this class should not be initialised without a _fqdn_attr_name.
 **/
//=============================================================================
const std::string &AttributeName::fully_qualified_attribute_name() const
{
    // TODO we could somehow force a check that the _fqdn_attr_name is actually valid
    // perhaps in the constructor
    if (_fqdn_attr_name.empty())
    {
        stringstream error_desc;

        error_desc << "ERROR _fqdn_attr_name attribute name parameter not set. Must be set before use"
                   << ends;

        LOG(Error) << error_desc.str() << endl;
        Tango::Except::throw_exception(EXCEPTION_TYPE_ATTR_NAME, error_desc.str().c_str(), __func__);
    }

    return _fqdn_attr_name;
}

//=============================================================================
//=============================================================================
const string &AttributeName::tango_host()
{
    if (_tango_host_cache.empty())
        _tango_host_cache = get_attr_tango_host(fully_qualified_attribute_name());

    return _tango_host_cache;
}

//=============================================================================
//=============================================================================
const string &AttributeName::tango_host_with_domain()
{
    if (_tango_host_with_domain_cache.empty())
        _tango_host_with_domain_cache = add_domain_to_tango_host(tango_host());

    return _tango_host_with_domain_cache;
}

//=============================================================================
//=============================================================================
const string &AttributeName::full_attribute_name()
{
    if (_full_attribute_name_cache.empty())
        _full_attribute_name_cache = get_full_attribute_name(fully_qualified_attribute_name());

    return _full_attribute_name_cache;
}

//=============================================================================
//=============================================================================
AttributeName::AttrNameErrors AttributeName::validate_domain_family_member_name()
{
    // we validate the various strings by generating them and returning the
    // error value (if any)
    return set_domain_family_member_name(full_attribute_name());
}

//=============================================================================
//=============================================================================
const string &AttributeName::fetch_domain_family_member_name(string &item)
{
    // we assume the user has called validate_domain_family_member() to ensure
    // the extraction is valid before trying to access the item
    if (item.empty())
        set_domain_family_member_name(full_attribute_name());

    return item;
}

//=============================================================================
//=============================================================================
AttributeName::AttrNameErrors AttributeName::set_domain_family_member_name(const string &full_attr_name)
{
    string::size_type first_slash = full_attr_name.find("/");

    if (first_slash == string::npos)
    {
        LOG(Error) << "(" << full_attr_name << "): Error: there is no slash in attribute name" << endl;
        return AttrNameErrors::NoSlashInAttribute;
    }

    string::size_type second_slash = full_attr_name.find("/", first_slash + 1);

    if (second_slash == string::npos)
    {
        LOG(Error) << "(" << full_attr_name << "): Error: there is only one slash in attribute name"
                   << endl;

        return AttrNameErrors::OnlyOneSlashInAttribute;
    }

    string::size_type third_slash = full_attr_name.find("/", second_slash + 1);

    if (third_slash == string::npos)
    {
        LOG(Error) << "(" << full_attr_name
                   << "): Error: there are only two slashes in attribute name" << endl;

        return AttrNameErrors::OnlyTwoSlashesInAttribute;
    }

    string::size_type last_slash = full_attr_name.rfind("/");

    if (last_slash != third_slash)
    {
        // Too many slashes provided!
        LOG(Error) << "(" << full_attr_name << "): Too many slashes provided in attribute name" << endl;
        return AttrNameErrors::TooManySlashesInAttribute;
    }

    if (first_slash == 0)
    {
        // empty domain
        LOG(Error) << "(" << full_attr_name << "): empty domain" << endl;
        return AttrNameErrors::EmptyDomainInAttribute;
    }

    if (second_slash - first_slash - 1 == 0)
    {
        // empty family
        LOG(Error) << "(" << full_attr_name << "): empty family" << endl;
        return AttrNameErrors::EmptyFamilyInAttribute;
    }

    if (third_slash - second_slash - 1 == 0)
    {
        // empty member
        LOG(Error) << "(" << full_attr_name << "): empty member" << endl;
        return AttrNameErrors::EmptyMemberInAttribute;
    }

    if (third_slash + 1 == full_attr_name.length())
    {
        // empty atribute name
        LOG(Error) << "(" << full_attr_name << "): empty attribute name" << endl;
        return AttrNameErrors::EmptyNameInAttribute;
    }

    _domain_cache = full_attr_name.substr(0, first_slash);
    _family_cache = full_attr_name.substr(first_slash + 1, second_slash - first_slash - 1);
    _member_cache = full_attr_name.substr(second_slash + 1, third_slash - second_slash - 1);
    _attribute_name_cache = full_attr_name.substr(third_slash + 1);

    return AttrNameErrors::NoError;
}

//=============================================================================
//=============================================================================
string AttributeName::get_attr_tango_host(const string &fqdn_attr_name)
{
    string::size_type start = fqdn_attr_name.find("tango://");

    if (start == string::npos)
        start = 0;
    else
        start = 8; // tango:// len

    string::size_type end = fqdn_attr_name.find('/', start);
    return fqdn_attr_name.substr(start, end - start);
}

//=============================================================================
//=============================================================================
string AttributeName::get_full_attribute_name(const string &fqdn_attr_name)
{
    string::size_type start = fqdn_attr_name.find("tango://");

    if (start == string::npos)
        start = 0;
    else
        start = 8; // tango:// len

    start = fqdn_attr_name.find('/', start);
    start++;
    return fqdn_attr_name.substr(start);
}

//=============================================================================
//=============================================================================
string AttributeName::add_domain_to_tango_host(const string &tango_host)
{
    string::size_type end1 = tango_host.find(".");

    if (end1 == string::npos)
    {
        // get host name without tango://
        string::size_type start = tango_host.find("tango://");

        if (start == string::npos)
            start = 0;
        else
            start = 8; // tango:// len

        string::size_type end2 = tango_host.find(":", start);

        string th = tango_host.substr(start, end2);
        string with_domain = tango_host;

        struct addrinfo hints;
        memset(&hints, 0, sizeof hints);
        hints.ai_family = AF_UNSPEC; /*either IPV4 or IPV6*/
        hints.ai_socktype = SOCK_STREAM;
        hints.ai_flags = AI_CANONNAME;

        struct addrinfo *result, *rp;

        int ret = getaddrinfo(th.c_str(), NULL, &hints, &result);

        if (ret != 0)
        {
            LOG(Error) << "Error: getaddrinfo: " << gai_strerror(ret) << endl;
            return tango_host;
        }

        for (rp = result; rp != NULL; rp = rp->ai_next)
        {
            with_domain = string(rp->ai_canonname) + tango_host.substr(end2);
            LOG(Debug) << "Found domain: " << with_domain << endl;
        }

        freeaddrinfo(result); // all done with this structure
        return with_domain;
    }

    return tango_host;
}

//=============================================================================
//=============================================================================
ostream &operator<<(ostream &os, const AttributeName &attr_name)
{
    os << attr_name.fully_qualified_attribute_name();
    return os;
}
}