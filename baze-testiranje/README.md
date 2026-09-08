# Scrum Backend API

Spring Boot backend aplikacija za upravljanje Scrum projektima. Koristi tri baze podataka: MongoDB, Neo4j i Redis, uz JWT autentifikaciju i real-time komunikaciju putem WebSocket-a.

## Tehnologije

- **Java 17** + **Spring Boot 3.5.4**
- **MongoDB** — glavni data store (korisnici, projekti, taskovi, poruke)
- **Neo4j** — graph baza za relacije (COLLABORATES\_WITH, WORKS\_ON)
- **Redis** — pub/sub za real-time chat
- **WebSocket** — real-time notifikacije
- **Spring Security** + **JWT** + **Google OAuth2**
- **Swagger / OpenAPI** — dokumentacija API-ja
- **Lombok**

## Funkcionalnosti

- Registracija i prijava korisnika (JWT + Google OAuth2)
- CRUD za korisnike, projekte i taskove
- Praćenje statusa taskova
- Real-time chat po projektu (Redis pub/sub + WebSocket)
- Real-time notifikacije (WebSocket)
- Graf relacija korisnika i projekata (Neo4j)
- Role-based access control (ADMIN, USER)

## Pokretanje

### 1. Pokrenuti baze podataka (Docker)

```bash
docker-compose up -d
```

Pokrece:
| Servis  | Port        |
|---------|-------------|
| MongoDB | 27017       |
| Redis   | 6379        |
| Neo4j   | 7474 / 7687 |

Neo4j kredencijali: `neo4j / test1234`

### 2. Pokrenuti aplikaciju

```bash
./mvnw spring-boot:run
```

Aplikacija se pokrece na `http://localhost:8080`.

## API Dokumentacija

Swagger UI dostupan na:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```
http://localhost:8080/api-docs
```

## Konfiguracija

Sve postavke se nalaze u `src/main/resources/application.properties`.

| Postavka | Podrazumevana vrednost |
|---|---|
| MongoDB URI | `mongodb://localhost:27017/teamdb` |
| Neo4j URI | `bolt://localhost:7687` |
| Redis host | `localhost:6379` |
| JWT expiration | 24h (86400000ms) |

## Glavne API rute

### Autentifikacija
| Metoda | Ruta | Opis |
|--------|------|------|
| POST | `/auth/register` | Registracija |
| POST | `/auth/login` | Prijava, vraca JWT token |

### Korisnici
| Metoda | Ruta | Opis |
|--------|------|------|
| GET | `/users` | Lista svih korisnika |
| GET | `/users/me` | Trenutno ulogovani korisnik |
| GET | `/users/{id}` | Korisnik po ID-u |
| POST | `/users` | Kreiranje korisnika |
| PUT | `/users/{id}` | Izmena korisnika |
| DELETE | `/users/{id}` | Brisanje korisnika |

### Projekti
| Metoda | Ruta | Opis |
|--------|------|------|
| GET | `/projects` | Lista projekata |
| POST | `/projects` | Kreiranje projekta |
| PUT | `/projects/{id}` | Izmena projekta |
| DELETE | `/projects/{id}` | Brisanje projekta |

### Taskovi
| Metoda | Ruta | Opis |
|--------|------|------|
| GET | `/tasks` | Lista taskova |
| POST | `/tasks` | Kreiranje taska |
| PUT | `/tasks/{id}` | Izmena taska |
| DELETE | `/tasks/{id}` | Brisanje taska |

### Chat
| Metoda | Ruta | Opis |
|--------|------|------|
| POST | `/chat/send` | Slanje poruke (Redis pub/sub) |
| GET | `/chat/{projectId}` | Istorija poruka projekta |

## Struktura projekta

```
src/main/java/com/example/demo/
├── DTO/                    # Data Transfer Objects
├── config/
│   ├── app/               # CORS, OpenAPI, Web konfiguracija
│   ├── redis/             # Redis i WebSocket konfiguracija
│   └── security/          # JWT filter i Security konfiguracija
├── enumeration/           # RoleName, TaskStatus enum
├── mongo/
│   ├── controller/        # REST kontroleri
│   ├── entities/          # MongoDB entiteti
│   ├── repository/        # MongoDB repozitorijumi
│   └── service/           # Biznis logika
├── neo4j/
│   ├── controller/        # Neo4j kontroleri
│   ├── entities/          # Graph node entiteti
│   ├── repository/        # Neo4j repozitorijumi
│   └── service/           # Graph servisna logika
├── redis/                 # Redis pub/sub publisher i subscriber
├── security/              # OAuth2 konfiguracija
└── websocket/             # WebSocket notifikacioni servis
```

## Testiranje

```bash
./mvnw test
```

Testovi koriste Mockito i Spring Security Test.
