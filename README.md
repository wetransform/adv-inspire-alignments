AdV INSPIRE Alignments
======================

Detaillierte Informationen zu den Alignments gibt es in der generierten Dokumentation bzw. in den ensprechenden hale-Projekten selbst.
Bei mehreren Projekt-Varianten zu einem Thema befindet sich eine kurze README im entsprechenden Ordner.

Die Alignments sind nach INSPIRE Annex und INSPIRE-Themen organisiert:

- Annex I: Ordner `annex-1/mappings`

Für das Öffnen der Alignment-Projekte oder das Generieren der Dokumentation wird halestudio in einer aktuellen Version benötigt.
Aktuelle Entwicklungsversionen können [hier](https://builds.wetransform.to/job/hale/job/hale~publish(master)/) heruntergeladen werden.


Einrichtung Gradle und hale
---------------------------

Für die Ausführung selbst wird das Werkzeug [Gradle](https://gradle.org/) verwendet, um möglichst wenig Anforderungen an das ausführende System zu stellen.

Für das Ausführen von Gradle mit hale müssen folgende Voraussetzungen geschaffen werden:

- Internetverbindung (für Verwendung eines Proxy ist weitere Konfiguration nötig)
- **Java 8** muss auf dem System installiert sein (erreichbar über `PATH` Umgebungsvariable)
- Um eine bestimmte _hale_ Version zu verwenden muss es auf dem System verfügbar sein, der Pfad zur HALE-Executable muss in der Datei `gradle.properties` angegeben werden (siehe `gradle.properties.sample` für ein Beispiel). Wird keine Angabe gemacht werden die hale-Bibliotheken für die Ausführung heruntergeladen.

Gradle selbst wird beim ersten Aufruf von `gradlew.bat` (Windows) bzw. `./gradlew` (Linux / Mac OS X) automatisch heruntergeladen. Folgend wird `gradlew` stellvertretend für den Aufruf im jeweiligen Betriebssystem verwendet.

### Proxy-Konfiguration

Gradle kann in der Datei `gradle.properties` für eine Verbindung über einen HTTP-Proxy konfiguriert werden.
Siehe auch die [entsprechende Gradle-Dokumentation](https://docs.gradle.org/current/userguide/build_environment.html#sec:accessing_the_web_via_a_proxy) oder die Beispiel-Datei `gradle.properties.sample`.

Die Einstellungen für den HTTP-Proxy für hale werden für die Transformation aus der Gradle-Konfiguration übernommen, sofern möglich.


Generieren der Dokumentation
----------------------------

Die Dokumentation zu allen hale-Projekten kann über entsprechende Gradle tasks generiert werden:

- **docHtml** - Generieren der HTML-Dokumentation
- **docExcel** - Generieren der Excel Mapping-Tabellen
- **doc** - Generieren aller Dokumentationsarten


Ausführen von Transformationen
------------------------------

Zum einfachen Ausführen von Transformationen sind Konfigurationen für die verschiedenen Projekte hinterlegt.

Für das Ausführen der Transformation müssen die Quell-Daten als XML/GML im Verzeichnis `quelldaten` abgelegt werden (Endungen `.xml`, `.gml` oder `.gz`).
Alternativ können auch anderere Standard-Ordner für die Quell-Daten konfiguriert werden (siehe Abschnitt *Weitere Konfigurationsoptionen*).

### Auflisten der Transformationen

Die konfigurierten Transformationen können über das Kommando `gradlew tasks` aufgelistet werden.

Gradle listet dabei alle *Tasks* auf die es kennt, interessant für uns ist die Kategorie *Transformation tasks*.
Die Auflistung der *Transformation tasks* sollte ähnlich wie hier aussehen:

```
Transformation tasks
--------------------
transform-all - Runs all transformation tasks.
transform-cp - Runs a transformation based on the project aaa-cp-01.halex.
...
```

Der *Task* `transform-all` ist speziell in der Hinsicht, dass er keine bestimmte, sondern alle Transformations-Tasks ausführt. Die Ausführung stoppt jedoch falls eine der Transformationen fehlschlägt.

Alle weiteren *Tasks* stehen für die Transformation eines bestimmten Projekts, ggf. in einer Variante (z.B. nach Modellart). Der Teil das Task-Namens nach `transform-` ist der Identifier der Transformation.

### Ausführen einer Transformation

Zum Ausführen einer Transformation wird einfach der entsprechende Task ausgeführt, z.B.:

```
gradlew transform-cp
```

Die transformierten Daten, die Report-Datei und die Konsolen-Ausgabe werden in einen Unterordner des Ordners `transformiert` abgelegt.
Der Name des Unterordners ist der Identifier der Transformation (z.B. `transformiert/cp`).

Vorhandene Dateien werden bei diesem Vorgang überschrieben.
Falls die Transformation frühzeitig abbricht kann es jedoch sein dass einzelne Dateien noch aus einer vorherigen Ausführung stammen.

### Prüfung der Transformation

Aktuell werden standardmäßig zwei Arten von Validierung auf den transformierten Daten durchgeführt:

1. XML Schema Validierung auf der geschriebenen GML-Datei (die Transformation schlägt fehl falls diese nicht erfolgreich ist)
2. Interne Validierung in hale vor dem Encoding der Daten (Warnungen im Report, die aktuell manuell geprüft werden müssen)

Auch wenn beide Validierungen erfolgreich sind bedeutet das noch nicht unbedingt, dass die erzeugte Datei gültig in jeder Hinsicht ist.
Die XML Schema Validierung prüft nur die Gültigkeit gegen das GML Anwendungsschema, welches aber schon aus technischen Gründen nicht alle Constraints des INSPIRE Modells umsetzt.
Die hale-interne Validierung baut hauptsächlich auf dem XML Schema auf und fügt nur beschränkt darüber hinausgehende Tests hinzu.

Speziell für letzteres, aber auch Allgemein bzgl. des gesamten Ablaufs der Transformation, empfiehlt es sich einen Blick in die Report-Datei der Transformation (`reports.log`) zu werfen.

Am einfachsten geht das indem man sie in halestudio importiert.
Dazu einfach in der **Report List**-Ansicht per Rechtsklick das Kontext-Menü öffnen, *Import Log* wählen und die Datei auswählen.

**Tip:** Falls schon andere Reports angezeigt werden, ist es einfacher die Reports der Transformation zu identifizieren, wenn man vor dem Import die Option **Clear Report List** oder **Delete Log** wählt.

### Definition von Transformationen

Die Transformationen die über Gradle als *Tasks* verfügbar sind werden über die Datei `transformations.json` definiert.
Diese Datei gilt als Referenz und Muster für die Transformations-Definitionen der hier verwalteten Projekte.
Deshalb sollten Anpassungen, die spezifisch für einen Datensatz sind oder einen Datenanbieter sind (z.B. Anpassung der INSPIRE Namespace Variablen) in einer separaten Datei erfolgen.

Zu diesem Zweck ist es möglich, Gradle zu konfigurieren eine andere Datei zum Lesen der Definitionen zu verwenden.
Dazu muss in der Datei `gradle.properties` die Eigenschaft `transformationsFile` entsprechend gesetzt werden, z.B.:

```
# Transformation definitions
transformationsFile=transformations-by.json
```

Die Definitions-Datei ist eine JSON-Datei die als benamte Einträge die einzelnen Transformations-Definitionen enthält.

Jede Transformations-Definition kann folgende Eigenschaften haben. Alle Angaben sind optional soweit nicht anders angegeben:

- **project** - Der Pfad zum hale-Projekt das für die Transformation verwendet werden soll (Pflichtangabe)
- **sourceFolder** - Der Pfad zu einem alternativen Ordner mit Quell-Daten die für die Transformation verwendet werden sollen
- **enabled** - Mit dem Wert `false` kann eine Definition deaktiviert werden ohne sie zu löschen
- **model** - Hier wird als Wert ein der Name eines AdV-Standardmodells erwartet (z.B. "DLKM"). Die Angabe wirkt sich auf drei Weisen aus: Es wird ein entsprechender Filter auf die Quell-Daten angewandt, vor der Transformation (Test des modellart-Attributs). Außerdem wird eine entsprechende Projekt-Variable gesetzt die im Alignment verwendet werden kann. Des weiteren wird ggf. ein Modell-spezifischer Ordner für die Quell-Daten verwendet (siehe _Quell-Daten aus anderem Ordner_).
- **variables** - Zuordnung von Projekt-Variablen um zum Beispiel einen spezifischen INSPIRE namespace anzugeben, der den Standard-Wert im Projekt überschreibt
- **additionalArgs** - Ein Array mit zusätzlichen Parametern die an den hale-Kommandozeilenaufruf zu übergeben sind

Hier ein Beispiel:

```
{
  "name": {
    "project": "pfad/zu/projekt.halex",
    "sourceFolder": "pfad/zu/quelldaten",
    "model": "DLKM",
    "variables": {
      "INSPIRE_NAMESPACE": "https://registry.gdi-de.org/id/de.by..."
    },
    "additionalArgs": [
      "-exclude-type",
      "AX_Flurstueck"
    ]
  },
  "transformation2": {
    ...
  }
}
```

### Weitere Konfigurationsoptionen

#### Quell-Daten aus anderem Ordner

Neben der Möglichkeit den Ordner für die Quell-Daten für eine Transformations-Definition explizit anzugeben gibt es auch die Möglichkeit, den Standard-Ordner (`quelldaten`) durch einen eigenen zu ersetzen.
Dazu wird die Gradle-Eigenschaft `defaultSourceFolder` verwendet.

Falls die Quell-Daten je nach Modellart vorliegen können auch folgende Gradle-Eigenschaften verwendet werden um den Ordner für die Daten je nach Modellart zu überschreiben:

- `defaultSourceFolder_DLKM`
- `defaultSourceFolder_Basis-DLM`
- `defaultSourceFolder_DLM50`
- `defaultSourceFolder_DLM250`
- `defaultSourceFolder_DLM1000`

Die Eigenschaft kann in der Datei `gradle.properties` konfiguriert werden (siehe auch `gradle.properties.sample`), oder auch beim Aufruf in der Kommandozeile mitgegeben werden, z.B.:

```
gradlew -PdefaultSourceFolder="C:\Meine Daten\DLKM" transform-cp
```

### Problembehandlung

#### Transformation startet nicht

Falls der Transformationsprozess über Gradle nicht startet, ist eine wahrscheinliche Ursache, dass der entsprechende Kommandozeilenaufruf nicht korrekt ist.

Durch das Übergeben des Parameters `--info` an Gradle wird beim Ausführen einer Transformation der komplette Kommandozeilen-Aufruf ausgegeben. Bitte prüfen Sie ihn auf Korrektheit (z.B. die verwendeten Pfade). Zu Testzwecken kann der Aufruf auch unabhängig von Gradle direkt ausgeführt werden.


Modell-spezifische Projekte
---------------------------

Die definierten Transformationsprojekte decken in den meisten Fällen alle relevanten AAA-Modelle ab.
Es gibt die Möglichkeit abgeleitete Projekte für ein Modell (z.B. DLM1000) zu erstellen, in denen die nicht anwendbaren Objektarten-Transformationen deaktiviert sind.
Dies erfolgt anhand der modell-spezifischen Filter-Definitionen in JSON-Dateien (z.B. `dlm-1000.model.json`).
Dort sind die Objektarten hinterlegt, die zu dem jeweiligen Modell gehören. Die Auflistung der Objektarten ist aus der HTML-Version der Objektartenkataloge übernommen worden.

Gradle Tasks um die abgeleiteten Projekte automatisch zu erzeugen werden basierend auf den Transformations-Definitionen erstellt. Voraussetzung ist dass in einer Definition eine Angabe zum Modell gemacht wurde und das Ableiten des Projekts nicht mit `"deriveProject": false` deaktiviert wurde.

Mit `gradlew tasks` können die Tasks aufgelistet werden. Alle Tasks deren Name mit `derive-` beginnt dienen zum Ableiten von modell-spezifischen Projekten.
Der Task `derive-all` ist ein spezieller Task, der das Ausführen aller Ableitungs-Tasks bewirkt.

**Hinweis:** Die Ableitungs-Tasks können nicht mit halestudio ausgeführt werden. Dazu ist eine aktuelle Version von [hale-cli](https://github.com/halestudio/hale-cli) (Konfiguration über Gradle property `haleCliExecutable`) oder die Ausführung basierend auf denen im Build konfigurierten Bibliotheken notwendig.

Um die Dokumentation für dies abgeleiteten Projekte zu erstellen müssen lediglich die Tasks zum Ableiten der Projekte vor den Dokumentations-Tasks ausgeführt werden.

Nicht in allen Fällen ist die automatische Bestimmung ob Transformationen in einem abgeleiteten Projekt enthalten sein sollen möglich. Deshalb kann es vorkommen, dass Join-Operationen in ein Projekt übernommen werden, auch wenn sie nicht relevant sind.
