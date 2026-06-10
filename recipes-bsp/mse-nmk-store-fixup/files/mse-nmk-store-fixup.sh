#!/bin/sh
#
# This script fixes the MSE configuration to _not_ store NMK changes
# peristantly in flash. This is to prevent flash wearout.
#
# Environment variable IFACE is set by udev rule.
#

# determine MSE MAC address
FW_MAC="$(hpav_test find_local_sta "$IFACE" | grep "^MAC address" | head -n1 | cut -c57-74)"

# in case we cannot determine the MAC, retry next boot
[ -z "$FW_MAC" ] && exit 0

# create temp workdir
MY_TMPDIR="$(mktemp -d)"
pushd "$MY_TMPDIR" > /dev/null

# read current config
hpav_test conf_file read "$IFACE" current-inka.conf "$FW_MAC" > /dev/null

# check whether storing NMK changes is (still) enabled
if hpav_test conf_file parse current-inka.conf | grep -q '^Store Key Change[[:space:]]*: true$'; then
	echo "Store Key Change is (still) enabled for this MSE102x, fixing."

	# modify config
	hpav_test conf_file modify current-inka.conf store_key_change false

	# write back config
	hpav_test conf_file write "$IFACE" current-inka.conf "$FW_MAC" > /dev/null
fi

# cleanup
popd > /dev/null
rm -rf "$MY_TMPDIR"
