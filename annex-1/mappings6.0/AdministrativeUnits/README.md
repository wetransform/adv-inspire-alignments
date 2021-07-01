Administrative Units
====================

Alignments aus dem 3A-Modell in das INSPIRE Administrative Units Anwendungsschema.

Es gibt drei Varianten des Alignments:

- `aaa-au-gebiete.halex` - Verwendung vorgegebener Geometrien (`AX_Gebiet_*`, `AX_Kommunales_Gebiet`)
- `aaa-au-kommunalesGebiet.halex` - Aggregation aus `AX_Kommunales_Gebiet`
- `aaa-au-flurstuecke.halex` - Aggregation aus `AX_Flurstueck`

Diese bauen wiederum auf einem gemeinsamen Basis-Alignment auf (`aaa-au-basis.halex`).

Zusätzlich gibt es hier ein Beispiel-Projekt, dass verwendet wurde um `AX_KommunalesGebiet` für Testzwecke aus Flurstücken zu erstellen, da keine im Testdatensatz Mustermonzel vorhanden waren (`hilfsprojekt-kommunalesGebiet.halex`).

