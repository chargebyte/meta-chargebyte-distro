#!/bin/sh

led_set_attr() {
	local led="$1"
	local attr="$2"
	local val="$3"

	[ -f "/sys/class/leds/$led/$attr" ] && echo "$val" > "/sys/class/leds/$led/$attr"
}

led_get_attr() {
	local led="$1"
	local attr="$2"

	[ -f "/sys/class/leds/$led/$attr" ] && cat "/sys/class/leds/$led/$attr"
}

led_on() {
	local led="$1"
	local max_brightness="$(led_get_attr "$led" "max_brightness")"

	led_set_attr "$led" "trigger" "none"
	led_set_attr "$led" "brightness" "$max_brightness"
}

led_off() {
	local led="$1"

	led_set_attr "$led" "trigger" "none"
	led_set_attr "$led" "brightness" 0
}

led_timer() {
	local led="$1"
	local on="$2"
	local off="$3"

	led_set_attr "$led" "trigger" "timer"
	led_set_attr "$led" "delay_on" "$on"
	led_set_attr "$led" "delay_off" "$off"
}

led_blink() {
	led_timer "$1" 500 500
}

led_pulse() {
	local led="$1"
	local max_brightness="$(led_get_attr "$led" "max_brightness")"

	led_set_attr "$led" "trigger" "pattern"
	led_set_attr "$led" "pattern" "0 1000 $max_brightness 1000"
}

led_transient() {
	local led="$1"

	led_set_attr "$led" "trigger" "transient"
	led_set_attr "$led" "duration" "10"
	led_set_attr "$led" "state" "1"
}

led_transient_kick() {
	local led="$1"

	led_set_attr "$led" "activate" 1
}

rgbled_set_color() {
	local led="$1"
	local color="$2"
	local max_brightness="$(led_get_attr "$led" "max_brightness")"
	local intensity

	case "$color" in
	black)
		intensity="0 0 0 0"
		;;
	white)
		intensity="0 0 0 $max_brightness"
		;;
	red)
		intensity="$max_brightness 0 0 0"
		;;
	yellow)
		intensity="$max_brightness $max_brightness 0 0"
		;;
	green)
		intensity="0 $max_brightness 0 0"
		;;
	purple)
		intensity="$max_brightness 0 $max_brightness 0"
		;;
	blue)
		intensity="0 0 $max_brightness 0"
		;;
	*)
		echo "Unknown color" >&2
		return 1
		;;
	esac

	led_set_attr "$led" "multi_intensity" "$intensity"
	if [ "$color" = black ]; then
		led_off "$led"
	else
		led_on "$led"
	fi
}
