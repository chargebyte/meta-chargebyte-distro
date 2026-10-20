SUMMARY = "Factory reset tool and reset hooks"
DESCRIPTION = "Factory reset tool with extensible reset hooks and GPIO trigger support."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch systemd

PV = "1"

SRC_URI = " \
    file://factory-reset \
    file://factory-reset-gpio-check \
    file://factory-reset.service \
    file://10_clear-root-pw \
    file://15_root-user-settings \
    file://20_clear-etc-systemd \
    file://20_ssh \
    file://20_wifi \
    file://30_ca-certificates \
    file://40_mosquitto \
    file://90_clear-journal \
    file://99_reboot \
"

RDEPENDS:${PN} += " \
    debianutils-run-parts \
    libgpiod-tools \
"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/factory-reset ${D}${sbindir}/factory-reset

    install -d ${D}${libexecdir}
    install -m 0755 ${WORKDIR}/factory-reset-gpio-check ${D}${libexecdir}/factory-reset-gpio-check

    install -d ${D}${libexecdir}/factory-reset.d
    install -m 0755 ${WORKDIR}/10_clear-root-pw ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/15_root-user-settings ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/20_clear-etc-systemd ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/20_ssh ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/20_wifi ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/30_ca-certificates ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/40_mosquitto ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/90_clear-journal ${D}${libexecdir}/factory-reset.d/
    install -m 0755 ${WORKDIR}/99_reboot ${D}${libexecdir}/factory-reset.d/

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/factory-reset.service ${D}${systemd_system_unitdir}/
}

FILES:${PN} += " \
    ${libexecdir}/factory-reset.d \
    ${libexecdir}/factory-reset-gpio-check \
    ${sbindir}/factory-reset \
"

SYSTEMD_SERVICE:${PN} = "factory-reset.service"
