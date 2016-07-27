AdV INSPIRE Alignments
======================

Detaillierte Informationen zu den Alignments gibt es in der generierten Dokumentation bzw. in den ensprechenden hale-Projekten selbst.
Bei mehreren Projekt-Varianten zu einem Thema befindet sich eine kurze README im entsprechenden Ordner.

Die Alignments sind nach INSPIRE Annex und INSPIRE-Themen organisiert:

- Annex I: Ordner `annex-1/mappings`

Für das Öffnen der Alignment-Projekte oder das Generieren der Dokumentation wird halestudio in einer aktuellen Version benötigt.
Aktuelle Entwicklungsversionen können [hier](https://builds.wetransform.to/job/hale/job/hale~publish(master)/) heruntergeladen werden.


Ausführung der Transformation
-----------------------------

Zum einfachen Ausführen von Transformationen sind Konfigurationen für die verschiedenen Projekte hinterlegt.
Für die Ausführung selbst wird das Werkzeug [Gradle](https://gradle.org/) verwendet, um möglichst wenig Anforderungen an das ausführende System zu stellen.

Für das Ausführen der Transformation müssen folgende Voraussetzungen geschaffen werden:

- Internetverbindung (für Verwendung eines Proxy ist weitere Konfiguration nötig)
- **Java 8** muss auf dem System installiert sein (erreichbar über `PATH` Umgebungsvariable)
- **hale** muss auf dem System in einer passenden Version verfügbar sein, der Pfad zur HALE-Executable muss in der Datei `gradle.properties` angegeben werden (siehe `gradle.properties.sample` für ein Beispiel)
- Die Quell-Daten als XML/GML müssen im Verzeichnis `quelldaten` abgelegt werden (Endungen `.xml`, `.gml` oder `.gz`). Alternativ kann auch ein anderer Standard-Ordner für die Quell-Daten konfiguriert werden (siehe Abschnitt *Weitere Konfigurationsoptionen*).

Gradle selbst wird beim ersten Aufruf von `gradlew.bat` (Windows) bzw. `./gradlew` (Linux / Mac OS X) automatisch heruntergeladen. Folgend wird `gradlew` stellvertretend für den Aufruf im jeweiligen Betriebssystem verwendet.

### Proxy-Konfiguration

Gradle kann in der Datei `gradle.properties` für eine Verbindung über einen HTTP-Proxy konfiguriert werden.
Siehe auch die [entsprechende Gradle-Dokumentation](https://docs.gradle.org/current/userguide/build_environment.html#sec:accessing_the_web_via_a_proxy) oder die Beispiel-Datei `gradle.properties.sample`.

Die Einstellungen für den HTTP-Proxy für hale werden für die Transformation aus der Gradle-Konfiguration übernommen, sofern möglich.

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
- **model** - Hier wird als Wert ein der Name eines AdV-Standardmodells erwartet (z.B. "DLKM"). Die Angabe wirkt sich auf zwei Weisen aus: Es wird ein entsprechender Filter auf die Quell-Daten angewandt, vor der Transformation (Test des modellart-Attributs). Außerdem wird eine entsprechende Projekt-Variable gesetzt die im Alignment verwendet werden kann.
- **variables** - Zuordnung von Projekt-Variablen um zum Beispiel einen spezifischen INSPIRE namespace anzugeben, der den Standard-Wert im Projekt überschreibt

Hier ein Beispiel:

```
{
  "name": {
    "project": "pfad/zu/projekt.halex",
    "sourceFolder": "pfad/zu/quelldaten",
    "model": "DLKM",
    "variables": {
      "INSPIRE_NAMESPACE": "https://registry.gdi-de.org/id/de.by..."
    }
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
Sie kann in der Datei `gradle.properties` konfiguriert werden, oder auch beim Aufruf in der Kommandozeile mitgegeben werden, z.B.:

```
gradlew -PdefaultSourceFolder=quelldaten/dlkm transform-cp
```

### Problembehandlung

#### Transformation startet nicht

Falls der Transformationsprozess über Gradle nicht startet, ist eine wahrscheinliche Ursache, dass der entsprechende Kommandozeilenaufruf nicht korrekt ist.

Bitte prüfen Sie zuerst ob Sie eine Datei `gradle.properties` angelegt haben, in der nach dem Muster der Datei `gradle.properties.sample` mindestens die Eigenschaft **haleExecutable** konfiguriert ist, z.B.:

```
haleExecutable=C:\my\hale\installation\HALE.exe
```

Durch das Übergeben des Parameters `--info` an Gradle wird beim Ausführen einer Transformation der komplette Kommandozeilen-Aufruf ausgegeben. Bitte prüfen Sie ihn auf Korrektheit (z.B. die verwendeten Pfade). Zu Testzwecken kann der Aufruf auch unabhängig von Gradle direkt ausgeführt werden.
