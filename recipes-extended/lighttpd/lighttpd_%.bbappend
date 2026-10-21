FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://lighttpd.conf \
    file://10-mime-types.conf \
    file://lighttpd.service \
    file://hardening.conf \
    file://lighttpd.tmpfiles \
    file://factory-reset \
    ${@bb.utils.contains('PACKAGECONFIG', 'https', 'file://60-https.conf', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'uploadprogress', 'file://50-uploadprogress.conf', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'php-cgi', 'file://40-php-cgi.conf', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'mqtt', 'file://20-mqtt.conf', '', d)} \
    ${@bb.utils.contains_any('PROJECT', 'truffle cardamom', 'file://30-truffle-cardamom.conf', '', d)} \
    ${@bb.utils.contains('PACKAGECONFIG', 'logrotate', 'file://lighttpd.logrotate', \
        'file://lighttpd-logrotate file://lighttpd-logrotate.service file://lighttpd-logrotate.timer', d)} \
"

PACKAGECONFIG:append = " lua"
PACKAGECONFIG[mqtt] = ""
# Optional features are disabled by default. HTTPS requires the openssl build option.
PACKAGECONFIG[php-cgi] = ",,,lighttpd-module-fastcgi php-cgi"
PACKAGECONFIG[uploadprogress] = ",,,lighttpd-module-uploadprogress"
PACKAGECONFIG[https] = ",,,lighttpd-module-openssl openssl-bin"
PACKAGECONFIG[logrotate] = ",,,logrotate"

RDEPENDS:${PN}:append = " lighttpd-module-proxy ${@bb.utils.contains('PACKAGECONFIG', 'logrotate', '', 'gzip', d)}"
SYSTEMD_SERVICE:${PN}:append = " ${@bb.utils.contains('PACKAGECONFIG', 'logrotate', '', 'lighttpd-logrotate.timer', d)}"

python __anonymous() {
    options = (d.getVar('PACKAGECONFIG') or '').split()
    if 'https' in options and 'openssl' not in options:
        bb.fatal('lighttpd: PACKAGECONFIG https requires PACKAGECONFIG openssl')
}

do_install:append() {
    # install mimetype mappings
    install -d ${D}${sysconfdir}/lighttpd.d
    install -m 0644 ${WORKDIR}/10-mime-types.conf ${D}${sysconfdir}/lighttpd.d/

    # create empty /www folder, removing Yocto upstream's defaults
    rm -rf ${D}/www
    install -d ${D}/www

    # MQTT configuration
    if ${@bb.utils.contains('PACKAGECONFIG', 'mqtt', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/20-mqtt.conf ${D}${sysconfdir}/lighttpd.d/
    fi

    if ${@bb.utils.contains_any('PROJECT', 'truffle cardamom', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/30-truffle-cardamom.conf ${D}${sysconfdir}/lighttpd.d/
    fi

    if ${@bb.utils.contains('PACKAGECONFIG', 'php-cgi', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/40-php-cgi.conf ${D}${sysconfdir}/lighttpd.d/
        if ${@bb.utils.contains('PACKAGECONFIG', 'uploadprogress', 'true', 'false', d)}; then
            sed -i 's|/usr/bin/php-cgi"|/usr/bin/php-cgi -d upload_max_filesize=450M -d post_max_size=460M"|' \
                ${D}${sysconfdir}/lighttpd.d/40-php-cgi.conf
        fi
    fi

    if ${@bb.utils.contains('PACKAGECONFIG', 'uploadprogress', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/50-uploadprogress.conf ${D}${sysconfdir}/lighttpd.d/
    fi

    if ${@bb.utils.contains('PACKAGECONFIG', 'https', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/60-https.conf ${D}${sysconfdir}/lighttpd.d/
        install -d -m 0700 ${D}${sysconfdir}/lighttpd/certs
    fi

    # Create volatile log/cache directories at boot, for either rotation backend.
    install -d ${D}${nonarch_libdir}/tmpfiles.d
    install -m 0644 ${WORKDIR}/lighttpd.tmpfiles ${D}${nonarch_libdir}/tmpfiles.d/lighttpd.conf
    sed -i -e 's,@LOCALSTATEDIR@,${localstatedir},g' \
        ${D}${nonarch_libdir}/tmpfiles.d/lighttpd.conf

    install -d ${D}${libexecdir}/factory-reset.d
    install -m 0755 ${WORKDIR}/factory-reset ${D}${libexecdir}/factory-reset.d/40_lighttpd
    sed -i -e 's,@LOCALSTATEDIR@,${localstatedir},g' \
        ${D}${libexecdir}/factory-reset.d/40_lighttpd

    # use our own service file
    install -m 0644 ${WORKDIR}/lighttpd.service ${D}${systemd_system_unitdir}
    install -d ${D}${systemd_system_unitdir}/lighttpd.service.d
    install -m 0644 ${WORKDIR}/hardening.conf ${D}${systemd_system_unitdir}/lighttpd.service.d/

    if ${@bb.utils.contains('PACKAGECONFIG', 'logrotate', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/logrotate.d
        install -m 0644 ${WORKDIR}/lighttpd.logrotate ${D}${sysconfdir}/logrotate.d/lighttpd
    else
        install -d ${D}${libexecdir}
        install -m 0755 ${WORKDIR}/lighttpd-logrotate ${D}${libexecdir}/lighttpd-logrotate
        install -m 0644 ${WORKDIR}/lighttpd-logrotate.service ${D}${systemd_system_unitdir}/
        install -m 0644 ${WORKDIR}/lighttpd-logrotate.timer ${D}${systemd_system_unitdir}/
        sed -i -e 's,@LIBEXECDIR@,${libexecdir},g' \
            ${D}${systemd_system_unitdir}/lighttpd-logrotate.service
        sed -i -e 's,@LOCALSTATEDIR@,${localstatedir},g' \
            ${D}${libexecdir}/lighttpd-logrotate
    fi
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/lighttpd.service.d/hardening.conf \
    ${nonarch_libdir}/tmpfiles.d/lighttpd.conf \
    ${libexecdir}/factory-reset.d/40_lighttpd \
    ${@bb.utils.contains('PACKAGECONFIG', 'logrotate', '', '${libexecdir}/lighttpd-logrotate ${systemd_system_unitdir}/lighttpd-logrotate.service', d)} \
    "

# Ensure the account used by lighttpd and its tmpfiles rules exists.
inherit useradd

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --home /www --no-create-home --shell /bin/false --user-group www-data;"
