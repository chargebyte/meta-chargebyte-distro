SUMMARY = "Set of Parsley distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    can-utils-essentials \
    ra-utils \
    ra-utils-fw-${MACHINE} \
    tpm2-pkcs11 \
    tpm2-tools \
    tpm2-tss-engine \
"
