# ADR-004 — Authentication Strategy

## Status

Accepted

## Decision

Authentication uses:

- Spring Security
- JWT Access Token
- BCrypt Password Encoding
- Stateless Sessions

Authorization uses Role-Based Access Control (RBAC).

## Rationale

- Stateless
- Scalable
- Enterprise standard
- Easy frontend integration