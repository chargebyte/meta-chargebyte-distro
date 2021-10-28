SUMMARY = "C Library for Linux Peripheral I/O (GPIO, LED, PWM, SPI, I2C, MMIO, Serial)"
DESCRIPTION = "c-periphery is a small C library for GPIO, LED, PWM, SPI, I2C, MMIO, and \
               Serial peripheral I/O interface access in userspace Linux. \
               c-periphery simplifies and consolidates the native Linux \
               APIs to these interfaces. c-periphery is useful in embedded Linux environments \
               (including Raspberry Pi, BeagleBone, etc. platforms) for interfacing with \
               external peripherals. c-periphery is  re-entrant, has no dependencies outside \
               the standard C library and Linux, compiles into a static library for easy \
               integration with other projects, and is MIT licensed."
HOMEPAGE = "https://github.com/vsergeev/c-periphery"
BUGTRACKER = "https://github.com/vsergeev/c-periphery/issues"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4097ec544cf01e9c7cfc4bdf8e4ed887"

SRC_URI = "https://github.com/vsergeev/${PN}/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "0d38115fab1f449d95d4752cd4456b30"
SRC_URI[sha256sum] = "ba298aac5c1f6ebe2b89ed623a2f678bedfa3b9dc78aaf4f0534a1dd304629da"

inherit cmake

EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON"
