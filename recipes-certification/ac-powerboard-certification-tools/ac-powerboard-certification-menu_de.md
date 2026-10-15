# AC-Powerboard Zertifizierungsmenü

`ac-powerboard-certification-menu` ist ein textbasiertes Menü für den Zertifizierungsprozess des AC-Powerboards. Damit können die Funkmodule (WLAN, Bluetooth) ein- und ausgeschaltet, das Board mit einem WLAN verbunden, der EVerest-Lademodus umgeschaltet und HF-Tests für BLE und WLAN durchgeführt werden.

Das Menü ist eine Oberfläche für das Kommandozeilenwerkzeug `ac-powerboard-certification`. Jede Aktion im Menü führt einen Befehl dieses Werkzeugs aus, siehe `ac-powerboard-certification --help`.

## Menü starten

1. Am Board als `root` anmelden, entweder per SSH oder über die serielle Konsole.
2. Menü starten:

   ```bash
   ac-powerboard-certification-menu
   ```

Das Menü muss als `root` ausgeführt werden. Das Terminalfenster sollte mindestens 80 Spalten breit und 25 Zeilen hoch sein.

## Bedienung

| Taste | Aktion |
|---|---|
| Pfeil hoch / runter | Eintrag auswählen |
| Enter | Ausgewählten Eintrag ausführen |
| Tab, Pfeil links / rechts | Zwischen den Schaltflächen wechseln (z. B. `OK` / `Back`) |
| Esc | Zurück zum vorherigen Menü, im Hauptmenü beenden |

Werte in Eingabefeldern werden mit Enter bestätigt. Ungültige Werte werden mit einer Fehlermeldung abgelehnt, der bisherige Wert bleibt erhalten.

## Hauptmenü

Der obere Teil des Hauptmenüs zeigt den aktuellen Zustand des Boards:

| Zeile | Bedeutung |
|---|---|
| `WiFi` | WLAN ein / aus / nicht verfügbar und Zustand des Dienstes `wpa_supplicant@wlan0` |
| `Bluetooth` | Bluetooth ein / aus / nicht verfügbar und Zustand des Dienstes `bluetooth` |
| `Drivers` | CC33xx-Funktreiber geladen (`loaded`) oder entladen (`unloaded`) |
| `WiFi net` | Verbundenes WLAN und IP-Adresse, `not connected`, oder `-` wenn WLAN aus ist |
| `EVerest` | Aktiver Lademodus (`pwm`, `hlc`, `hlc-only` oder `custom`) und Zustand von `everest.service` |
| `RF test` | Der gerade laufende HF-Test, oder `none` |

Der Zustand wird bei jeder Rückkehr ins Hauptmenü aktualisiert.

### WiFi: turn on / off

Schaltet WLAN ein oder aus. Aus sperrt WLAN per rfkill und stoppt den Dienst `wpa_supplicant@wlan0`. Ein macht dies rückgängig. Die Beschriftung zeigt immer die nächste mögliche Aktion.

### WiFi client network

Verbindet das Board als Client mit einem WLAN. Es wird immer nur ein Netzwerk gespeichert. WLAN muss eingeschaltet sein. Der obere Teil des Untermenüs zeigt das konfigurierte Netzwerk, den Verbindungszustand, die IP-Adresse und das Land.

- **Scan and connect:** Sucht ca. 5 Sekunden nach Netzwerken und listet sie mit Signalstärke und Verschlüsselung auf, das stärkste zuerst. Netzwerk auswählen und, falls nötig, die Passphrase eingeben. Unterstützt werden offene Netzwerke und WPA/WPA2 (PSK). Reine WPA3-, Enterprise- und WEP-Netzwerke werden als nicht unterstützt markiert und können nicht verwendet werden.
- **Connect to network by name:** SSID manuell eingeben, z. B. für versteckte Netzwerke. Für ein offenes Netzwerk die Passphrase leer lassen.
- **Forget network:** Entfernt das gespeicherte Netzwerk nach einer Bestätigung.
- **Country (regulatory domain):** Setzt das Land, das die erlaubten WLAN-Kanäle und Sendeleistungen festlegt. Ein Land aus der Liste wählen, `00` für die restriktivste weltweite Einstellung, oder `Other...` um einen beliebigen ISO 3166-1 alpha-2 Code einzugeben (z. B. `DE`). Voreingestellt ist `AT`.

Der Verbindungsaufbau dauert bis zu einer Minute. Die Passphrase muss 8 bis 63 Zeichen lang sein oder ein 64-stelliger Hex-Schlüssel. Auf dem Board wird nur der daraus abgeleitete Schlüssel gespeichert, nicht die Passphrase im Klartext. Schlägt die Verbindung fehl, SSID und Passphrase prüfen.

Die Netzwerkeinstellungen werden dauerhaft gespeichert und bleiben nach einem Neustart erhalten.

### Bluetooth: turn on / off

Schaltet Bluetooth ein oder aus. Aus stoppt den Dienst `bluetooth` und sperrt Bluetooth per rfkill. Ein macht dies rückgängig und schaltet den Controller wieder ein.

### CC33xx drivers: load / unload (WiFi + Bluetooth)

Entlädt oder lädt die Treiber des CC33xx-Funkmoduls. Entladen schaltet WLAN und Bluetooth gemeinsam ab. Das Laden dauert ca. eine Minute (bis zu 2 Minuten), während die Firmware startet. Danach werden WLAN und Bluetooth wieder gestartet.

### EVerest charging mode

Schaltet den Lademodus von EVerest um. Der aktuelle Modus ist vorausgewählt.

