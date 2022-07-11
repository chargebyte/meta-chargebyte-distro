LICENSE = "CLOSED"

# Make sure that only do_install is working
deltask do_fetch
deltask do_unpack
deltask do_prepare_recipe_sysroot
deltask do_patch
deltask do_deploy_source_date_epoch
deltask do_populate_lic
deltask do_configure
deltask do_compile
deltask do_package
deltask do_packagedata
deltask do_package_qa
deltask do_package_write_deb
do_install[nostamp] = "1"


do_install() {
	tar -czvf customerfs.tar.gz --files-from /dev/null
	cp -a --no-preserve=ownership ${WORKDIR}/${BPN}-${PV}/customerfs.tar.gz ${DEPLOY_DIR_IMAGE}
}

do_clean:append() {
    filename = "customerfs.tar.gz"
    artifact_path = os.path.join(d.getVar('DEPLOY_DIR_IMAGE', True), filename)
    if os.path.exists(artifact_path):
        os.remove(artifact_path)
}
