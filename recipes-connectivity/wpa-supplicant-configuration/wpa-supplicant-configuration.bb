DESCRIPTION = "Default wpa_supplicant configuration for wlan0 without networks on the AC-powerboard"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://wpa_supplicant-wlan0.conf"

inherit allarch

S = "${WORKDIR}"

RDEPENDS:${PN} += "wpa-supplicant"

# Networks are added at runtime (ac-powerboard-certification wifi-client), wpa_supplicant@wlan0 starts once this file exists
FILES:${PN} += "${sysconfdir}/wpa_supplicant/wpa_supplicant-wlan0.conf"
CONFFILES:${PN} += "${sysconfdir}/wpa_supplicant/wpa_supplicant-wlan0.conf"

do_install() {
	install -d ${D}${sysconfdir}/wpa_supplicant
	install -m 0600 ${WORKDIR}/wpa_supplicant-wlan0.conf ${D}${sysconfdir}/wpa_supplicant/
}
