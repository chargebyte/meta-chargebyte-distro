RDEPENDS:${PN}-dev = ""

# Bundle the RTL8821C WLAN and Bluetooth firmware and configuration.
PACKAGES =+ "${PN}-rtl8821c"

FILES:${PN}-rtl8821c = " \
  ${nonarch_base_libdir}/firmware/rtl_bt/rtl8821c_fw.bin \
  ${nonarch_base_libdir}/firmware/rtl_bt/rtl8821c_config.bin \
  ${nonarch_base_libdir}/firmware/rtw88/rtw8821c_fw.bin \
"

LICENSE:${PN}-rtl8821c = "Firmware-rtlwifi_firmware"
RDEPENDS:${PN}-rtl8821c += "${PN}-rtl-license"

PACKAGES =+ "${PN}-rtl8153"

FILES:${PN}-rtl8153 = " \
  ${nonarch_base_libdir}/firmware/rtl_nic/rtl8153*.fw \
"

LICENSE:${PN}-rtl8153 = "WHENCE"

RDEPENDS:${PN}-rtl8153c += "${PN}-whence-license"
