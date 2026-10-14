# AC-powerboard certification menu

`ac-powerboard-certification-menu` is a text-based menu for the certification process of the AC-powerboard. With it you can switch the radios (WiFi, Bluetooth) on and off, connect the board to a WiFi network, switch the EVerest charging mode and run RF tests for BLE and WiFi.

The menu is a front end for the command line tool `ac-powerboard-certification`. Every action in the menu runs a command of this tool, see `ac-powerboard-certification --help`.

## Starting the menu

1. Log in on the board as `root`, either via SSH or the serial console.
2. Start the menu:

   ```bash
   ac-powerboard-certification-menu
   ```

The menu must run as `root`. The terminal window should be at least 80 columns wide and 25 lines high.

## Navigation

| Key | Action |
|---|---|
| Arrow up / down | Select an entry |
| Enter | Run the selected entry |
| Tab, arrow left / right | Switch between the buttons (e.g. `OK` / `Back`) |
| Esc | Back to the previous menu, or exit in the main menu |

Values in input fields are confirmed with Enter. Invalid values are rejected with an error message and the previous value is kept.

## Main menu

The upper part of the main menu shows the current status of the board:

| Line | Meaning |
|---|---|
| `WiFi` | WiFi on / off / unavailable and the state of the `wpa_supplicant@wlan0` service |
| `Bluetooth` | Bluetooth on / off / unavailable and the state of the `bluetooth` service |
| `Drivers` | CC33xx radio drivers loaded or unloaded |
| `WiFi net` | Connected WiFi network and IP address, `not connected`, or `-` if WiFi is off |
| `EVerest` | Active charging mode (`pwm`, `hlc`, `hlc-only`, or `custom`) and the state of `everest.service` |
| `RF test` | The RF test that is currently running, or `none` |

The status is updated every time you return to the main menu.

### WiFi: turn on / off

Switches WiFi on or off. Off blocks WiFi via rfkill and stops the `wpa_supplicant@wlan0` service. On reverses this. The label always shows the action that is possible next.

### WiFi client network

Connects the board to a WiFi network as a client. Only one network is stored at a time. WiFi must be switched on. The upper part of the submenu shows the configured network, the connection state, the IP address and the country.

- **Scan and connect:** Searches for networks for about 5 seconds and lists them with signal strength and security, strongest first. Select a network and enter the passphrase if required. Open networks and WPA/WPA2 (PSK) networks are supported. WPA3-only, enterprise and WEP networks are marked as unsupported and cannot be used.
- **Connect to network by name:** Enter the SSID manually, e.g. for hidden networks. Leave the passphrase empty for an open network.
- **Forget network:** Removes the stored network after a confirmation.
- **Country (regulatory domain):** Sets the country that defines the allowed WiFi channels and transmit power. Select a country from the list, `00` for the most restrictive world setting, or `Other...` to enter any ISO 3166-1 alpha-2 code (e.g. `DE`). The default is `AT`.

Connecting takes up to a minute. The passphrase must have 8 to 63 characters, or be a 64 digit hex key. Only the derived key is stored on the board, not the plain passphrase. If the connection fails, check the SSID and the passphrase.

The network settings are stored permanently and survive a reboot.

### Bluetooth: turn on / off

Switches Bluetooth on or off. Off stops the `bluetooth` service and blocks Bluetooth via rfkill. On reverses this and powers the controller up again.

### CC33xx drivers: load / unload (WiFi + Bluetooth)

Unloads or loads the drivers of the CC33xx radio module. Unloading powers down WiFi and Bluetooth together. Loading takes about a minute (up to 2 minutes) while the firmware starts, after that WiFi and Bluetooth are started again.

### EVerest charging mode

Switches the charging mode of EVerest. The current mode is preselected.

| Mode | Meaning |
|---|---|
| PWM only (`pwm`) | Basic charging via PWM, no high level communication |
| HLC with PWM fallback (`hlc`) | ISO 15118 high level communication, falls back to PWM if the vehicle does not support it |
| HLC only (`hlc-only`) | Only ISO 15118 high level communication |

