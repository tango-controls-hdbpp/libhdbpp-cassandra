# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

* Prepared statements implemented along with a string cache for the queries. This will speed up many repeated database calls.
* Tango Events are now bound in a separate templated class to improve maintainability and reduce repeated code.
* More unit tests and a mean to test database based calls via a simple ad hoc connection to a cassandra database. This infrastructure can open a connection to a cassandra cluster and prime the database ready for use with the create_hdb_cassandra.cql file.
* Lots more cleaning and documenting the code, such as todos now showing up in the doxygen documents.

## [0.10.0] - 2017-09-13

### Added

* CHANGELOG.md file.
* INSTALL.md file.
* LICENCE file.
* Debian Package build files under debian/
* CMake build and configuration files.

### Changed

* Moved build system from Make to CMake
* README.md - Added lots of new information.
* When including `libhdb++.h` and `tango.h` use the correct path

### Removed

* Makefile build system
* Recursive link to libhdbpp
