SUMMARY = "RPC library for C++"
DESCRIPTION = "rpclib is a RPC library for C++, providing both a client and server implementation \
               using modern C++14 without code generation."
HOMEPAGE = "http://rpclib.net/"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=d49aa44c8300837bd95e0d345ce50fef"

SRC_URI = "git://github.com/rpclib/rpclib;branch=master;protocol=https \
           file://0001-Set-library-properties-to-fix-Yocto-s-QA-error.patch \
           file://0001-Add-possibility-to-obtain-local-used-client-IP-addre.patch"
SRCREV = "a663a1598a4b419123b2e13c0ae6a39c91dcf5b8"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

EXTRA_OECMAKE += " \
    -DBUILD_SHARED_LIBS=ON \
    -DRPCLIB_BUILD_TESTS=OFF \
    -DRPCLIB_GENERATE_COMPDB=OFF \
    -DRPCLIB_BUILD_EXAMPLES=OFF \
    -DRPCLIB_ENABLE_LOGGING=OFF \
    -DRPCLIB_ENABLE_COVERAGE=OFF \
    -DRPCLIB_CXX_STANDARD=14 \
    "
