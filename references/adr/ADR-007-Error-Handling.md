# ADR-007 — Error Handling

## Status

Accepted

## Decision

GlobalExceptionHandler handles every exception.

Every response follows

{
  "success": false,
  "message": "...",
  "data": null,
  "timestamp": "..."
}

## Rationale

Consistent API responses improve frontend integration.