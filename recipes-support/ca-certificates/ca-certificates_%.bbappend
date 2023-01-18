#
# install a systemd script which run's during first boot to
# install all custom certificates (which were possibly copied
# during firmware update)
#

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit systemd

SRC_URI += " \
    file://update-ca-certificates.service \
"

do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then

        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/update-ca-certificates.service ${D}${systemd_system_unitdir}/

        sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               -e 's,@SBINDIR@,${sbindir},g' \
               -e 's,@LIBEXECDIR@,${libexecdir}/${BPN},g' \
               ${D}${systemd_system_unitdir}/*.service
    fi
}

SYSTEMD_SERVICE_${PN} = "update-ca-certificates.service"
