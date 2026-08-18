SUMMARY = "Libraries to ease access to device baptism data"

DESCRIPTION = "This package contains a library and tool to read \
and modify an embedded device's baptism data."

HOMEPAGE = "https://github.com/mhei/libbaptismdata"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSES/LGPL-2.1-or-later.txt;md5=4fbd65380cdd255951079008b364516c"
SECTION = "libs"

SRC_URI = "https://github.com/mhei/${BPN}/releases/download/v${PV}/${BPN}-${PV}.tar.xz \
    file://bd-mdns-ssh.service \
    file://bd-mdns-ssh.conf \
"
SRC_URI[sha256sum] = "f1be8557abff1fa390f15d78e3833a77a7576ed28b9de9ffaef338b713f56bcb"

inherit autotools pkgconfig systemd

DEPENDS = "libubootenv"

RDEPENDS:${PN} += "libubootenv"

PACKAGECONFIG ??= "mdns"

PACKAGECONFIG[mdns] = "--with-avahi-mdns,--without-avahi-mdns,avahi"

SYSTEMD_SERVICE:${PN} = "${@bb.utils.contains('PACKAGECONFIG', 'mdns', 'bd-mdns-ssh.service', '', d)}"

do_install:append() {
    # install systemd service file for SSH server annoucement
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)} && \
       ${@bb.utils.contains('PACKAGECONFIG', 'mdns', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}/sshd.socket.d
        install -m 0644 ${WORKDIR}/bd-mdns-ssh.service ${D}${systemd_system_unitdir}/
        install -m 0644 ${WORKDIR}/bd-mdns-ssh.conf ${D}${systemd_system_unitdir}/sshd.socket.d
    fi
}

FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', \
                       bb.utils.contains('PACKAGECONFIG', 'mdns', '${systemd_system_unitdir}', '', d), \
                   '', d)}"
