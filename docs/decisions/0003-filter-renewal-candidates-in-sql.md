# 0003. Filter renewal candidates in SQL

Date: 2026-11-16 · Status: Accepted

## Context

The nightly reminder job loaded every active annual subscription and checked each
one with `Subscription.nextRenewalOn`. With 1.8 million subscriptions, it took
40 minutes and loaded the database at the start of the business day in Asia.

## Decision

`RenewalQueries.activeAnnualRenewingOn(date)` filters in SQL by month and day of
the start date (Feb 29 starts are included on Feb 28). The exact rule in
`Subscription.nextRenewalOn` stays the final check in `RenewalReminderService`.

## Consequences

- The job reads only the candidates for one day.
- The renewal rule now exists twice: exactly in Java, approximately in SQL.
  A property test (`RenewalCandidatesPropertiesTest`) checks that the SQL filter
  never drops a real renewal, and a component test checks that the reminders sent
  are the same as with a full scan.
- If billing periods ever change (for example, quarterly plans), both must change.
