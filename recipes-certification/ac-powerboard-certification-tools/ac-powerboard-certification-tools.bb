DESCRIPTION = "Tool set for the certification process of the AC-powerboard"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
	file://ac-powerboard-certification \
	file://ac-powerboard-certification-menu \
	"

inherit allarch

S = "${WORKDIR}"

# hcitool and hciconfig are used for the BLE test mode
RDEPENDS:${PN} += "bash bluez5 kmod cc33calibrator dialog wpa-supplicant-cli wpa-supplicant-passphrase wpa-supplicant-configuration"

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/ac-powerboard-certification ${D}${bindir}/
	install -m 0755 ${WORKDIR}/ac-powerboard-certification-menu ${D}${bindir}/
}
