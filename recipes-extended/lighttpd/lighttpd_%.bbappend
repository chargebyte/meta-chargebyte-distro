FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://lighttpd.conf file://10-mime-types.conf file://20-mqtt.conf file://lighttpd.service file://lighttpd.logrotate"

PACKAGECONFIG[lua] = "--with-lua,--without-lua,lua"
PACKAGECONFIG_append = " lua"

do_install_append() {
    # install mimetype mappings
    install -d "${D}${sysconfdir}/lighttpd.d"
    install -m 644 ${WORKDIR}/10-mime-types.conf ${D}${sysconfdir}/lighttpd.d/10-mime-types.conf
    install -m 644 ${WORKDIR}/20-mqtt.conf ${D}${sysconfdir}/lighttpd.d/20-mqtt.conf

    # create empty /www folder
    rm -rf ${D}/www
    mkdir ${D}/www

    install -d -m 0750 -o www-data -g www-data ${D}/${localstatedir}/log/lighttpd
    install -d -m 0755 -o root     -g root     ${D}/${localstatedir}/cache/lighttpd
    install -d -m 0750 -o www-data -g www-data ${D}/${localstatedir}/cache/lighttpd/uploads
    install -d -m 0750 -o www-data -g www-data ${D}/${localstatedir}/cache/lighttpd/compress

    install -d "${D}${sysconfdir}/logrotate.d"
    install -m 644 ${WORKDIR}/lighttpd.logrotate ${D}${sysconfdir}/logrotate.d/lighttpd
}

FILES_${PN} += " ${sysconfdir}/lighttpd.d ${sysconfdir}/logrotate.d"

#
# adding the user+group prevent packaging error for do_install_append above, but
# since the user/group already exists nothing is added in real; don't know yet,
# why using predefined groups does not work per se (guess: only elementary user/group
# setup during do_install per default)
#

inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --home /www --no-create-home --shell /bin/false --user-group www-data;"
