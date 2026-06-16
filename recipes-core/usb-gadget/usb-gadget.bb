SUMMARY = "Scripts to configure an UDC device as USB gadget"
DESCRIPTION = "This recipe bundles udev rules, systemd configuration and \
a small shell script to configure an UDC device as USB gadget."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch systemd

PV = "2"

RDEPENDS:${PN} += "libbaptismdata maccalc"

SRC_URI = " \
    file://90-usb-gadget.rules \
    file://usb-gadget@.service \
    file://usb-gadget.sh \
    file://usb-gadget.network \
"

do_install() {
    install -d ${D}/lib/udev/rules.d
    install -o root -g root -m 0644 ${WORKDIR}/90-usb-gadget.rules ${D}/lib/udev/rules.d

    install -d ${D}/usr/libexec
    install -o root -g root -m 0755 ${WORKDIR}/usb-gadget.sh ${D}/usr/libexec

    install -d ${D}${systemd_system_unitdir}
    install -o root -g root -m 0644 ${WORKDIR}/usb-gadget@.service ${D}${systemd_system_unitdir}

    install -d ${D}${systemd_system_unitdir}/getty.target.wants
    ln -s ../serial-getty@.service ${D}${systemd_system_unitdir}/getty.target.wants/serial-getty@ttyGS0.service

    install -d ${D}${systemd_unitdir}/network
    install -o root -g root -m 0644 ${WORKDIR}/usb-gadget.network ${D}${systemd_unitdir}/network
}

FILES:${PN} = "/"
