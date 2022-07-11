#
# This fixup eliminates the harmless but annoying message during the first boot:
# logrotate[535]: error: state file /var/lib/logrotate.status is world-readable and thus can be locked from other unprivileged users. Skipping lock acquisition...
# The upstream recipe should be fixed.
#
do_install:append() {
    # remove world-readable permissions
    chmod o-rwx ${D}${localstatedir}/lib/logrotate.status
}
