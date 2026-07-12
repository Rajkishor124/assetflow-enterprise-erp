# ADR-001 — Entity ID Strategy

## Status

Accepted

## Context

Every entity in the ERP requires a primary key that is efficient for joins,
indexing, sorting, and future scalability.

## Decision

All entities will use:

Long

@GeneratedValue(strategy = GenerationType.IDENTITY)

as the primary key.

## Rationale

- Faster indexing than UUID.
- Better PostgreSQL performance.
- Smaller index size.
- Easier debugging.
- Sequential IDs simplify administration.
- Well suited for ERP applications.

## Consequences

- Numeric identifiers are exposed externally.
- UUIDs may be introduced later for public APIs if required.