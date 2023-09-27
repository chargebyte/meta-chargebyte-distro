SUMMARY = "Python USBTMC driver for controlling instruments over USBTwisted Web Sockets"
HOMEPAGE = "https://github.com/python-ivi/python-usbtmc"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=28349e620c063ac78d1d51ced232e42a"

SRC_URI = "git://github.com/python-ivi/python-usbtmc.git;branch=master;protocol=https"
SRCREV = "d9bfb20b2ef002da787adb6b093e1679705c00e2"

inherit setuptools3

S = "${WORKDIR}/git"

RDEPENDS:${PN} += "python3-pyusb"

BBCLASSEXTEND = "native nativesdk"
