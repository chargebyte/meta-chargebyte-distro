SUMMARY = "Set of common distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    kernel-base \
    kernel-devicetree \
    kernel-modules \
    ${MACHINE_FIRMWARE} \
    openssh \
    openssh-sftp-server \
    ethtool \
    iproute2 \
    ${@bb.utils.contains("EXTRA_IMAGE_FEATURES", "debug-tweaks", "", "less", d)} \
    ${@bb.utils.contains("MACHINE", "parsley", "", \
        bb.utils.contains("SUBMACHINE", "micro", "", "open-plc-utils", d), d)} \
    u-boot-fw-utils \
    util-linux-lsblk \
    ${@bb.utils.contains("MACHINE", "parsley", "", \
        bb.utils.contains("SUBMACHINE", "micro", "", "usb-modeswitch usb-modeswitch-data", d), d)} \
    bsp-basefiles \
    maccalc \
    memtool \
    ${@bb.utils.contains("SUBMACHINE", "micro", "", "mini-snmpd", d)} \
    c-periphery \
"
