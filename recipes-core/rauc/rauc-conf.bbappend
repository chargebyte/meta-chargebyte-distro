FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# meta-rauc installs system.conf from the separate rauc-conf recipe. Keep the
# package machine-specific because FILESPATH selects a different configuration
# for each supported machine.
PACKAGE_ARCH = "${MACHINE_ARCH}"
