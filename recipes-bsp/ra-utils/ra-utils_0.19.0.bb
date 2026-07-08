SUMMARY = "Command line tools and library for interaction with chargebyte's safety controller"
DESCRIPTION = "The repository contains some command line tools for operating the so called \
               safety controller on chargebyte's products like Charge SOM."
HOMEPAGE = "https://github.com/chargebyte/ra-utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCREV = "23740e96ec4b2c166e55842dcd8f38d1873181d9"
PV = "0.19.0+git${SRCPV}"

SRC_URI = "git://github.com/chargebyte/ra-utils.git;protocol=https;branch=main"
S = "${WORKDIR}/git"

inherit cmake pkgconfig systemd

DEPENDS = "libgpiod libyaml"

PACKAGES =+ "${PN}-fw-chargesom ${PN}-fw-parsley"

SYSTEMD_SERVICE:${PN} = "ra-update@ttyLP1.service ra-update@ttyLP2.service"

FILES:${PN} += "${datadir}"
FILES:${PN} += "${systemd_system_unitdir}"

FILES:${PN}-fw-chargesom = "${datadir}/${PN}/chargesom_*"
FILES:${PN}-fw-parsley = "${datadir}/${PN}/parsley_*"
