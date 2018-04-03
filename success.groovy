// Die Transformation muss abgeschlossen worden sein
assert aggregated['eu.esdihumboldt.hale.transform'].report.completed == true
// ... ohne Fehler
assert aggregated['eu.esdihumboldt.hale.transform'].report.errors == 0
// Als Teil der Transformation müssen Objekte erzeugt worden sein (kein leeres Ergebnis)
assert aggregated['eu.esdihumboldt.hale.transform'].createdPerType.any { name, count -> count > 0 }


// Das Schreiben der Ziel-Datei muss abgeschlossen worden sein
assert aggregated['eu.esdihumboldt.hale.io.instance.write.transformed'].report.completed == true
// ... ohne Fehler
assert aggregated['eu.esdihumboldt.hale.io.instance.write.transformed'].report.errors == 0


// Die XML Schema Validierung muss durchgeführt worden sein
assert aggregated['eu.esdihumboldt.hale.io.xml.validator']?.report?.completed == true : 'XML schema validation was not run'
// ... ohne Fehler oder Warnungen
assert aggregated['eu.esdihumboldt.hale.io.xml.validator'].report.errors == 0
assert aggregated['eu.esdihumboldt.hale.io.xml.validator'].report.warnings == 0


// Die interne Validierung mit der hale API muss durchgeführt worden sein (z.B. Prüfung von lokalen Referenzen)
assert aggregated['eu.esdihumboldt.hale.instance.validation.internal']?.report?.completed == true : 'Internal validation was not run'
// ... ohne Fehler oder Warnungen
assert aggregated['eu.esdihumboldt.hale.instance.validation.internal'].report.errors == 0
assert aggregated['eu.esdihumboldt.hale.instance.validation.internal'].report.warnings == 0
