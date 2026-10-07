LICENSE = "CLOSED"

inherit systemd

PV = "1.27"

PACKAGE_ARCH = "all"

SRC_URI = " \
    file://device_info \
    file://baptism-data.config \
    file://system \
    file://system-generators \
    file://udev \
    file://led.sh \
    file://usb-mount.sh \
    file://usb-mount.d \
    ${@bb.utils.contains_any('MACHINE', 'chargesom lime parsley', '', 'file://root', d)} \
    file://init \
    file://rc.local \
    file://rc-once.sh \
    file://usb_modeswitch.d \
    file://rauc-helper.sh \
    file://mount-other-rootfs \
    file://led-boot-notification.service \
    file://led-boot-notification.sh \
    file://cc33xx-bt-enable.service \
"

do_install() {
    install -d ${D}${base_sbindir}
    install -o root -g root -m 0755 ${UNPACKDIR}/init ${D}${base_sbindir}/init

    install -d ${D}/etc
    install -o root -g root -m 0644 ${UNPACKDIR}/device_info         ${D}/etc
    install -o root -g root -m 0644 ${UNPACKDIR}/baptism-data.config ${D}/etc
    install -o root -g root -m 0755 ${UNPACKDIR}/rc.local            ${D}/etc

    install -d ${D}/etc/usb-mount.d
    install -o root -g root -m 0755 ${UNPACKDIR}/usb-mount.d/* ${D}/etc/usb-mount.d/

    install -d ${D}/usr/sbin
    install -o root -g root -m 0755 ${UNPACKDIR}/usb-mount.sh ${D}/usr/sbin/
    install -o root -g root -m 0755 ${UNPACKDIR}/mount-other-rootfs ${D}/usr/sbin/

    install -d ${D}${nonarch_base_libdir}
    install -o root -g root -m 0644 ${UNPACKDIR}/led.sh ${D}${nonarch_base_libdir}/
    install -o root -g root -m 0644 ${UNPACKDIR}/rauc-helper.sh ${D}${nonarch_base_libdir}/

    install -d ${D}/usr/libexec
    install -o root -g root -m 0755 ${UNPACKDIR}/led-boot-notification.sh ${D}/usr/libexec

    install -d ${D}${nonarch_base_libdir}/init
    install -d ${D}/etc/rc.once.d
    install -o root -g root -m 0755 ${UNPACKDIR}/rc-once.sh ${D}${nonarch_base_libdir}/init/

    install -d ${D}${systemd_system_unitdir}
    install -o root -g root -m 0644 ${UNPACKDIR}/led-boot-notification.service ${D}${systemd_system_unitdir}
    install -o root -g root -m 0644 ${UNPACKDIR}/cc33xx-bt-enable.service ${D}${systemd_system_unitdir}

    install -d ${D}${nonarch_base_libdir}/udev/rules.d
    install -o root -g root -m 0644 ${UNPACKDIR}/udev/* ${D}${nonarch_base_libdir}/udev/rules.d/

    install -d ${D}/etc/usb_modeswitch.d
    install -o root -g root -m 0644 ${UNPACKDIR}/usb_modeswitch.d/* ${D}/etc/usb_modeswitch.d/

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then

        install -d ${D}${systemd_unitdir}/system-generators/
        install -m 0755 ${UNPACKDIR}/system-generators/* ${D}${systemd_unitdir}/system-generators/

        install -d ${D}${systemd_system_unitdir}/
        install -m 0644 ${UNPACKDIR}/system/* ${D}${systemd_system_unitdir}/

        sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               -e 's,@SBINDIR@,${sbindir},g' \
               ${D}${systemd_system_unitdir}/*
    fi

    if ${@bb.utils.contains_any('MACHINE', 'chargesom lime parsley', 'false', 'true', d)}; then
        install -d ${D}/home/root/
        cp --no-preserve=ownership ${UNPACKDIR}/root/* ${D}/home/root/
    fi

    echo "DEVICE_FLAVOR='${FLAVOR}'" >> ${D}/etc/device_info
}

FILES:${PN} = "/"

SYSTEMD_SERVICE:${PN} = " \
    cc33xx-bt-enable.service \
    led-boot-notification.service \
    srv.mount \
    usb-mount@.service \
"

RDEPENDS:${PN} += " \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs \
    e2fsprogs-resize2fs \
    util-linux-blkid \
    findutils \
    mmc-utils \
    ${@bb.utils.contains('MACHINE', 'evachargese', 'sdimage', '', d)} \
    debianutils-run-parts \
    openssl-bin \
"
