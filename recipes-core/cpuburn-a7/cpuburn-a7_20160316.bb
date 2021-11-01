SUMMARY = "CPU burn app to stress ARM Cortex-A7 processor to generate as much heat as possible"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://cpuburn-a7.S;md5=41744107b14bc9d70edf09a6da09cf43"

DL_DIR_append = "/${PN}-${PV}"

SRC_URI = "https://raw.githubusercontent.com/ssvb/cpuburn-arm/ad7e646700d14b81413297bda02fb7fe96613c3f/cpuburn-a7.S;name=ssvb"

SRC_URI[ssvb.md5sum] = "41744107b14bc9d70edf09a6da09cf43"
SRC_URI[ssvb.sha256sum] = "47f10095a04d16c8d8d52598371fa01b6a6e87134083b9ec3c43d83ff8576f55"

S = "${WORKDIR}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} cpuburn-a7.S -o cpuburn-a7
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/cpuburn-a7 ${D}${bindir}/
}
