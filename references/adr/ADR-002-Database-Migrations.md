# ADR-002 — Database Migration Strategy

## Status

Accepted

## Decision

Flyway is the single source of truth for database schema evolution.

Hibernate will never generate or modify the schema.

Configuration:

spring.jpa.hibernate.ddl-auto=validate

## Rationale

- Version controlled schema
- Repeatable deployments
- Easy rollback
- Production safe
- Industry standard

## Consequences

Every schema modification requires a new Flyway migration.