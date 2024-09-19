# fix build without pandoc
# Issue: https://github.com/tpm2-software/tpm2-tss-engine/issues/284
# PR: https://github.com/tpm2-software/tpm2-tss-engine/pull/285
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-Makefile.am-Fix-build-without-pandoc.patch \
"
