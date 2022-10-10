SRCREV = "bd8898ed92685e12022dd33a04c87786b5262344"

SRC_URI = " \
    git://github.com/systemd/casync.git;protocol=https;branch=main \
    "

do_install_append() {
	# remove unnecessary stuff
	rm -rf ${D}/usr/share
}
