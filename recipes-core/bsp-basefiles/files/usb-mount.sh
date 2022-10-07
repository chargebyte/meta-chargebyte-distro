#!/bin/sh

# This script is called from our systemd unit file to mount or unmount
# a USB drive.

usage()
{
	echo "Usage: $0 {add|remove} device_name (e.g. sdb1)"
	exit 1
}

if [ $# -ne 2 ]; then
	usage
fi

ACTION=$1
DEVBASE=$2
DEVICE="/dev/${DEVBASE}"

# See if this drive is already mounted, and if so where
MOUNT_POINT=$(/bin/mount | /bin/grep ${DEVICE} | /usr/bin/awk '{ print $3 }')

do_mount()
{
	if [ -n "${MOUNT_POINT}" ]; then
		echo "Warning: ${DEVICE} is already mounted at ${MOUNT_POINT}"
		exit 1
	fi

	# Get info for this drive: $ID_FS_LABEL, $ID_FS_UUID, and $ID_FS_TYPE
	# M#2398: filter out ID_FS_PARTLABEL because of unquoted value in output of blkid
	eval $(/sbin/blkid -o udev ${DEVICE} | /bin/grep -v ID_FS_PARTLABEL)

	# skip if no filesystem is detected
	if [ -z "${ID_FS_TYPE}" ]; then
		echo "Info: ${DEVICE} contains no filesystem, skipped"
		exit 0
	fi

	# Figure out a mount point to use
	LABEL=${ID_FS_LABEL}
	if [ -z "${LABEL}" ]; then
		LABEL=${DEVBASE}
	elif /bin/grep -q " /media/${LABEL} " /etc/mtab; then
		# Already in use, make a unique one
		LABEL+="-${DEVBASE}"
	fi
	MOUNT_POINT="/media/${LABEL}"

	echo "Mount point: ${MOUNT_POINT}"

	/bin/mkdir -p ${MOUNT_POINT}

	# Global mount options
	OPTS="ro,relatime"

	# File system type specific mount options
	if [ ${ID_FS_TYPE} = "vfat" ]; then
		OPTS="$OPTS,fmask=0022,utf8=1"
	fi

	if ! /bin/mount -o ${OPTS} ${DEVICE} ${MOUNT_POINT}; then
		echo "Error: mounting ${DEVICE} failed (status = $?)"
		/bin/rmdir ${MOUNT_POINT}
		exit 1
	fi

	echo "**** Mounted ${DEVICE} at ${MOUNT_POINT} ****"
}

do_unmount()
{
	if [ -z ${MOUNT_POINT} ]; then
		echo "Warning: ${DEVICE} is not mounted"
	else
		/bin/umount -l ${DEVICE}
		echo "**** Unmounted ${DEVICE} ****"
	fi

	# Delete all empty dirs in /media that aren't being used as mount
	# points. This is kind of overkill, but if the drive was unmounted
	# prior to removal we no longer know its mount point, and we don't
	# want to leave it orphaned...
	for f in /media/* ; do
		if [[ -n $(/usr/bin/find "$f" -maxdepth 0 -type d -empty) ]]; then
			if ! /bin/grep -q " $f " /etc/mtab; then
				echo "**** Removing mount point $f ****"
				/bin/rmdir "$f"
			fi
		fi
	done
}

run_customer_scripts()
{
	ACTION=$1

	if [ -d "/etc/usb-mount.d" ]; then
		/bin/run-parts -a "$ACTION" -a "${MOUNT_POINT}" -- "/etc/usb-mount.d"
	fi
}

case "${ACTION}" in
	add)
		do_mount
		run_customer_scripts add
		;;
	remove)
		do_unmount
		;;
	*)
		usage
		;;
esac
