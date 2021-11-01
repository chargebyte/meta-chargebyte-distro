
do_install_append() {
    # do not pre-install wired.network or wifi stuff for our boards
    rm -rf ${D}${systemd_unitdir}/network
}

FILES_${PN}_remove = "${systemd_unitdir}/network/"
