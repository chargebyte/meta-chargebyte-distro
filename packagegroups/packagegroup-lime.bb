SUMMARY = "Set of Charge SOM distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# we inherite from chargesom platform so far
RDEPENDS:${PN} = " \
    packagegroup-chargesom \
"
