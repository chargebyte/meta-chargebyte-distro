SUMMARY = "Enable LE address resolution on TI CC33xx Bluetooth controllers"
DESCRIPTION = "Re-enables LE address resolution whenever the controller comes up. \
               Without it, CC33xx ignores connection requests from phones using \
               resolvable private addresses."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "1"

inherit systemd

SRC_URI = " \
    file://cc33xx-le-addr-resolution.c \
    file://cc33xx-le-addr-resolution.service \
"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -Wall -Wextra -o cc33xx-le-addr-resolution cc33xx-le-addr-resolution.c
}

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 cc33xx-le-addr-resolution ${D}${sbindir}/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/cc33xx-le-addr-resolution.service ${D}${systemd_system_unitdir}/
    sed -i -e 's,@SBINDIR@,${sbindir},g' ${D}${systemd_system_unitdir}/cc33xx-le-addr-resolution.service
}

SYSTEMD_SERVICE:${PN} = "cc33xx-le-addr-resolution.service"
