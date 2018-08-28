# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.12.1] - 2018-08-28

### Fixed

* Bug fix: Stop attempts to bind invalid events (i.e. errors etc), since this raises an exception. We still store the error.

## [0.12.0] - 2018-07-11

### Added

* Prepared statements implemented along with a string cache for the queries.
  * Added as a new class, PreparedStatementsCache + Unit Tests.
* Tango Events are now bound in a separate templated class to improve maintainability and reduce repeated code.
* More unit tests and a means to test database based calls via a simple ad hoc connection.
  * Includes a DbCommands class and a CassandraConnection class.
* AttributeCache class to separate out attribute caching functionality.
  * Also removes code from main class.
* New parameters, store_diag_time, added to interface.

### Changed

* Lots more cleaning and documenting the code, such as todos now showing up in the doxygen documents.
* Unit test binary now located under the build directory.
* Library binary now located under the build directory

## [0.11.0] - 2018-01-05

### Added 

* Consistency configuration parameter added to the library config parameters.

### Changed

* Refactor large HdbPPCassandra constructor.
* Changed shared library output directory to build location (more consistent with cmake).
* Unit test build now requires CMake 3.0.2, since it builds against the tango.pc pkgconfig file.

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
