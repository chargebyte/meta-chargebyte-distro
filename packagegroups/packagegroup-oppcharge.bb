SUMMARY = "Set of EVAcharge SE distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
    crda \
    hostapd \
    iw \
    qualcomm-qca9377 \
    qualcomm-qca9377-firmware \
    qualcomm-qca9377-module \
"
