# Upstream's Makefile uses `ifdef C` to gate the optional sparse static-
# analysis check, but oe-core's recipe passes EXTRA_OEMAKE = "C=0" to
# *disable* it. `ifdef` only tests whether C is defined, not its value, so
# C=0 still enables the sparse invocation -- and sparse-native isn't a
# DEPENDS, so it's never present. Drop C entirely so it's genuinely undefined.
EXTRA_OEMAKE:remove = "C=0"
