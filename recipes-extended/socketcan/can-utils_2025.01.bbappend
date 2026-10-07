# split out candump/cansend/cansniffer from all other binaries
PACKAGES =+ "${PN}-essentials"

FILES:${PN}-essentials = "${bindir}/candump ${bindir}/cansend ${bindir}/cansniffer"
FILES:${PN}:remove = "${bindir}/candump ${bindir}/cansend ${bindir}/cansniffer"
ALTERNATIVE:${PN}-essentials = "candump cansend"
ALTERNATIVE:${PN}:remove = "candump cansend"
