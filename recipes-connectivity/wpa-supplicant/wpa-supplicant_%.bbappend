FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0001-Don-t-install-wpa_supplicant-.service.patch \
    file://99-wifi.rules \
    file://10-cfg-present.conf \
"

PACKAGECONFIG ?= "openssl"

do_install:append () {
	# remove non-systemd-networkd stuff
	rm -rf ${D}${sysconfdir}/network

	# create config subdir
	install -d ${D}${sysconfdir}/wpa_supplicant

	install -d ${D}/lib/udev/rules.d
	install -o root -g root -m 0644 ${WORKDIR}/99-wifi.rules ${D}/lib/udev/rules.d

	# install config file condition
	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}/${systemd_system_unitdir}/wpa_supplicant@.service.d
		install -m 644 ${WORKDIR}/10-cfg-present.conf ${D}/${systemd_system_unitdir}/wpa_supplicant@.service.d/
	fi
}
