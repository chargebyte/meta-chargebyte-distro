LICENSE = "CLOSED"

inherit allarch

PV = "3"

SRC_URI = " \
    file://br0-mac-generator \
    file://br0.netdev \
    file://br0.network \
    file://can0.network \
    file://can1.network \
    file://eth0.network \
    file://eth1.network \
    file://eth2.network \
    file://wlan0.network \
    file://wwan.network \
"

do_install() {
    install -d ${D}/lib/systemd/network
    install -o root -g root -m 0644 ${WORKDIR}/*.net* ${D}/lib/systemd/network

    # remove files for HW interfaces not present on EVAcharge SE
    if ${@bb.utils.contains('MACHINE', 'evachargese', 'true', 'false', d)}; then
        rm -f ${D}/lib/systemd/network/can1.network
        rm -f ${D}/lib/systemd/network/eth2.network
    fi

    # install a workaround to set MAC address of br0 interface to eth0 one's
    install -d ${D}/lib/systemd/system-generators
    install -o root -g root -m 0755 ${WORKDIR}/br0-mac-generator ${D}/lib/systemd/system-generators/
}

FILES:${PN} = "/"
