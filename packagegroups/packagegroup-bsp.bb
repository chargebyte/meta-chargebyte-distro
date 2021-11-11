SUMMARY = "Set of packages for distribution project"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
    bsp-network-config \
    casync \
    python3 \
    python3-paho-mqtt \
    python3-pip \
    rauc \
"
