tune_ext4fs () {
        tune2fs -o +journal_data ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.ext4
}

# orphan_file is on by default since e2fsprogs 1.47.0 (host mke2fs.conf).
# Devices in the field can still be running resize2fs 1.46.5, which doesn't
# know this feature and aborts RAUC's resize=true slot update with
# "Filesystem has unsupported feature(s)". Keep it disabled so OTA images
# stay resizable by older, already-deployed resize2fs binaries.
EXTRA_IMAGECMD:ext4 = "-O ^orphan_file"
IMAGE_CMD:ext4 = "oe_mkext234fs ext4 ${EXTRA_IMAGECMD}; tune_ext4fs"

clean_etc_systemd_stuff() {
    # we want /etc/systemd/system clean, so move stuff to /lib/systemd/system
    cp -a ${IMAGE_ROOTFS}${sysconfdir}/systemd/system/* ${IMAGE_ROOTFS}${systemd_unitdir}/system
    rm -rf ${IMAGE_ROOTFS}${sysconfdir}/systemd/system/*

    # this is a marker for the fw update that it is safe to migrate this directory
    echo "# this file is a marker for firmware updates that this directory can be migrated" > \
        ${IMAGE_ROOTFS}${sysconfdir}/systemd/system/.keep-during-fw-update
}

IMAGE_PREPROCESS_COMMAND:append = " clean_etc_systemd_stuff; "
