#!/bin/sh
#
# This script determines the current platform it's running on and
# then checks whether the QCA firmware is booted up and whether
# the QCA "hangs" in bootloader mode. If so, it uses GPIOs to
# trigger a reset of the QCA, so that then the QCA firmware hopefully
# starts up correctly.
# This script is triggered by a udev rule and then loaded via systemd
# with environment parameters for network interface; in other words,
# it's not intended to be run directly by users.
#

# shortcut: nothing to do for empty, --all or loopback,
# we also need no update for alias interfaces or CAN devices
case "$IFACE" in
"" | "--all" | lo | can* | *:*)
    exit 0
    ;;
esac

# IFACE contains the kernel interface name of the interface brought up
# We need to compare the MAC address of this interface with QCA CP and/or
# QCA Mains interface MAC address to know which one is being processed.
IFACE_MAC=$(cat /sys/class/net/$IFACE/address)
[ -n "$IFACE_MAC" ] || exit 0

I2SE_OUI="00:01:87"
PLATFORM=$(tr -d '\0' < /proc/device-tree/model)

log() {
    logger -t cc-qca-fixup "$@"
}

gpios_export() {
    local gpio_list="$QCA_RESET_GPIO"

    for gpio in $gpio_list; do
        if [ ! -d /sys/class/gpio/gpio$gpio ]; then
            echo "$gpio" > /sys/class/gpio/export
        fi
    done
}

qca_reset() {
    echo low > /sys/class/gpio/gpio$QCA_RESET_GPIO/direction
    echo in > /sys/class/gpio/gpio$QCA_RESET_GPIO/direction
}

mx28_mac_from_otp() {
    local oui="$1"
    local otp_reg="$2"

    local addr="$(cat /sys/fsl_otp/HW_OCOTP_CUST$otp_reg)"
    [ ${#addr} -eq 10 ] || return 1

    printf "$oui:%02x:%02x:%02x\n" $(((addr >> 16) & 0xff)) $(((addr >> 8) & 0xff)) $((addr & 0xff))
}

mx6ull_mac_from_otp() {
    local oui="$1"
    local mac_name="$2"
    local otp_reg

    case "$mac_name" in
    wired)
        local addr2="$(cat /sys/fsl_otp/HW_OCOTP_MAC0)"
        local addr1="$(cat /sys/fsl_otp/HW_OCOTP_MAC1)"

        printf "%02x:%02x:%02x:%02x:%02x:%02x\n" $(((addr1 >> 8) & 0xff)) $((addr1 & 0xff)) $(((addr2 >> 24) & 0xff)) $(((addr2 >> 16) & 0xff)) $(((addr2 >> 8) & 0xff)) $((addr2 & 0xff))
        return 0
        ;;

    qca-mains)            otp_reg="HW_OCOTP_GP1" ;;
    qca-control-pilot)    otp_reg="HW_OCOTP_GP2" ;;
    qca-fw-mains)         otp_reg="HW_OCOTP_SW_GP1" ;;
    qca-fw-control-pilot) otp_reg="HW_OCOTP_SW_GP2" ;;
    *)
        return 1
    esac

    local addr="$(cat /sys/fsl_otp/$otp_reg)"

    printf "$oui:%02x:%02x:%02x\n" $(((addr >> 16) & 0xff)) $(((addr >> 8) & 0xff)) $((addr & 0xff))
}

determine_qca_reset_gpio() {
    case "$PLATFORM" in
    "I2SE EVAcharge SE")
        QCA_INTERFACE_MAC="$(mx28_mac_from_otp "$I2SE_OUI" 1)"

        # compare MACs and quit if they do not match
        [ "$IFACE_MAC" = "$QCA_INTERFACE_MAC" ] || exit 0

        QCA_RESET_GPIO=45
        ;;

    "I2SE Tarragon"*)
        for QCA_TARGET in "control-pilot" "mains"; do
            QCA_INTERFACE_MAC="$(mx6ull_mac_from_otp "$I2SE_OUI" qca-$QCA_TARGET)"

            # compare MACs
            if [ "$IFACE_MAC" = "$QCA_INTERFACE_MAC" ]; then
                IS_QCA_IF=1
                break
            fi
        done

        # exit if this is not a QCA interface
        [ -n "$IS_QCA_IF" ] || exit 0

        case "$QCA_TARGET" in
        control-pilot)
            QCA_RESET_GPIO=81
            ;;
        mains)
            QCA_RESET_GPIO=135
            ;;
        esac
        ;;

    *)
        exit 0
    esac
}

# do the main work: check whether the QCA is in bootloader and trigger
# reset via GPIO if so; we don't check for success to not delay boot process;
# in case this does not help, we assume that upper layers will report errors
# to monitoring systems
if plctool -r -i "$IFACE" local 2>&1 | grep -q BootLoader; then
    log "QCA700x BootLoader on $IFACE detected, triggering reset."
    determine_qca_reset_gpio
    gpios_export
    qca_reset
fi
