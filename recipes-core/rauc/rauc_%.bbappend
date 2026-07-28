FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://system.conf \
    file://01-rauc-tmpdir-configuration.conf \
"

RAUC_KEYRING_FILE = "keyring.pem"

SRC_URI += " \
    file://pre-install.sh \
    file://post-install.sh \
    file://system-info.sh \
    file://i2se-devel.crt \
    file://i2se-release.crt \
    file://01-rauc-tmpdir-configuration.conf \
"

do_install:append() {
    install -d ${D}/usr/lib/rauc
    install -d ${D}${sysconfdir}/rauc
    install -m 0644 ${UNPACKDIR}/i2se-devel.crt   ${D}${sysconfdir}/rauc/
    install -m 0644 ${UNPACKDIR}/i2se-release.crt ${D}${sysconfdir}/rauc/
    ln -sf i2se-release.crt ${D}${sysconfdir}/rauc/keyring.pem

    install -d ${D}/usr/lib/rauc
    install -o root -g root -m 0755 ${UNPACKDIR}/pre-install.sh  ${D}/usr/lib/rauc/
    install -o root -g root -m 0755 ${UNPACKDIR}/post-install.sh ${D}/usr/lib/rauc/
    install -o root -g root -m 0755 ${UNPACKDIR}/system-info.sh  ${D}/usr/lib/rauc/

    # Configure rauc.service to use /srv/rauc-tmp/ as temporary directory for downloads
    # and cleanup the directory before the rauc service starts.
    install -d ${D}${systemd_system_unitdir}/rauc.service.d/
    install -m 0644 ${UNPACKDIR}/01-rauc-tmpdir-configuration.conf ${D}${systemd_system_unitdir}/rauc.service.d/
}

FILES:${PN} += " \
    /usr/lib/rauc \
    ${systemd_system_unitdir}/rauc.service.d/* \
"

PACKAGECONFIG ??= "service network json nocreate"
