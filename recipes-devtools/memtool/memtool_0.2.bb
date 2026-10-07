#
# This file was derived from the 'Hello World!' example recipe in the
# Yocto Project Development Manual.
#
SUMMARY = "Freescale's memtool command line tool"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://memtool.c;beginline=1;endline=12;md5=f2e470b1341efde8c526dae4f7d9b8a9"

SRC_URI = "file://memtool.c"

S = "${UNPACKDIR}"

do_compile() {
    ${CC} ${LDFLAGS} memtool.c -o memtool
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 memtool ${D}${sbindir}
}
