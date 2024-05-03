SUMMARY = "A tina SNMP server for embedded systems"
HOMEPAGE = "https://github.com/troglobit/mini-snmpd"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=8f41487e236b91bf454621d635c4a28f"

SRC_URI = "https://github.com/troglobit/mini-snmpd/releases/download/v${PV}/${BPN}-${PV}.tar.gz"
SRC_URI[md5sum] = "f25c385b840f5f07152c62e868ae3349"
SRC_URI[sha256sum] = "77bc704a4ed4fdc386e2ba2e972d9457564c84abef7e9af5de5a2a231e5a9efe"

SRC_URI += " \
    file://mini-snmpd \
    file://mini-snmpd.service.in.patch \
    file://cc-mini-snmpd-env \
"

DEPENDS = "systemd"

inherit autotools pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "${BPN}.service"
SYSTEMD_AUTO_ENABLE ?= "disable"

do_install:append() {
    install -m 0755 -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/mini-snmpd ${D}${sysconfdir}/default/mini-snmpd
    install -m 0755 -d ${D}${sbindir}/
    install -m 0755 -t ${D}${sbindir} ${WORKDIR}/cc-mini-snmpd-env
}
