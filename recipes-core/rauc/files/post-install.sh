#!/bin/sh

# Trigger a reboot after the bundle finished installing, if the file
# /run/firmware-update-reboot-requested exists, otherwise do nothing.
#
# If the update has been performed by a service which might be already stopped
# due to configuration sync, the reboot can be automatically triggered once the
# installation process has finished successfully.

REBOOT_REQUESTED_FILE=/run/firmware-update-reboot-requested
if [ ! -f "$REBOOT_REQUESTED_FILE" ]; then
    exit 0
fi

logger -t rauc-post-install "Reboot has been requested using $REBOOT_REQUESTED_FILE file"
sync

# Prefer systemd reboot if available
if command -v systemctl >/dev/null 2>&1; then
	systemctl reboot
else
	reboot
fi

exit 0
