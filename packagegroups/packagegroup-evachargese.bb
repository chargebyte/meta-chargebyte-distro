SUMMARY = "Set of EVAcharge SE distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
    can-utils-essentials \
"
