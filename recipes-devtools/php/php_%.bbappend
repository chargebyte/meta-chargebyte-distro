
PACKAGECONFIG = "openssl \
    ${@bb.utils.filter('DISTRO_FEATURES', 'ipv6 pam', d)} \
"
