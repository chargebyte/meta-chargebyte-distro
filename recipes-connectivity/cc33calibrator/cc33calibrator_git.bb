DESCRIPTION = "TI CC33xx calibrator for WiFi RF tests in production line test (PLT) mode"
HOMEPAGE = "https://git.ti.com/cgit/cc33xx-wlan/cc33xx-utils"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://COPYING;md5=da402cab0cbf8e724c60ca65195cddec"

SRC_URI = "git://git.ti.com/git/cc33xx-wlan/cc33xx-utils.git;protocol=https;branch=master"
SRCREV = "6913b44bf6bff64e8138d12ffbd67f6965df7137"
PV = "1.0.0.50+git${SRCPV}"

S = "${WORKDIR}/git/cc33calibrator"

DEPENDS += "libnl"

inherit cmake pkgconfig
