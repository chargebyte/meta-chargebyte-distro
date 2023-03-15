FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG ?= ""

inherit systemd

SRC_URI += " file://fancontrol-hwinit.service"

do_install:append() {
    # install systemd service file
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d -m 0755 ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/fancontrol-hwinit.service ${D}${systemd_system_unitdir}
    fi
}

SYSTEMD_PACKAGES += "${PN}-fancontrol"
SYSTEMD_SERVICE:${PN}-fancontrol = "fancontrol-hwinit.service"
SYSTEMD_AUTO_ENABLE = "enable"

FILES:${PN}-fancontrol += "${systemd_system_unitdir}/fancontrol-hwinit.service"
