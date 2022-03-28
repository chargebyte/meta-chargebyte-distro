PACKAGECONFIG ?= "openssl"

do_install:append () {
	# remove non-systemd-networkd stuff
	rm -rf ${D}${sysconfdir}/network

	# create config subdir
	install -d ${D}${sysconfdir}/wpa_supplicant
}
