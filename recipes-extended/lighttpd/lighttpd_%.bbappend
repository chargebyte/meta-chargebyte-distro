FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://lighttpd.conf file://10-mime-types.conf file://20-mqtt.conf file://lighttpd.service file://lighttpd.logrotate"

PACKAGECONFIG:append = " lua"

do_install:append() {
    # install mimetype mappings
    install -m 644 ${WORKDIR}/10-mime-types.conf ${D}${sysconfdir}/lighttpd/10-mime-types.conf

    # create empty /www folder, removing Yocto upstream's defaults
    rm -rf ${D}/www
    install -d ${D}/www

    # MQTT configuration
    install -m 644 ${WORKDIR}/20-mqtt.conf ${D}${sysconfdir}/lighttpd/20-mqtt.conf

    # directories as specified in our lighttpd.conf, with our user permissions
    install -d -m 0750 -o www-data -g www-data ${D}/${localstatedir}/log/lighttpd
    install -d -m 0755 -o root     -g root     ${D}/${localstatedir}/cache/lighttpd
    install -d -m 0750 -o www-data -g www-data ${D}/${localstatedir}/cache/lighttpd/uploads
    install -d -m 0750 -o www-data -g www-data ${D}/${localstatedir}/cache/lighttpd/compress

    # use our own service file
    install -m 0644 ${WORKDIR}/lighttpd.service ${D}${systemd_system_unitdir}

    install -d ${D}${sysconfdir}/logrotate.d
    install -m 0644 ${WORKDIR}/lighttpd.logrotate ${D}${sysconfdir}/logrotate.d/lighttpd
}

FILES:${PN} += " \
    ${localstatedir}/log \
    ${localstatedir}/cache \
    "

#
# adding the user+group prevent packaging error for do_install:append above, but
# since the user/group already exists nothing is added in real; don't know yet,
# why using predefined groups does not work per se (guess: only elementary user/group
# setup during do_install per default)
#

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --home /www --no-create-home --shell /bin/false --user-group www-data;"
