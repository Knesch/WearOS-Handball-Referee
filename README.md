# Handball Schiedsrichter (Wear OS)

Diese App dient zum Tore zählen und Zeit stoppen auf einer Wear OS-Uhr.

Die Motivation für diese App ist, dass das heutige Handballspiel sehr schnell geworden ist und es
uns als Schiedsrichtern kaum noch möglich ist, die Tore im laufenden Spiel auf der Notizkarte zu
notieren, ohne Gefahr zu laufen, kritische Situationen nicht wahrzunehmen.
Es gibt diverse Wear OS Schiedsrichter-Apps, jedoch keine passende speziell für Handballschiedsrichter. Aus
diesem Grund habe ich mich entschlossen, eine eigene zu entwickeln.
Für Garmin-Uhren gibt es alternativ "Hektors Ref Watch", was viele Kollegen empfehlen. Hier ist nun
eine App für Wear OS.

Ziel der App ist eine einfache und intuitive Bedienung während des Spiels. Aus diesem Grund
beinhaltet sie vorrangig die Erfassung der Tore und die Stoppuhr. Für Strafen sollte weiterhin die
klassische Notizkarte oder das Tape auf dem Uhrarmband verwendet werden.

Über Feedback und Feature-Requests freue ich mich jederzeit.

## Features (Wear OS App)

* **Startbildschirm**: Schneller Einstieg über "Neues Spiel" oder Anpassung der Teams über "Konfiguration".
* **Trikotfarben-Anpassung**: In den Einstellungen können die Trikotfarben für Heim- und
  Gastmannschaft individuell gewählt werden (Weiß, Schwarz, Rot, Blau, Gelb, Grün, Orange, Lila).
* **Visuelle Orientierung**: Die Tor-Bereiche im Spielbildschirm sind in der jeweiligen Trikotfarbe
  hinterlegt, mit automatischer Kontrastanpassung der Schrift (Schwarz/Weiß).
* **Präzise Zeitnahme**: Starten und Stoppen der Spielzeit durch einfaches Tippen auf die
  Zeitanzeige.
* **Tor-Erfassung**:
    * Tippen auf die linke (Heim) oder rechte (Gast) Bildschirmhälfte fügt ein Tor hinzu.
    * Langes Drücken auf den jeweiligen Torbereich korrigiert den Spielstand (Tor abziehen).
* **Haptisches Feedback**:
    * Kurze Vibration zur Bestätigung bei Tor-Addition.
    * Lange Vibration bei Tor-Korrektur.
* **Halbzeit-Funktion**: Über das Menü erreichbar. Setzt nur die Uhr zurück, der Spielstand bleibt
  für die zweite Halbzeit erhalten.
* **Anzeigemodus-Konfiguration**: In den Einstellungen kann zwischen zwei Modi gewählt werden:
    * **Always-On Display**: Das Display bleibt während der Nutzung der App dauerhaft aktiv.
    * **Ongoing Activity**: Die App kann in den Hintergrund gewischt werden. Ein Icon auf dem Zifferblatt sowie ein Eintrag im Launcher ("Recents") erlauben den schnellen Rücksprung zum Spiel. Der aktuelle Spielstand wird dort monochrom angezeigt.
* **Automatischer Neustart**: Beim Wechsel des Anzeigemodus wird die App nach Bestätigung neu gestartet, um die System-Konfiguration (z.B. Wake-Lock) korrekt anzuwenden.
* **Sicherer Reset**: Vollständiges Zurücksetzen von Spielstand und Zeit nach Bestätigung einer
  Sicherheitsabfrage im Menü.
* **Navigation**: Intuitive Steuerung über Buttons und die Wear OS typische Swipe-Geste zum
  Zurückkehren.
* **Standalone App**: Funktioniert völlig unabhängig vom Smartphone.
* **Tile & Complication**: Schneller Zugriff auf die App und Status-Informationen direkt vom
  Zifferblatt.

## Features (Companion App - Smartphone)

* **Geräteverwaltung**: Automatische Erkennung von verbundenen Wear OS Uhren.
* **Installations-Assistent**: Prüft, ob die Wear OS App auf der Uhr installiert ist, und bietet einen direkten Link zum Play Store an.
* **Modernes Design**: Nutzt Edge-to-Edge Layout für eine optimale Ausnutzung des Bildschirms.
* **Dark-Mode Support**: Die App passt sich automatisch den Systemeinstellungen des Smartphones an (Hell/Dunkel).
* **Dynamic Color**: Unterstützt Material You (ab Android 12) für ein harmonisches Farbschema basierend auf dem Hintergrundbild des Nutzers.

## Bedienung (Wear OS)

| Aktion                            | Ergebnis                                 |
|:----------------------------------|:-----------------------------------------|
| **Tippen auf Zeit**               | Start / Stopp der Uhr                    |
| **Tippen auf Tor-Hälfte**         | Tor hinzufügen (+1)                      |
| **Langes Drücken auf Tor-Hälfte** | Tor abziehen (-1)                        |
| **Menü -> Halbzeit**              | Uhr auf 00:00 setzen (Spielstand bleibt) |
| **Menü -> Reset**                 | Alles löschen (nach Bestätigung)         |
| **Wischen von Links**             | Zurück zum vorherigen Bildschirm         |

