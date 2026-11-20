SUMMARY = "Enhanced config file parser"
DESCRIPTION = "A library to parse key=value configuration files and merge settings from multiple locations."
HOMEPAGE = "https://github.com/openSUSE/libeconf"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a79727d51b52871d7fc9413a06e19eaa"

SRC_URI = "https://github.com/openSUSE/libeconf/archive/refs/tags/v${PV}.tar.gz;downloadfilename=${BP}.tar.gz"
SRC_URI[sha256sum] = "ebbddde55c6fb9eb141fca03d0f3c251282ded60351f43933d83e2e2e0bff6de"

inherit cmake

EXTRA_OECMAKE = " \
    -DBUILD_SHARED_LIBS=ON \
    -DBUILD_TESTS=OFF \
    -DBUILD_EXAMPLES=OFF \
    -DBUILD_DOCUMENTATION=OFF \
"

PACKAGES =+ "${PN}-tools"
FILES:${PN}-tools = "${bindir}/econftool"
