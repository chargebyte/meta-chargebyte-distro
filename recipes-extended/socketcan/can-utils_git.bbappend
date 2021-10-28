
# split out candump/cansend/cansniffer from all other binaries
PACKAGES =+ "${PN}-essentials"

FILES_${PN}-essentials = "${bindir}/candump ${bindir}/cansend ${bindir}/cansniffer"
FILES_${PN}_remove = "${bindir}/candump ${bindir}/cansend ${bindir}/cansniffer"
