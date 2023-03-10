FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " file://fancontrol.service"

PACKAGECONFIG ?= ""

SYSTEMD_PACKAGES += "${PN}-fancontrol"
SYSTEMD_SERVICE:${PN}-fancontrol = "fancontrol.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install:append() {
    # install systemd service file
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/fancontrol.service ${D}${systemd_system_unitdir}
    fi
}

FILES:${PN}-fancontrol += "${systemd_system_unitdir}/fancontrol.service"
