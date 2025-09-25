SUMMARY = "Libraries to ease access to device baptism data"

DESCRIPTION = "This package contains a library and tool to read \
and modify an embedded device's baptism data."

HOMEPAGE = "https://github.com/mhei/libbaptismdata"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSES/LGPL-2.1-or-later.txt;md5=4fbd65380cdd255951079008b364516c"
SECTION = "libs"

SRC_URI = "https://github.com/mhei/${BPN}/releases/download/v${PV}/${BPN}-${PV}.tar.xz"
SRC_URI[sha256sum] = "43eeb1fd8f5cdbf9fd293c772cbdea3b5850012e08c37bbbb5e99409146c2774"

inherit autotools pkgconfig

DEPENDS = "libubootenv"

RDEPENDS:${PN} += "libubootenv"