The mode is written to `/etc/everest/config.yaml` and EVerest is restarted. A running charging session is interrupted. The setting survives a reboot. If the main menu shows `custom`, the configuration was changed manually and does not match any of the three modes.

### BLE channel test

Runs Bluetooth Low Energy test mode on the radio (HCI LE test commands). Bluetooth must be on. The upper part of the submenu shows the running test.

| Entry | Meaning |
|---|---|
| Start TX test | Starts continuous transmission with the settings below |
| Start RX test | Starts receiving on the selected channel and PHY |
| Stop test | Stops the running test. For an RX test, the number of received packets is shown |
| Channel | BLE channel 0 to 39, frequency = 2402 + 2 x channel MHz (default 19, 2440 MHz) |
| Payload length | 0 to 255 bytes (default 37) |
| Payload pattern | PRBS9, 11110000, 10101010, PRBS15, 11111111, 00000000, 00001111, 01010101 (default PRBS9) |
| PHY | LE 1M, LE 2M, LE Coded S8, LE Coded S2 (default LE 1M) |

Payload length and pattern are only used for the TX test. For the RX test, LE Coded S8 and S2 are the same receiver setting. Change the settings before starting a test, they are not applied to a test that is already running.

### WiFi RF test

Runs RF tests with the TI CC33xx calibrator in production line test (PLT) mode. WiFi must be on. During the test, the normal WiFi operation is stopped and an existing WiFi connection is lost. It is restored when the test is stopped. The upper part of the submenu shows the running test.

| Entry | Meaning |
|---|---|
| Start TX test (packets) | Starts continuous packet transmission with the settings below |
| Start TX test (single tone) | Starts transmission of an unmodulated carrier on the channel, shifted by the tone offset |
| Start RX test | Starts receiving on the selected channel and counts packets from all senders |
| Show RX statistics | Shows the statistics of the running RX test |
| Stop test | Stops the running test and restores normal WiFi operation. For an RX test, the final statistics are shown |
| Channel | WiFi channel 1 to 13 (2.4 GHz), frequency = 2407 + 5 x channel MHz (default 6, 2437 MHz) |
| Preamble | 11b short, 11b long, 11a/g OFDM, 11n mixed mode, 11ax SU, 11ax SU ER (default 11a/g OFDM) |
| Rate | Data rate, depends on the preamble: 1 to 11 Mbps for 11b, 6 to 54 Mbps for OFDM, MCS0 to MCS7 for 11n/11ax (default 6 Mbps) |
| TX power | -10 to 20 dBm (default 0 dBm) |
| Packet length | 0 to 16000 bytes |
| Packet delay | Pause between packets, 50 to 1000000 us |
| Tone offset | Offset of the single tone, -40 to 40 in 0.25 MHz steps (-10 to +10 MHz, default 0) |

Notes:

- Changing the rate also sets packet length and delay to values for a duty cycle of about 30 % (TI SDAA443, table 8-1). Adjust them afterwards if you need different values.
- Changing the preamble selects the first valid rate if the current rate does not fit the new preamble.
- Change the settings before starting a test, they are not applied to a test that is already running.

### Stop running RF test

Stops the running BLE or WiFi test, same as `Stop test` in the submenus.

## General notes

- Only one RF test can run at a time. Stop the running test before starting another one.
- When you exit the menu while an RF test is running, you are asked for confirmation. The test keeps running after the menu is closed. Start the menu again to stop it.
- The test settings (channel, power, ...) are saved in `/var/lib/ac-powerboard-certification/settings` and are restored when the menu is started again.
- Radio on/off, driver changes and RF tests only last until the next reboot. After a reboot, the board is in normal operation again.
- WiFi network, WiFi country and EVerest charging mode are stored permanently.
- Error messages of the command line tool are shown in an `Error` window.
