#!/bin/sh

. /lib/led.sh

MODEL=$(tr -d '\0' < /proc/device-tree/model)

# fallback if not overridden later
TRIGGER="none"

case "$MODEL" in
"I2SE EVAcharge SE")
	LED="evse:green:led3"
	;;
"chargebyte Tarragon"*)
	LED="green:boot"
	;;
"I2SE Tarragon"*)
	LED="evse:green:led1"
	;;
"chargebyte Charge SOM"*)
	LED="red:boot"
	TRIGGER="heartbeat"
	;;
*)
	exit 0
esac

led_set_attr "$LED" "trigger" "$TRIGGER"

MAXBRIGHTNESS="$(led_get_attr "$LED" "max_brightness")"
led_set_attr "$LED" "brightness" "$MAXBRIGHTNESS"
