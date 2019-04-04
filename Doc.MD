# Dokumentation FHNW wodss Applikation

## Technolgiestack

TODO

## Installation

### Vorbereitung:

1.) Heroku Account erstellen

2.) Heroku CLI installieren

3.) Heroku Login

Deployment:

1.) Heroku Registry Login

```bash
heroku container:login
```

2.) Heroku Applikation erstellen

```bash
heroku create fhnw-wodss
```

3.) PostgreSQL Addon aktivieren

```bash
heroku addons:create heroku-postgresql:hobby-dev
```

3.) Credentials anzeigen

```bash
heroku config
```
Der Aufbau der URL ist wie folgt:

```
postgres://DATENBANKBENUTZER:DATENBANKPASSWORT@DATENBANKURL
```

4.) Credentials anpassen

Die auseinandergenommenen Datenbankdaten müssen nun in unsere Buildfile `build.gradle` eingefügt werden (`<XXX>` mit den oben gewonnen Werten ersetzen):

```java
buildscript {
    ext {
        databaseUrl = '<DATENBANKURL>'
        databaseUser = <DATENBANKBENUTZER>
        databasePassword = '<DATENBANKPASSWORT>'
    }
    // Snipped
}
```

Beispiel:

```java
buildscript {
    ext {
        databaseUrl = 'jdbc:postgresql://ec2-23-19-136-232.compute-1.amazonaws.com:5432/d9klsnvh3v0cbs'
        databaseUser = 'suthhpumelhsxz'
        databasePassword = '3db09f1a079fjcefad572a3jeecc07a8061be9ebde9faj5589aa585b610e4252'
    }
    // Snipped
}
```

5.) Dynamogrösse setzen

```bash
heroku ps:scale web=1
```

5.) Applikation bauen und deployen

```bash
heroku container:push web
heroku container:release web
heroku logs --tail
```

6.) Applikation im Browser öffnen

```bash
heroku open
```
