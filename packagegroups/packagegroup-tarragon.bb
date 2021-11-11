SUMMARY = "Set of Tarragon distribution packages"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
    ${@bb.utils.contains("SUBMACHINE", "micro", "", "cc-qca-fixup", d)} \
    crda \
    hostapd \
    iw \
    ${@bb.utils.contains("SUBMACHINE", "micro", "", "lmsensors-fancontrol", d)} \
    stm32flash \
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
