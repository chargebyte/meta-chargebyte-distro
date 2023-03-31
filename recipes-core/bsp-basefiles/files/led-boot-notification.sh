#!/bin/sh

. /lib/led.sh

MODEL=$(tr -d '\0' < /proc/device-tree/model)

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
*)
	exit 0
esac

led_set_attr "$LED" "trigger" "none"
led_set_attr "$LED" "brightness" 200
