SUMMARY = "Open source flash program for STM32 using the ST serial bootloader"
HOMEPAGE = "https://sourceforge.net/projects/stm32flash/"
BUGTRACKER = "https://sourceforge.net/p/stm32flash/tickets/"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/I2SE/stm32flash.git;protocol=https;branch=master"

SRCREV = "ee5b009ae86a6fab29fb4bed7eecf7a0c90b0d56"

PV = "0.5+git${SRCPV}"
S = "${WORKDIR}/git"

do_install() {
    oe_runmake install DESTDIR=${D} PREFIX=${prefix}
}
