# Chapter 6 — Designing Boundaries: Domain, Data and APIs

SubFlow opens an API to partner companies and receives payment notifications
(webhooks) from PayFast. New since Chapter 5:

| Book section | Code |
|---|---|
| Entities, value objects, invariants | `domain.EmailAddress`; `Customer` normalizes its email |
| API contracts and errors | `api.ApiResponse`, `api.ApiError` (RFC 9457 shape), `api.ApiErrors` |
| Idempotent APIs and webhooks | `api.PartnerSubscriptionApi` + `IdempotencyStore`; `api.PaymentWebhookApi` + `ProcessedEvents` |
| Pagination | `service.InvoiceQueries`, `Page`, keyset SQL in `persistence.JdbcInvoiceQueries` |
| Compatibility | API records (`SubscriptionResponse`, `PriceQuoteResponse`) separate from the domain |
| Authorization | `service.Caller`; `CancellationService` checks ownership |
| Transactions and concurrency | `SubscriptionRepository.update(expected, updated)`, `TransactionRunner` |
| Events and the outbox | `domain.events`, `service.Outbox`, `OutboxRelay`, `RefundOnCancellation` |
| Schema evolution | `src/main/resources/db/migration/V1..V4` (expand and contract) |
| Caching | `service.CachedPriceQuotes` |

Problematic versions are in `examples.ch06.*.before`.

```
mvn -pl chapter-06-designing-boundaries test
```
