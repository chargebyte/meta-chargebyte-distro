FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://rsyslog.conf file://rsyslog.logrotate"

do_install:append() {
    # rename the file to be consistent with others
    mv ${D}${sysconfdir}/logrotate.d/logrotate.rsyslog ${D}${sysconfdir}/logrotate.d/rsyslog
}
