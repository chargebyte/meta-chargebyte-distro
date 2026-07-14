SUMMARY = "Set of Lime distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

# we inherite mainly from chargesom platform so far
RDEPENDS:${PN} = " \
    packagegroup-chargesom \
    modemmanager \
    ppp \
"
