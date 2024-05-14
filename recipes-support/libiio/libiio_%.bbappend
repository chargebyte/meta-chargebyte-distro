PACKAGECONFIG:append = " libiio-python3"

# don't run the daemon by default
SYSTEMD_AUTO_ENABLE:${PN}-iiod ?= "disable"
