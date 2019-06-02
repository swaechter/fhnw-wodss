# Integrationsanleitung

## Original Lösung
Link: [https://arcane-oasis-32041.herokuapp.com]()

Login: admin@admin.ch, 1234

## 1. Anleitung
Download: https://github.com/837/WODSS2019_Einsatzplanung/releases/tag/v2.1
Anleitung: https://github.com/837/WODSS2019_Einsatzplanung/tree/master/Frontend/group5-client

## 2. Integration
Code in ./Frontend/group5-client öffnen.

`npm install` ausführen.

.env.development mit unterem befüllen
```
VUE_APP_API_SERVER=https://fhnw-wodss-philipp.herokuapp.com
VUE_APP_API_PORT=443
```

`npm run serve`


Ihr token (https://jwt.io/)
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IkFMcnozRk9zZXNGTDhicURGSHNHIiwicm9sZSI6IkFETUlOSVNUUkFUT1IiLCJyYXdQYXNzd29yZCI6IjEyMzQiLCJsYXN0TmFtZSI6ImFkbWluIiwiZmlyc3ROYW1lIjoiYWRtaW4iLCJhY3RpdmUiOnRydWUsImVtYWlsQWRkcmVzcyI6ImFkbWluQGFkbWluLmNoIiwiaWF0IjoxNTU5NDg3MzcwLCJleHAiOjE1NTk1NzM3NzB9.3f1SUMoU0HoODOKRJ8nyOPZ0iAU1JI80aHGPi_DAFfY
```

src/main/javascript2/src/views/Dashboard.vue
```javascript
          this.loggedInEmployeeName = `${this.loggedInEmployee.employee.firstName} ${this.loggedInEmployee.employee.lastName}`;
          this.loggedInRole = this.loggedInEmployee.employee.role;
          this.loggedInId = String(this.loggedInEmployee.employee.id);
```




