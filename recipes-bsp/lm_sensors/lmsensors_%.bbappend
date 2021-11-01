FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " file://fancontrol.service"

SYSTEMD_PACKAGES += "${PN}-fancontrol"
SYSTEMD_SERVICE_${PN}-fancontrol = "fancontrol.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install_append() {
    # Insall sensord service script
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/fancontrol.service ${D}${systemd_unitdir}/system
    fi
}

FILES_${PN}-fancontrol += "${systemd_unitdir}/system/fancontrol.service"
