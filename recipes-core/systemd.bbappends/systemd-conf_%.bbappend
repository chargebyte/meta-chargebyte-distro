
do_install:append() {
    # do not pre-install wired.network or wifi stuff for our boards
    rm -rf ${D}${systemd_unitdir}/network
}

FILES:${PN}:remove = "${systemd_unitdir}/network/"
