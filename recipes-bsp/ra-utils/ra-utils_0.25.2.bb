SUMMARY = "Command line tools and library for interaction with chargebyte's safety controller"
DESCRIPTION = "The repository contains some command line tools for operating the so called \
               safety controller on chargebyte's products like Charge SOM."
HOMEPAGE = "https://github.com/chargebyte/ra-utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCREV = "ec36b9d44c5045f072903fbab47de75126d132b0"
PV = "0.25.2+git${SRCPV}"

SRC_URI = "git://github.com/chargebyte/ra-utils.git;protocol=https;branch=main"

inherit bash-completion cmake pkgconfig systemd

DEPENDS = "libgpiod libyaml systemd"

# required for factory reset
RDEPENDS:${PN} += "systemd-analyze"

PACKAGES =+ "${PN}-fw-chargesom ${PN}-fw-parsley"

SYSTEMD_SERVICE:${PN} = "ra-update@ttyLP1.service ra-update@ttyLP2.service"

FILES:${PN} += "${libexecdir}"
FILES:${PN} += "${systemd_system_unitdir}"
FILES:${PN} += "${libdir}/tmpfiles.d"

FILES:${PN}-fw-chargesom = "${datadir}/${PN}/chargesom_*"
FILES:${PN}-fw-parsley = "${datadir}/${PN}/parsley_*"
