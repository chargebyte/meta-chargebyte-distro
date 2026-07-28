FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-netlink-fix-empty-route-index-discovery.patch \
    file://0001-route-extend-lws_route_uidx_t-from-1-byte-to-2-bytes.patch \
"
