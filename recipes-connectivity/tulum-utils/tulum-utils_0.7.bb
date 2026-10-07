SUMMARY = "Command line tools for MSE102x chipset interaction"
DESCRIPTION = "hpav_test is simple command-line utility to help users to test MMEs, \
               operate config file, operate nvram, and so on."
HOMEPAGE = "https://github.com/Vertexcom-dev/tulum_utils"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dbc8c49aa6fe32a4b70291423a8264f4"

SRC_URI = "git://github.com/Vertexcom-dev/tulum_utils.git;protocol=https;branch=main"

SRCREV = "6c3ee2875c7cf493e6e697c44c40158ae6b5abad"
PV = "0.7+git${SRCPV}"


DEPENDS = "openssl libpcap zlib"

inherit bash-completion

EXTRA_OEMAKE += "-C hpav_test"

do_install() {
    oe_runmake install DESTDIR=${D}
}

FILES:${PN} = "${sbindir}"