## Technische Details

* **Plattform**: Wear OS 4.0+ (API 30+) / Android 8.0+ (API 26+)
* **UI Framework**: Jetpack Compose (Material 3)
* **Navigation**: Wear OS Navigation mit Swipe-to-Dismiss Support
* **Architektur**: MVVM (Model-View-ViewModel)
* **Sprache**: Kotlin
* **Tests**: JUnit 4 & JUnit 5 Unit Tests für Logik und StopWatch

## Installation für Entwickler

1. Projekt in **Android Studio** öffnen.
2. Ein **Wear OS Emulator** oder eine echte Uhr (via ADB) verbinden.
3. Die entsprechenden Module (`:wear` oder `:mobile`) bauen und installieren.

## Unit Tests

Die Logik der App ist durch umfangreiche Tests abgesichert. Führe sie mit folgendem Befehl aus:

```bash
./gradlew test
```

---

# Handball Referee (Wear OS)

This app is used for counting goals and stopping time on a Wear OS watch.

The motivation for this app is that today's handball game has become very fast and it is hardly possible for us as referees to note down goals on the score card during the game without running the risk of not perceiving critical situations.
There are various Wear OS referee apps, but no suitable one specifically for handball referees. For this reason, I decided to develop my own.
For Garmin watches, there is alternatively "Hektors Ref Watch", which many colleagues recommend. Here is now an app for Wear OS.

The goal of the app is simple and intuitive operation during the game. For this reason, it primarily includes goal tracking and the stopwatch. For penalties, the classic score card or tape on the watch strap should still be used.

I am always happy to receive feedback and feature requests.

## Features (Wear OS App)

* **Start screen**: Quick start via "New Game" or customization of teams via "Configuration".
* **Jersey color customization**: In the settings, the jersey colors for the home and guest teams can be selected individually (White, Black, Red, Blue, Yellow, Green, Orange, Purple).
* **Visual orientation**: The goal areas in the game screen are highlighted in the respective jersey color, with automatic contrast adjustment of the font (Black/White).
* **Precise timing**: Start and stop the game time by simply tapping on the time display.
* **Goal tracking**:
    * Tapping on the left (Home) or right (Guest) half of the screen adds a goal.
    * Long pressing on the respective goal area corrects the score (subtract goal).
* **Haptic feedback**:
    * Short vibration for confirmation when adding a goal.
    * Long vibration for goal correction.
* **Halftime function**: Accessible via the menu. Resets only the clock, the score remains for the second half.
* **Display Mode Configuration**: Choose between two modes in the settings:
    * **Always-On Display**: The display remains permanently active while using the app.
    * **Ongoing Activity**: The app can be swiped to the background. An icon on the watch face and an entry in the launcher ("Recents") allow quick return to the game. The current score is displayed there in monochrome.
* **Automatic Restart**: When changing the display mode, the app restarts after confirmation to correctly apply system configurations (e.g., wake lock).
* **Secure reset**: Complete reset of score and time after confirming a security prompt in the menu.
* **Navigation**: Intuitive control via buttons and the typical Wear OS swipe gesture to return.
* **Standalone App**: Works completely independently of the smartphone.
* **Tile & Complication**: Quick access to the app and status information directly from the watch face.

## Features (Companion App - Smartphone)

* **Device management**: Automatic detection of connected Wear OS watches.
* **Installation Assistant**: Checks whether the Wear OS app is installed on the watch and offers a direct link to the Play Store.
* **Modern Design**: Uses edge-to-edge layout for optimal use of the screen.
* **Dark-Mode Support**: The app automatically adapts to the smartphone's system settings (Light/Dark).
* **Dynamic Color**: Supports Material You (Android 12+) for a harmonious color scheme based on the user's wallpaper.

## Operation (Wear OS)

| Action                            | Result                                 |
|:----------------------------------|:-----------------------------------------|
| **Tap on time**               | Start / Stop the clock                    |
| **Tap on goal half**         | Add goal (+1)                      |
| **Long press on goal half** | Subtract goal (-1)                        |
| **Menu -> Halftime**              | Set clock to 00:00 (score remains) |
| **Menu -> Reset**                 | Clear everything (after confirmation)         |
| **Swipe from left**             | Back to previous screen         |

## Technical Details

* **Platform**: Wear OS 4.0+ (API 30+) / Android 8.0+ (API 26+)
* **UI Framework**: Jetpack Compose (Material 3)
* **Navigation**: Wear OS Navigation with Swipe-to-Dismiss support
* **Architecture**: MVVM (Model-View-ViewModel)
* **Language**: Kotlin
* **Tests**: JUnit 4 & JUnit 5 unit tests for logic and StopWatch

## Installation for Developers

1. Open project in **Android Studio**.
2. Connect a **Wear OS Emulator** or a real watch (via ADB).
3. Build and install the corresponding modules (`:wear` or `:mobile`).

## Unit Tests

The logic of the app is secured by extensive tests. Run them with the following command:

```bash
./gradlew test
```
