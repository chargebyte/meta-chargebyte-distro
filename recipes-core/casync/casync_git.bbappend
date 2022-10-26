# casync is usually not interactively used,
# so let's move bash completion stuff to developer build

FILES:${PN}:remove = "${datadir}/bash-completion"
FILES:${PN}-dev += "${datadir}/bash-completion"
