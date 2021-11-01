LICENSE = "LGPL-3.0+"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e6a600fd5e1d9cbde2d983680233ad02"

inherit cmake

SRC_URI = "git://github.com/I2SE/openv2g.git;protocol=https;branch=i2se-devel-master"

SRCREV = "b625f152b393cdbc0d1e8300b41be41e22ebbd7a"

PV = "0.9.4+git${SRCPV}"
S = "${WORKDIR}/git"
