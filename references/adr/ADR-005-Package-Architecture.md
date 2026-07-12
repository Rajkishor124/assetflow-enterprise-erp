# ADR-005 — Package Architecture

## Status

Accepted

## Decision

Feature-first architecture.

Example

assets/

organization/

booking/

maintenance/

Each feature owns

controller

service

repository

entity

dto

mapper

validation

## Rationale

- High cohesion
- Low coupling
- Easy maintenance
- Better scalability