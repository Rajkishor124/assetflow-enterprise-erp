# ADR-008 — Validation Strategy

## Status

Accepted

## Decision

Validation occurs at three layers.

1. Frontend (Zod)
2. Backend (Bean Validation)
3. Business Rules (Service Layer)

## Rationale

Never trust client input.

Business validation belongs inside services.