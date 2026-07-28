SUMMARY = "Command line tools and library for chargebyte's EEPROM contents"
DESCRIPTION = "The repository contains some command line tools and a library for \
               accessing/modifying various EEPROMs on chargebyte's products like Charge SOM."
HOMEPAGE = "https://github.com/chargebyte/cb-eeprom-utils"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCREV = "ca91b06d3352d5d81496cfa736ec940941111db1"
PV = "0.1.0+git${SRCPV}"

SRC_URI = "git://github.com/chargebyte/cb-eeprom-utils.git;protocol=https;branch=main"

inherit cmake
