# Testing

This document describes the test suite, the testing strategy, and how to run it.

## How to run

```bash
./mvnw test
```

All tests run against an in-memory **H2** database (for the persistence slice),
so no PostgreSQL instance is required to run the suite.

Current result: **25 tests, 0 failures.**

## Strategy: slice tests

This project uses **Spring slice tests** rather than full `@SpringBootTest`
integration tests. Each test loads only the layer it exercises, which keeps the
suite fast and the failures precise:

- `@DataJpaTest` — loads just the JPA layer (entities + repositories) against an
  embedded H2 database.
- `@WebMvcTest` — loads just the web layer (one controller + MVC infrastructure)
  with the service layer mocked via Mockito.
- Plain JUnit 5 unit tests — for Bean Validation constraints and the polymorphic
  design, which need no Spring context at all.

**Why H2 for the persistence slice instead of PostgreSQL?** Slice tests are meant
to be hermetic and runnable anywhere (including a grader's machine or CI) with no
external services to install or start. H2 in PostgreSQL-compatible defaults covers
the behaviour these tests assert — save/retrieve, delete, the unique constraint,
and a case-insensitive lookup. The production and local-run configuration uses
PostgreSQL (see `application.properties` and the README).

## What is covered

### 1. Validation tests — `StaffRatingValidationTest`

Pure Bean Validation tests driven by a `jakarta.validation.Validator`:

- a fully valid rating produces **no** violations,
- **invalid email** is rejected,
- **score above range** (11) and **below range** (0) are rejected,
- **missing name** (blank) is rejected,
- **all missing required fields** are rejected together,
- **over-length comment** (501 chars) is rejected.

### 2. Web / controller tests — `StaffRatingControllerTest` (`@WebMvcTest` + MockMvc)

- `GET /` returns **200** and includes the `ratings` model attribute,
- `GET /ratings/{id}` returns the detail view when found, and **redirects** when not,
- `GET /ratings/new` returns the create form,
- **POST create – success path**: valid input **redirects** to the new detail page
  and calls the service `save`,
- **POST create – failure path**: invalid input returns the **form view** with
  field errors and does **not** save,
- **POST create – duplicate email**: returns the form with an error on the `email`
  field and does not save,
- **POST update – success / missing-entity** paths,
- **POST delete**: removes the entry and redirects to the index.

### 3. Persistence tests — `StaffRatingRepositoryTest` (`@DataJpaTest`)

- saving **assigns an id and timestamps** and the entry can be **retrieved**,
- `findAll` returns saved entries,
- **delete removes** the entry,
- `findByEmailIgnoreCase` matches **regardless of case**,
- a **duplicate email violates the unique constraint** at flush time.

### 4. Design / polymorphism — `StaffProfilePolymorphismTest`

- the factory returns the correct `StaffMemberProfile` subclass per role,
- each role renders its own `displayTitle()` (`Prof. …`, `… (TA)`,
  `Instructor …`, `…, Staff`).

## Manual / end-to-end verification

Beyond the automated suite, the full CRUD flow was exercised through the browser
against a real PostgreSQL database: create → detail → edit (overall score
recomputed) → duplicate-email rejection → delete, with the data confirmed in the
`staff_rating` table.

## Use of AI

This document was written with AI assistance and reviewed before being included.
See the "Use of AI" section of the [README](README.md) for the full disclosure.
