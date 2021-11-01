LICENSE = "LGPL-3.0+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=e6a600fd5e1d9cbde2d983680233ad02"

DEPENDS = "libgcrypt libgpg-error libxml2"

inherit autotools pkgconfig

RDEPENDS_${PN} += "libgcrypt libgpg-error libxml2"

SRC_URI = "git://github.com/I2SE/libcrypti2c.git;protocol=https;branch=improvements"

SRCREV = "2539088ac985faafb6a0585dba348cbfa4645c08"

PV = "0.4.0+git${SRCPV}"
S = "${WORKDIR}/git"

# prevent a QA issue
do_install() {
    oe_runmake 'DESTDIR=${D}' install

    rm -rf ${D}/usr/share
}
