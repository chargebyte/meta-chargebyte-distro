SUMMARY = "Set of common distribution packages during development"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = " \
    can-utils \
    can-utils-essentials \
    cmake \
    ${@bb.utils.contains("TUNE_FEATURES", "cortexa7", "cpuburn-a7", "", d)} \
    fping \
    git \
    iperf2 \
    iptraf \
    iputils-ping \
    ldd \
    lmsensors-pwmconfig \
    mc \
    memtester \
    nano \
    openssl-bin \
    python3 \
    python3-pip \
    screen \
    ser2net \
    socat \
    sqlite3 \
    strace \
    subversion \
    systemd-analyze \
    tmux \
"
