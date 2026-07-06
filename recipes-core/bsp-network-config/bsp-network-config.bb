LICENSE = "CLOSED"

inherit allarch

PV = "14"

SRC_URI = " \
    file://br0-mac-generator \
    file://br0.netdev \
    file://br0.network \
    file://can0.network \
    file://can1.network \
    file://dsa-ports.network \
    file://eth0.network \
    file://eth0-parsley.network \
    file://eth1.network \
    file://eth2.network \
    file://eth2-chargesom.network \
    file://eth2-lime.network \
    file://eth2-parsley.network \
    file://eth3.network \
    file://wlan0.network \
    file://wwan.network \
    file://99-eth1-plca-config.rules \
"

do_install() {
    install -d ${D}/lib/systemd/network
    install -o root -g root -m 0644 ${WORKDIR}/*.net* ${D}/lib/systemd/network

    # install a workaround to set MAC address of br0 interface to eth0 one's
    install -d ${D}/lib/systemd/system-generators
    install -o root -g root -m 0755 ${WORKDIR}/br0-mac-generator ${D}/lib/systemd/system-generators/

    # remove files for HW interfaces not present on Tarragon
    if ${@bb.utils.contains('MACHINE', 'tarragon', 'true', 'false', d)}; then
        rm -f ${D}/lib/systemd/network/can1.network
        rm -f ${D}/lib/systemd/network/eth3.network
        rm -f ${D}/lib/systemd/network/dsa-ports.network
    fi

    # remove files for HW interfaces not present on EVAcharge SE
    if ${@bb.utils.contains('MACHINE', 'evachargese', 'true', 'false', d)}; then
        rm -f ${D}/lib/systemd/network/can1.network
        rm -f ${D}/lib/systemd/network/eth2.network
        rm -f ${D}/lib/systemd/network/eth3.network
        rm -f ${D}/lib/systemd/network/dsa-ports.network
    fi

    # remove files for HW interfaces not present on Charge SOM platforms
    if ${@bb.utils.contains('MACHINE', 'chargesom', 'true', 'false', d)}; then
        # rename specific file
        mv -f ${D}/lib/systemd/network/eth2-chargesom.network ${D}/lib/systemd/network/eth2.network
    else
        # delete chargesom specific files for all other platforms
        rm -f ${D}/lib/systemd/network/*chargesom*
    fi

    # remove files for HW interfaces not present on Lime platforms
    if ${@bb.utils.contains('MACHINE', 'lime', 'true', 'false', d)}; then
        # rename specific file
        mv -f ${D}/lib/systemd/network/eth2-lime.network ${D}/lib/systemd/network/eth2.network
    else
        # delete lime specific files for all other platforms
        rm -f ${D}/lib/systemd/network/*lime*
    fi

    # adapt for Parsley platform specifics
    if ${@bb.utils.contains('MACHINE', 'parsley', 'true', 'false', d)}; then
        # not present / not possible
        rm -f ${D}/lib/systemd/network/can1.network
        rm -f ${D}/lib/systemd/network/eth3.network
        rm -f ${D}/lib/systemd/network/wlan0.network
        rm -f ${D}/lib/systemd/network/wwan.network
        rm -f ${D}/lib/systemd/network/dsa-ports.network
        # rename specific files
        mv -f ${D}/lib/systemd/network/eth0-parsley.network ${D}/lib/systemd/network/eth0.network
        mv -f ${D}/lib/systemd/network/eth2-parsley.network ${D}/lib/systemd/network/eth2.network
        # we don't use a bridge (bridges are not compatible with profinet and we don't want to
        # to restrict users)
        rm -f ${D}/lib/systemd/network/br0.*
        # delete the workaround, not needed here
        rm -f ${D}/lib/systemd/system-generators/br0-mac-generator
        # install PLCA config udev rule
        install -d ${D}/lib/udev/rules.d
        install -o root -g root -m 0644 ${WORKDIR}/99-eth1-plca-config.rules ${D}/lib/udev/rules.d/
    else
        # delete parsley specific files for all other platforms
        rm -f ${D}/lib/systemd/network/*parsley*
    fi
}

FILES:${PN} = "/"
