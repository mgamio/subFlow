# Chapter 1 — Core Design Principles

| Book section | Before | After |
|---|---|---|
| DRY | `examples.ch01.dry.before` | `com.subflow.domain.PricingPolicy`, `Plan`, `BillingCycle` |
| KISS | `examples.ch01.kiss.before` | `com.subflow.domain.Subscription#isActiveOn` |
| YAGNI | `examples.ch01.yagni.before` (the discount engine) | deleted; `com.subflow.domain.Money` |
| Loose coupling, high cohesion | `examples.ch01.coupling.before` | `com.subflow.service.InvoiceService` + `InvoiceRepository`, `InvoiceNotifier` |
| Separation of concerns | `examples.ch01.soc.before` | `com.subflow.web`, `com.subflow.service.CancellationService`, `com.subflow.persistence` |
| Clean code | `examples.ch01.cleancode.before` | `examples.ch01.cleancode.after` |

Run only this chapter's tests:

```
mvn -pl chapter-01-core-design-principles test
```
