# ADR-006 — Auditing Strategy

## Status

Accepted

## Decision

Spring Data JPA Auditing is enabled.

Every entity stores

createdAt

updatedAt

createdBy

updatedBy

Version

Activity Logs are stored separately.

## Rationale

ERP systems require complete traceability.

Historical data must never be overwritten.