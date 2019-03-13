# API - Diskussion

- role readOnly + Enum
- pensum readOnly + start und end date
- Inkonsistentes Datenmodell: role bei Project und Allocation kann unterschiedlich sein
- includePast(Timestamp t): Wie ist der Timestamp t definiert? Oder besser mittels (from, to) loesen
- Projekt loeschen -> Vergangene Projekte duerfen nicht geloscht werden, sondern nur zukuenftige und nur vom Administrator? inactive flag bei Project braucht es nicht mehr. Beim Loeschen muessen alle Allocation auch geloescht werden -> foreign key constraint
- Projekt sistieren == Alle Resourcen abziehen und end datum auf aktuelles datum setzten
- Neuer Kundenwunsch: (from, to) Zeitfenster bei Pensum oder verschiedene (mehrere) Zeitfenster (bis August 80%, ab September bis Ende Jahr 50%)
- Pensum muss Masseinheit definiert werden, Pensum in Prozent oder in Minuten
- Wie Employee loeschen? Damit keine Dateninkonsistenz: Vergangene sensitive personendaten loeschen aber pensum beibehalten, zukuenftiges pensum auf 0 setzen
- allocation: Zeitzone aus start und end date rausnehmen, und start und end date auf Tagesbasis, alle Datumsangaben auf Tagesbasis, was fuer ein Fehler falls allocation nicht moeglich?
- Neues Attribut auf Projekt: Projektleiter, kann zweitweise wechseln -> Allocation braucht kein Role mehr
- Auf dem Employee braucht es die Role damit die Frage: Wer ist alles Projektleiter effizient beantwortet werden kann.

## Possible Front end frameworks

- JSP, JSF
- Vue, Angular, React
- Thymeleaf
- Vanilla.js
- hyperapp.js
- superfirke
- inferno.js
- Phoenix (Erlang)

## Possible Back end frameworks

- Spring Boot
- Express
- Laravel
- Vert.x
- JEE Jakarta
- Lift, Play
- Micronaut (Hip)
- Javalin
- Ratpack
- SparkJava
- (Chirook)
- Sirocco (-DB)

## Possible DB

- JPA, JOOQ
- Mongo
- JDBC
- Neo4J