# ADR-003 — REST API Versioning

## Status

Accepted

## Decision

Every endpoint begins with

/api/v1/

Example

/api/v1/assets

/api/v1/bookings

/api/v1/auth

## Rationale

Future API versions can coexist without breaking clients.