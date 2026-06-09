A full-stack articles publishing platform. Writers publish, readers discover, admins moderate.

> **إثراء** (Ithra) is Arabic for *enrichment* — the platform's purpose is to enrich readers
> with thoughtful, well-categorized content from a community of authors.

<img width="1919" height="2777" alt="3" src="https://github.com/user-attachments/assets/7c10b82e-1c11-46ae-a688-af6b629f9172" />
<img width="1919" height="1582" alt="2" src="https://github.com/user-attachments/assets/6801f5d6-1332-436f-9dfa-2b799c2e18f6" />
<img width="1919" height="2202" alt="1" src="https://github.com/user-attachments/assets/06b51323-6d15-4a61-aac0-0e4ed820dba4" />
<img width="1919" height="1072" alt="4" src="https://github.com/user-attachments/assets/77e0a9a3-1e38-4f1d-9f75-43a877ff8591" />
<img width="1919" height="858" alt="5" src="https://github.com/user-attachments/assets/5aff9da1-7377-4cdb-aa89-afb1069ccf46" />
<img width="1919" height="858" alt="6" src="https://github.com/user-attachments/assets/174ae0c3-e93a-49c1-86a0-2dc4fccdb2e7" />

## Overview

Ithra is a production-style content platform with:

- **Role-based access** — ADMIN, AUTHOR, READER, each with scoped capabilities
- **Email-verified authentication** — JWT access + refresh tokens, password reset flow,
  token-version invalidation on credential change
- **Article lifecycle** — drafts, publishing, soft hide, slug-based URLs, cover images,
  categories and tags
- **Engagement** — likes (toggle), view counting with cookie-based deduplication,
  threaded comments (verified users only)
- **Admin dashboard** — overview totals, articles & views over time, top articles,
  category breakdown
- **Soft deletes everywhere** — no record is ever physically removed
- **Image uploads** — multipart, type-checked, size-limited, served as static resources

## Stack

| Layer    | Technology                                                  |
|----------|-------------------------------------------------------------|
| Backend  | Spring Boot 4.1, Java 25, Spring Security, JPA, MySQL, JWT  |
| Frontend | Angular (standalone components), TypeScript, RxJS           |
| Build    | Maven (backend), npm (frontend)                             |
| Docs     | Swagger / OpenAPI 3 (auto-generated)                        |
| Email    | Spring Mail + Thymeleaf templates                           |

## Project Layout

articles-website/
├── backend/ithra/ -> Spring Boot REST API
└── frontend/ithra-frontend -> Angular SPA

## Quick Start

```bash
# Backend
cd backend/ithra
./mvnw spring-boot:run        # → http://localhost:8080

# Frontend
cd frontend/ithra-frontend
npm install
npm start                     # → http://localhost:4200

Swagger UI: http://localhost:8080/swagger-ui.html

See backend/ithra/README.md and
frontend/ithra-frontend/README.md for full setup details.

License
MIT


```markdown
# Ithra — Backend API

REST API for the Ithra articles platform. Built on Spring Boot 4.1 and Java 25.

## What's Inside

- **Auth** — register, login, refresh, email verification, password reset.
  JWT with `tokenVersion` claim → resetting a password invalidates every existing token.
- **Articles** — create / update / publish / hide / soft-delete, slug auto-generation,
  cookie-based view dedup, like toggle with denormalized count, owner-or-admin authorization.
- **Categories & Tags** — public read, admin write, many-to-many with articles.
- **Comments** — only verified users may post, owner-or-admin may delete.
- **Admin Stats** — overview totals, monthly trends (last 12 months), top 10 articles,
  category counts. Powered by aggregating JPQL queries.
- **Image Upload** — multipart, jpg/jpeg/png/webp, 5 MB cap, UUID-named files,
  served from `/uploads/**`.

## Architecture
controller → service → repository → MySQL
↓
mapper (MapStruct) ←→ DTOs (records / Lombok classes)


- `@RestControllerAdvice` for global exception handling
- `OncePerRequestFilter` JWT filter validates token + tokenVersion on every request
- `@PreAuthorize` for fine-grained method security
- Springdoc OpenAPI for live API documentation

## Run

Requires Java 25, Maven, MySQL 8+.

```bash
./mvnw spring-boot:run

## 3. frontend/README.md (new file)

```markdown
# Ithra — Frontend

Angular single-page application for the Ithra articles platform.

## Features

- **Public reader experience** — browse published articles, filter by category,
  search by title, read by slug
- **Author dashboard** — create, edit, publish, and manage your own articles
  with a rich content editor and cover image upload
- **Admin panel** — manage users, roles, categories, tags; view platform statistics
- **Authentication flows** — register, verify email, login, refresh tokens silently,
  request password reset, set new password
- **Engagement** — like articles, post comments (verified users only)
- **Reactive state** — RxJS observables, route-guarded protected areas,
  HTTP interceptor for bearer token + refresh

## Stack

- Angular (standalone components, signals)
- TypeScript
- RxJS
- Angular Router + Forms
- HttpClient with auth interceptor
- Talks to the Spring Boot API at `http://localhost:8080`

## Run

```bash
npm install
npm start

Open http://localhost:4200.

Configuration
The API base URL is set in src/environments/environment.ts:

export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
};


#The passowrd for dummy users is: 
Nm#fErF9L$