| Modus | Bedeutung |
|---|---|
| PWM only (`pwm`) | Basisladen über PWM, keine High-Level-Kommunikation |
| HLC with PWM fallback (`hlc`) | ISO 15118 High-Level-Kommunikation, Rückfall auf PWM wenn das Fahrzeug sie nicht unterstützt |
| HLC only (`hlc-only`) | Ausschließlich ISO 15118 High-Level-Kommunikation |

Der Modus wird in `/etc/everest/config.yaml` geschrieben und EVerest neu gestartet. Ein laufender Ladevorgang wird dabei abgebrochen. Die Einstellung bleibt nach einem Neustart erhalten. Zeigt das Hauptmenü `custom`, wurde die Konfiguration manuell geändert und entspricht keinem der drei Modi.

### BLE channel test

Führt den Bluetooth-Low-Energy-Testmodus auf dem Funkmodul aus (HCI LE Testbefehle). Bluetooth muss eingeschaltet sein. Der obere Teil des Untermenüs zeigt den laufenden Test.

| Eintrag | Bedeutung |
|---|---|
| Start TX test | Startet Dauersenden mit den unten stehenden Einstellungen |
| Start RX test | Startet den Empfang auf dem gewählten Kanal und PHY |
| Stop test | Stoppt den laufenden Test. Bei einem RX-Test wird die Anzahl der empfangenen Pakete angezeigt |
| Channel | BLE-Kanal 0 bis 39, Frequenz = 2402 + 2 x Kanal MHz (Standard 19, 2440 MHz) |
| Payload length | 0 bis 255 Bytes (Standard 37) |
| Payload pattern | PRBS9, 11110000, 10101010, PRBS15, 11111111, 00000000, 00001111, 01010101 (Standard PRBS9) |
| PHY | LE 1M, LE 2M, LE Coded S8, LE Coded S2 (Standard LE 1M) |

Payload length und Payload pattern werden nur beim TX-Test verwendet. Beim RX-Test sind LE Coded S8 und S2 dieselbe Empfängereinstellung. Einstellungen vor dem Start eines Tests ändern, auf einen bereits laufenden Test werden sie nicht angewendet.

### WiFi RF test

Führt HF-Tests mit dem TI CC33xx Calibrator im Production-Line-Test-Modus (PLT) aus. WLAN muss eingeschaltet sein. Während des Tests ist der normale WLAN-Betrieb gestoppt und eine bestehende WLAN-Verbindung wird getrennt. Beim Stoppen des Tests wird er wiederhergestellt. Der obere Teil des Untermenüs zeigt den laufenden Test.

| Eintrag | Bedeutung |
|---|---|
| Start TX test (packets) | Startet Dauersenden von Paketen mit den unten stehenden Einstellungen |
| Start TX test (single tone) | Startet das Senden eines unmodulierten Trägers auf dem Kanal, verschoben um den Tone offset |
| Start RX test | Startet den Empfang auf dem gewählten Kanal und zählt Pakete aller Sender |
| Show RX statistics | Zeigt die Statistik des laufenden RX-Tests |
| Stop test | Stoppt den laufenden Test und stellt den normalen WLAN-Betrieb wieder her. Bei einem RX-Test wird die abschließende Statistik angezeigt |
| Channel | WLAN-Kanal 1 bis 13 (2,4 GHz), Frequenz = 2407 + 5 x Kanal MHz (Standard 6, 2437 MHz) |
| Preamble | 11b short, 11b long, 11a/g OFDM, 11n mixed mode, 11ax SU, 11ax SU ER (Standard 11a/g OFDM) |
| Rate | Datenrate, abhängig von der Preamble: 1 bis 11 Mbps für 11b, 6 bis 54 Mbps für OFDM, MCS0 bis MCS7 für 11n/11ax (Standard 6 Mbps) |
| TX power | -10 bis 20 dBm (Standard 0 dBm) |
| Packet length | 0 bis 16000 Bytes |
| Packet delay | Pause zwischen den Paketen, 50 bis 1000000 µs |
| Tone offset | Verschiebung des Einzeltons, -40 bis 40 in 0,25-MHz-Schritten (-10 bis +10 MHz, Standard 0) |

Hinweise:

- Beim Ändern der Rate werden Packet length und Packet delay auf Werte für ein Tastverhältnis von ca. 30 % gesetzt (TI SDAA443, Tabelle 8-1). Werden andere Werte benötigt, diese danach anpassen.
- Beim Ändern der Preamble wird die erste gültige Rate gewählt, wenn die aktuelle Rate nicht zur neuen Preamble passt.
- Einstellungen vor dem Start eines Tests ändern, auf einen bereits laufenden Test werden sie nicht angewendet.

### Stop running RF test

Stoppt den laufenden BLE- oder WLAN-Test, wie `Stop test` in den Untermenüs.

## Allgemeine Hinweise

- Es kann immer nur ein HF-Test gleichzeitig laufen. Den laufenden Test stoppen, bevor ein anderer gestartet wird.
- Wird das Menü bei laufendem HF-Test beendet, erscheint eine Sicherheitsabfrage. Der Test läuft nach dem Schließen des Menüs weiter. Zum Stoppen das Menü erneut starten.
- Die Testeinstellungen (Kanal, Leistung, ...) werden in `/var/lib/ac-powerboard-certification/settings` gespeichert und beim nächsten Start des Menüs wiederhergestellt.
- Funk ein/aus, Treiberänderungen und HF-Tests gelten nur bis zum nächsten Neustart. Nach einem Neustart ist das Board wieder im Normalbetrieb.
- WLAN-Netzwerk, WLAN-Land und EVerest-Lademodus werden dauerhaft gespeichert.
- Fehlermeldungen des Kommandozeilenwerkzeugs werden in einem Fenster `Error` angezeigt.
