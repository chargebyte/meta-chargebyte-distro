# prevent some default daemons from being installed in the image since we rely on systemd-networkd only
RDEPENDS:${PN}:remove = "iputils-ninfod iputils-rarpd iputils-rdisc"
