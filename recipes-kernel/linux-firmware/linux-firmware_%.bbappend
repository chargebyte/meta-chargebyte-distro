RDEPENDS:${PN}-dev = ""

PACKAGES =+ "${PN}-rtl8153"

FILES:${PN}-rtl8153 = " \
  ${nonarch_base_libdir}/firmware/rtl_nic/rtl8153*.fw \
"

LICENSE:${PN}-rtl8153 = "WHENCE"

RDEPENDS:${PN}-rtl8153c += "${PN}-whence-license"
