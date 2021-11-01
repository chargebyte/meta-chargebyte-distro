# this fixes an error when development tools are pulling in bzip2 as native package
# but we already have the busybox applet enabled; this fix seems to be already mainlined
# at time of writing this comment
ALTERNATIVE_${PN} += " bzip2"
