# 📌 PINAPP - Spring Boot API

API desarrollada con Spring Boot 3.2.0 y Java 17 para la gestión de clientes y autenticación JWT. Contiene integración con Swagger, generación de logs mediante Log4j2 y soporte para despliegue con Docker.

---

## 🚀 Tecnologías utilizadas

- **Spring Boot 3.2.0**
- **Java 17**
- **JWT (Auth0 - java-jwt 4.4.0)**
- **Swagger / OpenAPI**
- **Log4j2**
- **Docker & Docker Compose**

---

## 🔐 Autenticación

Se utiliza autenticación JWT mediante la librería [`java-jwt`](https://github.com/auth0/java-jwt). El endpoint de login genera un token con los datos del usuario.

**POST /auth/login**

### Ejemplo de request:


```json
{
  "email": "admin@pinapp.com",
  "password": "admin123"
}
```

### Ejemplo de response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
  "role": "ADMIN",
  "email": "admin@pinapp.com",
  "name": "Admin"
}
```

## 👥 Endpoints de Clientes
#### POST /clients
Crea un nuevo cliente. Se debe enviar un body con los datos básicos.

#### GET /clients
Devuelve una lista de todos los clientes registrados junto con su edad estimada de fallecimiento.

### Ejemplo de respuesta:

```json

[
  {
    "firstName": "Alice",
    "lastName": "Johnson",
    "dni": 10101010,
    "age": 25,
    "birthDate": "1999-04-12",
    "estimatedDateOfDeath": "2079-04-12"
  },
  ...
]
```

#### GET /clients/metrics
Devuelve métricas estadísticas de los clientes actuales:

Ejemplo de respuesta:

```json
{
  "averageAge": 35.0,
  "standardDeviation": 10.57,
  "ageGroups": {
    "10-19": 1,
    "20-29": 6,
    "30-39": 6,
    "40-49": 5,
    "50-59": 1,
    "60-69": 1
  }
}
``` 

## 📒 Swagger (Documentación)
La documentación de la API está disponible en:

📎 http://localhost:8001/swagger-ui/index.html#/

## 🪵 Logs
Los logs de la aplicación se almacenan usando Log4j2.

📁 Los archivos se encuentran en la carpeta /logs en la raíz del proyecto, con el nombre:

applicationPinApp.log

## 🌱 Data Seed
Al iniciar la aplicación se generan automáticamente:

20 clientes con su nombre, apellido, dni, edad y fecha de nacimiento.

1 usuario administrador:

Email: javierramosnicolas@gmail.com

Password: Admin1234


## 🐳 Docker
El proyecto puede ejecutarse fácilmente usando Docker Compose.

Comandos recomendados:

```json
docker-compose build --no-cache
docker-compose up
```

## 🧑‍💻 Autor
Javier Ramos
📧 javierramosnicolas@gmail.com
