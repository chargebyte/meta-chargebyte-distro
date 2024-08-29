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
    libgpiod-tools \
    libiio \
    libiio-iiod \
    libiio-python3 \
    libiio-tests \
    lmbench \
    lmsensors-pwmconfig \
    mc \
    memtester \
    nano \
    openssl-bin \
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
    subversion \
    systemd-analyze \
    tmux \
"
