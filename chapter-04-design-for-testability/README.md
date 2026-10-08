# Chapter 4 — Design for Testability

What changed in SubFlow since Chapter 3:

- **Idempotent charges.** `PaymentCharger.charge` now takes a `ChargeRequest` with an
  idempotency key derived from the subscription and its billing period. Charging twice
  with the same key charges once.
- **Explicit unknown outcome.** A timeout is a `PaymentTimeoutException` ("we don't know"),
  not a `PaymentDeclinedException` ("no").
- **Retries in one place.** `RetryingPaymentCharger` decorates any `PaymentCharger`, with a
  `RetryPolicy` and an injected `Sleeper`, so tests never wait.
- **Billing periods as pure functions.** `Subscription.periodStartOn(day)` and
  `nextRenewalOn(day)`, checked by property-based tests.
- **Renewal reminders.** `RenewalReminderService` replaces a legacy job with static
  dependencies, refactored through seams under characterization tests.

The simulated `com.payfast.sdk` now supports idempotency keys and timeouts.

| Book section | Before | After / tests |
|---|---|---|
| Testing failure cases | `examples.ch04.failures.before` | `RetryingPaymentCharger`, `DoubleChargeTest` |
| Deterministic code | `examples.ch04.determinism.before` | `CheckoutService`, `Subscription#periodStartOn` |
| Test behavior, not implementation | `examples.ch04.doubles` (tests) | `CheckoutServiceTest`, `com.subflow.testing` |
| Contract tests | — | `PaymentChargerContract` and its four subclasses |
| Property-based testing | `examples.ch04.properties.before` | `SubscriptionPropertiesTest`, `PricingPropertiesTest` |
| Testing legacy code | `examples.ch04.legacy.before`, `.seam` | `RenewalReminderService`, `examples.ch04.legacy` tests |

```
mvn -pl chapter-04-design-for-testability test
```
