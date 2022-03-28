LICENSE = "CLOSED"

PV = "1.0"

inherit allarch systemd

SRC_URI = " \
    file://84-cc-qca-fixup.rules \
    file://cc-qca-fixup.sh \
    file://cc-qca-fixup@.service \
"

RDEPENDS:${PN} += "open-plc-utils"

do_install() {
    install -d ${D}/usr/libexec
    install -o root -g root -m 0755 ${WORKDIR}/cc-qca-fixup.sh ${D}/usr/libexec/

    install -d ${D}/lib/udev/rules.d
    install -o root -g root -m 0644 ${WORKDIR}/84-cc-qca-fixup.rules ${D}/lib/udev/rules.d/

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then

        install -d ${D}${systemd_system_unitdir}/
        install -m 0644 ${WORKDIR}/cc-qca-fixup@.service ${D}${systemd_system_unitdir}/

        # in case QCA firmware update is also included in image, we want to check for running QCA firmware before
        install -d ${D}${systemd_system_unitdir}/cc-qca-fw-update@.service.wants
        ln -sf ../cc-qca-fixup@.service ${D}${systemd_system_unitdir}/cc-qca-fw-update@.service.wants/

        sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               -e 's,@SBINDIR@,${sbindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               ${D}${systemd_system_unitdir}/*.service
    fi
}

SYSTEMD_SERVICE:${PN} = "cc-qca-fixup@.service"
FILES:${PN} = "/"
