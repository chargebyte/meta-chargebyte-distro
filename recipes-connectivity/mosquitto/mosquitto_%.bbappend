FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://mosquitto.conf \
            file://mosquitto.logrotate \
"

do_install:append() {
    install -d ${D}${sysconfdir}/mosquitto
    install -m 0644 ${WORKDIR}/mosquitto.conf ${D}${sysconfdir}/mosquitto/mosquitto.conf

    install -d "${D}${sysconfdir}/logrotate.d"
    install -m 644 ${WORKDIR}/mosquitto.logrotate ${D}${sysconfdir}/logrotate.d/mosquitto
}

FILES:${PN}:append = " ${sysconfdir}/logrotate.d"
