SUMMARY = "Command line tools and library for interaction with chargebyte's safety controller"
DESCRIPTION = "The repository contains some command line tools for operating the so called \
               safety controller on chargebyte's products like Charge SOM."
HOMEPAGE = "https://github.com/chargebyte/ra-utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCREV = "4f4c4198a207f1cda82a98975ef17df929806c2d"
PV = "0.3.0+git${SRCPV}"

SRC_URI = "git://github.com/chargebyte/ra-utils.git;protocol=https;branch=main"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

DEPENDS = "libgpiod"

do_install:append() {
    # rename the directory to match our package name
    mv "${D}${datadir}/ra-utils" "${D}${datadir}/csom-safety-fw"
}

FILES:${PN} += "${datadir}"
