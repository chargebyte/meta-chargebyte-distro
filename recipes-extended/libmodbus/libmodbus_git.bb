SUMMARY = "A Modbus library"
DESCRIPTION = "libmodbus is a C library designed to provide a fast and robust \
implementation of the Modbus protocol. It runs on Linux, Mac OS X, FreeBSD, \
QNX and Windows."
HOMEPAGE = "http://www.libmodbus.org/"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=4fbd65380cdd255951079008b364516c"

inherit autotools pkgconfig

SRC_URI = "git://github.com/I2SE/libmodbus.git;protocol=https;branch=i2se-master"

SRCREV = "e21d0594092727de07132573b36987fc4b77bf60"

PV = "3.1.4+git${SRCPV}"
S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "1"
