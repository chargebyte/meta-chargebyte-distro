SUMMARY = "Set of common distribution packages during development"
LICENSE = "CLOSED"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = " \
    autoconf-archive \
    bison \
    can-utils \
    can-utils-essentials \
    cmake \
    ${@bb.utils.contains("TUNE_FEATURES", "armv5", "", "cpuburn-arm", d)} \
    dtc \
    ${@bb.utils.contains("MACHINE", "chargesom", "fbida", "", d)} \
    fio \
    flex \
    fping \
    git \
    iperf2 \
    iproute2-tc \
    iproute2-nstat \
    iproute2-ss \
    iptraf \
    iputils-ping \
    ldd \
    libevdev \
    libgpiod-tools \
    libiio \
    libiio-iiod \
    libiio-python3 \
    libiio-tests \
    lmbench \
    ${@bb.utils.contains("MACHINE", "tarragon", "lmsensors-pwmconfig", "", d)} \
    mc \
    memtester \
    nano \
    openssl-bin \
    procps-ps \
    python3 \
    python3-pip \
    python3-pyserial \
    python3-shell \
    python3-usbtmc \
    rpclib \
    screen \
    ser2net \
    socat \
    sqlite3 \
    strace \
    stress-ng \
    systemd-analyze \
    tmux \
    ${@bb.utils.contains("MACHINE", "chargesom", "tulum-utils-bash-completion", "", d)} \
    vim \
"
