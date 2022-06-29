FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://base-files.cfg \
    file://disable-ifup-ifdown.cfg \
"
