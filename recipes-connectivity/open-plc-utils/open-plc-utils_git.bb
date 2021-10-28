LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7d83a9e9a9788beb9357262af385f6c7"

SRC_URI = "git://github.com/qca/open-plc-utils.git;protocol=https"

SRCREV = "32408520fcebe785983a68e39ec83830a3005779"

PV = "0.0.6+git${SRCPV}"
S = "${WORKDIR}/git"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

do_compile() {
    make \
        CROSS="${TARGET_PREFIX}" \
        CC="${CC}" \
        LD="${LD}" \
        EXTRA_CFLAGS="${TARGET_CPPFLAGS} ${TARGET_CFLAGS}" \
        LDFLAGS="${TARGET_CFLAGS} ${TARGET_LDFLAGS}"
}

do_install() {
    make install ROOTFS=${D} BIN=${D}/usr/bin MAN=${D}/usr/share/man/man1

    # yeah, a whitelist maybe a whitelist would be better...
    rm -f ${D}/usr/bin/CMEncrypt
    rm -f ${D}/usr/bin/amp*
    rm -f ${D}/usr/bin/config2cfg
    rm -f ${D}/usr/bin/coqos*
    rm -f ${D}/usr/bin/mac2*
    rm -f ${D}/usr/bin/*mdio*
    rm -f ${D}/usr/bin/sada
    rm -f ${D}/usr/bin/sdram
    rm -f ${D}/usr/bin/tty*
    rm -f ${D}/usr/bin/weeder
    rm -f ${D}/usr/bin/xml2pib
    rm -f ${D}/usr/bin/pib2xml
    rm -f ${D}/usr/bin/nics
    rm -f ${D}/usr/bin/ptsctl
}
