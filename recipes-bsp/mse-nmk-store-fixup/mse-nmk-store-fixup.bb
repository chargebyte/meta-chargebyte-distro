SUMMARY = "MSE102x configuration fixup to not store NMK changes in flash"
DESCRIPTION = "Reads and - if necessary - fixes the configuration so that NMK changes \
               are not stored in flash every time (e.g. during SLAC process)."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PV = "1"

inherit allarch systemd

SRC_URI = " \
    file://84-mse-nmk-store-fixup.rules \
    file://mse-nmk-store-fixup.sh \
    file://mse-nmk-store-fixup@.service \
"

RDEPENDS:${PN} += "tulum-utils"

do_install() {
    install -d ${D}/usr/libexec
    install -o root -g root -m 0755 ${UNPACKDIR}/mse-nmk-store-fixup.sh ${D}/usr/libexec/

    install -d ${D}${nonarch_base_libdir}/udev/rules.d
    install -o root -g root -m 0644 ${UNPACKDIR}/84-mse-nmk-store-fixup.rules ${D}${nonarch_base_libdir}/udev/rules.d/

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then

        install -d ${D}${systemd_system_unitdir}/
        install -m 0644 ${UNPACKDIR}/mse-nmk-store-fixup@.service ${D}${systemd_system_unitdir}/

        sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               -e 's,@SBINDIR@,${sbindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               ${D}${systemd_system_unitdir}/*.service
    fi
}

SYSTEMD_SERVICE:${PN} = "mse-nmk-store-fixup@.service"
FILES:${PN} = "/"
