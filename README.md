# Rate CS Teaching Staff

A full CRUD web application for rating computer science teaching staff
(professors, instructors, TAs, and general staff). Built with Spring Boot and
Thymeleaf for CMPT 276.

Each rating records a staff member's name, email, and role, three 1–10 scores
(clarity, niceness, knowledge), and an optional comment. The app computes an
overall score from the three sub-scores and presents role-specific display
titles through a small polymorphic profile model.

## Features

- **Create, read, update, delete** staff ratings through a server-rendered UI.
- **Server-side validation** (Jakarta Bean Validation): required fields, valid
  email, name length, score range (1–10), and comment length (≤ 500).
- **Unique email per staff member** — a case-insensitive check rejects a second
  rating for the same email, both in the form and at the database level.
- **Computed overall score** — the average of clarity, niceness, and knowledge,
  rounded to one decimal place.
- **Role-specific display titles** via a `StaffMemberProfile` hierarchy
  (`Prof. …`, `… (TA)`, `Instructor …`, `…, Staff`) built by a factory.
- **Sample data seeding** on first startup so the app is non-empty on a fresh
  database.

## Tech stack

- Java 21, Spring Boot 3.3
- Spring MVC + Thymeleaf (server-side rendering)
- Spring Data JPA / Hibernate
- Jakarta Bean Validation
- PostgreSQL (local + production), H2 in-memory (tests)
- Maven (via the bundled `mvnw` wrapper)
- Docker + Render for deployment

## Domain model and design

- `StaffRating` — the JPA entity. Holds the persisted fields and exposes three
  computed, non-persisted (`@Transient`) values: the overall score, the active
  profile, and the display title.
- `RoleType` — enum of `TA`, `PROF`, `INSTRUCTOR`, `STAFF`.
- `model.profile` — an abstract `StaffMemberProfile` with concrete subclasses
  per role, created by `StaffProfileFactory`. This is where the role-specific
  presentation (display title, badge) lives, keeping that logic out of the
  entity and the templates.
- `StaffRatingRepository` → `StaffRatingService` → `StaffRatingController` — a
  standard repository/service/controller split. The service owns the
  transactional boundary and the unique-email check; the controller handles
  binding, validation, and view selection.

## Running locally

Requires **Java 21** and a running **PostgreSQL** instance.

1. Create a database named `ratecsstaff` (the default connection uses
   `postgres` / `postgres` on `localhost:5432` — override with the environment
   variables below if yours differs).

2. Start the app:

   ```bash
   ./mvnw spring-boot:run
   ```

3. Open <http://localhost:8080>.

On first run with an empty database, a handful of sample ratings are inserted
automatically.

### Configuration

The datasource is driven by environment variables, with local defaults:

| Variable                     | Default                 |
|------------------------------|-------------------------|
| `DB_HOST`                    | `localhost`             |
| `DB_PORT`                    | `5432`                  |
| `DB_NAME`                    | `ratecsstaff`           |
| `SPRING_DATASOURCE_USERNAME` | `postgres`              |
| `SPRING_DATASOURCE_PASSWORD` | `postgres`              |
| `SPRING_DATASOURCE_URL`      | built from the above    |

## Testing

```bash
./mvnw test
```

The suite uses Spring slice tests (`@DataJpaTest`, `@WebMvcTest`) plus plain
JUnit 5 unit tests, and runs against in-memory H2 — no PostgreSQL required. See
[`TESTING.md`](TESTING.md) for the full strategy and coverage breakdown.

## Deployment

The app is containerized with the included `Dockerfile` and deployed to
**Render** using `render.yaml`, which provisions a managed PostgreSQL database
and wires its credentials into the web service via environment variables.

## Project structure

```
src/main/java/com/cmpt276/ratecsstaff/
├── RateCsStaffApplication.java   — Spring Boot entry point
├── config/DataSeeder.java        — seeds sample data on first run
├── controller/                   — StaffRatingController (CRUD routes)
├── service/                      — StaffRatingService (transactions, rules)
├── repository/                   — StaffRatingRepository (Spring Data JPA)
└── model/                        — StaffRating entity, RoleType, profile/ hierarchy

src/main/resources/
├── templates/                    — Thymeleaf views (index, detail, form, delete, layout)
├── static/css/                   — stylesheet
└── application.properties        — datasource + JPA config
```

## Use of AI

AI tools were used as an assistant during this assignment. Specifically, AI
helped with:

- **Documentation** — this README and `TESTING.md` were generated with AI
  assistance.
- **Front end** — the Thymeleaf templates and CSS.
- **Seeding the database** — the sample staff-rating data.
- **Design planning** — talking through the overall design and the connection
  between the back end and the front end.
- **Validation tests** — verifying the validation tests.
- **Debugging**.
- **Commit messages** — writing the Git commit messages.

All AI-assisted work was reviewed and verified before being included.
