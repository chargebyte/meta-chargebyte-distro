LICENSE = "CLOSED"

SRCREV = "20"
PV = "10.0.10.1+r${SRCREV}"

SRC_URI = "svn2://i2se-dev/svn/hc08sprg/trunk;protocol=http;module=${PN} \
           file://010_cross-compile.patch"

do_compile() {
    make -C ${S}/src
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 src/hc08sprg ${D}${bindir}
}
