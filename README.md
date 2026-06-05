# Handball Schiedsrichter (Wear OS)

Die Motivation für diese App ist, dass das heutige Handballspiel sehr schnell geworden ist und es uns als Schiedsrichtern kaum noch möglich ist, die Tore im laufenden Spiel auf der Notizkarte zu notieren, ohne Gefahr zu laufen, kritische Situationen nicht wahrzunehmen. 
Es gibt diverse Schiedsrichter-Apps, jedoch keine passende speziell für Handballschiedsrichter. Aus diesem Grund habe ich mich entschlossen, eine eigene zu entwickeln. 
Für Garmin-Uhren gibt es alternativ "Hektors Ref Watch", was viele Kollegen empfehlen. Hier ist nun eine App für Wear OS.

Ziel der App ist eine einfache und intuitive Bedienung während des Spiels. Aus diesem Grund beinhaltet sie vorrangig die Erfassung der Tore und die Stoppuhr. Für Strafen sollte weiterhin die klassische Notizkarte oder das Tape auf dem Uhrarmband verwendet werden.

Über Feedback und Feature-Requests freue ich mich jederzeit. 

## Features

*   **Startbildschirm**: Schneller Einstieg über "Neues Spiel" oder Anpassung der Teams über "Konfiguration".
*   **Trikotfarben-Anpassung**: In den Einstellungen können die Trikotfarben für Heim- und Gastmannschaft individuell gewählt werden (Weiß, Schwarz, Rot, Blau, Gelb, Grün, Orange, Lila).
*   **Visuelle Orientierung**: Die Tor-Bereiche im Spielbildschirm sind in der jeweiligen Trikotfarbe hinterlegt, mit automatischer Kontrastanpassung der Schrift (Schwarz/Weiß).
*   **Präzise Zeitnahme**: Starten und Stoppen der Spielzeit durch einfaches Tippen auf die Zeitanzeige.
*   **Tor-Erfassung**: 
    *   Tippen auf die linke (Heim) oder rechte (Gast) Bildschirmhälfte fügt ein Tor hinzu.
    *   Langes Drücken auf den jeweiligen Torbereich korrigiert den Spielstand (Tor abziehen).
*   **Haptisches Feedback**: 
    *   Kurze Vibration zur Bestätigung bei Tor-Addition.
    *   Lange Vibration bei Tor-Korrektur.
*   **Halbzeit-Funktion**: Über das Menü erreichbar. Setzt nur die Uhr zurück, der Spielstand bleibt für die zweite Halbzeit erhalten.
*   **Always-On Display**: Das Display bleibt während der Nutzung der App dauerhaft aktiv, damit die Zeit und der Spielstand jederzeit ablesbar sind.
*   **Sicherer Reset**: Vollständiges Zurücksetzen von Spielstand und Zeit nach Bestätigung einer Sicherheitsabfrage im Menü.
*   **Navigation**: Intuitive Steuerung über Buttons und die Wear OS typische Swipe-Geste zum Zurückkehren.
*   **Standalone App**: Funktioniert völlig unabhängig vom Smartphone.
*   **Tile & Complication**: Schneller Zugriff auf die App und Status-Informationen direkt vom Zifferblatt.

## Bedienung

| Aktion | Ergebnis |
| :--- | :--- |
| **Tippen auf Zeit** | Start / Stopp der Uhr |
| **Tippen auf Tor-Hälfte** | Tor hinzufügen (+1) |
| **Langes Drücken auf Tor-Hälfte** | Tor abziehen (-1) |
| **Menü -> Halbzeit** | Uhr auf 00:00 setzen (Spielstand bleibt) |
| **Menü -> Reset** | Alles löschen (nach Bestätigung) |
| **Wischen von Links** | Zurück zum vorherigen Bildschirm |

## Technische Details

*   **Plattform**: Wear OS 4.0+ (API 30+)
*   **UI Framework**: Jetpack Compose für Wear OS (Material 3)
*   **Navigation**: Wear OS Navigation mit Swipe-to-Dismiss Support
*   **Architektur**: MVVM (Model-View-ViewModel)
*   **Sprache**: Kotlin
*   **Tests**: JUnit 4 & JUnit 5 Unit Tests für Logik und StopWatch

## Installation für Entwickler

1.  Projekt in **Android Studio** öffnen.
2.  Ein **Wear OS Emulator** oder eine echte Uhr (via ADB) verbinden.
3.  Den `:app` Run-Configuration auswählen und starten.

## Unit Tests

Die Logik der App ist durch umfangreiche Tests abgesichert. Führe sie mit folgendem Befehl aus:
```bash
./gradlew test
```

---