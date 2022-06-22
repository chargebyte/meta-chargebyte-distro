FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://mosquitto.conf \
            file://mosquitto.service \
            file://mosquitto.logrotate \
"
do_install:append() {

    install -d ${D}${systemd_unitdir}/system/

    install -m 0644 ${WORKDIR}/mosquitto.service ${D}${systemd_unitdir}/system/mosquitto.service

    install -d ${D}${sysconfdir}/mosquitto
    install -m 0644 ${WORKDIR}/mosquitto.conf ${D}${sysconfdir}/mosquitto/mosquitto.conf

    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/mosquitto.init ${D}${sysconfdir}/init.d/mosquitto
    sed -i -e 's,@SBINDIR@,${sbindir},g' \
        -e 's,@BASE_SBINDIR@,${base_sbindir},g' \
        -e 's,@LOCALSTATEDIR@,${localstatedir},g' \
        -e 's,@SYSCONFDIR@,${sysconfdir},g' \
        ${D}${sysconfdir}/init.d/mosquitto

    install -d "${D}${sysconfdir}/logrotate.d"
    install -m 644 ${WORKDIR}/mosquitto.logrotate ${D}${sysconfdir}/logrotate.d/mosquitto
}

FILES:${PN}:append = " ${sysconfdir}/logrotate.d"
