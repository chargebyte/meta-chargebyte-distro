#!/bin/sh
# Copyright 2025 Michael Heimpold

CMD="$1"
UDC="$2"

GADGET_NAME="g1"
SERIALNUMBER="$(bd -n get serial#)"
[ -z "$SERIALNUMBER" ] || SERIALNUMBER="19810515"
VENDOR="$(bd -n get vendor)"
[ -n "$VENDOR" ] || VENDOR="chargebyte"
PRODUCT="$(bd -n get model)"
[ -n "$PRODUCT" ] || PRODUCT="$(tr -d '\0' < /proc/device-tree/model | sed "s/^$VENDOR//; s/^[[:space:]]*//; s/[[:space:]]*\$//")"
MAC="$(cat /sys/class/net/eth0/address 2>/dev/null)"
[ -n "$MAC" ] || MAC="00:01:87:00:be:eb"
HOST_MAC_ADDRESS="$(maccalc or "$MAC" "02:00:00:00:00:00")"

modprobe libcomposite
cd /sys/kernel/config/usb_gadget/

case "$CMD" in
start)
    mkdir "$GADGET_NAME" && cd "$GADGET_NAME"

    echo 0x1d6b > idVendor  # Linux Foundation
    echo 0x0104 > idProduct # Multifunction Composite Gadget
    echo 0x0200 > bcdUSB    # USB 2.0

    echo 0xef > bDeviceClass
    echo 0x02 > bDeviceSubClass
    echo 0x01 > bDeviceProtocol

    mkdir -p strings/0x409
    echo "$SERIALNUMBER" > strings/0x409/serialnumber
    echo "$VENDOR" > strings/0x409/manufacturer
    echo "$PRODUCT"  > strings/0x409/product

    mkdir -p functions/acm.usb0    # serial

    mkdir -p functions/rndis.usb0  # network
    echo "$HOST_MAC_ADDRESS" > functions/rndis.usb0/host_addr

    mkdir -p configs/c.1
    echo 2 > configs/c.1/MaxPower
    ln -s functions/acm.usb0   configs/c.1/
    ln -s functions/rndis.usb0 configs/c.1/

    # storage; only if the device is not in use at the moment
    if ! grep -q mmcblk0 /proc/mounts; then
        mkdir -p functions/mass_storage.0
        echo "/dev/mmcblk0" > functions/mass_storage.0/lun.0/file

        ln -s functions/mass_storage.0 configs/c.1/
    fi

    # OS descriptors
    echo 1       > os_desc/use
    echo 0xcd    > os_desc/b_vendor_code
    echo MSFT100 > os_desc/qw_sign

    echo RNDIS   > functions/rndis.usb0/os_desc/interface.rndis/compatible_id
    echo 5162001 > functions/rndis.usb0/os_desc/interface.rndis/sub_compatible_id

    ln -s configs/c.1 os_desc

    echo "$UDC" > UDC
    ;;

stop)
    cd "$GADGET_NAME"
    [ "$(cat UDC)" = "$UDC" ] || exit 0
    echo "" > UDC
    sleep 1
    rm os_desc/c.1
    rm configs/c.1/*.usb0
    rmdir functions/*
    rmdir strings/*
    rmdir configs/c.1
    cd ..
    rmdir "$GADGET_NAME"
    ;;

*)
    exit 1

esac
