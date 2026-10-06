SUMMARY = "Command line tools and library for chargebyte's EEPROM contents"
DESCRIPTION = "The repository contains some command line tools and a library for \
               accessing/modifying various EEPROMs on chargebyte's products like Charge SOM."
HOMEPAGE = "https://github.com/chargebyte/cb-eeprom-utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCREV = "9593ea5bceb5ac55618f9cea8a4deb2e363b0b63"
PV = "0.2.0+git${SRCPV}"

SRC_URI = "git://github.com/chargebyte/cb-eeprom-utils.git;protocol=https;branch=main"
S = "${WORKDIR}/git"

inherit cmake
