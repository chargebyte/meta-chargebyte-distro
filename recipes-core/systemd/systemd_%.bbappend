FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG:remove = " acl backlight binfmt hibernate ima machined polkit portabled smack sysusers vconsole nss-mymachines userdb utmp"
# iptc dropped: this systemd version no longer has an iptc PACKAGECONFIG
# option (libiptc/iptables firewall backend support was removed upstream
# in favour of nftables-only).
PACKAGECONFIG:append = " polkit_hostnamed_fallback"

RDEPENDS:${PN}:remove = "volatile-binds systemd-compat-units systemd-extra-utils udev-hwdb"

RRECOMMENDS:${PN}:remove = "udev-hwdb"

PACKAGECONFIG[ldconfig] = "-Dldconfig=true,-Dldconfig=false"

do_install:append() {
    # move /sbin/init away since we install our own for first run
    mv ${D}${base_sbindir}/init ${D}${base_sbindir}/init.orig

    # don't use this
    rm -f ${D}${sysconfdir}/tmpfiles.d/00-create-volatile.conf

    # we don't need a getty on VT
    rm -f ${D}${sysconfdir}/systemd/system/getty.target.wants/getty@tty1.service
    rm -f ${D}${systemd_system_unitdir}/getty@.service

    # we don't use containers
    rm -f ${D}${systemd_unitdir}/network/80-container*

    # create an empty machine-id
    touch ${D}${sysconfdir}/machine-id

    # REVIEW: disable systemd persistant journal for now
    rm -rf ${D}/${localstatedir}/log/journal

    # do not install .network.example files
    rm -rf ${D}${systemd_unitdir}/network
}

FILES:${PN}:append = " ${sysconfdir}/machine-id ${base_sbindir}/init.orig"
FILES:${PN}:remove = "${systemd_unitdir}/network/"
