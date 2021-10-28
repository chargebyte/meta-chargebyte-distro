LICENSE = "CLOSED"

inherit systemd

PV = "1.14"

PACKAGE_ARCH = "all"

SRC_URI = " \
    file://device_info \
    file://baptism-data.config \
    file://system \
    file://system-generators \
    file://udev \
    file://fstab \
    file://led.sh \
    file://usb-mount.sh \
    file://usb-mount.d \
    file://root \
    file://init \
    file://rc.local \
    file://rc-once.sh \
    file://usb_modeswitch.d \
    file://rauc-helper.sh \
    file://mount-other-rootfs \
    file://led-boot-notification.service \
"

do_install() {
    install -d ${D}/sbin
    install -o root -g root -m 0755 ${WORKDIR}/init ${D}/sbin/init

    install -d ${D}/etc
    install -o root -g root -m 0644 ${WORKDIR}/fstab               ${D}/etc
    install -o root -g root -m 0644 ${WORKDIR}/device_info         ${D}/etc
    install -o root -g root -m 0644 ${WORKDIR}/baptism-data.config ${D}/etc
    install -o root -g root -m 0755 ${WORKDIR}/rc.local            ${D}/etc

    install -d ${D}/etc/usb-mount.d
    install -o root -g root -m 0755 ${WORKDIR}/usb-mount.d/* ${D}/etc/usb-mount.d/

    install -d ${D}/usr/sbin
    install -o root -g root -m 0755 ${WORKDIR}/usb-mount.sh ${D}/usr/sbin/
    install -o root -g root -m 0755 ${WORKDIR}/mount-other-rootfs ${D}/usr/sbin/

    install -d ${D}/lib
    install -o root -g root -m 0644 ${WORKDIR}/led.sh ${D}/lib/
    install -o root -g root -m 0644 ${WORKDIR}/rauc-helper.sh ${D}/lib/

    install -d ${D}/lib/init
    install -d ${D}/etc/rc.once.d
    install -o root -g root -m 0755 ${WORKDIR}/rc-once.sh ${D}/lib/init/

    install -d ${D}/lib/systemd/system
    install -o root -g root -m 0644 ${WORKDIR}/led-boot-notification.service ${D}/lib/systemd/system
    install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants
    ln -sf /lib/systemd/system/led-boot-notification.service ${D}${sysconfdir}/systemd/system/multi-user.target.wants/led-boot-notification.service

    install -d ${D}/lib/udev/rules.d
    install -o root -g root -m 0644 ${WORKDIR}/udev/* ${D}/lib/udev/rules.d/

    install -d ${D}/etc/usb_modeswitch.d
    install -o root -g root -m 0644 ${WORKDIR}/usb_modeswitch.d/* ${D}/etc/usb_modeswitch.d/

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then

        install -d ${D}${systemd_unitdir}/system-generators/
        install -m 0755 ${WORKDIR}/system-generators/* ${D}${systemd_unitdir}/system-generators/

        install -d ${D}${systemd_system_unitdir}/
        install -m 0644 ${WORKDIR}/system/* ${D}${systemd_system_unitdir}/

        sed -i -e 's,@BASE_BINDIR@,${base_bindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               -e 's,@SBINDIR@,${sbindir},g' \
               -e 's,@BINDIR@,${bindir},g' \
               ${D}${systemd_system_unitdir}/*
    fi

    install -d ${D}/home/root/
    cp --no-preserve=ownership ${WORKDIR}/root/* ${D}/home/root/

    echo "DEVICE_FLAVOR='${FLAVOR}'" >> ${D}/etc/device_info
}

FILES_${PN} = "/"

SYSTEMD_SERVICE_${PN} = "srv.mount usb-mount@.service"

RDEPENDS_${PN} += " \
    e2fsprogs-e2fsck \
    e2fsprogs-mke2fs \
    e2fsprogs-resize2fs \
    util-linux-blkid \
    findutils \
    mmc-utils \
    ${@bb.utils.contains('MACHINE', 'evachargese', 'sdimage', '', d)} \
    debianutils-run-parts \
"
