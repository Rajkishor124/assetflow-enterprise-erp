# ADR-009 — Booking Conflict Detection

## Status

Accepted

## Decision

Bookings overlap when

existing.start < new.end

AND

existing.end > new.start

Overlapping bookings are rejected.

## Rationale

Prevents double-booking of shared resources while allowing adjacent bookings.