SRCREV = "bd8898ed92685e12022dd33a04c87786b5262344"

do_install_append() {
	# remove unnecessary stuff
	rm -rf ${D}/usr/share
}
