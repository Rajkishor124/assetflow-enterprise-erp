# Live Demo Script

Rehearse this exact sequence at least twice before presenting. It exercises
every module and every core business rule in one continuous, coherent story.

1. **Admin login** — show role-based dashboard/greeting differs from a
   regular user's.
2. **Set up organization** — Admin creates department "IT", creates employee
   "Bob", assigns Bob to IT.
3. **Promote role** — Admin sets Bob's role to `ASSET_MANAGER`.
4. **Bob creates a category** — logs in as Bob, creates asset category
   "Laptops" (warranty 36 months).
5. **Bob registers an asset** — `AF-001`, category Laptops. Confirm it shows
   `AVAILABLE`.
6. **Bob allocates the asset** — to "Alice" (Dept Head of IT). Confirm success
   and asset status flips to `ALLOCATED`.
7. **Attempt double-allocation** — try allocating `AF-001` to another user.
   Show the 409 error with Alice's name and the transfer suggestion.
8. **Transfer workflow** — Bob submits a transfer request for `AF-001` to
   "Charlie". Admin or Bob approves it. Confirm Charlie now holds it, asset
   stayed `ALLOCATED` throughout, and the allocation history shows both
   records.
9. **Resource booking** — Alice books "Conf Room A" 10:00–11:00. Attempt an
   overlapping 10:30–11:30 booking → show the conflict error. Create a
   non-overlapping (adjacent) 11:00–12:00 booking → show it succeeds.
10. **Maintenance request** — Charlie reports an issue on `AF-001`. Bob
    approves it (asset → `UNDER_MAINTENANCE`), assigns a technician, marks it
    `RESOLVED` (asset → `AVAILABLE`). Show the maintenance history log.
11. **Audit cycle** — Admin starts audit "Q3 Check", adds `AF-001` with Alice
    as auditor. Alice submits result `MISSING`. Show the discrepancy in the
    audit report. Close the cycle; show it's now read-only.
12. **Dashboard** — show summary cards (counts by status) and the
    assets-by-department chart reflect the actions taken above.
13. **Notifications** — show the notification bell/dropdown contains entries
    for the transfer approval and maintenance resolution.
14. **Activity log** — Admin opens the activity log; show chronological
    entries for every action performed in steps 2–11.
15. **Close** — summarize: relational schema with real constraints, RBAC
    enforced at the service layer, concurrency-safe allocation and booking,
    full audit trail, stateless JWT auth — tie each back to a judging
    criterion.

## Pre-demo checklist

- [ ] Seed data reset to a known clean state before the run.
- [ ] All four demo user accounts (Admin, Bob, Alice, Charlie) exist with known
      credentials.
- [ ] Backend and frontend both running via `docker-compose up`, verified
      warm (no cold-start lag during the actual demo).
- [ ] Swagger UI open in a spare tab in case a judge asks to see the API
      contract directly.
