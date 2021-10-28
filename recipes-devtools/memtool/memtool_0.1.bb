#
# This file was derived from the 'Hello World!' example recipe in the
# Yocto Project Development Manual.
#
SUMMARY = "Freescale's memtool command line tool"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://memtool.c;md5=b8124e34443f601f4be940bf9c6a32eb"

SRC_URI = "file://memtool.c"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${LDFLAGS} memtool.c -o memtool
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 memtool ${D}${sbindir}
}
