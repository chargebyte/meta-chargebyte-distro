SUMMARY = "A tool to install i.MX23/28 bootstreams in devices or image files"
DEPENDS = "dosfstools-native"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit autotools-brokensep

PV = "0.1+git${SRCPV}"

SRC_URI = "git://github.com/NXPmicro/imx-uuc.git;protocol=https"
SRCREV = "e3fbdfef978abd150d9ea71c4d174daded9c8d33"

S = "${WORKDIR}/git"

do_install() {
    install -d ${D}/usr/bin
    install -o root -g root -m 0755 ${B}/sdimage ${D}/usr/bin
}

FILES_${PN} += "/usr/bin/sdimage"
