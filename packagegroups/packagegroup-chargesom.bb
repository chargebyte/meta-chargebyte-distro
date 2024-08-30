SUMMARY = "Set of Charge SOM distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    can-utils-essentials \
    crda \
    hostapd \
    iw \
    wpa-supplicant \
    wpa-supplicant-cli \
    wpa-supplicant-passphrase \
    linux-firmware-mt7601u \
    linux-firmware-ralink \
    linux-firmware-rtl8168 \
    linux-firmware-rtl8188 \
    linux-firmware-rtl8192ce \
    linux-firmware-rtl8192cu \
    linux-firmware-rtl8192su \
    linux-firmware-rtl8723 \
    linux-firmware-rtl8821 \
"
