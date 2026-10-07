#
# This file was derived from the 'Hello World!' example recipe in the
# Yocto Project Development Manual.
#
SUMMARY = "MAC address manipulation utility"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://main.c;beginline=6;endline=8;md5=b96cea784b55f1ab01c0decb28e7938b"

SRC_URI = "file://main.c"

S = "${UNPACKDIR}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} main.c -o maccalc
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 maccalc ${D}${bindir}
}
