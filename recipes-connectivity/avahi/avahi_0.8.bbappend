FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# disable gtk dependency even if distro announces x11 or wayland support
PACKAGECONFIG ?= "dbus"

# patch systemd service file to prevent chroot of Avahi
SRC_URI += "file://0002-avahi-daemon-no-chroot.patch file://0001-cleanup-ensure-entries-are-dead-for-at-least-1s.patch"

# don't install avahi-daemon.conf as we want it product specific
FILES:avahi-daemon:remove = "${sysconfdir}/avahi/avahi-daemon.conf"

# don't install any default service file
do_install:append() {
        rm -rf ${D}${sysconfdir}/avahi/avahi-daemon.conf
        rm -rf ${D}${sysconfdir}/avahi/services/*
}
