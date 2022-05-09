LICENSE = "LGPL-3.0+"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e6a600fd5e1d9cbde2d983680233ad02"

inherit cmake

SRC_URI = "git://github.com/I2SE/openv2g.git;protocol=https;branch=0.9.5-improvements"

SRCREV = "64c8a2b994a11ce3476a1958bccdda894df205d8"

PV = "0.9.5+git${SRCPV}"
S = "${WORKDIR}/git"
