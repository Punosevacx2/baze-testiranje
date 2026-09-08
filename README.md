# Scrum Project Management Platform

A full-stack project management application built on a **polyglot persistence** architecture, combining MongoDB, Neo4j, and Redis to handle different data concerns efficiently. The backend is powered by Spring Boot 3 with JWT-based authentication, role-based access control, real-time WebSocket messaging, and a full REST API documented via Swagger.

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Roles & Permissions](#roles--permissions)
- [Database Design](#database-design)

---

## Architecture Overview

```
┌─────────────────────────────────────────────┐
│              Spring Boot Backend             │
│                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │ MongoDB  │  │  Neo4j   │  │  Redis   │  │
│  │          │  │          │  │          │  │
│  │ Users    │  │ UserNode │  │ Sessions │  │
│  │ Projects │  │ ProjNode │  │ Cache    │  │
│  │ Tasks    │  │ TaskNode │  │          │  │
│  │ Chat     │  │Relations │  │          │  │
│  └──────────┘  └──────────┘  └──────────┘  │
└─────────────────────────────────────────────┘
```

| Store   | Purpose |
|---------|---------|
| **MongoDB** | Primary document store — users, projects, tasks, chat messages, notifications |
| **Neo4j** | Graph store — relationships between users, projects, and tasks (collaborations, dependencies, memberships) |
| **Redis** | Caching and session management |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Primary DB | MongoDB |
| Graph DB | Neo4j |
| Cache | Redis |
| Auth | JWT (JJWT 0.11.5) + Google OAuth2 |
| Security | Spring Security |
| Real-time | WebSocket (STOMP) |
| API Docs | SpringDoc OpenAPI / Swagger UI |
| Build | Maven |
| Infrastructure | Docker Compose |
| Utilities | Lombok |

---

## Features

- **Authentication** — JWT-based login/signup with access & refresh tokens stored as HTTP-only cookies; Google OAuth2 login
- **Role-Based Access Control** — Three roles (`USER`, `MANAGER`, `ADMIN`) with method-level security (`@PreAuthorize`)
- **Project Management** — CRUD for projects; creation syncs data across MongoDB and Neo4j simultaneously
- **Task Management** — CRUD for tasks with status tracking and deadlines; tasks synced to Neo4j for dependency graphing
- **Graph Relations** — User collaborations, task dependencies, and project memberships managed in Neo4j
- **Real-time Chat** — Per-project chat via WebSocket; message history persisted in MongoDB
- **Notifications** — Notification entity stored in MongoDB
- **Swagger UI** — Interactive API documentation available at `/swagger-ui.html`

---

## Project Structure

```
src/main/java/com/example/demo/
├── config/
│   ├── app/              # CORS, Web, OpenAPI configuration
│   ├── redis/            # Redis & WebSocket configuration
│   └── security/         # JWT filter, Security config
├── DTO/                  # Request/Response Data Transfer Objects
├── enumeration/          # RoleName, TaskStatus enums
├── mongo/
│   ├── controller/       # AuthenticationController, ProjectController,
│   │                     # TaskController, UserController, ChatController
│   ├── entities/         # User, Project, Task, Role, Notification, ProjectMessage
│   ├── repository/       # Spring Data MongoDB repositories
│   └── service/          # Business logic
├── neo4j/
│   ├── controller/       # RelationController, UserNodeController
│   ├── entities/         # UserNode, ProjectNode, TaskNode
│   ├── repository/       # Spring Data Neo4j repositories
│   └── service/          # Graph relation logic
├── redis/                # Redis service
├── security/             # Security utilities
└── websocket/            # WebSocket handlers
```

---

## Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose

---

## Getting Started

### 1. Start infrastructure

```bash
docker-compose up -d
```

This starts:
- **MongoDB** on port `27017`
- **Redis** on port `6379`
- **Neo4j** on port `7474` (browser) / `7687` (Bolt)

Neo4j credentials: `neo4j / test1234`

### 2. Configure the application

Edit `src/main/resources/application.properties` and set your own values for:

```properties
# Google OAuth2 (replace with your credentials)
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET

# JWT secret key
security.jwt.secret-key=YOUR_SECRET_KEY
```

### 3. Build and run

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

### 4. Access Swagger UI

```
http://localhost:8080/swagger-ui.html
```

API docs JSON:
```
http://localhost:8080/api-docs
```

---

## API Reference

### Authentication — `/auth`

| Method | Endpoint | Description | Auth required |
|--------|----------|-------------|---------------|
| `POST` | `/auth/signup` | Register a new user | No |
| `POST` | `/auth/login` | Login, returns JWT cookies | No |
| `POST` | `/auth/refresh` | Refresh access token | Bearer token |

### Users — `/users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/users` | Get all users |
| `GET` | `/users/{id}` | Get user by ID |
| `PUT` | `/users/{id}` | Update user |
| `DELETE` | `/users/{id}` | Delete user |

### Projects — `/projects`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| `GET` | `/projects` | List all projects | `ADMIN` |
| `GET` | `/projects/{id}` | Get project by ID | Any |
| `POST` | `/projects` | Create project | `MANAGER` |
| `PUT` | `/projects/{id}` | Update project | Any |
| `DELETE` | `/projects/{id}` | Delete project | Any |

### Tasks — `/tasks`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/tasks` | List all tasks |
| `GET` | `/tasks/{id}` | Get task by ID |
| `POST` | `/tasks` | Create task |
| `PUT` | `/tasks/{id}` | Update task |
| `DELETE` | `/tasks/{id}` | Delete task |

### Relations (Neo4j) — `/relations`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/relations/user/{id1}/collaborates/{id2}` | Add collaborator relationship |
| `POST` | `/relations/task/{id1}/depends/{id2}` | Add task dependency |
| `GET` | `/relations/projects/{projectId}/users` | Get users in a project |
| `GET` | `/relations/{projectId}/tasks` | Get tasks in a project |
| `POST` | `/relations/{projectId}/tasks/{taskId}` | Add task to project |
| `POST` | `/relations/{projectId}/add-user/{userId}` | Add user to project |

### Chat — `/chat`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/chat/projects/{id}/messages` | Get project chat history |
| `POST` | `/chat/projects/{id}/messages` | Send a message |

---

## Roles & Permissions

| Role | Permissions |
|------|-------------|
| `USER` | Read projects and tasks, participate in chat |
| `MANAGER` | Create and manage projects, assign users and tasks |
| `ADMIN` | Full access to all resources and user management |

---

## Database Design

### MongoDB Collections

| Collection | Description |
|------------|-------------|
| `users` | User accounts with hashed passwords and roles |
| `projects` | Project documents |
| `tasks` | Task documents with status and deadline |
| `roles` | Role definitions |
| `projectmessages` | Chat messages per project |
| `notifications` | User notifications |

### Neo4j Graph Model

```
(User)-[:MEMBER_OF]->(Project)
(User)-[:COLLABORATES_WITH]->(User)
(Project)-[:HAS_TASK]->(Task)
(Task)-[:DEPENDS_ON]->(Task)
```

When a project or task is created via the REST API, a corresponding node is automatically created in Neo4j and linked to the creator's user node — keeping both databases in sync.
